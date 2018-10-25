package com.genericcompany.planningapp.dto;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.genericcompany.planningapp.custombinding.CommandParameter;
import com.genericcompany.planningapp.custombinding.SupportsAnnotationParameterResolution;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("serial")
@SupportsAnnotationParameterResolution
public class UserDTO extends UserCredentialsDTO
{
	@SerializedName(value="Title", alternate={"title", "ttl"})
	@CommandParameter("Title")
	private String title;
	
	@SerializedName(value="Last Name", alternate={"lastName", "lnm"})
	@CommandParameter("Last Name")
	private String last_name;
	
	@SerializedName(value="First Name", alternate={"firstName", "fnm"})
	@CommandParameter ("First Name")
	private String first_name;
	
	@CommandParameter ("ID")	
	@SerializedName(value="UserID", alternate={"ID", "uid"})
	private int id;
		
    public static String dTODBB = "manager";
    
    @Override
    public String getDTODB()
    {
    	return dTODBB;
    }
    
	public UserDTO(UserCredentialsDTO user)
	{
		super(user);
	}
	
	public UserDTO()
	{
		super();
	}
	
	public String getTitle() 
	{
		return title;
	}
	public void setTitle(String title) 
	{
		this.title = title;
	}
	public String getLast_name() 
	{
		return last_name;
	}
	public void setLast_name(String last_name) 
	{
		this.last_name = last_name;
	}
	public String getFirst_name() 
	{
		return first_name;
	}
	public void setFirst_name(String first_name) 
	{
		this.first_name = first_name;
	}
	public int getId() 
	{
		return id;
	}
	public void setId(int id) 
	{
		this.id = id;
	}
	public int getLoginCrId() 
	{
		return super.getId();
	}
	public void setLoginCrId(int id) 
	{
		super.setId(id);
	}	
	public int getLoginCrRlsId() 
	{
		return super.getRoles_id(); 
	}
	@CommandParameter ("Roles ID")	
	public void setLoginCrRlsId(int id) 
	{
		super.setRoles_id(id);
	}
	
	@Override
    public void writeExternal(ObjectOutput out) throws IOException 
    {
        out.writeUTF(Integer.toString(id));
        out.writeUTF(first_name);
        out.writeUTF(last_name);
        out.writeUTF(title);
    }

   @Override
   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException 
   {
       this.id = Integer.valueOf(in.readUTF());
       this.first_name = in.readUTF();
       this.last_name = in.readUTF();
       this.title = in.readUTF();
   }
}
