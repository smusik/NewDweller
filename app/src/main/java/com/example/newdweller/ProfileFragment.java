package com.example.newdweller;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfileFragment extends Fragment {
    Button btnLogOut;
    TextView fullName,email,address,status,order,id;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userID;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);

    }
    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        btnLogOut = view.findViewById(R.id.btnLogout);
        fullName = view.findViewById(R.id.profileName);
        email = view.findViewById(R.id.profileEmail);
        address = view.findViewById(R.id.profileAddress);
        status = view.findViewById(R.id.text_profileStatus);
        order = view.findViewById(R.id.text_profileOrder);
        id = view.findViewById(R.id.idReq);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = mAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                email.setText(documentSnapshot.getString("email"));
                fullName.setText(documentSnapshot.getString("fullname"));
                address.setText(documentSnapshot.getString("address"));
            }
        });

        DocumentReference docR = fStore.collection("requests").document(userID);
        docR.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot collR, @Nullable FirebaseFirestoreException error) {
                status.setText(collR.getString("status"));
                order.setText(collR.getString("order"));
                id.setText(collR.getLong("id_req").toString());
            }
        });


        //кнопка выхода;
        btnLogOut.setOnClickListener(view1 -> {
            mAuth.signOut();
            startActivity(new Intent(getActivity(),RegisterActivity.class));
        });
//Разлогиниться в firebase
        /*  btnLogOut = findViewById(R.id.btnLogout);

        btnLogOut.setOnClickListener(view ->{
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });*/


    }
}