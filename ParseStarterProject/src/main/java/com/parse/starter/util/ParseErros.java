package com.parse.starter.util;

import java.util.HashMap;

/**
 * Created by Renato on 14/07/2018.
 */

public class ParseErros {

    private HashMap<Integer,String> erros;

    public ParseErros() {
        this.erros = new HashMap<>();
        this.erros.put(-1,"Senha não preenchida.");
        this.erros.put(202,"Usuário já existe, escolha um outro nome de usuário.");
    }

    public String getErro(int codigoDeErro){
        if(erros.containsKey(codigoDeErro)){
            return this.erros.get(codigoDeErro);
        }else{
            return "Erro desconhecido";
        }
    }

}
