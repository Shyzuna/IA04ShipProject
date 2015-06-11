package fr.shipsimulator.structure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GenericMessageContent<T> {

	public List<T> content = new ArrayList<T>();
	
	public String serialize(){
		ObjectMapper mapper = new ObjectMapper();
		String serialized = "";
		try {
			serialized = mapper.writeValueAsString(content);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return serialized;
	}
	
	public List<T> deserialize(String serialized){
		ObjectMapper mapper = new ObjectMapper();
		List<T> deserialized = new ArrayList<T>();
		try {
			deserialized = mapper.readValue(serialized, new TypeReference<List<T>>(){});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return deserialized;
	}	
}