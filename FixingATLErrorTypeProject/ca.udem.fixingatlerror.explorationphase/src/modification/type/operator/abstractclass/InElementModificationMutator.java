package modification.type.operator.abstractclass;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.m2m.atl.core.emf.EMFModel;

import witness.generator.MetaModel;
import anatlyzer.atl.model.ATLModel;
import anatlyzer.atlext.ATL.InPatternElement;
import ca.udem.fixingatlerror.explorationphase.main;
import jmetal.core.Solution;

public class InElementModificationMutator extends AbstractTypeModificationMutator {
	
	@Override
	public List<Object> generateMutants(EMFModel atlModel, MetaModel inputMM, MetaModel outputMM, String outputFolder,ATLModel wrapper,Solution solution) {
		main.typeoperation="inelement";
		List<Object> comments;
		comments=
		
		 this.genericTypeModification(atlModel, outputFolder, InPatternElement.class, "type", new MetaModel[] {inputMM},wrapper,solution,  inputMM,  outputMM);
		main.typeoperation=null;
		return comments;
}
	
	@Override
	public String getDescription() {
		return "InPattern Element Modification";
	}
}
