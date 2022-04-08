package func;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import model.Mutant;
import model.MutationType;
import utils.ToolBox;
import utils.diff_match_patch;




public class GenerateMutants {
	public static final int NB_TREATMENTS = 5;
	public static final String WORKSPACE_ROOT_FOLDER_NAME   = "R://svn/material/ATLMutator/";  // 3source/ORIGINAL_FILE_NAME = original transformation

	
	public static final String ORIGINAL_TRANSFORMATION_FILE_NAME = "reference.atl";
	public static final String COMBINED_TRANSFORMATION_FILE_NAME = "_combined.atl";
	public static int LINE_ESCAPED = 7;
	
	static List<MutationType> mutationTypes; 
	
	
	public static void main(String[] args) {
		ToolBox.initializeRandom();
		
		/*
		 * INITIALIZATION
		 */
		String sourceFolderName = WORKSPACE_ROOT_FOLDER_NAME+"/source";
		String outputMutantsFolderName = WORKSPACE_ROOT_FOLDER_NAME+"/output";
		
		File rootFolder = new File(WORKSPACE_ROOT_FOLDER_NAME);
		if(! rootFolder.exists() || !rootFolder.isDirectory())
			throw new IllegalStateException("Invalid folder: "+rootFolder.getAbsolutePath());

		
		File sourceFolder = new File(sourceFolderName);
		File outputMutantsFolder = new File(outputMutantsFolderName);
		if(outputMutantsFolder.exists())
			outputMutantsFolder.delete(); 
		outputMutantsFolder.mkdir();
		
		if(!sourceFolder.exists() || !sourceFolder.isDirectory())
			throw new IllegalStateException("Invalid folder: "+sourceFolder.getAbsolutePath());

		System.out.println("Folder: "+sourceFolder.getAbsolutePath()+"  ("+sourceFolder.listFiles().length+" files)");
	
		File originalMT = new File(sourceFolder.getAbsolutePath() + "/" + ORIGINAL_TRANSFORMATION_FILE_NAME);
		try {
			Mutant.initHeader(originalMT);
		} catch (IOException e1) {	e1.printStackTrace(); }
		System.out.println("Original MT: "+originalMT.getAbsolutePath());
		
		mutationTypes = getMutationTypes(sourceFolder);
		

		/*
		 * MUTANT LISTING : lists of {2, 3, 4 ,... ,8} mutants in folder output
		 * 
		 * !!! WARNING OVERWRITE RISK !!! Clean output folder first !!
		 */
		generateMutantSets(outputMutantsFolder, mutationTypes);

		/*
		 * COMBINATION of mutants (in output folder)
		 */
		HashMap<String, ArrayList<File>> mutantsCombined = new HashMap<>();
		File[] numberOfMutations = outputMutantsFolder.listFiles();
		for (File numberMutantsFolder : numberOfMutations) {
			File[] exps = numberMutantsFolder.listFiles();
			ArrayList<File> combineds = new ArrayList<>(exps.length);
			for (File e : exps) {
				if(!e.getName().matches("rq*")) {
					File folderMutants = new File(e.getAbsolutePath()+"/mutants");
					combineMutantsInFolder(folderMutants,  originalMT );
					combineds.add(e);
				}
			}
			
			mutantsCombined.put(numberMutantsFolder.getName(), combineds);
		}
		
		/*
		 * BUILD RQ0 folder
		 */
		System.out.println("Bulding RQ0 folders");
		buildRQ0Folders(new File(rootFolder.getAbsolutePath()+"/output"), mutantsCombined);
		
		/*
		 * BUILD RQ1 folder
		 */
		System.out.println("Bulding RQ1 folders");
		buildRQ1Folders(new File(rootFolder.getAbsolutePath()+"/output"), mutantsCombined);
		
		
		
	}


	private static void buildRQ0Folders(File destFolder, HashMap<String, ArrayList<File>> mutantsCombined) {
		File rq0 = new File(destFolder.getAbsolutePath()+"/rq0/");
		if(rq0.exists() && rq0.listFiles().length > 0) {
			System.out.println("!! WARNING !! Output directory for RQ0 not empty. '"+rq0.getAbsolutePath()+"'");
			boolean continu = continueOrNotPrompt();
			if(!continu) return;
		}
		rq0.mkdirs();
		try {
			ToolBox.copyFolder(mutantsCombined.get("2mutants").get(0), new File(rq0.getAbsolutePath()+"/2mutants/"));
			ToolBox.copyFolder(mutantsCombined.get("4mutants").get(0), new File(rq0.getAbsolutePath()+"/4mutants/"));
			ToolBox.copyFolder(mutantsCombined.get("6mutants").get(0), new File(rq0.getAbsolutePath()+"/6mutants/"));
			ToolBox.copyFolder(mutantsCombined.get("8mutants").get(0), new File(rq0.getAbsolutePath()+"/8mutants/"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void buildRQ1Folders(File destFolder, HashMap<String, ArrayList<File>> mutantsCombined) {
		File rq1 = new File(destFolder.getAbsolutePath()+"/rq1/");
		if(rq1.exists() && rq1.listFiles().length > 0) {
			System.out.println("!! WARNING !! Output directory for RQ1 not empty. '"+rq1.getAbsolutePath()+"'");
			boolean continu = continueOrNotPrompt();
			if(!continu) return;
		}
		rq1.mkdirs();
		try {
			
			buildMutantsFolders(mutantsCombined, rq1, "3mutants");
			buildMutantsFolders(mutantsCombined, rq1, "4mutants");
			buildMutantsFolders(mutantsCombined, rq1, "5mutants");
			buildMutantsFolders(mutantsCombined, rq1, "6mutants");
			buildMutantsFolders(mutantsCombined, rq1, "7mutants");
			buildMutantsFolders(mutantsCombined, rq1, "8mutants");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private static boolean continueOrNotPrompt() {
		String res = "";
		while(!(res.equalsIgnoreCase("Y") || res.equalsIgnoreCase("N"))) {
			System.out.println("Continue ? Y / N");
			res = new Scanner(System.in).nextLine();
		}
		if(res.equalsIgnoreCase("N")) {
			System.out.println("Aborded by user.");
			return false;
		}
		return true;
	}


	private static void buildMutantsFolders(HashMap<String, ArrayList<File>> mutantsCombined, File rq0, String Nmutants) throws IOException {
		int i = 1;
		for (File fMutant : mutantsCombined.get(Nmutants)) {
			File folderMutants = new File(rq0.getAbsolutePath()+"/"+Nmutants+"/"+ (i++)+"/");
			folderMutants.mkdirs();
			ToolBox.copyFolder(fMutant, folderMutants);
		}
	}




	/**
	 * Combines mutants contained in folder, from an original file originalMT 
	 * @param folder (mutants folder. Output will be written in parent file of 'folder')
	 * @param originalMT
	 */
	private static void combineMutantsInFolder(File folder, File originalMT) {
		List<Mutant> mutants = getMutants(folder);
		
		System.out.println("\nCombination :");
		for (Mutant m : mutants) 
			System.out.println("  - "+m.f.getName());
		System.out.println();
		
		try {
			String originalString = ToolBox.extractStringFromFile(LINE_ESCAPED, originalMT);
			String header = Mutant.HEADER;
			LinkedList<diff_match_patch.Patch> patches = new LinkedList<>();
			diff_match_patch dmp = new diff_match_patch();
			
			for (Mutant m : mutants) {
				LinkedList<diff_match_patch.Diff> diffs = dmp.diff_main(originalString, m.content);
				LinkedList<diff_match_patch.Patch> patches2 = dmp.patch_make(originalString, diffs);
				patches.addAll(patches2);
				
				header += m.mutationDefinition+"\n";
			}
			
			Object[] resPatch = dmp.patch_apply(patches, originalString);
			String patchedText =  header + "\n" + (String) resPatch[0];
			
			
			File combinationFile = new File(folder.getParentFile().getAbsolutePath() + "/"+COMBINED_TRANSFORMATION_FILE_NAME);
			writeFile(combinationFile, patchedText);
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}




	private static void writeFile(File combinationFile, String combi) {
		try {
			combinationFile.createNewFile();
			FileWriter fw = new FileWriter(combinationFile);
				fw.write(combi);
			fw.close();
			System.out.println("Combination written in '"+combinationFile.getAbsolutePath()+"'");
		} catch (IOException e) {
			System.err.println("Error occured during writing of '"+combinationFile.getAbsolutePath()+"'.");
			e.printStackTrace();
		}
	}


	private static List<Mutant> getMutants(File f) {
		File[] mutantsFile = ToolBox.getFilesWithNameEndingWith(f, "atl");
		List<Mutant> res = new ArrayList<>(mutantsFile.length);
		for (int i = 0; i < mutantsFile.length; i++) {
			Mutant m = Mutant.mutantsTypes.get(mutantsFile[i].getName());
			if(m == null )
				System.out.println("Mutant file: "+mutantsFile[i].getName()+"\nMutant eluded (type not found). Clean output folder and run again." );
			else
				res.add(Mutant.mutantsTypes.get(mutantsFile[i].getName()));
		}
		return res;
	}

	
	

	private static void generateMutantSets(File outputFolder, List<MutationType> mutationTypes) {
		for (int i = 1; i <= NB_TREATMENTS; i++) {
			
			int MAX_MUTATIONS = 8;
			
			List<List<Mutant>> mutantsList = new ArrayList<>();
			for (int j = 2; j <= MAX_MUTATIONS; j++) {
				mutantsList.add(getRandomMutants(j, mutationTypes));
				
			}
			
			for (List<Mutant> ms : mutantsList) {
				File folder = new File(outputFolder.getAbsolutePath()+"/"+ms.size()+"mutants");
				if(folder.exists())
					folder.delete();
				folder.mkdir();
				
				File folderTreatment = new File(folder.getAbsolutePath()+"/"+i+"/mutants");
				if(folderTreatment.exists())
					folderTreatment.delete();
				folderTreatment.mkdirs();
				
				for (Mutant m : ms) {
					ToolBox.copyFile(m.f, folderTreatment, m.f.getName(), false);
				}
			}
		}
	}
	
	public static List<Mutant> getRandomMutants(int numberOfMutants, List<MutationType> mutationTypes) {
		ArrayList<Mutant> res = new ArrayList<>(numberOfMutants);
		List<MutationType> available = new ArrayList<>(mutationTypes);
//		for (int i = 0; i < numberOfMutants; i++) {
		int i = 0;
		while(res.size() < numberOfMutants) {
			if( i++ >= mutationTypes.size())
				available.addAll(mutationTypes);
			MutationType mt = ToolBox.getRandom(available);
			available.remove(mt);
			Mutant m = ToolBox.getRandom(mt.mutants);
			res.add(m);
		}
		return res;
	}

	public static List<MutationType> getMutationTypes(File folder) {
		List<MutationType> res = new ArrayList<>();

		File[] mutationTypes = folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory() && !pathname.getName().matches("((source)|(target))metamodel");
			}
		});

		for (File file : mutationTypes) {
			System.out.println(file.getAbsolutePath());
			File[] mutants = ToolBox.getFilesWithNameEndingWith(file, "atl");
			MutationType m = new MutationType(file.getName(), mutants);
			res.add(m);
		}
		return res;
	}

	
	
	public static Map<String, ArrayList<File>> getATLFiles(File folder){
		Map<String, ArrayList<File>> res = new HashMap<>();
		
		
		File[] mutationTypes = folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});
		
		for (File file : mutationTypes)  {
			res.put(file.getName(), new ArrayList<>());
			
			File[] mutants = ToolBox.getFilesWithNameEndingWith(file, "atl");
			for (File m : mutants) {
				res.get(file.getName()).add(m);
			}
		}
		System.out.println(res);
		return res;
	}

	


}
