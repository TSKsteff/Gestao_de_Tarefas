package br.com.univali.controleserivices.service.exceptions;

public class InvalidTaskException extends RuntimeException {
    public InvalidTaskException(String message) {
        super(message);
    }
}
