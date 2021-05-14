package umn.ac.id.rajoot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellActivity extends AppCompatActivity {
    private ArrayList<ItemModel> itemArrayList;
    EditText title, price, phone, description;
    Button img, sell;
    FirebaseFirestore firebaseFirestore;
    LinearLayout LLProgressBarLogin;
    ImageView imageView;
    Uri selectedImage;
    public static final int PICK_IMAGE = 1;
    FirebaseStorage storage;
    TextView titleToolbar;
    ImageView back;
    Spinner condition;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            selectedImage = data.getData();
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageURI(selectedImage);
        } else {
            Toast.makeText(this, "Gambar belum dipilih", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        //hide default action bar
        getSupportActionBar().hide();

        //define every id on page sell
        storage             = FirebaseStorage.getInstance();
        itemArrayList       = HomeFragment.getItemArrayList();
        firebaseFirestore   = FirebaseFirestore.getInstance();
        img                 = (Button) findViewById(R.id.btn_img_sell);
        sell                = (Button) findViewById(R.id.btn_sell);
        title               = (EditText) findViewById(R.id.input_sell_title);
        price               = (EditText) findViewById(R.id.input_sell_price);
        phone               = (EditText) findViewById(R.id.input_sell_phone);
        condition           = (Spinner) findViewById(R.id.input_sell_condition);
        description         = (EditText) findViewById(R.id.input_sell_description);
        LLProgressBarLogin  = findViewById(R.id.container_progress_bar_login);
        imageView           = (ImageView) findViewById(R.id.imageView);
        titleToolbar        = findViewById(R.id.text_title_toolbar);

        //Custom action bar action and set value / title
        titleToolbar.setText("Jual Kerajinan");
        back                = findViewById(R.id.back_toolbar);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Set value on spinner item
        String cond[] = {"BARU","BEKAS"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, cond);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        condition.setAdapter(spinnerArrayAdapter);

        //Add click action when image Button is Clicked
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);
            }
        });
        //Add click action when sell Button is Clicked
        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImage != null && !selectedImage.getPath().isEmpty()) {
                    //validation rule when input is empty
                    if (title.getText().toString().isEmpty()) {
                        title.setError("Judul harus diisi");
                        title.requestFocus();
                    } else {
                        if (price.getText().toString().isEmpty()) {
                            price.setError("Harga harus diisi");
                            price.requestFocus();
                        } else {
                            if (phone.getText().toString().isEmpty()) {
                                phone.setError("Telepon harus diisi");
                                phone.requestFocus();
                            } else {
                                if (description.getText().toString().isEmpty()) {
                                    description.setError("Deksripsi harus diisi");
                                    description.requestFocus();
                                } else {
                                    //set data to firebase
                                    LLProgressBarLogin.setVisibility(View.VISIBLE);
                                    StorageReference storageRef = storage.getReference();
                                    StorageReference mountainsRef = storageRef.child(title.getText().toString() + price.getText().toString() + ".jpg");
                                    imageView.setDrawingCacheEnabled(true);
                                    imageView.buildDrawingCache();
                                    Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                    byte[] data = baos.toByteArray();

                                    UploadTask uploadTask = mountainsRef.putBytes(data);
                                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                        @Override
                                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                            if (!task.isSuccessful()) {
                                                throw task.getException();
                                            }

                                            return mountainsRef.getDownloadUrl();
                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                SharedPreferences prefs = getSharedPreferences("Data", Context.MODE_PRIVATE);
                                                Uri downloadUri = task.getResult();
                                                List<String> list = new ArrayList<>();

                                                itemArrayList.add(new ItemModel(String.valueOf(itemArrayList.size()), prefs.getString("user", ""), title.getText().toString(), price.getText().toString(), phone.getText().toString(), condition.getSelectedItem().toString(), downloadUri.toString(), description.getText().toString()));
                                                for (ItemModel item : itemArrayList) {

                                                    list.add(item.position + "|" + item.user + "|" + item.title + "|" + item.price + "|" + item.phone + "|" + item.condition + "|" + item.path + "|"+ item.description);
                                                }
                                                Map data = new HashMap();
                                                data.put("data", list);
                                                firebaseFirestore.collection("items").document("items")
                                                        .set(data)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                System.out.println("Success tambah data");
                                                                LLProgressBarLogin.setVisibility(View.INVISIBLE);
                                                                Intent intent = new Intent(SellActivity.this, MainActivity.class);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                LLProgressBarLogin.setVisibility(View.INVISIBLE);
                                                            }
                                                        });
                                            } else {
                                                LLProgressBarLogin.setVisibility(View.INVISIBLE);
                                                Toast.makeText(SellActivity.this, "Upload Image Gagal", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }
                } else {
                    Toast.makeText(SellActivity.this, "Pilih Gambar terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

