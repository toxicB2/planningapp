package com.genericcompany.planningapp.custombinding;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})

public @interface SupportsAnnotationParameterResolution 
{
    public String value() default "";
}