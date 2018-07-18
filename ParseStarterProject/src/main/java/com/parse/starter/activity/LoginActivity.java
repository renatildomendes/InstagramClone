package com.parse.starter.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.starter.R;
import com.parse.starter.util.ParseErros;

public class LoginActivity extends AppCompatActivity {
    private EditText edit_login;
    private EditText edit_senha;
    private Button botao_logar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edit_login = findViewById(R.id.login_usuario);
        edit_senha = findViewById(R.id.login_senha);
        botao_logar = findViewById(R.id.login_botao_logar);

        //ParseUser.logOut();

        verificarSeUsuarioEstaLogado();

        botao_logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = edit_login.getText().toString();
                String senha = edit_senha.getText().toString();
                verificaLoginUsuario(usuario,senha);
            }
        });
    }

    private void verificaLoginUsuario(String usuario, String senha) {
        ParseUser.logInInBackground(usuario, senha, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e==null){
                    Toast.makeText(LoginActivity.this,"login realizado com sucesso",Toast.LENGTH_SHORT).show();
                    abrirTelaPrincipal();
                }else{
                    ParseErros parseErros = new ParseErros();
                    String erro = parseErros.getErro(e.getCode());
                    Toast.makeText(LoginActivity.this,"falha ao realizar login - "+erro,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void verificarSeUsuarioEstaLogado() {
        if(ParseUser.getCurrentUser()!=null){
            abrirTelaPrincipal();
        }else{
            Log.i("login Usuario","O usuario não está logado");
        }
    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void abrirCadastroUsuario(View view){
        Intent intent = new Intent(LoginActivity.this,CadastroActivity.class);
        startActivity(intent);
        finish();
    }

}
