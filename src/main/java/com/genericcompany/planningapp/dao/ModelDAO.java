package com.genericcompany.planningapp.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.genericcompany.planningapp.dto.InterfaceDTO;
import com.genericcompany.planningapp.dto.ModelDTO;
import com.genericcompany.planningapp.dto.UserDTO;
import com.genericcompany.planningapp.dto.UserRoleDTO;
import com.genericcompany.planningapp.userservice.QueryUserService;
import com.gnericcompany.planningapp.dto.headernames.ModelDTOHeaderNames;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

@SuppressWarnings("serial")

@Repository
public class ModelDAO<T> extends InterfaceDAO<T>
{
	protected JdbcTemplate jdbcTemplate;
	
	public ModelDAO(JdbcTemplate jdbcTemplateIN)
	{
		if (jdbcTemplate==null)
		{
		    jdbcTemplate = jdbcTemplateIN;
		}
	}
	
	public ModelDAO()
	{

	}
	
	@Autowired
	@Override
	public void setDatasource (DataSource dataSource) 
	{
		jdbcTemplate = new JdbcTemplate(dataSource);
	}	
	
	/*public List<T> getModels(final T dtoIn,final UserDTO loggedUser)
	{
		 List<T> models = jdbcTemplate.query("select * from model", new CourseRowMapper());
		 return models;
	}*/
	
	@Override
	public List<Map<String, Object>> getModels(final T dtoIn, 
			                                   final UserDTO loggedUser, 
			                                   final UserRoleDTO loggedUserCredentials) 
	{
		 String tableName = ((InterfaceDTO)dtoIn).getDTODB();
         List<Object> stringQueryAndCredentials = QueryUserService.setUpQueryBasedOnRole(loggedUser, 
        		                                                                         loggedUserCredentials, 
        		                                                                         tableName, 
        		                                                                         jdbcTemplate,
        		                                                                         -1);
		 String stringQuery = stringQueryAndCredentials.get(0).toString();
		 UserRoleDTO loggedUserCredentialsPOJO = (UserRoleDTO)stringQueryAndCredentials.get(1);
		 		 
		 //List<Map<String, Object>> models = jdbcTemplate.queryForList(stringQuery);		 
		 //models = new ModelDTOHeaderNames((InterfaceDTO)dtoIn).InitializeProperHeaderNames(models);
		 
		 List<Map<String, Object>> models = jdbcTemplate.query(stringQuery,
				                                             (ResultSet rs) -> 
		            {
		            	List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();		            	
					    Map<String, String> mappingMap = new ModelDTOHeaderNames((InterfaceDTO) dtoIn).headers;
					    
					    while (rs.next()) 
					    {
					    	HashMap<String,Object> resultsLine = new HashMap<>();
					    	mappingMap.entrySet()
					    	          .stream()
					    	          .forEach(x->{
					    	        	              try
					    	        	              {
					    	        	                  resultsLine.put(x.getValue(), rs.getString(x.getKey()));
					    	        	              }
					    	        	              catch (Exception e)
					    	        	              {
					    	        		              //Handle exceptions
					    	        	            	  //of unused values
					    	        	              }
					    	        	          }
					    	                  );
					        results.add(resultsLine);
					    }					    
					    return results;
					});
		 		 
		 if(loggedUserCredentials == null)
		 {
			 models.add(new HashMap<String, Object>() {{put("currentUserPermissions",((Object)loggedUserCredentialsPOJO));}});	 
		 }
		 return models;
	}
	
	@Override
	public void addModel (final T modelIn, final UserDTO loggedUser) 
	{
		 // Checking privileges and think how to make UI being consistent
		 
		
		 String tableName = ((InterfaceDTO)modelIn).getDTODB();
		 KeyHolder keyHolder = new GeneratedKeyHolder();
		 if(modelIn instanceof ModelDTO)
		 {	
		     jdbcTemplate.update(new PreparedStatementCreator() 
		     {
			 
		         @Override
		         public PreparedStatement createPreparedStatement(Connection con) throws SQLException 
		         {
			        String sql = "INSERT INTO "+ tableName + " (name, "
			   		                                  + "category,"
			   		                                  + " origination_area,"
			   		                                  + " risk,"
			   		                                  + " val_so,"
			   		                                  + " amr_so,"
			   		                                  + " is_validated,"
			   		                                  + " Manager_id) VALUES (?,?,?,?,?,?,?,?)"; //AT20180911
		            PreparedStatement stmt = con.prepareStatement(sql, new String[] {"id"}); 
		            stmt.setString(1, ((ModelDTO)modelIn).getName());
		            stmt.setString(2, ((ModelDTO)modelIn).getCategory());
		            stmt.setString(3, ((ModelDTO)modelIn).getOrigination_area());
		            stmt.setString(4, ((ModelDTO)modelIn).getRisk());
		            stmt.setString(5, ((ModelDTO)modelIn).getVal_so());
		            stmt.setString(6, ((ModelDTO)modelIn).getAmr_so());
		            stmt.setBoolean(7, ((ModelDTO)modelIn).getIs_validated());
		            stmt.setInt(8, loggedUser.getId());
		       
		            return stmt;
		         } 
		     }, keyHolder);
			 ((ModelDTO)modelIn).setID(keyHolder.getKey().intValue());
		 }
	}
	
	@Override
	public void updateModel (final T modelIn, 
			                 final UserDTO loggedUser) 
	{
		 String tableName = ((InterfaceDTO)modelIn).getDTODB();
		 if(modelIn instanceof ModelDTO)
		 {	
		     jdbcTemplate.update(new PreparedStatementCreator() 
		     {
		 
			     @Override
		         public PreparedStatement createPreparedStatement(Connection con) throws SQLException 
			     {
			         String sql = "UPDATE "+ tableName + " SET name = ?, "
                                                 + "category = ?,"
                                                 + " origination_area = ?,"
                                                 + " risk = ?,"
                                                 + " val_so = ?,"
                                                 + " amr_so = ?,"
                                                 + " is_validated = ? WHERE id = ?";
		             PreparedStatement stmt = con.prepareStatement(sql);
		     
		             stmt.setString(1, ((ModelDTO)modelIn).getName());
		             stmt.setString(2, ((ModelDTO)modelIn).getCategory());
		             stmt.setString(3, ((ModelDTO)modelIn).getOrigination_area());
		             stmt.setString(4, ((ModelDTO)modelIn).getRisk());
		             stmt.setString(5, ((ModelDTO)modelIn).getVal_so());
		             stmt.setString(6, ((ModelDTO)modelIn).getAmr_so());
		             stmt.setBoolean(7, ((ModelDTO)modelIn).getIs_validated()); 
		             stmt.setInt(8, ((ModelDTO)modelIn).getID());  
		     
		             return stmt;
		          }
		     });
		 }
	}
	
	@Override
	public void deleteModel(final int id, 
			                final T dtoIn, 
			                final UserDTO loggedUser) 
	{
		 String tableName = ((InterfaceDTO)dtoIn).getDTODB();
		 jdbcTemplate.update(new PreparedStatementCreator() 
		 {
			 @Override
		     public PreparedStatement createPreparedStatement(Connection con) throws SQLException 
			 {
				 String sql = "DELETE FROM "+ tableName + " WHERE id = ?"; //AT20180911
		         PreparedStatement stmt = con.prepareStatement(sql);
		         stmt.setInt(1, id);
		         return stmt;
		     }
		 });
    }
	
	@Override
	public T getModel (int id, final T dtoIn) 
	{
		String tableName = ((InterfaceDTO)dtoIn).getDTODB();
		String sql = "SELECT * FROM "+ tableName + " WHERE id = ?";
		T modelDTO = (T)jdbcTemplate.queryForObject(sql, new ModelsRowMapper<T>(dtoIn), id);
		return modelDTO;
	}
	
	@Override
	public List<String> columnNames(final T dtoIn, 
                                    final UserDTO loggedUser)
	{
		List<String> rolesColumnsInModels;
		String tableName = ((InterfaceDTO)dtoIn).getDTODB();
	    String colHeaderNames = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = "
                                + "'resourceallocation' AND TABLE_NAME ='"+ tableName+"'";
	    try
	    {
            rolesColumnsInModels = jdbcTemplate.queryForList(colHeaderNames, String.class);
	    }
	    catch (Exception e)
	    {
	    	//Handle this exception properly
	    	rolesColumnsInModels = new ArrayList<String>();
	    }
	    
	    return rolesColumnsInModels;
	}
	
	@Override
	public List<Map<String, Object>> getModelList(final T dtoIn, 
			                                      final UserDTO loggedUser, 
			                                      final UserRoleDTO loggedUserCredentials,
			                                      final int id) 
	{
		 String tableName = ((InterfaceDTO)dtoIn).getDTODB();
         List<Object> stringQueryAndCredentials = QueryUserService.setUpQueryBasedOnRole(loggedUser, 
        		                                                                         loggedUserCredentials, 
        		                                                                         tableName, 
        		                                                                         jdbcTemplate,
        		                                                                         id);
         
		 String stringQuery = stringQueryAndCredentials.get(0).toString();
		 UserRoleDTO loggedUserCredentialsPOJO = (UserRoleDTO)stringQueryAndCredentials.get(1);
		
		 List<Map<String, Object>> models = jdbcTemplate.query(stringQuery,
                                           (ResultSet rs) -> 
         {                   
             List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();		            	
             Map<String, String> mappingMap = new ModelDTOHeaderNames((InterfaceDTO) dtoIn).headers;

             while (rs.next()) 
            {
                HashMap<String,Object> resultsLine = new HashMap<>();
                mappingMap.entrySet()
                          .stream()
                          .forEach(x->{
                                          try
                                          {
                                              resultsLine.put(x.getValue(), rs.getString(x.getKey()));
                                          }
                                          catch (Exception e)
                                          {
                                          //Handle exceptions
       	                                  //of unused values
                                          }
                                      }
                                  );
                results.add(resultsLine);
            }					    
           return results;
        });

		return models;
	}
	
	public Map<String, Object> valuesOfColumn(String tableColumn)
	{
		if (Pattern.compile("\\\\w*\\\\.\\\\w*").matcher(tableColumn).matches())
		{ 
		    return new HashMap<String, Object>();
		}
		StringBuilder lableColumnSB = new StringBuilder(tableColumn);
		String tableName = lableColumnSB.substring(0, lableColumnSB.indexOf("."));
		//String tableName = lableColumnSB.substring(lableColumnSB.indexOf(".")+1, lableColumnSB.length());		
		String colHeaderNames = "SELECT id, " +tableColumn+ " FROM "+tableName;
		try 
		{
			//return jdbcTemplate.queryForMap(colHeaderNames);
			 Map<String, Object> names = jdbcTemplate.query(colHeaderNames,
                                                           (ResultSet rs) -> 
             {                   
                 Map<String, Object> results = new HashMap<String, Object>();		            	

                 while (rs.next()) 
                 {
                     try
                     {
                    	 results.put(Integer.toString(rs.getInt("id")), rs.getString(tableColumn));
                     }
                     catch (Exception e)
                     {
                         //Handle exceptions
                         //of unused values
                     }             
                }					    
                return results;
            });
			return names;
			
		}
		catch (Exception e)
		{
			return new HashMap<String, Object>();
		}
	}
}
