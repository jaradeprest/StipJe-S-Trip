package chiel.jara.stipjestrip.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import chiel.jara.stipjestrip.R;
import chiel.jara.stipjestrip.model.Comic;

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ComicRowViewHolder> {

    class ComicRowViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName, tvAuthor, tvYear;
        private ImageView ivComic;


        public ComicRowViewHolder (@NonNull View itemView){
            super(itemView);
            tvName=itemView.findViewById(R.id.tv_row_name);
            tvAuthor=itemView.findViewById(R.id.tv_row_author);
            tvYear=itemView.findViewById(R.id.tv_row_year);
            ivComic=itemView.findViewById(R.id.iv_row_image);
        }
    }

    private List<Comic> comics;

    public ComicAdapter(List<Comic> comics){
        this.comics = comics;
    }

    @NonNull
    @Override
    public ComicRowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comic_row, viewGroup, false);
        ComicRowViewHolder comicRowViewHolder = new ComicRowViewHolder(row);
        return comicRowViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ComicRowViewHolder comicRowViewHolder, int i) {
        Comic currentComic = comics.get(i);
        //instellen op viewholder
        comicRowViewHolder.tvName.setText(currentComic.getName());
        comicRowViewHolder.tvAuthor.setText(currentComic.getAuthor());
        comicRowViewHolder.tvYear.setText(currentComic.getYear());
        try {
            FileInputStream fis = comicRowViewHolder.itemView.getContext().openFileInput(currentComic.getImgID());
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            comicRowViewHolder.ivComic.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return comics.size();
    }
}
