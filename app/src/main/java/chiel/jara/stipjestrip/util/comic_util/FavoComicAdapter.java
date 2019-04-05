package chiel.jara.stipjestrip.util.comic_util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import chiel.jara.stipjestrip.DetailActivity;
import chiel.jara.stipjestrip.MapActivity;
import chiel.jara.stipjestrip.R;
import chiel.jara.stipjestrip.model.comic_model.Comic;
import chiel.jara.stipjestrip.model.comic_model.ComicDatabase;

public class FavoComicAdapter extends RecyclerView.Adapter<FavoComicAdapter.ComicRowViewHolder>{

    public class ComicRowViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName, tvAuthor;
        private ImageView ivComic;
        private ImageButton btnDetails;
        private ImageButton btnLiked;
        private ImageButton btnVisited;

        private View.OnClickListener visitedListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context c = v.getContext();
                int position = getAdapterPosition();
                Comic visited = comics.get(position);

                if (visited.isVisited()){
                    visited.setVisited(false);
                    btnVisited.setColorFilter(Color.rgb(127, 127, 127));
                    ComicDatabase.getInstance(c).getMethodsComic().updateComic(visited);
                }else {
                    visited.setVisited(true);
                    btnVisited.setColorFilter(Color.WHITE);
                    ComicDatabase.getInstance(c).getMethodsComic().updateComic(visited);
                }
            }
        };

        private View.OnClickListener likedListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context c = v.getContext();
                int position = getAdapterPosition();
                Comic toLike = comics.get(position);

                if (toLike.isFavorite()){
                    toLike.setFavorite(false);
                    btnLiked.setColorFilter(Color.rgb(127, 127, 127));
                    ComicDatabase.getInstance(c).getMethodsComic().updateComic(toLike);
                    int pos = getAdapterPosition();
                    Comic delComic = comics.get(pos);
                    delComic.setFavorite(false);
                    comics.remove(delComic);
                    notifyDataSetChanged();
                }else {
                    toLike.setFavorite(true);
                    btnLiked.setColorFilter(Color.RED);
                    ComicDatabase.getInstance(c).getMethodsComic().updateComic(toLike);
                }
            }
        };

        private View.OnClickListener detailListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context c = v.getContext();
                Intent intent = new Intent(c, DetailActivity.class);
                int position = getAdapterPosition();
                Comic toSeeDetails = comics.get(position);
                intent.putExtra("comic", toSeeDetails);
                c.startActivity(intent);
            }
        };

        private View.OnClickListener listListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context c = v.getContext();
                Intent intent = new Intent(c, MapActivity.class);
                int position = getAdapterPosition();
                Comic toSeeOnMap = comics.get(position);
                intent.putExtra("chosen", toSeeOnMap);
                c.startActivity(intent);
            }
        };

        public ComicRowViewHolder (@NonNull View itemView){
            super(itemView);
            tvName=itemView.findViewById(R.id.tv_row_name);
            tvAuthor=itemView.findViewById(R.id.tv_row_author);
            ivComic=itemView.findViewById(R.id.iv_row_image);
            btnDetails=itemView.findViewById(R.id.btn_detail);
            btnDetails.setOnClickListener(detailListener);
            btnLiked=itemView.findViewById(R.id.btn_liked_list);
            btnLiked.setOnClickListener(likedListener);
            btnVisited=itemView.findViewById(R.id.btn_visited_list);
            btnVisited.setOnClickListener(visitedListener);
            ivComic.setOnClickListener(listListener);
        }
    }

    private List<Comic> comics;
    public FavoComicAdapter(List<Comic> comics){
        this.comics = comics;
    }

    @NonNull
    @Override
    public FavoComicAdapter.ComicRowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comic_row, viewGroup, false);
        FavoComicAdapter.ComicRowViewHolder comicRowViewHolder = new FavoComicAdapter.ComicRowViewHolder(row);
        return comicRowViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoComicAdapter.ComicRowViewHolder comicRowViewHolder, final int i) {
        //sort by a-z
        Collections.sort(comics, Comic.BY_NAME_ALPHABETICAL);
        //which comic?
        final Comic currentComic = comics.get(i);
        //instellen op viewholder
        comicRowViewHolder.tvName.setText(currentComic.getName());
        comicRowViewHolder.tvAuthor.setText(currentComic.getAuthor());
        try {
            FileInputStream fis = comicRowViewHolder.itemView.getContext().openFileInput(currentComic.getImgID());
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            comicRowViewHolder.ivComic.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (currentComic.isFavorite()){
            comicRowViewHolder.btnLiked.setColorFilter(Color.RED);
        }else {comicRowViewHolder.btnLiked.setColorFilter(Color.rgb(127, 127, 127));}
        if (currentComic.isVisited()){
            comicRowViewHolder.btnVisited.setColorFilter(Color.WHITE);
        }else {comicRowViewHolder.btnVisited.setColorFilter(Color.rgb(127, 127, 127));}
    }

    @Override
    public int getItemCount() {
        return comics.size();
    }
}
