package com.example.test2.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test2.Database.Tables.Mesec;
import com.example.test2.R;

import java.util.ArrayList;

public class OverviewAdapter extends RecyclerView.Adapter<OverviewAdapter.OverviewViewHolder>{

    private ArrayList<Mesec> meseci;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public static class OverviewViewHolder extends RecyclerView.ViewHolder{
        public TextView mesec;
        public TextView stroski;
        public ImageView delete;

        public OverviewViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            mesec = itemView.findViewById(R.id.pregled_recycler_mesec);
            stroski = itemView.findViewById(R.id.pregled_recycler_strosek);
            delete = itemView.findViewById(R.id.pregled_recycler_delete);

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

    public OverviewAdapter(ArrayList<Mesec> meseci){
        this.meseci = meseci;
    }

    @NonNull
    @Override
    public OverviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pregled_recycler_item, parent, false);
        OverviewViewHolder hvh = new OverviewViewHolder(v, listener);
        return hvh;
    }

    @Override
    public void onBindViewHolder(@NonNull OverviewViewHolder holder, int position) {
        Mesec m = meseci.get(position);

        holder.mesec.setText(m.getDisplayDate());
        holder.stroski.setText(m.getStroski()+"€");
    }

    @Override
    public int getItemCount() {
        return meseci.size();
    }
}
