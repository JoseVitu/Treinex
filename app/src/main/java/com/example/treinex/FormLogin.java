package com.example.treinex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.ktx.Firebase;

public class FormLogin extends AppCompatActivity {

    private TextView forgot_pass;
    private EditText campo_login, campo_senha;
    private ProgressBar progress_bar;
    private Button bt_entrar;
    String[] mensagens = {"Preencha todos so campos!", "Autenticação realizada!"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);
        iniciarComponentes();
        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder esqueceuSenha = new AlertDialog.Builder(FormLogin.this);
                esqueceuSenha.setMessage("Entre em contato com jose.sito@cvale.com.br para alterar sua senha");
                esqueceuSenha.setTitle("Treinex");
                esqueceuSenha.create().show();
            }
        });
        bt_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = campo_login.getText().toString();
                String senha = campo_senha.getText().toString();

                if (email.isEmpty() || senha.isEmpty()){
                    Snackbar snackbar = Snackbar.make(view, mensagens[0],Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }else{
                    AutenticarUsuario();
                }

            }
        });
    }
    private void AutenticarUsuario(){
        String email = campo_login.getText().toString();
        String senha = campo_senha.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progress_bar.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run(){
                            TelaPrincipal();

                        }
                    }, 3000);

                }
            }
        });
    }
    private void TelaPrincipal(){
        Intent intent = new Intent(FormLogin.this, TelaPrincipal.class);
        startActivity(intent);
        finish();
    }
    private void iniciarComponentes() {
        forgot_pass = findViewById(R.id.forgot_pass);
        campo_login = findViewById(R.id.campo_login);
        campo_senha = findViewById(R.id.campo_senha);
        bt_entrar = findViewById(R.id.bt_entrar);
        progress_bar = findViewById(R.id.progress_bar);
    }
}