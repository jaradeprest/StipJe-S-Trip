package chiel.jara.stipjestrip.util;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import chiel.jara.stipjestrip.R;
import chiel.jara.stipjestrip.model.Comic;

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ComicRowViewHolder> {

    public void setItems(ArrayList<Comic> comics){
        comics.addAll(comics);
    }

    class ComicRowViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName, tvAuthor, tvYear;

        public ComicRowViewHolder (@NonNull View itemView){
            super(itemView);
            tvName=itemView.findViewById(R.id.tv_row_name);
            tvAuthor=itemView.findViewById(R.id.tv_row_author);
            tvYear=itemView.findViewById(R.id.tv_row_year);
            //IMAGE ???
        }
    }

    private ArrayList<Comic> comics;

    public ComicAdapter(ArrayList<Comic> comics){this.comics = comics;}

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
    }

    @Override
    public int getItemCount() {
        return comics.size();
    }
}
