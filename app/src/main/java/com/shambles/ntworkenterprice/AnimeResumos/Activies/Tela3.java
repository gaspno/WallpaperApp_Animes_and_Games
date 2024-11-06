package com.shambles.ntworkenterprice.AnimeResumos.Activies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.shambles.ntworkenterprice.AnimeResumos.Adapters.AdapterWallpapers;
import com.shambles.ntworkenterprice.AnimesReview.R;


import java.util.List;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

public class Tela3 extends AppCompatActivity {
    private RecyclerView recyclerViewWallpaper;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_sticks);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        setad();
        recyclerViewWallpaper = findViewById(R.id.WallpaperrecicleView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),1);
        recyclerViewWallpaper.setLayoutManager(layoutManager);
        recyclerViewWallpaper.setHasFixedSize(false);

        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        // Create a reference with an initial file path and name

        StorageReference Animeselect =  storage.getReference().child(getIntent().getStringExtra("Anime")).child("miniature");
        Animeselect.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                // All the prefixes under listRef.
                List<StorageReference> references = listResult.getItems();
                recyclerViewWallpaper.setAdapter(new AdapterWallpapers(references, getApplicationContext()));
                // You may call listAll() recursively on them.
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setad() {


    }


    //setWallpaper();

}