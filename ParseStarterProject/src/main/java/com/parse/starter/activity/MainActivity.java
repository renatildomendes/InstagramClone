/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.parse.starter.Fragment.HomeFragment;
import com.parse.starter.R;
import com.parse.starter.adapter.TabAdapter;
import com.parse.starter.helper.SlidingTabLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbarPrincipal;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    toolbarPrincipal = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_principal);
    toolbarPrincipal.setLogo(R.drawable.instagramlogo);
    setSupportActionBar(toolbarPrincipal);

    slidingTabLayout = (SlidingTabLayout) findViewById(R.id.main_sliding_tab);
    viewPager = (ViewPager) findViewById(R.id.main_view_pager);

      TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager(),this);
      viewPager.setAdapter(tabAdapter);
      slidingTabLayout.setCustomTabView(R.layout.tab_view, R.id.text_item_tab);
      slidingTabLayout.setDistributeEvenly(true);
      slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this,R.color.vermelho_vermelho));
      slidingTabLayout.setViewPager(viewPager);

  }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_sair:
                deslogar();
            case R.id.action_compartilhar:
                pedirPermissaoDeLeituraDeArquivos();
                compartilharFoto();
            case R.id.action_configuracoes:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void compartilharFoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data != null){
            //recuperar local do recurso
            Uri localImagemSelecionada = data.getData();

            try {
                //recupera a imagem do local que foi selecionada
                Bitmap imagem = MediaStore.Images.Media.getBitmap(getContentResolver(),localImagemSelecionada);

                //comprimir no formato PNG
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imagem.compress(Bitmap.CompressFormat.PNG,75,stream);

                //cria um array de bytes da imagem
                byte[] byteArray = stream.toByteArray();

                //criar nome único da imagem baseado na hora
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                String nomeImagem = simpleDateFormat.format(new Date());

                //cria um arquivo com o formato do próprio parse
                ParseFile parseFile =new ParseFile(nomeImagem+".png",byteArray);

                //monta objeto para salvar no parse
                ParseObject parseObject = new ParseObject("Imagem");
                parseObject.put("username",ParseUser.getCurrentUser().getUsername());//OBS: usar objectId no lugar de username
                parseObject.put("imagem",parseFile);

                //salvar dados
                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            Toast.makeText(getApplicationContext(),"Imagem postada com sucesso.",Toast.LENGTH_SHORT).show();
                            TabAdapter adapterNovo = (TabAdapter) viewPager.getAdapter();
                            HomeFragment homeFragmentNovo = (HomeFragment) adapterNovo.getFragment(0);
                            homeFragmentNovo.atualizaPostagens();
                        }else{
                            Toast.makeText(getApplicationContext(),"Erro ao postar sua imagem - Tente novamente.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void pedirPermissaoDeLeituraDeArquivos(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        2);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    public void logarUsuario(){
      ParseUser.logInInBackground("renato", "teste123", new LogInCallback() {
          @Override
          public void done(ParseUser user, ParseException e) {
              if(e==null){
                  Log.i("loginUsuario","Sucesso ao logar usuário");
              }else{
                  Log.i("loginUsuario","Erro ao logar usuário - "+e.getMessage());
              }
          }
      });
  }

  public void deslogar(){
      ParseUser.logOutInBackground(new LogOutCallback() {
          @Override
          public void done(ParseException e) {
              if(e==null){
                  Log.i("EncerrarSessãoUsuario","Sucesso ao encerrar sessão do usuário");
                  Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                  startActivity(intent);
                  finish();
              }else{
                  Log.i("EncerrarSessãoUsuario","Erro ao encerrar sessão do usuário - "+e.getMessage());
              }
          }
      });
  }


}
