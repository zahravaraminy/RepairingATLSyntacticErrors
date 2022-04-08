package model;

import java.util.ArrayList;
import java.util.HashMap;

public class Mutation {
	
	
	public static HashMap<String, ArrayList<Mutation>> mutations = new HashMap<>();
	
	
	String type;
	String parameters;
	String location;
	
	public Mutation(String type, String parameters, String location) {
		this.type = type;
		this.parameters = parameters;
		this.location = location;
		registerMutation(this);
	}
	
	
	private static void registerMutation(Mutation m) {
		if(!mutations.containsKey(m.type))
			mutations.put(m.type, new ArrayList<>());
		mutations.get(m.type).add(m);
	}


	public static Mutation parseMutation(String mTxt) {
		String type = mTxt.split("\"")[1];
		String parameters = mTxt.substring(mTxt.lastIndexOf("\"")+2, mTxt.indexOf("("));
		String location = mTxt.substring(mTxt.lastIndexOf("(")+1, mTxt.length() - 1);
		return new Mutation(type, parameters, location);
	}
	
	public String getType() {
		return type;
	}
}
