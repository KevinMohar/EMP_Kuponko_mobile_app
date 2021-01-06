package com.example.test2.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test2.Database.Tables.Racun;
import com.example.test2.Database.Tables.Trgovina;
import com.example.test2.Database.ViewModels.KuponkoViewModel;
import com.example.test2.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private List<Racun> racuni = new ArrayList<>();
    private OnItemClickListener listener;
    private KuponkoViewModel viewModel;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder{
        public TextView imeRacuna;
        public TextView datumRacuna;
        public TextView znesekRacuna;
        public ImageView delete;

        public HomeViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            imeRacuna = itemView.findViewById(R.id.home_recycler_item_imeRacuna);
            datumRacuna = itemView.findViewById(R.id.home_recycler_item_datumRacuna);
            znesekRacuna = itemView.findViewById(R.id.home_recycler_item_znesekRacuna);
            delete = itemView.findViewById(R.id.home_recycler_item_delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });

        }
    }

    public void setRacuni(List<Racun> racuni){
        this.racuni = racuni;
    }

    public void setViewModel(KuponkoViewModel model){this.viewModel = model;}

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_recycler_item, parent, false);
        HomeViewHolder hvh = new HomeViewHolder(v, listener);
        return hvh;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Racun r = racuni.get(position);
        Trgovina t = viewModel.getTrgovinaById(r.getIdTrgovine());

        holder.znesekRacuna.setText(String.format("%.2f", r.getZnesek()) +"€");
        holder.imeRacuna.setText("RAČUN "+ (position+1) +": "+ t.getIme());
        // TODO: formatiraj datum v lepso obliko
        holder.datumRacuna.setText(new SimpleDateFormat("dd.mm.yyyy hh:mm:ss").format(r.getDatum()));
    }

    @Override
    public int getItemCount() {
        return racuni.size();
    }
}
