package com.example.test2.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test2.Izdelek;
import com.example.test2.R;

import java.util.ArrayList;

public class RacunOverviewAdapter extends RecyclerView.Adapter<RacunOverviewAdapter.OverviewRacunViewHolder>{

    private ArrayList<Izdelek> izdelki;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onCardClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public static class OverviewRacunViewHolder extends RecyclerView.ViewHolder{

        TextView izdelek;
        TextView kolicina;
        TextView znesekKos;
        TextView znesekSkupaj;
        ImageView delete;
        ConstraintLayout card;

        public OverviewRacunViewHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);
            izdelek = itemView.findViewById(R.id.racun_overview_recycler_item_izdelek);
            kolicina = itemView.findViewById(R.id.racun_overview_recycler_item_kolicina);
            znesekKos = itemView.findViewById(R.id.racun_overview_recycler_item_znesek_kos);
            znesekSkupaj = itemView.findViewById(R.id.racun_overview_recycler_item_znesek_skupaj);
            delete = itemView.findViewById(R.id.racun_overview_recycler_item_delete);
            card = itemView.findViewById(R.id.racun_overview_recycler_item_card);

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onCardClick(position);
                    }
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onDeleteClick(position);
                    }
                }
            });
        }
    }

    public void setIzdelki(ArrayList<Izdelek> izdelkiA){
        this.izdelki = izdelkiA;
    }

    @NonNull
    @Override
    public OverviewRacunViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.racun_overview_recycler_item, parent, false);
        OverviewRacunViewHolder hvh = new OverviewRacunViewHolder(v, listener);
        return hvh;
    }

    @Override
    public void onBindViewHolder(@NonNull OverviewRacunViewHolder holder, int position) {
        Izdelek i = izdelki.get(position);
        holder.izdelek.setText(i.ime);
        holder.znesekKos.setText(i.cena+"€");
        holder.znesekSkupaj.setText((i.cena*i.kolicina)+"€");
        holder.kolicina.setText("x"+i.kolicina);
    }

    @Override
    public int getItemCount() {
        return izdelki.size();
    }
}
