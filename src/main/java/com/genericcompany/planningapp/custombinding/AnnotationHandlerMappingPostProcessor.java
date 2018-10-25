package com.genericcompany.planningapp.custombinding;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.google.common.collect.Lists;

public class AnnotationHandlerMappingPostProcessor implements BeanPostProcessor 
{

    @Override
    public Object postProcessAfterInitialization(Object bean, String arg1) throws BeansException 
    {
    	if (bean instanceof HandlerMethodArgumentResolver) 
        {
    		int d =5;
    		int dd =6;
        }
    	if (bean instanceof RequestMappingHandlerAdapter) 
        {
            RequestMappingHandlerAdapter adapter = (RequestMappingHandlerAdapter) bean;
            List<HandlerMethodArgumentResolver> resolvers = new ArrayList(adapter.getArgumentResolvers());
            HandlerMethodArgumentResolver res24 = resolvers.get(24);
            HandlerMethodArgumentResolver res0 = resolvers.get(0);
            if(res24 instanceof AnnotationServletModelAttributeResolver)
            {
                resolvers.set(24,res0);
                resolvers.set(0, res24);
            }
            adapter.setInitBinderArgumentResolvers(resolvers);
            adapter.setArgumentResolvers(resolvers);
            /*if (resolvers == null) 
            {
                resolvers = Lists.newArrayList();
            }
            resolvers.add(new AnnotationServletModelAttributeResolver(false));
            adapter.setCustomArgumentResolvers(resolvers);*/
        }

        return bean;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String arg1) throws BeansException 
    {
        if (bean instanceof RequestMappingHandlerAdapter) 
        {
            RequestMappingHandlerAdapter adapter = (RequestMappingHandlerAdapter) bean;
            List<HandlerMethodArgumentResolver> resolvers = adapter.getCustomArgumentResolvers();
            if (resolvers == null) 
            {
                resolvers = Lists.newArrayList();
            }
            resolvers.add(new AnnotationServletModelAttributeResolver(false));
            adapter.setCustomArgumentResolvers(resolvers);
        }

        return bean;
    }

}
