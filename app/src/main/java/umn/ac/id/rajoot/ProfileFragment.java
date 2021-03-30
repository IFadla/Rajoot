package umn.ac.id.rajoot;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ProfileFragment extends Fragment {
    LinearLayout itemCraftsForSale;
    LinearLayout itemRatingApp;
    LinearLayout itemInformation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        itemCraftsForSale = view.findViewById(R.id.item_kerajinan_dijual);
        itemRatingApp = view.findViewById(R.id.item_beri_nilai);
        itemInformation = view.findViewById(R.id.item_informasi);

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
}