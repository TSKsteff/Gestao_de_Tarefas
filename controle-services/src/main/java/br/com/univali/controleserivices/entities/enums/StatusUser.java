package br.com.univali.controleserivices.entities.enums;

import lombok.Getter;

@Getter
public enum StatusUser {

    ACTIVE("Active"),

    INACTIVE("Inactive");

    private String value;

    StatusUser(String value){
        this.value = value;
    }
}
