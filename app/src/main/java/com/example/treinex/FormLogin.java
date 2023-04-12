package com.example.treinex;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
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
    private void alertRedefinirSenha() {
        AlertDialog.Builder redefinirSenha = new AlertDialog.Builder(this);
        redefinirSenha.setTitle("Redefinir senha");
        redefinirSenha.setMessage("Insira seu endereço de e-mail: ");

        // Define o fundo da caixa de diálogo como azul (#21409a)
        redefinirSenha.setIcon(R.drawable.ic_alert);
        redefinirSenha.setCancelable(false);

        // Cria o campo para o usuário digitar o e-mail
        EditText campoDigitarEmail = new EditText(this);
        campoDigitarEmail.setHint("Digite seu e-mail");
        campoDigitarEmail.setTextColor(Color.WHITE);

        // Define a cor de fundo do campo como azul (#21409a)
        campoDigitarEmail.setBackgroundColor(Color.parseColor("#21409a"));

        // Define a cor do cursor e das barras de seleção do campo como branco (#FFFFFF)
        campoDigitarEmail.setHighlightColor(Color.WHITE);
        campoDigitarEmail.setHintTextColor(Color.WHITE);

        // Define as margens e o padding do campo
        LinearLayout.LayoutParams linearLayout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setMargins(24, 24, 24, 0);
        campoDigitarEmail.setPadding(16, 16, 16, 16);
        campoDigitarEmail.setLayoutParams(linearLayout);

        // Define o campo de texto na caixa de diálogo
        redefinirSenha.setView(campoDigitarEmail);

        // Define o botão de cancelar
        redefinirSenha.setNegativeButton("Cancelar", null);

        // Define o botão de recuperar senha
        redefinirSenha.setPositiveButton("Recuperar senha", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(campoDigitarEmail.getText().toString().trim())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
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

        // Cria e mostra a caixa de diálogo
        AlertDialog alertDialog = redefinirSenha.create();
        alertDialog.show();

        // Define a cor do texto dos botões como branco (#FFFFFF)
        Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setTextColor(Color.parseColor("#21409a"));
        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(Color.parseColor("#21409a"));
    }





    private void iniciarComponentes() {
        forgot_pass = findViewById(R.id.forgot_pass);
        campo_login = findViewById(R.id.campo_login);
        campo_senha = findViewById(R.id.campo_senha);
        bt_entrar = findViewById(R.id.bt_entrar);
        progress_bar = findViewById(R.id.progress_bar);
    }


}