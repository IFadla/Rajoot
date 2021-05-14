package umn.ac.id.rajoot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;

import java.io.InputStream;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CraftDetailsActivity extends AppCompatActivity {
    private TextView title, price, condition, description;
    private LinearLayout back;
    FirebaseFirestore firebaseFirestore;
    private Button call;
    private ImageView imgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_craft_details);

        //Define id every item on detail product
        getSupportActionBar().hide();
        imgs                = (ImageView) findViewById(R.id.imgs);
        call                = (Button) findViewById(R.id.btn_call_detail);
        back                = (LinearLayout) findViewById(R.id.ic_back_detail);
        title               = (TextView) findViewById(R.id.txt_title);
        price               = (TextView) findViewById(R.id.txt_price);
        condition           = (TextView) findViewById(R.id.txt_condition);
        description         = (TextView) findViewById(R.id.txt_description);
        firebaseFirestore   = FirebaseFirestore.getInstance();

        //Click and value set up on detail product
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = getIntent().getExtras().getString("phone");
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });
        title.setText(getIntent().getExtras().getString("title"));
        description.setMovementMethod(new ScrollingMovementMethod());
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        formatRupiah.setMinimumFractionDigits(0);
        price.setText(formatRupiah.format(Double.parseDouble(getIntent().getExtras().getString("price"))));
        condition.setText(getIntent().getExtras().getString("condition"));
        description.setText(getIntent().getExtras().getString("description"));

        //Set action bar action when back image is clicked
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //go to download function
        new DownLoadImageTask(imgs).execute(getIntent().getExtras().getString("path"));
    }

    //Download image on detail product page
    private class DownLoadImageTask extends AsyncTask<String,Void, Bitmap> {
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