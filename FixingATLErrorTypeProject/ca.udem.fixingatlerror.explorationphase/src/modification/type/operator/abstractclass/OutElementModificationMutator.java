package modification.type.operator.abstractclass;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.m2m.atl.core.emf.EMFModel;

import witness.generator.MetaModel;
import anatlyzer.atl.model.ATLModel;
import anatlyzer.atlext.ATL.OutPatternElement;
import ca.udem.fixingatlerror.explorationphase.main;
import jmetal.core.Solution;

public class OutElementModificationMutator extends AbstractTypeModificationMutator {
	
	@Override
	public List<Object> generateMutants(EMFModel atlModel, MetaModel inputMM, MetaModel outputMM, String outputFolder,ATLModel wrapper,Solution solution) {
	
		main.typeoperation="outelement";
		List<Object> comments;
		comments=
		this.genericTypeModification(atlModel, outputFolder, OutPatternElement.class, "type", new MetaModel[] {outputMM},wrapper,solution,inputMM,outputMM);
		main.typeoperation=null;
		return comments;
	 
	}
	
	@Override
	public String getDescription() {
		return "OutPattern Element Modification";
	}
}
