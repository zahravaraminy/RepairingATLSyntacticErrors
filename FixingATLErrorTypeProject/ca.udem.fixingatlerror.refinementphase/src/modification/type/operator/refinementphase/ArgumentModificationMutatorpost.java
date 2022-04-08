package modification.type.operator.refinementphase;

import java.util.List;

import org.eclipse.m2m.atl.core.emf.EMFModel;

import witness.generator.MetaModel;
import anatlyzer.atl.model.ATLModel;
import anatlyzer.atlext.OCL.OperationCallExp;
import ca.udem.fixingatlerror.refinementphase.main;
import jmetal.core.Solution;

//public class ArgumentModificationMutator extends AbstractDeletionMutator {

public class ArgumentModificationMutatorpost extends AbstractTypeModificationMutatorpost {

	@Override
	public List<Object> generateMutants(EMFModel atlModel, MetaModel inputMM, MetaModel outputMM, String outputFolder,
			ATLModel wrapper, Solution solution) {
		// System.out.println("modify argument");
		main.typeoperation = "argu";
		List<Object> comments;
		comments = this.genericTypeModification(atlModel, outputFolder, OperationCallExp.class, "arguments",
				new MetaModel[] { inputMM, outputMM }, wrapper, solution, inputMM, outputMM);
		main.typeoperation = null;
		return comments;

	}

	@Override
	public String getDescription() {
		return "Argument Type Modification";
	}
}
