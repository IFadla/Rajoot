package umn.ac.id.rajoot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.io.InputStream;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class UpdateCraftActivity extends AppCompatActivity {
    private EditText title, price, phone, description;
    Button update, delete, img_btton_update;
    int position = 0;
    FirebaseFirestore firebaseFirestore;
    LinearLayout LLProgressBarLogin;
    private static ArrayList<ItemModel> itemArrayList;
    private ImageView imageViewUpdate;
    public static final int PICK_IMAGE = 1;
    Uri selectedImage, downloadUri;
    FirebaseStorage storage;
    TextView titleToolbar;
    ImageView back;
    Spinner condition;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            selectedImage = data.getData();
            imageViewUpdate.setVisibility(View.VISIBLE);
            imageViewUpdate.setImageURI(selectedImage);
            getSupportActionBar().hide();
            StorageReference storageRef = storage.getReference();
            StorageReference mountainsRef = storageRef.child(title.getText().toString() + price.getText().toString() + ".jpg");
            imageViewUpdate.setDrawingCacheEnabled(true);
            imageViewUpdate.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) imageViewUpdate.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] datas = baos.toByteArray();

            UploadTask uploadTask = mountainsRef.putBytes(datas);
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
                        downloadUri = task.getResult();
                    } else {
                        Toast.makeText(UpdateCraftActivity.this, "Upload Image Gagal", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Gambar belum dipilih", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_craft);

        //Hide action bar this Update Craft / Perbarui Kerajinan
        getSupportActionBar().hide();

        //Define id every item on Page Update
        delete              = (Button) findViewById(R.id.btn_craft_delete);
        storage             = FirebaseStorage.getInstance();
        imageViewUpdate     = findViewById(R.id.imageViewUpdate);
        itemArrayList       = HomeFragment.getItemArrayList();
        firebaseFirestore   = FirebaseFirestore.getInstance();
        position            = Integer.parseInt(getIntent().getExtras().getString("position"));
        title               = (EditText) findViewById(R.id.update_sell_title);
        price               = (EditText) findViewById(R.id.update_sell_price);
        phone               = (EditText) findViewById(R.id.update_sell_phone);
        condition           = (Spinner) findViewById(R.id.update_sell_condition);
        description         = (EditText) findViewById(R.id.update_sell_description);
        update              = (Button) findViewById(R.id.btn_craft_update);
        LLProgressBarLogin  = findViewById(R.id.container_progress_bar_login);
        titleToolbar        = findViewById(R.id.text_title_toolbar);
        titleToolbar.setText("Perbarui Kerajinan");
        back                = findViewById(R.id.back_toolbar);
        img_btton_update    = (Button) findViewById(R.id.btn_img_sell_update);

        //Action bar when back image clicked
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Define existing value every item on update form
        String cond[] = {"BARU","BEKAS"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, cond);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        condition.setAdapter(spinnerArrayAdapter);
        setSpinText(condition, getIntent().getExtras().getString("condition"));
        title.setText(getIntent().getExtras().getString("title"));
        price.setText(getIntent().getExtras().getString("price").replace("K", "000"));
        phone.setText(getIntent().getExtras().getString("phone"));
        description.setText(getIntent().getExtras().getString("description"));
        new DownLoadImageTask(imageViewUpdate).execute(getIntent().getExtras().getString("path"));

        //go to Pick Image when existing image clicked
        imageViewUpdate.setOnClickListener(new View.OnClickListener() {
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

        //go to Pick Image when button image clicked
        img_btton_update.setOnClickListener(new View.OnClickListener() {
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

        //Update data to firebase when button update clicked
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("Data", Context.MODE_PRIVATE);
                LLProgressBarLogin.setVisibility(View.VISIBLE);
                List<String> list = new ArrayList<>();

                itemArrayList.set(position, new ItemModel(String.valueOf(position),
                        prefs.getString("user", ""),
                        title.getText().toString(),
                        price.getText().toString(),
                        phone.getText().toString(),
                        condition.getSelectedItem().toString(),
                        selectedImage != null ? downloadUri.toString() : itemArrayList.get(position).path,
                        description.getText().toString()));
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
                                System.out.println("Success update data");
                                LLProgressBarLogin.setVisibility(View.INVISIBLE);
                                Intent intent = new Intent(UpdateCraftActivity.this, CraftsForSaleActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println("Gagal update data");
                                LLProgressBarLogin.setVisibility(View.INVISIBLE);
                            }
                        });
            }
        });

        //Delete and Update data to firebase when button delete clicked
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LLProgressBarLogin.setVisibility(View.VISIBLE);
                itemArrayList.remove(position);
                List<String> list = new ArrayList<>();
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
                                LLProgressBarLogin.setVisibility(View.INVISIBLE);
                                LLProgressBarLogin.setVisibility(View.INVISIBLE);
                                Intent intent = new Intent(UpdateCraftActivity.this, CraftsForSaleActivity.class);
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
            }
        });
    }

    //set existing spinner value
    public void setSpinText(Spinner spin, String text)
    {
        for(int i= 0; i < spin.getAdapter().getCount(); i++)
        {
            if(spin.getAdapter().getItem(i).toString().contains(text))
            {
                spin.setSelection(i);
            }
        }

    }

    //Download existing image
    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        /*
            doInBackground(Params... params)
                Override this method to perform a computation on a background thread.
         */
        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */
        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }
}