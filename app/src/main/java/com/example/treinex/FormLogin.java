package com.example.treinex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
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
                alertRedefinirSenha();
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
                    AutenticarUsuario(view);
                }

            }
        });
    }
    private void AutenticarUsuario(View view){
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

                }else{

                    String erro;

                    try {

                        throw task.getException();

                    }catch(Exception e){
                        erro = "E-mail ou senha incorretos! Certifique-se de estar digitando corretamente.";

                    }
                    Snackbar snackbar = Snackbar.make(view, erro,Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();

        if(usuarioAtual != null){
            TelaPrincipal();
        }

    }

    private void TelaPrincipal(){
        Intent intent = new Intent(FormLogin.this, TelaPrincipal.class);
        startActivity(intent);
        finish();

    }


    //problema nao solucionado
    private void alertRedefinirSenha(){

            AlertDialog.Builder redefinirSenha = new AlertDialog.Builder(this);
            redefinirSenha.setTitle("Redefinir senha");
            redefinirSenha.setMessage("Insira seu endereço de e-mail: ");
            redefinirSenha.setCancelable(false);
            EditText campoDigitarEmail = new EditText(this);
            LinearLayout.LayoutParams linearLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            redefinirSenha.setView(campoDigitarEmail);
            campoDigitarEmail.setLayoutParams(linearLayout);
            redefinirSenha.setNegativeButton("Cancelar", null);
            redefinirSenha.setPositiveButton("Recuperar senha", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(campoDigitarEmail.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(getBaseContext(), "E-mail enviado", Toast.LENGTH_LONG).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getBaseContext(), "E-mail errado", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
            redefinirSenha.create().show();


    }





    private void iniciarComponentes() {
        forgot_pass = findViewById(R.id.forgot_pass);
        campo_login = findViewById(R.id.campo_login);
        campo_senha = findViewById(R.id.campo_senha);
        bt_entrar = findViewById(R.id.bt_entrar);
        progress_bar = findViewById(R.id.progress_bar);
    }


}