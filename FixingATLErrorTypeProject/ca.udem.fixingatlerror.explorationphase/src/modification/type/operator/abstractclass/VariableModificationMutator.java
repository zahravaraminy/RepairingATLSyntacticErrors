package modification.type.operator.abstractclass;

import java.util.List;

import org.eclipse.m2m.atl.core.emf.EMFModel;

import witness.generator.MetaModel;
import anatlyzer.atl.model.ATLModel;
import anatlyzer.atlext.OCL.VariableDeclaration;
import jmetal.core.Solution;

public class VariableModificationMutator extends AbstractTypeModificationMutator {
	
	@Override
	public List<Object> generateMutants(EMFModel atlModel, MetaModel inputMM, MetaModel outputMM, String outputFolder,
			ATLModel wrapper,Solution solution) {
		System.out.println("modify variable");
		String comments;
		 
	return	this.genericTypeModification(atlModel, outputFolder, VariableDeclaration.class, "type", new MetaModel[] {inputMM, outputMM}, true,
				wrapper,solution,inputMM,outputMM);
//	return comments;
	}
	
	@Override
	public String getDescription() {
		return "Variable Type Modification";
	}
}
