package deletion.rule.operator;

import java.util.List;

import org.eclipse.m2m.atl.core.emf.EMFModel;

import witness.generator.MetaModel;
import anatlyzer.atl.model.ATLModel;
import anatlyzer.atlext.ATL.MatchedRule;
import deletion.operator.AbstractDeletionMutator;
import jmetal.core.Solution;

public class ParentRuleDeletionMutator extends AbstractDeletionMutator {

	@Override
	public List<Object> generateMutants(EMFModel atlModel, MetaModel inputMM, MetaModel outputMM, String outputFolder,
			ATLModel wrapper, Solution solution) {

		return this.genericDeletion(atlModel, outputFolder, MatchedRule.class, MatchedRule.class, "children", wrapper,
				solution, outputMM);
	}

	@Override
	public String getDescription() {
		return "Deletion of Parent Rule";
	}
}
