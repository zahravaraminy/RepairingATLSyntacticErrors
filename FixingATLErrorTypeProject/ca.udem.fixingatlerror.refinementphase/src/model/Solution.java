package model;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import utils.ToolBox;

public class Solution {
	
	public static final String FITNESS_FILE_PREFIX = "SolutionFUN";
	public static final String ATL_FILE_PREFIX = "finalresult";
	public static final String ATL_SUFFIX = ".atl";

	
	int idx;
	File atlFile;
	HashMap<String, ArrayList<Mutation>> mutations = new HashMap<>();
	
	File folder;

	int numberOfMutations = -1;
	int initialNumberOfErrors = -1;
	
	/**
	 * ORDER MATTERS : [ #OPE , #ERR , COV ]
	 */
	double[] fitness;
	RunConfig cfg;
		


	/**
	 * ATTENTION ! Strongly dependent to result.txt syntax. (Example at the end of this java file)
	 * @param fd
	 * @param resultFileEntry
	 */
	public Solution(File fd, String resultFileEntry) {
		this.folder = fd;

		String[] linesMutations = resultFileEntry.substring(0, resultFileEntry.lastIndexOf("]")).split("\n");
		parseMutations(linesMutations);
		
		String[] linesAfterMutations = resultFileEntry.substring(resultFileEntry.lastIndexOf("]")+1).split("\n");
		idx = Integer.parseInt(linesAfterMutations[2]);
		fitness = parseFitness(linesAfterMutations[4], idx);
		atlFile = getATL();
	}


	private void parseMutations(String[] lines) {
		for (String l : lines) {
			if(l.startsWith(SolutionSet.MUTATION_PREFIX)) {
				Mutation m = Mutation.parseMutation(l.substring(SolutionSet.MUTATION_PREFIX.length()));
				addMutation(m);
			}
		}
	}

	
	/**
	 * If fitStr doed not contains 3 cells length double table, tries FITNESS_FILE_PREFIX + index. If not found, tries the file entered on "FitnessFunction Is" 
	 * @param fitStr
	 * @param index
	 * @return
	 */
	private double[] parseFitness(String fitStr, int index) {
		double[] res = new double[3];
		String[] fitTab = fitStr.split(" +");
		if(fitStr.split(" ").length >= 3) {
			res[0] = Double.parseDouble(fitTab[0]);
			res[1] = Double.parseDouble(fitTab[1]);
			res[2] = Double.parseDouble(fitTab[2]);
		} else {
			File f = new File(folder.getAbsolutePath()+"/"+FITNESS_FILE_PREFIX+index);
			if(!f.exists()) { //PREFIX class variable hardcoded NOT found, we try the fileName in the results.txt file
				File[] fileFUN = folder.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return name.equals(fitStr);
					}
				});
				
				if(fileFUN == null || fileFUN.length == 0) 
					throw new IllegalStateException("File '"+fitStr+"' not found in '"+folder.getAbsolutePath()+"'.");
				
				f = fileFUN[0];
				
				if(!f.exists())
					throw new IllegalStateException("File '"+fileFUN[0].getAbsolutePath()+"' does not exist.");
			}

			try {
				String FUNcontent = ToolBox.extractStringFromFile(f);
				return parseFitness(FUNcontent, index);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return res;
	}


	public File getATL(){
		return new File(ATL_FILE_PREFIX+idx+ATL_SUFFIX);
	}


	private void addMutation(Mutation m) {
		if(!mutations.containsKey(m.getType()))
			mutations.put(m.getType(), new ArrayList<>());
		mutations.get(m.getType()).add(m);
	}


	public int getNumberOfErrors() {
		return (int)fitness[1];
	}
	public int getNumberOfOperations() {
		return (int)fitness[1];
	}
	public int getCoverage() {
		return (int)fitness[2];
	}
	public File getFile() {
		return folder;
	}
	public int getIdx() {
		return idx;
	}
	
	/**
	 * file must be XXmutants/results/CONFIG/solutionY
	 * @return X
	 */
	public int getNumberOfMutations() {
		if(numberOfMutations <= 0) {
			String s = getFile().getParentFile().getParentFile().getParentFile().getName();
			numberOfMutations = Integer.parseInt(s.substring(0, s.indexOf("m")).trim());
		}
		return numberOfMutations;
	}
	
	
	@Override
	public String toString() {
		return /*numberOfMutations+"."+cfg+"."+*/folder.getName()+"("+Arrays.toString(fitness)+")";
	}





	public String getCSVLine() {
//		#ERRin;#ERRout
		return fitness[1]+";"+fitness[0]+";"+idx;
	}


	public static String getCSVHeader() {
		return "ERRout;OPE;idx";
	}
}

/*
 * results.txt file excerpt
 * 
first generation find solution
1
numberinitialerror
1
solutionsize
100
EstimatedTime
4194907ms
numberIteration
4000
------------------------------------------------------------------------------
next result
[
-- MUTATION "Deletion of Binding" tables in s (line 32:1-36:2 of original transformation)
]
index
29
FitnessFunction Is 
1.0 0 13
number-error
0
------------------------------------------------------------------------------

next result
[
-- MUTATION "Deletion of Binding" tables in s (line 32:1-36:2 of original transformation)
, 
-- MUTATION "Creation of Binding" tables in s (line 31:1-40:2 of original transformation)
]
index
139
FitnessFunction Is 
2.0 0 12
number-error
0
------------------------------------------------------------------------------

next result
[]
index
484
FitnessFunction Is 
0.0 1 12
number-error
1
------------------------------------------------------------------------------

next result
[]
index
3990
FitnessFunction Is 
0.0 1 12
number-error
1
------------------------------------------------------------------------------

next result
[
-- MUTATION "Deletion of Binding" tables in s (line 32:1-36:2 of original transformation)
]
index
3998
FitnessFunction Is 
1.0 0 13
number-error
0
------------------------------------------------------------------------------

next result
[
-- MUTATION "Creation of Binding" tables in s (line 31:1-40:2 of original transformation)
, 
-- MUTATION "Deletion of Binding" tables in s (line 32:1-36:2 of original transformation)
]
index
4000
FitnessFunction Is 
2.0 0 12
number-error
0
------------------------------------------------------------------------------

 * 
 * 
 */ 
