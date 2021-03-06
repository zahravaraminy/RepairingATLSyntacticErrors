package modification.feature.operator.refinementphase;

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
import org.eclipse.m2m.atl.core.emf.EMFModel;

import witness.generator.MetaModel;
import anatlyzer.atl.model.ATLModel;
import anatlyzer.atlext.ATL.Binding;
import ca.udem.fixingatlerror.refinementphase.main;
import ca.udem.fixingatlerror.refinementphase.Setting;
import ca.udem.fixingatlerror.refinementphase.Settingpostprocessing;
import ca.udem.fixingatlerror.refinementphase.Operationspostprocessing;
import jmetal.core.Solution;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.metaheuristics.nsgaII.NSGAIIpostprocessing;
import jmetal.problems.MyProblempostprocessing;
import transML.exceptions.transException;

public class BindingModificationMutatorpost extends AbstractFeatureModificationMutatorpost {

	@Override
	public List<Object> generateMutants(EMFModel atlModel, MetaModel inputMM, MetaModel outputMM, String outputFolder,
			ATLModel wrapper, Solution solution) {
		main.typeoperation = "binding";
		List<Object> comments = null;
		comments = this.genericAttributeModification(atlModel, outputFolder, Binding.class, "propertyName", outputMM,
				wrapper, solution);
		main.typeoperation = null;

		return comments;
	}

	@Override
	public String getDescription() {
		return "Binding-target Modification";
	}

	@Override
	protected List<Object> replacements(EMFModel atlModel, EClass oldFeatureValue, EObject object2modify,
			String currentAttributeValue, MetaModel metamodel) {
		// return object2modify instanceof Binding?

		// this.featureReplacements(atlModel,oldFeatureValue,toString(((Binding)object2modify).getOutPatternElement().getType()),
		// currentAttributeValue, metamodel) :
		// new ArrayList<Object>();

		// return
		// this.featureReplacements(atlModel,oldFeatureValue,toString(((Binding)object2modify).getOutPatternElement().getType()),
		// currentAttributeValue, metamodel);
		return this.featureReplacements(oldFeatureValue);
	}

	private List<Object> featureReplacements(EClass inputclassifier) {
		// TODO Auto-generated method stub
		Settingpostprocessing s = new Settingpostprocessing();
		List<Object> replacements = new ArrayList<Object>();
		String MMRootPath3 = s.getsourcemetamodel();
		MetaModel meta = null;
		try {
			meta = new MetaModel(MMRootPath3);
		} catch (transException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// NSGAII.writer.println("bind1");
		List<Object> mainclass = new ArrayList<Object>();
		List<EStructuralFeature> mainclass4 = new ArrayList<EStructuralFeature>();
		for (EClassifier classifier : meta.getEClassifiers()) {
			if (classifier instanceof EClass) {
				EClass child = ((EClass) classifier);
				// if(child.getName().equals(inputclassifier.getName())) {

				for (int y = 0; y < classifier.eContents().size(); y++) {

					if (classifier.eContents().get(y) instanceof EAttribute
							|| classifier.eContents().get(y) instanceof EReference)
						mainclass4.add((EStructuralFeature) classifier.eContents().get(y));

				}
				/*
				 * for (int y = 0; y < child.getEAllSuperTypes().size(); y++) { for (int
				 * y2=0;y2<child.getEAllSuperTypes().get(y).eContents().size();y2++) {
				 * 
				 * if (child.getEAllSuperTypes().get(y).eContents().get(y2) instanceof
				 * EAttribute ||child.getEAllSuperTypes().get(y).eContents().get(y2) instanceof
				 * EReference ) mainclass4.add( (EStructuralFeature)
				 * child.getEAllSuperTypes().get(y).eContents().get(y2));
				 * 
				 * }
				 * 
				 * 
				 * }
				 */

				// }

			}
			// mainclass.add((EClass)classifier.eContents());
		}

		// EClass mmtype = (EClass)metamodel.getEClassifier(type);
		for (EStructuralFeature o : mainclass4) {
			if (o != null) {
				mainclass.add(o.getName());
			}
		}
		// NSGAII.writer.println("rrr3");
		// System.out.println(mainclass);
		// NSGAII.writer.println(mainclass);

		return mainclass;
	}
}
