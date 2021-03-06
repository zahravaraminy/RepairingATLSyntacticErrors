package modification.type.operator.refinementphase;

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
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.core.ModelFactory;
import org.eclipse.m2m.atl.core.emf.EMFModel;
import org.eclipse.m2m.atl.core.emf.EMFModelFactory;
import org.eclipse.m2m.atl.core.emf.EMFReferenceModel;
import org.eclipse.m2m.atl.engine.parser.AtlParser;
import witness.generator.MetaModel;
import anatlyzer.atlext.ATL.ForEachOutPatternElement;
import anatlyzer.atlext.ATL.Helper;
import anatlyzer.atlext.ATL.InPattern;
import anatlyzer.atlext.ATL.InPatternElement;
import anatlyzer.atlext.ATL.LocatedElement;
import anatlyzer.atlext.ATL.Module;
import anatlyzer.atlext.ATL.OutPatternElement;
import anatlyzer.atlext.OCL.Attribute;
import anatlyzer.atlext.OCL.CollectionType;
import anatlyzer.atlext.OCL.LoopExp;
import anatlyzer.atlext.OCL.NavigationOrAttributeCallExp;
import anatlyzer.atlext.OCL.OclExpression;
import anatlyzer.atlext.OCL.OclModelElement;
import anatlyzer.atlext.OCL.Operation;
import anatlyzer.atlext.OCL.PropertyCallExp;
import anatlyzer.atlext.OCL.VariableDeclaration;
import anatlyzer.atlext.OCL.VariableExp;
//import anatlyzer.evaluation.mutators.ATLModel;
import anatlyzer.atl.model.ATLModel;
import modification.operator.refinementphase.AbstractMutator;
import ca.udem.fixingatlerror.refinementphase.main;
import ca.udem.fixingatlerror.refinementphase.Operations;
import ca.udem.fixingatlerror.refinementphase.Operationspostprocessing;
import ca.udem.fixingatlerror.refinementphase.Settingpostprocessing;
import jmetal.core.Solution;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.metaheuristics.nsgaII.NSGAIIpostprocessing;
import jmetal.problems.MyProblem;
import jmetal.problems.MyProblempostprocessing;
import transML.exceptions.transException;

public abstract class AbstractTypeModificationMutatorpost extends AbstractMutator {

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

	private int numberoutpattern = 0;
	private boolean repeatargument = false;
	private String m = "C:/Users/varaminz/Downloads/Project/workspaceTEST34/anatlyzer.atl.tests.api/trafo2/zahra2mutants/finalresult"
			+ main.totalnumber + ".atl";
	private List<Integer> alloclrule = new ArrayList<Integer>();
	private List<Integer> checkedruleinpattern = new ArrayList<Integer>();
	List<String> listattrforcheck = new ArrayList<String>();
	// private List<Integer> indexallerrorocl = new ArrayList<Integer>();
	private ArrayList<String> listattrnavigation = new ArrayList<String>();
	private ArrayList<String> listreplacement = new ArrayList<String>();
	private List<String> checklistattr = new ArrayList<String>();
	private List<String> listoldclass = new ArrayList<String>();
	private List<String> listsubclasslefthandside = new ArrayList<String>();
	private List<Integer> listsubclassrule = new ArrayList<Integer>();
	private List<Integer> checknotrepeat = new ArrayList<Integer>();
	private List<Integer> listrandomInt = new ArrayList<Integer>();
	private int startcombinedattr = 0;
	private int indexrule = -1;
	private EMFModel atlModel3;
	private int start = 0;
	private String m2 = "C:/Users/varaminz/Downloads/Project/workspaceTEST34/anatlyzer.atl.tests.api/trafo/PNML2PetriNet2.atl";
	private EMFModel atlModel4;
	private ArrayList<Integer> oclindex;
	private int indexcorrectocl = -1;
	private int numrun = 0;
	private boolean findcorrectclass = false;

	protected <ToModify extends LocatedElement> List<Object> genericTypeModification(EMFModel atlModel,
			String outputFolder, Class<ToModify> ToModifyClass, String featureName, MetaModel[] metamodels,
			ATLModel wrapper, Solution solution, MetaModel metamodels1, MetaModel metamodels2) {
		return this.genericTypeModification(atlModel, outputFolder, ToModifyClass, featureName, metamodels, false,
				wrapper, solution, metamodels1, metamodels2);

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

	protected <ToModify extends LocatedElement> List<Object> genericTypeModification(EMFModel atlModel,
			String outputFolder, Class<ToModify> ToModifyClass, String featureName, MetaModel[] metamodels,
			boolean exactToModifyType, ATLModel wrapper, Solution solution, MetaModel metamodels1,
			MetaModel metamodels2) {
		List<Object> ReturnResult = new ArrayList<Object>();
		List<ToModify> modifiable = (List<ToModify>) wrapper.allObjectsOf(ToModifyClass);

		ReturnResult.add(wrapper);
		ReturnResult.add(atlModel);
		// NSGAII.writer.println("collectionlocation");

		if (modifiable.size() > 0) {

			if (exactToModifyType)
				filterSubtypes(modifiable, ToModifyClass);

			// we will add a comment to the module, documenting the mutation
			// Module module = AbstractDeletionMutator.getWrapper().getModule();
			Module module = wrapper.getModule();

			EDataTypeEList<String> comments = null;
			if (module != null) {
				EStructuralFeature f = wrapper.source(module).eClass().getEStructuralFeature("commentsAfter");
				comments = (EDataTypeEList<String>) wrapper.source(module).eGet(f);
			}
			if (Operationspostprocessing.statecase.equals("case7")
					&& NSGAIIpostprocessing.numberoperationargument == 1) {
				comments.clear();
				// NSGAIIpostprocessing.numberoperationargument=-1;
			}
			int randomInt = -2;
			boolean checkmutationapply = false;
			String comment = null;
			int count = -1;
			ArrayList<Integer> filterrule = new ArrayList<Integer>();
			boolean numberrun = true;
			int numberoclkindof = checknumberoclkindofinrules();
			if (numberoclkindof == 0 && Operationspostprocessing.statecase.equals("case7"))
				checkmutationapply = true;
			System.out.println("mmmm");
			System.out.println(NSGAIIpostprocessing.errorrule);
			System.out.println(numberoclkindof);
			System.out.println(NSGAIIpostprocessing.ocliskineoflocation);
			while (checkmutationapply == false) {

				// if (numberrun==true) {
				// System.out.println(modifiable.get(randomInt).getLocation());
				// ye line ocl ra bar migardoone
				List<Integer> Result = ReturnFirstIndex(randomInt, modifiable.size(), checkmutationapply, count,
						solution);
				randomInt = Result.get(0);

				String[] array = modifiable.get(randomInt).getLocation().split(":", 2);
				// array oclkindlineattr faghat hamon khati ke ocl hast attri dare
				int indexoclkindlineattr = NSGAIIpostprocessing.oclkindlineattr.indexOf(array[0]);
				// khati ke entekhab kard dar ocliskineoflocation peyda mikone [30, 31, 52, 73,
				// 102, 104, 123, 125]
				int indexocliskineoflocation = NSGAIIpostprocessing.ocliskineoflocation
						.indexOf(Integer.parseInt(array[0]));
				// toye oclkindlineattr koja hamin ocl tamom mishe
				System.out.println("indexoclkindlineattr");
				System.out.println(Integer.parseInt(array[0]));
				System.out.println(indexoclkindlineattr);
				System.out.println(NSGAIIpostprocessing.ocliskineoflocation);
				System.out.println(indexocliskineoflocation);
				System.out.println(NSGAIIpostprocessing.errorrule);
				int numboclthisline = 0;
				// int numberoclkindof = 0;
				if (indexoclkindlineattr >= 0) {
					int indexoclkindlineattrsecond = -1;
					if ((indexocliskineoflocation + 1) == NSGAIIpostprocessing.ocliskineoflocation.size())
						indexoclkindlineattrsecond = NSGAIIpostprocessing.oclkindlineattr.size();
					if (indexocliskineoflocation >= 0
							&& (indexocliskineoflocation + 1) != NSGAIIpostprocessing.ocliskineoflocation.size())
						indexoclkindlineattrsecond = NSGAIIpostprocessing.oclkindlineattr.indexOf(Integer
								.toString(NSGAIIpostprocessing.ocliskineoflocation.get(indexocliskineoflocation + 1)));

					for (int i = indexoclkindlineattr + 2; i < indexoclkindlineattrsecond; i++) {
						if (NSGAIIpostprocessing.oclkindlineattr.get(i).toString().contains("oclIsKindOf")
								|| NSGAIIpostprocessing.oclkindlineattr.get(i).toString().contains("oclAsType")) {
							numboclthisline = numboclthisline + 1;
						}
					}
				}
				int indexrule = -1;
				// ocliskind of to kodom rule
				for (int j = 0; j < NSGAIIpostprocessing.faultrule.size(); j++) {
					if (Integer.parseInt(array[0]) > NSGAIIpostprocessing.faultrule.get(j)
							&& Integer.parseInt(array[0]) < NSGAIIpostprocessing.finalrule.get(j))

						indexrule = j;

				}

				numberrun = false;
				// NSGAIIpostprocessing.errorrule.contains(indexrule) && indexoclkindlineattr>=0
				if (NSGAIIpostprocessing.errorrule.contains(indexrule) && indexoclkindlineattr >= 0
						&& Operationspostprocessing.statecase.equals("case7")) {
					// tedade kole ocliskindeof ke bayad to rulha ke error dare peida kone
					// numberoclkindof=checknumberoclkindofinrules();
					// numberoclkindof
					if (this.listoldclass.size() == numberoclkindof || this.listoldclass.size() > numberoclkindof)
						checkmutationapply = true;

				}
				if (numberoclkindof == 0)
					checkmutationapply = true;
				boolean IsThereCollection = false;

				IsThereCollection = ReturnIsThereCollection(NSGAIIpostprocessing.sequencelocation,
						NSGAIIpostprocessing.ocliskineoflocation);
				boolean check = true;
				// if( NSGAII.locationfilter.contains(Integer.parseInt(array[0])) &&
				// Class2Rel.typeoperation.equals("argu"))
				// check=false;

				boolean iteration = false;
				System.out.println("yyyy");
				System.out.println(this.alloclrule);
				System.out.println(this.listoldclass);
				System.out.println(indexrule);
				System.out.println(numberoclkindof);
				System.out.println(this.listoldclass.size());
				System.out.println(Integer.parseInt(array[0]));
				System.out.println(NSGAIIpostprocessing.errorrule);
				System.out.println(randomInt);
				System.out.println(indexrule);
				this.listreplacement.clear();
				this.listreplacement = new ArrayList<String>();
				// NSGAIIpostprocessing.errorrule.contains(indexrule)
				// toye rule ke error dare bashe va hameye ocl ha dar rul error dar ra check kon
				// && !alloclrule.contains(Integer.parseInt(array[0]))
				// && !this.checknotrepeat.contains( randomInt)
				// && NSGAIIpostprocessing.errorrule.contains(indexrule)
				if (modifiable.size() > 0

						&& !this.listrandomInt.contains(randomInt) && check == true
						&& NSGAIIpostprocessing.errorrule.contains(indexrule)) {

					// alloclrule.add(Integer.parseInt(array[0]));
					EStructuralFeature featureDefinition = wrapper.source(modifiable.get(randomInt)).eClass()
							.getEStructuralFeature(featureName);

					// khat ghable ->exists dare
					if (NSGAIIpostprocessing.iterationcall.contains(Integer.parseInt(array[0]) - 1)) {

						iteration = true;

					} else {
						iteration = false;

					}

					// case 6,7
					if (featureDefinition != null && featureDefinition.getUpperBound() == 1) {

						ReturnResult = Postprocessingforinpatternmodification(wrapper, randomInt, solution, modifiable,
								featureName, metamodels1, metamodels2, atlModel, metamodels, comments, ReturnResult,
								indexrule);

						System.out.println("finalinpa");
						EStructuralFeature feature = wrapper.source(module).eClass()
								.getEStructuralFeature("commentsAfter");
						System.out.println(((EDataTypeEList<String>) wrapper.source(module).eGet(feature)).toString());

						checkmutationapply = true;

					}

					else if (featureDefinition != null) {
						// Operation Argument Type Modification for OCLISKindof

						// Case1 run mishe
						boolean listlineofcode = false;
						listlineofcode = true;
						if (listlineofcode == true && iteration == false) {

							List<EObject> value = (List<EObject>) wrapper.source(modifiable.get(randomInt))
									.eGet(featureDefinition);

							ReturnResult = Postprocessingforoclwithoutiteration(wrapper, value, randomInt,
									featureDefinition, checkmutationapply, atlModel, metamodels, count, solution, array,
									comments, comment, indexrule, modifiable, ReturnResult);

							// System.out.println(this.findcorrectclass);
							System.out.println("kkkkkk");
							System.out.println(this.listoldclass);
							// checkmutationapply=this.findcorrectclass;
						}
						// NSGAII.lineoclerror.contains( Integer.parseInt(array[0]))
						if (listlineofcode == true && iteration == true) {
							List<EObject> value = (List<EObject>) wrapper.source(modifiable.get(randomInt))
									.eGet(featureDefinition);

							ReturnResult = Postprocessingforoclwithiteration(wrapper, value, randomInt,
									featureDefinition, checkmutationapply, atlModel, metamodels, count, solution, array,
									comments, comment, indexrule, modifiable, ReturnResult, indexrule);

							// NSGAII.writer.println("value");
							// NSGAII.writer.println(value);

						}

						System.out.println("checknotrepeat");
						System.out.println(this.checknotrepeat);

					}
				}

			}
			NSGAIIpostprocessing.numberoperationargument = -1;

		}
		return ReturnResult;
		// return null;

	}

	private <ToModify extends LocatedElement> List<Object> Postprocessingforinpatternmodification(ATLModel wrapper,
			int randomInt, Solution solution, List<ToModify> modifiable, String featureName, MetaModel metamodels1,
			MetaModel metamodels2, EMFModel atlModel, MetaModel[] metamodels, EDataTypeEList<String> comments,
			List<Object> ReturnResult, int index) {
		// TODO Auto-generated method stub

		List<InPattern> modifiable2 = (List<InPattern>) wrapper.allObjectsOf(InPattern.class);
		// NSGAII.writer.println(Operations.statecase);
		EStructuralFeature feature = wrapper.source(modifiable2.get(randomInt)).eClass()
				.getEStructuralFeature("filter");
		System.out.println("beforefeature");
		System.out.println(randomInt);
		System.out.println(feature);
		if (feature != null) {
			System.out.println("afterfeature");
			System.out.println(feature.getUpperBound());
			System.out.println(feature.getLowerBound());
			if (feature.getUpperBound() == 1 && feature.getLowerBound() == 0) {
				EObject link = (EObject) wrapper.source(modifiable2.get(randomInt)).eGet(feature);

				if (link != null) {
					LocatedElement object = (LocatedElement) wrapper.target(link);

					System.out.println("fffffffffffffff");
					System.out.println(toString(link));
					System.out.println(object.toString());
					System.out.println(feature.getName());
					ReturnResult = Analysiseachbinding(solution, modifiable, wrapper, featureName, metamodels1,
							metamodels2, atlModel, metamodels, comments, ReturnResult, index);
				} else {

					ReturnResult = Analysiseachbinding(solution, modifiable, wrapper, featureName, metamodels1,
							metamodels2, atlModel, metamodels, comments, ReturnResult, index);

				}
			}

		} else {

		}

		return ReturnResult;
	}

	private <ToModify extends LocatedElement> List<Object> Analysiseachbinding(Solution solution,
			List<ToModify> modifiable, ATLModel wrapper, String featureName, MetaModel metamodels1,
			MetaModel metamodels2, EMFModel atlModel, MetaModel[] metamodels, EDataTypeEList<String> comments,
			List<Object> ReturnResult, int id) {
		// TODO Auto-generated method stub

		Settingpostprocessing sp = new Settingpostprocessing();
		for (int h = 0; h < solution.getCoSolutionpostprocessing().getOp().listpropertyname.size(); h++) {

			String specificoutput = ReturnSpecificOutput(wrapper, featureName, h);
			String specificinput = ReturnSpecificInput(wrapper, featureName, h, modifiable);

			System.out.println("out");
			for (int h2 = 0; h2 < solution.getCoSolutionpostprocessing().getOp().listpropertyname.get(h).size(); h2++) {

				String typeleftside = ReturnTypeLeftHandSideBinding(specificoutput,
						solution.getCoSolutionpostprocessing().getOp().listpropertyname.get(h).get(h2));
				if (typeleftside != null) {
					if (!typeleftside.equals("StringType") && !typeleftside.equals("EString")
							&& !typeleftside.equals("String")) {

						String helpertype = IfRightHandSideISHelperReturnHelperType(wrapper, h, h2);
						String usingcondition = IfRightHandSideIsUsingCondition(wrapper, h, h2);
						System.out.println("outleft");

						System.out.println(helpertype);
						System.out.println(usingcondition);
						if (((helpertype != null && !helpertype.equals("StringType")) || usingcondition != null)) {
							EObject objout = FindOutpatternIsequaltoLeftHandSideType(wrapper, typeleftside,
									featureName);
							String outpatternfound = null;
							if (objout != null)
								outpatternfound = toString(objout);
							System.out.println("this.numberoutpattern");
							System.out.println(this.numberoutpattern);
							// Boolean subclassexist=IFOutPatternHasSubClass(helpertype);
							// && NSGAIIpostprocessing.errorrule.contains(this.indexrule)
							if (this.numberoutpattern == 0) {
								Boolean subclassexist1 = false;
								Boolean subclassexist2 = false;
								if (usingcondition != null)
									subclassexist2 = IFOutPatternHasSubClass(usingcondition, 0);
								subclassexist1 = IFOutPatternHasSubClass(typeleftside, 1);
								if (subclassexist1 == true && subclassexist1 == false) {
									boolean lefthassubclass = FindOutPatternIsequalLeftHandSidewithsubclassess(wrapper,
											featureName);
									if (lefthassubclass == true
											&& !this.checkedruleinpattern.contains(this.indexrule)) {
										for (int h1 = 0; h1 < this.listsubclassrule.size(); h1++) {

											EStructuralFeature featureDefinition = wrapper
													.source(modifiable.get(this.listsubclassrule.get(h1))).eClass()
													.getEStructuralFeature(featureName);
											EObject object2modify_src = wrapper
													.source(modifiable.get(this.listsubclassrule.get(h1)));
											EObject oldFeatureValue = (EObject) object2modify_src
													.eGet(featureDefinition);
											if (usingcondition != null)
												ReturnResult = ModifyInpatternElement(atlModel,
														(EObject) oldFeatureValue, metamodels, usingcondition,
														featureDefinition, object2modify_src, modifiable, comments,
														wrapper, ReturnResult, id, outpatternfound, featureName,
														specificoutput);

											else
												ReturnResult = ModifyInpatternElement(atlModel,
														(EObject) oldFeatureValue, metamodels, helpertype,
														featureDefinition, object2modify_src, modifiable, comments,
														wrapper, ReturnResult, id, outpatternfound, featureName,
														specificoutput);

										}

									}

								}

							}
							if (this.numberoutpattern > 1) {
								Boolean subclassexist = false;
								if (helpertype != null)

									subclassexist = IFOutPatternHasSubClass(helpertype, 0);
								if (subclassexist == false) {
									FindOutPatternIsequalLeftHandSideandHasNoFilter(wrapper, featureName, typeleftside);
									EStructuralFeature featureDefinition = wrapper
											.source(modifiable.get(this.indexrule)).eClass()
											.getEStructuralFeature(featureName);
									EObject object2modify_src = wrapper.source(modifiable.get(this.indexrule));
									EObject oldFeatureValue = (EObject) object2modify_src.eGet(featureDefinition);

									ReturnResult = ModifyInpatternElement(atlModel, (EObject) oldFeatureValue,
											metamodels, helpertype, featureDefinition, object2modify_src, modifiable,
											comments, wrapper, ReturnResult, id, outpatternfound, featureName,
											specificoutput);

								}

							}
							if (this.numberoutpattern == 1 && this.indexrule > 0) {

								EStructuralFeature featureDefinition = wrapper.source(modifiable.get(this.indexrule))
										.eClass().getEStructuralFeature(featureName);
								EObject object2modify_src = wrapper.source(modifiable.get(this.indexrule));
								EObject oldFeatureValue = (EObject) object2modify_src.eGet(featureDefinition);
								Boolean subclassexist;
								if (helpertype != null) {
									subclassexist = IFOutPatternHasSubClass(helpertype, 0);
									if (subclassexist == false)
										ReturnResult = ModifyInpatternElement(atlModel, (EObject) oldFeatureValue,
												metamodels, helpertype, featureDefinition, object2modify_src,
												modifiable, comments, wrapper, ReturnResult, id, outpatternfound,
												featureName, specificoutput);
								} else {
									subclassexist = IFOutPatternHasSubClass(usingcondition, 0);
									if (subclassexist == false)
										ReturnResult = ModifyInpatternElement(atlModel, (EObject) oldFeatureValue,
												metamodels, usingcondition, featureDefinition, object2modify_src,
												modifiable, comments, wrapper, ReturnResult, id, outpatternfound,
												featureName, specificoutput);
								}
								this.numberoutpattern = 0;
								System.out.println("found");
								System.out.println(subclassexist);
								System.out.println(this.indexrule);
								System.out.println(typeleftside);
								System.out.println(helpertype);
								System.out.println(outpatternfound);
								System.out.println(specificinput);

							}

						}
						if (helpertype == null && usingcondition == null) {

							// String typenavigation=null;
							System.out.println("typeleftside");
							System.out.println(typeleftside);
							String typenavigation = ExtractTypeRightHandSideIfThereisnotHelper(h, h2, specificinput);
							EObject objout = FindOutpatternIsequaltoLeftHandSideType(wrapper, typeleftside,
									featureName);
							this.checkedruleinpattern.add(this.indexrule);
							String outpatternfound = null;
							if (objout != null)
								outpatternfound = toString(objout);
							Boolean subclassexist = IFOutPatternHasSubClass(typenavigation, 0);
							if (this.numberoutpattern == 1 && subclassexist == false
									&& !this.checkedruleinpattern.contains(this.indexrule)) {
								EStructuralFeature featureDefinition = wrapper.source(modifiable.get(this.indexrule))
										.eClass().getEStructuralFeature(featureName);
								EObject object2modify_src = wrapper.source(modifiable.get(this.indexrule));
								EObject oldFeatureValue = (EObject) object2modify_src.eGet(featureDefinition);
								ReturnResult = ModifyInpatternElement(atlModel, (EObject) oldFeatureValue, metamodels,
										typenavigation, featureDefinition, object2modify_src, modifiable, comments,
										wrapper, ReturnResult, id, outpatternfound, featureName, specificoutput);
							}

							System.out.println("found2");
							System.out.println(subclassexist);
							System.out.println(outpatternfound);
							System.out.println(this.numberoutpattern);
							this.numberoutpattern = 0;

						}

					}
				}

			}
			// }
		}

		System.out.println("finished");

		return ReturnResult;
	}

	private void FindOutPatternIsequalLeftHandSideandHasNoFilter(ATLModel wrapper, String featureName,
			String typeleftside) {
		// TODO Auto-generated method stub

		List<OutPatternElement> outpatternlist = (List<OutPatternElement>) wrapper
				.allObjectsOf(OutPatternElement.class);

		int id = -1;
		for (int i = 0; i < outpatternlist.size(); i++) {

			EStructuralFeature featureDefinition = wrapper.source(outpatternlist.get(i)).eClass()
					.getEStructuralFeature(featureName);
			String[] array = outpatternlist.get(i).getLocation().split(":", 2);
			id = FindRuleforThisLine(array);
			if (featureDefinition != null && featureDefinition.getUpperBound() == 1) {

				EObject object2modify_src = wrapper.source(outpatternlist.get(i));
				EObject oldFeatureValue = (EObject) object2modify_src.eGet(featureDefinition);

				if (typeleftside.equals(toString(oldFeatureValue))) {
					List<InPattern> containers = (List<InPattern>) wrapper.allObjectsOf(InPattern.class);
					EStructuralFeature feature = wrapper.source(containers.get(id)).eClass()
							.getEStructuralFeature("filter");

					if (feature != null) {
						System.out.println("hasnotfilter");
						if (feature.getUpperBound() == 1 && feature.getLowerBound() == 0) {
							EObject link = (EObject) wrapper.source(containers.get(id)).eGet(feature);
							if (link == null) {
								System.out.println(Integer.parseInt(array[0]));
								this.indexrule = id;
							}

						}
					}

				}
			}
		}

	}

	private boolean FindOutPatternIsequalLeftHandSidewithsubclassess(ATLModel wrapper, String featureName) {
		// TODO Auto-generated method stub

		List<OutPatternElement> outpatternlist = (List<OutPatternElement>) wrapper
				.allObjectsOf(OutPatternElement.class);
		this.listsubclassrule.clear();
		this.listsubclassrule = new ArrayList<Integer>();
		int id = -1;
		for (int j = 0; j < this.listsubclasslefthandside.size(); j++) {
			for (int i = 0; i < outpatternlist.size(); i++) {

				EStructuralFeature featureDefinition = wrapper.source(outpatternlist.get(i)).eClass()
						.getEStructuralFeature(featureName);
				String[] array = outpatternlist.get(i).getLocation().split(":", 2);
				id = FindRuleforThisLine(array);
				if (featureDefinition != null && featureDefinition.getUpperBound() == 1
						&& !this.checkedruleinpattern.contains(id)) {

					EObject object2modify_src = wrapper.source(outpatternlist.get(i));
					EObject oldFeatureValue = (EObject) object2modify_src.eGet(featureDefinition);

					if (this.listsubclasslefthandside.get(j).equals(toString(oldFeatureValue))) {

						this.listsubclassrule.add(id);
						this.checkedruleinpattern.add(id);

					}
				}
			}

		}
		if (this.listsubclasslefthandside.size() == this.listsubclassrule.size())
			return true;
		else
			return false;
	}

	private Integer FindRuleforThisLine(String[] array) {
		int idrule = -1;
		for (int j = 0; j < NSGAIIpostprocessing.faultrule.size(); j++) {
			if (Integer.parseInt(array[0]) > NSGAIIpostprocessing.faultrule.get(j)
					&& Integer.parseInt(array[0]) < NSGAIIpostprocessing.finalrule.get(j))

				idrule = j;

		}
		return idrule;
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

	private String ExtractTypeRightHandSideIfThereisnotHelper(int h, int h2, String specificinput) {
		// TODO Auto-generated method stub
		String typenavigation = null;
		int index = -1;
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
			if (NSGAIIpostprocessing.allattributrightside.get(h).get(h2).get(index).chars().count() == 1) {
				typenavigation = specificinput;
				indexclassname = NSGAIIpostprocessing.classnamestring.indexOf(typenavigation);

			} else {
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
			System.out.println("initialtype");
			System.out.println(typenavigation);

			for (int j = 1; j < NSGAIIpostprocessing.allattributrightside.get(h).get(h2).size(); j++) {

				// String typenavigation=null;
				for (int i = NSGAIIpostprocessing.classnamestartpoint
						.get(indexclassname); i < NSGAIIpostprocessing.classnamestartpoint.get(indexclassname)
								+ NSGAIIpostprocessing.classnamelength.get(indexclassname); i++) {

					// hameye attributha ke in class dare check kon
					if (NSGAIIpostprocessing.listinheritattributesourcemetamodel.get(i).getName()
							.equals(NSGAIIpostprocessing.allattributrightside.get(h).get(h2).get(j))) {

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

	private <ToModify extends LocatedElement> List<Object> ModifyInpatternElement(EMFModel atlModel,
			EObject oldFeatureValue, MetaModel[] metamodels, String helpertype, EStructuralFeature featureDefinition,
			EObject object2modify_src, List<ToModify> modifiable, EDataTypeEList<String> comments, ATLModel wrapper,
			List<Object> ReturnResult, int id, String outpatternfound, String featureName, String specificoutput) {
		// TODO Auto-generated method stub
		List<EObject> replacements = this.replacements(atlModel, (EObject) oldFeatureValue, metamodels);

		int randomInt2 = -1;
		for (int i = 0; i < replacements.size(); i++) {
			// System.out.println(toString(replacements.get(i)));
			if (toString(replacements.get(i)).equals(helpertype)) {
				randomInt2 = i;
			}

		}

		boolean checkinputoutput = true;
		for (int i = 0; i < modifiable.size(); i++) {

			EStructuralFeature featureDefinition2 = wrapper.source(modifiable.get(i)).eClass()
					.getEStructuralFeature(featureName);
			EObject object2modify_src2 = wrapper.source(modifiable.get(i));
			EObject oldFeatureValue2 = (EObject) object2modify_src2.eGet(featureDefinition2);

			if (toString(oldFeatureValue2).equals(toString(replacements.get(randomInt2)))) {

				List<OutPatternElement> variables = (List<OutPatternElement>) wrapper
						.allObjectsOf(OutPatternElement.class);

				for (int j = 0; j < variables.size(); j++) {

					EStructuralFeature featureDefinitionout = wrapper.source(variables.get(j)).eClass()
							.getEStructuralFeature(featureName);
					EObject object2modify_src3 = wrapper.source(variables.get(j));
					EObject oldFeatureValue3 = (EObject) object2modify_src3.eGet(featureDefinitionout);
					if (toString(oldFeatureValue3).equals(specificoutput)
							&& !NSGAIIpostprocessing.rulefilter.contains(i)) {
						checkinputoutput = false;
						System.out.println("rrrrrrrr");
						System.out.println(i);
					}
				}

			}

		}

		System.out.println("outpatternfound");
		System.out.println(outpatternfound);
		System.out.println(toString(replacements.get(randomInt2)));

		if (!toString(oldFeatureValue).equals(toString(replacements.get(randomInt2))) && checkinputoutput == true) {
			// if(true) {
			copyFeatures(oldFeatureValue, replacements.get(randomInt2));
			object2modify_src.eSet(featureDefinition, replacements.get(randomInt2));

			// if (comments != null)
			comments.add("\n-- MUTATION \"" + this.getDescription() + "\" from "
					+ toString(modifiable.get(this.indexrule)) + ":" + toString(oldFeatureValue) + " to "
					+ toString(modifiable.get(this.indexrule)) + ":" + toString(replacements.get(randomInt2))
					+ " (line " + modifiable.get(this.indexrule).getLocation() + " of original transformation)\n");
			String comment = "\n-- MUTATION \"" + this.getDescription() + "\" from "
					+ toString(modifiable.get(this.indexrule)) + ":" + toString(oldFeatureValue) + " to "
					+ toString(modifiable.get(this.indexrule)) + ":" + toString(replacements.get(randomInt2))
					+ " (line " + modifiable.get(this.indexrule).getLocation() + " of original transformation)\n";
			System.out.println("\n-- MUTATION \"" + this.getDescription() + "\" from "
					+ toString(modifiable.get(this.indexrule)) + ":" + toString(oldFeatureValue) + " to "
					+ toString(modifiable.get(this.indexrule)) + ":" + toString(replacements.get(randomInt2))
					+ " (line " + modifiable.get(this.indexrule).getLocation() + " of original transformation)\n");
			NSGAIIpostprocessing.numop = NSGAIIpostprocessing.numop + 1;
			ReturnResult.set(0, wrapper);
			ReturnResult.set(1, atlModel);
			ReturnResult.add(comment);
			NSGAIIpostprocessing.indexrulechangedinpattern.add(this.indexrule);

		}
		// checkmutationapply = true;

		return ReturnResult;
	}

	private boolean IFOutPatternHasSubClass(String outpatternfound, int i) {
		// TODO Auto-generated method stub

		Settingpostprocessing s = new Settingpostprocessing();
		String MMRootPath2 = null;
		if (i == 0)
			MMRootPath2 = s.gettargetmetamodel();
		else
			MMRootPath2 = s.getsourcemetamodel();
		MetaModel metatarget = null;
		try {
			metatarget = new MetaModel(MMRootPath2);
		} catch (transException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.listsubclasslefthandside.clear();
		this.listsubclasslefthandside = new ArrayList<String>();
		for (EClassifier classifier : metatarget.getEClassifiers()) {

			if (classifier instanceof EClass) {

				EClass child = ((EClass) classifier);

				if (child.getName().equals(outpatternfound)) {

					for (EClassifier classifier2 : metatarget.getEClassifiers()) {

						if (classifier2 instanceof EClass) {

							EClass child2 = ((EClass) classifier2);

							if (child.isSuperTypeOf(child2) && !child2.equals(child)) {

								this.listsubclasslefthandside.add(child2.getName());
								System.out.println("inherit");
								System.out.println(child2);
								// return true;

							}
						}
					}
				}
			}
		}
		if (this.listsubclasslefthandside.size() > 0)
			return true;
		else
			return false;
	}

	private EObject FindOutpatternIsequaltoLeftHandSideType(ATLModel wrapper, String typeleftside, String featureName) {
		// TODO Auto-generated method stub
		List<OutPatternElement> outpatternlist = (List<OutPatternElement>) wrapper
				.allObjectsOf(OutPatternElement.class);
		EObject oldFeatureValue2 = null;
		for (int i = 0; i < outpatternlist.size(); i++) {

			EStructuralFeature featureDefinition = wrapper.source(outpatternlist.get(i)).eClass()
					.getEStructuralFeature(featureName);
			String[] array = outpatternlist.get(i).getLocation().split(":", 2);

			if (featureDefinition != null && featureDefinition.getUpperBound() == 1) {

				EObject object2modify_src = wrapper.source(outpatternlist.get(i));
				EObject oldFeatureValue = (EObject) object2modify_src.eGet(featureDefinition);

				System.out.println("eqq");
				System.out.println(toString(oldFeatureValue));

				if (toString(oldFeatureValue).equals(typeleftside) && !typeleftside.equals("String")) {
					System.out.println("2eqq");
					this.numberoutpattern = this.numberoutpattern + 1;
					for (int j = 0; j < NSGAIIpostprocessing.faultrule.size(); j++) {
						if (Integer.parseInt(array[0]) > NSGAIIpostprocessing.faultrule.get(j)
								&& Integer.parseInt(array[0]) < NSGAIIpostprocessing.finalrule.get(j))

							this.indexrule = j;

					}
					oldFeatureValue2 = oldFeatureValue;
					// return (toString(oldFeatureValue2));

				}

			}

		}
		return oldFeatureValue2;
	}

	private String IfRightHandSideISHelperReturnHelperType(ATLModel wrapper, int h, int h2) {
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

						String helpertype = ReturnHelpetType(wrapper, helper, h, h2, i, j);

						return helpertype;

					}

				}
			}
		return null;

	}

	private String ReturnHelpetType(ATLModel wrapper, List<Helper> helper, int h, int h2, int i, int j) {
		// TODO Auto-generated method stub
		System.out.println("orek");
		System.out.println(NSGAIIpostprocessing.allattributrightside.get(h).get(h2).get(j));
		String helper_return = helper.get(i).getDefinition().getFeature() instanceof Operation
				? toString(((Operation) helper.get(i).getDefinition().getFeature()).getReturnType())
				: toString(((Attribute) helper.get(i).getDefinition().getFeature()).getType());

		System.out.println(helper_return);

		if (helper_return.equals("SequenceType")) {

			List<CollectionType> variables3 = (List<CollectionType>) wrapper.allObjectsOf(CollectionType.class);
			String[] array = helper.get(i).getLocation().split(":", 2);

			for (int k = 0; k < variables3.size(); k++) {

				String[] array2 = variables3.get(k).getLocation().split(":", 2);

				if (Integer.parseInt(array[0]) == Integer.parseInt(array2[0])) {

					EStructuralFeature featureDefinition = wrapper.source(variables3.get(k)).eClass()
							.getEStructuralFeature("elementType");
					EObject object2modify_src = wrapper.source(variables3.get(k));
					EObject oldFeatureValue = (EObject) object2modify_src.eGet(featureDefinition);
					return toString(oldFeatureValue);

				}

			}

		}

		return helper_return;

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
				System.out.println(leftsidebinding);
				System.out.println(typenavigation);
				// indexattrnavi=indexattrnavi+1;

			}
		}

		// leftsidebinding.getCoSolutionpostprocessing().getOp().listpropertyname.get(h).get(h2)
		return typenavigation;
	}

	private <ToModify extends LocatedElement> String ReturnSpecificInput(ATLModel wrapper, String featureName, int h,
			List<ToModify> modifiable) {
		// TODO Auto-generated method stub
		int indexrule = -1;
		List<OutPatternElement> variables = (List<OutPatternElement>) wrapper.allObjectsOf(OutPatternElement.class);
		String[] array = variables.get(h).getLocation().split(":", 2);
		for (int j = 0; j < NSGAIIpostprocessing.faultrule.size(); j++) {
			if (Integer.parseInt(array[0]) > NSGAIIpostprocessing.faultrule.get(j)
					&& Integer.parseInt(array[0]) < NSGAIIpostprocessing.finalrule.get(j))

				indexrule = j;

		}
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

	private int checknumberoclkindofinrules() {
		// TODO Auto-generated method stub
		int numberallocl = 0;
		for (int h = 0; h < NSGAIIpostprocessing.ocliskineoflocation.size(); h++) {

			int indexruletp = -1;
			int indexoclkindlineattrsecond = -1;

			for (int j = 0; j < NSGAIIpostprocessing.faultrule.size(); j++) {
				if (NSGAIIpostprocessing.ocliskineoflocation.get(h) > NSGAIIpostprocessing.faultrule.get(j)
						&& NSGAIIpostprocessing.ocliskineoflocation.get(h) < NSGAIIpostprocessing.finalrule.get(j))

					indexruletp = j;

			}

			if (NSGAIIpostprocessing.errorrule.contains(indexruletp)) {

				int indexoclkindlineattrfirst = NSGAIIpostprocessing.oclkindlineattr
						.indexOf(Integer.toString(NSGAIIpostprocessing.ocliskineoflocation.get(h)));
				// System.out.println("zahra");
				// System.out.println(indexoclkindlineattrfirst);
				if ((h + 1) == NSGAIIpostprocessing.ocliskineoflocation.size())
					indexoclkindlineattrsecond = NSGAIIpostprocessing.oclkindlineattr.size();
				if (indexoclkindlineattrfirst >= 0 && (h + 1) != NSGAIIpostprocessing.ocliskineoflocation.size())
					indexoclkindlineattrsecond = NSGAIIpostprocessing.oclkindlineattr
							.indexOf(Integer.toString(NSGAIIpostprocessing.ocliskineoflocation.get(h + 1)));

				// System.out.println("zahra2");
				// System.out.println(indexoclkindlineattrfirst);
				// System.out.println(indexoclkindlineattrsecond);
				for (int i = indexoclkindlineattrfirst + 2; i < indexoclkindlineattrsecond; i++) {
					if (NSGAIIpostprocessing.oclkindlineattr.get(i).toString().contains("oclIsKindOf")
							|| NSGAIIpostprocessing.oclkindlineattr.get(i).toString().contains("oclAsType")) {
						// this.indexallerrorocl.add(i );
						numberallocl = numberallocl + 1;
						System.out.println("tedad");
						System.out.println(numberallocl);
					}

				}

			}
			// !this.alloclrule.contains(NSGAIIpostprocessing.ocliskineoflocation.get(h))
			/*
			 * if(this.listoldclass.size()==numboclthisline &&
			 * NSGAIIpostprocessing.ocliskineoflocation.get(h)>=NSGAIIpostprocessing.
			 * faultrule.get(0) &&NSGAIIpostprocessing.errorrule.contains(indexruletp))
			 * checkmutationapply=true; if( this.listoldclass.size()<numboclthisline &&
			 * NSGAIIpostprocessing.ocliskineoflocation.get(h)>=NSGAIIpostprocessing.
			 * faultrule.get(0) && NSGAIIpostprocessing.errorrule.contains(indexruletp))
			 * checkmutationapply=false;
			 * 
			 */

		}

		return numberallocl;
	}

	private <ToModify extends LocatedElement> List<Object> Postprocessingforoclwithoutiteration(ATLModel wrapper,
			List<EObject> value2, int randomInt, EStructuralFeature featureDefinition, boolean checkmutationapply,
			EMFModel atlModel, MetaModel[] metamodels, int count, Solution solution, String[] array,
			EDataTypeEList<String> comments, String comment, int indexrule, List<ToModify> modifiable,
			List<Object> ReturnResult) {
		// TODO Auto-generated method stub

		if (value2.size() > 0) {
			// ye ja ke ocl hast peida kon
			int randomInt3 = calculaterandomInt2(value2);
			// classi ke dakhele ocl hast ra begir
			EObject oldFeatureValue2 = value2.get(randomInt3);
			// System.out.println(solution.listfilterdeletion);
			// System.out.println(Integer.parseInt(array[0]));
			System.out.println("inside");
			System.out.println(toString(oldFeatureValue2));

			// System.out.println(
			// oldFeatureValue2.eClass().getEPackage().getEClassifier("OclModelElement").isInstance(oldFeatureValue2));
			if (!oldFeatureValue2.eClass().getEPackage().getEClassifier("OclModelElement").isInstance(oldFeatureValue2))
				checkmutationapply = false;
			if (oldFeatureValue2.eClass().getEPackage().getEClassifier("OclModelElement").isInstance(oldFeatureValue2)

			) {

				// ye class dige baraye dakhele ocl entekhab kon
				List<EObject> replacements = this.replacements(atlModel, (EObject) oldFeatureValue2, metamodels);

				boolean findreplace = false;

				int randomInt4 = 0;
				boolean perivoustype = false;
				int numloop = 1;

				String attributenavigation;
				while (findreplace == false) {
					boolean findcorrectnavigation = false;
					// ArrayList<String> prohibitclass = new ArrayList<String>();
					findallattributespecificoclkindof(array, oldFeatureValue2);
					System.out.println("after");
					for (int i = this.start; i < this.indexcorrectocl; i++) {
						// this.numrun=this.numrun+1;
						System.out.println(NSGAIIpostprocessing.oclkindlineattr.get(i));
					}
					System.out.println("startindex");
					System.out.println(this.numrun);
					int indexattrnavi = this.start;
					String typenavigation = null;
					attributenavigation = NSGAIIpostprocessing.oclkindlineattr.get(indexattrnavi);
					// ArrayList<String> prohibitclass = new ArrayList<String>();
					// prohibitclass shamele class ha ke to hamoon khat bashe vali toye oclkindof
					// dige bashe
					/*
					 * for(int i=1;i<this.oclindex.size()-1;i++) { if(
					 * this.indexcorrectocl!=this.oclindex.get(i)) {
					 * 
					 * prohibitclass.add( NSGAIIpostprocessing.oclkindlineattr.get(
					 * this.oclindex.get(i) +1)); }
					 * 
					 * }
					 */
					while (findcorrectnavigation == false) {
						randomInt4 = calculaterandomInt(replacements);

						// int randomIntvar = -2;
						// list hameye navigationha
						// List<VariableExp> variables =
						// (List<VariableExp>)wrapper.allObjectsOf(VariableExp.class);
						// boolean findcorrectnavigation=false;

						// while dare choon bayad hameye navigationha ra barresi kone ta yeki ke to on
						// khate
						// peyda kone
						// while(findcorrectnavigation==false) {
						// choose randomly one navigation
						// List<Integer> Resultvariable = ReturnFirstIndex(randomIntvar,
						// variables.size(), checkmutationapply, count, solution);
						// randomIntvar = Resultvariable.get(0);

						// line of navigation
						// String[] arrayvariable = variables.get(randomIntvar).getLocation().split(":",
						// 2);
						// EObject navigationExpression = variables.get(randomIntvar).eContainer();
						// toye oclkindlineattr koja hamin khate ocl hast

						// && Integer.parseInt(arrayvariable[0])==(Integer.parseInt(array[0]))
						// navigationExpression instanceof NavigationOrAttributeCallExp
						// line navigation ke khatesh ba ocliskind barabar bashe
						// if(true ) {

						// EStructuralFeature featureDefinitionvar =
						// wrapper.source(navigationExpression).eClass().getEStructuralFeature("name");
						// Object object2modify_src2 =
						// wrapper.source(navigationExpression).eGet(featureDefinitionvar);
						// System.out.println("oooooooooooolllllllld");
						String str = "";
						for (int l = this.startcombinedattr; l < this.listattrnavigation.size(); l++)
							str = str + this.listattrnavigation.get(l);
						// System.out.println("str");
						// System.out.println(str);

						String newstr = str;
						str = str + toString(oldFeatureValue2);
						// System.out.println("str");
						// System.out.println(str);
						// System.out.println("newstr");
						// System.out.println(newstr);

						int id = -1;
						for (int l = 0; l < this.checklistattr.size(); l++)
							if (this.checklistattr.get(l).endsWith(str)) {
								id = l;
								System.out.println("yes");
							}
						newstr = newstr + toString(replacements.get(randomInt4));
						boolean checknewclass = false;
						for (int l = 0; l < this.checklistattr.size(); l++)
							if (id != l && this.checklistattr.get(l).endsWith(newstr)) {

								checknewclass = true;
							}

						// System.out.println(str);
						// System.out.println(newstr);
						// System.out.println(checknewclass);
						// System.out.println(this.listattrnavigation);
						// System.out.println(this.startcombinedattr);
						// System.out.println(this.checklistattr);

						// System.out.println(this.checknotrepeat);
						// System.out.println(this.indexcorrectocl);
						// avalin attribut navigation
						// !toString(oldFeatureValue2).equals(toString(replacements.get(randomInt4))))
						// !prohibitclass.contains(toString(replacements.get(randomInt4)))
						System.out.println(this.listreplacement);
						if (checknewclass == false
								&& !this.listreplacement.contains(toString(replacements.get(randomInt4)))
								&& !toString(oldFeatureValue2).equals(toString(replacements.get(randomInt4)))) {
							this.listreplacement.add(toString(replacements.get(randomInt4)));
							findcorrectnavigation = true;
						}

					}
					// System.out.println(prohibitclass);
					System.out.println(this.indexcorrectocl);
					System.out.println(toString(replacements.get(randomInt4)));
					EObject oldFeatureValue = null;
					// System.out.println("prohibitclass");
					boolean checknotrepeat = false;
					// if(NSGAIIpostprocessing.oclkindlineattr.get(indexattrnavi).equals(
					// attributenavigation) &&
					// !prohibitclass.contains(toString(replacements.get(randomInt4))) ) {

					if (!this.checknotrepeat.contains(this.indexcorrectocl) && this.indexcorrectocl > 0) {
						System.out.println(attributenavigation);
						System.out.println("attrnavigation");
						System.out.println(this.listattrnavigation);
						// list kon hameye inpattern ha
						if (this.listattrnavigation.size() > 0)
							if (NSGAIIpostprocessing.classnamestring.contains(this.listattrnavigation.get(0))) {
								typenavigation = this.listattrnavigation.get(0);
								indexattrnavi = this.startcombinedattr;

							} else if (this.listattrnavigation.size() == 0
									|| !NSGAIIpostprocessing.classnamestring.contains(this.listattrnavigation.get(0))) {
								// list kon hameye inpattern ha
								List<InPatternElement> listinpattern = (List<InPatternElement>) wrapper
										.allObjectsOf(InPatternElement.class);
								EStructuralFeature featureDefinitionpattern = wrapper
										.source(listinpattern.get(indexrule)).eClass().getEStructuralFeature("type");
								// inpattern in rule ra begir
								EObject object2modify_pt = wrapper.source(listinpattern.get(indexrule));
								oldFeatureValue = (EObject) object2modify_pt.eGet(featureDefinitionpattern);
								typenavigation = toString(oldFeatureValue);
								indexattrnavi = 0;
								this.numrun = this.numrun + 1;

							}

						/*
						 * List<InPatternElement> listinpattern = (List<InPatternElement>)
						 * wrapper.allObjectsOf(InPatternElement.class); EStructuralFeature
						 * featureDefinitionpattern =
						 * wrapper.source(listinpattern.get(indexrule)).eClass()
						 * .getEStructuralFeature("type"); // inpattern in rule ra begir EObject
						 * object2modify_pt = wrapper.source(listinpattern.get(indexrule)); EObject
						 * oldFeatureValue = (EObject) object2modify_pt.eGet(featureDefinitionpattern);
						 * typenavigation=toString(oldFeatureValue);
						 */
						System.out.println("inpattern");
						System.out.println(this.listattrnavigation);
						System.out.println(this.numrun);
						System.out.println(typenavigation);

						// class inpattern ra dar list classha peyda kon
						checknotrepeat = true;
						for (int j = 0; j < this.numrun; j++) {
							boolean check = false;
							int indexclassname = NSGAIIpostprocessing.classnamestring.indexOf(typenavigation);
							// String typenavigation=null;
							for (int i = NSGAIIpostprocessing.classnamestartpoint.get(
									indexclassname); i < NSGAIIpostprocessing.classnamestartpoint.get(indexclassname)
											+ NSGAIIpostprocessing.classnamelength.get(indexclassname); i++) {

								// hameye attributha ke in class dare check kon
								if (NSGAIIpostprocessing.listinheritattributesourcemetamodel.get(i).getName()
										.equals(this.listattrnavigation.get(indexattrnavi))) {

									typenavigation = NSGAIIpostprocessing.listnavigationtypeinheritattr.get(i);
									check = true;
									System.out.println(typenavigation);
									// indexattrnavi=indexattrnavi+1;

								}
							}
							if (check == true) {
								indexattrnavi = indexattrnavi + 1;

							}

						}
						if (oldFeatureValue != null)
							if (typenavigation == toString(oldFeatureValue) && this.listattrnavigation.size() > 0) {
								findreplace = true;
								checknotrepeat = false;
								this.listoldclass.add(toString(oldFeatureValue2));

							}
						// attributenavigation=object2modify_src3.toString();

						// findcorrectnavigation=true;

					} else {
						findreplace = true;
					}
					// }
					// }
					if (checknotrepeat == true) {
						ReturnResult = CheckInheritanceinsideoclandclass(typenavigation, oldFeatureValue2, findreplace,
								perivoustype, checkmutationapply, value2, comments, modifiable, comment, replacements,
								randomInt4, randomInt, randomInt3, indexrule, wrapper, atlModel, ReturnResult, array);

						findreplace = this.findcorrectclass;

					}
				}
			}

		}

		return ReturnResult;
	}

	private <ToModify extends LocatedElement> List<Object> Postprocessingforoclwithiteration(ATLModel wrapper,
			List<EObject> value2, int randomInt, EStructuralFeature featureDefinition, boolean checkmutationapply,
			EMFModel atlModel, MetaModel[] metamodels, int count, Solution solution, String[] array,
			EDataTypeEList<String> comments, String comment, int indexrule, List<ToModify> modifiable,
			List<Object> ReturnResult, int idrule) {
		// TODO Auto-generated method stub

		if (value2.size() > 0) {
			// ye ja ke ocl hast peida kon
			int randomInt3 = calculaterandomInt2(value2);
			// classi ke dakhele ocl hast ra begir
			EObject oldFeatureValue2 = value2.get(randomInt3);
			// System.out.println(solution.listfilterdeletion);
			// System.out.println(Integer.parseInt(array[0]));
			System.out.println("inside");
			System.out.println("inside");
			System.out.println(toString(oldFeatureValue2));
			// && !this.listoldclass.contains( toString(oldFeatureValue2))
			// System.out.println(
			// oldFeatureValue2.eClass().getEPackage().getEClassifier("OclModelElement").isInstance(oldFeatureValue2));
			if (!oldFeatureValue2.eClass().getEPackage().getEClassifier("OclModelElement").isInstance(oldFeatureValue2))
				checkmutationapply = false;
			if (oldFeatureValue2.eClass().getEPackage().getEClassifier("OclModelElement").isInstance(oldFeatureValue2)

			) {

				// ye class dige baraye dakhele ocl entekhab kon
				List<EObject> replacements = this.replacements(atlModel, (EObject) oldFeatureValue2, metamodels);

				boolean findreplace = false;

				int randomInt4 = 0;
				boolean perivoustype = false;
				int numloop = 1;

				String attributenavigation;
				while (findreplace == false) {
					boolean findcorrectnavigation = false;
					ArrayList<String> prohibitclass = new ArrayList<String>();
					ArrayList<String> prohibitattr = new ArrayList<String>();

					findallattributespecificoclkindofwithiteration(array, oldFeatureValue2);
					System.out.println("after");
					for (int i = this.start; i < this.indexcorrectocl; i++) {
						// this.numrun=this.numrun+1;
						System.out.println(NSGAIIpostprocessing.oclkindlineattr.get(i));
					}
					System.out.println("startindex");
					System.out.println(this.numrun);
					int indexattrnavi = this.start;
					String typenavigation = null;
					attributenavigation = NSGAIIpostprocessing.oclkindlineattr.get(indexattrnavi);
					// ArrayList<String> prohibitclass = new ArrayList<String>();
					// prohibitclass shamele class ha ke to hamoon khat bashe vali toye oclkindof
					// dige bashe
					/*
					 * for(int i=1;i<this.oclindex.size()-1;i++) { if(
					 * this.indexcorrectocl!=this.oclindex.get(i)) {
					 * 
					 * // prohibitclass.add( NSGAIIpostprocessing.oclkindlineattr.get(
					 * this.oclindex.get(i) +1)); //
					 * prohibitattr.add(NSGAIIpostprocessing.oclkindlineattr.get(
					 * this.oclindex.get(i) -1) ); }
					 * 
					 * }
					 */
					while (findcorrectnavigation == false) {
						randomInt4 = calculaterandomInt(replacements);
						// har vaght dakhele ocliskind() avaz mishe to array save mikone
						// oldargumenttype [23, ObjectNode, 34, ProtocolTransition]
						// vaghti dobare operation argument seda mizane ghabliha ro to array save mikone
						// az onha estefade mikone
						System.out.println("argutyp");
						System.out.println(NSGAIIpostprocessing.indexrulechangedinpattern);
						System.out.println(idrule);
						/*
						 * if(NSGAIIpostprocessing.numberoperationargument==-1 &&
						 * NSGAIIpostprocessing.oldargumenttype.size()>0 &&
						 * NSGAIIpostprocessing.indexrulechangedinpattern.contains(idrule )) { String
						 * candidatereplace=null; int id= NSGAIIpostprocessing.oldargumenttype.indexOf(
						 * Integer.toString(this.indexcorrectocl+1 )); if(id>=0) {
						 * candidatereplace=NSGAIIpostprocessing.oldargumenttype.get(id +1);
						 * 
						 * // System.out.println("argutyp");
						 * System.out.println(NSGAIIpostprocessing.oldargumenttype);
						 * 
						 * boolean findprevioustype=false; while(findprevioustype==false) {
						 * 
						 * randomInt4 = calculaterandomInt(replacements); if(
						 * toString(replacements.get(randomInt4)).equals( candidatereplace))
						 * findprevioustype=true;
						 * 
						 * } System.out.println(candidatereplace);
						 * System.out.println(NSGAIIpostprocessing.indexrulechangedinpattern);
						 * System.out.println(Integer.parseInt(array[0])); } }
						 * 
						 */
						// int randomIntvar = -2;
						// list hameye navigationha
						// List<VariableExp> variables =
						// (List<VariableExp>)wrapper.allObjectsOf(VariableExp.class);
						// boolean findcorrectnavigation=false;

						// while dare choon bayad hameye navigationha ra barresi kone ta yeki ke to on
						// khate
						// peyda kone
						// while(findcorrectnavigation==false) {
						// choose randomly one navigation
						// List<Integer> Resultvariable = ReturnFirstIndex(randomIntvar,
						// variables.size(), checkmutationapply, count, solution);
						// randomIntvar = Resultvariable.get(0);

						// line of navigation
						// String[] arrayvariable = variables.get(randomIntvar).getLocation().split(":",
						// 2);
						// EObject navigationExpression = variables.get(randomIntvar).eContainer();
						// toye oclkindlineattr koja hamin khate ocl hast

						// && Integer.parseInt(arrayvariable[0])==(Integer.parseInt(array[0]))
						// navigationExpression instanceof NavigationOrAttributeCallExp
						// line navigation ke khatesh ba ocliskind barabar bashe
						// if(true ) {

						// EStructuralFeature featureDefinitionvar =
						// wrapper.source(navigationExpression).eClass().getEStructuralFeature("name");
						// Object object2modify_src2 =
						// wrapper.source(navigationExpression).eGet(featureDefinitionvar);

						System.out.println(this.listattrforcheck);
						boolean checkattr = false;
						int indexclassname = NSGAIIpostprocessing.classnamestring
								.indexOf(toString(replacements.get(randomInt4)));
						for (int i = NSGAIIpostprocessing.classnamestartpoint2
								.get(indexclassname); i < NSGAIIpostprocessing.classnamestartpoint2.get(indexclassname)
										+ NSGAIIpostprocessing.classnamelength2.get(indexclassname); i++) {
							// System.out.println("kor1");
							// System.out.println(NSGAIIpostprocessing.listsourcemetamodel.get(i).getName());
							for (int j = 0; j < this.listattrforcheck.size(); j++) {
								// hameye attributha ke in class dare check kon

								if (NSGAIIpostprocessing.listsourcemetamodel.get(i).getName()
										.contains(this.listattrforcheck.get(j))) {
									// check=true;
									System.out.println("roz");
									// typenavigation=NSGAIIpostprocessing.listnavigationtypeinheritattr.get(i);
									// System.out.println("kor");
									checkattr = true;

								}
							}
						}
						if (this.listattrforcheck.size() == 0)
							checkattr = true;
						if (checkattr == true) {
							System.out.println("atrcheck");
							System.out.println(this.listattrnavigation);
							String str = "";
							int index = -1;
							if (this.listattrnavigation.size() > 0)
								index = NSGAIIpostprocessing.classnamestring.indexOf(this.listattrnavigation.get(0));
							if (index >= 0)
								this.startcombinedattr = 1;
							for (int l = this.startcombinedattr; l < this.listattrnavigation.size(); l++)
								str = str + this.listattrnavigation.get(l);
							System.out.println("str");
							System.out.println(str);
							String newstr = str;
							str = str + toString(oldFeatureValue2);
							System.out.println("str");
							System.out.println(str);
							System.out.println("newstr");
							System.out.println(newstr);
							System.out.println(this.checklistattr);
							int id = -1;
							for (int l = 0; l < this.checklistattr.size(); l++)
								if (this.checklistattr.get(l).endsWith(str)) {
									id = l;
									System.out.println("yes");
								}
							newstr = newstr + toString(replacements.get(randomInt4));
							boolean checknewclass = false;
							for (int l = 0; l < this.checklistattr.size(); l++)
								if (id != l && this.checklistattr.get(l).endsWith(newstr)) {

									checknewclass = true;
								}

							System.out.println(str);
							System.out.println(newstr);
							System.out.println(checknewclass);
							System.out.println(this.listattrnavigation);
							System.out.println(this.startcombinedattr);
							System.out.println(this.checklistattr);
							// if(!toString(oldFeatureValue2).equals(toString(replacements.get(randomInt4))))
							// avalin attribut navigation
							if (checknewclass == false
									&& !this.listreplacement.contains(toString(replacements.get(randomInt4)))) {

								this.listreplacement.add(toString(replacements.get(randomInt4)));
								findcorrectnavigation = true;
							}

						}

					}
					System.out.println(prohibitclass);
					System.out.println(this.indexcorrectocl);

					System.out.println(toString(replacements.get(randomInt4)));
					// System.out.println("prohibitclass");
					// if(NSGAIIpostprocessing.oclkindlineattr.get(indexattrnavi).equals(
					// attributenavigation) &&
					// !prohibitclass.contains(toString(replacements.get(randomInt4))) ) {
					// !this.checknotrepeat.contains( this.indexcorrectocl)

					// !this.checknotrepeat.contains( this.indexcorrectocl) &&
					boolean checknotrepeat = false;
					EObject oldFeatureValue = null;
					if (!this.checknotrepeat.contains(this.indexcorrectocl) && this.indexcorrectocl > 0) {

						if (this.listattrnavigation.size() > 0) {
							if (NSGAIIpostprocessing.classnamestring.contains(this.listattrnavigation.get(0))) {
								typenavigation = this.listattrnavigation.get(0);
								indexattrnavi = 1;

							} else {
								// list kon hameye inpattern ha
								List<InPatternElement> listinpattern = (List<InPatternElement>) wrapper
										.allObjectsOf(InPatternElement.class);
								EStructuralFeature featureDefinitionpattern = wrapper
										.source(listinpattern.get(indexrule)).eClass().getEStructuralFeature("type");
								// inpattern in rule ra begir
								EObject object2modify_pt = wrapper.source(listinpattern.get(indexrule));
								oldFeatureValue = (EObject) object2modify_pt.eGet(featureDefinitionpattern);
								typenavigation = toString(oldFeatureValue);
								indexattrnavi = 0;
								this.listattrnavigation.add(typenavigation);

							}
						}
						checknotrepeat = true;
						this.numrun = this.listattrnavigation.size() - 1;
						// if(NSGAIIpostprocessing.oclkindlineattr.get(indexattrnavi).equals(
						// attributenavigation) &&
						// !prohibitclass.contains(toString(replacements.get(randomInt4))) ) {
						System.out.println(attributenavigation);

						System.out.println("inpattern");

						System.out.println(this.numrun);
						System.out.println(typenavigation);
						// class inpattern ra dar list classha peyda kon
						this.numrun = 1;
						this.repeatargument = false;
						for (int j = 0; j < this.listattrnavigation.size() - 1; j++) {
							boolean check = false;
							int indexclassname = NSGAIIpostprocessing.classnamestring.indexOf(typenavigation);
							// String typenavigation=null;
							if (indexclassname > 0) {
								for (int i = NSGAIIpostprocessing.classnamestartpoint
										.get(indexclassname); i < NSGAIIpostprocessing.classnamestartpoint
												.get(indexclassname)
												+ NSGAIIpostprocessing.classnamelength.get(indexclassname); i++) {
									System.out.println(indexattrnavi);
									// hameye attributha ke in class dare check kon
									if (NSGAIIpostprocessing.listinheritattributesourcemetamodel.get(i).getName()
											.equals(this.listattrnavigation.get(indexattrnavi))) {
										check = true;
										System.out.println("roz");
										typenavigation = NSGAIIpostprocessing.listnavigationtypeinheritattr.get(i);
										System.out.println(typenavigation);

									}
								}
							}

							if (check == true)
								indexattrnavi = indexattrnavi + 1;
							else
								this.repeatargument = true;

						}
						if (oldFeatureValue != null)
							if (typenavigation == toString(oldFeatureValue) && this.listattrnavigation.size() > 0) {
								findreplace = true;
								checknotrepeat = false;
								this.listoldclass.add(toString(oldFeatureValue2));

							}
						// attributenavigation=object2modify_src3.toString();

						// findcorrectnavigation=true;

						// }
					} else {
						findreplace = true;
					}
					// }
					if (checknotrepeat == true) {
						ReturnResult = CheckInheritanceinsideoclandclass(typenavigation, oldFeatureValue2, findreplace,
								perivoustype, checkmutationapply, value2, comments, modifiable, comment, replacements,
								randomInt4, randomInt, randomInt3, indexrule, wrapper, atlModel, ReturnResult, array);

						findreplace = this.findcorrectclass;
					}

				}
			}

		}

		return ReturnResult;
	}

	private void findallattriflinebeforeoclandtwolinebeforeiteration(String[] array) {
		this.listattrnavigation.clear();
		this.listattrnavigation = new ArrayList<String>();
		if (NSGAIIpostprocessing.ocliskineoflocation.contains(Integer.parseInt(array[0]) - 1)
				&& NSGAIIpostprocessing.iterationcall.contains(Integer.parseInt(array[0]) - 2)) {

			int indexoclkindlineattr = NSGAIIpostprocessing.leftsideattriteration
					.indexOf(Integer.toString((Integer.parseInt(array[0])) - 2));
			int indexocliskineoflocation = NSGAIIpostprocessing.fleshline.indexOf(Integer.parseInt(array[0]) - 2);
			int indexoclkindlineattrsecond = -1;
			if (indexoclkindlineattr >= 0) {
				// int indexoclkindlineattrsecond=-1;
				if ((indexocliskineoflocation + 1) == NSGAIIpostprocessing.fleshline.size())
					indexoclkindlineattrsecond = NSGAIIpostprocessing.leftsideattriteration.size();
				if (indexocliskineoflocation >= 0
						&& (indexocliskineoflocation + 1) != NSGAIIpostprocessing.fleshline.size())
					indexoclkindlineattrsecond = NSGAIIpostprocessing.leftsideattriteration.indexOf(
							Integer.toString(NSGAIIpostprocessing.fleshline.get(indexocliskineoflocation + 1)));

			}

			for (int i = indexoclkindlineattr + 1; i < indexoclkindlineattrsecond; i++) {
				boolean addattrleft = false;
				System.out.println("ppppppp");
				// System.out.println(NSGAIIpostprocessing.leftsideattriteration.get(i));
				for (int j = 0; j < NSGAIIpostprocessing.listsourcemetamodel.size(); j++) {
					// System.out.println(NSGAIIpostprocessing.listsourcemetamodel.get(
					// j).getName());
					if ((NSGAIIpostprocessing.classnamestring
							.contains(NSGAIIpostprocessing.leftsideattriteration.get(i))
							|| NSGAIIpostprocessing.listsourcemetamodel.get(j).getName()
									.equals(NSGAIIpostprocessing.leftsideattriteration.get(i))))

					{
						// this.numrun=this.numrun+1;

						addattrleft = true;

					}

				}
				System.out.println(addattrleft);

				if (addattrleft == true)
					this.listattrnavigation.add(NSGAIIpostprocessing.leftsideattriteration.get(i));

			}

			int indexoclkindlineattr2 = NSGAIIpostprocessing.oclkindlineattr
					.indexOf(Integer.toString((Integer.parseInt(array[0])) - 1));
			// khati ke entekhab kard dar ocliskineoflocation peyda mikone
			int indexocliskineoflocation2 = NSGAIIpostprocessing.ocliskineoflocation
					.indexOf(Integer.parseInt(array[0]) - 1);
			// toye oclkindlineattr koja hamin ocl tamom mishe
			// int
			// indexoclkindlineattrsecond=NSGAIIpostprocessing.oclkindlineattr.indexOf(Integer.toString(NSGAIIpostprocessing.ocliskineoflocation.get(
			// indexocliskineoflocation+1)));
			int indexoclkindlineattrsecond2 = -1;
			if (indexoclkindlineattr2 >= 0) {
				// int indexoclkindlineattrsecond=-1;
				if ((indexocliskineoflocation2 + 1) == NSGAIIpostprocessing.ocliskineoflocation.size())
					indexoclkindlineattrsecond2 = NSGAIIpostprocessing.oclkindlineattr.size();
				if (indexocliskineoflocation2 >= 0
						&& (indexocliskineoflocation2 + 1) != NSGAIIpostprocessing.ocliskineoflocation.size())
					indexoclkindlineattrsecond2 = NSGAIIpostprocessing.oclkindlineattr.indexOf(Integer
							.toString(NSGAIIpostprocessing.ocliskineoflocation.get(indexocliskineoflocation2 + 1)));

			}

			for (int i = indexoclkindlineattr2 + 1; i < indexoclkindlineattrsecond2; i++) {
				boolean addattrleft = false;
				System.out.println("ppppppp");
				// System.out.println(NSGAIIpostprocessing.leftsideattriteration.get(i));
				for (int j = 0; j < NSGAIIpostprocessing.listsourcemetamodel.size(); j++) {
					int iditrlrtter3 = -1;
					// System.out.println(NSGAIIpostprocessing.listsourcemetamodel.get(
					// j).getName());
					if ((NSGAIIpostprocessing.classnamestring.contains(NSGAIIpostprocessing.oclkindlineattr.get(i))
							|| NSGAIIpostprocessing.listsourcemetamodel.get(j).getName()
									.equals(NSGAIIpostprocessing.oclkindlineattr.get(i))))

					{
						boolean checkclasstype = true;
						if (NSGAIIpostprocessing.classnamestring.contains(NSGAIIpostprocessing.oclkindlineattr.get(i))
								&& NSGAIIpostprocessing.oclkindlineattr.get(i - 1).startsWith("oclIsKindOf")) {
							checkclasstype = false;

						}

						if (checkclasstype == true) {
							iditrlrtter3 = NSGAIIpostprocessing.oclletter
									.indexOf(Integer.toString((Integer.parseInt(array[0])) - 2));
							String str = null;
							if (iditrlrtter3 >= 0) {
								str = NSGAIIpostprocessing.oclletter.get(iditrlrtter3 + 1);
							}
							if (!NSGAIIpostprocessing.oclkindlineattr.get(i).toString().equals(str)) {
								System.out.println("pl");
								addattrleft = true;
							}
						}
						// this.numrun=this.numrun+1;

					}

				}
				System.out.println(addattrleft);

				if (addattrleft == true)
					this.listattrnavigation.add(NSGAIIpostprocessing.oclkindlineattr.get(i));

			}

		}

	}

	// in baraye samte chape
	private void findallattributespecificoclliteration(String[] array) {

		this.listattrnavigation.clear();
		this.listattrnavigation = new ArrayList<String>();
		List<String> listattrpriviousocl = new ArrayList<String>();
		List<String> listattrnextocl = new ArrayList<String>();
		this.listattrforcheck.clear();
		this.listattrforcheck = new ArrayList<String>();
		int iditrlrtter = -1;
		int iditrlrtter2 = -1;
		int iditrlrtter3 = -1;
		// oclkindof ghablesh dare peyda kon classesh ra begir
		// oclIsKindOf(UML!AcceptCallAction) AcceptCallAction
		// exists(edge str=edge
		if (NSGAIIpostprocessing.ocliskineoflocation.contains(Integer.parseInt(array[0]) - 2)) {

			String str2 = "";
			int id = NSGAIIpostprocessing.oclkindlineattr.indexOf(Integer.toString(Integer.parseInt(array[0]) - 2));
			int id2 = NSGAIIpostprocessing.oclkindlineattr.indexOf((array[0]));
			boolean checknextline = false;
			for (int i = id + 1; i < id2; i++) {
				if (!NSGAIIpostprocessing.oclkindlineattr.get(i)
						.equals(Integer.toString(Integer.parseInt(array[0]) - 1)) && checknextline == false)
					listattrpriviousocl.add(NSGAIIpostprocessing.oclkindlineattr.get(i));
				else {
					checknextline = true;
				}

				if (NSGAIIpostprocessing.oclkindlineattr.get(i).contains("oclIsKindOf(")) {
					str2 = NSGAIIpostprocessing.oclkindlineattr.get(i + 1);

				}

			}
			// checknextline=false;
			iditrlrtter = NSGAIIpostprocessing.oclletter.indexOf(Integer.toString((Integer.parseInt(array[0])) - 3));
			this.listattrnavigation.add(str2);
			// listattrpriviousocl=[if, edge, source, oclIsKindOf(UML, AcceptEventAction,
			// then, 123, edge, source, oclAsType(UML, ProtocolTransition,
			// trigger->exists(t, |]
			System.out.println("z1");
			System.out.println(listattrpriviousocl);
			System.out.println(this.listattrnavigation);
			System.out.println(iditrlrtter);

		}

		/*
		 * if(NSGAIIpostprocessing.iterationcall.contains(Integer.parseInt(array[0])+1)
		 * && !NSGAIIpostprocessing.ocliskineoflocation.contains(
		 * Integer.parseInt(array[0])+2)) {
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * }
		 */
		if (NSGAIIpostprocessing.ocliskineoflocation.contains(Integer.parseInt(array[0]) + 2)) {

			String str2 = "";
			int id = NSGAIIpostprocessing.oclkindlineattr.indexOf((array[0]));
			int id2 = NSGAIIpostprocessing.oclkindlineattr.indexOf(Integer.toString(Integer.parseInt(array[0]) + 2));
			System.out.println(NSGAIIpostprocessing.oclkindlineattr);
			System.out.println(id);
			System.out.println(id2);
			for (int i = id + 1; i < id2; i++) {
				listattrnextocl.add(NSGAIIpostprocessing.oclkindlineattr.get(i));
				System.out.println(listattrnextocl);

			}
			// iditrlrtter2=NSGAIIpostprocessing.oclletter.indexOf(Integer.toString((Integer.parseInt(array[0]))+1
			// ));
			// this.listattrnavigation.add(str2 );
			iditrlrtter3 = NSGAIIpostprocessing.oclletter.indexOf(Integer.toString((Integer.parseInt(array[0])) - 1));
			int indexoclkindlineattr = NSGAIIpostprocessing.leftsideattriteration
					.indexOf(Integer.toString((Integer.parseInt(array[0])) + 1));
			int indexocliskineoflocation = NSGAIIpostprocessing.fleshline.indexOf(Integer.parseInt(array[0]) + 1);
			int indexoclkindlineattrsecond = -1;
			if (indexoclkindlineattr >= 0) {
				// int indexoclkindlineattrsecond=-1;
				if ((indexocliskineoflocation + 1) == NSGAIIpostprocessing.fleshline.size())
					indexoclkindlineattrsecond = NSGAIIpostprocessing.leftsideattriteration.size();
				if (indexocliskineoflocation >= 0
						&& (indexocliskineoflocation + 1) != NSGAIIpostprocessing.fleshline.size())
					indexoclkindlineattrsecond = NSGAIIpostprocessing.leftsideattriteration.indexOf(
							Integer.toString(NSGAIIpostprocessing.fleshline.get(indexocliskineoflocation + 1)));

			}

			System.out.println("thisc");
			System.out.println(NSGAIIpostprocessing.leftsideattriteration);
			System.out.println(indexoclkindlineattr);
			System.out.println(indexoclkindlineattrsecond);
			for (int i = indexoclkindlineattr + 1; i < indexoclkindlineattrsecond; i++) {
				boolean addattrleft = false;
				System.out.println("ppppppp");
				System.out.println(NSGAIIpostprocessing.leftsideattriteration.get(i));
				for (int j = 0; j < NSGAIIpostprocessing.listsourcemetamodel.size(); j++) {

					if ((NSGAIIpostprocessing.classnamestring
							.contains(NSGAIIpostprocessing.leftsideattriteration.get(i))
							|| NSGAIIpostprocessing.listsourcemetamodel.get(j).getName()
									.equals(NSGAIIpostprocessing.leftsideattriteration.get(i)))
							&& !listattrnextocl.contains(NSGAIIpostprocessing.leftsideattriteration.get(i)))

					{
						// this.numrun=this.numrun+1;
						String str = null;

						if (iditrlrtter3 >= 0) {
							str = NSGAIIpostprocessing.oclletter.get(iditrlrtter3 + 1);
						}
						System.out.println(str + "p");
						System.out.println(NSGAIIpostprocessing.leftsideattriteration.get(i) + "p");
						// System.out.println(stringCompare(str,
						// NSGAIIpostprocessing.leftsideattriteration.get(i)));
						// System.out.println(str.equals(NSGAIIpostprocessing.leftsideattriteration.get(i).toString()));
						if (!NSGAIIpostprocessing.leftsideattriteration.get(i).toString().equals(str)) {
							System.out.println("p2");
							addattrleft = true;
						}
					}

				}

				if (addattrleft == true)
					this.listattrforcheck.add(NSGAIIpostprocessing.leftsideattriteration.get(i));
				// System.out.println(NSGAIIpostprocessing.leftsideattriteration.get(i));
				// this.listattrnavigation.add(NSGAIIpostprocessing.leftsideattriteration.get(i)
				// );

			}

		}
		System.out.println(this.listattrforcheck);
		System.out.println("next");
		System.out.println(Integer.parseInt(array[0]));
		System.out.println(listattrnextocl);
		System.out.println(iditrlrtter2);
		// khate iteration attributha ra begir not edge.source.trigger->exists(t |
		// edge source trigger age ba str bala yeki nist beriz to array
		int indexoclkindlineattr = NSGAIIpostprocessing.leftsideattriteration
				.indexOf(Integer.toString((Integer.parseInt(array[0])) - 1));
		int indexocliskineoflocation = NSGAIIpostprocessing.fleshline.indexOf(Integer.parseInt(array[0]) - 1);
		int indexoclkindlineattrsecond = -1;
		if (indexoclkindlineattr >= 0) {
			// int indexoclkindlineattrsecond=-1;
			if ((indexocliskineoflocation + 1) == NSGAIIpostprocessing.fleshline.size())
				indexoclkindlineattrsecond = NSGAIIpostprocessing.leftsideattriteration.size();
			if (indexocliskineoflocation >= 0
					&& (indexocliskineoflocation + 1) != NSGAIIpostprocessing.fleshline.size())
				indexoclkindlineattrsecond = NSGAIIpostprocessing.leftsideattriteration
						.indexOf(Integer.toString(NSGAIIpostprocessing.fleshline.get(indexocliskineoflocation + 1)));

		}

		System.out.println("newlist");
		System.out.println(indexoclkindlineattr);
		System.out.println(indexoclkindlineattrsecond);
		System.out.println(listattrpriviousocl);
		for (int i = indexoclkindlineattr + 1; i < indexoclkindlineattrsecond; i++) {
			boolean addattrleft = false;
			System.out.println("ppppppp");
			// System.out.println(NSGAIIpostprocessing.leftsideattriteration.get(i));
			for (int j = 0; j < NSGAIIpostprocessing.listsourcemetamodel.size(); j++) {
				// System.out.println(NSGAIIpostprocessing.listsourcemetamodel.get(
				// j).getName());
				if ((NSGAIIpostprocessing.classnamestring.contains(NSGAIIpostprocessing.leftsideattriteration.get(i))
						|| NSGAIIpostprocessing.listsourcemetamodel.get(j).getName()
								.equals(NSGAIIpostprocessing.leftsideattriteration.get(i)))
						&& !listattrpriviousocl.contains(NSGAIIpostprocessing.leftsideattriteration.get(i)))

				{
					// this.numrun=this.numrun+1;
					String str = null;

					if (iditrlrtter >= 0) {
						str = NSGAIIpostprocessing.oclletter.get(iditrlrtter + 1);
					}
					System.out.println(str + "p");
					System.out.println(NSGAIIpostprocessing.leftsideattriteration.get(i) + "p");
					// System.out.println(stringCompare(str,
					// NSGAIIpostprocessing.leftsideattriteration.get(i)));
					// System.out.println(str.equals(NSGAIIpostprocessing.leftsideattriteration.get(i).toString()));
					if (!NSGAIIpostprocessing.leftsideattriteration.get(i).toString().equals(str)) {
						System.out.println("pl");
						addattrleft = true;
					}
				}

			}
			System.out.println(addattrleft);

			if (addattrleft == true)
				this.listattrnavigation.add(NSGAIIpostprocessing.leftsideattriteration.get(i));

		}

		System.out.println("lllllllll");
		System.out.println(this.listattrnavigation);

	}

	private void findallattributespecificoclkindofwithiteration(String[] array, EObject oldFeatureValue2) {
		// TODO Auto-generated method stub

		this.listattrnavigation = new ArrayList<String>();
		// attributhaye samte chap
		findallattributespecificoclliteration(array);
		// this.startcombinedattr=this.listattrnavigation.size();
		this.indexcorrectocl = -1;
		this.numrun = 0;
		this.findcorrectclass = false;

		// khati ke entekhab kard o dar oclkindlineattr peyda mikone
		int indexoclkindlineattr = NSGAIIpostprocessing.oclkindlineattr.indexOf(array[0]);
		// khati ke entekhab kard dar ocliskineoflocation peyda mikone
		int indexocliskineoflocation = NSGAIIpostprocessing.ocliskineoflocation.indexOf(Integer.parseInt(array[0]));
		// toye oclkindlineattr koja hamin ocl tamom mishe
		// int
		// indexoclkindlineattrsecond=NSGAIIpostprocessing.oclkindlineattr.indexOf(Integer.toString(NSGAIIpostprocessing.ocliskineoflocation.get(
		// indexocliskineoflocation+1)));
		int indexoclkindlineattrsecond = -1;
		if (indexoclkindlineattr >= 0) {
			// int indexoclkindlineattrsecond=-1;
			if ((indexocliskineoflocation + 1) == NSGAIIpostprocessing.ocliskineoflocation.size())
				indexoclkindlineattrsecond = NSGAIIpostprocessing.oclkindlineattr.size();
			if (indexocliskineoflocation >= 0
					&& (indexocliskineoflocation + 1) != NSGAIIpostprocessing.ocliskineoflocation.size())
				indexoclkindlineattrsecond = NSGAIIpostprocessing.oclkindlineattr.indexOf(
						Integer.toString(NSGAIIpostprocessing.ocliskineoflocation.get(indexocliskineoflocation + 1)));

		}

		System.out.println("ok");
		System.out.println(NSGAIIpostprocessing.oclkindlineattr);
		System.out.println(indexoclkindlineattr);
		System.out.println(Integer.parseInt(array[0]));
		System.out.println(indexoclkindlineattrsecond);
		int indexpreviousocl = -1;
		// jahaee ke dar oclkindlineattr toye hamin khat oclkindof darim moshakhas kon
		this.oclindex = new ArrayList<Integer>();
		this.oclindex.add(indexoclkindlineattr);
		// int indexcorrectocl=-1;
		// toye oclkindlineattr jaee ke in khat oclkind shoro mishe ta jaee ke tamom
		// mishe search kon
		// oclindex shamele index khate shoro index oclha index khat badi hast
		boolean check = false;
		this.indexcorrectocl = -1;
		this.checklistattr = (new ArrayList<String>());
		int idx = 0;
		String str = "";
		for (int i = indexoclkindlineattr + 1; i < indexoclkindlineattrsecond - 1; i++) {
			if (!NSGAIIpostprocessing.oclkindlineattr.get(i).toString().contains("oclIsKindOf"))
				str = str + NSGAIIpostprocessing.oclkindlineattr.get(i).toString();
			System.out.println("strzahra");
			System.out.println(str);

			if (NSGAIIpostprocessing.oclkindlineattr.get(i).toString().contains("oclIsKindOf")) {
				this.oclindex.add(i);
				System.out.println("oclindex");
				System.out.println(this.oclindex);
				System.out.println(i);
				System.out.println(this.checknotrepeat);
				System.out.println("oldreal");
				System.out.println(NSGAIIpostprocessing.oclkindlineattr);
				System.out.println(toString(oldFeatureValue2));
				System.out.println(NSGAIIpostprocessing.oclkindlineattr.get(i + 1).toString());
				str = str + NSGAIIpostprocessing.oclkindlineattr.get(i + 1).toString();
				this.checklistattr.add(str);
				System.out.println("str");
				System.out.println(str);
				System.out.println(this.checklistattr);
				str = "";
				// !this.checknotrepeat.contains(i )
				if (!this.checknotrepeat.contains(i)
						&& toString(oldFeatureValue2).equals(NSGAIIpostprocessing.oclkindlineattr.get(i + 1).toString())

				) {
					check = true;
					System.out.println("orek");
					System.out.println(i);
					this.indexcorrectocl = i;

				}
				i = i + 1;
			}
		}
		if (check == true) {
			this.oclindex.add(indexoclkindlineattrsecond);
			System.out.println("oclindex");
			System.out.println(this.oclindex);

			// int numrun=0;
			int indexoclindex = this.oclindex.indexOf(this.indexcorrectocl);
			// int start=0;
			if (indexoclindex - 1 == 0)
				this.start = this.oclindex.get(indexoclindex - 1);
			else
				this.start = this.oclindex.get(indexoclindex - 1) + 2;
		}
		System.out.println(this.start);
		System.out.println(this.indexcorrectocl);
		System.out.println("checkrightside");
		this.numrun = 0;

		for (int i = this.start; i < this.indexcorrectocl; i++) {
			boolean checkatt = false;
			for (int j = 0; j < NSGAIIpostprocessing.listsourcemetamodel.size(); j++) {

				if (NSGAIIpostprocessing.classnamestring.contains(NSGAIIpostprocessing.oclkindlineattr.get(i))
						|| NSGAIIpostprocessing.listsourcemetamodel.get(j).getName()
								.equals(NSGAIIpostprocessing.oclkindlineattr.get(i))) {
					// this.numrun=this.numrun+1;
					String str2 = null;

					int iditrlrtter = NSGAIIpostprocessing.oclletter
							.indexOf(Integer.toString((Integer.parseInt(array[0])) - 1));
					if (iditrlrtter >= 0) {
						str2 = NSGAIIpostprocessing.oclletter.get(iditrlrtter + 1);
					}
					System.out.println(str2 + "p");
					System.out.println(NSGAIIpostprocessing.oclkindlineattr.get(i) + "p");
					// System.out.println(stringCompare(str,
					// NSGAIIpostprocessing.leftsideattriteration.get(i)));
					System.out.println(str.equals(NSGAIIpostprocessing.oclkindlineattr.get(i).toString()));
					if (!NSGAIIpostprocessing.oclkindlineattr.get(i).toString().equals(str2)) {
						System.out.println("pl");
						checkatt = true;
						// addattrleft=true;
					}
				}
			}
			if (checkatt == true) {
				this.numrun = this.listattrnavigation.size() - 1;
				this.listattrnavigation.add(NSGAIIpostprocessing.oclkindlineattr.get(i));
				System.out.println(NSGAIIpostprocessing.oclkindlineattr.get(i));
			}

		}
		System.out.println("11startindex");
		System.out.println(this.checklistattr);
		System.out.println(this.numrun);
		System.out.println(this.listattrnavigation);
		System.out.println(NSGAIIpostprocessing.oclkindlineattr.get(this.start));

	}

	private void findallattributespecificoclkindof(String[] array, EObject oldFeatureValue2) {
		// TODO Auto-generated method stub
		this.listattrnavigation.clear();
		this.listattrnavigation = new ArrayList<String>();
		this.listattrforcheck.clear();
		this.listattrforcheck = new ArrayList<String>();

		findallattriflinebeforeoclandtwolinebeforeiteration(array);
		this.startcombinedattr = 0;
		this.indexcorrectocl = -1;
		// this.numrun=0;
		this.findcorrectclass = false;
		// khati ke entekhab kard o dar oclkindlineattr peyda mikone
		int indexoclkindlineattr = NSGAIIpostprocessing.oclkindlineattr.indexOf(array[0]);
		// khati ke entekhab kard dar ocliskineoflocation peyda mikone
		int indexocliskineoflocation = NSGAIIpostprocessing.ocliskineoflocation.indexOf(Integer.parseInt(array[0]));
		// toye oclkindlineattr koja hamin ocl tamom mishe
		// int
		// indexoclkindlineattrsecond=NSGAIIpostprocessing.oclkindlineattr.indexOf(Integer.toString(NSGAIIpostprocessing.ocliskineoflocation.get(
		// indexocliskineoflocation+1)));
		int indexoclkindlineattrsecond = -1;
		if (indexoclkindlineattr >= 0) {
			// array oclkindlineattr=[30, self, oclIsKindOf(UML, ExecutableNode, self,
			// oclIsKindOf(UML, InitialNode, self, incoming->isEmpty(, self,
			// oclIsKindOf(UML, InitialNode, self, incoming->exists(edge, |, 31, edge,
			// source, oclIsKindOf(UML, AcceptEventAction, 52, e, oclIsKindOf(UML,
			// ObjectNode, 73, edge, source, oclIsKindOf(UML, AcceptEventAction, 102, if,
			// edge, source, oclIsKindOf(UML, ProtocolTransition, then, 104, t, event,
			// oclIsKindOf(UML, TimeEvent, 123, if, edge, source, oclIsKindOf(UML,
			// AcceptEventAction, then, 125, t, event, oclIsKindOf(UML, TimeEvent]
			//
			// int indexoclkindlineattrsecond=-1;
			// indexocliskineoflocation jaee ke shoro mishe
			if ((indexocliskineoflocation + 1) == NSGAIIpostprocessing.ocliskineoflocation.size())
				indexoclkindlineattrsecond = NSGAIIpostprocessing.oclkindlineattr.size();
			// indexoclkindlineattrsecond jaee ke in khat tamom miske ex:73
			if (indexocliskineoflocation >= 0
					&& (indexocliskineoflocation + 1) != NSGAIIpostprocessing.ocliskineoflocation.size())
				indexoclkindlineattrsecond = NSGAIIpostprocessing.oclkindlineattr.indexOf(
						Integer.toString(NSGAIIpostprocessing.ocliskineoflocation.get(indexocliskineoflocation + 1)));

		}

		System.out.println("ok");
		System.out.println(NSGAIIpostprocessing.oclkindlineattr);
		System.out.println(indexoclkindlineattr);
		System.out.println(Integer.parseInt(array[0]));
		System.out.println(indexoclkindlineattrsecond);
		int indexpreviousocl = -1;
		// jahaee ke dar oclkindlineattr toye hamin khat oclkindof darim moshakhas kon
		this.oclindex = new ArrayList<Integer>();
		this.oclindex.add(indexoclkindlineattr);
		// int indexcorrectocl=-1;
		// toye oclkindlineattr jaee ke in khat oclkind shoro mishe ta jaee ke tamom
		// mishe search kon
		// oclindex shamele index khate shoro index oclha index khat badi hast
		boolean check = false;
		this.indexcorrectocl = -1;
		this.checklistattr = (new ArrayList<String>());
		ArrayList<Integer> indexsecond = (new ArrayList<Integer>());
		boolean attrcombined = false;
		String str = "";
		for (int i = indexoclkindlineattr + 1; i < indexoclkindlineattrsecond - 1; i++) {
			if (!NSGAIIpostprocessing.oclkindlineattr.get(i).toString().contains("oclIsKindOf")
					&& !NSGAIIpostprocessing.oclkindlineattr.get(i).toString().contains("oclAsType"))
				if (NSGAIIpostprocessing.oclkindlineattr.get(i).toString().length() == 1)
					indexsecond.add(i);
			str = str + NSGAIIpostprocessing.oclkindlineattr.get(i).toString();
			System.out.println("str");
			System.out.println(str);
			System.out.println(this.checklistattr);
			if (NSGAIIpostprocessing.oclkindlineattr.get(i).toString().contains("oclIsKindOf")
					|| NSGAIIpostprocessing.oclkindlineattr.get(i).toString().contains("oclAsType")) {
				this.oclindex.add(i);
				System.out.println("oclindex");
				System.out.println(this.oclindex);
				System.out.println(toString(oldFeatureValue2));
				str = str + NSGAIIpostprocessing.oclkindlineattr.get(i + 1).toString();
				this.checklistattr.add(str);
				System.out.println("str");
				System.out.println(str);
				System.out.println(this.checklistattr);
				str = "";

				if (toString(oldFeatureValue2).equals(NSGAIIpostprocessing.oclkindlineattr.get(i + 1).toString())
						&& !this.checknotrepeat.contains(i)) {
					check = true;

					System.out.println("orek");
					System.out.println(i);
					this.indexcorrectocl = i;

				}
				i = i + 1;
			}
		}

		// this.oclindex.add(indexoclkindlineattrsecond);
		System.out.println("oclindex");
		System.out.println(this.oclindex);

		// int numrun=0;
		if (check == true) {
			int indexoclindex = this.oclindex.indexOf(this.indexcorrectocl);
			// int start=0;
			if (indexoclindex - 1 == 0)
				this.start = this.oclindex.get(indexoclindex - 1) + 2;
			else
				this.start = this.oclindex.get(indexoclindex - 1) + 3;
		}

		System.out.println(this.start);
		System.out.println(this.indexcorrectocl);
		/*
		 * for(int i=this.start;i<this.indexcorrectocl;i++) { this.numrun=this.numrun+1;
		 * System.out.println(NSGAIIpostprocessing.oclkindlineattr.get(i)); }
		 */
		// this.numrun=0;
		for (int i = this.start; i < this.indexcorrectocl; i++) {
			boolean checkatt = false;
			for (int j = 0; j < NSGAIIpostprocessing.listsourcemetamodel.size(); j++) {

				if (NSGAIIpostprocessing.classnamestring.contains(NSGAIIpostprocessing.oclkindlineattr.get(i))
						|| NSGAIIpostprocessing.listsourcemetamodel.get(j).getName()
								.equals(NSGAIIpostprocessing.oclkindlineattr.get(i))) {
					// add 2020/13/06
					int iditrlrtter = NSGAIIpostprocessing.oclletter
							.indexOf(Integer.toString((Integer.parseInt(array[0])) - 2));
					String str2 = null;
					if (iditrlrtter >= 0) {
						str2 = NSGAIIpostprocessing.oclletter.get(iditrlrtter + 1);
					}
					// this.numrun=this.numrun+1;
					if (!NSGAIIpostprocessing.oclkindlineattr.get(i).toString().equals(str2) && !this.listattrnavigation
							.contains(NSGAIIpostprocessing.oclkindlineattr.get(i).toString())) {

						checkatt = true;
					}
				}
			}
			if (checkatt == true) {
				this.numrun = this.listattrnavigation.size() - 1;
				this.listattrnavigation.add(NSGAIIpostprocessing.oclkindlineattr.get(i));
				System.out.println(NSGAIIpostprocessing.oclkindlineattr.get(i));
			}

		}

		if (this.indexcorrectocl >= 0) {
			for (int k = 0; k < indexsecond.size(); k++) {
				if (this.indexcorrectocl < indexsecond.get(k) && (indexsecond.get(k)
						- this.indexcorrectocl < indexoclkindlineattrsecond - this.indexcorrectocl))
					indexoclkindlineattrsecond = indexsecond.get(k);
			}
			for (int i = this.indexcorrectocl; i < indexoclkindlineattrsecond - 1; i++) {
				for (int j = 0; j < NSGAIIpostprocessing.listsourcemetamodel.size(); j++) {
					System.out.println(NSGAIIpostprocessing.listsourcemetamodel.get(j).getName());
					System.out.println(NSGAIIpostprocessing.oclkindlineattr.get(i));
					if (NSGAIIpostprocessing.listsourcemetamodel.get(j).getName()
							.equals(NSGAIIpostprocessing.oclkindlineattr.get(i))) {
						this.listattrforcheck.add(NSGAIIpostprocessing.oclkindlineattr.get(i));
					}
				}

			}
		}
		this.numrun = this.listattrnavigation.size() - 1;
		System.out.println("11startindex");
		System.out.println(this.listattrforcheck);
		System.out.println(this.listattrnavigation);
		System.out.println(this.checklistattr);
		System.out.println(this.numrun);
		System.out.println(NSGAIIpostprocessing.oclkindlineattr.get(this.start));

	}

	private <ToModify extends LocatedElement> List<Object> CheckInheritanceinsideoclandclass(String typenavigation,
			EObject oldFeatureValue2, boolean findreplace, boolean perivoustype, boolean checkmutationapply,
			List<EObject> value2, EDataTypeEList<String> comments, List<ToModify> modifiable, String comment,
			List<EObject> replacements, int randomInt4, int randomInt, int randomInt3, int indexrule, ATLModel wrapper,
			EMFModel atlModel, List<Object> ReturnResult, String[] array) {
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

		EClass childo = null;
		EClass child2o = null;
		EClass child3o = null;

		// find inside ocl in list of classifier
		for (EClassifier classifier : metatarget.getEClassifiers()) {

			if (classifier instanceof EClass) {

				EClass child = ((EClass) classifier);

				if (child.getName().equals(typenavigation)) {
					childo = child;

					System.out.println("1Aiiiiiiiiiiii");
					System.out.println(typenavigation);

				}
			}
		}

		boolean correctattr = false;
		boolean correctattrnew = false;
		boolean attrerror = false;
		for (EClassifier classifier2 : metatarget.getEClassifiers()) {
			if (classifier2 instanceof EClass) {

				EClass child2 = ((EClass) classifier2);
				EClass child3 = ((EClass) classifier2);
				if (child2.getName().equals(toString(oldFeatureValue2))) {
					child2o = child2;
					System.out.println("2Aiiiiiiiiiiii");
					System.out.println(toString(oldFeatureValue2));
					// agar in rule joz rule hast ke attr ocl ke error dare tosh hast
					// attrerror=true;
				}

				if (child3.getName().equals(toString(replacements.get(randomInt4)))) {
					child3o = child3;
					System.out.println("3Aiiiiiiiiiiii");
					System.out.println(toString(replacements.get(randomInt4)));
					// correctattrnew=true;// System.out.println("oooiiiiiiiiiiii");

				}

			}

		}

		boolean checkattr = false;
		boolean checkattr2 = false;
		checkattr = Chechaccessattributepreiviousargument(oldFeatureValue2, checkattr);
		checkattr2 = Chechaccessattributepreiviousargument(replacements.get(randomInt4), checkattr2);
		if (this.listattrforcheck.size() == 0)
			checkattr = true;

		if (childo != null && child2o != null)
			if ((childo.isSuperTypeOf(child2o) && checkattr == true)) {

				findreplace = true;
				perivoustype = true;
				checkmutationapply = true;
				this.findcorrectclass = true;
				this.listrandomInt.add(randomInt);
				this.checknotrepeat.add(this.indexcorrectocl);
				this.listoldclass.add(toString(oldFeatureValue2));
				this.alloclrule.add(Integer.parseInt(array[0]));
				System.out.println("\n-- MUTATION \"" + this.getDescription() + "\" from "
						+ toString(modifiable.get(randomInt)) + ":" + toString(oldFeatureValue2) + " to "
						+ toString(modifiable.get(randomInt)) + ":" + toString(replacements.get(randomInt4)) + " (line "
						+ modifiable.get(randomInt).getLocation() + " of original transformation)\n");

				// correctattrnew=true;
				System.out.println("notchange");
				System.out.println("y1");

			}
		if (childo != null && child3o != null)
			if ((childo.isSuperTypeOf(child3o) && perivoustype == false)
					&& !toString(replacements.get(randomInt4)).equals(typenavigation) && checkattr2 == true) {
				this.listrandomInt.add(randomInt);
				this.checknotrepeat.add(this.indexcorrectocl);
				this.alloclrule.add(Integer.parseInt(array[0]));
				this.listoldclass.add(toString(replacements.get(randomInt4)));
				System.out.println("y2");
				// perivoustype=false;
				findreplace = true;
				checkmutationapply = true;
				this.findcorrectclass = true;
				correctattrnew = true;

			}
		// toString(replacements.get(randomInt4)).equals( typenavigation )
		if (childo == null || child2o == null || child3o == null || this.repeatargument == true) {
			System.out.println("y3");
			this.listoldclass.add(toString(oldFeatureValue2));
			this.listrandomInt.add(randomInt);
			this.checknotrepeat.add(this.indexcorrectocl);
			this.alloclrule.add(Integer.parseInt(array[0]));

		}
		// if (correctattr==false && correctattrnew==false)
		// findreplace=false;
		System.out.println("randomint");
		System.out.println(checkattr);
		System.out.println(checkattr2);
		System.out.println(this.listrandomInt);
		System.out.println(this.checknotrepeat);
		System.out.println(this.alloclrule);
		System.out.println(this.listoldclass);
		ReturnResult = Applynewoperationandaddcomments(perivoustype, indexrule, replacements, randomInt4, randomInt,
				randomInt3, oldFeatureValue2, value2, comments, comment, modifiable, wrapper, atlModel, ReturnResult,
				checkmutationapply, correctattrnew);

		return ReturnResult;

	}

	private boolean Chechaccessattributepreiviousargument(EObject oldFeatureValue2, boolean checkattr) {
		// TODO Auto-generated method stub

		int indexclassname = NSGAIIpostprocessing.classnamestring.indexOf(toString(oldFeatureValue2));
		for (int i = NSGAIIpostprocessing.classnamestartpoint2
				.get(indexclassname); i < NSGAIIpostprocessing.classnamestartpoint2.get(indexclassname)
						+ NSGAIIpostprocessing.classnamelength2.get(indexclassname); i++) {
			System.out.println("kor1");
			System.out.println(this.listattrforcheck);
			System.out.println(NSGAIIpostprocessing.listsourcemetamodel.get(i).getName());
			for (int j = 0; j < this.listattrforcheck.size(); j++) {
				// hameye attributha ke in class dare check kon

				if (NSGAIIpostprocessing.listsourcemetamodel.get(i).getName().contains(this.listattrforcheck.get(j))) {
					// check=true;
					// System.out.println("roz");
					// typenavigation=NSGAIIpostprocessing.listnavigationtypeinheritattr.get(i);
					System.out.println("kor");
					checkattr = true;

				}
			}
		}
		if (this.listattrforcheck.size() == 0)
			checkattr = true;
		return checkattr;

	}

	private <ToModify extends LocatedElement> List<Object> Applynewoperationandaddcomments(boolean perivoustype,
			int indexrule, List<EObject> replacements, int randomInt4, int randomInt, int randomInt3,
			EObject oldFeatureValue2, List<EObject> value2, EDataTypeEList<String> comments, String comment,
			List<ToModify> modifiable, ATLModel wrapper, EMFModel atlModel, List<Object> ReturnResult,
			boolean checkmutationapply, boolean correctattrnew) {
		// TODO Auto-generated method stub
		if (correctattrnew == true) {
			NSGAII.argumentlist.add(String.valueOf(indexrule));
			NSGAII.argumentlist.add(toString(replacements.get(randomInt4)));
			setvariable(randomInt, randomInt4, randomInt3);
			copyFeatures(oldFeatureValue2, replacements.get(randomInt4));
			// NSGAII.writer.println(Operations.statecase);
			value2.set(randomInt3, replacements.get(randomInt4));

			// if (comments != null)
			comments.add("\n-- MUTATION \"" + this.getDescription() + "\" from " + toString(modifiable.get(randomInt))
					+ ":" + toString(oldFeatureValue2) + " to " + toString(modifiable.get(randomInt)) + ":"
					+ toString(replacements.get(randomInt4)) + " (line " + modifiable.get(randomInt).getLocation()
					+ " of original transformation)\n");
			System.out.println("\n-- MUTATION \"" + this.getDescription() + "\" from "
					+ toString(modifiable.get(randomInt)) + ":" + toString(oldFeatureValue2) + " to "
					+ toString(modifiable.get(randomInt)) + ":" + toString(replacements.get(randomInt4)) + " (line "
					+ modifiable.get(randomInt).getLocation() + " of original transformation)\n");
			comment = "\n-- MUTATION \"" + this.getDescription() + "\" from " + toString(modifiable.get(randomInt))
					+ ":" + toString(oldFeatureValue2) + " to " + toString(modifiable.get(randomInt)) + ":"
					+ toString(replacements.get(randomInt4)) + " (line " + modifiable.get(randomInt).getLocation()
					+ " of original transformation)\n";
			NSGAII.numop = NSGAII.numop + 1;
			NSGAIIpostprocessing.oldargumenttype.add(Integer.toString(this.indexcorrectocl + 1));
			NSGAIIpostprocessing.oldargumenttype.add(toString(oldFeatureValue2));
			NSGAIIpostprocessing.oclkindlineattr.set(this.indexcorrectocl + 1, toString(replacements.get(randomInt4)));
			checkmutationapply = true;
			ReturnResult.set(0, wrapper);
			ReturnResult.set(1, atlModel);
			ReturnResult.add(comment);
		}

		return ReturnResult;
	}

	private String getType(EObject container, VariableExp containee, MetaModel inputMM, MetaModel outputMM) {
		// TODO Auto-generated method stub

		EClassifier c = null;
		VariableDeclaration def = containee.getReferredVariable();

		// obtain type (classifier) of variable expression
		// ..............................
		// case 1 -> in pattern element
		if (def != null) {
			if (def instanceof InPatternElement) {

				c = inputMM.getEClassifier(def.getType().getName());
				// System.out.println(c);
			}
			// case 2 -> for each out pattern element
			else if (def instanceof ForEachOutPatternElement) {
				// System.out.println("2222222222222");
				def = ((ForEachOutPatternElement) def).getIterator();
				if (def.eContainer() instanceof OutPatternElement) {
					OutPatternElement element = (OutPatternElement) def.eContainer();
					if (element.getType() instanceof OclModelElement)
						c = outputMM.getEClassifier(((OclModelElement) element.getType()).getName());
				}
			}
			// case 3 -> iterator
			else if (def instanceof Iterator) {
				// System.out.println("3333333333333");
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
				// System.out.println(c);
			}
			// case 4 -> variable declaration
			else {
				if (toString(def).equals("self")) {
					// System.out.println("4444444444");
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
					// System.out.println("5555555555");
					c = inputMM.getEClassifier(((VariableDeclaration) def).getType().getName());
				} else if (((VariableDeclaration) def).getType() instanceof CollectionType) {
					// System.out.println("6666666666");
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

	private <ToModify extends LocatedElement> List<Object> OperationPreviousGeneration(int randomInt, Solution solution,
			EMFModel atlModel, MetaModel[] metamodels, List<ToModify> modifiable, ATLModel wrapper, String featureName,
			EDataTypeEList<String> comments, List<Object> ReturnResult, MetaModel metamodel1, MetaModel metamodel2) {
		// TODO Auto-generated method stub

		ToModify modifiable2 = modifiable.get(randomInt);
		String[] array = modifiable2.getLocation().split(":", 2);
		if (Operations.statecase.equals("case7")) {
			if (!solution.listlineofcodes.contains(Integer.parseInt(array[0]))) {
				ReturnResult = ApplyFromPreviousCase7(randomInt, solution, atlModel, metamodels, modifiable2, wrapper,
						featureName, comments, ReturnResult);
			} else {
				setvariable(-2, -2, -2);
			}
		} else {

			ReturnResult = ApplyFromPreviousOtherCase(randomInt, solution, atlModel, metamodels, modifiable2, wrapper,
					featureName, comments, ReturnResult, metamodel1, metamodel2, array);

		}
		return ReturnResult;
	}

	private <ToModify extends LocatedElement> List<Object> ApplyFromPreviousOtherCase(int randomInt, Solution solution,
			EMFModel atlModel, MetaModel[] metamodels, ToModify modifiable2, ATLModel wrapper, String featureName,
			EDataTypeEList<String> comments, List<Object> ReturnResult, MetaModel metamodel1, MetaModel metamodel2,
			String[] array) {
		// NSGAII.writer.println("after crossover case dovom");
		List<String> liststringiniteration = new ArrayList<String>();
		if (Operations.statecase.equals("case6")) {
			List<VariableExp> variables = (List<VariableExp>) wrapper.allObjectsOf(VariableExp.class);

			for (int i = 0; i < variables.size(); i++) {
				EObject navigationExpression = variables.get(i).eContainer();
				if (navigationExpression instanceof NavigationOrAttributeCallExp) {
					EStructuralFeature featureDefinition2 = wrapper.source(navigationExpression).eClass()
							.getEStructuralFeature("name");
					Object object2modify_src2 = wrapper.source(navigationExpression).eGet(featureDefinition2);
					String[] array2 = variables.get(i).getLocation().split(":", 2);

					String type = getType(navigationExpression, variables.get(i), metamodel1, metamodel2);

					int indexrule = -1;
					for (int j = 0; j < NSGAII.faultrule.size(); j++) {
						if (Integer.parseInt(array[0]) > NSGAII.faultrule.get(j)
								&& Integer.parseInt(array[0]) < NSGAII.finalrule.get(j))

							indexrule = j;

					}
					if (type != null) {

						// list attribute ke ro khat iteration hast va toye in rule hast begir chon roye
						// khat iteration hast pas cardinality * dare
						if (NSGAII.iterationcall.contains(Integer.parseInt(array2[0]))
								&& Integer.parseInt(array2[0]) < NSGAII.finalrule.get(indexrule)
								&& Integer.parseInt(array2[0]) > NSGAII.faultrule.get(indexrule)) {
							liststringiniteration.add(object2modify_src2.toString());

						}

					}

				}
			}
		}
		String comment = null;
		EStructuralFeature featureDefinition = wrapper.source(modifiable2).eClass().getEStructuralFeature(featureName);
		EObject object2modify_src = wrapper.source(modifiable2);
		EObject oldFeatureValue = (EObject) object2modify_src.eGet(featureDefinition);
		List<EObject> replacements = this.replacements(atlModel, (EObject) oldFeatureValue, metamodels);
		int randomInt2 = -2;
		randomInt2 = FindSecondIndex(randomInt2, replacements.size());
		boolean check = false;
		if (liststringiniteration.size() > 0) {
			// to arraye ke list classha hast esme inpattern ra toye list peyda kon ba
			// indexesh
			int indexclassname = NSGAII.classnamestring.indexOf(toString(replacements.get(randomInt2)));

			for (int j = 0; j < liststringiniteration.size(); j++) {
				for (int i = NSGAII.classnamestartpoint.get(indexclassname); i < NSGAII.classnamestartpoint
						.get(indexclassname) + NSGAII.classnamelength.get(indexclassname); i++) {
					if (NSGAII.listinheritattributesourcemetamodel.get(i).getName().equals(liststringiniteration.get(j))
							&& NSGAII.listinheritattributesourcemetamodel.get(i).getUpperBound() == 1) {
						setvariable(-2, -2, -2);

						check = true;
					}
				}

			}

		}
		if (check == false) {
			if (!oldFeatureValue.toString().equals(replacements.get(randomInt2).toString())) {
				copyFeatures(oldFeatureValue, replacements.get(randomInt2));
				StoreTwoIndex(randomInt, randomInt2);
				object2modify_src.eSet(featureDefinition, replacements.get(randomInt2));
				// NSGAII.writer.println(Operations.statecase);
				// NSGAII.writer.println(randomInt);
				// NSGAII.writer.println(randomInt2);

				comments.add("\n-- MUTATION \"" + this.getDescription() + "\" from " + toString(modifiable2) + ":"
						+ toString(oldFeatureValue) + " to " + toString(modifiable2) + ":"
						+ toString(replacements.get(randomInt2)) + " (line " + modifiable2.getLocation()
						+ " of original transformation)\n");
				comment = "\n-- MUTATION \"" + this.getDescription() + "\" from " + toString(modifiable2) + ":"
						+ toString(oldFeatureValue) + " to " + toString(modifiable2) + ":"
						+ toString(replacements.get(randomInt2)) + " (line " + modifiable2.getLocation()
						+ " of original transformation)\n";
				System.out.println("\n-- MUTATION \"" + this.getDescription() + "\" from " + toString(modifiable2) + ":"
						+ toString(oldFeatureValue) + " to " + toString(modifiable2) + ":"
						+ toString(replacements.get(randomInt2)) + " (line " + modifiable2.getLocation()
						+ " of original transformation)\n");

				NSGAII.numop = NSGAII.numop + 1;
				// checkmutationapply = true;
				ReturnResult.set(0, wrapper);
				ReturnResult.set(1, atlModel);
				ReturnResult.add(comment);
			} else {
				StoreTwoIndex(randomInt, randomInt2);
			}
		}

		// TODO Auto-generated method stub
		return ReturnResult;
	}

	private <ToModify extends LocatedElement> List<Object> ApplyFromPreviousCase7(int randomInt, Solution solution,
			EMFModel atlModel, MetaModel[] metamodels, ToModify modifiable2, ATLModel wrapper, String featureName,
			EDataTypeEList<String> comments, List<Object> ReturnResult) {
		// TODO Auto-generated method stub
		// NSGAII.writer.println("after crossover" );
		// System.out.println("outargument");
		String comment = null;
		EStructuralFeature featureDefinition = wrapper.source(modifiable2).eClass().getEStructuralFeature(featureName);
		List<EObject> value = (List<EObject>) wrapper.source(modifiable2).eGet(featureDefinition);
		int randomInt3 = calculaterandomInt2(value);
		EObject oldFeatureValue2 = value.get(randomInt3);
		// System.out.println(oldFeatureValue2.toString());
		List<EObject> replacements = this.replacements(atlModel, (EObject) oldFeatureValue2, metamodels);
		int randomInt4 = calculaterandomInt(replacements);
		// NSGAII.argumentlist.add(String.valueOf(indexrule));
		// NSGAII.argumentlist.add(toString(replacements.get(randomInt4)));
		setvariable(randomInt, randomInt4, randomInt3);
		if (!oldFeatureValue2.toString().equals(replacements.get(randomInt4).toString())) {
			copyFeatures(oldFeatureValue2, replacements.get(randomInt4));
			// NSGAII.writer.println(Operations.statecase);
			// NSGAII.writer.println(randomInt);
			// NSGAII.writer.println(randomInt4);
			// NSGAII.writer.println(randomInt3);
			value.set(randomInt3, replacements.get(randomInt4));
			// if (comments != null)
			comments.add("\n-- MUTATION \"" + this.getDescription() + "\" from " + toString(modifiable2) + ":"
					+ toString(oldFeatureValue2) + " to " + toString(modifiable2) + ":"
					+ toString(replacements.get(randomInt4)) + " (line " + modifiable2.getLocation()
					+ " of original transformation)\n");
			comment = "\n-- MUTATION \"" + this.getDescription() + "\" from " + toString(modifiable2) + ":"
					+ toString(oldFeatureValue2) + " to " + toString(modifiable2) + ":"
					+ toString(replacements.get(randomInt4)) + " (line " + modifiable2.getLocation()
					+ " of original transformation)\n";
			System.out.println("\n-- MUTATION \"" + this.getDescription() + "\" from " + toString(modifiable2) + ":"
					+ toString(oldFeatureValue2) + " to " + toString(modifiable2) + ":"
					+ toString(replacements.get(randomInt4)) + " (line " + modifiable2.getLocation()
					+ " of original transformation)\n");
			NSGAII.numop = NSGAII.numop + 1;
			// checkmutationapply = true;
			ReturnResult.set(0, wrapper);
			ReturnResult.set(1, atlModel);
			ReturnResult.add(comment);

		}
		return ReturnResult;
	}

	/*
	*/
	/*
	 * ELSE if
	 * 
	 * List<EObject> value = (List<EObject>)
	 * wrapper.source(modifiable.get(randomInt)) .eGet(featureDefinition); // int
	 * randomInt3=(int) (Math.random() * (value.size()));
	 * NSGAII.writer.println("second if"); NSGAII.writer.println(value);
	 * 
	 * // for (int i=0; i<value.size(); i++) { if (value.size() > 0) { int
	 * randomInt3 = calculaterandomInt2(value); NSGAII.writer.println(randomInt3);
	 * // EObject oldFeatureValue = value.get(i); EObject oldFeatureValue2 =
	 * value.get(randomInt3); List<EObject> replacements =
	 * this.replacements(atlModel, (EObject) oldFeatureValue2, metamodels);
	 * NSGAII.writer.println("replacement"); NSGAII.writer.println(replacements); //
	 * System.out.println(replacements); boolean checkargu = false; if
	 * (replacements.size() > 0) { //
	 * System.out.println(toString(oldFeatureValue2)); for (int i = 0; i <
	 * replacements.size(); i++) { //
	 * System.out.println(toString(replacements.get(i))); if
	 * (toString(oldFeatureValue2).equals(toString(replacements.get(i)))) {
	 * checkargu = true; } // System.out.println(checkargu);
	 * 
	 * }
	 * 
	 * int randomInt4 = calculaterandomInt(replacements);
	 * NSGAII.argumentlist.add(String.valueOf(indexrule));
	 * NSGAII.argumentlist.add(toString(replacements.get(randomInt4)));
	 * setvariable(randomInt, randomInt4, randomInt3);
	 * copyFeatures(oldFeatureValue2, replacements.get(randomInt4));
	 * NSGAII.writer.println("case2"); NSGAII.writer.println(Operations.statecase);
	 * value.set(randomInt3, replacements.get(randomInt4)); if (comments != null)
	 * comments.add("\n-- MUTATION \"" + this.getDescription() + "\" from " +
	 * toString(modifiable.get(randomInt)) + ":" + toString(oldFeatureValue2) +
	 * " to " + toString(modifiable.get(randomInt)) + ":" +
	 * toString(replacements.get(randomInt4)) + " (line " +
	 * modifiable.get(randomInt).getLocation() + " of original transformation)\n");
	 * comment = "\n-- MUTATION \"" + this.getDescription() + "\" from " +
	 * toString(modifiable.get(randomInt)) + ":" + toString(oldFeatureValue2) +
	 * " to " + toString(modifiable.get(randomInt)) + ":" +
	 * toString(replacements.get(randomInt4)) + " (line " +
	 * modifiable.get(randomInt).getLocation() + " of original transformation)\n";
	 * // this.save(this.atlModel3, outputFolder); this.save2(atlModel,
	 * outputFolder); this.save2(atlModel,
	 * "C:/Users/varaminz/Downloads/Project/workspaceTEST34/anatlyzer.atl.tests.api/trafo2/zahra2cover"
	 * ); NSGAII.numop = NSGAII.numop + 1; checkmutationapply = true;
	 * wrapper.updateresource(atlModel.getResource()); //
	 * wrapper.setResource(atlModel.getResource()); ReturnResult.set(0, wrapper);
	 * 
	 * }
	 * 
	 * }
	 * 
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
					// NSGAII.writer.println("Word is at position " + indexfound + " on line " +
					// linecount);
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
					// NSGAII.writer.println(collectionlocation2);

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

		if (Operationspostprocessing.statecase.equals("case1") || Operationspostprocessing.statecase.equals("case6")
				|| Operationspostprocessing.statecase.equals("case4")
				|| Operationspostprocessing.statecase.equals("case9")) {
			switch (MyProblempostprocessing.indexoperation) {
			case 1:
				MyProblempostprocessing.oldoperation1 = randomInt;
				MyProblempostprocessing.replaceoperation1 = randomInt2;

				break;
			case 2:
				MyProblempostprocessing.oldoperation2 = randomInt;
				MyProblempostprocessing.replaceoperation2 = randomInt2;

				break;
			case 3:
				MyProblempostprocessing.oldoperation3 = randomInt;
				MyProblempostprocessing.replaceoperation3 = randomInt2;

				break;
			case 4:
				MyProblempostprocessing.oldoperation4 = randomInt;
				MyProblempostprocessing.replaceoperation4 = randomInt2;

				break;
			case 5:
				MyProblempostprocessing.oldoperation5 = randomInt;
				MyProblempostprocessing.replaceoperation5 = randomInt2;

				break;
			case 6:
				MyProblempostprocessing.oldoperation6 = randomInt;
				MyProblempostprocessing.replaceoperation6 = randomInt2;

				break;
			case 7:
				MyProblempostprocessing.oldoperation7 = randomInt;
				MyProblempostprocessing.replaceoperation7 = randomInt2;

				break;
			case 8:
				MyProblempostprocessing.oldoperation8 = randomInt;
				MyProblempostprocessing.replaceoperation8 = randomInt2;

				break;
			case 9:
				MyProblempostprocessing.oldoperation9 = randomInt;
				MyProblempostprocessing.replaceoperation9 = randomInt2;

				break;
			case 10:
				MyProblempostprocessing.oldoperation10 = randomInt;
				MyProblempostprocessing.replaceoperation10 = randomInt2;

				break;
			case 11:
				MyProblempostprocessing.oldoperation11 = randomInt;
				MyProblempostprocessing.replaceoperation11 = randomInt2;

				break;
			case 12:
				MyProblempostprocessing.oldoperation12 = randomInt;
				MyProblempostprocessing.replaceoperation12 = randomInt2;

				break;
			case 13:
				MyProblempostprocessing.oldoperation13 = randomInt;
				MyProblempostprocessing.replaceoperation13 = randomInt2;

				break;
			case 14:
				MyProblempostprocessing.oldoperation14 = randomInt;
				MyProblempostprocessing.replaceoperation14 = randomInt2;

				break;
			case 15:
				MyProblempostprocessing.oldoperation15 = randomInt;
				MyProblempostprocessing.replaceoperation15 = randomInt2;

				break;
			case 16:
				MyProblempostprocessing.oldoperation16 = randomInt;
				MyProblempostprocessing.replaceoperation16 = randomInt2;

				break;
			case 17:
				MyProblempostprocessing.oldoperation17 = randomInt;
				MyProblempostprocessing.replaceoperation17 = randomInt2;

				break;
			case 18:
				MyProblempostprocessing.oldoperation18 = randomInt;
				MyProblempostprocessing.replaceoperation18 = randomInt2;

				break;
			case 19:
				MyProblempostprocessing.oldoperation19 = randomInt;
				MyProblempostprocessing.replaceoperation19 = randomInt2;

				break;
			case 20:
				MyProblempostprocessing.oldoperation20 = randomInt;
				MyProblempostprocessing.replaceoperation20 = randomInt2;

				break;
			case 21:
				MyProblempostprocessing.oldoperation21 = randomInt;
				MyProblempostprocessing.replaceoperation21 = randomInt2;

				break;
			case 22:
				MyProblempostprocessing.oldoperation22 = randomInt;
				MyProblempostprocessing.replaceoperation22 = randomInt2;

				break;
			}

		}

	}

	private int FindSecondIndex(int randomInt2, int size) {
		// TODO Auto-generated method stub
		/*
		 * if (NSGAII.statemutcrossinitial.equals("mutation")) {
		 * 
		 * if (Operations.statecase.equals("case1") ||
		 * Operations.statecase.equals("case6") || Operations.statecase.equals("case4"))
		 * {
		 * 
		 * switch (MyProblem.indexoperation) { case 1: if (MyProblem.replaceoperation1
		 * != -1) randomInt2 = (int) (MyProblem.replaceoperation1); else { Random
		 * number_generator = new Random(); randomInt2 = number_generator.nextInt(size);
		 * 
		 * }
		 * 
		 * 
		 * break; case 2: if (MyProblem.replaceoperation2 != -1) randomInt2 = (int)
		 * (MyProblem.replaceoperation2); else { Random number_generator = new Random();
		 * randomInt2 = number_generator.nextInt(size);
		 * 
		 * }
		 * 
		 * 
		 * break; case 3: if (MyProblem.replaceoperation3 != -1) randomInt2 = (int)
		 * (MyProblem.replaceoperation3); else { Random number_generator = new Random();
		 * randomInt2 = number_generator.nextInt(size);
		 * 
		 * }
		 * 
		 * 
		 * break; case 4: if (MyProblem.replaceoperation4 != -1) randomInt2 = (int)
		 * (MyProblem.replaceoperation4); else { Random number_generator = new Random();
		 * randomInt2 = number_generator.nextInt(size);
		 * 
		 * }
		 * 
		 * 
		 * break; case 5: if (MyProblem.replaceoperation5 != -1) randomInt2 = (int)
		 * (MyProblem.replaceoperation5); else { Random number_generator = new Random();
		 * randomInt2 = number_generator.nextInt(size);
		 * 
		 * }
		 * 
		 * 
		 * break; case 6: if (MyProblem.replaceoperation6 != -1) randomInt2 = (int)
		 * (MyProblem.replaceoperation6); else { Random number_generator = new Random();
		 * randomInt2 = number_generator.nextInt(size);
		 * 
		 * }
		 * 
		 * 
		 * break; case 7: if (MyProblem.replaceoperation7 != -1) randomInt2 = (int)
		 * (MyProblem.replaceoperation7); else { Random number_generator = new Random();
		 * randomInt2 = number_generator.nextInt(size);
		 * 
		 * }
		 * 
		 * 
		 * break; case 8: if (MyProblem.replaceoperation8 != -1) randomInt2 = (int)
		 * (MyProblem.replaceoperation8); else { Random number_generator = new Random();
		 * randomInt2 = number_generator.nextInt(size);
		 * 
		 * }
		 * 
		 * 
		 * break; case 9: if (MyProblem.replaceoperation9 != -1) randomInt2 = (int)
		 * (MyProblem.replaceoperation9); else { Random number_generator = new Random();
		 * randomInt2 = number_generator.nextInt(size);
		 * 
		 * }
		 * 
		 * 
		 * break; case 10: if (MyProblem.replaceoperation10 != -1) randomInt2 = (int)
		 * (MyProblem.replaceoperation10); else { Random number_generator = new
		 * Random(); randomInt2 = number_generator.nextInt(size);
		 * 
		 * }
		 * 
		 * 
		 * break; case 11: if (MyProblem.replaceoperation11 != -1) randomInt2 = (int)
		 * (MyProblem.replaceoperation11); else { Random number_generator = new
		 * Random(); randomInt2 = number_generator.nextInt(size);
		 * 
		 * }
		 * 
		 * 
		 * break; case 12: if (MyProblem.replaceoperation12 != -1) randomInt2 = (int)
		 * (MyProblem.replaceoperation12); else { Random number_generator = new
		 * Random(); randomInt2 = number_generator.nextInt(size);
		 * 
		 * }
		 * 
		 * 
		 * break; case 13: if (MyProblem.replaceoperation13 != -1) randomInt2 = (int)
		 * (MyProblem.replaceoperation13); else { Random number_generator = new
		 * Random(); randomInt2 = number_generator.nextInt(size);
		 * 
		 * }
		 * 
		 * 
		 * break; case 14: if (MyProblem.replaceoperation14 != -1) randomInt2 = (int)
		 * (MyProblem.replaceoperation14); else { Random number_generator = new
		 * Random(); randomInt2 = number_generator.nextInt(size);
		 * 
		 * }
		 * 
		 * 
		 * break; case 15: if (MyProblem.replaceoperation15 != -1) randomInt2 = (int)
		 * (MyProblem.replaceoperation15); else { Random number_generator = new
		 * Random(); randomInt2 = number_generator.nextInt(size);
		 * 
		 * }
		 * 
		 * break; case 16: if (MyProblem.replaceoperation16 != -1) randomInt2 = (int)
		 * (MyProblem.replaceoperation16); else { Random number_generator = new
		 * Random(); randomInt2 = number_generator.nextInt(size);
		 * 
		 * }
		 * 
		 * break; case 17: if (MyProblem.replaceoperation17 != -1) randomInt2 = (int)
		 * (MyProblem.replaceoperation17); else { Random number_generator = new
		 * Random(); randomInt2 = number_generator.nextInt(size);
		 * 
		 * }
		 * 
		 * break; case 18: if (MyProblem.replaceoperation18 != -1) randomInt2 = (int)
		 * (MyProblem.replaceoperation18); else { Random number_generator = new
		 * Random(); randomInt2 = number_generator.nextInt(size);
		 * 
		 * }
		 * 
		 * break; case 19: if (MyProblem.replaceoperation19 != -1) randomInt2 = (int)
		 * (MyProblem.replaceoperation19); else { Random number_generator = new
		 * Random(); randomInt2 = number_generator.nextInt(size);
		 * 
		 * }
		 * 
		 * break; case 20: if (MyProblem.replaceoperation20 != -1) randomInt2 = (int)
		 * (MyProblem.replaceoperation20); else { Random number_generator = new
		 * Random(); randomInt2 = number_generator.nextInt(size);
		 * 
		 * }
		 * 
		 * break; case 21: if (MyProblem.replaceoperation21 != -1) randomInt2 = (int)
		 * (MyProblem.replaceoperation21); else { Random number_generator = new
		 * Random(); randomInt2 = number_generator.nextInt(size);
		 * 
		 * }
		 * 
		 * break; case 22: if (MyProblem.replaceoperation22 != -1) randomInt2 = (int)
		 * (MyProblem.replaceoperation22); else { Random number_generator = new
		 * Random(); randomInt2 = number_generator.nextInt(size);
		 * 
		 * }
		 * 
		 * break;
		 * 
		 * }
		 * 
		 * }
		 * 
		 * }
		 */// else {
		Random number_generator = new Random();
		randomInt2 = number_generator.nextInt(size);
		// randomInt2=(int) (Math.random() * (replacements.size()-1));
		// }
		return randomInt2;

	}

	private int[] something() {
		int number1 = 1;
		int number2 = 2;
		return new int[] { number1, number2 };
	}

	private List<Integer> ReturnFirstIndex(int randomInt, int size, boolean checkmutationapply, int count,
			Solution solution) {
		// TODO Auto-generated method stub
		/*
		 * if (NSGAIIpostprocessing.statemutcrossinitial.equals("mutation")) {
		 * 
		 * if (Operationspostprocessing.statecase.equals("case1") ||
		 * Operationspostprocessing.statecase.equals("case7") ||
		 * Operationspostprocessing.statecase.equals("case6") ||
		 * Operationspostprocessing.statecase.equals("case4")) {
		 * 
		 * 
		 * switch (MyProblempostprocessing.indexoperation) { case 1: if
		 * (MyProblempostprocessing.oldoperation1 != -1) { randomInt = (int)
		 * (MyProblempostprocessing.oldoperation1);
		 * solution.setpreviousgeneration(true);
		 * 
		 * 
		 * } else { Random number_generator = new Random(); randomInt =
		 * number_generator.nextInt(size); solution.setpreviousgeneration(false); }
		 * 
		 * break; case 2: if (MyProblempostprocessing.oldoperation2 != -1) { randomInt =
		 * (int) (MyProblempostprocessing.oldoperation2);
		 * 
		 * solution.setpreviousgeneration(true); } else { Random number_generator = new
		 * Random(); randomInt = number_generator.nextInt(size);
		 * solution.setpreviousgeneration(false); }
		 * 
		 * 
		 * break; case 3: if (MyProblempostprocessing.oldoperation3 != -1) { randomInt =
		 * (int) (MyProblempostprocessing.oldoperation3);
		 * solution.setpreviousgeneration(true);
		 * 
		 * } else { Random number_generator = new Random(); randomInt =
		 * number_generator.nextInt(size); solution.setpreviousgeneration(false);
		 * 
		 * }
		 * 
		 * 
		 * break; case 4: if (MyProblempostprocessing.oldoperation4 != -1) { randomInt =
		 * (int) (MyProblempostprocessing.oldoperation4);
		 * solution.setpreviousgeneration(true);
		 * 
		 * } else { Random number_generator = new Random(); randomInt =
		 * number_generator.nextInt(size); solution.setpreviousgeneration(false); }
		 * 
		 * 
		 * break; case 5: if (MyProblempostprocessing.oldoperation5 != -1) { randomInt =
		 * (int) (MyProblempostprocessing.oldoperation5); // System.out.print("r55r");
		 * solution.setpreviousgeneration(true); } else { Random number_generator = new
		 * Random(); randomInt = number_generator.nextInt(size);
		 * solution.setpreviousgeneration(false); }
		 * 
		 * 
		 * break; case 6: if (MyProblempostprocessing.oldoperation6 != -1) { randomInt =
		 * (int) (MyProblempostprocessing.oldoperation6);
		 * solution.setpreviousgeneration(true); } else { Random number_generator = new
		 * Random(); randomInt = number_generator.nextInt(size);
		 * solution.setpreviousgeneration(false); }
		 * 
		 * 
		 * break; case 7: if (MyProblempostprocessing.oldoperation7 != -1) { randomInt =
		 * (int) (MyProblempostprocessing.oldoperation7);
		 * 
		 * solution.setpreviousgeneration(true); } else { Random number_generator = new
		 * Random(); randomInt = number_generator.nextInt(size);
		 * solution.setpreviousgeneration(false); }
		 * 
		 * 
		 * break; case 8: if (MyProblempostprocessing.oldoperation8 != -1) { randomInt =
		 * (int) (MyProblempostprocessing.oldoperation8);
		 * solution.setpreviousgeneration(true);
		 * 
		 * } else { Random number_generator = new Random(); randomInt =
		 * number_generator.nextInt(size); solution.setpreviousgeneration(false); }
		 * 
		 * 
		 * break; case 9: if (MyProblempostprocessing.oldoperation9 != -1) { randomInt =
		 * (int) (MyProblempostprocessing.oldoperation9);
		 * solution.setpreviousgeneration(true); } else { Random number_generator = new
		 * Random(); randomInt = number_generator.nextInt(size);
		 * solution.setpreviousgeneration(false); }
		 * 
		 * 
		 * break; case 10: if (MyProblempostprocessing.oldoperation10 != -1) { randomInt
		 * = (int) (MyProblempostprocessing.oldoperation10);
		 * solution.setpreviousgeneration(true); } else { Random number_generator = new
		 * Random(); randomInt = number_generator.nextInt(size);
		 * solution.setpreviousgeneration(false); }
		 * 
		 * 
		 * break; case 11: if (MyProblempostprocessing.oldoperation11 != -1) { randomInt
		 * = (int) (MyProblempostprocessing.oldoperation11);
		 * solution.setpreviousgeneration(true); } else { Random number_generator = new
		 * Random(); randomInt = number_generator.nextInt(size);
		 * solution.setpreviousgeneration(false); }
		 * 
		 * 
		 * break; case 12: if (MyProblempostprocessing.oldoperation12 != -1) { randomInt
		 * = (int) (MyProblempostprocessing.oldoperation12);
		 * solution.setpreviousgeneration(true); } else { Random number_generator = new
		 * Random(); randomInt = number_generator.nextInt(size);
		 * solution.setpreviousgeneration(false); }
		 * 
		 * 
		 * break; case 13: if (MyProblempostprocessing.oldoperation13 != -1) { randomInt
		 * = (int) (MyProblempostprocessing.oldoperation13);
		 * solution.setpreviousgeneration(true); } else { Random number_generator = new
		 * Random(); randomInt = number_generator.nextInt(size);
		 * solution.setpreviousgeneration(false); }
		 * 
		 * 
		 * break; case 14: if (MyProblempostprocessing.oldoperation14 != -1) { randomInt
		 * = (int) (MyProblempostprocessing.oldoperation14);
		 * solution.setpreviousgeneration(true); } else { Random number_generator = new
		 * Random(); randomInt = number_generator.nextInt(size);
		 * solution.setpreviousgeneration(false); }
		 * 
		 * 
		 * break;
		 * 
		 * }
		 * 
		 * }
		 * 
		 * } else {
		 */
		Random number_generator = new Random();
		randomInt = number_generator.nextInt(size);
		checkmutationapply = true;
		solution.setpreviousgeneration(false);

		// }

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

		switch (MyProblempostprocessing.indexoperation) {
		case 1:
			MyProblempostprocessing.oldoperation1 = randomInt;
			MyProblempostprocessing.replaceoperation1 = randomInt2;
			MyProblempostprocessing.secondoldoperation1 = randomInt3;
			break;
		case 2:
			MyProblempostprocessing.oldoperation2 = randomInt;
			MyProblempostprocessing.replaceoperation2 = randomInt2;
			MyProblempostprocessing.secondoldoperation2 = randomInt3;

			break;
		case 3:
			MyProblempostprocessing.oldoperation3 = randomInt;
			MyProblempostprocessing.replaceoperation3 = randomInt2;
			MyProblempostprocessing.secondoldoperation3 = randomInt3;

			break;
		case 4:
			MyProblempostprocessing.oldoperation4 = randomInt;
			MyProblempostprocessing.replaceoperation4 = randomInt2;
			MyProblempostprocessing.secondoldoperation4 = randomInt3;

			break;
		case 5:
			MyProblempostprocessing.oldoperation5 = randomInt;
			MyProblempostprocessing.replaceoperation5 = randomInt2;
			MyProblempostprocessing.secondoldoperation5 = randomInt3;

			break;
		case 6:
			MyProblempostprocessing.oldoperation6 = randomInt;
			MyProblempostprocessing.replaceoperation6 = randomInt2;
			MyProblempostprocessing.secondoldoperation6 = randomInt3;

			break;
		case 7:
			MyProblempostprocessing.oldoperation7 = randomInt;
			MyProblempostprocessing.replaceoperation7 = randomInt2;
			MyProblempostprocessing.secondoldoperation7 = randomInt3;

			break;
		case 8:
			MyProblempostprocessing.oldoperation8 = randomInt;
			MyProblempostprocessing.replaceoperation8 = randomInt2;
			MyProblempostprocessing.secondoldoperation8 = randomInt3;

			break;
		case 9:
			MyProblempostprocessing.oldoperation9 = randomInt;
			MyProblempostprocessing.replaceoperation9 = randomInt2;
			MyProblempostprocessing.secondoldoperation9 = randomInt3;

			break;
		case 10:
			MyProblempostprocessing.oldoperation10 = randomInt;
			MyProblempostprocessing.replaceoperation10 = randomInt2;
			MyProblempostprocessing.secondoldoperation10 = randomInt3;

			break;
		case 11:
			MyProblempostprocessing.oldoperation11 = randomInt;
			MyProblempostprocessing.replaceoperation11 = randomInt2;
			MyProblempostprocessing.secondoldoperation11 = randomInt3;

			break;
		case 12:
			MyProblempostprocessing.oldoperation12 = randomInt;
			MyProblempostprocessing.replaceoperation12 = randomInt2;
			MyProblempostprocessing.secondoldoperation12 = randomInt3;

			break;
		case 13:
			MyProblempostprocessing.oldoperation13 = randomInt;
			MyProblempostprocessing.replaceoperation13 = randomInt2;
			MyProblempostprocessing.secondoldoperation13 = randomInt3;

			break;
		case 14:
			MyProblempostprocessing.oldoperation14 = randomInt;
			MyProblempostprocessing.replaceoperation14 = randomInt2;
			MyProblempostprocessing.secondoldoperation14 = randomInt3;

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
		Settingpostprocessing s = new Settingpostprocessing();
		// OCL MODEL ELEMENT
		// .......................................................................
		// System.out.println("replacement");
		if (mmtype.isInstance(toReplace)) {
			for (MetaModel metamodel : metamodels) {
				// search class to replace
				// String MMRootPath3 =
				// "C:/Users/varaminz/Downloads/Project/workspaceTEST34/anatlyzer.atl.tests.api/trafo/PNML.ecore";
				String MMRootPath3 = null;
				if (Operationspostprocessing.statecase.equals("case1")) {
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
				// System.out.println(meta.getEClassifiers());
				List<EClass> options = new ArrayList<EClass>();

				if (main.typeoperation.equals("argu") || main.typeoperation.equals("outelement")
						|| main.typeoperation.equals("inelement")) {

					List<EClass> mainclass = new ArrayList<EClass>();
					for (EClassifier classifier : meta.getEClassifiers()) {
						if (classifier instanceof EClass) {
							if (Operationspostprocessing.statecase.equals("case1")
									&& Operationspostprocessing.statecase.equals("case7")) {
								if (((EClass) classifier).isAbstract() == false)
									mainclass.add((EClass) classifier);
							}

							else {

								if (NSGAIIpostprocessing.lazyrulelocation.size() == 0) {
									// next line was for first step of project
									// if(((EClass) classifier).isAbstract()==false)
									mainclass.add((EClass) classifier);

								} else
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
	/*
	 * EObject object2modify_src = wrapper.source(modifiable.get(randomInt));
	 * EObject oldFeatureValue = (EObject)
	 * object2modify_src.eGet(featureDefinition); //
	 * NSGAII.writer.println(toString(oldFeatureValue)); // if (oldFeatureValue !=
	 * null) { boolean findreplace=false; List<EObject> replacements =
	 * this.replacements(atlModel, (EObject) oldFeatureValue, metamodels);
	 * 
	 * 
	 * // if (replacements.size() > 0) { int randomInt2 = -2;
	 * while(findreplace==false) {
	 * 
	 * randomInt2 = FindSecondIndex(randomInt2, replacements.size());
	 * 
	 * List<String> liststringiniteration = new ArrayList<String>();
	 * 
	 * if(Operationspostprocessing.statecase.equals("case6")) { //list hameye
	 * navigation ha ro begir List<VariableExp> variables =
	 * (List<VariableExp>)wrapper.allObjectsOf(VariableExp.class);
	 * 
	 * for(int i=0;i<variables.size();i++) { EObject navigationExpression =
	 * variables.get(i).eContainer(); if (navigationExpression instanceof
	 * NavigationOrAttributeCallExp) { EStructuralFeature featureDefinition2 =
	 * wrapper.source(navigationExpression).eClass().getEStructuralFeature("name");
	 * Object object2modify_src2 =
	 * wrapper.source(navigationExpression).eGet(featureDefinition2); String[]
	 * array2 = variables.get(i).getLocation().split(":", 2);
	 * 
	 * String type = getType(navigationExpression, variables.get(i), metamodels1,
	 * metamodels2); if(type!=null) {
	 * 
	 * //list attribute ke ro khat iteration hast va toye in rule hast begir chon
	 * roye khat iteration hast pas cardinality * dare
	 * if(NSGAIIpostprocessing.iterationcall.contains( Integer.parseInt(array2[0]))
	 * ) { liststringiniteration.add(object2modify_src2.toString() );
	 * 
	 * }
	 * 
	 * 
	 * } } }
	 * 
	 * 
	 * 
	 * 
	 * if(NSGAIIpostprocessing.inpatternhasfilter.get(indexrule)==1 ) {
	 * 
	 * if(!toString(oldFeatureValue).equals(toString(replacements.get(randomInt2))))
	 * { if(!NSGAIIpostprocessing.inpatternstringlocation.contains(toString(
	 * replacements.get(randomInt2)) )) findreplace=true; else { int
	 * indexof=NSGAIIpostprocessing.inpatternstringlocation.indexOf(
	 * toString(replacements.get(randomInt2)));
	 * 
	 * if( NSGAIIpostprocessing.inpatternhasfilter.get(indexof)==1) {
	 * 
	 * findreplace=true; }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * 
	 * } else {
	 * 
	 * if (!toString(oldFeatureValue).equals(toString(replacements.get(randomInt2)))
	 * &&
	 * !NSGAIIpostprocessing.inpatternstringlocation.contains(toString(replacements.
	 * get(randomInt2)) )) findreplace=true;
	 * 
	 * 
	 * }
	 * 
	 * // age attribute ro khate iterationha hast va age inpattern entekhab shode in
	 * attribute cardinality 1 dare ye inpattern dige entekhab kon chon error mide
	 * if(liststringiniteration.size()>0) { // to arraye ke list classha hast esme
	 * inpattern ra toye list peyda kon ba indexesh int
	 * indexclassname=NSGAIIpostprocessing.classnamestring.indexOf(
	 * toString(replacements.get(randomInt2)));
	 * 
	 * for(int j=0;j<liststringiniteration.size();j++) { for(int
	 * i=NSGAIIpostprocessing.classnamestartpoint.get(indexclassname);i<
	 * NSGAIIpostprocessing.classnamestartpoint.get(indexclassname)+
	 * NSGAIIpostprocessing.classnamelength.get(indexclassname);i++) {
	 * if(NSGAIIpostprocessing.listinheritattributesourcemetamodel.get(i).getName().
	 * equals( liststringiniteration.get(j)) &&
	 * NSGAIIpostprocessing.listinheritattributesourcemetamodel.get(i).getUpperBound
	 * ()==1 ) {findreplace= false;
	 * 
	 * 
	 * } }
	 * 
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * } else { if
	 * (!toString(oldFeatureValue).equals(toString(replacements.get(randomInt2))))
	 * findreplace=true; } System.out.println("findreplace");
	 * System.out.println(toString(oldFeatureValue));
	 * System.out.println(toString(replacements.get(randomInt2)));
	 * System.out.println(findreplace); }
	 */
	// NSGAII.writer.println("last");
	// NSGAII.writer.println(toString(replacements.get(randomInt2)));
	/// NSGAII.writer.println("randomInt2");
	// NSGAII.writer.println(randomInt2);
	/*
	 * copyFeatures(oldFeatureValue, replacements.get(randomInt2));
	 * System.out.println(toString(replacements.get(randomInt2)));
	 * StoreTwoIndex(randomInt, randomInt2);
	 * object2modify_src.eSet(featureDefinition, replacements.get(randomInt2));
	 * 
	 * // if (comments != null) comments.add("\n-- MUTATION \"" +
	 * this.getDescription() + "\" from " + toString(modifiable.get(randomInt)) +
	 * ":" + toString(oldFeatureValue) + " to " +
	 * toString(modifiable.get(randomInt)) + ":" +
	 * toString(replacements.get(randomInt2)) + " (line " +
	 * modifiable.get(randomInt).getLocation() + " of original transformation)\n");
	 * comment = "\n-- MUTATION \"" + this.getDescription() + "\" from " +
	 * toString(modifiable.get(randomInt)) + ":" + toString(oldFeatureValue) +
	 * " to " + toString(modifiable.get(randomInt)) + ":" +
	 * toString(replacements.get(randomInt2)) + " (line " +
	 * modifiable.get(randomInt).getLocation() + " of original transformation)\n";
	 * System.out.println("\n-- MUTATION \"" + this.getDescription() + "\" from " +
	 * toString(modifiable.get(randomInt)) + ":" + toString(oldFeatureValue) +
	 * " to " + toString(modifiable.get(randomInt)) + ":" +
	 * toString(replacements.get(randomInt2)) + " (line " +
	 * modifiable.get(randomInt).getLocation() + " of original transformation)\n");
	 * NSGAIIpostprocessing.numop = NSGAIIpostprocessing.numop + 1;
	 * checkmutationapply = true; ReturnResult.set(0, wrapper); ReturnResult.set(1,
	 * atlModel); ReturnResult.add(comment);
	 */

	// }

	// }

}
