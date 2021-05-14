package umn.ac.id.rajoot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class InformationActivity extends AppCompatActivity {
    TextView text_title_toolbar;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        //Hide default action bar
        getSupportActionBar().hide();

        //Set custom action bar id
        text_title_toolbar  = (TextView) findViewById(R.id.text_title_toolbar);
        back                = (ImageView) findViewById(R.id.back_toolbar);

        //Set action and value action bar
        text_title_toolbar.setText("Informasi");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}