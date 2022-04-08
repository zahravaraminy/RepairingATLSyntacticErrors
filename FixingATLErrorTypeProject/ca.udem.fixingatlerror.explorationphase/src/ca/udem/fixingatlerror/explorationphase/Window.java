package ca.udem.fixingatlerror.explorationphase;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.*;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.m2m.atl.common.ATLExecutionException;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.core.IExtractor;
import org.eclipse.m2m.atl.core.IModel;
import org.eclipse.m2m.atl.core.ModelFactory;
import org.eclipse.m2m.atl.core.emf.EMFExtractor;
import org.eclipse.m2m.atl.core.emf.EMFModel;
import org.eclipse.m2m.atl.core.emf.EMFModelFactory;
import org.eclipse.m2m.atl.core.emf.EMFReferenceModel;
import org.eclipse.m2m.atl.engine.parser.AtlParser;

import exceptions.*;
import ruletypeextraction.files.*;
import ruletypesmm.RuletypesmmPackage;

public class Window  {

	
	String ATLTransName;
	Resource resource;
	public Window() {

	

	}






	
	
	
	
	
	/*Button that executes the whole thing. First, the ATL file is injected into a model.
	 *Then, types from the model are extracted. Finally, the Similarity Matrix is calculated */
	public void getSMClick(String pathATLTransformation, EMFModel atlModel2) {
		try{
		
						
		/*	String pathATLTransformation="examples/class2rel/trafo/waelnewcl2rel.atl";	
			String pathSrcEcoreMMFile="examples/class2rel/metamodels/class.ecore";
			String pathTrgEcoreMMFile="examples/class2rel/metamodels/relational.ecore";*/
		//	String pathOUTFile="C:/Users/waelkessentini/workspacemars/tester footprints folder";
			
			Setting s=new Setting();
			//String pathATLTransformation="examples/class2rel/trafo/PNML2PetriNetExperience1.atl";	
			//String pathSrcEcoreMMFile="examples/class2rel/metamodels/Class.ecore";
			//String pathTrgEcoreMMFile= "examples/class2rel/metamodels/Relational.ecore";
			
			String pathSrcEcoreMMFile=s.gettargetmetamodel();
			String pathTrgEcoreMMFile= s.getsourcemetamodel();
	
	//		String pathSrcEcoreMMFile="examples/class2rel/metamodels/PNML.ecore";
	//		String pathTrgEcoreMMFile= "examples/class2rel/metamodels/PetriNet.ecore";
	
			String pathOUTFile="/outputfootprints";
			
			
			boolean ok = checkFields(pathATLTransformation, pathSrcEcoreMMFile,
					pathTrgEcoreMMFile, pathOUTFile);

			if (ok){
				//ATLFile2Model afm = new ATLFile2Model(pathATLTransformation, pathOUTFile + "\trans_injected.xmi");
			//	ATLFile2Model afm = new ATLFile2Model(pathATLTransformation, "tempModel.xmi");
			/*	try{
					//messageLabel.setText(afm.injectATLTrafo());
					try {
			//			afm.injectATLTrafo(atlModel2);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				//	IExtractor extractor = new EMFExtractor();
				//	extractor.extract((IModel)atlModel2, "tempModel.xmi");
					
					} catch (ATLCoreException e){
						e.printStackTrace();
				}*/
				try {
					ExtractRuleTypes runner = new ExtractRuleTypes();
					//runner.loadModels("tempModel.xmi", pathSrcEcoreMMFile,pathTrgEcoreMMFile);
					runner.loadModels2(atlModel2, pathSrcEcoreMMFile,pathTrgEcoreMMFile);
					runner.doExtractRuleTypes(new NullProgressMonitor());
					runner.saveModels("tempwaelTypesExtracted.xmi");
					
					//******* wael 
					
					 RuletypesmmPackage.eINSTANCE.eClass();
					 
					 Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
					    Map<String, Object> m = reg.getExtensionToFactoryMap();
					    m.put("xmi", new XMIResourceFactoryImpl());

					    // Obtain a new resource set
					    ResourceSet resSet = new ResourceSetImpl();
					    
					    // Get the resource
					    this.resource = resSet.getResource(URI
					      .createURI("tempwaelTypesExtracted.xmi"), true);
					
					
					//********
					//runner.saveModels(pathOUTFile + "/" + 
						//	ATLTransName.substring(0, ATLTransName.lastIndexOf('.')) + "Types.xmi");
					/*waelmessageLabel.append(" --- \n Types extracted from model --> OK \n");
					messageLabel.update(messageLabel.getGraphics());
				wael*/
					/*SimilarityMatrix sm = new SimilarityMatrix("tempTypesExtracted.xmi", pathOUTFile + "/" + 
										ATLTransName.substring(0, ATLTransName.lastIndexOf('.')) + "SM.csv");
					sm.main();
					messageLabel.append(" --- \n File with Similarity Matrix created --> OK \n");
					messageLabel.update(messageLabel.getGraphics());*/
				
					
					/*myprojectwael	ATLTransName=	"class2relational2_fixed.atl";
					File f = new File("tempTypesExtracted.xmi"); // This file contains the model with the types extracted
					File fl = new File (pathOUTFile + "/" +      // We want to copy the model before into this one
							ATLTransName.substring(0, ATLTransName.lastIndexOf('.')) + "Types.xmi");
								
					InputStream input = null;
					OutputStream output = null;
					try {
					 input = new FileInputStream(f);
					 output = new FileOutputStream(fl);
					 byte[] buf = new byte[1024];
					 int bytesRead;
					 while ((bytesRead = input.read(buf)) > 0) {
					         output.write(buf, 0, bytesRead);
					 }
					 input.close();
					 output.close();
					 f.delete();

					 //Deleting temporal files:
					 File tmp2 = new File("tempModel.xmi");
					 tmp2.delete();
					 
					}catch (FileNotFoundException e){
					//	messageLabel.setForeground(Color.red);
					//	messageLabel.append("FileNotFoundException: " + e.toString());
					//	messageLabel.update(messageLabel.getGraphics());
						e.printStackTrace();
					} catch (IOException e){
					//	messageLabel.setForeground(Color.red);
					//	messageLabel.append("IOException: " + e.toString());
					//	messageLabel.update(messageLabel.getGraphics());
					//	e.printStackTrace();
					}
			myprojectwael	*/
				} catch (ATLCoreException e) {
				//	messageLabel.setForeground(Color.red);
				//	messageLabel.append("ATLCoreException: " + e.toString());
				//	messageLabel.update(messageLabel.getGraphics());
					e.printStackTrace();
				} catch (IOException e) {
				//	messageLabel.setForeground(Color.red);
				//	messageLabel.append("IOException: " + e.toString());
				//	messageLabel.update(messageLabel.getGraphics());
					e.printStackTrace();
				} catch (ATLExecutionException e) {
				//	messageLabel.setForeground(Color.red);
				//	messageLabel.append("ATLExecutionException: " + e.toString());
				//	messageLabel.update(messageLabel.getGraphics());
				//	e.printStackTrace();
				}
				
				
				
			}
			else{
				//messageLabel.setForeground(Color.red);
			//	messageLabel.setText("Error while loading files, please check that files and paths are correct");
				//messageLabel.update(messageLabel.getGraphics());
			}
		} catch (ParametersException e) {
		//	messageLabel.setText(e.toString());
		//	e.printStackTrace();
		}
		

	}
	
	private boolean checkFields(String pathSrcMMEcoreFile, String pathTrgMMEcoreFile, String pathATLTransformation,
			String csvPath)	throws ParametersException {

		try {
			
			boolean ok = true;
			
			if (!new File(pathATLTransformation).exists()) {
				//messageLabel
				//		.setText("  ERROR. File for the ATL transformation not found.");
				ok = false;
			} else if (!new File(pathSrcMMEcoreFile).exists() && !pathSrcMMEcoreFile.startsWith("http://")) {
			//	messageLabel
				//		.setText("  ERROR. File for source metamodel not found.");
				ok = false;
			} else if (!new File(pathTrgMMEcoreFile).exists() && !pathTrgMMEcoreFile.startsWith("http://")) {
			//	messageLabel
				//		.setText("  ERROR. File for the target metamodel not found.");
				ok = false;
			} else if (!new File(csvPath).exists() && !csvPath.contains(" ")) {
				File file = new File(csvPath);
				file.mkdir();
			}
			
			return ok;
			
		} catch (NumberFormatException e) {
			throw new ParametersException(
					"ERROR. Insert a natural number for the number model generated with the ASSL file.");
		}
	}
}
