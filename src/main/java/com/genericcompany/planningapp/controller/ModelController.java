package com.genericcompany.planningapp.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.genericcompany.planningapp.dao.ModelDAO;
import com.genericcompany.planningapp.dto.InterfaceDTO;
import com.genericcompany.planningapp.dto.ModelDTO;
import com.genericcompany.planningapp.dto.UserDTO;
import com.genericcompany.planningapp.dto.UserRoleDTO;

@Controller
@SessionAttributes({"currentUserPermissions"}) 

public class ModelController 
{
	@Autowired
	ModelDAO<ModelDTO> modelDAO;
	
	/*@Autowired
	private HttpServletResponse response;
	
	@Autowired
	private HttpServletRequest requestIn;*/
	
	@RequestMapping("/models")
	public String getModels(@ModelAttribute("model") ModelDTO modelDTO, 
                                                           Model model, 
                                            HttpServletRequest request) 
	{
	    HttpSession session = request.getSession();
	    
	    UserDTO user = (UserDTO)session.getAttribute("userValidated"); //TODO: check if one exists 
		if (modelDTO == null)
		{
			modelDTO = new ModelDTO();
		}
		
	    UserRoleDTO userRole = (UserRoleDTO)session.getAttribute("currentUserPermissions"); //TODO: check if one exists 
	    List<Map<String, Object>> modelListOut = modelDAO.getModels(modelDTO, user, userRole);
	    
	    if(modelListOut.get(modelListOut.size() - 1)
	    		       .keySet()
	    		       .contains("currentUserPermissions"))
	    {
	    	model.addAttribute("currentUserPermissions", (UserRoleDTO)modelListOut.get(modelListOut.size() - 1)
	    			                                                              .entrySet()
                                                                                  .stream()
                                                                                  .filter(x->x.getKey().equals("currentUserPermissions"))
                                                                                  .map(Map.Entry::getValue)
                                                                                  .collect(java.util.stream.Collectors.toList()).toArray()[0]);
	    	modelListOut.remove(modelListOut.size() - 1);
	    }
	    
		//model.addAttribute("models", modelListOut); // TODO: make sure the parameters are reset for differnt users
	    Map<String, Object> setOut = new HashMap<String, Object>() {{ put("type", (Object)"model"); put("data", (Object)modelListOut);}};
	    model.addAttribute("models", setOut);
	    model.addAttribute("title", "Models:");
	    return "models";
	}
	
	@RequestMapping("/addModel")
	public String addModel(@ModelAttribute("model") ModelDTO modelDTO, Model model) 
	{	    
		if (modelDTO == null)
		{
			modelDTO = new ModelDTO();
		}
		model.addAttribute("model", modelDTO);
    	model.addAttribute("title", "Add Model");
		return "addModel";
	}
	
	@RequestMapping("/doAddModel")
	public String doAddModel (@ModelAttribute("model") ModelDTO modelDTO, 
			                                                 Model model, 
			                                  HttpServletRequest request) 
	{
	    HttpSession session = request.getSession();
	    UserDTO user = (UserDTO) session.getAttribute("userValidated");
        try 
	    {
	    	modelDAO.addModel(modelDTO, user);
	    } 
	    catch (Throwable th) 
	    {
	    	model.addAttribute("error", th.getLocalizedMessage());
	        return "addModel";
	    }
	    return "redirect:models";
	}
	
	/*@RequestMapping("/model/update/{id}")
	public String updateModel(@ModelAttribute("model") ModelDTO modelDTOIN, @PathVariable int id, Model model) 
	{
		if (modelDTOIN == null)
		{
			modelDTOIN = new ModelDTO();
		}
		ModelDTO modelDTO = modelDAO.getModel(id,modelDTOIN);
		model.addAttribute("model", modelDTO);
		model.addAttribute("title", "Update Model");
		return "addModel";
	}*/
	
	@RequestMapping("/model/update/{id}")
	public String updateItem(@ModelAttribute("model") ModelDTO modelDTOIN, 
			                 @PathVariable int id, 
			                 Model model,
			                 HttpServletRequest request)
	{
	    HttpSession session = request.getSession();	    
	    UserDTO user = (UserDTO)session.getAttribute("userValidated"); //TODO: check if one exists 

	    UserRoleDTO userRole = (UserRoleDTO)session.getAttribute("currentUserPermissions");
		List<Map<String, Object>> userDTOList;
		if (modelDTOIN == null)
		{
			//Default setting
			modelDTOIN = new ModelDTO();
		}
		try
		{
		    userDTOList = modelDAO.getModelList(modelDTOIN, user, userRole, id); 
		}
		catch (Exception e)
		{
			userDTOList = new ArrayList<Map<String, Object>>();
		}
		
		//Map<String, Object> mapFromClass = QueryUserService.class2Map(userDTO);	
		Map<String, Object> mapFromClass = userDTOList.get(0);
		Map<String, Object> setOut = new HashMap<String, Object>() {{ put("actionPermitted", (Object)"true");
		                                                              put("data", (Object)mapFromClass);}}; // TODO Check permissions for Writing
		model.addAttribute("model", setOut);
		model.addAttribute("title", "Update Model");
		return "addModel";
	}
	
	@RequestMapping("/model/delete/{id}")
	public String deleteModel(@ModelAttribute("model") ModelDTO modelDTOIN, 
			                                    @PathVariable("id") int id, 
			                                                   Model model,
			                                    HttpServletRequest request) 
	{
		//TODO: 
		HttpSession session = request.getSession();
	    UserDTO user = (UserDTO) session.getAttribute("user");
		modelDAO.deleteModel(id, modelDTOIN, user);
		return "redirect:/models";
	}
}
