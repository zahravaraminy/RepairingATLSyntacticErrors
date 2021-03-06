//  BitFlipMutation.java
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

package jmetal.operators.mutation;

import jmetal.core.Solution;
import jmetal.encodings.solutionType.BinaryRealSolutionType;
import jmetal.encodings.solutionType.BinarySolutionType;
import jmetal.encodings.solutionType.IntSolutionType;
import jmetal.encodings.variable.Binary;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.problems.AdaptiveInterface.Input;
//import jmetal.problems.AdaptiveInterface.MyInput;
import jmetal.problems.AdaptiveInterface.Rule;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;

import anatlyzer.atlext.ATL.Binding;
import ca.udem.fixingatlerror.explorationphase.CoSolution;

/**
 * This class implements a bit flip mutation operator.
 * NOTE: the operator is applied to binary or integer solutions, considering the
 * whole solution as a single encodings.variable.
 */
public class BitFlipMutation extends Mutation {
  /**
   * Valid solution types to apply this operator 
   */
  private static final List VALID_TYPES = Arrays.asList(BinarySolutionType.class,
      BinaryRealSolutionType.class,
      IntSolutionType.class) ;

  private Double mutationProbability_ = null ;
  
	/**
	 * Constructor
	 * Creates a new instance of the Bit Flip mutation operator
	 */
	public BitFlipMutation(HashMap<String, Object> parameters) {
		super(parameters) ;
  	if (parameters.get("probability") != null)
  		mutationProbability_ = (Double) parameters.get("probability") ;  		
	} // BitFlipMutation

	/**
	 * Perform the mutation operation
	 * @param probability Mutation probability
	 * @param solution The solution to mutate
	 * @throws JMException
	 */
	public void doMutation(double probability, Solution solution) throws JMException {
		try {
			if ((solution.getType().getClass() == BinarySolutionType.class) ||
					(solution.getType().getClass() == BinaryRealSolutionType.class)) {
				for (int i = 0; i < solution.getDecisionVariables().length; i++) {
					for (int j = 0; j < ((Binary) solution.getDecisionVariables()[i]).getNumberOfBits(); j++) {
						if (PseudoRandom.randDouble() < probability) {
							((Binary) solution.getDecisionVariables()[i]).bits_.flip(j);
						}
					}
				}

				for (int i = 0; i < solution.getDecisionVariables().length; i++) {
					((Binary) solution.getDecisionVariables()[i]).decode();
				}
			} // if
			else { // Integer representation
				/*for (int i = 0; i < solution.numberOfVariables() ; i++  )//.getDecisionVariables().length; i++)
					if (PseudoRandom.randDouble() < probability) {
						int value = PseudoRandom.randInt(
								(int)solution.getDecisionVariables()[i].getLowerBound(),
								(int)solution.getDecisionVariables()[i].getUpperBound());
						MySolution S = new MySolution();
						S.Mymutation(value, S.rules);
						
						//solution.getDecisionVariables()[i].setValue(value);
						 
					} // if*/
			//	System.out.println("mutation");
//System.out.println("Wael ="+solution.operation().size());
			ArrayList<Integer> numbers1 = new ArrayList<Integer>();
			ArrayList<Integer> indexmutation = new ArrayList<Integer>();
			ArrayList<Integer> operationmutation = new ArrayList<Integer>();
			ArrayList<Integer> operationoriginal = new ArrayList<Integer>();
			
			int nummutation=0;
			Random number_generator = new Random();
			int temp=-1;
			//	int i = number_generator.nextInt(solution.operation().size());
			//int i=-1;
	//if(PseudoRandom.randDouble() > 0.1)
			for (int i = 0; i <solution.getoperations().size() ; i++  )	
		{
				
				
				if (PseudoRandom.randDouble() < 0.15) //.getDecisionVariables().length; i++)
		
	{
		boolean checksecondmutant=false;
		/*if(ip==1) {
			if(IntSolutionType.operations_size<=10 ) {
			if (PseudoRandom.randDouble() > 0.4) 
				checksecondmutant=true;
			}
			else {
				if (PseudoRandom.randDouble() > 0.2) 
					checksecondmutant=true;
				}
			}*/
		
					//	System.out.println(PseudoRandom.randDouble());
				//for (int i = 0; i <IntSolutionType.operations_size ; i++  )//.getDecisionVariables().length; i++)
				//	if (PseudoRandom.randDouble() < probability) 
			if (checksecondmutant==false) 
					{  
						/*boolean tp=false;
						while(tp==false) {
						 i = number_generator.nextInt(solution.operation().size());
						if(i!=temp)
							tp=true;
						}*/
						numbers1.clear();
						for(int jj=0;jj<i;jj++)
							numbers1.add(solution.operation().get(jj));
						for(int jj=i+1;jj<solution.operation().size();jj++)
							numbers1.add(solution.operation().get(jj));
						
						int  value =  PseudoRandom.randInt( 0,  (int)solution.operation().size()/2);
						
				//int value =1;
					//	System.out.println(value);
					//	System.out.println(IntSolutionType.operations_size);
							 if (value >= IntSolutionType.operations_size) {
								 //System.out.println("ce fait !!!!!");
								 value = IntSolutionType.operations_size-1;}
					//		  Random number_generator = new Random();
						      int nb = 0;
						      nb = (int) (Math.random() * 2 ); 
						   //   System.out.println(nb);
						      if(value >= solution.operation().size()) { nb = 0 ; } 
						     // System.out.println("nombre ? choisir : "+nb);
						      //System.out.println("mutation point: "+value);
						      //System.out.println("size of rules : "+IntSolutionType.rules.size());
						      nb=0;// khodam add kardam
						      boolean check=false;
						      if (nb==0)
						      {
						    	  NSGAII.writer.println("probability mutation");
						    	//  while (check==false) {
						    	  int numoperation = -10; 
						    	  boolean chh=false;
						    	  while (chh==false) {
						    	  Random randomGenerator = new Random();
						    	  numoperation = CoSolution.min_operations_interval + randomGenerator.nextInt(CoSolution.max_operations_interval - CoSolution.min_operations_interval+1);
						    	  chh=checksituationchh(numoperation,chh);
								    nummutation++;	 
						    	  }
						    //	  if(ip==1 && (PseudoRandom.randDouble() < 0.1))
						    //			  numoperation=-3;
						    //	  else
						    //	  int cc=0;
						    	//  boolean mut=false;
						    //	  while(mut==false) {
						    	 
						    	/*   for(int p=0;p<i;p++)
						    		   if(solution.operation().get(p)==2)
						    			   cc=cc+1;
						    	   for(int p=i+1;p<solution.operation().size();p++)
						    		   if(solution.operation().get(p)==2)
						    			   cc=cc+1;  
						    	   if(numoperation!=2)
						    		   mut=true;
						    	   else {
						    		   if(cc<NSGAII.countfilter)
						    			   mut=true; 
						    	   }
						    	  }*/
						    	  
						    		   
						    	 
						    //	  List<Integer> listdelet = new ArrayList<Integer>();
						    //	  replacetwodeletoperation(Solution solution, List<Integer> listdelet, int i)
						    	 
						  /*  	  if(numoperation==5) {
						    		  for(int p=i+1;p<solution.operation().size();p++ ) {
						    			  if(solution.operation().get(p)==5 && !solution.inorout.get(p).equals("out"))
						    				  listdelet.add(p);
						    			  
						    		  }
						    		  if (listdelet.size()>0) {
						    		 solution=  replacetwodeletoperation( solution,  listdelet,  i);
						    		 i=listdelet.get(listdelet.size()-1);
						    		  }
						    		  
						    	  }*/
						    	
						    	  solution.getlistmutation().add(solution.operation().get(i));
						    	   solution.getlistmutation().add(numoperation);
						    	   
						    	   temp=i;
						    	  switch(i+1) {
				          		   case 1:
				          			 solution.setoldrandomIntoperation1(-1);
				          			solution.setreplacerandomIntoperation1(-1);
				          			 solution.setsecondoldrandomIntoperation1(-1); 
				              		   break;
				          		   case 2:
				          			 solution.setoldrandomIntoperation2(-1);
				          			solution.setreplacerandomIntoperation2(-1);
				          			solution.setsecondoldrandomIntoperation2(-1);
				              		   break;
				          		   case 3:
				          			 solution.setoldrandomIntoperation3(-1);
				          			solution.setreplacerandomIntoperation3(-1);
				          			solution.setsecondoldrandomIntoperation3(-1);
				              		   break;
				          		   case 4:
				          			 solution.setoldrandomIntoperation4(-1);
				          			solution.setreplacerandomIntoperation4(-1);
				          			solution.setsecondoldrandomIntoperation4(-1);
				              		   break;
				          		   case 5:
				          			 solution.setoldrandomIntoperation5(-1);
				          			solution.setreplacerandomIntoperation5(-1);
				          			solution.setsecondoldrandomIntoperation5(-1);
				              		   break; 
				          		 case 6:
				          			 solution.setoldrandomIntoperation6(-1);
				          			solution.setreplacerandomIntoperation6(-1);
				          			solution.setsecondoldrandomIntoperation6(-1);
				              		   break; 
				          		 case 7:
				          			 solution.setoldrandomIntoperation7(-1);
				          			solution.setreplacerandomIntoperation7(-1);
				          			solution.setsecondoldrandomIntoperation7(-1);
				              		   break; 
				          		case 8:
				          			 solution.setoldrandomIntoperation8(-1);
				          			solution.setreplacerandomIntoperation8(-1);
				          			solution.setsecondoldrandomIntoperation8(-1);
				              		   break; 
				          		case 9:
				          			 solution.setoldrandomIntoperation9(-1);
				          			solution.setreplacerandomIntoperation9(-1);
				          			solution.setsecondoldrandomIntoperation9(-1);
				              		   break; 
				          		case 10:
				          			 solution.setoldrandomIntoperation10(-1);
				          			solution.setreplacerandomIntoperation10(-1);
				          			solution.setsecondoldrandomIntoperation10(-1);
				              		   break; 
				          		case 11:
				          			 solution.setoldrandomIntoperation11(-1);
				          			solution.setreplacerandomIntoperation11(-1);
				          			solution.setsecondoldrandomIntoperation11(-1);
				              		   break; 
				          		case 12:
				          			 solution.setoldrandomIntoperation12(-1);
				          			solution.setreplacerandomIntoperation12(-1);
				          			solution.setsecondoldrandomIntoperation12(-1);
				              		   break; 
				          		case 13:
				          			 solution.setoldrandomIntoperation13(-1);
				          			solution.setreplacerandomIntoperation13(-1);
				          			solution.setsecondoldrandomIntoperation13(-1);
				              		   break; 
				          		case 14:
				          			 solution.setoldrandomIntoperation14(-1);
				          			solution.setreplacerandomIntoperation14(-1);
				          			solution.setsecondoldrandomIntoperation14(-1);
				              		   break; 
				          		 case 15:
				          			 solution.setoldrandomIntoperation15(-1);
				          			solution.setreplacerandomIntoperation15(-1);
				          			solution.setsecondoldrandomIntoperation15(-1);
				              		   break; 
				          		 case 16:
				          			 solution.setoldrandomIntoperation16(-1);
				          			solution.setreplacerandomIntoperation16(-1);
				          			solution.setsecondoldrandomIntoperation16(-1);
				              		   break; 
				          		 case 17:
				          			 solution.setoldrandomIntoperation17(-1);
				          			solution.setreplacerandomIntoperation17(-1);
				          			solution.setsecondoldrandomIntoperation17(-1);
				              		   break; 
				          		case 18:
				          			 solution.setoldrandomIntoperation18(-1);
				          			solution.setreplacerandomIntoperation18(-1);
				          			solution.setsecondoldrandomIntoperation18(-1);
				              		   break; 
				          		case 19:
				          			 solution.setoldrandomIntoperation19(-1);
				          			solution.setreplacerandomIntoperation19(-1);
				          			solution.setsecondoldrandomIntoperation19(-1);
				              		   break; 
				          		case 20:
				          			 solution.setoldrandomIntoperation20(-1);
				          			solution.setreplacerandomIntoperation20(-1);
				          			solution.setsecondoldrandomIntoperation20(-1);
				              		   break; 
				          		case 21:
				          			 solution.setoldrandomIntoperation21(-1);
				          			solution.setreplacerandomIntoperation21(-1);
				          			solution.setsecondoldrandomIntoperation21(-1);
				              		   break; 
				          		case 22:
				          			 solution.setoldrandomIntoperation22(-1);
				          			solution.setreplacerandomIntoperation22(-1);
				          			solution.setsecondoldrandomIntoperation22(-1);
				              		   break; 
				          		
				          		   }
						    	  solution.setoperation(i, numoperation);
						    	  indexmutation.add(i);
						    	  operationmutation.add( numoperation);
						    	  solution.modifypropertyname.set(i, "khali");
						    	  solution.newstring.set(i, "khali");
						    	  solution.inorout.set(i, "empty");
						    	  Binding b=null;
						    	  solution.newbindings.set(i, b);
						    	  EClassifier obj=null;
						    	  solution.listeobject.set(i, obj);
						      
						      
						      }
						      if ( nb==1)
						    	  
						      {
						    	  //solution.rule().remove(value);
						    	  //IntSolutionType.rules.remove(IntSolutionType.rules.get(value))  ;
						      }
						
				//MySolution S = new MySolution();
				//S.Mymutation(value);
					   	  } //IntSolutionType.rules
	}
				else {
					
					operationoriginal.add( solution.operation().get(i));
					
					
				}
				
			}
		
	    	int indexof=-1;
			for (int i = 0; i <solution.getoperations().size() ; i++  )	
			{
				
				if(indexmutation.contains(i)) {
					 indexof=indexof+1;
					 if( !operationoriginal.contains( operationmutation.get(indexof))) {
						 NSGAII.writer.println("adade mutation");
				    	   NSGAII.writer.println(operationmutation.get(indexof));
						 operationoriginal.add(operationmutation.get(indexof));
					 }
					 else {
						  boolean chh=false;
						  int numoperation2 =-1;
				    	  while (chh==false  ) {
				    	  Random randomGenerator = new Random();
				    	  numoperation2 = CoSolution.min_operations_interval + randomGenerator.nextInt(CoSolution.max_operations_interval - CoSolution.min_operations_interval+1);
				    	 chh=checksituationchh(numoperation2,chh);
				    	 if(operationoriginal.contains(numoperation2))
				    	   chh=false;
				    	  }
				    	  NSGAII.writer.println("adade mutation");
				    	   NSGAII.writer.println(numoperation2);
				    	  solution.setoperation(i, numoperation2);
						 operationmutation.set(indexof, numoperation2);
						 operationoriginal.add( numoperation2);
						 
						 
					 }
					 
					 
				
				}		
			}
			if(indexmutation.size()==0) {
				NSGAII.writer.println("size sefr");
				solution=domutation( operationoriginal, solution) ;
				
				
			}
				
			
			} // else
		} catch (ClassCastException e1) {
			Configuration.logger_.severe("BitFlipMutation.doMutation: " +
					"ClassCastException error" + e1.getMessage());
			Class cls = java.lang.String.class;
			String name = cls.getName();
			throw new JMException("Exception in " + name + ".doMutation()");
		}
	} // doMutation

	
	public Solution domutation(ArrayList<Integer> operationoriginal, Solution solution) {
	  boolean chh=false;
	  int numoperation=-10;
	  Random number_generator = new Random();
	  int i = number_generator.nextInt(solution.operation().size());
   	  while (chh==false ) {
   	  Random randomGenerator = new Random();
   	  numoperation = CoSolution.min_operations_interval + randomGenerator.nextInt(CoSolution.max_operations_interval - CoSolution.min_operations_interval+1);
   	  chh=checksituationchh(numoperation,chh);
   	// if(solution.operation().contains(numoperation) &&  numoperation!=solution.operation().get(i))
   	//  chh=false;
   		  
		    
   	  }
   	NSGAII.writer.println("adade mutation");
	NSGAII.writer.println(numoperation);
   	
	   solution.getlistmutation().add(numoperation);
	  
		
     
     	 solution.getlistmutation().add(solution.operation().get(i));
	  switch(i+1) {
	   case 1:
		 solution.setoldrandomIntoperation1(-1);
		solution.setreplacerandomIntoperation1(-1);
		 solution.setsecondoldrandomIntoperation1(-1); 
		   break;
	   case 2:
		 solution.setoldrandomIntoperation2(-1);
		solution.setreplacerandomIntoperation2(-1);
		solution.setsecondoldrandomIntoperation2(-1);
		   break;
	   case 3:
		 solution.setoldrandomIntoperation3(-1);
		solution.setreplacerandomIntoperation3(-1);
		solution.setsecondoldrandomIntoperation3(-1);
		   break;
	   case 4:
		 solution.setoldrandomIntoperation4(-1);
		solution.setreplacerandomIntoperation4(-1);
		solution.setsecondoldrandomIntoperation4(-1);
		   break;
	   case 5:
		 solution.setoldrandomIntoperation5(-1);
		solution.setreplacerandomIntoperation5(-1);
		solution.setsecondoldrandomIntoperation5(-1);
		   break; 
	 case 6:
		 solution.setoldrandomIntoperation6(-1);
		solution.setreplacerandomIntoperation6(-1);
		solution.setsecondoldrandomIntoperation6(-1);
		   break; 
	 case 7:
		 solution.setoldrandomIntoperation7(-1);
		solution.setreplacerandomIntoperation7(-1);
		solution.setsecondoldrandomIntoperation7(-1);
		   break; 
	case 8:
		 solution.setoldrandomIntoperation8(-1);
		solution.setreplacerandomIntoperation8(-1);
		solution.setsecondoldrandomIntoperation8(-1);
		   break; 
	case 9:
		 solution.setoldrandomIntoperation9(-1);
		solution.setreplacerandomIntoperation9(-1);
		solution.setsecondoldrandomIntoperation9(-1);
		   break; 
	case 10:
		 solution.setoldrandomIntoperation10(-1);
		solution.setreplacerandomIntoperation10(-1);
		solution.setsecondoldrandomIntoperation10(-1);
		   break; 
	case 11:
		 solution.setoldrandomIntoperation11(-1);
		solution.setreplacerandomIntoperation11(-1);
		solution.setsecondoldrandomIntoperation11(-1);
		   break; 
	case 12:
		 solution.setoldrandomIntoperation12(-1);
		solution.setreplacerandomIntoperation12(-1);
		solution.setsecondoldrandomIntoperation12(-1);
		   break; 
	case 13:
		 solution.setoldrandomIntoperation13(-1);
		solution.setreplacerandomIntoperation13(-1);
		solution.setsecondoldrandomIntoperation13(-1);
		   break; 
	case 14:
		 solution.setoldrandomIntoperation14(-1);
		solution.setreplacerandomIntoperation14(-1);
		solution.setsecondoldrandomIntoperation14(-1);
		   break; 
	 case 15:
		 solution.setoldrandomIntoperation15(-1);
		solution.setreplacerandomIntoperation15(-1);
		solution.setsecondoldrandomIntoperation15(-1);
		   break; 
	 case 16:
		 solution.setoldrandomIntoperation16(-1);
		solution.setreplacerandomIntoperation16(-1);
		solution.setsecondoldrandomIntoperation16(-1);
		   break; 
	 case 17:
		 solution.setoldrandomIntoperation17(-1);
		solution.setreplacerandomIntoperation17(-1);
		solution.setsecondoldrandomIntoperation17(-1);
		   break; 
	case 18:
		 solution.setoldrandomIntoperation18(-1);
		solution.setreplacerandomIntoperation18(-1);
		solution.setsecondoldrandomIntoperation18(-1);
		   break; 
	case 19:
		 solution.setoldrandomIntoperation19(-1);
		solution.setreplacerandomIntoperation19(-1);
		solution.setsecondoldrandomIntoperation19(-1);
		   break; 
	case 20:
		 solution.setoldrandomIntoperation20(-1);
		solution.setreplacerandomIntoperation20(-1);
		solution.setsecondoldrandomIntoperation20(-1);
		   break; 
	case 21:
		 solution.setoldrandomIntoperation21(-1);
		solution.setreplacerandomIntoperation21(-1);
		solution.setsecondoldrandomIntoperation21(-1);
		   break; 
	case 22:
		 solution.setoldrandomIntoperation22(-1);
		solution.setreplacerandomIntoperation22(-1);
		solution.setsecondoldrandomIntoperation22(-1);
		   break; 
	
	   }
	  solution.setoperation(i, numoperation);
	  solution.modifypropertyname.set(i, "khali");
	  solution.newstring.set(i, "khali");
	  solution.inorout.set(i, "empty");
	  Binding b=null;
	  solution.newbindings.set(i, b);
	  EClassifier obj=null;
	  solution.listeobject.set(i, obj);

     return solution;
		
		
		
	}
	private boolean checksituationchh(int numoperation, boolean chh) {
		// TODO Auto-generated method stub
		 if(NSGAII.checkcollection==true) {
	 		   
	 		   if(NSGAII.checkfilter==false) {
		    	     {
		    	    	 if(NSGAII.checksequence==false)
		    	    	 {
		    	    		 if(NSGAII.checkoperationcall == true) {
		    	    			 
		    	    			 if(NSGAII.checkiteration==true) {
		    	    		 		if( (numoperation)!=4 && (numoperation)!=5) 
		    	    		 		{
		    	    		 			chh=true;
		    	    		 		}
		    	    		 		}
		    	    			 else {
		    	    				 if( (numoperation)!=4 && (numoperation)!=5 && (numoperation)!=10) 
			    	    		 		{
		    	    					 chh=true;
			    	    		 		}
		    	    				 
		    	    				 
		    	    			 }
		    	    			 
		    	    		 	
		    	    		 }
		    	    		 else {
		    	    			 if(NSGAII.checkiteration==true) {
		    	    			 if( (numoperation)!=4 && (numoperation)!=5 && (numoperation)!=2) 
			    	    		 	{
		    	    				 chh=true;	
			    	    		 	}
		    	    			 }
		    	    			 else {
		    	    				 if( (numoperation)!=4 && (numoperation)!=5 && (numoperation)!=2 && (numoperation)!=10) 
				    	    		 	{
		    	    					 chh=true;
				    	    		 	}
		    	    				 
		    	    			 }
		    	    		 }
		    	    	 }
		    	    	else {
		    	    		if(NSGAII.checkoperationcall == true) {
		    	    		if(NSGAII.checkiteration==true) {	
		    	    		if( (numoperation)!=5) 
		    	    		{
		    	    			chh=true;
		    	    		}
		    	    		}
		    	    		else {	
			    	    		if( (numoperation)!=5 && (numoperation)!=10) 
			    	    		{
			    	    			chh=true;
			    	    		}
			    	    		}
		    	    		
		    	    		}
		    	    		else {
		    	    			if(NSGAII.checkiteration==true) {	
		    	    			if( (numoperation)!=5 && (numoperation)!=2) 
			    	    		{
		    	    				chh=true;
			    	    		}
		    	    			}
		    	    			else {
		    	    				if( (numoperation)!=5 && (numoperation)!=2 && (numoperation)!=10) 
				    	    		{
		    	    					chh=true;
				    	    		}
		    	    				
		    	    				
		    	    			}
		    	    			
		    	    		}
		    	    	   }
		    	    		
		    	     }
		    	    	 }
		    	    	 else {
		    	    		 if(NSGAII.checksequence==false) {
		    	    			 if(NSGAII.checkoperationcall == true) {
		    	    				 if(NSGAII.checkiteration==true) {	
		    	    			 if( (numoperation)!=4 )
		    	    			 {
		    	    				 chh=true;
		    	    			 }
		    	    			}
		    	    				 else {
		    	    					 if( (numoperation)!=4 && (numoperation)!=10)
				    	    			 {
		    	    						 chh=true;
				    	    			 }
		    	    					 
		    	    				 }
		    	    			 }
		    	    			 else {
		    	    				 if(NSGAII.checkiteration==true) {	
		    	    				 if( (numoperation)!=2 && (numoperation)!=4)
			    	    			 {
		    	    					 chh=true;
			    	    			 }
		    	    				 }
		    	    				 else {	
			    	    				 if( (numoperation)!=2 && (numoperation)!=4 && (numoperation)!=10)
				    	    			 {
			    	    					 chh=true;	
				    	    			 }
			    	    				 }
		    	    			 }
		    	    		 }
		    	    		 else {
		    	    			 if(NSGAII.checkoperationcall == true) {
		    	    				 if(NSGAII.checkiteration==true) {	 
		    	    					 chh=true;
		    	    				 }
		    	    				 else {	 
		    	    					 if( (numoperation)!=10) {
		    	    						 chh=true;
				    	    				 }
		    	    				 }
		    	    		 }
		    	    		 else {
		    	    			 if(NSGAII.checkiteration==true) {	 
		    	    			 if((numoperation)!=2) {
		    	    				 chh=true;
		    	    			 }
		    	    			 }
		    	    			 else {
		    	    				 if((numoperation)!=10 && (numoperation)!=2) {
		    	    					 chh=true;
				    	    			 }
		    	    				 
		    	    			 }
		    	    			 
		    	    			 
		    	    		 }
		    	    		 }   
		    	    		  }
	 	  }
	 	   else if(NSGAII.checkcollection==false) {
	 		   if(NSGAII.checkfilter==false) {
	 			   if(NSGAII.checksequence==false) {
	 				  if(NSGAII.checkoperationcall == true) {
	 					 if(NSGAII.checkiteration==true) {	 
	 				   	if((numoperation)!=5 && (numoperation)!=4  && (numoperation)!=7) 
	 				   	{
	 				   	chh=true;
	 				   	}
	 					 }
	 					 else {
	 						if((numoperation)!=5 && (numoperation)!=4  && (numoperation)!=7 && (numoperation)!=10) 
		 				   	{
	 							chh=true;
		 				   	}
	 						 
	 					 }
	 				  }
	 				  else {
	 					 if(NSGAII.checkiteration==true) {	 
	 					 if((numoperation)!=5 && (numoperation)!=4  && (numoperation)!=7  && (numoperation)!=2) 
		 				   	{
	 						chh=true;
		 				   	}
	 					 }
	 					 else {
	 						if((numoperation)!=5 && (numoperation)!=4  && (numoperation)!=7  && (numoperation)!=2 && (numoperation)!=10) 
		 				   	{
	 							chh=true;
		 				   	}
	 						 
	 					 }
	 					  
	 				  }
	 			   }
	 			   else {
	 				  if(NSGAII.checkoperationcall == true) {
	 					 if(NSGAII.checkiteration==true) {	 
	 				  if((numoperation)!=5 && (numoperation)!=7) 
	 				  {
	 					 chh=true;
	 				  }
	 					 }
	 					 else {
	 						if((numoperation)!=5 && (numoperation)!=7  && (numoperation)!=10) 
			 				  {
	 							chh=true;
			 				  }
	 						 
	 						 
	 					 }
	 				  }
	 				  else {
	 					 if(NSGAII.checkiteration==true) {	 
	 					 if((numoperation)!=5 && (numoperation)!=7 && (numoperation)!=2) 
		 				  {
	 						chh=true;
		 				  }
	 					 }
	 					 else {
	 						if((numoperation)!=5 && (numoperation)!=7 && (numoperation)!=2 && (numoperation)!=10) 
			 				  {
	 							chh=true;
			 				  }
	 						 
	 					 }
	 					  
	 				  }
	 			   }
					}
					else {
						if(NSGAII.checksequence==false) {
						if(NSGAII.checkoperationcall == true) {
							if(NSGAII.checkiteration==true) {	 
						if( (numoperation)!=4  && (numoperation)!=7) 
						{
							
							chh=true;
						}
							}
							else {
								if( (numoperation)!=4  && (numoperation)!=7 && (numoperation)!=10) 
								{
									
									chh=true;
								}
								
							}
							}
						else {
							if(NSGAII.checkiteration==true) {	
							if( (numoperation)!=4  && (numoperation)!=7 && (numoperation)!=2) 
							{
								
								chh=true;
							}
							}
							else {
								if( (numoperation)!=4  && (numoperation)!=7 && (numoperation)!=2 && (numoperation)!=10) 
								{
									
									chh=true;
								}
								
							}
							
						}
						}
						else {
							if(NSGAII.checkoperationcall == true) {
								if(NSGAII.checkiteration==true) {
							if((numoperation)!=7) 
							{
								chh=true;	
							}
								}
								else {
									if((numoperation)!=7 && (numoperation)!=10) 
									{
										chh=true;
									}
										}
							}
							else {
								if(NSGAII.checkiteration==true) {
								if((numoperation)!=2 && (numoperation)!=7) 
								{
									chh=true;
								}
								}
								else {
									if((numoperation)!=2 && (numoperation)!=7  && (numoperation)!=10) 
									{
										
			    	    	    	     chh=true;
			    	    	    	     
									}
									}
								
								
								
							}
						}
						}
	 		   
	 	   } 
				return chh;
	}

	private Solution replacetwodeletoperation(Solution solution, List<Integer> listdelet, int i) {
		// TODO Auto-generated method stub
		
		for(int pp=0;pp< listdelet.size();pp++)
		{	
			 switch(i+1) {
    		   case 1:
    			   
    			   solution.setoldrandomIntoperation1(replacetwodelet(solution, listdelet.get(pp) ));
       			solution.setreplacerandomIntoperation1(replacetwodelet2(solution, listdelet.get(pp) ));
       			solution.setsecondoldrandomIntoperation1(replacetwodelet3(solution, listdelet.get(pp) ));
        		   break;
    		   case 2:
    			 solution.setoldrandomIntoperation2(replacetwodelet(solution, listdelet.get(pp) ));
    			solution.setreplacerandomIntoperation2(replacetwodelet2(solution, listdelet.get(pp) ));
    			solution.setsecondoldrandomIntoperation2(replacetwodelet3(solution, listdelet.get(pp) ));
        		   break;
    		   case 3:
    			 solution.setoldrandomIntoperation3(replacetwodelet(solution, listdelet.get(pp) ));
    			solution.setreplacerandomIntoperation3(replacetwodelet2(solution, listdelet.get(pp) ));
    			solution.setsecondoldrandomIntoperation3(replacetwodelet3(solution, listdelet.get(pp) ));
        		   break;
    		   case 4:
    			 solution.setoldrandomIntoperation4(replacetwodelet(solution, listdelet.get(pp) ));
    			solution.setreplacerandomIntoperation4(replacetwodelet2(solution, listdelet.get(pp) ));
    			solution.setsecondoldrandomIntoperation4(replacetwodelet3(solution, listdelet.get(pp) ));
        		   break;
    		   case 5:
    			 solution.setoldrandomIntoperation5(replacetwodelet(solution, listdelet.get(pp) ));
    			solution.setreplacerandomIntoperation5(replacetwodelet2(solution, listdelet.get(pp) ));
    			solution.setsecondoldrandomIntoperation5(replacetwodelet3(solution, listdelet.get(pp) ));
        		   break; 
    		 case 6:
    			 solution.setoldrandomIntoperation6(replacetwodelet(solution, listdelet.get(pp) ));
    			solution.setreplacerandomIntoperation6(replacetwodelet2(solution, listdelet.get(pp) ));
    			solution.setsecondoldrandomIntoperation6(replacetwodelet3(solution, listdelet.get(pp) ));
        		   break; 
    		 case 7:
    			 solution.setoldrandomIntoperation7(replacetwodelet(solution, listdelet.get(pp) ));
    			solution.setreplacerandomIntoperation7(replacetwodelet2(solution, listdelet.get(pp) ));
    			solution.setsecondoldrandomIntoperation7(replacetwodelet3(solution, listdelet.get(pp) ));
        		   break; 
    		case 8:
    			 solution.setoldrandomIntoperation8(replacetwodelet(solution, listdelet.get(pp) ));
    			solution.setreplacerandomIntoperation8(replacetwodelet2(solution, listdelet.get(pp) ));
    			solution.setsecondoldrandomIntoperation8(replacetwodelet3(solution, listdelet.get(pp) ));
        		   break; 
    		case 9:
    			 solution.setoldrandomIntoperation9(replacetwodelet(solution, listdelet.get(pp) ));
    			solution.setreplacerandomIntoperation9(replacetwodelet2(solution, listdelet.get(pp) ));
    			solution.setsecondoldrandomIntoperation9(replacetwodelet3(solution, listdelet.get(pp) ));
        		   break; 
    		case 10:
    			 solution.setoldrandomIntoperation10(replacetwodelet(solution, listdelet.get(pp) ));
    			solution.setreplacerandomIntoperation10(replacetwodelet2(solution, listdelet.get(pp) ));
    			solution.setsecondoldrandomIntoperation10(replacetwodelet3(solution, listdelet.get(pp) ));
        		   break; 
    		case 11:
    			 solution.setoldrandomIntoperation11(replacetwodelet(solution, listdelet.get(pp) ));
    			solution.setreplacerandomIntoperation11(replacetwodelet2(solution, listdelet.get(pp) ));
    			solution.setsecondoldrandomIntoperation11(replacetwodelet3(solution, listdelet.get(pp) ));
        		   break; 
    		case 12:
    			 solution.setoldrandomIntoperation12(replacetwodelet(solution, listdelet.get(pp) ));
    			solution.setreplacerandomIntoperation12(replacetwodelet2(solution, listdelet.get(pp) ));
    			solution.setsecondoldrandomIntoperation12(replacetwodelet3(solution, listdelet.get(pp) ));
        		   break; 
    		case 13:
    			 solution.setoldrandomIntoperation13(replacetwodelet(solution, listdelet.get(pp) ));
    			solution.setreplacerandomIntoperation13(replacetwodelet2(solution, listdelet.get(pp) ));
    			solution.setsecondoldrandomIntoperation13(replacetwodelet3(solution, listdelet.get(pp) ));
        		   break; 
    		case 14:
    			 solution.setoldrandomIntoperation14(replacetwodelet(solution, listdelet.get(pp) ));
    			solution.setreplacerandomIntoperation14(replacetwodelet2(solution, listdelet.get(pp) ));
    			solution.setsecondoldrandomIntoperation14(replacetwodelet3(solution, listdelet.get(pp) ));
        		   break; 
    		 case 15:
    			 solution.setoldrandomIntoperation15(replacetwodelet(solution, listdelet.get(pp) ));
    			solution.setreplacerandomIntoperation15(replacetwodelet2(solution, listdelet.get(pp) ));
    			solution.setsecondoldrandomIntoperation15(replacetwodelet3(solution, listdelet.get(pp) ));
        		   break; 
    		 case 16:
    			 solution.setoldrandomIntoperation16(replacetwodelet(solution, listdelet.get(pp) ));
    			solution.setreplacerandomIntoperation16(replacetwodelet2(solution, listdelet.get(pp) ));
    			solution.setsecondoldrandomIntoperation16(replacetwodelet3(solution, listdelet.get(pp) ));
        		   break; 
    		 case 17:
    			 solution.setoldrandomIntoperation17(replacetwodelet(solution, listdelet.get(pp) ));
    			solution.setreplacerandomIntoperation17(replacetwodelet2(solution, listdelet.get(pp) ));
    			solution.setsecondoldrandomIntoperation17(replacetwodelet3(solution, listdelet.get(pp) ));
        		   break; 
    		case 18:
    			 solution.setoldrandomIntoperation18(replacetwodelet(solution, listdelet.get(pp) ));
    			solution.setreplacerandomIntoperation18(replacetwodelet2(solution, listdelet.get(pp) ));
    			solution.setsecondoldrandomIntoperation18(replacetwodelet3(solution, listdelet.get(pp) ));
        		   break; 
    		case 19:
    			 solution.setoldrandomIntoperation19(replacetwodelet(solution, listdelet.get(pp) ));
    			solution.setreplacerandomIntoperation19(replacetwodelet2(solution, listdelet.get(pp) ));
    			solution.setsecondoldrandomIntoperation19(replacetwodelet3(solution, listdelet.get(pp) ));
        		   break; 
    		case 20:
    			 solution.setoldrandomIntoperation20(replacetwodelet(solution, listdelet.get(pp) ));
    			solution.setreplacerandomIntoperation20(replacetwodelet2(solution, listdelet.get(pp) ));
    			solution.setsecondoldrandomIntoperation20(replacetwodelet3(solution, listdelet.get(pp) ));
        		   break; 
    		case 21:
    			 solution.setoldrandomIntoperation21(replacetwodelet(solution, listdelet.get(pp) ));
    			solution.setreplacerandomIntoperation21(replacetwodelet2(solution, listdelet.get(pp) ));
    			solution.setsecondoldrandomIntoperation21(replacetwodelet3(solution, listdelet.get(pp) ));
        		   break; 
    		case 22:
    			 solution.setoldrandomIntoperation22(replacetwodelet(solution, listdelet.get(pp) ));
    			solution.setreplacerandomIntoperation22(replacetwodelet2(solution, listdelet.get(pp) ));
    			solution.setsecondoldrandomIntoperation22(replacetwodelet3(solution, listdelet.get(pp) ));
        		   break; 
    		
    		   }
			 i=listdelet.get(pp);
			 
		
		}
		
		return solution;
		
	}

	private int replacetwodelet(Solution solution, int p) {
		// TODO Auto-generated method stub
		
			
			 switch(p+1) {
    		   case 1:
    			   
    			return solution.getoldrandomIntoperation1();
    			
    		   case 2:
    			   return solution.getoldrandomIntoperation2();
    			   case 3:
    				   return solution.getoldrandomIntoperation3();
    		   case 4:
    			   return solution.getoldrandomIntoperation4();
    		   case 5:
    			   return solution.getoldrandomIntoperation5();
    		 case 6:
    			 return solution.getoldrandomIntoperation6();
    		 case 7:
    			 return solution.getoldrandomIntoperation7();
    		case 8:
    			return solution.getoldrandomIntoperation8();
    		case 9:
    			return solution.getoldrandomIntoperation9();
    		case 10:
    			return solution.getoldrandomIntoperation10();
    		case 11:
    			return solution.getoldrandomIntoperation11();
    		case 12:
    			return solution.getoldrandomIntoperation12();
    		case 13:
    			return solution.getoldrandomIntoperation13();
    		case 14:
    			return solution.getoldrandomIntoperation14();
    		 case 15:
    			 return solution.getoldrandomIntoperation15(); 
    		 case 16:
    			 return solution.getoldrandomIntoperation16();
    		 case 17:
    			 return solution.getoldrandomIntoperation17();
    		case 18:
    			return solution.getoldrandomIntoperation18(); 
    		case 19:
    			return solution.getoldrandomIntoperation19();
    		case 20:
    			return solution.getoldrandomIntoperation20();
    		case 21:
    			return solution.getoldrandomIntoperation21();
    		case 22:
    			return solution.getoldrandomIntoperation22();
    		   }
			return p;
	    	
		
	}
	private int replacetwodelet2(Solution solution, int p) {
		// TODO Auto-generated method stub
		
			
			 switch(p+1) {
    		   case 1:
    			   
    			
    		return	solution.getreplacerandomIntoperation1();
    			
        		  
    		   case 2:
    			   return	solution.getreplacerandomIntoperation2();
    		   case 3:
    			   return	solution.getreplacerandomIntoperation3();
    		   case 4:
    			   return	solution.getreplacerandomIntoperation4();
    		   case 5:
    			   return	solution.getreplacerandomIntoperation5();
    		 case 6:
    			 return	solution.getreplacerandomIntoperation6();
    		 case 7:
    			 return	solution.getreplacerandomIntoperation7();
    		case 8:
    			return	solution.getreplacerandomIntoperation8();
    		case 9:
    			return	solution.getreplacerandomIntoperation9();
    		case 10:
    			return	solution.getreplacerandomIntoperation10();
    		case 11:
    			return	solution.getreplacerandomIntoperation11(); 
    		case 12:
    			return	solution.getreplacerandomIntoperation12();
    		case 13:
    			return	solution.getreplacerandomIntoperation13();
    		case 14:
    			return	solution.getreplacerandomIntoperation14(); 
    		 case 15:
    			 return	solution.getreplacerandomIntoperation15();
    		 case 16:
    			 return	solution.getreplacerandomIntoperation16();
    		 case 17:
    			 return	solution.getreplacerandomIntoperation17();
    		case 18:
    			return	solution.getreplacerandomIntoperation18();
    		case 19:
    			return	solution.getreplacerandomIntoperation19();
    		case 20:
    			return	solution.getreplacerandomIntoperation20();
    		case 21:
    			return	solution.getreplacerandomIntoperation21();
    		case 22:
    			return	solution.getreplacerandomIntoperation22();
    		
    		   }
			return p;
	    	
		
	}
	private int replacetwodelet3(Solution solution, int p) {
		// TODO Auto-generated method stub
		
			
			 switch(p+1) {
    		   case 1:
    			   
    			 
    			return  solution.getsecondoldrandomIntoperation1(); 
        		 
    		   case 2:
    			
    			return solution.getsecondoldrandomIntoperation2();
        		   
    		   case 3:
    			 
    			
    		 return	solution.getsecondoldrandomIntoperation3();
        		   
    		   case 4:
    			   return	solution.getsecondoldrandomIntoperation4();
    		   case 5:
    			   return	solution.getsecondoldrandomIntoperation5();
    		 case 6:
    			 return	solution.getsecondoldrandomIntoperation6();
    		 case 7:
    			 return	solution.getsecondoldrandomIntoperation7();
    		case 8:
    			 return	solution.getsecondoldrandomIntoperation8();
    		case 9:
    			 return	solution.getsecondoldrandomIntoperation9();
    		case 10:
    			 return	solution.getsecondoldrandomIntoperation10();
    		case 11:
    			 return	solution.getsecondoldrandomIntoperation11();
    		case 12:
    			 return	solution.getsecondoldrandomIntoperation12(); 
    		case 13:
    			 return	solution.getsecondoldrandomIntoperation13();
    		case 14:
    			 return	solution.getsecondoldrandomIntoperation14();
    		 case 15:
    			 return	solution.getsecondoldrandomIntoperation15();
    		 case 16:
    			 return	solution.getsecondoldrandomIntoperation16();
    		 case 17:
    			 return	solution.getsecondoldrandomIntoperation17();
    		case 18:
    			 return	solution.getsecondoldrandomIntoperation18(); 
    		case 19:
    			 return	solution.getsecondoldrandomIntoperation19();
    		case 20:
    			 return	solution.getsecondoldrandomIntoperation20();
    		case 21:
    			 return	solution.getsecondoldrandomIntoperation21();
    		case 22:
    			 return	solution.getsecondoldrandomIntoperation22();
    		
    		   }
			return p;
	    	
		
	}
	
	
	/**
	 * Executes the operation
	 * @param object An object containing a solution to mutate
	 * @return An object containing the mutated solution
	 * @throws JMException 
	 */
	public Object execute(Object object) throws JMException {
		Solution solution = (Solution) object;

		if (!VALID_TYPES.contains(solution.getType().getClass())) {
			Configuration.logger_.severe("BitFlipMutation.execute: the solution " +
					"is not of the right type. The type should be 'Binary', " +
					"'BinaryReal' or 'Int', but " + solution.getType() + " is obtained");

			Class cls = java.lang.String.class;
			String name = cls.getName();
			throw new JMException("Exception in " + name + ".execute()");
		} // if 

		doMutation(mutationProbability_, solution);
		return solution;
	} // execute
} // BitFlipMutation
