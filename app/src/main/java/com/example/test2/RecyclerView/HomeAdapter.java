package com.example.test2.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test2.R;
import com.example.test2.Toolbox.Racun;

import java.text.DateFormat;
import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private ArrayList<Racun> racuni;
    private OnItemClickListener listener;

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

    public HomeAdapter(ArrayList<Racun> racuni){
        this.racuni = racuni;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recycler_item, parent, false);
        HomeViewHolder hvh = new HomeViewHolder(v, listener);
        return hvh;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Racun r = racuni.get(position);

        holder.znesekRacuna.setText(String.format("%.2f", r.Znesek) +"€");
        holder.imeRacuna.setText("RAČUN "+ (position+1) +": "+ r.Trgovina.Ime);
        holder.datumRacuna.setText(r.getDate());
    }

    @Override
    public int getItemCount() {
        return racuni.size();
    }
}
