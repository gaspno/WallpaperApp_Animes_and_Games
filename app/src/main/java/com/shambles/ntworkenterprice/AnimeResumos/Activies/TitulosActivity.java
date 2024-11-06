package com.shambles.ntworkenterprice.AnimeResumos.Activies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.shambles.ntworkenterprice.AnimeResumos.Adapters.AdapterTitulos;
import com.shambles.ntworkenterprice.AnimesReview.BuildConfig;
import com.shambles.ntworkenterprice.Modelos.AnimeManga;
import com.shambles.ntworkenterprice.AnimesReview.R;
import java.util.Random;


public class TitulosActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG ="TitulosActivity" ;
    private RecyclerView recyclerViewCidades;
    private final List<AnimeManga> animeMangas =new ArrayList<>();
    private StorageReference Animeselect;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //criar Threads

        ThirdThread thirdThread=new ThirdThread();
        FourTread fourTread=new FourTread();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolsbar);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        setad();
        Toolbar toolbar = findViewById(R.id.toolbar ) ;
        setSupportActionBar(toolbar) ;
        DrawerLayout drawer = findViewById(R.id.drawer_layout ) ;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer , toolbar , R.string.navigation_drawer_open ,
                R.string.navigation_drawer_close ) ;
        drawer.addDrawerListener(toggle) ;
        toggle.syncState() ;
        NavigationView navigationView = findViewById(R.id.nav_view ) ;
        navigationView.setNavigationItemSelectedListener(this) ;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child("AnimesBackgrounds");

        // Create a reference with an initial file path and name

        Animeselect= storageRef.child("Animes");
        recyclerViewCidades=findViewById(R.id.RecicleMitos);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewCidades.setLayoutManager(layoutManager);
        recyclerViewCidades.setHasFixedSize(true);


        fourTread.start();
        thirdThread.start();





    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the main_menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menuoptions, menu);
        MenuItem mSearch = menu.findItem(R.id.search);
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("submit",query);


                StorageReference ref = Animeselect.child(query);
                ref.list(1).addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        if(listResult.getItems().size()>0) {
                            animeMangas.clear();
                            animeMangas.add(new AnimeManga(Animeselect.getName(), ref.getName(), ref.getPath(), ref.getName()));
                            recyclerViewCidades.setAdapter(new AdapterTitulos(animeMangas, getApplicationContext()));
                        }else
                            generatePost();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
               // adapter.getFilter().filter(newText);
                Log.d("change",newText);

                Animeselect.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        animeMangas.clear();
                        final List<StorageReference> references =listResult.getPrefixes();
                        for (StorageReference ref:references
                        )
                        {
                            String comp=ref.getName().toUpperCase();
                            if(comp.contains(newText.toUpperCase()))
                            animeMangas.add(new AnimeManga(Animeselect.getName(),ref.getName(),ref.getPath(),ref.getName()));
                        }
                        recyclerViewCidades.setAdapter(new AdapterTitulos(animeMangas, getApplicationContext()));

                    }
                });
                return true;
            }
        });


        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

     /*   switch(item.getItemId()) {
            case R.id.search:
                Toast.makeText(getApplicationContext(),"test",Toast.LENGTH_LONG).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }*/

        return true;
    }


    @Override
    public boolean onNavigationItemSelected ( @NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId() ;
        switch (id){

            case R.id. nav_gallery:
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.setType("image/*");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id. nav_slideshow :
                Intent intentRating = new Intent(Intent.ACTION_VIEW);
                intentRating.setData(Uri.parse(
                        "https://play.google.com/store/apps/details?id=com.shambles.ntworkenterprice.AnimesReview"));
                startActivity(intentRating);
              break;

            case R.id. nav_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out this app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case R.id. nav_send :
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","gaspgames@outlook.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));


                break;
        }
        DrawerLayout drawer = findViewById(R.id. drawer_layout ) ;
        drawer.closeDrawer(GravityCompat.START ) ;
        return true;
    }

    private void setad() {
        FrameLayout adContainerView = findViewById(R.id.adView4);
        AdView mAdView = new AdView(getApplicationContext());
        mAdView.setAdUnitId("ca-app-pub-9324003669548340/3511398273");
        adContainerView.addView(mAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.setAdSize(new AdSize(AdSize.FULL_WIDTH,100));
        mAdView.loadAd(adRequest);

    }

    class ThirdThread extends  Thread{
        @Override
        public void run() {

            generatePost();

        }
    }
    class FourTread extends Thread{
        //configura os botões
        @Override
        public void run() {

            Random random=new Random();

            Button oldanimes = findViewById(R.id.oldanimesBt);
            Button games = findViewById(R.id.gamesBt);
            Button animes = findViewById(R.id.animesBt);
            Button oldgames = findViewById(R.id.oldgamesBt);
            Button films = findViewById(R.id.filmsBt);
            Button series = findViewById(R.id.seriesBt);
            oldanimes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewCidades=null;
                    recyclerViewCidades=findViewById(R.id.RecicleMitos);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerViewCidades.setLayoutManager(layoutManager);
                    recyclerViewCidades.setHasFixedSize(true);
                    Animeselect= storageRef.child("OldAnimes");

                    generatePost();//Thread para gerar dados enquanto deixa livre a interface




                }
            });
            animes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewCidades=null;
                    recyclerViewCidades=findViewById(R.id.RecicleMitos);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerViewCidades.setLayoutManager(layoutManager);
                    recyclerViewCidades.setHasFixedSize(true);
                    Animeselect= storageRef.child("Animes");

                    generatePost();//Thread para gerar dados enquanto deixa livre a interface




                }

            });
            games.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewCidades=null;
                    recyclerViewCidades=findViewById(R.id.RecicleMitos);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerViewCidades.setLayoutManager(layoutManager);
                    recyclerViewCidades.setHasFixedSize(true);
                    Animeselect= storageRef.child("Games");
                    generatePost();;//Thread para gerar dados enquanto deixa livre a interface



                }
            });
            oldgames.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewCidades=null;
                    recyclerViewCidades=findViewById(R.id.RecicleMitos);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerViewCidades.setLayoutManager(layoutManager);
                    recyclerViewCidades.setHasFixedSize(true);
             Animeselect= storageRef.child("OldGames");

             generatePost();//Thread para gerar dados enquanto deixa livre a interface

                }
            });
            films.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerViewCidades=null;
                    recyclerViewCidades=findViewById(R.id.RecicleMitos);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerViewCidades.setLayoutManager(layoutManager);
                    recyclerViewCidades.setHasFixedSize(true);
                    Animeselect= storageRef.child("Films");

                    generatePost();
                }
            });
            series.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerViewCidades=null;
                    recyclerViewCidades=findViewById(R.id.RecicleMitos);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerViewCidades.setLayoutManager(layoutManager);
                    recyclerViewCidades.setHasFixedSize(true);
                    Animeselect= storageRef.child("Series");

                    generatePost();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {

        Button Not, MoreApps, AVALUATION;
        RatingBar simpleRatingBar;
        Dialog dialogInter = new Dialog(TitulosActivity.this);
        dialogInter.setContentView(R.layout.promoappgasp);
        dialogInter.setCanceledOnTouchOutside(false);
        dialogInter.setOwnerActivity(TitulosActivity.this);
        dialogInter.getWindow();
        dialogInter.setCancelable(false);
        Not = dialogInter.findViewById(R.id.notId);
        MoreApps = dialogInter.findViewById(R.id.MoreApps);
        AVALUATION = dialogInter.findViewById(R.id.avaliarId);
        simpleRatingBar = dialogInter.findViewById(R.id.ratingLevelId);
        MoreApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRating = new Intent(Intent.ACTION_VIEW);
                intentRating.setData(Uri.parse(
                        "https://play.google.com/store/apps/dev?id=8934541135388811083"));
                startActivity(intentRating);
            }
        });
        Not.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        AVALUATION.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRating = new Intent(Intent.ACTION_VIEW);
                intentRating.setData(Uri.parse(
                        "https://play.google.com/store/apps/details?id=com.shambles.ntworkenterprice.AnimesReview"));
                startActivity(intentRating);
            }
        });
        simpleRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Intent intentRating = new Intent(Intent.ACTION_VIEW);
                intentRating.setData(Uri.parse(
                        "https://play.google.com/store/apps/details?id=com.shambles.ntworkenterprice.AnimesReview"));
                startActivity(intentRating);
            }
        });

        dialogInter.show();
        super.onBackPressed();

    }

    public void generatePost(){
        //gera os dados para a recicle view

        animeMangas.clear();

        Animeselect.listAll().addOnSuccessListener(listResult -> {

            // All the prefixes under listRef.
            final List<StorageReference> references =listResult.getPrefixes();
            for (StorageReference ref:references)
            {

            animeMangas.add(new AnimeManga(Animeselect.getName(),ref.getName(),ref.getPath(),ref.getName()));

            }

            recyclerViewCidades.setAdapter(new AdapterTitulos(animeMangas, getApplicationContext()));

           // You may call listAll() recursively on them.
            //recyclerViewCidades.stopNestedScroll();


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}