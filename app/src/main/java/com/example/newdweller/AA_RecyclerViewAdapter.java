package com.example.newdweller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class AA_RecyclerViewAdapter extends RecyclerView.Adapter<AA_RecyclerViewAdapter.MyViewHolder> {
    private final RecylerViewInterface recylerViewInterface;

    Context context;
    ArrayList<AcidModel>acidModels;

    public AA_RecyclerViewAdapter(Context context, ArrayList<AcidModel>acidModels,
                                  RecylerViewInterface recylerViewInterface){
        this.context = context;
        this.acidModels = acidModels;
        this.recylerViewInterface = recylerViewInterface;
    }
    @NonNull
    @Override
    public AA_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // смотреть колонки
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row,parent,false);
        return new AA_RecyclerViewAdapter.MyViewHolder(view,recylerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull AA_RecyclerViewAdapter.MyViewHolder holder, int position) {
        //позиция recycleview
        holder.text_el.setText(acidModels.get(position).getAcidNames());
        holder.text_el_d.setText(acidModels.get(position).getAcidDesc());
        holder.imageView.setImageResource(acidModels.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        //знать число
        return acidModels.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView text_el,text_el_d;

        public MyViewHolder(@NonNull View itemView, RecylerViewInterface recylerViewInterface){
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView2);
            text_el = itemView.findViewById(R.id.text_electro);
            text_el_d = itemView.findViewById(R.id.text_electro_desc);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    if(recylerViewInterface!=null){
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            recylerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });

        }
    }
}
