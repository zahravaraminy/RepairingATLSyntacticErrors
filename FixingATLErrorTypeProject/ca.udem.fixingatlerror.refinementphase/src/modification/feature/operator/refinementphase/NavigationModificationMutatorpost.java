package modification.feature.operator.refinementphase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import anatlyzer.atlext.OCL.Attribute;
import anatlyzer.atlext.OCL.CollectionType;
import anatlyzer.atlext.OCL.Iterator;
import anatlyzer.atlext.OCL.LoopExp;
import anatlyzer.atlext.OCL.NavigationOrAttributeCallExp;
import anatlyzer.atlext.OCL.OclExpression;
import anatlyzer.atlext.OCL.OclModelElement;
import anatlyzer.atlext.OCL.Operation;
import anatlyzer.atlext.OCL.Parameter;
import anatlyzer.atlext.OCL.PropertyCallExp;
import anatlyzer.atlext.OCL.VariableDeclaration;
import anatlyzer.atlext.OCL.VariableExp;
import anatlyzer.atl.model.ATLModel;
//import anatlyzer.evaluation.mutators.ATLModel;
import deletion.operator.refinementphase.AbstractDeletionMutator;
import ca.udem.fixingatlerror.refinementphase.Setting;
import jmetal.core.Solution;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.metaheuristics.nsgaII.NSGAIIpostprocessing;
import jmetal.problems.MyProblem;
import transML.exceptions.transException;

public class NavigationModificationMutatorpost extends AbstractFeatureModificationMutator {
	private String m = "C:/Users/varaminz/Downloads/Project/workspaceTEST34/anatlyzer.atl.tests.api/trafo2/zahra2mutants/finalresult.atl";
	private EMFModel atlModel3;
	private int indexrulespecificinput = -1;

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
	public List<Object> generateMutants(EMFModel atlModel, MetaModel inputMM, MetaModel outputMM, String outputFolder,
			ATLModel wrapper, Solution solution) {

		List<Object> ReturnResult = new ArrayList<Object>();
		List<VariableExp> variables = (List<VariableExp>) wrapper.allObjectsOf(VariableExp.class);

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
		// Module module = AbstractDeletionMutator.getWrapper().getModule();
		int randomInt = -2;
		boolean checkmutationapply = false;
		int count = -1;
		while (checkmutationapply == false) {

			count = count + 1;
			List<Integer> Result = ReturnFirstIndex(randomInt, variables.size(), checkmutationapply, count, solution);
			randomInt = Result.get(0);

			String[] array = variables.get(randomInt).getLocation().split(":", 2);
			if (Result.get(1) == 0)
				checkmutationapply = false;
			else
				checkmutationapply = false;
			if (randomInt == -2)
				checkmutationapply = true;

			else if (solution.getpreviousgeneration() == true) {

				ReturnResult = OperationPreviousGenerationModefyBindingNavigation(randomInt, solution, atlModel,
						inputMM, variables, wrapper, null, comments, ReturnResult, outputMM);
				checkmutationapply = true;

			}
			// navigate navigation expressions starting from each variable
			// for (VariableExp variable : variables) {
			// else if (variables.size() > 0 && randomInt != -2 &&
			// solution.getpreviousgeneration() == false &&
			// Integer.parseInt(array[0])>NSGAII.faultrule.get(0)) {
			else if (variables.size() > 0 && randomInt != -2) {
				EObject navigationExpression = variables.get(randomInt).eContainer();

				if (navigationExpression instanceof NavigationOrAttributeCallExp) {

					EStructuralFeature featureDefinition = wrapper.source(navigationExpression).eClass()
							.getEStructuralFeature("name");
					Object object2modify_src2 = wrapper.source(navigationExpression).eGet(featureDefinition);
					// System.out.println(object2modify_src2.toString());
					// System.out.println(Integer.parseInt(array[0]));
					// System.out.println("collectionlocation");
					// System.out.println(randomInt);
					// obtain list of replacements

					String navigation = object2modify_src2.toString();

					ReturnResult = AnalysisNavigationBinding(solution, wrapper, inputMM, outputMM, atlModel, comments,
							ReturnResult, variables, outputFolder);
					checkmutationapply = true;

					// String type = getType(navigationExpression, variables.get(randomInt),
					// inputMM, outputMM);

					// String navigation =
					// ((NavigationOrAttributeCallExp)navigationExpression).getName();

					// EObject oldFeatureValue2 = (EObject)
					// object2modify_src2.eGet(featureDefinition);
					// System.out.println(type);

					// if(type!=null) {
					/*
					 * System.out.println(object2modify_src2);
					 * System.out.println(object2modify_src2.toString()); System.out.println(type);
					 * System.out.println(navigation);
					 * System.out.println(Integer.parseInt(array[0]));
					 */
					int indexrule = -1;
					indexrule = FindIndexRule(array);
					// System.out.println(indexrule);
					if (indexrule == 100) {
						List<InPatternElement> modifiable2 = (List<InPatternElement>) wrapper
								.allObjectsOf(InPatternElement.class);
						EStructuralFeature featureDefinitionpattern = wrapper.source(modifiable2.get(indexrule))
								.eClass().getEStructuralFeature("type");
						EObject object2modify_src = wrapper.source(modifiable2.get(indexrule));
						EObject oldFeatureValue = (EObject) object2modify_src.eGet(featureDefinitionpattern);

						EClassifier classifier2 = inputMM.getEClassifier(toString(oldFeatureValue));
						List<Object> replacements = this.replacementsnavigation(atlModel, (EClass) classifier2, null,
								navigation, inputMM);

						// for PNML2PetriNet Class2Relational if(NSGAII.locationfilter.contains(
						// Integer.parseInt(array[0])) && NSGAII.parameterlocation.contains(
						// Integer.parseInt(array[0])) &&
						// NSGAII.listnot.contains(Integer.parseInt(array[0])))
						// checkmutationapply = true;
						// for class 2rel type!=null
						// for PNML2PetriNet if(!NSGAII.locationfilter.contains(
						// Integer.parseInt(array[0])) && !NSGAII.parameterlocation.contains(
						// Integer.parseInt(array[0]))
						// && !NSGAII.listnot.contains(Integer.parseInt(array[0]))) {
						if (!NSGAII.parameterlocation.contains(Integer.parseInt(array[0]))) {

							int index1 = -1;
							int index2 = -1;
							int indexmax1 = -1;
							int indexmax2 = -1;
							boolean findmax = false;
							boolean countusing = false;

							// System.out.println(solution.getCoSolution().getOp().listpropertynamelocation);
							// System.out.println("bbbbbbbbbbbbbbbbbbbb");
							boolean locatedinfilter = false;
							// agar joz binding hast type left hand side ra be dast miare
							// agar joz folter hast example negah midare be jash
							for (int i = 0; i < solution.getCoSolution().getOp().listpropertynamelocation.size(); i++) {

								for (int j = 0; j < solution.getCoSolution().getOp().listpropertynamelocation.get(i)
										.size(); j++) {

									if (solution.getCoSolution().getOp().listpropertynamelocation.get(i)
											.get(j) == Integer.parseInt(array[0])) {
										index1 = i;
										index2 = j;
									}
									if (solution.getCoSolution().getOp().listpropertynamelocation.get(i)
											.get(j) > Integer.parseInt(array[0]) && findmax == false) {
										findmax = true;
										indexmax1 = i;
										indexmax2 = j;
									}

								}

								// agar beyne akharin binding dar avali rule va avalin binding rule badi bashe
								// yani toye filter hast
								/*
								 * if( i<solution.getCoSolution().getOp().listpropertynamelocation.size()-2 &&
								 * solution.getCoSolution().getOp().listpropertynamelocation.get(i).size()>0 &&
								 * solution.getCoSolution().getOp().listpropertynamelocation.get(i+1).size()>0)
								 * { if( Integer.parseInt(array[0])>
								 * solution.getCoSolution().getOp().listpropertynamelocation.get(i).get(
								 * solution.getCoSolution().getOp().listpropertynamelocation.get(i).size()-1) &&
								 * Integer.parseInt(array[0])<
								 * solution.getCoSolution().getOp().listpropertynamelocation.get(i+1).get( 0))
								 * locatedinfilter=true; }
								 */

							}

							for (int i = 0; i < NSGAII.locationfrom.size(); i++) {
								if (Integer.parseInt(array[0]) > NSGAII.locationfrom.get(i)
										&& Integer.parseInt(array[0]) < NSGAII.locationfilter.get(i))
									locatedinfilter = true;

							}
							if (index1 == -1 && index2 == -1) {
								index1 = indexmax1;
								index2 = indexmax2 - 1;

							}
							/*
							 * System.out.println(locatedinfilter); System.out.println(index1);
							 * System.out.println(index2); System.out.println("indexmax1");
							 * 
							 * System.out.println(countusing); System.out.println(indexmax1);
							 * System.out.println(indexmax2);
							 */
							if (indexrule < NSGAII.preconditionlocation.size()) {
								if (NSGAII.preconditionlocation.get(indexrule) > Integer.parseInt(array[0])) {
									countusing = true;
								}
							}
							if (countusing == false) {
								Setting s = new Setting();
								String MMRootPath3 = s.getsourcemetamodel();
								String MMRootPath2 = s.gettargetmetamodel();
								MetaModel meta = null;
								MetaModel metatarget = null;
								try {
									meta = new MetaModel(MMRootPath3);
									metatarget = new MetaModel(MMRootPath2);
								} catch (transException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								List<String> mainclass = new ArrayList<String>();
								int yy = 0;

								List<EStructuralFeature> mainclass4 = new ArrayList<EStructuralFeature>();
								// List<EStructuralFeature> maintarget = new ArrayList<EStructuralFeature>();
								List<EStructuralFeature> maintarget2 = new ArrayList<EStructuralFeature>();
								for (EClassifier classifier : meta.getEClassifiers()) {
									if (classifier instanceof EClass) {
										for (int y = 0; y < classifier.eContents().size(); y++) {

											if (classifier.eContents().get(y) instanceof EAttribute
													|| classifier.eContents().get(y) instanceof EReference) {

												mainclass4.add((EStructuralFeature) classifier.eContents().get(y));

											}

										}
									}
								}
								for (EStructuralFeature o : mainclass4) {
									if (o != null) {
										mainclass.add(o.getName());
									}
								}
								/*
								 * for (EClassifier classifier : metatarget.getEClassifiers()) { if (classifier
								 * instanceof EClass) {
								 * 
								 * for (int y=0;y<classifier.eContents().size();y++) {
								 * 
								 * 
								 * 
								 * if (classifier.eContents().get(y) instanceof EAttribute
								 * ||classifier.eContents().get(y) instanceof EReference ) {
								 * 
								 * maintarget.add( (EStructuralFeature) classifier.eContents().get(y));
								 * 
								 * 
								 * 
								 * }
								 * 
								 * }
								 * 
								 * } }
								 */

								int index = 0;
								int upperbound = 0;
								/*
								 * for(int h=0;h<replacements.size();h++) { for(int
								 * h2=0;h2<maintarget.size();h2++) {
								 * if(replacements.get(h).equals(maintarget.get(h2).getName())) maintarget2.add(
								 * maintarget.get(h2));
								 * 
								 * } }
								 */

								String typeoutput = null;
								if (index1 >= 0 && index2 == -1) {

									typeoutput = "example";

								} else {
									// type class left hand side binding bar migarde
									// agar toye filter bashe hich type bar nemigarde
									for (int i = 0; i < mainclass.size(); i++)
										if (solution.getCoSolution().getOp().listpropertyname.get(index1).get(index2)
												.equals(mainclass.get(i).toString())) {
											typeoutput = mainclass4.get(i).getEType().getName();
											upperbound = mainclass4.get(i).getUpperBound();

										}

									if (typeoutput == null)

										typeoutput = "example";
								}

								/*
								 * System.out.println(typeoutput); System.out.println(locatedinfilter);
								 * System.out.println( replacements);
								 * System.out.println(NSGAII.listnavigationtype); // int retval
								 * =replacements.indexOf(navigation); System.out.println(navigation); //
								 * System.out.println(retval);
								 * 
								 * System.out.println("listinpattern"); System.out.println(upperbound);
								 * System.out.println(Integer.parseInt(array[0]));
								 * System.out.println(locatedinfilter); System.out.println(classifier2);
								 */
								boolean unitfind = false;
								boolean checkcondition = false;
								int randomInt2 = -2;
								while (unitfind == false) {
									randomInt2 = FindSecondIndex(randomInt2, replacements.size());

									// System.out.println( NSGAII.listnavigationtype.get(randomInt2));
									// System.out.println( replacements);
									// System.out.println(NSGAII.listnavigationtype);

									// agar type left hand side az noe string bashe
									if (typeoutput.equals("EString")
											|| typeoutput.equals("String") && randomInt2 != 564) {
										String[] array2 = variables.get(randomInt + 1).getLocation().split(":", 2);
										// System.out.println(Integer.parseInt(array2[0]));
										// System.out.println(Integer.parseInt(array[0]));

										if (Integer.parseInt(array2[0]) != Integer.parseInt(array[0]) && Math
												.abs(Integer.parseInt(array2[0]) - Integer.parseInt(array[0])) != 1) {
											if (!NSGAII.iterationcall.contains(Integer.parseInt(array[0]))) {

												// agar in khat iteration ha (select, collect) nadashte bashim
												if (NSGAII.listnavigationtype.get(randomInt2).equals("EString")
														|| NSGAII.listnavigationtype.get(randomInt2).equals("String")) {
													System.out.println("111111111111");

													if (!navigation.equals(replacements.get(randomInt2).toString()))
														unitfind = true;
													else {

														unitfind = true;
														checkcondition = true;
														checkmutationapply = true;

													}

													List<InPatternElement> modifiable = (List<InPatternElement>) wrapper
															.allObjectsOf(InPatternElement.class);
													EObject oldFeaturepattern = null;
													for (int i = 0; i < modifiable.size(); i++) {
														EStructuralFeature featureinpattern2 = wrapper
																.source(modifiable.get(i)).eClass()
																.getEStructuralFeature("type");
														EObject object2modifyinpattern2 = wrapper
																.source(modifiable.get(i));
														EObject oldFeaturepattern2 = (EObject) object2modifyinpattern2
																.eGet(featureinpattern2);
														String[] array3 = modifiable.get(i).getLocation().split(":", 2);
														if (Integer.parseInt(array3[0]) < NSGAII.finalrule
																.get(indexrule)
																&& Integer.parseInt(array3[0]) > NSGAII.faultrule
																		.get(indexrule)) {

															oldFeaturepattern = oldFeaturepattern2;
														}

													}

													int indexclassname = NSGAII.classnamestring
															.indexOf(toString(oldFeaturepattern));

													for (int i = NSGAII.classnamestartpoint
															.get(indexclassname); i < NSGAII.classnamestartpoint
																	.get(indexclassname)
																	+ NSGAII.classnamelength.get(indexclassname); i++) {
														if (NSGAII.listinheritattributesourcemetamodel.get(i).getName()
																.equals(replacements.get(randomInt2).toString())
																&& !NSGAII.listinheritattributesourcemetamodel.get(i)
																		.getEType().getName().equals("String")
																&& !NSGAII.listinheritattributesourcemetamodel.get(i)
																		.getEType().getName().equals("EString")) {
															unitfind = false;

														}
													}

												}
											} else {

												if (!NSGAII.listnavigationtype.get(randomInt2).equals("EString")
														&& !NSGAII.listnavigationtype.get(randomInt2).equals("String")
														&& !NSGAII.listnavigationtype.get(randomInt2)
																.equals("EBoolean")) {
													// agar left hand side hamchonan string bashe va in khat iteration
													// ha (select, collect) dashte bashim upperbound bayad -1 bashe
													if (NSGAII.iterationcall.contains(Integer.parseInt(array[0]))) {
														if (!navigation.equals(replacements.get(randomInt2).toString())
																&& NSGAII.listsourcemetamodel.get(randomInt2)
																		.getUpperBound() == -1) {
															unitfind = true;
															System.out.println("222222222");
														}

													}

												}

											}

										} else {

											if (!NSGAII.qumecall.contains(Integer.parseInt(array[0]))) {

												if (!NSGAII.listnavigationtype.get(randomInt2).equals("EString")
														&& !NSGAII.listnavigationtype.get(randomInt2).equals("String")
														&& !NSGAII.listnavigationtype.get(randomInt2)
																.equals("EBoolean")) {

													if (!navigation.equals(replacements.get(randomInt2).toString())) {
														unitfind = true;
														System.out.println("3333333");
													}

												}

											} else {

												List<InPatternElement> modifiable = (List<InPatternElement>) wrapper
														.allObjectsOf(InPatternElement.class);
												EObject oldFeaturepattern = null;
												for (int i = 0; i < modifiable.size(); i++) {
													EStructuralFeature featureinpattern2 = wrapper
															.source(modifiable.get(i)).eClass()
															.getEStructuralFeature("type");
													EObject object2modifyinpattern2 = wrapper.source(modifiable.get(i));
													EObject oldFeaturepattern2 = (EObject) object2modifyinpattern2
															.eGet(featureinpattern2);
													String[] array3 = modifiable.get(i).getLocation().split(":", 2);
													if (Integer.parseInt(array3[0]) < NSGAII.finalrule.get(indexrule)
															&& Integer.parseInt(array3[0]) > NSGAII.faultrule
																	.get(indexrule)) {

														oldFeaturepattern = oldFeaturepattern2;
													}

												}

												int indexclassname = NSGAII.classnamestring
														.indexOf(toString(oldFeaturepattern));

												// System.out.println(NSGAII.listsourcemetamodel.get(randomInt2).getUpperBound());
												if (NSGAII.listnavigationtype.get(randomInt2).equals("EString")
														|| NSGAII.listnavigationtype.get(randomInt2).equals("String")
																&& NSGAII.listsourcemetamodel.get(randomInt2)
																		.getUpperBound() == 1) {

													if (!navigation.equals(replacements.get(randomInt2).toString())) {
														unitfind = true;
														System.out.println("8888888");
													}

												}
												for (int i = NSGAII.classnamestartpoint
														.get(indexclassname); i < NSGAII.classnamestartpoint
																.get(indexclassname)
																+ NSGAII.classnamelength.get(indexclassname); i++) {
													if (NSGAII.listinheritattributesourcemetamodel.get(i).getName()
															.equals(replacements.get(randomInt2).toString())
															&& !NSGAII.listinheritattributesourcemetamodel.get(i)
																	.getEType().getName().equals("String")
															&& !NSGAII.listinheritattributesourcemetamodel.get(i)
																	.getEType().getName().equals("EString")) {
														unitfind = false;

													}
												}

											}
										}

									}

									if (typeoutput.equals("EBoolean")) {
										if (NSGAII.listnavigationtype.get(randomInt2).equals("EBoolean"))
											if (!navigation.equals(replacements.get(randomInt2).toString()))
												unitfind = true;

									}
									if (locatedinfilter == true && randomInt2 != 564) {
										// toye filter bashe vali hamin khat iterationha bashan
										if (NSGAII.iterationcall.contains(Integer.parseInt(array[0]))) {

											List<InPatternElement> modifiable = (List<InPatternElement>) wrapper
													.allObjectsOf(InPatternElement.class);
											EObject oldFeaturepattern = null;

											for (int i = 0; i < modifiable.size(); i++) {
												EStructuralFeature featureinpattern2 = wrapper.source(modifiable.get(i))
														.eClass().getEStructuralFeature("type");
												EObject object2modifyinpattern2 = wrapper.source(modifiable.get(i));
												EObject oldFeaturepattern2 = (EObject) object2modifyinpattern2
														.eGet(featureinpattern2);
												String[] array2 = modifiable.get(i).getLocation().split(":", 2);
												if (Integer.parseInt(array2[0]) < NSGAII.finalrule.get(indexrule)
														&& Integer.parseInt(array2[0]) > NSGAII.faultrule
																.get(indexrule)) {

													oldFeaturepattern = oldFeaturepattern2;
												}

											}

											int indexclassname = NSGAII.classnamestring
													.indexOf(toString(oldFeaturepattern));

											// System.out.println("ppppppppppp");
											if (!navigation.equals(replacements.get(randomInt2).toString())
													&& NSGAII.listsourcemetamodel.get(randomInt2).getUpperBound() == -1
													&& !NSGAII.listnavigationtype.get(randomInt2).equals("EString")
													&& !NSGAII.listnavigationtype.get(randomInt2).equals("String")) {
												unitfind = true;

											}

											for (int i = NSGAII.classnamestartpoint.get(
													indexclassname); i < NSGAII.classnamestartpoint.get(indexclassname)
															+ NSGAII.classnamelength.get(indexclassname); i++) {
												if (NSGAII.listinheritattributesourcemetamodel.get(i).getName()
														.equals(replacements.get(randomInt2).toString())
														&& NSGAII.listinheritattributesourcemetamodel.get(i)
																.getUpperBound() == 1) {
													unitfind = false;

												}
											}

										} else { // baraye navigation toye filter bashe vali to in khat iteration ha
													// nabashan

											if (!navigation.equals(replacements.get(randomInt2).toString())
													&& NSGAII.listsourcemetamodel.get(randomInt2).getUpperBound() == -1)
												unitfind = true;

										}

									}
									/*
									 * if( NSGAII.listnot.contains(Integer.parseInt(array[0]))) {
									 * if(NSGAII.listnavigationtype.get(randomInt2).equals("EBoolean")) if
									 * (!navigation.equals( replacements.get(randomInt2).toString())) unitfind =
									 * true; }
									 */
									if (!typeoutput.equals("EString") && !typeoutput.equals("String")
											&& !typeoutput.equals("EBoolean") && locatedinfilter == false
											&& randomInt2 != 564) {

										// System.out.println(NSGAII.listnavigationtype.get(randomInt2));
										// System.out.println(replacements.get(randomInt2).toString());
										// System.out.println(maintarget.get(randomInt2).getLowerBound());
										// System.out.println(maintarget.get(randomInt2).getUpperBound());

										// System.out.println(NSGAII.listnavigationtype.get(randomInt2));
										// System.out.println(replacements.get(randomInt2).toString());
										// System.out.println(maintarget.get(randomInt2).getLowerBound());
										// System.out.println(maintarget.get(randomInt2).getUpperBound());
										if (!NSGAII.qumecall.contains(Integer.parseInt(array[0]))) {
											if (!NSGAII.listnavigationtype.get(randomInt2).equals("EString")
													&& !NSGAII.listnavigationtype.get(randomInt2).equals("String")
													&& !NSGAII.listnavigationtype.get(randomInt2).equals("EBoolean")) {
												if (NSGAII.iterationcall.contains(Integer.parseInt(array[0]))) {

													// Type left hand side string nabashe va in khat iteration bashe
													if (!navigation.equals(replacements.get(randomInt2).toString())
															&& NSGAII.listsourcemetamodel.get(randomInt2)
																	.getUpperBound() == -1)
														unitfind = true;

													List<InPatternElement> modifiable = (List<InPatternElement>) wrapper
															.allObjectsOf(InPatternElement.class);
													EObject oldFeaturepattern = null;

													for (int i = 0; i < modifiable.size(); i++) {
														EStructuralFeature featureinpattern2 = wrapper
																.source(modifiable.get(i)).eClass()
																.getEStructuralFeature("type");
														EObject object2modifyinpattern2 = wrapper
																.source(modifiable.get(i));
														EObject oldFeaturepattern2 = (EObject) object2modifyinpattern2
																.eGet(featureinpattern2);
														String[] array2 = modifiable.get(i).getLocation().split(":", 2);
														if (Integer.parseInt(array2[0]) < NSGAII.finalrule
																.get(indexrule)
																&& Integer.parseInt(array2[0]) > NSGAII.faultrule
																		.get(indexrule)) {

															oldFeaturepattern = oldFeaturepattern2;
														}

													}

													int indexclassname = NSGAII.classnamestring
															.indexOf(toString(oldFeaturepattern));

													for (int i = NSGAII.classnamestartpoint
															.get(indexclassname); i < NSGAII.classnamestartpoint
																	.get(indexclassname)
																	+ NSGAII.classnamelength.get(indexclassname); i++) {
														if (NSGAII.listinheritattributesourcemetamodel.get(i).getName()
																.equals(replacements.get(randomInt2).toString())
																&& NSGAII.listinheritattributesourcemetamodel.get(i)
																		.getUpperBound() == 1) {
															unitfind = false;

														}
													}

												} else {

													if (!navigation.equals(replacements.get(randomInt2).toString())
															&& (NSGAII.listsourcemetamodel.get(randomInt2)
																	.getLowerBound() != 0))
														unitfind = true;
												}
											}
										} else {
											String[] array2 = variables.get(randomInt + 1).getLocation().split(":", 2);

											/*
											 * if(Integer.parseInt(array2[0])!=Integer.parseInt(array[0])) {
											 * 
											 * 
											 * if( NSGAII.listnavigationtype.get(randomInt2).equals("EString")||
											 * NSGAII.listnavigationtype.get(randomInt2).equals("String") ) {
											 * 
											 * if (!navigation.equals( replacements.get(randomInt2).toString())) {
											 * unitfind = true; System.out.println("5555555555"); } else {
											 * 
											 * unitfind = true; checkcondition=true; checkmutationapply = true;
											 * 
											 * } }
											 * 
											 * }
											 */
											// else { NSGAII.listnavigationtype.get(randomInt2).equals("EString") &&
											// NSGAII.listnavigationtype.get(randomInt2).equals("String")&&
											// !NSGAII.listnavigationtype.get(randomInt2).equals("EBoolean")

											if (NSGAII.listnavigationtype.get(randomInt2).equals("EString")
													|| NSGAII.listnavigationtype.get(randomInt2).equals("String")) {

												if (!navigation.equals(replacements.get(randomInt2).toString()))

												{
													System.out.println("6666666");
													System.out.println(NSGAII.listnavigationtype.get(randomInt2));
													unitfind = true;

												}
												List<InPatternElement> modifiable = (List<InPatternElement>) wrapper
														.allObjectsOf(InPatternElement.class);
												EObject oldFeaturepattern = null;

												for (int i = 0; i < modifiable.size(); i++) {
													EStructuralFeature featureinpattern2 = wrapper
															.source(modifiable.get(i)).eClass()
															.getEStructuralFeature("type");
													EObject object2modifyinpattern2 = wrapper.source(modifiable.get(i));
													EObject oldFeaturepattern2 = (EObject) object2modifyinpattern2
															.eGet(featureinpattern2);
													String[] array3 = modifiable.get(i).getLocation().split(":", 2);
													if (Integer.parseInt(array3[0]) < NSGAII.finalrule.get(indexrule)
															&& Integer.parseInt(array3[0]) > NSGAII.faultrule
																	.get(indexrule)) {

														oldFeaturepattern = oldFeaturepattern2;
													}

												}

												int indexclassname = NSGAII.classnamestring
														.indexOf(toString(oldFeaturepattern));

												for (int i = NSGAII.classnamestartpoint
														.get(indexclassname); i < NSGAII.classnamestartpoint
																.get(indexclassname)
																+ NSGAII.classnamelength.get(indexclassname); i++) {
													if (NSGAII.listinheritattributesourcemetamodel.get(i).getName()
															.equals(replacements.get(randomInt2).toString())
															&& !NSGAII.listinheritattributesourcemetamodel.get(i)
																	.getEType().getName().equals("String")
															&& !NSGAII.listinheritattributesourcemetamodel.get(i)
																	.getEType().getName().equals("EString")) {
														unitfind = false;

													}
												}

											}

											// }

										}

									}

								}

								/*
								 * System.out.println("type3");
								 * 
								 * 
								 * System.out.println(toString(navigationExpression, variables.get(randomInt)) +
								 * navigation ); System.out.println("navigation2");
								 * System.out.println(NSGAII.listsourcemetamodel.get(randomInt2));
								 * System.out.println(NSGAII.listsourcemetamodel.get(randomInt2).getUpperBound()
								 * );
								 * 
								 * System.out.println(randomInt); System.out.println(randomInt2);
								 */

								if (checkcondition == false) {
									// wrapper.source(navigationExpression).eSet(featureDefinition,
									// replacements.get(randomInt2));
									wrapper.source(navigationExpression).eSet(featureDefinition,
											replacements.get(randomInt2));
									StoreTwoIndex(randomInt, randomInt2, -2);
									System.out.println("\n-- MUTATION \"" + this.getDescription() + "\" from "
											+ toString(navigationExpression, variables.get(randomInt)) + navigation
											+ " to " + toString(navigationExpression, variables.get(randomInt))
											+ replacements.get(randomInt2) + " (line "
											+ ((LocatedElement) navigationExpression).getLocation()
											+ " of original transformation)\n");
									comments.add("\n-- MUTATION \"" + this.getDescription() + "\" from "
											+ toString(navigationExpression, variables.get(randomInt)) + navigation
											+ " to " + toString(navigationExpression, variables.get(randomInt))
											+ replacements.get(randomInt2) + " (line "
											+ ((LocatedElement) navigationExpression).getLocation()
											+ " of original transformation)\n");
									comment = "\n-- MUTATION \"" + this.getDescription() + "\" from "
											+ toString(navigationExpression, variables.get(randomInt)) + navigation
											+ " to " + toString(navigationExpression, variables.get(randomInt))
											+ replacements.get(randomInt2) + " (line "
											+ ((LocatedElement) navigationExpression).getLocation()
											+ " of original transformation)\n";
									NSGAII.numop = NSGAII.numop + 1;
									checkmutationapply = true;
									ReturnResult.set(0, wrapper);
									ReturnResult.set(1, atlModel);
									ReturnResult.add(comment);

								}
							}

						}
						// for (Object replacement : replacements) {
						/*
						 * if(replacements.size()>0){
						 * wrapper.source(navigationExpression).eSet(featureDefinition,
						 * replacements.get(randomInt2)); System.out.println(variables.get(randomInt));
						 * System.out.println(replacements.get(randomInt2)); // mutation: documentation
						 * //if (comments!=null) comments.add("\n-- MUTATION \"" + this.getDescription()
						 * + "\" from " + toString(navigationExpression, variable) + navigation + " to "
						 * + toString(navigationExpression, variable) + replacement + " (line " +
						 * ((LocatedElement)navigationExpression).getLocation() +
						 * " of original transformation)\n"); //if (comments!=null)
						 * comments.add("\n-- MUTATION \"" + this.getDescription() + "\" from " +
						 * toString(navigationExpression, variables.get(randomInt)) + navigation +
						 * " to " + toString(navigationExpression, variables.get(randomInt)) +
						 * replacements.get(randomInt2) + " (line " +
						 * ((LocatedElement)navigationExpression).getLocation() +
						 * " of original transformation)\n");
						 * //System.out.println("toString(navigationExpression, variable)= "+toString(
						 * navigationExpression, variable));
						 * //System.out.println("toString(navigationExpression)= "+toString(
						 * navigationExpression)); //System.out.println("variable="+toString(variable));
						 * //System.out.println("navigation="+navigation);
						 * //System.out.println("replacement="+replacement); // save mutant
						 * System.out.println("\n-- MUTATION \"" + this.getDescription() + "\" from " +
						 * toString(navigationExpression, variables.get(randomInt)) + navigation +
						 * " to " + toString(navigationExpression, variables.get(randomInt)) +
						 * replacements.get(randomInt2) + " (line " +
						 * ((LocatedElement)navigationExpression).getLocation() +
						 * " of original transformation)\n"); // System.out.println("navigation");
						 * this.save(this.atlModel3, outputFolder); this.save2(this.atlModel3,
						 * outputFolder);
						 * 
						 * 
						 * // remove comment // if (comments!=null) comments.remove(comments.size()-1);
						 * // } }
						 */
						// restore original value
						// AbstractDeletionMutator.getWrapper().source(navigationExpression).eSet(featureDefinition,
						// navigation);
					}
					// }
				}
			}

			// continue navigation in current expression (e.g. object.feature1.feature2)
			/*
			 * if (navigationExpression instanceof NavigationOrAttributeCallExp &&
			 * navigationExpression.eContainer() instanceof NavigationOrAttributeCallExp) {
			 * navigationExpression = navigationExpression.eContainer(); } else
			 * navigationExpression = null;
			 */
			// }
		}
		return ReturnResult;
	}

	private List<Object> AnalysisNavigationBinding(Solution solution, ATLModel wrapper, MetaModel inputMM,
			MetaModel outputMM, EMFModel atlModel, EDataTypeEList<String> comments, List<Object> ReturnResult,
			List<VariableExp> variables, String outputFolder) {

		String feature = "type";
		List<InPatternElement> modifiable = (List<InPatternElement>) wrapper.allObjectsOf(InPatternElement.class);
		for (int h = 0; h < solution.getCoSolutionpostprocessing().getOp().listpropertyname.size(); h++) {

			String specificoutput = ReturnSpecificOutput(wrapper, feature, h);
			String specificinput = ReturnSpecificInput(wrapper, feature, h, modifiable);

			System.out.println("index9");
			System.out.println(NSGAIIpostprocessing.errorrule);
			System.out.println(this.indexrulespecificinput);
			if (NSGAIIpostprocessing.errorrule.contains(this.indexrulespecificinput)) {
				System.out.println("out");
				System.out.println(specificoutput);
				System.out.println(specificinput);
				for (int h2 = 0; h2 < solution.getCoSolutionpostprocessing().getOp().listpropertyname.get(h)
						.size(); h2++) {
					//

					String typhelper = IfRightHandSideISHelperReturnHelperType2(wrapper, h, h2);
					String usingcondition = null;
					if (typhelper == null)
						usingcondition = IfRightHandSideIsUsingCondition(wrapper, h, h2);
					String extractedtypemeta = null;
					if (typhelper == null && usingcondition == null)
						extractedtypemeta = ExtractTypeRightHandSideIfThereisnotHelper(h, h2, specificinput);

					ReturnResult = IfRightHandSideBindingIsBooleanType(typhelper, usingcondition, extractedtypemeta,
							solution, wrapper, inputMM, outputMM, atlModel, comments, ReturnResult, modifiable,
							outputFolder, variables, h, h2, specificoutput, specificinput);

				}

			}

		}

		// TODO Auto-generated method stub
		return ReturnResult;
	}

	private String ReturnTypeLeftHandSideBinding(String specificoutput, String leftsidebinding) {
		// TODO Auto-generated method stub
		String typenavigation = null;
		int indexclassname = NSGAIIpostprocessing.classnamestringtarget.indexOf(specificoutput);
		// String typenavigation=null;
		for (int i = NSGAIIpostprocessing.classnamestartpointtarget
				.get(indexclassname); i < NSGAIIpostprocessing.classnamestartpointtarget.get(indexclassname)
						+ NSGAIIpostprocessing.classnamelengthtarget.get(indexclassname); i++) {

			// hameye attributha ke in class dare check kon
			if (NSGAIIpostprocessing.listinheritattributesourcemetamodeltarget.get(i).getName()
					.equals(leftsidebinding)) {

				typenavigation = NSGAIIpostprocessing.listnavigationtypeinheritattrtarget.get(i);
				// System.out.println(leftsidebinding);
				// System.out.println(typenavigation);
				// indexattrnavi=indexattrnavi+1;

			}
		}

		// leftsidebinding.getCoSolutionpostprocessing().getOp().listpropertyname.get(h).get(h2)
		return typenavigation;
	}

	private List<Object> IfRightHandSideBindingIsBooleanType(String typhelper, String usingcondition,
			String extractedtypemeta, Solution solution, ATLModel wrapper, MetaModel inputMM, MetaModel outputMM,
			EMFModel atlModel, EDataTypeEList<String> comments, List<Object> ReturnResult,
			List<InPatternElement> modifiable, String outputFolder, List<VariableExp> variables, int h, int h2,
			String specificoutput, String specificinput) {

		System.out.println("typright");
		System.out.println(typhelper);
		System.out.println(usingcondition);
		System.out.println(extractedtypemeta);
		if (typhelper != null) {

			System.out.println(typhelper);

			if (typhelper.equals("BooleanType")) {
				String typeleftside = ReturnTypeLeftHandSideBinding(specificoutput,
						solution.getCoSolutionpostprocessing().getOp().listpropertyname.get(h).get(h2));
				ReturnResult = ChooseAttributeAndModifyNavigation(typeleftside, specificinput, wrapper, h, h2, solution,
						solution.getCoSolutionpostprocessing().getOp().listpropertyname.get(h).get(h2), inputMM,
						outputMM, atlModel, comments, ReturnResult, modifiable, variables);

				System.out.println(typhelper);
			}
		}

		if (usingcondition != null) {

			System.out.println(usingcondition);

			if (usingcondition.equals("EBoolean") || usingcondition.equals("Boolean")) {
				String typeleftside = ReturnTypeLeftHandSideBinding(specificoutput,
						solution.getCoSolutionpostprocessing().getOp().listpropertyname.get(h).get(h2));
				ReturnResult = ChooseAttributeAndModifyNavigation(typeleftside, specificinput, wrapper, h, h2, solution,
						solution.getCoSolutionpostprocessing().getOp().listpropertyname.get(h).get(h2), inputMM,
						outputMM, atlModel, comments, ReturnResult, modifiable, variables);

				System.out.println(usingcondition);
			}
		}

		if (extractedtypemeta != null) {

			System.out.println(extractedtypemeta);

			if (extractedtypemeta.equals("EBoolean") || extractedtypemeta.equals("Boolean")) {
				String typeleftside = ReturnTypeLeftHandSideBinding(specificoutput,
						solution.getCoSolutionpostprocessing().getOp().listpropertyname.get(h).get(h2));

				ReturnResult = ChooseAttributeAndModifyNavigation(typeleftside, specificinput, wrapper, h, h2, solution,
						solution.getCoSolutionpostprocessing().getOp().listpropertyname.get(h).get(h2), inputMM,
						outputMM, atlModel, comments, ReturnResult, modifiable, variables);

				System.out.println(extractedtypemeta);
				System.out.println("boolean3");

			}
		}
		// TODO Auto-generated method stub
		return ReturnResult;
	}

	private List<Object> ChooseAttributeAndModifyNavigation(String typeleftside, String specificinput, ATLModel wrapper,
			int h, int h2, Solution solution, String string, MetaModel inputMM, MetaModel outputMM, EMFModel atlModel,
			EDataTypeEList<String> comments, List<Object> ReturnResult, List<InPatternElement> modifiable,
			List<VariableExp> variables) {
		// TODO Auto-generated method stub
		if (!typeleftside.equals("EBoolean") && !typeleftside.equals("Boolean")) {

			List<String> allattrleft = ExtractwholeAttrRightSide(specificinput, wrapper, h, h2, solution);
			int randomInt = ChooseRandomnotBooleanattrType(allattrleft,
					solution.getCoSolutionpostprocessing().getOp().listpropertyname.get(h).get(h2), h, h2);
			ReturnResult = ModifyNavigation(solution, wrapper, inputMM, outputMM, atlModel, comments, ReturnResult,
					modifiable, variables,
					solution.getCoSolutionpostprocessing().getOp().listpropertynamelocation.get(h).get(h2),
					allattrleft.get(randomInt), h, h2);

			System.out.println(allattrleft);
		}
		return ReturnResult;
	}

	private List<Object> ModifyNavigation(Solution solution, ATLModel wrapper, MetaModel inputMM, MetaModel outputMM,
			EMFModel atlModel, EDataTypeEList<String> comments, List<Object> ReturnResult,
			List<InPatternElement> modifiable, List<VariableExp> variables, Integer location,
			String candidatenavigation, int h, int h2) {
		// TODO Auto-generated method stub

		for (int i = 0; i < variables.size(); i++) {
			String[] array = variables.get(i).getLocation().split(":", 2);
			EObject navigationExpression = variables.get(i).eContainer();

			if (navigationExpression instanceof NavigationOrAttributeCallExp
					&& (Integer.parseInt(array[0]) == location)) {

				EStructuralFeature featureDefinition = wrapper.source(navigationExpression).eClass()
						.getEStructuralFeature("name");
				Object object2modify_src2 = wrapper.source(navigationExpression).eGet(featureDefinition);
				String navigation = object2modify_src2.toString();
				List<Object> replacements = this.replacementsnavigation(atlModel, null, null, navigation, inputMM);

				int index = -1;
				for (int j = 0; j < replacements.size(); j++) {
					System.out.println((replacements.get(j)).toString());
					if ((replacements.get(j)).toString().equals(candidatenavigation)) {
						index = j;

					}

				}
				System.out.println("oldnavigation");
				System.out.println(navigation);
				System.out.println(replacements.get(index));

				if (!navigation.equals(replacements.get(index).toString())) {
					wrapper.source(navigationExpression).eSet(featureDefinition, replacements.get(index));
					// StoreTwoIndex(randomInt, randomInt2, -2);
					System.out.println("\n-- MUTATION \"" + this.getDescription() + "\" from "
							+ toString(navigationExpression, variables.get(i)) + navigation + " to "
							+ toString(navigationExpression, variables.get(i)) + replacements.get(index) + " (line "
							+ ((LocatedElement) navigationExpression).getLocation() + " of original transformation)\n");
					comments.add("\n-- MUTATION \"" + this.getDescription() + "\" from "
							+ toString(navigationExpression, variables.get(i)) + navigation + " to "
							+ toString(navigationExpression, variables.get(i)) + replacements.get(index) + " (line "
							+ ((LocatedElement) navigationExpression).getLocation() + " of original transformation)\n");
					String comment = "\n-- MUTATION \"" + this.getDescription() + "\" from "
							+ toString(navigationExpression, variables.get(i)) + navigation + " to "
							+ toString(navigationExpression, variables.get(i)) + replacements.get(index) + " (line "
							+ ((LocatedElement) navigationExpression).getLocation() + " of original transformation)\n";
					NSGAII.numop = NSGAII.numop + 1;

					ReturnResult.set(0, wrapper);
					ReturnResult.set(1, atlModel);
					ReturnResult.add(comment);

				}
			}

		}

		return ReturnResult;
	}

	private int ChooseRandomnotBooleanattrType(List<String> allattrleft, String leftsideattribute, int row,
			int column) {

		int indexattr = 0;
		// List<Integer> indexattrwithabsresult = new ArrayList<Integer>();
		// indexattrwithabsresult.add(indexattr );
		// indexattrwithabsresult.add(Math.abs(allattrleft.get(indexattr).compareTo(
		// leftsideattribute) ) );
		System.out.println("attlist");
		// for(int i=0;i<allattrleft.size();i++) {
		// j=j+2 choon 2 ta 2 ta bayad bere jolo avali hamishe attribute hast dovomi
		// hamishe type attribute hast
		for (int j = 0; j < allattrleft.size(); j = j + 2) {
			if (!allattrleft.get(j + 1).equals("Boolean") && !allattrleft.get(j + 1).equals("EBoolean")) {
				if (Math.abs(allattrleft.get(j).compareTo(leftsideattribute)) < Math
						.abs(allattrleft.get(indexattr).compareTo(leftsideattribute))) {
					indexattr = j;
					// indexattrwithabsresult.set(0,indexattr );
					// indexattrwithabsresult.set(1,Math.abs(allattrleft.get(indexattr).compareTo(
					// leftsideattribute) ) );

					System.out.println(allattrleft.get(j).compareTo(leftsideattribute));
					System.out.println(allattrleft.get(j));

				}
			}
		}
		// }
		return indexattr;
	}

	private List<String> ExtractwholeAttrRightSide(String specificinput, ATLModel wrapper, int h, int h2,
			Solution solution) {
		// TODO Auto-generated method stub
		// liste kole attributhaye candid az outpattern element ba typesh toye
		// ListAttrReplace por mishe va barmigarde
		ArrayList<String> ListAttrReplacement = new ArrayList<String>();
		int indexclassname = NSGAIIpostprocessing.classnamestring.indexOf(specificinput);

		// for(int j=0;j<helpernavigation.size();j++) {
		for (int i = NSGAIIpostprocessing.classnamestartpoint
				.get(indexclassname); i < NSGAIIpostprocessing.classnamestartpoint.get(indexclassname)
						+ NSGAIIpostprocessing.classnamelength.get(indexclassname); i++) {
			// System.out.println("listreplace1");
			// System.out.println(NSGAIIpostprocessing.listinheritattributesourcemetamodel.get(i).getName());
			// System.out.println(NSGAIIpostprocessing.listnavigationtypeinheritattr.get(i));
			ListAttrReplacement.add(NSGAIIpostprocessing.listinheritattributesourcemetamodel.get(i).getName());
			ListAttrReplacement.add(NSGAIIpostprocessing.listnavigationtypeinheritattr.get(i));
			// System.out.println("listreplace");

			// System.out.println(ListAttrReplacement);
			// return
			// NSGAIIpostprocessing.listinheritattributesourcemetamodeltarget.get(i).getName();

		}

		// }
		return ListAttrReplacement;
	}

	private String ExtractTypeRightHandSideIfThereisnotHelper(int h, int h2, String specificinput) {
		// TODO Auto-generated method stub
		String typenavigation = null;
		int index = -1;
		// NSGAIIpostprocessing.allattributrightside.get(
		// h).get(h2).get(i).chars().count()==1// harfe aval ye caracter hast n.
		for (int i = 0; i < NSGAIIpostprocessing.allattributrightside.get(h).get(h2).size(); i++) {
			if (index == -1 && (NSGAIIpostprocessing.allattributrightside.get(h).get(h2).get(i).chars().count() == 1
					|| NSGAIIpostprocessing.classnamestring
							.contains(NSGAIIpostprocessing.allattributrightside.get(h).get(h2).get(i)))) {

				index = i;
			}

		}
		System.out.println("rightside");
		System.out.println(NSGAIIpostprocessing.allattributrightside.get(h).get(h2));
		System.out.println(index);
		int indexclassname = -1;
		if (index >= 0) {
			// vatghi n. caracter bood type az classe input pattern shoro mishe
			if (NSGAIIpostprocessing.allattributrightside.get(h).get(h2).get(index).chars().count() == 1) {
				typenavigation = specificinput;
				indexclassname = NSGAIIpostprocessing.classnamestring.indexOf(typenavigation);

			} else {
				// vagarna az type class ke
				typenavigation = NSGAIIpostprocessing.allattributrightside.get(h).get(h2).get(index);
				indexclassname = NSGAIIpostprocessing.classnamestring
						.indexOf(NSGAIIpostprocessing.allattributrightside.get(h).get(h2).get(index));
			}
			/*
			 * int indexclassname=NSGAIIpostprocessing.classnamestring.indexOf(
			 * NSGAIIpostprocessing.allattributrightside.get( h).get(h2).get(0));
			 * if(indexclassname>=0)
			 * typenavigation=NSGAIIpostprocessing.allattributrightside.get(
			 * h).get(h2).get(0); else { typenavigation=specificinput;
			 * indexclassname=NSGAIIpostprocessing.classnamestring.indexOf( typenavigation);
			 * 
			 * }
			 */
			System.out.println("initialtyper");
			System.out.println(typenavigation);
			System.out.println(NSGAIIpostprocessing.allattributrightside.get(h).get(h2).get(1));
			System.out.println("initialtyper");
			for (int j = 1; j < NSGAIIpostprocessing.allattributrightside.get(h).get(h2).size(); j++) {

				// String typenavigation=null;
				for (int i = NSGAIIpostprocessing.classnamestartpoint
						.get(indexclassname); i < NSGAIIpostprocessing.classnamestartpoint.get(indexclassname)
								+ NSGAIIpostprocessing.classnamelength.get(indexclassname); i++) {
					// hameye attributha ke in class dare check kon
					if (NSGAIIpostprocessing.listinheritattributesourcemetamodel.get(i).getName()
							.equalsIgnoreCase(NSGAIIpostprocessing.allattributrightside.get(h).get(h2).get(j))) {

						typenavigation = NSGAIIpostprocessing.listnavigationtypeinheritattr.get(i);

						System.out.println(typenavigation);
						// indexattrnavi=indexattrnavi+1;

					}
				}
				indexclassname = NSGAIIpostprocessing.classnamestring.indexOf(typenavigation);
			}

		}

		return typenavigation;
	}

	private String IfRightHandSideIsUsingCondition(ATLModel wrapper, int h, int h2) {
		// TODO Auto-generated method stub

		List<CollectionType> variables3 = (List<CollectionType>) wrapper.allObjectsOf(CollectionType.class);

		int indexusing = NSGAIIpostprocessing.usingfunction
				.indexOf(NSGAIIpostprocessing.allattributrightside.get(h).get(h2).get(0));
		System.out.println("indexusing");
		System.out.println(NSGAIIpostprocessing.usingfunction);
		System.out.println(NSGAIIpostprocessing.allattributrightside.get(h).get(h2).get(0));
		System.out.println(indexusing);
		if (indexusing >= 0) {

			for (int k = 0; k < variables3.size(); k++) {

				String[] array2 = variables3.get(k).getLocation().split(":", 2);

				EStructuralFeature featureDefinition = wrapper.source(variables3.get(k)).eClass()
						.getEStructuralFeature("elementType");
				EObject object2modify_src = wrapper.source(variables3.get(k));
				EObject oldFeatureValue = (EObject) object2modify_src.eGet(featureDefinition);
				if (NSGAIIpostprocessing.usingline.get(indexusing).equals(Integer.parseInt(array2[0]))) {
					return toString(oldFeatureValue);
				}
				System.out.println("old");
				System.out.println(Integer.parseInt(array2[0]));
				System.out.println(toString(oldFeatureValue));

			}
		}
		return null;
	}

	private String IfRightHandSideISHelperReturnHelperType2(ATLModel wrapper, int h, int h2) {
		// TODO Auto-generated method stub
		List<anatlyzer.atlext.ATL.Helper> helper = (List<Helper>) wrapper
				.allObjectsOf(anatlyzer.atlext.ATL.Helper.class);
		// hameye helperha ro ba samte rast moghayese mikone ta check kone aya samte
		// rast helper hast ya na
		System.out.println(NSGAIIpostprocessing.allattributrightside.get(h).get(h2));
		System.out.println("checkhelper");
		for (int j = 0; j < NSGAIIpostprocessing.allattributrightside.get(h).get(h2).size(); j++)

			// list kole right hand side
			// System.out.println(NSGAIIpostprocessing.allattributrightside.get(
			// h).get(h2).size());
			for (int i = 0; i < helper.size(); i++) {
				{
					String helper_name = helper.get(i).getDefinition().getFeature() instanceof Operation
							? ((Operation) helper.get(i).getDefinition().getFeature()).getName()
							: ((Attribute) helper.get(i).getDefinition().getFeature()).getName();

					// if right hand side is helper
					System.out.println(NSGAIIpostprocessing.allattributrightside.get(h).get(h2).get(j));
					System.out.println(helper_name);

					if (NSGAIIpostprocessing.allattributrightside.get(h).get(h2).get(j).equals(helper_name)) {

						String helpertype = ReturnHelpetType2(wrapper, helper, h, h2, i, j);

						return helpertype;

					}

				}
			}
		return null;

	}

	private String ReturnHelpetType2(ATLModel wrapper, List<Helper> helper, int h, int h2, int i, int j) {
		// TODO Auto-generated method stub
		System.out.println("orek");
		System.out.println(NSGAIIpostprocessing.allattributrightside.get(h).get(h2).get(j));
		String helper_return = helper.get(i).getDefinition().getFeature() instanceof Operation
				? toString(((Operation) helper.get(i).getDefinition().getFeature()).getReturnType())
				: toString(((Attribute) helper.get(i).getDefinition().getFeature()).getType());

		System.out.println(helper_return);

		return helper_return;

	}

	private String ReturnSpecificInput(ATLModel wrapper, String featureName, int h, List<InPatternElement> modifiable) {
		// TODO Auto-generated method stub
		int indexrule = -1;
		List<OutPatternElement> variables = (List<OutPatternElement>) wrapper.allObjectsOf(OutPatternElement.class);
		String[] array = variables.get(h).getLocation().split(":", 2);
		for (int j = 0; j < NSGAIIpostprocessing.faultrule.size(); j++) {
			if (Integer.parseInt(array[0]) > NSGAIIpostprocessing.faultrule.get(j)
					&& Integer.parseInt(array[0]) < NSGAIIpostprocessing.finalrule.get(j))

				indexrule = j;

		}
		this.indexrulespecificinput = indexrule;
		EStructuralFeature featureDefinition = wrapper.source(modifiable.get(indexrule)).eClass()
				.getEStructuralFeature(featureName);
		EObject object2modify_src = wrapper.source(modifiable.get(indexrule));
		EObject oldFeatureValue = (EObject) object2modify_src.eGet(featureDefinition);

		return toString(oldFeatureValue);

	}

	private String ReturnSpecificOutput(ATLModel wrapper, String featureName, int h) {
		// TODO Auto-generated method stub
		List<OutPatternElement> variables = (List<OutPatternElement>) wrapper.allObjectsOf(OutPatternElement.class);
		EStructuralFeature featureDefinitionout = wrapper.source(variables.get(h)).eClass()
				.getEStructuralFeature(featureName);
		EObject object2modify_src2 = wrapper.source(variables.get(h));
		EObject oldFeatureValue2 = (EObject) object2modify_src2.eGet(featureDefinitionout);
		return toString(oldFeatureValue2);

	}

	private List<Object> OperationPreviousGenerationModefyBindingNavigation(int randomInt, Solution solution,
			EMFModel atlModel, MetaModel inputMM, List<VariableExp> variables, ATLModel wrapper, Object object,
			EDataTypeEList<String> comments, List<Object> ReturnResult, MetaModel outputMM) {
		// List<Object> ReturnResult = new ArrayList<Object>();
		EObject navigationExpression = variables.get(randomInt).eContainer();
		if (navigationExpression instanceof NavigationOrAttributeCallExp) {

			EStructuralFeature featureDefinition = wrapper.source(navigationExpression).eClass()
					.getEStructuralFeature("name");
			Object object2modify_src2 = wrapper.source(navigationExpression).eGet(featureDefinition);
			// String type = getType(navigationExpression, variables.get(randomInt),
			// inputMM, outputMM);

			String navigation = object2modify_src2.toString();
			String[] array = variables.get(randomInt).getLocation().split(":", 2);

			// int indexrule = -1;
			// indexrule = FindIndexRule(array);
			EObject oldFeaturepattern = null;
			if (NSGAII.iterationcall.contains(Integer.parseInt(array[0]))) {
				List<InPatternElement> modifiable = (List<InPatternElement>) wrapper
						.allObjectsOf(InPatternElement.class);

				int indexrule = -1;
				indexrule = FindIndexRule(array);
				for (int i = 0; i < modifiable.size(); i++) {
					EStructuralFeature featureinpattern2 = wrapper.source(modifiable.get(i)).eClass()
							.getEStructuralFeature("type");
					EObject object2modifyinpattern2 = wrapper.source(modifiable.get(i));
					EObject oldFeaturepattern2 = (EObject) object2modifyinpattern2.eGet(featureinpattern2);
					String[] array2 = modifiable.get(i).getLocation().split(":", 2);
					if (Integer.parseInt(array2[0]) < NSGAII.finalrule.get(indexrule)
							&& Integer.parseInt(array2[0]) > NSGAII.faultrule.get(indexrule)) {

						oldFeaturepattern = oldFeaturepattern2;
					}

				}

			}
			String comment = null;

			// List<InPatternElement> modifiable2 = (List<InPatternElement>)
			// wrapper.allObjectsOf(InPatternElement.class);
			/*
			 * EStructuralFeature featureDefinitionpattern =
			 * wrapper.source(modifiable2.get(indexrule)).eClass()
			 * .getEStructuralFeature("type"); EObject object2modify_src =
			 * wrapper.source(modifiable2.get(indexrule)); EObject oldFeatureValue =
			 * (EObject) object2modify_src.eGet(featureDefinitionpattern); EClassifier
			 * classifier2 = inputMM.getEClassifier( toString(oldFeatureValue));
			 */

			List<Object> replacements = this.replacementsnavigation(atlModel, null, null, navigation, inputMM);
			int randomInt2 = -2;
			randomInt2 = FindSecondIndex(randomInt2, replacements.size());
			boolean check = false;
			if (oldFeaturepattern != null) {
				int indexclassname = NSGAII.classnamestring.indexOf(toString(oldFeaturepattern));
				for (int i = NSGAII.classnamestartpoint.get(indexclassname); i < NSGAII.classnamestartpoint
						.get(indexclassname) + NSGAII.classnamelength.get(indexclassname); i++) {
					if (NSGAII.listinheritattributesourcemetamodel.get(i).getName()
							.equals(replacements.get(randomInt2).toString())
							&& NSGAII.listinheritattributesourcemetamodel.get(i).getUpperBound() == 1) {
						check = true;

					}
				}
			}

			if (check == false) {
				wrapper.source(navigationExpression).eSet(featureDefinition, replacements.get(randomInt2));
				StoreTwoIndex(randomInt, randomInt2, -2);

				comments.add("\n-- MUTATION \"" + this.getDescription() + "\" from "
						+ toString(navigationExpression, variables.get(randomInt)) + navigation + " to "
						+ toString(navigationExpression, variables.get(randomInt)) + replacements.get(randomInt2)
						+ " (line " + ((LocatedElement) navigationExpression).getLocation()
						+ " of original transformation)\n");
				comment = "\n-- MUTATION \"" + this.getDescription() + "\" from "
						+ toString(navigationExpression, variables.get(randomInt)) + navigation + " to "
						+ toString(navigationExpression, variables.get(randomInt)) + replacements.get(randomInt2)
						+ " (line " + ((LocatedElement) navigationExpression).getLocation()
						+ " of original transformation)\n";
				System.out.println("\n-- MUTATION \"" + this.getDescription() + "\" from "
						+ toString(navigationExpression, variables.get(randomInt)) + navigation + " to "
						+ toString(navigationExpression, variables.get(randomInt)) + replacements.get(randomInt2)
						+ " (line " + ((LocatedElement) navigationExpression).getLocation()
						+ " of original transformation)\n");
				NSGAII.writer.println("\n-- MUTATION \"" + this.getDescription() + "\" from "
						+ toString(navigationExpression, variables.get(randomInt)) + navigation + " to "
						+ toString(navigationExpression, variables.get(randomInt)) + replacements.get(randomInt2)
						+ " (line " + ((LocatedElement) navigationExpression).getLocation()
						+ " of original transformation)\n");
				NSGAII.numop = NSGAII.numop + 1;

				ReturnResult.set(0, wrapper);
				ReturnResult.set(1, atlModel);
				ReturnResult.add(comment);

			} else {

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

		// Setting s=new Setting();
		// List<Object> replacements = new ArrayList<Object>();
		List<Object> mainclass = new ArrayList<Object>();
		/*
		 * String MMRootPath3 = s.gettargetmetamodel(); MetaModel meta=null; try {
		 * meta=new MetaModel(MMRootPath3); } catch (transException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 * NSGAII.listnavigationtype.clear(); NSGAII.listnavigationtype = new
		 * ArrayList<String>(); int yy=0;
		 * 
		 * EClass child2 = null; List<Object> mainclass = new ArrayList<Object>();
		 * List<EStructuralFeature> mainclass4 = new ArrayList<EStructuralFeature>();
		 * for (EClassifier classifier : meta.getEClassifiers()) { if (classifier
		 * instanceof EClass) { EClass child = ((EClass) classifier); //
		 * if(child.getName().equals(oldFeatureValue.getName())) { child2=child;
		 * 
		 * for (int y=0;y<classifier.eContents().size();y++) {
		 * 
		 * if (classifier.eContents().get(y) instanceof EAttribute
		 * ||classifier.eContents().get(y) instanceof EReference ) { mainclass4.add(
		 * (EStructuralFeature) classifier.eContents().get(y));
		 * 
		 * NSGAII.listnavigationtype.add(mainclass4.get(yy).getEType().getName());
		 * yy=yy+1; }
		 * 
		 * }
		 * 
		 * // } /*
		 */

		// }

		// }

		/*
		 * for(EClassifier classifier2: child2.getEAllSuperTypes()) {
		 * 
		 * for (int y=0;y<classifier2.eContents().size();y++) {
		 * 
		 * if (classifier2.eContents().get(y) instanceof EAttribute
		 * ||classifier2.eContents().get(y) instanceof EReference ) { mainclass4.add(
		 * (EStructuralFeature) classifier2.eContents().get(y));
		 * 
		 * NSGAII.listnavigationtype.add(mainclass4.get(yy).getEType().getName());
		 * yy=yy+1; }
		 * 
		 * }
		 * 
		 * 
		 * 
		 * }
		 */

		for (EStructuralFeature o : NSGAIIpostprocessing.listsourcemetamodel) {
			if (o != null) {
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
	private String toString(EObject container, EObject containee) {
		EObject next = containee;
		String string = "";
		do {
			string += toString(next) + ".";
			next = next.eContainer();
		} while (next != container && next != null);
		return string;
	}

	/**
	 * It navigates from the variable "containee" to the navigation expression
	 * "container", and returns the type of "container".
	 * 
	 * @param container
	 * @param containee
	 * @param inputMM
	 * @param outputMM
	 * @return
	 */
	private String getType(EObject container, VariableExp containee, MetaModel inputMM, MetaModel outputMM) {
		EClassifier c = null;
		VariableDeclaration def = containee.getReferredVariable();

		// obtain type (classifier) of variable expression
		// ..............................
		// case 1 -> in pattern element
		if (def != null) {
			if (def instanceof InPatternElement) {
				System.out.println("1111111111111");
				c = inputMM.getEClassifier(def.getType().getName());
				System.out.println(c);
			}
			// case 2 -> for each out pattern element
			else if (def instanceof ForEachOutPatternElement) {
				System.out.println("2222222222222");
				def = ((ForEachOutPatternElement) def).getIterator();
				if (def.eContainer() instanceof OutPatternElement) {
					OutPatternElement element = (OutPatternElement) def.eContainer();
					if (element.getType() instanceof OclModelElement)
						c = outputMM.getEClassifier(((OclModelElement) element.getType()).getName());
				}
			}
			// case 3 -> iterator
			else if (def instanceof Iterator) {
				System.out.println("3333333333333");
				if (def.eContainer() instanceof LoopExp) {
					LoopExp iterator = (LoopExp) def.eContainer();
					OclExpression exp = iterator.getSource();
					while (c == null && exp != null) {
						if (exp instanceof OclModelElement) {
							c = inputMM.getEClassifier(((OclModelElement) exp).getName());
							exp = null;
						} else if (exp instanceof PropertyCallExp) {
							exp = ((PropertyCallExp) exp).getSource();
						} else if (exp instanceof VariableExp) {
							c = inputMM.getEClassifier(getType(container, (VariableExp) exp, inputMM, outputMM));
							exp = null;
						} else
							exp = null;
					}
				}
				System.out.println(c);
			}
			// case 4 -> variable declaration
			else {
				if (toString(def).equals("self")) {
					System.out.println("4444444444");
					EObject helper = containee;
					while (helper != null && !(helper instanceof Helper))
						helper = helper.eContainer();
					if (helper instanceof Helper) {
						if (((Helper) helper).getDefinition().getContext_() != null
								&& ((Helper) helper).getDefinition().getContext_().getContext_() != null
								&& ((Helper) helper).getDefinition().getContext_()
										.getContext_() instanceof OclModelElement)
							c = inputMM.getEClassifier(
									((OclModelElement) ((Helper) helper).getDefinition().getContext_().getContext_())
											.getName());
					}
				} else if (((VariableDeclaration) def).getType() instanceof OclModelElement) {
					System.out.println("5555555555");
					c = inputMM.getEClassifier(((VariableDeclaration) def).getType().getName());
				} else if (((VariableDeclaration) def).getType() instanceof CollectionType) {
					System.out.println("6666666666");
					c = inputMM.getEClassifier(
							((CollectionType) ((VariableDeclaration) def).getType()).getElementType().getName());
				}
			}

			// obtain type (classifier) of container
			// ........................................
			EObject next = containee.eContainer();
			while (c != null && next != null && next != container) {

				if (c instanceof EClass) {
					EStructuralFeature name = next.eClass().getEStructuralFeature("name");
					EStructuralFeature feature = null;

					if (name != null) {
						String nameValue = next.eGet(name).toString();
						feature = ((EClass) c).getEStructuralFeature(nameValue);
					} else {
						System.out.println("Warning: " + next.eClass().getName() + " " + "with null name feature ");
					}
					if (feature != null) {
						c = feature.getEType();
						next = next.eContainer();
					} else
						next = null;
				}
			}
		}
		return c != null ? c.getName() : null;

	}

	@Override
	protected List<Object> replacements(EMFModel atlModel, EClass oldFeatureValue, EObject object2modify,
			String currentAttributeValue, MetaModel metamodel) {
		// TODO Auto-generated method stub

		System.out.println("replacement navigation");
		return null;
	}

}
