package com.softtime.miko;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.softtime.miko.BmobData.User;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


public class LoginActivity extends Activity {

    EditText loginEmail;
    EditText loginPassword;
    Button   loginBtn;
    User user;
    User gettedUserInfo;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bmob.initialize(this,Config.applicationId);
        loginEmail = (EditText) findViewById(R.id.etLoginEmail);
        loginPassword = (EditText)findViewById(R.id.etLoginPassword);
        loginBtn = (Button) findViewById(R.id.btnLoginLogin);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = loginEmail.getText().toString();
                final String password = loginPassword.getText().toString();
                user = new User();
                user.setUsername(loginEmail.getText().toString());
                user.setPassword(password);
                user.login(LoginActivity.this,new SaveListener() {
                    @Override
                    public void onSuccess() {
                        //获取登录设备的标识
                        String installationId = BmobInstallation.getInstallationId(LoginActivity.this);
                        //将标识更新到User表
                        user.setInstallationId(installationId);
                        user.update(LoginActivity.this,user.getObjectId(),new UpdateListener() {
                            @Override
                            public void onSuccess() {
                                Intent i =new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(i);
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                Toast.makeText(LoginActivity.this,"有错误出现~："+s,Toast.LENGTH_LONG).show();
                            }
                        });

                        finish();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(LoginActivity.this,"noO!~"+s,Toast.LENGTH_LONG).show();

                    }
                });
            }
        });


       // Intent i =new Intent(this,MainActivity.class);
       // startActivity(i);
    }



}
