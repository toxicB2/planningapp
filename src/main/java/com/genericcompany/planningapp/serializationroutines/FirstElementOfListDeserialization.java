package com.genericcompany.planningapp.serializationroutines;

import java.lang.reflect.Type;

import com.genericcompany.planningapp.dto.UserDTO;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

//Currently the following is not in use

public class FirstElementOfListDeserialization implements JsonDeserializer<UserDTO> 
{
	  @Override
	  public UserDTO deserialize(JsonElement json, 
	                           Type typeOfT,
	     JsonDeserializationContext context) throws JsonParseException 
	  {
	      /*Generic<T> x = new Generic<T>(T.class);
	      T modelIn = x.buildOne();        

	      JsonArray jArray = (JsonArray) json; // get json array
	      JsonObject jsonObject = (JsonObject) jArray.get(0); // get first object

	      // do what you want with the first object
	      myModel.setParameter(jsonObject.get("parameter").getAsInt());

	      // ignore next json objects*/
	      return new UserDTO();
	  }
}
