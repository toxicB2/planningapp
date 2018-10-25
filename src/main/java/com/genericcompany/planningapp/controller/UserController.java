package com.genericcompany.planningapp.controller;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.genericcompany.planningapp.dao.ModelDAO;
import com.genericcompany.planningapp.dao.UserDAO;
import com.genericcompany.planningapp.dto.InterfaceDTO;
import com.genericcompany.planningapp.dto.MapWrapperDTO;
import com.genericcompany.planningapp.dto.ModelDTO;
import com.genericcompany.planningapp.dto.UserCredentialsDTO;
import com.genericcompany.planningapp.dto.UserDTO;
import com.genericcompany.planningapp.dto.UserRoleDTO;
import com.genericcompany.planningapp.loginvalidation.db.*;
import com.genericcompany.planningapp.userservice.QueryUserService;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

@Controller
@SessionAttributes({"user",
	                "userValidated",
	                "currentUserPermissions",
	                "title"}) 

public class UserController 
{
	@Autowired
	UserDAO<UserRoleDTO> userRoleDAO;
	
	@Autowired
	UserDAO<UserCredentialsDTO> userCredentialsDAO;
	
	@Autowired
	UserDAO<UserDTO> userDAO;
	
	//--------------------------------------------------------------------------------------
	@RequestMapping (value="/login", method=RequestMethod.GET)
	public String login (Model model) 
	{		
	    UserCredentialsDTO user = new UserCredentialsDTO();
	    model.addAttribute("user", user);
	    return "login";
	}
	
	@RequestMapping (value="/login", method=RequestMethod.POST)
	public String doLogin (@ModelAttribute ("user") UserCredentialsDTO user,
			                                     HttpServletRequest request,
			                                                    Model model) 
	{	
		 if (user == null)
		 {
			 user = new UserCredentialsDTO();
		 }
		 
		 UserDTO userDTO = new UserDTO(user);
		 UserRoleDTO userRoleDTO = new UserRoleDTO();
		 try 
		 {
			 userDTO = userDAO.recordExists(userDTO);
			 model.addAttribute("userValidated", userDTO);
			 
			 userRoleDTO = userRoleDAO.getModel(userDTO.getRoles_id(), userRoleDTO);	
			 model.addAttribute("currentUserPermissions", userRoleDTO);
			 
		     return "redirect:"+request.getSession().getAttribute("URLin").toString();
		 }
	     catch (Exception e)
		 {
	         user.setMessage("Invalid user name or password. Please try again");
		     return "login";
		 }
	}
	//--------------------------------------------------------------------------------------------------------
	@RequestMapping("/users")
	public String getModels(@ModelAttribute("user")UserDTO userDTO, 
                                                       Model model, 
                                        HttpServletRequest request) 
	{
	    HttpSession session = request.getSession();
	    
	    UserDTO user = (UserDTO)session.getAttribute("userValidated"); //TODO: check if one exists 
		if (userDTO == null)
		{
			userDTO = new UserDTO();
		}
		
	    UserRoleDTO userRole = (UserRoleDTO)session.getAttribute("currentUserPermissions"); //TODO: check if one exists 
	    List<Map<String, Object>> modelListOut = userDAO.getModels(userDTO, user, userRole);
	    
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
	    
	    Map<String, Object> setOut = new HashMap() {{ put("type", (Object)"user"); 
	                                                  put("data", (Object)modelListOut);
	                                                  put("actionPermitted", (Object)"true");
                                                      put("DataType", (Object)"User");}};
	    model.addAttribute("models", setOut);
	    model.addAttribute("title", "Users:");
		//model.addAttribute("models", modelListOut); // TODO: make sure the parameters are reset for differnt users
	    return "models";
	}
	
	@RequestMapping("/user/update/{id}")
	public String updateItem(@ModelAttribute("model") UserDTO userDTOIN, 
			                 @PathVariable int id, 
			                 Model model,
			                 HttpServletRequest request)
	{
	    HttpSession session = request.getSession();	    
	    UserDTO user = (UserDTO)session.getAttribute("userValidated"); //TODO: check if one exists 

	    UserRoleDTO userRole = (UserRoleDTO)session.getAttribute("currentUserPermissions");
		List<Map<String, Object>> userDTOList;
		if (userDTOIN == null)
		{
			//Default setting
			userDTOIN = new UserDTO();
		}
		try
		{
		    userDTOList = userDAO.getModelList(userDTOIN, user, userRole, id); 
		}
		catch (Exception e)
		{
			userDTOList = new ArrayList<Map<String, Object>>();
		}
		
		//Map<String, Object> mapFromClass = QueryUserService.class2Map(userDTO);	
		Map<String, Object> mapFromClass = userDTOList.get(0);
		
		//The following is built using the relationship in the DB 
		//(for each Role_id there is one and only one Credential_id)
		
		//Map<String, Object>loginCredentialsNames = userDAO.valuesOfColumn("logincredentials.userName");
		//Map<String, Object>roleNames = userDAO.valuesOfColumn("roles.roleName");
		
		List<Map<String, Object>> roleUserName = userDAO.roleUserName();
		Map<String, Object>loginCredentialsNames = roleUserName.get(1);
		Map<String, Object>roleNames = roleUserName.get(0);
		
		Map<String, Object> setOut = new HashMap<String, Object>() {{ put("actionPermitted", (Object)"true");
		                                                              put("DataType", (Object)"User");
		                                                              put("Credentials ID", (Object)loginCredentialsNames);
		                                                              put("Roles ID", (Object)roleNames);
		                                                              put("data", (Object)mapFromClass);}}; // TODO Check permissions for Writing
		model.addAttribute("model", setOut);
		model.addAttribute("title", "Update Manager");
		return "addModel";
	}
	
	@RequestMapping("/addUser")
	public String addUser(@ModelAttribute("model") UserDTO userDTOIN, 
			                                             Model model,
			                              HttpServletRequest request)
	{
	    HttpSession session = request.getSession();	    
	    UserDTO user = (UserDTO)session.getAttribute("userValidated"); //TODO: check if one exists 

	    UserRoleDTO userRole = (UserRoleDTO)session.getAttribute("currentUserPermissions");
		List<Map<String, Object>> userDTOList;
		List<String> userList; 
		if (userDTOIN == null)
		{
			//Default setting
			userDTOIN = new UserDTO();
		}

		userList = userDAO.columnNames(userDTOIN, user); 	
		Map<String, Object> mapFromClass = userList.stream().collect(Collectors.toMap(x -> x, x ->" "));
		
		//The following is built using the relationship in the DB 
		//(for each Role_id there is one and only one Credential_id)
		
		//Map<String, Object>loginCredentialsNames = userDAO.valuesOfColumn("logincredentials.userName");
		//Map<String, Object>roleNames = userDAO.valuesOfColumn("roles.roleName");
		
		List<Map<String, Object>> roleUserName = userDAO.roleUserName();
		Map<String, Object>loginCredentialsNames = roleUserName.get(1);
		Map<String, Object>roleNames = roleUserName.get(0);
		
		Map<String, Object> setOut = new HashMap<String, Object>() {{ put("actionPermitted", (Object)"true");
		                                                              put("DataType", (Object)"User");
		                                                              put("Credentials ID", (Object)loginCredentialsNames);
		                                                              put("Roles ID", (Object)roleNames);
		                                                              put("data", (Object)mapFromClass);}}; // TODO Check permissions for Writing
		model.addAttribute("model", setOut);
		model.addAttribute("title", "Add Manager");
		return "addModel";
	}
	
	@InitBinder("saveModel")
	public void registrarInitBinder(WebDataBinder binder) 
	{ 
		/*DateFormat dateFormat = new SimpleDateFormat("MMM d, YYYY");
		CustomDateEditor orderDateEditor = new CustomDateEditor(dateFormat, true);
		binder.registerCustomEditor(Date.class, orderDateEditor);*/
	}
	
	//---------------------------------------------------------------------------------------------------
	@RequestMapping(value ="/doAddUser", method=RequestMethod.POST, 
			 consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	/*public String doAddModel (@ModelAttribute UserDTO us, 
			                          BindingResult result,
			                                   Model model, 
			                    HttpServletRequest request) */
	public /*@ResponseBody*/ String doAddModel (@RequestBody MultiValueMap<String, Object> map, 
                                                                          BindingResult result,
                                                                                   Model model, 
                                                                    HttpServletRequest request)
	{
	    if (result.hasErrors())
	    {
	        return "error"; // Write an error page (view)
	    }
	    Map<String, Object> noListMap = new HashMap<String, Object>();
	    map.entrySet().stream().forEach(x->{
					    	        	       try
					    	        	       {					    	        	    	   
					    	        	    	   noListMap.put(x.getKey(), x.getValue().toArray()[0]);
					    	        	       }
					    	        	       catch (Exception e)
					    	        	       {
					    	        		       //Handle exceptions
					    	        	           //of unused values 
					    	        	       }
					    	        	   }
					    	            );	    
	    Gson gson = new Gson();
	    UserDTO userDTO = gson.fromJson(gson.toJsonTree(noListMap), UserDTO.class);
	    userDTO.setLoginCrRlsId(Integer.parseInt(map.get("Roles ID").toArray()[0].toString()));
	    userDTO.setLoginCrId(Integer.parseInt(map.get("Credentials ID").toArray()[0].toString()));
	    
	    HttpSession session = request.getSession();
	    UserDTO user = (UserDTO) session.getAttribute("userValidated");
	   
        try 
	    {
        	if (session.getAttribute("title").toString().contains("Update"))
        	{
	    	    userDAO.updateModel(userDTO, user);
        	}
        	else 
        	{
        		userDAO.addModel(userDTO, user);
        	}
	    } 
	    catch (Throwable th) 
	    {
	    	model.addAttribute("error", th.getLocalizedMessage());
	        return "addModel";
	        //should be "error"
	    }
	    return "redirect:users";
	}
	
	@RequestMapping("/user/delete/{id}")
	public String deleteModel(@ModelAttribute("model") UserDTO modelDTOIN, 
			                                    @PathVariable("id") int id, 
			                                                   Model model,
			                                    HttpServletRequest request) 
	{
		//TODO: 
		HttpSession session = request.getSession();
	    UserDTO user = (UserDTO) session.getAttribute("user");
		userDAO.deleteModel(id, modelDTOIN, user);
		return "redirect:/users";
	}
	
	protected static Map<String, Object> introspect(Object obj) throws Exception 
	{
	    Map<String, Object> result = new HashMap<String, Object>();
	    BeanInfo info = Introspector.getBeanInfo(obj.getClass());
	    for (PropertyDescriptor pd : info.getPropertyDescriptors()) 
	    {
	        Method reader = pd.getReadMethod();
	        if (reader != null)
	            result.put(pd.getName(), reader.invoke(obj));
	    }
	    return result;
	}
}
