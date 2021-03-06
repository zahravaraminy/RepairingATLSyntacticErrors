package jmetal.problems;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.core.ModelFactory;
import org.eclipse.m2m.atl.core.emf.EMFModel;
import org.eclipse.m2m.atl.core.emf.EMFModelFactory;
import org.eclipse.m2m.atl.core.emf.EMFReferenceModel;
import org.eclipse.m2m.atl.engine.parser.AtlParser;

import anatlyzer.atl.util.ATLSerializer;
import anatlyzer.atlext.ATL.Binding;
import anatlyzer.atl.model.ATLModel;
import ca.udem.fixingatlerror.refinementphase.main;
import ca.udem.fixingatlerror.refinementphase.CoSolution;
import ca.udem.fixingatlerror.refinementphase.Operations;
import ca.udem.fixingatlerror.refinementphase.Setting;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.IntSolutionType;
import jmetal.metaheuristics.nsgaII.NSGAII;
//package anatlyzer.examples.api;
//import jmetal.problems.AdaptiveInterface.CoSolution;
//import jmetal.problems.AdaptiveInterface.Rule;
import jmetal.util.JMException;
import witness.generator.MetaModel;

public class MyProblem extends Problem {
	int lower_rules_size = 5;
	int upper_rules_size = 10;
	// public ATLModel wrapper;
	// public EMFModel atlModel; // model of the original transformation
	// public static ArrayList<String> comments2 = new ArrayList<String>();
	public static CoSolution S2;
	public static int oldoperation1 = 0;
	public static int replaceoperation1 = 0;
	public static int secondoldoperation1 = 0;
	public static int oldoperation2 = 0;
	public static int replaceoperation2 = 0;
	public static int secondoldoperation2 = 0;
	public static int oldoperation3 = 0;
	public static int replaceoperation3 = 0;
	public static int secondoldoperation3 = 0;
	public static int oldoperation4 = 0;
	public static int replaceoperation4 = 0;
	public static int secondoldoperation4 = 0;
	public static int oldoperation5 = 0;
	public static int replaceoperation5 = 0;
	public static int secondoldoperation5 = 0;
	public static int oldoperation6 = 0;
	public static int replaceoperation6 = 0;
	public static int secondoldoperation6 = 0;
	public static int oldoperation7 = 0;
	public static int replaceoperation7 = 0;
	public static int secondoldoperation7 = 0;
	public static int oldoperation8 = 0;
	public static int replaceoperation8 = 0;
	public static int secondoldoperation8 = 0;
	public static int oldoperation9 = 0;
	public static int replaceoperation9 = 0;
	public static int secondoldoperation9 = 0;
	public static int oldoperation10 = 0;
	public static int replaceoperation10 = 0;
	public static int secondoldoperation10 = 0;
	public static int oldoperation11 = 0;
	public static int replaceoperation11 = 0;
	public static int secondoldoperation11 = 0;
	public static int oldoperation12 = 0;
	public static int replaceoperation12 = 0;
	public static int secondoldoperation12 = 0;
	public static int oldoperation13 = 0;
	public static int replaceoperation13 = 0;
	public static int secondoldoperation13 = 0;
	public static int oldoperation14 = 0;
	public static int replaceoperation14 = 0;
	public static int secondoldoperation14 = 0;
	public static int oldoperation15 = 0;
	public static int replaceoperation15 = 0;
	public static int secondoldoperation15 = 0;
	public static int oldoperation16 = 0;
	public static int replaceoperation16 = 0;
	public static int secondoldoperation16 = 0;
	public static int oldoperation17 = 0;
	public static int replaceoperation17 = 0;
	public static int secondoldoperation17 = 0;
	public static int oldoperation18 = 0;
	public static int replaceoperation18 = 0;
	public static int secondoldoperation18 = 0;
	public static int oldoperation19 = 0;
	public static int replaceoperation19 = 0;
	public static int secondoldoperation19 = 0;
	public static int oldoperation20 = 0;
	public static int replaceoperation20 = 0;
	public static int secondoldoperation20 = 0;
	public static int oldoperation21 = 0;
	public static int replaceoperation21 = 0;
	public static int secondoldoperation21 = 0;
	public static int oldoperation22 = 0;
	public static int replaceoperation22 = 0;
	public static int secondoldoperation22 = 0;
	public static int indexoperation = -1;
	public static Solution repeatoperation;
	public static ArrayList<Integer> listold;

	// public static ArrayList<Integer> reductiondeletion = new
	// ArrayList<Integer>();
	int applyoperation = 0;
	private String m = "C:/Users/varaminz/Downloads/Project/workspaceTEST34/anatlyzer.atl.tests.api/trafo2/zahra2mutants/finalresult.atl";
	private EMFModel atlModel3;
	int sumfirstfit = 0;
	int sumsecondfit = 0;
	int sumthirdfit = 0;

	public MyProblem(String solutionType) throws ClassNotFoundException {
		this(solutionType, 1);

	} // Adapt_Interface

	public int getsumfirstfit() {
		return this.sumfirstfit;
	}

	public int getsumsecondfit() {
		return this.sumsecondfit;
	}

	public int getsumthirdfit() {
		return this.sumthirdfit;
	}

	public void setsumfirstfit(int i) {
		this.sumfirstfit = i;
	}

	public void setsumsecondfit(int i) {
		this.sumsecondfit = i;
	}

	public void setsumthirdfit(int i) {
		this.sumthirdfit = i;
	}

	public MyProblem(String solutionType, Integer numberOfVariables) {
		/*
		 * System.out.println("probleminitial"); try { this.atlModel =
		 * this.loadTransformationModel(
		 * "C:/Users/varaminz/Downloads/Project/workspaceTEST34/anatlyzer.atl.tests.api/trafo/Class2TableExperience1.atl"
		 * ); } catch (ATLCoreException e1) { // TODO Auto-generated catch block
		 * e1.printStackTrace(); } this.wrapper = new
		 * ATLModel(this.atlModel.getResource());
		 */

		numberOfVariables_ = 100; // S.SolutionSize(min_rules_size, max_rules_size) ; //
		numberOfObjectives_ = 3;
		numberOfConstraints_ = 0;
		problemName_ = "Transformation_Coevolution";
		lowerLimit_ = new double[numberOfVariables_];
		upperLimit_ = new double[numberOfVariables_];
		for (int i = 0; i < numberOfVariables; i++) {
			lowerLimit_[i] = IntSolutionType.min_operations_size;// lower_rules_size;
			upperLimit_[i] = IntSolutionType.min_operations_size; // upper_rules_size;
		}
		if (solutionType.compareTo("Int") == 0) {
			// System.out.println("v1111");
			solutionType_ = new IntSolutionType(this);
		} else {
			System.out.println("Error: solution type " + solutionType + " invalid");
			System.exit(-1);
		}

	}

	public List<EPackage> retPackMM(Resource resourceMM) {
		ResourceSet resourceSet = resourceMM.getResourceSet();
		List<EPackage> metamodel = new ArrayList<EPackage>();
		for (EObject obj : resourceMM.getContents()) {
			if (obj instanceof EPackage) {
				EPackage.Registry.INSTANCE.put(((EPackage) obj).getNsURI(),
						((EPackage) obj).getEFactoryInstance().getEPackage());
				resourceSet.getPackageRegistry().put(((EPackage) obj).getNsURI(),
						((EPackage) obj).getEFactoryInstance().getEPackage());
				metamodel.add((EPackage) obj);
			}
		}
		return metamodel;
	}

	public Resource retPackResouceMM(String MMPath) {
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore",
				new EcoreResourceFactoryImpl());
		URI fileURI = URI.createFileURI(MMPath);// ecore.getFullPath().toOSString());
		Resource resource = resourceSet.getResource(fileURI, true);
		return resource;
	}

	private /* static */ long index = 1;

	private String getValidNameOfFile(String outputFolder) {
		String outputfile = null;
		String aux = null;
		aux = File.separatorChar + "finalresult" + main.totalnumber + ".atl";
		outputfile = outputFolder + aux;
		return outputfile;
	}

	protected boolean save(EMFModel atlModel, String outputFolder) {
		try {
			// save atl file
			String atl_transformation = this.getValidNameOfFile(outputFolder);
			AtlParser atlParser = new AtlParser();
			atlParser.extract(atlModel, atl_transformation);

			// compile transformation
			String asm_transformation = atl_transformation.replace(".atl", ".asm");
		} catch (ATLCoreException e) {
		}
		// catch (FileNotFoundException e) {}
		// catch (IOException e) {}

		return false;
	}

	private EMFModel loadTransformationModel(String atlTransformationFile) throws ATLCoreException {
		ModelFactory modelFactory = new EMFModelFactory();

		EMFReferenceModel atlMetamodel = (EMFReferenceModel) modelFactory.getBuiltInResource("ATL.ecore");

		// EMFReferenceModel atlMetamodel =
		// (EMFReferenceModel)modelFactory.getBuiltInResource("C:\\Users\\wael\\OneDrive\\workspaceATLoperations\\mutants\\anatlyzer.evaluation.mutants\\ATL.ecore");
		AtlParser atlParser = new AtlParser();
		EMFModel atlModel = (EMFModel) modelFactory.newModel(atlMetamodel);
		atlParser.inject(atlModel, atlTransformationFile);
		atlModel.setIsTarget(true);

		// // Should we want to serialize the model.
		// String injectedFile = "file:/" + atlTransformationFile + ".xmi";
		// IExtractor extractor = new EMFExtractor();
		// extractor.extract(atlModel, injectedFile);

		return atlModel;
	}

	@Override
	public void evaluate(Solution solution, FileWriter csvWriterpareto) throws JMException {

		// CoSolution S = new CoSolution();
		CoSolution S = solution.getCoSolution();
		S2 = S;
		// bod System.out.println(S.getOp().TRANSFORMATION);
		Variable[] variable = solution.getDecisionVariables();
		double[] f = new double[numberOfObjectives_];

		/*
		 * for(int i=0;i< variable.length;i++) { f[0] = variable[i].getValue(); }
		 */

		// System.out.println(" Size of solution : "+ IntSolutionType.rules_size);
		// System.out.println(IntSolutionType.operations_size);
		// f[0] = IntSolutionType.operations_size ; //solution.rule().size();
		// //IntSolutionType.rules_size ;//S.fitness_1() ;

		// NSGAII.writer.println("first-fitnessfunction");
		// f[0] = solution.operation().size(); //in bod

		// NSGAII.writer.println(f[0]);
		// System.out.println( "fonction objective 1 "+ solution.operation().size());
		// System.out.println(solution.getoperations());
		// System.out.println("ffffffffffffff");

		// Execution de la solution
		List<Object> comments = null;
		Setting s = new Setting();
		Boolean checksavestatus = false;
		if (NSGAII.counter <= s.getpopulationsize()) { // 60 ba tedade population
			oldoperation4 = solution.getoldrandomIntoperation4();
			replaceoperation4 = solution.getreplacerandomIntoperation4();
			secondoldoperation4 = solution.getsecondoldrandomIntoperation4();
			oldoperation3 = solution.getoldrandomIntoperation3();
			replaceoperation3 = solution.getreplacerandomIntoperation3();
			secondoldoperation3 = solution.getsecondoldrandomIntoperation3();
			oldoperation2 = solution.getoldrandomIntoperation2();
			replaceoperation2 = solution.getreplacerandomIntoperation2();
			secondoldoperation2 = solution.getsecondoldrandomIntoperation2();
			oldoperation1 = solution.getoldrandomIntoperation1();
			replaceoperation1 = solution.getreplacerandomIntoperation1();
			secondoldoperation1 = solution.getsecondoldrandomIntoperation1();
			oldoperation5 = solution.getoldrandomIntoperation5();
			replaceoperation5 = solution.getreplacerandomIntoperation5();
			secondoldoperation5 = solution.getsecondoldrandomIntoperation5();
			oldoperation6 = solution.getoldrandomIntoperation6();
			replaceoperation6 = solution.getreplacerandomIntoperation6();
			secondoldoperation6 = solution.getsecondoldrandomIntoperation6();
			oldoperation7 = solution.getoldrandomIntoperation7();
			replaceoperation7 = solution.getreplacerandomIntoperation7();
			secondoldoperation7 = solution.getsecondoldrandomIntoperation7();
			oldoperation8 = solution.getoldrandomIntoperation8();
			replaceoperation8 = solution.getreplacerandomIntoperation8();
			secondoldoperation8 = solution.getsecondoldrandomIntoperation8();
			oldoperation9 = solution.getoldrandomIntoperation9();
			replaceoperation9 = solution.getreplacerandomIntoperation9();
			secondoldoperation9 = solution.getsecondoldrandomIntoperation9();
			oldoperation10 = solution.getoldrandomIntoperation10();
			replaceoperation10 = solution.getreplacerandomIntoperation10();
			secondoldoperation10 = solution.getsecondoldrandomIntoperation10();
			oldoperation11 = solution.getoldrandomIntoperation11();
			replaceoperation11 = solution.getreplacerandomIntoperation11();
			secondoldoperation11 = solution.getsecondoldrandomIntoperation11();
			oldoperation12 = solution.getoldrandomIntoperation12();
			replaceoperation12 = solution.getreplacerandomIntoperation12();
			secondoldoperation12 = solution.getsecondoldrandomIntoperation12();
			oldoperation13 = solution.getoldrandomIntoperation13();
			replaceoperation13 = solution.getreplacerandomIntoperation13();
			secondoldoperation13 = solution.getsecondoldrandomIntoperation13();
			oldoperation14 = solution.getoldrandomIntoperation14();
			replaceoperation14 = solution.getreplacerandomIntoperation14();
			secondoldoperation14 = solution.getsecondoldrandomIntoperation14();
			oldoperation15 = solution.getoldrandomIntoperation15();
			replaceoperation15 = solution.getreplacerandomIntoperation15();
			secondoldoperation15 = solution.getsecondoldrandomIntoperation15();
			oldoperation16 = solution.getoldrandomIntoperation16();
			replaceoperation16 = solution.getreplacerandomIntoperation16();
			secondoldoperation16 = solution.getsecondoldrandomIntoperation16();
			oldoperation17 = solution.getoldrandomIntoperation17();
			replaceoperation17 = solution.getreplacerandomIntoperation17();
			secondoldoperation17 = solution.getsecondoldrandomIntoperation17();
			oldoperation18 = solution.getoldrandomIntoperation18();
			replaceoperation18 = solution.getreplacerandomIntoperation18();
			secondoldoperation18 = solution.getsecondoldrandomIntoperation18();
			oldoperation19 = solution.getoldrandomIntoperation19();
			replaceoperation19 = solution.getreplacerandomIntoperation19();
			secondoldoperation19 = solution.getsecondoldrandomIntoperation19();
			oldoperation20 = solution.getoldrandomIntoperation20();
			replaceoperation20 = solution.getreplacerandomIntoperation20();
			secondoldoperation20 = solution.getsecondoldrandomIntoperation20();
			oldoperation21 = solution.getoldrandomIntoperation21();
			replaceoperation21 = solution.getreplacerandomIntoperation21();
			secondoldoperation21 = solution.getsecondoldrandomIntoperation21();
			oldoperation22 = solution.getoldrandomIntoperation22();
			replaceoperation22 = solution.getreplacerandomIntoperation22();
			secondoldoperation22 = solution.getsecondoldrandomIntoperation22();

			/*
			 * NSGAII.writer.println("beforeoperationpopulation");
			 * NSGAII.writer.println(oldoperation1);
			 * NSGAII.writer.println(replaceoperation1); NSGAII.writer.println("2222");
			 * NSGAII.writer.println(oldoperation2);
			 * NSGAII.writer.println(replaceoperation2); NSGAII.writer.println("3333");
			 * NSGAII.writer.println(oldoperation3);
			 * NSGAII.writer.println(replaceoperation3); NSGAII.writer.println("444");
			 * NSGAII.writer.println(oldoperation4);
			 * NSGAII.writer.println(replaceoperation4); NSGAII.writer.println("555");
			 * NSGAII.writer.println(oldoperation5);
			 * NSGAII.writer.println(replaceoperation5); NSGAII.writer.println("6666");
			 * NSGAII.writer.println(oldoperation6);
			 * NSGAII.writer.println(replaceoperation6); NSGAII.writer.println("777");
			 * NSGAII.writer.println(oldoperation7);
			 * NSGAII.writer.println(replaceoperation7); NSGAII.writer.println("888");
			 * NSGAII.writer.println(oldoperation8);
			 * NSGAII.writer.println(replaceoperation8); NSGAII.writer.println("999");
			 * NSGAII.writer.println(oldoperation9);
			 * NSGAII.writer.println(replaceoperation9); NSGAII.writer.println("100");
			 * NSGAII.writer.println(oldoperation10);
			 * NSGAII.writer.println(replaceoperation10); NSGAII.writer.println("11");
			 * NSGAII.writer.println(oldoperation11);
			 * NSGAII.writer.println(replaceoperation11); NSGAII.writer.println("12");
			 * NSGAII.writer.println(oldoperation12);
			 * NSGAII.writer.println(replaceoperation12); NSGAII.writer.println("13");
			 * NSGAII.writer.println(oldoperation13);
			 * NSGAII.writer.println(replaceoperation13); NSGAII.writer.println("14");
			 * NSGAII.writer.println(oldoperation14);
			 * NSGAII.writer.println(replaceoperation14);
			 */
			listold = new ArrayList<Integer>();
			NSGAII.numop = 0;
			NSGAII.deletlist.clear();
			NSGAII.deletlist = new ArrayList<Integer>();
			NSGAII.listoutpatternmodify.clear();
			NSGAII.listoutpatternmodify = new ArrayList<Integer>();
			NSGAII.counterdelet = 0;
			solution = SetListfirstandsecondindices(solution, oldoperation1, oldoperation2, oldoperation3,
					oldoperation4, oldoperation5, oldoperation6, oldoperation7, oldoperation8, oldoperation9,
					oldoperation10, oldoperation11, oldoperation12, oldoperation13, oldoperation14, oldoperation15,
					replaceoperation1, replaceoperation2, replaceoperation3, replaceoperation4, replaceoperation5,
					replaceoperation6, replaceoperation7, replaceoperation8, replaceoperation9, replaceoperation10,
					replaceoperation11, replaceoperation12, replaceoperation13, replaceoperation14, replaceoperation15);
			for (int i = 0; i < IntSolutionType.operations.size(); i++) {
				// System.out.println(IntSolutionType.operations.get(i));
				indexoperation = i + 1;
				// if(IntSolutionType.operations.get(i)!=-3) {
				this.applyoperation = NSGAII.numop;

				int numop = NSGAII.numop;
				comments = S.getOp().executeOperations(IntSolutionType.operations.get(i), S,
						IntSolutionType.operations.size(), solution, this.sumfirstfit, this.sumsecondfit,
						this.sumthirdfit, csvWriterpareto);

				if (comments != null) {

					if (comments.size() == 3 && IntSolutionType.operations.size() != MyProblem.indexoperation) {
						solution.totalcomments.add((String) comments.get(2));

					}
					if (comments.size() == 3 && IntSolutionType.operations.size() == MyProblem.indexoperation) {

						this.sumfirstfit = (int) comments.get(0);
						this.sumsecondfit = (int) comments.get(1);
						this.sumthirdfit = (int) comments.get(2);

					}
					if (comments.size() == 6) {
						solution.totalcomments.add((String) comments.get(2));
						this.sumfirstfit = (int) comments.get(3);
						this.sumsecondfit = (int) comments.get(4);
						this.sumthirdfit = (int) comments.get(5);

					}

				}

				if (numop == NSGAII.numop)
					IntSolutionType.operations.set(i, -3);

				// }
			}

			solution.setoldrandomIntoperation4(oldoperation4);
			solution.setreplacerandomIntoperation4(replaceoperation4);
			solution.setsecondoldrandomIntoperation4(secondoldoperation4);
			solution.setoldrandomIntoperation3(oldoperation3);
			solution.setreplacerandomIntoperation3(replaceoperation3);
			solution.setsecondoldrandomIntoperation3(secondoldoperation3);
			solution.setoldrandomIntoperation2(oldoperation2);
			solution.setreplacerandomIntoperation2(replaceoperation2);
			solution.setsecondoldrandomIntoperation2(secondoldoperation2);
			solution.setoldrandomIntoperation1(oldoperation1);
			solution.setreplacerandomIntoperation1(replaceoperation1);
			solution.setsecondoldrandomIntoperation1(secondoldoperation1);
			solution.setoldrandomIntoperation5(oldoperation5);
			solution.setreplacerandomIntoperation5(replaceoperation5);
			solution.setsecondoldrandomIntoperation5(secondoldoperation5);
			solution.setoldrandomIntoperation6(oldoperation6);
			solution.setreplacerandomIntoperation6(replaceoperation6);
			solution.setsecondoldrandomIntoperation6(secondoldoperation6);
			solution.setoldrandomIntoperation7(oldoperation7);
			solution.setreplacerandomIntoperation7(replaceoperation7);
			solution.setsecondoldrandomIntoperation7(secondoldoperation7);
			solution.setoldrandomIntoperation8(oldoperation8);
			solution.setreplacerandomIntoperation8(replaceoperation8);
			solution.setsecondoldrandomIntoperation8(secondoldoperation8);
			solution.setoldrandomIntoperation9(oldoperation9);
			solution.setreplacerandomIntoperation9(replaceoperation9);
			solution.setsecondoldrandomIntoperation9(secondoldoperation9);
			solution.setoldrandomIntoperation10(oldoperation10);
			solution.setreplacerandomIntoperation10(replaceoperation10);
			solution.setsecondoldrandomIntoperation10(secondoldoperation10);
			solution.setoldrandomIntoperation11(oldoperation11);
			solution.setreplacerandomIntoperation11(replaceoperation11);
			solution.setsecondoldrandomIntoperation11(secondoldoperation11);
			solution.setoldrandomIntoperation12(oldoperation12);
			solution.setreplacerandomIntoperation12(replaceoperation12);
			solution.setsecondoldrandomIntoperation12(secondoldoperation12);
			solution.setoldrandomIntoperation13(oldoperation13);
			solution.setreplacerandomIntoperation13(replaceoperation13);
			solution.setsecondoldrandomIntoperation13(secondoldoperation13);
			solution.setoldrandomIntoperation14(oldoperation14);
			solution.setreplacerandomIntoperation14(replaceoperation14);
			solution.setsecondoldrandomIntoperation14(secondoldoperation14);
			solution.setoldrandomIntoperation15(oldoperation15);
			solution.setreplacerandomIntoperation15(replaceoperation15);
			solution.setsecondoldrandomIntoperation15(secondoldoperation15);
			solution.setoldrandomIntoperation16(oldoperation16);
			solution.setreplacerandomIntoperation16(replaceoperation16);
			solution.setsecondoldrandomIntoperation16(secondoldoperation16);
			solution.setoldrandomIntoperation17(oldoperation17);
			solution.setreplacerandomIntoperation17(replaceoperation17);
			solution.setsecondoldrandomIntoperation17(secondoldoperation17);
			solution.setoldrandomIntoperation18(oldoperation18);
			solution.setreplacerandomIntoperation18(replaceoperation18);
			solution.setsecondoldrandomIntoperation18(secondoldoperation18);
			solution.setoldrandomIntoperation19(oldoperation19);
			solution.setreplacerandomIntoperation19(replaceoperation19);
			solution.setsecondoldrandomIntoperation19(secondoldoperation19);
			solution.setoldrandomIntoperation20(oldoperation20);
			solution.setreplacerandomIntoperation20(replaceoperation20);
			solution.setsecondoldrandomIntoperation20(secondoldoperation20);
			solution.setoldrandomIntoperation21(oldoperation21);
			solution.setreplacerandomIntoperation21(replaceoperation21);
			solution.setsecondoldrandomIntoperation21(secondoldoperation21);
			solution.setoldrandomIntoperation22(oldoperation22);
			solution.setreplacerandomIntoperation22(replaceoperation22);
			solution.setsecondoldrandomIntoperation22(secondoldoperation22);

		} else {
			oldoperation4 = solution.getoldrandomIntoperation4();
			replaceoperation4 = solution.getreplacerandomIntoperation4();
			secondoldoperation4 = solution.getsecondoldrandomIntoperation4();
			oldoperation3 = solution.getoldrandomIntoperation3();
			replaceoperation3 = solution.getreplacerandomIntoperation3();
			secondoldoperation3 = solution.getsecondoldrandomIntoperation3();
			oldoperation2 = solution.getoldrandomIntoperation2();
			replaceoperation2 = solution.getreplacerandomIntoperation2();
			secondoldoperation2 = solution.getsecondoldrandomIntoperation2();
			oldoperation1 = solution.getoldrandomIntoperation1();
			replaceoperation1 = solution.getreplacerandomIntoperation1();
			secondoldoperation1 = solution.getsecondoldrandomIntoperation1();
			oldoperation5 = solution.getoldrandomIntoperation5();
			replaceoperation5 = solution.getreplacerandomIntoperation5();
			secondoldoperation5 = solution.getsecondoldrandomIntoperation5();
			oldoperation6 = solution.getoldrandomIntoperation6();
			replaceoperation6 = solution.getreplacerandomIntoperation6();
			secondoldoperation6 = solution.getsecondoldrandomIntoperation6();
			oldoperation7 = solution.getoldrandomIntoperation7();
			replaceoperation7 = solution.getreplacerandomIntoperation7();
			secondoldoperation7 = solution.getsecondoldrandomIntoperation7();
			oldoperation8 = solution.getoldrandomIntoperation8();
			replaceoperation8 = solution.getreplacerandomIntoperation8();
			secondoldoperation8 = solution.getsecondoldrandomIntoperation8();
			oldoperation9 = solution.getoldrandomIntoperation9();
			replaceoperation9 = solution.getreplacerandomIntoperation9();
			secondoldoperation9 = solution.getsecondoldrandomIntoperation9();
			oldoperation10 = solution.getoldrandomIntoperation10();
			replaceoperation10 = solution.getreplacerandomIntoperation10();
			secondoldoperation10 = solution.getsecondoldrandomIntoperation10();
			oldoperation11 = solution.getoldrandomIntoperation11();
			replaceoperation11 = solution.getreplacerandomIntoperation11();
			secondoldoperation11 = solution.getsecondoldrandomIntoperation11();
			oldoperation12 = solution.getoldrandomIntoperation12();
			replaceoperation12 = solution.getreplacerandomIntoperation12();
			secondoldoperation12 = solution.getsecondoldrandomIntoperation12();
			oldoperation13 = solution.getoldrandomIntoperation13();
			replaceoperation13 = solution.getreplacerandomIntoperation13();
			secondoldoperation13 = solution.getsecondoldrandomIntoperation13();
			oldoperation14 = solution.getoldrandomIntoperation14();
			replaceoperation14 = solution.getreplacerandomIntoperation14();
			secondoldoperation14 = solution.getsecondoldrandomIntoperation14();
			oldoperation15 = solution.getoldrandomIntoperation15();
			replaceoperation15 = solution.getreplacerandomIntoperation15();
			secondoldoperation15 = solution.getsecondoldrandomIntoperation15();
			oldoperation16 = solution.getoldrandomIntoperation16();
			replaceoperation16 = solution.getreplacerandomIntoperation16();
			secondoldoperation16 = solution.getsecondoldrandomIntoperation16();
			oldoperation17 = solution.getoldrandomIntoperation17();
			replaceoperation17 = solution.getreplacerandomIntoperation17();
			secondoldoperation17 = solution.getsecondoldrandomIntoperation17();
			oldoperation18 = solution.getoldrandomIntoperation18();
			replaceoperation18 = solution.getreplacerandomIntoperation18();
			secondoldoperation18 = solution.getsecondoldrandomIntoperation18();
			oldoperation19 = solution.getoldrandomIntoperation19();
			replaceoperation19 = solution.getreplacerandomIntoperation19();
			secondoldoperation19 = solution.getsecondoldrandomIntoperation19();
			oldoperation20 = solution.getoldrandomIntoperation20();
			replaceoperation20 = solution.getreplacerandomIntoperation20();
			secondoldoperation20 = solution.getsecondoldrandomIntoperation20();
			oldoperation21 = solution.getoldrandomIntoperation21();
			replaceoperation21 = solution.getreplacerandomIntoperation21();
			secondoldoperation21 = solution.getsecondoldrandomIntoperation21();
			oldoperation22 = solution.getoldrandomIntoperation22();
			replaceoperation22 = solution.getreplacerandomIntoperation22();
			secondoldoperation22 = solution.getsecondoldrandomIntoperation22();

			/*
			 * System.out.println("111"); System.out.println(oldoperation1);
			 * System.out.println(replaceoperation1);
			 * 
			 * System.out.println(oldoperation2); System.out.println(replaceoperation2);
			 * 
			 * System.out.println(oldoperation3); System.out.println(replaceoperation3);
			 * 
			 * System.out.println(oldoperation4); System.out.println(replaceoperation4);
			 * 
			 * System.out.println(oldoperation5); System.out.println(replaceoperation5);
			 * 
			 * System.out.println(oldoperation6); System.out.println(replaceoperation6);
			 * 
			 * System.out.println(oldoperation7); System.out.println(replaceoperation7);
			 * 
			 * System.out.println(oldoperation8); System.out.println(replaceoperation8);
			 * 
			 * System.out.println(oldoperation9); System.out.println(replaceoperation9);
			 * 
			 * System.out.println(oldoperation10); System.out.println(replaceoperation10);
			 * System.out.println(oldoperation11); System.out.println(replaceoperation11);
			 * System.out.println(oldoperation12); System.out.println(replaceoperation12);
			 * System.out.println(oldoperation13); System.out.println(replaceoperation13);
			 * System.out.println(oldoperation14); System.out.println(replaceoperation14);
			 * System.out.println(oldoperation15); System.out.println(replaceoperation15);
			 * System.out.println(oldoperation16); System.out.println(replaceoperation16);
			 * System.out.println(oldoperation17); System.out.println(replaceoperation17);
			 * NSGAII.writer.println("11");
			 * 
			 * NSGAII.writer.println("beforeoperationpopulation");
			 * NSGAII.writer.println("111"); NSGAII.writer.println(oldoperation1);
			 * NSGAII.writer.println(replaceoperation1); NSGAII.writer.println("222");
			 * NSGAII.writer.println(oldoperation2);
			 * NSGAII.writer.println(replaceoperation2); NSGAII.writer.println("333");
			 * NSGAII.writer.println(oldoperation3);
			 * NSGAII.writer.println(replaceoperation3); NSGAII.writer.println("444");
			 * NSGAII.writer.println(oldoperation4);
			 * NSGAII.writer.println(replaceoperation4); NSGAII.writer.println("555");
			 * NSGAII.writer.println(oldoperation5);
			 * NSGAII.writer.println(replaceoperation5); NSGAII.writer.println("666");
			 * NSGAII.writer.println(oldoperation6);
			 * NSGAII.writer.println(replaceoperation6); NSGAII.writer.println("777");
			 * NSGAII.writer.println(oldoperation7);
			 * NSGAII.writer.println(replaceoperation7); NSGAII.writer.println("888");
			 * NSGAII.writer.println(oldoperation8);
			 * NSGAII.writer.println(replaceoperation8); NSGAII.writer.println("999");
			 * NSGAII.writer.println(oldoperation9);
			 * NSGAII.writer.println(replaceoperation9); NSGAII.writer.println("100");
			 * NSGAII.writer.println(oldoperation10);
			 * NSGAII.writer.println(replaceoperation10); NSGAII.writer.println("11");
			 * NSGAII.writer.println(oldoperation11);
			 * NSGAII.writer.println(replaceoperation11); NSGAII.writer.println("12");
			 * NSGAII.writer.println(oldoperation12);
			 * NSGAII.writer.println(replaceoperation12); NSGAII.writer.println("13");
			 * NSGAII.writer.println(oldoperation13);
			 * NSGAII.writer.println(replaceoperation13); NSGAII.writer.println("14");
			 * NSGAII.writer.println(oldoperation14);
			 * NSGAII.writer.println(replaceoperation14);
			 */
			NSGAII.numop = 0;
			// bod System.out.println(solution.getoperations());
			NSGAII.deletlist.clear();
			NSGAII.deletlist = new ArrayList<Integer>();
			NSGAII.counterdelet = 0;
			NSGAII.listoutpatternmodify.clear();
			NSGAII.listoutpatternmodify = new ArrayList<Integer>();
			/*
			 * NSGAII.writer.println("nextgenerationbefore");
			 * NSGAII.writer.println(this.sumfirstfit);
			 * NSGAII.writer.println(this.sumsecondfit);
			 * NSGAII.writer.println(this.sumthirdfit);
			 */
			solution = SetListfirstandsecondindices(solution, oldoperation1, oldoperation2, oldoperation3,
					oldoperation4, oldoperation5, oldoperation6, oldoperation7, oldoperation8, oldoperation9,
					oldoperation10, oldoperation11, oldoperation12, oldoperation13, oldoperation14, oldoperation15,
					replaceoperation1, replaceoperation2, replaceoperation3, replaceoperation4, replaceoperation5,
					replaceoperation6, replaceoperation7, replaceoperation8, replaceoperation9, replaceoperation10,
					replaceoperation11, replaceoperation12, replaceoperation13, replaceoperation14, replaceoperation15);
			for (int i = 0; i < solution.getoperations().size(); i++) {
				indexoperation = i + 1;
				// this.applyoperation=NSGAII.numop;
				// System.out.println(solution.getoperations().get(i));
				// if(solution.getoperations().get(i)!=-3) {
				int numop = NSGAII.numop;
				comments = S.getOp().executeOperations(solution.getoperations().get(i), S,
						solution.getoperations().size(), solution, this.sumfirstfit, this.sumsecondfit,
						this.sumthirdfit, csvWriterpareto);
				if (comments != null) {

					if (comments.size() == 3 && IntSolutionType.operations.size() != MyProblem.indexoperation) {
						solution.totalcomments.add((String) comments.get(2));

					}
					if (comments.size() == 3 && IntSolutionType.operations.size() == MyProblem.indexoperation) {

						this.sumfirstfit = (int) comments.get(0);
						this.sumsecondfit = (int) comments.get(1);
						this.sumthirdfit = (int) comments.get(2);

					}
					if (comments.size() == 6) {
						solution.totalcomments.add((String) comments.get(2));
						this.sumfirstfit = (int) comments.get(3);
						this.sumsecondfit = (int) comments.get(4);
						this.sumthirdfit = (int) comments.get(5);

					}
				}

				if (numop == NSGAII.numop)
					solution.getoperations().set(i, -3);
			}
			/*
			 * NSGAII.writer.println("nextgenerationafter");
			 * NSGAII.writer.println(this.sumfirstfit);
			 * NSGAII.writer.println(this.sumsecondfit);
			 * NSGAII.writer.println(this.sumthirdfit);
			 */

		}

		solution.setoldrandomIntoperation4(oldoperation4);
		solution.setreplacerandomIntoperation4(replaceoperation4);
		solution.setsecondoldrandomIntoperation4(secondoldoperation4);
		solution.setoldrandomIntoperation3(oldoperation3);
		solution.setreplacerandomIntoperation3(replaceoperation3);
		solution.setsecondoldrandomIntoperation3(secondoldoperation3);
		solution.setoldrandomIntoperation2(oldoperation2);
		solution.setreplacerandomIntoperation2(replaceoperation2);
		solution.setsecondoldrandomIntoperation2(secondoldoperation2);
		solution.setoldrandomIntoperation1(oldoperation1);
		solution.setreplacerandomIntoperation1(replaceoperation1);
		solution.setsecondoldrandomIntoperation1(secondoldoperation1);
		solution.setoldrandomIntoperation5(oldoperation5);
		solution.setreplacerandomIntoperation5(replaceoperation5);
		solution.setsecondoldrandomIntoperation5(secondoldoperation5);
		solution.setoldrandomIntoperation6(oldoperation6);
		solution.setreplacerandomIntoperation6(replaceoperation6);
		solution.setsecondoldrandomIntoperation6(secondoldoperation6);
		solution.setoldrandomIntoperation7(oldoperation7);
		solution.setreplacerandomIntoperation7(replaceoperation7);
		solution.setsecondoldrandomIntoperation7(secondoldoperation7);
		solution.setoldrandomIntoperation8(oldoperation8);
		solution.setreplacerandomIntoperation8(replaceoperation8);
		solution.setsecondoldrandomIntoperation8(secondoldoperation8);
		solution.setoldrandomIntoperation9(oldoperation9);
		solution.setreplacerandomIntoperation9(replaceoperation9);
		solution.setsecondoldrandomIntoperation9(secondoldoperation9);
		solution.setoldrandomIntoperation10(oldoperation10);
		solution.setreplacerandomIntoperation10(replaceoperation10);
		solution.setsecondoldrandomIntoperation10(secondoldoperation10);
		solution.setoldrandomIntoperation11(oldoperation11);
		solution.setreplacerandomIntoperation11(replaceoperation11);
		solution.setsecondoldrandomIntoperation11(secondoldoperation11);
		solution.setoldrandomIntoperation12(oldoperation12);
		solution.setreplacerandomIntoperation12(replaceoperation12);
		solution.setsecondoldrandomIntoperation12(secondoldoperation12);
		solution.setoldrandomIntoperation13(oldoperation13);
		solution.setreplacerandomIntoperation13(replaceoperation13);
		solution.setsecondoldrandomIntoperation13(secondoldoperation13);
		solution.setoldrandomIntoperation14(oldoperation14);
		solution.setreplacerandomIntoperation14(replaceoperation14);
		solution.setsecondoldrandomIntoperation14(secondoldoperation14);
		solution.setoldrandomIntoperation15(oldoperation15);
		solution.setreplacerandomIntoperation15(replaceoperation15);
		solution.setsecondoldrandomIntoperation15(secondoldoperation15);
		solution.setoldrandomIntoperation16(oldoperation16);
		solution.setreplacerandomIntoperation16(replaceoperation16);
		solution.setsecondoldrandomIntoperation16(secondoldoperation16);
		solution.setoldrandomIntoperation17(oldoperation17);
		solution.setreplacerandomIntoperation17(replaceoperation17);
		solution.setsecondoldrandomIntoperation17(secondoldoperation17);
		solution.setoldrandomIntoperation18(oldoperation18);
		solution.setreplacerandomIntoperation18(replaceoperation18);
		solution.setsecondoldrandomIntoperation18(secondoldoperation18);
		solution.setoldrandomIntoperation19(oldoperation19);
		solution.setreplacerandomIntoperation19(replaceoperation19);
		solution.setsecondoldrandomIntoperation19(secondoldoperation19);
		solution.setoldrandomIntoperation20(oldoperation20);
		solution.setreplacerandomIntoperation20(replaceoperation20);
		solution.setsecondoldrandomIntoperation20(secondoldoperation20);
		solution.setoldrandomIntoperation21(oldoperation21);
		solution.setreplacerandomIntoperation21(replaceoperation21);
		solution.setsecondoldrandomIntoperation21(secondoldoperation21);
		solution.setoldrandomIntoperation22(oldoperation22);
		solution.setreplacerandomIntoperation22(replaceoperation22);
		solution.setsecondoldrandomIntoperation22(secondoldoperation22);
		/*
		 * NSGAII.writer.println(solution.inorout);
		 * NSGAII.writer.println("setafteroperationpopulation");
		 * NSGAII.writer.println("111"); NSGAII.writer.println(oldoperation1);
		 * NSGAII.writer.println(replaceoperation1); NSGAII.writer.println("222");
		 * NSGAII.writer.println(oldoperation2);
		 * NSGAII.writer.println(replaceoperation2); NSGAII.writer.println("333");
		 * NSGAII.writer.println(oldoperation3);
		 * NSGAII.writer.println(replaceoperation3); NSGAII.writer.println("444");
		 * NSGAII.writer.println(oldoperation4);
		 * NSGAII.writer.println(replaceoperation4); NSGAII.writer.println("555");
		 * NSGAII.writer.println(oldoperation5);
		 * NSGAII.writer.println(replaceoperation5); NSGAII.writer.println("666");
		 * NSGAII.writer.println(oldoperation6);
		 * NSGAII.writer.println(replaceoperation6); NSGAII.writer.println("777");
		 * NSGAII.writer.println(oldoperation7);
		 * NSGAII.writer.println(replaceoperation7); NSGAII.writer.println("888");
		 * NSGAII.writer.println(oldoperation8);
		 * NSGAII.writer.println(replaceoperation8); NSGAII.writer.println("999");
		 * NSGAII.writer.println(oldoperation9);
		 * NSGAII.writer.println(replaceoperation9); NSGAII.writer.println("100");
		 * NSGAII.writer.println(oldoperation10);
		 * NSGAII.writer.println(replaceoperation10); NSGAII.writer.println("11");
		 * NSGAII.writer.println(oldoperation11);
		 * NSGAII.writer.println(replaceoperation11); NSGAII.writer.println("12");
		 * NSGAII.writer.println(oldoperation12);
		 * NSGAII.writer.println(replaceoperation12); NSGAII.writer.println("13");
		 * NSGAII.writer.println(oldoperation13);
		 * NSGAII.writer.println(replaceoperation13); NSGAII.writer.println("14");
		 * NSGAII.writer.println(oldoperation14);
		 * NSGAII.writer.println(replaceoperation14); NSGAII.writer.println("15");
		 * NSGAII.writer.println(oldoperation15);
		 * NSGAII.writer.println(replaceoperation15);
		 * System.out.println(solution.inorout);
		 * 
		 * NSGAII.writer.println(solution.inorout);
		 * NSGAII.writer.println(solution.newbindings);
		 */
		oldoperation4 = 0;
		replaceoperation4 = 0;
		secondoldoperation4 = 0;
		oldoperation3 = 0;
		replaceoperation3 = 0;
		secondoldoperation3 = 0;
		oldoperation2 = 0;
		replaceoperation2 = 0;
		secondoldoperation2 = 0;
		oldoperation1 = 0;
		replaceoperation1 = 0;
		secondoldoperation1 = 0;
		oldoperation5 = 0;
		replaceoperation5 = 0;
		secondoldoperation5 = 0;
		oldoperation6 = 0;
		replaceoperation6 = 0;
		secondoldoperation6 = 0;
		oldoperation7 = 0;
		replaceoperation7 = 0;
		secondoldoperation7 = 0;
		oldoperation8 = 0;
		replaceoperation8 = 0;
		secondoldoperation8 = 0;
		oldoperation9 = 0;
		replaceoperation9 = 0;
		secondoldoperation9 = 0;
		oldoperation10 = 0;
		replaceoperation10 = 0;
		secondoldoperation10 = 0;
		oldoperation11 = 0;
		replaceoperation11 = 0;
		secondoldoperation11 = 0;
		oldoperation12 = 0;
		replaceoperation12 = 0;
		secondoldoperation12 = 0;
		oldoperation13 = 0;
		replaceoperation13 = 0;
		secondoldoperation13 = 0;
		oldoperation14 = 0;
		replaceoperation14 = 0;
		secondoldoperation14 = 0;
		oldoperation15 = 0;
		replaceoperation15 = 0;
		secondoldoperation15 = 0;
		oldoperation16 = 0;
		replaceoperation16 = 0;
		secondoldoperation16 = 0;
		oldoperation17 = 0;
		replaceoperation17 = 0;
		secondoldoperation17 = 0;
		oldoperation18 = 0;
		replaceoperation18 = 0;
		secondoldoperation18 = 0;
		oldoperation19 = 0;
		replaceoperation19 = 0;
		secondoldoperation19 = 0;
		oldoperation20 = 0;
		replaceoperation20 = 0;
		secondoldoperation20 = 0;
		oldoperation21 = 0;
		replaceoperation21 = 0;
		secondoldoperation21 = 0;
		oldoperation22 = 0;
		replaceoperation22 = 0;
		secondoldoperation22 = 0;

		solution.setindex(main.totalnumber);

		// bod System.out.println("fin de l'execution de toutes les operations ");
		solution.setcomments2(solution.totalcomments);

		// NSGAII.writer.println("first-fitnessfunction");
		f[0] = NSGAII.numop;
		// NSGAII.writer.println(f[0]);
		solution.setfirstfitness(f[0]);
		// int nberrors=S.getOp().getAnalyser().getATLModel().getErrors().getNbErrors();
		int nberrors = NSGAII.fitness2;
		solution.setnumbererror(nberrors);
		f[1] = NSGAII.fitness2;
		// NSGAII.writer.println("second-fitnessfunction");
		// NSGAII.writer.println(f[1]);
		solution.setsecondfitness(f[1]);
		// System.out.println( "fonction objective 2 nb erreurs= "+ nberrors);
		// Calcul de la couverture :
		// bod f[2]=S.Coverage(null);
		f[2] = NSGAII.fitness3;
		solution.setfitnessthird(NSGAII.fitness3);
		// NSGAII.writer.println("third-fitnessfunction");
		// NSGAII.writer.println(f[2]);
		// System.out.println( "fffffffffffffffffffffffffffffffffffffff= "+ f[2]);
		solution.setCoSolution(S);
		// Il faut faire la normalisation avant
		solution.setObjective(0, f[0]);
		solution.setObjective(1, f[1]);
		// solution.setObjective(2,0-f[2]);
		solution.setObjective(2, f[2]);
		main.totalnumber = main.totalnumber + 1;
	}

	private Solution SetListfirstandsecondindices(Solution solution, int oldoperation1, int oldoperation2,
			int oldoperation3, int oldoperation4, int oldoperation5, int oldoperation6, int oldoperation7,
			int oldoperation8, int oldoperation9, int oldoperation10, int oldoperation11, int oldoperation12,
			int oldoperation13, int oldoperation14, int oldoperation15, int replaceoperation1, int replaceoperation2,
			int replaceoperation3, int replaceoperation4, int replaceoperation5, int replaceoperation6,
			int replaceoperation7, int replaceoperation8, int replaceoperation9, int replaceoperation10,
			int replaceoperation11, int replaceoperation12, int replaceoperation13, int replaceoperation14,
			int replaceoperation15) {
		// TODO Auto-generated method stub
		solution.listfirstindices.add(oldoperation1);
		solution.listfirstindices.add(oldoperation2);
		solution.listfirstindices.add(oldoperation3);
		solution.listfirstindices.add(oldoperation4);
		solution.listfirstindices.add(oldoperation5);
		solution.listfirstindices.add(oldoperation6);
		solution.listfirstindices.add(oldoperation7);
		solution.listfirstindices.add(oldoperation8);
		solution.listfirstindices.add(oldoperation9);
		solution.listfirstindices.add(oldoperation10);
		solution.listfirstindices.add(oldoperation11);
		solution.listfirstindices.add(oldoperation12);
		solution.listfirstindices.add(oldoperation13);
		solution.listfirstindices.add(oldoperation14);
		solution.listfirstindices.add(oldoperation15);
		solution.listsecondindices.add(replaceoperation1);
		solution.listsecondindices.add(replaceoperation2);
		solution.listsecondindices.add(replaceoperation3);
		solution.listsecondindices.add(replaceoperation4);
		solution.listsecondindices.add(replaceoperation5);
		solution.listsecondindices.add(replaceoperation6);
		solution.listsecondindices.add(replaceoperation7);
		solution.listsecondindices.add(replaceoperation8);
		solution.listsecondindices.add(replaceoperation9);
		solution.listsecondindices.add(replaceoperation10);
		solution.listsecondindices.add(replaceoperation11);
		solution.listsecondindices.add(replaceoperation12);
		solution.listsecondindices.add(replaceoperation13);
		solution.listsecondindices.add(replaceoperation14);
		solution.listsecondindices.add(replaceoperation15);
		return solution;
	}

	protected void save2(EMFModel atlModel, String outputFolder) {
		try {
			// save atl file
			String aux = File.separatorChar + "finalresult" + main.totalnumber + ".atl";
			String atl_transformation = outputFolder + aux;
			AtlParser atlParser = new AtlParser();
			atlParser.extract(atlModel, atl_transformation);
		} catch (ATLCoreException e) {
		}

	}

	private void SaveAsXMI() {
		// TODO Auto-generated method stub
		ResourceSet outputMetamodelResourceSet = new ResourceSetImpl();

	//	String str = "C:/Users/varaminz/Downloads/Project/workspaceTEST34/anatlyzer.atl.tests.api/trafo2/zahra2cover";
		Resource outputMetamodelResource = outputMetamodelResourceSet.createResource(URI.createFileURI(
				new File("examples/class2rel/trafo/fixmodel" + main.totalnumber + ".xmi").getAbsolutePath()));
		outputMetamodelResourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*",
				new XMLResourceFactoryImpl());
		outputMetamodelResource.getContents().add(Operations.lastmodel.getRoot());
		try {
			outputMetamodelResource.save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
