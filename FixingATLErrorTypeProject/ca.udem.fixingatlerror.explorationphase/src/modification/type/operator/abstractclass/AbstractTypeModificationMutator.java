package modification.type.operator.abstractclass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

// REMARKS
// => any subtype of a class is compatible with the class
// => no class has incompatible supertypes
// => the information of the static analysis could be used to select an incompatible
//    class of interest, e.g. a class that does not define a feature used by the rule

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
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
import anatlyzer.atl.util.ATLUtils;

import witness.generator.MetaModel;
import anatlyzer.atlext.ATL.ATLFactory;
import anatlyzer.atlext.ATL.ATLPackage;
import anatlyzer.atlext.ATL.Binding;
import anatlyzer.atlext.ATL.ContextHelper;
import anatlyzer.atlext.ATL.ForEachOutPatternElement;
import anatlyzer.atlext.ATL.Helper;
import anatlyzer.atlext.ATL.InPattern;
import anatlyzer.atlext.ATL.InPatternElement;
import anatlyzer.atlext.ATL.LocatedElement;
import anatlyzer.atlext.ATL.MatchedRule;
import anatlyzer.atlext.ATL.Module;
import anatlyzer.atlext.ATL.OutPattern;
import anatlyzer.atlext.ATL.OutPatternElement;
import anatlyzer.atlext.ATL.StaticHelper;
import anatlyzer.atlext.OCL.Attribute;
import anatlyzer.atlext.OCL.CollectionType;
import anatlyzer.atlext.OCL.IteratorExp;
import anatlyzer.atlext.OCL.LoopExp;
import anatlyzer.atlext.OCL.NavigationOrAttributeCallExp;
import anatlyzer.atlext.OCL.OCLFactory;
import anatlyzer.atlext.OCL.OCLPackage;
import anatlyzer.atlext.OCL.OclContextDefinition;
import anatlyzer.atlext.OCL.OclExpression;
import anatlyzer.atlext.OCL.OclFeatureDefinition;
import anatlyzer.atlext.OCL.OclModelElement;
import anatlyzer.atlext.OCL.OclType;
import anatlyzer.atlext.OCL.Operation;
import anatlyzer.atlext.OCL.OperationCallExp;
import anatlyzer.atlext.OCL.Parameter;
import anatlyzer.atlext.OCL.PropertyCallExp;
import anatlyzer.atlext.OCL.VariableDeclaration;
import anatlyzer.atlext.OCL.VariableExp;
//import anatlyzer.evaluation.mutators.ATLModel;
import anatlyzer.atl.model.ATLModel;
import evaluation.mutator.AbstractMutator;
import deletion.operator.AbstractDeletionMutator;
//import anatlyzer.evaluation.tester.Tester;
import ca.udem.fixingatlerror.explorationphase.main;
import ca.udem.fixingatlerror.explorationphase.Operations;
import ca.udem.fixingatlerror.explorationphase.Setting;
import jmetal.core.Solution;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.problems.MyProblem;
import transML.exceptions.transException;
import anatlyzer.atl.types.Type;

public abstract class AbstractTypeModificationMutator extends AbstractMutator {

	/**
	 * Generic type modification. It allows subtypes of the container class.
	 * 
	 * @param atlModel
	 * @param outputFolder
	 * @param ToModifyClass
	 *            container class of the class of objects to modify (example
	 *            Parameter)
	 * @param featureName
	 *            feature to modify (example type)
	 * @param metamodel
	 *            metamodel containing the candidate types for the modification
	 */
	private String m = "C:/Users/varaminz/Downloads/Project/workspaceTEST34/anatlyzer.atl.tests.api/trafo2/zahra2mutants/finalresult"
			+ main.totalnumber + ".atl";
	private EMFModel atlModel3;
	private String m2 = "C:/Users/varaminz/Downloads/Project/workspaceTEST34/anatlyzer.atl.tests.api/trafo/PNML2PetriNet2.atl";
	private EMFModel atlModel4;

	protected <ToModify extends LocatedElement> List<Object> genericTypeModification(EMFModel atlModel, String outputFolder,
			Class<ToModify> ToModifyClass, String featureName, MetaModel[] metamodels, ATLModel wrapper,Solution solution,MetaModel metamodels1, MetaModel metamodels2) {
		return this.genericTypeModification(atlModel, outputFolder, ToModifyClass, featureName, metamodels, false,
				wrapper,solution,metamodels1,metamodels2);

	}

	/**
	 * Generic type modification (OclModelElement, CollectionType or PrimitiveType).
	 * 
	 * @param atlModel
	 * @param outputFolder
	 * @param ToModifyClass
	 *            container class of the class of objects to modify (example
	 *            Parameter)
	 * @param featureName
	 *            feature to modify (example type)
	 * @param metamodel
	 *            metamodel containing the candidate types for the modification
	 * @param exactToModifyType
	 */
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

	protected <ToModify extends LocatedElement> List<Object> genericTypeModification(EMFModel atlModel, String outputFolder,
			Class<ToModify> ToModifyClass, String featureName, MetaModel[] metamodels, boolean exactToModifyType,
			ATLModel wrapper,Solution solution,MetaModel metamodels1, MetaModel metamodels2) {
		List<Object> ReturnResult = new ArrayList<Object>();
		List<ToModify> modifiable = (List<ToModify>) wrapper.allObjectsOf(ToModifyClass);
		
	/*	List<Helper> helper = (List<Helper>) wrapper.allObjectsOf(Helper.class);
		anatlyzer.atlext.OCL.OclType	returnop=null;		
		List<OperationCallExp> op = (List<OperationCallExp>) wrapper.allObjectsOf(OperationCallExp.class);
		if ( op.get(29).eContainingFeature() == ATLPackage.Literals.BINDING__VALUE  ) {
			
			Binding b = (Binding) op.get(29).eContainer();
			returnop=(OclType) b.getLeftType();
		}
		System.out.println(returnop);
		
		Operation operation = OCLFactory.eINSTANCE.createOperation();
		operation.setName("zahra" );
		operation.setReturnType(  (returnop) );
		for (OclExpression argument : op.get(29).getArguments()) {
			Parameter parameter = OCLFactory.eINSTANCE.createParameter();
			parameter.setType   ( (OclType) (argument.getInferredType()) );
		//	parameter.setVarName(getNiceParameterName(argument, operation.getParameters()));
			operation.getParameters().add(parameter);
		}
			
		OclFeatureDefinition def = OCLFactory.eINSTANCE.createOclFeatureDefinition();
		def.setFeature (operation);
		StaticHelper newhelper = ATLFactory.eINSTANCE.createStaticHelper();
		newhelper.setDefinition(def);
		newhelper.setLocation(" 10:6-9:32");*/
		
		ReturnResult.add(wrapper);
		ReturnResult.add(atlModel);
//		NSGAII.writer.println("collectionlocation");
			
		if (modifiable.size() > 0) {

			if (exactToModifyType)
				filterSubtypes(modifiable, ToModifyClass);

			// we will add a comment to the module, documenting the mutation
			// Module module = AbstractDeletionMutator.getWrapper().getModule();
			Module module = wrapper.getModule();
			
			
			
			
	/*		String helper_name    = helper.get(4).getDefinition().getFeature() instanceof Operation?
	                ((Operation)helper.get(4).getDefinition().getFeature()).getName():
                    ((Attribute)helper.get(4).getDefinition().getFeature()).getName();
	                String helper_return  = helper.get(4).getDefinition().getFeature() instanceof Operation? 
			                toString(((Operation)helper.get(4).getDefinition().getFeature()).getReturnType()):
	                        toString(((Attribute)helper.get(4).getDefinition().getFeature()).getType());
			                String helper_parameter    = helper.get(4).getDefinition().getFeature() instanceof Operation?
			       				 (((Operation)helper.get(4).getDefinition().getFeature()).getParameters()).toString():
			       				                        ((Attribute)helper.get(4).getDefinition().getFeature()).getName();
			 
			       				 */

			EDataTypeEList<String> comments = null;
			if (module != null) {
				EStructuralFeature f = wrapper.source(module).eClass().getEStructuralFeature("commentsAfter");
				comments = (EDataTypeEList<String>) wrapper.source(module).eGet(f);
			}
			int randomInt = -2;
			boolean checkmutationapply = false;
			String comment = null;
			int count = -1;
			ArrayList<Integer> filterrule = new ArrayList<Integer>(); 
			while (checkmutationapply == false) {
				count = count + 1;
		//		NSGAII.writer.println("startwhile");
		//		System.out.println("inelement");
				List<Integer> Result = ReturnFirstIndex(randomInt, modifiable.size(), checkmutationapply, count,solution);
				randomInt = Result.get(0);
				if (Result.get(1) == 0)
					checkmutationapply = false;
				else {
					checkmutationapply = false;
				}

		//		NSGAII.writer.println("randomInt");
		//		NSGAII.writer.println(randomInt);
				if (randomInt == -2)
					checkmutationapply = true;
				else if(solution.getpreviousgeneration()==true ) {
			//		System.out.println("else1");
			//		System.out.println(randomInt);
					ReturnResult= OperationPreviousGeneration(randomInt,solution, atlModel,  metamodels,modifiable,wrapper,featureName,comments, ReturnResult,metamodels1, metamodels2);
					checkmutationapply = true;
				}
				else if (randomInt != -1 && randomInt != -2 && solution.getpreviousgeneration()==false) {
			//		System.out.println("else2");
			//		System.out.println(randomInt);
					// System.out.println(modifiable.get(randomInt).getLocation());
					String[] array = modifiable.get(randomInt).getLocation().split(":", 2);
				//	NSGAII.writer.println(Operations.statecase);
				//	NSGAII.writer.println(Integer.parseInt(array[0]));
					int indexrule = -1;
					boolean IsThereCollection = false;
					for (int j = 0; j < NSGAII.faultrule.size(); j++) {
						if (Integer.parseInt(array[0]) > NSGAII.faultrule.get(j)
								&& Integer.parseInt(array[0]) < NSGAII.finalrule.get(j))

							indexrule = j;

					}

					IsThereCollection=ReturnIsThereCollection(NSGAII.sequencelocation,NSGAII.ocliskineoflocation);
				//	NSGAII.writer.println("indexrule");
				//	NSGAII.writer.println(indexrule);
				//	NSGAII.writer.println(IsThereCollection);
				//	NSGAII.writer.println(NSGAII.errorrule);
				//	System.out.println(indexrule);
				//	System.out.println(NSGAII.errorrule);
				//	filterrule.add(indexrule);
					if (!NSGAII.errorrule.contains(indexrule) && IsThereCollection == false
							&& main.typeoperation.equals("collectionelement"))
						checkmutationapply = true;
					if (!NSGAII.errorrule.contains(indexrule) && IsThereCollection == false
							&& main.typeoperation.equals("argu"))
						checkmutationapply = true;
					// NSGAII.errorrule.contains(indexrule)
					boolean check=true;
					//if( NSGAII.locationfilter.contains(Integer.parseInt(array[0])) && Class2Rel.typeoperation.equals("argu"))
						//check=false;
					
						
					if (modifiable.size() > 0 && NSGAII.errorrule.contains(indexrule) && check==true) {
						
						EStructuralFeature featureDefinition = wrapper.source(modifiable.get(randomInt)).eClass()
								.getEStructuralFeature(featureName);
						
				//		NSGAII.writer.println(toString(featureDefinition));
				//		NSGAII.writer.println(featureDefinition.getUpperBound());
						//featureDefinition.getUpperBound() == 1
				//		NSGAII.writer.println("beforefeature");
						
						if (featureDefinition != null && featureDefinition.getUpperBound() == 1) {
					//		NSGAII.writer.println("afterfeature");
					//		NSGAII.writer.println(Operations.statecase);
							EObject object2modify_src = wrapper.source(modifiable.get(randomInt));
							EObject oldFeatureValue = (EObject) object2modify_src.eGet(featureDefinition);
						//	NSGAII.writer.println(toString(oldFeatureValue));
					//		if (oldFeatureValue != null) {
							boolean findreplace=false;
								List<EObject> replacements = this.replacements(atlModel, (EObject) oldFeatureValue,
										metamodels);
								
							
						//		if (replacements.size() > 0) {
									int randomInt2 = -2;
									while(findreplace==false) {
										
											randomInt2 = FindSecondIndex(randomInt2, replacements.size());
											
											List<String> liststringiniteration = new ArrayList<String>();
									
									if(Operations.statecase.equals("case6")) {
										//list hameye navigation ha ro begir 
										List<VariableExp> variables = (List<VariableExp>)wrapper.allObjectsOf(VariableExp.class);
										
										for(int i=0;i<variables.size();i++) {
										EObject navigationExpression = variables.get(i).eContainer();
										if (navigationExpression instanceof NavigationOrAttributeCallExp) {
											EStructuralFeature featureDefinition2 = wrapper.source(navigationExpression).eClass().getEStructuralFeature("name");
											Object object2modify_src2 = wrapper.source(navigationExpression).eGet(featureDefinition2);
											String[] array2 = variables.get(i).getLocation().split(":", 2);
											
											String type       = getType(navigationExpression, variables.get(i), metamodels1, metamodels2); 
											if(type!=null) {
												
											//list attribute ke ro khat iteration hast va toye in rule hast begir chon roye khat iteration hast pas cardinality * dare
											if(NSGAII.iterationcall.contains( Integer.parseInt(array2[0]))  && 
													Integer.parseInt(array2[0])< NSGAII.finalrule.get(indexrule)  && Integer.parseInt(array2[0])>NSGAII.faultrule.get(indexrule)  ) {
												liststringiniteration.add(object2modify_src2.toString() );
													
											}
											
											
											}
										}
										}
										
										
										
										
										if(NSGAII.inpatternhasfilter.get(indexrule)==1 ) {
											
											if(!toString(oldFeatureValue).equals(toString(replacements.get(randomInt2)))) {
												if(!NSGAII.inpatternstringlocation.contains(toString(replacements.get(randomInt2)) ))
													findreplace=true;
												else {
													int indexof=NSGAII.inpatternstringlocation.indexOf( toString(replacements.get(randomInt2)));
													
													if( NSGAII.inpatternhasfilter.get(indexof)==1)
													{
														
														findreplace=true;
													}
														
												}
												
											}
											
											
										}
										else {
											
											if (!toString(oldFeatureValue).equals(toString(replacements.get(randomInt2))) && !NSGAII.inpatternstringlocation.contains(toString(replacements.get(randomInt2)) ))
												findreplace=true;
											
											
										}
										
										// age attribute ro khate iterationha hast va age inpattern entekhab shode in attribute  cardinality 1 dare ye inpattern dige entekhab kon chon error mide
										if(liststringiniteration.size()>0) {
											// to arraye ke list classha hast esme inpattern ra toye list peyda kon ba indexesh
											int indexclassname=NSGAII.classnamestring.indexOf( toString(replacements.get(randomInt2)));
											
											for(int j=0;j<liststringiniteration.size();j++) {
											for(int i=NSGAII.classnamestartpoint.get(indexclassname);i<NSGAII.classnamestartpoint.get(indexclassname)+NSGAII.classnamelength.get(indexclassname);i++) {
												if(NSGAII.listinheritattributesourcemetamodel.get(i).getName().equals( liststringiniteration.get(j)) && NSGAII.listinheritattributesourcemetamodel.get(i).getUpperBound()==1 )
													{findreplace= false;
													
													
													}
											}
											
										}
											
											
										}
										
										
										
									}
									else {
									if (!toString(oldFeatureValue).equals(toString(replacements.get(randomInt2))))
										findreplace=true;
									}
									}
						//			NSGAII.writer.println("last");
						//			NSGAII.writer.println(toString(replacements.get(randomInt2)));
						///			NSGAII.writer.println("randomInt2");
						//			NSGAII.writer.println(randomInt2);
									copyFeatures(oldFeatureValue, replacements.get(randomInt2));
									StoreTwoIndex(randomInt, randomInt2);
									object2modify_src.eSet(featureDefinition, replacements.get(randomInt2));

								//	if (comments != null)
										comments.add("\n-- MUTATION \"" + this.getDescription() + "\" from "
												+ toString(modifiable.get(randomInt)) + ":" + toString(oldFeatureValue)
												+ " to " + toString(modifiable.get(randomInt)) + ":"
												+ toString(replacements.get(randomInt2)) + " (line "
												+ modifiable.get(randomInt).getLocation()
												+ " of original transformation)\n");
									comment = "\n-- MUTATION \"" + this.getDescription() + "\" from "
											+ toString(modifiable.get(randomInt)) + ":" + toString(oldFeatureValue)
											+ " to " + toString(modifiable.get(randomInt)) + ":"
											+ toString(replacements.get(randomInt2)) + " (line "
											+ modifiable.get(randomInt).getLocation()
											+ " of original transformation)\n";
									System.out.println("\n-- MUTATION \"" + this.getDescription() + "\" from "
											+ toString(modifiable.get(randomInt)) + ":" + toString(oldFeatureValue)
											+ " to " + toString(modifiable.get(randomInt)) + ":"
											+ toString(replacements.get(randomInt2)) + " (line "
											+ modifiable.get(randomInt).getLocation()
											+ " of original transformation)\n");
									NSGAII.numop = NSGAII.numop + 1;
									checkmutationapply = true;
									ReturnResult.set(0, wrapper);
									ReturnResult.set(1, atlModel);
									ReturnResult.add(comment);

							//	}

						//	}
						}

					else if (featureDefinition != null ) {
						// Operation Argument Type Modification for OCLISKindof
						
						
						boolean listlineofcode=false;
						if (Result.get(1) == 1 && (solution.listlineofcodes.contains(Integer.parseInt(array[0])) ||
								solution.listfilterdeletion.contains(Integer.parseInt(array[0]))) )
							checkmutationapply = false;
						  if(!solution.listlineofcodes.contains(Integer.parseInt(array[0])) &&  !solution.listfilterdeletion.contains(Integer.parseInt(array[0])) )
							listlineofcode=true;
				//		  System.out.println(solution.listfilterdeletion);
				//		  System.out.println(Integer.parseInt(array[0]));
					//	      NSGAII.writer.println("argumenttype");
					//	      NSGAII.writer.println(Integer.parseInt(array[0]));
					//	      NSGAII.writer.println(solution.listlineofcodes);
					//	      NSGAII.writer.println(solution.listfilterdeletion);
					//	      NSGAII.writer.println(listlineofcode);
						  
						      
						  /*    for(int p2=0;p2<NSGAII.errorrule.size();p2++)
						      { int occu=0;
	
						    	  for(int p=0;p<filterrule.size();p++)
						    		  
					            	 if(NSGAII.errorrule.get(p2)==filterrule.get(p))
					            		 occu=occu+1;
						    	  if(occu>0)
						    	   occurancerrule.add(occu);
						    	  
						      }
						      int countoccuranc=0;
						      for(int p2=0;p2<occurancerrule.size();p2++)
						    	  if(occurancerrule.get(p2)>=5)
						    		  countoccuranc=countoccuranc+1;
						      if(countoccuranc==occurancerrule.size())
						    	  checkmutationapply = true;*/
						//      System.out.println(occurancerrule);
					       /*     for(int p=0;p<NSGAII.errorrule.size();p++) {
					            	
					             if(!filterrule.contains(NSGAII.errorrule.get(p)))
					              filterrulecheck=true;
					            
					            }
					             if(filterrulecheck==false)
					              checkmutationapply = true;*/
						  /*    boolean countusing=false;
						      if(indexrule<NSGAII.preconditionlocation.size() && Operations.statecase.equals("case7") ) {
									if(NSGAII.preconditionlocation.get( indexrule ) > Integer.parseInt(array[0]))
									{
										countusing=true;
									}
									}*/
						      
						      
						      //NSGAII.lineoclerror.contains( Integer.parseInt(array[0]))
								if(listlineofcode==true   ) {
								List<EObject> value = (List<EObject>) wrapper.source(modifiable.get(randomInt))
										.eGet(featureDefinition);
								
						//		NSGAII.writer.println("value");
						//		NSGAII.writer.println(value);
                                if (value.size()==0)
                                	checkmutationapply = false;
								// for (int i=0; i<value.size(); i++) {
								if (value.size() > 0) {
									int randomInt3 = calculaterandomInt2(value);
							//		NSGAII.writer.println(randomInt3);
									// EObject oldFeatureValue = value.get(i);
									EObject oldFeatureValue2 = value.get(randomInt3);
							//		 System.out.println(solution.listfilterdeletion);
							//		  System.out.println(Integer.parseInt(array[0]));
									 
								//	  System.out.println( oldFeatureValue2.eClass().getEPackage().getEClassifier("OclModelElement").isInstance(oldFeatureValue2));
									if (!oldFeatureValue2.eClass().getEPackage().getEClassifier("OclModelElement").isInstance(oldFeatureValue2)) 
										checkmutationapply = false;
									if (oldFeatureValue2.eClass().getEPackage().getEClassifier("OclModelElement").isInstance(oldFeatureValue2)) 
									{
										
									//	NSGAII.writer.println("apply");
										
									
									List<EObject> replacements = this.replacements(atlModel,
											(EObject) oldFeatureValue2, metamodels);
									
									
								//	NSGAII.writer.println("replacement7");
								//	NSGAII.writer.println(replacements);
									
									boolean findreplace = false;
									//	if (replacements.size() > 0) {
										// System.out.println(toString(oldFeatureValue2));
										
										int randomInt4 = 0;
										while(findreplace==false) {
											
											
											 randomInt4 = calculaterandomInt(replacements);	
											
											
											if (!toString(oldFeatureValue2).equals(toString(replacements.get(randomInt4))))
												findreplace=true;
											}
										
										//System.out.println(randomInt4);
										NSGAII.argumentlist.add(String.valueOf(indexrule));
										NSGAII.argumentlist.add(toString(replacements.get(randomInt4)));
										setvariable(randomInt, randomInt4, randomInt3);
										copyFeatures(oldFeatureValue2, replacements.get(randomInt4));
								//		NSGAII.writer.println(Operations.statecase);
										value.set(randomInt3, replacements.get(randomInt4));
									//	if (comments != null)
											comments.add("\n-- MUTATION \"" + this.getDescription() + "\" from "
													+ toString(modifiable.get(randomInt)) + ":" + toString(oldFeatureValue2)
													+ " to " + toString(modifiable.get(randomInt)) + ":"
													+ toString(replacements.get(randomInt4)) + " (line "
													+ modifiable.get(randomInt).getLocation()
													+ " of original transformation)\n");
											System.out.println("\n-- MUTATION \"" + this.getDescription() + "\" from "
													+ toString(modifiable.get(randomInt)) + ":" + toString(oldFeatureValue2)
													+ " to " + toString(modifiable.get(randomInt)) + ":"
													+ toString(replacements.get(randomInt4)) + " (line "
													+ modifiable.get(randomInt).getLocation()
													+ " of original transformation)\n");
										comment = "\n-- MUTATION \"" + this.getDescription() + "\" from "
												+ toString(modifiable.get(randomInt)) + ":" + toString(oldFeatureValue2)
												+ " to " + toString(modifiable.get(randomInt)) + ":"
												+ toString(replacements.get(randomInt4)) + " (line "
												+ modifiable.get(randomInt).getLocation()
												+ " of original transformation)\n";
										NSGAII.numop = NSGAII.numop + 1;
										checkmutationapply = true;
										ReturnResult.set(0, wrapper);
										ReturnResult.set(1, atlModel);
										ReturnResult.add(comment);
									}

									//	}

								}	}
					}
					}
				}

			}
				
			
		}
		return ReturnResult;
		//return null;

	}
	

	private String getType(EObject container, VariableExp containee, MetaModel inputMM,
			MetaModel outputMM) {
		// TODO Auto-generated method stub
		
		EClassifier         c   = null;
		VariableDeclaration def = containee.getReferredVariable();
		
		// obtain type (classifier) of variable expression ..............................
		// case 1 -> in pattern element
		if(def!=null) {
		if (def instanceof InPatternElement) {
		
			c = inputMM.getEClassifier(def.getType().getName());
		//	System.out.println(c);
		}
		// case 2 -> for each out pattern element
		else if (def instanceof ForEachOutPatternElement) {
		//	System.out.println("2222222222222");
			def = ((ForEachOutPatternElement)def).getIterator();
			if (def.eContainer() instanceof OutPatternElement) {
				OutPatternElement element = (OutPatternElement)def.eContainer();
				if (element.getType() instanceof OclModelElement)
					c = outputMM.getEClassifier(((OclModelElement)element.getType()).getName());
			}
		}
		// case 3 -> iterator
		else if (def instanceof Iterator) {
		//	System.out.println("3333333333333");
			if (def.eContainer() instanceof LoopExp) {
				LoopExp  iterator = (LoopExp)def.eContainer();
				OclExpression exp = iterator.getSource();
				while (c==null && exp!=null) {
					if (exp instanceof OclModelElement)  {
						c   = inputMM.getEClassifier(((OclModelElement)exp).getName());
						exp = null;
					}
					else if (exp instanceof PropertyCallExp) {
						exp = ((PropertyCallExp)exp).getSource();
					}
					else if (exp instanceof VariableExp) {
						c = inputMM.getEClassifier(getType(container, (VariableExp)exp, inputMM, outputMM));
						exp = null;
					}
					else exp = null;
				}
			}
		//	System.out.println(c);
		}
		// case 4 -> variable declaration
		else {
			if (toString(def).equals("self")) {
				//System.out.println("4444444444");
				EObject helper = containee;
				while (helper!=null && !(helper instanceof Helper)) helper = helper.eContainer();
				if (helper instanceof Helper) {
					if (((Helper)helper).getDefinition().getContext_()!=null &&
						((Helper)helper).getDefinition().getContext_().getContext_()!=null &&
					    ((Helper)helper).getDefinition().getContext_().getContext_() instanceof OclModelElement)
						c = inputMM.getEClassifier(((OclModelElement)((Helper)helper).getDefinition().getContext_().getContext_()).getName());
				}
			}
			else if (((VariableDeclaration)def).getType() instanceof OclModelElement) {
				//System.out.println("5555555555");
				c = inputMM.getEClassifier(((VariableDeclaration)def).getType().getName());
			}
			else if (((VariableDeclaration)def).getType() instanceof CollectionType) {
				//System.out.println("6666666666");
				c = inputMM.getEClassifier( ((CollectionType)((VariableDeclaration)def).getType()).getElementType().getName());
			}
		}
			
		// obtain type (classifier) of container ........................................
		EObject  next = containee.eContainer();
		while (c!=null && next!=null && next!=container) {
			if (c instanceof EClass) {
				EStructuralFeature name      = next.eClass().getEStructuralFeature("name");
				EStructuralFeature feature   = null;
				if ( name != null ) {
					String             nameValue = next.eGet(name).toString();
					feature = ((EClass)c).getEStructuralFeature(nameValue);
				} else {
					System.out.println("Warning: " + next.eClass().getName() + " " + "with null name feature ");
				}
				if (feature!=null) {
					c    = feature.getEType();
					next = next.eContainer();
				}
				else next=null;
			}
		}
	}
		return c!=null? c.getName() : null;
		
	}

	private <ToModify extends LocatedElement> List<Object> OperationPreviousGeneration(int randomInt,Solution solution,EMFModel atlModel, MetaModel[] metamodels, List<ToModify> modifiable,ATLModel wrapper,String featureName
			,EDataTypeEList<String> comments,List<Object> ReturnResult,MetaModel metamodel1,MetaModel metamodel2) {
		// TODO Auto-generated method stub
		
		ToModify modifiable2=modifiable.get(randomInt);
		String[] array = modifiable2.getLocation().split(":", 2);
		if( Operations.statecase.equals("case7" )) {
		  if(!solution.listlineofcodes.contains(Integer.parseInt(array[0])) )
		  {
			  ReturnResult=ApplyFromPreviousCase7( randomInt, solution, atlModel,  metamodels,  modifiable2, wrapper, featureName
					, comments, ReturnResult);	
		  }
		  else {
			  setvariable(-2, -2, -2);
		  }
		}  
		else {
			 
				  ReturnResult=ApplyFromPreviousOtherCase( randomInt, solution, atlModel,  metamodels,  modifiable2, wrapper, featureName
						, comments, ReturnResult,metamodel1,metamodel2,array);	
			
			 
			}  
		  return ReturnResult;	
}
	private <ToModify extends LocatedElement> List<Object> ApplyFromPreviousOtherCase(int randomInt, Solution solution, EMFModel atlModel,
			MetaModel[] metamodels, ToModify modifiable2, ATLModel wrapper, String featureName,
			EDataTypeEList<String> comments, List<Object> ReturnResult, MetaModel metamodel1,MetaModel metamodel2,String[] array) {
	//	NSGAII.writer.println("after crossover case dovom");
		List<String> liststringiniteration = new ArrayList<String>();
		if(Operations.statecase.equals("case6" )){
		List<VariableExp> variables = (List<VariableExp>)wrapper.allObjectsOf(VariableExp.class);
		
		for(int i=0;i<variables.size();i++) {
		EObject navigationExpression = variables.get(i).eContainer();
		if (navigationExpression instanceof NavigationOrAttributeCallExp) {
			EStructuralFeature featureDefinition2 = wrapper.source(navigationExpression).eClass().getEStructuralFeature("name");
			Object object2modify_src2 = wrapper.source(navigationExpression).eGet(featureDefinition2);
			String[] array2 = variables.get(i).getLocation().split(":", 2);
			
			String type       = getType(navigationExpression, variables.get(i), metamodel1, metamodel2); 
			
			int indexrule = -1;
			for (int j = 0; j < NSGAII.faultrule.size(); j++) {
				if (Integer.parseInt(array[0]) > NSGAII.faultrule.get(j)
						&& Integer.parseInt(array[0]) < NSGAII.finalrule.get(j))

					indexrule = j;

			}
			if(type!=null) {
				
			//list attribute ke ro khat iteration hast va toye in rule hast begir chon roye khat iteration hast pas cardinality * dare
				if(NSGAII.iterationcall.contains( Integer.parseInt(array2[0]))  && 
						Integer.parseInt(array2[0])< NSGAII.finalrule.get(indexrule)  && Integer.parseInt(array2[0])>NSGAII.faultrule.get(indexrule)  ) {
					liststringiniteration.add(object2modify_src2.toString() );
						
				}
			
			
			}
			
			
			
		}
		}
	}
		String comment = null;
		EStructuralFeature featureDefinition = wrapper.source(modifiable2).eClass()
				.getEStructuralFeature(featureName);
		EObject object2modify_src = wrapper.source(modifiable2);
		EObject oldFeatureValue = (EObject) object2modify_src.eGet(featureDefinition);
		List<EObject> replacements = this.replacements(atlModel,
				(EObject) oldFeatureValue, metamodels);
		int randomInt2 = -2;
		randomInt2 = FindSecondIndex(randomInt2, replacements.size());
		boolean check=false;
		if(liststringiniteration.size()>0) {
			// to arraye ke list classha hast esme inpattern ra toye list peyda kon ba indexesh
			int indexclassname=NSGAII.classnamestring.indexOf( toString(replacements.get(randomInt2)));
			
			for(int j=0;j<liststringiniteration.size();j++) {
			for(int i=NSGAII.classnamestartpoint.get(indexclassname);i<NSGAII.classnamestartpoint.get(indexclassname)+NSGAII.classnamelength.get(indexclassname);i++) {
				if(NSGAII.listinheritattributesourcemetamodel.get(i).getName().equals( liststringiniteration.get(j)) && NSGAII.listinheritattributesourcemetamodel.get(i).getUpperBound()==1 )
					{ setvariable(-2, -2, -2);
					
						check=true;
					}
			}
			
		}
			
			
		}
		if(check==false) {
		if(!oldFeatureValue.toString().equals(replacements.get(randomInt2).toString()))
		{
		copyFeatures(oldFeatureValue, replacements.get(randomInt2));
		StoreTwoIndex(randomInt, randomInt2);
		object2modify_src.eSet(featureDefinition, replacements.get(randomInt2));
	//	NSGAII.writer.println(Operations.statecase);
	//	NSGAII.writer.println(randomInt);
	//	NSGAII.writer.println(randomInt2);
		
		comments.add("\n-- MUTATION \"" + this.getDescription() + "\" from "
				+ toString(modifiable2) + ":" + toString(oldFeatureValue)
				+ " to " + toString(modifiable2) + ":"
				+ toString(replacements.get(randomInt2)) + " (line "
				+ modifiable2.getLocation()
				+ " of original transformation)\n");
	comment = "\n-- MUTATION \"" + this.getDescription() + "\" from "
			+ toString(modifiable2) + ":" + toString(oldFeatureValue)
			+ " to " + toString(modifiable2) + ":"
			+ toString(replacements.get(randomInt2)) + " (line "
			+ modifiable2.getLocation()
			+ " of original transformation)\n";
	System.out.println("\n-- MUTATION \"" + this.getDescription() + "\" from "
			+ toString(modifiable2) + ":" + toString(oldFeatureValue)
			+ " to " + toString(modifiable2) + ":"
			+ toString(replacements.get(randomInt2)) + " (line "
			+ modifiable2.getLocation()
			+ " of original transformation)\n");
	
	NSGAII.numop = NSGAII.numop + 1;
	//	checkmutationapply = true;
	ReturnResult.set(0, wrapper);
	ReturnResult.set(1, atlModel);
	ReturnResult.add(comment);
		}else {
			StoreTwoIndex(randomInt, randomInt2);
		}
	}
		
		// TODO Auto-generated method stub
		return ReturnResult;
	}

	private <ToModify extends LocatedElement> List<Object> ApplyFromPreviousCase7(int randomInt,Solution solution,EMFModel atlModel, MetaModel[] metamodels, ToModify modifiable2,ATLModel wrapper,String featureName
			,EDataTypeEList<String> comments,List<Object> ReturnResult) {
		// TODO Auto-generated method stub
	//	NSGAII.writer.println("after crossover" );
	//	System.out.println("outargument");
		String comment = null;
		EStructuralFeature featureDefinition = wrapper.source(modifiable2).eClass()
				.getEStructuralFeature(featureName);
		List<EObject> value = (List<EObject>) wrapper.source(modifiable2)
				.eGet(featureDefinition);
		int randomInt3 = calculaterandomInt2(value);
		EObject oldFeatureValue2 = value.get(randomInt3);
	//	System.out.println(oldFeatureValue2.toString());
		List<EObject> replacements = this.replacements(atlModel,
				(EObject) oldFeatureValue2, metamodels);
	int randomInt4 = calculaterandomInt(replacements);
	//NSGAII.argumentlist.add(String.valueOf(indexrule));
	//NSGAII.argumentlist.add(toString(replacements.get(randomInt4)));
	setvariable(randomInt, randomInt4, randomInt3);
	if(!oldFeatureValue2.toString().equals( replacements.get(randomInt4).toString())) {
	copyFeatures(oldFeatureValue2, replacements.get(randomInt4));
//	NSGAII.writer.println(Operations.statecase);
//	NSGAII.writer.println(randomInt);
//	NSGAII.writer.println(randomInt4);
//	NSGAII.writer.println(randomInt3);
	value.set(randomInt3, replacements.get(randomInt4));
//	if (comments != null)
		comments.add("\n-- MUTATION \"" + this.getDescription() + "\" from "
				+ toString(modifiable2) + ":" + toString(oldFeatureValue2)
				+ " to " + toString(modifiable2) + ":"
				+ toString(replacements.get(randomInt4)) + " (line "
				+ modifiable2.getLocation()
				+ " of original transformation)\n");
	comment = "\n-- MUTATION \"" + this.getDescription() + "\" from "
			+ toString(modifiable2) + ":" + toString(oldFeatureValue2)
			+ " to " + toString(modifiable2) + ":"
			+ toString(replacements.get(randomInt4)) + " (line "
			+ modifiable2.getLocation()
			+ " of original transformation)\n";
	System.out.println( "\n-- MUTATION \"" + this.getDescription() + "\" from "
			+ toString(modifiable2) + ":" + toString(oldFeatureValue2)
			+ " to " + toString(modifiable2) + ":"
			+ toString(replacements.get(randomInt4)) + " (line "
			+ modifiable2.getLocation()
			+ " of original transformation)\n");
	NSGAII.numop = NSGAII.numop + 1;
	//		checkmutationapply = true;
	ReturnResult.set(0, wrapper);
	ReturnResult.set(1, atlModel);
	ReturnResult.add(comment);
	
	}
	 return ReturnResult;		
	}

	/*ELSE if 
	 * 
	 * List<EObject> value = (List<EObject>) wrapper.source(modifiable.get(randomInt))
									.eGet(featureDefinition);
							// int randomInt3=(int) (Math.random() * (value.size()));
							NSGAII.writer.println("second if");
							NSGAII.writer.println(value);

							// for (int i=0; i<value.size(); i++) {
							if (value.size() > 0) {
								int randomInt3 = calculaterandomInt2(value);
								NSGAII.writer.println(randomInt3);
								// EObject oldFeatureValue = value.get(i);
								EObject oldFeatureValue2 = value.get(randomInt3);
								List<EObject> replacements = this.replacements(atlModel,
										(EObject) oldFeatureValue2, metamodels);
								NSGAII.writer.println("replacement");
								NSGAII.writer.println(replacements);
								// System.out.println(replacements);
								boolean checkargu = false;
								if (replacements.size() > 0) {
									// System.out.println(toString(oldFeatureValue2));
									for (int i = 0; i < replacements.size(); i++) {
										// System.out.println(toString(replacements.get(i)));
										if (toString(oldFeatureValue2).equals(toString(replacements.get(i)))) {
											checkargu = true;
										}
										// System.out.println(checkargu);

									}
									
									int randomInt4 = calculaterandomInt(replacements);
									NSGAII.argumentlist.add(String.valueOf(indexrule));
									NSGAII.argumentlist.add(toString(replacements.get(randomInt4)));
									setvariable(randomInt, randomInt4, randomInt3);
									copyFeatures(oldFeatureValue2, replacements.get(randomInt4));
									NSGAII.writer.println("case2");
									NSGAII.writer.println(Operations.statecase);
									value.set(randomInt3, replacements.get(randomInt4));
									if (comments != null)
										comments.add("\n-- MUTATION \"" + this.getDescription() + "\" from "
												+ toString(modifiable.get(randomInt)) + ":" + toString(oldFeatureValue2)
												+ " to " + toString(modifiable.get(randomInt)) + ":"
												+ toString(replacements.get(randomInt4)) + " (line "
												+ modifiable.get(randomInt).getLocation()
												+ " of original transformation)\n");
									comment = "\n-- MUTATION \"" + this.getDescription() + "\" from "
											+ toString(modifiable.get(randomInt)) + ":" + toString(oldFeatureValue2)
											+ " to " + toString(modifiable.get(randomInt)) + ":"
											+ toString(replacements.get(randomInt4)) + " (line "
											+ modifiable.get(randomInt).getLocation()
											+ " of original transformation)\n";
									// this.save(this.atlModel3, outputFolder);
									this.save2(atlModel, outputFolder);
									this.save2(atlModel,
											"C:/Users/varaminz/Downloads/Project/workspaceTEST34/anatlyzer.atl.tests.api/trafo2/zahra2cover");
									NSGAII.numop = NSGAII.numop + 1;
									checkmutationapply = true;
									 wrapper.updateresource(atlModel.getResource());
							//		wrapper.setResource(atlModel.getResource());
									ReturnResult.set(0, wrapper);
									
								}

							}

	 */
	private Boolean ReturnIsThereCollection(List<Integer> collectionlocation, List<Integer> collectionlocation2) {
		// TODO Auto-generated method stub
		List<Integer> collectionrule = new ArrayList<Integer>();
		Boolean IsThereCollection = false;
		if (main.typeoperation.equals("collectionelement")) {
			for (int i = 0; i < collectionlocation.size(); i++)
				for (int j = 0; j < NSGAII.faultrule.size(); j++) {
					if (collectionlocation.get(i) > NSGAII.faultrule.get(j)
							&& collectionlocation.get(i) < NSGAII.finalrule.get(j))
						collectionrule.add(j);

				}
			for (int i = 0; i < collectionrule.size(); i++)
				if (NSGAII.errorrule.contains(collectionrule.get(i)))
					IsThereCollection = true;
		} else if (main.typeoperation.equals("argu")) {
			for (int i = 0; i < collectionlocation2.size(); i++)
				for (int j = 0; j < NSGAII.faultrule.size(); j++) {
					if (collectionlocation2.get(i) > NSGAII.faultrule.get(j)
							&& collectionlocation2.get(i) < NSGAII.finalrule.get(j))
						collectionrule.add(j);

				}
			for (int i = 0; i < collectionrule.size(); i++)
				if (NSGAII.errorrule.contains(collectionrule.get(i)))
					IsThereCollection = true;
		}

		return IsThereCollection;

	}

	private Object[] FindFaultyLocation() {
		// TODO Auto-generated method stub
		String stringSearch = "rule";
		String stringSearch2 = "}";
		String stringSearch3 = "Sequence";
		String stringSearch4 = ".oclIsKindOf";
		List<Integer> collectionlocation = new ArrayList<Integer>();
		List<Integer> collectionlocation2 = new ArrayList<Integer>();
		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new FileReader(
					"C:/Users/varaminz/Downloads/Project/workspaceTEST34/anatlyzer.atl.tests.api/trafo2/zahra2mutants/finalresult"
							+ main.totalnumber + ".atl"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NSGAII.faultrule.clear();
		NSGAII.finalrule.clear();
		int linecount = 0;
		int lastline = 0;
		String line;

		try {
			while ((line = bf.readLine()) != null) {
				linecount++;
				int indexfound = line.indexOf(stringSearch);
				int indexfound2 = line.indexOf(stringSearch2);
				int indexfound3 = line.indexOf(stringSearch3);
				int indexfound4 = line.indexOf(stringSearch4);
				if (indexfound > -1) {
					// System.out.println("Word is at position " + indexfound + " on line " +
					// linecount);
				//	NSGAII.writer.println("Word is at position " + indexfound + " on line " + linecount);
					NSGAII.faultrule.add(linecount);

				}
				if (indexfound2 > -1) {
					lastline = linecount;

				}
				if (indexfound3 > -1) {
					collectionlocation.add(linecount);

				}
				if (indexfound4 > -1) {
					collectionlocation2.add(linecount);
			//		NSGAII.writer.println(collectionlocation2);

				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 1; i < NSGAII.faultrule.size(); i++)
			NSGAII.finalrule.add(NSGAII.faultrule.get(i) - 2);
		NSGAII.finalrule.add(lastline);
		try {
			bf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Object[] { collectionlocation, collectionlocation2 };

	}

	private void StoreTwoIndex(int randomInt, int randomInt2) {
		// TODO Auto-generated method stub

		if (Operations.statecase.equals("case1") || Operations.statecase.equals("case6")
				|| Operations.statecase.equals("case4") || Operations.statecase.equals("case9")) {
			switch (MyProblem.indexoperation) {
			case 1:
				MyProblem.oldoperation1 = randomInt;
				MyProblem.replaceoperation1 = randomInt2;

				break;
			case 2:
				MyProblem.oldoperation2 = randomInt;
				MyProblem.replaceoperation2 = randomInt2;

				break;
			case 3:
				MyProblem.oldoperation3 = randomInt;
				MyProblem.replaceoperation3 = randomInt2;

				break;
			case 4:
				MyProblem.oldoperation4 = randomInt;
				MyProblem.replaceoperation4 = randomInt2;

				break;
			case 5:
				MyProblem.oldoperation5 = randomInt;
				MyProblem.replaceoperation5 = randomInt2;

				break;
			case 6:
				MyProblem.oldoperation6 = randomInt;
				MyProblem.replaceoperation6 = randomInt2;

				break;
			case 7:
				MyProblem.oldoperation7 = randomInt;
				MyProblem.replaceoperation7 = randomInt2;

				break;
			case 8:
				MyProblem.oldoperation8 = randomInt;
				MyProblem.replaceoperation8 = randomInt2;

				break;
			case 9:
				MyProblem.oldoperation9 = randomInt;
				MyProblem.replaceoperation9 = randomInt2;

				break;
			case 10:
				MyProblem.oldoperation10 = randomInt;
				MyProblem.replaceoperation10 = randomInt2;

				break;
			case 11:
				MyProblem.oldoperation11 = randomInt;
				MyProblem.replaceoperation11 = randomInt2;

				break;
			case 12:
				MyProblem.oldoperation12 = randomInt;
				MyProblem.replaceoperation12 = randomInt2;

				break;
			case 13:
				MyProblem.oldoperation13 = randomInt;
				MyProblem.replaceoperation13 = randomInt2;

				break;
			case 14:
				MyProblem.oldoperation14 = randomInt;
				MyProblem.replaceoperation14 = randomInt2;

				break;
			case 15:
				MyProblem.oldoperation15 = randomInt;
				MyProblem.replaceoperation15 = randomInt2;

				break;
			case 16:
				MyProblem.oldoperation16 = randomInt;
				MyProblem.replaceoperation16 = randomInt2;

				break;
			case 17:
				MyProblem.oldoperation17 = randomInt;
				MyProblem.replaceoperation17 = randomInt2;

				break;
			case 18:
				MyProblem.oldoperation18 = randomInt;
				MyProblem.replaceoperation18 = randomInt2;

				break;
			case 19:
				MyProblem.oldoperation19 = randomInt;
				MyProblem.replaceoperation19 = randomInt2;

				break;
			case 20:
				MyProblem.oldoperation20 = randomInt;
				MyProblem.replaceoperation20 = randomInt2;

				break;
			case 21:
				MyProblem.oldoperation21 = randomInt;
				MyProblem.replaceoperation21 = randomInt2;

				break;
			case 22:
				MyProblem.oldoperation22 = randomInt;
				MyProblem.replaceoperation22 = randomInt2;

				break;
			}

		}

	}

	private int FindSecondIndex(int randomInt2, int size) {
		// TODO Auto-generated method stub
		if (NSGAII.statemutcrossinitial.equals("mutation")) {

			if (Operations.statecase.equals("case1") || Operations.statecase.equals("case6")
					|| Operations.statecase.equals("case4")) {

				switch (MyProblem.indexoperation) {
				case 1:
					if (MyProblem.replaceoperation1 != -1)
						randomInt2 = (int) (MyProblem.replaceoperation1);
					else {
						Random number_generator = new Random();
						randomInt2 = number_generator.nextInt(size);

					}
					// randomInt2=(int) (Math.random() * (replacements.size()-1));

					break;
				case 2:
					if (MyProblem.replaceoperation2 != -1)
						randomInt2 = (int) (MyProblem.replaceoperation2);
					else {
						Random number_generator = new Random();
						randomInt2 = number_generator.nextInt(size);

					}
					// randomInt2=(int) (Math.random() * (replacements.size()-1));

					break;
				case 3:
					if (MyProblem.replaceoperation3 != -1)
						randomInt2 = (int) (MyProblem.replaceoperation3);
					else {
						Random number_generator = new Random();
						randomInt2 = number_generator.nextInt(size);

					}
					// randomInt2=(int) (Math.random() * (replacements.size()-1));

					break;
				case 4:
					if (MyProblem.replaceoperation4 != -1)
						randomInt2 = (int) (MyProblem.replaceoperation4);
					else {
						Random number_generator = new Random();
						randomInt2 = number_generator.nextInt(size);

					}
					// randomInt2=(int) (Math.random() * (replacements.size()-1));

					break;
				case 5:
					if (MyProblem.replaceoperation5 != -1)
						randomInt2 = (int) (MyProblem.replaceoperation5);
					else {
						Random number_generator = new Random();
						randomInt2 = number_generator.nextInt(size);

					}
					// randomInt2=(int) (Math.random() * (replacements.size()-1));

					break;
				case 6:
					if (MyProblem.replaceoperation6 != -1)
						randomInt2 = (int) (MyProblem.replaceoperation6);
					else {
						Random number_generator = new Random();
						randomInt2 = number_generator.nextInt(size);

					}
					// randomInt2=(int) (Math.random() * (replacements.size()-1));

					break;
				case 7:
					if (MyProblem.replaceoperation7 != -1)
						randomInt2 = (int) (MyProblem.replaceoperation7);
					else {
						Random number_generator = new Random();
						randomInt2 = number_generator.nextInt(size);

					}
					// randomInt2=(int) (Math.random() * (replacements.size()-1));

					break;
				case 8:
					if (MyProblem.replaceoperation8 != -1)
						randomInt2 = (int) (MyProblem.replaceoperation8);
					else {
						Random number_generator = new Random();
						randomInt2 = number_generator.nextInt(size);

					}
					// randomInt2=(int) (Math.random() * (replacements.size()-1));

					break;
				case 9:
					if (MyProblem.replaceoperation9 != -1)
						randomInt2 = (int) (MyProblem.replaceoperation9);
					else {
						Random number_generator = new Random();
						randomInt2 = number_generator.nextInt(size);

					}
					// randomInt2=(int) (Math.random() * (replacements.size()-1));

					break;
				case 10:
					if (MyProblem.replaceoperation10 != -1)
						randomInt2 = (int) (MyProblem.replaceoperation10);
					else {
						Random number_generator = new Random();
						randomInt2 = number_generator.nextInt(size);

					}
					// randomInt2=(int) (Math.random() * (replacements.size()-1));

					break;
				case 11:
					if (MyProblem.replaceoperation11 != -1)
						randomInt2 = (int) (MyProblem.replaceoperation11);
					else {
						Random number_generator = new Random();
						randomInt2 = number_generator.nextInt(size);

					}
					// randomInt2=(int) (Math.random() * (replacements.size()-1));

					break;
				case 12:
					if (MyProblem.replaceoperation12 != -1)
						randomInt2 = (int) (MyProblem.replaceoperation12);
					else {
						Random number_generator = new Random();
						randomInt2 = number_generator.nextInt(size);

					}
					// randomInt2=(int) (Math.random() * (replacements.size()-1));

					break;
				case 13:
					if (MyProblem.replaceoperation13 != -1)
						randomInt2 = (int) (MyProblem.replaceoperation13);
					else {
						Random number_generator = new Random();
						randomInt2 = number_generator.nextInt(size);

					}
					// randomInt2=(int) (Math.random() * (replacements.size()-1));

					break;
				case 14:
					if (MyProblem.replaceoperation14 != -1)
						randomInt2 = (int) (MyProblem.replaceoperation14);
					else {
						Random number_generator = new Random();
						randomInt2 = number_generator.nextInt(size);

					}
					// randomInt2=(int) (Math.random() * (replacements.size()-1));

					break;
				case 15:
					if (MyProblem.replaceoperation15 != -1)
						randomInt2 = (int) (MyProblem.replaceoperation15);
					else {
						Random number_generator = new Random();
						randomInt2 = number_generator.nextInt(size);

					}

					break;
				case 16:
					if (MyProblem.replaceoperation16 != -1)
						randomInt2 = (int) (MyProblem.replaceoperation16);
					else {
						Random number_generator = new Random();
						randomInt2 = number_generator.nextInt(size);

					}

					break;
				case 17:
					if (MyProblem.replaceoperation17 != -1)
						randomInt2 = (int) (MyProblem.replaceoperation17);
					else {
						Random number_generator = new Random();
						randomInt2 = number_generator.nextInt(size);

					}

					break;
				case 18:
					if (MyProblem.replaceoperation18 != -1)
						randomInt2 = (int) (MyProblem.replaceoperation18);
					else {
						Random number_generator = new Random();
						randomInt2 = number_generator.nextInt(size);

					}

					break;
				case 19:
					if (MyProblem.replaceoperation19 != -1)
						randomInt2 = (int) (MyProblem.replaceoperation19);
					else {
						Random number_generator = new Random();
						randomInt2 = number_generator.nextInt(size);

					}

					break;
				case 20:
					if (MyProblem.replaceoperation20 != -1)
						randomInt2 = (int) (MyProblem.replaceoperation20);
					else {
						Random number_generator = new Random();
						randomInt2 = number_generator.nextInt(size);

					}

					break;
				case 21:
					if (MyProblem.replaceoperation21 != -1)
						randomInt2 = (int) (MyProblem.replaceoperation21);
					else {
						Random number_generator = new Random();
						randomInt2 = number_generator.nextInt(size);

					}

					break;
				case 22:
					if (MyProblem.replaceoperation22 != -1)
						randomInt2 = (int) (MyProblem.replaceoperation22);
					else {
						Random number_generator = new Random();
						randomInt2 = number_generator.nextInt(size);

					}

					break;

				}

			}

		} else {
			Random number_generator = new Random();
			randomInt2 = number_generator.nextInt(size);
			// randomInt2=(int) (Math.random() * (replacements.size()-1));
		}
		return randomInt2;

	}

	private int[] something() {
		int number1 = 1;
		int number2 = 2;
		return new int[] { number1, number2 };
	}

	private List<Integer> ReturnFirstIndex(int randomInt, int size, boolean checkmutationapply, int count, Solution solution) {
		// TODO Auto-generated method stub
		if (NSGAII.statemutcrossinitial.equals("mutation")) {

			if (Operations.statecase.equals("case1") || Operations.statecase.equals("case7")
					|| Operations.statecase.equals("case6") || Operations.statecase.equals("case4")) {

				/*
				 * System.out.print("mutationstart"); System.out.print(MyProblem.oldoperation1);
				 * System.out.print(MyProblem.replaceoperation1); System.out.print("m");
				 * System.out.print(MyProblem.oldoperation2);
				 * System.out.print(MyProblem.replaceoperation2); System.out.print("m");
				 * System.out.print(MyProblem.oldoperation3);
				 * System.out.print(MyProblem.replaceoperation3);System.out.print("m");
				 * System.out.print(MyProblem.oldoperation4);
				 * System.out.print(MyProblem.replaceoperation4);System.out.print("m");
				 * System.out.print(MyProblem.oldoperation5);
				 * System.out.print(MyProblem.replaceoperation5);System.out.print("m");
				 * System.out.print(MyProblem.oldoperation6);
				 * System.out.print(MyProblem.replaceoperation6);System.out.print("m");
				 * System.out.print(MyProblem.oldoperation7);
				 * System.out.print(MyProblem.replaceoperation7);
				 * System.out.print(MyProblem.oldoperation8);
				 * System.out.print(MyProblem.replaceoperation8);
				 * System.out.print(MyProblem.oldoperation9);
				 * System.out.print(MyProblem.replaceoperation9);
				 * System.out.print(MyProblem.oldoperation10);
				 * System.out.print(MyProblem.replaceoperation10);
				 * System.out.print(MyProblem.oldoperation11);
				 * System.out.print(MyProblem.replaceoperation11);
				 * System.out.print(MyProblem.oldoperation12);
				 * System.out.print(MyProblem.replaceoperation12);
				 * System.out.print(MyProblem.oldoperation13);
				 * System.out.print(MyProblem.replaceoperation13);
				 * System.out.print(MyProblem.oldoperation14);
				 * System.out.print(MyProblem.replaceoperation14);
				 * System.out.println(MyProblem.indexoperation);
				 */
				// if (MyProblem.oldoperation1!=-1)
				// randomInt=(int) ( MyProblem.oldoperation1);
				// else

				// randomInt=(int) (Math.random() * (modifiable.size()-1));
				switch (MyProblem.indexoperation) {
				case 1:
					if (MyProblem.oldoperation1 != -1) {
						randomInt = (int) (MyProblem.oldoperation1);
					    solution.setpreviousgeneration(true);
					/*	if (count == 7) {
							MyProblem.oldoperation1 = -1;
							MyProblem.replaceoperation1 = -1;
							MyProblem.secondoldoperation1 = -1;
							Random number_generator = new Random();
							randomInt = number_generator.nextInt(size);
							NSGAII.writer.println("index 1 update shod");
							
						}*/

					} else {
						Random number_generator = new Random();
						randomInt = number_generator.nextInt(size);
						 solution.setpreviousgeneration(false);
					}
					// randomInt=(int) (Math.random() * (modifiable.size()-1));

					break;
				case 2:
					if (MyProblem.oldoperation2 != -1) {
						randomInt = (int) (MyProblem.oldoperation2);
						
						 solution.setpreviousgeneration(true);
					} else {
						Random number_generator = new Random();
						randomInt = number_generator.nextInt(size);
						 solution.setpreviousgeneration(false);
					}
					// randomInt=(int) (Math.random() * (modifiable.size()-1));

					break;
				case 3:
					if (MyProblem.oldoperation3 != -1) {
						randomInt = (int) (MyProblem.oldoperation3);
						 solution.setpreviousgeneration(true);
						
					} else {
						Random number_generator = new Random();
						randomInt = number_generator.nextInt(size);
						 solution.setpreviousgeneration(false);

					}
					// randomInt=(int) (Math.random() * (modifiable.size()-1));

					break;
				case 4:
					if (MyProblem.oldoperation4 != -1) {
						randomInt = (int) (MyProblem.oldoperation4);
						 solution.setpreviousgeneration(true);
						
					} else {
						Random number_generator = new Random();
						randomInt = number_generator.nextInt(size);
						 solution.setpreviousgeneration(false);
					}
					// randomInt=(int) (Math.random() * (modifiable.size()-1));

					break;
				case 5:
					if (MyProblem.oldoperation5 != -1) {
						randomInt = (int) (MyProblem.oldoperation5);
						// System.out.print("r55r");
						 solution.setpreviousgeneration(true);
					} else {
						Random number_generator = new Random();
						randomInt = number_generator.nextInt(size);
						 solution.setpreviousgeneration(false);
					}
					// randomInt=(int) (Math.random() * (modifiable.size()-1));

					break;
				case 6:
					if (MyProblem.oldoperation6 != -1) {
						randomInt = (int) (MyProblem.oldoperation6);
						 solution.setpreviousgeneration(true);
					} else {
						Random number_generator = new Random();
						randomInt = number_generator.nextInt(size);
						 solution.setpreviousgeneration(false);
					}
					// randomInt=(int) (Math.random() * (modifiable.size()-1));

					break;
				case 7:
					if (MyProblem.oldoperation7 != -1) {
						randomInt = (int) (MyProblem.oldoperation7);
						// System.out.print("r77r");
						 solution.setpreviousgeneration(true);
					} else {
						Random number_generator = new Random();
						randomInt = number_generator.nextInt(size);
						 solution.setpreviousgeneration(false);
					}
					// randomInt=(int) (Math.random() * (modifiable.size()-1));

					break;
				case 8:
					if (MyProblem.oldoperation8 != -1) {
						randomInt = (int) (MyProblem.oldoperation8);
						 solution.setpreviousgeneration(true);

					} else {
						Random number_generator = new Random();
						randomInt = number_generator.nextInt(size);
						 solution.setpreviousgeneration(false);
					}
					// randomInt=(int) (Math.random() * (modifiable.size()-1));

					break;
				case 9:
					if (MyProblem.oldoperation9 != -1) {
						randomInt = (int) (MyProblem.oldoperation9);
						 solution.setpreviousgeneration(true);
					} else {
						Random number_generator = new Random();
						randomInt = number_generator.nextInt(size);
						 solution.setpreviousgeneration(false);
					}
					// randomInt=(int) (Math.random() * (modifiable.size()-1));

					break;
				case 10:
					if (MyProblem.oldoperation10 != -1) {
						randomInt = (int) (MyProblem.oldoperation10);
						 solution.setpreviousgeneration(true);
					} else {
						Random number_generator = new Random();
						randomInt = number_generator.nextInt(size);
						 solution.setpreviousgeneration(false);
					}
					// randomInt=(int) (Math.random() * (modifiable.size()-1));

					break;
				case 11:
					if (MyProblem.oldoperation11 != -1) {
						randomInt = (int) (MyProblem.oldoperation11);
						 solution.setpreviousgeneration(true);
					} else {
						Random number_generator = new Random();
						randomInt = number_generator.nextInt(size);
						 solution.setpreviousgeneration(false);
					}
					// randomInt=(int) (Math.random() * (modifiable.size()-1));

					break;
				case 12:
					if (MyProblem.oldoperation12 != -1) {
						randomInt = (int) (MyProblem.oldoperation12);
						 solution.setpreviousgeneration(true);
					} else {
						Random number_generator = new Random();
						randomInt = number_generator.nextInt(size);
						 solution.setpreviousgeneration(false);
					}
					// randomInt=(int) (Math.random() * (modifiable.size()-1));

					break;
				case 13:
					if (MyProblem.oldoperation13 != -1) {
						randomInt = (int) (MyProblem.oldoperation13);
						 solution.setpreviousgeneration(true);
					} else {
						Random number_generator = new Random();
						randomInt = number_generator.nextInt(size);
						 solution.setpreviousgeneration(false);
					}
					// randomInt=(int) (Math.random() * (modifiable.size()-1));

					break;
				case 14:
					if (MyProblem.oldoperation14 != -1) {
						randomInt = (int) (MyProblem.oldoperation14);
						 solution.setpreviousgeneration(true);
					} else {
						Random number_generator = new Random();
						randomInt = number_generator.nextInt(size);
						 solution.setpreviousgeneration(false);
					}
					// randomInt=(int) (Math.random() * (modifiable.size()-1));

					break;

				}

			}

		} else {
			Random number_generator = new Random();
			randomInt = number_generator.nextInt(size);
			checkmutationapply = true;
			solution.setpreviousgeneration(false);

		}

		List<Integer> Result = new ArrayList<Integer>();
		Result.add(randomInt);
		if (checkmutationapply == true)
			Result.add(1);
		else
			Result.add(0);
		return Result;
	}

	private void setvariable(int randomInt, int randomInt2, int randomInt3) {
		// TODO Auto-generated method stub

		switch (MyProblem.indexoperation) {
		case 1:
			MyProblem.oldoperation1 = randomInt;
			MyProblem.replaceoperation1 = randomInt2;
			MyProblem.secondoldoperation1 = randomInt3;
			break;
		case 2:
			MyProblem.oldoperation2 = randomInt;
			MyProblem.replaceoperation2 = randomInt2;
			MyProblem.secondoldoperation2 = randomInt3;

			break;
		case 3:
			MyProblem.oldoperation3 = randomInt;
			MyProblem.replaceoperation3 = randomInt2;
			MyProblem.secondoldoperation3 = randomInt3;

			break;
		case 4:
			MyProblem.oldoperation4 = randomInt;
			MyProblem.replaceoperation4 = randomInt2;
			MyProblem.secondoldoperation4 = randomInt3;

			break;
		case 5:
			MyProblem.oldoperation5 = randomInt;
			MyProblem.replaceoperation5 = randomInt2;
			MyProblem.secondoldoperation5 = randomInt3;

			break;
		case 6:
			MyProblem.oldoperation6 = randomInt;
			MyProblem.replaceoperation6 = randomInt2;
			MyProblem.secondoldoperation6 = randomInt3;

			break;
		case 7:
			MyProblem.oldoperation7 = randomInt;
			MyProblem.replaceoperation7 = randomInt2;
			MyProblem.secondoldoperation7 = randomInt3;

			break;
		case 8:
			MyProblem.oldoperation8 = randomInt;
			MyProblem.replaceoperation8 = randomInt2;
			MyProblem.secondoldoperation8 = randomInt3;

			break;
		case 9:
			MyProblem.oldoperation9 = randomInt;
			MyProblem.replaceoperation9 = randomInt2;
			MyProblem.secondoldoperation9 = randomInt3;

			break;
		case 10:
			MyProblem.oldoperation10 = randomInt;
			MyProblem.replaceoperation10 = randomInt2;
			MyProblem.secondoldoperation10 = randomInt3;

			break;
		case 11:
			MyProblem.oldoperation11 = randomInt;
			MyProblem.replaceoperation11 = randomInt2;
			MyProblem.secondoldoperation11 = randomInt3;

			break;
		case 12:
			MyProblem.oldoperation12 = randomInt;
			MyProblem.replaceoperation12 = randomInt2;
			MyProblem.secondoldoperation12 = randomInt3;

			break;
		case 13:
			MyProblem.oldoperation13 = randomInt;
			MyProblem.replaceoperation13 = randomInt2;
			MyProblem.secondoldoperation13 = randomInt3;

			break;
		case 14:
			MyProblem.oldoperation14 = randomInt;
			MyProblem.replaceoperation14 = randomInt2;
			MyProblem.secondoldoperation14 = randomInt3;

			break;
		}
	}

	private int calculaterandomInt(List<EObject> replacements) {
		// TODO Auto-generated method stub

		int randomInt2 = -2;
		if (NSGAII.statemutcrossinitial == "mutation") {

			switch (MyProblem.indexoperation) {
			case 1:
				if (MyProblem.replaceoperation1 != -1)
					randomInt2 = (int) (MyProblem.replaceoperation1);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(replacements.size());

				}
				// randomInt2=(int) (Math.random() * (replacements.size()-1));

				break;
			case 2:
				if (MyProblem.replaceoperation2 != -1)
					randomInt2 = (int) (MyProblem.replaceoperation2);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(replacements.size());

				}
				// randomInt2=(int) (Math.random() * (replacements.size()-1));

				break;
			case 3:
				if (MyProblem.replaceoperation3 != -1)
					randomInt2 = (int) (MyProblem.replaceoperation3);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(replacements.size());

				}
				// randomInt2=(int) (Math.random() * (replacements.size()-1));

				break;
			case 4:
				if (MyProblem.replaceoperation4 != -1)
					randomInt2 = (int) (MyProblem.replaceoperation4);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(replacements.size());

				}
				// randomInt2=(int) (Math.random() * (replacements.size()-1));

				break;
			case 5:
				if (MyProblem.replaceoperation5 != -1)
					randomInt2 = (int) (MyProblem.replaceoperation5);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(replacements.size());

				}
				// randomInt2=(int) (Math.random() * (replacements.size()-1));

				break;
			case 6:
				if (MyProblem.replaceoperation6 != -1)
					randomInt2 = (int) (MyProblem.replaceoperation6);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(replacements.size());

				}
				// randomInt2=(int) (Math.random() * (replacements.size()-1));

				break;
			case 7:
				if (MyProblem.replaceoperation7 != -1)
					randomInt2 = (int) (MyProblem.replaceoperation7);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(replacements.size());

				}
				// randomInt2=(int) (Math.random() * (replacements.size()-1));

				break;
			case 8:
				if (MyProblem.replaceoperation8 != -1)
					randomInt2 = (int) (MyProblem.replaceoperation8);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(replacements.size());

				}
				// randomInt2=(int) (Math.random() * (replacements.size()-1));

				break;
			case 9:
				if (MyProblem.replaceoperation9 != -1)
					randomInt2 = (int) (MyProblem.replaceoperation9);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(replacements.size());

				}
				// randomInt2=(int) (Math.random() * (replacements.size()-1));

				break;
			case 10:
				if (MyProblem.replaceoperation10 != -1)
					randomInt2 = (int) (MyProblem.replaceoperation10);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(replacements.size());

				}
				// randomInt2=(int) (Math.random() * (replacements.size()-1));

				break;
			case 11:
				if (MyProblem.replaceoperation11 != -1)
					randomInt2 = (int) (MyProblem.replaceoperation11);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(replacements.size());

				}
				// randomInt2=(int) (Math.random() * (replacements.size()-1));

				break;
			case 12:
				if (MyProblem.replaceoperation12 != -1)
					randomInt2 = (int) (MyProblem.replaceoperation12);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(replacements.size());

				}
				// randomInt2=(int) (Math.random() * (replacements.size()-1));

				break;
			case 13:
				if (MyProblem.replaceoperation13 != -1)
					randomInt2 = (int) (MyProblem.replaceoperation13);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(replacements.size());

				}
				// randomInt2=(int) (Math.random() * (replacements.size()-1));

				break;
			case 14:
				if (MyProblem.replaceoperation14 != -1)
					randomInt2 = (int) (MyProblem.replaceoperation14);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(replacements.size());

				}
				// randomInt2=(int) (Math.random() * (replacements.size()-1));

				break;
			}

		} else {
			Random number_generator = new Random();
			randomInt2 = number_generator.nextInt(replacements.size());
			// randomInt2=(int) (Math.random() * (replacements.size()-1));
		}
		return randomInt2;

	}

	private int calculaterandomInt2(List<EObject> value) {
		// TODO Auto-generated method stub
		int randomInt = -2;
		if (NSGAII.statemutcrossinitial == "mutation") {

			switch (MyProblem.indexoperation) {
			case 1:
				if (MyProblem.secondoldoperation1 != -1)
					randomInt = (int) (MyProblem.secondoldoperation1);
				else {
					Random number_generator = new Random();
					randomInt = number_generator.nextInt(value.size());

				}
				// randomInt=(int) (Math.random() * (modifiable.size()-1));

				break;
			case 2:
				if (MyProblem.secondoldoperation2 != -1)
					randomInt = (int) (MyProblem.secondoldoperation2);
				else {
					Random number_generator = new Random();
					randomInt = number_generator.nextInt(value.size());

				}
				// randomInt=(int) (Math.random() * (modifiable.size()-1));

				break;
			case 3:
				if (MyProblem.secondoldoperation3 != -1)
					randomInt = (int) (MyProblem.secondoldoperation3);
				else {
					Random number_generator = new Random();
					randomInt = number_generator.nextInt(value.size());

				}
				// randomInt=(int) (Math.random() * (modifiable.size()-1));

				break;
			case 4:
				if (MyProblem.secondoldoperation4 != -1)
					randomInt = (int) (MyProblem.secondoldoperation4);
				else {
					Random number_generator = new Random();
					randomInt = number_generator.nextInt(value.size());

				}
				// randomInt=(int) (Math.random() * (modifiable.size()-1));

				break;
			case 5:
				if (MyProblem.secondoldoperation5 != -1)
					randomInt = (int) (MyProblem.secondoldoperation5);
				else {
					Random number_generator = new Random();
					randomInt = number_generator.nextInt(value.size());

				}
				// randomInt=(int) (Math.random() * (modifiable.size()-1));

				break;
			case 6:
				if (MyProblem.secondoldoperation6 != -1)
					randomInt = (int) (MyProblem.secondoldoperation6);
				else {
					Random number_generator = new Random();
					randomInt = number_generator.nextInt(value.size());

				}
				// randomInt=(int) (Math.random() * (modifiable.size()-1));

				break;
			case 7:
				if (MyProblem.secondoldoperation7 != -1)
					randomInt = (int) (MyProblem.secondoldoperation7);
				else {
					Random number_generator = new Random();
					randomInt = number_generator.nextInt(value.size());

				}
				// randomInt=(int) (Math.random() * (modifiable.size()-1));

				break;
			case 8:
				if (MyProblem.secondoldoperation8 != -1)
					randomInt = (int) (MyProblem.secondoldoperation8);
				else {
					Random number_generator = new Random();
					randomInt = number_generator.nextInt(value.size());

				}
				// randomInt=(int) (Math.random() * (modifiable.size()-1));

				break;
			case 9:
				if (MyProblem.secondoldoperation9 != -1)
					randomInt = (int) (MyProblem.secondoldoperation9);
				else {
					Random number_generator = new Random();
					randomInt = number_generator.nextInt(value.size());

				}
				// randomInt=(int) (Math.random() * (modifiable.size()-1));

				break;
			case 10:
				if (MyProblem.secondoldoperation10 != -1)
					randomInt = (int) (MyProblem.secondoldoperation10);
				else {
					Random number_generator = new Random();
					randomInt = number_generator.nextInt(value.size());

				}
				// randomInt=(int) (Math.random() * (modifiable.size()-1));

				break;
			case 11:
				if (MyProblem.secondoldoperation11 != -1)
					randomInt = (int) (MyProblem.secondoldoperation11);
				else {
					Random number_generator = new Random();
					randomInt = number_generator.nextInt(value.size());

				}
				// randomInt=(int) (Math.random() * (modifiable.size()-1));

				break;
			case 12:
				if (MyProblem.secondoldoperation12 != -1)
					randomInt = (int) (MyProblem.secondoldoperation12);
				else {
					Random number_generator = new Random();
					randomInt = number_generator.nextInt(value.size());

				}
				// randomInt=(int) (Math.random() * (modifiable.size()-1));

				break;
			case 13:
				if (MyProblem.secondoldoperation13 != -1)
					randomInt = (int) (MyProblem.secondoldoperation13);
				else {
					Random number_generator = new Random();
					randomInt = number_generator.nextInt(value.size());

				}
				// randomInt=(int) (Math.random() * (modifiable.size()-1));

				break;
			case 14:
				if (MyProblem.secondoldoperation14 != -1)
					randomInt = (int) (MyProblem.secondoldoperation14);
				else {
					Random number_generator = new Random();
					randomInt = number_generator.nextInt(value.size());

				}
				// randomInt=(int) (Math.random() * (modifiable.size()-1));

				break;
			case 15:
				if (MyProblem.secondoldoperation15 != -1)
					randomInt = (int) (MyProblem.secondoldoperation15);
				else {
					Random number_generator = new Random();
					randomInt = number_generator.nextInt(value.size());

				}
				// randomInt=(int) (Math.random() * (modifiable.size()-1));

				break;
			case 16:
				if (MyProblem.secondoldoperation16 != -1)
					randomInt = (int) (MyProblem.secondoldoperation16);
				else {
					Random number_generator = new Random();
					randomInt = number_generator.nextInt(value.size());

				}
				// randomInt=(int) (Math.random() * (modifiable.size()-1));

				break;
			case 17:
				if (MyProblem.secondoldoperation17 != -1)
					randomInt = (int) (MyProblem.secondoldoperation17);
				else {
					Random number_generator = new Random();
					randomInt = number_generator.nextInt(value.size());

				}
				// randomInt=(int) (Math.random() * (modifiable.size()-1));

				break;
			case 18:
				if (MyProblem.secondoldoperation18 != -1)
					randomInt = (int) (MyProblem.secondoldoperation18);
				else {
					Random number_generator = new Random();
					randomInt = number_generator.nextInt(value.size());

				}
				// randomInt=(int) (Math.random() * (modifiable.size()-1));

				break;
			case 19:
				if (MyProblem.secondoldoperation19 != -1)
					randomInt = (int) (MyProblem.secondoldoperation19);
				else {
					Random number_generator = new Random();
					randomInt = number_generator.nextInt(value.size());

				}
				// randomInt=(int) (Math.random() * (modifiable.size()-1));

				break;
			case 20:
				if (MyProblem.secondoldoperation20 != -1)
					randomInt = (int) (MyProblem.secondoldoperation20);
				else {
					Random number_generator = new Random();
					randomInt = number_generator.nextInt(value.size());

				}
				// randomInt=(int) (Math.random() * (modifiable.size()-1));

				break;
			case 21:
				if (MyProblem.secondoldoperation21 != -1)
					randomInt = (int) (MyProblem.secondoldoperation21);
				else {
					Random number_generator = new Random();
					randomInt = number_generator.nextInt(value.size());

				}
				// randomInt=(int) (Math.random() * (modifiable.size()-1));

				break;
			case 22:
				if (MyProblem.secondoldoperation22 != -1)
					randomInt = (int) (MyProblem.secondoldoperation22);
				else {
					Random number_generator = new Random();
					randomInt = number_generator.nextInt(value.size());

				}
				// randomInt=(int) (Math.random() * (modifiable.size()-1));

				break;

			}

		} else {
			Random number_generator = new Random();
			randomInt = number_generator.nextInt(value.size());
			// randomInt2=(int) (Math.random() * (replacements.size()-1));
		}
		return randomInt;

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

	/**
	 * It returns the list of classes that replace a given one: a compatible
	 * subclass, a compatible superclass, a compatible unrelated class, an
	 * incompatible subclass, and an incompatible unrelated class.
	 * 
	 * @param toReplace
	 * @param metamodel
	 * @return
	 */
	private List<EObject> replacements(EMFModel atlModel, EObject toReplace, MetaModel[] metamodels) {
		List<EObject> replacements = new ArrayList<EObject>();

		EPackage pack = toReplace.eClass().getEPackage();
		// System.out.println("objects");
		// System.out.println(pack);
		EClass mmtype = (EClass) pack.getEClassifier("OclModelElement");
		EClass collection = (EClass) pack.getEClassifier("CollectionType");
		EClass collectionExp = (EClass) pack.getEClassifier("CollectionExp");
		EClass primitive = (EClass) pack.getEClassifier("Primitive");
		Setting s=new Setting();
		// OCL MODEL ELEMENT
		// .......................................................................
		// System.out.println("replacement");
		if (mmtype.isInstance(toReplace)) {
			for (MetaModel metamodel : metamodels) {
				// search class to replace
				// String MMRootPath3 =
				// "C:/Users/varaminz/Downloads/Project/workspaceTEST34/anatlyzer.atl.tests.api/trafo/PNML.ecore";
				String MMRootPath3 = null;
				if (Operations.statecase.equals("case1")) {
					// MMRootPath3 =
					// "C:/Users/varaminz/Downloads/Project/workspaceTEST34/anatlyzer.atl.tests.api/trafo/PetriNet.ecore";
					MMRootPath3 = s.getsourcemetamodel();
				} else {
					// MMRootPath3 =
					// "C:/Users/varaminz/Downloads/Project/workspaceTEST34/anatlyzer.atl.tests.api/trafo/PNML.ecore";
					MMRootPath3 = s.gettargetmetamodel();
				}
				MetaModel meta = null;
				try {
					meta = new MetaModel(MMRootPath3);
				} catch (transException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				EStructuralFeature featureName = toReplace.eClass().getEStructuralFeature("name");
				String featureValue = toReplace.eGet(featureName).toString();
				// EClassifier classToReplace = metamodel.getEClassifier(featureValue);
				EClassifier classToReplace = meta.getEClassifier(featureValue);
				// System.out.println(featureName);
				// System.out.println(featureValue);
				// System.out.println(classToReplace2);
				// System.out.println(metamodel.getEClassifiers());
			//	System.out.println(meta.getEClassifiers());
				List<EClass> options = new ArrayList<EClass>();
				
				if (main.typeoperation.equals("argu") || main.typeoperation.equals("outelement")
						|| main.typeoperation.equals("inelement")) {

					List<EClass> mainclass = new ArrayList<EClass>();
					for (EClassifier classifier : meta.getEClassifiers()) {
						if (classifier instanceof EClass) {
							if(Operations.statecase.equals("case1") && Operations.statecase.equals("case7"))
							{
							 if(((EClass) classifier).isAbstract()==false)	
								 mainclass.add((EClass) classifier);
							}
								
							else {
								
								if(NSGAII.lazyrulelocation.size()==0) {
									 if(((EClass) classifier).isAbstract()==false)	
										 mainclass.add((EClass) classifier);
									
								}
								else
								mainclass.add((EClass) classifier);
							}
						}
					}
					// System.out.println(mainclass.size());
					for (int ii = 0; ii < mainclass.size(); ii++) {
						options.add(mainclass.get(ii));

					}

				}
				// metamodel ra be meta tabdil kardam
				else {
					if (classToReplace != null && classToReplace instanceof EClass) {
						EClass replace = (EClass) classToReplace;
						// List<EClass> options = new ArrayList<EClass>();
						List<EClass> copy1 = new ArrayList<EClass>();
						List<EClass> copy2 = new ArrayList<EClass>();
						List<EClass> copy3 = new ArrayList<EClass>();
						List<EClass> copy4 = new ArrayList<EClass>();
						List<EClass> copy5 = new ArrayList<EClass>();
						// search classes to use as replacement
						// copy1=getCompatibleSubclass (replace, metamodel);
						copy1 = getCompatibleSubclass(replace, meta);
						for (int ii = 0; ii < copy1.size(); ii++) {
							options.add(copy1.get(ii));

						}
						// options.add( getCompatibleSubclass (replace, metamodel) );
						// System.out.println(copy1.size());
						copy1.clear();
						// System.out.println(copy1.size());
						copy2 = getCompatibleSuperclass(replace);
						// System.out.println(copy2);
						if (copy2 != null) {
							for (int ii = 0; ii < copy2.size(); ii++)
								options.add(copy2.get(ii));
						}
						// options.add( getCompatibleSuperclass (replace) );

						copy3 = getCompatibleUnrelatedClass(replace, meta);
						for (int ii = 0; ii < copy3.size(); ii++)
							options.add(copy3.get(ii));

						// options.add( getCompatibleUnrelatedClass (replace, metamodel) );
						copy3.clear();
						copy4 = getIncompatibleSubclass(replace, meta);
						for (int ii = 0; ii < copy4.size(); ii++)
							options.add(copy4.get(ii));

						// options.add( getIncompatibleSubclass (replace, metamodel) );
						copy4.clear();
						copy5 = getIncompatibleUnrelatedClass(replace, meta);
						for (int ii = 0; ii < copy5.size(); ii++)
							options.add(copy5.get(ii));

						/*
						 * System.out.println( "options"); for(int ii=0;ii<options.size();ii++) {
						 * System.out.println( options.get(ii));
						 * 
						 * }
						 */
					}
				}
				// options.add( getIncompatibleUnrelatedClass(replace, metamodel) );
				// create an OclModelElement for each found replacement class
				for (EClass option : options) {
					if (option != null) {
						EObject object = (EObject) atlModel.newElement(mmtype);
						object.eSet(mmtype.getEStructuralFeature("name"), option.getName());
						replacements.add(object);
					}
				}
				

				break; // khodam add kardam
			}
		}

		// COLLECTION TYPE
		// .........................................................................

		else if (collection.isInstance(toReplace)) {

			// replace by each different collection type
			String[] options = { "SequenceType", "SetType", "BagType", "OrderedSetType" };
			for (String option : options) {
				if (!((EClass) pack.getEClassifier(option)).isInstance(toReplace)) {
					EClass collectionType = (EClass) pack.getEClassifier(option);
					replacements.add((EObject) atlModel.newElement(collectionType));
				}
			}
		}

		// COLLECTION EXPRESSION TYPE
		// ..............................................................

		else if (collectionExp.isInstance(toReplace)) {

			// replace by each different collection type
			String[] options = { "SequenceExp", "SetExp", "BagExp", "OrderedSetExp" };
			for (String option : options) {
				if (!((EClass) pack.getEClassifier(option)).isInstance(toReplace)) {
					EClass collectionExpType = (EClass) pack.getEClassifier(option);
					replacements.add((EObject) atlModel.newElement(collectionExpType));
				}
			}
		}

		// PRIMITIVE TYPE
		// ..........................................................................

		else if (primitive.isInstance(toReplace)) {

			// replace by each different collection type
			String[] options = { "StringType", "BooleanType", "IntegerType", "RealType" };
			for (String option : options) {
				if (!((EClass) pack.getEClassifier(option)).isInstance(toReplace)) {
					EClass primitiveType = (EClass) pack.getEClassifier(option);
					replacements.add((EObject) atlModel.newElement(primitiveType));
				}
			}
		}

		return replacements;
	}

	/**
	 * Given a class, it returns a compatible superclass (i.e. with the same
	 * features).
	 * 
	 * @param c
	 * @return a compatible superclass, null if there is none
	 */
	// protected EClass getCompatibleSuperclass (EClass c) {
	protected List<EClass> getCompatibleSuperclass(EClass c) {
		List<EClass> superclasses = c.getEAllSuperTypes();
		// System.out.println("super");
		// System.out.println(superclasses);
		return c.getEStructuralFeatures().size() > 0 ? getCompatible(c, superclasses) : null;
	}

	/**
	 * Given a class, it returns a compatible subclass (i.e. with the same
	 * features).
	 * 
	 * @param c
	 * @param mm
	 * @return a compatible subclass, null if there is none
	 */
	// protected EClass getCompatibleSubclass (EClass c, MetaModel mm) {
	protected List<EClass> getCompatibleSubclass(EClass c, MetaModel mm) {
		List<EClass> subclasses = subclasses(c, mm);
		return getCompatible(c, subclasses);
	}

	/**
	 * Given a class, it returns a compatible class that is neither a superclass nor
	 * a subclass.
	 * 
	 * @param c
	 * @param mm
	 * @return a compatible unrelated class, null if there is none
	 */
	// protected EClass getCompatibleUnrelatedClass (EClass c, MetaModel mm) {
	protected List<EClass> getCompatibleUnrelatedClass(EClass c, MetaModel mm) {
		List<EClass> unrelatedClasses = unrelatedClasses(c, mm);
		return getCompatible(c, unrelatedClasses);
	}

	/**
	 * Given a class, it returns an incompatible subclass (i.e. with different
	 * features).
	 * 
	 * @param c
	 * @param mm
	 * @return an incompatible subclass, null if there is none
	 */
	// protected EClass getIncompatibleSubclass (EClass c, MetaModel mm) {
	protected List<EClass> getIncompatibleSubclass(EClass c, MetaModel mm) {
		List<EClass> subclasses = subclasses(c, mm);
		return getIncompatible(c, subclasses);
	}

	/**
	 * Given a class, it returns an incompatible class that is neither a superclass
	 * nor a subclass.
	 * 
	 * @param c
	 * @param mm
	 * @return an incompatible unrelated class, null if there is none
	 */
	// protected EClass getIncompatibleUnrelatedClass (EClass c, MetaModel mm) {
	protected List<EClass> getIncompatibleUnrelatedClass(EClass c, MetaModel mm) {
		List<EClass> unrelatedClasses = unrelatedClasses(c, mm);
		return getIncompatible(c, unrelatedClasses);
	}

	// -------------------------------------------------------------------------------------
	// AUXILIARY METHODS
	// -------------------------------------------------------------------------------------

	/**
	 * Given a class, it returns a compatible class (i.e. with the same features)
	 * from a list of candidates.
	 * 
	 * @param c
	 * @param candidates
	 * @return a compatible class, null if there is none
	 */
	// private EClass getCompatible (EClass c, List<EClass> candidates) {
	private List<EClass> getCompatible(EClass c, List<EClass> candidates) {
		// EClass compatible = null;
		List<EClass> compatible = new ArrayList<EClass>();
		// for (int i=0; i<candidates.size() && compatible==null; i++) {
		for (int i = 0; i < candidates.size(); i++) {
			EClass c2 = candidates.get(i);
			if (isCompatibleWith(c2, c))
				compatible.add(c2);
		}
		return compatible;
	}

	// private EClass getIncompatible (EClass c, List<EClass> candidates) {
	private List<EClass> getIncompatible(EClass c, List<EClass> candidates) {
		// EClass incompatible = null;
		List<EClass> incompatible = new ArrayList<EClass>();
		;
		for (int i = 0; i < candidates.size(); i++) {
			EClass c2 = candidates.get(i);
			if (!isCompatibleWith(c2, c))
				incompatible.add(c2);
			// incompatible = c2;
		}
		return incompatible;
	}

	/**
	 * It checks whether a class c1 is compatible with (i.e. it can substitute
	 * safely) another class c2. c1 is compatible with c2 if c1 defines at least all
	 * features that c2 defines (it can define more).
	 * 
	 * @param c1
	 *            class
	 * @param c2
	 *            class
	 * @return
	 */
	private boolean isCompatibleWith(EClass c1, EClass c2) {
		boolean compatible = true;

		for (int i = 0; i < c2.getEAllStructuralFeatures().size() && compatible; i++) {
			EStructuralFeature feature2 = c2.getEAllStructuralFeatures().get(i);
			EStructuralFeature feature1 = c1.getEStructuralFeature(feature2.getName());
			// c1 cannot substitute c2 if:
			// - c1 lacks one of the features of c2
			// - c1 has a feature with same name but different type
			// - c1 has a feature with same name but it is monovalued, while the one in c1
			// is multivalued (or viceversa)
			if (feature1 == null || feature1.getEType() != feature2.getEType()
					|| (feature1.getUpperBound() == 1 && feature2.getUpperBound() != 1)
					|| (feature1.getUpperBound() != 1 && feature2.getUpperBound() == 1))
				compatible = false;
		}

		return compatible;
	}

	/**
	 * It returns the list of classes that are neither superclasses nor subclasses
	 * of a given class.
	 * 
	 * @param c
	 * @param mm
	 * @return
	 */
	private List<EClass> unrelatedClasses(EClass c, MetaModel mm) {
		List<EClass> subclasses = subclasses(c, mm);
		List<EClass> unrelatedClasses = new ArrayList<EClass>();
		for (EClassifier classifier : mm.getEClassifiers()) {
			if (classifier instanceof EClass && classifier != c && !c.getEAllSuperTypes().contains(classifier)
					&& !subclasses.contains(classifier))
				unrelatedClasses.add((EClass) classifier);
		}
		// System.out.println("unrelated");
		// System.out.println(unrelatedClasses);
		return unrelatedClasses;
	}

	/**
	 * It copies all features (except name) "from" an object "to" another object.
	 * 
	 * @param from
	 * @param to
	 * @param features
	 */
	private void copyFeatures(EObject from, EObject to) {
		for (EStructuralFeature feature : from.eClass().getEAllStructuralFeatures()) {
			if (!feature.getName().equals("name") && to.eClass().getEAllStructuralFeatures().contains(feature))
				to.eSet(feature, from.eGet(feature));
		}
	}

	/*
	 * 
	 * 
	 * 
	 * EcoreFactory theCoreFactory = EcoreFactory.eINSTANCE;
	 * 
	 * 
	 * EClass netelementEClass = theCoreFactory.createEClass();
	 * netelementEClass.setName("NetElement");
	 * 
	 * 
	 * EClass nameEClass = theCoreFactory.createEClass();
	 * nameEClass.setName("Name"); EClass netcontentEClass =
	 * theCoreFactory.createEClass(); netcontentEClass.setName("NetContent"); EClass
	 * labelEClass = theCoreFactory.createEClass(); labelEClass.setName("Label");
	 * EClass arcEClass = theCoreFactory.createEClass(); arcEClass.setName("Arc");
	 * EClass netcontentelementEClass = theCoreFactory.createEClass();
	 * netcontentelementEClass.setName("NetContentElement"); EClass transitionEClass
	 * = theCoreFactory.createEClass(); transitionEClass.setName("Transition");
	 * 
	 * EClass placeEClass = theCoreFactory.createEClass();
	 * placeEClass.setName("Place");
	 * 
	 * 
	 * EPackage pnmlEPackage = theCoreFactory.createEPackage();
	 * pnmlEPackage.setName("PBNMLPackage"); pnmlEPackage.setNsPrefix("pnml");
	 * pnmlEPackage.setNsURI("http:///com.ibm.dynamic.example.bookstore.ecore");
	 * EcorePackage theCorePackage = EcorePackage.eINSTANCE;
	 * 
	 * 
	 * EAttribute labeltext = theCoreFactory.createEAttribute();
	 * labeltext.setName("text"); labeltext.setEType(theCorePackage.getEString());
	 * EReference netelement_name = theCoreFactory.createEReference();
	 * netelement_name.setName("name"); netelement_name.setEType(nameEClass);
	 * netelement_name.setLowerBound(0); netelement_name.setUpperBound(1);//
	 * netelement_name.setContainment(true);
	 * 
	 * EReference name_labels = theCoreFactory.createEReference();
	 * name_labels.setName("labels"); name_labels.setEType(labelEClass);
	 * name_labels.setUpperBound(EStructuralFeature.UNBOUNDED_MULTIPLICITY);
	 * name_labels.setContainment(true);
	 * 
	 * EReference netelement_contents = theCoreFactory.createEReference();
	 * netelement_contents.setName("contents");
	 * netelement_contents.setEType(netcontentEClass);
	 * netelement_contents.setUpperBound(EStructuralFeature.UNBOUNDED_MULTIPLICITY);
	 * netelement_contents.setContainment(true);
	 * 
	 * EReference netcontent_name = theCoreFactory.createEReference();
	 * netcontent_name.setName("name"); netcontent_name.setEType(nameEClass);
	 * netcontent_name.setLowerBound(0);// netcontent_name.setUpperBound(1);//
	 * netcontent_name.setContainment(true);
	 * 
	 * EReference arc_source = theCoreFactory.createEReference();
	 * arc_source.setName("source"); arc_source.setEType(netcontentelementEClass);
	 * arc_source.setLowerBound(1);// arc_source.setUpperBound(1);//
	 * arc_source.setContainment(false);
	 * 
	 * EReference arc_target = theCoreFactory.createEReference();
	 * arc_target.setName("target"); arc_target.setEType(netcontentelementEClass);
	 * arc_target.setLowerBound(1);// arc_target.setUpperBound(1);//
	 * arc_target.setContainment(false);
	 * 
	 * 
	 * netelementEClass.getEStructuralFeatures().add(netelement_name);
	 * netelementEClass.getEStructuralFeatures().add(netelement_contents);
	 * 
	 * nameEClass.getEStructuralFeatures().add(name_labels);
	 * netcontentEClass.getEStructuralFeatures().add(netcontent_name);
	 * arcEClass.getEStructuralFeatures().add(arc_source);
	 * arcEClass.getEStructuralFeatures().add(arc_target);
	 * labelEClass.getEStructuralFeatures().add(labeltext);
	 * 
	 * 
	 * pnmlEPackage.getEClassifiers().add(netelementEClass);
	 * pnmlEPackage.getEClassifiers().add(nameEClass);
	 * pnmlEPackage.getEClassifiers().add(netcontentEClass);
	 * pnmlEPackage.getEClassifiers().add(arcEClass);
	 * pnmlEPackage.getEClassifiers().add(labelEClass);
	 * pnmlEPackage.getEClassifiers().add(arcEClass);
	 * 
	 * pnmlEPackage.getEClassifiers().add(netcontentelementEClass);
	 * pnmlEPackage.getEClassifiers().add(transitionEClass);
	 * pnmlEPackage.getEClassifiers().add(placeEClass);
	 * 
	 * 
	 * 
	 * 
	 * EFactory bookFactoryInstance = pnmlEPackage.getEFactoryInstance();
	 * 
	 * 
	 * EObject chooseObject = bookFactoryInstance.create(arcEClass); // EObject
	 * bookStoreObject = bookFactoryInstance.create(bookStoreEClass);
	 * 
	 * System.out.println(chooseObject);
	 * 
	 */

}
