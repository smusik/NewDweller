package com.example.newdweller;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText etFullname,etAdress,etRegEmail,etRegPassword;
    TextView tvLoginHere;
    Button btnRegister;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userID;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[@#$%^&+=])" +
                    "(?=\\S+$)" +
                    ".{6,}" +
                    "$");
    private static final Pattern EMAIL_ADDRESS =
            Pattern.compile(
                    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}"+
                            "\\@"+
                            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}"+
                            "("+ "\\."+
                            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}"+
                            ")+"
            );
    private static final Pattern FULLNAME =
            Pattern.compile(
                    "([А-ЯЁ][а-яё]+[\\-\\s]?){3,}"
                    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        etFullname=findViewById(R.id.etFullname);//полное имя
        etAdress=findViewById(R.id.etAdress);
        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPassword = findViewById(R.id.etRegPass);
        tvLoginHere = findViewById(R.id.tvLoginHere);// перейти на поле входа
        btnRegister = findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        //FirebaseUser user = mAuth.getCurrentUser();
        progressBar =findViewById(R.id.progressBar);

        if(mAuth.getCurrentUser()!=null){ //добавлено 05.02.23 1:55
            startActivity(new Intent(RegisterActivity.this,MainActivity.class));
            finish();
        }
        btnRegister.setOnClickListener(view ->{
            createUser();
        });

        tvLoginHere.setOnClickListener(view ->{
            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
        });
    }

    private void createUser() {
        String email = etRegEmail.getText().toString();
        String password = etRegPassword.getText().toString();
        String fullname = etFullname.getText().toString();
        String address = etAdress.getText().toString();

        if (TextUtils.isEmpty(fullname)) {
            etFullname.setError("Обязательное поле");
            etFullname.requestFocus();
        } else if (!FULLNAME.matcher(fullname).matches()){
            etFullname.setError("Пример ввода: Иванов Иван Иванович");
            etFullname.requestFocus();
        }else if (TextUtils.isEmpty(address)) {
            etAdress.setError("Обязательное поле");
            etAdress.requestFocus();
        } else if (TextUtils.isEmpty(email)) {
            etRegEmail.setError("Email не может быть пустым");
            etRegEmail.requestFocus();
        } else if (!EMAIL_ADDRESS.matcher(email).matches()) {
            etRegEmail.setError("Введите действительный Email");
            etRegEmail.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            etRegPassword.setError("Пароль не может быть пустым");
            etRegPassword.requestFocus();
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            etRegPassword.setError("Пароль слишком легкий");
            etRegPassword.requestFocus();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Пользователь зарегестрирован", Toast.LENGTH_SHORT).show();
                    userID = mAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fStore.collection("users").document(userID);

                    Map<String, Object> user = new HashMap<>();
                    user.put("fullname", fullname);
                    user.put("address", address);
                    user.put("email", email);
                    user.put("password",password);
                    documentReference.set(user).addOnSuccessListener((OnSuccessListener) (aVoid) -> {
                        Log.d(TAG, "Успех:Профиль содан " + userID);
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Неудачно: " + e);
                        }
                    });
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    Toast.makeText(RegisterActivity.this, "Ошибка регистрации: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }
}




