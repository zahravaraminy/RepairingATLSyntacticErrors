//  Solution.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Description: 
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

package jmetal.core;

import jmetal.encodings.solutionType.IntSolutionType;
import jmetal.encodings.variable.Binary;
import jmetal.metaheuristics.nsgaII.NSGAIIpostprocessing;
//import jmetal.problems.AdaptiveInterface.File;
import jmetal.problems.MyProblem;

import java.awt.List;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.core.ModelFactory;
import org.eclipse.m2m.atl.core.emf.EMFModel;
import org.eclipse.m2m.atl.core.emf.EMFModelFactory;
import org.eclipse.m2m.atl.core.emf.EMFReferenceModel;
import org.eclipse.m2m.atl.engine.parser.AtlParser;

import anatlyzer.atl.model.ATLModel;
import anatlyzer.atlext.ATL.Binding;
import anatlyzer.atlext.OCL.OclExpression;
import ca.udem.fixingatlerror.refinementphase.main;
import ca.udem.fixingatlerror.refinementphase.CoSolution;
import ca.udem.fixingatlerror.refinementphase.CoSolutionpostprocessing;


/**
 * Class representing a solution for a problem.
 */
public class Solution implements Serializable {

	private EDataTypeEList<String> comments = null;
	public ArrayList<String> totalcomments = new ArrayList<String>();
	private double firstfitness;
	private double secondfitness;
	private int fitnessthird;
	private boolean previousgeneration;
	private ArrayList<ArrayList<String>> listpropertyname = new ArrayList<ArrayList<String>>();
	public ArrayList<String> inorout = new ArrayList<String>();
	public ArrayList<String> modifypropertyname = new ArrayList<String>();
	public ArrayList<EClassifier> classifiersolution = new ArrayList<EClassifier>();
	public ArrayList<OclExpression> expression = new ArrayList<OclExpression>();
	public ArrayList<Binding> newbindings = new ArrayList<Binding>();
	public ArrayList<String> newstring = new ArrayList<String>();
	public ArrayList<EClassifier> listeobject = new ArrayList<EClassifier>();

	private int index;
	private int numbererror;
	private int oldrandomIntoperation4;
	private int replacerandomIntoperation4;
	private int secondoldrandomIntoperation4;
	private int oldrandomIntoperation3;
	private int replacerandomIntoperation3;
	private int secondoldrandomIntoperation3;
	private int oldrandomIntoperation2;
	private int replacerandomIntoperation2;
	private int secondoldrandomIntoperation2;
	private int oldrandomIntoperation1;
	private int replacerandomIntoperation1;
	private int secondoldrandomIntoperation1;
	private int oldrandomIntoperation5;
	private int replacerandomIntoperation5;
	private int secondoldrandomIntoperation5;
	private int oldrandomIntoperation6;
	private int replacerandomIntoperation6;
	private int secondoldrandomIntoperation6;
	private int oldrandomIntoperation7;
	private int replacerandomIntoperation7;
	private int secondoldrandomIntoperation7;
	private int oldrandomIntoperation8;
	private int replacerandomIntoperation8;
	private int secondoldrandomIntoperation8;
	private int oldrandomIntoperation9;
	private int replacerandomIntoperation9;
	private int secondoldrandomIntoperation9;
	private int oldrandomIntoperation10;
	private int replacerandomIntoperation10;
	private int secondoldrandomIntoperation10;
	private int oldrandomIntoperation11;
	private int replacerandomIntoperation11;
	private int secondoldrandomIntoperation11;
	private int oldrandomIntoperation12;
	private int replacerandomIntoperation12;
	private int secondoldrandomIntoperation12;
	private int oldrandomIntoperation13;
	private int replacerandomIntoperation13;
	private int secondoldrandomIntoperation13;
	private int oldrandomIntoperation14;
	private int replacerandomIntoperation14;
	private int secondoldrandomIntoperation14;
	private int oldrandomIntoperation15;
	private int replacerandomIntoperation15;
	private int secondoldrandomIntoperation15;
	private int oldrandomIntoperation16;
	private int replacerandomIntoperation16;
	private int secondoldrandomIntoperation16;
	private int oldrandomIntoperation17;
	private int replacerandomIntoperation17;
	private int secondoldrandomIntoperation17;
	private int oldrandomIntoperation18;
	private int replacerandomIntoperation18;
	private int secondoldrandomIntoperation18;
	private int oldrandomIntoperation19;
	private int replacerandomIntoperation19;
	private int secondoldrandomIntoperation19;
	private int oldrandomIntoperation20;
	private int replacerandomIntoperation20;
	private int secondoldrandomIntoperation20;
	private int oldrandomIntoperation21;
	private int replacerandomIntoperation21;
	private int secondoldrandomIntoperation21;
	private int oldrandomIntoperation22;
	private int replacerandomIntoperation22;
	private int secondoldrandomIntoperation22;
	public ArrayList<Integer> TwoIndexDeletion = new ArrayList<Integer>();
	public ArrayList<Integer> listlineofcodes = new ArrayList<Integer>();
	public ArrayList<Integer> listfilterdeletion = new ArrayList<Integer>();
	public ArrayList<Integer> listfirstindices = new ArrayList<Integer>();
	public ArrayList<Integer> listsecondindices = new ArrayList<Integer>();
	private ArrayList<Integer> listmutation = new ArrayList<Integer>();

	/**
	 * Stores the problem
	 */
	private Problem problem_;

	/**
	 * Stores the type of the encodings.variable
	 */
	private SolutionType type_;

	/**
	 * Stores the decision variables of the solution.
	 */
	private Variable[] variable_;
	/**
	 * Stores the rule of the solution.
	 */
	private ArrayList<Integer> operations_;
	private ArrayList<String> comments2;

	/**
	 * Stores the objectives values of the solution.
	 */
	private final double[] objective_;

	/**
	 * Stores the number of objective values of the solution
	 */
	private int numberOfObjectives_;

	/**
	 * Stores the so called fitness value. Used in some metaheuristics
	 */
	private double fitness_;

	/**
	 * Used in algorithm AbYSS, this field is intended to be used to know when a
	 * <code>Solution</code> is marked.
	 */
	private boolean marked_;

	/**
	 * Stores the so called rank of the solution. Used in NSGA-II
	 */
	private int rank_;

	/**
	 * Stores the overall constraint violation of the solution.
	 */
	private double overallConstraintViolation_;

	/**
	 * Stores the number of constraints violated by the solution.
	 */
	private int numberOfViolatedConstraints_;

	/**
	 * This field is intended to be used to know the location of a solution into a
	 * <code>SolutionSet</code>. Used in MOCell
	 */
	private int location_;

	/**
	 * Stores the distance to his k-nearest neighbor into a
	 * <code>SolutionSet</code>. Used in SPEA2.
	 */
	private double kDistance_;

	/**
	 * Stores the crowding distance of the the solution in a
	 * <code>SolutionSet</code>. Used in NSGA-II.
	 */
	private double crowdingDistance_;

	/**
	 * Stores the distance between this solution and a <code>SolutionSet</code>.
	 * Used in AbySS.
	 */
	private double distanceToSolutionSet_;

	private CoSolution S_;
	private CoSolutionpostprocessing Sc_;

	/**
	 * Constructor.
	 */
	public Solution() {
		problem_ = null;
		marked_ = false;
		overallConstraintViolation_ = 0.0;
		numberOfViolatedConstraints_ = 0;
		type_ = null;
		variable_ = null;
		objective_ = null;
		operations_ = null;
		S_ = null;
	} // Solution

	/**
	 * Constructor
	 * 
	 * @param numberOfObjectives
	 *            Number of objectives of the solution
	 * 
	 *            This constructor is used mainly to read objective values from a
	 *            file to variables of a SolutionSet to apply quality indicators
	 */
	public Solution(int numberOfObjectives) {
		numberOfObjectives_ = numberOfObjectives;
		objective_ = new double[numberOfObjectives];
	}

	/**
	 * Constructor.
	 * 
	 * @param problem
	 *            The problem to solve
	 * @throws ClassNotFoundException
	 */

	public Solution(Problem problem) throws ClassNotFoundException {
		problem_ = problem;
		type_ = problem.getSolutionType();
		numberOfObjectives_ = problem.getNumberOfObjectives();
		objective_ = new double[numberOfObjectives_];

		// Setting initial values
		fitness_ = 0.0;
		kDistance_ = 0.0;
		crowdingDistance_ = 0.0;
		distanceToSolutionSet_ = Double.POSITIVE_INFINITY;
		// <-
		oldrandomIntoperation4 = -2;
		replacerandomIntoperation4 = -2;
		secondoldrandomIntoperation4 = -2;
		oldrandomIntoperation3 = -2;
		replacerandomIntoperation3 = -2;
		secondoldrandomIntoperation3 = -2;
		oldrandomIntoperation2 = -2;
		replacerandomIntoperation2 = -2;
		secondoldrandomIntoperation2 = -2;
		oldrandomIntoperation1 = -2;
		replacerandomIntoperation1 = -2;
		secondoldrandomIntoperation1 = -2;
		oldrandomIntoperation5 = -2;
		replacerandomIntoperation5 = -2;
		secondoldrandomIntoperation5 = -2;
		oldrandomIntoperation6 = -2;
		replacerandomIntoperation6 = -2;
		secondoldrandomIntoperation6 = -2;
		oldrandomIntoperation7 = -2;
		replacerandomIntoperation7 = -2;
		secondoldrandomIntoperation7 = -2;
		oldrandomIntoperation8 = -2;
		replacerandomIntoperation8 = -2;
		secondoldrandomIntoperation8 = -2;
		oldrandomIntoperation9 = -2;
		replacerandomIntoperation9 = -2;
		secondoldrandomIntoperation9 = -2;
		oldrandomIntoperation10 = -2;
		replacerandomIntoperation10 = -2;
		secondoldrandomIntoperation10 = -2;
		oldrandomIntoperation11 = -2;
		replacerandomIntoperation11 = -2;
		secondoldrandomIntoperation11 = -2;
		oldrandomIntoperation12 = -2;
		replacerandomIntoperation12 = -2;
		secondoldrandomIntoperation12 = -2;
		oldrandomIntoperation13 = -2;
		replacerandomIntoperation13 = -2;
		secondoldrandomIntoperation13 = -2;
		oldrandomIntoperation14 = -2;
		replacerandomIntoperation14 = -2;
		secondoldrandomIntoperation14 = -2;
		oldrandomIntoperation15 = -2;
		replacerandomIntoperation15 = -2;
		secondoldrandomIntoperation15 = -2;
		oldrandomIntoperation16 = -2;
		replacerandomIntoperation16 = -2;
		secondoldrandomIntoperation16 = -2;
		oldrandomIntoperation17 = -2;
		replacerandomIntoperation17 = -2;
		secondoldrandomIntoperation17 = -2;
		oldrandomIntoperation18 = -2;
		replacerandomIntoperation18 = -2;
		secondoldrandomIntoperation18 = -2;
		oldrandomIntoperation19 = -2;
		replacerandomIntoperation19 = -2;
		secondoldrandomIntoperation19 = -2;
		oldrandomIntoperation20 = -2;
		replacerandomIntoperation20 = -2;
		secondoldrandomIntoperation20 = -2;
		oldrandomIntoperation21 = -2;
		replacerandomIntoperation21 = -2;
		secondoldrandomIntoperation21 = -2;
		oldrandomIntoperation22 = -2;
		replacerandomIntoperation22 = -2;
		secondoldrandomIntoperation22 = -2;
		int operationsize = 10;
		comments = null;
		index = 0;
		numbererror = 0;
		// variable_ = problem.solutionType_.createVariables() ;
		if (main.postprocessing == false) {
			variable_ = type_.createVariables();
			operations_ = IntSolutionType.operations;
		}
		this.classifiersolution.clear();
		classifiersolution = new ArrayList<EClassifier>();
		for (int l = 0; l < operationsize; l++) {
			EClassifier classifier = null;
			this.classifiersolution.add(classifier);
		}
		this.newbindings.clear();
		newbindings = new ArrayList<Binding>();
		for (int l = 0; l < operationsize; l++) {
			Binding classifier = null;
			this.newbindings.add(classifier);
		}
		this.inorout.clear();
		inorout = new ArrayList<String>();
		for (int l = 0; l < operationsize; l++)
			this.inorout.add("empty");

		this.expression.clear();
		expression = new ArrayList<OclExpression>();
		for (int l = 0; l < operationsize; l++) {
			OclExpression exp = null;
			this.expression.add(exp);
		}
		this.modifypropertyname.clear();
		modifypropertyname = new ArrayList<String>();
		for (int l = 0; l < operationsize; l++)
			this.modifypropertyname.add("khali");

		this.newstring.clear();
		newstring = new ArrayList<String>();
		for (int l = 0; l < operationsize; l++)
			this.newstring.add("khali");

		this.listeobject.clear();
		listeobject = new ArrayList<EClassifier>();
		for (int l = 0; l < operationsize; l++) {
			EClassifier ob = null;
			this.listeobject.add(ob);
		}

		if (main.postprocessing == false)
			S_ = new CoSolution();
		else
			Sc_ = new CoSolutionpostprocessing();
		// System.out.println("end solution");
	} // Solution

	public ArrayList<ArrayList<String>> getlistpropertyname() {
		return listpropertyname;
	}

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
		return atlModel;
	}

	static public Solution getNewSolution(Problem problem) throws ClassNotFoundException {
		return new Solution(problem);
	}

	/**
	 * Constructor
	 * 
	 * @param problem
	 *            The problem to solve
	 */
	public Solution(Problem problem, Variable[] variables) {
		problem_ = problem;
		type_ = problem.getSolutionType();
		numberOfObjectives_ = problem.getNumberOfObjectives();
		objective_ = new double[numberOfObjectives_];

		// Setting initial values
		fitness_ = 0.0;
		kDistance_ = 0.0;
		crowdingDistance_ = 0.0;
		distanceToSolutionSet_ = Double.POSITIVE_INFINITY;
		// <-

		variable_ = variables;
		operations_ = IntSolutionType.operations;
		S_ = IntSolutionType.S;
	} // Constructor

	/**
	 * Copy constructor.
	 * 
	 * @param solution
	 *            Solution to copy.
	 */
	public Solution(Solution solution) {
		problem_ = solution.problem_;
		type_ = solution.type_;

		numberOfObjectives_ = solution.getNumberOfObjectives();
		objective_ = new double[numberOfObjectives_];
		for (int i = 0; i < objective_.length; i++) {
			objective_[i] = solution.getObjective(i);
		} // for
			// <-

		// variable_ = type_.copyVariables(solution.variable_) ;
		// operations_ = solution.rule();
		overallConstraintViolation_ = solution.getOverallConstraintViolation();
		numberOfViolatedConstraints_ = solution.getNumberOfViolatedConstraint();
		distanceToSolutionSet_ = solution.getDistanceToSolutionSet();
		crowdingDistance_ = solution.getCrowdingDistance();
		kDistance_ = solution.getKDistance();
		fitness_ = solution.getFitness();
		marked_ = solution.isMarked();
		rank_ = solution.getRank();
		location_ = solution.getLocation();
		this.operations_ = new ArrayList<Integer>();
		this.operations_.clear();
		this.classifiersolution.clear();
		this.modifypropertyname.clear();
		this.newstring.clear();
		this.inorout.clear();
		this.expression.clear();
		this.newbindings.clear();
		this.listeobject.clear();
		this.inorout = new ArrayList<String>();
		this.modifypropertyname = new ArrayList<String>();
		this.classifiersolution = new ArrayList<EClassifier>();
		this.expression = new ArrayList<OclExpression>();
		this.newbindings = new ArrayList<Binding>();
		this.newstring = new ArrayList<String>();
		this.listeobject = new ArrayList<EClassifier>();

		for (int i = 0; i < solution.operation().size(); i++) {
			this.classifiersolution.add(solution.classifiersolution.get(i));
			this.modifypropertyname.add(solution.modifypropertyname.get(i));
			this.newstring.add(solution.newstring.get(i));
			this.inorout.add(solution.inorout.get(i));
			this.newbindings.add(solution.newbindings.get(i));
			this.expression.add(solution.expression.get(i));
			this.operations_.add(solution.operation().get(i));
			this.listeobject.add(solution.listeobject.get(i));
			S_ = solution.getCoSolution();
			Sc_ = solution.getCoSolutionpostprocessing();
			oldrandomIntoperation4 = solution.getoldrandomIntoperation4();
			replacerandomIntoperation4 = solution.getreplacerandomIntoperation4();
			secondoldrandomIntoperation4 = solution.getsecondoldrandomIntoperation4();
			oldrandomIntoperation3 = solution.getoldrandomIntoperation3();
			replacerandomIntoperation3 = solution.getreplacerandomIntoperation3();
			secondoldrandomIntoperation3 = solution.getsecondoldrandomIntoperation3();
			oldrandomIntoperation2 = solution.getoldrandomIntoperation2();
			replacerandomIntoperation2 = solution.getreplacerandomIntoperation2();
			secondoldrandomIntoperation2 = solution.getsecondoldrandomIntoperation2();
			oldrandomIntoperation1 = solution.getoldrandomIntoperation1();
			replacerandomIntoperation1 = solution.getreplacerandomIntoperation1();
			secondoldrandomIntoperation1 = solution.getsecondoldrandomIntoperation1();
			oldrandomIntoperation5 = solution.getoldrandomIntoperation5();
			replacerandomIntoperation5 = solution.getreplacerandomIntoperation5();
			secondoldrandomIntoperation5 = solution.getsecondoldrandomIntoperation5();
			oldrandomIntoperation6 = solution.getoldrandomIntoperation6();
			replacerandomIntoperation6 = solution.getreplacerandomIntoperation6();
			secondoldrandomIntoperation6 = solution.getsecondoldrandomIntoperation6();
			oldrandomIntoperation7 = solution.getoldrandomIntoperation7();
			replacerandomIntoperation7 = solution.getreplacerandomIntoperation7();
			secondoldrandomIntoperation7 = solution.getsecondoldrandomIntoperation7();
			oldrandomIntoperation8 = solution.getoldrandomIntoperation8();
			replacerandomIntoperation8 = solution.getreplacerandomIntoperation8();
			secondoldrandomIntoperation8 = solution.getsecondoldrandomIntoperation8();
			oldrandomIntoperation9 = solution.getoldrandomIntoperation9();
			replacerandomIntoperation9 = solution.getreplacerandomIntoperation9();
			secondoldrandomIntoperation9 = solution.getsecondoldrandomIntoperation9();
			oldrandomIntoperation10 = solution.getoldrandomIntoperation10();
			replacerandomIntoperation10 = solution.getreplacerandomIntoperation10();
			secondoldrandomIntoperation10 = solution.getsecondoldrandomIntoperation10();
			oldrandomIntoperation11 = solution.getoldrandomIntoperation11();
			replacerandomIntoperation11 = solution.getreplacerandomIntoperation11();
			secondoldrandomIntoperation11 = solution.getsecondoldrandomIntoperation11();
			oldrandomIntoperation12 = solution.getoldrandomIntoperation12();
			replacerandomIntoperation12 = solution.getreplacerandomIntoperation12();
			secondoldrandomIntoperation12 = solution.getsecondoldrandomIntoperation12();
			oldrandomIntoperation13 = solution.getoldrandomIntoperation13();
			replacerandomIntoperation13 = solution.getreplacerandomIntoperation13();
			secondoldrandomIntoperation13 = solution.getsecondoldrandomIntoperation13();
			oldrandomIntoperation14 = solution.getoldrandomIntoperation14();
			replacerandomIntoperation14 = solution.getreplacerandomIntoperation14();
			secondoldrandomIntoperation14 = solution.getsecondoldrandomIntoperation14();
			oldrandomIntoperation15 = solution.getoldrandomIntoperation15();
			replacerandomIntoperation15 = solution.getreplacerandomIntoperation15();
			secondoldrandomIntoperation15 = solution.getsecondoldrandomIntoperation15();
			oldrandomIntoperation16 = solution.getoldrandomIntoperation16();
			replacerandomIntoperation16 = solution.getreplacerandomIntoperation16();
			secondoldrandomIntoperation16 = solution.getsecondoldrandomIntoperation16();
			oldrandomIntoperation17 = solution.getoldrandomIntoperation17();
			replacerandomIntoperation17 = solution.getreplacerandomIntoperation17();
			secondoldrandomIntoperation17 = solution.getsecondoldrandomIntoperation17();
			oldrandomIntoperation18 = solution.getoldrandomIntoperation18();
			replacerandomIntoperation18 = solution.getreplacerandomIntoperation18();
			secondoldrandomIntoperation18 = solution.getsecondoldrandomIntoperation18();
			oldrandomIntoperation19 = solution.getoldrandomIntoperation19();
			replacerandomIntoperation19 = solution.getreplacerandomIntoperation19();
			secondoldrandomIntoperation19 = solution.getsecondoldrandomIntoperation19();
			oldrandomIntoperation20 = solution.getoldrandomIntoperation20();
			replacerandomIntoperation20 = solution.getreplacerandomIntoperation20();
			secondoldrandomIntoperation20 = solution.getsecondoldrandomIntoperation20();
			oldrandomIntoperation21 = solution.getoldrandomIntoperation21();
			replacerandomIntoperation21 = solution.getreplacerandomIntoperation21();
			secondoldrandomIntoperation21 = solution.getsecondoldrandomIntoperation21();
			oldrandomIntoperation22 = solution.getoldrandomIntoperation22();
			replacerandomIntoperation22 = solution.getreplacerandomIntoperation22();
			secondoldrandomIntoperation22 = solution.getsecondoldrandomIntoperation22();

		}
		// rules_ = solution.getRules();
	} // Solution

	public double getfirstfitness() {
		return firstfitness;
	}

	public int getfitnessthird() {
		return fitnessthird;
	}

	public boolean getpreviousgeneration() {
		return this.previousgeneration;
	}

	public void setpreviousgeneration(boolean b) {
		this.previousgeneration = b;
	}

	public double getsecondfitness() {
		return secondfitness;
	}

	public int getoldrandomIntoperation4() {
		return oldrandomIntoperation4;
	}

	public int getreplacerandomIntoperation4() {
		return replacerandomIntoperation4;
	}

	public int getsecondoldrandomIntoperation4() {
		return secondoldrandomIntoperation4;
	}

	public int getoldrandomIntoperation3() {
		return oldrandomIntoperation3;
	}

	public int getreplacerandomIntoperation3() {
		return replacerandomIntoperation3;
	}

	public int getsecondoldrandomIntoperation3() {
		return secondoldrandomIntoperation3;
	}

	public int getoldrandomIntoperation2() {
		return oldrandomIntoperation2;
	}

	public int getreplacerandomIntoperation2() {
		return replacerandomIntoperation2;
	}

	public int getsecondoldrandomIntoperation2() {
		return secondoldrandomIntoperation2;
	}

	public int getoldrandomIntoperation1() {
		return oldrandomIntoperation1;
	}

	public int getreplacerandomIntoperation1() {
		return replacerandomIntoperation1;
	}

	public int getsecondoldrandomIntoperation1() {
		return secondoldrandomIntoperation1;
	}

	public int getoldrandomIntoperation5() {
		return oldrandomIntoperation5;
	}

	public int getreplacerandomIntoperation5() {
		return replacerandomIntoperation5;
	}

	public int getsecondoldrandomIntoperation5() {
		return secondoldrandomIntoperation5;
	}

	public int getoldrandomIntoperation6() {
		return oldrandomIntoperation6;
	}

	public int getreplacerandomIntoperation6() {
		return replacerandomIntoperation6;
	}

	public int getsecondoldrandomIntoperation6() {
		return secondoldrandomIntoperation6;
	}

	public int getoldrandomIntoperation7() {
		return oldrandomIntoperation7;
	}

	public int getreplacerandomIntoperation7() {
		return replacerandomIntoperation7;
	}

	public int getsecondoldrandomIntoperation7() {
		return secondoldrandomIntoperation7;
	}

	public int getoldrandomIntoperation8() {
		return oldrandomIntoperation8;
	}

	public int getreplacerandomIntoperation8() {
		return replacerandomIntoperation8;
	}

	public int getsecondoldrandomIntoperation8() {
		return secondoldrandomIntoperation8;
	}

	public int getoldrandomIntoperation9() {
		return oldrandomIntoperation9;
	}

	public int getreplacerandomIntoperation9() {
		return replacerandomIntoperation9;
	}

	public int getsecondoldrandomIntoperation9() {
		return secondoldrandomIntoperation9;
	}

	public int getoldrandomIntoperation10() {
		return oldrandomIntoperation10;
	}

	public int getreplacerandomIntoperation10() {
		return replacerandomIntoperation10;
	}

	public int getsecondoldrandomIntoperation10() {
		return secondoldrandomIntoperation10;
	}

	public int getoldrandomIntoperation11() {
		return oldrandomIntoperation11;
	}

	public int getreplacerandomIntoperation11() {
		return replacerandomIntoperation11;
	}

	public int getsecondoldrandomIntoperation11() {
		return secondoldrandomIntoperation11;
	}

	public int getoldrandomIntoperation12() {
		return oldrandomIntoperation12;
	}

	public int getreplacerandomIntoperation12() {
		return replacerandomIntoperation12;
	}

	public int getsecondoldrandomIntoperation12() {
		return secondoldrandomIntoperation12;
	}

	public int getoldrandomIntoperation13() {
		return oldrandomIntoperation13;
	}

	public int getreplacerandomIntoperation13() {
		return replacerandomIntoperation13;
	}

	public int getsecondoldrandomIntoperation13() {
		return secondoldrandomIntoperation13;
	}

	public int getoldrandomIntoperation14() {
		return oldrandomIntoperation14;
	}

	public int getreplacerandomIntoperation14() {
		return replacerandomIntoperation14;
	}

	public int getsecondoldrandomIntoperation14() {
		return secondoldrandomIntoperation14;
	}

	public int getoldrandomIntoperation15() {
		return oldrandomIntoperation15;
	}

	public int getreplacerandomIntoperation15() {
		return replacerandomIntoperation15;
	}

	public int getsecondoldrandomIntoperation15() {
		return secondoldrandomIntoperation15;
	}

	public int getoldrandomIntoperation16() {
		return oldrandomIntoperation16;
	}

	public int getreplacerandomIntoperation16() {
		return replacerandomIntoperation16;
	}

	public int getsecondoldrandomIntoperation16() {
		return secondoldrandomIntoperation16;
	}

	public int getoldrandomIntoperation17() {
		return oldrandomIntoperation17;
	}

	public int getreplacerandomIntoperation17() {
		return replacerandomIntoperation17;
	}

	public int getsecondoldrandomIntoperation17() {
		return secondoldrandomIntoperation17;
	}

	public int getoldrandomIntoperation18() {
		return oldrandomIntoperation18;
	}

	public int getreplacerandomIntoperation18() {
		return replacerandomIntoperation18;
	}

	public int getsecondoldrandomIntoperation18() {
		return secondoldrandomIntoperation18;
	}

	public int getoldrandomIntoperation19() {
		return oldrandomIntoperation19;
	}

	public int getreplacerandomIntoperation19() {
		return replacerandomIntoperation19;
	}

	public int getsecondoldrandomIntoperation19() {
		return secondoldrandomIntoperation19;
	}

	public int getoldrandomIntoperation20() {
		return oldrandomIntoperation20;
	}

	public int getreplacerandomIntoperation20() {
		return replacerandomIntoperation20;
	}

	public int getsecondoldrandomIntoperation20() {
		return secondoldrandomIntoperation20;
	}

	public int getoldrandomIntoperation21() {
		return oldrandomIntoperation21;
	}

	public int getreplacerandomIntoperation21() {
		return replacerandomIntoperation21;
	}

	public int getsecondoldrandomIntoperation21() {
		return secondoldrandomIntoperation21;
	}

	public int getoldrandomIntoperation22() {
		return oldrandomIntoperation22;
	}

	public int getreplacerandomIntoperation22() {
		return replacerandomIntoperation22;
	}

	public int getsecondoldrandomIntoperation22() {
		return secondoldrandomIntoperation22;
	}

	public void setfirstfitness(double i) {
		this.firstfitness = i;
	}

	public void setfitnessthird(int i) {
		this.fitnessthird = i;
	}

	public void setsecondfitness(double i) {
		this.secondfitness = i;
	}

	public void setoldrandomIntoperation4(int i) {
		oldrandomIntoperation4 = i;
	}

	public void setreplacerandomIntoperation4(int i) {
		replacerandomIntoperation4 = i;
	}

	public void setsecondoldrandomIntoperation4(int i) {
		secondoldrandomIntoperation4 = i;
	}

	public void setoldrandomIntoperation3(int i) {
		oldrandomIntoperation3 = i;
	}

	public void setreplacerandomIntoperation3(int i) {
		replacerandomIntoperation3 = i;
	}

	public void setsecondoldrandomIntoperation3(int i) {
		secondoldrandomIntoperation3 = i;
	}

	public void setoldrandomIntoperation2(int i) {
		oldrandomIntoperation2 = i;
	}

	public void setreplacerandomIntoperation2(int i) {
		replacerandomIntoperation2 = i;
	}

	public void setsecondoldrandomIntoperation2(int i) {
		secondoldrandomIntoperation2 = i;
	}

	public void setoldrandomIntoperation1(int i) {
		oldrandomIntoperation1 = i;
	}

	public void setreplacerandomIntoperation1(int i) {
		replacerandomIntoperation1 = i;
	}

	public void setsecondoldrandomIntoperation1(int i) {
		secondoldrandomIntoperation1 = i;
	}

	public void setoldrandomIntoperation5(int i) {
		oldrandomIntoperation5 = i;
	}

	public void setreplacerandomIntoperation5(int i) {
		replacerandomIntoperation5 = i;
	}

	public void setsecondoldrandomIntoperation5(int i) {
		secondoldrandomIntoperation5 = i;
	}

	public void setoldrandomIntoperation6(int i) {
		oldrandomIntoperation6 = i;
	}

	public void setreplacerandomIntoperation6(int i) {
		replacerandomIntoperation6 = i;
	}

	public void setsecondoldrandomIntoperation6(int i) {
		secondoldrandomIntoperation6 = i;
	}

	public void setoldrandomIntoperation7(int i) {
		oldrandomIntoperation7 = i;
	}

	public void setreplacerandomIntoperation7(int i) {
		replacerandomIntoperation7 = i;
	}

	public void setsecondoldrandomIntoperation7(int i) {
		secondoldrandomIntoperation7 = i;
	}

	public void setoldrandomIntoperation8(int i) {
		oldrandomIntoperation8 = i;
	}

	public void setreplacerandomIntoperation8(int i) {
		replacerandomIntoperation8 = i;
	}

	public void setsecondoldrandomIntoperation8(int i) {
		secondoldrandomIntoperation8 = i;
	}

	public void setoldrandomIntoperation9(int i) {
		oldrandomIntoperation9 = i;
	}

	public void setreplacerandomIntoperation9(int i) {
		replacerandomIntoperation9 = i;
	}

	public void setsecondoldrandomIntoperation9(int i) {
		secondoldrandomIntoperation9 = i;
	}

	public void setoldrandomIntoperation10(int i) {
		oldrandomIntoperation10 = i;
	}

	public void setreplacerandomIntoperation10(int i) {
		replacerandomIntoperation10 = i;
	}

	public void setsecondoldrandomIntoperation10(int i) {
		secondoldrandomIntoperation10 = i;
	}

	public void setoldrandomIntoperation11(int i) {
		oldrandomIntoperation11 = i;
	}

	public void setreplacerandomIntoperation11(int i) {
		replacerandomIntoperation11 = i;
	}

	public void setsecondoldrandomIntoperation11(int i) {
		secondoldrandomIntoperation11 = i;
	}

	public void setoldrandomIntoperation12(int i) {
		oldrandomIntoperation12 = i;
	}

	public void setreplacerandomIntoperation12(int i) {
		replacerandomIntoperation12 = i;
	}

	public void setsecondoldrandomIntoperation12(int i) {
		secondoldrandomIntoperation12 = i;
	}

	public void setoldrandomIntoperation15(int i) {
		oldrandomIntoperation15 = i;
	}

	public void setreplacerandomIntoperation15(int i) {
		replacerandomIntoperation15 = i;
	}

	public void setsecondoldrandomIntoperation15(int i) {
		secondoldrandomIntoperation15 = i;
	}

	public void setoldrandomIntoperation16(int i) {
		oldrandomIntoperation16 = i;
	}

	public void setreplacerandomIntoperation16(int i) {
		replacerandomIntoperation16 = i;
	}

	public void setsecondoldrandomIntoperation16(int i) {
		secondoldrandomIntoperation16 = i;
	}

	public void setoldrandomIntoperation17(int i) {
		oldrandomIntoperation17 = i;
	}

	public void setreplacerandomIntoperation17(int i) {
		replacerandomIntoperation17 = i;
	}

	public void setsecondoldrandomIntoperation17(int i) {
		secondoldrandomIntoperation17 = i;
	}

	public void setoldrandomIntoperation18(int i) {
		oldrandomIntoperation18 = i;
	}

	public void setreplacerandomIntoperation18(int i) {
		replacerandomIntoperation18 = i;
	}

	public void setsecondoldrandomIntoperation18(int i) {
		secondoldrandomIntoperation18 = i;
	}

	public void setoldrandomIntoperation19(int i) {
		oldrandomIntoperation19 = i;
	}

	public void setreplacerandomIntoperation19(int i) {
		replacerandomIntoperation19 = i;
	}

	public void setsecondoldrandomIntoperation19(int i) {
		secondoldrandomIntoperation19 = i;
	}

	public void setoldrandomIntoperation20(int i) {
		oldrandomIntoperation20 = i;
	}

	public void setreplacerandomIntoperation20(int i) {
		replacerandomIntoperation20 = i;
	}

	public void setsecondoldrandomIntoperation20(int i) {
		secondoldrandomIntoperation20 = i;
	}

	public void setoldrandomIntoperation21(int i) {
		oldrandomIntoperation21 = i;
	}

	public void setreplacerandomIntoperation21(int i) {
		replacerandomIntoperation21 = i;
	}

	public void setsecondoldrandomIntoperation21(int i) {
		secondoldrandomIntoperation21 = i;
	}

	public void setoldrandomIntoperation22(int i) {
		oldrandomIntoperation22 = i;
	}

	public void setreplacerandomIntoperation22(int i) {
		replacerandomIntoperation22 = i;
	}

	public void setsecondoldrandomIntoperation22(int i) {
		secondoldrandomIntoperation22 = i;
	}

	public void setoldrandomIntoperation13(int i) {
		oldrandomIntoperation13 = i;
	}

	public void setreplacerandomIntoperation13(int i) {
		replacerandomIntoperation13 = i;
	}

	public void setsecondoldrandomIntoperation13(int i) {
		secondoldrandomIntoperation13 = i;
	}

	public void setoldrandomIntoperation14(int i) {
		oldrandomIntoperation14 = i;
	}

	public void setreplacerandomIntoperation14(int i) {
		replacerandomIntoperation14 = i;
	}

	public void setsecondoldrandomIntoperation14(int i) {
		secondoldrandomIntoperation14 = i;
	}

	public void setoperation(int index, int i) {
		operations_.set(index, i);
	}

	public ArrayList<Integer> getlistmutation() {
		return listmutation;
	}

	public void setlistmutation(int index, int element) {
		listmutation.set(index, element);
	}

	public ArrayList<Integer> getoperations() {
		// TODO Auto-generated method stub
		return operations_;
	}

	public int getnumbererror() {
		return numbererror;
	}

	public int getindex() {
		return index;
	}

	public CoSolution getCoSolution() {
		// TODO Auto-generated method stub
		return S_;
	}

	public CoSolutionpostprocessing getCoSolutionpostprocessing() {
		// TODO Auto-generated method stub
		return Sc_;
	}

	public void setindex(int i) {
		index = i;
	}

	public void setnumbererror(int i) {
		numbererror = i;
	}

	public ArrayList<String> getcomments2() {
		return comments2;

	}

	public void setcomments2(ArrayList<String> s) {
		comments2 = s;
	}

	public void setcomments(EDataTypeEList<String> comment) {
		comments = comment;
	}

	public void setCoSolution(CoSolution s_) {
		S_ = s_;
	}

	public void setCoSolutionpostprocessing(CoSolutionpostprocessing s_) {
		Sc_ = s_;
	}

	/**
	 * Sets the distance between this solution and a <code>SolutionSet</code>. The
	 * value is stored in <code>distanceToSolutionSet_</code>.
	 * 
	 * @param distance
	 *            The distance to a solutionSet.
	 */
	public void setDistanceToSolutionSet(double distance) {
		distanceToSolutionSet_ = distance;
	} // SetDistanceToSolutionSet

	/**
	 * Gets the distance from the solution to a <code>SolutionSet</code>. <b>
	 * REQUIRE </b>: this method has to be invoked after calling
	 * <code>setDistanceToPopulation</code>.
	 * 
	 * @return the distance to a specific solutionSet.
	 */
	public double getDistanceToSolutionSet() {
		return distanceToSolutionSet_;
	} // getDistanceToSolutionSet

	/**
	 * Sets the distance between the solution and its k-nearest neighbor in a
	 * <code>SolutionSet</code>. The value is stored in <code>kDistance_</code>.
	 * 
	 * @param distance
	 *            The distance to the k-nearest neighbor.
	 */
	public void setKDistance(double distance) {
		kDistance_ = distance;
	} // setKDistance

	/**
	 * Gets the distance from the solution to his k-nearest nighbor in a
	 * <code>SolutionSet</code>. Returns the value stored in
	 * <code>kDistance_</code>. <b> REQUIRE </b>: this method has to be invoked
	 * after calling <code>setKDistance</code>.
	 * 
	 * @return the distance to k-nearest neighbor.
	 */
	double getKDistance() {
		return kDistance_;
	} // getKDistance

	/**
	 * Sets the crowding distance of a solution in a <code>SolutionSet</code>. The
	 * value is stored in <code>crowdingDistance_</code>.
	 * 
	 * @param distance
	 *            The crowding distance of the solution.
	 */
	public void setCrowdingDistance(double distance) {
		crowdingDistance_ = distance;
	} // setCrowdingDistance

	/**
	 * Gets the crowding distance of the solution into a <code>SolutionSet</code>.
	 * Returns the value stored in <code>crowdingDistance_</code>. <b> REQUIRE </b>:
	 * this method has to be invoked after calling <code>setCrowdingDistance</code>.
	 * 
	 * @return the distance crowding distance of the solution.
	 */
	public double getCrowdingDistance() {
		// System.out.println("crowding distance : "+crowdingDistance_);
		return crowdingDistance_;
	} // getCrowdingDistance

	/**
	 * Sets the fitness of a solution. The value is stored in <code>fitness_</code>.
	 * 
	 * @param fitness
	 *            The fitness of the solution.
	 */
	public void setFitness(double fitness) {
		fitness_ = fitness;
	} // setFitness

	/**
	 * Gets the fitness of the solution. Returns the value of stored in the
	 * encodings.variable <code>fitness_</code>. <b> REQUIRE </b>: This method has
	 * to be invoked after calling <code>setFitness()</code>.
	 * 
	 * @return the fitness.
	 */
	public double getFitness() {
		return fitness_;
	} // getFitness

	/**
	 * Sets the value of the i-th objective.
	 * 
	 * @param i
	 *            The number identifying the objective.
	 * @param value
	 *            The value to be stored.
	 */
	public void setObjective(int i, double value) {
		objective_[i] = value;
	} // setObjective

	/**
	 * Returns the value of the i-th objective.
	 * 
	 * @param i
	 *            The value of the objective.
	 */
	public double getObjective(int i) {
		return objective_[i];
	} // getObjective

	/**
	 * Returns the number of objectives.
	 * 
	 * @return The number of objectives.
	 */
	public int getNumberOfObjectives() {
		if (objective_ == null)
			return 0;
		else
			return numberOfObjectives_;
	} // getNumberOfObjectives

	/**
	 * Returns the number of decision variables of the solution.
	 * 
	 * @return The number of decision variables.
	 */
	public int numberOfVariables() {
		return problem_.getNumberOfVariables();
	} // numberOfVariables

	public ArrayList<Integer> operation() {

		/*
		 * ArrayList<Rule> R = new ArrayList<>(); MySolution S = new MySolution();
		 * for(int i=0; i<MySolution.rules.size();i++) { R.add(MySolution.rules.get(i));
		 * }
		 */
		/*
		 * for(int i=0; i<IntSolutionType.rules.size();i++) {
		 * System.out.println("!!!!!!!!!!!!!"+IntSolutionType.rules.get(i).rule_text);}
		 */
		return operations_;
	}

	public EDataTypeEList<String> comments() {

		return comments;
	}

	/**
	 * Returns a string representing the solution.
	 * 
	 * @return The string.
	 */
	public String toString() {
		String aux = "";
		for (int i = 0; i < this.numberOfObjectives_; i++)
			aux = aux + this.getObjective(i) + " ";

		return aux;
	} // toString

	/**
	 * Returns the decision variables of the solution.
	 * 
	 * @return the <code>DecisionVariables</code> object representing the decision
	 *         variables of the solution.
	 */
	public Variable[] getDecisionVariables() {
		return variable_;
	} // getDecisionVariables

	/**
	 * Sets the decision variables for the solution.
	 * 
	 * @param variables
	 *            The <code>DecisionVariables</code> object representing the
	 *            decision variables of the solution.
	 */
	public void setDecisionVariables(Variable[] variables) {
		variable_ = variables;
	} // setDecisionVariables

	public Problem getProblem() {
		return problem_;
	}

	/**
	 * Indicates if the solution is marked.
	 * 
	 * @return true if the method <code>marked</code> has been called and, after
	 *         that, the method <code>unmarked</code> hasn't been called. False in
	 *         other case.
	 */
	public boolean isMarked() {
		return this.marked_;
	} // isMarked

	/**
	 * Establishes the solution as marked.
	 */
	public void marked() {
		this.marked_ = true;
	} // marked

	/**
	 * Established the solution as unmarked.
	 */
	public void unMarked() {
		this.marked_ = false;
	} // unMarked

	/**
	 * Sets the rank of a solution.
	 * 
	 * @param value
	 *            The rank of the solution.
	 */
	public void setRank(int value) {
		this.rank_ = value;
	} // setRank

	/**
	 * Gets the rank of the solution. <b> REQUIRE </b>: This method has to be
	 * invoked after calling <code>setRank()</code>.
	 * 
	 * @return the rank of the solution.
	 */
	public int getRank() {
		return this.rank_;
	} // getRank

	/**
	 * Sets the overall constraints violated by the solution.
	 * 
	 * @param value
	 *            The overall constraints violated by the solution.
	 */
	public void setOverallConstraintViolation(double value) {
		this.overallConstraintViolation_ = value;
	} // setOverallConstraintViolation

	/**
	 * Gets the overall constraint violated by the solution. <b> REQUIRE </b>: This
	 * method has to be invoked after calling
	 * <code>overallConstraintViolation</code>.
	 * 
	 * @return the overall constraint violation by the solution.
	 */
	public double getOverallConstraintViolation() {
		return this.overallConstraintViolation_;
	} // getOverallConstraintViolation

	/**
	 * Sets the number of constraints violated by the solution.
	 * 
	 * @param value
	 *            The number of constraints violated by the solution.
	 */
	public void setNumberOfViolatedConstraint(int value) {
		this.numberOfViolatedConstraints_ = value;
	} // setNumberOfViolatedConstraint

	/**
	 * Gets the number of constraint violated by the solution. <b> REQUIRE </b>:
	 * This method has to be invoked after calling
	 * <code>setNumberOfViolatedConstraint</code>.
	 * 
	 * @return the number of constraints violated by the solution.
	 */
	public int getNumberOfViolatedConstraint() {
		return this.numberOfViolatedConstraints_;
	} // getNumberOfViolatedConstraint

	/**
	 * Sets the location of the solution into a solutionSet.
	 * 
	 * @param location
	 *            The location of the solution.
	 */
	public void setLocation(int location) {
		this.location_ = location;
	} // setLocation

	/**
	 * Gets the location of this solution in a <code>SolutionSet</code>. <b> REQUIRE
	 * </b>: This method has to be invoked after calling <code>setLocation</code>.
	 * 
	 * @return the location of the solution into a solutionSet
	 */
	public int getLocation() {
		return this.location_;
	} // getLocation

	/**
	 * Sets the type of the encodings.variable.
	 * 
	 * @param type
	 *            The type of the encodings.variable.
	 */
	// public void setType(String type) {
	// type_ = Class.forName("") ;
	// } // setType

	/**
	 * Sets the type of the encodings.variable.
	 * 
	 * @param type
	 *            The type of the encodings.variable.
	 */
	public void setType(SolutionType type) {
		type_ = type;
	} // setType

	/**
	 * Gets the type of the encodings.variable
	 * 
	 * @return the type of the encodings.variable
	 */
	public SolutionType getType() {
		return type_;
	} // getType

	/**
	 * Returns the aggregative value of the solution
	 * 
	 * @return The aggregative value.
	 */
	public double getAggregativeValue() {
		double value = 0.0;
		for (int i = 0; i < getNumberOfObjectives(); i++) {
			value += getObjective(i);
		}
		return value;
	} // getAggregativeValue

	/**
	 * Returns the number of bits of the chromosome in case of using a binary
	 * representation
	 * 
	 * @return The number of bits if the case of binary variables, 0 otherwise This
	 *         method had a bug which was fixed by Rafael Olaechea
	 */
	public int getNumberOfBits() {
		int bits = 0;

		for (int i = 0; i < variable_.length; i++)
			if ((variable_[i].getVariableType() == jmetal.encodings.variable.Binary.class)
					|| (variable_[i].getVariableType() == jmetal.encodings.variable.BinaryReal.class))

				bits += ((Binary) (variable_[i])).getNumberOfBits();

		return bits;
	} // getNumberOfBits
} // Solution
