package com.example.soda.soda.register;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.soda.soda.R;
import com.example.soda.soda.R2;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {
    @BindView(R2.id.user_id_edit_text_register)
    EditText userIdEditTextRegister;
    @BindView(R2.id.user_pwd_edit_text_register)
    EditText userPwdEditTextRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }
    @OnClick(R2.id.regester_btn_register)
    void Register(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount
                            (userIdEditTextRegister.getText().toString().trim(),userPwdEditTextRegister.getText().toString().trim());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();

    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context,RegisterActivity.class);
        context.startActivity(intent);
    }
}
