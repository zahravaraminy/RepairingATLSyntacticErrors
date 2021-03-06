//  NSGAII.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package jmetal.metaheuristics.nsgaII;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.core.ModelFactory;
import org.eclipse.m2m.atl.core.emf.EMFModel;
import org.eclipse.m2m.atl.core.emf.EMFModelFactory;
import org.eclipse.m2m.atl.core.emf.EMFReferenceModel;
import org.eclipse.m2m.atl.engine.parser.AtlParser;

import anatlyzer.atl.analyser.Analyser;
import anatlyzer.atl.model.ATLModel;
import anatlyzer.atl.model.ErrorModel;
import anatlyzer.atlext.ATL.Rule;
import anatlyzer.atlext.OCL.OclExpression;
import anatlyzer.evaluation.models.ModelGenerationStrategy;
import ca.udem.fixingatlerror.refinementphase.BaseTest;
import ca.udem.fixingatlerror.refinementphase.main;
import ca.udem.fixingatlerror.refinementphase.CoSolution;
import ca.udem.fixingatlerror.refinementphase.CoSolutionpostprocessing;
import ca.udem.fixingatlerror.refinementphase.Operations;
import ca.udem.fixingatlerror.refinementphase.Setting;
import ca.udem.fixingatlerror.refinementphase.Settingpostprocessing;
import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.encodings.solutionType.IntSolutionType;
//import jmetal.qualityIndicator.QualityIndicator;
//import jmetal.util.Distance;
import jmetal.util.JMException;
//import jmetal.util.Ranking;
//import jmetal.util.comparators.CrowdingComparator;
import transML.exceptions.transException;
import witness.generator.MetaModel;

/**
 * Implementation of NSGA-II. This implementation of NSGA-II makes use of a
 * QualityIndicator object to obtained the convergence speed of the algorithm.
 * This version is used in the paper: A.J. Nebro, J.J. Durillo, C.A. Coello
 * Coello, F. Luna, E. Alba "A Study of Convergence Speed in Multi-Objective
 * Metaheuristics." To be presented in: PPSN'08. Dortmund. September 2008.
 */

public class NSGAIIpostprocessing extends Algorithm {
	/**
	 * Constructor
	 * 
	 * @param problem
	 *            Problem to solve
	 */
	public NSGAIIpostprocessing(Problem problem) {
		super(problem);
	} // NSGAIIZA

	public void setString(String str) {
		this.statemutcrossinitial = str;
	}

	public static ArrayList<String> listnavigationtypeinheritattrtarget = new ArrayList<String>();
	public static ArrayList<String> listnavigationtypeinheritattr = new ArrayList<String>();
	// public static ArrayList<ArrayList<String>> rightsideattr = new
	// ArrayList<ArrayList<String>>();
	public static ArrayList<ArrayList<ArrayList<String>>> allattributrightside = new ArrayList<ArrayList<ArrayList<String>>>();
	public static ArrayList<Integer> sequencelocation = new ArrayList<Integer>();
	public static ArrayList<Integer> ocliskineoflocationoriginal = new ArrayList<Integer>();
	public static ArrayList<Integer> LastSolutions = new ArrayList<Integer>();
	public static ArrayList<Integer> ocliskineoflocation = new ArrayList<Integer>();
	public static ArrayList<Integer> filterlocation = new ArrayList<Integer>();
	public static ArrayList<Integer> errattoclrule = new ArrayList<Integer>();
	public static ArrayList<Integer> operationcalllocation = new ArrayList<Integer>();
	public static ArrayList<String> listnavigationtype = new ArrayList<String>();
	public static ArrayList<Integer> locationfilter = new ArrayList<Integer>();
	public static List<String> errattrargumentop = new ArrayList<String>();
	public static ArrayList<Integer> locationfrom = new ArrayList<Integer>();
	public static ArrayList<Integer> parameterlocation = new ArrayList<Integer>();
	public static ArrayList<Integer> listnot = new ArrayList<Integer>();
	public static ArrayList<Integer> iterationcall = new ArrayList<Integer>();
	public static ArrayList<Integer> qumecall = new ArrayList<Integer>();
	public static ArrayList<Integer> emptycollectionlocation = new ArrayList<Integer>();
	public static ArrayList<Integer> preconditionlocation = new ArrayList<Integer>();
	public static ArrayList<String> inpatternstringlocation = new ArrayList<String>();
	public static ArrayList<Integer> inpatternhasfilter = new ArrayList<Integer>();
	public static ArrayList<Integer> lazyrulelocation = new ArrayList<Integer>();
	public static ArrayList<Integer> lineoclerror = new ArrayList<Integer>();
	public static ArrayList<Integer> emptyopperationlocation = new ArrayList<Integer>();
	public static ArrayList<Integer> fleshline = new ArrayList<Integer>();
	public static List<Integer> iterationrule = new ArrayList<Integer>();
	public static ArrayList<String> classnamestring = new ArrayList<String>();
	public static ArrayList<String> classnamestringtarget = new ArrayList<String>();
	public static ArrayList<String> oclletter = new ArrayList<String>();
	public static ArrayList<Integer> classnamestartpoint = new ArrayList<Integer>();
	public static ArrayList<Integer> classnamestartpointtarget = new ArrayList<Integer>();
	public static ArrayList<Integer> classnamelengthtarget = new ArrayList<Integer>();
	public static ArrayList<Integer> classnamestartpoint2 = new ArrayList<Integer>();
	public static ArrayList<Integer> classnamelength = new ArrayList<Integer>();
	public static ArrayList<Integer> classnamelength2 = new ArrayList<Integer>();
	public static List<EStructuralFeature> listsourcemetamodel = new ArrayList<EStructuralFeature>();
	public static List<EStructuralFeature> listinheritattributesourcemetamodel = new ArrayList<EStructuralFeature>();
	public static List<EStructuralFeature> listinheritattributesourcemetamodeltarget = new ArrayList<EStructuralFeature>();
	public static ArrayList<String> usingfunction = new ArrayList<String>();
	public static ArrayList<String> oldargumenttype = new ArrayList<String>();
	public static ArrayList<Integer> indexrulechangedinpattern = new ArrayList<Integer>();
	public static ArrayList<Integer> usingline = new ArrayList<Integer>();
	// public static boolean postprocessing=true;
	public static ArrayList<String> leftsideattriteration = new ArrayList<String>();
	public static List<Integer> forbiddenoperations = new ArrayList<Integer>();
	// public static List<EClassifier> listclassinsourcemm = new
	// ArrayList<EClassifier>();
	private String folderMutants;
	public static int counter = 0;
	public static int numberoperationargument = 1;
	public static int numberinitialerror;
	public static boolean checkcollection = false;
	public static String BestSolutionLocation;
	public static boolean checkfilter = false;
	public static boolean checksequence = false;
	public static boolean checkoperationcall = false;
	public static boolean checkiteration = false;
	public static String inputfaultytransformation;
	public static String combinedfaultytransformations;
	private EMFModel atlModel;
	public static PrintWriter writer;
	public static int fitness2 = 0;
	public static int fitness3 = 0;
	public static int indexmodeltransformation = 1;
	public static List<Integer> faultrule = new ArrayList<Integer>();
	public static List<Integer> rulefilter = new ArrayList<Integer>();
	public static List<Integer> finalrule = new ArrayList<Integer>();
	public static List<Integer> faultlocation = new ArrayList<Integer>();
	public static List<Integer> errorrule = new ArrayList<Integer>();
	public static List<Integer> listoutpatternmodify = new ArrayList<Integer>();
	public static ArrayList<Integer> startrule = new ArrayList<Integer>();
	public static ArrayList<Integer> finishedrule = new ArrayList<Integer>();

	public static String statemutcrossinitial = "notmutation";
	public static int numop = 0;
	public static int counterdelet = 0;
	public static int countfilter = 0;
	public static boolean startsituation = false;
	public static int fixedgeneration = -1;
	public static ArrayList<Integer> listfilterapplied = new ArrayList<Integer>();
	public static List<String> argumentlist = new ArrayList<String>();
	public static List<String> oclkindlineattr = new ArrayList<String>();
	public static List<Integer> deletlist = new ArrayList<Integer>();
	public static List<EClassifier> classifierliast = new ArrayList<EClassifier>();
	public static List<List<String>> originalmetamodellist = new ArrayList<List<String>>();
	public static List<List<String>> listattrhelper = new ArrayList<List<String>>();
	public static List<List<Integer>> linehelper = new ArrayList<List<Integer>>();
	public static List<String> alllistnavigationsplitdot = new ArrayList<String>();
	public FileWriter csvWriterpareto;

	/**
	 * Runs the NSGA-II algorithm.
	 * 
	 * @return a <code>SolutionSet</code> that is a set of non dominated solutions
	 *         as a result of the algorithm execution
	 * @throws JMException
	 */
	public SolutionSet execute() throws JMException, ClassNotFoundException {

		Settingpostprocessing s = new Settingpostprocessing();

		// QualityIndicator indicators; // QualityIndicator object
		int requiredEvaluations; // Use in the example of use of the
		// indicators object (see below)
		requiredEvaluations = 0;

		// Create the initial solutionSet

		Solution newSolution;
		CoSolutionpostprocessing cocou = new CoSolutionpostprocessing();
		InitializeinputParameters();
		ArrayList<ArrayList<String>> listproperty = cocou.getOp().listpropertyname;
		FindFaultyRuleForOriginalTransformation();
		FindFaultyRule(listproperty);

		CoSolution.check = 0;
		// cocou.w.getSMClick(
		// "examples/class2rel/trafo/Class2TableExperience1.atl",null);

		// List<String> firstfoot=cocou.footprintscalcul("");
		boolean checkiteration = false;

		main.postprocessing = true;
		newSolution = new Solution(problem_);

		// writer.println(newSolution.getoperations());
		// writer.println("zzzahraaaaaaaa");

		problem_.evaluate(newSolution, this.csvWriterpareto);

		problem_.evaluateConstraints(newSolution);

		System.out.println("end");

		return null;

	} // execute

	private void InitializeinputParameters() {
		// TODO Auto-generated method stub
		NSGAIIpostprocessing.listnavigationtypeinheritattrtarget.clear();
		NSGAIIpostprocessing.listnavigationtypeinheritattrtarget = new ArrayList<String>();
		NSGAIIpostprocessing.listnavigationtypeinheritattr.clear();
		NSGAIIpostprocessing.listnavigationtypeinheritattr = new ArrayList<String>();
		// public static ArrayList<ArrayList<String>> rightsideattr = new
		// ArrayList<ArrayList<String>>();
		NSGAIIpostprocessing.allattributrightside.clear();
		NSGAIIpostprocessing.allattributrightside = new ArrayList<ArrayList<ArrayList<String>>>();
		NSGAIIpostprocessing.sequencelocation.clear();
		NSGAIIpostprocessing.sequencelocation = new ArrayList<Integer>();
		NSGAIIpostprocessing.ocliskineoflocationoriginal.clear();
		NSGAIIpostprocessing.ocliskineoflocationoriginal = new ArrayList<Integer>();
		NSGAIIpostprocessing.LastSolutions.clear();
		NSGAIIpostprocessing.LastSolutions = new ArrayList<Integer>();
		NSGAIIpostprocessing.ocliskineoflocation.clear();
		NSGAIIpostprocessing.ocliskineoflocation = new ArrayList<Integer>();
		NSGAIIpostprocessing.filterlocation.clear();
		NSGAIIpostprocessing.filterlocation = new ArrayList<Integer>();
		NSGAIIpostprocessing.errattoclrule.clear();
		NSGAIIpostprocessing.errattoclrule = new ArrayList<Integer>();
		NSGAIIpostprocessing.operationcalllocation.clear();
		NSGAIIpostprocessing.operationcalllocation = new ArrayList<Integer>();
		NSGAIIpostprocessing.listnavigationtype.clear();
		NSGAIIpostprocessing.listnavigationtype = new ArrayList<String>();
		NSGAIIpostprocessing.locationfilter.clear();
		NSGAIIpostprocessing.locationfilter = new ArrayList<Integer>();
		NSGAIIpostprocessing.errattrargumentop.clear();
		NSGAIIpostprocessing.errattrargumentop = new ArrayList<String>();
		NSGAIIpostprocessing.locationfrom.clear();
		NSGAIIpostprocessing.locationfrom = new ArrayList<Integer>();
		NSGAIIpostprocessing.parameterlocation.clear();
		NSGAIIpostprocessing.parameterlocation = new ArrayList<Integer>();
		NSGAIIpostprocessing.listnot.clear();
		NSGAIIpostprocessing.listnot = new ArrayList<Integer>();
		NSGAIIpostprocessing.iterationcall.clear();
		NSGAIIpostprocessing.iterationcall = new ArrayList<Integer>();
		NSGAIIpostprocessing.qumecall.clear();
		NSGAIIpostprocessing.qumecall = new ArrayList<Integer>();
		NSGAIIpostprocessing.emptycollectionlocation.clear();
		NSGAIIpostprocessing.emptycollectionlocation = new ArrayList<Integer>();
		NSGAIIpostprocessing.preconditionlocation.clear();
		NSGAIIpostprocessing.preconditionlocation = new ArrayList<Integer>();
		NSGAIIpostprocessing.inpatternstringlocation.clear();
		NSGAIIpostprocessing.inpatternstringlocation = new ArrayList<String>();
		NSGAIIpostprocessing.inpatternhasfilter.clear();
		NSGAIIpostprocessing.inpatternhasfilter = new ArrayList<Integer>();
		NSGAIIpostprocessing.lazyrulelocation.clear();
		NSGAIIpostprocessing.lazyrulelocation = new ArrayList<Integer>();
		NSGAIIpostprocessing.lineoclerror.clear();
		NSGAIIpostprocessing.lineoclerror = new ArrayList<Integer>();
		NSGAIIpostprocessing.emptyopperationlocation.clear();
		NSGAIIpostprocessing.emptyopperationlocation = new ArrayList<Integer>();
		NSGAIIpostprocessing.fleshline.clear();
		NSGAIIpostprocessing.fleshline = new ArrayList<Integer>();
		NSGAIIpostprocessing.iterationrule.clear();
		NSGAIIpostprocessing.iterationrule = new ArrayList<Integer>();
		NSGAIIpostprocessing.classnamestring.clear();
		NSGAIIpostprocessing.classnamestring = new ArrayList<String>();
		NSGAIIpostprocessing.classnamestringtarget.clear();
		NSGAIIpostprocessing.classnamestringtarget = new ArrayList<String>();
		NSGAIIpostprocessing.oclletter.clear();
		NSGAIIpostprocessing.oclletter = new ArrayList<String>();
		NSGAIIpostprocessing.classnamestartpoint.clear();
		NSGAIIpostprocessing.classnamestartpoint = new ArrayList<Integer>();
		NSGAIIpostprocessing.classnamestartpointtarget.clear();
		NSGAIIpostprocessing.classnamestartpointtarget = new ArrayList<Integer>();
		NSGAIIpostprocessing.classnamelengthtarget.clear();
		NSGAIIpostprocessing.classnamelengthtarget = new ArrayList<Integer>();
		NSGAIIpostprocessing.classnamestartpoint2.clear();
		NSGAIIpostprocessing.classnamestartpoint2 = new ArrayList<Integer>();
		NSGAIIpostprocessing.classnamelength.clear();
		NSGAIIpostprocessing.classnamelength = new ArrayList<Integer>();
		NSGAIIpostprocessing.classnamelength2.clear();
		NSGAIIpostprocessing.classnamelength2 = new ArrayList<Integer>();
		NSGAIIpostprocessing.listsourcemetamodel.clear();
		NSGAIIpostprocessing.listsourcemetamodel = new ArrayList<EStructuralFeature>();
		NSGAIIpostprocessing.listinheritattributesourcemetamodel.clear();
		NSGAIIpostprocessing.listinheritattributesourcemetamodel = new ArrayList<EStructuralFeature>();
		NSGAIIpostprocessing.listinheritattributesourcemetamodeltarget.clear();
		NSGAIIpostprocessing.listinheritattributesourcemetamodeltarget = new ArrayList<EStructuralFeature>();
		NSGAIIpostprocessing.usingfunction.clear();
		NSGAIIpostprocessing.usingfunction = new ArrayList<String>();
		NSGAIIpostprocessing.usingline.clear();
		NSGAIIpostprocessing.usingline = new ArrayList<Integer>();
		main.postprocessing = false;
		// NSGAIIpostprocessing.postprocessing=false;
		NSGAIIpostprocessing.leftsideattriteration.clear();
		NSGAIIpostprocessing.leftsideattriteration = new ArrayList<String>();
		NSGAIIpostprocessing.forbiddenoperations.clear();
		NSGAIIpostprocessing.forbiddenoperations = new ArrayList<Integer>();
		// List<Integer> forbiddenoperations;
		// public static List<EClassifier> listclassinsourcemm = new
		// ArrayList<EClassifier>();
		// private String folderMutants;
		NSGAIIpostprocessing.counter = 0;
		// NSGAIIpostprocessing.numberinitialerror;
		NSGAIIpostprocessing.checkcollection = false;
		// public static String BestSolutionLocation;
		NSGAIIpostprocessing.checkfilter = false;
		NSGAIIpostprocessing.checksequence = false;
		NSGAIIpostprocessing.checkoperationcall = false;
		NSGAIIpostprocessing.checkiteration = false;
		// public static String inputfaultytransformation;
		// public static String combinedfaultytransformations;
		// private EMFModel atlModel;
		// public static PrintWriter writer;
		NSGAIIpostprocessing.fitness2 = 0;
		NSGAIIpostprocessing.fitness3 = 0;
		NSGAIIpostprocessing.indexmodeltransformation = 1;
		NSGAIIpostprocessing.faultrule.clear();
		NSGAIIpostprocessing.faultrule = new ArrayList<Integer>();
		NSGAIIpostprocessing.finalrule.clear();
		NSGAIIpostprocessing.finalrule = new ArrayList<Integer>();
		NSGAIIpostprocessing.faultlocation.clear();
		NSGAIIpostprocessing.faultlocation = new ArrayList<Integer>();
		NSGAIIpostprocessing.errorrule.clear();
		NSGAIIpostprocessing.errorrule = new ArrayList<Integer>();
		NSGAIIpostprocessing.listoutpatternmodify.clear();
		NSGAIIpostprocessing.listoutpatternmodify = new ArrayList<Integer>();
		NSGAIIpostprocessing.statemutcrossinitial = "notmutation";
		NSGAIIpostprocessing.numop = 0;
		NSGAIIpostprocessing.counterdelet = 0;
		NSGAIIpostprocessing.countfilter = 0;
		NSGAIIpostprocessing.startsituation = false;
		NSGAIIpostprocessing.fixedgeneration = -1;
		NSGAIIpostprocessing.listfilterapplied.clear();
		NSGAIIpostprocessing.listfilterapplied = new ArrayList<Integer>();
		NSGAIIpostprocessing.argumentlist.clear();
		NSGAIIpostprocessing.argumentlist = new ArrayList<String>();
		NSGAIIpostprocessing.oclkindlineattr.clear();
		NSGAIIpostprocessing.oclkindlineattr = new ArrayList<String>();
		NSGAIIpostprocessing.deletlist.clear();
		NSGAIIpostprocessing.deletlist = new ArrayList<Integer>();
		NSGAIIpostprocessing.classifierliast.clear();
		NSGAIIpostprocessing.classifierliast = new ArrayList<EClassifier>();
		NSGAIIpostprocessing.originalmetamodellist.clear();
		NSGAIIpostprocessing.originalmetamodellist = new ArrayList<List<String>>();
		NSGAIIpostprocessing.listattrhelper.clear();
		NSGAIIpostprocessing.listattrhelper = new ArrayList<List<String>>();
		NSGAIIpostprocessing.linehelper.clear();
		NSGAIIpostprocessing.linehelper = new ArrayList<List<Integer>>();
		NSGAIIpostprocessing.alllistnavigationsplitdot.clear();
		NSGAIIpostprocessing.alllistnavigationsplitdot = new ArrayList<String>();
		// public FileWriter csvWriterpareto;

	}

	private void FindFaultyRuleForOriginalTransformation() {
		// TODO Auto-generated method stub

		BaseTest op = new BaseTest();
		try {
			EMFModel emfModel = this.loadTransformationModel(NSGAIIpostprocessing.combinedfaultytransformations);
			System.out.println(NSGAIIpostprocessing.combinedfaultytransformations);
			ATLModel atlmodel = new ATLModel(emfModel.getResource());
		} catch (ATLCoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<Integer> errorline = new ArrayList<Integer>();
		BufferedReader bf = null;
		String stringSearch = "rule";
		String stringSearch2 = "}";
		String stringSearch4 = ".oclIsKindOf";
		try {
			bf = new BufferedReader(new FileReader(NSGAIIpostprocessing.combinedfaultytransformations));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int linecount = 0;
		int lastline = 0;
		String line;
		try {
			while ((line = bf.readLine()) != null) {

				linecount++;
				int indexfound = line.indexOf(stringSearch);
				int indexfound2 = line.indexOf(stringSearch2);
				int indexfound4 = line.indexOf(stringSearch4);
				if (indexfound > -1) {

					NSGAIIpostprocessing.startrule.add(linecount);

				}
				if (indexfound2 > -1) {
					lastline = linecount;

				}
				if (indexfound4 > -1) {
					NSGAIIpostprocessing.ocliskineoflocationoriginal.add(linecount);

				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 1; i < NSGAIIpostprocessing.startrule.size(); i++)
			NSGAIIpostprocessing.finishedrule.add(NSGAIIpostprocessing.startrule.get(i) - 2);
		NSGAIIpostprocessing.finishedrule.add(lastline);
		Settingpostprocessing s = new Settingpostprocessing();
		try {
			System.out.println(NSGAIIpostprocessing.combinedfaultytransformations);
			System.out.println(s.getsourcemetamodel());
			System.out.println(s.gettargetmetamodel());
			op.typing(NSGAIIpostprocessing.combinedfaultytransformations,
					new Object[] { s.getsourcemetamodel(), s.gettargetmetamodel() },
					new String[] { s.getsecondecorename(), s.getfirstecorename() }, true, null, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Analyser analyser = op.getAnalyser();
		ATLModel model = analyser.getATLModel();
		System.out.println(NSGAIIpostprocessing.combinedfaultytransformations);
		System.out.println(model.getErrors().getNbErrors());

		try {
			bf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < model.getErrors().getfaultlocation().size(); i++) {
			String[] array = model.getErrors().getfaultlocation().get(i).split(":", 2);
			errorline.add(Integer.parseInt(array[0]));

		}
		ArrayList<Integer> errattrargumenlocation = new ArrayList<Integer>();
		for (int i = 0; i < model.getErrors().getfaultlocationnofeature().size(); i++) {
			String[] array = model.getErrors().getfaultlocationnofeature().get(i).split(":", 2);

			if (NSGAIIpostprocessing.ocliskineoflocationoriginal.contains(Integer.parseInt(array[0]) - 1)) {

				NSGAIIpostprocessing.errattrargumentop.add(model.getErrors().getmsgerror().get(i));
				errattrargumenlocation.add(Integer.parseInt(array[0]));
				System.out.println("tttttt");
				System.out.println(model.getErrors().getfaultlocationnofeature().get(i));
				// NSGAIIpostprocessing.attrerr.add( );

			}

		}
		Collections.sort(errorline);
		// line error ha ra peida mikone ke attr no feature dashte bashe age nadashte
		// bashe empty barmigarde
		for (int i = 0; i < errorline.size(); i++)
			for (int j = 0; j < NSGAIIpostprocessing.startrule.size(); j++) {
				if (errorline.get(i) >= NSGAIIpostprocessing.startrule.get(j)
						&& errorline.get(i) <= NSGAIIpostprocessing.finishedrule.get(j))
					NSGAIIpostprocessing.errorrule.add(j);
				// rule barmigardone ke attr ocl tosh moshkel dare
				if (errorline.get(i) >= NSGAIIpostprocessing.startrule.get(j)
						&& errorline.get(i) <= NSGAIIpostprocessing.finishedrule.get(j)
						&& errattrargumenlocation.contains(errorline.get(i)))
					NSGAIIpostprocessing.errattoclrule.add(j);
				// if(errorline.get(i)>=startrule.get(j) &&
				// errorline.get(i)<=finishedrule.get(j) && errattrargumenlocation.contains(
				// errorline))
				// NSGAIIpostprocessing.errattoclrule.add(j );
			}

		// createclassifier();

		System.out.println("errorline");
		System.out.println(errorline);
		System.out.println(errattrargumenlocation);
		System.out.println(NSGAIIpostprocessing.errattoclrule);

	}

	private ArrayList<Integer> buildForbiddenOperation() {
		ArrayList<Integer> forbiddenoperations = new ArrayList<Integer>();

		if (!NSGAIIpostprocessing.checkcollection)
			forbiddenoperations.add(7);

		if (!NSGAIIpostprocessing.checkfilter)
			forbiddenoperations.add(5);

		if (!NSGAIIpostprocessing.checksequence)
			forbiddenoperations.add(4);

		if (!NSGAIIpostprocessing.checkiteration)
			forbiddenoperations.add(10);

		if (!NSGAIIpostprocessing.checkoperationcall)
			forbiddenoperations.add(2);

		return forbiddenoperations;

	}

	private void FindFaultyRule(ArrayList<ArrayList<String>> listproperty) {
		// TODO Auto-generated method stub
		BaseTest op = new BaseTest();
		Settingpostprocessing s = new Settingpostprocessing();

		if (NSGAIIpostprocessing.startsituation == false) {
			String stringSearch = "rule";
			String stringSearch14 = "lazy";
			String stringSearch2 = "}";
			String stringSearch3 = "Sequence(";
			String stringSearch9 = "Sequence {}";
			String stringSearch8 = "asSequence";
			// String stringSearch4 = ".oclIsKindOf";
			List<String> stringSearch4 = Arrays.asList(new String[] { ".startsWith", ".oclIsKindOf", ".oclAsType" });
			String stringSearch5 = "from";
			String stringSearch6 = "to";

			String stringSearch7 = "using";
			String stringSearch10 = "->";
			String stringSearch19 = "<-";
			String stringSearch11 = "thisModule";
			String stringSearch12 = "not";
			String stringSearch13 = "'";
			String stringSearch15 = "!";
			String stringSearch18 = "|";
			String stringSearch16 = "def:";
			String stringSearch17 = ".";
			boolean insidehelper = false;
			boolean preconditionforto = false;
			ArrayList<Integer> listassequence = new ArrayList<Integer>();
			ArrayList<Integer> StartBindingRule = new ArrayList<Integer>();
			ArrayList<String> listcollectionoperation = new ArrayList<String>();
			ArrayList<Integer> listlazy = new ArrayList<Integer>();
			ArrayList<Integer> inpattern = new ArrayList<Integer>();
			List<String> iterationlist = Arrays
					.asList(new String[] { "select", "collect", "reject", "forAll", "isUnique", "exists" });
			List<String> helpersplitter = Arrays.asList(new String[] { "\\.", "+", "_", "'", "->" });
			List<String> temporarylist = Arrays.asList(new String[] { "isEmpty", "notEmpty", "includes", "excludes",
					"includesAll", "excludesAll", "size", "sum", "count", "indexOf", "union", "intersection", "first",
					"last", "asBag", "asSequence", "asSet", "flatten", "append", "prepend", "including", "excluding" });
			List<String> collectionempty = Arrays.asList(new String[] { "isEmpty()", "notEmpty()", "includes()",
					"excludes()", "includesAll()", "excludesAll()", "size()", "sum()", "count()", "indexOf()",
					"union()", "intersection()", "first()", "last()", "asBag()", "asSequence()", "asSet()", "flatten()",
					"append()", "prepend()", "including()", "excluding()" });

			List<String> operationcall = Arrays.asList(new String[] { "oclIsKindOf", "oclIsTypeOf", "startsWith",
					"endsWith", "concat", "trim", "max", "min", "exp", "log", "floor", "size", "toInteger", "toReal",
					"indexOf", "lastIndexOf", "abs" });
			List<String> operationempty = Arrays.asList(new String[] { "oclIsKindOf()", "oclIsUndefined()",
					"oclIsTypeOf()", "startsWith()", "endsWith()", "concat()", "trim()", "max()", "min()", "exp()",
					"log()", "floor()", "size()", "toInteger()", "toReal()", "indexOf()", "lastIndexOf()", "abs()" });
			List<String> leftsideiterationsplitter = Arrays.asList(new String[] { "=", "<-" });
			// for petrinet
			// BufferedReader bf = new BufferedReader(new
			// FileReader("C:/Users/varaminz/Downloads/Project/workspaceTEST34/anatlyzer.atl.tests.api/trafo/PNML2PetriNetExperience1.atl"));
			int collefthandside = 0;
			BufferedReader bf = null;
			NSGAIIpostprocessing.allattributrightside = new ArrayList<ArrayList<ArrayList<String>>>();
			ArrayList<String> row2 = null;
			ArrayList<ArrayList<String>> rightsideattr = new ArrayList<ArrayList<String>>();
			try {
				bf = new BufferedReader(new FileReader(NSGAIIpostprocessing.inputfaultytransformation));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			int linecount = 0;
			int lastline = 0;
			String line;
			int indexfrom = -1;
			boolean precondition = false;
			boolean fleshcondition = false;
			boolean precondition2 = false;
			boolean checkisfilter = false;
			boolean checkusing = false;
			int indexhelper = 0;
			int liners = -1;
			try {
				while ((line = bf.readLine()) != null) {

					linecount++;
					int indexfound = line.indexOf(stringSearch);
					int indexfound2 = line.indexOf(stringSearch2);
					int indexfound3 = line.indexOf(stringSearch3);
					// int indexfound4 = line.indexOf(stringSearch4);
					int colon = line.indexOf(stringSearch5);
					int indexfound5 = line.indexOf(stringSearch6);

					int indexfound7 = line.indexOf(stringSearch7);
					int indexfound8 = line.indexOf(stringSearch8);
					int indexfound9 = line.indexOf(stringSearch9);
					int indexfound10 = line.indexOf(stringSearch10);
					int indexfound11 = line.indexOf(stringSearch11);
					int indexfound12 = line.indexOf(stringSearch12);
					int indexfound13 = line.indexOf(stringSearch13);
					int indexfound14 = line.indexOf(stringSearch14);
					int indexfound15 = line.indexOf(stringSearch15);
					int indexfound16 = line.indexOf(stringSearch16);
					int indexfound17 = line.indexOf(stringSearch17);
					int indexfound18 = line.indexOf(stringSearch18);
					int indexfound19 = line.indexOf(stringSearch19);

					if (indexfound19 > -1) {

						System.out.println("rightside");
						String[] parts = line.split("<-|\\->");
						// String[] parts2 = parts[1].split("\\.");

						String[] parts2 = parts[1]
								.split("<-|\\+|\\.|\\'|\\;|\\->|\\;|\\ |\\->|\\s+|\\,|\\(|\\)" + "|\\!");
						System.out.println("part1");

						System.out.println(parts[1]);
						liners = linecount;
						checkisfilter = true;
						row2 = new ArrayList<String>();
						rightsideattr.add(new ArrayList<String>());
						for (int j = 0; j < parts2.length; j++) {
							if (!parts2[j].isEmpty() && !line.equals(" ") && !parts2[j].equals(s.getfirstecorename())) {
								// row2.add(parts2[j]);
								rightsideattr.get(rightsideattr.size() - 1).add(parts2[j].trim());

							}
							// if(!row2.isEmpty())
							// NSGAIIpostprocessing.rightsideattr.get(
							// NSGAIIpostprocessing.rightsideattr.size()-1).add( row2);
						}
					}
					System.out.println("ch");
					System.out.println(rightsideattr);
					System.out.println(!line.equals("}"));
					System.out.println(!line.contains("<-"));
					System.out.println(checkisfilter);
					System.out.println(linecount > liners);
					System.out.println(checkisfilter);

					/*
					 * if (!line.equals("}") && !line.contains(":") &&
					 * NSGAIIpostprocessing.locationfilter.size()>0 &&
					 * linecount>NSGAIIpostprocessing.locationfilter.get(NSGAIIpostprocessing.
					 * locationfilter.size()-1 )+1 && checkisfilter==true && !line.contains("<-") &&
					 * linecount>liners && NSGAIIpostprocessing.faultrule.size()>0 &&
					 * linecount>NSGAIIpostprocessing.faultrule.get(
					 * NSGAIIpostprocessing.faultrule.size()-1) && !line.equals(" ")) { //
					 * ArrayList<String> row2 = new ArrayList<String>(); String[] parts2 =
					 * line.split("<-|\\+|\\.|\\'|\\;|\\->|\\;|\\ |\\)|\\)->|\\s+" + "|\\!");
					 * for(int j=0;j<parts2.length;j++) if(!parts2[j].isEmpty() &&
					 * !line.equals(" ")) { //row2.add(parts2[j]); rightsideattr.get(
					 * rightsideattr.size()-1).add( parts2[j]); }
					 * 
					 * //row2.add(line);
					 * 
					 * 
					 * System.out.println("rightside2"); System.out.println(line);
					 * 
					 * }
					 */
					System.out.println("array");
					System.out.println(rightsideattr);
					if (indexfound10 > -1) {

						SplitAtrrLeftSideIteraion(linecount, leftsideiterationsplitter, line);
						NSGAIIpostprocessing.fleshline.add(linecount);
						fleshcondition = true;

					}
					if (indexfound18 > -1) {

						String[] parts = line.split("->");
						String[] parts2 = parts[1].split("\\(");
						String[] parts3 = parts2[1].split("\\||\\s+");
						NSGAIIpostprocessing.oclletter.add(Integer.toString(linecount));
						NSGAIIpostprocessing.oclletter.add(parts3[0]);

					}
					for (int i = 0; i < temporarylist.size(); i++) {
						if (line.indexOf(temporarylist.get(i)) > -1 && fleshcondition == true) {
							NSGAIIpostprocessing.filterlocation.add(linecount);

						}

					}
					fleshcondition = false;
					for (int i = 0; i < operationcall.size(); i++) {
						if (line.indexOf(operationcall.get(i)) > -1) {
							NSGAIIpostprocessing.operationcalllocation.add(linecount);

						}

					}
					for (int i = 0; i < collectionempty.size(); i++) {
						if (line.indexOf(collectionempty.get(i)) > -1) {
							NSGAIIpostprocessing.emptycollectionlocation.add(linecount);

						}

					}
					for (int i = 0; i < operationempty.size(); i++) {
						if (line.indexOf(operationempty.get(i)) > -1) {
							NSGAIIpostprocessing.emptyopperationlocation.add(linecount);

						}

					}

					for (int i = 0; i < iterationlist.size(); i++) {
						if (line.indexOf(iterationlist.get(i)) > -1) {

							String[] parts = line.split("->");
							String[] parts2 = parts[0].toString().split("\\.");
							if (parts2.length > 1)

								NSGAIIpostprocessing.alllistnavigationsplitdot.add(Integer.toString(linecount));
							NSGAIIpostprocessing.alllistnavigationsplitdot.add(parts2[parts2.length - 1]);

							NSGAIIpostprocessing.iterationcall.add(linecount);

						}

					}
					if (colon > -1) {

						indexfrom = linecount;
						precondition2 = true;
						if (linecount > 5)
							NSGAIIpostprocessing.locationfrom.add(linecount);

					}

					if (indexfound15 > -1 && precondition2 == true && linecount - indexfrom == 1) {
						String[] parts = line.split("!");

						NSGAIIpostprocessing.inpatternstringlocation.add(parts[1]);
						inpattern.add(linecount);

					}
					if (indexfound7 > -1) {
						precondition = true;
						checkusing = true;

					}

					if (checkusing == true) {
						String[] parts = line.split(":");
						// String[] parts2 = parts[0].split("\\s+");
						// String[] parts3 = parts2[1].split("!|\\)");
						NSGAIIpostprocessing.usingfunction.add(parts[0].trim());
						NSGAIIpostprocessing.usingline.add(linecount);
						// System.out.println("using");
						// System.out.println(linecount);
						// System.out.println(parts[0]);
						// System.out.println(parts2[j]);

					}
					if (indexfound5 > -1) {
						precondition2 = false;
						checkisfilter = true;
						if (precondition == false && (line.length() == 2 || line.length() == 3)) {

							NSGAIIpostprocessing.locationfilter.add(linecount);

						}
						if (linecount - indexfrom > 2 && precondition == false && line.length() == 2) {
							// NSGAII.locationfilter.add( indexfrom+1);

						}

						if (linecount - indexfrom > 2 && precondition == true && line.length() == 2)
							NSGAIIpostprocessing.preconditionlocation.add(linecount - 2);

					}
					if (indexfound > -1) {
						insidehelper = false;
						precondition = false;
						// bod System.out.println("Word is at position " + indexfound + " on line " +
						// linecount);
						NSGAIIpostprocessing.faultrule.add(linecount);

					}
					System.out.println("collefthandside");
					System.out.println(collefthandside);
					System.out.println(listproperty.size());
					if (collefthandside < listproperty.size())
						if (listproperty.get(collefthandside).size() == rightsideattr.size()) {
							// if(!rightsideattr.isEmpty())
							NSGAIIpostprocessing.allattributrightside.add(rightsideattr);

							rightsideattr = new ArrayList<ArrayList<String>>();

							// rightsideattr.add(new ArrayList<String>());
							// NSGAIIpostprocessing.allattributrightside = new
							// ArrayList<ArrayList<ArrayList<String>>>();
							// NSGAIIpostprocessing.allattributrightside.add( rightsideattr);
							collefthandside = collefthandside + 1;
							// rightsideattr = new ArrayList<ArrayList<String>>();

						}
					if (indexfound2 > -1) {
						checkisfilter = false;
						lastline = linecount;
						checkusing = false;
						// if(!NSGAIIpostprocessing.rightsideattr.contains(row2) && !row2.isEmpty())
						// NSGAIIpostprocessing.rightsideattr.add( row2);
						// NSGAIIpostprocessing.rightsideattr.get(
						// NSGAIIpostprocessing.rightsideattr.size()-1).add( parts2[j]);
						System.out.println("listproperty");
						System.out.println(listproperty);
						// System.out.println(listproperty.get( NSGAIIpostprocessing.faultrule.size()));
						System.out.println(NSGAIIpostprocessing.faultrule.size());
						// System.out.println(listproperty.get( NSGAIIpostprocessing.faultrule.size()));

						System.out.println(rightsideattr);
						System.out.println("all1");
						System.out.println(NSGAIIpostprocessing.allattributrightside);
						System.out.println("all2");

						System.out.println(rightsideattr.size());
						System.out.println(NSGAIIpostprocessing.allattributrightside);

						/*
						 * if(NSGAIIpostprocessing.faultrule.size()<listproperty.size() ) {
						 * if(listproperty.get( NSGAIIpostprocessing.faultrule.size()).isEmpty() ) {
						 * 
						 * rightsideattr = new ArrayList<ArrayList<String>>();
						 * 
						 * rightsideattr.add(new ArrayList<String>());
						 * //NSGAIIpostprocessing.allattributrightside = new
						 * ArrayList<ArrayList<ArrayList<String>>>();
						 * NSGAIIpostprocessing.allattributrightside.add( rightsideattr); }
						 * 
						 * }
						 */
						// rightsideattr.clear();
						// rightsideattr = new ArrayList<ArrayList<String>>();
						// NSGAIIpostprocessing.rightsideattr.add(new ArrayList<String>());
						// row2.clear();
						System.out.println("all3");
						System.out.println(NSGAIIpostprocessing.allattributrightside);

					}
					if (indexfound3 > -1) {
						NSGAIIpostprocessing.sequencelocation.add(linecount);

					}
					if (indexfound8 > -1) {
						listassequence.add(linecount);

					}
					if (indexfound9 > -1) {
						listassequence.add(linecount);

					}
					if (line.indexOf(stringSearch4.get(0)) > -1 || line.indexOf(stringSearch4.get(1)) > -1
							|| line.indexOf(stringSearch4.get(2)) > -1) {
						String[] parts = line.toString().split(",|\\.|\\s+|and|or|!|\\)|\\->");
						NSGAIIpostprocessing.ocliskineoflocation.add(linecount);

						NSGAIIpostprocessing.oclkindlineattr.add(String.valueOf(linecount));

						for (int j = 0; j < parts.length; j++)
							if (!parts[j].isEmpty()) {
								// String[] parts2 =parts[j].split("\\s+");
								NSGAIIpostprocessing.oclkindlineattr.add(parts[j]);

								// System.out.println(parts2[1]);

							}
						System.out.println("ok");
						System.out.println(NSGAIIpostprocessing.oclkindlineattr);

					}
					if (indexfound11 > -1) {
						NSGAIIpostprocessing.parameterlocation.add(linecount);

					}
					if (indexfound12 > -1) {
						NSGAIIpostprocessing.listnot.add(linecount);

					}
					if (indexfound13 > -1) {
						NSGAIIpostprocessing.qumecall.add(linecount);

					}
					if (indexfound14 > -1) {
						listlazy.add(linecount);

					}

					if (indexfound16 > -1) {
						NSGAIIpostprocessing.listattrhelper.add(new ArrayList<String>());
						NSGAIIpostprocessing.linehelper.add(new ArrayList<Integer>());
						// NSGAIIpostprocessing.listattrhelper.get(indexhelper).add(new
						// ArrayList<String>());
						String[] parts = line.split("def:");
						String[] subparts = parts[1].split(":");
						NSGAIIpostprocessing.listattrhelper.get(indexhelper).add(subparts[0]);
						System.out.println(NSGAIIpostprocessing.listattrhelper.get(indexhelper));
						indexhelper = indexhelper + 1;
						insidehelper = true;

					}

					if (indexfound17 > -1 && insidehelper == true) {
						String[] parts = line.split("\\+|\\.|\\'|\\;|\\->|\\;|\\ |\\)|\\)->|\\s+");

						// String str = new String(parts);

						System.out.println("kkk");
						System.out.println(parts.length);
						for (int j = 0; j < parts.length; j++)
							if (!parts[j].equals(" ")) {
								NSGAIIpostprocessing.listattrhelper.get(indexhelper - 1).add(parts[j]);
								System.out.println(parts[j]);
							}
					}

					for (int i = 0; i < NSGAIIpostprocessing.listattrhelper.size(); i++) {

						String[] parts = NSGAIIpostprocessing.listattrhelper.get(i).get(0).split("\\s+");
						if (line.contains(parts[1])) {

							NSGAIIpostprocessing.linehelper.get(i).add(linecount);

						}

					}

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("whole");
			System.out.println(NSGAIIpostprocessing.usingfunction);
			System.out.println(NSGAIIpostprocessing.usingline);
			System.out.println(NSGAIIpostprocessing.allattributrightside);
			System.out.println(NSGAIIpostprocessing.locationfrom);
			System.out.println(NSGAIIpostprocessing.locationfilter);
			System.out.println(NSGAIIpostprocessing.oclletter);
			System.out.println(NSGAIIpostprocessing.fleshline);
			System.out.println(NSGAIIpostprocessing.ocliskineoflocation);
			System.out.println(NSGAIIpostprocessing.leftsideattriteration);
			System.out.println(NSGAIIpostprocessing.iterationcall);
			System.out.println(NSGAIIpostprocessing.listattrhelper);

			for (int i = 0; i < NSGAIIpostprocessing.locationfilter.size(); i++) {
				if (NSGAIIpostprocessing.locationfilter.get(i) - NSGAIIpostprocessing.locationfrom.get(i) > 2) {

					NSGAIIpostprocessing.rulefilter.add(i);

				}

			}
			System.out.println(alllistnavigationsplitdot);
			System.out.println(NSGAIIpostprocessing.listattrhelper);
			for (int i = 1; i < NSGAIIpostprocessing.faultrule.size(); i++)
				NSGAIIpostprocessing.finalrule.add(NSGAIIpostprocessing.faultrule.get(i) - 2);
			NSGAIIpostprocessing.finalrule.add(lastline);
			calculatelistsourcemetamodel();
			calculatelisttargetmetamodel();

			// for step one project
			/*
			 * for(int i=0;i<NSGAIIpostprocessing.inpatternstringlocation.size();i++ )
			 * 
			 * if(
			 * (NSGAIIpostprocessing.locationfilter.get(i)-NSGAIIpostprocessing.locationfrom
			 * .get(i))>2) NSGAIIpostprocessing.inpatternhasfilter.add(1); else
			 * NSGAIIpostprocessing.inpatternhasfilter.add(0);
			 * 
			 */
		}

	}

	private void SplitAtrrLeftSideIteraion(int linecount, List<String> leftsideiterationsplitter, String line) {
		// TODO Auto-generated method stub
		NSGAIIpostprocessing.leftsideattriteration.add(String.valueOf(linecount));
		boolean checkleft = false;
		for (int i = 0; i < leftsideiterationsplitter.size(); i++) {
			if (line.toString().contains(leftsideiterationsplitter.get(i))) {
				checkleft = true;
			}

		}
		if (checkleft == true) {

			String[] parts = line.toString().split("\\=|\\->|\\<-");
			String[] parts2 = parts[1].split("\\.|\\!|\\.|\\s+");

			for (int jj = 0; jj < parts2.length; jj++)
				if (!parts2[jj].isEmpty()) {
					// System.out.println("second");
					NSGAIIpostprocessing.leftsideattriteration.add(parts2[jj]);

				}

		} else {
			String[] parts = line.toString().split("\\=|\\->|\\<-");
			String[] parts2 = parts[0].split("\\.|\\!|\\.|\\s+");
			for (int jj = 0; jj < parts2.length; jj++)
				if (!parts2[jj].isEmpty()) {
					// System.out.println("second");
					NSGAIIpostprocessing.leftsideattriteration.add(parts2[jj]);

				}

		}

	}

	private void calculatelistsourcemetamodel() {
		// TODO Auto-generated method stub

		Settingpostprocessing s = new Settingpostprocessing();
		String MMRootPath2 = s.gettargetmetamodel();
		MetaModel metatarget = null;
		try {
			metatarget = new MetaModel(MMRootPath2);
		} catch (transException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int yy = 0;
		int start = 0;
		int lenght = 0;
		int start2 = 0;
		int lenght2 = 0;
		for (EClassifier classifier : metatarget.getEClassifiers()) {

			if (classifier instanceof EClass) {

				EClass child = ((EClass) classifier);
				NSGAIIpostprocessing.classnamestring.add(child.getName());
				NSGAIIpostprocessing.classnamestartpoint.add(start);

				NSGAIIpostprocessing.classnamestartpoint2.add(start2);

				for (int y = 0; y < classifier.eContents().size(); y++) {

					if (classifier.eContents().get(y) instanceof EAttribute
							|| classifier.eContents().get(y) instanceof EReference) {

						NSGAIIpostprocessing.listsourcemetamodel
								.add((EStructuralFeature) classifier.eContents().get(y));
						NSGAIIpostprocessing.listinheritattributesourcemetamodel
								.add((EStructuralFeature) classifier.eContents().get(y));
						NSGAIIpostprocessing.listnavigationtype
								.add(((EStructuralFeature) classifier.eContents().get(y)).getEType().getName());
						NSGAIIpostprocessing.listnavigationtypeinheritattr
								.add(((EStructuralFeature) classifier.eContents().get(y)).getEType().getName());
						yy = yy + 1;
						lenght = lenght + 1;

						lenght2 = lenght2 + 1;
					}

				}

				NSGAIIpostprocessing.classnamelength2.add(lenght2);
				start2 = start2 + lenght2;
				lenght2 = 0;

				for (int i = 0; i < child.getEAllSuperTypes().size(); i++) {

					EClass classifier2 = child.getEAllSuperTypes().get(i);

					if (classifier2.getName() != null) {
						for (int j = 0; j < classifier2.eContents().size(); j++) {
							if (classifier2.eContents().get(j) instanceof EAttribute
									|| classifier2.eContents().get(j) instanceof EReference) {

								NSGAIIpostprocessing.listinheritattributesourcemetamodel
										.add((EStructuralFeature) classifier2.eContents().get(j));
								NSGAIIpostprocessing.listnavigationtypeinheritattr.add(
										((EStructuralFeature) classifier2.eContents().get(j)).getEType().getName());
								lenght = lenght + 1;

							}

						}

					}
				}

				for (EClassifier classifier2 : metatarget.getEClassifiers()) {
					if (classifier2 instanceof EClass) {
						EClass child2 = ((EClass) classifier2);
						if (child.isSuperTypeOf(child2) && !child2.equals(child)) {

							for (int j = 0; j < child2.eContents().size(); j++) {

								if (child2.eContents().get(j) instanceof EAttribute
										|| child2.eContents().get(j) instanceof EReference) {

									NSGAIIpostprocessing.listinheritattributesourcemetamodel
											.add((EStructuralFeature) child2.eContents().get(j));
									NSGAIIpostprocessing.listnavigationtypeinheritattr
											.add(((EStructuralFeature) child2.eContents().get(j)).getEType().getName());
									lenght = lenght + 1;

								}

							}

						} else {
							if (!child2.isSuperTypeOf(child)) {
								for (int j = 0; j < child2.eContents().size(); j++) {

									if ((child2.eContents().get(j) instanceof EAttribute
											|| child2.eContents().get(j) instanceof EReference)
											&& ((EStructuralFeature) child2.eContents().get(j)).getEType().getName()
													.equals(child.getName())) {

										NSGAIIpostprocessing.listinheritattributesourcemetamodel
												.add((EStructuralFeature) child2.eContents().get(j));
										NSGAIIpostprocessing.listnavigationtypeinheritattr.add(
												((EStructuralFeature) child2.eContents().get(j)).getEType().getName());
										lenght = lenght + 1;

									}

								}
							}

						}

					}

				}

				NSGAIIpostprocessing.classnamelength.add(lenght);
				start = start + lenght;
				lenght = 0;

			}
		}

	}

	private void calculatelisttargetmetamodel() {
		// TODO Auto-generated method stub
		// baraye targete eshtebahi neveshtam getsource
		Settingpostprocessing s = new Settingpostprocessing();
		String MMRootPath2 = s.getsourcemetamodel();
		MetaModel metatarget = null;
		try {
			metatarget = new MetaModel(MMRootPath2);
		} catch (transException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int yy = 0;
		int start = 0;
		int lenght = 0;
		int start2 = 0;
		int lenght2 = 0;
		for (EClassifier classifier : metatarget.getEClassifiers()) {

			if (classifier instanceof EClass) {

				EClass child = ((EClass) classifier);
				NSGAIIpostprocessing.classnamestringtarget.add(child.getName());
				NSGAIIpostprocessing.classnamestartpointtarget.add(start);

				for (int y = 0; y < classifier.eContents().size(); y++) {

					if (classifier.eContents().get(y) instanceof EAttribute
							|| classifier.eContents().get(y) instanceof EReference) {

						// NSGAIIpostprocessing.listsourcemetamodel.add( (EStructuralFeature)
						// classifier.eContents().get(y));
						NSGAIIpostprocessing.listinheritattributesourcemetamodeltarget
								.add((EStructuralFeature) classifier.eContents().get(y));
						// NSGAIIpostprocessing.listnavigationtype.add(((EStructuralFeature)
						// classifier.eContents().get(y)).getEType().getName());
						NSGAIIpostprocessing.listnavigationtypeinheritattrtarget
								.add(((EStructuralFeature) classifier.eContents().get(y)).getEType().getName());
						yy = yy + 1;
						lenght = lenght + 1;

						// lenght2=lenght2+1;
					}

				}

				for (int i = 0; i < child.getEAllSuperTypes().size(); i++) {

					EClass classifier2 = child.getEAllSuperTypes().get(i);

					if (classifier2.getName() != null) {
						for (int j = 0; j < classifier2.eContents().size(); j++) {
							if (classifier2.eContents().get(j) instanceof EAttribute
									|| classifier2.eContents().get(j) instanceof EReference) {

								NSGAIIpostprocessing.listinheritattributesourcemetamodeltarget
										.add((EStructuralFeature) classifier2.eContents().get(j));
								NSGAIIpostprocessing.listnavigationtypeinheritattrtarget.add(
										((EStructuralFeature) classifier2.eContents().get(j)).getEType().getName());
								lenght = lenght + 1;

							}

						}

					}
				}

				/*
				 * for (EClassifier classifier2 : metatarget.getEClassifiers()) { if
				 * (classifier2 instanceof EClass) { EClass child2 = ((EClass) classifier2);
				 * if(child.isSuperTypeOf(child2) && !child2.equals(child)) {
				 * 
				 * for(int j=0;j< child2.eContents().size();j++) {
				 * 
				 * if (child2.eContents().get(j) instanceof EAttribute
				 * ||child2.eContents().get(j) instanceof EReference ) {
				 * 
				 * NSGAIIpostprocessing.listinheritattributesourcemetamodeltarget.add(
				 * (EStructuralFeature) child2.eContents().get(j));
				 * NSGAIIpostprocessing.listnavigationtypeinheritattrtarget.add(((
				 * EStructuralFeature) child2.eContents().get(j)).getEType().getName());
				 * lenght=lenght+1;
				 * 
				 * }
				 * 
				 * 
				 * }
				 * 
				 * 
				 * } else { if( !child2.isSuperTypeOf(child) ) { for(int j=0;j<
				 * child2.eContents().size();j++) {
				 * 
				 * if ((child2.eContents().get(j) instanceof EAttribute
				 * ||child2.eContents().get(j) instanceof EReference) && ( (EStructuralFeature)
				 * child2.eContents().get(j)).getEType().getName().equals( child.getName() ) ) {
				 * 
				 * 
				 * NSGAIIpostprocessing.listinheritattributesourcemetamodeltarget.add(
				 * (EStructuralFeature) child2.eContents().get(j));
				 * NSGAIIpostprocessing.listnavigationtypeinheritattrtarget.add(((
				 * EStructuralFeature) child2.eContents().get(j)).getEType().getName());
				 * lenght=lenght+1;
				 * 
				 * 
				 * 
				 * }
				 * 
				 * 
				 * } }
				 * 
				 * }
				 * 
				 * }
				 * 
				 * }
				 */

				NSGAIIpostprocessing.classnamelengthtarget.add(lenght);
				start = start + lenght;
				lenght = 0;

			}
		}

	}

	private void createclassifier() {
		// TODO Auto-generated method stub
		Setting s = new Setting();
		final String MMRootPath2 = s.getsourcemetamodel();

		List<EPackage> metamodels2 = retPackMM(retPackResouceMM(MMRootPath2));
		MetaModel metamodel = null;
		for (EPackage p : metamodels2) {
			metamodel = new MetaModel(p);
		}

		for (EClassifier classifier : metamodel.getEClassifiers()) {
			if (classifier instanceof EClass) {
				EClass child = ((EClass) classifier);

				NSGAIIpostprocessing.classifierliast.add(classifier);
			}

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

	private /* static */ long index = 1;

	private String getValidNameOfFile(String outputFolder) {
		String outputfile = null;
		String aux = null;
		for (long i = index; outputfile == null; i++) {
			aux = File.separatorChar + "finalresult" + ".atl";
			if (!new File(outputFolder, aux).exists()) {
				outputfile = outputFolder + aux;
				index = i;
			} else
				index = i;
		}
		return outputfile;
	}

	protected boolean save(EMFModel atlModel, String outputFolder) {
		try {
			// save atl file
			System.out.println("outputFolder");
			System.out.println(outputFolder);
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

	public void Testeroperate() throws ATLCoreException {

		String temporalFolder = "C:/Users/varaminz/Downloads/Project/workspaceTEST34/anatlyzer.atl.tests.api/trafo2/zahra2";
		String trafo = "C:/Users/varaminz/Downloads/Project/workspaceTEST34/anatlyzer.atl.tests.api/trafo/PNML2PetriNet.atl";
		this.folderMutants = temporalFolder + "mutants" + File.separator;
		this.atlModel = this.loadTransformationModel(trafo);
		this.deleteDirectory(this.folderMutants, true);
		this.createDirectory(this.folderMutants);
		this.save(this.atlModel, this.folderMutants);

	}

} // NSGA-II
