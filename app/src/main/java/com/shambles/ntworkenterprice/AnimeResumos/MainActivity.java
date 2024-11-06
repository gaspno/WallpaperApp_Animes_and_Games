package com.shambles.ntworkenterprice.AnimeResumos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;
import com.shambles.ntworkenterprice.AnimeResumos.Activies.TitulosActivity;
import com.shambles.ntworkenterprice.AnimesReview.R;

public class MainActivity extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                SafetyNetAppCheckProviderFactory.getInstance());
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        ImageView image =findViewById(R.id.imageView);
        Animation animation1 =AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move);

        Glide.with(getApplicationContext()).load(R.drawable.home).circleCrop().into(image);
        image.startAnimation(animation1);
        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                checkinternet();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //criar thread



        //cada Thread configura um botão
    }

    private void checkinternet() {
        ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()==true )
        {
            Intent intent=new Intent(getApplicationContext(), TitulosActivity.class);
            startActivity(intent);
            finish();

        }
        else
        {
            AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle(getResources().getString(R.string.tryAgain));
            dialog.setPositiveButton(R.string.yes, (dialog12, id) -> checkinternet()).setNegativeButton(R.string.no, (dialog1, id) -> finish());


            dialog.show();

        }
    }

}