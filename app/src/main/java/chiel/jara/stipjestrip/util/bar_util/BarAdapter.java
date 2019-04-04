package chiel.jara.stipjestrip.util.bar_util;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import chiel.jara.stipjestrip.MapActivity;
import chiel.jara.stipjestrip.R;
import chiel.jara.stipjestrip.model.bar_model.Bar;
/**
 * Created By Chiel&Jara 03/2019
 */
public class BarAdapter extends RecyclerView.Adapter<BarAdapter.BarRowViewHolder> {

    public class BarRowViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName, tvAdress, tvPhone, tvWebsite, tvDescription;
        private ImageButton btnMap;
        private Button btnRating;

        private View.OnClickListener toMapListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, MapActivity.class);
                int position = getAdapterPosition();
                Bar barToSee = bars.get(position);
                intent.putExtra("chosenBar", barToSee);
                context.startActivity(intent);
            }
        };

        public BarRowViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tv_name_bar);
            tvAdress=itemView.findViewById(R.id.tv_adress_bar);
            tvPhone=itemView.findViewById(R.id.tv_phone_bar);
            tvWebsite=itemView.findViewById(R.id.tv_website_bar);
            tvDescription=itemView.findViewById(R.id.tv_description_bar);
            btnMap=itemView.findViewById(R.id.btn_barList_maps);
            btnMap.setOnClickListener(toMapListener);
            btnRating=itemView.findViewById(R.id.btn_barList_rating);
        }
    }

    private List<Bar> bars, filteredBars;
    public BarAdapter(List<Bar> bars) {
        this.bars = bars;
        this.filteredBars = bars;
    }

    @NonNull
    @Override
    public BarRowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bar_row, viewGroup, false);
        BarRowViewHolder barRowViewHolder = new BarRowViewHolder(row);
        return barRowViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final BarRowViewHolder barRowViewHolder,  int i) {
        Collections.sort(filteredBars, Bar.BY_NAME_ALPHABETICAL);
        Bar currentBar = filteredBars.get(i);
        barRowViewHolder.tvName.setText(currentBar.getName());
        barRowViewHolder.tvAdress.setText(currentBar.getStreet()+" "+currentBar.getHouseNumber()+", "+currentBar.getPostalcode()+" "+currentBar.getCity());
        barRowViewHolder.tvPhone.setText(currentBar.getPhone());
        barRowViewHolder.tvWebsite.setText(currentBar.getWebsite());
        barRowViewHolder.tvDescription.setText(currentBar.getDescription());
        //if currentBar = rated, show rating in button
        if (currentBar.isRated()){
            String rating = String.valueOf(currentBar.getRating())+"/10";
            barRowViewHolder.btnRating.setText(rating);
            barRowViewHolder.btnRating.setVisibility(View.VISIBLE);
        }else {
            barRowViewHolder.btnRating.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return filteredBars.size();
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String filterComic = (constraint.toString()).toLowerCase();

                if (filterComic.isEmpty()) {
                    filteredBars = bars;
                } else {
                    ArrayList<Bar> tempList = new ArrayList<>();
                    for (Bar bar : bars) {
                        if (bar.getName().toLowerCase().contains(filterComic)) {
                            tempList.add(bar);
                        }
                    }
                    filteredBars = tempList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredBars;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredBars = (ArrayList<Bar>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}

//DOCUMENTATION: how to sort list by a-z: https://www.youtube.com/watch?v=AREhvfVGxlo