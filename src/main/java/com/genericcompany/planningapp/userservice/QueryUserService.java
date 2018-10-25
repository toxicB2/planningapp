package com.genericcompany.planningapp.userservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.jdbc.core.JdbcTemplate;

import com.genericcompany.planningapp.dao.ModelsRowMapper;
import com.genericcompany.planningapp.dto.InterfaceDTO;
import com.genericcompany.planningapp.dto.UserCredentialsDTO;
import com.genericcompany.planningapp.dto.UserDTO;
import com.genericcompany.planningapp.dto.UserRoleDTO;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class QueryUserService 
{
	public QueryUserService()
	{
		
	}
	
	/*public static UserRoleDTO getUserRoleWithID (int id, final UserRoleDTO dtoIn, final JdbcTemplate jdbcTemplate) 
	{
		String sql = "SELECT * FROM roles WHERE id = ?";
		return (UserRoleDTO)jdbcTemplate.queryForObject(sql, new ModelsRowMapper<UserRoleDTO>(dtoIn), id);
	}*/
	
    public static Map<String, Object> class2Map(Object elementIn)
    { 
		return  QueryUserService.parse(new Gson().toJson(elementIn));
    }
    
	public static boolean IntBoolean2Boolean (Object objectIN)
	{
		String value = objectIN.toString();
	    boolean returnValue = false;
	    if ("1".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value) || 
	        "true".equalsIgnoreCase(value) || "on".equalsIgnoreCase(value))
	        returnValue = true;
	    return returnValue;
	}
	
	public static HashMap<String, Object> parse(String json) 
	{
	    JsonObject object = (JsonObject) new JsonParser().parse(json);
	    Set<Map.Entry<String, JsonElement>> set = object.entrySet();
	    Iterator<Map.Entry<String, JsonElement>> iterator = set.iterator();
	    HashMap<String, Object> map = new HashMap<String, Object>();
	    
	    while (iterator.hasNext()) 
	    {
	        Map.Entry<String, JsonElement> entry = iterator.next();
	        String key = entry.getKey();
	        JsonElement value = entry.getValue();
	        if (!value.isJsonPrimitive()) 
	        {
	            map.put(key, parse(value.toString()));
	        } else 
	        {
	            map.put(key, value.getAsString());
	        }
	    }
	    return map;
	}
	
	public static List<Object> setUpQueryBasedOnRole(UserDTO user, 
			                                         UserRoleDTO userCredentials, 
			                                         String tableName, 
			                                         JdbcTemplate jdbcTemplate,
			                                         int id)
	{
		Map<String, Object> rolesPermissions;
		Gson gson = new Gson();
		if (userCredentials == null)
		{   		
	        String roleHeaders = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS "
            + "WHERE TABLE_SCHEMA = 'resourceallocation' AND "
            + "TABLE_NAME ='roles' AND COLUMN_NAME LIKE '%'";

            List<String> rolesColumns = jdbcTemplate.queryForList(roleHeaders,String.class);
	    
	        StringBuilder roleResp4Table = new StringBuilder("SELECT");
	        for (String roles: rolesColumns)
	        {
	    	    roleResp4Table.append(" ");
	    	    roleResp4Table.append(roles);
	    	    roleResp4Table.append(",");
	        }
	        roleResp4Table.setLength(roleResp4Table.length() - 1);
	        roleResp4Table.append(" FROM roles WHERE id=");
	        roleResp4Table.append(Integer.toString(user.getRoles_id()));
	   
	        rolesPermissions = jdbcTemplate.queryForMap(roleResp4Table.toString());
		}
		else
		{
			String jString = gson.toJson(userCredentials);	    
			rolesPermissions = QueryUserService.parse(jString);
		}
	    // Set up the user privileges in SessionAttribute ------------------------------------------------------------
	    
	    
	    // Get all table headers in "tableName" ----------------------------------------------------------------------
		
	    String colHeaderNames = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = "
                + "'resourceallocation' AND TABLE_NAME ='"+ tableName+"'";
        List<String> rolesColumnsInModels = jdbcTemplate.queryForList(colHeaderNames, String.class);
	
	    // Set up queries ---------------------------------------------------------------------------------------------
        
    	JsonElement jsonElement = gson.toJsonTree(rolesPermissions);
    	UserRoleDTO rolesPermissionsPOJO = gson.fromJson(jsonElement, UserRoleDTO.class);
        
	    rolesColumnsInModels = rolesColumnsInModels
                .stream()
                .filter(x->(!x.equals("Manager_id")))
                .collect(java.util.stream.Collectors.toList());
    	
        //TODO Try Catch block if anything went wrong before and the following doesnt work
	    if(QueryUserService.IntBoolean2Boolean(rolesPermissions 
	    		      .entrySet()
	    		      .stream()
	    		      .filter(x->x.getKey().equals("allModelsWrite"))
	    		      .map(Map.Entry::getValue)
	    		      .collect(java.util.stream.Collectors.toList()).toArray()[0])||
	       QueryUserService.IntBoolean2Boolean(rolesPermissions
	    	    	  .entrySet()
	    	    	  .stream()
	    	    	  .filter(x->x.getKey().equals("allModelsRead"))
	    	    	  .map(Map.Entry::getValue)
	    	    	  .collect(java.util.stream.Collectors.toList()).toArray()[0]))
	    {
	    	StringBuilder tempSQLbuilder = new StringBuilder("SELECT");
	    	for (String roles: rolesColumnsInModels)
	 	    {
	    		tempSQLbuilder.append(" ");
	    		tempSQLbuilder.append(tableName);
	    		tempSQLbuilder.append("."); 
	    		tempSQLbuilder.append(roles);
	    		tempSQLbuilder.append(",");
	 	    }
	    	if (tableName.equals("model")) // Switch instead of if/else
	    	{
	    	    tempSQLbuilder.append(" concat(manager.first_name,' ', manager.last_name)");
		    	tempSQLbuilder.append(" FROM ");
		    	tempSQLbuilder.append(tableName);    	
		    	tempSQLbuilder.append(" LEFT JOIN manager ON manager.id = model.Manager_id ");
		    	if (id>0)
		    	{
		    	    tempSQLbuilder.append(" WHERE "+tableName+".id = "+Integer.toString(id)+" ");
		    	}
		    	tempSQLbuilder.append("ORDER BY "+tableName+".id");
	    	}
	    	else if (tableName.equals("manager")) 
	    	{
	    		tempSQLbuilder.setLength(tempSQLbuilder.length() - 1);
		    	tempSQLbuilder.append(" FROM ");
		    	tempSQLbuilder.append(tableName);    	
		    	if (id>0)
		    	{
		    	    tempSQLbuilder.append(" WHERE "+tableName+".id = "+Integer.toString(id));
		    	}
		    	tempSQLbuilder.append(" ORDER BY "+tableName+".id");
	    	}
	    	else 
	    	{
	    		tempSQLbuilder.setLength(tempSQLbuilder.length() - 1);
	    		tempSQLbuilder.append(" FROM ");
		    	tempSQLbuilder.append(tableName);
	    	}	    	
		    return new ArrayList<Object>() {{add(tempSQLbuilder.toString()); add(rolesPermissionsPOJO);}};
	    }
	    else if(QueryUserService.IntBoolean2Boolean(rolesPermissions
	    		.entrySet()
	    		.stream()
	    		.filter(x->x.getKey().equals("modelWrite"))
	    		.map(Map.Entry::getValue)
	    		.collect(java.util.stream.Collectors.toList()).toArray()[0])||
	    		QueryUserService.IntBoolean2Boolean(rolesPermissions
	    		.entrySet()
	    		.stream()
	    		.filter(x->x.getKey().equals("modelRead"))
	    		.map(Map.Entry::getValue)
	    		.collect(java.util.stream.Collectors.toList()).toArray()[0]))
	    {	    //todo rewrite based on security options
	    	StringBuilder tempSQLbuilder = new StringBuilder("SELECT");
	    	for (String roles: rolesColumnsInModels)
	 	    {
	    		tempSQLbuilder.append(" ");
	    		tempSQLbuilder.append(tableName);
	    		tempSQLbuilder.append("."); 
	    		tempSQLbuilder.append(roles);
	    		tempSQLbuilder.append(",");
	 	    }
	    	tempSQLbuilder.append(" concat(manager.first_name,' ', manager.last_name)");
	    	tempSQLbuilder.append(" FROM ");
	    	tempSQLbuilder.append(tableName);
	    	tempSQLbuilder.append(" LEFT JOIN manager ON manager.id = model.Manager_id ORDER BY "+tableName+".id;");
	    	
		    return new ArrayList<Object>() {{add(tempSQLbuilder.toString()); add(rolesPermissionsPOJO);}};    
	    }
	    return new ArrayList<Object>(){{add(("SELECT amr_so, val_so FROM "+ tableName)); add(rolesPermissionsPOJO);}};
		//return ("SELECT amr_so, val_so FROM "+ tableName);
	}

}
