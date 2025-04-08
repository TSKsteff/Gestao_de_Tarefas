package br.com.univali.controleserivices.service.exceptions;

public class ResourceNotFoundExceptoin extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundExceptoin(Object id) {
        super("Resource no found . ID " + id);
    }

}