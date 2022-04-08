package modification.type.operator.abstractclass;

import java.util.List;

import org.eclipse.m2m.atl.core.emf.EMFModel;

import witness.generator.MetaModel;
import anatlyzer.atl.model.ATLModel;
import anatlyzer.atlext.OCL.OclContextDefinition;
import jmetal.core.Solution;

public class HelperContextModificationMutator extends AbstractTypeModificationMutator {
	
	@Override
	public List<Object> generateMutants(EMFModel atlModel, MetaModel inputMM, MetaModel outputMM, String outputFolder,
			ATLModel wrapper,Solution solution) {
		
		return this.genericTypeModification(atlModel, outputFolder, OclContextDefinition.class, "context_", new MetaModel[] {inputMM, outputMM},
				wrapper,solution,inputMM,outputMM);

	}
	
	@Override
	public String getDescription() {
		return "Helper Context Type Modification";
	}
}
