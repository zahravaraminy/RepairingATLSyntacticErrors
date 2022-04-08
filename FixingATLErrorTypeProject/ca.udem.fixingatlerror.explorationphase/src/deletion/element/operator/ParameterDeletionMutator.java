package deletion.element.operator;

import java.util.List;

import org.eclipse.m2m.atl.core.emf.EMFModel;

import witness.generator.MetaModel;
import anatlyzer.atl.model.ATLModel;
import anatlyzer.atlext.ATL.CalledRule;
import anatlyzer.atlext.OCL.Operation;
import anatlyzer.atlext.OCL.Parameter;
import deletion.operator.AbstractDeletionMutator;
import jmetal.core.Solution;

public class ParameterDeletionMutator extends AbstractDeletionMutator {

	@Override
	public List<Object> generateMutants(EMFModel atlModel, MetaModel inputMM, MetaModel outputMM, String outputFolder,
			ATLModel wrapper, Solution solution) {

		// deletion of parameters in helpers
		this.genericDeletion(atlModel, outputFolder, Operation.class, Parameter.class, "parameters", wrapper, solution,
				outputMM);
		// deletion of parameters in called rules
		this.genericDeletion(atlModel, outputFolder, CalledRule.class, Parameter.class, "parameters", wrapper, solution,
				outputMM);

		return null;
	}

	@Override
	public String getDescription() {
		return "Deletion of Parameter";
	}
}