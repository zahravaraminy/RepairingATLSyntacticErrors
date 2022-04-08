package deletion.element.operator;

import java.util.List;

import org.eclipse.m2m.atl.core.emf.EMFModel;

import witness.generator.MetaModel;
import anatlyzer.atl.model.ATLModel;
import anatlyzer.atlext.ATL.InPattern;
import anatlyzer.atlext.OCL.OclExpression;
import deletion.operator.AbstractDeletionMutator;
import jmetal.core.Solution;

public class FilterDeletionMutator extends AbstractDeletionMutator {

	@Override
	public List<Object> generateMutants(EMFModel atlModel, MetaModel inputMM, MetaModel outputMM, String outputFolder,
			ATLModel wrapper, Solution solution) {

		return this.genericDeletion(atlModel, outputFolder, InPattern.class, OclExpression.class, "filter", wrapper,
				solution, outputMM);
	}

	@Override
	public String getDescription() {
		return "Deletion of Filter";
	}
}
