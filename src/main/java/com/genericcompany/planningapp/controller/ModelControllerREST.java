package com.genericcompany.planningapp.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.genericcompany.planningapp.dao.ModelDAO;
import com.genericcompany.planningapp.dao.ModelsRowMapper;
import com.genericcompany.planningapp.dto.InterfaceDTO;
import com.genericcompany.planningapp.dto.ModelDTO;
import com.genericcompany.planningapp.dto.UserDTO;

@RestController
public class ModelControllerREST 
{
	@Autowired
	ModelDAO<ModelDTO> modelDAO;
	
	@RequestMapping("/modelsREST")
	public List<ModelDTO> getModels(@ModelAttribute("model") ModelDTO modelDTO, 
			                                                 Model model,
			                                                 HttpServletRequest request)
	{
	    HttpSession session = request.getSession();
	    UserDTO user = (UserDTO) session.getAttribute("user");
		//List<ModelDTO> allModels = modelDAO.getModels(modelDTO, user);	
		List<ModelDTO> allModels = new ArrayList<ModelDTO>();
	    return allModels;
	}

	@RequestMapping(value= "/model/getModelREST", method=RequestMethod.GET)
	public ModelDTO getModel(@RequestParam(value="id", defaultValue="1") int id, 
			                 @ModelAttribute("model") ModelDTO modelDTOIN, Model model) 
	{
		return modelDAO.getModel(id,modelDTOIN);
	}
	
	@RequestMapping(value= "/model/deleteModelREST", method=RequestMethod.DELETE)
	public boolean deleteModel(@RequestParam(value="id") int id, 
			                    @ModelAttribute("model") ModelDTO modelDTOIN, Model model,
			                    HttpServletRequest request) 
	{
		try
		{	
		     HttpSession session = request.getSession();
		     UserDTO user = (UserDTO) session.getAttribute("user");
		     modelDAO.deleteModel(id,modelDTOIN, user);
		     return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	@RequestMapping(value= "/model/insertModelREST", method=RequestMethod.POST)
	public boolean insertModel(@RequestParam(value="id") int id, 
			                    @ModelAttribute("model") ModelDTO modelDTOIN, Model model,
			                    HttpServletRequest request) 
	{
	    HttpSession session = request.getSession();
	    UserDTO user = (UserDTO) session.getAttribute("user");
        try 
	    {
	    	modelDAO.addModel(modelDTOIN, user);
	    	return true;
	    } 
		catch (Exception e)
		{
			return false;
		}
	}
}
