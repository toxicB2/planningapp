package com.genericcompany.planningapp.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.genericcompany.planningapp.dto.ModelDTO;
import com.genericcompany.planningapp.dto.UserDTO;
import com.genericcompany.planningapp.dto.UserRoleDTO;

@SuppressWarnings("serial")

@Repository
public abstract class InterfaceDAO<T> implements Serializable
{
	private JdbcTemplate jdbcTemplate;
	@Autowired
	public abstract void setDatasource (DataSource T);	
	public abstract List<Map<String, Object>> getModels(final T dtoIn,
			                                            final UserDTO loggedUser, 
			                                            final UserRoleDTO loggedUserCredentials);
	
	public abstract void addModel (final T modelIn, 
			                       final UserDTO loggedUser); 
	public abstract void updateModel (final T modelIn, 
			                          final UserDTO loggedUser); 
	public abstract void deleteModel(final int id, 
			                         final T dtoIn, 
			                         final UserDTO loggedUser);
	public abstract List<String> columnNames(final T dtoIn, 
                                             final UserDTO loggedUser);
	public abstract T getModel (int id, final T dtoIn);
	public abstract List<Map<String, Object>> getModelList(final T dtoIn, 
                                                           final UserDTO loggedUser, 
                                                           final UserRoleDTO loggedUserCredentials,
                                                           final int id); 
}
