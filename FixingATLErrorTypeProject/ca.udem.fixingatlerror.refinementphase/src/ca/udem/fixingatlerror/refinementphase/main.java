package ca.udem.fixingatlerror.refinementphase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import anatlyzer.atl.analyser.Analyser;
import anatlyzer.atl.model.ATLModel;
import func.ResultsCollectorRQ1;
import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.metaheuristics.nsgaII.NSGAIIpostprocessing;
import jmetal.problems.MyProblempostprocessing;
import jmetal.util.JMException;

public class main   extends BaseTest{

	public static String typeoperation = null;
	public static Logger logger_; // Logger object
	public static FileHandler fileHandler_; // FileHandler object
	public static int checkoffspring = 0;
	public static int totalnumber = 1;
	public static boolean postprocessing = true;
	public static boolean HIDE_SYSTEM_ERR = false;
	public static String HIDE_SYSTEM_ERR_FILE = "err.log";

	public static void main(String[] args) throws Exception {

		if (HIDE_SYSTEM_ERR) {
			System.err.println("System.err redirected to '" + HIDE_SYSTEM_ERR_FILE + "'");
			PrintStream console = System.err;
			File file = new File(HIDE_SYSTEM_ERR_FILE);
			FileOutputStream fos = new FileOutputStream(file);
			PrintStream ps = new PrintStream(fos);
			System.setErr(ps);

			System.err.println("System.err - on " + new Date());

		}

		// First Step
		/*
		 * for(int i=0;i <1;i++) { new Class2Rel().run(args); checkoffspring=0;
		 * totalnumber=1; }
		 */
		// Second Step
		// After finished whole evaluations run this section
		// postprocessing section
		// ResultsCollectorRQ1 resultcollector= new ResultsCollectorRQ1();
		// ResultsCollectorRQ1.extractbestsolution();
		// Postproceesing for each finalresult+NSGAII.LastSolutions+.atl
		// System.out.println(NSGAII.LastSolutions);

		PostProcessingATLModelTransformation();
	}

	private static void PostProcessingATLModelTransformation() throws ClassNotFoundException, JMException {
		// TODO Auto-generated method stub

		int indexbestsolution = 0;

		ResultsCollectorRQ1 rc = new ResultsCollectorRQ1();
		System.out.println("listarray");
		ResultsCollectorRQ1.extractbestsolution();
		System.out.println(ResultsCollectorRQ1.LastSolutions);

		Settingpostprocessing s = new Settingpostprocessing();

		for (int nummut = 3; nummut <= 8; nummut++) {
			for (int numevaluation = 1; numevaluation <= 5; numevaluation++) {

				NSGAIIpostprocessing.numberoperationargument = 1;

				NSGAIIpostprocessing.inputfaultytransformation = rc.dataFolderName + "rq1" + "/" + nummut + "mutants"
						+ "/" + numevaluation + "/Results" + "/nsga" + "/solution" + "/finalresult"
						+ ResultsCollectorRQ1.LastSolutions.get(indexbestsolution) + ".atl";
				System.out.println("zzzzzzzzzzzz");
				// NSGAIIpostprocessing.inputfaultytransformation=rc.dataFolderName+"rq1"+"/"+
				// nummut+"mutants"+"/"+numevaluation+"/Results"+"/nsga"+"/solution"+"/finalresult"
				// +"48458.atl";

				NSGAIIpostprocessing.BestSolutionLocation = rc.dataFolderName + "rq1" + "/" + nummut + "mutants" + "/"
						+ numevaluation + "/Results" + "/nsga";
				NSGAIIpostprocessing.combinedfaultytransformations = rc.dataFolderName + "rq1" + "/" + nummut
						+ "mutants" + "/" + numevaluation + "/" + "_combined" + ".atl";

				Problem problem;
				Algorithm algorithm;

				// NSGAII.postprocessing = true;

				problem = new MyProblempostprocessing("Int");
				algorithm = new NSGAIIpostprocessing(problem);
				// in ra comment migirim ta ejra nakonim faghat error ra hesab konim
				SolutionSet populatio = algorithm.execute();

				indexbestsolution = indexbestsolution + 1;
				BaseTest op = new BaseTest();

				// combinedfaultytransformations
				try {
					op.typing(NSGAIIpostprocessing.combinedfaultytransformations,
							new Object[] { s.getsourcemetamodel(), s.gettargetmetamodel() },
							new String[] { s.getsecondecorename(), s.getfirstecorename() }, true, null, null);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Analyser analyser = op.getAnalyser();
				ATLModel model = analyser.getATLModel();
				System.out.println("error bestsolution");
				System.out.println(model.getErrors().getNbErrors());

				System.out.println(NSGAIIpostprocessing.BestSolutionLocation + "/" + "BestSolution" + ".atl");

			}
		}

	}

	/*
	 * private void run(String[] args) throws Exception { // test to load
	 * transformation, modify, and write back // flash va office
	 * 
	 * Problem problem; // The problem to solve Algorithm algorithm; // The
	 * algorithm to use Operator crossover; // Crossover operator Operator mutation;
	 * // Mutation operator Operator selection; // Selection operator
	 * 
	 * HashMap parameters; // Operator parameters
	 * 
	 * QualityIndicator indicators; // Object to get quality indicators
	 * 
	 * // Logger object and file to store log messages logger_ =
	 * Configuration.logger_; logger_.setLevel(Level.OFF); fileHandler_ = new
	 * FileHandler("NSGAII_main.log"); logger_.addHandler(fileHandler_);
	 * 
	 * // definetable();
	 * 
	 * indicators = null; if (args.length == 1) { Object[] params = { "Real" };
	 * problem = (new ProblemFactory()).getProblem(args[0], params); } // if else if
	 * (args.length == 2) { Object[] params = { "Real" }; problem = (new
	 * ProblemFactory()).getProblem(args[0], params); indicators = new
	 * QualityIndicator(problem, args[1]); } // if else { // Default problem //
	 * problem = new Kursawe("Real", 3); // problem = new Kursawe("BinaryReal", 3);
	 * // problem = new Water("Real"); // problem = new ZDT3("ArrayReal", 30);
	 * problem = new MyProblem("Int"); // problem = new ConstrEx("Real"); // problem
	 * = new DTLZ1("Real"); // problem = new OKA2("Real") ; } // else
	 * 
	 * algorithm = new NSGAII(problem); Setting s = new Setting();
	 * algorithm.setInputParameter("populationSize", s.getpopulationsize()); // 60
	 * 160 daghighan bayad ba on 160 nsga // va myproblem barabar bashe
	 * algorithm.setInputParameter("maxEvaluations", 2000); // 25000
	 * 
	 * // Mutation and Crossover for Real codification parameters = new HashMap();
	 * parameters.put("probability", 0.9); parameters.put("distributionIndex",
	 * 50.0);// 20 bod crossover =
	 * CrossoverFactory.getCrossoverOperator("SinglePointCrossover", parameters);
	 * 
	 * parameters = new HashMap(); // parameters.put("probability",
	 * 1.0/problem.getNumberOfVariables()) ; parameters.put("probability", 0.2);//
	 * 0.4 0.2 0.5 bod parameters.put("distributionIndex", 30.0);// 50 bod mutation
	 * = MutationFactory.getMutationOperator("BitFlipMutation" + "", parameters);
	 * 
	 * // Selection Operator parameters = null; selection =
	 * SelectionFactory.getSelectionOperator("BinaryTournament2", parameters);
	 * 
	 * // Add the operators to the algorithm algorithm.addOperator("crossover",
	 * crossover); algorithm.addOperator("mutation", mutation);
	 * algorithm.addOperator("selection", selection);
	 * 
	 * // Add the indicator object to the algorithm
	 * algorithm.setInputParameter("indicators", indicators);
	 * 
	 * // Execute the Algorithm long initTime = System.currentTimeMillis();
	 * SolutionSet populatio = algorithm.execute(); long estimatedTime =
	 * System.currentTimeMillis() - initTime;
	 * 
	 * FileWriter pw = new FileWriter(s.getnewoutputresult() +
	 * NSGAII.indexmodeltransformation + "paretosize.csv", true);
	 * 
	 * pw.append(";" + ";" + ";" + ";" + ";" + estimatedTime + ";" + 1500 + ";" +
	 * NSGAII.fixedgeneration);
	 * 
	 * pw.close(); System.out.println("khoroji");
	 * logger_.info("Total execution time: " + estimatedTime + "ms"); //
	 * population.printVariablesToFile("VAR");
	 * logger_.info("Variables values have been writen to file VAR"); //
	 * population.printRulesToFile("Rule");
	 * System.out.println(" ************* estimatedTime ************ " +
	 * estimatedTime);
	 * System.out.println(" ************* verification des solution ************ " +
	 * populatio.size());
	 * 
	 * logger_.info("Objectives values have been writen to file FUN");
	 * populatio.printObjectivesToFile( s.getpath() +
	 * "/resultformodeltransformation" + NSGAII.indexmodeltransformation + "/FUN",
	 * estimatedTime, populatio.size()); NSGAII.indexmodeltransformation =
	 * NSGAII.indexmodeltransformation + 1;
	 * 
	 * // Ta inja
	 * 
	 * /* if (indicators != null) { logger_.info("Quality indicators") ;
	 * logger_.info("Hypervolume: " + indicators.getHypervolume(population)) ;
	 * logger_.info("GD         : " + indicators.getGD(population)) ;
	 * logger_.info("IGD        : " + indicators.getIGD(population)) ;
	 * logger_.info("Spread     : " + indicators.getSpread(population)) ;
	 * logger_.info("Epsilon    : " + indicators.getEpsilon(population)) ;
	 * 
	 * int evaluations =
	 * ((Integer)algorithm.getOutputParameter("evaluations")).intValue();
	 * logger_.info("Speed      : " + evaluations + " evaluations") ; } // if
	 */

	// load the model
	/*
	 * typing(TRANSFORMATION, new Object[] { CLASS_METAMODEL, REL_METAMODEL }, new
	 * String[] { "Class", "Rel" }, true); Analyser analyser = getAnalyser();
	 * System.out.println("errors int ATL = "+analyser.getATLModel().getErrors());
	 */

	// change name of transformation module
	/*
	 * Module module = analyser.getATLModel().allObjectsOf(Module.class).get(0);
	 * module.setName("class2relational_modified");
	 * 
	 * 
	 * for (ModuleElement e : module.getElements()) { if (e instanceof MatchedRule)
	 * { // template for renaming ATL rules MatchedRule mr = (MatchedRule) e;
	 * mr.setName(mr.getName()+"_extended"); } }
	 * 
	 * // template for deleting an ATL rule module.getElements().remove(1);
	 * 
	 * // template for creating a new ATL Rule ATLFactory atlFactory =
	 * ATLFactory.eINSTANCE; MatchedRule newRule = atlFactory.createMatchedRule();
	 * newRule.setName("TestRule"); InPattern iP = atlFactory.createInPattern();
	 * OutPattern oP = atlFactory.createOutPattern(); newRule.setInPattern(iP);
	 * newRule.setOutPattern(oP); module.getElements().add(newRule);
	 * 
	 * // write back the model to text file
	 * ATLSerializer.serialize(analyser.getATLModel(),
	 * "examples/class2rel/trafo/class2relational_modified.atl");
	 */

	// }

	private void deleteDirectory(String directory, boolean recursive) {
		File folder = new File(directory);
		if (folder.exists())
			for (File file : folder.listFiles()) {
				if (file.isDirectory())
					deleteDirectory(file.getPath(), recursive);
				file.delete();
			}
		folder.delete();
	}

	/**
	 * It creates a directory.
	 * 
	 * @param folder
	 *            name of directory
	 */
	private void createDirectory(String directory) {
		File folder = new File(directory);
		while (!folder.exists())
			folder.mkdir();
	}

}
