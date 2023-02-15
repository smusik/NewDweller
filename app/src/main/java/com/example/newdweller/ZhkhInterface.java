package com.example.newdweller;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ZhkhInterface extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interface_zhkh);

        String text_for_recyclerViews = getIntent().getStringExtra("TEXT_FOR_RECYCLERNAME");
        String big_description = getIntent().getStringExtra("BIG_DESCRIPTION");
        String cost_v = getIntent().getStringExtra("COST_V");

        TextView nameTextView = findViewById(R.id.text_title);
        TextView descriptionBig = findViewById(R.id.text_desc);
        TextView costText = findViewById(R.id.text_cost);


        nameTextView.setText(text_for_recyclerViews);
        descriptionBig.setText(big_description);
        costText.setText(cost_v);

    }


}
