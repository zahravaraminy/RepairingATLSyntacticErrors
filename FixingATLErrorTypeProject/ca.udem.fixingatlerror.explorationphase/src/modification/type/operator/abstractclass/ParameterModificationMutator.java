package modification.type.operator.abstractclass;

import java.util.List;

import org.eclipse.m2m.atl.core.emf.EMFModel;

import witness.generator.MetaModel;
import anatlyzer.atl.model.ATLModel;
import anatlyzer.atlext.OCL.Parameter;
//import anatlyzer.evaluation.tester.Tester;
import jmetal.core.Solution;

public class ParameterModificationMutator extends AbstractTypeModificationMutator {
	
	@Override
	public List<Object> generateMutants(EMFModel atlModel, MetaModel inputMM, MetaModel outputMM, String outputFolder,
			ATLModel wrapper,Solution solution) {
		System.out.println("parameter modify");
	return	this.genericTypeModification(atlModel, outputFolder, Parameter.class, "type", new MetaModel[] {inputMM, outputMM},
				wrapper,solution,inputMM,outputMM);
	   
	
	}
	
	@Override
	public String getDescription() {
		//Tester.str="parameter";
		return "Parameter Type Modification";
	}
}
