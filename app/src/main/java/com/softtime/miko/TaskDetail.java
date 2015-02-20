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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.softtime.miko.Adapter.PicAdapter;
import com.softtime.miko.BmobData.pic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class TaskDetail extends ActionBarActivity {
    TextView textViewdetailTitle;
    TextView textViewdetailDesc;
    ImageView ivTakePic ;//拍照按钮
    ImageView ivSelectPic;//选一张照片按钮
    ImageView ivTaskBg ;//总任务的背景图
    Intent comeIntent;
    ListView lvTopicDetail;
    private File currentImageFile = null;//照片暂存File
    private static final int REQUEST_CODE_TAKE_PICTURE = 1;//动作代码，拍照动作
    private  static final int CROP = 2;//裁剪代码动作
    private  static  final  int SELECT_AND_CROP= 3;//选择图片
    private  static  final  int PREUPLOAD= 4;//准备上传
    String topicObjectId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        //找到各种组件
        getComponent();
        //获取刚刚传过来的Intnet
        comeIntent = getIntent();
        String taskTitle = comeIntent.getStringExtra("title");//获取传来的任务名称
        String taskDesc = comeIntent.getStringExtra("desc");//获取传来的任务介绍
        topicObjectId = comeIntent.getStringExtra("objectId");
        textViewdetailTitle.setText(taskTitle);
        textViewdetailDesc.setText(taskDesc);//让这两个TextView显示
        //设置任务的背景图片显示
        setTopicBackgroundImgDisplay();
        //读取这个topic下的图片，先找数据（上传者的名字，图片地址，图片描述）
        loadPicture();




                //拍照按钮的单击事件
                ivTakePic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        File dir = new File(Environment.getExternalStorageDirectory(), "pictures/softtime");//存储路径
                        //路径不存在的话则建立
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        currentImageFile = new File(dir, System.currentTimeMillis() + ".jpg");//路径+名称 = 存储文件具体位置
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

        //选图事件
        ivSelectPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File dir = new File(Environment.getExternalStorageDirectory(), "pictures/softtime");//存储路径
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

                Intent intentGet = new Intent("android.intent.action.GET_CONTENT");   //选内容
                intentGet.setType("image/*");
                intentGet.putExtra("crop",true);
                intentGet.putExtra("scale",true);
                intentGet.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentImageFile));//把文件转换成Uri格式然后放到Extra里，相机应用会提取这个的，拍完就保存到这里
                startActivityForResult(intentGet,SELECT_AND_CROP);
            }
        });



    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_CANCELED){
            //什么也不做
        }else {
            switch (requestCode) {
                case REQUEST_CODE_TAKE_PICTURE://如果动作是拍照

                    Intent intentCrop = new Intent("com.android.camera.action.CROP");   //准备裁剪
                    intentCrop.setDataAndType(Uri.fromFile(currentImageFile), "image/*");//裁剪程序会读取这个，表示裁剪哪个文件
                    //下面这个是设置在开启的Intent中设置显示的VIEW可缩放
                    intentCrop.putExtra("scale", true);
                    intentCrop.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentImageFile));//裁剪后保存到哪里，这也是裁剪程序自己搞定的
                    startActivityForResult(intentCrop, CROP);

                    break;

                case CROP:
                    if (resultCode == RESULT_OK) {


                        Intent intentToSubmit = new Intent(TaskDetail.this, EditPhotoInfo.class);//准备打开编辑发布页面
                        //传入刚才拍摄照片的Uri，好像这里的Uri就是自动是已经被裁减过的了，这都是自动的貌似
                        intentToSubmit.setData(Uri.fromFile(currentImageFile));
                        //需要传入一下String啊，怎么办呢。这边传了那边收不到,用第三个类吧。。。
                        Config.setTemp(currentImageFile.getAbsolutePath());
                        intentToSubmit.putExtra("topicObjectId",topicObjectId);
                        startActivity(intentToSubmit);
                    }

                case SELECT_AND_CROP:

                    Intent intentSelectCrop = new Intent("com.android.camera.action.CROP");   //准备裁剪
                    intentSelectCrop.setDataAndType(data.getData(), "image/*");
                    intentSelectCrop.putExtra("scale", true);
                    intentSelectCrop.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentImageFile));//裁剪后保存到哪里，这也是裁剪程序自己搞定的
                    startActivityForResult(intentSelectCrop, PREUPLOAD);

                    break;

                case PREUPLOAD:
                    Intent intentToPreSubmit = new Intent(TaskDetail.this, EditPhotoInfo.class);//准备打开编辑发布页面
                    intentToPreSubmit.setData(Uri.fromFile(currentImageFile));
                    Config.setTemp(currentImageFile.getAbsolutePath());
                    intentToPreSubmit.putExtra("topicObjectId",topicObjectId);
                    startActivity(intentToPreSubmit);
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getComponent(){
        ivTakePic = (ImageView) findViewById(R.id.iv_task_detail_take_pic);
        ivSelectPic = (ImageView) findViewById(R.id.iv_task_detail_select_pic);
        textViewdetailTitle = (TextView) findViewById(R.id.textView_task_detail_title);
        textViewdetailDesc = (TextView) findViewById(R.id.textView_task_detail_desc);
        ivTaskBg = (ImageView) findViewById(R.id.iv_task_detail_background);
        lvTopicDetail = (ListView) findViewById(R.id.listView_task_detail);
    }

    private  void setTopicBackgroundImgDisplay(){
        ImageLoaderConfiguration ilConfinguration = ImageLoaderConfiguration.createDefault(this);//创建默认配置文件
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();//设置配置选项
        ImageLoader.getInstance().init(ilConfinguration);//绑定配置文件
        ImageLoader.getInstance().loadImage(comeIntent.getStringExtra("background"), options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                ivTaskBg.setImageBitmap(bitmap);

            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
    }

    private  void loadPicture(){
        BmobQuery<pic> topicPic = new BmobQuery<pic>();
        topicPic.addWhereEqualTo("topicObjectId",topicObjectId);
        topicPic.findObjects(this,new FindListener<pic>() {
            @Override
            public void onSuccess(List<pic> pics) {
                PicAdapter picAdapter = new PicAdapter(TaskDetail.this,R.layout.listview_item_main_task,pics);
                lvTopicDetail.setAdapter(picAdapter);
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(TaskDetail.this, "读取数据失败" + s, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
