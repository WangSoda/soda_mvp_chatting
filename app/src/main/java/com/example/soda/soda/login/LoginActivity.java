package com.example.soda.soda.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.soda.soda.R;
import com.example.soda.soda.R2;
import com.example.soda.soda.data.remote.StatusResponse;
import com.example.soda.soda.main.MainActivity;
import com.example.soda.soda.welcome.WelcomeActivity;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    @BindView(R2.id.user_id_edit_text_login)
    EditText userIdEditTextLogin;
    @BindView(R2.id.user_pwd_edit_text_login)
    EditText userPwdEditTextLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }
    @OnClick(R2.id.login_btn_login)
    void Login(View view){
        Toast.makeText(this,"Login",Toast.LENGTH_SHORT).show();

        StatusResponse response = new StatusResponse();
        response.userLogin(userIdEditTextLogin.getText().toString().trim(), userPwdEditTextLogin.getText().toString().trim(),
                new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        {

                            int selfPermission = ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            int permissionId = PackageManager.PERMISSION_GRANTED;
                            int requestCode = 1;
                            if (selfPermission != permissionId){
                                ActivityCompat.requestPermissions(LoginActivity.this,
                                        new String[]{
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                                        },requestCode);
                            } else {
                                //
                                EMClient.getInstance().chatManager().loadAllConversations();
                                EMClient.getInstance().groupManager().loadAllGroups();
                            }

                        }

                        MainActivity.startActivity(LoginActivity.this);

                    }

                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
    }
    @OnClick(R2.id.regester_btn_login)
    void Register(View view){


    }

    public static void startActivity(Context context) {

        Intent intent = new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }


}
