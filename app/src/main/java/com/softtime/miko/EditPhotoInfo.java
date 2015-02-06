package com.softtime.miko;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.LocalThumbnailListener;

import java.io.File;


public class EditPhotoInfo extends ActionBarActivity {

    ImageView prePic ;//预览处
    EditText  editPhotoInfo;//文字编辑框
    Button    submit;//发布按钮
    File comePhoto;//刚才传来的文件
    String comeFilePath;//刚才传来的路径
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo_info);
        Intent comeIntent = getIntent();//接受Intent
        Uri filePath = comeIntent.getData();//获取刚才拍的图片Uri
        //不知道为什么，就是收不到上个页面传来的String,用第三方类看看能解决不……成功了……这样也行。。我擦
        comeFilePath = Config.getTemp();
        //将传来的路径转成File吧，这样的话Uri，path，File都有了，为所欲为吧
        comePhoto = new File(comeFilePath);
        System.out.println(comeFilePath);
       //实例化控件
       prePic = (ImageView) findViewById(R.id.iv_editPhotoinfo_previewPic);
       //设置缩略图为预览
        BmobProFile bmobProFile = new BmobProFile();
        bmobProFile.getInstance(this).getLocalThumbnail(comeFilePath,1,new LocalThumbnailListener() {
            @Override
            public void onSuccess(String s) {
                File tempFile = new File(s);
                prePic.setImageURI(Uri.fromFile(tempFile));
                Toast.makeText(EditPhotoInfo.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(EditPhotoInfo.this,"MainActivity -localThumbnail-->生成缩略图失败 :"+i+","+s,Toast.LENGTH_LONG).show();
            }
        });




    }



}
