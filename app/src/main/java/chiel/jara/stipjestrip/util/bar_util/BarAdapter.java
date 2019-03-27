package chiel.jara.stipjestrip.util.bar_util;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import chiel.jara.stipjestrip.R;
import chiel.jara.stipjestrip.model.bar_model.Bar;
import chiel.jara.stipjestrip.model.comic_model.Comic;

public class BarAdapter extends RecyclerView.Adapter<BarAdapter.BarRowViewHolder> {

    public class BarRowViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName, tvAdress, tvPhone, tvWebsite, tvDescription;

        public BarRowViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tv_name_bar);
            tvAdress=itemView.findViewById(R.id.tv_adress_bar);
            tvPhone=itemView.findViewById(R.id.tv_phone_bar);
            tvWebsite=itemView.findViewById(R.id.tv_website_bar);
            tvDescription=itemView.findViewById(R.id.tv_description_bar);
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
    public void onBindViewHolder(@NonNull BarRowViewHolder barRowViewHolder, final int i) {
        final Bar currentBar = filteredBars.get(i);
        barRowViewHolder.tvName.setText(currentBar.getName());
        barRowViewHolder.tvAdress.setText(currentBar.getStreet()+" "+currentBar.getHouseNumber()+", "+currentBar.getPostalcode()+" "+currentBar.getCity());
        barRowViewHolder.tvPhone.setText(currentBar.getPhone());
        barRowViewHolder.tvWebsite.setText(currentBar.getWebsite());
        barRowViewHolder.tvDescription.setText(currentBar.getDescription());
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
