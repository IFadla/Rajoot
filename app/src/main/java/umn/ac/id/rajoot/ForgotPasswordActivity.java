package umn.ac.id.rajoot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    TextView text_title_toolbar;
    ImageView back;
    EditText mEmail;
    Button mBtnForgot;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //Define every single id on forgot password
        mEmail          = findViewById(R.id.input_forgot_email);
        mBtnForgot      = findViewById(R.id.btn_forgot_password);
        firebaseAuth    = FirebaseAuth.getInstance();

        // Hide actionbar this splashscreen
        getSupportActionBar().hide();

        //Set custom action bar id
        text_title_toolbar  = (TextView) findViewById(R.id.text_title_toolbar);
        back                = (ImageView) findViewById(R.id.back_toolbar);

        //Set action and value action bar
        text_title_toolbar.setText("Lupa Password");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBtnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Masukan email yang terdaftar!");
                    return;
                }

                firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ForgotPasswordActivity.this, "Link reset kata sandi berhasil dikirim ke email!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ForgotPasswordActivity.this, "Link reset tidak berhasil dikirm!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}