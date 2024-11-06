package com.shambles.ntworkenterprice.AnimeResumos.Activies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shambles.ntworkenterprice.AnimesReview.R;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.model.AspectRatio;
import java.io.File;

import java.io.IOException;

public class CropTela extends AppCompatActivity {


    private static final String TAG ="CropTela" ;
    private StorageReference Image;
    private ImageView imageView;
    private InterstitialAd mInterstitialAd;


    /**
     * Persist URI image to crop URI if specific permissions are required
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_tela);
        imageView=findViewById(R.id.backend);
        loadAdinter();
        CropObjetive();

    }

    private void CropObjetive() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        try {

            File file=File.createTempFile("temp","png");


            Image=storage.getReferenceFromUrl(getIntent().getStringExtra("URL"));
            Image.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Uri uri;
                    uri=Uri.fromFile(file);
                    // start picker to get image for cropping and then use the image in cropping activity
                    imageCrop(uri);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            Bitmap bitmap=BitmapFactory.decodeFile(resultUri.getEncodedPath());
            imageView.setImageBitmap(bitmap);
            showPopup(bitmap);
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            finish();
        }
        else {
            finish();
        }
    }


    public void  imageCrop(Uri imageuri) {
    /*    DisplayMetrics widthMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(widthMetrics);
        int width = widthMetrics.widthPixels;

        DisplayMetrics heightMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(heightMetrics);
        int height = heightMetrics.heightPixels;
        float aspectRatio = (float) width/height;
        */



        try {
            UCrop.Options options = new UCrop.Options();
            File file2=File.createTempFile("temp2","png");
            options.setLogoColor(getColor(R.color.windowBackground));
            options.setAspectRatioOptions(1,
                    new AspectRatio("9x16",9,16),
                    new AspectRatio("9x21",9,21),
                    new AspectRatio("5x7",5,7),
                    new AspectRatio("3x4",3,4));
            options.setRootViewBackgroundColor(getColor(R.color.contrasttextcolor));
            options.setToolbarColor(getColor(R.color.windowBackground));
            options.setToolbarWidgetColor(getColor(R.color.setwall));
            options.setStatusBarColor(getColor(R.color.setwall));
            options.setCropGridColor(getColor(R.color.contrasttextcolor));
            options.setActiveControlsWidgetColor(getColor(R.color.setwall));
            options.setDimmedLayerColor(getColor(R.color.titleBackground));
            options.setCropFrameColor(getColor(R.color.setwall));
            options.setToolbarTitle("Adjust and Crop the Image");
            UCrop.of(imageuri,Uri.fromFile(file2)).withOptions(options).start(CropTela.this);



        } catch (IOException e) {
            e.printStackTrace();
        }




    }



    public void showPopup(Bitmap set) {
        Button both,lock,home,download;
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.backgroundset);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOwnerActivity(this);
        dialog.getWindow();
        dialog.setCancelable(true);
        both=dialog.findViewById(R.id.BothScrensId);
        lock=dialog.findViewById(R.id.LockScrenId);
        home=dialog.findViewById(R.id.HomeScrenId);
        download=dialog.findViewById(R.id.DownloadId);
        final WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());
        both.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                        manager.setBitmap(set);
                        Toast.makeText(getApplicationContext(), "Wallpaper set!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        ultimatead();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    finish();
                }
            }
        });
        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    manager.setBitmap(set,null,true, WallpaperManager.FLAG_LOCK);
                    Toast.makeText(getApplicationContext(), "Wallpaper set!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    ultimatead();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    finish();
                }
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    manager.setBitmap(set,null,true, WallpaperManager.FLAG_SYSTEM);
                    Toast.makeText(getApplicationContext(), "Wallpaper set!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    ultimatead();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    finish();
                }
            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    MediaStore.Images.Media.insertImage(getContentResolver(), set, Image.getPath(), "fromanimesbackgrounds");
                    Toast.makeText(getApplicationContext(), "Download Complete", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    ultimatead();
                } else {
                    requestPermission();
                }
            }
        });

        dialog.show();

    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(CropTela.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(CropTela.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
           ActivityCompat.requestPermissions(CropTela.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        }else{
           ActivityCompat.requestPermissions(CropTela.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        }
    }
    private void ultimatead(){
        if (mInterstitialAd!=null) {
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when fullscreen content is dismissed.
                    Log.d("TAG", "The ad was dismissed.");
                    finish();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    // Called when fullscreen content failed to show.
                    Log.d("TAG", "The ad failed to show.");
                    finish();
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    // Called when fullscreen content is shown.
                    // Make sure to set your reference to null so you don't
                    // show it a second time.
                    mInterstitialAd = null;
                    Log.d("TAG", "The ad was shown.");
                }
            });
                    mInterstitialAd.show(CropTela.this);
        }else{
            finish();
        }

    }
    private void loadAdinter() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-9324003669548340/3223801278", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                Log.i(TAG, "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.i(TAG, loadAdError.getMessage());
                mInterstitialAd = null;

            }
        });


    }


}




