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
import anatlyzer.atlext.OCL.OclExpression;
import anatlyzer.evaluation.models.ModelGenerationStrategy;
//import anatlyzer.evaluation.report.Report;
//import anatlyzer.evaluation.tester.Tester;
import ca.udem.fixingatlerror.explorationphase.BaseTest;
import ca.udem.fixingatlerror.explorationphase.main;
import ca.udem.fixingatlerror.explorationphase.CoSolution;
import ca.udem.fixingatlerror.explorationphase.Operations;
import ca.udem.fixingatlerror.explorationphase.Setting;
import jmetal.core.*;
import jmetal.encodings.solutionType.IntSolutionType;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Distance;
import jmetal.util.JMException;
import jmetal.util.Ranking;
import jmetal.util.comparators.CrowdingComparator;
import transML.exceptions.transException;
import witness.generator.MetaModel;

/** 
 *  Implementation of NSGA-II.
 *  This implementation of NSGA-II makes use of a QualityIndicator object
 *  to obtained the convergence speed of the algorithm. This version is used
 *  in the paper:
 *     A.J. Nebro, J.J. Durillo, C.A. Coello Coello, F. Luna, E. Alba 
 *     "A Study of Convergence Speed in Multi-Objective Metaheuristics." 
 *     To be presented in: PPSN'08. Dortmund. September 2008.
 */

public class NSGAII extends Algorithm {
  /**
   * Constructor
   * @param problem Problem to solve
   */
  public NSGAII(Problem problem) {
    super (problem) ;
  } // NSGAIIZA
  public void setString(String str){
      this.statemutcrossinitial = str;
  }
  public static ArrayList<Integer> sequencelocation = new ArrayList<Integer>();
  public static ArrayList<Integer> ocliskineoflocation = new ArrayList<Integer>();
  public static ArrayList<Integer> filterlocation = new ArrayList<Integer>();	
  public static ArrayList<Integer> operationcalllocation = new ArrayList<Integer>();
  public static ArrayList<String> listnavigationtype = new ArrayList<String>();	
  public static ArrayList<Integer> locationfilter = new ArrayList<Integer>();	
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
  public static List<Integer> iterationrule = new ArrayList<Integer>();
  public static ArrayList<String> classnamestring = new ArrayList<String>();
  public static ArrayList<Integer> classnamestartpoint = new ArrayList<Integer>();
  public static boolean postprocessing=false;
  public static ArrayList<Integer> classnamelength = new ArrayList<Integer>();
  public static List<EStructuralFeature> listsourcemetamodel = new ArrayList<EStructuralFeature>();
  public static List<EStructuralFeature> listinheritattributesourcemetamodel = new ArrayList<EStructuralFeature>();
  public static List<Integer> forbiddenoperations;
 // public static List<EClassifier> listclassinsourcemm = new ArrayList<EClassifier>();
  private String folderMutants;
  public static int counter=0;
  public static int numberinitialerror;
  public static boolean checkcollection=false;
  public static boolean checkfilter=false;
  public static boolean checksequence=false;
  public static boolean checkoperationcall=false;
  public static boolean checkiteration=false;
  
  private EMFModel atlModel;
  public static PrintWriter writer;
  public static int fitness2=0;
  public static int fitness3=0;
  public static int indexmodeltransformation=1;
  public static List<Integer> faultrule = new ArrayList<Integer>();
  public static List<Integer> finalrule = new ArrayList<Integer>();
  public static List<Integer> faultlocation = new ArrayList<Integer>();
  public static List<Integer> errorrule = new ArrayList<Integer>();
  public static List<Integer> listoutpatternmodify = new ArrayList<Integer>();
  public static String statemutcrossinitial="notmutation";
  public static int numop=0;
  public static int counterdelet=0;
  public static int countfilter=0;
  public static boolean startsituation=false;
  public static int fixedgeneration=-1;
  public static ArrayList<Integer> listfilterapplied = new ArrayList<Integer>();
  public static List<String> argumentlist = new ArrayList<String>();
  public static List<Integer> deletlist = new ArrayList<Integer>();
  public static List<EClassifier> classifierliast = new ArrayList<EClassifier >();
  public static List<List<String>> originalmetamodellist = new ArrayList<List<String>>();
  public FileWriter csvWriterpareto;
  /**   
   * Runs the NSGA-II algorithm.
   * @return a <code>SolutionSet</code> that is a set of non dominated solutions
   * as a result of the algorithm execution
   * @throws JMException 
   */
  public SolutionSet execute() throws JMException, ClassNotFoundException {
    int populationSize;
    int maxEvaluations;
    int evaluations;
    Setting s=new  Setting();
    File dir=new File(s.getpath()+"/resultformodeltransformation"+NSGAII.indexmodeltransformation);
    if (!dir.exists())
    dir.mkdir();
    QualityIndicator indicators; // QualityIndicator object
    int requiredEvaluations; // Use in the example of use of the
    // indicators object (see below)
    try {
		writer = new PrintWriter(dir.getAbsolutePath()+"/file-operatin.txt", "UTF-8");
		try {
			this.csvWriterpareto= new FileWriter(dir.getAbsolutePath()+"/paretosize.csv");
			this.csvWriterpareto.append("generation;");
			this.csvWriterpareto.append("avg-f1;");
			this.csvWriterpareto.append("avg-f2;");
			this.csvWriterpareto.append("avg-f3;");
			this.csvWriterpareto.append("sizepareto;");
			this.csvWriterpareto.append("time(ms);");
			this.csvWriterpareto.append("numberiteration");
			this.csvWriterpareto.append("firstgenerationfixederror"+"\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    SolutionSet population;
    SolutionSet offspringPopulation;
    SolutionSet union;

    Operator mutationOperator;
    Operator crossoverOperator;
    Operator selectionOperator;

    Distance distance = new Distance();

    //Read the parameters
    populationSize = ((Integer) getInputParameter("populationSize")).intValue();
    maxEvaluations = ((Integer) getInputParameter("maxEvaluations")).intValue();
    indicators = (QualityIndicator) getInputParameter("indicators");

    //Initialize the variables
    population = new SolutionSet(populationSize);
    evaluations = 0;

    requiredEvaluations = 0;

    //Read the operators
    mutationOperator = operators_.get("mutation");
    crossoverOperator = operators_.get("crossover");
    selectionOperator = operators_.get("selection");
    
    // Create the initial solutionSet
   
    Solution newSolution;
    CoSolution cocou=new CoSolution();
    FindFaultyRule();
    deleteDirectory (s.getpath()+"/resultformodeltransformation"+NSGAII.indexmodeltransformation+"/solution", false);
    CoSolution.check=0;
  //  cocou.w.getSMClick( "examples/class2rel/trafo/Class2TableExperience1.atl",null);
    
//	List<String> firstfoot=cocou.footprintscalcul(""); 
    boolean checkiteration=false;
     //  for (int i = 0; i < populationSize; i++) {
    for (int i = 0; i < s.getpopulationsize(); i++) {// 60 30 //200
    	main.postprocessing=false;
      newSolution = new Solution(problem_);
  
  //    writer.println(newSolution.getoperations());
 //     writer.println("zzzahraaaaaaaa");
      NSGAII.counter=NSGAII.counter+1;
      problem_.evaluate(newSolution,this.csvWriterpareto);
     
      problem_.evaluateConstraints(newSolution);
      evaluations++;
      population.add(newSolution);
    
      
    } //for 
    main.postprocessing=false;
     maxEvaluations=s.getmaxevaluation();// 8000 30000 for 20 error //1000
    // Generations 
     //   while (checkiteration==false) {
    while (evaluations < maxEvaluations) {
	  System.out.println("startNSGAII");
      // Create the offSpring solutionSet      
      offspringPopulation = new SolutionSet(populationSize);
      Solution[] parents = new Solution[2];
      for (int i = 0; i < (populationSize / 2); i++) {
    	      if (evaluations < maxEvaluations) {
          //obtain parents
          parents[0] = (Solution) selectionOperator.execute(population);
          parents[1] = (Solution) selectionOperator.execute(population);
          System.out.println( parents[0].getoperations());
          writer.println("choosefrompopulation-firstparent");
          writer.println(parents[0].getoperations());
          writer.println(parents[0].getindex());
      //    System.out.println("indexcrossover");
    //      System.out.println( parents[0].getindex());
    //     System.out.println( parents[1].getindex());
          writer.println("choosefrompopulation-secondparent");
          writer.println(parents[1].getoperations());
          writer.println(parents[1].getindex());
      ///    System.out.println(parents[0].inorout);
     //     System.out.println(parents[1].inorout);
          
          
          Solution[] offSpring = (Solution[]) crossoverOperator.execute(parents);
//          System.out.println("offspring");
          writer.println("aftercrossover");
         // System.out.println("aftercrossover");
      //   System.out.println( offSpring[0].getoperations());
      //   System.out.println( offSpring[1].getoperations());
      //   System.out.println( offSpring[0].inorout);
     //    System.out.println( offSpring[1].inorout);
         
          writer.println(offSpring[0].getoperations());
          writer.println(offSpring[1].getoperations());
         
          mutationOperator.execute(offSpring[0]);
      //   System.out.println("mutationafter");
          writer.println("aftermutation");
     //     System.out.println( offSpring[0].getoperations());
          writer.println(offSpring[0].getoperations());
          mutationOperator.execute(offSpring[1]);
     /*     System.out.println( offSpring[1].getoperations());
          System.out.println( offSpring[0].inorout);
          System.out.println( offSpring[1].inorout);
          System.out.println( offSpring[0].classifiersolution);
          System.out.println( offSpring[1].classifiersolution);*/
          NSGAII.counter=NSGAII.counter+1;
          writer.println(offSpring[1].getoperations());
      //    writer.println(offSpring[0].inorout);
     //     writer.println(offSpring[1].inorout);
           NSGAII.statemutcrossinitial="mutation";
          main.checkoffspring=1;
          
          problem_.evaluate(offSpring[0],this.csvWriterpareto);
          problem_.evaluateConstraints(offSpring[0]);
          writer.println("offSpring[0].getoperations()");
          writer.println(offSpring[0].getindex());
          NSGAII.counter=NSGAII.counter+1;
          main.checkoffspring=1;
          
          problem_.evaluate(offSpring[1],this.csvWriterpareto);
          problem_.evaluateConstraints(offSpring[1]);
          writer.println("offSpring[1].getoperations()");
          writer.println(offSpring[1].getindex());
//bod          System.out.println("evaluation");
//bod          System.out.println(evaluations);
          offspringPopulation.add(offSpring[0]);
          offspringPopulation.add(offSpring[1]);
          
          evaluations += 2;
             } // if                            
      } // for
     // System.out.println("offspringsizeeeee");
    //  System.out.println(offspringPopulation.size());
      // Create the solutionSet union of solutionSet and offSpring
      union = ((SolutionSet) population).union(offspringPopulation);

      // Ranking the union
      Ranking ranking = new Ranking(union,this.csvWriterpareto);

      int remain = populationSize;
      int index = 0;
      SolutionSet front = null;
      population.clear();

      // Obtain the next front
      front = ranking.getSubfront(index);
      writer.println("ranking");
      writer.println(front.size());
      for (int k = 0; k < front.size(); k++) {
    	  writer.println(front.get(k));  
    	  writer.println(front.get(k).getindex());  
    	  writer.println(front.get(k).getnumbererror()); 
    	  if(front.get(k).getnumbererror()==0)
    		  checkiteration=true;
      }

      while ((remain > 0) && (remain >= front.size())) {
        //Assign crowding distance to individuals
        distance.crowdingDistanceAssignment(front, problem_.getNumberOfObjectives());
        //Add the individuals of this front
        for (int k = 0; k < front.size(); k++) {
          population.add(front.get(k));
        } // for

        //Decrement remain
        remain = remain - front.size();

        //Obtain the next front
        index++;
        if (remain > 0) {
          front = ranking.getSubfront(index);
        } // if        
      } // while

      // Remain is less than front(index).size, insert only the best one
      if (remain > 0) {  // front contains individuals to insert                        
        distance.crowdingDistanceAssignment(front, problem_.getNumberOfObjectives());
        front.sort(new CrowdingComparator());
        for (int k = 0; k < remain; k++) {
          population.add(front.get(k));
        } // for

        remain = 0;
      } // if                               

      // This piece of code shows how to use the indicator object into the code
      // of NSGA-II. In particular, it finds the number of evaluations required
      // by the algorithm to obtain a Pareto front with a hypervolume higher
      // than the hypervolume of the true Pareto front.
      if ((indicators != null) &&
          (requiredEvaluations == 0)) {
        double HV = indicators.getHypervolume(population);
        if (HV >= (0.98 * indicators.getTrueParetoFrontHypervolume())) {
          requiredEvaluations = evaluations;
        } // if
      } // if
//      System.out.println("after crossover");
      writer.println("choosed next generation");
      
     
      for(int kk=0;kk<population.size();kk++) {
//      System.out.println(population.get(kk).getindex());
//      System.out.println(population.get(kk).getnumbererror());
//      System.out.println(population.get(kk).getoperations());
      writer.println(population.get(kk).getindex());
     writer.println(population.get(kk).getnumbererror());
      writer.println(population.get(kk).getoperations());
     
      }
    } // while
    
   
    // Return as output parameter the required evaluations
    setOutputParameter("evaluations", requiredEvaluations);

    // Return the first non-dominated front
    Ranking ranking = new Ranking(population,this.csvWriterpareto);
    ranking.getSubfront(0).printFeasibleFUN("FUN_NSGAII") ;
    writer.println("subfront");
    writer.println(ranking.getSubfront(0).size());
    writer.close();
    try {
		this.csvWriterpareto.flush();
		 this.csvWriterpareto.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   
   // writer2.close();
    return ranking.getSubfront(0);
  } // execute
  
  
  private ArrayList<Integer> buildForbiddenOperation() {
		ArrayList<Integer> forbiddenoperations = new ArrayList<Integer>();
		
		if (!NSGAII.checkcollection )
			forbiddenoperations.add(7);
		
		if (!NSGAII.checkfilter )
			forbiddenoperations.add(5);
		
		if (!NSGAII.checksequence )
			forbiddenoperations.add(4);
		
		if (!NSGAII.checkiteration )
			forbiddenoperations.add(10);
		
		if (!NSGAII.checkoperationcall )
			forbiddenoperations.add(2);
		
		return forbiddenoperations;
		
	}
  
  private void FindFaultyRule() {
	// TODO Auto-generated method stub
	  BaseTest op=new BaseTest();
	  Setting s = new Setting();
	  
		if(NSGAII.startsituation==false)
		{
		String stringSearch = "rule";
		String stringSearch14 = "lazy";
		String stringSearch2 = "}";
		String stringSearch3 = "Sequence(";
		String stringSearch9 = "Sequence {}";
		String stringSearch8 = "asSequence";
		String stringSearch4 = ".oclIsKindOf";
		String stringSearch5 = "from";
		String stringSearch6 = "to";
		
		
		String stringSearch7 = "using";
		String stringSearch10 = "->";
		String stringSearch11 = "thisModule";
		String stringSearch12 = "not";
		String stringSearch13 = "'";
		String stringSearch15 = "!";
		
		boolean preconditionforto=false;
		ArrayList<Integer> listassequence = new ArrayList<Integer>();
		ArrayList<Integer> StartBindingRule = new ArrayList<Integer>();
		ArrayList<String> listcollectionoperation = new ArrayList<String>();
		ArrayList<Integer> listlazy = new ArrayList<Integer>();
		ArrayList<Integer> inpattern = new ArrayList<Integer>();
		List<String> iterationlist    = Arrays.asList ( new String[]{"select", "collect", "reject", "forAll", "isUnique", "exists"} );
		
		List<String> temporarylist    = Arrays.asList ( new String[]{"isEmpty", "notEmpty", "includes", "excludes", "includesAll", "excludesAll","size", "sum", "count",
				"indexOf","union", "intersection","first", "last", "asBag", "asSequence", "asSet", "flatten", "append", "prepend", "including", "excluding"} );
		List<String> collectionempty    = Arrays.asList ( new String[]{"isEmpty()", "notEmpty()", "includes()", "excludes()", "includesAll()", "excludesAll()","size()", "sum()", "count()",
				"indexOf()","union()", "intersection()","first()", "last()", "asBag()", "asSequence()", "asSet()", "flatten()", "append()", "prepend()", "including()", "excluding()"} );
		
		List<String> operationcall    = Arrays.asList ( new String[]{"oclIsKindOf", "oclIsTypeOf", "startsWith", "endsWith", "concat", "trim", "max","min", "exp", "log", "floor","size", "toInteger"
				 , "toReal", "indexOf","lastIndexOf", "abs"} );
		List<String> operationempty    = Arrays.asList ( new String[]{"oclIsKindOf()", "oclIsUndefined()","oclIsTypeOf()", "startsWith()", "endsWith()", "concat()", "trim()", "max()","min()", "exp()", "log()", "floor()","size()", "toInteger()"
				 , "toReal()", "indexOf()","lastIndexOf()", "abs()"} );

		// for petrinet
//        BufferedReader bf = new BufferedReader(new FileReader("C:/Users/varaminz/Downloads/Project/workspaceTEST34/anatlyzer.atl.tests.api/trafo/PNML2PetriNetExperience1.atl"));
        BufferedReader bf = null;
		try {
			bf = new BufferedReader(new FileReader(s.getinputfaultytransformation()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        int linecount = 0;
        int lastline=0;
        String line;
        int indexfrom=-1;
        boolean precondition=false;
        boolean fleshcondition=false;
        boolean precondition2=false;
        try {
			while (( line = bf.readLine()) != null)
			{
				
			    linecount++;
			    int indexfound = line.indexOf(stringSearch);
			    int indexfound2 = line.indexOf(stringSearch2);
			    int indexfound3 = line.indexOf(stringSearch3);
				int indexfound4 = line.indexOf(stringSearch4);
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
				
				if (indexfound10 > -1) {
					fleshcondition=true;
					

				}
				for(int i=0;i<temporarylist.size();i++) {
					if(line.indexOf(temporarylist.get(i))>-1 &&  fleshcondition==true) 
					{
						NSGAII.filterlocation.add( linecount);
							
					}
					
				}
				fleshcondition=false;
				for(int i=0;i<operationcall.size();i++) {
					if(line.indexOf(operationcall.get(i))>-1 ) 
					{
						NSGAII.operationcalllocation.add( linecount);
							
					}
					
				}
				for(int i=0;i<collectionempty.size();i++) {
					if(line.indexOf(collectionempty.get(i))>-1 ) 
					{
						NSGAII.emptycollectionlocation.add( linecount);
							
					}
					
				}
				for(int i=0;i<operationempty.size();i++) {
					if(line.indexOf(operationempty.get(i))>-1 ) 
					{
						NSGAII.emptyopperationlocation.add( linecount);
							
					}
					
				}
				
				for(int i=0;i<iterationlist.size();i++) {
					if(line.indexOf(iterationlist.get(i))>-1 ) 
					{
						
						NSGAII.iterationcall.add( linecount);
						
							
					}
					
				}
				if(colon>-1) {
					
					 indexfrom=linecount;
					 precondition2=true;
					 if(linecount>5)
					 NSGAII.locationfrom.add(linecount);
					 
				}
				if(indexfound15>-1 && precondition2==true && linecount-indexfrom==1 ) {
					String[] parts = line.split("!");
					
					NSGAII.inpatternstringlocation.add(parts[1]);
					inpattern.add( linecount);
				
					
				}
				if(indexfound7>-1) {
					precondition=true;
					
				
				}
				
				if(indexfound5 >-1) {
					precondition2=false;
					 if(precondition==false && (line.length()==2 || line.length()==3)) {
						 NSGAII.locationfilter.add( linecount);
						
					 }
					 if(linecount-indexfrom>2 && precondition==false && line.length()==2) {
					//	 NSGAII.locationfilter.add( indexfrom+1);
						
					 }
					 
					 if(linecount-indexfrom>2 && precondition==true && line.length()==2)
						 NSGAII.preconditionlocation.add( linecount-2);
					
						 
				}
			    if (indexfound > -1) 
			    {
			    	precondition=false;
 //bod               System.out.println("Word is at position " + indexfound + " on line " + linecount);
			        NSGAII.faultrule.add(linecount);
			        
			    }
			    if (indexfound2 > -1) 
			    {
			        lastline=linecount;
			        
			    }
			    if (indexfound3 > -1) {
					NSGAII.sequencelocation.add(linecount);

				}
			    if (indexfound8 > -1) {
					listassequence.add(linecount);

				}
			    if (indexfound9 > -1) {
					listassequence.add(linecount);

				}
				if (indexfound4 > -1) {
					NSGAII.ocliskineoflocation.add(linecount);
					

				}
				if (indexfound11 > -1) {
					NSGAII.parameterlocation.add(linecount);
					

				}
				if (indexfound12 > -1) {
					NSGAII.listnot.add(linecount);
					

				}
				if (indexfound13 > -1) {
					NSGAII.qumecall.add(linecount);
					

				}
				if (indexfound14 > -1) {
					listlazy.add(linecount);
					

				}
				
				
			
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        for(int i=1;i<NSGAII.faultrule.size();i++)
        NSGAII.finalrule.add(NSGAII.faultrule.get(i)-2);
        NSGAII.finalrule.add(lastline);
        try {
			op.typing(s.getinputfaultytransformation(), new Object[] {s.getsourcemetamodel(),s.gettargetmetamodel()}, 
					   new String[] { s.getsecondecorename(), s.getfirstecorename() }, true,null,null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   Analyser analyser = op.getAnalyser();
		ATLModel model=analyser.getATLModel();
		System.out.println("tedad");
		System.out.println(NSGAII.iterationcall);
		System.out.println(NSGAII.ocliskineoflocation);
		System.out.println(NSGAII.operationcalllocation);
		System.out.println(NSGAII.locationfrom);
		System.out.println( NSGAII.locationfilter);
		System.out.println(NSGAII.finalrule);
		System.out.println(NSGAII.preconditionlocation);
		System.out.println(NSGAII.iterationcall);
		System.out.println(NSGAII.qumecall);
		System.out.println(NSGAII.iterationcall);
		System.out.println(NSGAII.preconditionlocation);
		
		System.out.println(NSGAII.locationfilter);
		System.out.println(NSGAII.parameterlocation);
		System.out.println(model.getErrors().getNbErrors());
		NSGAII.numberinitialerror=model.getErrors().getNbErrors();
		if(model.getErrors().getNbErrors() <=5)
			
		IntSolutionType.max_operations_size=(model.getErrors().getNbErrors()+1);
		if(model.getErrors().getNbErrors()==6)
			IntSolutionType.max_operations_size=7;
		if(model.getErrors().getNbErrors()>6)
		   IntSolutionType.max_operations_size=(8);
			
		System.out.println("size");
		 System.out.println(IntSolutionType.max_operations_size);
        try {
			bf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        for(int i=0;i<model.getErrors().getfaultlocation().size();i++)
		 {
			 String[] array = model.getErrors().getfaultlocation().get(i).split(":",2);
		
			
		           NSGAII.faultlocation.add(Integer.parseInt(array[0]));
		           
		 }
        Collections.sort(NSGAII.faultlocation);
        
		 for(int i=0;i<NSGAII.faultlocation.size();i++)
			 for(int j=0;j<NSGAII.faultrule.size();j++)
			 {
				 if(NSGAII.faultlocation.get(i)>=NSGAII.faultrule.get(j) && NSGAII.faultlocation.get(i)<=NSGAII.finalrule.get(j))
				 NSGAII.errorrule.add(j);
			 }
		 
		 createclassifier();
		 for(int i=0;i<NSGAII.inpatternstringlocation.size();i++ )
			 
			 if( (NSGAII.locationfilter.get(i)-NSGAII.locationfrom.get(i))>2)
					NSGAII.inpatternhasfilter.add(1);
			else
				NSGAII.inpatternhasfilter.add(0);
		 
		 NSGAII.startsituation=true;
		 NSGAII.writer.println(NSGAII.finalrule);
		 NSGAII.writer.println(NSGAII.faultrule);
		 NSGAII.writer.println(NSGAII.faultlocation);
		 NSGAII.writer.println(NSGAII.errorrule);
		 System.out.println(NSGAII.inpatternhasfilter);
		 System.out.println(NSGAII.faultrule);
		 
		 System.out.println(NSGAII.filterlocation);
		 System.out.println(NSGAII.operationcalllocation);
		
		 List<Integer> collectionrule = new ArrayList<Integer>();
		 List<Integer> collectionlocation = new ArrayList<Integer>();
		 List<Integer> filterrule = new ArrayList<Integer>();
		 List<Integer> sequencerule = new ArrayList<Integer>();
		 List<Integer> operationcallrule = new ArrayList<Integer>();
		
		 
		 for (int i = 0; i < NSGAII.operationcalllocation.size(); i++)
				for (int j = 0; j < NSGAII.faultrule.size(); j++) {
					if (NSGAII.operationcalllocation.get(i) > NSGAII.faultrule.get(j)
							&& NSGAII.operationcalllocation.get(i) < NSGAII.finalrule.get(j))
						operationcallrule.add(j);

				}
		/* for (int i = 0; i < NSGAII.iterationcall.size(); i++)
				for (int j = 0; j < NSGAII.faultrule.size(); j++) {
					if (NSGAII.iterationcall.get(i) > NSGAII.faultrule.get(j)
							&& NSGAII.iterationcall.get(i) < NSGAII.finalrule.get(j))
						iterationrule.add(j);

				}*/
		 //iteration ha ke to binding bashe to filter nabashe
		 
		 for (int i = 0; i < NSGAII.iterationcall.size(); i++)
				for (int j = 0; j < NSGAII.faultrule.size(); j++) {
					
					if (NSGAII.iterationcall.get(i) > NSGAII.locationfilter.get(j)
							&& NSGAII.iterationcall.get(i) < NSGAII.finalrule.get(j))
						NSGAII.iterationrule.add(j);

				}

		 for (int i = 0; i < listlazy.size(); i++)
				for (int j = 0; j < NSGAII.faultrule.size(); j++) {
					if (listlazy.get(i) >= NSGAII.faultrule.get(j)
							&& listlazy.get(i) <= NSGAII.finalrule.get(j))
						NSGAII.lazyrulelocation.add(j);

				}
		 for (int i = 0; i < NSGAII.filterlocation.size(); i++)
				for (int j = 0; j < NSGAII.faultrule.size(); j++) {
					if (NSGAII.filterlocation.get(i) > NSGAII.faultrule.get(j)
							&& NSGAII.filterlocation.get(i) < NSGAII.finalrule.get(j))
						filterrule.add(j);

				}
		 for (int i = 0; i < NSGAII.ocliskineoflocation.size(); i++) {
			 
				for (int j = 0; j < NSGAII.faultrule.size(); j++) {
					if (NSGAII.ocliskineoflocation.get(i) > NSGAII.faultrule.get(j)
							&& NSGAII.ocliskineoflocation.get(i) < NSGAII.finalrule.get(j) && !NSGAII.locationfilter.contains( NSGAII.ocliskineoflocation.get(i)))
					{
						collectionrule.add(j);
						
					
					}
				}
		 }

		 for (int i = 0; i < NSGAII.sequencelocation.size(); i++)
				for (int j = 0; j < NSGAII.faultrule.size(); j++) {
					//&& !listassequence.contains(NSGAII.sequencelocation.get(i))
					if (NSGAII.sequencelocation.get(i) > NSGAII.faultrule.get(j)
							&& NSGAII.sequencelocation.get(i) < NSGAII.finalrule.get(j))
						sequencerule.add(j);

				}
		 

		 for (int i = 0; i < collectionrule.size(); i++)
				if (NSGAII.errorrule.contains(collectionrule.get(i)))
					NSGAII.checkcollection = true;
		/* for(int i = 0; i < NSGAII.faultlocation.size(); i++) {
			 if(NSGAII.ocliskineoflocation.contains(NSGAII.faultlocation.get(i))) {
				 NSGAII.lineoclerror.add(NSGAII.faultlocation.get(i) );
				 
			 }
			 
		 }
		 if(NSGAII.lineoclerror.size()>0)
			 NSGAII.checkcollection = true;
		 else
			 NSGAII.checkcollection = false;*/
		/* for(int i = 0; i < NSGAII.locationfilter.size(); i++) {
			 
			 if(!NSGAII.faultlocation.contains(NSGAII.locationfilter.get(i) ) &&  NSGAII.ocliskineoflocation.contains(NSGAII.locationfilter.get(i) ) ) {
				 NSGAII.checkcollection = false;
		 }
	 }*/
		 
		 for (int i = 0; i < filterrule.size(); i++)
				if (NSGAII.errorrule.contains(filterrule.get(i))) {
				//	NSGAII.countfilter=NSGAII.countfilter+1;
					NSGAII.checkfilter = true;
				}
		 
		 for (int i = 0; i < sequencerule.size(); i++)
				if (NSGAII.errorrule.contains(sequencerule.get(i)))
					NSGAII.checksequence = true;
		 for (int i = 0; i < operationcallrule.size(); i++)
				if (NSGAII.errorrule.contains(operationcallrule.get(i)))
					NSGAII.checkoperationcall = true;
		
		 for (int i = 0; i < NSGAII.iterationrule.size(); i++)
				if (NSGAII.errorrule.contains(NSGAII.iterationrule.get(i)))
					NSGAII.checkiteration = true;
		 
		 System.out.println("checkfilter");
		 System.out.println(IntSolutionType.max_operations_size);
		 System.out.println(NSGAII.faultrule);
		 System.out.println(NSGAII.finalrule);
		 System.out.println(NSGAII.emptyopperationlocation);
		 System.out.println(NSGAII.inpatternstringlocation);
		 System.out.println(NSGAII.inpatternhasfilter);
		 System.out.println(NSGAII.errorrule);
		 System.out.println(NSGAII.iterationrule);
		 System.out.println(NSGAII.checkiteration);
		 System.out.println(NSGAII.checkcollection);
		 System.out.println(NSGAII.ocliskineoflocation);
		// NSGAII.writer.println(collectionrule);
		 NSGAII.writer.println(NSGAII.checksequence);
	/*	 System.out.println(NSGAII.checksequence);
		 System.out.println(NSGAII.checkcollection);
		 System.out.println("checkfilter");
		 System.out.println(NSGAII.errorrule );
		 System.out.println(NSGAII.checkiteration );
		 System.out.println(NSGAII.ocliskineoflocation);
		 System.out.println(NSGAII.iterationcall);
		 System.out.println(NSGAII.faultrule);
		 System.out.println(NSGAII.finalrule);
		 System.out.println(NSGAII.listnot);
		 System.out.println(NSGAII.parameterlocation);
		 System.out.println(NSGAII.locationfilter);
		 System.out.println(NSGAII.checkfilter);
		 System.out.println(NSGAII.checkoperationcall);*/
	//	 System.out.println("checkfilter");
	//	 System.out.println(NSGAII.checkfilter);
		 calculatelistsourcemetamodel();
		 NSGAII.forbiddenoperations = buildForbiddenOperation();
		}
    
		
	
}
private void calculatelistsourcemetamodel() {
	// TODO Auto-generated method stub
	
	Setting s=new Setting();
	String MMRootPath2     = s.gettargetmetamodel();
	 MetaModel metatarget=null;
	 try {
		metatarget=new MetaModel(MMRootPath2);
	} catch (transException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 int yy=0;
	 int start=0;
	 int lenght=0;
	 for (EClassifier classifier : metatarget.getEClassifiers()) {
		 
			if (classifier instanceof EClass) {
				
				EClass child = ((EClass) classifier);
				NSGAII.classnamestring.add(child.getName());
				NSGAII.classnamestartpoint.add(start);
				
				 
				 
				for (int  y=0;y<classifier.eContents().size();y++)
				{
					
						
					
					if (classifier.eContents().get(y) instanceof EAttribute
							||classifier.eContents().get(y) instanceof EReference ) {
						
						NSGAII.listsourcemetamodel.add( (EStructuralFeature) classifier.eContents().get(y));
						NSGAII.listinheritattributesourcemetamodel.add( (EStructuralFeature) classifier.eContents().get(y)); 
						NSGAII.listnavigationtype.add(((EStructuralFeature) classifier.eContents().get(y)).getEType().getName());
						yy=yy+1;
						lenght=lenght+1;
						
						
					}
					
				}
			
				 for (int i=0 ;i< child.getEAllSuperTypes().size();i++) {
					
					 EClass classifier2=child.getEAllSuperTypes().get(i);
					
					 if(classifier2.getName()!=null) {
					 for(int j=0;j< classifier2.eContents().size();j++) {
						 if (classifier2.eContents().get(j) instanceof EAttribute
									||classifier2.eContents().get(j) instanceof EReference ) {
							 
							 
							 NSGAII.listinheritattributesourcemetamodel.add( (EStructuralFeature) classifier2.eContents().get(j));  
							
							 lenght=lenght+1;
							 
						 }
						 
						 
					 }
					 
					 } 
				 }
				
				 for (EClassifier classifier2 : metatarget.getEClassifiers()) {
					 if (classifier2 instanceof EClass) {
						 EClass child2 = ((EClass) classifier2);
						 if(child.isSuperTypeOf(child2) && !child2.equals(child)) {
							 
							 for(int j=0;j< child2.eContents().size();j++) {
								 
								 if (child2.eContents().get(j) instanceof EAttribute
											||child2.eContents().get(j) instanceof EReference ) {
									 
									 NSGAII.listinheritattributesourcemetamodel.add( (EStructuralFeature) child2.eContents().get(j));  
									 
									 lenght=lenght+1;
									 
								 }
								 
								 
							 }
							 
							 
						 }
						 else {
							 if( !child2.isSuperTypeOf(child) ) {
							 for(int j=0;j< child2.eContents().size();j++) {
								 
								 if ((child2.eContents().get(j) instanceof EAttribute
											||child2.eContents().get(j) instanceof EReference) &&  ( (EStructuralFeature) child2.eContents().get(j)).getEType().getName().equals( child.getName() ) ) {
									 
									 
									 NSGAII.listinheritattributesourcemetamodel.add( (EStructuralFeature) child2.eContents().get(j));  
									 
									 lenght=lenght+1;
									 
									 
									 
								 }
								 
								 
							 }
							 }
							 
						 }
						 
					 }
					 
				 }
				 
				 
				
				NSGAII.classnamelength.add(lenght);
				start=start+lenght;
				lenght=0;
				
				
			}
			}
	 
	 		
	 
	
	
	
	
}
private void createclassifier() {
	// TODO Auto-generated method stub
	Setting s=new Setting();
	final String MMRootPath2     = s.getsourcemetamodel();
	
	List<EPackage>   metamodels2= retPackMM(retPackResouceMM(MMRootPath2));
	MetaModel metamodel = null;
	 for (EPackage p: metamodels2) {
		 metamodel = new MetaModel(p); 
	 }

	 for (EClassifier classifier : metamodel.getEClassifiers()) {
		 if (classifier instanceof EClass) {
				EClass child = ((EClass) classifier);
			
				NSGAII.classifierliast.add(classifier);
		 }
		 
	 }
	 
	 
	
}
public List<EPackage> retPackMM(Resource resourceMM)
{
	ResourceSet resourceSet=resourceMM.getResourceSet();
	List<EPackage> metamodel = new ArrayList<EPackage>();
	for (EObject obj : resourceMM.getContents()) {
		if (obj instanceof EPackage) {
			EPackage.Registry.INSTANCE.put		(((EPackage)obj).getNsURI(), ((EPackage)obj).getEFactoryInstance().getEPackage());
			resourceSet.getPackageRegistry().put(((EPackage)obj).getNsURI(), ((EPackage)obj).getEFactoryInstance().getEPackage());
			metamodel.add((EPackage)obj);
		}
	}
	return metamodel;
}
public Resource retPackResouceMM(String MMPath)
	 {	 	
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
		URI fileURI = URI.createFileURI(MMPath);//ecore.getFullPath().toOSString());		
		Resource resource = resourceSet.getResource(fileURI, true);	
		return resource;
	 }
private void deleteDirectory (String directory, boolean recursive) {
		File folder = new File(directory);
		if (folder.exists())
			for (File file : folder.listFiles()) {				
				if (file.isDirectory()) deleteDirectory(file.getPath(), recursive);
				file.delete();
			}
		folder.delete();
	}
	
	/**
	 * It creates a directory.
	 * @param folder name of directory
	 */
	private void createDirectory (String directory) {
		File folder = new File(directory);
		while (!folder.exists()) 
			folder.mkdir();
	}
	private EMFModel loadTransformationModel (String atlTransformationFile) throws ATLCoreException {
		ModelFactory      modelFactory = new EMFModelFactory();
		
		EMFReferenceModel atlMetamodel = (EMFReferenceModel)modelFactory.getBuiltInResource("ATL.ecore");
		
		//EMFReferenceModel atlMetamodel = (EMFReferenceModel)modelFactory.getBuiltInResource("C:\\Users\\wael\\OneDrive\\workspaceATLoperations\\mutants\\anatlyzer.evaluation.mutants\\ATL.ecore");
		AtlParser         atlParser    = new AtlParser();		
		EMFModel          atlModel     = (EMFModel)modelFactory.newModel(atlMetamodel);
		atlParser.inject (atlModel, atlTransformationFile);	
		atlModel.setIsTarget(true);				
		
//		// Should we want to serialize the model.
//		String injectedFile = "file:/" + atlTransformationFile + ".xmi";
//		IExtractor extractor = new EMFExtractor();
//		extractor.extract(atlModel, injectedFile);
		
		return atlModel;
	}
	private /*static*/ long index = 1;
	private String getValidNameOfFile (String outputFolder) {
		String outputfile = null;
		String aux        = null;
		for (long i=index; outputfile==null; i++) {
			aux = File.separatorChar + "finalresult" + ".atl";
			if (!new File(outputFolder, aux).exists()) { 
				outputfile = outputFolder + aux;
				index = i;
			}
			else index = i;
		}
		return outputfile;
	}
	
	protected boolean save (EMFModel atlModel, String outputFolder) {
		try {
			// save atl file
			System.out.println("outputFolder");
			System.out.println(outputFolder);
			String atl_transformation = this.getValidNameOfFile(outputFolder);
			AtlParser atlParser       = new AtlParser();
			atlParser.extract(atlModel, atl_transformation);
			
			// compile transformation
			String asm_transformation = atl_transformation.replace(".atl", ".asm");
		} 
		catch (ATLCoreException e) {}
	//	catch (FileNotFoundException e) {} 
	//	catch (IOException e) {}
		
		return false;
	}
	

  public void Testeroperate () throws ATLCoreException  {
		
		
		String temporalFolder="C:/Users/varaminz/Downloads/Project/workspaceTEST34/anatlyzer.atl.tests.api/trafo2/zahra2";
		String trafo="C:/Users/varaminz/Downloads/Project/workspaceTEST34/anatlyzer.atl.tests.api/trafo/PNML2PetriNet.atl";
		this.folderMutants = temporalFolder + "mutants" + File.separator;
		this.atlModel = this.loadTransformationModel(trafo);
		 this.deleteDirectory(this.folderMutants, true);
		 this.createDirectory(this.folderMutants);
		 this.save(this.atlModel, this.folderMutants);
			
		
		
	}
	
} // NSGA-II
