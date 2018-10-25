package com.genericcompany.planningapp.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.genericcompany.planningapp.dto.*;

import org.springframework.jdbc.core.RowMapper;

public final class ModelsRowMapper<T> implements RowMapper<T> 
{
	private T dto;
	public ModelsRowMapper(T dtoIn)
	{
		dto = dtoIn;
	}
	
	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException 
	{
		try
		{
		    T model = (T)dto.getClass().newInstance();
		
		    if(model instanceof ModelDTO)
		    {	
		        //ModelDTO model = new ModelDTO();
		        ((ModelDTO)model).setID(rs.getInt("id"));
		        ((ModelDTO)model).setName(rs.getString("name"));
		        ((ModelDTO)model).setCategory(rs.getString("category"));
		        ((ModelDTO)model).setOrigination_area(rs.getString("origination_area"));
		        ((ModelDTO)model).setRisk(rs.getString("risk"));
		        ((ModelDTO)model).setVal_so(rs.getString("val_so"));
		        ((ModelDTO)model).setAmr_so(rs.getString("amr_so"));
		        ((ModelDTO)model).setIs_validated(rs.getBoolean("is_validated"));
		        ((ModelDTO)model).setManager_id(rs.getInt("Manager_id"));        
		    }
		    else if(model instanceof UserDTO)
		    {
		    	((UserDTO)model).setFirst_name(rs.getString("first_name"));
		    	((UserDTO)model).setLast_name(rs.getString("last_name")); 
		    	((UserDTO)model).setTitle(rs.getString("title")); 
		    	((UserDTO)model).setId(rs.getInt("id")); 
		    	
		    	((UserDTO)model).setLoginCrRlsId(rs.getInt("LogInCredentials_Roles_id"));
		    	((UserDTO)model).setLoginCrId(rs.getInt("LogInCredentials_id"));
		    	
		    	((UserCredentialsDTO)model).setPassword(((UserCredentialsDTO)dto).getPassword());
		    	((UserCredentialsDTO)model).setUserName(((UserCredentialsDTO)dto).getUserName());
		    	((UserCredentialsDTO)model).setEmail(((UserCredentialsDTO)dto).getEmail());
		    }
			else if(model instanceof UserRoleDTO)
			{
				((UserRoleDTO)model).setId(rs.getInt("id"));
		        ((UserRoleDTO)model).setRoleName(rs.getString("roleName"));
		        
		        ((UserRoleDTO)model).setModelRead(rs.getBoolean("modelRead"));
		        ((UserRoleDTO)model).setAllModelsRead(rs.getBoolean("allModelsRead"));
		        ((UserRoleDTO)model).setModelWrite(rs.getBoolean("modelWrite"));
		        ((UserRoleDTO)model).setAllModelsWrite(rs.getBoolean("allModelsWrite"));
		        
		        ((UserRoleDTO)model).setAnalystRead(rs.getBoolean("analystRead"));
		        ((UserRoleDTO)model).setAllAnalystRead(rs.getBoolean("allAnalystRead"));
		        ((UserRoleDTO)model).setAnalystWrite(rs.getBoolean("analystWrite"));
		        ((UserRoleDTO)model).setAllAnalystWrite(rs.getBoolean("allAnalystWrite"));
		        
		        ((UserRoleDTO)model).setModelRead(rs.getBoolean("managerRead"));
		        ((UserRoleDTO)model).setAllModelsRead(rs.getBoolean("allManagerRead"));
		        ((UserRoleDTO)model).setModelWrite(rs.getBoolean("managerWrite"));
		        ((UserRoleDTO)model).setAllModelsWrite(rs.getBoolean("allManagerWrite"));
		    }
			else if(model instanceof UserCredentialsDTO)
			{
				((UserCredentialsDTO)model).setId(rs.getInt("id"));     
		        ((UserCredentialsDTO)model).setUserName(rs.getString("userName"));
		        ((UserCredentialsDTO)model).setPassword(rs.getString("password"));
		        ((UserCredentialsDTO)model).setRoles_id(rs.getInt("Roles_id"));  
		        ((UserCredentialsDTO)model).setEmail(rs.getString("email"));
		    }
		    return (T)model;
		}
		catch (Exception e)
		{
			return (T)(new ModelDTO());
		}
		
	}
}
