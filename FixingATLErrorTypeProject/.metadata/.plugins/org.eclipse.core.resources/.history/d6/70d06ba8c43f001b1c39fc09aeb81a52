//  SinglePointCrossover.java
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

package jmetal.operators.crossover;

import jmetal.core.Solution;
import jmetal.encodings.solutionType.BinaryRealSolutionType;
import jmetal.encodings.solutionType.BinarySolutionType;
import jmetal.encodings.solutionType.IntSolutionType;
import jmetal.encodings.variable.Binary;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.problems.MyProblem;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;

import anatlyzer.atlext.ATL.Binding;
import anatlyzer.atlext.OCL.OclExpression;

/**
 * This class allows to apply a Single Point crossover operator using two parent
 * solutions.
 */
public class SinglePointCrossover extends Crossover {
  /**
   * Valid solution types to apply this operator 
   */
  private static final List VALID_TYPES = Arrays.asList(BinarySolutionType.class,
  		                                            BinaryRealSolutionType.class,
  		                                            IntSolutionType.class) ;

  private Double crossoverProbability_ = null;

  /**
   * Constructor
   * Creates a new instance of the single point crossover operator
   */
  public SinglePointCrossover(HashMap<String, Object> parameters) {
  	super(parameters) ;
  	if (parameters.get("probability") != null)
  		crossoverProbability_ = (Double) parameters.get("probability") ;  		
  } // SinglePointCrossover


  /**
   * Constructor
   * Creates a new instance of the single point crossover operator
   */
  //public SinglePointCrossover(Properties properties) {
  //    this();
  //} // SinglePointCrossover

  /**
   * Perform the crossover operation.
   * @param probability Crossover probability
   * @param parent1 The first parent
   * @param parent2 The second parent   
   * @return An array containig the two offsprings
   * @throws JMException
   */
  public Solution[] doCrossover(double probability,
          Solution parent1,
          Solution parent2) throws JMException {
    Solution[] offSpring = new Solution[2];
    offSpring[0] = new Solution(parent1);
    offSpring[1] = new Solution(parent2);
    System.out.println(parent1.inorout);
    System.out.println(parent2.inorout);
    
  /*  offSpring[0].classifiersolution=parent1.classifiersolution;
    offSpring[1].classifiersolution=parent2.classifiersolution;
    offSpring[0].modifypropertyname=parent1.modifypropertyname;
    offSpring[1].modifypropertyname=parent2.modifypropertyname;
    offSpring[0].newstring=parent1.newstring;
    offSpring[1].newstring=parent2.newstring;
    offSpring[0].inorout=parent1.inorout;
    offSpring[1].inorout=parent2.inorout;
    offSpring[0].expression=parent1.expression;
    offSpring[1].expression=parent2.expression;
    offSpring[0].modifypropertyname=parent1.modifypropertyname;
    offSpring[1].modifypropertyname=parent2.modifypropertyname;
    offSpring[0].newbindings=parent1.newbindings;
    offSpring[1].newbindings=parent2.newbindings;*/
  //  System.out.println("single111");
  //  System.out.println(PseudoRandom.randDouble());
    try {
      if (PseudoRandom.randDouble() < probability) {
    	//  System.out.println("single222");
        if ((parent1.getType().getClass() == BinarySolutionType.class) ||
            (parent1.getType().getClass() == BinaryRealSolutionType.class)) {
          //1. Compute the total number of bits
        //	System.out.println("real");
          int totalNumberOfBits = 0;
          for (int i = 0; i < parent1.getDecisionVariables().length; i++) {
            totalNumberOfBits +=
                    ((Binary) parent1.getDecisionVariables()[i]).getNumberOfBits();
          }
          
          int crossoverPoint = -1;
          //2. Calculate the point to make the crossover
         
                crossoverPoint = PseudoRandom.randInt(0, totalNumberOfBits - 1);
              
          
          //3. Compute the encodings.variable containing the crossoverPoint bit
          int variable = 0;
          int acountBits =
                  ((Binary) parent1.getDecisionVariables()[variable]).getNumberOfBits();

          while (acountBits < (crossoverPoint + 1)) {
            variable++;
            acountBits +=
                    ((Binary) parent1.getDecisionVariables()[variable]).getNumberOfBits();
          }

          //4. Compute the bit into the selected encodings.variable
          int diff = acountBits - crossoverPoint;
          int intoVariableCrossoverPoint =
                  ((Binary) parent1.getDecisionVariables()[variable]).getNumberOfBits() - diff ;

          //5. Make the crossover into the gene;
          Binary offSpring1, offSpring2;
          offSpring1 =
                  (Binary) parent1.getDecisionVariables()[variable].deepCopy();
          offSpring2 =
                  (Binary) parent2.getDecisionVariables()[variable].deepCopy();

          for (int i = intoVariableCrossoverPoint;
                  i < offSpring1.getNumberOfBits();
                  i++) {
            boolean swap = offSpring1.bits_.get(i);
            offSpring1.bits_.set(i, offSpring2.bits_.get(i));
            offSpring2.bits_.set(i, swap);
          }

          offSpring[0].getDecisionVariables()[variable] = offSpring1;
          offSpring[1].getDecisionVariables()[variable] = offSpring2;

          //6. Apply the crossover to the other variables
          for (int i = 0; i < variable; i++) {
            offSpring[0].getDecisionVariables()[i] =
                    parent2.getDecisionVariables()[i].deepCopy();

            offSpring[1].getDecisionVariables()[i] =
                    parent1.getDecisionVariables()[i].deepCopy();

          }

          //7. Decode the results
          for (int i = 0; i < offSpring[0].getDecisionVariables().length; i++) {
            ((Binary) offSpring[0].getDecisionVariables()[i]).decode();
            ((Binary) offSpring[1].getDecisionVariables()[i]).decode();
          }
        } // Binary or BinaryReal
        else { // Integer representation
        	// Integer representation
        	//System.out.println("integer");
        //	System.out.println(offSpring[0].getoperations().size());
        	//System.out.println(offSpring[1].getoperations().size());
        	int min=0;
        	 boolean checkparent1=false;
             boolean checkparent2=false;
             boolean checkparent3=false;
             boolean checkparent4=false;
            
             int crossoverPoint=-1;
        	if (offSpring[0].getoperations().size()<offSpring[1].getoperations().size())
        		min=offSpring[0].getoperations().size();
        	else
        		min=offSpring[1].getoperations().size();
        	System.out.println(offSpring[0].operation()); 
            System.out.println(offSpring[1].operation());
            int firstmin=-1;
            int firstmax=-1;
            int secondmin=-1;
            int secondmax=-1;
            int minboth=-1;
            int maxboth=-1;
            for(int ii=0;ii< offSpring[0].operation().size();ii++) {
            	if(offSpring[0].operation().get(ii)!=-3 && checkparent1==false)
            	{
            		  checkparent1=true;
            		  firstmin=ii+1;
            	}
            	if(offSpring[0].operation().get(ii)!=-3 && (ii+1)!= offSpring[0].operation().size())
            	{
            		  
            		  firstmax=ii+1;
            	}
            	if(offSpring[1].operation().get(ii)!=-3 && checkparent2==false)
            	{
            		  checkparent2=true;
            		  secondmin=ii+1;
            	}
            	if(offSpring[1].operation().get(ii)!=-3 && (ii+1)!= offSpring[0].operation().size())
            	{
            		  
            		  secondmax=ii+1;
            	}
            	
            }
            if(firstmin<=secondmin)
            	minboth=firstmin;
            else
            	minboth=secondmin;
            if(firstmax>=secondmax)
            	maxboth=firstmax;
            else
            	maxboth=secondmax;
            while(checkparent3==false) {
            	System.out.println("whilecross");
            	
            	 int numbercheckparent1=0;
                 int numbercheckparent2=0;
            crossoverPoint = PseudoRandom.randInt(minboth , offSpring[0].operation().size());
            if(crossoverPoint>0) {
           
            for(int ii=0;ii<crossoverPoint;ii++) {
            	 
            	 if(offSpring[0].operation().get(ii)==-3)
            		numbercheckparent1=numbercheckparent1+1;
           	  if(offSpring[1].operation().get(ii)==-3)
           		 numbercheckparent2=numbercheckparent2+1;
              }
            for(int ii=crossoverPoint;ii<offSpring[0].operation().size();ii++) {
          	 
          	  if(offSpring[0].operation().get(ii)==-3)
          		  numbercheckparent2=numbercheckparent2+1;
          	  if(offSpring[1].operation().get(ii)==-3)
          		  numbercheckparent1=numbercheckparent1+1;
        
            }
            if(numbercheckparent1!=offSpring[0].operation().size() && numbercheckparent2!=offSpring[0].operation().size())
            	checkparent3=true;
            if(numbercheckparent1==offSpring[0].operation().size() || numbercheckparent2==offSpring[0].operation().size()
            		&& minboth==maxboth) {
            	checkparent3=true;
            	crossoverPoint=minboth-1;
            	
            }
            }
            if(crossoverPoint==0)
            	checkparent3=true;
            System.out.println( crossoverPoint);
            System.out.println( offSpring[0].operation());
            System.out.println( offSpring[1].operation());
            System.out.println( minboth);
            System.out.println( maxboth);
            NSGAII.writer.println(crossoverPoint);
            NSGAII.writer.println(numbercheckparent1);
            NSGAII.writer.println(numbercheckparent2);
            NSGAII.writer.println(checkparent3);
            
            }
           // int crossoverPoint = PseudoRandom.randInt(0 , IntSolutionType.min_operations_size-1);
       /* 	 while((checkparent1==false || checkparent2==false )) {
                  crossoverPoint = PseudoRandom.randInt(0 , min);
                  if(crossoverPoint==0 || crossoverPoint== offSpring[0].operation().size()
                		  ||numbercheckparent1==offSpring[0].operation().size()
                		  || numbercheckparent2==offSpring[0].operation().size()) {
                	  checkparent1=true;
                	  checkparent2=true;
                  }
            
            for(int ii=0;ii<crossoverPoint;ii++) {
          	  if(offSpring[0].operation().get(ii)!=-3)
          		  checkparent1=true;
          	  if(offSpring[1].operation().get(ii)!=-3)
          		  checkparent1=true;
          	 if(offSpring[0].operation().get(ii)==-3)
          		numbercheckparent1=numbercheckparent1+1;
         	  if(offSpring[1].operation().get(ii)==-3)
         		 numbercheckparent2=numbercheckparent2+1;
            }
            for(int ii=crossoverPoint;ii<offSpring[0].operation().size();ii++) {
            	  if(offSpring[0].operation().get(ii)!=-3)
            		  checkparent2=true;
            	  if(offSpring[1].operation().get(ii)!=-3)
            		  checkparent2=true;
            	  if(offSpring[0].operation().get(ii)==-3)
            		  numbercheckparent1=numbercheckparent1+1;
            	  if(offSpring[1].operation().get(ii)==-3)
            		  numbercheckparent2=numbercheckparent2+1;
          
              }
           
           NSGAII.writer.println(checkparent1);
           NSGAII.writer.println(checkparent2);
           System.out.println(crossoverPoint); 
           System.out.println(checkparent1);
           System.out.println(checkparent2);
      
        }*/
        	 
          //  System.out.println(crossoverPoint);
           Integer parents1;
           Integer parents2;
           String str1;
           String str2;
           Binding bin1;
           Binding bin2;
           EClassifier classifier1;
           EClassifier classifier2;
           EClassifier obj1;
           EClassifier obj2;
           //parents1 =  parent1;
          // parents2 =  parent2;
          // MySolution S = new MySolution ();
           //S.MyCrossover(parent1.rule(), parent2.rule(), crossoverPoint);
           ArrayList<Integer> numbers1 = new ArrayList<Integer>();  
           ArrayList<Integer> numbers2 = new ArrayList<Integer>();
           int old1,old2,old3,old4,old5;
           int secondold1,secondold2,secondold3,secondold4,secondold5;
           int replace1,replace2,replace3,replace4,replace5;
           NSGAII.writer.println("cut crossover");
           System.out.println(crossoverPoint);
           NSGAII.writer.println(crossoverPoint);
           offSpring[0].setoldrandomIntoperation1(0);
           offSpring[0].setreplacerandomIntoperation1(0);
           offSpring[0].setsecondoldrandomIntoperation1(0);
           offSpring[0].setoldrandomIntoperation2(0);
           offSpring[0].setreplacerandomIntoperation2(0);
           offSpring[0].setsecondoldrandomIntoperation2(0);
           offSpring[0].setoldrandomIntoperation3(0);
           offSpring[0].setreplacerandomIntoperation3(0);
           offSpring[0].setsecondoldrandomIntoperation3(0);
           offSpring[0].setoldrandomIntoperation4(0);
           offSpring[0].setreplacerandomIntoperation4(0);
           offSpring[0].setsecondoldrandomIntoperation4(0);
           offSpring[0].setoldrandomIntoperation5(0);
           offSpring[0].setreplacerandomIntoperation5(0);
           offSpring[0].setsecondoldrandomIntoperation5(0);
           offSpring[0].setoldrandomIntoperation6(0);
           offSpring[0].setreplacerandomIntoperation6(0);
           offSpring[0].setsecondoldrandomIntoperation6(0);
           offSpring[0].setoldrandomIntoperation7(0);
           offSpring[0].setreplacerandomIntoperation7(0);
           offSpring[0].setsecondoldrandomIntoperation7(0);
           offSpring[0].setoldrandomIntoperation8(0);
           offSpring[0].setreplacerandomIntoperation8(0);
           offSpring[0].setsecondoldrandomIntoperation8(0);
           offSpring[0].setoldrandomIntoperation9(0);
           offSpring[0].setreplacerandomIntoperation9(0);
           offSpring[0].setsecondoldrandomIntoperation9(0);
           offSpring[0].setoldrandomIntoperation10(0);
           offSpring[0].setreplacerandomIntoperation10(0);
           offSpring[0].setsecondoldrandomIntoperation10(0);
           offSpring[0].setoldrandomIntoperation11(0);
           offSpring[0].setreplacerandomIntoperation11(0);
           offSpring[0].setsecondoldrandomIntoperation11(0);
           offSpring[0].setoldrandomIntoperation12(0);
           offSpring[0].setreplacerandomIntoperation12(0);
           offSpring[0].setsecondoldrandomIntoperation12(0);
           offSpring[0].setoldrandomIntoperation13(0);
           offSpring[0].setreplacerandomIntoperation13(0);
           offSpring[0].setsecondoldrandomIntoperation13(0);
           offSpring[0].setoldrandomIntoperation14(0);
           offSpring[0].setreplacerandomIntoperation14(0);
           offSpring[0].setsecondoldrandomIntoperation14(0);
           offSpring[0].setoldrandomIntoperation15(0);
           offSpring[0].setreplacerandomIntoperation15(0);
           offSpring[0].setsecondoldrandomIntoperation15(0);
           offSpring[0].setoldrandomIntoperation16(0);
           offSpring[0].setreplacerandomIntoperation16(0);
           offSpring[0].setsecondoldrandomIntoperation16(0);
           offSpring[0].setoldrandomIntoperation17(0);
           offSpring[0].setreplacerandomIntoperation17(0);
           offSpring[0].setsecondoldrandomIntoperation17(0);
           offSpring[0].setoldrandomIntoperation18(0);
           offSpring[0].setreplacerandomIntoperation18(0);
           offSpring[0].setsecondoldrandomIntoperation18(0);
           offSpring[0].setoldrandomIntoperation19(0);
           offSpring[0].setreplacerandomIntoperation19(0);
           offSpring[0].setsecondoldrandomIntoperation19(0);
           offSpring[0].setoldrandomIntoperation20(0);
           offSpring[0].setreplacerandomIntoperation20(0);
           offSpring[0].setsecondoldrandomIntoperation20(0);
           offSpring[0].setoldrandomIntoperation21(0);
           offSpring[0].setreplacerandomIntoperation21(0);
           offSpring[0].setsecondoldrandomIntoperation21(0);
           offSpring[0].setoldrandomIntoperation22(0);
           offSpring[0].setreplacerandomIntoperation22(0);
           offSpring[0].setsecondoldrandomIntoperation22(0);
           
           
           
           offSpring[1].setoldrandomIntoperation1(0);
           offSpring[1].setreplacerandomIntoperation1(0);
           offSpring[1].setsecondoldrandomIntoperation1(0);
           offSpring[1].setoldrandomIntoperation2(0);
           offSpring[1].setreplacerandomIntoperation2(0);
           offSpring[1].setsecondoldrandomIntoperation2(0);
           offSpring[1].setoldrandomIntoperation3(0);
           offSpring[1].setreplacerandomIntoperation3(0);
           offSpring[1].setsecondoldrandomIntoperation3(0);
           offSpring[1].setoldrandomIntoperation4(0);
           offSpring[1].setreplacerandomIntoperation4(0);
           offSpring[1].setsecondoldrandomIntoperation4(0);
           offSpring[1].setoldrandomIntoperation5(0);
           offSpring[1].setreplacerandomIntoperation5(0);
           offSpring[1].setsecondoldrandomIntoperation5(0);
           offSpring[1].setoldrandomIntoperation6(0);
           offSpring[1].setreplacerandomIntoperation6(0);
           offSpring[1].setsecondoldrandomIntoperation6(0);
           offSpring[1].setoldrandomIntoperation7(0);
           offSpring[1].setreplacerandomIntoperation7(0);
           offSpring[1].setsecondoldrandomIntoperation7(0);
           offSpring[1].setoldrandomIntoperation8(0);
           offSpring[1].setreplacerandomIntoperation8(0);
           offSpring[1].setsecondoldrandomIntoperation8(0);
           offSpring[1].setoldrandomIntoperation9(0);
           offSpring[1].setreplacerandomIntoperation9(0);
           offSpring[1].setsecondoldrandomIntoperation9(0);
           offSpring[1].setoldrandomIntoperation10(0);
           offSpring[1].setreplacerandomIntoperation10(0);
           offSpring[1].setsecondoldrandomIntoperation10(0);
           offSpring[1].setoldrandomIntoperation11(0);
           offSpring[1].setreplacerandomIntoperation11(0);
           offSpring[1].setsecondoldrandomIntoperation11(0);
           offSpring[1].setoldrandomIntoperation12(0);
           offSpring[1].setreplacerandomIntoperation12(0);
           offSpring[1].setsecondoldrandomIntoperation12(0);
           offSpring[1].setoldrandomIntoperation13(0);
           offSpring[1].setreplacerandomIntoperation13(0);
           offSpring[1].setsecondoldrandomIntoperation13(0);
           offSpring[1].setoldrandomIntoperation14(0);
           offSpring[1].setreplacerandomIntoperation14(0);
           offSpring[1].setsecondoldrandomIntoperation14(0);
           offSpring[1].setoldrandomIntoperation15(0);
           offSpring[1].setreplacerandomIntoperation15(0);
           offSpring[1].setsecondoldrandomIntoperation15(0);
           offSpring[1].setoldrandomIntoperation16(0);
           offSpring[1].setreplacerandomIntoperation16(0);
           offSpring[1].setsecondoldrandomIntoperation16(0);
           offSpring[1].setoldrandomIntoperation17(0);
           offSpring[1].setreplacerandomIntoperation17(0);
           offSpring[1].setsecondoldrandomIntoperation17(0);
           offSpring[1].setoldrandomIntoperation18(0);
           offSpring[1].setreplacerandomIntoperation18(0);
           offSpring[1].setsecondoldrandomIntoperation18(0);
           offSpring[1].setoldrandomIntoperation19(0);
           offSpring[1].setreplacerandomIntoperation19(0);
           offSpring[1].setsecondoldrandomIntoperation19(0);
           offSpring[1].setoldrandomIntoperation20(0);
           offSpring[1].setreplacerandomIntoperation20(0);
           offSpring[1].setsecondoldrandomIntoperation20(0);
           offSpring[1].setoldrandomIntoperation21(0);
           offSpring[1].setreplacerandomIntoperation21(0);
           offSpring[1].setsecondoldrandomIntoperation21(0);
           offSpring[1].setoldrandomIntoperation22(0);
           offSpring[1].setreplacerandomIntoperation22(0);
           offSpring[1].setsecondoldrandomIntoperation22(0);
    /*       for(int l=0;l<offSpring[0].operation().size();l++) {
         	  EClassifier classifier=null;
         	  offSpring[0].classifiersolution.add(classifier);
           }
           for(int l=0;l<offSpring[1].operation().size();l++) {
         	  EClassifier classifier=null;
         	  offSpring[1].classifiersolution.add(classifier);
           }
           for(int l=0;l<offSpring[0].operation().size();l++)
         	  offSpring[0].inorout.add("empty");
           for(int l=0;l<offSpring[1].operation().size();l++)
         	  offSpring[1].inorout.add("empty");
           for(int l=0;l<offSpring[0].operation().size();l++) {
        	   OclExpression exp=null;
          	  offSpring[0].expression.add(exp);
           }
           for(int l=0;l<offSpring[1].operation().size();l++) {
        	   OclExpression exp=null;
          	  offSpring[1].expression.add(exp);
           }
    /*       for(int l=0;l<offSpring[0].operation().size();l++) {
        	   Binding b = null;
          	  offSpring[0].getCoSolution().getOp().newbindings.add(b);
           }
           for(int l=0;l<offSpring[1].operation().size();l++) {
        	   Binding b = null;
          	  offSpring[1].getCoSolution().getOp().newbindings.add(b);
           }
     
           
           offSpring[0].inorout=parent1.inorout;
           offSpring[1].inorout=parent2.inorout;
           offSpring[0].modifypropertyname=parent1.modifypropertyname;
           offSpring[1].modifypropertyname=parent2.modifypropertyname;
           
           offSpring[0].expression=parent1.expression;
           offSpring[1].expression=parent2.expression;
           offSpring[0].classifiersolution=parent1.classifiersolution;
           offSpring[1].classifiersolution=parent2.classifiersolution;*/
           System.out.println("crossover");
           System.out.println(crossoverPoint);
           
          
           
           for (int i = 0; i < crossoverPoint; i++) {
        	   NSGAII.writer.println("accepted");
        	   parents1 =  parent1.operation().get(i);
        	   parents2 = parent2.operation().get(i);
        	   offSpring[0].operation().set(i, parents2) ;
        	   numbers1.add(parents2);
        	   offSpring[1].operation().set(i, parents1) ;
        	   numbers2.add(parents1);
        	   str1=parent1.inorout.get(i);
        	   str2=parent2.inorout.get(i);
        	   offSpring[0].inorout.set(i, str2);
        	   offSpring[1].inorout.set(i, str1);
        	   
        	   obj1=parent1.listeobject.get(i);
        	   obj2=parent2.listeobject.get(i);
        	   offSpring[0].listeobject.set(i, obj2);
        	   offSpring[1].listeobject.set(i, obj1);
        	  
        	   
        	   str1=parent1.modifypropertyname.get(i);
        	   str2=parent2.modifypropertyname.get(i);
        	   
        	   offSpring[0].modifypropertyname.set(i, str2);
        	   offSpring[1].modifypropertyname.set(i, str1);
        	   
        	   str1=parent1.newstring.get(i);
        	   str2=parent2.newstring.get(i);
        	   
        	   offSpring[0].newstring.set(i, str2);
        	   offSpring[1].newstring.set(i, str1);
        	   
        	   offSpring[0].expression.set(i, parent2.expression.get(i));
        	   offSpring[1].expression.set(i, parent1.expression.get(i));
        	   classifier1=parent1.classifiersolution.get(i);
        	   classifier2=parent2.classifiersolution.get(i);
        	   
        	   offSpring[0].classifiersolution.set(i, classifier2);
        	   offSpring[1].classifiersolution.set(i, classifier1);
        	   bin1=parent1.newbindings.get(i);
        	   bin2=parent2.newbindings.get(i);
        	   offSpring[0].newbindings.set(i, bin2);
        	   offSpring[1].newbindings.set(i, bin1);
        	 
        	   //   switch (parents1) {
        	//   case 1:
        	   
        		   switch(i+1) {
        		   case 1:
        			   old1=parent1.getoldrandomIntoperation1();
        			   replace1=parent1.getreplacerandomIntoperation1();
        			   secondold1=parent1.getsecondoldrandomIntoperation1();
        			   offSpring[0].setoldrandomIntoperation1(parent2.getoldrandomIntoperation1());
        			   offSpring[0].setreplacerandomIntoperation1(parent2.getreplacerandomIntoperation1());
        			   offSpring[0].setsecondoldrandomIntoperation1(parent2.getsecondoldrandomIntoperation1());
        			   offSpring[1].setoldrandomIntoperation1(old1);
        			   offSpring[1].setreplacerandomIntoperation1(replace1);
        			   offSpring[1].setsecondoldrandomIntoperation1(secondold1);
            		   break;
        		   case 2:
        			   old2=parent1.getoldrandomIntoperation2();
        			   replace2=parent1.getreplacerandomIntoperation2();
        			   secondold2=parent1.getsecondoldrandomIntoperation2();
        			   offSpring[0].setoldrandomIntoperation2(parent2.getoldrandomIntoperation2());
        			   offSpring[0].setreplacerandomIntoperation2(parent2.getreplacerandomIntoperation2());
        			   offSpring[0].setsecondoldrandomIntoperation2(parent2.getsecondoldrandomIntoperation2());
        			   offSpring[1].setoldrandomIntoperation2(old2);
        			   offSpring[1].setreplacerandomIntoperation2(replace2);
        			   offSpring[1].setsecondoldrandomIntoperation2(secondold2);
            		   break;
        		   case 3:
        			   old3=parent1.getoldrandomIntoperation3();
        			   replace3=parent1.getreplacerandomIntoperation3();
        			   secondold3=parent1.getsecondoldrandomIntoperation3();
        			   offSpring[0].setoldrandomIntoperation3(parent2.getoldrandomIntoperation3());
        			   offSpring[0].setreplacerandomIntoperation3(parent2.getreplacerandomIntoperation3());
        			   offSpring[0].setsecondoldrandomIntoperation3(parent2.getsecondoldrandomIntoperation3());
        			   offSpring[1].setoldrandomIntoperation3(old3);
        			   offSpring[1].setreplacerandomIntoperation3(replace3);
        			   offSpring[1].setsecondoldrandomIntoperation3(secondold3);
            		   break;
        		   case 4:
        			   old4=parent1.getoldrandomIntoperation4();
        			   replace4=parent1.getreplacerandomIntoperation4();
        			   secondold4=parent1.getsecondoldrandomIntoperation4();
        			   offSpring[0].setoldrandomIntoperation4(parent2.getoldrandomIntoperation4());
        			   offSpring[0].setreplacerandomIntoperation4(parent2.getreplacerandomIntoperation4());
        			   offSpring[0].setsecondoldrandomIntoperation4(parent2.getsecondoldrandomIntoperation4());
        			   offSpring[1].setoldrandomIntoperation4(old4);
        			   offSpring[1].setreplacerandomIntoperation4(replace4);
        			   offSpring[1].setsecondoldrandomIntoperation4(secondold4);
            		   break;
        		   case 5:
        			   old5=parent1.getoldrandomIntoperation5();
        			   replace5=parent1.getreplacerandomIntoperation5();
        			   secondold5=parent1.getsecondoldrandomIntoperation5();
        			   offSpring[0].setoldrandomIntoperation5(parent2.getoldrandomIntoperation5());
        			   offSpring[0].setreplacerandomIntoperation5(parent2.getreplacerandomIntoperation5());
        			   offSpring[0].setsecondoldrandomIntoperation5(parent2.getsecondoldrandomIntoperation5());
        			   offSpring[1].setoldrandomIntoperation5(old5);
        			   offSpring[1].setreplacerandomIntoperation5(replace5);
        			   offSpring[1].setsecondoldrandomIntoperation5(secondold5);
            		   break; 
        		   case 6:
        			   old2=parent1.getoldrandomIntoperation6();
        			   replace2=parent1.getreplacerandomIntoperation6();
        			   secondold2=parent1.getsecondoldrandomIntoperation6();
        			   offSpring[0].setoldrandomIntoperation6(parent2.getoldrandomIntoperation6());
        			   offSpring[0].setreplacerandomIntoperation6(parent2.getreplacerandomIntoperation6());
        			   offSpring[0].setsecondoldrandomIntoperation6(parent2.getsecondoldrandomIntoperation6());
        			   offSpring[1].setoldrandomIntoperation6(old2);
        			   offSpring[1].setreplacerandomIntoperation6(replace2);
        			   offSpring[1].setsecondoldrandomIntoperation6(secondold2);
            		   break; 
        		   case 7:
        			   old2=parent1.getoldrandomIntoperation7();
        			   replace2=parent1.getreplacerandomIntoperation7();
        			   secondold2=parent1.getsecondoldrandomIntoperation7();
        			   offSpring[0].setoldrandomIntoperation7(parent2.getoldrandomIntoperation7());
        			   offSpring[0].setreplacerandomIntoperation7(parent2.getreplacerandomIntoperation7());
        			   offSpring[0].setsecondoldrandomIntoperation7(parent2.getsecondoldrandomIntoperation7());
        			   offSpring[1].setoldrandomIntoperation7(old2);
        			   offSpring[1].setreplacerandomIntoperation7(replace2);
        			   offSpring[1].setsecondoldrandomIntoperation7(secondold2);
            		   break; 
        		   case 8:
        			   old2=parent1.getoldrandomIntoperation8();
        			   replace2=parent1.getreplacerandomIntoperation8();
        			   secondold2=parent1.getsecondoldrandomIntoperation8();
        			   offSpring[0].setoldrandomIntoperation8(parent2.getoldrandomIntoperation8());
        			   offSpring[0].setreplacerandomIntoperation8(parent2.getreplacerandomIntoperation8());
        			   offSpring[0].setsecondoldrandomIntoperation8(parent2.getsecondoldrandomIntoperation8());
        			   offSpring[1].setoldrandomIntoperation8(old2);
        			   offSpring[1].setreplacerandomIntoperation8(replace2);
        			   offSpring[1].setsecondoldrandomIntoperation8(secondold2);
            		   break; 
            		   
        		   case 9:
        			   old2=parent1.getoldrandomIntoperation9();
        			   replace2=parent1.getreplacerandomIntoperation9();
        			   secondold2=parent1.getsecondoldrandomIntoperation9();
        			   offSpring[0].setoldrandomIntoperation9(parent2.getoldrandomIntoperation9());
        			   offSpring[0].setreplacerandomIntoperation9(parent2.getreplacerandomIntoperation9());
        			   offSpring[0].setsecondoldrandomIntoperation9(parent2.getsecondoldrandomIntoperation9());
        			   offSpring[1].setoldrandomIntoperation9(old2);
        			   offSpring[1].setreplacerandomIntoperation9(replace2);
        			   offSpring[1].setsecondoldrandomIntoperation9(secondold2);
            		   
            		   break; 

        		   case 10:
        			   old2=parent1.getoldrandomIntoperation10();
        			   replace2=parent1.getreplacerandomIntoperation10();
        			   secondold2=parent1.getsecondoldrandomIntoperation10();
        			   offSpring[0].setoldrandomIntoperation10(parent2.getoldrandomIntoperation10());
        			   offSpring[0].setreplacerandomIntoperation10(parent2.getreplacerandomIntoperation10());
        			   offSpring[0].setsecondoldrandomIntoperation10(parent2.getsecondoldrandomIntoperation10());
        			   offSpring[1].setoldrandomIntoperation10(old2);
        			   offSpring[1].setreplacerandomIntoperation10(replace2);
        			   offSpring[1].setsecondoldrandomIntoperation10(secondold2);
            		   break; 
        		   case 11:
        			   old2=parent1.getoldrandomIntoperation11();
        			   replace2=parent1.getreplacerandomIntoperation11();
        			   secondold2=parent1.getsecondoldrandomIntoperation11();
        			   offSpring[0].setoldrandomIntoperation11(parent2.getoldrandomIntoperation11());
        			   offSpring[0].setreplacerandomIntoperation11(parent2.getreplacerandomIntoperation11());
        			   offSpring[0].setsecondoldrandomIntoperation11(parent2.getsecondoldrandomIntoperation11());
        			   offSpring[1].setoldrandomIntoperation11(old2);
        			   offSpring[1].setreplacerandomIntoperation11(replace2);
        			   offSpring[1].setsecondoldrandomIntoperation11(secondold2);
            		   break; 
        		   case 12:
        			   old2=parent1.getoldrandomIntoperation12();
        			   replace2=parent1.getreplacerandomIntoperation12();
        			   secondold2=parent1.getsecondoldrandomIntoperation12();
        			   offSpring[0].setoldrandomIntoperation12(parent2.getoldrandomIntoperation12());
        			   offSpring[0].setreplacerandomIntoperation12(parent2.getreplacerandomIntoperation12());
        			   offSpring[0].setsecondoldrandomIntoperation12(parent2.getsecondoldrandomIntoperation12());
        			   offSpring[1].setoldrandomIntoperation12(old2);
        			   offSpring[1].setreplacerandomIntoperation12(replace2);
        			   offSpring[1].setsecondoldrandomIntoperation12(secondold2);
            		   break; 
        		   case 13:
        			   old2=parent1.getoldrandomIntoperation13();
        			   replace2=parent1.getreplacerandomIntoperation13();
        			   secondold2=parent1.getsecondoldrandomIntoperation13();
        			   offSpring[0].setoldrandomIntoperation13(parent2.getoldrandomIntoperation13());
        			   offSpring[0].setreplacerandomIntoperation13(parent2.getreplacerandomIntoperation13());
        			   offSpring[0].setsecondoldrandomIntoperation13(parent2.getsecondoldrandomIntoperation13());
        			   offSpring[1].setoldrandomIntoperation13(old2);
        			   offSpring[1].setreplacerandomIntoperation13(replace2);
        			   offSpring[1].setsecondoldrandomIntoperation13(secondold2);
            		   break; 
        		   case 14:
        			   old2=parent1.getoldrandomIntoperation14();
        			   replace2=parent1.getreplacerandomIntoperation14();
        			   secondold2=parent1.getsecondoldrandomIntoperation14();
        			   offSpring[0].setoldrandomIntoperation14(parent2.getoldrandomIntoperation14());
        			   offSpring[0].setreplacerandomIntoperation14(parent2.getreplacerandomIntoperation14());
        			   offSpring[0].setsecondoldrandomIntoperation14(parent2.getsecondoldrandomIntoperation14());
        			   offSpring[1].setoldrandomIntoperation14(old2);
        			   offSpring[1].setreplacerandomIntoperation14(replace2);
        			   offSpring[1].setsecondoldrandomIntoperation14(secondold2);
            		   break; 
        		   case 15:
        			   old5=parent1.getoldrandomIntoperation15();
        			   replace5=parent1.getreplacerandomIntoperation15();
        			   secondold5=parent1.getsecondoldrandomIntoperation15();
        			   offSpring[0].setoldrandomIntoperation15(parent2.getoldrandomIntoperation15());
        			   offSpring[0].setreplacerandomIntoperation15(parent2.getreplacerandomIntoperation15());
        			   offSpring[0].setsecondoldrandomIntoperation15(parent2.getsecondoldrandomIntoperation15());
        			   offSpring[1].setoldrandomIntoperation15(old5);
        			   offSpring[1].setreplacerandomIntoperation15(replace5);
        			   offSpring[1].setsecondoldrandomIntoperation15(secondold5);
            		   break; 
        		   case 16:
        			   old2=parent1.getoldrandomIntoperation16();
        			   replace2=parent1.getreplacerandomIntoperation16();
        			   secondold2=parent1.getsecondoldrandomIntoperation16();
        			   offSpring[0].setoldrandomIntoperation16(parent2.getoldrandomIntoperation16());
        			   offSpring[0].setreplacerandomIntoperation16(parent2.getreplacerandomIntoperation16());
        			   offSpring[0].setsecondoldrandomIntoperation16(parent2.getsecondoldrandomIntoperation16());
        			   offSpring[1].setoldrandomIntoperation16(old2);
        			   offSpring[1].setreplacerandomIntoperation16(replace2);
        			   offSpring[1].setsecondoldrandomIntoperation16(secondold2);
            		   break; 
        		   case 17:
        			   old2=parent1.getoldrandomIntoperation17();
        			   replace2=parent1.getreplacerandomIntoperation17();
        			   secondold2=parent1.getsecondoldrandomIntoperation17();
        			   offSpring[0].setoldrandomIntoperation17(parent2.getoldrandomIntoperation17());
        			   offSpring[0].setreplacerandomIntoperation17(parent2.getreplacerandomIntoperation17());
        			   offSpring[0].setsecondoldrandomIntoperation17(parent2.getsecondoldrandomIntoperation17());
        			   offSpring[1].setoldrandomIntoperation17(old2);
        			   offSpring[1].setreplacerandomIntoperation17(replace2);
        			   offSpring[1].setsecondoldrandomIntoperation17(secondold2);
            		   break; 
        		   case 18:
        			   old2=parent1.getoldrandomIntoperation18();
        			   replace2=parent1.getreplacerandomIntoperation18();
        			   secondold2=parent1.getsecondoldrandomIntoperation18();
        			   offSpring[0].setoldrandomIntoperation18(parent2.getoldrandomIntoperation18());
        			   offSpring[0].setreplacerandomIntoperation18(parent2.getreplacerandomIntoperation18());
        			   offSpring[0].setsecondoldrandomIntoperation18(parent2.getsecondoldrandomIntoperation18());
        			   offSpring[1].setoldrandomIntoperation18(old2);
        			   offSpring[1].setreplacerandomIntoperation18(replace2);
        			   offSpring[1].setsecondoldrandomIntoperation18(secondold2);
            		   break; 
            		   
        		   case 19:
        			   old2=parent1.getoldrandomIntoperation19();
        			   replace2=parent1.getreplacerandomIntoperation19();
        			   secondold2=parent1.getsecondoldrandomIntoperation19();
        			   offSpring[0].setoldrandomIntoperation19(parent2.getoldrandomIntoperation19());
        			   offSpring[0].setreplacerandomIntoperation19(parent2.getreplacerandomIntoperation19());
        			   offSpring[0].setsecondoldrandomIntoperation19(parent2.getsecondoldrandomIntoperation19());
        			   offSpring[1].setoldrandomIntoperation19(old2);
        			   offSpring[1].setreplacerandomIntoperation19(replace2);
        			   offSpring[1].setsecondoldrandomIntoperation19(secondold2);
            		   
            		   break; 

        		   case 20:
        			   old2=parent1.getoldrandomIntoperation20();
        			   replace2=parent1.getreplacerandomIntoperation20();
        			   secondold2=parent1.getsecondoldrandomIntoperation20();
        			   offSpring[0].setoldrandomIntoperation20(parent2.getoldrandomIntoperation20());
        			   offSpring[0].setreplacerandomIntoperation20(parent2.getreplacerandomIntoperation20());
        			   offSpring[0].setsecondoldrandomIntoperation20(parent2.getsecondoldrandomIntoperation20());
        			   offSpring[1].setoldrandomIntoperation20(old2);
        			   offSpring[1].setreplacerandomIntoperation20(replace2);
        			   offSpring[1].setsecondoldrandomIntoperation20(secondold2);
            		   break; 
        		   case 21:
        			   old2=parent1.getoldrandomIntoperation21();
        			   replace2=parent1.getreplacerandomIntoperation21();
        			   secondold2=parent1.getsecondoldrandomIntoperation21();
        			   offSpring[0].setoldrandomIntoperation21(parent2.getoldrandomIntoperation21());
        			   offSpring[0].setreplacerandomIntoperation21(parent2.getreplacerandomIntoperation21());
        			   offSpring[0].setsecondoldrandomIntoperation21(parent2.getsecondoldrandomIntoperation21());
        			   offSpring[1].setoldrandomIntoperation21(old2);
        			   offSpring[1].setreplacerandomIntoperation21(replace2);
        			   offSpring[1].setsecondoldrandomIntoperation21(secondold2);
            		   break; 
        		   case 22:
        			   old2=parent1.getoldrandomIntoperation22();
        			   replace2=parent1.getreplacerandomIntoperation22();
        			   secondold2=parent1.getsecondoldrandomIntoperation22();
        			   offSpring[0].setoldrandomIntoperation22(parent2.getoldrandomIntoperation22());
        			   offSpring[0].setreplacerandomIntoperation22(parent2.getreplacerandomIntoperation22());
        			   offSpring[0].setsecondoldrandomIntoperation22(parent2.getsecondoldrandomIntoperation22());
        			   offSpring[1].setoldrandomIntoperation22(old2);
        			   offSpring[1].setreplacerandomIntoperation22(replace2);
        			   offSpring[1].setsecondoldrandomIntoperation22(secondold2);
            		   break; 



        		   }
        		   
        	
        	//   }
        	   //System.out.println("offSpring 0 : "+offSpring[0].rule().get(i).rule_text);
        	   //System.out.println("offSpring 1 : "+offSpring[1].rule().get(i).rule_text);
             } // for
         //  System.out.println("remove tekrari");
         //  System.out.println(parent1.getoperations());
           
          
        //   System.out.println(offSpring[0].getoperations());
       //    System.out.println(offSpring[1].getoperations());
           boolean checkcross=false;
           int j=crossoverPoint;
           for (int i = crossoverPoint; i < offSpring[0].operation().size(); i++) {
        	   checkcross=false;
        	   switch(i+1) {
      		   case 1:
      			   offSpring[0].setoldrandomIntoperation1(parent1.getoldrandomIntoperation1());
      			   offSpring[0].setreplacerandomIntoperation1(parent1.getreplacerandomIntoperation1());
      			 offSpring[0].setsecondoldrandomIntoperation1(parent1.getsecondoldrandomIntoperation1());
          		   break;
      		   case 2:
      			   offSpring[0].setoldrandomIntoperation2(parent1.getoldrandomIntoperation2());
      			   offSpring[0].setreplacerandomIntoperation2(parent1.getreplacerandomIntoperation2());
      			 offSpring[0].setsecondoldrandomIntoperation2(parent1.getsecondoldrandomIntoperation2());
          		   break;
      		   case 3:
      			   offSpring[0].setoldrandomIntoperation3(parent1.getoldrandomIntoperation3());
      			   offSpring[0].setreplacerandomIntoperation3(parent1.getreplacerandomIntoperation3());
      			 offSpring[0].setsecondoldrandomIntoperation3(parent1.getsecondoldrandomIntoperation3());
          		   break;
      		   case 4:
      			   offSpring[0].setoldrandomIntoperation4(parent1.getoldrandomIntoperation4());
      			   offSpring[0].setreplacerandomIntoperation4(parent1.getreplacerandomIntoperation4());
      			 offSpring[0].setsecondoldrandomIntoperation4(parent1.getsecondoldrandomIntoperation4());
          		   break;
      		   case 5:
      			   offSpring[0].setoldrandomIntoperation5(parent1.getoldrandomIntoperation5());
      			   offSpring[0].setreplacerandomIntoperation5(parent1.getreplacerandomIntoperation5());
      			 offSpring[0].setsecondoldrandomIntoperation5(parent1.getsecondoldrandomIntoperation5());
          		   break; 
      		 case 6:
    			   offSpring[0].setoldrandomIntoperation6(parent1.getoldrandomIntoperation6());
    			   offSpring[0].setreplacerandomIntoperation6(parent1.getreplacerandomIntoperation6());
    			   offSpring[0].setsecondoldrandomIntoperation6(parent1.getsecondoldrandomIntoperation6());
        		   break; 
      		 case 7:
  			   offSpring[0].setoldrandomIntoperation7(parent1.getoldrandomIntoperation7());
  			   offSpring[0].setreplacerandomIntoperation7(parent1.getreplacerandomIntoperation7());
  			 offSpring[0].setsecondoldrandomIntoperation7(parent1.getsecondoldrandomIntoperation7());
      		   break; 
      		 case 8:
  			   offSpring[0].setoldrandomIntoperation8(parent1.getoldrandomIntoperation8());
  			   offSpring[0].setreplacerandomIntoperation8(parent1.getreplacerandomIntoperation8());
  			 offSpring[0].setsecondoldrandomIntoperation8(parent1.getsecondoldrandomIntoperation8());
      		   break; 
      		 case 9:
  			   offSpring[0].setoldrandomIntoperation9(parent1.getoldrandomIntoperation9());
  			   offSpring[0].setreplacerandomIntoperation9(parent1.getreplacerandomIntoperation9());
  			 offSpring[0].setsecondoldrandomIntoperation9(parent1.getsecondoldrandomIntoperation9());
      		   break; 
    		
      		 case 10:
  			   offSpring[0].setoldrandomIntoperation10(parent1.getoldrandomIntoperation10());
  			   offSpring[0].setreplacerandomIntoperation10(parent1.getreplacerandomIntoperation10());
  			 offSpring[0].setsecondoldrandomIntoperation10(parent1.getsecondoldrandomIntoperation10());
      		   break; 
      		case 11:
   			   offSpring[0].setoldrandomIntoperation11(parent1.getoldrandomIntoperation11());
   			   offSpring[0].setreplacerandomIntoperation11(parent1.getreplacerandomIntoperation11());
   			 offSpring[0].setsecondoldrandomIntoperation11(parent1.getsecondoldrandomIntoperation11());
       		   break; 
      		case 12:
   			   offSpring[0].setoldrandomIntoperation12(parent1.getoldrandomIntoperation12());
   			   offSpring[0].setreplacerandomIntoperation12(parent1.getreplacerandomIntoperation12());
   			 offSpring[0].setsecondoldrandomIntoperation12(parent1.getsecondoldrandomIntoperation12());
       		   break; 
      		case 13:
    			   offSpring[0].setoldrandomIntoperation13(parent1.getoldrandomIntoperation13());
    			   offSpring[0].setreplacerandomIntoperation13(parent1.getreplacerandomIntoperation13());
    			 offSpring[0].setsecondoldrandomIntoperation13(parent1.getsecondoldrandomIntoperation13());
        		   break;
      		case 14:
    			   offSpring[0].setoldrandomIntoperation14(parent1.getoldrandomIntoperation14());
    			   offSpring[0].setreplacerandomIntoperation14(parent1.getreplacerandomIntoperation14());
    			 offSpring[0].setsecondoldrandomIntoperation14(parent1.getsecondoldrandomIntoperation14());
        		   break; 
     		
     	   case 15:
  			   offSpring[0].setoldrandomIntoperation15(parent1.getoldrandomIntoperation15());
  			   offSpring[0].setreplacerandomIntoperation15(parent1.getreplacerandomIntoperation15());
  			 offSpring[0].setsecondoldrandomIntoperation15(parent1.getsecondoldrandomIntoperation15());
      		   break; 
  		 case 16:
			   offSpring[0].setoldrandomIntoperation16(parent1.getoldrandomIntoperation16());
			   offSpring[0].setreplacerandomIntoperation16(parent1.getreplacerandomIntoperation16());
			   offSpring[0].setsecondoldrandomIntoperation16(parent1.getsecondoldrandomIntoperation16());
    		   break; 
  		 case 17:
			   offSpring[0].setoldrandomIntoperation17(parent1.getoldrandomIntoperation17());
			   offSpring[0].setreplacerandomIntoperation17(parent1.getreplacerandomIntoperation17());
			 offSpring[0].setsecondoldrandomIntoperation17(parent1.getsecondoldrandomIntoperation17());
  		   break; 
  		 case 18:
			   offSpring[0].setoldrandomIntoperation18(parent1.getoldrandomIntoperation18());
			   offSpring[0].setreplacerandomIntoperation18(parent1.getreplacerandomIntoperation18());
			 offSpring[0].setsecondoldrandomIntoperation18(parent1.getsecondoldrandomIntoperation18());
  		   break; 
  		 case 19:
			   offSpring[0].setoldrandomIntoperation19(parent1.getoldrandomIntoperation19());
			   offSpring[0].setreplacerandomIntoperation19(parent1.getreplacerandomIntoperation19());
			 offSpring[0].setsecondoldrandomIntoperation19(parent1.getsecondoldrandomIntoperation19());
  		   break; 
		
  		 case 20:
			   offSpring[0].setoldrandomIntoperation20(parent1.getoldrandomIntoperation20());
			   offSpring[0].setreplacerandomIntoperation20(parent1.getreplacerandomIntoperation20());
			 offSpring[0].setsecondoldrandomIntoperation20(parent1.getsecondoldrandomIntoperation20());
  		   break; 
  		case 21:
			   offSpring[0].setoldrandomIntoperation21(parent1.getoldrandomIntoperation21());
			   offSpring[0].setreplacerandomIntoperation21(parent1.getreplacerandomIntoperation21());
			 offSpring[0].setsecondoldrandomIntoperation21(parent1.getsecondoldrandomIntoperation21());
   		   break; 
  		case 22:
			   offSpring[0].setoldrandomIntoperation22(parent1.getoldrandomIntoperation22());
			   offSpring[0].setreplacerandomIntoperation22(parent1.getreplacerandomIntoperation22());
			 offSpring[0].setsecondoldrandomIntoperation22(parent1.getsecondoldrandomIntoperation22());
   		   break; 
  	
    		
      		   }
      		
        	
        		      
           }
           checkcross=false;
            j=crossoverPoint;
           for (int i = crossoverPoint; i < offSpring[1].operation().size(); i++) {
        	   checkcross=false;
        	   switch(i+1) {
      		   case 1:
      			   offSpring[1].setoldrandomIntoperation1(parent2.getoldrandomIntoperation1());
      			   offSpring[1].setreplacerandomIntoperation1(parent2.getreplacerandomIntoperation1());
      			 offSpring[1].setsecondoldrandomIntoperation1(parent2.getsecondoldrandomIntoperation1());
          		   break;
      		   case 2:
      			   offSpring[1].setoldrandomIntoperation2(parent2.getoldrandomIntoperation2());
      			   offSpring[1].setreplacerandomIntoperation2(parent2.getreplacerandomIntoperation2());
      			 offSpring[1].setsecondoldrandomIntoperation2(parent2.getsecondoldrandomIntoperation2());
          		   break;
      		   case 3:
      			   offSpring[1].setoldrandomIntoperation3(parent2.getoldrandomIntoperation3());
      			   offSpring[1].setreplacerandomIntoperation3(parent2.getreplacerandomIntoperation3());
      			 offSpring[1].setsecondoldrandomIntoperation3(parent2.getsecondoldrandomIntoperation3());
          		   break;
      		   case 4:
      			   offSpring[1].setoldrandomIntoperation4(parent2.getoldrandomIntoperation4());
      			   offSpring[1].setreplacerandomIntoperation4(parent2.getreplacerandomIntoperation4());
      			 offSpring[1].setsecondoldrandomIntoperation4(parent2.getsecondoldrandomIntoperation4());
          		   break;
      		   case 5:
      			   offSpring[1].setoldrandomIntoperation5(parent2.getoldrandomIntoperation5());
      			   offSpring[1].setreplacerandomIntoperation5(parent2.getreplacerandomIntoperation5());
      			 offSpring[1].setsecondoldrandomIntoperation5(parent2.getsecondoldrandomIntoperation5());
          		   break; 
      		 case 6:
    			   offSpring[1].setoldrandomIntoperation6(parent2.getoldrandomIntoperation6());
    			   offSpring[1].setreplacerandomIntoperation6(parent2.getreplacerandomIntoperation6());
    			   offSpring[1].setsecondoldrandomIntoperation6(parent2.getsecondoldrandomIntoperation6());
        		   break; 
      		case 7:
 			   offSpring[1].setoldrandomIntoperation7(parent2.getoldrandomIntoperation7());
 			   offSpring[1].setreplacerandomIntoperation7(parent2.getreplacerandomIntoperation7());
 			  offSpring[1].setsecondoldrandomIntoperation7(parent2.getsecondoldrandomIntoperation7());
     		   break; 
      		case 8:
 			   offSpring[1].setoldrandomIntoperation8(parent2.getoldrandomIntoperation8());
 			   offSpring[1].setreplacerandomIntoperation8(parent2.getreplacerandomIntoperation8());
 			  offSpring[1].setsecondoldrandomIntoperation8(parent2.getsecondoldrandomIntoperation8());
     		   break; 
      		case 9:
 			   offSpring[1].setoldrandomIntoperation9(parent2.getoldrandomIntoperation9());
 			   offSpring[1].setreplacerandomIntoperation9(parent2.getreplacerandomIntoperation9());
 			  offSpring[1].setsecondoldrandomIntoperation9(parent2.getsecondoldrandomIntoperation9());
     		   break; 
      		case 10:
 			   offSpring[1].setoldrandomIntoperation10(parent2.getoldrandomIntoperation10());
 			   offSpring[1].setreplacerandomIntoperation10(parent2.getreplacerandomIntoperation10());
 			  offSpring[1].setsecondoldrandomIntoperation10(parent2.getsecondoldrandomIntoperation10());
     		   break; 
   		
      		case 11:
  			   offSpring[1].setoldrandomIntoperation11(parent2.getoldrandomIntoperation11());
  			   offSpring[1].setreplacerandomIntoperation11(parent2.getreplacerandomIntoperation11());
  			 offSpring[1].setsecondoldrandomIntoperation11(parent2.getsecondoldrandomIntoperation11());
      		   break; 
      		case 12:
  			   offSpring[1].setoldrandomIntoperation12(parent2.getoldrandomIntoperation12());
  			   offSpring[1].setreplacerandomIntoperation12(parent2.getreplacerandomIntoperation12());
  			 offSpring[1].setsecondoldrandomIntoperation12(parent2.getsecondoldrandomIntoperation12());
      		   break; 
   		
      		case 13:
   			   offSpring[1].setoldrandomIntoperation13(parent2.getoldrandomIntoperation13());
   			   offSpring[1].setreplacerandomIntoperation13(parent2.getreplacerandomIntoperation13());
   			 offSpring[1].setsecondoldrandomIntoperation13(parent2.getsecondoldrandomIntoperation13());
       		   break;
      		case 14:
   			   offSpring[1].setoldrandomIntoperation14(parent2.getoldrandomIntoperation14());
   			   offSpring[1].setreplacerandomIntoperation14(parent2.getreplacerandomIntoperation14());
   			 offSpring[1].setsecondoldrandomIntoperation14(parent2.getsecondoldrandomIntoperation14());
       		   break; 
      	   case 15:
  			   offSpring[1].setoldrandomIntoperation15(parent2.getoldrandomIntoperation15());
  			   offSpring[1].setreplacerandomIntoperation15(parent2.getreplacerandomIntoperation15());
  			 offSpring[1].setsecondoldrandomIntoperation15(parent2.getsecondoldrandomIntoperation15());
      		   break; 
  		 case 16:
			   offSpring[1].setoldrandomIntoperation16(parent2.getoldrandomIntoperation16());
			   offSpring[1].setreplacerandomIntoperation16(parent2.getreplacerandomIntoperation16());
			   offSpring[1].setsecondoldrandomIntoperation16(parent2.getsecondoldrandomIntoperation16());
    		   break; 
  		case 17:
			   offSpring[1].setoldrandomIntoperation17(parent2.getoldrandomIntoperation17());
			   offSpring[1].setreplacerandomIntoperation17(parent2.getreplacerandomIntoperation17());
			  offSpring[1].setsecondoldrandomIntoperation17(parent2.getsecondoldrandomIntoperation17());
 		   break; 
  		case 18:
			   offSpring[1].setoldrandomIntoperation18(parent2.getoldrandomIntoperation18());
			   offSpring[1].setreplacerandomIntoperation18(parent2.getreplacerandomIntoperation18());
			  offSpring[1].setsecondoldrandomIntoperation18(parent2.getsecondoldrandomIntoperation18());
 		   break; 
  		case 19:
			   offSpring[1].setoldrandomIntoperation19(parent2.getoldrandomIntoperation19());
			   offSpring[1].setreplacerandomIntoperation19(parent2.getreplacerandomIntoperation19());
			  offSpring[1].setsecondoldrandomIntoperation19(parent2.getsecondoldrandomIntoperation19());
 		   break; 
  		case 20:
			   offSpring[1].setoldrandomIntoperation20(parent2.getoldrandomIntoperation20());
			   offSpring[1].setreplacerandomIntoperation20(parent2.getreplacerandomIntoperation20());
			  offSpring[1].setsecondoldrandomIntoperation20(parent2.getsecondoldrandomIntoperation20());
 		   break; 
		
  		case 21:
			   offSpring[1].setoldrandomIntoperation21(parent2.getoldrandomIntoperation21());
			   offSpring[1].setreplacerandomIntoperation21(parent2.getreplacerandomIntoperation21());
			 offSpring[1].setsecondoldrandomIntoperation21(parent2.getsecondoldrandomIntoperation21());
  		   break; 
  		case 22:
			   offSpring[1].setoldrandomIntoperation22(parent2.getoldrandomIntoperation22());
			   offSpring[1].setreplacerandomIntoperation22(parent2.getreplacerandomIntoperation22());
			 offSpring[1].setsecondoldrandomIntoperation22(parent2.getsecondoldrandomIntoperation22());
  		   break; 
		
    		
      		   }
      	
        		      
           }
         //  System.out.println(offSpring[0].getoperations());
        //   System.out.println(offSpring[1].getoperations());
           /* for (int i = crossoverPoint; i < parent1.numberOfVariables(); i++) {
              parents1 =  parent1;
              parents2 =  parent2;
              
             
              offSpring[0]=parents1;
              offSpring[1]=parents2;*/

          /*int crossoverPoint = PseudoRandom.randInt(0, parent1.numberOfVariables() - 1);
          int valueX1;
          int valueX2;
          for (int i = crossoverPoint; i < parent1.numberOfVariables(); i++) {
            valueX1 = (int) parent1.getDecisionVariables()[i].getValue();
            valueX2 = (int) parent2.getDecisionVariables()[i].getValue();
            offSpring[0].getDecisionVariables()[i].setValue(valueX2);
            offSpring[1].getDecisionVariables()[i].setValue(valueX1);*/
          //} // for
            
        } // Int representation
      }
    } catch (ClassCastException e1) {
      Configuration.logger_.severe("SinglePointCrossover.doCrossover: Cannot perfom " +
              "SinglePointCrossover");
      Class cls = java.lang.String.class;
      String name = cls.getName();
      throw new JMException("Exception in " + name + ".doCrossover()");
    }
    return offSpring;
  } // doCrossover

  /**
   * Executes the operation
   * @param object An object containing an array of two solutions
   * @return An object containing an array with the offSprings
   * @throws JMException
   */
  public Object execute(Object object) throws JMException {
    Solution[] parents = (Solution[]) object;

    if (!(VALID_TYPES.contains(parents[0].getType().getClass())  &&
        VALID_TYPES.contains(parents[1].getType().getClass())) ) {

      Configuration.logger_.severe("SinglePointCrossover.execute: the solutions " +
              "are not of the right type. The type should be 'Binary' or 'Int', but " +
              parents[0].getType() + " and " +
              parents[1].getType() + " are obtained");

      Class cls = java.lang.String.class;
      String name = cls.getName();
      throw new JMException("Exception in " + name + ".execute()");
    } // if

    if (parents.length < 2) {
      Configuration.logger_.severe("SinglePointCrossover.execute: operator " +
              "needs two parents");
      Class cls = java.lang.String.class;
      String name = cls.getName();
      throw new JMException("Exception in " + name + ".execute()");
    } 
    
    Solution[] offSpring;
    offSpring = doCrossover(crossoverProbability_,
            parents[0],
            parents[1]);

    //-> Update the offSpring solutions
    for (int i = 0; i < offSpring.length; i++) {
      offSpring[i].setCrowdingDistance(0.0);
      offSpring[i].setRank(0);
    }
    return offSpring;
  } // execute
} // SinglePointCrossover
