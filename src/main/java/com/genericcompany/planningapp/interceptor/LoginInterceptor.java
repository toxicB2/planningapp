package com.genericcompany.planningapp.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.genericcompany.planningapp.dto.UserCredentialsDTO;
import com.genericcompany.planningapp.dto.UserDTO;

public class LoginInterceptor extends HandlerInterceptorAdapter
{
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception 
	{
	    HttpSession session = request.getSession();
	    UserCredentialsDTO user = (UserCredentialsDTO) session.getAttribute("user");
	    UserDTO userValidated = (UserDTO) session.getAttribute("userValidated");
	    String context = request.getContextPath();
	    StringBuilder URL = new StringBuilder();
	    String tempURL = request.getRequestURL().toString();
	    if(!tempURL.contains("login"))
	    {
	        if (request.getQueryString()!=null)
	        {
	    	    URL.append(tempURL).append("?").append(request.getQueryString());
	        }
	        else
	        {
	    	    URL.append(tempURL);
	        }
	        request.getSession().setAttribute("URLin", URL.toString());
	    }
		if ((!request.getRequestURI().equals(context + "/login") 
				&& (user == null || user.getUserName() == null))
				||
			(!request.getRequestURI().equals(context + "/login") 
				&& (userValidated==null || userValidated.getLast_name() == null)))
		{
			response.sendRedirect(request.getContextPath() + "/login");
			return false;
		}
		return true;
	}

}
