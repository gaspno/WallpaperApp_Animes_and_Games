package com.shambles.ntworkenterprice.AnimeResumos.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.shambles.ntworkenterprice.AnimeResumos.Activies.CropTela;
import com.shambles.ntworkenterprice.AnimesReview.R;
import java.util.List;

public class AdapterWallpapers extends RecyclerView.Adapter<AdapterWallpapers.Vison> {

    private final List<StorageReference> lista;
    private final Context contextToGlide;




    public AdapterWallpapers(List<StorageReference> lista, Context context) {
        this.lista = lista;
        contextToGlide=context;
    }



    @NonNull
    @Override
    public Vison onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterWallpapers.Vison((LayoutInflater.from(parent.getContext()).inflate(R.layout.modelowallpaper,parent,false)));
    }

    @Override
    public void onBindViewHolder(@NonNull Vison holder,int pos) {


        lista.get(pos*2).getDownloadUrl().addOnSuccessListener(uri -> {

            Glide.with(contextToGlide).load(uri).into(holder.tela);
            if(holder.getPosition()%15==0) {
                AdView mAdView = new AdView(contextToGlide);
                mAdView.setAdUnitId("ca-app-pub-9324003669548340/3511398273");
                holder.AD.removeAllViews();
                holder.AD.addView(mAdView);
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.setAdSize(new AdSize(AdSize.FULL_WIDTH, 100));
                mAdView.loadAd(adRequest);
            }else  if(pos%5==0&&pos!=0) {
                AdView mAdView = new AdView(contextToGlide);
                mAdView.setAdUnitId("ca-app-pub-9324003669548340/5849964610");
                holder.AD.removeAllViews();
                holder.AD.addView(mAdView);
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.setAdSize(new AdSize(AdSize.FULL_WIDTH, 100));
                mAdView.loadAd(adRequest);
            }else
            {
                holder.AD.removeAllViews();
            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(contextToGlide,"Error"+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        lista.get((pos*2)+1).getDownloadUrl().addOnSuccessListener(uri -> Glide.with(contextToGlide).load(uri).into(holder.tela2)).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(contextToGlide,"Error"+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        holder.tela.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    v.setClickable(false);
                    lista.get(holder.getPosition()*2).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Intent intent= new Intent(contextToGlide, CropTela.class);
                            intent.putExtra("URL",uri.toString().replace("miniature","").replace("webp","jpg"));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            contextToGlide.startActivity(intent);
                            v.setClickable(true);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
    }
                });
        holder.tela2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                v.setClickable(false);
                lista.get((holder.getPosition()*2)+1).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Intent intent= new Intent(contextToGlide, CropTela.class);
                        intent.putExtra("URL",uri.toString().replace("miniature","").replace("webp","jpg"));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        contextToGlide.startActivity(intent);
                        v.setClickable(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });
    }
    @Override
    public int getItemCount() {
        return lista.size()/2;
    }

    public class Vison extends RecyclerView.ViewHolder{

        ImageView tela,tela2;
        FrameLayout AD;


        public Vison(@NonNull View itemView) {
            super(itemView);

            tela=itemView.findViewById(R.id.screnwall);
            tela2=itemView.findViewById(R.id.screnwall2);
            AD=itemView.findViewById(R.id.adLayout);

        }
    }
}
