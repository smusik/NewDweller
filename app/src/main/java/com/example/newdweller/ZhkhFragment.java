package com.example.newdweller;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.RecoverySystem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class ZhkhFragment extends Fragment implements RecylerViewInterface{
    //new
    ArrayList<AcidModel> acidModels = new ArrayList<>();
    int[]acidImages ={R.drawable.electric_bolt_24,R.drawable.water_hot,
            R.drawable.water_cold,R.drawable.water,R.drawable.kap_remont,R.drawable.domofon};//измененено 06.02.23 0:55
    //
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_zhkh, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.mRecyclerView);

        //acid
        setAcidModels();
         //
        AA_RecyclerViewAdapter adapter = new AA_RecyclerViewAdapter(getActivity(), acidModels,this);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // Inflate the layout for this fragment
        return v;

    }
    //new
    private void setAcidModels(){
        String[] acidNames = getResources().getStringArray(R.array.text_for_recyclerViews);
        String[] acidDesc = getResources().getStringArray(R.array.description);
        String[] acidBDesc = getResources().getStringArray(R.array.big_desctiption);
        String[] acidCost = getResources().getStringArray(R.array.cost_v);

        for (int i=0;i<acidNames.length;i++){
            acidModels.add(new AcidModel(acidNames[i], acidDesc[i], acidImages[i],acidBDesc[i],acidCost[i]));
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(ZhkhFragment.this.getActivity(), ZhkhInterface.class);
        intent.putExtra("TEXT_FOR_RECYCLERNAME",acidModels.get(position).getAcidNames());
        intent.putExtra("BIG_DESCRIPTION",acidModels.get(position).getAcidBDesc());
        intent.putExtra("COST_V",acidModels.get(position).getAcidCost());
        startActivity(intent);
    }
    //new
}