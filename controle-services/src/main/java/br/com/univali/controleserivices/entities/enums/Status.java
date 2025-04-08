package br.com.univali.controleserivices.entities.enums;

import lombok.Getter;

@Getter
public enum Status {


    PENDENTE("PENDENTE"),
    EM_ANDAMENTO("EM_ANDAMENTO"),
    CONCLUIDO("CONCLUIDO");

    private String value;

    Status(String value){
        this.value =  value;
    }
}
