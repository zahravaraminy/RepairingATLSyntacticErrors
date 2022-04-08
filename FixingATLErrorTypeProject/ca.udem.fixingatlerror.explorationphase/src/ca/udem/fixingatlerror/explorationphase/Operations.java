package ca.udem.fixingatlerror.explorationphase;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.io.PrintWriter;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.core.ModelFactory;
import org.eclipse.m2m.atl.core.emf.EMFModel;
import org.eclipse.m2m.atl.core.emf.EMFModelFactory;
import org.eclipse.m2m.atl.core.emf.EMFReferenceModel;
import org.eclipse.m2m.atl.engine.parser.AtlParser;

import anatlyzer.examples.api.BaseTest;
import jmetal.core.Solution;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.problems.MyProblem;
import jmetal.util.PseudoRandom;
import transML.exceptions.transException;
import witness.generator.MetaModel;
import anatlyzer.atl.analyser.Analyser;
import anatlyzer.atl.analyser.namespaces.GlobalNamespace;
import anatlyzer.atl.analyser.Analyser;
import anatlyzer.atl.util.ATLSerializer;
import anatlyzer.atl.util.ATLUtils;
import anatlyzer.atl.util.ATLUtils.ModelInfo;
import anatlyzer.atlext.ATL.ATLFactory;
import anatlyzer.atlext.ATL.Binding;
import anatlyzer.atlext.ATL.CalledRule;
import anatlyzer.atlext.ATL.Helper;
import anatlyzer.atlext.ATL.InPattern;
import anatlyzer.atlext.ATL.InPatternElement;
import anatlyzer.atlext.ATL.LazyRule;
import anatlyzer.atlext.ATL.MatchedRule;
import anatlyzer.atlext.ATL.Module;
import anatlyzer.atlext.ATL.ModuleElement;
import anatlyzer.atlext.ATL.OutPattern;
import anatlyzer.atlext.ATL.OutPatternElement;
import anatlyzer.atlext.ATL.Rule;
import anatlyzer.atlext.OCL.VariableDeclaration;
import anatlyzer.evaluation.models.ModelGenerationStrategy;
import evaluation.mutator.AbstractMutator;
import creation.operator.BindingCreationMutator;
import creation.operator.InElementCreationMutator;
import creation.operator.OutElementCreationMutator;
import deletion.operator.ArgumentDeletionMutator;
import deletion.operator.BindingDeletionMutator;
import deletion.element.operator.FilterDeletionMutator;
import deletion.helper.operator.HelperContextDeletionMutator;
import deletion.helper.operator.HelperDeletionMutator;
import deletion.element.operator.InElementDeletionMutator;
import deletion.element.operator.OutElementDeletionMutator;
import deletion.element.operator.ParameterDeletionMutator;
import deletion.rule.operator.ParentRuleDeletionMutator;
import deletion.rule.operator.RuleDeletionMutator;
import deletion.element.operator.VariableDeletionMutator;
import modification.operator.ParentRuleModificationMutator;
import modification.operator.PrimitiveValueModificationMutator;
import modification.feature.operator.BindingModificationMutator;
import modification.feature.operator.NavigationModificationMutator;
import modification.invocation.operator.CollectionOperationModificationMutator;
import modification.invocation.operator.HelperOperationModificationMutator;
import modification.invocation.operator.IteratorModificationMutator;
import modification.invocation.operator.OperatorModificationMutator;
import modification.invocation.operator.PredefinedOperationModificationMutator;
import modification.type.operator.abstractclass.ArgumentModificationMutator;
import modification.type.operator.abstractclass.CollectionModificationMutator;
import modification.type.operator.abstractclass.HelperContextModificationMutator;
import modification.type.operator.abstractclass.HelperReturnModificationMutator;
import modification.type.operator.abstractclass.InElementModificationMutator;
import modification.type.operator.abstractclass.OutElementModificationMutator;
import modification.type.operator.abstractclass.ParameterModificationMutator;
import modification.type.operator.abstractclass.VariableModificationMutator;
//import anatlyzer.evaluation.raw.MUData;
//import anatlyzer.evaluation.raw.MUTransformation;
//import anatlyzer.evaluation.report.Report;
//import anatlyzer.evaluation.tester.Tester;
import anatlyzer.atl.model.ATLModel;
public class Operations extends BaseTest {

	
	/*public static final String TRANSFORMATION = "examples/class2rel/trafo/class2relationaltest.atl";

	//public static final String TRANSFORMATION = "examples/class2rel/trafo/waelnewcl2rel.atl";

	public static final String CLASS_METAMODEL  =  "examples/class2rel/metamodels/class.ecore";
	public static final String REL_METAMODEL   = "examples/class2rel/metamodels/relational.ecore";*/

	// PNML2PetriNet
//	public static final String TRANSFORMATION = "examples/class2rel/trafo/PNML2PetriNetExperience1.atl";
//	public static final String CLASS_METAMODEL  =  "examples/class2rel/metamodels/PetriNet.ecore";
//	public static final String REL_METAMODEL   = "examples/class2rel/metamodels/PNML.ecore";
	//public static final String TRANSFORMATION = "examples/class2rel/trafo/Class2TableExperience1.atl";
	//public static final String CLASS_METAMODEL  =  "examples/class2rel/metamodels/Relational.ecore";
	//public static final String REL_METAMODEL   = "examples/class2rel/metamodels/Class.ecore";
	public ArrayList<ArrayList<String>> listpropertyname = new ArrayList<ArrayList<String>>();
	public ArrayList<ArrayList<Integer>> listpropertynamelocation = new ArrayList<ArrayList<Integer>>();
    public static String statecase=null;
    //public ArrayList<Binding> newbindings = new ArrayList<Binding>();
    
    public ArrayList<ArrayList<Integer>> originalwrapper = new ArrayList<ArrayList<Integer>>();
	ATLModel model;
	Analyser analyser;
	ArrayList<MatchedRule> L;
	public ATLModel wrapper;
	private String atlFile;            // name of original transformation
	public EMFModel atlModel;         // model of the original transformation
	public static ATLModel  lastmodel;         // model of the original transformation
	public static EMFModel emfModel; 
	private GlobalNamespace namespace; // meta-models used by the transformation (union of inputMetamodels and outputMetamodels)
	private List<String> inputMetamodels  = new ArrayList<String>(); // input metamodels (IN)
	private List<String> outputMetamodels = new ArrayList<String>(); // output metamodels (OUT)
    private HashMap<String, ModelInfo> aliasToPaths = new HashMap<String, ModelInfo>();
	private ResourceSet rs;
	//private Report report;
	public static String str;
	//private MUData rawData;
	//private MUTransformation currentMutant; // The mutant being evaluated (if != null)
	private long initTime;
	private MetaModel iMetaModel = null, oMetaModel = null;
	private ModelGenerationStrategy.STRATEGY modelGenerationStrategy;
	 private EMFModel atlModel3;
	int populationsize=100;
	double populationsizedouble=100.0;
	// temporal folders
	private String folderMutants;
	private String folderModels;
	private String folderTemp;

	// configuration options
	private boolean generateMutants = false;
	private boolean generateTestModels = false;
	private boolean alwaysCheckRuleConflicts = true;
	
	private EPackage pack;

	
	private EMFModel loadTransformationModel (String atlTransformationFile) throws ATLCoreException {
		ModelFactory      modelFactory = new EMFModelFactory();
		
		EMFReferenceModel atlMetamodel = (EMFReferenceModel)modelFactory.getBuiltInResource("ATL.ecore");
		
		//EMFReferenceModel atlMetamodel = (EMFReferenceModel)modelFactory.getBuiltInResource("C:\\Users\\wael\\OneDrive\\workspaceATLoperations\\mutants\\anatlyzer.evaluation.mutants\\ATL.ecore");
		AtlParser         atlParser    = new AtlParser();		
		EMFModel          atlModel     = (EMFModel)modelFactory.newModel(atlMetamodel);
		atlParser.inject (atlModel, atlTransformationFile);	
		atlModel.setIsTarget(true);				
		
//		// Should we want to serialize the model.
//		String injectedFile = "file:/" + atlTransformationFile + ".xmi";
//		IExtractor extractor = new EMFExtractor();
//		extractor.extract(atlModel, injectedFile);
		
		return atlModel;
	}
	public List<EPackage> retPackMM(Resource resourceMM)
	 {
		ResourceSet resourceSet=resourceMM.getResourceSet();
		List<EPackage> metamodel = new ArrayList<EPackage>();
		for (EObject obj : resourceMM.getContents()) {
			if (obj instanceof EPackage) {
				EPackage.Registry.INSTANCE.put		(((EPackage)obj).getNsURI(), ((EPackage)obj).getEFactoryInstance().getEPackage());
				resourceSet.getPackageRegistry().put(((EPackage)obj).getNsURI(), ((EPackage)obj).getEFactoryInstance().getEPackage());
				metamodel.add((EPackage)obj);
			}
		}
		return metamodel;
	 }
	public Resource retPackResouceMM(String MMPath)
	 {	 	
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
		URI fileURI = URI.createFileURI(MMPath);//ecore.getFullPath().toOSString());		
		Resource resource = resourceSet.getResource(fileURI, true);	
		return resource;
	 }


	private void loadMetamodelsFromTransformation() throws transException {
		// register ecore factory
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
		
		//System.out.println("A");
	/*	
		try {
			ATLModel tmpAtlModel = new ATLModel(atlModel.getResource());
				
			for (ModelInfo info : ATLUtils.getModelInfo(tmpAtlModel)) {
				//System.out.println("info.getMetamodelName()");
				
				if (info.isInput()) 
					 this.inputMetamodels.add (info.getMetamodelName());
				else this.outputMetamodels.add(info.getMetamodelName());
				aliasToPaths.put(info.getMetamodelName(), info);
				//System.out.println(info.isInput());
				//System.out.println(info.getURIorPath());
				
				final String MMRootPath3     = "C:/Users/varaminz/Downloads/Project/workspaceTEST34/anatlyzer.atl.tests.api/trafo/factory.ecore";

				List<EPackage>   metamodels= retPackMM(retPackResouceMM(MMRootPath3));
				
				//System.out.println(info);
				//System.out.println("ddd");
		        for (EPackage p: metamodels) {
		        	this.pack = p;
		        	if (p.getNsURI()!=null && !p.getNsURI().equals("")) rs.getPackageRegistry().put(p.getNsURI(), p);
		        	if (p.getName().equals(info.getMetamodelName()))    rs.getPackageRegistry().put(info.getMetamodelName(), p);
		        	
		        	
		        	// assign instance class name to data types (it is empty in uml/kermeta meta-models)
		        	for (EClassifier classifier : p.getEClassifiers())
		        		if (classifier instanceof EDataType)
		        			if (((EDataType)classifier).getInstanceClassName() == null)
		        				if      (classifier.getName().equals("String"))  ((EDataType)classifier).setInstanceClassName("java.lang.String");
		        				else if (classifier.getName().equals("Integer")) ((EDataType)classifier).setInstanceClassName("java.lang.Integer");
		        				else if (classifier.getName().equals("Boolean")) ((EDataType)classifier).setInstanceClassName("java.lang.Boolean");
		        }
			}
		} 
		catch (Exception e ) {
			throw new transException(transException.ERROR.GENERIC_ERROR, e.getMessage());
		}*/
	}

	public void Testeroperate (String trafo, String temporalFolder) throws ATLCoreException, transException {
		
	//	this.rs       = new ResourceSetImpl();
	
//		this.report   = new Report();
		Setting s=new Setting();
		this.atlFile  = trafo;
		this.atlModel = this.loadTransformationModel(trafo);
		this.wrapper = new ATLModel(this.atlModel.getResource());
//		this.loadMetamodelsFromTransformation();
//		this.modelGenerationStrategy = ModelGenerationStrategy.STRATEGY.Lite;
		this.folderMutants = temporalFolder + "mutants" + File.separator;
		this.folderModels  = temporalFolder + "testmodels" + File.separator;
		this.folderTemp    = temporalFolder + "temp" + File.separator;
		//For PNML
		 final String MMRootPath     = s.gettargetmetamodel();
		
		 List<EPackage>   metamodels= retPackMM(retPackResouceMM(MMRootPath));
			for (EPackage p: metamodels) {
				 this.iMetaModel = new MetaModel(p); 
			 }
			//for PetriNet
			final String MMRootPath2     = s.getsourcemetamodel();
			
			List<EPackage>   metamodels2= retPackMM(retPackResouceMM(MMRootPath2));
			// List<EPackage> metamodels2 = EMFUtils.loadEcoreMetamodel("file:///D:\\java\\ATLproject\\neonssmallprojects\\anatlyzer.evaluation.mutants\\trafo\\PetriNet.ecore");
			 for (EPackage p: metamodels2) {
				 this.oMetaModel = new MetaModel(p); 
			 }
		//for petrinet	
		// this.iMetaModel.setName ("PNML");
		// this.oMetaModel.setName ("PetriNet");
		 this.iMetaModel.setName (s.getfirstecorename());
		 this.oMetaModel.setName (s.getsecondecorename());
			String temporalcover=temporalFolder+ "cover" + File.separator;
			ExtractPropertyname(this.wrapper);
			
	}
	private void ExtractPropertyname(ATLModel wrapper) {
		
		int row=0;
		this.listpropertyname.clear();
		this.listpropertynamelocation.clear();
		this.originalwrapper.clear();
		 listpropertyname = new ArrayList<ArrayList<String>>();
		 listpropertynamelocation = new ArrayList<ArrayList<Integer>>();
		 ArrayList<ArrayList<Integer>> listtemp = new ArrayList<ArrayList<Integer>>();
		 originalwrapper = new ArrayList<ArrayList<Integer>>();
		 
		for (Rule rule : (List<Rule>)wrapper.allObjectsOf(Rule.class)) {
			for (OutPatternElement outElement : rule.getOutPattern().getElements()) {
				ArrayList<String> row2 = new ArrayList<String>();
				ArrayList<Integer> row3 = new ArrayList<Integer>();
				
				ArrayList<Integer> row5 = new ArrayList<Integer>();
				
				for(int j=0;j<outElement.getBindings().size();j++) 
				{
					row2.add(outElement.getBindings().get(j).getPropertyName());
					row5.add(0);
					String[] array=outElement.getBindings().get(j).getLocation().split(":", 2);
					
					row3.add(Integer.parseInt(array[0]));
				}
				row=row+1;
				
			this.listpropertyname.add(row2);
			//if(row3.size()>0)
			this.listpropertynamelocation.add(row3);
			
			this.originalwrapper.add(row5);
			}
			
			
		}
		System.out.println(this.listpropertyname);
		System.out.println(this.listpropertynamelocation);
		System.out.println(this.originalwrapper);
		
	}
	private /*static*/ long index = 1;
	private String getValidNameOfFile (String outputFolder) throws IOException {
		String outputfile = null;
		String aux        = null;
		aux = File.separatorChar + "finalresult" +main.totalnumber+ ".atl";
		outputfile = outputFolder + aux;
		return outputfile;
	}
	
	public void save (EMFModel atlModel, String outputFolder) throws IOException {
		try {
			// save atl file
			String atl_transformation = this.getValidNameOfFile(outputFolder);
			AtlParser atlParser       = new AtlParser();
			atlParser.extract(atlModel, atl_transformation);
			
			// compile transformation
			String asm_transformation = atl_transformation.replace(".atl", ".asm");
		} 
		catch (ATLCoreException e) {}
	//	catch (FileNotFoundException e) {} 
	//	catch (IOException e) {}
		
		
	}
	

	private void deleteDirectory (String directory, boolean recursive) {
		File folder = new File(directory);
		
		if (folder.exists())
			for (File file : folder.listFiles()) {				
				if (file.isDirectory()) deleteDirectory(file.getPath(), recursive);
				
				file.delete();
				
				
			}
		folder.delete();
		
	}
	
	/**
	 * It creates a directory.
	 * @param folder name of directory
	 */
	private void createDirectory (String directory) {
		File folder = new File(directory);
		while (!folder.exists()) 
			folder.mkdir();
	}
	
	Operations() throws Exception 
    {
		//Here I have to read and convert the ATL rule
		Setting s=new Setting();
		   Testeroperate(s.getinputfaultytransformation(), s.getoutputfolder());
		}
	
	public Operations(String atlfilepath, EMFModel atlModel2, ATLModel atlModel4) throws Exception 
    {
		
		Setting s=new Setting();
		
//			typing(atlfilepath, new Object[] {CLASS_METAMODEL,REL_METAMODEL }, 
//					   new String[] { "PetriNet", "PNML" }, true);
			typing(atlfilepath, new Object[] {s.getsourcemetamodel(),s.gettargetmetamodel() }, 
					   new String[] { s.getsecondecorename(), s.getfirstecorename()}, true,atlModel2,atlModel4);
			
		
		 analyser = getAnalyser();
		 model=analyser.getATLModel();
    }
	
	public   List<Object> executeOperations(int num,CoSolution S,int sizeoperation, Solution solution, int sumfirstfit, int sumsecondfit, int sumthirdfit, FileWriter csvWriter)
	 {
		Setting s=new Setting();
		
		if (main.checkoffspring==1)
			try {
//				Testeroperate("C:/Users/varaminz/Downloads/Project/workspaceTEST34/anatlyzer.atl.tests.api/trafo/PNML2PetriNetExperience1.atl", "C:/Users/varaminz/Downloads/Project/workspaceTEST34/anatlyzer.atl.tests.api/trafo2/zahra2");
			Testeroperate(s.getinputfaultytransformation(), s.getoutputfolder());
				main.checkoffspring=0;
				
			
			} catch (ATLCoreException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (transException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				
			List<Object> comments = null;
			List<Object> returnonjects =new ArrayList<Object>();
			System.out.println(num);
				try {
			
			switch (num) {
			case 1:
				//ModuleNamemodificationOperator(S);
				//deleteOperator(S);
				statecase="case1";
				System.out.println("modify1");
	//			NSGAII.writer.println("modify1");
			//	if (Class2Rel.fixoperation.get(1).equals(0)) {
				AbstractMutator[] operators = {
						// deletion
					 new OutElementModificationMutator(),
				
					 
					 
						};
				 comments = null;
				for (AbstractMutator operator : operators) {
					//System.out.println("case1");
					comments=operator.generateMutants(atlModel, this.iMetaModel, this.oMetaModel, this.folderMutants,this.wrapper,solution);
					//Tester.wrapper2=new  anatlyzer.evaluation.mutators.ATLModel(this.atlModel.getResource());
				
					}
			
			//	}
				 this.wrapper=(ATLModel) comments.get(0);
					this.atlModel=(EMFModel) comments.get(1);
				 break;
				
			case 2:
				statecase="case2";
				System.out.println("modify2");
		//		NSGAII.writer.println("modify2");
				AbstractMutator[] operators2 = {
						// deletion
					//	new FilterDeletionMutator(),
					new PredefinedOperationModificationMutator(),
						
					};
				comments = null;
				for (AbstractMutator operator : operators2) {
					//System.out.println("case2");
					comments=operator.generateMutants(atlModel, this.iMetaModel, this.oMetaModel, this.folderMutants,this.wrapper,solution);
					//Tester.wrapper2=new  anatlyzer.evaluation.mutators.ATLModel(this.atlModel.getResource());
			
					}
				
				//}
				 this.wrapper=(ATLModel) comments.get(0);
					this.atlModel=(EMFModel) comments.get(1);
				 break;
			case 3:
				statecase="case10";
				System.out.println("modify3");
			//	NSGAII.writer.println("modify3");
				AbstractMutator[] operators3 = {
						// deletion
						new BindingModificationMutator(),// in bood
					};
				comments = null;
				for (AbstractMutator operator : operators3) {
					//System.out.println("case3");
					comments=operator.generateMutants(atlModel, this.iMetaModel, this.oMetaModel, this.folderMutants,this.wrapper,solution);
					//Tester.wrapper2=new  anatlyzer.evaluation.mutators.ATLModel(this.atlModel.getResource());
			        
					}
				
				//}
				 this.wrapper=(ATLModel) comments.get(0);
					this.atlModel=(EMFModel) comments.get(1);
				 break;
			case 4:
				statecase="case4";
				System.out.println("modify4");
			//	NSGAII.writer.println("modify4");
				AbstractMutator[] operators4 = {
						// deletion
						//new OutElementDeletionMutator(),
						 new CollectionModificationMutator(),// in bod
						};
		        comments = null;
				for (AbstractMutator operator : operators4) {
					//System.out.println("case4");
					comments=operator.generateMutants(atlModel, this.iMetaModel, this.oMetaModel, this.folderMutants,this.wrapper,solution);
					//Tester.wrapper2=new  anatlyzer.evaluation.mutators.ATLModel(this.atlModel.getResource());
					
					}
				
				//}
				 this.wrapper=(ATLModel) comments.get(0);
				 this.atlModel=(EMFModel) comments.get(1);
				 break;
			case 5:
				statecase="case5";
				System.out.println("modify5");
			//	NSGAII.writer.println("modify5");
				//DeleteRule2(S);
			//	if (Class2Rel.fixoperation.get(5).equals(0)) {
				AbstractMutator[] operators5 = {
						// deletion
						
					//	new BindingDeletionMutator(), //in bood
						new CollectionOperationModificationMutator(),
				};
				comments = null;
				for (AbstractMutator operator : operators5) {
					//System.out.println("case5");
					comments=operator.generateMutants(atlModel, this.iMetaModel, this.oMetaModel, this.folderMutants,this.wrapper,solution);
					//Tester.wrapper2=new  anatlyzer.evaluation.mutators.ATLModel(this.atlModel.getResource());
				
					}
				
			//	}
				 this.wrapper=(ATLModel) comments.get(0);
				this.atlModel=(EMFModel) comments.get(1);
				 break;
				 
			case 6:
				 
				//DeleteRule2(S);
				statecase="case6";
				System.out.println("modify6");
		//		NSGAII.writer.println("modify6");
				AbstractMutator[] operators6 = {
						// deletion
						 new InElementModificationMutator(),
			
				};
				comments = null;
				for (AbstractMutator operator : operators6) {
					
					comments=operator.generateMutants(atlModel, this.iMetaModel, this.oMetaModel, this.folderMutants,this.wrapper,solution);
					//Tester.wrapper2=new  anatlyzer.evaluation.mutators.ATLModel(this.atlModel.getResource());
				
					}
				 this.wrapper=(ATLModel) comments.get(0);
				this.atlModel=(EMFModel) comments.get(1);
				 break;
				 
			case 7:
				 
				
				statecase="case7";
				System.out.println("modify7");
			//	NSGAII.writer.println("modify7");
				AbstractMutator[] operators7 = {
						// deletion
						
					//	new HelperContextModificationMutator(),
					//	 new HelperReturnModificationMutator(),
						new ArgumentModificationMutator(),
						
				};
				comments = null;
				for (AbstractMutator operator : operators7) {
					
					comments=operator.generateMutants(atlModel, this.iMetaModel, this.oMetaModel, this.folderMutants,this.wrapper,solution);
					//Tester.wrapper2=new  anatlyzer.evaluation.mutators.ATLModel(this.atlModel.getResource());
				
					}
				 this.wrapper=(ATLModel) comments.get(0);
				 this.atlModel=(EMFModel) comments.get(1);
				 break;
				 
			case 8:
				 
				
				statecase="case8";
				System.out.println("modify8");
		//		NSGAII.writer.println("modify8");
				AbstractMutator[] operators8 = {
						// deletion
						
						 new BindingCreationMutator(),			
				};
				comments = null;
				for (AbstractMutator operator : operators8) {
					
					comments=operator.generateMutants(atlModel, this.iMetaModel, this.oMetaModel, this.folderMutants,this.wrapper,solution);
					//Tester.wrapper2=new  anatlyzer.evaluation.mutators.ATLModel(this.atlModel.getResource());
				
					}
				 this.wrapper=(ATLModel) comments.get(0);
				 this.atlModel=(EMFModel) comments.get(1);
				 break;
				
       case 9:
				 
				
				statecase="case9";
				System.out.println("modify9");
			//	NSGAII.writer.println("modify7");
				AbstractMutator[] operators9 = {
						// deletion
						
						 
						new NavigationModificationMutator(),
						
				};
				comments = null;
				for (AbstractMutator operator : operators9) {
					
					comments=operator.generateMutants(atlModel, this.iMetaModel, this.oMetaModel, this.folderMutants,this.wrapper,solution);
					//Tester.wrapper2=new  anatlyzer.evaluation.mutators.ATLModel(this.atlModel.getResource());
				
					}
				 this.wrapper=(ATLModel) comments.get(0);
				 this.atlModel=(EMFModel) comments.get(1);
				 break;
				 
       case 10:
			 
			
			statecase="case3";
			System.out.println("modify10");
		//	NSGAII.writer.println("modify7");
			AbstractMutator[] operators10 = {
					// deletion
					
					 
					 new IteratorModificationMutator(),
					
			};
			comments = null;
			for (AbstractMutator operator : operators10) {
				
				comments=operator.generateMutants(atlModel, this.iMetaModel, this.oMetaModel, this.folderMutants,this.wrapper,solution);
				//Tester.wrapper2=new  anatlyzer.evaluation.mutators.ATLModel(this.atlModel.getResource());
			
				}
			 this.wrapper=(ATLModel) comments.get(0);
			 this.atlModel=(EMFModel) comments.get(1);
			 break;
				
			}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//conflit++;
				//System.out.println("aaaaaaa: "+conflit);
			}
		try {
			if(sizeoperation==MyProblem.indexoperation && NSGAII.numop>0) {
				
				
			this.save(atlModel,
					s.getnewoutputresult()+NSGAII.indexmodeltransformation);
		   
			//List<Object> CoSolutionco = CalculateNumberError((EMFModel) comments.get(1),(ATLModel) comments.get(0));
			List<Object> CoSolutionco = CalculateNumberError(this.atlModel,this.wrapper,sumfirstfit,sumsecondfit,sumthirdfit,csvWriter);
			if(comments==null) {
				List<Object> comments2 = new ArrayList<Object>();
				comments2.add((int) CoSolutionco.get(2));
				comments2.add((int) CoSolutionco.get(3));
				comments2.add((int) CoSolutionco.get(4));
				return comments2;
			}
			else {
				comments.add((int) CoSolutionco.get(2));
				comments.add((int) CoSolutionco.get(3));
				comments.add((int) CoSolutionco.get(4));
				return comments;
			}
				
			
			
			}
			if(NSGAII.numop==0 && sizeoperation==MyProblem.indexoperation) {
				this.save(atlModel,
						s.getnewoutputresult()+NSGAII.indexmodeltransformation);
				List<Object> CoSolutionco=CalculateNumberError(this.atlModel,this.wrapper,sumfirstfit,sumsecondfit,sumthirdfit,csvWriter);
				if(comments==null) {
					List<Object> comments2 = new ArrayList<Object>();
					comments2.add((int) CoSolutionco.get(2));
					comments2.add((int) CoSolutionco.get(3));
					comments2.add((int) CoSolutionco.get(4));
					return comments2;
				}
				else {
					comments.add((int) CoSolutionco.get(2));
					comments.add((int) CoSolutionco.get(3));
					comments.add((int) CoSolutionco.get(4));
					return comments;
				}
					
				
			}
			
			
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//this.wrapper=(ATLModel) comments.get(0);
		//this.atlModel=(EMFModel) comments.get(1);
		
		return comments;
	 }
	
	private List<Object> CalculateNumberError(EMFModel atlModel2, ATLModel atlModel4, int sumfirstfit, int sumsecondfit, int sumthirdfit, FileWriter csvWriter) {
		// TODO Auto-generated method stub
		 CoSolution cocou=new CoSolution();
		 List<Object> ReturnResult = new ArrayList<Object>();
		 try {
			 Setting setting=new Setting();
			 String newfile=setting.getnewoutputresult()+NSGAII.indexmodeltransformation+"/finalresult"+main.totalnumber+".atl";
				cocou.setOp(new Operations(newfile,atlModel2,atlModel4));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 int nberrors=cocou.getOp().getAnalyser().getATLModel().getErrors().getNbErrors();
		 cocou.setsecondfit(nberrors);
		 NSGAII.fitness2=nberrors;
		 ReturnResult.add(nberrors);
		 if(nberrors==0 && NSGAII.fixedgeneration==-1)	{
			 if(NSGAII.counter%this.populationsize==0)
			 NSGAII.fixedgeneration=NSGAII.counter/(this.populationsize);
			 else
				 NSGAII.fixedgeneration=(NSGAII.counter/(this.populationsize))+1;
		 }
		 int fit3=cocou.Coverage(atlModel2);
		 NSGAII.fitness3=fit3;
		 cocou.setThirdfit(fit3);
		 ReturnResult.add(fit3);
		 
		 if(NSGAII.counter%this.populationsize==0 ) {
			
			    
				float sumfirst=((float)(sumfirstfit+NSGAII.numop))/(float)(this.populationsizedouble);
				float sumsecond=((float)(sumsecondfit+nberrors))/(float)(this.populationsizedouble);
				float sumthird=((float)(sumthirdfit+fit3))/(float)(this.populationsizedouble);
				
				printtofile(sumfirst,sumsecond,sumthird,csvWriter);
				ReturnResult.add( 0);
				ReturnResult.add( 0);
				ReturnResult.add( 0);
		 }
		 else {
			    sumfirstfit=sumfirstfit+NSGAII.numop;
				sumsecondfit=sumsecondfit+nberrors;
				sumthirdfit=sumthirdfit+fit3;
				ReturnResult.add( sumfirstfit);
				ReturnResult.add( sumsecondfit);
				ReturnResult.add( sumthirdfit);
		 }
		
		
		
		 return ReturnResult;
		
	}
	private void printtofile(float sumfirstfit,float sumsecondfit, float sumthirdfit, FileWriter csvWriter) {
		// TODO Auto-generated method stub
		
		 
		
		 
			 try {
				 csvWriter.append(NSGAII.counter/(this.populationsize)+ ";");
				csvWriter.append(sumfirstfit+ ";");
				 csvWriter.append(sumsecondfit+ ";");
				 csvWriter.append(sumthirdfit+ ";");
				 if(NSGAII.counter==this.populationsize)
					 csvWriter.append( "\n");
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			 	 
		 
		
			 
		
	}
	
	 
	
	
	
	
	
	public  void Createrule() throws IOException
	 {
		Module module = analyser.getATLModel().allObjectsOf(Module.class).get(0);
		
	// template for creating a new ATL Rule
			ATLFactory atlFactory = ATLFactory.eINSTANCE;
			MatchedRule newRule = atlFactory.createMatchedRule();
			newRule.setName("TestRule");
			InPattern iP = atlFactory.createInPattern();
			OutPattern oP = atlFactory.createOutPattern();
			newRule.setInPattern(iP);
			newRule.setOutPattern(oP);
			module.getElements().add(newRule);
        
	 }
	
	
	public  void changeBinding() throws IOException
	 {
		Module module = analyser.getATLModel().allObjectsOf(Module.class).get(0);
		L=new ArrayList<MatchedRule>();
	// L Contains all the MatchedRules
			for (ModuleElement e : module.getElements()) {
				if (e instanceof MatchedRule) {
					L.add((MatchedRule)e);
				}
			}
			System.out.println("Taille de L= "+L.size());
			int r=PseudoRandom.randInt(0,L.size());
		
			MatchedRule mr=L.get(r);
			String s =mr.getOutPattern().getElements().get(0).getBindings().get(1).getPropertyName();
			mr.getOutPattern().getElements().get(0).getBindings().get(0).setPropertyName(s);
		
			/*	MatchedRule mr=L.get(1);
			String s =mr.getOutPattern().getElements().get(0).getBindings().get(1).getPropertyName();
			mr.getOutPattern().getElements().get(0).getBindings().get(0).setPropertyName(s);
		*/
			
	 }
	
	//Done : supprimer n'importe quel Bindind de n'importe quelle Regle
	public  void DeleteBinding(CoSolution S) throws IOException
	 {
		System.out.println("Debut Delete Binding");
		int rMr=0;
	 int rEl = 0;
	 int rBind=0;
	 L=new ArrayList<MatchedRule>();
		
		Module module = analyser.getATLModel().allObjectsOf(Module.class).get(0);
		
		// L Contains all the MatchedRules
			for (ModuleElement e : module.getElements()) {
				if (e instanceof MatchedRule) {
					L.add((MatchedRule)e);
				}
			}
			System.out.println("Taille de L= "+L.size());
if(L.size()!=0)
{			rMr=PseudoRandom.randInt(0,L.size()-1);
		
			MatchedRule mr=L.get(rMr);
		//System.out.println(mr.getOutPattern().getElements().get);	
			
	if(!(mr.getOutPattern().getElements()==null))		
	{		
			rEl=PseudoRandom.randInt(0,mr.getOutPattern().getElements().size()-1);
			if ((rEl!=0 ||!(mr.getOutPattern().getElements().get(rEl).getBindings().size()==0)))
	{		rBind=PseudoRandom.randInt(0,mr.getOutPattern().getElements().get(rEl).getBindings().size()-1);
		//module.getElements()
			System.out.println("rMr= "+rMr);
			System.out.println("rBind= "+rEl);
			System.out.println("rEl= "+rBind);
			
		mr.getOutPattern().getElements().get(rEl).getBindings().remove(rBind);
		
		System.out.println("Fin Delete Binding");
	}	
	}
}	
System.out.println("44444444");
	ATLSerializer.serialize(S.getOp().getAnalyser().getATLModel(),  "examples/class2rel/trafo/waelnewcl2rel4.atl");
	 }
	
	public static  int random(int min, int max)
	{
		
    	int x=(int)(Math.random()*100);
    	if(max-min<=1 || max==0)
    		return 0;
    	while (x<min || x>max)
    	{
    		x=(int)(Math.random()*100);
    		//System.out.println(x+".");	
    	}
    	return x;
    }
	
	//public Analyser getAnalyser() {
		//return analyser;
	//}
}
