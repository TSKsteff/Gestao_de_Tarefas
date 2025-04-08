package br.com.univali.controleserivices.service.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("O email já está em uso: " + email);
    }
}