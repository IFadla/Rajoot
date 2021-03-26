package umn.ac.id.rajoot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Hide actionbar this splashscreen
        getSupportActionBar().hide();

        // Show this splashscreen, after delay 4000 millis then go to main activity
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(SplashScreen.this, StartActivity.class);
                startActivity(homeIntent);
                overridePendingTransition(0,0);
                finish();
            }
        }, 4000);
    }
}