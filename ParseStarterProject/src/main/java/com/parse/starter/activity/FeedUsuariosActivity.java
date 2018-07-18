package com.parse.starter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.starter.R;
import com.parse.starter.adapter.HomeAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class FeedUsuariosActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listView;
    private String username; //username do usuario clicado
    private ArrayAdapter<ParseObject> adapter;
    private ArrayList<ParseObject> postagens;
    private ParseQuery<ParseObject> query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_usuarios);

        //Recupera dados enviados da intent
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        postagens = new ArrayList<>();

        toolbar = findViewById(R.id.toolbar_feed_usuario);
        toolbar.setTitle(username);
        toolbar.setTitleTextColor(getResources().getColor(R.color.preto_preto));
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left);
        setSupportActionBar(toolbar);

        //configura listview e adapter
        listView = findViewById(R.id.list_feed_usuario);
        adapter = new HomeAdapter(getApplicationContext(),postagens);
        listView.setAdapter(adapter);

        //recupera postagens
        getPostagens();

    }

    private void getPostagens() {
        query = ParseQuery.getQuery("Imagem");
        query.whereEqualTo("username", username);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    if(objects.size()>0){
                        postagens.clear();
                        for (ParseObject parseObject : objects){
                            postagens.add(parseObject);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }else{

                }
            }
        });
    }

}
