package creation.operator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.core.ModelFactory;
import org.eclipse.m2m.atl.core.emf.EMFModel;
import org.eclipse.m2m.atl.core.emf.EMFModelFactory;
import org.eclipse.m2m.atl.core.emf.EMFReferenceModel;
import org.eclipse.m2m.atl.engine.parser.AtlParser;

import transML.exceptions.transException;
import transML.utils.modeling.EMFUtils;
import witness.generator.MetaModel;
import anatlyzer.atlext.ATL.ATLFactory;
import anatlyzer.atlext.ATL.Binding;
import anatlyzer.atlext.ATL.MatchedRule;
import anatlyzer.atlext.ATL.Module;
import anatlyzer.atlext.ATL.OutPatternElement;
import anatlyzer.atlext.ATL.Rule;
import anatlyzer.atlext.OCL.BooleanExp;
import anatlyzer.atlext.OCL.IntegerExp;
import anatlyzer.atlext.OCL.NavigationOrAttributeCallExp;
import anatlyzer.atlext.OCL.OCLFactory;
import anatlyzer.atlext.OCL.OclExpression;
import anatlyzer.atlext.OCL.RealExp;
import anatlyzer.atlext.OCL.StringExp;
import anatlyzer.atlext.OCL.VariableDeclaration;
import anatlyzer.atlext.OCL.VariableExp;
//bod import anatlyzer.evaluation.mutators.ATLModel;
import anatlyzer.atl.model.ATLModel;
import evaluation.mutator.AbstractMutator;
import deletion.operator.AbstractDeletionMutator;
//import anatlyzer.evaluation.tester.Tester;
import ca.udem.fixingatlerror.explorationphase.Operations;
import ca.udem.fixingatlerror.explorationphase.Setting;
import jmetal.core.Solution;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.problems.MyProblem;

public class BindingCreationMutator extends AbstractMutator {

	private String m = "C:/Users/varaminz/Downloads/Project/workspaceTEST34/anatlyzer.atl.tests.api/trafo2/zahra2mutants/finalresult.atl";
	private EMFModel atlModel3;

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
		List<Rule> modifiable = (List<Rule>) wrapper.allObjectsOf(Rule.class);
		ReturnResult.add(wrapper);
		ReturnResult.add(atlModel);
		String comment = null;
		Module module = wrapper.getModule();
		EDataTypeEList<String> comments = null;
		if (module != null) {
			EStructuralFeature feature = wrapper.source(module).eClass().getEStructuralFeature("commentsAfter");
			comments = (EDataTypeEList<String>) wrapper.source(module).eGet(feature);
		}
		int randomInt = -2;
		boolean checkmutationapply = false;
		int counter = -1;
		while (checkmutationapply == false) {

			// System.out.println(counter);
			List<Integer> Result = ReturnFirstIndex(randomInt, modifiable.size(), checkmutationapply, solution);
			randomInt = Result.get(0);
			// NSGAII.writer.println(randomInt);
			// NSGAII.writer.println(solution.getpreviousgeneration());
			if (Result.get(1) == 0)
				checkmutationapply = false;
			else
				checkmutationapply = false;

			if (randomInt == -2)
				checkmutationapply = true;

			else if (solution.getpreviousgeneration() == true) {
				// System.out.println(Operations.statecase);
				// System.out.println(MyProblem.indexoperation);
				ReturnResult = OperationPreviousGenerationDeletion(randomInt, solution, atlModel, modifiable, wrapper,
						comments, ReturnResult, outputMM);
				checkmutationapply = true;
			}
			// && akhar o pak konam
			else if (randomInt != -1 && randomInt != -2 && solution.getpreviousgeneration() == false) {
				// NSGAII.writer.println("createmodify8");
				List<? extends VariableDeclaration> ivariables = getVariableDeclarations(modifiable.get(randomInt));
				// System.out.println("createmodify8");

				// if (modifiable.get(randomInt).getOutPattern() != null) {
				// int randomInt2 = (int) (Math.random() *
				// (modifiable.get(randomInt).getOutPattern().getElements().size()));
				int randomInt2 = -2;
				randomInt2 = FindSecondIndex(randomInt2,
						modifiable.get(randomInt).getOutPattern().getElements().size());

				String[] array = modifiable.get(randomInt).getOutPattern().getElements().get(randomInt2).getLocation()
						.split(":", 2);
				int indexrule = -1;

				for (int j = 0; j < NSGAII.faultrule.size(); j++) {
					if (Integer.parseInt(array[0]) > NSGAII.faultrule.get(j)
							&& Integer.parseInt(array[0]) < NSGAII.finalrule.get(j))

						indexrule = j;

				}
				// System.out.println(Integer.parseInt(array[0]));

				if (NSGAII.errorrule.contains(indexrule) && ivariables.size() > 0) {
					EClassifier classifier = outputMM.getEClassifier(modifiable.get(randomInt).getOutPattern()
							.getElements().get(randomInt2).getType().getName());
					String[] array2 = modifiable.get(randomInt).getOutPattern().getElements().get(randomInt2)
							.getLocation().split(":", 2);
					if (classifier instanceof EClass) {
						EStructuralFeature feature = wrapper
								.source(modifiable.get(randomInt).getOutPattern().getElements().get(randomInt2))
								.eClass().getEStructuralFeature("bindings");
						List<Binding> realbindings = (List<Binding>) wrapper
								.source(modifiable.get(randomInt).getOutPattern().getElements().get(randomInt2))
								.eGet(feature);
						int row = 0;

						if (realbindings.size() > 0) {
							for (int h = 0; h < solution.getCoSolution().getOp().listpropertynamelocation.size(); h++) {
								if (solution.getCoSolution().getOp().listpropertynamelocation.get(h).size() > 0)
									if (solution.getCoSolution().getOp().listpropertynamelocation.get(h)
											.get(0) == Integer.parseInt(array2[0]) + 1) {
										row = h;
										break;
									}
							}
							List<Binding> newbindings = new ArrayList<Binding>();

							newbindings
									.add(this.getBinding6(
											(EClass) classifier, outputMM, modifiable.get(randomInt).getOutPattern()
													.getElements().get(randomInt2).getBindings(),
											solution, ivariables, row)); // binding for property of subclass (correct
																			// value)
							// NSGAII.writer.println(newbindings);
							// System.out.println(newbindings);
							for (Binding binding : newbindings) {
								boolean checkforcreate = false;
								for (int i = 0; i < realbindings.size(); i++) {
									if (binding != null) {
										if (solution.getCoSolution().getOp().originalwrapper.get(row).get(i) == 1
												&& realbindings.get(i).getValue().getClass()
														.equals(binding.getValue().getClass())
												&& realbindings.get(i).getPropertyName()
														.equals(binding.getPropertyName())) {
											checkforcreate = true;

										}
									}
								}
								if (binding != null && checkforcreate == false) {

									// System.out.println(realbindings.size());

									// mutation: add binding
									// realbindings.add(binding);

									solution.newbindings.set(MyProblem.indexoperation - 1, binding);
									// System.out.println(solution.newbindings.get(MyProblem.indexoperation - 1));
									realbindings.add(binding);
									solution.getCoSolution().getOp().originalwrapper.get(row).add(1);
									StoreTwoIndex(randomInt, randomInt2);
									// mutation: documentation
									// if (comments!=null)
									comments.add("\n-- MUTATION \"" + this.getDescription() + "\" " + toString(binding)
											+ " in "
											+ toString(modifiable.get(randomInt).getOutPattern().getElements()
													.get(randomInt2))
											+ " (line " + modifiable.get(randomInt).getOutPattern().getElements()
													.get(randomInt2).getLocation()
											+ " of original transformation)\n");
									System.out.println("\n-- MUTATION \"" + this.getDescription() + "\" "
											+ toString(binding) + " in "
											+ toString(modifiable.get(randomInt).getOutPattern().getElements()
													.get(randomInt2))
											+ " (line " + modifiable.get(randomInt).getOutPattern().getElements()
													.get(randomInt2).getLocation()
											+ " of original transformation)\n");

									/*
									 * NSGAII.writer.println("\n-- MUTATION \"" + this.getDescription() + "\" " +
									 * toString(binding) + " in " +
									 * toString(modifiable.get(randomInt).getOutPattern().getElements().get(
									 * randomInt2)) + " (line " +
									 * modifiable.get(randomInt).getOutPattern().getElements()
									 * .get(randomInt2).getLocation() + " of original transformation)\n");
									 */

									comment = "\n-- MUTATION \"" + this.getDescription() + "\" " + toString(binding)
											+ " in "
											+ toString(modifiable.get(randomInt).getOutPattern().getElements()
													.get(randomInt2))
											+ " (line " + modifiable.get(randomInt).getOutPattern().getElements()
													.get(randomInt2).getLocation()
											+ " of original transformation)\n";
									ReturnResult.set(0, wrapper);
									ReturnResult.set(1, atlModel);
									NSGAII.numop = NSGAII.numop + 1;
									checkmutationapply = true;

									ReturnResult.add(comment);

								}

							}
							counter = counter + 1;
							if (counter == 7)
								checkmutationapply = true;

						}
					}

				}

			}
		}

		return ReturnResult;
	}

	private List<Object> OperationPreviousGenerationDeletion(int randomInt, Solution solution, EMFModel atlModel,
			List<Rule> modifiable, ATLModel wrapper, EDataTypeEList<String> comments, List<Object> ReturnResult,
			MetaModel outputMM) {
		// TODO Auto-generated method stub

		String comment = null;
		Rule modifiable2 = modifiable.get(randomInt);
		int randomInt2 = -2;
		randomInt2 = FindSecondIndex(randomInt2, modifiable2.getOutPattern().getElements().size());

		// EClassifier classifier = outputMM.getEClassifier(
		// modifiable2.getOutPattern().getElements().get(randomInt2).getType().getName());

		String[] array2 = modifiable2.getOutPattern().getElements().get(randomInt2).getLocation().split(":", 2);
		int row = 0;
		for (int h = 0; h < solution.getCoSolution().getOp().listpropertynamelocation.size(); h++) {
			if (solution.getCoSolution().getOp().listpropertynamelocation.get(h).size() > 0)
				if (solution.getCoSolution().getOp().listpropertynamelocation.get(h)
						.get(0) == Integer.parseInt(array2[0]) + 1) {
					row = h;
					break;
				}
		}

		// if (classifier instanceof EClass) {
		EStructuralFeature feature = wrapper.source(modifiable2.getOutPattern().getElements().get(randomInt2)).eClass()
				.getEStructuralFeature("bindings");
		List<Binding> realbindings = (List<Binding>) wrapper
				.source(modifiable2.getOutPattern().getElements().get(randomInt2)).eGet(feature);

		// Binding b=solution.newbindings.get(MyProblem.indexoperation-1);
		String str = solution.newstring.get(MyProblem.indexoperation - 1);
		// System.out.println("create2");
		// System.out.println(b);
		// NSGAII.writer.println("create2");
		// NSGAII.writer.println(str);
		Binding b2 = ATLFactory.eINSTANCE.createBinding();
		b2.setPropertyName(str);
		String classifier2 = solution.modifypropertyname.get(MyProblem.indexoperation - 1);
		// System.out.println(classifier2);
		EStructuralFeature newfeature = null;
		for (EClassifier classifier : outputMM.getEClassifiers()) {
			if (classifier instanceof EClass) {
				EClass child = ((EClass) classifier);
				for (int i = 0; i < child.getEStructuralFeatures().size(); i++) {
					if (child.getEStructuralFeatures().get(i).getName().equals(str)) {
						newfeature = child.getEStructuralFeatures().get(i);
						break;
					}
				}
			}
		}
		int index = -1;
		// System.out.println(NSGAII.classifierliast.indexOf(classifier2));
		/*
		 * for (int p = 0; p < NSGAII.classifierliast.size(); p++) if
		 * (NSGAII.classifierliast.get(p).getName().equals(classifier2)) index = p;
		 * System.out.println(index); EClass child = ((EClass)
		 * NSGAII.classifierliast.get(index));
		 */
		// NSGAII.writer.println(b.getPropertyName());
		// NSGAII.writer.println(classifier2);
		// for (int p = 0; p < child.getEStructuralFeatures().size(); p++) {
		// if (child.getEStructuralFeatures().get(p).getName().equals(str)) {
		// EStructuralFeature newfeature = child.getEStructuralFeatures().get(p);
		// b2.setValue(solution.expression.get(MyProblem.indexoperation-1));
		List<? extends VariableDeclaration> ivariables = getVariableDeclarations(modifiable2);
		b2.setValue(getCompatibleValue2(newfeature.getEType().getName(), true, true, null, newfeature.getUpperBound(),
				ivariables));
		boolean checkforcreate = false;
		/*
		 * System.out.println(str); System.out.println(randomInt2);
		 * System.out.println(row); System.out.println(realbindings.size());
		 */
		for (int i = 0; i < realbindings.size(); i++) {
			if (solution.getCoSolution().getOp().originalwrapper.get(row).get(i) == 1
					&& realbindings.get(i).getValue().getClass().equals(b2.getValue().getClass())
					&& realbindings.get(i).getPropertyName().equals(str)) {
				checkforcreate = true;

			}
		}
		// if(!realbindings.contains(b2)) {
		if (checkforcreate == false) {
			realbindings.add(b2);

			/*
			 * System.out.println("aftercreate2");
			 * System.out.println(solution.expression.get(MyProblem.indexoperation - 1));
			 * System.out.println(solution.getCoSolution().getOp().listpropertyname.get(row)
			 * );
			 */
			// solution.inorout.set(MyProblem.indexoperation - 1, "out");
			solution.getCoSolution().getOp().listpropertyname.get(row).add(str);
			solution.getCoSolution().getOp().listpropertynamelocation.get(row)
					.add(solution.getCoSolution().getOp().listpropertynamelocation.get(row)
							.get(solution.getCoSolution().getOp().listpropertynamelocation.get(row).size() - 1) + 1);

			solution.getCoSolution().getOp().originalwrapper.get(row).add(1);
			StoreTwoIndex(randomInt, randomInt2);
			// mutation: documentation
			// if (comments!=null)
			System.out.println("\n-- MUTATION \"" + this.getDescription() + "\" " + str + " in "
					+ toString(modifiable2.getOutPattern().getElements().get(randomInt2)) + " (line "
					+ modifiable.get(randomInt).getOutPattern().getElements().get(randomInt2).getLocation()
					+ " of original transformation)\n");
			/*
			 * NSGAII.writer.println("\n-- MUTATION \"" + this.getDescription() + "\" " +
			 * str + " in " +
			 * toString(modifiable2.getOutPattern().getElements().get(randomInt2)) +
			 * " (line " +
			 * modifiable.get(randomInt).getOutPattern().getElements().get(randomInt2).
			 * getLocation() + " of original transformation)\n");
			 */

			comments.add("\n-- MUTATION \"" + this.getDescription() + "\" " + str + " in "
					+ toString(modifiable2.getOutPattern().getElements().get(randomInt2)) + " (line "
					+ modifiable2.getOutPattern().getElements().get(randomInt2).getLocation()
					+ " of original transformation)\n");
			comment = "\n-- MUTATION \"" + this.getDescription() + "\" " + str + " in "
					+ toString(modifiable2.getOutPattern().getElements().get(randomInt2)) + " (line "
					+ modifiable.get(randomInt).getOutPattern().getElements().get(randomInt2).getLocation()
					+ " of original transformation)\n";
			ReturnResult.set(0, wrapper);
			ReturnResult.set(1, atlModel);
			NSGAII.numop = NSGAII.numop + 1;
			// checkmutationapply = true;
			solution.newbindings.set(MyProblem.indexoperation - 1, b2);
			ReturnResult.add(comment);
		} else {
			solution.newbindings.set(MyProblem.indexoperation - 1, null);
			solution.inorout.set(MyProblem.indexoperation - 1, "empty");
			StoreTwoIndex(-2, -2);

		}

		// if(!solution.getCoSolution().getOp().listpropertyname.get(row).contains(b.getPropertyName()))
		// {

		// }

		return ReturnResult;
	}

	private OclExpression getCompatibleValue2(String type, boolean monovalued, boolean ordered, Object object,
			int bound, List<? extends VariableDeclaration> variables) {
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
				// expression = OCLFactory.eINSTANCE.createSequenceExp();
				expression = OCLFactory.eINSTANCE.createVariableExp();
				List<EStructuralFeature> maintarget = new ArrayList<EStructuralFeature>();
				Setting s = new Setting();
				MetaModel metatarget = null;
				try {
					metatarget = new MetaModel(s.gettargetmetamodel());
				} catch (transException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for (EClassifier classifier : metatarget.getEClassifiers()) {
					if (classifier instanceof EClass) {
						for (int y = 0; y < classifier.eContents().size(); y++) {
							if (classifier.eContents().get(y) instanceof EReference) {
								maintarget.add((EStructuralFeature) classifier.eContents().get(y));
							}
						}
					}
				}

				int randomInt2 = -2;
				randomInt2 = FindThirdIndex(randomInt2, maintarget.size());
				NavigationOrAttributeCallExp nc = OCLFactory.eINSTANCE.createNavigationOrAttributeCallExp();
				nc.setIsStaticCall(false);
				nc.setImplicitlyCasted(false);
				nc.setName(maintarget.get(randomInt2).getName());

				VariableDeclaration sel = OCLFactory.eINSTANCE.createVariableDeclaration();
				String str = variables.get(0).getVarName();

				sel.setVarName(str);
				VariableExp vExp = OCLFactory.eINSTANCE.createVariableExp();
				vExp.setReferredVariable(variables.get(0));
				nc.setSource(vExp);
				nc.setInitializedVariable(sel);
				(expression) = (sel.getInitExpression());

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

	private int FindSecondIndex(int randomInt2, int size) {
		// TODO Auto-generated method stub
		if (NSGAII.statemutcrossinitial == "mutation") {

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

		} else {
			Random number_generator = new Random();
			randomInt2 = number_generator.nextInt(size);
			// randomInt2=(int) (Math.random() * (replacements.size()-1));
		}

		return randomInt2;

	}

	private int FindThirdIndex(int randomInt2, int size) {
		// TODO Auto-generated method stub
		if (NSGAII.statemutcrossinitial == "mutation") {

			switch (MyProblem.indexoperation) {
			case 1:
				if (MyProblem.secondoldoperation1 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation1);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(size);

				}

				break;
			case 2:
				if (MyProblem.secondoldoperation2 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation2);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(size);

				}

				break;
			case 3:
				if (MyProblem.secondoldoperation3 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation3);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(size);

				}

				break;
			case 4:
				if (MyProblem.secondoldoperation4 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation4);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(size);

				}

				break;
			case 5:
				if (MyProblem.secondoldoperation5 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation5);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(size);

				}

				break;
			case 6:
				if (MyProblem.secondoldoperation6 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation6);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(size);

				}

				break;
			case 7:
				if (MyProblem.secondoldoperation7 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation7);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(size);

				}

				break;
			case 8:
				if (MyProblem.secondoldoperation8 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation8);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(size);

				}

				break;
			case 9:
				if (MyProblem.secondoldoperation9 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation9);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(size);

				}

				break;
			case 10:
				if (MyProblem.secondoldoperation10 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation10);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(size);

				}

				break;
			case 11:
				if (MyProblem.secondoldoperation11 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation11);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(size);

				}

				break;
			case 12:
				if (MyProblem.secondoldoperation12 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation12);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(size);

				}

				break;
			case 13:
				if (MyProblem.secondoldoperation13 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation13);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(size);

				}

				break;
			case 14:
				if (MyProblem.secondoldoperation14 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation14);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(size);

				}

				break;
			case 15:
				if (MyProblem.secondoldoperation15 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation15);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(size);

				}

				break;
			case 16:
				if (MyProblem.secondoldoperation16 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation16);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(size);

				}

				break;
			case 17:
				if (MyProblem.secondoldoperation17 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation17);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(size);

				}

				break;
			case 18:
				if (MyProblem.secondoldoperation18 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation18);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(size);

				}

				break;
			case 19:
				if (MyProblem.secondoldoperation19 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation19);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(size);

				}

				break;
			case 20:
				if (MyProblem.secondoldoperation20 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation20);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(size);

				}

				break;
			case 21:
				if (MyProblem.secondoldoperation21 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation21);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(size);

				}

				break;
			case 22:
				if (MyProblem.secondoldoperation22 != -1)
					randomInt2 = (int) (MyProblem.secondoldoperation22);
				else {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(size);

				}

				break;

			}

		} else {
			Random number_generator = new Random();
			randomInt2 = number_generator.nextInt(size);
			// randomInt2=(int) (Math.random() * (replacements.size()-1));
		}

		return randomInt2;

	}

	private List<Integer> ReturnFirstIndex(int randomInt, int size, boolean checkmutationapply, Solution solution) {
		// TODO Auto-generated method stub
		if (NSGAII.statemutcrossinitial.equals("mutation")) {

			switch (MyProblem.indexoperation) {

			case 1:
				NSGAII.writer.println(MyProblem.oldoperation1);
				if (MyProblem.oldoperation1 != -1) {
					randomInt = (int) (MyProblem.oldoperation1);
					solution.setpreviousgeneration(true);
				} else

				{
					Random number_generator = new Random();
					if (size > 1)
						randomInt = number_generator.nextInt(size);
					else
						randomInt = 0;
					solution.setpreviousgeneration(false);

				}
				NSGAII.writer.println(randomInt);
				// randomInt=(int) (Math.random() * (containers.size()));

				break;
			case 2:
				NSGAII.writer.println(MyProblem.oldoperation2);
				if (MyProblem.oldoperation2 != -1) {
					randomInt = (int) (MyProblem.oldoperation2);
					solution.setpreviousgeneration(true);
				} else

				{
					Random number_generator = new Random();
					if (size > 1)
						randomInt = number_generator.nextInt(size);
					else
						randomInt = 0;
					solution.setpreviousgeneration(false);

				}
				NSGAII.writer.println(randomInt);
				// randomInt=(int) (Math.random() * (containers.size()));

				break;
			case 3:
				NSGAII.writer.println(MyProblem.oldoperation3);
				if (MyProblem.oldoperation3 != -1) {
					randomInt = (int) (MyProblem.oldoperation3);
					solution.setpreviousgeneration(true);
				} else {
					Random number_generator = new Random();
					if (size > 1)
						randomInt = number_generator.nextInt(size);
					else
						randomInt = 0;
					solution.setpreviousgeneration(false);

				}
				NSGAII.writer.println(randomInt);
				break;
			case 4:
				NSGAII.writer.println(MyProblem.oldoperation4);
				if (MyProblem.oldoperation4 != -1) {
					randomInt = (int) (MyProblem.oldoperation4);
					solution.setpreviousgeneration(true);
				} else {
					Random number_generator = new Random();
					if (size > 1)
						randomInt = number_generator.nextInt(size);
					else
						randomInt = 0;
					solution.setpreviousgeneration(false);

				}
				NSGAII.writer.println(randomInt);
				break;
			case 5:
				if (MyProblem.oldoperation5 != -1) {
					randomInt = (int) (MyProblem.oldoperation5);
					solution.setpreviousgeneration(true);
				} else {
					Random number_generator = new Random();
					if (size > 1)
						randomInt = number_generator.nextInt(size);
					else
						randomInt = 0;
					solution.setpreviousgeneration(false);
				}

				// randomInt=(int) (Math.random() * (containers.size()));

				break;
			case 6:
				if (MyProblem.oldoperation6 != -1) {
					randomInt = (int) (MyProblem.oldoperation6);
					solution.setpreviousgeneration(true);
				} else {
					Random number_generator = new Random();
					if (size > 1)
						randomInt = number_generator.nextInt(size);
					else
						randomInt = 0;
					solution.setpreviousgeneration(false);
				}

				// randomInt=(int) (Math.random() * (containers.size()));

				break;
			case 7:
				if (MyProblem.oldoperation7 != -1) {
					randomInt = (int) (MyProblem.oldoperation7);
					solution.setpreviousgeneration(true);
				} else {
					Random number_generator = new Random();
					if (size > 1)
						randomInt = number_generator.nextInt(size);
					else
						randomInt = 0;
					solution.setpreviousgeneration(false);
				}

				break;
			case 8:
				if (MyProblem.oldoperation8 != -1) {
					randomInt = (int) (MyProblem.oldoperation8);
					solution.setpreviousgeneration(true);
				} else {
					Random number_generator = new Random();
					if (size > 1)
						randomInt = number_generator.nextInt(size);
					else
						randomInt = 0;
					solution.setpreviousgeneration(false);
				}

				break;
			case 9:
				if (MyProblem.oldoperation9 != -1) {
					randomInt = (int) (MyProblem.oldoperation9);
					solution.setpreviousgeneration(true);
				} else {
					Random number_generator = new Random();
					if (size > 1)
						randomInt = number_generator.nextInt(size);
					else
						randomInt = 0;
					solution.setpreviousgeneration(false);
				}

				// randomInt=(int) (Math.random() * (containers.size()));

				break;
			case 10:
				if (MyProblem.oldoperation10 != -1) {
					randomInt = (int) (MyProblem.oldoperation10);
					solution.setpreviousgeneration(true);
				} else {
					Random number_generator = new Random();
					if (size > 1)
						randomInt = number_generator.nextInt(size);
					else
						randomInt = 0;
					solution.setpreviousgeneration(false);
				}

				// randomInt=(int) (Math.random() * (containers.size()));

				break;
			case 11:
				if (MyProblem.oldoperation11 != -1) {
					randomInt = (int) (MyProblem.oldoperation11);
					solution.setpreviousgeneration(true);
				} else {
					Random number_generator = new Random();
					if (size > 1)
						randomInt = number_generator.nextInt(size);
					else
						randomInt = 0;
					solution.setpreviousgeneration(false);
				}

				// randomInt=(int) (Math.random() * (containers.size()));

				break;
			case 12:
				if (MyProblem.oldoperation12 != -1) {
					randomInt = (int) (MyProblem.oldoperation12);
					solution.setpreviousgeneration(true);
				} else {
					Random number_generator = new Random();
					if (size > 1)
						randomInt = number_generator.nextInt(size);
					else
						randomInt = 0;
					solution.setpreviousgeneration(false);
				}

				// randomInt=(int) (Math.random() * (containers.size()));

				break;
			case 13:
				if (MyProblem.oldoperation13 != -1) {
					randomInt = (int) (MyProblem.oldoperation13);
					solution.setpreviousgeneration(true);
				} else {
					Random number_generator = new Random();
					if (size > 1)
						randomInt = number_generator.nextInt(size);
					else
						randomInt = 0;
					solution.setpreviousgeneration(false);
				}

				break;
			case 14:
				if (MyProblem.oldoperation14 != -1) {
					randomInt = (int) (MyProblem.oldoperation14);
					solution.setpreviousgeneration(true);
				} else {
					Random number_generator = new Random();
					if (size > 1)
						randomInt = number_generator.nextInt(size);
					else
						randomInt = 0;
					solution.setpreviousgeneration(false);
				}

				break;

			}

		} else {
			{
				Random number_generator = new Random();
				if (size > 1)
					randomInt = number_generator.nextInt(size);
				else
					randomInt = 0;
				checkmutationapply = true;
				solution.setpreviousgeneration(false);
			}

		}
		List<Integer> Result = new ArrayList<Integer>();
		Result.add(randomInt);
		if (checkmutationapply == true)
			Result.add(1);
		else
			Result.add(0);
		return Result;

	}

	private void StoreThirdIndex(int randomInt) {
		// TODO Auto-generated method stub

		switch (MyProblem.indexoperation) {
		case 1:
			MyProblem.secondoldoperation1 = randomInt;

			break;
		case 2:
			MyProblem.secondoldoperation2 = randomInt;

			break;
		case 3:
			MyProblem.secondoldoperation3 = randomInt;

			break;
		case 4:
			MyProblem.secondoldoperation4 = randomInt;

			break;
		case 5:
			MyProblem.secondoldoperation5 = randomInt;

			break;
		case 6:
			MyProblem.secondoldoperation6 = randomInt;

			break;
		case 7:
			MyProblem.secondoldoperation7 = randomInt;

			break;
		case 8:
			MyProblem.secondoldoperation8 = randomInt;

			break;
		case 9:
			MyProblem.secondoldoperation9 = randomInt;

			break;
		case 10:
			MyProblem.secondoldoperation10 = randomInt;

			break;
		case 11:
			MyProblem.secondoldoperation11 = randomInt;

			break;
		case 12:
			MyProblem.secondoldoperation12 = randomInt;

			break;
		case 13:
			MyProblem.secondoldoperation13 = randomInt;

			break;
		case 14:
			MyProblem.secondoldoperation14 = randomInt;

			break;
		case 15:
			MyProblem.secondoldoperation15 = randomInt;

			break;
		case 16:
			MyProblem.secondoldoperation16 = randomInt;

			break;
		case 17:
			MyProblem.secondoldoperation17 = randomInt;

			break;
		case 18:
			MyProblem.secondoldoperation18 = randomInt;

			break;
		case 19:
			MyProblem.secondoldoperation19 = randomInt;

			break;
		case 20:
			MyProblem.secondoldoperation20 = randomInt;

			break;
		case 21:
			MyProblem.secondoldoperation21 = randomInt;

			break;
		case 22:
			MyProblem.secondoldoperation22 = randomInt;

			break;

		}

	}

	private void StoreTwoIndex(int randomInt, int randomInt2) {
		// TODO Auto-generated method stub

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

	public List<? extends VariableDeclaration> getVariableDeclarations(Rule rule) {
		if (rule instanceof MatchedRule && ((MatchedRule) rule).getInPattern() != null)
			return ((MatchedRule) rule).getInPattern().getElements();
		if (rule.getVariables() != null)
			return rule.getVariables();
		return new ArrayList<>();
	}

	@Override
	public String getDescription() {
		return "Creation of Binding";
	}

	/**
	 * It returns a duplicate binding.
	 * 
	 * @param clazz
	 *            out class for binding
	 * @param bindings
	 *            list of bindings defined for the our class
	 * @param ivariables
	 *            input variable declarations
	 */
	private Binding getBinding1(EClass clazz, List<Binding> bindings, List<? extends VariableDeclaration> ivariables) {
		if (bindings != null && bindings.size() > 0) {
			Binding duplicate = bindings.get(new Random().nextInt(bindings.size()));
			String propertyName = duplicate.getPropertyName();
			// if (clazz.getEStructuralFeature(propertyName) == null)
			// System.out.println("error dare");

			Binding binding = null;
			if (clazz.getEStructuralFeature(propertyName) != null) {
				binding = ATLFactory.eINSTANCE.createBinding();
				binding.setPropertyName(propertyName);

				binding.setValue(getCompatibleValue(clazz.getEStructuralFeature(propertyName), ivariables));
			}
			return binding;
		}
		return null;
	}

	/**
	 * It returns a non-duplicate binding with primitive type and correct value.
	 * 
	 * @param clazz
	 *            out class for binding
	 * @param bindings
	 *            list of bindings defined for the our class
	 * @param ivariables
	 *            input variable declarations
	 */
	private Binding getBinding2(EClass clazz, List<Binding> bindings, List<? extends VariableDeclaration> ivariables) {
		for (EStructuralFeature feature : clazz.getEAllStructuralFeatures()) {
			if (hasPrimitiveType(feature)
					&& !bindings.stream().anyMatch(b -> b.getPropertyName().equals(feature.getName()))) {
				String propertyName = feature.getName();
				Binding binding = ATLFactory.eINSTANCE.createBinding();
				binding.setPropertyName(propertyName);

				binding.setValue(getCompatibleValue(feature, ivariables));
				return binding;
			}
		}
		return null;
	}

	/**
	 * It returns a non-duplicate binding with non-primitive type and correct value.
	 * 
	 * @param clazz
	 *            out class for binding
	 * @param bindings
	 *            list of bindings defined for the our class
	 * @param ivariables
	 *            input variable declarations
	 */
	private Binding getBinding3(EClass clazz, List<Binding> bindings, List<? extends VariableDeclaration> ivariables) {
		for (EStructuralFeature feature : clazz.getEAllStructuralFeatures()) {
			if (!hasPrimitiveType(feature)
					&& !bindings.stream().anyMatch(b -> b.getPropertyName().equals(feature.getName()))) {
				String propertyName = feature.getName();
				Binding binding = ATLFactory.eINSTANCE.createBinding();

				binding.setPropertyName(propertyName);
				binding.setValue(getCompatibleValue(feature, ivariables));
				return binding;
			}
		}
		return null;
	}

	/**
	 * It returns a non-duplicate binding with an incorrect value.
	 * 
	 * @param clazz
	 *            out class for binding
	 * @param bindings
	 *            list of bindings defined for the our class
	 * @param ivariables
	 *            input variable declarations
	 */
	private Binding getBinding4(EClass clazz, List<Binding> bindings, List<? extends VariableDeclaration> ivariables) {
		for (EStructuralFeature feature : clazz.getEAllStructuralFeatures()) {
			if (!bindings.stream().anyMatch(b -> b.getPropertyName().equals(feature.getName()))) {
				String propertyName = feature.getName();
				Binding binding = ATLFactory.eINSTANCE.createBinding();

				binding.setPropertyName(propertyName);
				binding.setValue(getIncompatibleValue(feature, ivariables));
				return binding;
			}
		}
		return null;
	}

	/**
	 * It returns a binding for a property defined in a subclass and correct value.
	 * 
	 * @param clazz
	 *            out class for binding
	 * @param metamodel
	 *            output metamodel
	 * @param solution
	 * @param ivariables
	 *            input variable declarations
	 * @param location
	 */
	private Binding getBinding6(EClass clazz, MetaModel metamodel, List<Binding> bindings, Solution solution,
			List<? extends VariableDeclaration> ivariables, int row) {
		// System.out.println("startcreate");

		Binding binding = null;
		boolean check = false;
		ArrayList<EStructuralFeature> featurelist = new ArrayList<EStructuralFeature>();
		ArrayList<String> classifierlist = new ArrayList<String>();
		for (EClassifier classifier : metamodel.getEClassifiers()) {
			if (classifier instanceof EClass) {
				EClass child = ((EClass) classifier); // child.equals(clazz)
				if (!child.getEStructuralFeatures().isEmpty()) {

					// System.out.println("binding52");
					// NSGAII.writer.println(child);
					// System.out.println(solution.getCoSolution().getOp().listpropertyname.get(row));
					// System.out.println(solution.getCoSolution().getOp().listpropertynamelocation.get(row));
					// NSGAII.writer.println(solution.getCoSolution().getOp().listpropertyname.get(row));

					for (int i = 0; i < child.getEStructuralFeatures().size(); i++) {
						// if (!solution.getCoSolution().getOp().listpropertyname.get(row)
						// .contains(child.getEStructuralFeatures().get(i).getName())) {
						// solution.classifiersolution.set(MyProblem.indexoperation-1,classifier );
						featurelist.add(child.getEStructuralFeatures().get(i));
						classifierlist.add(classifier.getName());
					}
				}
			}
		}
		boolean checkfirstside = false;
		int number = -1;
		EStructuralFeature feature = null;

		while (checkfirstside == false) {
			number = new Random().nextInt(featurelist.size());

			feature = featurelist.get(number);
			if (!solution.getCoSolution().getOp().listpropertyname.get(row).contains(feature.getName())
					&& !feature.getName().equals("orderedMessages"))
				checkfirstside = true;

		}
		solution.modifypropertyname.set(MyProblem.indexoperation - 1, classifierlist.get(number));

		binding = ATLFactory.eINSTANCE.createBinding();
		binding.setPropertyName(feature.getName());
		solution.newstring.set(MyProblem.indexoperation - 1, feature.getName());
		solution.inorout.set(MyProblem.indexoperation - 1, "out");
		solution.getCoSolution().getOp().listpropertyname.get(row).add(feature.getName());
		solution.getCoSolution().getOp().listpropertynamelocation.get(row)
				.add(solution.getCoSolution().getOp().listpropertynamelocation.get(row)
						.get(solution.getCoSolution().getOp().listpropertynamelocation.get(row).size() - 1) + 1);
		// System.out.println("thisgetname2");
		// System.out.println(feature.getName());
		check = true;
		OclExpression exp = getCompatibleValue(feature, ivariables);
		// solution.expression.set(MyProblem.indexoperation-1, exp);
		binding.setValue(exp);
		// return binding;
		// }
		return binding;
	}

	private Binding getBinding5(EClass clazz, MetaModel metamodel, List<? extends VariableDeclaration> ivariables) {
		// System.out.println(metamodel.getEClassifiers());
		for (EClassifier classifier : metamodel.getEClassifiers()) {
			if (classifier instanceof EClass) {
				EClass child = ((EClass) classifier);
				// System.out.println("binding51");
				// System.out.println(clazz.getName());
				// System.out.println(child.getEAllSuperTypes());
				// System.out.println(child.getEStructuralFeatures().isEmpty());
				if (child.getEAllSuperTypes().contains(clazz) && !child.getEStructuralFeatures().isEmpty()) {
					int number = new Random().nextInt(child.getEStructuralFeatures().size());
					// System.out.println("binding52");
					// for (int i = 0; i < child.getEStructuralFeatures().size(); i++)
					// System.out.println(child.getEStructuralFeatures().get(i).getName());
					EStructuralFeature feature = child.getEStructuralFeatures().get(number);
					Binding binding = ATLFactory.eINSTANCE.createBinding();
					binding.setPropertyName(feature.getName());
					// System.out.println("thisgetname");

					// System.out.println(feature.getName());
					binding.setValue(getCompatibleValue(feature, ivariables));
					return binding;
				}
			}
		}
		return null;
	}

	/**
	 * It returns a compatible ocl expression for the received feature.
	 * 
	 * @param feature
	 * @param variables
	 *            (used when the feature has a non-primitive type)
	 */
	private OclExpression getCompatibleValue(EStructuralFeature feature,
			List<? extends VariableDeclaration> variables) {
		// System.out.println("dakhel");
		// System.out.println(feature.getName());
		// System.out.println(feature.getUpperBound());

		/// return getCompatibleValue(feature.getEType().getName(),
		/// feature.getUpperBound()==1, feature.isOrdered(), variables);
		// return getCompatibleValue(feature.getEType().getName(),
		/// feature.getUpperBound() == 1, true, variables);
		return getCompatibleValue(feature.getEType().getName(), true, true, variables, feature.getUpperBound());
	}

	/**
	 * It returns a compatible ocl expression for the received type.
	 * 
	 * @param type
	 * @param monovalued
	 * @param ordered
	 *            (used in case of collections, i.e., when monovalued==true)
	 * @param variables
	 *            (used when the type is not primitive)
	 * @param bound
	 */
	private OclExpression getCompatibleValue(String type, boolean monovalued, boolean ordered,
			List<? extends VariableDeclaration> variables, int bound) {
		OclExpression expression = null;
		// System.out.println(type);

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
				// expression = OCLFactory.eINSTANCE.createSequenceExp();
				expression = OCLFactory.eINSTANCE.createVariableExp();
				List<EStructuralFeature> maintarget = new ArrayList<EStructuralFeature>();
				Setting s = new Setting();
				MetaModel metatarget = null;

				try {
					metatarget = new MetaModel(s.gettargetmetamodel());
				} catch (transException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				for (EClassifier classifier : metatarget.getEClassifiers()) {
					if (classifier instanceof EClass) {
						for (int y = 0; y < classifier.eContents().size(); y++) {
							if (classifier.eContents().get(y) instanceof EReference) {
								maintarget.add((EStructuralFeature) classifier.eContents().get(y));
							}
						}
					}
				}

				boolean check = false;
				int randomInt2 = 0;
				while (check == false) {
					Random number_generator = new Random();
					randomInt2 = number_generator.nextInt(maintarget.size());
					if (maintarget.get(randomInt2).getUpperBound() == bound)
						if ((maintarget.get(randomInt2).getUpperBound() == 1)) {
							if ((maintarget.get(randomInt2).getLowerBound() != 0))
								check = true;
						} else
							check = true;
				}
				StoreThirdIndex(randomInt2);
				NavigationOrAttributeCallExp nc = OCLFactory.eINSTANCE.createNavigationOrAttributeCallExp();
				nc.setIsStaticCall(false);
				nc.setImplicitlyCasted(false);
				nc.setName(maintarget.get(randomInt2).getName());
				VariableDeclaration sel = OCLFactory.eINSTANCE.createVariableDeclaration();
				String str = variables.get(0).getVarName();

				sel.setVarName(str);
				VariableExp vExp = OCLFactory.eINSTANCE.createVariableExp();
				vExp.setReferredVariable(variables.get(0));
				nc.setSource(vExp);
				nc.setInitializedVariable(sel);

				(expression) = (sel.getInitExpression());

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

	/**
	 * It returns a compatible ocl expression for the received feature.
	 * 
	 * @param feature
	 * @return
	 */
	private OclExpression getIncompatibleValue(EStructuralFeature feature,
			List<? extends VariableDeclaration> variables) {
		List<String> types = new ArrayList<String>();
		if (!EMFUtils.isBoolean(feature.getEType().getName())) {
			types.add("Boolean");
		}
		if (!EMFUtils.isFloating(feature.getEType().getName())) {
			types.add("Double");
		}
		if (!EMFUtils.isInteger(feature.getEType().getName())) {
			types.add("Integer");
		}
		if (!EMFUtils.isString(feature.getEType().getName())) {
			types.add("String");
		}
		if (types.size() < 4) {
			types.add("Other");
		}
		return getCompatibleValue(types.get(new Random().nextInt(types.size())), feature.getUpperBound() != 1,
				!feature.isOrdered(), variables, feature.getUpperBound());
	}

	/**
	 * It returns whether the received feature has a primitive type.
	 */
	private boolean hasPrimitiveType(EStructuralFeature feature) {
		return EMFUtils.isBoolean(feature.getEType().getName()) || EMFUtils.isFloating(feature.getEType().getName())
				|| EMFUtils.isInteger(feature.getEType().getName()) || EMFUtils.isString(feature.getEType().getName());
	}
}
