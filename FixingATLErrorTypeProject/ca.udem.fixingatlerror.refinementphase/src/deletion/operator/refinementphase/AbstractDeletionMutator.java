package deletion.operator.refinementphase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import anatlyzer.examples.api.BaseTest;
import ca.udem.fixingatlerror.refinementphase.main;
import ca.udem.fixingatlerror.refinementphase.Operations;
import jmetal.core.Solution;
import jmetal.encodings.solutionType.IntSolutionType;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.problems.MyProblem;
import transML.utils.modeling.EMFUtils;

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
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.core.ModelFactory;
import org.eclipse.m2m.atl.core.emf.EMFModel;
import org.eclipse.m2m.atl.core.emf.EMFModelFactory;
import org.eclipse.m2m.atl.core.emf.EMFReferenceModel;
import org.eclipse.m2m.atl.engine.parser.AtlParser;

import anatlyzer.atl.analyser.Analyser;
import anatlyzer.atl.util.ATLSerializer;
import anatlyzer.atlext.ATL.ATLFactory;
import anatlyzer.atlext.ATL.Binding;
import anatlyzer.atlext.ATL.ContextHelper;
import anatlyzer.atlext.ATL.Helper;
import anatlyzer.atlext.ATL.LocatedElement;
import anatlyzer.atlext.ATL.Module;
import anatlyzer.atlext.ATL.OutPatternElement;
import anatlyzer.atlext.OCL.BooleanExp;
import anatlyzer.atlext.OCL.IntegerExp;
import anatlyzer.atlext.OCL.OCLFactory;
import anatlyzer.atlext.OCL.OclContextDefinition;
import anatlyzer.atlext.OCL.OclExpression;
import anatlyzer.atlext.OCL.OperationCallExp;
import anatlyzer.atlext.OCL.RealExp;
import anatlyzer.atlext.OCL.StringExp;
//import anatlyzer.evaluation.mutators.ATLModel;
import anatlyzer.atl.model.ATLModel;
import modification.operator.refinementphase.AbstractMutator;
import witness.generator.MetaModel;

public abstract class AbstractDeletionMutator extends AbstractMutator {

	// private static final Container not = null;
	// private static final Container and = null;
	static int i = 1;
	private static ATLModel wrapper;
//	public static final String TRANSFORMATION = "D:/java/ATLproject/neonssmallprojects/anatlyzer.evaluation.mutants/trafo/PNML2PetriNet.atl";
	private EMFModel atlModel3;
	// public static final String TRANSFORMATION =
	// "examples/class2rel/trafo/waelnewcl2rel.atl";
	Analyser analyser;
//	public static final String CLASS_METAMODEL = "D:/java/ATLproject/neonssmallprojects/anatlyzer.evaluation.mutants/trafo/PNML.ecore";
//	public static final String REL_METAMODEL = "D:/java/ATLproject/neonssmallprojects/anatlyzer.evaluation.mutants/trafo/PetriNet.ecore";
  //pak konam
	
//	private String m = "C:/Users/varaminz/Downloads/Project/workspaceTEST34/anatlyzer.atl.tests.api/trafo2/zahra2mutants/finalresult"
//			+ main.totalnumber + ".atl";

	// private String m="examples/class2rel/trafo/class2relationaltest - Copy.atl";
	// static List<Container> containers2;
	/**
	 * Generic deletion. It allows subtypes of both the container class and the
	 * class to delete.
	 * 
	 * @param atlModel
	 * @param outputFolder
	 * @param ContainerClass
	 *            container class of the class of objects to delete (example
	 *            OutPattern)
	 * @param ToDeleteClass
	 *            class of objects to delete (example Binding)
	 * @param relation
	 *            containment relation (example bindings)
	 * @return
	 */
	protected <Container extends LocatedElement, ToDelete/* extends LocatedElement */> List<Object> genericDeletion(
			EMFModel atlModel, String outputFolder, Class<Container> ContainerClass, Class<ToDelete> ToDeleteClass,
			String relation, ATLModel wrapper, Solution solution,MetaModel outputMM) {
		return this.genericDeletion(atlModel, outputFolder, ContainerClass, ToDeleteClass, relation, false, wrapper,
				solution, outputMM);

	}

	/*
	 * protected <ToModify extends LocatedElement> void
	 * genericTypeModification(EMFModel atlModel, String outputFolder,
	 * Class<ToModify> ToModifyClass, String featureName, MetaModel[] metamodels) {
	 * this.genericTypeModification(atlModel, outputFolder, ToModifyClass,
	 * featureName, metamodels, false); }
	 */

	/**
	 * Generic deletion. It allows subtypes of the class to delete. The parameter
	 * 'exactContainerType' allows configuring whether the type of the container
	 * must be exactly the one received, or if the subtypes should be also
	 * considered.
	 * 
	 * @param atlModel
	 * @param outputFolder
	 * @param ContainerClass
	 *            container class of the class of objects to delete (example
	 *            OutPattern)
	 * @param ToDeleteClass
	 *            class of objects to delete (example Binding)
	 * @param relation
	 *            containment relation (example bindings)
	 * @param exactContainerType
	 *            false to consider also subtypes of the ContainerClass, true to
	 *            discard subtypes of the ContainerClass
	 */
	private List<EClass> unrelatedClasses(EClass c, MetaModel mm) {
		List<EClass> subclasses = subclasses(c, mm);
		List<EClass> unrelatedClasses = new ArrayList<EClass>();
		for (EClassifier classifier : mm.getEClassifiers()) {
			if (classifier instanceof EClass && classifier != c && !c.getEAllSuperTypes().contains(classifier)
					&& !subclasses.contains(classifier))
				unrelatedClasses.add((EClass) classifier);
		}
		return unrelatedClasses;
	}

	private void copyFeatures(EObject from, EObject to) {
		for (EStructuralFeature feature : from.eClass().getEAllStructuralFeatures()) {
			if (!feature.getName().equals("name") && to.eClass().getEAllStructuralFeatures().contains(feature))
				to.eSet(feature, from.eGet(feature));
		}
	}

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

	private EClass getCompatible(EClass c, List<EClass> candidates) {
		EClass compatible = null;
		for (int i = 0; i < candidates.size() && compatible == null; i++) {
			EClass c2 = candidates.get(i);
			if (isCompatibleWith(c2, c))
				compatible = c2;
		}
		return compatible;
	}

	protected EClass getCompatibleSubclass(EClass c, MetaModel mm) {
		List<EClass> subclasses = subclasses(c, mm);
		return getCompatible(c, subclasses);
	}

	protected EClass getCompatibleSuperclass(EClass c) {
		List<EClass> superclasses = c.getEAllSuperTypes();
		return c.getEStructuralFeatures().size() > 0 ? getCompatible(c, superclasses) : null;
	}

	protected EClass getCompatibleUnrelatedClass(EClass c, MetaModel mm) {
		List<EClass> unrelatedClasses = unrelatedClasses(c, mm);
		return getCompatible(c, unrelatedClasses);
	}

	protected EClass getIncompatibleSubclass(EClass c, MetaModel mm) {
		List<EClass> subclasses = subclasses(c, mm);
		return getIncompatible(c, subclasses);
	}

	private EClass getIncompatible(EClass c, List<EClass> candidates) {
		EClass incompatible = null;
		for (int i = 0; i < candidates.size() && incompatible == null; i++) {
			EClass c2 = candidates.get(i);
			if (!isCompatibleWith(c2, c))
				incompatible = c2;
		}
		return incompatible;
	}

	protected EClass getIncompatibleUnrelatedClass(EClass c, MetaModel mm) {
		List<EClass> unrelatedClasses = unrelatedClasses(c, mm);
		return getIncompatible(c, unrelatedClasses);
	}

	private List<EObject> replacements(EMFModel atlModel, EObject toReplace, MetaModel[] metamodels) {
		List<EObject> replacements = new ArrayList<EObject>();

		EPackage pack = toReplace.eClass().getEPackage();
		EClass mmtype = (EClass) pack.getEClassifier("OclModelElement");
		EClass collection = (EClass) pack.getEClassifier("CollectionType");
		EClass collectionExp = (EClass) pack.getEClassifier("CollectionExp");
		EClass primitive = (EClass) pack.getEClassifier("Primitive");

		// OCL MODEL ELEMENT
		// .......................................................................

		if (mmtype.isInstance(toReplace)) {
			for (MetaModel metamodel : metamodels) {
				// search class to replace
				EStructuralFeature featureName = toReplace.eClass().getEStructuralFeature("name");
				String featureValue = toReplace.eGet(featureName).toString();
				EClassifier classToReplace = metamodel.getEClassifier(featureValue);

				if (classToReplace != null && classToReplace instanceof EClass) {
					EClass replace = (EClass) classToReplace;
					List<EClass> options = new ArrayList<EClass>();

					// search classes to use as replacement
					options.add(getCompatibleSubclass(replace, metamodel));
					options.add(getCompatibleSuperclass(replace));
					options.add(getCompatibleUnrelatedClass(replace, metamodel));
					options.add(getIncompatibleSubclass(replace, metamodel));
					options.add(getIncompatibleUnrelatedClass(replace, metamodel));

					// create an OclModelElement for each found replacement class
					for (EClass option : options) {
						if (option != null) {
							EObject object = (EObject) atlModel.newElement(mmtype);
							object.eSet(mmtype.getEStructuralFeature("name"), option.getName());
							replacements.add(object);
						}
					}
				}
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

	public void FindError() {

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

	protected <Container extends LocatedElement, ToDelete/* extends LocatedElement */> List<Object> genericDeletion(
			EMFModel atlModel, String outputFolder, Class<Container> ContainerClass, Class<ToDelete> ToDeleteClass,
			String relation, boolean exactContainerType, ATLModel wrapper, Solution solution,MetaModel outputMM) {
		List<Object> ReturnResult = new ArrayList<Object>();
		// FindError();
		List<Container> containers = (List<Container>) wrapper.allObjectsOf(ContainerClass);
		 
		
		ReturnResult.add(wrapper);
		ReturnResult.add(atlModel);
		Module module = wrapper.getModule();
		EDataTypeEList<String> comments = null;
		String comment = null;
		if (module != null) {
			EStructuralFeature feature = wrapper.source(module).eClass().getEStructuralFeature("commentsAfter");
			comments = (EDataTypeEList<String>) wrapper.source(module).eGet(feature);
		}

		if (exactContainerType)
			filterSubtypes(containers, ContainerClass);

		boolean choose = false;
		boolean choose2 = false;
		// while(choose==false){
		// Random randomGenerator = new Random();
		boolean checkmutationapply = false;

		int randomInt = -2;
		ArrayList<Integer> filterrule = new ArrayList<Integer>(); 
		while (checkmutationapply == false) {
			System.out.println("whilefilter");
			System.out.println(containers.size());
			List<Integer> Result = ReturnFirstIndex(randomInt, containers.size(), checkmutationapply, solution);
			randomInt = Result.get(0);
		//	NSGAII.writer.println(randomInt);
		//	NSGAII.writer.println(solution.getpreviousgeneration());
			if (Result.get(1) == 0)
				checkmutationapply = false;
			else
				checkmutationapply = false;
			if (randomInt == -2)
				checkmutationapply = true;
			else if (solution.getpreviousgeneration() == true) {
		//		System.out.println("line out1");
				ReturnResult = OperationPreviousGenerationDeletion(randomInt, solution, atlModel, containers, wrapper,
						relation, comments, ReturnResult, outputMM);
				checkmutationapply = true;
			} else if (randomInt != -1 && randomInt != -2 && solution.getpreviousgeneration() == false) {
				// System.out.println(containers.get(randomInt).getLocation());
				String[] array = containers.get(randomInt).getLocation().split(":", 2);
			//	NSGAII.writer.println("line out2");
				System.out.println("line out2");
			//	System.out.println(Operations.statecase);
			//	NSGAII.writer.println(Integer.parseInt(array[0]));
				int indexrule = -1;
			//	NSGAII.writer.println(Operations.statecase);
				for (int j = 0; j < NSGAII.faultrule.size(); j++) {
					if (Integer.parseInt(array[0]) > NSGAII.faultrule.get(j)
							&& Integer.parseInt(array[0]) < NSGAII.finalrule.get(j))

						indexrule = j;

				}
		//		NSGAII.writer.println("indexrule");
		//		NSGAII.writer.println(indexrule);
		//		System.out.println(indexrule);
		//		NSGAII.writer.println(NSGAII.errorrule);
				System.out.println(NSGAII.errorrule);
				filterrule.add(indexrule);
				System.out.println(filterrule);
				System.out.println(NSGAII.listfilterapplied);
				if (containers.size() > 0 && NSGAII.errorrule.contains(indexrule)) {

					EStructuralFeature feature = wrapper.source(containers.get(randomInt)).eClass()
							.getEStructuralFeature(relation);

					if (feature != null) {
						System.out.println(NSGAII.errorrule);
						if (feature.getUpperBound() == 1 && feature.getLowerBound() == 0) {
							EObject link = (EObject) wrapper.source(containers.get(randomInt)).eGet(feature);
							boolean filterrulecheck=false;
				            for(int p=0;p<NSGAII.errorrule.size();p++)
				             if(!filterrule.contains(NSGAII.errorrule.get(p)) &&  !NSGAII.listfilterapplied.contains(NSGAII.errorrule.get(p)))
				              filterrulecheck=true;
				            System.out.println(filterrulecheck);
				             if(filterrulecheck==false)
				              checkmutationapply = true;
							if (link != null) {
								LocatedElement object = (LocatedElement) wrapper.target(link);
								  boolean listlineofcode = false;  
							        String[] array2 = object.getLocation().split(":", 2);
							        
							        if (!solution.listfilterdeletion.contains(Integer.parseInt(array2[0])))
							         listlineofcode = true;
							        
								if (ToDeleteClass.isAssignableFrom(object.getClass()) && listlineofcode == true) {
									wrapper.source(containers.get(randomInt)).eSet(feature, null);
									updatefirstIndex(randomInt);
									
										comments.add("\n-- MUTATION \"" + this.getDescription() + "\" "
												+ toString(object) + " in " + toString(containers.get(randomInt))
												+ " (line " + object.getLocation() + " of original transformation)\n");
									// System.out.println("comments");
									comment = "\n-- MUTATION \"" + this.getDescription() + "\" " + toString(object)
											+ " in " + toString(containers.get(randomInt)) + " (line "
											+ object.getLocation() + " of original transformation)\n";
							/*		NSGAII.writer.println("\n-- MUTATION \"" + this.getDescription() + "\" " + toString(object)
							         + " in " + toString(containers.get(randomInt)) + " (line "
							         + object.getLocation() + " of original transformation)\n");*/
									 solution.listfilterdeletion.add(Integer.parseInt(array2[0]));
									 NSGAII.listfilterapplied.add(indexrule);
									NSGAII.numop = NSGAII.numop + 1;
									checkmutationapply = true;
									choose = true;
									  ReturnResult.set(0, wrapper);
								         ReturnResult.set(1, atlModel);
								         ReturnResult.add(comment);
									
								}

							} else {
								choose = true;
							}
						}

						// CASE 2: multivalued feature
						// ........................................................

						else {

							List<EObject> link = (List<EObject>) wrapper.source(containers.get(randomInt))
									.eGet(feature);

						//	NSGAII.writer.println("5link");
						//	NSGAII.writer.println(feature.getLowerBound());
						//	NSGAII.writer.println(link.size());
							if (feature.getLowerBound() == link.size())
								checkmutationapply = true;
							if (feature.getLowerBound() < link.size()) {

								int size = link.size();

								int randomInt2 = -2;
								ArrayList<Integer> tempsecondint = receivesecondrandomInt(randomInt2, size, solution,
										randomInt);
								randomInt2 = tempsecondint.get(1);
								
								// delet konam
								System.out.println(Operations.statecase);
						//		System.out.println(size);
						//		System.out.println(solution.listlineofcodes);
						//		System.out.println(Integer.parseInt(array[0]));
						//		System.out.println(randomInt2);
								int newsecondindex = -1;
								if (randomInt2 == -1) {
									newsecondindex = updaterandomInt(randomInt, tempsecondint, solution);
									checkmutationapply = true;
								}
								// mutation: remove object
								if (randomInt2 != -1) {
									
									int row = 0;
									for (int h = 0; h < solution.getCoSolution().getOp().listpropertynamelocation
											.size(); h++) {

										if (solution.getCoSolution().getOp().listpropertynamelocation.get(h)
												.get(0) == Integer.parseInt(array[0]) + 1) {
											row = h;
											break;
										}
									}
									int indexoriginal = -1;
									int newindex = -1;
									boolean b = false;
									int lastindex = -1;
									for (int h = 0; h < solution.getCoSolution().getOp().originalwrapper.get(row)
											.size(); h++) {
										if (solution.getCoSolution().getOp().listpropertyname.get(row).get(h) != null
												&& solution.getCoSolution().getOp().originalwrapper.get(row)
														.get(h) == 0) {
											indexoriginal = indexoriginal + 1;
											newindex = indexoriginal;
									
											if (indexoriginal == randomInt2) {
												lastindex = h;
												
											}
												}

										else if (solution.getCoSolution().getOp().originalwrapper.get(row).get(h) == 1
												&& b == false && solution.getCoSolution().getOp().listpropertyname
														.get(row).get(h) != null) {
											newindex = newindex + 1;
											if (newindex == randomInt2) {
												lastindex = h;
												b = true;
												break;
											}
										}

									}
									LocatedElement object = null;
									
							//		System.out.println(lastindex );
									if(indexoriginal < randomInt2) 
									{
										if(solution.getCoSolution().getOp().listpropertyname.get(row).get(lastindex)!=null)
										{
											System.out.println("delet 2" );
										//	System.out.println(row);
										//	System.out.println(randomInt);
										//	System.out.println(solution.getCoSolution().getOp().listpropertyname.get(row));
											EStructuralFeature feature2 = wrapper.source(containers.get(randomInt))
													.eClass().getEStructuralFeature("bindings");
											List<Binding> realbindings = (List<Binding>) wrapper
													.source(containers.get(randomInt)).eGet(feature2);
											solution.newbindings.set(MyProblem.indexoperation-1, realbindings.get(randomInt2));
											
									
										//	solution.listeobject.set(MyProblem.indexoperation-1, link.get(randomInt2));
									//		List<OutPatternElement> listoutpattern =  wrapper.allObjectsOf(OutPatternElement.class);
									//		EClassifier classifier = outputMM.getEClassifier(
									//				listoutpattern.get(randomInt).getType().getName());
								//			solution.expression.set(MyProblem.indexoperation-1, realbindings.get(randomInt2).getValue());
											solution.modifypropertyname.set(MyProblem.indexoperation - 1,
													solution.getCoSolution().getOp().listpropertyname.get(row).get(lastindex));
								//			System.out.println(classifier.getName());
							//				solution.classifiersolution.set(MyProblem.indexoperation-1,classifier );
											realbindings.remove(randomInt2);
											// bod NSGAII.counterdelet = NSGAII.counterdelet + 2;
											
										/*	NSGAII.writer
													.println("\n-- outMUTATION \"" + this.getDescription() + "\" "
															+ solution.getCoSolution().getOp().listpropertyname.get(row)
																	.get(lastindex)
															+ " in " + toString(containers.get(randomInt)) + " (line "
															+ solution.getCoSolution().getOp().listpropertynamelocation
																	.get(row).get(lastindex)
															+ " of original transformation)\n");*/

											// if (comments != null)
											comments.add(
													"\n-- outdelet1MUTATION \"" + this.getDescription() + "\" "
															+ solution.getCoSolution().getOp().listpropertyname.get(row)
																	.get(lastindex)
															+ " in " + toString(containers.get(randomInt)) + " (line "
															+ solution.getCoSolution().getOp().listpropertynamelocation
																	.get(row).get(lastindex)
															+ " of original transformation)\n");
										/*	System.out.println(
													"\n-- outdeletMUTATION \"" + this.getDescription() + "\" "
															+ solution.getCoSolution().getOp().listpropertyname.get(row)
																	.get(lastindex)
															+ " in " + toString(containers.get(randomInt)) + " (line "
															+ solution.getCoSolution().getOp().listpropertynamelocation
																	.get(row).get(lastindex)
															+ " of original transformation)\n");*/
												//	solution.getCoSolution().getOp().newbindings.get(row).set(lastindex, null);
											tempsecondint.set(0, lastindex);
											tempsecondint.set(1, lastindex);
											solution.inorout.set(MyProblem.indexoperation - 1, "out");
											newsecondindex = updaterandomInt(row, tempsecondint, solution);
											comment = "\n-- MUTATION \"" + this.getDescription() + "\" "
													+ solution.getCoSolution().getOp().listpropertyname.get(row)
															.get(lastindex)
													+ " in " + toString(containers.get(randomInt)) + " (line "
													+ solution.getCoSolution().getOp().listpropertynamelocation.get(row)
															.get(lastindex)
													+ " of original transformation)\n";
											NSGAII.numop = NSGAII.numop + 1;
											checkmutationapply = true;
										
											solution.getCoSolution().getOp().listpropertyname.get(row).set(lastindex,
													null);
									
											ReturnResult.set(0, wrapper);
											ReturnResult.set(1, atlModel);
											ReturnResult.add(comment);

									}	
									}
									// System.out.println(object);
									
								//	if (ToDeleteClass.isAssignableFrom(object.getClass())) {
									else {
										boolean listlineofcode = false;
										EObject eobject = link.get(randomInt2);
										object = (LocatedElement) wrapper.target(eobject);
										String[] array2 = object.getLocation().split(":", 2);

										System.out.println(Integer.parseInt(array2[0]));
										if (!solution.listlineofcodes.contains(Integer.parseInt(array2[0])))
											listlineofcode = true;
										if (Result.get(1) == 1
												&& solution.listlineofcodes.contains(Integer.parseInt(array2[0])))
											checkmutationapply = false;

										if (listlineofcode == true) {
											int location = 0;
											for (int h = 0; h < solution.getCoSolution()
													.getOp().listpropertynamelocation.size(); h++) {

												if (solution.getCoSolution().getOp().listpropertynamelocation.get(h)
														.get(0) <= Integer.parseInt(array2[0])
														&& Integer.parseInt(array2[0]) <= solution.getCoSolution()
																.getOp().listpropertynamelocation
																		.get(h)
																		.get(solution.getCoSolution()
																				.getOp().listpropertynamelocation.get(h)
																						.size()
																				- 1)) {
													location = h;
													break;
												}
											}
										//	System.out.println(
											//		solution.getCoSolution().getOp().listpropertyname.get(location));
										//	System.out.println(solution.getCoSolution().getOp().listpropertynamelocation
											//		.get(location));
											int indexof = -1;
											for(int p=0;p<solution.getCoSolution().getOp().listpropertynamelocation
													.get(location).size();p++)
												if(solution.getCoSolution().getOp().listpropertynamelocation
													.get(location).get(p).equals(  Integer.parseInt(array2[0]) ))
													indexof=p;
									//		int indexof = solution.getCoSolution().getOp().listpropertynamelocation
												//	.get(location).indexOf(Integer.parseInt(array2[0]));
											solution.getCoSolution().getOp().listpropertyname.get(location).set(indexof,
													null);
											// solution.getCoSolution().getOp().listpropertynamelocation.get(location).set(indexof,
											// -1);
										//	System.out.println(
										//			solution.getCoSolution().getOp().listpropertyname.get(location));
										//	System.out.println(solution.getCoSolution().getOp().listpropertynamelocation
										//			.get(location));
											link.remove(randomInt2);
											NSGAII.deletlist.add(randomInt);
									//		System.out.println("yes");
											// Tester.module2.getElements().remove(randomInt2);
											// mutation: documentation
										//	NSGAII.writer.println("link size");
											// NSGAII.writer.println(NSGAII.deletlist);
										//	NSGAII.writer.println(containers.size());
										//	NSGAII.writer.println(size);
										//	NSGAII.writer.println(randomInt);
									//bod		NSGAII.counterdelet = NSGAII.counterdelet + 1;

										//	NSGAII.writer.println(randomInt2);
										/*	NSGAII.writer.println("\n-- MUTATION \"" + this.getDescription() + "\" "
													+ toString(object) + " in " + toString(containers.get(randomInt))
													+ " (line " + object.getLocation()
													+ " of original transformation)\n");*/

										//	NSGAII.writer.println(Operations.statecase);
											// if (comments != null)
											comments.add("\n-- MUTATION \"" + this.getDescription() + "\" "
													+ toString(object) + " in " + toString(containers.get(randomInt))
													+ " (line " + object.getLocation()
													+ " of original transformation)\n");

											// MyProblem.oldoperation5=randomInt;
											// MyProblem.replaceoperation5=randomInt2;

											newsecondindex = updaterandomInt(randomInt, tempsecondint, solution);

										//	NSGAII.writer.println("randomIntlast");
										//	NSGAII.writer.println(randomInt);
										//	NSGAII.writer.println(newsecondindex);
										//	NSGAII.writer.println(Integer.parseInt(array2[0]));
											solution.listlineofcodes.add(Integer.parseInt(array2[0]));

									//		NSGAII.writer.println(solution.listlineofcodes);
											comment = "\n-- MUTATION \"" + this.getDescription() + "\" "
													+ toString(object) + " in " + toString(containers.get(randomInt))
													+ " (line " + object.getLocation()
													+ " of original transformation)\n";
											NSGAII.numop = NSGAII.numop + 1;
											checkmutationapply = true;
										/*	System.out.println( "\n-- MUTATION \"" + this.getDescription() + "\" "
													+ toString(object) + " in " + toString(containers.get(randomInt))
													+ " (line " + object.getLocation()
													+ " of original transformation)\n");*/
											ReturnResult.set(0, wrapper);
											ReturnResult.set(1, atlModel);
											ReturnResult.add(comment);
											choose = true;

										}
									}
								}

							}

							else {
								choose = true;
							}

						}
					}

				}
			}

		}

	//	ReturnResult.add(comment);

		return ReturnResult;

	}

	private <Container extends LocatedElement, ToDelete/* extends LocatedElement */> List<Object> OperationPreviousGenerationDeletion(
			int randomInt, Solution solution, EMFModel atlModel, List<Container> containers, ATLModel wrapper,
			String relation, EDataTypeEList<String> comments, List<Object> ReturnResult,MetaModel outputMM) {
	//	System.out.println( solution.inorout);
	//	System.out.println(solution.inorout.get(MyProblem.indexoperation-1));
	//	NSGAII.writer.println( solution.inorout);
	//	NSGAII.writer.println(solution.inorout.get(MyProblem.indexoperation-1));
		
		if (solution.inorout.get(MyProblem.indexoperation-1).equals("out")) {
			EStructuralFeature feature2 = wrapper.source(containers.get(randomInt))
					.eClass().getEStructuralFeature("bindings");
			List<Binding> realbindings = (List<Binding>) wrapper
					.source(containers.get(randomInt)).eGet(feature2);
			
		//	List<EObject> link = (List<EObject>) wrapper.source(containers.get(randomInt))
		//			.eGet(feature2);
			
			Binding b=solution.newbindings.get(MyProblem.indexoperation-1);
		//	EObject obj=solution.listeobject.get(MyProblem.indexoperation-1);
				
				 
				 
		//	b2.setPropertyName(b.getPropertyName());
			String classifier2 = solution.modifypropertyname.get(MyProblem.indexoperation - 1);
		//	EClassifier classifier2=solution.classifiersolution.get(MyProblem.indexoperation-1 );
			int index=-1;
	//		for(int p=0;p<NSGAII.classifierliast.size();p++)
	//			if(NSGAII.classifierliast.get(p).getName().equals(classifier2))
		//			index=p;
	/*		EClass child = ((EClass) NSGAII.classifierliast.get(index));
			for(int p=0;p<child.getEStructuralFeatures().size();p++) {
				if(child.getEStructuralFeatures().get(p).getName().equals(b.getPropertyName())) {
					EStructuralFeature newfeature = child.getEStructuralFeatures().get(p);
					//b2.setValue(solution.expression.get(MyProblem.indexoperation-1));
					b2.setValue(getCompatibleValue(newfeature.getEType().getName(), newfeature.getUpperBound() == 1, true, null));
					
			
				}
			}*/
	/*		System.out.println("outdelet");
			System.out.println(randomInt);
			System.out.println(b.getPropertyName());
			System.out.println(Operations.statecase);
			
			System.out.println(b);
			NSGAII.writer.println(b);
			NSGAII.writer.println(classifier2);
			NSGAII.writer.println(b.getPropertyName());*/
			
			index=-1;
			for(int i=0;i<realbindings.size();i++) {
				//NSGAII.writer.println(realbindings.get(i));
				if( solution.getCoSolution().getOp().originalwrapper.get(randomInt).get(i) == 1
						&& realbindings.get(i).getValue().getClass().equals( b.getValue().getClass())
						&& realbindings.get(i).getPropertyName().equals(classifier2)) {
			//	System.out.println(realbindings.get(i));
			//	System.out.println(realbindings.get(i).getValue().getClass());
				
				index=i;
		//		System.out.println(i);
		//		System.out.println("yescontainis");
			//	NSGAII.writer.println(realbindings.get(i).getClass());
			//	NSGAII.writer.println(realbindings.get(i).getValue());
			//	NSGAII.writer.println(b2.getValue());
			//	if(realbindings.get(i).equals(b)) 
			//		NSGAII.writer.println("yescontainis");
				}
			
				
			}
				/*	for(int i=0;i<realbindings.size();i++)
			{     NSGAII.writer.println(realbindings.get(i));
				//if(realbindings.contains(b2)) 
					if(realbindings.get(i).equals(b2))
					    index=i;
					
				}*/
		/*	for(int i=0;i<link.size();i++) {
				EObject eobject = link.get(i);
				LocatedElement object = (LocatedElement) wrapper.target(eobject);
				String[] array2 = object.getLocation().split(":", 2);

				NSGAII.writer.println(Integer.parseInt(array2[0]));

			   if(link.get(i).equals(obj))
				   index=link.indexOf(obj);
			}*/
			//	System.out.println(index);
			//	NSGAII.writer.println(index);
			if(index!=-1) {
			//	int indexof=-1;
			//	indexof=solution.getCoSolution().getOp().listpropertyname.get(randomInt).indexOf(b.getPropertyName() );
	//		int randomInt2 = -2;
				ArrayList<Integer> tempsecondint = new ArrayList<Integer>();
				//= receivesecondrandomInt(randomInt2, -1, solution,
	//				randomInt);
		//	randomInt2 = tempsecondint.get(1);
				
			realbindings.remove(index);
			// bod NSGAII.counterdelet = NSGAII.counterdelet + 2;
			String comment = null;
			// if (comments != null)
			comments.add(
					"\n-- outdelet2MUTATION \"" + this.getDescription() + "\" "
							+ solution.getCoSolution().getOp().listpropertyname.get(randomInt)
									.get(index)
							+ " in " + toString(containers.get(randomInt)) + " (line "
							+ solution.getCoSolution().getOp().listpropertynamelocation
									.get(randomInt).get(index)
							+ " of original transformation)\n");
		/*	System.out.println(
					"\n-- outdeletMUTATION \"" + this.getDescription() + "\" "
							+ solution.getCoSolution().getOp().listpropertyname.get(randomInt)
									.get(index)
							+ " in " + toString(containers.get(randomInt)) + " (line "
							+ solution.getCoSolution().getOp().listpropertynamelocation
									.get(randomInt).get(index)
							+ " of original transformation)\n");
			NSGAII.writer.println(
					"\n-- outdeletMUTATION \"" + this.getDescription() + "\" "
							+ solution.getCoSolution().getOp().listpropertyname.get(randomInt)
									.get(index)
							+ " in " + toString(containers.get(randomInt)) + " (line "
							+ solution.getCoSolution().getOp().listpropertynamelocation
									.get(randomInt).get(index)
							+ " of original transformation)\n");*/
				//	solution.getCoSolution().getOp().newbindings.get(row).set(lastindex, null);
			tempsecondint.add(0, index);
			tempsecondint.add(1, index);
			int newsecondindex;
			newsecondindex = updaterandomInt(randomInt, tempsecondint, solution);
			comment = "\n-- MUTATION \"" + this.getDescription() + "\" "
					+ solution.getCoSolution().getOp().listpropertyname.get(randomInt)
							.get(index)
					+ " in " + toString(containers.get(randomInt)) + " (line "
					+ solution.getCoSolution().getOp().listpropertynamelocation.get(randomInt)
							.get(index)
					+ " of original transformation)\n";
			NSGAII.numop = NSGAII.numop + 1;
			//checkmutationapply = true;
			solution.getCoSolution().getOp().listpropertyname.get(randomInt).set(index,
					null);
	
			ReturnResult.set(0, wrapper);
			ReturnResult.set(1, atlModel);
			ReturnResult.add(comment);

			
			
			}else {
				ArrayList<Integer> tempsecondint2 = new ArrayList<Integer>();
				tempsecondint2.add(-2);
				tempsecondint2.add(-2);
				int newsecondindex;
				newsecondindex = updaterandomInt(-2, tempsecondint2, solution);
				solution.inorout.set(MyProblem.indexoperation-1, "empty");
				solution.newbindings.set(MyProblem.indexoperation-1, null);
				
			}
			
		}
		else {
			if(Operations.statecase.equals("case2")) {
				String comment = null;
				
				Container modifiable2 = containers.get(randomInt);
				EStructuralFeature feature = wrapper.source(modifiable2).eClass().getEStructuralFeature(relation);
				
				EObject link = (EObject) wrapper.source(containers.get(randomInt)).eGet(feature);

				if (link != null) {
					LocatedElement object = (LocatedElement) wrapper.target(link);
					  boolean listlineofcode = false;  
				        String[] array2 = object.getLocation().split(":", 2);
				        
				        if (!solution.listfilterdeletion.contains(Integer.parseInt(array2[0]))
				        	)
				         listlineofcode = true;
				        
					if (listlineofcode == true) {
						wrapper.source(containers.get(randomInt)).eSet(feature, null);
						updatefirstIndex(randomInt);
						
							comments.add("\n-- MUTATION \"" + this.getDescription() + "\" "
									+ toString(object) + " in " + toString(containers.get(randomInt))
									+ " (line " + object.getLocation() + " of original transformation)\n");
						// System.out.println("comments");
						comment = "\n-- MUTATION \"" + this.getDescription() + "\" " + toString(object)
								+ " in " + toString(containers.get(randomInt)) + " (line "
								+ object.getLocation() + " of original transformation)\n";
				/*		NSGAII.writer.println("\n-- MUTATION \"" + this.getDescription() + "\" " + toString(object)
				         + " in " + toString(containers.get(randomInt)) + " (line "
				         + object.getLocation() + " of original transformation)\n");*/
						int indexrule = -1;
						//	NSGAII.writer.println(Operations.statecase);
							for (int j = 0; j < NSGAII.faultrule.size(); j++) {
								if (Integer.parseInt(array2[0]) > NSGAII.faultrule.get(j)
										&& Integer.parseInt(array2[0]) < NSGAII.finalrule.get(j))

									indexrule = j;

							}
						 solution.listfilterdeletion.add(Integer.parseInt(array2[0]));
						 NSGAII.listfilterapplied.add(indexrule);
						NSGAII.numop = NSGAII.numop + 1;
						
						
						  ReturnResult.set(0, wrapper);
					         ReturnResult.set(1, atlModel);
					         ReturnResult.add(comment);
						
					}

				}
				
			}
			
		else {
		String comment = null;
		// TODO Auto-generated method stub
		Container modifiable2 = containers.get(randomInt);
		EStructuralFeature feature = wrapper.source(modifiable2).eClass().getEStructuralFeature(relation);
		List<EObject> link = (List<EObject>) wrapper.source(modifiable2).eGet(feature);
		int randomInt2 = -2;
		ArrayList<Integer> tempsecondint = receivesecondrandomInt(randomInt2, -1, solution, randomInt);
		randomInt2 = tempsecondint.get(1);
		int newsecondindex2 = -1;
		if (randomInt2 == -1) {
			newsecondindex2 = updaterandomInt(randomInt, tempsecondint, solution);
			
		}
		else if(randomInt2!=-1){
		EObject eobject = link.get(randomInt2);
		LocatedElement object = (LocatedElement) wrapper.target(eobject);
		String[] array2 = object.getLocation().split(":", 2);
		if (!solution.listlineofcodes.contains(Integer.parseInt(array2[0]))) {
			link.remove(randomInt2);
			comments.add("\n-- MUTATION \"" + this.getDescription() + "\" " + toString(object) + " in "
					+ toString(containers.get(randomInt)) + " (line " + object.getLocation()
					+ " of original transformation)\n");
			int newsecondindex;
			newsecondindex = updaterandomInt(randomInt, tempsecondint, solution);
			solution.listlineofcodes.add(Integer.parseInt(array2[0]));

		//	NSGAII.writer.println("after crossover delet");
			comment = "\n-- MUTATION \"" + this.getDescription() + "\" " + toString(object) + " in "
					+ toString(containers.get(randomInt)) + " (line " + object.getLocation()
					+ " of original transformation)\n";
			NSGAII.numop = NSGAII.numop + 1;

		/*	NSGAII.writer.println("\n-- MUTATION \"" + this.getDescription() + "\" " + toString(object) + " in "
					+ toString(containers.get(randomInt)) + " (line " + object.getLocation()
					+ " of original transformation)\n");
			System.out.println("\n-- MUTATION \"" + this.getDescription() + "\" " + toString(object) + " in "
					+ toString(containers.get(randomInt)) + " (line " + object.getLocation()
					+ " of original transformation)\n");*/

			ReturnResult.set(0, wrapper);
			ReturnResult.set(1, atlModel);
			ReturnResult.add(comment);

		} else {
			//ArrayList<Integer> tempsecondint2 = new ArrayList<Integer>();
			//tempsecondint2.add(-1);
			//tempsecondint2.add(-1);
			int newsecondindex;
			newsecondindex = updaterandomInt(randomInt, tempsecondint, solution);
			solution.inorout.set(MyProblem.indexoperation-1, "empty");
			solution.newbindings.set(MyProblem.indexoperation-1, null);
			
		}
		}
		}
		
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
			//	System.out.println("else");
			//	NSGAII.writer.println("else");
				expression = OCLFactory.eINSTANCE.createSequenceExp();
				// expression = OCLFactory.eINSTANCE.createVariableExp();
			//	System.out.println(expression);
				// bod if (variables.size()>0)
				// ((VariableExp)expression).setReferredVariable(variables.get(0));
			}
		}

		else {
		//	System.out.println("else2");
		//	NSGAII.writer.println("else2");
			expression = OCLFactory.eINSTANCE.createSequenceExp();
			// expression = ordered?

			// OCLFactory.eINSTANCE.createSequenceExp() :
			// OCLFactory.eINSTANCE.createSetExp();
		}

		return expression;

		
	}

	private void updatefirstIndex(int randomInt) {
		// TODO Auto-generated method stub
		if (Operations.statecase.equals("case2") || Operations.statecase.equals("case3")
				|| Operations.statecase.equals("case8") || Operations.statecase.equals("case7")
				|| Operations.statecase.equals("case6")) {

			// MyProblem.oldoperation2=randomInt;
	//		NSGAII.writer.println("apply case2");
	//		NSGAII.writer.println(randomInt);
			switch (MyProblem.indexoperation) {
			case 1:
				MyProblem.oldoperation1 = randomInt;

				break;
			case 2:
				MyProblem.oldoperation2 = randomInt;

				break;
			case 3:
				MyProblem.oldoperation3 = randomInt;

				break;
			case 4:
				MyProblem.oldoperation4 = randomInt;

				break;
			case 5:
				MyProblem.oldoperation5 = randomInt;

				break;
			case 6:
				MyProblem.oldoperation6 = randomInt;

				break;
			case 7:
				MyProblem.oldoperation7 = randomInt;

				break;
			case 8:
				MyProblem.oldoperation8 = randomInt;

				break;
			case 9:
				MyProblem.oldoperation9 = randomInt;

				break;
			case 10:
				MyProblem.oldoperation10 = randomInt;

				break;
			case 11:
				MyProblem.oldoperation11 = randomInt;

				break;
			case 12:
				MyProblem.oldoperation12 = randomInt;

				break;
			case 13:
				MyProblem.oldoperation13 = randomInt;

				break;
			case 14:
				MyProblem.oldoperation14 = randomInt;

				break;
			case 15:
				MyProblem.oldoperation15 = randomInt;

				break;
			case 16:
				MyProblem.oldoperation16 = randomInt;

				break;
			case 17:
				MyProblem.oldoperation17 = randomInt;

				break;
			case 18:
				MyProblem.oldoperation18 = randomInt;

				break;
			case 19:
				MyProblem.oldoperation19 = randomInt;

				break;
			case 20:
				MyProblem.oldoperation20 = randomInt;

				break;
			case 21:
				MyProblem.oldoperation21 = randomInt;

				break;
			case 22:
				MyProblem.oldoperation22 = randomInt;

				break;

			}

		}

	}

	private List<Integer> ReturnFirstIndex(int randomInt, int size, boolean checkmutationapply, Solution solution) {
		// TODO Auto-generated method stub
		System.out.println(NSGAII.statemutcrossinitial);
		
		if (NSGAII.statemutcrossinitial.equals("mutation")) {

			if (Operations.statecase.equals("case2") || Operations.statecase.equals("case5")) {
				
				switch (MyProblem.indexoperation) {
				case 1:
					if (MyProblem.oldoperation1 != -1 ) {
						randomInt = (int) (MyProblem.oldoperation1);
						solution.setpreviousgeneration(true);
					} else 

					{
						randomInt=choosefirstindex( randomInt, size, solution);
						solution.setpreviousgeneration(false);

					}
					
					// randomInt=(int) (Math.random() * (containers.size()));

					break;
				case 2:
					if (MyProblem.oldoperation2 != -1 ) {
						randomInt = (int) (MyProblem.oldoperation2);
						solution.setpreviousgeneration(true);
					} else 

					{
						
						randomInt=choosefirstindex( randomInt, size, solution);
						solution.setpreviousgeneration(false);

					}
					 
					// randomInt=(int) (Math.random() * (containers.size()));

					break;
				case 3:
					if (MyProblem.oldoperation3 != -1) {
						randomInt = (int) (MyProblem.oldoperation3);
						solution.setpreviousgeneration(true);
					} else {
						randomInt=choosefirstindex( randomInt, size, solution);
						solution.setpreviousgeneration(false);

					}
					 
					break;
				case 4:
					if (MyProblem.oldoperation4 != -1 ) {
						randomInt = (int) (MyProblem.oldoperation4);
						solution.setpreviousgeneration(true);
					} else {
						randomInt=choosefirstindex( randomInt, size, solution);
						solution.setpreviousgeneration(false);

					}

					break;
				case 5:
					if (MyProblem.oldoperation5 != -1 ) {
						randomInt = (int) (MyProblem.oldoperation5);
						solution.setpreviousgeneration(true);
					} else {
						randomInt=choosefirstindex( randomInt, size, solution);
						solution.setpreviousgeneration(false);
					}

					// randomInt=(int) (Math.random() * (containers.size()));

					break;
				case 6:
					if (MyProblem.oldoperation6 != -1 ) {
						randomInt = (int) (MyProblem.oldoperation6);
						solution.setpreviousgeneration(true);
					} else  {
						randomInt=choosefirstindex( randomInt, size, solution);
						solution.setpreviousgeneration(false);
					}

					// randomInt=(int) (Math.random() * (containers.size()));

					break;
				case 7:
					if (MyProblem.oldoperation7 != -1 ) {
						randomInt = (int) (MyProblem.oldoperation7);
						solution.setpreviousgeneration(true);
					} else {
						randomInt=choosefirstindex( randomInt, size, solution);
						solution.setpreviousgeneration(false);
					}

					break;
				case 8:
					if (MyProblem.oldoperation8 != -1 ) {
						randomInt = (int) (MyProblem.oldoperation8);
						solution.setpreviousgeneration(true);
					} else  {
						randomInt=choosefirstindex( randomInt, size, solution);
						solution.setpreviousgeneration(false);
					}

					break;
				case 9:
					if (MyProblem.oldoperation9 != -1 ) {
						randomInt = (int) (MyProblem.oldoperation9);
						solution.setpreviousgeneration(true);
					} else  {
						randomInt=choosefirstindex( randomInt, size, solution);
						solution.setpreviousgeneration(false);
					}

					// randomInt=(int) (Math.random() * (containers.size()));

					break;
				case 10:
					if (MyProblem.oldoperation10 != -1 ) {
						randomInt = (int) (MyProblem.oldoperation10);
						solution.setpreviousgeneration(true);
					} else  {
						randomInt=choosefirstindex( randomInt, size, solution);
						solution.setpreviousgeneration(false);
					}

					// randomInt=(int) (Math.random() * (containers.size()));

					break;
				case 11:
					if (MyProblem.oldoperation11 != -1) {
						randomInt = (int) (MyProblem.oldoperation11);
						solution.setpreviousgeneration(true);
					} else {
						randomInt=choosefirstindex( randomInt, size, solution);
						solution.setpreviousgeneration(false);
					}

					// randomInt=(int) (Math.random() * (containers.size()));

					break;
				case 12:
					if (MyProblem.oldoperation12 != -1) {
						randomInt = (int) (MyProblem.oldoperation12);
						solution.setpreviousgeneration(true);
					} else {
						randomInt=choosefirstindex( randomInt, size, solution);
						solution.setpreviousgeneration(false);
					}

					// randomInt=(int) (Math.random() * (containers.size()));

					break;
				case 13:
					if (MyProblem.oldoperation13 != -1) {
						randomInt = (int) (MyProblem.oldoperation13);
						solution.setpreviousgeneration(true);
					} else {
						randomInt=choosefirstindex( randomInt, size, solution);
						solution.setpreviousgeneration(false);
					}

					break;
				case 14:
					if (MyProblem.oldoperation14 != -1) {
						randomInt = (int) (MyProblem.oldoperation14);
						solution.setpreviousgeneration(true);
					} else {
						randomInt=choosefirstindex( randomInt, size, solution);
						solution.setpreviousgeneration(false);
					}

					break;

				}

			} else if (Operations.statecase.equals("case3") || Operations.statecase.equals("case7")) {
				// if (MyProblem.oldoperation5!=-1)
				// randomInt=(int) ( MyProblem.oldoperation5);
				// else

			}

		} else {
			{
				boolean uniqindex=false;
				System.out.println(size);
				while( uniqindex==false) {
				Random number_generator = new Random();
				if (size > 1)
					randomInt = number_generator.nextInt(size);
				else
					randomInt = 0;
				checkmutationapply = true;
				solution.setpreviousgeneration(false);
				ArrayList<Integer> numcase5 = new ArrayList<Integer>(); 
				for(int p=0;p <solution.getoperations().size();p++)
					if(p!=MyProblem.indexoperation-1 && solution.getoperations().get(p)==2)
						numcase5.add(p);
				System.out.println(numcase5);
				for(int p=0;p<numcase5.size();p++) {
					if(solution.listfirstindices.get( numcase5.get(p))!=randomInt)
						uniqindex=true;
		
					}
				if(numcase5.size()==0) 
					uniqindex=true;
				
				}
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
 private int choosefirstindex(int randomInt,int size,Solution solution) {
	 boolean uniqindex=false;
		while( uniqindex==false) {
		Random number_generator = new Random();
		System.out.println(size);
		if (size > 1)
			randomInt = number_generator.nextInt(size);
		else
			randomInt = 0;
		
		solution.setpreviousgeneration(false);
		if(Operations.statecase.equals("case2")) {
		ArrayList<Integer> numcase5 = new ArrayList<Integer>(); 
		for(int p=0;p <solution.getoperations().size();p++)
			if(p!=MyProblem.indexoperation-1 && solution.getoperations().get(p)==2)
				numcase5.add(p);
		System.out.println(randomInt);
		System.out.println(numcase5);
		for(int p=0;p<numcase5.size();p++) {
			if(solution.listfirstindices.get( numcase5.get(p))!=randomInt)
				uniqindex=true;

			}
		if(numcase5.size()==0) 
			uniqindex=true;
		}
		else
			uniqindex=true;
		}
		return randomInt;
 }
	private ArrayList<Integer> realsecondindex(int replace, Solution solution, int randomInt, int randomInt2) {
		ArrayList<Integer> realrunsecondIndex = new ArrayList<Integer>();
		realrunsecondIndex.add(replace);
	/*	System.out.println("start real");
		System.out.println(replace);
		System.out.println(randomInt);
		System.out.println(randomInt2);*/
		int temp = replace;
	//	System.out.println(solution.TwoIndexDeletion);
		for (int h = 0; h < solution.TwoIndexDeletion.size(); h++) {
			if (solution.TwoIndexDeletion.get(h) == randomInt) {
				if (solution.TwoIndexDeletion.get(h + 1) <= temp) {
				//	System.out.println("randomint2");
					randomInt2 = (int) (temp) - 1;
					temp = randomInt2;
				}
				if (solution.TwoIndexDeletion.get(h + 1) == replace) {
					randomInt2 = -1;
				}
			}
			h = h + 1;
			if (randomInt2 == -1) {
				break;
			}

		}
	//	System.out.println(randomInt2);
		if (randomInt2 == -1) {
			realrunsecondIndex.clear();
			realrunsecondIndex = new ArrayList<Integer>();
			realrunsecondIndex.add(-1);
			realrunsecondIndex.add(-1);
		}

		else if (randomInt2 == -2)
			realrunsecondIndex.add(replace);
		else
			realrunsecondIndex.add(randomInt2);
		return realrunsecondIndex;

	}

	private ArrayList<Integer> receivesecondrandomInt(int randomInt2, int size, Solution solution, int randomInt) {
		// TODO Auto-generated method stub
		ArrayList<Integer> realrunsecondIndex = new ArrayList<Integer>();
	//	System.out.println(NSGAII.statemutcrossinitial);

		if (NSGAII.statemutcrossinitial.equals("mutation")) {

			if (Operations.statecase.equals("case5") || Operations.statecase.equals("case7")) {
				NSGAII.writer.println("apply case5 dovom");
				// NSGAII.writer.println(link.size());

				switch (MyProblem.indexoperation) {
				case 1:if (solution.inorout.get(0).equals("empty")) {
					if (MyProblem.replaceoperation1 != -1) {

						realrunsecondIndex = realsecondindex((int) (MyProblem.replaceoperation1), solution, randomInt,
								randomInt2);
					} else {
						randomInt2 =  ChooseRandomInt( size, randomInt2, solution, randomInt);
						realrunsecondIndex.add(randomInt2);
						realrunsecondIndex.add(randomInt2);

					}
				}else {
					
					realrunsecondIndex.add(MyProblem.replaceoperation1);
					realrunsecondIndex.add(MyProblem.replaceoperation1);

					
				}
					
					// randomInt2=(int) (Math.random() * (size-1));
					break;
				case 2:if (solution.inorout.get(1).equals("empty")) {
					if (MyProblem.replaceoperation2 != -1) {

						realrunsecondIndex = realsecondindex((int) (MyProblem.replaceoperation2), solution, randomInt,
								randomInt2);
						// System.out.println("55555555");
						// System.out.println(randomInt2);
					} else {
						randomInt2 =  ChooseRandomInt( size, randomInt2, solution, randomInt);
						realrunsecondIndex.add(randomInt2);
						realrunsecondIndex.add(randomInt2);

					}
				}else {
					
					realrunsecondIndex.add(MyProblem.replaceoperation2);
					realrunsecondIndex.add(MyProblem.replaceoperation2);

					
				}

					// randomInt2=(int) (Math.random() * (size-1));
					break;
				case 3:if (solution.inorout.get(2).equals("empty")) {
					if (MyProblem.replaceoperation3 != -1) {

						realrunsecondIndex = realsecondindex((int) (MyProblem.replaceoperation3), solution, randomInt,
								randomInt2);
						// System.out.println("55555555");
						// System.out.println(randomInt2);
					} else {
						randomInt2 =  ChooseRandomInt( size, randomInt2, solution, randomInt);
						realrunsecondIndex.add(randomInt2);
						realrunsecondIndex.add(randomInt2);

					}
			}else {
				
				realrunsecondIndex.add(MyProblem.replaceoperation3);
				realrunsecondIndex.add(MyProblem.replaceoperation3);

				
			}

					// randomInt2=(int) (Math.random() * (size-1));
					break;
				case 4:if (solution.inorout.get(3).equals("empty")) {
					if (MyProblem.replaceoperation4 != -1) {

						realrunsecondIndex = realsecondindex((int) (MyProblem.replaceoperation4), solution, randomInt,
								randomInt2);
						// System.out.println("55555555");
						// System.out.println(randomInt2);
					} else {
						randomInt2 =  ChooseRandomInt( size, randomInt2, solution, randomInt);
						realrunsecondIndex.add(randomInt2);
						realrunsecondIndex.add(randomInt2);

					}
		}else {
			
			realrunsecondIndex.add(MyProblem.replaceoperation4);
			realrunsecondIndex.add(MyProblem.replaceoperation4);

			
		}

					// randomInt2=(int) (Math.random() * (size-1));
					break;
				case 5:if (solution.inorout.get(4).equals("empty")) {
					if (MyProblem.replaceoperation5 != -1) {

						realrunsecondIndex = realsecondindex((int) (MyProblem.replaceoperation5), solution, randomInt,
								randomInt2);
						// System.out.println("55555555");
						// System.out.println(randomInt2);
					} else {
						randomInt2 =  ChooseRandomInt( size, randomInt2, solution, randomInt);
						realrunsecondIndex.add(randomInt2);
						realrunsecondIndex.add(randomInt2);

					}
	}
					else {
						
						realrunsecondIndex.add(MyProblem.replaceoperation5);
						realrunsecondIndex.add(MyProblem.replaceoperation5);

						
					}

					// randomInt2=(int) (Math.random() * (size-1));
					break;

				case 6:if (solution.inorout.get(5).equals("empty")) {
					if (MyProblem.replaceoperation6 != -1) {

						realrunsecondIndex = realsecondindex((int) (MyProblem.replaceoperation6), solution, randomInt,
								randomInt2);
						// System.out.println("55555555");
						// System.out.println(randomInt2);
					} else {
						randomInt2 =  ChooseRandomInt( size, randomInt2, solution, randomInt);
						realrunsecondIndex.add(randomInt2);
						realrunsecondIndex.add(randomInt2);

					}
				}else {
					
					realrunsecondIndex.add(MyProblem.replaceoperation6);
					realrunsecondIndex.add(MyProblem.replaceoperation6);

					
				}

					// randomInt2=(int) (Math.random() * (size-1));
					break;

				case 7:if (solution.inorout.get(6).equals("empty")) {
					if (MyProblem.replaceoperation7 != -1) {

						realrunsecondIndex = realsecondindex((int) (MyProblem.replaceoperation7), solution, randomInt,
								randomInt2);
						// System.out.println("55555555");
						// System.out.println(randomInt2);
					} else {
						randomInt2 =  ChooseRandomInt( size, randomInt2, solution, randomInt);
						realrunsecondIndex.add(randomInt2);
						realrunsecondIndex.add(randomInt2);

					}}
				else {
					
					realrunsecondIndex.add(MyProblem.replaceoperation7);
					realrunsecondIndex.add(MyProblem.replaceoperation7);

					
				}

					// randomInt2=(int) (Math.random() * (size-1));
					break;
				case 8:if (solution.inorout.get(7).equals("empty")) {
					if (MyProblem.replaceoperation8 != -1) {

						realrunsecondIndex = realsecondindex((int) (MyProblem.replaceoperation8), solution, randomInt,
								randomInt2);
						// System.out.println("55555555");
						// System.out.println(randomInt2);
					} else {
						randomInt2 =  ChooseRandomInt( size, randomInt2, solution, randomInt);
						realrunsecondIndex.add(randomInt2);
						realrunsecondIndex.add(randomInt2);

					}
			}else {
				
				realrunsecondIndex.add(MyProblem.replaceoperation8);
				realrunsecondIndex.add(MyProblem.replaceoperation8);

				
			}

					// randomInt2=(int) (Math.random() * (size-1));
					break;

				case 9:if (solution.inorout.get(8).equals("empty")) {
					if (MyProblem.replaceoperation9 != -1) {

						realrunsecondIndex = realsecondindex((int) (MyProblem.replaceoperation9), solution, randomInt,
								randomInt2);
						// System.out.println("55555555");
						// System.out.println(randomInt2);
					} else {
						randomInt2 =  ChooseRandomInt( size, randomInt2, solution, randomInt);
						realrunsecondIndex.add(randomInt2);
						realrunsecondIndex.add(randomInt2);

					}
		}
		else {
			
			realrunsecondIndex.add(MyProblem.replaceoperation9);
			realrunsecondIndex.add(MyProblem.replaceoperation9);

			
		}

					// randomInt2=(int) (Math.random() * (size-1));
					break;
				case 10:if (solution.inorout.get(9).equals("empty")) {
					if (MyProblem.replaceoperation10 != -1) {

						realrunsecondIndex = realsecondindex((int) (MyProblem.replaceoperation10), solution, randomInt,
								randomInt2);
						// System.out.println("55555555");
						// System.out.println(randomInt2);
					} else {
						randomInt2 =  ChooseRandomInt( size, randomInt2, solution, randomInt);
						realrunsecondIndex.add(randomInt2);
						realrunsecondIndex.add(randomInt2);

					}
	}else {
		
		realrunsecondIndex.add(MyProblem.replaceoperation10);
		realrunsecondIndex.add(MyProblem.replaceoperation10);

		
	}

					// randomInt2=(int) (Math.random() * (size-1));
					break;
				case 11:if (solution.inorout.get(10).equals("empty")) {
					if (MyProblem.replaceoperation11 != -1) {

						realrunsecondIndex = realsecondindex((int) (MyProblem.replaceoperation11), solution, randomInt,
								randomInt2);
						// System.out.println("55555555");
						// System.out.println(randomInt2);
					} else {
						randomInt2 =  ChooseRandomInt( size, randomInt2, solution, randomInt);
						realrunsecondIndex.add(randomInt2);
						realrunsecondIndex.add(randomInt2);

					}
				}else {
					
					realrunsecondIndex.add(MyProblem.replaceoperation11);
					realrunsecondIndex.add(MyProblem.replaceoperation11);

					
				}

					// randomInt2=(int) (Math.random() * (size-1));
					break;
				case 12:if (solution.inorout.get(11).equals("empty")) {
					if (MyProblem.replaceoperation12 != -1) {

						realrunsecondIndex = realsecondindex((int) (MyProblem.replaceoperation12), solution, randomInt,
								randomInt2);
						// System.out.println("55555555");
						// System.out.println(randomInt2);
					} else {
						randomInt2 =  ChooseRandomInt( size, randomInt2, solution, randomInt);
						realrunsecondIndex.add(randomInt2);
						realrunsecondIndex.add(randomInt2);

					}
				}else {
					
					realrunsecondIndex.add(MyProblem.replaceoperation12);
					realrunsecondIndex.add(MyProblem.replaceoperation12);

					
				}

					// randomInt2=(int) (Math.random() * (size-1));
					break;

				case 13:if (solution.inorout.get(12).equals("empty")) {
					if (MyProblem.replaceoperation13 != -1) {

						realrunsecondIndex = realsecondindex((int) (MyProblem.replaceoperation13), solution, randomInt,
								randomInt2);
						// System.out.println("55555555");
						// System.out.println(randomInt2);
					} else {
						randomInt2 =  ChooseRandomInt( size, randomInt2, solution, randomInt);
						realrunsecondIndex.add(randomInt2);
						realrunsecondIndex.add(randomInt2);

					}
			}else {
				
				realrunsecondIndex.add(MyProblem.replaceoperation13);
				realrunsecondIndex.add(MyProblem.replaceoperation13);

				
			}

					// randomInt2=(int) (Math.random() * (size-1));
					break;
				case 14:if (solution.inorout.get(13).equals("empty")) {
					if (MyProblem.replaceoperation14 != -1) {

						realrunsecondIndex = realsecondindex((int) (MyProblem.replaceoperation14), solution, randomInt,
								randomInt2);
						// System.out.println("55555555");
						// System.out.println(randomInt2);
					} else {
						randomInt2 =  ChooseRandomInt( size, randomInt2, solution, randomInt);
						realrunsecondIndex.add(randomInt2);
						realrunsecondIndex.add(randomInt2);

					}
		}else {
			
			realrunsecondIndex.add(MyProblem.replaceoperation14);
			realrunsecondIndex.add(MyProblem.replaceoperation14);

			
		}

					// randomInt2=(int) (Math.random() * (size-1));
					break;
				case 15:if (solution.inorout.get(14).equals("empty")) {
					if (MyProblem.replaceoperation15 != -1) {

						realrunsecondIndex = realsecondindex((int) (MyProblem.replaceoperation15), solution, randomInt,
								randomInt2);
						// System.out.println("55555555");
						// System.out.println(randomInt2);
					} else {
						randomInt2 =  ChooseRandomInt( size, randomInt2, solution, randomInt);
						realrunsecondIndex.add(randomInt2);
						realrunsecondIndex.add(randomInt2);

					}
}else {
		
		realrunsecondIndex.add(MyProblem.replaceoperation15);
		realrunsecondIndex.add(MyProblem.replaceoperation15);

		
	}

					// randomInt2=(int) (Math.random() * (size-1));
					break;

				case 16:if (solution.inorout.get(15).equals("empty")) {
					if (MyProblem.replaceoperation16 != -1) {

						realrunsecondIndex = realsecondindex((int) (MyProblem.replaceoperation16), solution, randomInt,
								randomInt2);
						// System.out.println("55555555");
						// System.out.println(randomInt2);
					} else {
						randomInt2 =  ChooseRandomInt( size, randomInt2, solution, randomInt);
						realrunsecondIndex.add(randomInt2);
						realrunsecondIndex.add(randomInt2);

					}
}else {
	
	realrunsecondIndex.add(MyProblem.replaceoperation16);
	realrunsecondIndex.add(MyProblem.replaceoperation16);

	
}

					// randomInt2=(int) (Math.random() * (size-1));
					break;

				case 17:if (solution.inorout.get(16).equals("empty")) {
					if (MyProblem.replaceoperation17 != -1) {

						realrunsecondIndex = realsecondindex((int) (MyProblem.replaceoperation17), solution, randomInt,
								randomInt2);
						// System.out.println("55555555");
						// System.out.println(randomInt2);
					} else {
						randomInt2 =  ChooseRandomInt( size, randomInt2, solution, randomInt);
						realrunsecondIndex.add(randomInt2);
						realrunsecondIndex.add(randomInt2);

					}
			}
					else {
						
						realrunsecondIndex.add(MyProblem.replaceoperation17);
						realrunsecondIndex.add(MyProblem.replaceoperation17);

						
					}


					// randomInt2=(int) (Math.random() * (size-1));
					break;
				case 18:if (solution.inorout.get(17).equals("empty")) {
					if (MyProblem.replaceoperation18 != -1) {

						realrunsecondIndex = realsecondindex((int) (MyProblem.replaceoperation18), solution, randomInt,
								randomInt2);
						// System.out.println("55555555");
						// System.out.println(randomInt2);
					} else {
						randomInt2 =  ChooseRandomInt( size, randomInt2, solution, randomInt);
						realrunsecondIndex.add(randomInt2);
						realrunsecondIndex.add(randomInt2);

					}
			}
					else {
						
						realrunsecondIndex.add(MyProblem.replaceoperation18);
						realrunsecondIndex.add(MyProblem.replaceoperation18);

						
					}


					// randomInt2=(int) (Math.random() * (size-1));
					break;

				case 19:if (solution.inorout.get(18).equals("empty")) {
					if (MyProblem.replaceoperation19 != -1) {

						realrunsecondIndex = realsecondindex((int) (MyProblem.replaceoperation19), solution, randomInt,
								randomInt2);
						// System.out.println("55555555");
						// System.out.println(randomInt2);
					} else {
						randomInt2 =  ChooseRandomInt( size, randomInt2, solution, randomInt);
						realrunsecondIndex.add(randomInt2);
						realrunsecondIndex.add(randomInt2);

					}
				}
				else {
					
					realrunsecondIndex.add(MyProblem.replaceoperation19);
					realrunsecondIndex.add(MyProblem.replaceoperation19);

					
				}

					// randomInt2=(int) (Math.random() * (size-1));
					break;
				case 20:if (solution.inorout.get(19).equals("empty")) {
					if (MyProblem.replaceoperation20 != -1) {

						realrunsecondIndex = realsecondindex((int) (MyProblem.replaceoperation20), solution, randomInt,
								randomInt2);
						// System.out.println("55555555");
						// System.out.println(randomInt2);
					} else {
						randomInt2 =  ChooseRandomInt( size, randomInt2, solution, randomInt);
						realrunsecondIndex.add(randomInt2);
						realrunsecondIndex.add(randomInt2);
					}
				}
				else {
					
					realrunsecondIndex.add(MyProblem.replaceoperation20);
					realrunsecondIndex.add(MyProblem.replaceoperation20);

					
				}

					// randomInt2=(int) (Math.random() * (size-1));
					break;
				case 21:if (solution.inorout.get(20).equals("empty")) {
					if (MyProblem.replaceoperation21 != -1) {

						realrunsecondIndex = realsecondindex((int) (MyProblem.replaceoperation21), solution, randomInt,
								randomInt2);
						// System.out.println("55555555");
						// System.out.println(randomInt2);
					} else {
						randomInt2 =  ChooseRandomInt( size, randomInt2, solution, randomInt);
						realrunsecondIndex.add(randomInt2);
						realrunsecondIndex.add(randomInt2);
					}
				}
				else {
					
					realrunsecondIndex.add(MyProblem.replaceoperation21);
					realrunsecondIndex.add(MyProblem.replaceoperation21);

					
				}

					// randomInt2=(int) (Math.random() * (size-1));
					break;
				case 22:if (solution.inorout.get(21).equals("empty")) {
					if (MyProblem.replaceoperation22 != -1) {

						realrunsecondIndex = realsecondindex((int) (MyProblem.replaceoperation22), solution, randomInt,
								randomInt2);
						// System.out.println("55555555");
						// System.out.println(randomInt2);
					} else {
						
							randomInt2 =  ChooseRandomInt( size, randomInt2, solution, randomInt);
						
						realrunsecondIndex.add(randomInt2);
						realrunsecondIndex.add(randomInt2);
					}
				}
				else {
					
					realrunsecondIndex.add(MyProblem.replaceoperation22);
					realrunsecondIndex.add(MyProblem.replaceoperation22);

					
				}

					// randomInt2=(int) (Math.random() * (size-1));
					break;

				}
			}

		} else {
			boolean uniqindex=false;
			while(uniqindex==false) {
			Random number_generator = new Random();
			if (size > 1)
				randomInt2 = number_generator.nextInt(size);
			else
				randomInt2 = 0;
			
			uniqindex=chooseuniquecasedelet(solution,  randomInt, randomInt2);
			}
			realrunsecondIndex.add(randomInt2);
			realrunsecondIndex.add(randomInt2);

		}

	//	System.out.println("mutation");
	//	System.out.println(size);
	//	System.out.println(randomInt2);
		return realrunsecondIndex;

	}
    private int ChooseRandomInt(int size,int randomInt2,Solution solution,int randomInt) {
    	
		Random number_generator = new Random();
		if (size > 1)
			randomInt2 = number_generator.nextInt(size);
		else
			randomInt2 = 0;
		
		
	return randomInt2;
    }
	private boolean chooseuniquecasedelet(Solution solution, int randomInt,int randomInt2) {
		// TODO Auto-generated method stub
		boolean uniqindex=false;
		ArrayList<Integer> numcase5 = new ArrayList<Integer>(); 
		for(int p=0;p <solution.getoperations().size();p++)
			if(p!=MyProblem.indexoperation-1 && solution.getoperations().get(p)==5)
				numcase5.add(p);
		for(int p=0;p<numcase5.size();p++) {
			if(solution.listfirstindices.get( numcase5.get(p))!=randomInt)
				uniqindex=true;

			if(solution.listfirstindices.get( numcase5.get(p))==randomInt && solution.listsecondindices.get( numcase5.get(p))!=randomInt2)
				uniqindex=true;
		}
		if(numcase5.size()==0) 
			uniqindex=true;
	/*	System.out.println(solution.listfirstindices);
		System.out.println(solution.listsecondindices);
		System.out.println(randomInt);
		System.out.println(randomInt2);*/
		return uniqindex;
		
	}

	private int findrealsecondindex(int randomInt, int randomInt2, Solution solution) {
		for (int h = 0; h < solution.TwoIndexDeletion.size(); h++) {
			if (solution.TwoIndexDeletion.get(h) == randomInt)
				if (solution.TwoIndexDeletion.get(h + 1) <= randomInt2)
					randomInt2 = randomInt2 + 1;
			h = h + 1;

		}
		solution.TwoIndexDeletion.add(randomInt);
		solution.TwoIndexDeletion.add(randomInt2);
		return randomInt2;

	}

	private int updaterandomInt(int randomInt, ArrayList<Integer> tempsecondint, Solution solution) {
		// TODO Auto-generated method stub
		int newsecondindex = 0;
		switch (MyProblem.indexoperation) {

		case 1:
			// if (MyProblem.oldoperation1 == -1 || MyProblem.oldoperation1 == -2) {

			// }
			if (solution.inorout.get(0).equals("empty")) {
				if (tempsecondint.get(0) != -1) {
					if (tempsecondint.get(0) == tempsecondint.get(1))
						newsecondindex = findrealsecondindex(randomInt, tempsecondint.get(1), solution);
					else {
						newsecondindex = tempsecondint.get(0);
						solution.TwoIndexDeletion.add(randomInt);
						solution.TwoIndexDeletion.add(newsecondindex);

					}
				} else {
					randomInt = -2;
					newsecondindex = -2;
				}
			} else {
				newsecondindex = tempsecondint.get(0);
				solution.TwoIndexDeletion.add(randomInt);
				solution.TwoIndexDeletion.add(newsecondindex);

			}
			MyProblem.oldoperation1 = randomInt;
			MyProblem.replaceoperation1 = newsecondindex;
			break;
		case 2:
			if(solution.inorout.get(1).equals("empty")) {
			if (tempsecondint.get(0) != -1) {
				if (tempsecondint.get(0) == tempsecondint.get(1))
					newsecondindex = findrealsecondindex(randomInt, tempsecondint.get(1), solution);
				else {
					newsecondindex = tempsecondint.get(0);
					solution.TwoIndexDeletion.add(randomInt);
					solution.TwoIndexDeletion.add(newsecondindex);

				}
			} else {
				randomInt = -2;
				newsecondindex = -2;
			}
			}
			else {
				newsecondindex = tempsecondint.get(0);
				solution.TwoIndexDeletion.add(randomInt);
				solution.TwoIndexDeletion.add(newsecondindex);
			}

			MyProblem.oldoperation2 = randomInt;
			MyProblem.replaceoperation2 = newsecondindex;
			break;
		case 3:
			if(solution.inorout.get(2).equals("empty")) {
			if (tempsecondint.get(0) != -1) {
				if (tempsecondint.get(0) == tempsecondint.get(1))
					newsecondindex = findrealsecondindex(randomInt, tempsecondint.get(1), solution);
				else {
					newsecondindex = tempsecondint.get(0);
					solution.TwoIndexDeletion.add(randomInt);
					solution.TwoIndexDeletion.add(newsecondindex);

				}
			} else {
				randomInt = -2;
				newsecondindex = -2;
			}
			}
			else {
				newsecondindex = tempsecondint.get(0);
				solution.TwoIndexDeletion.add(randomInt);
				solution.TwoIndexDeletion.add(newsecondindex);
			}
			MyProblem.oldoperation3 = randomInt;
			MyProblem.replaceoperation3 = newsecondindex;

			break;
		case 4:
			if(solution.inorout.get(3).equals("empty")) {
			if (tempsecondint.get(0) != -1) {
				if (tempsecondint.get(0) == tempsecondint.get(1))
					newsecondindex = findrealsecondindex(randomInt, tempsecondint.get(1), solution);
				else {
					newsecondindex = tempsecondint.get(0);
					solution.TwoIndexDeletion.add(randomInt);
					solution.TwoIndexDeletion.add(newsecondindex);

				}
			} else {
				randomInt = -2;
				newsecondindex = -2;
			}
			}else {
				newsecondindex = tempsecondint.get(0);
				solution.TwoIndexDeletion.add(randomInt);
				solution.TwoIndexDeletion.add(newsecondindex);
			}

			MyProblem.oldoperation4 = randomInt;

			MyProblem.replaceoperation4 = newsecondindex;
			break;
		case 5:
			if(solution.inorout.get(4).equals("empty")) {
			if (tempsecondint.get(0) != -1) {
				if (tempsecondint.get(0) == tempsecondint.get(1))
					newsecondindex = findrealsecondindex(randomInt, tempsecondint.get(1), solution);
				else {
					newsecondindex = tempsecondint.get(0);
					solution.TwoIndexDeletion.add(randomInt);
					solution.TwoIndexDeletion.add(newsecondindex);

				}
			} else {
				randomInt = -2;
				newsecondindex = -2;
			}}
			else {
				newsecondindex = tempsecondint.get(0);
				solution.TwoIndexDeletion.add(randomInt);
				solution.TwoIndexDeletion.add(newsecondindex);
			}

			MyProblem.oldoperation5 = randomInt;
			MyProblem.replaceoperation5 = newsecondindex;

			break;
		case 6:
			if(solution.inorout.get(5).equals("empty")) {
			if (tempsecondint.get(0) != -1) {
				if (tempsecondint.get(0) == tempsecondint.get(1))
					newsecondindex = findrealsecondindex(randomInt, tempsecondint.get(1), solution);
				else {
					newsecondindex = tempsecondint.get(0);
					solution.TwoIndexDeletion.add(randomInt);
					solution.TwoIndexDeletion.add(newsecondindex);

				}
			} else {
				randomInt = -2;
				newsecondindex = -2;
			}
			}
			else {
				newsecondindex = tempsecondint.get(0);
				solution.TwoIndexDeletion.add(randomInt);
				solution.TwoIndexDeletion.add(newsecondindex);
			}
			MyProblem.oldoperation6 = randomInt;
			MyProblem.replaceoperation6 = newsecondindex;

			break;
		case 7:
			if(solution.inorout.get(6).equals("empty")) {
			if (tempsecondint.get(0) != -1) {
				if (tempsecondint.get(0) == tempsecondint.get(1))
					newsecondindex = findrealsecondindex(randomInt, tempsecondint.get(1), solution);
				else {
					newsecondindex = tempsecondint.get(0);
					solution.TwoIndexDeletion.add(randomInt);
					solution.TwoIndexDeletion.add(newsecondindex);

				}
			} else {
				randomInt = -2;
				newsecondindex = -2;
			}
			}
			else {
				newsecondindex = tempsecondint.get(0);
				solution.TwoIndexDeletion.add(randomInt);
				solution.TwoIndexDeletion.add(newsecondindex);
			}
			MyProblem.oldoperation7 = randomInt;
			MyProblem.replaceoperation7 = newsecondindex;

			break;
		case 8:
			if(solution.inorout.get(7).equals("empty")) {
			if (tempsecondint.get(0) != -1) {
				if (tempsecondint.get(0) == tempsecondint.get(1))
					newsecondindex = findrealsecondindex(randomInt, tempsecondint.get(1), solution);
				else {
					newsecondindex = tempsecondint.get(0);
					solution.TwoIndexDeletion.add(randomInt);
					solution.TwoIndexDeletion.add(newsecondindex);

				}
			} else {
				randomInt = -2;
				newsecondindex = -2;
			}
			}else {
				newsecondindex = tempsecondint.get(0);
				solution.TwoIndexDeletion.add(randomInt);
				solution.TwoIndexDeletion.add(newsecondindex);
			}
			MyProblem.oldoperation8 = randomInt;
			MyProblem.replaceoperation8 = newsecondindex;

			break;
		case 9:if(solution.inorout.get(8).equals("empty")) {
			if (tempsecondint.get(0) != -1) {
				if (tempsecondint.get(0) == tempsecondint.get(1))
					newsecondindex = findrealsecondindex(randomInt, tempsecondint.get(1), solution);
				else {
					newsecondindex = tempsecondint.get(0);
					solution.TwoIndexDeletion.add(randomInt);
					solution.TwoIndexDeletion.add(newsecondindex);

				}
			} else {
				randomInt = -2;
				newsecondindex = -2;
			}
		}else {
			newsecondindex = tempsecondint.get(0);
			solution.TwoIndexDeletion.add(randomInt);
			solution.TwoIndexDeletion.add(newsecondindex);
		}
			MyProblem.oldoperation9 = randomInt;
			MyProblem.replaceoperation9 = newsecondindex;

			break;
		case 10:if(solution.inorout.get(9).equals("empty")) {
			if (tempsecondint.get(0) != -1) {
				if (tempsecondint.get(0) == tempsecondint.get(1))
					newsecondindex = findrealsecondindex(randomInt, tempsecondint.get(1), solution);
				else {
					newsecondindex = tempsecondint.get(0);
					solution.TwoIndexDeletion.add(randomInt);
					solution.TwoIndexDeletion.add(newsecondindex);

				}
			} else {
				randomInt = -2;
				newsecondindex = -2;
			}}else {
				newsecondindex = tempsecondint.get(0);
				solution.TwoIndexDeletion.add(randomInt);
				solution.TwoIndexDeletion.add(newsecondindex);
			}

			MyProblem.oldoperation10 = randomInt;

			MyProblem.replaceoperation10 = newsecondindex;
			break;
		case 11:if(solution.inorout.get(10).equals("empty")) {
			if (tempsecondint.get(0) != -1) {
				if (tempsecondint.get(0) == tempsecondint.get(1))
					newsecondindex = findrealsecondindex(randomInt, tempsecondint.get(1), solution);
				else {
					newsecondindex = tempsecondint.get(0);
					solution.TwoIndexDeletion.add(randomInt);
					solution.TwoIndexDeletion.add(newsecondindex);

				}
			} else {
				randomInt = -2;
				newsecondindex = -2;
			}
		}else {
			newsecondindex = tempsecondint.get(0);
			solution.TwoIndexDeletion.add(randomInt);
			solution.TwoIndexDeletion.add(newsecondindex);
		}
			MyProblem.oldoperation11 = randomInt;

			MyProblem.replaceoperation11 = newsecondindex;
			break;
		case 12:
			if(solution.inorout.get(11).equals("empty")) {
			if (tempsecondint.get(0) != -1) {
				if (tempsecondint.get(0) == tempsecondint.get(1))
					newsecondindex = findrealsecondindex(randomInt, tempsecondint.get(1), solution);
				else {
					newsecondindex = tempsecondint.get(0);
					solution.TwoIndexDeletion.add(randomInt);
					solution.TwoIndexDeletion.add(newsecondindex);
				}
			} else {
				randomInt = -2;
				newsecondindex = -2;
			}
			}else {
				newsecondindex = tempsecondint.get(0);
				solution.TwoIndexDeletion.add(randomInt);
				solution.TwoIndexDeletion.add(newsecondindex);
			}
			MyProblem.oldoperation12 = randomInt;
			MyProblem.replaceoperation12 = newsecondindex;

			break;
		case 13:
			if(solution.inorout.get(12).equals("empty")) {
			if (tempsecondint.get(0) != -1) {
				if (tempsecondint.get(0) == tempsecondint.get(1))
					newsecondindex = findrealsecondindex(randomInt, tempsecondint.get(1), solution);
				else {
					newsecondindex = tempsecondint.get(0);
					solution.TwoIndexDeletion.add(randomInt);
					solution.TwoIndexDeletion.add(newsecondindex);

				}
			} else {
				randomInt = -2;
				newsecondindex = -2;
			}
			}else {
				newsecondindex = tempsecondint.get(0);
				solution.TwoIndexDeletion.add(randomInt);
				solution.TwoIndexDeletion.add(newsecondindex);
			}
			MyProblem.oldoperation13 = randomInt;
			MyProblem.replaceoperation13 = newsecondindex;

			break;
		case 14:
			if(solution.inorout.get(13).equals("empty")) {
			if (tempsecondint.get(0) != -1) {
				if (tempsecondint.get(0) == tempsecondint.get(1))
					newsecondindex = findrealsecondindex(randomInt, tempsecondint.get(1), solution);
				else {
					newsecondindex = tempsecondint.get(0);
					solution.TwoIndexDeletion.add(randomInt);
					solution.TwoIndexDeletion.add(newsecondindex);

				}
			} else {
				randomInt = -2;
				newsecondindex = -2;
			}
			}else {
				newsecondindex = tempsecondint.get(0);
				solution.TwoIndexDeletion.add(randomInt);
				solution.TwoIndexDeletion.add(newsecondindex);
			}
			MyProblem.oldoperation14 = randomInt;
			MyProblem.replaceoperation14 = newsecondindex;

			break;
		case 15:
			if(solution.inorout.get(14).equals("empty")) {
			if (tempsecondint.get(0) != -1) {
				if (tempsecondint.get(0) == tempsecondint.get(1))
					newsecondindex = findrealsecondindex(randomInt, tempsecondint.get(1), solution);
				else {
					newsecondindex = tempsecondint.get(0);
					solution.TwoIndexDeletion.add(randomInt);
					solution.TwoIndexDeletion.add(newsecondindex);

				}
			} else {
				randomInt = -2;
				newsecondindex = -2;
			}
			}else {
				newsecondindex = tempsecondint.get(0);
				solution.TwoIndexDeletion.add(randomInt);
				solution.TwoIndexDeletion.add(newsecondindex);
			}
			MyProblem.oldoperation15 = randomInt;
			MyProblem.replaceoperation15 = newsecondindex;

			break;
		case 16:
			if(solution.inorout.get(15).equals("empty")) {
			if (tempsecondint.get(0) != -1) {
				if (tempsecondint.get(0) == tempsecondint.get(1))
					newsecondindex = findrealsecondindex(randomInt, tempsecondint.get(1), solution);
				else {
					newsecondindex = tempsecondint.get(0);
					solution.TwoIndexDeletion.add(randomInt);
					solution.TwoIndexDeletion.add(newsecondindex);

				}
			} else {
				randomInt = -2;
				newsecondindex = -2;
			}
			}else {
				newsecondindex = tempsecondint.get(0);
				solution.TwoIndexDeletion.add(randomInt);
				solution.TwoIndexDeletion.add(newsecondindex);
			}
			MyProblem.oldoperation16 = randomInt;
			MyProblem.replaceoperation16 = newsecondindex;

			break;
		case 17:
			if(solution.inorout.get(16).equals("empty")) {
			if (tempsecondint.get(0) != -1) {
				if (tempsecondint.get(0) == tempsecondint.get(1))
					newsecondindex = findrealsecondindex(randomInt, tempsecondint.get(1), solution);
				else {
					newsecondindex = tempsecondint.get(0);
					solution.TwoIndexDeletion.add(randomInt);
					solution.TwoIndexDeletion.add(newsecondindex);

				}
			} else {
				randomInt = -2;
				newsecondindex = -2;
			}
			}else {
				newsecondindex = tempsecondint.get(0);
				solution.TwoIndexDeletion.add(randomInt);
				solution.TwoIndexDeletion.add(newsecondindex);
			}
			MyProblem.oldoperation17 = randomInt;
			MyProblem.replaceoperation17 = newsecondindex;

			break;
		case 18:
			if(solution.inorout.get(17).equals("empty")) {
			if (tempsecondint.get(0) != -1) {
				if (tempsecondint.get(0) == tempsecondint.get(1))
					newsecondindex = findrealsecondindex(randomInt, tempsecondint.get(1), solution);
				else {
					newsecondindex = tempsecondint.get(0);
					solution.TwoIndexDeletion.add(randomInt);
					solution.TwoIndexDeletion.add(newsecondindex);

				}
			} else {
				randomInt = -2;
				newsecondindex = -2;
			}
			}else {
				newsecondindex = tempsecondint.get(0);
				solution.TwoIndexDeletion.add(randomInt);
				solution.TwoIndexDeletion.add(newsecondindex);
			}
			MyProblem.oldoperation18 = randomInt;
			MyProblem.replaceoperation18 = newsecondindex;

			break;
		case 19:
			if(solution.inorout.get(18).equals("empty")) {
			if (tempsecondint.get(0) != -1) {
				if (tempsecondint.get(0) == tempsecondint.get(1))
					newsecondindex = findrealsecondindex(randomInt, tempsecondint.get(1), solution);
				else {
					newsecondindex = tempsecondint.get(0);
					solution.TwoIndexDeletion.add(randomInt);
					solution.TwoIndexDeletion.add(newsecondindex);

				}
			} else {
				randomInt = -2;
				newsecondindex = -2;
			}
			}else {
				newsecondindex = tempsecondint.get(0);
				solution.TwoIndexDeletion.add(randomInt);
				solution.TwoIndexDeletion.add(newsecondindex);
			}
			MyProblem.oldoperation19 = randomInt;
			MyProblem.replaceoperation19 = newsecondindex;

			break;
		case 20:
			if(solution.inorout.get(19).equals("empty")) {
			if (tempsecondint.get(0) != -1) {
				if (tempsecondint.get(0) == tempsecondint.get(1))
					newsecondindex = findrealsecondindex(randomInt, tempsecondint.get(1), solution);
				else {
					newsecondindex = tempsecondint.get(0);
					solution.TwoIndexDeletion.add(randomInt);
					solution.TwoIndexDeletion.add(newsecondindex);

				}
			} else {
				randomInt = -2;
				newsecondindex = -2;
			}
			}else {
				newsecondindex = tempsecondint.get(0);
				solution.TwoIndexDeletion.add(randomInt);
				solution.TwoIndexDeletion.add(newsecondindex);
			}
			MyProblem.oldoperation20 = randomInt;

			MyProblem.replaceoperation20 = newsecondindex;
			break;
		case 21:
			if(solution.inorout.get(20).equals("empty")) {
			if (tempsecondint.get(0) != -1) {
				if (tempsecondint.get(0) == tempsecondint.get(1))
					newsecondindex = findrealsecondindex(randomInt, tempsecondint.get(1), solution);
				else {
					newsecondindex = tempsecondint.get(0);
					solution.TwoIndexDeletion.add(randomInt);
					solution.TwoIndexDeletion.add(newsecondindex);

				}
			} else {
				randomInt = -2;
				newsecondindex = -2;
			}
			}else {
				newsecondindex = tempsecondint.get(0);
				solution.TwoIndexDeletion.add(randomInt);
				solution.TwoIndexDeletion.add(newsecondindex);
			}
			MyProblem.oldoperation21 = randomInt;

			MyProblem.replaceoperation21 = newsecondindex;
			break;
		case 22:
			if(solution.inorout.get(21).equals("empty")) {
			if (tempsecondint.get(0) != -1) {
				if (tempsecondint.get(0) == tempsecondint.get(1))
					newsecondindex = findrealsecondindex(randomInt, tempsecondint.get(1), solution);
				else {
					newsecondindex = tempsecondint.get(0);
					solution.TwoIndexDeletion.add(randomInt);
					solution.TwoIndexDeletion.add(newsecondindex);

				}
			} else {
				randomInt = -2;
				newsecondindex = -2;
			}
			}else {
				newsecondindex = tempsecondint.get(0);
				solution.TwoIndexDeletion.add(randomInt);
				solution.TwoIndexDeletion.add(newsecondindex);
			}
			MyProblem.oldoperation22 = randomInt;
			MyProblem.replaceoperation22 = newsecondindex;

			break;

		}

		return newsecondindex;
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

		// // Should we want to serialize the model.
		// String injectedFile = "file:/" + atlTransformationFile + ".xmi";
		// IExtractor extractor = new EMFExtractor();
		// extractor.extract(atlModel, injectedFile);

		return atlModel;
	}

	public Resource retPackResouceMM(String MMPath) {
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore",
				new EcoreResourceFactoryImpl());
		URI fileURI = URI.createFileURI(MMPath);// ecore.getFullPath().toOSString());
		Resource resource = resourceSet.getResource(fileURI, true);
		return resource;
	}

	// }

	/*
	 * public static ATLModel getWrapper() {
	 * 
	 * return Tester.wrapper2; }
	 */

	public static void setWrapper(ATLModel wrapper) {
		AbstractDeletionMutator.wrapper = wrapper;
	}
}
