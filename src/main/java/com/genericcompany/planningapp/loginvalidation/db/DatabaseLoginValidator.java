package com.genericcompany.planningapp.loginvalidation.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.ui.Model;

import com.genericcompany.planningapp.dto.ModelDTO;
import com.genericcompany.planningapp.dto.UserDTO;

public class DatabaseLoginValidator 
{
	public static boolean Compare2DB(UserDTO user, Model model) //AT2019
	{
		
		if (user.getUserName().equals("admin") && user.getPassword().equals("admin"))
			return true;
		return false;
	}
}
