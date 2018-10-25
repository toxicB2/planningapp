package com.gnericcompany.planningapp.dto.headernames;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.genericcompany.planningapp.dto.InterfaceDTO;

@SuppressWarnings("serial")

@ManagedBean(name="modelDTOHeaderNames")
@RequestScoped

public class ModelDTOHeaderNames 
{
	static private Map<String, String> headersModelDTO= new HashMap<String, String>()
			{{ put("id","ID");
               put("name","Name");
               put("category","Category");
               put("origination_area", "Origination area");
               put("risk","Risk");
               put("val_so","Validation Sign-off");
               put("amr_so", "AMR Sign-off");
               put("is_validated", "Has the model been validated");
               put("manager_id", "Manager ID");
               put("concat(manager.first_name,' ', manager.last_name)", "Manager");
			}};
    
	static private Map<String, String> headersManagerDTO= new HashMap<String, String>()
			{{ put("id","ID");
			   put("last_name","Last Name");
               put("first_name","First Name");
               put("title", "Title");
               put("LogInCredentials_Roles_id","Roles ID");
               put("LogInCredentials_id","Credentials ID");
			}};
			
	static private Map<String, String> headersRolesDTO= new HashMap<String, String>()
			{{ put("id","ID");
 
			}};
			
	static private Map<String, String> headersAnalystDTO= new HashMap<String, String>()
			{{ put("id","ID");
 
			}};
			
	static private Map<String, String> headersLoginCredentialsDTO= new HashMap<String, String>()
			{{ put("id","ID");
 
			}};
			
	public Map<String, String> headers;
	
	public ModelDTOHeaderNames(InterfaceDTO DTOIN)
	{
        switch (DTOIN.getDTODB()) {
            case "manager":  headers = headersManagerDTO;
                     break;
            case "roles":  headers = headersRolesDTO;
                     break;
            case "analyst":  headers = headersAnalystDTO;
                     break;
            case "logincredentials":  headers = headersLoginCredentialsDTO;
                     break;
            case "model":  headers = headersModelDTO;
                     break;
            default:/* "model"*/ headers = headersModelDTO;
                     break;
        }
		
	} 
	public List<Map<String, Object>> InitializeProperHeaderNames(List<Map<String, Object>> listIn)
	{
		Map<String,Object> searchMap = listIn.get(0);
		Map<String,Object> newSearchMap = new HashMap<String, Object>();
		try
		{
			searchMap.entrySet()
			         .forEach(sMap->{
			        	                String tempString;
			        	                List<String> tempList = headers.entrySet()
			        	                                               .stream()
			        	                                               .filter(x->x.getKey()
			        	                                              		       .equals(sMap.getKey()))
			        	                                               .map(Map.Entry::getValue)
			                                                           .collect(java.util.stream.Collectors.toList());
			        	                if(tempList.size()>0)
			        	                {
			        	                    tempString = tempList.get(0).toString();
			        	                }
			        	                else
			        	                {
			        	                	tempString = sMap.getKey();
			        	                }
			        	                newSearchMap.put(tempString, sMap.getValue());	 
			                        }
					         );
			listIn.remove(0);
			listIn.add(0, newSearchMap);
			return listIn;
		}
		catch (Exception e)
		{
			return listIn;
		}
	} 
}
