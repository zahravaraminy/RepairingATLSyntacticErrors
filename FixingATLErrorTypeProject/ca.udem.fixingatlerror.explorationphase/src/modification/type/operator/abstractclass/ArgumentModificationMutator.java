package modification.type.operator.abstractclass;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.m2m.atl.core.emf.EMFModel;

import witness.generator.MetaModel;
import anatlyzer.atl.model.ATLModel;
import anatlyzer.atlext.ATL.Binding;
import anatlyzer.atlext.OCL.OperationCallExp;
import deletion.operator.AbstractDeletionMutator;
import ca.udem.fixingatlerror.explorationphase.main;
import jmetal.core.Solution;

//public class ArgumentModificationMutator extends AbstractDeletionMutator {

public class ArgumentModificationMutator extends AbstractTypeModificationMutator {
	
	@Override
	public List<Object> generateMutants(EMFModel atlModel, MetaModel inputMM, MetaModel outputMM, String outputFolder,ATLModel wrapper,Solution solution) {
	//	System.out.println("modify argument");
		main.typeoperation="argu";
		List<Object> comments;
		comments=
		this.genericTypeModification(atlModel, outputFolder, OperationCallExp.class, "arguments", new MetaModel[] {inputMM, outputMM},wrapper,solution,inputMM , outputMM);
		main.typeoperation=null;
		return comments;
	
	}
	
	
	@Override
	public String getDescription() {
		return "Argument Type Modification";
	}
}
