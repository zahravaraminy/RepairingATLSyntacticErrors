package deletion.helper.operator;

import java.util.List;

import org.eclipse.m2m.atl.core.emf.EMFModel;

import witness.generator.MetaModel;
import anatlyzer.atl.model.ATLModel;
import anatlyzer.atlext.ATL.Helper;
import anatlyzer.atlext.ATL.Module;
import deletion.operator.AbstractDeletionMutator;
import jmetal.core.Solution;

public class HelperDeletionMutator extends AbstractDeletionMutator {

	@Override
	public List<Object> generateMutants(EMFModel atlModel, MetaModel inputMM, MetaModel outputMM, String outputFolder,
			ATLModel wrapper, Solution solution) {

		return this.genericDeletion(atlModel, outputFolder, Module.class, Helper.class, "elements", wrapper, solution,
				outputMM);
	}

	@Override
	public String getDescription() {
		return "Deletion of Helper";
	}
}
