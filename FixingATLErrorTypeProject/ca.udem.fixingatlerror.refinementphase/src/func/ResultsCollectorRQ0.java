package func;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.stream.Collectors;

import model.RunConfig;
import model.SolutionSet;
import utils.ToolBox;

public class ResultsCollectorRQ0 {
	static String MTname = "CD2RD";//PNML2PN CD2RD

	static	String dataFolderName = "R://svn/Models-Varaminy2019-data/"+MTname+"/";
	static	String sourceFolderName = "R://svn/Models-Varaminy2019-data/"+MTname+"/Mutants";

	
	
	public static void main(String[] args) {
		ToolBox.initializeRandom();
		
		String rq0FolderName = dataFolderName+"/rq0";
		
		File dataFolder = new File(dataFolderName);
		if(! dataFolder.exists() || !dataFolder.isDirectory())
			throw new IllegalStateException("Invalid folder: "+dataFolder.getAbsolutePath());
		System.out.println("Folder: "+dataFolder.getAbsolutePath());

		
		File rq0Folder = new File(rq0FolderName);
		if(! rq0Folder.exists() || !rq0Folder.isDirectory())
			throw new IllegalStateException("Invalid folder: "+rq0Folder.getAbsolutePath());
		
		File[] mutantsFolders = rq0Folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.isDirectory();
			}
		});
		Arrays.sort(mutantsFolders, new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				String o1Number = o1.getName().substring(0,  o1.getName().indexOf("m"));
				String o2Number = o2.getName().substring(0,  o2.getName().indexOf("m"));
				return o1Number.compareTo(o2Number);
			}
		});
		System.out.println("  -> "+mutantsFolders.length+" mutants folders.");
		System.out.println(Arrays.toString(mutantsFolders).replaceAll(", ", "\n "));
		System.out.println();
		
		/*
		 * Collect and parse solutions
		 */
		HashMap<String, ArrayList<SolutionSet>> solutionsConfigurations = new HashMap<>();
		for (File mutantsFolder : mutantsFolders) {
			System.out.println("Mutants folder: "+mutantsFolder.getAbsolutePath());
			ArrayList<SolutionSet> solSets = collectSolutions(solutionsConfigurations, new File(mutantsFolder.getAbsolutePath() + "/results/nsga/"));
			System.out.println("   NSGA."+Arrays.toString(computeAvgFitness(solSets)));
			solSets = collectSolutions(solutionsConfigurations, new File(mutantsFolder.getAbsolutePath() + "/results/rnd/"));
			System.out.println("   RND."+Arrays.toString(computeAvgFitness(solSets)));
			System.out.println();
		}

		System.out.println("\n\nSolutions:");
		System.out.println(format(solutionsConfigurations));
		
//		HashMap<String, double[]> avgFitnessConfigurations = new HashMap<>();
		
		ArrayList<String> configList = new ArrayList<>(solutionsConfigurations.keySet());
		configList.sort(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				String[] o1t = o1.split("\\.");
				String[] o2t = o2.split("\\.");
				int cmp = o1t[0].compareTo(o2t[0]);
				if(cmp == 0)
					cmp = o1t[1].compareTo(o2t[1]);
				return cmp;
			}
		});
		
		
		
		String csv = SolutionSet.CSV_HEADER;
		
		for (File mutantsFolder : mutantsFolders) {
			String numberOfMutants = mutantsFolder.getName().substring(0,  mutantsFolder.getName().indexOf("m"));
			csv += getCSVLines(solutionsConfigurations, RunConfig.NSGA+"."+numberOfMutants);
			csv += getCSVLines(solutionsConfigurations, RunConfig.RND+"."+numberOfMutants);
		}
		
		try {
			File out = new File(rq0Folder.getAbsolutePath()+"/rq0.csv");
			ToolBox.write(csv, out);
			System.out.println("Results writen in '"+out.getAbsolutePath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//\data\experiment\rq0\4mutants\results\nsga\solution1
		
//		System.out.println(formatAvgs(avgFitnessConfigurations));
	
	}

	private static String getCSVLines(HashMap<String, ArrayList<SolutionSet>> solutionsConfigurations, String config) {
		String csv = "";
		ArrayList<SolutionSet> solSets = solutionsConfigurations.get(config);
		for (SolutionSet solSet : solSets) 
			csv += solSet.getCSVLinesMinErrors();
		return csv;
	}

	private static  double[] computeAvgFitness(ArrayList<SolutionSet> solSets) {
		double[] res = new double[3];
		int numberOfSols = 0;
		for (SolutionSet s : solSets) {
			for (int i = 0; i < res.length; i++) {
				res[i] += (s.getAvgFitness()[i]);// * s.getSolutions().size());
				
			}
			numberOfSols += 1 ; //s.getSolutions().size();
		}

		for (int i = 0; i < res.length; i++)
			res[i] = res[i] / numberOfSols;
		return res;
	}

	private static ArrayList<SolutionSet> collectSolutions(HashMap<String, ArrayList<SolutionSet>> solutions, File folder) {
		ArrayList<SolutionSet> res = new ArrayList<>();
		File[] nsgaSolutionFolders = folder.listFiles();
		if (nsgaSolutionFolders == null) {
			System.out.println(folder.getAbsolutePath() + ": NO solutions");
		} else {
			File[] nsgaSolutions = folder.listFiles();
			for (File nsgaSol : nsgaSolutions) {
				SolutionSet nsgaSet = new SolutionSet(nsgaSol);
				if (nsgaSet.getSolutions().size() > 0) {
					if (solutions.get(nsgaSet.getConfiguration()) == null)
						solutions.put(nsgaSet.getConfiguration(), new ArrayList<>());
					solutions.get(nsgaSet.getConfiguration()).add(nsgaSet);
					res.add(nsgaSet);
				}
			}
		}
		return res;
	}

	static String format(Collection<SolutionSet> c) {
	  String s = c.stream().map(SolutionSet::toStringSimple).collect(Collectors.joining(","));
	  return String.format("[%s]", s);
	}
	
	static String format(HashMap<String, ArrayList<SolutionSet>> css) {
		
		String res = "";
		
		for (String keyConfig : css.keySet()) {
			res+= keyConfig +"\n";
			res += "  "+format(css.get(keyConfig)) +"\n";
		}
		
	  return String.format("[%s]", res);
	}

	static String formatAvgs(HashMap<String, double[]> avgs) {
		
		String res = "";
		
		for (String keyConfig : avgs.keySet()) {
			res+= keyConfig +"\n";
			res += "  "+Arrays.toString(avgs.get(keyConfig)) +"\n";
		}
		
	  return String.format("[%s]", res);
	}

	
}
