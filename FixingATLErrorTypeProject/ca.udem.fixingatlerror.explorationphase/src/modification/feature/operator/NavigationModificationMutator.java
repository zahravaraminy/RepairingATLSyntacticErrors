package modification.feature.operator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.core.ModelFactory;
import org.eclipse.m2m.atl.core.emf.EMFModel;
import org.eclipse.m2m.atl.core.emf.EMFModelFactory;
import org.eclipse.m2m.atl.core.emf.EMFReferenceModel;
import org.eclipse.m2m.atl.engine.parser.AtlParser;

import witness.generator.MetaModel;
import anatlyzer.atlext.ATL.ForEachOutPatternElement;
import anatlyzer.atlext.ATL.Helper;
import anatlyzer.atlext.ATL.InPatternElement;
import anatlyzer.atlext.ATL.LocatedElement;
import anatlyzer.atlext.ATL.Module;
import anatlyzer.atlext.ATL.OutPatternElement;
import anatlyzer.atlext.ATL.Rule;
import anatlyzer.atlext.OCL.CollectionType;
import anatlyzer.atlext.OCL.Iterator;
import anatlyzer.atlext.OCL.LoopExp;
import anatlyzer.atlext.OCL.NavigationOrAttributeCallExp;
import anatlyzer.atlext.OCL.OclExpression;
import anatlyzer.atlext.OCL.OclModelElement;
import anatlyzer.atlext.OCL.Parameter;
import anatlyzer.atlext.OCL.PropertyCallExp;
import anatlyzer.atlext.OCL.VariableDeclaration;
import anatlyzer.atlext.OCL.VariableExp;
import anatlyzer.atl.model.ATLModel;
//import anatlyzer.evaluation.mutators.ATLModel;
import deletion.operator.AbstractDeletionMutator;
//import anatlyzer.evaluation.tester.Tester;
import ca.udem.fixingatlerror.explorationphase.Setting;
import jmetal.core.Solution;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.problems.MyProblem;
import transML.exceptions.transException;

public class NavigationModificationMutator extends AbstractFeatureModificationMutator {
	private String m="C:/Users/varaminz/Downloads/Project/workspaceTEST34/anatlyzer.atl.tests.api/trafo2/zahra2mutants/finalresult.atl";
	 private EMFModel atlModel3;
	 private EMFModel loadTransformationModel (String atlTransformationFile) throws ATLCoreException {
			ModelFactory      modelFactory = new EMFModelFactory();
			
			EMFReferenceModel atlMetamodel = (EMFReferenceModel)modelFactory.getBuiltInResource("ATL.ecore");
			
			//EMFReferenceModel atlMetamodel = (EMFReferenceModel)modelFactory.getBuiltInResource("C:\\Users\\wael\\OneDrive\\workspaceATLoperations\\mutants\\anatlyzer.evaluation.mutants\\ATL.ecore");
			AtlParser         atlParser    = new AtlParser();		
			EMFModel          atlModel     = (EMFModel)modelFactory.newModel(atlMetamodel);
			atlParser.inject (atlModel, atlTransformationFile);	
			atlModel.setIsTarget(true);				
			
//			// Should we want to serialize the model.
//			String injectedFile = "file:/" + atlTransformationFile + ".xmi";
//			IExtractor extractor = new EMFExtractor();
//			extractor.extract(atlModel, injectedFile);
			
			return atlModel;
		}
		

	@Override
	public List<Object> generateMutants(EMFModel atlModel, MetaModel inputMM, MetaModel outputMM, String outputFolder,ATLModel wrapper, Solution solution) {
		
		
		
		List<Object> ReturnResult = new ArrayList<Object>();
		List<VariableExp> variables = (List<VariableExp>)wrapper.allObjectsOf(VariableExp.class);
		
		ReturnResult.add(wrapper);
		ReturnResult.add(atlModel);
		String comment = null;
		Module module = wrapper.getModule();
		EDataTypeEList<String> comments = null;
		if (module != null) {
			EStructuralFeature feature = wrapper.source(module).eClass().getEStructuralFeature("commentsAfter");
			comments = (EDataTypeEList<String>) wrapper.source(module).eGet(feature);
		}
		
		
		// we will add a comment to the module, documenting the mutation 
		//Module module = AbstractDeletionMutator.getWrapper().getModule();
		int randomInt = -2;
		boolean checkmutationapply = false;
		int count=-1;
		while (checkmutationapply == false) {
			
		 count = count + 1;
		List<Integer> Result = ReturnFirstIndex(randomInt,  variables.size(), checkmutationapply, count, solution);
		randomInt = Result.get(0);
		
		String[] array = variables.get(randomInt).getLocation().split(":", 2);
		if (Result.get(1) == 0)
			checkmutationapply = false;
		else
			checkmutationapply = false;
		if (randomInt == -2)
			checkmutationapply = true;
		
		else if (solution.getpreviousgeneration() == true) {
			
			ReturnResult = OperationPreviousGenerationModefyBindingNavigation(randomInt, solution, atlModel, inputMM,
					variables, wrapper, null, comments, ReturnResult,outputMM);
			checkmutationapply = true;
			
			
			
		}
		// navigate navigation expressions starting from each variable
		//for (VariableExp variable : variables) {
		else if (variables.size() > 0 && randomInt != -2 && solution.getpreviousgeneration() == false &&  Integer.parseInt(array[0])>NSGAII.faultrule.get(0)) {
			EObject navigationExpression = variables.get(randomInt).eContainer();
		//	while  (navigationExpression != null) {
			
				if (navigationExpression instanceof NavigationOrAttributeCallExp) {
					
					EStructuralFeature featureDefinition = wrapper.source(navigationExpression).eClass().getEStructuralFeature("name");
					Object object2modify_src2 = wrapper.source(navigationExpression).eGet(featureDefinition);
					//System.out.println(object2modify_src2.toString());
					//System.out.println(Integer.parseInt(array[0]));
					//System.out.println("collectionlocation");
					//System.out.println(randomInt);
					// obtain list of replacements
					
					
					String navigation = object2modify_src2.toString();
					
			//	String type       = getType(navigationExpression, variables.get(randomInt), inputMM, outputMM); 
					
					//String navigation = ((NavigationOrAttributeCallExp)navigationExpression).getName();
					
				//	EObject oldFeatureValue2 = (EObject) object2modify_src2.eGet(featureDefinition);
					//System.out.println(type);
					
		//			if(type!=null) {
					/*System.out.println(object2modify_src2);
					System.out.println(object2modify_src2.toString());
					System.out.println(type);
					System.out.println(navigation);
					System.out.println(Integer.parseInt(array[0]));*/
					int indexrule = -1;
					indexrule = FindIndexRule(array);
					//System.out.println(indexrule);
					if (NSGAII.errorrule.contains(indexrule)) {
					List<InPatternElement> modifiable2 = (List<InPatternElement>) wrapper.allObjectsOf(InPatternElement.class);
					EStructuralFeature featureDefinitionpattern = wrapper.source(modifiable2.get(indexrule)).eClass()
							.getEStructuralFeature("type");
					EObject object2modify_src = wrapper.source(modifiable2.get(indexrule));
					EObject oldFeatureValue = (EObject) object2modify_src.eGet(featureDefinitionpattern);
					
					
					EClassifier classifier2 = inputMM.getEClassifier(
							toString(oldFeatureValue));
					List<Object> replacements = this.replacementsnavigation(atlModel, (EClass)classifier2,null, navigation, inputMM);
					
					
				// for PNML2PetriNet Class2Relational	if(NSGAII.locationfilter.contains( Integer.parseInt(array[0])) &&  NSGAII.parameterlocation.contains( Integer.parseInt(array[0])) && NSGAII.listnot.contains(Integer.parseInt(array[0])))
				//			checkmutationapply = true;
					//for class 2rel type!=null
				//for PNML2PetriNet	if(!NSGAII.locationfilter.contains( Integer.parseInt(array[0]))   && !NSGAII.parameterlocation.contains( Integer.parseInt(array[0]))
					//		&& !NSGAII.listnot.contains(Integer.parseInt(array[0])))  {
					if( !NSGAII.parameterlocation.contains( Integer.parseInt(array[0]))) {
						
						int index1=-1;
						int index2=-1;
						int indexmax1=-1;
						int indexmax2=-1;
						boolean findmax=false;
						boolean countusing=false;
						
						//System.out.println(solution.getCoSolution().getOp().listpropertynamelocation);	
						//System.out.println("bbbbbbbbbbbbbbbbbbbb");	
						boolean locatedinfilter=false;
						// agar joz binding hast type left hand side ra be dast miare 
						// agar joz folter hast example negah midare be jash
						for(int i=0;i<solution.getCoSolution().getOp().listpropertynamelocation.size();i++) {
							
							for(int j=0;j<solution.getCoSolution().getOp().listpropertynamelocation.get(i).size();j++) {
								
								if(solution.getCoSolution().getOp().listpropertynamelocation.get(i).get(j)==Integer.parseInt(array[0]))
								{
									index1=i;
									index2=j;
								}
								if(solution.getCoSolution().getOp().listpropertynamelocation.get(i).get(j)>Integer.parseInt(array[0]) && findmax==false)
								{
									findmax=true;
									indexmax1=i;
									indexmax2=j;
								}
								
									
							}
							
							// agar beyne akharin binding dar avali rule va avalin binding rule badi bashe yani toye filter hast
					/*		if( i<solution.getCoSolution().getOp().listpropertynamelocation.size()-2 && 
									solution.getCoSolution().getOp().listpropertynamelocation.get(i).size()>0
									&& solution.getCoSolution().getOp().listpropertynamelocation.get(i+1).size()>0)
							{
								if(  Integer.parseInt(array[0])> solution.getCoSolution().getOp().listpropertynamelocation.get(i).get( solution.getCoSolution().getOp().listpropertynamelocation.get(i).size()-1) 
									&& Integer.parseInt(array[0])< solution.getCoSolution().getOp().listpropertynamelocation.get(i+1).get( 0))
								locatedinfilter=true;
							}*/
								
						}
						
						for(int i=0;i<NSGAII.locationfrom.size();i++) {
							if(Integer.parseInt(array[0])>NSGAII.locationfrom.get(i) && Integer.parseInt(array[0])<NSGAII.locationfilter.get(i))
								locatedinfilter=true;
							
						}
						if(index1==-1 && index2==-1) {
							index1=indexmax1;
							index2=indexmax2-1;
							
						}
						/*System.out.println(locatedinfilter);
						System.out.println(index1);	
						System.out.println(index2);	
						System.out.println("indexmax1");	
						
						System.out.println(countusing);	
						System.out.println(indexmax1);	
						System.out.println(indexmax2);	*/
						if(indexrule<NSGAII.preconditionlocation.size() ) {
						if(NSGAII.preconditionlocation.get( indexrule ) > Integer.parseInt(array[0]))
						{
							countusing=true;
						}
						}
						if(countusing==false) {	
						Setting s=new Setting();
						String MMRootPath3     = s.getsourcemetamodel();
						String MMRootPath2     = s.gettargetmetamodel();
						 MetaModel meta=null;
						 MetaModel metatarget=null;
						 try {
							meta=new MetaModel(MMRootPath3);
							metatarget=new MetaModel(MMRootPath2);
						} catch (transException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						 List<String> mainclass = new ArrayList<String>();
						 int yy=0;
						 
						 List<EStructuralFeature> mainclass4 = new ArrayList<EStructuralFeature>();
					//	 List<EStructuralFeature> maintarget = new ArrayList<EStructuralFeature>();
						 List<EStructuralFeature> maintarget2 = new ArrayList<EStructuralFeature>();
						 for (EClassifier classifier : meta.getEClassifiers()) {
								if (classifier instanceof EClass) {
									for (int  y=0;y<classifier.eContents().size();y++)
									{
										
										if (classifier.eContents().get(y) instanceof EAttribute
												||classifier.eContents().get(y) instanceof EReference ) {
											
											mainclass4.add( (EStructuralFeature) classifier.eContents().get(y));
											
											
											
										}
											
									}	
								}
								}
						 for (EStructuralFeature o : mainclass4) {
								if (o!=null) {
									mainclass.add(o.getName());
								}
							}
					/*	 for (EClassifier classifier : metatarget.getEClassifiers()) {
								if (classifier instanceof EClass) {
									
									for (int  y=0;y<classifier.eContents().size();y++)
									{
										
											
										
										if (classifier.eContents().get(y) instanceof EAttribute
												||classifier.eContents().get(y) instanceof EReference ) {
											
											maintarget.add( (EStructuralFeature) classifier.eContents().get(y));
											
											
											
										}
										
									}
									
								}
								}*/
						 
						int index=0;
						int upperbound=0;
				/*		 for(int h=0;h<replacements.size();h++) {
							 for(int h2=0;h2<maintarget.size();h2++) {
								 if(replacements.get(h).equals(maintarget.get(h2).getName()))
									 maintarget2.add( maintarget.get(h2));
									
							 }
							 }*/
						
						 String typeoutput=null;
						 if(index1>=0 && index2==-1) {
							 
							 typeoutput="example";
							 
						 }
						 else {
							 // type class left hand side binding bar migarde
							 // agar toye filter bashe hich type bar nemigarde
							 for(int i=0;i<mainclass.size();i++) 
								 if(solution.getCoSolution().getOp().listpropertyname.get(index1).get(index2).equals( mainclass.get(i).toString()))
								 {
										typeoutput=mainclass4.get(i).getEType().getName();
										upperbound=mainclass4.get(i).getUpperBound();
										
								 }
							 
							 if(typeoutput==null)

								 typeoutput="example";
						 }
						
						
						
						/*System.out.println(typeoutput);
						System.out.println(locatedinfilter);
						System.out.println( replacements);
						System.out.println(NSGAII.listnavigationtype);
					//	int retval =replacements.indexOf(navigation);
						System.out.println(navigation);
					//	System.out.println(retval);
						
						System.out.println("listinpattern");
						System.out.println(upperbound);
						System.out.println(Integer.parseInt(array[0]));
						System.out.println(locatedinfilter);
						System.out.println(classifier2);*/
						boolean unitfind = false;
						boolean checkcondition=false;
						int randomInt2 = -2;
						while (unitfind == false) {
							randomInt2 = FindSecondIndex(randomInt2, replacements.size());
							
							//System.out.println( NSGAII.listnavigationtype.get(randomInt2));
						//	System.out.println( replacements);
						//	System.out.println(NSGAII.listnavigationtype);
							
							// agar type left hand side az noe string bashe 
							if(typeoutput.equals("EString") || typeoutput.equals("String") && randomInt2!=564) {
								String[] array2 = variables.get(randomInt+1).getLocation().split(":", 2);
								//System.out.println(Integer.parseInt(array2[0]));
								//System.out.println(Integer.parseInt(array[0]));
								
								if(Integer.parseInt(array2[0])!=Integer.parseInt(array[0]) && Math.abs(Integer.parseInt(array2[0])-Integer.parseInt(array[0]))!=1) {
									if(!NSGAII.iterationcall.contains(Integer.parseInt(array[0]) )) {
										
										// agar in khat iteration ha (select, collect) nadashte bashim 
									if( NSGAII.listnavigationtype.get(randomInt2).equals("EString") || NSGAII.listnavigationtype.get(randomInt2).equals("String")) {
										System.out.println("111111111111");
										
										if (!navigation.equals( replacements.get(randomInt2).toString()))
											unitfind = true;
										else {
											
											unitfind = true;
											checkcondition=true;
											checkmutationapply = true;
											
										}
										
										List<InPatternElement> modifiable = (List<InPatternElement>) wrapper.allObjectsOf(InPatternElement.class);
										EObject oldFeaturepattern = null;
										for(int i=0;i<modifiable.size();i++) {
											EStructuralFeature featureinpattern2 = wrapper.source(modifiable.get(i)).eClass()
													.getEStructuralFeature("type");
											EObject object2modifyinpattern2 = wrapper.source(modifiable.get(i));
											EObject oldFeaturepattern2 = (EObject) object2modifyinpattern2.eGet(featureinpattern2);
											String[] array3 = modifiable.get(i).getLocation().split(":", 2);
											if(Integer.parseInt(array3[0]) < NSGAII.finalrule.get( indexrule) && Integer.parseInt(array3[0])>NSGAII.faultrule.get(indexrule) ) {
												
												oldFeaturepattern=oldFeaturepattern2;
											}
											
										}
										
										int indexclassname=NSGAII.classnamestring.indexOf( toString(oldFeaturepattern));
										
										for(int i=NSGAII.classnamestartpoint.get(indexclassname);i<NSGAII.classnamestartpoint.get(indexclassname)+NSGAII.classnamelength.get(indexclassname);i++) {
											if(NSGAII.listinheritattributesourcemetamodel.get(i).getName().equals( replacements.get(randomInt2).toString()) && !NSGAII.listinheritattributesourcemetamodel.get(i).getEType().getName().equals("String") && !NSGAII.listinheritattributesourcemetamodel.get(i).getEType().getName().equals("EString") )
												{unitfind = false;
												
												
												}
										}
										
										
										
										
										
									}
									}
										else {
											
											if(!NSGAII.listnavigationtype.get(randomInt2).equals("EString") && !NSGAII.listnavigationtype.get(randomInt2).equals("String") &&  !NSGAII.listnavigationtype.get(randomInt2).equals("EBoolean"))
											{
												// agar left hand side hamchonan string bashe va in khat iteration ha (select, collect) dashte bashim upperbound bayad -1 bashe
												if(NSGAII.iterationcall.contains(Integer.parseInt(array[0]) ))
												{
													if (!navigation.equals( replacements.get(randomInt2).toString())&&  NSGAII.listsourcemetamodel.get(randomInt2).getUpperBound() == -1)
													{
														unitfind = true;
													System.out.println("222222222");
													}
														
												}
												
											}
											
											
										
										}
									
									
									
									
									
									
								}
								else {
									
									if(!NSGAII.qumecall.contains( Integer.parseInt(array[0])) ) {
										
									if( !NSGAII.listnavigationtype.get(randomInt2).equals("EString") && !NSGAII.listnavigationtype.get(randomInt2).equals("String") && !NSGAII.listnavigationtype.get(randomInt2).equals("EBoolean")) {
										
										if (!navigation.equals( replacements.get(randomInt2).toString()))
										{
											unitfind = true;
											System.out.println("3333333");
										}
										
									}
									
									}
									else {
										
										
										List<InPatternElement> modifiable = (List<InPatternElement>) wrapper.allObjectsOf(InPatternElement.class);
										EObject oldFeaturepattern = null;
										for(int i=0;i<modifiable.size();i++) {
											EStructuralFeature featureinpattern2 = wrapper.source(modifiable.get(i)).eClass()
													.getEStructuralFeature("type");
											EObject object2modifyinpattern2 = wrapper.source(modifiable.get(i));
											EObject oldFeaturepattern2 = (EObject) object2modifyinpattern2.eGet(featureinpattern2);
											String[] array3 = modifiable.get(i).getLocation().split(":", 2);
											if(Integer.parseInt(array3[0]) < NSGAII.finalrule.get( indexrule) && Integer.parseInt(array3[0])>NSGAII.faultrule.get(indexrule) ) {
												
												oldFeaturepattern=oldFeaturepattern2;
											}
											
										}
										
										int indexclassname=NSGAII.classnamestring.indexOf( toString(oldFeaturepattern));
										
										
										
										
										
										
										//System.out.println(NSGAII.listsourcemetamodel.get(randomInt2).getUpperBound());
										if( NSGAII.listnavigationtype.get(randomInt2).equals("EString") || NSGAII.listnavigationtype.get(randomInt2).equals("String") && NSGAII.listsourcemetamodel.get(randomInt2).getUpperBound() == 1) {
											
											if (!navigation.equals( replacements.get(randomInt2).toString()) )
											{
												unitfind = true;
												System.out.println("8888888");
											}
											
										}
										for(int i=NSGAII.classnamestartpoint.get(indexclassname);i<NSGAII.classnamestartpoint.get(indexclassname)+NSGAII.classnamelength.get(indexclassname);i++) {
											if(NSGAII.listinheritattributesourcemetamodel.get(i).getName().equals( replacements.get(randomInt2).toString()) && !NSGAII.listinheritattributesourcemetamodel.get(i).getEType().getName().equals("String") && !NSGAII.listinheritattributesourcemetamodel.get(i).getEType().getName().equals("EString") )
												{unitfind = false;
												
												
												}
										}
										
										
										
										
									}
								}
								
									
									
							}
							
							if(typeoutput.equals("EBoolean") ) {
								if(NSGAII.listnavigationtype.get(randomInt2).equals("EBoolean"))
									if (!navigation.equals( replacements.get(randomInt2).toString()))
									unitfind = true;	
								
							}
							if(locatedinfilter==true && randomInt2!=564) {
								// toye filter bashe vali hamin khat iterationha bashan
								if(NSGAII.iterationcall.contains(Integer.parseInt(array[0]) ))
								{
									
									List<InPatternElement> modifiable = (List<InPatternElement>) wrapper.allObjectsOf(InPatternElement.class);
									EObject oldFeaturepattern = null;
									
									for(int i=0;i<modifiable.size();i++) {
										EStructuralFeature featureinpattern2 = wrapper.source(modifiable.get(i)).eClass()
												.getEStructuralFeature("type");
										EObject object2modifyinpattern2 = wrapper.source(modifiable.get(i));
										EObject oldFeaturepattern2 = (EObject) object2modifyinpattern2.eGet(featureinpattern2);
										String[] array2 = modifiable.get(i).getLocation().split(":", 2);
										if(Integer.parseInt(array2[0]) < NSGAII.finalrule.get( indexrule) && Integer.parseInt(array2[0])>NSGAII.faultrule.get(indexrule) ) {
											
											oldFeaturepattern=oldFeaturepattern2;
										}
										
									}
									
									
									
									
									
									int indexclassname=NSGAII.classnamestring.indexOf( toString(oldFeaturepattern));
									
									
									
								//	System.out.println("ppppppppppp");
									if (!navigation.equals( replacements.get(randomInt2).toString())&&  NSGAII.listsourcemetamodel.get(randomInt2).getUpperBound() == -1
											&& !NSGAII.listnavigationtype.get(randomInt2).equals("EString") &&  !NSGAII.listnavigationtype.get(randomInt2).equals("String"))
									{
										unitfind = true;
										
									}
									
									
									
									for(int i=NSGAII.classnamestartpoint.get(indexclassname);i<NSGAII.classnamestartpoint.get(indexclassname)+NSGAII.classnamelength.get(indexclassname);i++) {
										if(NSGAII.listinheritattributesourcemetamodel.get(i).getName().equals( replacements.get(randomInt2).toString()) && NSGAII.listinheritattributesourcemetamodel.get(i).getUpperBound()==1 )
											{unitfind = false;
											
											
											}
									}
									
								}
								else { // baraye navigation toye filter bashe vali to in khat iteration ha nabashan
									
									if (!navigation.equals( replacements.get(randomInt2).toString())&&  NSGAII.listsourcemetamodel.get(randomInt2).getUpperBound() == -1
										)
										unitfind = true;
									
									
								}
								
							}
						/*	if( NSGAII.listnot.contains(Integer.parseInt(array[0]))) {
								if(NSGAII.listnavigationtype.get(randomInt2).equals("EBoolean"))
								if (!navigation.equals( replacements.get(randomInt2).toString()))
								unitfind = true;
							}*/
							if(!typeoutput.equals("EString") && !typeoutput.equals("String") && !typeoutput.equals("EBoolean") && locatedinfilter==false && randomInt2!=564) {
								
							    //  System.out.println(NSGAII.listnavigationtype.get(randomInt2));
							    //  System.out.println(replacements.get(randomInt2).toString());
							    //  System.out.println(maintarget.get(randomInt2).getLowerBound());
							    //  System.out.println(maintarget.get(randomInt2).getUpperBound());
							
								
								    //  System.out.println(NSGAII.listnavigationtype.get(randomInt2));
								    //  System.out.println(replacements.get(randomInt2).toString());
								    //  System.out.println(maintarget.get(randomInt2).getLowerBound());
								    //  System.out.println(maintarget.get(randomInt2).getUpperBound());
									if(!NSGAII.qumecall.contains( Integer.parseInt(array[0]))) {
									if(!NSGAII.listnavigationtype.get(randomInt2).equals("EString") &&  !NSGAII.listnavigationtype.get(randomInt2).equals("String") && !NSGAII.listnavigationtype.get(randomInt2).equals("EBoolean"))
									{
										if(NSGAII.iterationcall.contains(Integer.parseInt(array[0]) ))
										{
											
											// Type left hand side string nabashe va in khat iteration bashe
											if (!navigation.equals( replacements.get(randomInt2).toString())&&  NSGAII.listsourcemetamodel.get(randomInt2).getUpperBound() == -1 )
												unitfind = true;
											
											List<InPatternElement> modifiable = (List<InPatternElement>) wrapper.allObjectsOf(InPatternElement.class);
											EObject oldFeaturepattern = null;
											
											for(int i=0;i<modifiable.size();i++) {
												EStructuralFeature featureinpattern2 = wrapper.source(modifiable.get(i)).eClass()
														.getEStructuralFeature("type");
												EObject object2modifyinpattern2 = wrapper.source(modifiable.get(i));
												EObject oldFeaturepattern2 = (EObject) object2modifyinpattern2.eGet(featureinpattern2);
												String[] array2 = modifiable.get(i).getLocation().split(":", 2);
												if(Integer.parseInt(array2[0]) < NSGAII.finalrule.get( indexrule) && Integer.parseInt(array2[0])>NSGAII.faultrule.get(indexrule) ) {
													
													oldFeaturepattern=oldFeaturepattern2;
												}
												
											}
											
											int indexclassname=NSGAII.classnamestring.indexOf( toString(oldFeaturepattern));
											
											for(int i=NSGAII.classnamestartpoint.get(indexclassname);i<NSGAII.classnamestartpoint.get(indexclassname)+NSGAII.classnamelength.get(indexclassname);i++) {
												if(NSGAII.listinheritattributesourcemetamodel.get(i).getName().equals( replacements.get(randomInt2).toString()) && NSGAII.listinheritattributesourcemetamodel.get(i).getUpperBound()==1 )
													{unitfind = false;
													
													
													}
											}
											
											
											
											
											
											
											
											
												
										}
										else {
											
										if (!navigation.equals( replacements.get(randomInt2).toString())&& (NSGAII.listsourcemetamodel.get(randomInt2).getLowerBound() != 0))
										unitfind = true;
										}
									}
									}
									else {
										String[] array2 = variables.get(randomInt+1).getLocation().split(":", 2);
										
									/*	if(Integer.parseInt(array2[0])!=Integer.parseInt(array[0])) {
										
											
										if( NSGAII.listnavigationtype.get(randomInt2).equals("EString")|| NSGAII.listnavigationtype.get(randomInt2).equals("String") ) {
											
											if (!navigation.equals( replacements.get(randomInt2).toString()))
											{
												unitfind = true;
												System.out.println("5555555555");
											}
											else {
												
												unitfind = true;
												checkcondition=true;
												checkmutationapply = true;
												
											}
										}
										
										}*/
									//	else { NSGAII.listnavigationtype.get(randomInt2).equals("EString") && NSGAII.listnavigationtype.get(randomInt2).equals("String")&& !NSGAII.listnavigationtype.get(randomInt2).equals("EBoolean")
											
											if( NSGAII.listnavigationtype.get(randomInt2).equals("EString") || NSGAII.listnavigationtype.get(randomInt2).equals("String")) {
												
												
												if (!navigation.equals( replacements.get(randomInt2).toString()))
													
												{
													System.out.println("6666666");
													System.out.println(NSGAII.listnavigationtype.get(randomInt2));
													unitfind = true;
													
												}
												List<InPatternElement> modifiable = (List<InPatternElement>) wrapper.allObjectsOf(InPatternElement.class);
												EObject oldFeaturepattern = null;
												
												for(int i=0;i<modifiable.size();i++) {
													EStructuralFeature featureinpattern2 = wrapper.source(modifiable.get(i)).eClass()
															.getEStructuralFeature("type");
													EObject object2modifyinpattern2 = wrapper.source(modifiable.get(i));
													EObject oldFeaturepattern2 = (EObject) object2modifyinpattern2.eGet(featureinpattern2);
													String[] array3 = modifiable.get(i).getLocation().split(":", 2);
													if(Integer.parseInt(array3[0]) < NSGAII.finalrule.get( indexrule) && Integer.parseInt(array3[0])>NSGAII.faultrule.get(indexrule) ) {
														
														oldFeaturepattern=oldFeaturepattern2;
													}
													
												}
												
												int indexclassname=NSGAII.classnamestring.indexOf( toString(oldFeaturepattern));
												
												for(int i=NSGAII.classnamestartpoint.get(indexclassname);i<NSGAII.classnamestartpoint.get(indexclassname)+NSGAII.classnamelength.get(indexclassname);i++) {
													if(NSGAII.listinheritattributesourcemetamodel.get(i).getName().equals( replacements.get(randomInt2).toString())
															&& !NSGAII.listinheritattributesourcemetamodel.get(i).getEType().getName().equals("String") && !NSGAII.listinheritattributesourcemetamodel.get(i).getEType().getName().equals("EString"))
														{unitfind = false;
														
														
														}
												}
												
												
												
												
												
												
											}
											
											
								//		}
										
										
										
										
									}
								
								
								
								
								
							}
							
							
							
							
							
						}
						
						/*System.out.println("type3");
						
						
						System.out.println(toString(navigationExpression, variables.get(randomInt)) +
								navigation );
						System.out.println("navigation2");
						System.out.println(NSGAII.listsourcemetamodel.get(randomInt2));
						System.out.println(NSGAII.listsourcemetamodel.get(randomInt2).getUpperBound());
						
						System.out.println(randomInt);
						System.out.println(randomInt2);*/
						
						
						if(checkcondition==false) {
						//wrapper.source(navigationExpression).eSet(featureDefinition, replacements.get(randomInt2));
						wrapper.source(navigationExpression).eSet(featureDefinition, replacements.get(randomInt2));
						StoreTwoIndex(randomInt, randomInt2, -2);
						System.out.println("\n-- MUTATION \"" + this.getDescription() + "\" from " + toString(navigationExpression, variables.get(randomInt)) +
								navigation + " to " + toString(navigationExpression, variables.get(randomInt)) + replacements.get(randomInt2)   +
								" (line " + ((LocatedElement)navigationExpression).getLocation() + " of original transformation)\n");
						comments.add("\n-- MUTATION \"" + this.getDescription() + "\" from " + toString(navigationExpression, variables.get(randomInt)) +
								navigation + " to " + toString(navigationExpression, variables.get(randomInt)) + replacements.get(randomInt2)   +
								" (line " + ((LocatedElement)navigationExpression).getLocation() + " of original transformation)\n");
						comment = "\n-- MUTATION \"" + this.getDescription() + "\" from " + toString(navigationExpression, variables.get(randomInt)) +
								navigation + " to " + toString(navigationExpression, variables.get(randomInt)) + replacements.get(randomInt2)   +
								" (line " + ((LocatedElement)navigationExpression).getLocation() + " of original transformation)\n";
						NSGAII.numop = NSGAII.numop + 1;
						checkmutationapply = true;
						ReturnResult.set(0, wrapper);
						ReturnResult.set(1, atlModel);
						ReturnResult.add(comment);
						
						}	
				}
					
						
						
		}
				//	for (Object replacement : replacements) {
			/*		if(replacements.size()>0){
						wrapper.source(navigationExpression).eSet(featureDefinition, replacements.get(randomInt2));
						System.out.println(variables.get(randomInt));
						System.out.println(replacements.get(randomInt2));
						// mutation: documentation
						//if (comments!=null) comments.add("\n-- MUTATION \"" + this.getDescription() + "\" from " + toString(navigationExpression, variable) + navigation + " to " + toString(navigationExpression, variable) + replacement  + " (line " + ((LocatedElement)navigationExpression).getLocation() + " of original transformation)\n");
						//if (comments!=null) comments.add("\n-- MUTATION \"" + this.getDescription() + "\" from " + toString(navigationExpression, variables.get(randomInt)) + navigation + " to " + toString(navigationExpression, variables.get(randomInt)) + replacements.get(randomInt2)  + " (line " + ((LocatedElement)navigationExpression).getLocation() + " of original transformation)\n");
							//System.out.println("toString(navigationExpression, variable)= "+toString(navigationExpression, variable));
							//System.out.println("toString(navigationExpression)= "+toString(navigationExpression));
							//System.out.println("variable="+toString(variable));
							//System.out.println("navigation="+navigation);
						    //System.out.println("replacement="+replacement);
							// save mutant
						System.out.println("\n-- MUTATION \"" + this.getDescription() + "\" from " + toString(navigationExpression, variables.get(randomInt)) + navigation + " to " + toString(navigationExpression, variables.get(randomInt)) + replacements.get(randomInt2)   + " (line " + ((LocatedElement)navigationExpression).getLocation() + " of original transformation)\n");
					//	System.out.println("navigation");
						this.save(this.atlModel3, outputFolder);
						this.save2(this.atlModel3, outputFolder);
                        

						// remove comment
					//	if (comments!=null) comments.remove(comments.size()-1);
				//	}
					}*/
					// restore original value
				//	AbstractDeletionMutator.getWrapper().source(navigationExpression).eSet(featureDefinition, navigation);					
				}
//		}
		}
		}
				
				// continue navigation in current expression (e.g. object.feature1.feature2)
		/*		if (navigationExpression instanceof NavigationOrAttributeCallExp && navigationExpression.eContainer() instanceof NavigationOrAttributeCallExp) {
					navigationExpression = navigationExpression.eContainer();
				}
				else navigationExpression = null;*/
		//	}
		}
				return ReturnResult;
	}

	


	private List<Object> OperationPreviousGenerationModefyBindingNavigation(int randomInt, Solution solution,
			EMFModel atlModel, MetaModel inputMM, List<VariableExp> variables, ATLModel wrapper, Object object,
			EDataTypeEList<String> comments, List<Object> ReturnResult, MetaModel outputMM) {
		//List<Object> ReturnResult = new ArrayList<Object>();
		EObject navigationExpression = variables.get(randomInt).eContainer();
				if (navigationExpression instanceof NavigationOrAttributeCallExp) {
					
					EStructuralFeature featureDefinition = wrapper.source(navigationExpression).eClass().getEStructuralFeature("name");
					Object object2modify_src2 = wrapper.source(navigationExpression).eGet(featureDefinition);
				//	String type       = getType(navigationExpression, variables.get(randomInt), inputMM, outputMM); 
					
					
					String navigation = object2modify_src2.toString();
					String[] array = variables.get(randomInt).getLocation().split(":", 2);
					
					//int indexrule = -1;
					//indexrule = FindIndexRule(array);
					EObject oldFeaturepattern = null;
					if(NSGAII.iterationcall.contains(Integer.parseInt(array[0]) ))
					{
						List<InPatternElement> modifiable = (List<InPatternElement>) wrapper.allObjectsOf(InPatternElement.class);
						
						int indexrule = -1;
						indexrule = FindIndexRule(array);
						for(int i=0;i<modifiable.size();i++) {
							EStructuralFeature featureinpattern2 = wrapper.source(modifiable.get(i)).eClass()
									.getEStructuralFeature("type");
							EObject object2modifyinpattern2 = wrapper.source(modifiable.get(i));
							EObject oldFeaturepattern2 = (EObject) object2modifyinpattern2.eGet(featureinpattern2);
							String[] array2 = modifiable.get(i).getLocation().split(":", 2);
							if(Integer.parseInt(array2[0]) < NSGAII.finalrule.get( indexrule) && Integer.parseInt(array2[0])>NSGAII.faultrule.get(indexrule) ) {
								
								oldFeaturepattern=oldFeaturepattern2;
							}
							
						}
						
						
						
						
						
					}
					String comment = null;
					
					
				//		List<InPatternElement> modifiable2 = (List<InPatternElement>) wrapper.allObjectsOf(InPatternElement.class);
					/*	EStructuralFeature featureDefinitionpattern = wrapper.source(modifiable2.get(indexrule)).eClass()
								.getEStructuralFeature("type");
						EObject object2modify_src = wrapper.source(modifiable2.get(indexrule));
						EObject oldFeatureValue = (EObject) object2modify_src.eGet(featureDefinitionpattern);
						EClassifier classifier2 = inputMM.getEClassifier(
								toString(oldFeatureValue));*/
						
						List<Object> replacements = this.replacementsnavigation(atlModel, null,null, navigation, inputMM);
						int randomInt2 = -2;
						randomInt2 = FindSecondIndex(randomInt2, replacements.size());
						boolean check=false;
						if(oldFeaturepattern!=null) {
						int indexclassname=NSGAII.classnamestring.indexOf( toString(oldFeaturepattern));
						for(int i=NSGAII.classnamestartpoint.get(indexclassname);i<NSGAII.classnamestartpoint.get(indexclassname)+NSGAII.classnamelength.get(indexclassname);i++) {
							if(NSGAII.listinheritattributesourcemetamodel.get(i).getName().equals( replacements.get(randomInt2).toString()) && NSGAII.listinheritattributesourcemetamodel.get(i).getUpperBound()==1 )
								{check=true;
								
								
								}
						}
						}
						
						if(check==false) {
						wrapper.source(navigationExpression).eSet(featureDefinition, replacements.get(randomInt2));
						StoreTwoIndex(randomInt, randomInt2, -2);
						
						comments.add("\n-- MUTATION \"" + this.getDescription() + "\" from " + toString(navigationExpression, variables.get(randomInt)) +
								navigation + " to " + toString(navigationExpression, variables.get(randomInt)) + replacements.get(randomInt2)   +
								" (line " + ((LocatedElement)navigationExpression).getLocation() + " of original transformation)\n");
						comment = "\n-- MUTATION \"" + this.getDescription() + "\" from " + toString(navigationExpression, variables.get(randomInt)) +
								navigation + " to " + toString(navigationExpression, variables.get(randomInt)) + replacements.get(randomInt2)   +
								" (line " + ((LocatedElement)navigationExpression).getLocation() + " of original transformation)\n";
						System.out.println( "\n-- MUTATION \"" + this.getDescription() + "\" from " + toString(navigationExpression, variables.get(randomInt)) +
								navigation + " to " + toString(navigationExpression, variables.get(randomInt)) + replacements.get(randomInt2)   +
								" (line " + ((LocatedElement)navigationExpression).getLocation() + " of original transformation)\n");
						NSGAII.writer.println( "\n-- MUTATION \"" + this.getDescription() + "\" from " + toString(navigationExpression, variables.get(randomInt)) +
								navigation + " to " + toString(navigationExpression, variables.get(randomInt)) + replacements.get(randomInt2)   +
								" (line " + ((LocatedElement)navigationExpression).getLocation() + " of original transformation)\n");
						NSGAII.numop = NSGAII.numop + 1;
						
						ReturnResult.set(0, wrapper);
						ReturnResult.set(1, atlModel);
						ReturnResult.add(comment);
							
						}
						else {
							
							setvariable(-2, -2, -2);
							
						}
					
				}
		
		
		
		
		// TODO Auto-generated method stub
		return ReturnResult;
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

	private List<Object> replacementsnavigation(EMFModel atlModel, EClass oldFeatureValue, String type,
			String navigation, MetaModel inputMM) {
		
	//	Setting s=new Setting();
		//List<Object> replacements = new ArrayList<Object>();
		 List<Object> mainclass = new ArrayList<Object>();	 
	/*	String MMRootPath3     = s.gettargetmetamodel();
		 MetaModel meta=null;
		 try {
			meta=new MetaModel(MMRootPath3);
		} catch (transException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 NSGAII.listnavigationtype.clear();
		 NSGAII.listnavigationtype = new ArrayList<String>();	
		int yy=0;
		
		EClass child2 = null;
		 List<Object> mainclass = new ArrayList<Object>();	 
		 List<EStructuralFeature> mainclass4 = new ArrayList<EStructuralFeature>();
		 for (EClassifier classifier : meta.getEClassifiers()) {
				if (classifier instanceof EClass) {
					EClass child = ((EClass) classifier);
			 //     if(child.getName().equals(oldFeatureValue.getName())) {
			    	  child2=child;
			    	 
					for (int  y=0;y<classifier.eContents().size();y++)
					{
						
						if (classifier.eContents().get(y) instanceof EAttribute
								||classifier.eContents().get(y) instanceof EReference ) {
							mainclass4.add( (EStructuralFeature) classifier.eContents().get(y));
						    
						NSGAII.listnavigationtype.add(mainclass4.get(yy).getEType().getName());
						yy=yy+1;
						}
							
					}
		
			//	}
			/*    */
			      
			      
			      
			      
		//		}
				
				
		//	}
		 
	/*	 for(EClassifier classifier2: child2.getEAllSuperTypes()) {
		    	
		    	for (int  y=0;y<classifier2.eContents().size();y++)
				{
		    		
					if (classifier2.eContents().get(y) instanceof EAttribute
							||classifier2.eContents().get(y) instanceof EReference ) {
						mainclass4.add( (EStructuralFeature) classifier2.eContents().get(y));
					
					NSGAII.listnavigationtype.add(mainclass4.get(yy).getEType().getName());
					yy=yy+1;
					}
						
				}
		    	
		    	
		    	
		    }*/

		 for (EStructuralFeature o : NSGAII.listsourcemetamodel) {
				if (o!=null) {
					mainclass.add(o.getName());
				}
			}
		 
		
		// TODO Auto-generated method stub
		return mainclass;
	}


	@Override
	public String getDescription() {
		return "Navigation Modification";
	}

	/**
	 * @param containee
	 * @param container
	 */
	private String toString (EObject container, EObject containee) {
		EObject next   = containee;
		String  string = "";
		do {
			string += toString(next) + ".";
			next    = next.eContainer(); 
		} while (next!=container && next!=null);
		return string;
	}

	/**
	 * It navigates from the variable "containee" to the navigation expression "container", and returns the type of "container".
	 * @param container
	 * @param containee
	 * @param inputMM
	 * @param outputMM
	 * @return
	 */
	private String getType (EObject container, VariableExp containee, MetaModel inputMM, MetaModel outputMM) {
		EClassifier         c   = null;
		VariableDeclaration def = containee.getReferredVariable();
		
		// obtain type (classifier) of variable expression ..............................
		// case 1 -> in pattern element
		if(def!=null) {
		if (def instanceof InPatternElement) {
			System.out.println("1111111111111");
			c = inputMM.getEClassifier(def.getType().getName());
			System.out.println(c);
		}
		// case 2 -> for each out pattern element
		else if (def instanceof ForEachOutPatternElement) {
			System.out.println("2222222222222");
			def = ((ForEachOutPatternElement)def).getIterator();
			if (def.eContainer() instanceof OutPatternElement) {
				OutPatternElement element = (OutPatternElement)def.eContainer();
				if (element.getType() instanceof OclModelElement)
					c = outputMM.getEClassifier(((OclModelElement)element.getType()).getName());
			}
		}
		// case 3 -> iterator
		else if (def instanceof Iterator) {
			System.out.println("3333333333333");
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
			System.out.println(c);
		}
		// case 4 -> variable declaration
		else {
			if (toString(def).equals("self")) {
				System.out.println("4444444444");
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
				System.out.println("5555555555");
				c = inputMM.getEClassifier(((VariableDeclaration)def).getType().getName());
			}
			else if (((VariableDeclaration)def).getType() instanceof CollectionType) {
				System.out.println("6666666666");
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


	@Override
	protected List<Object> replacements(EMFModel atlModel, EClass oldFeatureValue, EObject object2modify,
			String currentAttributeValue, MetaModel metamodel) {
		// TODO Auto-generated method stub
		
		System.out.println("replacement navigation");
		return null;
	}



	


	

	
	
}
