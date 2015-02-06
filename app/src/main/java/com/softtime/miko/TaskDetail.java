package com.softtime.miko;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TaskDetail extends ActionBarActivity {
    TextView textViewdetailTitle;
    TextView textViewdetailDesc;
    Button btnTakePic ;//拍照按钮
    Button btnSelectPic;//选一张照片按钮
    ImageView iv ;//图片预览处
    private File currentImageFile = null;//照片暂存File
    private static final int REQUEST_CODE_TAKE_PICTURE = 1;//动作代码，拍照动作
    private  static final int CROP = 2;//裁剪代码动作
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        //找到各种组件
        btnTakePic = (Button) findViewById(R.id.btn_task_detail_take_pic);
        textViewdetailTitle = (TextView) findViewById(R.id.textView_task_detail_title);
        textViewdetailDesc = (TextView) findViewById(R.id.textView_task_detail_desc);
        iv = (ImageView) findViewById(R.id.imageView_preview_pic);
        //获取刚刚传过来的Intnet
        Intent comeIntent = getIntent();
        String taskTitle = comeIntent.getStringExtra("title");//获取传来的任务名称
        String taskDesc = comeIntent.getStringExtra("desc");//获取传来的任务介绍
        textViewdetailTitle.setText(taskTitle);
        textViewdetailDesc.setText(taskDesc);//让这两个TextView显示
        //拍照按钮的单击事件
        btnTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File dir = new File(Environment.getExternalStorageDirectory(), "pictures/softtime");//存储路径
                //路径不存在的话则建立
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                currentImageFile = new File(dir, System.currentTimeMillis()+".jpg");//路径+名称 = 存储文件具体位置
                //文件不存在的话则新建
                if (!currentImageFile.exists()) {
                    try {
                        currentImageFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //以上只是新建了一个文件
                //准备打开相机
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentImageFile));//把文件转换成Uri格式然后放到Extra里，相机应用会提取这个的，拍完就保存到这里
                startActivityForResult(i, REQUEST_CODE_TAKE_PICTURE);//开始执行动作码
            }




        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //分辨动作码
        switch (requestCode) {
            case REQUEST_CODE_TAKE_PICTURE://如果动作是拍照

                Intent intentCrop = new Intent("com.android.camera.action.CROP");   //准备裁剪
                intentCrop.setDataAndType(Uri.fromFile(currentImageFile),"image/*");//裁剪程序会读取这个，表示裁剪哪个文件
                //下面这个是设置在开启的Intent中设置显示的VIEW可缩放
                intentCrop.putExtra("scale",true);
                intentCrop.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(currentImageFile));//裁剪后保存到哪里，这也是裁剪程序自己搞定的
                startActivityForResult(intentCrop,CROP);

                break;

            case CROP:
                if(resultCode ==RESULT_OK) {

                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(currentImageFile)));//建立一张图——我该补一下这个知识了
                        System.out.println(bitmap+"ddd");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    //感觉上面这一段好像没什么用
                    Intent intentToSubmit = new Intent(TaskDetail.this, EditPhotoInfo.class);//准备打开编辑发布页面
                    //传入刚才拍摄照片的Uri，好像这里的Uri就是自动是已经被裁减过的了，这都是自动的貌似
                    intentToSubmit.setData(Uri.fromFile(currentImageFile));
                    //需要传入一下String啊，怎么办呢。这边传了那边收不到,用第三个类吧。。。
                    Config.setTemp(currentImageFile.getAbsolutePath());

                    startActivity(intentToSubmit);
                }
            default:
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
