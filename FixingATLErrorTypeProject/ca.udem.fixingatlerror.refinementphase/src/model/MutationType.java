package model;
import java.io.File;
import java.util.ArrayList;

public class MutationType {
	public String name;
	public ArrayList<Mutant> mutants;
	
	
	public MutationType(String name, File... mutants ) {
		this.name = name;
		this.mutants = new ArrayList<>();
		for (File file : mutants) {
			this.mutants.add(new Mutant(this, file));
		}
	}
	
	@Override
	public String toString() {
		return name+"("+mutants.size()+")";
	}
}
