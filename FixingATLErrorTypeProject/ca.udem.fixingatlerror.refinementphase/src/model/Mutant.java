package model;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import func.GenerateMutants;
import utils.ToolBox;

public class Mutant {
	public static Map<String, Mutant> mutantsTypes = new HashMap<>();;
	
	public static String HEADER;

	MutationType type;
	
	public File f;
	public File containingFolder;
	
	public String content;
	public String mutationDefinition;
	

	public Mutant(MutationType mutationType, File file) {
		type = mutationType;
		f = file;
		mutantsTypes.put(f.getName(), this);
		containingFolder = f.getParentFile();
		try {
			content = ToolBox.extractStringFromFile(GenerateMutants.LINE_ESCAPED, file);
			String fullContent = ToolBox.extractStringFromFile(file);
			mutationDefinition = ToolBox.getMutationDefinitionLine(fullContent);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(HEADER == null) {
			try {
				initHeader(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void initHeader(File f) throws IOException {
		String fullContent = ToolBox.extractStringFromFile(f);
		String[] tmp = fullContent.split("\n");
		HEADER = "";
		for (int i = 0; i < 4; i++) {
			System.out.println(tmp[i]);
			HEADER += tmp[i] + "\n";
		}
	}
	@Override
	public String toString() {
		return type.name+"#"+f.getName();
	}
	
	
}
