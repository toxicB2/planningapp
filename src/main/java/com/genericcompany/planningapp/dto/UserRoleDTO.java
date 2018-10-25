package com.genericcompany.planningapp.dto;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.genericcompany.planningapp.userservice.QueryUserService;

public class UserRoleDTO implements InterfaceDTO
{
	private int id;
	private String roleName;
	private boolean modelRead;
	private boolean modelWrite;
	private boolean allModelsRead;
	private boolean allModelsWrite;
	private boolean analystRead;
	private boolean analystWrite;
	private boolean allAnalystWrite;
	private boolean allAnalystRead;
	private boolean managerRead;
	private boolean managerWrite;
	private boolean allManagerRead;
	
    public static String dTODBB = "roles";
	
    public UserRoleDTO()
    {
    	
    }
    
    @Override
    public String getDTODB()
    {
    	return dTODBB;
    }
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public boolean isModelRead() {
		return modelRead;
	}
	public void setModelRead(boolean modelRead) {
		this.modelRead = modelRead;
	}
	public boolean isModelWrite() {
		return modelWrite;
	}
	public void setModelWrite(boolean modelWrite) {
		this.modelWrite = modelWrite;
	}
	public boolean isAllModelsRead() {
		return allModelsRead;
	}
	public void setAllModelsRead(boolean allModelsRead) {
		this.allModelsRead = allModelsRead;
	}
	public boolean isAllModelsWrite() {
		return allModelsWrite;
	}
	public void setAllModelsWrite(boolean allModelsWrite) {
		this.allModelsWrite = allModelsWrite;
	}
	public boolean isAnalystRead() {
		return analystRead;
	}
	public void setAnalystRead(boolean analystRead) {
		this.analystRead = analystRead;
	}
	public boolean isAnalystWrite() {
		return analystWrite;
	}
	public void setAnalystWrite(boolean analystWrite) {
		this.analystWrite = analystWrite;
	}
	public boolean isAllAnalystWrite() {
		return allAnalystWrite;
	}
	public void setAllAnalystWrite(boolean allAnalystWrite) {
		this.allAnalystWrite = allAnalystWrite;
	}
	public boolean isAllAnalystRead() {
		return allAnalystRead;
	}
	public void setAllAnalystRead(boolean allAnalystRead) {
		this.allAnalystRead = allAnalystRead;
	}
	public boolean isManagerRead() {
		return managerRead;
	}
	public void setManagerRead(boolean managerRead) {
		this.managerRead = managerRead;
	}
	public boolean isManagerWrite() {
		return managerWrite;
	}
	public void setManagerWrite(boolean managerWrite) {
		this.managerWrite = managerWrite;
	}
	public boolean isAllManagerRead() {
		return allManagerRead;
	}
	public void setAllManagerRead(boolean allManagerRead) {
		this.allManagerRead = allManagerRead;
	}
	public boolean isAllManagerWrite() {
		return allManagerWrite;
	}
	public void setAllManagerWrite(boolean allManagerWrite) {
		this.allManagerWrite = allManagerWrite;
	}
	private boolean allManagerWrite;

    @Override
    public void writeExternal(ObjectOutput out) throws IOException 
    {
        out.writeUTF(Integer.toString(id));
        out.writeUTF(roleName);
        out.writeUTF(Boolean.toString(modelRead));
        out.writeUTF(Boolean.toString(modelWrite));
        out.writeUTF(Boolean.toString(analystRead));
        out.writeUTF(Boolean.toString(analystWrite));
        out.writeUTF(Boolean.toString(allModelsRead));
        out.writeUTF(Boolean.toString(allModelsWrite));
        out.writeUTF(Boolean.toString(allManagerRead));
        out.writeUTF(Boolean.toString(allManagerWrite));
        out.writeUTF(Boolean.toString(allAnalystRead));
        out.writeUTF(Boolean.toString(allAnalystWrite));
    }

   @Override
   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException 
   {
       this.id = Integer.valueOf(in.readUTF());
       this.roleName = in.readUTF();
       this.modelRead = QueryUserService.IntBoolean2Boolean(in.readUTF());
       this.modelWrite = QueryUserService.IntBoolean2Boolean(in.readUTF());
       this.analystRead = QueryUserService.IntBoolean2Boolean(in.readUTF());
       this.analystWrite = QueryUserService.IntBoolean2Boolean(in.readUTF());
       this.allManagerRead = QueryUserService.IntBoolean2Boolean(in.readUTF());
       this.allManagerWrite = QueryUserService.IntBoolean2Boolean(in.readUTF());
       this.allModelsRead = QueryUserService.IntBoolean2Boolean(in.readUTF());
       this.allModelsWrite = QueryUserService.IntBoolean2Boolean(in.readUTF());
       this.allAnalystRead = QueryUserService.IntBoolean2Boolean(in.readUTF());
       this.allAnalystWrite = QueryUserService.IntBoolean2Boolean(in.readUTF());
   }
}
