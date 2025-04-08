package br.com.univali.controleserivices.service.exceptions;

public class UserNotActiveException extends RuntimeException {
    public UserNotActiveException() {
        super("O usuário está desativado e não pode fazer login.");
    }
}