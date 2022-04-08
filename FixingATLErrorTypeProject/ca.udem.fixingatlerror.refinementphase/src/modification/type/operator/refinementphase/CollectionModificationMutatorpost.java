package modification.type.operator.refinementphase;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.m2m.atl.core.emf.EMFModel;

import witness.generator.MetaModel;
import anatlyzer.atl.model.ATLModel;
import anatlyzer.atlext.OCL.CollectionType;
import ca.udem.fixingatlerror.refinementphase.main;
import jmetal.core.Solution;

public class CollectionModificationMutatorpost extends AbstractTypeModificationMutatorpost {

	@Override
	public List<Object> generateMutants(EMFModel atlModel, MetaModel inputMM, MetaModel outputMM, String outputFolder,
			ATLModel wrapper, Solution solution) {
		main.typeoperation = "collectionelement";
		List<Object> comments;
		comments = this.genericTypeModification(atlModel, outputFolder, CollectionType.class, "elementType",
				new MetaModel[] { inputMM, outputMM }, wrapper, solution, inputMM, outputMM);
		main.typeoperation = null;
		return comments;
	}

	@Override
	public String getDescription() {
		return "Collection Type Modification";
	}
}
