package umn.ac.id.rajoot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Hide actionbar this splashscreen
        getSupportActionBar().hide();
    }

    // When the button daftar clicked then go to the register activity
    public void loginToRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    // When the button masuk clicked then go to the main activity
    public void masuk(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}