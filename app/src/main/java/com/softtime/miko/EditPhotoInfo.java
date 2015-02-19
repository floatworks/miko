package com.softtime.miko;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.LocalThumbnailListener;
import com.bmob.btp.callback.UploadListener;
import com.bmob.btp.file.BTPFileResponse;
import com.softtime.miko.BmobData.User;
import com.softtime.miko.BmobData.pic;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;


public class EditPhotoInfo extends ActionBarActivity {

    ImageView prePic ;//预览处
    EditText  editPhotoInfo;//文字编辑框
    Button    submit;//发布按钮
    File comePhoto;//刚才传来的文件
    String comeFilePath;//刚才传来的路径
    User loginedUser;
    String topicObjectId;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo_info);
        TextView tvTopBar = (TextView) findViewById(R.id.tv_titlebar);
        final EditText etPicDesc = (EditText)findViewById(R.id.et_editPhotoinfo_inputText);
        tvTopBar.setText("编辑信息");
        loginedUser = BmobUser.getCurrentUser(this,User.class);
        submit = (Button) findViewById(R.id.btn_editPhotoinfo_submit);
        final Intent comeIntent = getIntent();//接受Intent
        Uri filePath = comeIntent.getData();//获取刚才拍的图片Uri
        topicObjectId = comeIntent.getStringExtra("topicObjectId");//获得改Topic的ID
        comeFilePath = Config.getTemp();//不知道为什么，就是收不到上个页面传来的String,用外部类看看能解决不……成功了……这样也行。。我擦
        comePhoto = new File(comeFilePath);//将传来的路径转成File吧，这样的话Uri，path，File都有了，为所欲为吧
        System.out.println(comeFilePath);
        prePic = (ImageView) findViewById(R.id.iv_editPhotoinfo_previewPic);//实例化控件
        BmobProFile bmobProFile = new BmobProFile();
        bmobProFile.getInstance(this).getLocalThumbnail(comeFilePath,1,new LocalThumbnailListener() {
            @Override
            public void onSuccess(String s) {
                File tempFile = new File(s);
                prePic.setImageURI(Uri.fromFile(tempFile));
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(EditPhotoInfo.this,"MainActivity -localThumbnail-->生成缩略图失败 :"+i+","+s,Toast.LENGTH_LONG).show();
            }
        });//设置缩略图为预览


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //需要照片描述，登录用户ObjectId,话题ObjectId，picUrl,picFilename
                //先上传图片
                BTPFileResponse uploadResponse = BmobProFile.getInstance(EditPhotoInfo.this).upload(comeFilePath,new UploadListener() {
                    @Override
                    public void onSuccess(String s, String s2) {
                        String uploadedFilename = s;
                        String uploadedUrl = s2;
                        String userObjectId = loginedUser.getObjectId();
                        String topicObjectId = comeIntent.getStringExtra("topicObjectId");
                        String picDesc = etPicDesc.getText().toString();
                        //上传到pic表
                        pic uploadPic = new pic();
                        uploadPic.setPicFilename(uploadedFilename);
                        uploadPic.setPicUrl(uploadedUrl);
                        uploadPic.setUser(userObjectId);
                        uploadPic.setTopicObjectId(topicObjectId);
                        uploadPic.setPicDesc(picDesc);
                        uploadPic.save(EditPhotoInfo.this,new SaveListener() {
                            @Override
                            public void onSuccess() {
                            Toast.makeText(EditPhotoInfo.this,"上传成功！",Toast.LENGTH_LONG).show();
                            finish();
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                Toast.makeText(EditPhotoInfo.this,"错误："+s,Toast.LENGTH_LONG).show();

                            }
                        });
                    }

                    @Override
                    public void onProgress(int i) {
                        Toast.makeText(EditPhotoInfo.this,"图片上传中"+i+"%",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(EditPhotoInfo.this,"图片上传错误"+s,Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }



}
