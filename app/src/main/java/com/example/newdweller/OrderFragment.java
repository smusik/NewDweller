package com.example.newdweller;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import kotlin.random.URandomKt;


public class OrderFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    Spinner spinner;

    FirebaseAuth mAuth;

    FirebaseFirestore fStore;
    String userID;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    Button createBtn;
    EditText address, comment,dateBtn,timeBtn;
    int hour,minute;
    ProgressBar progressBar;
    String date;
    TextView text_req,text_req2,text_req3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        timeBtn =view.findViewById(R.id.etTime);
        dateBtn = view.findViewById(R.id.etDate);
        //dateBtn.setText(getTodayDate());
        createBtn = view.findViewById(R.id.btnCreateO);
        spinner =  view.findViewById(R.id.spinner);
        address=view.findViewById(R.id.putAddress);
        comment=view.findViewById(R.id.etComment);
        progressBar= view.findViewById(R.id.progressBarO);
        text_req = view.findViewById(R.id.text_req);
        text_req2 = view.findViewById(R.id.text_req2);
        text_req3 = view.findViewById(R.id.text_req3);
        //поместим адрес пользователя в поле putAddress
        userID = mAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                address.setText(documentSnapshot.getString("address"));
            }
        });
        //наполнение спинера

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.order_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //дата
        initDatePicker();

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        //время
        popTimePicker();

        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               timePickerDialog.show();
            }
        });
        makeRequest();
    }

    /*private String getTodayDate() {
        final Calendar cal =Calendar.getInstance();
        int year =cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month=month+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day,month,year);

    }*/
//клик по дате
    public void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month+1;
                date = makeDateString(day,month,year);
                dateBtn.setText(date);
            }
        };
        final Calendar cal = Calendar.getInstance();
        int year =cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
            cal.add(Calendar.DAY_OF_MONTH,14);

        datePickerDialog = new DatePickerDialog(getContext(),dateSetListener,year,month,day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
        datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());

    }
    //ФОРМАТ ВЫВОДА ДАТЫ
    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " +year;
    }
    //ДАТА
    private String getMonthFormat(int month) {
        if(month==1)
            return "ЯНВ";
        if(month==2)
            return "ФЕВ";
        if(month==3)
            return "МАРТ";
        if(month==4)
            return "АПР";
        if(month==5)
            return "МАЙ";
        if(month==6)
            return "ИЮНЬ";
        if(month==7)
            return "ИЮЛЬ";
        if(month==8)
            return "АВГ";
        if(month==9)
            return "СЕНТ";
        if(month==10)
            return "ОКТ";
        if(month==11)
            return "НОЯ";
        if(month==12)
            return "ДЕК";

        return "ЯНВ";
    }
    //время
    private void popTimePicker() {
        TimePickerDialog.OnTimeSetListener onTimeSetListener  = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int sHour, int sMinute) {
                hour=sHour;
                minute=sMinute;
                timeBtn.setText(String.format(Locale.getDefault(),"%02d:%02d",hour,minute));
            }
        };
        timePickerDialog =new TimePickerDialog(getContext(),onTimeSetListener,hour,minute,true);

    }

    private void makeRequest() {

        String comm = comment.getText().toString();
        String spin= spinner.getSelectedItem().toString();

            createBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //text_req.setVisibility(view.VISIBLE);
                    //text_req2.setVisibility(view.VISIBLE);
                    //text_req3.setVisibility(view.VISIBLE);
                    createBtn.setVisibility(view.INVISIBLE);
                    progressBar.setVisibility(view.VISIBLE);
                    if ( date!=null && timePickerDialog!=null && comm.length()<100){
                        DocumentReference req = fStore.collection("requests").document(userID);
                        Map<String,Object> request = new HashMap<>();
                        request.put("id_req",getId());
                        request.put("order",spin);
                        request.put("date",date);
                        request.put("hour",hour);
                        request.put("minute",minute);
                        request.put("comment",comm);
                        request.put("status","В обработке");
                        req.set(request).addOnSuccessListener((OnSuccessListener)(aVoid)->{
                        Log.d(TAG,"Статус заявки отображается в профиле" +userID);
                        Toast.makeText(getActivity(), "Заявка создана, ее статус отображается в профиле", Toast.LENGTH_SHORT).show();
                        createBtn.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        //text_req.setVisibility(view.VISIBLE);
                        //text_req2.setVisibility(view.VISIBLE);
                        //text_req3.setVisibility(view.VISIBLE);
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG,"Неудачно: "+ e.toString());
                        }
                    });
                }else{
                        showMessage("Пожалуйста, выберите дату и время");
                        createBtn.setVisibility(view.VISIBLE);
                        progressBar.setVisibility(view.INVISIBLE);
                        //text_req.setVisibility(view.INVISIBLE);
                        //text_req2.setVisibility(view.INVISIBLE);
                        //text_req3.setVisibility(view.INVISIBLE);
            }
        }
        });


    }
    //спинер,заполнение позициями из стринг
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String choice = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    private void showMessage(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();

    }


}