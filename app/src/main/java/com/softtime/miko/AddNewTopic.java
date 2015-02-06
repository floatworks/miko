package com.softtime.miko;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.bmob.btp.file.BTPFileResponse;
import com.softtime.miko.BmobData.User;
import com.softtime.miko.BmobData.myImg;
import com.softtime.miko.BmobData.task;

import java.io.File;
import java.io.IOException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;


public class AddNewTopic extends ActionBarActivity {
    Button btnSubmit;
    Button btnSetPic;
    File currentImageFile;//就是新创建的那个文件。
    User logginedUser;
    ImageView ivTopicBackground;
    EditText etAddTopicTitle;
    EditText etAddTopicdesc;
    private static final int CROP = 2;//裁剪代码动作
    private static final int UPLOAD = 3;//上传代码动作
    private static final int PREUPLOAD = 4;//上传之前，只是展示
    String uploadedFileName;//上传成功后返回的文件名
    String uploadedUrl;//上传成功后返回的Url

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_topic);
        logginedUser = BmobUser.getCurrentUser(AddNewTopic.this,User.class);
        ivTopicBackground = (ImageView) findViewById(R.id.iv_add_new_topic_topicbackground);
        etAddTopicTitle = (EditText) findViewById(R.id.et_add_new_topic_title);
        etAddTopicdesc = (EditText) findViewById(R.id.et_add_new_topic_intro);
        btnSubmit = (Button) findViewById(R.id.btn_add_new_topic_submit);
        btnSetPic = (Button) findViewById(R.id.btn_add_new_topic_setpic);
        btnSetPic.setOnClickListener(new View.OnClickListener() {
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

                Intent intentGet = new Intent("android.intent.action.GET_CONTENT");   //准备裁剪
                intentGet.setType("image/*");
                intentGet.putExtra("crop", true);
                intentGet.putExtra("scale",true);
                intentGet.putExtra("aspectX", 2);
                intentGet.putExtra("aspectY", 2);
                intentGet.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentImageFile));//把文件转换成Uri格式然后放到Extra里，相机应用会提取这个的，拍完就保存到这里
                startActivityForResult(intentGet,CROP);
            }
        });//《--------setOnclickLinstener结束
        //===================================================================================================================================
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(currentImageFile.toString());//点击就得分开上传
                //先上传文件
                BTPFileResponse response = BmobProFile.getInstance(AddNewTopic.this).upload(currentImageFile.toString(),new UploadListener() {
                    @Override
                    public void onSuccess(String filename, String url) {
                        uploadedFileName = filename;
                        uploadedUrl = url;
                        myImg myImg = new myImg();
                        myImg.setCommentNum(0);
                        myImg.setImgFileName(filename);
                        myImg.setImgUrl(url);
                        myImg.setUserEmail(logginedUser.getEmail());
                        myImg.setUserObjectId(logginedUser.getObjectId());
                        myImg.save(AddNewTopic.this);
                        //发布任务
                        task task = new task();
                        task.setTaskname(etAddTopicTitle.getText().toString());
                        task.setDesc(etAddTopicdesc.getText().toString());
                        task.setCreateUserEmail(logginedUser.getEmail());
                        task.setCreateUserObjectId(logginedUser.getObjectId());
                        task.setBackgroundFilename(filename);
                        task.setBackgroundUrl(url);
                        task.save(AddNewTopic.this,new SaveListener() {
                            @Override
                            public void onSuccess() {
                                finish();
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                Toast.makeText(AddNewTopic.this,s,Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onProgress(int i) {
                        Toast.makeText(AddNewTopic.this,"正在上传："+i+"%",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(AddNewTopic.this,s,Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });

    }
//《--------OnCreate结束================================================================================================================================================================
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_CANCELED){
            //这里什么也不做
        }else {
            switch (requestCode){
                case CROP:

                    Intent intentCrop = new Intent("com.android.camera.action.CROP");   //准备裁剪
                    intentCrop.setDataAndType(data.getData(),"image/*");
                    intentCrop.putExtra("scale",true);
                    intentCrop.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(currentImageFile));//裁剪后保存到哪里，这也是裁剪程序自己搞定的
                    startActivityForResult(intentCrop,PREUPLOAD);

                    break;

                case PREUPLOAD:
                    System.out.println(currentImageFile.toString());//FILE文件toString就是显示文件的路径
                    ivTopicBackground.setImageURI(Uri.fromFile(currentImageFile));
                    break;

            }
        }
    }



}
