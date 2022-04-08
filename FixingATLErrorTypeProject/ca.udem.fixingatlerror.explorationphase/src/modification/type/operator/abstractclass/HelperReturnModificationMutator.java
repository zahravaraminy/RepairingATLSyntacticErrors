package modification.type.operator.abstractclass;

import java.util.List;

import org.eclipse.m2m.atl.core.emf.EMFModel;

import witness.generator.MetaModel;
import anatlyzer.atl.model.ATLModel;
import anatlyzer.atlext.OCL.Attribute;
import anatlyzer.atlext.OCL.Operation;
import jmetal.core.Solution;

public class HelperReturnModificationMutator extends AbstractTypeModificationMutator {
	
	@Override
	public List<Object> generateMutants(EMFModel atlModel, MetaModel inputMM, MetaModel outputMM, String outputFolder,
			ATLModel wrapper,Solution solution) {
		return this.genericTypeModification(atlModel, outputFolder, Operation.class, "returnType", new MetaModel[] {inputMM, outputMM},
				wrapper,solution,inputMM,outputMM);
}
	
	@Override
	public String getDescription() {
		return "Helper Return Type Modification";
	}
}
