package com.genericcompany.planningapp.dto;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Map;

import com.genericcompany.planningapp.userservice.QueryUserService;

public class MapWrapperDTO implements InterfaceDTO
{
	private int ID;
	private Map<String, Object> model;
	
    public static String dTODBB = "generic";
	
    @Override
    public String getDTODB()
    {
    	return dTODBB;
    }
    
    public Map<String, Object> getMap()
    {
    	return this.model;
    }
    
    public void setMap(Map<String, Object> mp)
    {
    	this.model = mp;
    }
    @Override
    public void writeExternal(ObjectOutput out) throws IOException 
    {
        out.writeUTF(Integer.toString(ID));
    }

   @Override
   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException 
   {
       this.ID = Integer.valueOf(in.readUTF());
   }
}
