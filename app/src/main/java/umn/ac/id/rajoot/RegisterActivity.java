package umn.ac.id.rajoot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText mFullName, mEmail, mPassword;
    Button mRegisterBtn, mGotoLogin;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    LinearLayout LLProgressBarRegister;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Hide actionbar this splashscreen
        getSupportActionBar().hide();

        mFullName               = findViewById(R.id.input_register_name);
        mEmail                  = findViewById(R.id.input_register_email);
        mPassword               = findViewById(R.id.input_register_password);
        mRegisterBtn            = findViewById(R.id.btn_regiseter);
        mGotoLogin              = findViewById(R.id.btn_go_to_login);
        firebaseAuth            = FirebaseAuth.getInstance();
        firebaseFirestore       = FirebaseFirestore.getInstance();
        LLProgressBarRegister   = findViewById(R.id.container_progress_bar_register);

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = mFullName.getText().toString().trim();
                String email    = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(fullName)) {
                    mFullName.setError("Lengkapi nama lengkap!");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Lengkapi email");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Membutuhkan Kata sandi!");
                }

                if (password.length() < 6 ) {
                    mPassword.setError("Kata sandi minimal 6 karakter!");
                    return;
                }

                LLProgressBarRegister.setVisibility(View.VISIBLE);
                
                // Register the user in firebase
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userID = firebaseAuth.getCurrentUser().getUid();

                            DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("fullName", fullName);
                            user.put("email", email);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "onSuccess: user profile is created for" + userID);
                                }
                            });

                            Toast.makeText(RegisterActivity.this, "Berhasil daftar akun!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        } else {
                            LLProgressBarRegister.setVisibility(View.INVISIBLE);
                            Toast.makeText(RegisterActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    // When the button masuk clicked then go to the login activity
    public void registerToLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}