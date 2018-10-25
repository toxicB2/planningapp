package com.genericcompany.planningapp.dto;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.genericcompany.planningapp.custombinding.CommandParameter;
import com.genericcompany.planningapp.custombinding.SupportsAnnotationParameterResolution;
import com.genericcompany.planningapp.userservice.QueryUserService;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("serial")
public class UserCredentialsDTO implements InterfaceDTO
{
	private String userName;
	private String password;
	private String message;
	private String email;
	
	@SerializedName("LoginCr ID")
	protected int id;
	
	@SerializedName("LoginRoles ID")
	protected int roles_id;
	
	public static String dTODBB = "logincredentials";
		
    @Override
    public String getDTODB()
    {
    	return dTODBB;
    }
    
    public UserCredentialsDTO()
    {
    	
    }
	public UserCredentialsDTO(UserCredentialsDTO cpy)
	{
		this.id= cpy.getId();
		this.roles_id = cpy.getRoles_id();
		this.message = cpy.getMessage();
		this.userName = cpy.getUserName();
		this.password = cpy.getPassword();
		this.email = cpy.getEmail();
	}
	
	public int getId() 
	{
		return id;
	}
	public void setId(int id) 
	{
		this.id = id;
	}
	public int getRoles_id() 
	{
		return roles_id;
	}
	public void setRoles_id(int id) 
	{
		this.roles_id = id;
	}
	public String getMessage() 
	{
		return message;
	}
	public void setMessage(String message) 
	{
		this.message = message;
	}
	public String getUserName() 
	{
		return userName;
	}
	public void setUserName(String userName) 
	{
		this.userName = userName;
	}
	public String getEmail() 
	{
		return email;
	}
	public void setEmail(String email) 
	{
		this.email = email;
	}
	public String getPassword() 
	{
		return password;
	}
	public void setPassword(String password) 
	{
		this.password = password;
	}
	public boolean messageExists() 
	{
		return message != null && message.trim().length() > 0;
	}
	
	@Override
    public void writeExternal(ObjectOutput out) throws IOException 
    {
        out.writeUTF(Integer.toString(id));
        out.writeUTF(email);
        out.writeUTF(userName);
        out.writeUTF(password);
        out.writeUTF(Integer.toString(roles_id));
    }

   @Override
   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException 
   {
       this.id = Integer.valueOf(in.readUTF());
       this.email = in.readUTF();
       this.userName = in.readUTF();
       this.password = in.readUTF();
       this.roles_id = Integer.valueOf(in.readUTF());
   }
}
