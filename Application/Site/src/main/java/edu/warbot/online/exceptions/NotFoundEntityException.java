package edu.warbot.online.exceptions;

/**
 * Created by BEUGNON on 31/03/2015.
 *
 * @author Sebastien Beugnon
 */
public class NotFoundEntityException extends Exception {
    private Class<?> entityClass;

    public NotFoundEntityException(Class<?> entityClass) {
        super();
        this.entityClass = entityClass;
    }
}
