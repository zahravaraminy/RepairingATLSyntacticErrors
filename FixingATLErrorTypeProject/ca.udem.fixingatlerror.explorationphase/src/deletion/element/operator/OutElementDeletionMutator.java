package deletion.element.operator;

import java.util.List;

import org.eclipse.m2m.atl.core.emf.EMFModel;

import witness.generator.MetaModel;
import anatlyzer.atl.model.ATLModel;
import anatlyzer.atlext.ATL.OutPattern;
import anatlyzer.atlext.ATL.PatternElement;
import deletion.operator.AbstractDeletionMutator;
import jmetal.core.Solution;

public class OutElementDeletionMutator extends AbstractDeletionMutator {

	@Override
	public List<Object> generateMutants(EMFModel atlModel, MetaModel inputMM, MetaModel outputMM, String outputFolder,
			ATLModel wrapper, Solution solution) {
		System.out.println("delete outpattern");
		return this.genericDeletion(atlModel, outputFolder, OutPattern.class, PatternElement.class, "elements", wrapper,
				solution, outputMM);
	}

	@Override
	public String getDescription() {
		return "Deletion of OutPattern Element";
	}
}
