package model;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import func.GenerateMutants;
import jmetal.metaheuristics.nsgaII.NSGAII;
import utils.ToolBox;
import utils.diff_match_patch;

public class SolutionSet {
	
	public static String CSV_HEADER = "CFG;MUT;NUM;ERRin;"+Solution.getCSVHeader()+";GEN\n";

	public static String SOLUTION_SEPARATOR = "------------------------------------------------------------------------------";
	public static String MUTATION_PREFIX = "-- MUTATION";
	
	public static final String RESULT_FILE_NAME = "result.txt";

	int numberOfMutations = -1;
	File folder, resultFile, mutantFile;
	String resultFileContent;
	int experimentNumber;
	
	double[] avgFitness = null;
	RunConfig cfg;
	
	ArrayList<Solution> solutions;
	ArrayList<Solution> minErrorsSolutions;
	int minErrors = Integer.MAX_VALUE;
	private int firstGenerationWithoutError;
	private int initialNumberOfErrors = -1;
	
	public SolutionSet(File fd) {
		this.folder = fd;
		try {
			experimentNumber = Integer.parseInt(fd.getParentFile().getParentFile().getParentFile().getName());
		} catch (NumberFormatException e1) {
			// Run for RQ0 
		}
		cfg = RunConfig.valueOf(fd.getParentFile().getName().toUpperCase());
		numberOfMutations = getNumberOfMutations();
		
		
		
		mutantFile = new File(folder.getParentFile().getParentFile().getParentFile().getAbsolutePath()+"/"+GenerateMutants.COMBINED_TRANSFORMATION_FILE_NAME);
		resultFile = new File(folder.getAbsolutePath()+"/"+RESULT_FILE_NAME);
		
		if(resultFile == null || !resultFile.exists()) 
			throw new IllegalAccessError("Folder '"+folder.getAbsolutePath()+"' does not contain "+RESULT_FILE_NAME);
		
				
		try {
			solutions = parseResultFile();
			getMinErrorsSolutions();//Initialize minErrorSolutions and minErrors
			getAvgFitness();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(folder + ": " + solutions.size() + " sol. (" + getMinErrorsSolutions().size() + " with " + minErrors + " errors) "
				+ Arrays.toString(avgFitness));
	}
	
	private double[] computeAvgFitness() {
		double[] res = new double[3];
		ArrayList<Solution> sols = this.getMinErrorsSolutions();
		
		for (Solution s : sols) 
			for (int i = 0; i < res.length; i++) 
				res [i] += s.fitness[i] ;
			
		for (int i = 0; i < res.length; i++) 
			res[i] = res[i] / sols.size();
		return res;
	}

	ArrayList<Solution> getMinErrorsSolutions() {
		if(minErrorsSolutions == null) {
			minErrorsSolutions = new ArrayList<>();
			for (Solution s : solutions) {
				if(s.getNumberOfErrors() < minErrors)
					minErrors = s.getNumberOfErrors();
				if(minErrors == 0)
					break;
			}
			for (Solution s : solutions) {
				if(s.getNumberOfErrors() == minErrors) {
					minErrorsSolutions.add(s);
				}
			}
			
			Collections.sort(minErrorsSolutions, new Comparator<Solution>() {
				@Override
				public int compare(Solution o1, Solution o2) {
					return Double.compare(o1.fitness[0], o2.fitness[0]);
				}
			});
		}
		return minErrorsSolutions;
	}
	
	
	
	private ArrayList<Solution> parseResultFile() throws IOException {
		ArrayList<Solution> solutions = new ArrayList<>();
		
		resultFileContent = ToolBox.extractStringFromFile(resultFile);
		String[] tabContent = resultFileContent.split(SOLUTION_SEPARATOR);
		//String 0 = header;
		String header = tabContent[0];
		String[] headerTab = header.trim().split("\n");
		
		
		for (int i = 0; i < headerTab.length; i++) {
			if(headerTab[i].startsWith("first generation"))
				firstGenerationWithoutError = Integer.parseInt(headerTab[i+1].trim());
			if(headerTab[i].startsWith("numberinitial"))
				initialNumberOfErrors = Integer.parseInt(headerTab[i+1].trim());
		}
		
		String resultSolutions[] = new String[tabContent.length-1];
		for (int i = 1; i < tabContent.length; i++) 
			resultSolutions[i-1] = tabContent[i].trim();
		
		for (int i = 0; i < resultSolutions.length; i++) {
			if(!resultSolutions[i].trim().isEmpty()) {
				Solution s = new Solution(folder, resultSolutions[i]);
				s.initialNumberOfErrors = initialNumberOfErrors;
				s.cfg = cfg;
				s.numberOfMutations = numberOfMutations;
				solutions.add(s);
				
			}
		}
		return solutions;
	}
	

	public String getConfiguration() {
		return cfg+"."+numberOfMutations;
	}
	
	public RunConfig getCfg() {
		return cfg;
	}
	/**
	 * file must be XXmutants/results/CONFIG/
	 * @return X
	 */
	public int getNumberOfMutations() {
		if(numberOfMutations <= 0) {
			
			String[] pathTab = getFolder().getAbsolutePath().split("\\\\");
			for (String s : pathTab) {
				if(s.endsWith("mutants")) {
					numberOfMutations = Integer.parseInt(s.substring(0, s.indexOf("m")).trim());
					break;
				}
			}
			
//			String s = getFolder().getParentFile().getParentFile().getParentFile().getName();
//			numberOfMutations = Integer.parseInt(s.substring(0, s.indexOf("m")).trim());
		}
		return numberOfMutations;
	}
	
	
	public File getFolder() {
		return folder;
	}

	
	public double[] getAvgFitness() {
		if(avgFitness == null)
			avgFitness = computeAvgFitness();
		return avgFitness;
	}
	
	public ArrayList<Solution> getSolutions() {
		return solutions;
	}
	
	public int getFirstGenerationWithoutError() {
		return firstGenerationWithoutError;
	}
	
	public int getInitialNumberOfErrors() {
		return initialNumberOfErrors;
	}
	
	public int getExperimentNumber() {
		return experimentNumber;
	}
	
	@Override
	public String toString() {
		String res = "("+numberOfMutations+"."+cfg+":{";
		if(solutions.size() == 0)
			return res += "0})";
		
		for (Solution s : solutions) 
			res += s.toString() + ", ";
		res = res.substring(0, res.length()-2);
		res += "})";
		return res;
	}
	
	public String toStringSimple() {
		String res = "("+cfg+"."+numberOfMutations+":{"+getMinErrorsSolutions().size()+"})";
		return res;
	}

	
	/**
	 * CSV header : CFG;MUT;NUM;ERRin;ERRout;OPE;GEN\n
	 * @return
	 */
	public String getCSVLinesMinErrors() {
		String res = "";
		
		// Sort by size (OPE)
		ArrayList<Solution> sols = new ArrayList<>(getMinErrorsSolutions());
		sols.sort(new Comparator<Solution>() {
			@Override
			public int compare(Solution o1, Solution o2) {
				return Double.compare(o1.fitness[0], o2.fitness[0]);
			}
		});
		
		for (Solution s : sols) {
			res += cfg+";"+numberOfMutations+";"+experimentNumber+";"+initialNumberOfErrors+";"+s.getCSVLine()+";"+getFirstGenerationWithoutError()+"\n";
			break; // ONLY THE FIRST !
		}
		return res;
	}

	public File generateDIFFBetweenBestAndOriginal(File originalMT) throws IOException {
		cleanFilesEndsStartsWith("-o.html");
		
		String originalString = ToolBox.extractStringFromFile(originalMT);
		
		// Sort by size (OPE)
		ArrayList<Solution> sols = new ArrayList<>(getMinErrorsSolutions());
		sols.sort(new Comparator<Solution>() {
			@Override
			public int compare(Solution o1, Solution o2) {
				return Double.compare(o1.fitness[0], o2.fitness[0]);
			}
		});
		Solution s = getMinErrorsSolutions().get(0);
				
		String solutionString = ToolBox.extractStringFromFile(new File(folder.getAbsolutePath()+"/"+s.getATL()));
		
		String diffClean = getCleanDiff(originalString, solutionString);
		
		
		NSGAII.LastSolutions.add(s.getIdx());
		
		File diffFile = new File(folder.getAbsolutePath()+"/diff"+s.getIdx()+"-o.html");
		ToolBox.write(diffClean,	diffFile);
		System.out.println("DIFFS with original written in '"+diffFile+"'"	);
		return diffFile;
	}

	private String getCleanDiff(String originalString, String solutionString) {
		diff_match_patch dmp = new diff_match_patch();
		LinkedList<diff_match_patch.Diff> diffs = dmp.diff_main(originalString, solutionString);
		dmp.diff_cleanupSemantic(diffs);
		String diffClean = dmp.diff_prettyHtml(diffs);
		return diffClean;
	}

	public File generateDIFFBetweenBestAndMutant() throws IOException {
		cleanFilesEndsStartsWith("-m.html");

		String originalString = ToolBox.extractStringFromFile(mutantFile);
		
		// Sort by size (OPE)
		ArrayList<Solution> sols = new ArrayList<>(getMinErrorsSolutions());
		sols.sort(new Comparator<Solution>() {
			@Override
			public int compare(Solution o1, Solution o2) {
				return Double.compare(o1.fitness[0], o2.fitness[0]);
			}
		});
		
		Solution s = getMinErrorsSolutions().get(0);
				
		String solutionString = ToolBox.extractStringFromFile(new File(folder.getAbsolutePath()+"/"+s.getATL()));
		
		String diffClean = getCleanDiff(originalString, solutionString);
		
		
		File diffFile = new File(folder.getAbsolutePath()+"/diff"+s.getIdx()+"-m.html");
		ToolBox.write(diffClean,	diffFile);
		System.out.println("DIFFS with mutant written in '"+diffFile+"'"	);
		return diffFile;
	}

	private void cleanFilesEndsStartsWith(String match) {
		for (File f : folder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(match);
			}
		})) {
			f.delete();
		}
	}

}
