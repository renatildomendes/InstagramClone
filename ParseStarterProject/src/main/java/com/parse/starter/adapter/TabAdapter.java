package com.parse.starter.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.ViewGroup;

import com.parse.starter.Fragment.HomeFragment;
import com.parse.starter.Fragment.UsuariosFragment;
import com.parse.starter.R;

import java.util.HashMap;

/**
 * Created by Renato on 16/07/2018.
 */

public class TabAdapter extends FragmentStatePagerAdapter{

    private Context context;
    private String[] abas = new String[] {"HOME","USUARIOS"};
    private int[] icones = new int[]{R.drawable.ic_action_home,R.drawable.ic_group};
    private int tamanhoIcone;
    private HashMap<Integer,Fragment> fragmentosUtilizados;

    public TabAdapter(FragmentManager fm, Context c) {
        super(fm);
        this.context = c;
        this.fragmentosUtilizados = new HashMap<>();
        double escala = this.context.getResources().getDisplayMetrics().density;
        tamanhoIcone = (int) (36 * escala);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch(position) {
            case 0:
                fragment = new HomeFragment();
                fragmentosUtilizados.put(position,fragment);
                break;
            case 1:
                fragment = new UsuariosFragment();
                //fragmentosUtilizados.put(position,fragment);
                break;
        }
        return fragment;
    }

    public Fragment getFragment(Integer indice){
        return fragmentosUtilizados.get(indice);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //recupera icone de acordo com a posição
        Drawable drawable = ContextCompat.getDrawable(this.context,icones[position]);
        drawable.setBounds(0,0,tamanhoIcone,tamanhoIcone);
        //permite colocar uma imagem dentro de um texto
        ImageSpan imageSpan = new ImageSpan(drawable);

        //classe utilizada para retornar CharSequence
        SpannableString spannableString = new SpannableString(" ");
        spannableString.setSpan(imageSpan,0,spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    @Override
    public int getCount() {
        return icones.length;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        fragmentosUtilizados.remove(position);
    }
}
