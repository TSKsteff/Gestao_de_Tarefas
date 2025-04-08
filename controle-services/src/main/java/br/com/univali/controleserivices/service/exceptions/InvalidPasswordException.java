package br.com.univali.controleserivices.service.exceptions;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("A senha fornecida não atende aos critérios de segurança.");
    }
}