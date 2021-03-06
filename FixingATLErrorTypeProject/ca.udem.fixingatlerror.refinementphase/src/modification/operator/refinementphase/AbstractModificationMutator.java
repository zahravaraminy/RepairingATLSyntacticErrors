package modification.operator.refinementphase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.core.ModelFactory;
import org.eclipse.m2m.atl.core.emf.EMFModel;
import org.eclipse.m2m.atl.core.emf.EMFModelFactory;
import org.eclipse.m2m.atl.core.emf.EMFReferenceModel;
import org.eclipse.m2m.atl.engine.parser.AtlParser;

import witness.generator.MetaModel;
import anatlyzer.atlext.ATL.Binding;
import anatlyzer.atlext.ATL.LocatedElement;
import anatlyzer.atlext.ATL.Module;
import anatlyzer.atlext.ATL.OutPatternElement;
import anatlyzer.atlext.OCL.BooleanExp;
import anatlyzer.atlext.OCL.IntegerExp;
import anatlyzer.atlext.OCL.OCLFactory;
import anatlyzer.atlext.OCL.OclExpression;
import anatlyzer.atlext.OCL.RealExp;
import anatlyzer.atlext.OCL.StringExp;
import anatlyzer.atl.model.ATLModel;
//import anatlyzer.evaluation.mutators.ATLModel;
import modification.operator.refinementphase.AbstractMutator;
import ca.udem.fixingatlerror.refinementphase.main;
import ca.udem.fixingatlerror.refinementphase.Operations;
import jmetal.core.Solution;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.problems.MyProblem;
import transML.utils.modeling.EMFUtils;

public abstract class AbstractModificationMutator extends AbstractMutator {

	private EMFModel atlModel3;

	protected abstract List<Object> replacements(EMFModel atlModel, EClass oldFeatureValue, EObject object2modify,
			String currentAttributeValue, MetaModel metamodel);

	/**
	 * Generic modification. It allows subtypes of the class to modify.
	 * 
	 * @param atlModel
	 * @param outputFolder
	 * @param ToModifyClass
	 *            class of objects to modify (example Binding)
	 * @param featureName
	 *            feature to modify (example propertyName)
	 * @param metamodel
	 *            metamodel containing the candidate types for the modification
	 * @return
	 */
	protected <ToModify extends LocatedElement> List<Object> genericAttributeModification(EMFModel atlModel,
			String outputFolder, Class<ToModify> ToModifyClass, String feature, MetaModel metamodel, ATLModel wrapper,
			Solution solution) {
		try {
			return genericAttributeModification(atlModel, outputFolder, ToModifyClass, feature, metamodel, false,
					wrapper, solution);
		} catch (ATLCoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Generic modification. The parameter 'exactContainerType' allows configuring
	 * whether the type of the class to modify must be exactly the one received, or
	 * if the subtypes should be also considered.
	 * 
	 * @param atlModel
	 * @param outputFolder
	 * @param ToModifyClass
	 *            class of objects to modify (example Binding)
	 * @param featureName
	 *            feature to modify (example propertyName)
	 * @param metamodel
	 *            metamodel containing the candidate types for the modification
	 * @param exactToModifyType
	 *            false to consider also subtypes of the class ToModify, true to
	 *            discard subtypes of the class ToModify
	 */
	private EMFModel loadTransformationModel(String atlTransformationFile) throws ATLCoreException {
		ModelFactory modelFactory = new EMFModelFactory();
		EMFReferenceModel atlMetamodel = (EMFReferenceModel) modelFactory.getBuiltInResource("ATL.ecore");
		AtlParser atlParser = new AtlParser();
		EMFModel atlModel = (EMFModel) modelFactory.newModel(atlMetamodel);
		atlParser.inject(atlModel, atlTransformationFile);
		atlModel.setIsTarget(true);

		return atlModel;
	}

	public void FindErrorRule() {
		String stringSearch = "rule";
		String stringSearch2 = "}";

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
				if (indexfound > -1) {
					// System.out.println("Word is at position " + indexfound + " on line " +
					// linecount);
					NSGAII.writer.println("Word is at position " + indexfound + " on line " + linecount);
					NSGAII.faultrule.add(linecount);

				}
				if (indexfound2 > -1) {
					lastline = linecount;

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

	}

	protected <ToModify extends LocatedElement> List<Object> genericAttributeModification(EMFModel atlModel,
			String outputFolder, Class<ToModify> ToModifyClass, String feature, MetaModel metamodel,
			boolean exactToModifyType, ATLModel wrapper, Solution solution) throws ATLCoreException {
		List<Object> ReturnResult = new ArrayList<Object>();
		List<ToModify> modifiable = (List<ToModify>) wrapper.allObjectsOf(ToModifyClass);
		ReturnResult.add(wrapper);
		ReturnResult.add(atlModel);
		// System.out.println("whilemodify");
		// System.out.println( modifiable.size());
		// we will add a comment to the module, documenting the mutation
		// Module module = AbstractDeletionMutator.getWrapper().getModule();
		EDataTypeEList<String> comments = null;
		String comment = null;
		Module module = wrapper.getModule();
		if (module != null) {
			EStructuralFeature f = wrapper.source(module).eClass().getEStructuralFeature("commentsAfter");
			comments = (EDataTypeEList<String>) wrapper.source(module).eGet(f);
		}

		if (exactToModifyType)
			filterSubtypes(modifiable, ToModifyClass);
		int randomInt = -2;

		boolean checkmutationapply = false;
		int count = -1;
		while (checkmutationapply == false) {

			// System.out.println("whilemodify");

			count = count + 1;
			List<Integer> Result = ReturnFirstIndex(randomInt, modifiable.size(), checkmutationapply, count, solution);
			randomInt = Result.get(0);
			// System.out.println( randomInt);
			if (Result.get(1) == 0)
				checkmutationapply = false;
			else
				checkmutationapply = false;
			if (randomInt == -2)
				checkmutationapply = true;
			else if (solution.getpreviousgeneration() == true) {
				// System.out.println(Operations.statecase);
				// System.out.println(randomInt);
				ReturnResult = OperationPreviousGenerationModefyBinding(randomInt, solution, atlModel, metamodel,
						modifiable, wrapper, feature, comments, ReturnResult);
				checkmutationapply = true;
				// System.out.println(checkmutationapply);
			}

			else if (modifiable.size() > 0 && randomInt != -2 && solution.getpreviousgeneration() == false) {

				if (!Operations.statecase.equals("case5") && !Operations.statecase.equals("case2")
						&& !Operations.statecase.equals("case3")) {
					int row = 0;
					int column = 0;
					int ii = -1;
					int bindingindex = -1;
					boolean satisfycondition = false;
					// System.out.println("modify");
					// System.out.println(randomInt);
					for (int h = 0; h < solution.getCoSolution().getOp().listpropertyname.size(); h++) {
						int pp = -1;
						for (int h2 = 0; h2 < solution.getCoSolution().getOp().listpropertyname.get(h).size(); h2++) {

							if (solution.getCoSolution().getOp().listpropertyname.get(h).get(h2) != null
									&& satisfycondition == false) {
								pp = pp + 1;
								ii = ii + 1;
								if (ii == randomInt) {
									row = h;
									column = h2;
									bindingindex = pp;
									satisfycondition = true;
									break;
								}

							}
						}
					}
					// System.out.println(row);
					// System.out.println(satisfycondition);
					// System.out.println(column);
					// System.out.println(bindingindex);
					// System.out.println(solution.getCoSolution().getOp().originalwrapper.get(row));
					// System.out.println(solution.getCoSolution().getOp().listpropertyname.get(row));

					if (solution.getCoSolution().getOp().originalwrapper.get(row).get(column) == 1) {

						if (solution.getCoSolution().getOp().listpropertyname.get(row).get(column) != null
								&& satisfycondition == true) {
							// System.out.println("outmodify");
							List<ToModify> containers = (List<ToModify>) wrapper.allObjectsOf(OutPatternElement.class);
							EStructuralFeature feature2 = wrapper.source(containers.get(row)).eClass()
									.getEStructuralFeature("bindings");
							List<EObject> link = (List<EObject>) wrapper.source(containers.get(row)).eGet(feature2);

							List<Binding> realbindings = (List<Binding>) wrapper.source(containers.get(row))
									.eGet(feature2);
							List<OutPatternElement> modifiable2 = (List<OutPatternElement>) wrapper
									.allObjectsOf(OutPatternElement.class);
							EClassifier classifier = metamodel.getEClassifier(modifiable2.get(row).getType().getName());
							solution.listeobject.set(MyProblem.indexoperation - 1, classifier);

							// EStructuralFeature featuredef = wrapper.source(link.get(bindingindex))
							// .eClass().getEStructuralFeature("propertyName");
							// System.out.println("test");
							boolean applyreplacement = false;
							Binding binding = realbindings.get(bindingindex);
							Binding temp = binding;
							List<Object> replacements = this.replacements(atlModel, (EClass) classifier,
									realbindings.get(bindingindex), binding.getPropertyName(), metamodel);
							String oldFeatureValue = binding.getPropertyName();
							for (int y1 = 0; y1 < replacements.size(); y1++)
								if (!solution.getCoSolution().getOp().listpropertyname.get(row)
										.contains(replacements.get(y1).toString()))
									applyreplacement = true;
							int randomInt2 = -2;
							boolean unitfind = false;
							while (unitfind == false) {
								randomInt2 = FindSecondIndex(randomInt2, replacements.size());
								if (!oldFeatureValue.equals(replacements.get(randomInt2).toString())
										&& !replacements.get(randomInt2).toString().equals("orderedMessages"))
									unitfind = true;
								if (applyreplacement == false)
									unitfind = true;
							}
							// NSGAII.writer.println("bindingtarget");
							// NSGAII.writer.println(oldFeatureValue);
							// NSGAII.writer.println(replacements.get(randomInt2).toString());
							// System.out.println(binding.getPropertyName());
							if (replacements.get(randomInt2) != null && applyreplacement == true) {
								solution.newbindings.set(MyProblem.indexoperation - 1, temp);

								// solution.expression.set(MyProblem.indexoperation - 1, binding.getValue());
								binding.setPropertyName(replacements.get(randomInt2).toString());
								// List<OutPatternElement> listoutpattern =
								// wrapper.allObjectsOf(OutPatternElement.class);
								// EClassifier classifier2 =
								// metamodel.getEClassifier(listoutpattern.get(row).getType().getName());
								solution.modifypropertyname.set(MyProblem.indexoperation - 1,
										solution.getCoSolution().getOp().listpropertyname.get(row).get(column));

								// solution.modifypropertyname.set(MyProblem.indexoperation-1,replacements.get(randomInt2).toString()
								// );
								// solution.modifypropertyname.set(MyProblem.indexoperation - 1,
								// classifier2.getName());
								realbindings.set(bindingindex, binding);
								// System.out.println(solution.newstring);
								solution.newstring.set(MyProblem.indexoperation - 1,
										replacements.get(randomInt2).toString());
								// System.out.println(randomInt2);
								// System.out.println(solution.newbindings);

								// System.out.println(solution.getCoSolution().getOp().listpropertynamelocation.get(row));
								// System.out.println(solution.getCoSolution().getOp().listpropertyname.get(row));

								// binding.setPropertyName(replacements.get(randomInt2).toString() );
								// realbindings.set(bindingindex, binding);

								solution.getCoSolution().getOp().listpropertyname.get(row).set(column,
										replacements.get(randomInt2).toString());
								// System.out.println(solution.getCoSolution().getOp().listpropertyname.get(row));

								NSGAII.writer.println("\n-outmodifyMUTATION \"" + this.getDescription() + "\" from "
										+ oldFeatureValue + " to " + replacements.get(randomInt2) + " (line "
										+ solution.getCoSolution().getOp().listpropertynamelocation.get(row).get(column)
										+ " of original transformation)\n");

								// if (comments != null)
								comments.add("\n--outmodify1 MUTATION2 \"" + this.getDescription() + "\" from "
										+ oldFeatureValue + " to " + replacements.get(randomInt2) + " (line "
										+ solution.getCoSolution().getOp().listpropertynamelocation.get(row).get(column)
										+ " of original transformation)\n");
								System.out.println("\n--outmodify MUTATION2 \"" + this.getDescription() + "\" from "
										+ oldFeatureValue + " to " + replacements.get(randomInt2) + " (line "
										+ solution.getCoSolution().getOp().listpropertynamelocation.get(row).get(column)
										+ " of original transformation)\n");
								comment = "\n-- MUTATION \"" + this.getDescription() + "\" from " + oldFeatureValue
										+ " to " + replacements.get(randomInt2) + " (line "
										+ solution.getCoSolution().getOp().listpropertynamelocation.get(row).get(column)
										+ " of original transformation)\n";
								// NSGAII.writer.println("randomint modify");
								// NSGAII.writer.println(randomInt);
								// NSGAII.writer.println(randomInt2);
								NSGAII.numop = NSGAII.numop + 1;
								// solution.getCoSolution().getOp().newbindings.get(row).set(column, binding);
								solution.inorout.set(MyProblem.indexoperation - 1, "out");
								StoreTwoIndex(row, column, row);

								checkmutationapply = true;
								ReturnResult.set(0, wrapper);
								ReturnResult.set(1, atlModel);
								ReturnResult.add(comment);
							}

						}

					} else {

						EStructuralFeature featureDefinition = wrapper.source(modifiable.get(randomInt)).eClass()
								.getEStructuralFeature(feature);
						if (wrapper.source(modifiable.get(randomInt)).eGet(featureDefinition) != null) {

							String[] array = modifiable.get(randomInt).getLocation().split(":", 2);
							// System.out.println("modifylocation");
							// System.out.println(NSGAII.listoutpatternmodify);
							// System.out.println(randomInt);
							// System.out.println(Integer.parseInt(array[0]));
							// NSGAII.writer.println(Operations.statecase);
							// NSGAII.writer.println(modifiable.size());
							// NSGAII.writer.println(Integer.parseInt(array[0]));
							int indexrule = -1;
							indexrule = FindIndexRule(array);
							// NSGAII.writer.println("indexrule");
							// NSGAII.writer.println(solution.listlineofcodes);
							// NSGAII.writer.println(indexrule);
							// System.out.println(indexrule);
							// System.out.println(NSGAII.errorrule);
							// if (featureDefinition != null && featureDefinition.getUpperBound() == 1
							// && NSGAII.errorrule.contains(indexrule)) {
							boolean listlineofcode = false;
							boolean haveotherrule = false;
							for (int p = 0; p < NSGAII.errorrule.size(); p++)
								if (NSGAII.errorrule.get(p) != indexrule)
									haveotherrule = true;
							if (NSGAII.errorrule.contains(indexrule)) {
								if (!solution.listlineofcodes.contains(Integer.parseInt(array[0])))
									listlineofcode = true;
								if (solution.listlineofcodes.contains(Integer.parseInt(array[0]))
										&& haveotherrule == false)
									checkmutationapply = true;
								if (solution.listlineofcodes.contains(Integer.parseInt(array[0]))
										&& haveotherrule == true)
									checkmutationapply = false;
								EObject object2modify_src = wrapper.source(modifiable.get(randomInt));
								Object oldFeatureValue = object2modify_src.eGet(featureDefinition);
								int location = 0;
								for (int h = 0; h < solution.getCoSolution().getOp().listpropertynamelocation
										.size(); h++) {
									if (solution.getCoSolution().getOp().listpropertynamelocation.get(h).size() > 0)
										if (solution.getCoSolution().getOp().listpropertynamelocation.get(h)
												.get(0) <= Integer.parseInt(array[0])
												&& Integer.parseInt(array[0]) <= solution.getCoSolution()
														.getOp().listpropertynamelocation
																.get(h)
																.get(solution.getCoSolution()
																		.getOp().listpropertynamelocation.get(h).size()
																		- 1)) {
											location = h;
											break;
										}
								}

								// System.out.println(location);
								// NSGAII.writer.println(location);
								// System.out.println(solution.listlineofcodes);
								// if (rowoutpattern.contains(location)) {
								// System.out.println(listlineofcode);
								// System.out.println("boro");
								List<OutPatternElement> modifiable2 = (List<OutPatternElement>) wrapper
										.allObjectsOf(OutPatternElement.class);
								EClassifier classifier = metamodel
										.getEClassifier(modifiable2.get(location).getType().getName());
								solution.listeobject.set(MyProblem.indexoperation - 1, classifier);
								List<Object> replacements = this.replacements(atlModel, (EClass) classifier,
										modifiable.get(randomInt), oldFeatureValue.toString(), metamodel);
								// System.out.println(replacements);
								// NSGAII.writer.println("replacementmodify");
								// NSGAII.writer.println(NSGAII.listoutpatternmodify);
								// NSGAII.writer.println(classifier);
								// NSGAII.writer.println(oldFeatureValue.toString());
								// NSGAII.writer.println(replacements);
								// NSGAII.writer.println(location);
								// NSGAII.writer.println(row);
								// NSGAII.writer.println(solution.getCoSolution().getOp().listpropertyname.get(location));
								if (!NSGAII.listoutpatternmodify.contains(location))
									NSGAII.listoutpatternmodify.add(location);
								// if (replacements.size() > 0) {
								if (listlineofcode == true) {
									ArrayList<Integer> checklist = new ArrayList<Integer>();

									int randomInt2 = -2;
									boolean unitfind = false;
									boolean applyreplacement = false;
									// boolean checkreplacement=false;
									ArrayList<String> temppropertyname = new ArrayList<String>();

									for (int y = 0; y < solution.getCoSolution().getOp().originalwrapper.get(location)
											.size(); y++)
										if (solution.getCoSolution().getOp().originalwrapper.get(location).get(y) == 0)
											temppropertyname.add(solution.getCoSolution().getOp().listpropertyname
													.get(location).get(y));
									for (int y1 = 0; y1 < replacements.size(); y1++)
										if (!temppropertyname.contains(replacements.get(y1).toString()))
											applyreplacement = true;

									if (randomInt2 == -2) {
										while (unitfind == false) {
											randomInt2 = FindSecondIndex(randomInt2, replacements.size());
											/*
											 * if (!temppropertyname.contains( replacements.get(randomInt2).toString())
											 * )
											 */
											if (!oldFeatureValue.equals(replacements.get(randomInt2).toString())
													&& !replacements.get(randomInt2).toString()
															.equals("orderedMessages"))
												unitfind = true;
											if (applyreplacement == false)
												unitfind = true;
											/*
											 * if(checklist.size()==replacements.size()) { unitfind = true;
											 * checkreplacement=true; }
											 * 
											 * if(!checklist.contains(randomInt2)) checklist.add(randomInt2);
											 */

										}
									}
									if (applyreplacement == false && haveotherrule == false)
										checkmutationapply = true;
									// System.out.println( NSGAII.listoutpatternmodify.size());
									// System.out.println( replacements.get(randomInt2).toString());
									// NSGAII.writer.println( replacements.get(randomInt2).toString());
									/*
									 * if (checkreplacement==true && NSGAII.listoutpatternmodify.size()==6) {
									 * NSGAII.writer.println("sefrkon"); System.out.println("sefrkon");
									 * NSGAII.writer.println(NSGAII.listoutpatternmodify); checkmutationapply =
									 * true; StoreTwoIndex(-2,-2,-2); }
									 */
									// randomInt2 = FindSecondIndex(randomInt2, replacements.size());
									if (replacements.get(randomInt2) != null && applyreplacement == true) {

										int indexof = solution.getCoSolution().getOp().listpropertynamelocation
												.get(location).indexOf(Integer.parseInt(array[0]));
										// System.out.println(solution.getCoSolution().getOp().listpropertyname.get(location));
										// System.out.println(
										// solution.getCoSolution().getOp().listpropertynamelocation.get(location));

										wrapper.source(modifiable.get(randomInt)).eSet(featureDefinition,
												replacements.get(randomInt2));

										solution.getCoSolution().getOp().listpropertyname.get(location).set(indexof,
												replacements.get(randomInt2).toString());
										// System.out.println(solution.getCoSolution().getOp().listpropertyname.get(location));
										// System.out.println(
										// solution.getCoSolution().getOp().listpropertynamelocation.get(location));

										StoreTwoIndex(randomInt, randomInt2, location);
										// if (comments != null)
										comments.add("\n-- MUTATION \"" + this.getDescription() + "\" from "
												+ oldFeatureValue.toString() + " to " + replacements.get(randomInt2)
												+ " (line " + modifiable.get(randomInt).getLocation()
												+ " of original transformation)\n");
										comment = "\n-- MUTATION \"" + this.getDescription() + "\" from "
												+ oldFeatureValue.toString() + " to " + replacements.get(randomInt2)
												+ " (line " + modifiable.get(randomInt).getLocation()
												+ " of original transformation)\n";
										System.out.println("\n-- MUTATION \"" + this.getDescription() + "\" from "
												+ oldFeatureValue.toString() + " to " + replacements.get(randomInt2)
												+ " (line " + modifiable.get(randomInt).getLocation()
												+ " of original transformation)\n");
										// NSGAII.writer.println("randomint modify");
										// NSGAII.writer.println(randomInt);
										// NSGAII.writer.println(randomInt2);
										NSGAII.numop = NSGAII.numop + 1;
										checkmutationapply = true;
										ReturnResult.set(0, wrapper);
										ReturnResult.set(1, atlModel);
										ReturnResult.add(comment);
									}
								}
								// }
							}

						}

					}
				}

				else {

					EStructuralFeature featureDefinition = wrapper.source(modifiable.get(randomInt)).eClass()
							.getEStructuralFeature(feature);

					String[] array = modifiable.get(randomInt).getLocation().split(":", 2);

					// String[] array2 = array[1].split("-", 2);
					// String[] array3 = array[1].split(":", 2);
					// System.out.println("collectionlocation");

					// if (array3.length>0)
					// System.out.println(Integer.parseInt(array3[0]));
					// else
					// System.out.println(Integer.parseInt(array2[1]));
					// System.out.println(modifiable.get(randomInt).getLocation());
					// System.out.println(modifiable.size());

					int indexrule = -1;
					indexrule = FindIndexRule(array);
					// System.out.println(indexrule);
					if (modifiable.size() == 1 && !Operations.statecase.equals("case2")
							&& !Operations.statecase.equals("case3"))
						checkmutationapply = true;
					boolean check = true;
					if (NSGAII.parameterlocation.contains(Integer.parseInt(array[0]))
							&& Operations.statecase.equals("case2"))
						check = false;
					if (!NSGAII.iterationrule.contains(indexrule) && Operations.statecase.equals("case3"))
						check = false;
					if (NSGAII.errorrule.contains(indexrule) && check == true) {
						EObject object2modify_src = wrapper.source(modifiable.get(randomInt));
						Object oldFeatureValue = object2modify_src.eGet(featureDefinition);

						// System.out.println(oldFeatureValue.toString());
						List<Object> replacements = this.replacements(atlModel, null, modifiable.get(randomInt),
								oldFeatureValue.toString(), metamodel);

						if (Operations.statecase.equals("case3")) {

							String[] array3 = array[1].split(":", 2);
							String[] array4 = array3[0].split("-", 2);
							replacements = refinereplacements(replacements, solution, Integer.parseInt(array[0]),
									indexrule, oldFeatureValue.toString(), Integer.parseInt(array4[1]));

						}

						// System.out.println(" replacements2");
						// System.out.println( replacements);
						// System.out.println(" replacements2");
						if (replacements.size() > 0 && !oldFeatureValue.toString().equals("toString")) {
							int randomInt2 = -2;
							boolean unitfind = false;
							while (unitfind == false) {
								randomInt2 = FindSecondIndex(randomInt2, replacements.size());
								if (Operations.statecase.equals("case2")) {

									if (NSGAII.emptyopperationlocation.contains(Integer.parseInt(array[0]))) {
										if (!oldFeatureValue.toString().equals(replacements.get(randomInt2).toString())
												&& !replacements.get(randomInt2).toString().equals("oclIsKindOf"))
											unitfind = true;

									} else {
										if (!oldFeatureValue.toString().equals(replacements.get(randomInt2).toString())
												&& !oldFeatureValue.toString().equals("oclIsKindOf")
												&& !oldFeatureValue.toString().equals("oclIsUndefined")
												&& !oldFeatureValue.toString().equals("allInstances"))
											unitfind = true;
										if (!oldFeatureValue.toString().equals(replacements.get(randomInt2).toString())
												&& oldFeatureValue.toString().equals("oclIsKindOf")
												&& !replacements.get(randomInt2).toString().equals("oclIsUndefined"))
											unitfind = true;
										if (!oldFeatureValue.toString().equals(replacements.get(randomInt2).toString())
												&& oldFeatureValue.toString().equals("oclIsUndefined")
												&& !replacements.get(randomInt2).toString().equals("oclIsKindOf"))
											unitfind = true;
										if (!oldFeatureValue.toString().equals(replacements.get(randomInt2).toString())
												&& oldFeatureValue.toString().equals("allInstances")
												&& !replacements.get(randomInt2).toString().equals("oclIsUndefined")
												&& !replacements.get(randomInt2).toString().equals("oclIsKindOf"))
											unitfind = true;

									}
								}
								if (Operations.statecase.equals("case5")) {
									if (NSGAII.emptycollectionlocation.contains(Integer.parseInt(array[0]))) {
										if (!oldFeatureValue.toString().equals(replacements.get(randomInt2).toString())
												&& !replacements.get(randomInt2).toString().equals("union"))
											unitfind = true;

									} else {
										if (!oldFeatureValue.toString().equals(replacements.get(randomInt2).toString()))
											unitfind = true;

									}
								}

								if (Operations.statecase.equals("case3")) {
									if (!oldFeatureValue.toString().equals(replacements.get(randomInt2).toString()))
										unitfind = true;

								}

							}
							// System.out.println( replacements.get(randomInt2).toString());
							wrapper.source(modifiable.get(randomInt)).eSet(featureDefinition,
									replacements.get(randomInt2));
							StoreTwoIndex(randomInt, randomInt2, indexrule);
							comments.add("\n-- MUTATION \"" + this.getDescription() + "\" from "
									+ oldFeatureValue.toString() + " to " + replacements.get(randomInt2) + " (line "
									+ modifiable.get(randomInt).getLocation() + " of original transformation)\n");
							comment = "\n-- MUTATION \"" + this.getDescription() + "\" from "
									+ oldFeatureValue.toString() + " to " + replacements.get(randomInt2) + " (line "
									+ modifiable.get(randomInt).getLocation() + " of original transformation)\n";
							System.out.println("\n-- MUTATION \"" + this.getDescription() + "\" from "
									+ oldFeatureValue.toString() + " to " + replacements.get(randomInt2) + " (line "
									+ modifiable.get(randomInt).getLocation() + " of original transformation)\n");
							NSGAII.numop = NSGAII.numop + 1;
							checkmutationapply = true;
							ReturnResult.set(0, wrapper);
							ReturnResult.set(1, atlModel);
							ReturnResult.add(comment);

						}

					}

				}

			}
			// return comment;
		}

		// ReturnResult.add(comment);
		return ReturnResult;
	}

	private List<Object> refinereplacements(List<Object> replacements, Solution solution, int location, int indexrule,
			String oldstr, int closedbracetlocation) {
		// TODO Auto-generated method stub

		// Iterationha ba OCLKind of tadakhol dare
		boolean find = false;
		boolean find2 = false;
		int index1 = -1;
		int index2 = -1;
		List<Object> updatereplacements = new ArrayList<Object>();
		ArrayList<Integer> realindexlist = new ArrayList<Integer>();
		// System.out.println(NSGAII.iterationcall);
		// System.out.println(solution.getCoSolution().getOp().listpropertynamelocation);
		// System.out.println("ppppp");
		boolean countusing = false;

		/*
		 * for(int k=0;k<NSGAII.iterationcall.size();k++) { find=false; for(int
		 * i=0;i<solution.getCoSolution().getOp().listpropertynamelocation.size();i++) {
		 * 
		 * for(int
		 * j=0;j<solution.getCoSolution().getOp().listpropertynamelocation.get(i).size()
		 * ;j++) {
		 * 
		 * if(solution.getCoSolution().getOp().listpropertynamelocation.get(i).get(j)==
		 * NSGAII.iterationcall.get(k)) { find=true;
		 * 
		 * realindexlist.add(NSGAII.iterationcall.get(k)); }
		 * if(solution.getCoSolution().getOp().listpropertynamelocation.get(i).get(j)>
		 * NSGAII.iterationcall.get(k) && find==false && find2==false) { find2=true;
		 * index1=i; index2=j-1;
		 * 
		 * }
		 * 
		 * } }
		 * 
		 * System.out.println(realindexlist); System.out.println(countusing);
		 * System.out.println(index1); System.out.println(index2);
		 * if(indexrule<NSGAII.preconditionlocation.size() &&
		 * NSGAII.preconditionlocation.size()>0) { if(NSGAII.preconditionlocation.get(
		 * indexrule ) > location) { countusing=true; } } if(find==false &&
		 * countusing==false && NSGAII.preconditionlocation.size()>0)
		 * realindexlist.add(solution.getCoSolution().getOp().listpropertynamelocation.
		 * get(index1).get(index2)); }
		 */
		// System.out.println("reallist");
		// System.out.println(location);
		// System.out.println(realindexlist);
		// System.out.println(NSGAII.iterationcall);
		// System.out.println(NSGAII.iterationcall);
		// System.out.println(closedbracetlocation);
		int posclosedbracet = -1;
		posclosedbracet = NSGAII.iterationcall.indexOf(closedbracetlocation - 2);
		if (posclosedbracet > 0)
			location = closedbracetlocation - 2;

		boolean check = false;
		// if this place there is iteration: select collect
		int pos = -1;
		pos = NSGAII.iterationcall.indexOf(location);
		int pos2 = -1;
		// if in next place there is ocl kind of
		int previousocl = -1;

		pos2 = NSGAII.operationcalllocation.indexOf(location + 1);
		// if in previous place there is ocl kind of
		previousocl = NSGAII.operationcalllocation.indexOf(location - 1);

		// badesh ocl bashe
		if (pos2 > 0 && previousocl == -1) {
			for (int i = 0; i < replacements.size(); i++)
				if (!replacements.get(i).equals("isUnique") && !replacements.get(i).equals("forAll")
						&& !replacements.get(i).equals("exists")) {
					if (oldstr.equals("reject") && !replacements.get(i).equals("reject")
							&& !replacements.get(i).equals("select") && !replacements.get(i).equals("collect")) {
						updatereplacements.add(replacements.get(i));
						check = true;
					}
					if (oldstr.equals("select") && !replacements.get(i).equals("select")
							&& !replacements.get(i).equals("reject") && !replacements.get(i).equals("collect")) {
						updatereplacements.add(replacements.get(i));
						check = true;
					}

				}

		}
		// ghablesh ocl bashe
		if (previousocl > 0 && pos2 == -1) {
			for (int i = 0; i < replacements.size(); i++) {
				if (oldstr.equals("reject") && !replacements.get(i).equals("reject")
						&& !replacements.get(i).equals("select") && !replacements.get(i).equals("collect")) {
					updatereplacements.add(replacements.get(i));
					check = true;
				}
				if (oldstr.equals("select") && !replacements.get(i).equals("select")
						&& !replacements.get(i).equals("reject") && !replacements.get(i).equals("collect")) {
					updatereplacements.add(replacements.get(i));
					check = true;
				}

			}

		}

		// PNMLPetriNet and Class2Relational
		/*
		 * if(pos>0) {
		 * 
		 * if(realindexlist.get(pos)==realindexlist.get(pos-1)) for(int
		 * i=0;i<replacements.size();i++) if(!replacements.get(i).equals("select") &&
		 * !replacements.get(i).equals("collect")) {
		 * updatereplacements.add(replacements.get(i)); check=true; }
		 * 
		 * } if(pos==0) {
		 * 
		 * if(countusing==false) { if(realindexlist.get(pos)==realindexlist.get(pos+1))
		 * for(int i=0;i<replacements.size();i++)
		 * if(!replacements.get(i).equals("isUnique") &&
		 * !replacements.get(i).equals("forAll") &&
		 * !replacements.get(i).equals("select")) {
		 * updatereplacements.add(replacements.get(i)); check=true; } } else { return
		 * replacements;
		 * 
		 * }
		 * 
		 * }
		 */

		/*
		 * if(check==false && pos2==-1) {
		 * 
		 * for(int i=0;i<replacements.size();i++)
		 * if(!replacements.get(i).equals("reject") &&
		 * !replacements.get(i).equals("collect")) {
		 * updatereplacements.add(replacements.get(i)); check=true;
		 * 
		 * }
		 * 
		 * }
		 */

		if (check == false && pos2 == -1) {
			return replacements;

		}

		return updatereplacements;
	}

	private <ToModify extends LocatedElement> List<Object> OperationPreviousGenerationModefyBinding(int randomInt,
			Solution solution, EMFModel atlModel, MetaModel metamodel, List<ToModify> modifiable, ATLModel wrapper,
			String feature, EDataTypeEList<String> comments, List<Object> ReturnResult) {
		// System.out.println(solution.inorout);
		// System.out.println(solution.inorout.get(MyProblem.indexoperation - 1));

		if (!Operations.statecase.equals("case5") && !Operations.statecase.equals("case2")
				&& !Operations.statecase.equals("case3")) {
			if (solution.inorout.get(MyProblem.indexoperation - 1).equals("out")) {

				List<ToModify> containers = (List<ToModify>) wrapper.allObjectsOf(OutPatternElement.class);
				EStructuralFeature feature2 = wrapper.source(containers.get(randomInt)).eClass()
						.getEStructuralFeature("bindings");
				List<Binding> realbindings = (List<Binding>) wrapper.source(containers.get(randomInt)).eGet(feature2);
				int index = -1;

				Binding b = solution.newbindings.get(MyProblem.indexoperation - 1);
				// Binding b2 = ATLFactory.eINSTANCE.createBinding();
				// String str = solution.newstring.get(MyProblem.indexoperation - 1);
				// b2.setPropertyName(str);
				String classifier2 = solution.modifypropertyname.get(MyProblem.indexoperation - 1);
				// System.out.println(classifier2);
				// NSGAII.writer.println(classifier2);

				for (int i = 0; i < realbindings.size(); i++) {
					// NSGAII.writer.println(realbindings.get(i));
					if (solution.getCoSolution().getOp().originalwrapper.get(randomInt).get(i) == 1
							&& realbindings.get(i).getValue().getClass().equals(b.getValue().getClass())
							&& realbindings.get(i).getPropertyName().equals(classifier2)) {
						// System.out.println(realbindings.get(i));
						// System.out.println(realbindings.get(i).getValue().getClass());

						index = i;
						// System.out.println(i);
						// System.out.println("yescontainis");
						// NSGAII.writer.println(realbindings.get(i).getClass());
						// NSGAII.writer.println(realbindings.get(i).getValue());
						// NSGAII.writer.println(b2.getValue());
						// if(realbindings.get(i).equals(b))
						// NSGAII.writer.println("yescontainis");
					}

				}

				// System.out.println(b.getPropertyName());
				// NSGAII.writer.println(index);
				// NSGAII.writer.println(b.getPropertyName());
				// b2.setValue(solution.expression.get(MyProblem.indexoperation-1));
				// if(index!=-1) {
				if (index != -1) {
					/*
					 * index=-1; for(int i=0;i<realbindings.size();i++) {
					 * if(realbindings.get(i).equals(b2)) { index=i;
					 * 
					 * }
					 * 
					 * }
					 */
					// index = realbindings.indexOf(b2);
					int randomInt2 = -2;
					randomInt2 = FindSecondIndex(randomInt2, -1);
					Binding binding = realbindings.get(index);
					// System.out.println(index);
					// NSGAII.writer.println(index);
					String oldFeatureValue = binding.getPropertyName();

					// System.out.println(solution.newstring.get(MyProblem.indexoperation - 1));
					// NSGAII.writer.println(solution.newstring.get(MyProblem.indexoperation - 1));
					binding.setPropertyName(solution.newstring.get(MyProblem.indexoperation - 1));

					solution.getCoSolution().getOp().listpropertyname.get(randomInt).set(randomInt2,
							solution.newstring.get(MyProblem.indexoperation - 1));

					realbindings.set(index, binding);
					String comment = null;

					// if (comments != null)
					comments.add("\n--outmodify2 MUTATION2 \"" + this.getDescription() + "\" from " + oldFeatureValue
							+ " to " + solution.newstring.get(MyProblem.indexoperation - 1) + " (line "
							+ solution.getCoSolution().getOp().listpropertynamelocation.get(randomInt).get(randomInt2)
							+ " of original transformation)\n");
					System.out.println("\n--outmodify2 MUTATION2 \"" + this.getDescription() + "\" from "
							+ oldFeatureValue + " to " + solution.newstring.get(MyProblem.indexoperation - 1)
							+ " (line "
							+ solution.getCoSolution().getOp().listpropertynamelocation.get(randomInt).get(randomInt2)
							+ " of original transformation)\n");
					NSGAII.writer.println("\n--outmodify2 MUTATION2 \"" + this.getDescription() + "\" from "
							+ oldFeatureValue + " to " + solution.newstring.get(MyProblem.indexoperation - 1)
							+ " (line "
							+ solution.getCoSolution().getOp().listpropertynamelocation.get(randomInt).get(randomInt2)
							+ " of original transformation)\n");
					comment = "\n-- MUTATION \"" + this.getDescription() + "\" from " + oldFeatureValue + " to "
							+ solution.newstring.get(MyProblem.indexoperation - 1) + " (line "
							+ solution.getCoSolution().getOp().listpropertynamelocation.get(randomInt).get(randomInt2)
							+ " of original transformation)\n";
					// NSGAII.writer.println("randomint modify");
					// NSGAII.writer.println(randomInt);
					// NSGAII.writer.println(randomInt2);
					NSGAII.numop = NSGAII.numop + 1;
					// solution.getCoSolution().getOp().newbindings.get(row).set(column, binding);

					StoreTwoIndex(randomInt, randomInt2, randomInt);

					ReturnResult.set(0, wrapper);
					ReturnResult.set(1, atlModel);
					ReturnResult.add(comment);

				}

				else {
					StoreTwoIndex(-2, -2, -2);
					solution.inorout.set(MyProblem.indexoperation - 1, "empty");
					solution.newbindings.set(MyProblem.indexoperation - 1, null);

				}

			} else {
				String comment = null;
				ToModify modifiable2 = modifiable.get(randomInt);
				String[] array = modifiable2.getLocation().split(":", 2);
				// System.out.println(array[0]);
				// System.out.println(solution.listlineofcodes);
				if (!solution.listlineofcodes.contains(Integer.parseInt(array[0]))) {
					EStructuralFeature featureDefinition = wrapper.source(modifiable2).eClass()
							.getEStructuralFeature(feature);
					EObject object2modify_src = wrapper.source(modifiable2);
					Object oldFeatureValue = object2modify_src.eGet(featureDefinition);
					int location = -2;
					location = FindThirdIndex(location);
					EClassifier eclassifier = solution.listeobject.get(MyProblem.indexoperation - 1);
					int indexof = solution.getCoSolution().getOp().listpropertynamelocation.get(location)
							.indexOf(Integer.parseInt(array[0]));

					// NSGAII.writer.println("ghablimodify");
					// NSGAII.writer.println(eclassifier);
					List<Object> replacements = this.replacements(atlModel, (EClass) eclassifier, modifiable2,
							oldFeatureValue.toString(), metamodel);
					// NSGAII.writer.println(replacements);
					int randomInt2 = -2;
					randomInt2 = FindSecondIndex(randomInt2, replacements.size());
					if (!oldFeatureValue.toString().equals(replacements.get(randomInt2).toString())) {
						wrapper.source(modifiable2).eSet(featureDefinition, replacements.get(randomInt2));
						solution.getCoSolution().getOp().listpropertyname.get(location).set(indexof,
								replacements.get(randomInt2).toString());

						StoreTwoIndex(randomInt, randomInt2, location);
						// if (comments != null)
						comments.add("\n-- MUTATION \"" + this.getDescription() + "\" from "
								+ oldFeatureValue.toString() + " to " + replacements.get(randomInt2) + " (line "
								+ modifiable.get(randomInt).getLocation() + " of original transformation)\n");
						comment = "\n-- MUTATION \"" + this.getDescription() + "\" from " + oldFeatureValue.toString()
								+ " to " + replacements.get(randomInt2) + " (line "
								+ modifiable.get(randomInt).getLocation() + " of original transformation)\n";
						System.out.println("\n-- MUTATION \"" + this.getDescription() + "\" from "
								+ oldFeatureValue.toString() + " to " + replacements.get(randomInt2) + " (line "
								+ modifiable.get(randomInt).getLocation() + " of original transformation)\n");
						// NSGAII.writer.println("after crossover modify");
						// NSGAII.writer.println(randomInt);
						// NSGAII.writer.println(randomInt2);
						NSGAII.numop = NSGAII.numop + 1;
						// checkmutationapply = true;
						ReturnResult.set(0, wrapper);
						ReturnResult.set(1, atlModel);
						ReturnResult.add(comment);

					} else {
						StoreTwoIndex(randomInt, randomInt2, location);
					}

				} else {
					StoreTwoIndex(-2, -2, -2);
					solution.inorout.set(MyProblem.indexoperation - 1, "empty");
					solution.newbindings.set(MyProblem.indexoperation - 1, null);

				}

			}
		} else {
			String comment = null;
			EStructuralFeature featureDefinition = wrapper.source(modifiable.get(randomInt)).eClass()
					.getEStructuralFeature(feature);
			EObject object2modify_src = wrapper.source(modifiable.get(randomInt));
			Object oldFeatureValue = object2modify_src.eGet(featureDefinition);
			List<Object> replacements = this.replacements(atlModel, null, modifiable.get(randomInt),
					oldFeatureValue.toString(), metamodel);
			String[] array = modifiable.get(randomInt).getLocation().split(":", 2);
			if (Operations.statecase.equals("case3")) {
				int randomInt3 = -1;
				String[] array3 = array[1].split(":", 2);
				String[] array4 = array3[0].split("-", 2);

				randomInt3 = FindThirdIndex(randomInt3);
				replacements = refinereplacements(replacements, solution, Integer.parseInt(array[0]), randomInt3,
						oldFeatureValue.toString(), Integer.parseInt(array4[1]));
			}
			int randomInt2 = -2;
			boolean unitfind = false;
			randomInt2 = FindSecondIndex(randomInt2, replacements.size());

			wrapper.source(modifiable.get(randomInt)).eSet(featureDefinition, replacements.get(randomInt2));
			StoreTwoIndex(randomInt, randomInt2, -2);
			comments.add("\n-- MUTATION \"" + this.getDescription() + "\" from " + oldFeatureValue.toString() + " to "
					+ replacements.get(randomInt2) + " (line " + modifiable.get(randomInt).getLocation()
					+ " of original transformation)\n");
			comment = "\n-- MUTATION \"" + this.getDescription() + "\" from " + oldFeatureValue.toString() + " to "
					+ replacements.get(randomInt2) + " (line " + modifiable.get(randomInt).getLocation()
					+ " of original transformation)\n";
			System.out.println("\n-- MUTATION \"" + this.getDescription() + "\" from " + oldFeatureValue.toString()
					+ " to " + replacements.get(randomInt2) + " (line " + modifiable.get(randomInt).getLocation()
					+ " of original transformation)\n");
			NSGAII.writer.println("\n-- MUTATION \"" + this.getDescription() + "\" from " + oldFeatureValue.toString()
					+ " to " + replacements.get(randomInt2) + " (line " + modifiable.get(randomInt).getLocation()
					+ " of original transformation)\n");
			NSGAII.numop = NSGAII.numop + 1;
			ReturnResult.set(0, wrapper);
			ReturnResult.set(1, atlModel);
			ReturnResult.add(comment);

		}

		return ReturnResult;
	}

	private OclExpression getCompatibleValue(String type, boolean monovalued, boolean ordered, Object object) {
		// TODO Auto-generated method stub
		OclExpression expression = null;

		if (monovalued) {
			if (EMFUtils.isString(type)) {
				expression = OCLFactory.eINSTANCE.createStringExp();
				((StringExp) expression).setStringSymbol("dummy");
			} else if (EMFUtils.isInteger(type)) {
				expression = OCLFactory.eINSTANCE.createIntegerExp();
				((IntegerExp) expression).setIntegerSymbol(0);
			} else if (EMFUtils.isBoolean(type)) {
				expression = OCLFactory.eINSTANCE.createBooleanExp();
				((BooleanExp) expression).setBooleanSymbol(false);
			} else if (EMFUtils.isFloating(type)) {
				expression = OCLFactory.eINSTANCE.createRealExp();
				((RealExp) expression).setRealSymbol(0);
			} else {
				// System.out.println("else");
				expression = OCLFactory.eINSTANCE.createSequenceExp();
				// expression = OCLFactory.eINSTANCE.createVariableExp();
				// System.out.println(expression);
				// bod if (variables.size()>0)
				// ((VariableExp)expression).setReferredVariable(variables.get(0));
			}
		}

		else {
			// System.out.println("else2");
			expression = OCLFactory.eINSTANCE.createSequenceExp();
			// expression = ordered?

			// OCLFactory.eINSTANCE.createSequenceExp() :
			// OCLFactory.eINSTANCE.createSetExp();
		}

		return expression;

	}

	protected int FindIndexRule(String[] array) {
		// TODO Auto-generated method stub
		int indexrule = -1;
		for (int j = 0; j < NSGAII.faultrule.size(); j++) {

			if (Integer.parseInt(array[0]) > NSGAII.faultrule.get(j)
					&& Integer.parseInt(array[0]) < NSGAII.finalrule.get(j))

				indexrule = j;
			// NSGAII.errorrule.add(j);
		}
		return indexrule;

	}

	private void SaveAsXMI(ATLModel wrapper) {
		// TODO Auto-generated method stub
		ResourceSet outputMetamodelResourceSet = new ResourceSetImpl();

		String str = "C:/Users/varaminz/Downloads/Project/workspaceTEST34/anatlyzer.atl.tests.api/trafo2/zahra2cover";
		Resource outputMetamodelResource = outputMetamodelResourceSet.createResource(
				URI.createFileURI(new File(str + "/fixmodel" + main.totalnumber + ".xmi").getAbsolutePath()));
		outputMetamodelResourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*",
				new XMLResourceFactoryImpl());
		outputMetamodelResource.getContents().add(wrapper.getRoot());
		try {
			outputMetamodelResource.save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected List<Integer> ReturnFirstIndex(int randomInt, int size, boolean checkmutationapply, int count,
			Solution solution) {
		// TODO Auto-generated method stub
		if (NSGAII.statemutcrossinitial == "mutation") {

			// if (Operations.statecase.equals("case10") ||
			// Operations.statecase.equals("case9") || Operations.statecase.equals("case3")
			// || || Operations.statecase.equals("case9")) {
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
			 * System.out.println(MyProblem.indexoperation);
			 */

			switch (MyProblem.indexoperation) {
			case 1:
				if (MyProblem.oldoperation1 != -1) {

					randomInt = (int) (MyProblem.oldoperation1);
					solution.setpreviousgeneration(true);
				} else {
					Random number_generator = new Random();
					randomInt = number_generator.nextInt(size);
					solution.setpreviousgeneration(false);

				}

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
				break;
			case 5:
				if (MyProblem.oldoperation5 != -1) {
					randomInt = (int) (MyProblem.oldoperation5);
					solution.setpreviousgeneration(true);
				} else {
					Random number_generator = new Random();
					randomInt = number_generator.nextInt(size);
					solution.setpreviousgeneration(false);
				}

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

				break;
			case 7:
				if (MyProblem.oldoperation7 != -1) {
					randomInt = (int) (MyProblem.oldoperation7);
					solution.setpreviousgeneration(true);
				} else {
					Random number_generator = new Random();
					randomInt = number_generator.nextInt(size);
					solution.setpreviousgeneration(false);
				}

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

				break;
			case 15:
				if (MyProblem.oldoperation15 != -1) {
					randomInt = (int) (MyProblem.oldoperation15);
					solution.setpreviousgeneration(true);
				} else {
					Random number_generator = new Random();
					randomInt = number_generator.nextInt(size);
					solution.setpreviousgeneration(false);
				}

				break;
			case 16:
				if (MyProblem.oldoperation16 != -1) {
					randomInt = (int) (MyProblem.oldoperation16);
					solution.setpreviousgeneration(true);
				} else {
					Random number_generator = new Random();
					randomInt = number_generator.nextInt(size);
					solution.setpreviousgeneration(false);
				}

				break;
			case 17:
				if (MyProblem.oldoperation17 != -1) {
					randomInt = (int) (MyProblem.oldoperation17);
					solution.setpreviousgeneration(true);
				} else {
					Random number_generator = new Random();
					randomInt = number_generator.nextInt(size);
					solution.setpreviousgeneration(false);
				}

				break;
			case 18:
				if (MyProblem.oldoperation18 != -1) {
					randomInt = (int) (MyProblem.oldoperation18);
					solution.setpreviousgeneration(true);

				} else {
					Random number_generator = new Random();
					randomInt = number_generator.nextInt(size);
					solution.setpreviousgeneration(false);
				}

				break;
			case 19:
				if (MyProblem.oldoperation19 != -1) {
					randomInt = (int) (MyProblem.oldoperation19);
					solution.setpreviousgeneration(true);
				} else {
					Random number_generator = new Random();
					randomInt = number_generator.nextInt(size);
					solution.setpreviousgeneration(false);
				}
				// randomInt=(int) (Math.random() * (modifiable.size()-1));

				break;
			case 20:
				if (MyProblem.oldoperation20 != -1) {
					randomInt = (int) (MyProblem.oldoperation20);
					// System.out.print("r10r");
					solution.setpreviousgeneration(true);
				} else {
					Random number_generator = new Random();
					randomInt = number_generator.nextInt(size);
					solution.setpreviousgeneration(false);
				}

				break;
			case 21:
				if (MyProblem.oldoperation21 != -1) {
					randomInt = (int) (MyProblem.oldoperation21);
					solution.setpreviousgeneration(true);
				} else {
					Random number_generator = new Random();
					randomInt = number_generator.nextInt(size);
					solution.setpreviousgeneration(false);
				}
				// randomInt=(int) (Math.random() * (modifiable.size()-1));

				break;
			case 22:
				if (MyProblem.oldoperation22 != -1) {
					randomInt = (int) (MyProblem.oldoperation22);
					solution.setpreviousgeneration(true);
				} else {
					Random number_generator = new Random();
					randomInt = number_generator.nextInt(size);
					solution.setpreviousgeneration(false);
				}

				break;

			}

			// }

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

	private int FindThirdIndex(int randomInt2) {
		// TODO Auto-generated method stub
		if (NSGAII.statemutcrossinitial == "mutation") {

			// if (Operations.statecase.equals("case10")) {// hamon case3 hast

			switch (MyProblem.indexoperation) {
			case 1:
				if (MyProblem.secondoldoperation1 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation1);

				break;
			case 2:
				if (MyProblem.secondoldoperation2 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation2);

				break;
			case 3:
				if (MyProblem.secondoldoperation3 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation3);
				break;
			case 4:
				if (MyProblem.secondoldoperation4 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation4);

				break;
			case 5:
				if (MyProblem.secondoldoperation5 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation5);

				break;
			case 6:
				if (MyProblem.secondoldoperation6 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation6);

				break;
			case 7:
				if (MyProblem.secondoldoperation7 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation7);
				break;
			case 8:
				if (MyProblem.secondoldoperation8 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation8);

				break;
			case 9:
				if (MyProblem.secondoldoperation9 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation9);

				break;
			case 10:
				if (MyProblem.secondoldoperation10 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation10);

				break;
			case 11:
				if (MyProblem.secondoldoperation11 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation11);

				break;
			case 12:
				if (MyProblem.secondoldoperation12 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation12);

				break;
			case 13:
				if (MyProblem.secondoldoperation13 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation13);

				break;
			case 14:
				if (MyProblem.secondoldoperation14 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation14);

				break;
			case 15:
				if (MyProblem.secondoldoperation15 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation15);

				break;
			case 16:
				if (MyProblem.secondoldoperation16 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation16);

				break;
			case 17:
				if (MyProblem.secondoldoperation17 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation17);

				break;
			case 18:
				if (MyProblem.secondoldoperation18 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation18);

				break;
			case 19:
				if (MyProblem.secondoldoperation19 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation19);

				break;
			case 20:
				if (MyProblem.secondoldoperation20 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation20);

				break;
			case 21:
				if (MyProblem.secondoldoperation21 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation21);

				break;
			case 22:
				if (MyProblem.secondoldoperation22 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation22);
				break;

			}

			// }
		}
		return randomInt2;
	}

	protected int FindSecondIndex(int randomInt2, int size) {
		// TODO Auto-generated method stub
		if (NSGAII.statemutcrossinitial == "mutation") {

			// if (Operations.statecase.equals("case10") ||
			// Operations.statecase.equals("case9")) {// hamon case3 hast

			switch (MyProblem.indexoperation) {
			case 1:
				if (MyProblem.replaceoperation1 != -1)
					randomInt2 = (int) (MyProblem.replaceoperation1);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(size);

				}

				break;
			case 2:
				if (MyProblem.replaceoperation2 != -1)
					randomInt2 = (int) (MyProblem.replaceoperation2);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(size);

				}

				break;
			case 3:
				if (MyProblem.replaceoperation3 != -1)
					randomInt2 = (int) (MyProblem.replaceoperation3);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(size);

				}

				break;
			case 4:
				if (MyProblem.replaceoperation4 != -1)
					randomInt2 = (int) (MyProblem.replaceoperation4);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(size);

				}

				break;
			case 5:
				if (MyProblem.replaceoperation5 != -1)
					randomInt2 = (int) (MyProblem.replaceoperation5);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(size);

				}

				break;
			case 6:
				if (MyProblem.replaceoperation6 != -1)
					randomInt2 = (int) (MyProblem.replaceoperation6);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(size);

				}

				break;
			case 7:
				if (MyProblem.replaceoperation7 != -1)
					randomInt2 = (int) (MyProblem.replaceoperation7);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(size);

				}

				break;
			case 8:
				if (MyProblem.replaceoperation8 != -1)
					randomInt2 = (int) (MyProblem.replaceoperation8);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(size);

				}

				break;
			case 9:
				if (MyProblem.replaceoperation9 != -1)
					randomInt2 = (int) (MyProblem.replaceoperation9);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(size);

				}

				break;
			case 10:
				if (MyProblem.replaceoperation10 != -1)
					randomInt2 = (int) (MyProblem.replaceoperation10);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(size);

				}

				break;
			case 11:
				if (MyProblem.replaceoperation11 != -1)
					randomInt2 = (int) (MyProblem.replaceoperation11);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(size);

				}

				break;
			case 12:
				if (MyProblem.replaceoperation12 != -1)
					randomInt2 = (int) (MyProblem.replaceoperation12);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(size);

				}

				break;
			case 13:
				if (MyProblem.replaceoperation13 != -1)
					randomInt2 = (int) (MyProblem.replaceoperation13);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(size);

				}

				break;
			case 14:
				if (MyProblem.replaceoperation14 != -1)
					randomInt2 = (int) (MyProblem.replaceoperation14);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(size);

				}

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

			// }
		} else {
			Random number_generator = new Random();
			randomInt2 = number_generator.nextInt(size);
			// randomInt2=(int) (Math.random() * (replacements.size()-1));
		}

		return randomInt2;
	}

	protected void StoreTwoIndex(int randomInt, int randomInt2, int location) {
		// TODO Auto-generated method stub
		// if (Operations.statecase.equals("case10")) {
		switch (MyProblem.indexoperation) {
		case 1:
			MyProblem.oldoperation1 = randomInt;
			MyProblem.replaceoperation1 = randomInt2;
			MyProblem.secondoldoperation1 = location;

			break;
		case 2:
			MyProblem.oldoperation2 = randomInt;
			MyProblem.replaceoperation2 = randomInt2;
			MyProblem.secondoldoperation2 = location;

			break;
		case 3:
			MyProblem.oldoperation3 = randomInt;
			MyProblem.replaceoperation3 = randomInt2;
			MyProblem.secondoldoperation3 = location;

			break;
		case 4:
			MyProblem.oldoperation4 = randomInt;
			MyProblem.replaceoperation4 = randomInt2;
			MyProblem.secondoldoperation4 = location;

			break;
		case 5:
			MyProblem.oldoperation5 = randomInt;
			MyProblem.replaceoperation5 = randomInt2;
			MyProblem.secondoldoperation5 = location;

			break;
		case 6:
			MyProblem.oldoperation6 = randomInt;
			MyProblem.replaceoperation6 = randomInt2;
			MyProblem.secondoldoperation6 = location;

			break;
		case 7:
			MyProblem.oldoperation7 = randomInt;
			MyProblem.replaceoperation7 = randomInt2;
			MyProblem.secondoldoperation7 = location;

			break;
		case 8:
			MyProblem.oldoperation8 = randomInt;
			MyProblem.replaceoperation8 = randomInt2;
			MyProblem.secondoldoperation8 = location;

			break;
		case 9:
			MyProblem.oldoperation9 = randomInt;
			MyProblem.replaceoperation9 = randomInt2;
			MyProblem.secondoldoperation9 = location;

			break;
		case 10:
			MyProblem.oldoperation10 = randomInt;
			MyProblem.replaceoperation10 = randomInt2;
			MyProblem.secondoldoperation10 = location;

			break;
		case 11:
			MyProblem.oldoperation11 = randomInt;
			MyProblem.replaceoperation11 = randomInt2;
			MyProblem.secondoldoperation11 = location;

			break;
		case 12:
			MyProblem.oldoperation12 = randomInt;
			MyProblem.replaceoperation12 = randomInt2;
			MyProblem.secondoldoperation12 = location;

			break;
		case 13:
			MyProblem.oldoperation13 = randomInt;
			MyProblem.replaceoperation13 = randomInt2;
			MyProblem.secondoldoperation13 = location;

			break;
		case 14:
			MyProblem.oldoperation14 = randomInt;
			MyProblem.replaceoperation14 = randomInt2;
			MyProblem.secondoldoperation14 = location;

			break;
		case 15:
			MyProblem.oldoperation15 = randomInt;
			MyProblem.replaceoperation15 = randomInt2;
			MyProblem.secondoldoperation15 = location;

			break;
		case 16:
			MyProblem.oldoperation16 = randomInt;
			MyProblem.replaceoperation16 = randomInt2;
			MyProblem.secondoldoperation16 = location;

			break;
		case 17:
			MyProblem.oldoperation17 = randomInt;
			MyProblem.replaceoperation17 = randomInt2;
			MyProblem.secondoldoperation17 = location;

			break;
		case 18:
			MyProblem.oldoperation18 = randomInt;
			MyProblem.replaceoperation18 = randomInt2;
			MyProblem.secondoldoperation18 = location;

			break;
		case 19:
			MyProblem.oldoperation19 = randomInt;
			MyProblem.replaceoperation19 = randomInt2;
			MyProblem.secondoldoperation19 = location;

			break;
		case 20:
			MyProblem.oldoperation20 = randomInt;
			MyProblem.replaceoperation20 = randomInt2;
			MyProblem.secondoldoperation20 = location;

			break;
		case 21:
			MyProblem.oldoperation21 = randomInt;
			MyProblem.replaceoperation21 = randomInt2;
			MyProblem.secondoldoperation21 = location;

			break;
		case 22:
			MyProblem.oldoperation22 = randomInt;
			MyProblem.replaceoperation22 = randomInt2;
			MyProblem.secondoldoperation22 = location;

			break;

		}

		// }

	}

	private void testbuffer() throws ATLCoreException {
		// TODO Auto-generated method stub
		String nsURI = this.atlModel3.toString();
		ModelFactory modelFactory = new EMFModelFactory();
		EMFReferenceModel atlMetamodel = (EMFReferenceModel) modelFactory.getBuiltInResource("ATL.ecore");
		AtlParser atlParser = new AtlParser();
		EMFModel atlModel2 = null;
		try {
			atlModel2 = (EMFModel) modelFactory.newModel(atlMetamodel);
		} catch (ATLCoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			atlParser.inject(atlModel2, nsURI);
		} catch (ATLCoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		atlModel2.setIsTarget(true);
		ATLModel wrapper2 = new ATLModel(atlModel2.getResource());

	}

	/*
	 * FOr PRedefined List<EObject> value = null; if (featureDefinition != null &&
	 * featureDefinition.getUpperBound() == 1) { if (featureDefinition2 != null)
	 * value = (List<EObject>)
	 * wrapper.source(modifiable.get(randomInt)).eGet(featureDefinition2);
	 * 
	 * EObject object2modify_src = wrapper.source(modifiable.get(randomInt)); Object
	 * oldFeatureValue = object2modify_src.eGet(featureDefinition);
	 * System.out.println("oldFeatureValue"); System.out.println("11111111111111");
	 * System.out.println(oldFeatureValue);
	 * System.out.println(modifiable.get(randomInt)); List<Object> replacements =
	 * this.replacements(modifiable.get(randomInt), oldFeatureValue.toString(),
	 * metamodel); System.out.println(replacements); int randomInt2 = (int)
	 * (Math.random() * (replacements.size())); // System.out.println( randomInt2 );
	 * // System.out.println(replacements.size()); // for (Object replacement :
	 * replacements) { if (replacements.size() > 0) { if
	 * (replacements.get(randomInt2) != null) {
	 * 
	 * boolean checkkind=false;
	 * if(oldFeatureValue.toString().equals("oclIsKindOf"))checkkind=true;
	 * 
	 * 
	 * if (replacements.get(randomInt2).toString().equals("oclIsKindOf") ||
	 * replacements.get(randomInt2).toString().equals("oclIsTypeOf")) { if
	 * (value.size() > 0 &&
	 * Class2Rel.hmap.get(oldFeatureValue).equals(Class2Rel.hmap.get(replacements.
	 * get(randomInt2))) ) {
	 * wrapper.source(modifiable.get(randomInt)).eSet(featureDefinition,
	 * replacements.get(randomInt2)); // System.out.println("choose replacement");
	 * System.out.println(replacements.get(randomInt2));
	 * System.out.println("333333333"); // mutation: documentation if (comments !=
	 * null) comments.add("\n-- MUTATION \"" + this.getDescription() + "\" from " +
	 * oldFeatureValue.toString() + " to " + replacements.get(randomInt2) +
	 * " (line " + modifiable.get(randomInt).getLocation() +
	 * " of original transformation)\n"); comment="\n-- MUTATION \"" +
	 * this.getDescription() + "\" from " + oldFeatureValue.toString() + " to " +
	 * replacements.get(randomInt2) + " (line " +
	 * modifiable.get(randomInt).getLocation() + " of original transformation)\n";
	 * // System.out.println( "\n-- MUTATION \"" + this.getDescription() +
	 * "\" from " + // oldFeatureValue.toString() + " to " +
	 * replacements.get(randomInt2) + " (line //
	 * " + modifiable.get(randomInt).getLocation() + " of original //
	 * transformation)\n");
	 * 
	 * // save mutant
	 * 
	 * System.out.println(oldFeatureValue.getClass());
	 * System.out.println(replacements.get(randomInt2).getClass());
	 * this.save(this.atlModel3, outputFolder); this.save2(this.atlModel3,
	 * outputFolder);
	 * 
	 * } } else if (oldFeatureValue.toString().equals("oclIsUndefined") ) {
	 * 
	 * if (!replacements.get(randomInt2).toString().equals("concat") &&
	 * !replacements.get(randomInt2).toString().equals("toString") &&
	 * !replacements.get(randomInt2).toString().equals("floor") &&
	 * !replacements.get(randomInt2).toString().equals("size") &&
	 * !replacements.get(randomInt2).toString().equals("abs") &&
	 * !replacements.get(randomInt2).toString().equals("endsWith") &&
	 * !replacements.get(randomInt2).toString().equals("startsWith") &&
	 * !replacements.get(randomInt2).toString().equals("max")&&
	 * Class2Rel.hmap.get(oldFeatureValue).equals(Class2Rel.hmap.get(replacements.
	 * get(randomInt2)))) {
	 * 
	 * wrapper.source(modifiable.get(randomInt)).eSet(featureDefinition,
	 * replacements.get(randomInt2)); // System.out.println("choose replacement");
	 * System.out.println(replacements.get(randomInt2));
	 * System.out.println("444444444");
	 * System.out.println(oldFeatureValue.getClass());
	 * System.out.println(replacements.get(randomInt2).getClass()); // mutation:
	 * documentation if (comments != null) comments.add("\n-- MUTATION \"" +
	 * this.getDescription() + "\" from " + oldFeatureValue.toString() + " to " +
	 * replacements.get(randomInt2) + " (line " +
	 * modifiable.get(randomInt).getLocation() + " of original transformation)\n");
	 * comment="\n-- MUTATION \"" + this.getDescription() + "\" from " +
	 * oldFeatureValue.toString() + " to " + replacements.get(randomInt2) +
	 * " (line " + modifiable.get(randomInt).getLocation() +
	 * " of original transformation)\n"; // System.out.println( "\n-- MUTATION \"" +
	 * this.getDescription() + "\" from " + // oldFeatureValue.toString() + " to " +
	 * replacements.get(randomInt2) + " (line //
	 * " + modifiable.get(randomInt).getLocation() + " of original //
	 * transformation)\n");
	 * 
	 * // save mutant this.save(this.atlModel3, outputFolder);
	 * this.save2(this.atlModel3, outputFolder);
	 * 
	 * }
	 * 
	 * } else if (oldFeatureValue.toString().equals("oclIsKindOf")) {
	 * 
	 * if (! replacements.get(randomInt2).toString().equals("oclType") &&
	 * !replacements.get(randomInt2).toString().equals("toString") &&
	 * !replacements.get(randomInt2).toString().equals("concat") &&
	 * !replacements.get(randomInt2).toString().equals("floor") &&
	 * !replacements.get(randomInt2).toString().equals("startsWith") &&
	 * !replacements.get(randomInt2).toString().equals("endsWith") &&
	 * Class2Rel.hmap.get(oldFeatureValue).equals(Class2Rel.hmap.get(replacements.
	 * get(randomInt2)))) {
	 * 
	 * wrapper.source(modifiable.get(randomInt)).eSet(featureDefinition,
	 * replacements.get(randomInt2)); // System.out.println("choose replacement");
	 * System.out.println(replacements.get(randomInt2));
	 * System.out.println("55555555555");
	 * System.out.println(oldFeatureValue.getClass());
	 * System.out.println(replacements.get(randomInt2).getClass());
	 * 
	 * // mutation: documentation if (comments != null)
	 * comments.add("\n-- MUTATION \"" + this.getDescription() + "\" from " +
	 * oldFeatureValue.toString() + " to " + replacements.get(randomInt2) +
	 * " (line " + modifiable.get(randomInt).getLocation() +
	 * " of original transformation)\n"); comment="\n-- MUTATION \"" +
	 * this.getDescription() + "\" from " + oldFeatureValue.toString() + " to " +
	 * replacements.get(randomInt2) + " (line " +
	 * modifiable.get(randomInt).getLocation() + " of original transformation)\n";
	 * // System.out.println( "\n-- MUTATION \"" + this.getDescription() +
	 * "\" from " + // oldFeatureValue.toString() + " to " +
	 * replacements.get(randomInt2) + " (line //
	 * " + modifiable.get(randomInt).getLocation() + " of original //
	 * transformation)\n");
	 * 
	 * // save mutant this.save(this.atlModel3, outputFolder);
	 * this.save2(this.atlModel3, outputFolder);
	 * 
	 * }
	 * 
	 * }
	 * 
	 * 
	 * else if(!oldFeatureValue.toString().equals("oclIsKindOf") &&
	 * !oldFeatureValue.toString().equals("oclIsUndefined") &&
	 * !replacements.get(randomInt2).toString().endsWith("oclIsKindOf") &&
	 * !replacements.get(randomInt2).toString().equals("oclIsTypeOf") &&
	 * Class2Rel.hmap.get(oldFeatureValue).equals(Class2Rel.hmap.get(replacements.
	 * get(randomInt2))) ) {
	 * 
	 * wrapper.source(modifiable.get(randomInt)).eSet(featureDefinition,
	 * replacements.get(randomInt2)); // System.out.println("choose replacement");
	 * System.out.println(replacements.get(randomInt2));
	 * System.out.println("888888888");
	 * System.out.println(oldFeatureValue.getClass());
	 * System.out.println(replacements.get(randomInt2).getClass());
	 * 
	 * // mutation: documentation if (comments != null)
	 * comments.add("\n-- MUTATION \"" + this.getDescription() + "\" from " +
	 * oldFeatureValue.toString() + " to " + replacements.get(randomInt2) +
	 * " (line " + modifiable.get(randomInt).getLocation() +
	 * " of original transformation)\n"); comment="\n-- MUTATION \"" +
	 * this.getDescription() + "\" from " + oldFeatureValue.toString() + " to " +
	 * replacements.get(randomInt2) + " (line " +
	 * modifiable.get(randomInt).getLocation() + " of original transformation)\n";
	 * // System.out.println( "\n-- MUTATION \"" + this.getDescription() +
	 * "\" from " + // oldFeatureValue.toString() + " to " +
	 * replacements.get(randomInt2) + " (line //
	 * " + modifiable.get(randomInt).getLocation() + " of original //
	 * transformation)\n");
	 * 
	 * // save mutant this.save(this.atlModel3, outputFolder);
	 * this.save2(this.atlModel3, outputFolder);
	 * 
	 * } // remove comment // if (comments!=null)
	 * comments.remove(comments.size()-1);
	 * 
	 * } // }
	 * 
	 * // restore original value // object2modify_src.eSet(featureDefinition,
	 * oldFeatureValue); } } // } Class2Rel.typeoperation=null;
	 * 
	 * 
	 */
	/*
	 * For Operator if (featureDefinition != null &&
	 * featureDefinition.getUpperBound() == 1) { EObject object2modify_src =
	 * wrapper.source(modifiable.get(randomInt)); Object oldFeatureValue =
	 * object2modify_src.eGet(featureDefinition);
	 * System.out.println("oldFeatureValueoperator");
	 * System.out.println(oldFeatureValue);
	 * 
	 * List<Object> replacements = this.replacements(modifiable.get(randomInt),
	 * oldFeatureValue.toString(), metamodel); System.out.println(replacements); int
	 * randomInt2 = (int) (Math.random() * (replacements.size()));
	 * 
	 * //Class2Rel.hmap.get(oldFeatureValue).equals(Class2Rel.hmap.get(replacements.
	 * get(randomInt2))) System.out.println(randomInt2); //
	 * System.out.println(replacements.size()); // for (Object replacement :
	 * replacements) { if (replacements.size() > 0) { if
	 * (replacements.get(randomInt2) != null ) {
	 * 
	 * if(oldFeatureValue.toString().equals("and") ) { if(
	 * (replacements.get(randomInt2).toString().equals("div") ||
	 * replacements.get(randomInt2).toString().equals("implies") ||
	 * replacements.get(randomInt2).toString().equals("+") ||
	 * replacements.get(randomInt2).toString().equals("mod")||
	 * replacements.get(randomInt2).toString().equals("xor")||
	 * replacements.get(randomInt2).toString().equals("-")||
	 * replacements.get(randomInt2).toString().equals("*")||
	 * replacements.get(randomInt2).toString().equals("/")) ) {
	 * 
	 * 
	 * 
	 * 
	 * 
	 * } else {
	 * 
	 * wrapper.source(modifiable.get(randomInt)).eSet(featureDefinition,
	 * replacements.get(randomInt2)); System.out.println("choose replacement");
	 * System.out.println(replacements.get(randomInt2));
	 * 
	 * // mutation: documentation if (comments != null)
	 * comments.add("\n-- MUTATION \"" + this.getDescription() + "\" from " +
	 * oldFeatureValue.toString() + " to " + replacements.get(randomInt2) +
	 * " (line " + modifiable.get(randomInt).getLocation() +
	 * " of original transformation)\n");
	 * 
	 * comment="\n-- MUTATION \"" + this.getDescription() + "\" from " +
	 * oldFeatureValue.toString() + " to " + replacements.get(randomInt2) +
	 * " (line " + modifiable.get(randomInt).getLocation() +
	 * " of original transformation)\n"; // save mutant this.save(this.atlModel3,
	 * outputFolder); this.save2(this.atlModel3, outputFolder);
	 * 
	 * 
	 * } } if(oldFeatureValue.toString().equals("implies") ) if
	 * ((replacements.get(randomInt2).toString().equals("+")) ) {
	 * 
	 * 
	 * 
	 * 
	 * 
	 * } else { wrapper.source(modifiable.get(randomInt)).eSet(featureDefinition,
	 * replacements.get(randomInt2)); System.out.println("choose replacement");
	 * System.out.println(replacements.get(randomInt2));
	 * 
	 * // mutation: documentation if (comments != null)
	 * comments.add("\n-- MUTATION \"" + this.getDescription() + "\" from " +
	 * oldFeatureValue.toString() + " to " + replacements.get(randomInt2) +
	 * " (line " + modifiable.get(randomInt).getLocation() +
	 * " of original transformation)\n");
	 * 
	 * comment="\n-- MUTATION \"" + this.getDescription() + "\" from " +
	 * oldFeatureValue.toString() + " to " + replacements.get(randomInt2) +
	 * " (line " + modifiable.get(randomInt).getLocation() +
	 * " of original transformation)\n"; // save mutant this.save(this.atlModel3,
	 * outputFolder); this.save2(this.atlModel3, outputFolder); } // remove comment
	 * // if (comments!=null) comments.remove(comments.size()-1); } } // }
	 * 
	 * // restore original value // object2modify_src.eSet(featureDefinition,
	 * oldFeatureValue); } Class2Rel.typeoperation=null;
	 * 
	 */
	/*
	 * For Collection if (featureDefinition != null &&
	 * featureDefinition.getUpperBound() == 1) { EObject object2modify_src =
	 * wrapper.source(modifiable.get(randomInt)); Object oldFeatureValue =
	 * object2modify_src.eGet(featureDefinition);
	 * System.out.println("oldFeatureValueoperator");
	 * System.out.println(oldFeatureValue);
	 * 
	 * List<Object> replacements = this.replacements(modifiable.get(randomInt),
	 * oldFeatureValue.toString(), metamodel); System.out.println(replacements); int
	 * randomInt2 = (int) (Math.random() * (replacements.size()));
	 * 
	 * //Class2Rel.hmap.get(oldFeatureValue).equals(Class2Rel.hmap.get(replacements.
	 * get(randomInt2))) System.out.println(randomInt2); //
	 * System.out.println(replacements.size()); // for (Object replacement :
	 * replacements) { if (replacements.size() > 0) { if
	 * (replacements.get(randomInt2) != null ) {
	 * 
	 * if(oldFeatureValue.toString().equals("isEmpty") ) { if(
	 * (replacements.get(randomInt2).toString().equals("prepend") ||
	 * replacements.get(randomInt2).toString().equals("including") //
	 * replacements.get(randomInt2).toString().equals("+") || //
	 * replacements.get(randomInt2).toString().equals("mod")|| //
	 * replacements.get(randomInt2).toString().equals("xor")|| //
	 * replacements.get(randomInt2).toString().equals("-")|| //
	 * replacements.get(randomInt2).toString().equals("*")|| //
	 * replacements.get(randomInt2).toString().equals("/") ) ) {
	 * 
	 * 
	 * 
	 * 
	 * 
	 * } } if(oldFeatureValue.toString().equals("first") ) if
	 * ((replacements.get(randomInt2).toString().equals("append")) ||
	 * replacements.get(randomInt2).toString().equals("including") ||
	 * replacements.get(randomInt2).toString().equals("union") ||
	 * replacements.get(randomInt2).toString().equals("excluding") ||
	 * replacements.get(randomInt2).toString().equals("prepend") ) {
	 * 
	 * 
	 * 
	 * 
	 * 
	 * } else { wrapper.source(modifiable.get(randomInt)).eSet(featureDefinition,
	 * replacements.get(randomInt2)); System.out.println("choose replacement");
	 * System.out.println(replacements.get(randomInt2));
	 * 
	 * // mutation: documentation if (comments != null)
	 * comments.add("\n-- MUTATION \"" + this.getDescription() + "\" from " +
	 * oldFeatureValue.toString() + " to " + replacements.get(randomInt2) +
	 * " (line " + modifiable.get(randomInt).getLocation() +
	 * " of original transformation)\n");
	 * 
	 * comment="\n-- MUTATION \"" + this.getDescription() + "\" from " +
	 * oldFeatureValue.toString() + " to " + replacements.get(randomInt2) +
	 * " (line " + modifiable.get(randomInt).getLocation() +
	 * " of original transformation)\n"; // save mutant this.save(this.atlModel3,
	 * outputFolder); this.save2(this.atlModel3, outputFolder); } // remove comment
	 * // if (comments!=null) comments.remove(comments.size()-1); } } // }
	 * 
	 * // restore original value // object2modify_src.eSet(featureDefinition,
	 * oldFeatureValue); } Class2Rel.typeoperation=null;
	 * 
	 * 
	 */

}
