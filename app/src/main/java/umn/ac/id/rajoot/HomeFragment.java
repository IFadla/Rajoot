package umn.ac.id.rajoot;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private SaleItemAdapter adapter;
    FirebaseFirestore firebaseFirestore;
    private static ArrayList<ItemModel> itemArrayList;
    private static ArrayList<ItemModel> itemArrayListView;

    public static ArrayList<ItemModel> getItemArrayList() {
        return itemArrayList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //Define id every single item on Home Menu
        itemArrayList       = new ArrayList<>();
        itemArrayListView   = new ArrayList<>();
        recyclerView        = view.findViewById(R.id.recycle_view_home);
        firebaseFirestore   = FirebaseFirestore.getInstance();

        //Set value of list product on home page
        adapter = new SaleItemAdapter(itemArrayListView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        //Retreiving data from firebase on Home page
        getData();

        return view;
    }


    //Retreiving data from firebase
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
                                itemArrayList.add(new ItemModel(String.valueOf(i), split[1], split[2], split[3], split[4], split[5], split[6], split[7]));
                            }

                            ArrayList data2 = (ArrayList) map.get("data");
                            for (int i = data2.size() - 1; i >= 0; i--) {
                                String[] split = data2.get(i).toString().split("\\|");
                                itemArrayListView.add(new ItemModel(String.valueOf(i), split[1], split[2], split[3], split[4], split[5], split[6], split[7]));
                            }

                            adapter.notifyDataSetChanged();
                        }
                        Log.d("TAG", "DocumentSnapshot data: ");
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