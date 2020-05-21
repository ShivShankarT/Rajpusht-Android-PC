package in.rajpusht.pc.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import in.rajpusht.pc.ui.home.HomeActivity;
import in.rajpusht.pc.R;
import in.rajpusht.pc.databinding.ActivitySplashScreenBinding;

public class SplashScreenActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySplashScreenBinding viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                //Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        },100);
    }
}
