package com.example.newdweller;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText etLoginEmail, etLoginPassword;
    TextView tvRegisterHere;
    Button btnLogin;
    //ProgressBar progressBar;
    FirebaseAuth mAuth;
    private static final Pattern EMAIL_ADDRESS =
            Pattern.compile(
                    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}"+
                            "\\@"+
                            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}"+
                            "("+ "\\."+
                            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}"+
                            ")+"
            );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPassword = findViewById(R.id.etLoginPass);
        tvRegisterHere = findViewById(R.id.tvRegisterHere);
        btnLogin = findViewById(R.id.btnLogin);
        //progressBar=findViewById(R.id.progressBar2);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null){ //добавлено 05.02.23 1:55
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
        btnLogin.setOnClickListener(view -> {
            loginUser();
            //progressBar.setVisibility(View.VISIBLE);
        });
        tvRegisterHere.setOnClickListener(view ->{
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void loginUser(){
        String email = etLoginEmail.getText().toString();
        String password = etLoginPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            etLoginEmail.setError("Email не может быть пустым");
            etLoginEmail.requestFocus();
        }else if (!EMAIL_ADDRESS.matcher(email).matches()) {
            etLoginEmail.setError("Введите действительный Email");
            etLoginEmail.requestFocus();}
        else if (TextUtils.isEmpty(password)){
            etLoginPassword.setError("Пароль не может быть пустым");
            etLoginPassword.requestFocus();
        }else{
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Успешный вход", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }else{
                        Toast.makeText(LoginActivity.this, "Ошибка входа: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        //progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

}
