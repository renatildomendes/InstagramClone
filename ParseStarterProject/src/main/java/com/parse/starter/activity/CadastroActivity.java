package com.parse.starter.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.starter.R;
import com.parse.starter.util.ParseErros;

public class CadastroActivity extends AppCompatActivity {

    private EditText nome;
    private EditText email;
    private EditText senha;
    private Button botao;
    private TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        nome = findViewById(R.id.cadastro_nome);
        email = findViewById(R.id.cadastro_email);
        senha = findViewById(R.id.cadastro_senha);
        botao = findViewById(R.id.cadastro_botao);
        //login = findViewById(R.id.cadastro_login);
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarUsuario();
            }
        });

    }

    private void cadastrarUsuario() {
        ParseUser usuario = new ParseUser();
        usuario.setUsername(nome.getText().toString());
        usuario.setEmail(email.getText().toString());
        usuario.setPassword(senha.getText().toString());
        usuario.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    Toast.makeText(CadastroActivity.this,"Sucesso ao cadastrar usuário",Toast.LENGTH_SHORT).show();
                    abrirLoginUsuario();
                }else{
                    ParseErros parseErros = new ParseErros();
                    String erro = parseErros.getErro(e.getCode());
                    Log.i("CadastroUsuario","Erro: "+e.getCode()+" - "+e.getMessage());
                    Toast.makeText(CadastroActivity.this,"Erro ao cadastrar usuário, "+erro,Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void abrirLoginUsuario() {
        Intent intent = new Intent(CadastroActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void abrirLoginUsuario(View view) {
        Intent intent = new Intent(CadastroActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}
