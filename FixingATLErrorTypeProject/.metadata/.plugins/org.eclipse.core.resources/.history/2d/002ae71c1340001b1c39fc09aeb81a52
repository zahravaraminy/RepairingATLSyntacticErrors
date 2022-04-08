package creation.operator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.core.ModelFactory;
import org.eclipse.m2m.atl.core.emf.EMFModel;
import org.eclipse.m2m.atl.core.emf.EMFModelFactory;
import org.eclipse.m2m.atl.core.emf.EMFReferenceModel;
import org.eclipse.m2m.atl.engine.parser.AtlParser;

import witness.generator.MetaModel;
import anatlyzer.atlext.ATL.ATLFactory;
import anatlyzer.atlext.ATL.MatchedRule;
import anatlyzer.atlext.ATL.Module;
import anatlyzer.atlext.ATL.PatternElement;
import anatlyzer.atlext.OCL.OCLFactory;
import anatlyzer.atlext.OCL.OclModel;
import anatlyzer.atlext.OCL.OclModelElement;
import anatlyzer.atl.model.ATLModel;
import evaluation.mutator.AbstractMutator;
import jmetal.core.Solution;
import deletion.operator.AbstractDeletionMutator;
//import anatlyzer.evaluation.tester.Tester;

public class OutElementCreationMutator extends AbstractMutator {

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
		return atlModel;
	}

	@Override
	public List<Object> generateMutants(EMFModel atlModel, MetaModel inputMM, MetaModel outputMM, String outputFolder,
			ATLModel wrapper, Solution solution) {

		List<Object> ReturnResult = new ArrayList<Object>();
		try {
			this.atlModel3 = this.loadTransformationModel(this.m);
		} catch (ATLCoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.save(this.atlModel3, outputFolder);
		wrapper = new ATLModel(this.atlModel3.getResource());

		EDataTypeEList<String> comments = null;
		String comment = null;
		Module module = wrapper.getModule();
		if (module != null) {
			EStructuralFeature feature = wrapper.source(module).eClass().getEStructuralFeature("commentsBefore");
			comments = (EDataTypeEList<String>) wrapper.source(module).eGet(feature);
		}

		List<MatchedRule> modifiable = (List<MatchedRule>) wrapper.allObjectsOf(MatchedRule.class);
		int randomInt3 = (int) (Math.random() * (modifiable.size()));
		// for each matched rule
		// for (MatchedRule rule :
		// (List<MatchedRule>)AbstractDeletionMutator.getWrapper().allObjectsOf(MatchedRule.class))
		// {
		if (modifiable.size() > 0) {
			// if (rule.getOutPattern()!=null) {
			if (modifiable.get(randomInt3).getOutPattern() != null) {
				// current out-pattern elements
				EStructuralFeature feature = wrapper.source(modifiable.get(randomInt3).getOutPattern()).eClass()
						.getEStructuralFeature("elements");
				List<PatternElement> realelements = (List<PatternElement>) wrapper
						.source(modifiable.get(randomInt3).getOutPattern()).eGet(feature);

				// new out-pattern elements
				List<PatternElement> newelements = new ArrayList<PatternElement>();
				newelements.addAll(this.getOutElement1(outputMM)); // out-pattern element for each meta-model class
				int randomInt2 = (int) (Math.random() * (newelements.size()));
				// for each new out-pattern element
				// for (PatternElement element : newelements) {
				if (newelements.size() > 0) {
					// if (element!=null) {
					if (newelements.get(randomInt2) != null) {
						// mutation: add out-pattern element
						// realelements.add(element);
						realelements.add(newelements.get(randomInt2));
						// mutation: documentation
						if (comments != null)
							comments.add("\n-- MUTATION \"" + this.getDescription() + "\" "
									+ toString(newelements.get(randomInt2)) + " in "
									+ toString(modifiable.get(randomInt3)) + " (line "
									+ modifiable.get(randomInt3).getLocation() + " of original transformation)\n");
						comment = "\n-- MUTATION \"" + this.getDescription() + "\" "
								+ toString(newelements.get(randomInt2)) + " in " + toString(modifiable.get(randomInt3))
								+ " (line " + modifiable.get(randomInt3).getLocation()
								+ " of original transformation)\n";
						// save mutant
						this.save(this.atlModel3, outputFolder);
						this.save2(this.atlModel3, outputFolder);

						// restore: remove added out-pattern element and comment
						// realelements.remove(element);
						// if (comments!=null) comments.remove(comments.size()-1);
					}
				}
			}
		}
		return ReturnResult;
	}

	@Override
	public String getDescription() {
		return "Creation of OutPattern Element";
	}

	/**
	 * It returns a list of out-pattern elements.
	 * 
	 * @param output
	 *            metamodel
	 */
	private List<PatternElement> getOutElement1(MetaModel metamodel) {
		List<PatternElement> elements = new ArrayList<PatternElement>();
		for (EClassifier classifier : metamodel.getEClassifiers()) {
			if (classifier instanceof EClass) {
				PatternElement element = ATLFactory.eINSTANCE.createSimpleInPatternElement();
				OclModelElement ome = OCLFactory.eINSTANCE.createOclModelElement();
				OclModel mm = OCLFactory.eINSTANCE.createOclModel();
				ome.setName(classifier.getName());
				mm.setName(metamodel.getName());
				ome.setModel(mm);
				element.setType(ome);
				element.setVarName("dummy" + ome.getName() + elements.size());
				elements.add(element);
			}
		}
		return elements;
	}
}
