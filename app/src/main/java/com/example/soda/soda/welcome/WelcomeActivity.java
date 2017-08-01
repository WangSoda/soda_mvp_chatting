package com.example.soda.soda.welcome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.soda.soda.R;
import com.example.soda.soda.R2;
import com.example.soda.soda.chatting.ChattingActivity;
import com.example.soda.soda.login.LoginActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 欢迎界面，用于判断界面的跳转和
 */
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);


    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @OnClick(R2.id.main_btn_welcome)
    void startActivity(View view) {

//        MainActivity.startActivity(this);
        ChattingActivity.startActivity(this,"username2");
    }
    @OnClick(R2.id.login_btn_welcome)
    void startLoginActivity(View view){

            LoginActivity.startActivity(this);

    }
}
