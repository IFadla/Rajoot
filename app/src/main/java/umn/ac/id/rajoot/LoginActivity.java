package umn.ac.id.rajoot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    public static final int TIME_INTERVAL = 2000;
    private long backPressed;
    EditText mEmail, mPassword;
    Button mLoginBtn;
    LinearLayout LLProgressBarLogin;
    FirebaseAuth firebaseAuth;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Hide actionbar this splashscreen
        getSupportActionBar().hide();

        mEmail              = findViewById(R.id.input_login_email);
        mPassword           = findViewById(R.id.input_login_password);
        mLoginBtn           = findViewById(R.id.btn_login);
        LLProgressBarLogin  = findViewById(R.id.container_progress_bar_login);
        firebaseAuth        = FirebaseAuth.getInstance();

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email    = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Lengkapi email!");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Membutuhkan kata sandi!");
                }

                if (password.length() < 6 ) {
                    mPassword.setError("Kata sandi minimal 6 karakter!");
                    return;
                }

                LLProgressBarLogin.setVisibility(View.VISIBLE);

                // Authenticate the user
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            prefs = getSharedPreferences("Data", Context.MODE_PRIVATE);
                            editor = prefs.edit();

                            editor.putString("user", mEmail.getText().toString());
                            editor.apply();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(LoginActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            LLProgressBarLogin.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });
    }

    // When the button daftar clicked then go to the register activity
    public void loginToRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    // When the button daftar clicked then go to the register activity
    public void loginToForgotPassword(View view) {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (backPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        } else {
            Toast.makeText(LoginActivity.this,"Press Back Again!",Toast.LENGTH_SHORT).show();
        }

        backPressed = System.currentTimeMillis();
    }
}