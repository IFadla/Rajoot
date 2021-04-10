package umn.ac.id.rajoot;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ProfileFragment extends Fragment {
    LinearLayout itemCraftsForSale;
    LinearLayout itemRatingApp;
    LinearLayout itemInformation;
    LinearLayout itemLogout;
    ConstraintLayout clContainerVerify;
    Button btnVerifyLogout;
    TextView dFullName, tNotVerify, tVerify;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        itemCraftsForSale   = view.findViewById(R.id.item_kerajinan_dijual);
        itemRatingApp       = view.findViewById(R.id.item_beri_nilai);
        itemInformation     = view.findViewById(R.id.item_informasi);
        itemLogout          = view.findViewById(R.id.item_keluar);
        dFullName           = view.findViewById(R.id.full_name_profile);
        tNotVerify          = view.findViewById(R.id.not_verify);
        tVerify             = view.findViewById(R.id.verify);
        clContainerVerify   = view.findViewById(R.id.container_verify_email);
        btnVerifyLogout     = view.findViewById(R.id.btn_verify_logout);
        firebaseAuth        = FirebaseAuth.getInstance();
        firebaseFirestore   = FirebaseFirestore.getInstance();
        userID              = firebaseAuth.getCurrentUser().getUid();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (!firebaseUser.isEmailVerified()) {
            tVerify.setVisibility(View.GONE);
            tNotVerify.setVisibility(View.VISIBLE);

            tNotVerify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            clContainerVerify.setVisibility(View.VISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("TAG", "onFailure: Email not sent" + e.getMessage());
                        }
                    });
                }
            });
        }

        final DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w("TAG", "Listen failed.", error);
                    return;
                }

                if (value != null && value.exists()) {
                    dFullName.setText(value.getString("fullName"));
                    Log.d("TAG", "Current data: " + value.getData());
                } else {
                    Log.d("TAG", "Current data: null");
                }
            }
        });

        itemCraftsForSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCraftForSale();
            }
        });

        itemRatingApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFormRating();
            }
        });

        itemInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToInformation();
            }
        });

        itemLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutAccount();
            }
        });

        btnVerifyLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutAccount();
            }
        });

        return view;
    }

    // When the button daftar clicked then go to the crafts for sale activity
    public void goToCraftForSale() {
        Intent intent = new Intent(getActivity(), CraftsForSaleActivity.class);
        startActivity(intent);
    }

    // When the button daftar clicked then go to the information activity
    public void goToInformation() {
        Intent intent = new Intent(getActivity(), InformationActivity.class);
        startActivity(intent);
    }

    // When the button daftar clicked then go to the google form
    public void goToFormRating() {
        Toast.makeText(getActivity(),"Google Form Under Maintenance!",Toast.LENGTH_SHORT).show();
    }

    public void logoutAccount() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }
}