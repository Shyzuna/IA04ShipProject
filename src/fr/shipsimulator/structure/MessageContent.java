package fr.shipsimulator.structure;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageContent {

	public List<Integer> content = new ArrayList<Integer>();
	
	public String serialize(){
		ObjectMapper mapper = new ObjectMapper();
		String serialized = "";
		try {
			serialized = mapper.writeValueAsString(content);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return serialized;
	}
	
}
