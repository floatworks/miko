package com.softtime.miko;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.bmob.btp.file.BTPFileResponse;
import com.softtime.miko.BmobData.User;

import java.io.File;
import java.io.IOException;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;


public class EditProfile extends ActionBarActivity {
    File currentImageFile;//就是新创建的那个文件。
    Button btnUploadAvatar;
    Uri imageUri;
    String upPhotoUrl;
    String upPhotoFileName;
    ImageView ivAvatar;
    User gettedUserInfo;
    User finalCurrentUser;
    private static final int GET = 1;//选照片动作
    private static final int CROP = 2;//裁剪代码动作
    private static final int UPLOAD = 3;//上传代码动作


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Bmob.initialize(this, Config.applicationId);
        gettedUserInfo = BmobUser.getCurrentUser(this, User.class);

        btnUploadAvatar = (Button) findViewById(R.id.btn_edit_profile_upload_avatar);
        ivAvatar = (ImageView) findViewById(R.id.iv_edit_profile_avatar);//更改个人信息的页面里面的头像预览


        //从网上读取头像



        //绑定事件监听器
        btnUploadAvatar.setOnClickListener(new View.OnClickListener() {
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
                intentGet.putExtra("crop",true);
                intentGet.putExtra("scale",true);
                intentGet.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentImageFile));//把文件转换成Uri格式然后放到Extra里，相机应用会提取这个的，拍完就保存到这里
                startActivityForResult(intentGet,CROP);

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED){
            //这里什么也不做
        }else{
            switch (requestCode){
                case CROP:

                    Intent intentCrop = new Intent("com.android.camera.action.CROP");   //准备裁剪
                    intentCrop.setDataAndType(data.getData(),"image/*");
                    intentCrop.putExtra("scale",true);
                    intentCrop.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(currentImageFile));//裁剪后保存到哪里，这也是裁剪程序自己搞定的
                    startActivityForResult(intentCrop,UPLOAD);

                    break;

                case UPLOAD:

                    BTPFileResponse response = BmobProFile.getInstance(this).upload(currentImageFile.toString(),new UploadListener() {
                        @Override
                        public void onSuccess(String filename, String url) {
                            ivAvatar.setImageURI(Uri.fromFile(currentImageFile));
                            updateUserinfo(filename, url);//成功的话就该把返回值神马的写进数据库了。
                        }

                        @Override
                        public void onProgress(int i) {
                            Toast.makeText(EditProfile.this,"上传进度："+i,Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(int i, String s) {
                            Toast.makeText(EditProfile.this,"错误："+s,Toast.LENGTH_SHORT).show();

                        }
                    });
                    break;

            }
        }



    }

    private void updateUserinfo(String filename , String fileUrl){
         //把上传信息保存到数据库
        User user = new User();
        user.setAvatarFileName(filename);
        user.setAvatarUrl(fileUrl);
        String ObjectId = gettedUserInfo.getObjectId();
        user.update(EditProfile.this,ObjectId,new UpdateListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(EditProfile.this,"上传成功啦",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });


    }


}
