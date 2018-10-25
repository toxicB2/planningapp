package com.genericcompany.planningapp.serializationroutines;

public class Generic<T>
{
    private Class<T> clazz;

    public Generic(Class<T> clazz)
    {
        this.clazz = clazz;
    }
    public T buildOne() throws InstantiationException,
        IllegalAccessException
    {
        return clazz.newInstance();
    }
}