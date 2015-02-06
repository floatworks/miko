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


public class RegActivity extends Activity {

    EditText editTextEmail;
    EditText editTextPassword;
    Button   btnReg;
    User userReg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        Bmob.initialize(this, "3c18f2577233b3d021465b2885790e29");
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        btnReg = (Button) findViewById(R.id.buttonReg);
        userReg= new User();


        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userReg.setEmail(editTextEmail.getText().toString());
                userReg.setUsername(editTextEmail.getText().toString());

                userReg.setInstallationId(BmobInstallation.getInstallationId(RegActivity.this));
                userReg.setPassword(editTextPassword.getText().toString());
 


                userReg.signUp(RegActivity.this,new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Intent i = new Intent(RegActivity.this,MainActivity.class);
                        startActivity(i);
                        finish();


                    }
                    
                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(RegActivity.this,"注册失败"+s,Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    private  void initForm(){

    }

}
