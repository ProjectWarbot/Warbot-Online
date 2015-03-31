package edu.warbot.exceptions;

/**
 * Created by BEUGNON on 31/03/2015.
 *
 * @author Sebastien Beugnon
 */
public class EntityNotFoundException
{
    private Class<?> entityClass;
    public EntityNotFoundException(Class<?> entityClass)
    {
        super();
        this.entityClass = entityClass;
    }
}
