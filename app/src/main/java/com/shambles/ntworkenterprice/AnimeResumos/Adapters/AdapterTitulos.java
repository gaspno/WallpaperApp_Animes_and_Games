package com.shambles.ntworkenterprice.AnimeResumos.Adapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.shambles.ntworkenterprice.AnimeResumos.Activies.Tela3;
import com.shambles.ntworkenterprice.Modelos.AnimeManga;
import com.shambles.ntworkenterprice.AnimesReview.R;
import java.util.List;
public class AdapterTitulos extends RecyclerView.Adapter<AdapterTitulos.Vision> {

    private final List<AnimeManga> animeMangaList;
    private final Context context;


    public AdapterTitulos(List<AnimeManga> animeMangas, Context applicationContext) {

        animeMangaList = animeMangas;
        context=applicationContext;

    }


    @NonNull
    @Override
    public Vision onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vision(LayoutInflater.from(parent.getContext()).inflate(R.layout.modelosagas, parent, false));
    }






    @Override
    public void onBindViewHolder(@NonNull Vision holder, int position) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        int safePosition = holder.getBindingAdapterPosition();
        StorageReference animeselect = storage.getReference().child(animeMangaList.get(safePosition).getTexto());

        // Create a reference with an initial file path and name,


        animeselect.list(1).addOnSuccessListener(listResult -> {

            StorageReference references;

            references = listResult.getItems().get(0);
            references.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(context).load(uri).into(holder.titulo)).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        });

        holder.textTitulo.setText(animeMangaList.get(position).getDescription());
        holder.titulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle=new Bundle();
                bundle.putInt("IdPost",view.getId());
                bundle.putString("Anime",animeMangaList.get(safePosition).getTexto());
                Intent intent= new Intent(context, Tela3.class);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                try {
                   finalize();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return animeMangaList.size();
    }

    public static class Vision extends RecyclerView.ViewHolder{

        ImageView titulo;
        TextView textTitulo;

        public Vision(@NonNull View itemView) {
            super(itemView);
            titulo=itemView.findViewById(R.id.titule);
            textTitulo=itemView.findViewById(R.id.textTitulo);
        }
    }
}
