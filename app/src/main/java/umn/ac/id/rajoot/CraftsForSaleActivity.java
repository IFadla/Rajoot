package umn.ac.id.rajoot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class CraftsForSaleActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CraftItemAdapter adapter;
    FirebaseFirestore firebaseFirestore;
    private static ArrayList<ItemModel> itemArrayList;
    private static ArrayList<ItemModel> itemArrayListView;
    SharedPreferences prefs;
    TextView title, noData;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crafts_for_sale);

        //hide default action bar
        getSupportActionBar().hide();

        //Define id every single item on Sale Item / Kerajinan Dijual
        itemArrayListView       = new ArrayList<>();
        itemArrayList       = new ArrayList<>();
        recyclerView        = (RecyclerView) findViewById(R.id.recycle_view_craft_for_sale);
        firebaseFirestore   = FirebaseFirestore.getInstance();
        prefs               = getSharedPreferences("Data", Context.MODE_PRIVATE);
        noData              = findViewById(R.id.no_data);
        title               = findViewById(R.id.text_title_toolbar);
        back                = findViewById(R.id.back_toolbar);

        //Set click action and value on item
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title.setText("Kerajinan Dijual");
        adapter = new CraftItemAdapter(itemArrayListView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        //Retrieving data from firebase on Sale Item / Kerajinan Dijual Page
        getData();
    }

    public void getData(){
        DocumentReference docRef = firebaseFirestore.collection("items").document("items");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Map map = document.getData();
                        if (map.get("data") != null) {
                            ArrayList data = (ArrayList) map.get("data");
                            for (int i = 0; i < data.size(); i++) {
                                String[] split = data.get(i).toString().split("\\|");
                                if (prefs.getString("user", "").equals(split[1])) {
                                    itemArrayList.add(new ItemModel(String.valueOf(i), split[1], split[2], split[3], split[4], split[5], split[6], split[7]));
                                }
                            }

                            for (int j = itemArrayList.size() - 1; j >= 0; j--) {
                                itemArrayListView.add(new ItemModel(itemArrayList.get(j).position,
                                        itemArrayList.get(j).user,
                                        itemArrayList.get(j).title,
                                        itemArrayList.get(j).price,
                                        itemArrayList.get(j).phone,
                                        itemArrayList.get(j).condition,
                                        itemArrayList.get(j).path,
                                        itemArrayList.get(j).description));
                            }

                            if (itemArrayList.size() == 0) {
                                noData.setVisibility(View.VISIBLE);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }
}