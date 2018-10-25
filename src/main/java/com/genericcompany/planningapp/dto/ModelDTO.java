package com.genericcompany.planningapp.dto;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.genericcompany.planningapp.userservice.QueryUserService;

@SuppressWarnings("serial")

@ManagedBean(name="modelDTO")
@RequestScoped

public class ModelDTO implements InterfaceDTO
{
	private int ID;
	private String Name;
	private String Category;
	private String Origination_area;
	private String Risk;
	private String Val_so;
	private String Amr_so;
	private Boolean Is_validated;
	private int Manager_id;
	
	public static String dTODBB = "model";
	
    @Override
    public String getDTODB()
    {
    	return dTODBB;
    }
    
	public ModelDTO()
	{

	}
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getCategory() {
		return Category;
	}
	public void setCategory(String category) {
		Category = category;
	}
	public String getOrigination_area() {
		return Origination_area;
	}
	public void setOrigination_area(String origination_area) {
		Origination_area = origination_area;
	}
	public String getRisk() {
		return Risk;
	}
	public void setRisk(String risk) {
		Risk = risk;
	}
	public String getVal_so() {
		return Val_so;
	}
	public void setVal_so(String val_so) {
		Val_so = val_so;
	}
	public String getAmr_so() {
		return Amr_so;
	}
	public void setAmr_so(String amr_so) {
		Amr_so = amr_so;
	}
	public Boolean getIs_validated() {
		return Is_validated;
	}
	public void setIs_validated(Boolean is_validated) {
		Is_validated = is_validated;
	}	
	public int getManager_id() {
		return Manager_id;
	}
	public void setManager_id(int manager_id) {
		Manager_id = manager_id;
	}
	 
    @Override
    public void writeExternal(ObjectOutput out) throws IOException 
    {
        out.writeUTF(Integer.toString(ID));
        out.writeUTF(Name);
        out.writeUTF(Category);
        out.writeUTF(Origination_area);
        out.writeUTF(Risk);
        out.writeUTF(Val_so);
        out.writeUTF(Amr_so);
        out.writeUTF(Is_validated.toString());
        out.writeUTF(Integer.toString(Manager_id));
    }

   @Override
   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException 
   {
       this.ID = Integer.valueOf(in.readUTF());
       this.Name = in.readUTF();
       this.Category = in.readUTF();
       this.Origination_area = in.readUTF();
       this.Risk = in.readUTF();
       this.Val_so = in.readUTF();
       this.Amr_so = in.readUTF();
       this.Is_validated = QueryUserService.IntBoolean2Boolean(in.readUTF());
       this.Manager_id = Integer.valueOf(in.readUTF());
   }
}
