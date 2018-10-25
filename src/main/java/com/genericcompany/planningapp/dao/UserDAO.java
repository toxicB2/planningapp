package com.genericcompany.planningapp.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.genericcompany.planningapp.dto.UserCredentialsDTO;
import com.genericcompany.planningapp.dto.UserDTO;
import com.genericcompany.planningapp.dto.UserRoleDTO;

@SuppressWarnings("serial")

@Repository
public class UserDAO<T> extends ModelDAO<T>
{
	//@Autowired
	public UserDAO()
	{
        super();
	}
	
	/*@Autowired
	@Override
	public void setDatasource (DataSource dataSource) 
	{
		jdbcTemplate = new JdbcTemplate(dataSource);
	}*/	
	
	//private JdbcTemplate jdbcTemplate;
	
	@Override
	public void addModel(final T modelIn, final UserDTO loggedUser)  
	{
		 String tableName = ((InterfaceDTO)modelIn).getDTODB();
		 KeyHolder keyHolder = new GeneratedKeyHolder();
		 if(modelIn instanceof UserDTO)
		 {	
			 
		     jdbcTemplate.update(new PreparedStatementCreator() 
		     {
			 
		         @Override
		         public PreparedStatement createPreparedStatement(Connection con) throws SQLException 
		         {
			        String sql = "INSERT INTO "+ tableName + " (LogInCredentials_Roles_id, "
			   		                                  + "LogInCredentials_id,"
			   		                                  + " first_name,"
			   		                                  + " last_name,"
			   		                                  + " title) VALUES (?,?,?,?,?)"; 
		            PreparedStatement stmt = con.prepareStatement( sql,new String[] {"id"}); 
		            stmt.setInt(1, ((UserDTO)modelIn).getLoginCrRlsId());
		            stmt.setInt(2, ((UserDTO)modelIn).getLoginCrId());
		            stmt.setString(3, ((UserDTO)modelIn).getFirst_name());
		            stmt.setString(4, ((UserDTO)modelIn).getLast_name());
		            stmt.setString(5, ((UserDTO)modelIn).getTitle());
		            //stmt.setInt(8, modelIn.getManager_id());
		       
		            return stmt;
		         } 
		       }, keyHolder);
			 ((UserDTO)modelIn).setId(keyHolder.getKey().intValue());
		 }
	}
	
	@Override
	public void updateModel (final T modelIn, final UserDTO loggedUser)  
	{
		 String tableName = ((InterfaceDTO)modelIn).getDTODB();
		 
		 if(modelIn instanceof UserDTO)
		 {	
		     jdbcTemplate.update(new PreparedStatementCreator() 
		     {
		 
		    	 @Override
		         public PreparedStatement createPreparedStatement(Connection con) throws SQLException 
		         {
			        String sql = "UPDATE "+ tableName + " SET" 
			        		                          + " LogInCredentials_Roles_id = ?,"
			   		                                  + " LogInCredentials_id = ?,"
			   		                                  + " first_name = ?,"
			   		                                  + " last_name = ?,"
			   		                                  + " title = ?"
			   		                                  + " WHERE id = ?";
		            PreparedStatement stmt = con.prepareStatement(sql); 
		            stmt.setInt(1, ((UserDTO)modelIn).getLoginCrRlsId());
		            stmt.setInt(2, ((UserDTO)modelIn).getLoginCrId());
		            stmt.setString(3, ((UserDTO)modelIn).getFirst_name());
		            stmt.setString(4, ((UserDTO)modelIn).getLast_name());
		            stmt.setString(5, ((UserDTO)modelIn).getTitle());
		            stmt.setInt(6, ((UserDTO)modelIn).getId());
		       
		            return stmt;
		         } 
		     });
		 }
	}
	
	public T recordExists (final T modelIn) 
	{
		 T ManagerID;
		 int credentialsID;
		 UserCredentialsDTO modelInParent = new UserCredentialsDTO((UserCredentialsDTO)modelIn);
		 //TODO refactor with probably extends
		 String tableName = modelInParent.getDTODB();
		 String childTableName = ((UserDTO)modelIn).getDTODB();
		 
		 //String sql = "SELECT id FROM "+ tableName + " WHERE userName = ? AND password = ?";
		 String sql = "SELECT * FROM "+ tableName + " WHERE userName = ? AND password = ?";
		 String sql2 = "SELECT * FROM "+ childTableName + " WHERE LogInCredentials_id = ?";
		 try
		 {
			 /*Object[] tempArr = new Object[] {((UserCredentialsDTO)modelIn).getUserName(),((UserCredentialsDTO)modelIn).getPassword()};
		     credentialsID = jdbcTemplate.queryForObject(sql, tempArr, Integer.class); //AT20190927 Need Roles_id here too */
			 
		     Object[] tempArr = new Object[] {((UserCredentialsDTO)modelIn).getUserName(),
		    		                          ((UserCredentialsDTO)modelIn).getPassword()};
		     UserCredentialsDTO modelOutParent = jdbcTemplate.queryForObject(sql, new ModelsRowMapper<UserCredentialsDTO>(modelInParent), tempArr); 
		     credentialsID = modelOutParent.getId();
		     
			 Object[] tempArr2 = new Object[] {credentialsID};
			 ModelsRowMapper<T> wrap = new ModelsRowMapper<T>(modelIn);
		     ManagerID = jdbcTemplate.queryForObject(sql2, tempArr2, wrap);
		     ((UserDTO)ManagerID).setRoles_id(modelOutParent.getRoles_id());
		     
		 }
		 catch (Exception e)
		 {
			 return modelIn;
		 }
		 try
		 {
		     return ((T)ManagerID);
		 }
		 catch (Exception e)
		 {
			 // TODO Produce error and stop
			 return modelIn;
		 }
	}
	
	public List<Map<String, Object>> roleUserName () 
	{
		String sql = "SELECT logincredentials.id, roles.roleName, logincredentials.userName "
				   + "FROM roles "
				   + "INNER JOIN logincredentials "
				   + "ON roles.id = logincredentials.Roles_id "
				   + "ORDER BY logincredentials.id";
		try 
		{
			 List<Map<String, Object>> names = jdbcTemplate.query(sql,
                                                           (ResultSet rs) -> 
             {                   
                 Map<String, Object> pairRoles = new HashMap<String, Object>();	
                 Map<String, Object> pairUsers = new HashMap<String, Object>();	

                 while (rs.next()) 
                 {
                     try
                     {
                    	 pairUsers.put(Integer.toString(rs.getInt("logincredentials.id")),rs.getString("logincredentials.userName"));
                    	 pairRoles.put(Integer.toString(rs.getInt("logincredentials.id")),rs.getString("roles.roleName"));
                     }
                     catch (Exception e)
                     {
                         //Handle exceptions
                         //of unused values
                     }             
                }					    
                return new ArrayList<Map<String, Object>>(){{add(pairRoles); add(pairUsers); }};
            });
			 
			return names;
			
		}
		catch (Exception e)
		{
			return new ArrayList<Map<String, Object>>();
		}
	
	}
}
