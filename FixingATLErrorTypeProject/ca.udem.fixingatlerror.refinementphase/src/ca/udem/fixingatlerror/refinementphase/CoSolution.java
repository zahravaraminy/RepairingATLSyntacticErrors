package ca.udem.fixingatlerror.refinementphase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.m2m.atl.core.emf.EMFModel;

import anatlyzer.atl.util.ATLSerializer;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.util.PseudoRandom;
import ruletypesmm.Rule;
import ruletypesmm.Trafo;
import ruletypesmm.RuletypesmmPackage;

public class CoSolution {

	// List<String> metamodellist = new ArrayList<String>(Arrays.asList("src_Class",
	// "src_Class.persistent",
	// "src_Class.attr","src_Attribute","src_Attribute.type","src_Type","src_Type.type"));
	/*
	 * List<String> metamodellist = new
	 * ArrayList<String>(Arrays.asList("src_LocatedElement",
	 * "src_LocatedElement.location",
	 * "src_IdedElement","src_IdedElement.location","src_IdedElement.id","src_URI",
	 * "src_URI.location","src_URI.value","src_PNMLDocument",
	 * "src_PNMLDocument.location","src_PNMLDocument.xmlns","src_PNMLDocument.nets",
	 * "src_NetElement","src_NetElement.type","src_NetElement.document",
	 * "src_NetElement.contents","src_NetElement.name","src_NetElement.id",
	 * "src_NetElement.location"
	 * ,"src_NetContent","src_NetContent.location","src_NetContent.net",
	 * "src_NetContent.name", "src_LabeledElement","src_LabeledElement.location",
	 * "src_LabeledElement.labels","src_Label","src_Label.location",
	 * "src_Label.text","src_Label.labeledElement","src_Name",
	 * "src_Name.net","src_Name.netContent","src_Name.labels","src_Name.location",
	 * "src_NetContentElement","src_NetContentElement.id",
	 * "src_NetContentElement.net","src_NetContentElement.name",
	 * "src_NetContentElement.location",
	 * "src_Arc","src_Arc.id","src_Arc.source","src_Arc.target","src_Arc.net",
	 * "src_Arc.name","src_Arc.location","src_Place",
	 * "src_Place.net","src_Place.name","src_Place.id","src_Place.location",
	 * "src_Transition",
	 * "src_Transition.net","src_Transition.name","src_Transition.id",
	 * "src_Transition.location",
	 * "trg_LocatedElement","trg_LocatedElement.location","trg_NamedElement",
	 * "trg_NamedElement.name",
	 * "trg_NamedElement.location","trg_PetriNet","trg_PetriNet.elements",
	 * "trg_PetriNet.name","trg_PetriNet.location",
	 * "trg_PetriNet.arcs","trg_Element","trg_Element.net","trg_Element.name",
	 * "trg_Element.location","trg_Place","trg_Place.net","trg_Place.name",
	 * "trg_Place.location","trg_Place.incomingArc","trg_Place.outgoingArc",
	 * "trg_Transition","trg_Transition.net","trg_Transition.name",
	 * "trg_Transition.location","trg_Transition.incomingArc",
	 * "trg_Transition.outgoingArc","trg_Arc","trg_Arc.name","trg_Arc.location",
	 * "trg_Arc.weight","trg_Arc.net",
	 * "trg_PlaceToTransition","trg_PlaceToTransition.weight",
	 * "trg_PlaceToTransition.net","trg_PlaceToTransition.name",
	 * "trg_PlaceToTransition.location","trg_PlaceToTransition.from",
	 * "trg_PlaceToTransition.to","trg_TransitionToPlace"
	 * ,"trg_TransitionToPlace.weight","trg_TransitionToPlace.net",
	 * "trg_TransitionToPlace.name","trg_TransitionToPlace.location",
	 * "trg_TransitionToPlace.from","trg_TransitionToPlace.to"));
	 */

	/*
	 * List<String> metamodellist = new
	 * ArrayList<String>(Arrays.asList("src_NamedElt", "src_NamedElt.name",
	 * "src_Package", "src_Package.elems","src_Package.name", "src_Classifier",
	 * "src_Classifier.package","src_Classifier.name", "src_DataType",
	 * "src_DataType.package","src_DataType.name", "src_Class",
	 * "src_Class.isAbstract","src_Class.supers","src_Class.att","src_Class.package"
	 * ,"src_Class.name", "src_Attribute",
	 * "src_Attribute.multiValued","src_Attribute.type","src_Attribute.owner",
	 * "src_Attribute.name", "trg_Named", "trg_Named.name",
	 * "trg_Table","trg_Table.col","trg_Table.key","trg_Table.name", "trg_Column",
	 * "trg_Column.type","trg_Column.name","trg_Type","trg_Type.name", "trg_Schema",
	 * "trg_Schema.tables","trg_Schema.types" ));
	 */
	ArrayList<Integer> operations;
	// private List<String> originalmetamodellist = new ArrayList<String>();
	// private List<String> editedmetamodellist = new ArrayList<String>();
	List<List<String>> editedmetamodellist = new ArrayList<List<String>>();
	private int secondfit;
	private int Thirdfit;
	List<String> rowone;
	public static int check = 0;
	static int min_solution_size = 1;
	static int max_solution_size = 4;
	Operations op;

	public Operations getOp() {
		return op;
	}

	public int getsecondfit() {
		return secondfit;
	}

	public void setsecondfit(int i) {
		secondfit = i;
	}

	public int getThirdfit() {
		return Thirdfit;
	}

	public void setThirdfit(int i) {
		Thirdfit = i;
	}

	public void setOp(Operations op) {
		this.op = op;
	}

	// these values represents the existing operations
	public static int min_operations_interval = 1;
	public static int max_operations_interval = 10;// bod 7
	public Window w;

	int numberofoperations;
	int solutionsize;

	static int objectives_number = 2;// bod
	ArrayList<Double> objectives;
	ArrayList<String> objectives_names;

	int nberrors;

	int coverage;

	int idealpoint;

	/////////
	int effort;

	int rank;
	double dominance;
	double distance;
	double crowding_distance;

	public CoSolution() {
		try {
			this.op = new Operations();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.w = new Window();

		this.operations = new ArrayList<Integer>();

		this.numberofoperations = 0;
		this.nberrors = 0;
		this.solutionsize = 0;
		this.idealpoint = 0;

		/////
		this.dominance = 0;
		this.distance = 0;
		this.crowding_distance = 0;
		this.objectives = new ArrayList<Double>();
		this.objectives_names = new ArrayList<String>();

	}

	CoSolution(CoSolution a) throws Exception {
		this.op = a.op;
		this.operations = new ArrayList<Integer>(a.operations);

		this.w = new Window();
		this.numberofoperations = a.numberofoperations;
		this.nberrors = a.nberrors;
		this.solutionsize = a.solutionsize;
		this.idealpoint = a.idealpoint;

		/////////////
		this.rank = a.rank;
		this.dominance = a.dominance;
		this.distance = a.distance;
		this.crowding_distance = a.crowding_distance;
		this.objectives = new ArrayList<Double>();

		for (int i = 0; i < a.objectives.size(); i++) {
			this.objectives.add(a.objectives.get(i));
		}

		this.objectives_names = new ArrayList<String>();

		for (int i = 0; i < a.objectives_names.size(); i++) {
			this.objectives_names.add(a.objectives_names.get(i));
		}

	}

	void create_solution() throws IOException {
		// creation of a sequence of operations
		// solutionsize = Operations.random(min_solution_size, max_solution_size);
		Random r = new Random();
		solutionsize = min_solution_size + r.nextInt(max_solution_size - min_solution_size);

		System.out.println("le nb aleatoire de la taille de la solution est : " + solutionsize);
		// this.numberofoperations++;

		for (int i = 0; i < solutionsize; i++) {
			// set of operations on the ATL Rules.
			// int numoperation=Operations.random(min_operations_interval,
			// max_operations_interval);

			// int numoperation = min_operations_interval +
			// r.nextInt(max_operations_interval - min_operations_interval);
			int numoperation = PseudoRandom.randInt(min_operations_interval, max_operations_interval);
			System.out.println("le num aleatoire de l operation est : " + numoperation);

			// operations contains the number of the operation to apply

			this.operations.add(numoperation);

			// this.parameters.add(op.addparameters(numoperation));

		}

	}

	/*
	 * fin des tests void create_test_solution() throws IOException { // creation of
	 * a sequence of operations solutionsize = 1;
	 * 
	 * //this.numberofoperations++;
	 * 
	 * /* for(int i=0;i<solutionsize;i++) { // set of operations on the ATL Rules.
	 * int numoperation=Operations.random(min_operations_interval,
	 * max_operations_interval);
	 * 
	 * // operations contains the number of the operation to apply
	 * 
	 * this.operations.add(numoperation);
	 * 
	 * // this.parameters.add(op.addparameters(numoperation));
	 * 
	 * }
	 * 
	 * this.operations.add(1); this.operations.add(2);
	 * 
	 * }
	 */

	/*
	 * void evaluate_solution() throws Exception { int tab[]=new int[5];
	 * int[]temp=new int[2];
	 * 
	 * this.objectives = new ArrayList<Double>() ; this.objectives_names = new
	 * ArrayList<String>() ;
	 * 
	 * 
	 * for(int i=0;i<this.operations.size();i++) {
	 * 
	 * 
	 * op.executeOperations(this.operations.get(i));
	 * System.out.println("fin de l'execution de l'operation:  "+this.operations.get
	 * (i));
	 * 
	 * } System.out.println("fin de l'execution de toutes les operations  ");
	 * 
	 * ATLSerializer.serialize(this.op.analyser.getATLModel(),
	 * "examples/class2rel/trafo/waelnewcl2rel.atl");
	 * 
	 * this.op= new Operations("examples/class2rel/trafo/waelnewcl2rel.atl");
	 * 
	 * this.nberrors=op.analyser.getATLModel().getErrors().getNbErrors();
	 * 
	 * System.out.println("errors= "+op.analyser.getATLModel().getErrors().
	 * getNbErrors());
	 * 
	 * 
	 * 
	 * 
	 * this.solutionsize=operations.size(); // Here I need to add the fitness
	 * functions to calulate.
	 * 
	 * this.w.getSMClick();
	 * 
	 * this.coverage=footprintscalcul(""); System.out.
	 * println("*********************Fin execution de la Solution *****************"
	 * ); System.out.println("errors= "+this.nberrors);
	 * System.out.println("coverage= "+this.coverage);
	 * System.out.println("size of the solution= "+this.solutionsize);
	 * System.out.println("**************************************");
	 * 
	 * // au lieu de la methode all_metrics
	 * objectives.add((double)this.solutionsize);
	 * this.objectives_names.add("solutionsize");
	 * objectives.add((double)this.nberrors); this.objectives_names.add("nberrors");
	 * objectives.add((double)this.coverage); this.objectives_names.add("coverage");
	 * }
	 */

	public int Coverage(EMFModel atlModel2) {
		// this.w.getSMClick( "examples/class2rel/trafo/PNML2PetriNetExperience1.atl");
		// this.w.getSMClick( "examples/class2rel/trafo/Class2TableExperience1.atl");
		check = 1;
		// List<String> firstfoot=footprintscalcul("");

		Setting setting = new Setting();

		this.w.getSMClick(setting.getnewoutputresult() + NSGAII.indexmodeltransformation + "/finalresult"
				+ main.totalnumber + ".atl", atlModel2);
		// this.w.getSMClick(
		// "C:/Users/varaminz/Downloads/Project/workspaceTEST34/anatlyzer.atl.tests.api/trafo2/zahra2mutant/finalresult"+Class2Rel.totalnumber+".atl");
		check = 1;
		List<String> secondfoot = footprintscalcul("");
		List<String> commonlist2 = new ArrayList<String>();
		List<String> differlist2 = new ArrayList<String>();

		int sum = 0;
		int s2 = 0;
		int s3 = 0;
		/*
		 * bod for(int i=0;i<NSGAII.originalmetamodellist.size();i++) {
		 * 
		 * List<String>
		 * commonlist=intersection5(NSGAII.originalmetamodellist.get(i),this.
		 * editedmetamodellist.get(i));
		 * NSGAII.writer.println(NSGAII.originalmetamodellist.get(i));
		 * NSGAII.writer.println(this.editedmetamodellist.get(i));
		 * NSGAII.writer.println("listdeletshodelo"); NSGAII.writer.println(commonlist);
		 * for(int j=0;j<commonlist.size();j++)
		 * if(secondfoot.contains(commonlist.get(j))) {
		 * NSGAII.writer.println(commonlist.get(j)); s3=s3+1; }
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * }
		 */
		// this.originalmetamodellist.clear();
		this.editedmetamodellist.clear();
		NSGAII.argumentlist.clear();
		editedmetamodellist = new ArrayList<List<String>>();
		// originalmetamodellist = new ArrayList<List<String>>();
		NSGAII.argumentlist = new ArrayList<String>();
		// NSGAII.writer.println("thirdfitness");
		// NSGAII.writer.println(NSGAII.counterdelet);
		// NSGAII.writer.println(s3);
		// NSGAII.writer.println(((metamodellist.size())-(secondfoot.size()))+
		// NSGAII.counterdelet);
		return (((setting.getmetamodellist().size()) - (secondfoot.size())));
		// return s3; bod
	}

	void displaysolution() {
		for (int i = 0; i < operations.size(); i++) {

			System.out.print("*******Operations to execute ***** :  " + this.operations.get(i) + " : ");
			switch (this.operations.get(i)) {
			case 1:
				System.out.println("CreateInitialState ");
				break;
			case 2:
				System.out.println("CreateSimpleState ");
				break;
			case 3:
				System.out.println("CreateFinalState ");
				break;
			case 4:
				System.out.println("CreateSuccessorSimple ");
				break;
			case 5:
				System.out.println("CreatePredeccessorSimple ");
				break;
			case 6:
				System.out.println("CreatePredeccessortoInitial ");
				break;
			case 7:
				System.out.println("CreateSuccessortoFinal ");
				break;
			case 8:

				System.out.println("CreateSuccessorInitial ");
				break;
			case 9:
				System.out.println("CreatePredeccessorFinal ");
				break;
			case 10:
				System.out.println("DeleteInitialState ");
				break;
			case 11:
				System.out.println("DeleteSimpleState ");
				break;
			case 12:
				System.out.println("DeleteFinalState ");
				break;
			case 13:
				System.out.println("RemoveSuccessorSimple ");
				break;
			case 14:
				System.out.println("RemovePredeccessorSimple ");
				break;
			case 15:
				System.out.println("RemovePredeccessortoinitial ");
				break;
			case 16:
				System.out.println("RemoveSuccessortoFinal ");
				break;
			case 17:
				System.out.println("RemoveSuccessorInitial ");
				break;
			case 18:
				System.out.println("RemovePredecessorFinal ");
				break;

			}
		}

	}

	// I execute the operations of the solution then I evaluate it

	/*
	 * void evaluate_solution() throws Exception { int tab[]=new int[5];
	 * int[]temp=new int[2];
	 * 
	 * //this.objectives = new ArrayList<Double>() ; // this.objectives_names = new
	 * ArrayList<String>() ;
	 * 
	 * 
	 * for(int i=0;i<this.operations.size();i++) {
	 * 
	 * 
	 * op.executeOperations(this.operations.get(i));
	 * System.out.println("fin de l'execution de l'operation:  "+this.operations.get
	 * (i));
	 * 
	 * } System.out.println("fin de l'execution de toutes les operations  ");
	 * 
	 * // Here I need to add the fitness functions to calulate.
	 * 
	 * 
	 * System.out.println("waelnb errors est "+op.model.getErrors().getNbErrors());
	 * 
	 * System.out.println("nb errors est "+op.analyser.getATLModel().getErrors().
	 * getNbErrors());
	 * this.nberrors=op.analyser.getATLModel().getErrors().getNbErrors();
	 * 
	 * System.out.println("size operations= "+operations.size());
	 * 
	 * this.solutionsize=operations.size();
	 * 
	 * 
	 * // this.coverage=footprintscalcul("class2relational2_fixedTypes.xmi");
	 * 
	 * //******* wael
	 * 
	 * w.getSMClick();
	 * 
	 * //this.coverage=footprintscalcul(w.resource);
	 * this.coverage=footprintscalcul(""); //******** wael
	 * ATLSerializer.serialize(op.analyser.getATLModel(),
	 * "examples/class2rel/trafo/cl2rel.atl");
	 * 
	 * //System.out.print("\n starting all metrics"); //this.all_metrics();
	 * 
	 * 
	 * 
	 * }
	 */

	public List<String> footprintscalcul(String Typesmmpath) {
		// int footprintscalcul (Resource resource)

		// Typesmmpath="file/C:/Users/waelkessentini/workspacemars/tester footprints
		// folder/class2relational2_fixedTypes.xmi";

		/* 10 mai 2016 */
		Typesmmpath = "tempwaelTypesExtracted.xmi";
		RuletypesmmPackage.eINSTANCE.eClass();

		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("xmi", new XMIResourceFactoryImpl());

		// Obtain a new resource set
		ResourceSet resSet = new ResourceSetImpl();

		// Get the resource
		Resource resource = resSet.getResource(URI.createURI(Typesmmpath), true);
		/* 10 mai 2016 */

		// Resource resource = resSet.getResource(URI
		// .createURI("Class2RelationalTypes.xmi"), true);
		// Get the first model element and cast it to the right type, in my
		// example everything is hierarchical included in this first node

		Trafo trafo = (Trafo) resource.getContents().get(0);
		// this.originalmetamodellist.clear();
		// this.editedmetamodellist.clear();
		// bod comment kardam
		/*
		 * bod NSGAII.writer.println("footprint Rulha"); for (Rule r :
		 * trafo.getRules()){
		 * 
		 * NSGAII.writer.println(r.getAllFootPrints());
		 * 
		 * rowone = new ArrayList<String>(); this.rowone=r.getAllFootPrints(); if
		 * (check==0) NSGAII.originalmetamodellist.add(this.rowone); else
		 * this.editedmetamodellist.add(this.rowone);
		 * 
		 * }
		 */

		/*
		 * System.out.println("afterpool");
		 * System.out.println(this.originalmetamodellist);
		 * System.out.println(this.editedmetamodellist);
		 */
		EList<Rule> r1 = trafo.getRules();
		// bod System.out.println("nb de regles : "+r1.size());
		// list1=r1.get(0).getAllFootPrints();
		// list2=r1.get(1).getAllFootPrints();
		// System.out.println("union = "+union(r1));
		Setting s = new Setting();
		List<String> atllist = union(r1);
		// System.out.println("intersection = "+ intersection(metamodellist,atllist));
		// NSGAII.writer.println(metamodellist);
		// NSGAII.writer.println(atllist);
		List<String> coveragelist = intersection(s.getmetamodellist(), atllist);
		// List<String> deletlist=intersection5(s.getmetamodellist(),atllist);
		// NSGAII.writer.println("list delet");
		// NSGAII.writer.println(atllist);
		// NSGAII.writer.println(coveragelist);
		// NSGAII.writer.println(deletlist);
		// NSGAII.writer.println("coverage result = "+coveragelist.size()+" et
		// metamodellist= "+metamodellist.size());
		// bod System.out.println("coverage result = "+coveragelist.size()+" et
		// metamodellist= "+metamodellist.size());
		// return (metamodellist.size())-(coveragelist.size());

		// return (atllist); bod
		return coveragelist;
	}

	public <String> List<String> union(EList<Rule> r1) {

		List<String> list1;
		List<String> list2;

		Set<String> set = new HashSet<String>();

		for (int i = 0; i < r1.size(); i++) {
			list1 = (List<String>) r1.get(i).getAllFootPrints();
			set.addAll(list1);
			// set.addAll(list2);
		}
		return new ArrayList<String>(set);
	}

	/*
	 * public <T> List<T> union(List<T> list1, List<T> list2, EList<Rule> r1) {
	 * 
	 * 
	 * 
	 * Set<T> set = new HashSet<T>();
	 * 
	 * for(int i=0;i<r1.size();i++) { list1=(List<T>) r1.get(i).getAllFootPrints();
	 * set.addAll(list1); // set.addAll(list2); } return new ArrayList<T>(set); }
	 */

	public List<String> intersection4(List<String> list1, List<String> list2, int i, List<String> list3) {
		List<String> list = new ArrayList<String>();

		for (int j = 0; j < list1.size(); j++) {

			if (NSGAII.argumentlist.get(j).equals(String.valueOf(i))) {
				if (list2.contains(NSGAII.argumentlist.get(j + 1)) && !list3.contains(NSGAII.argumentlist.get(j + 1))) {
					list.add(NSGAII.argumentlist.get(j + 1));
				}

			}
			j = j + 1;

		}
		return list;
	}

	public List<String> intersection3(List<String> list1, List<String> list2) {
		List<String> list = new ArrayList<String>();

		for (int j = 0; j < list1.size(); j++) {

			int indexfound = list1.get(j).indexOf(".");

			if (indexfound > -1) {
				String extensionRemoved = (list1.get(j)).split("\\.")[1];
				boolean check = false;
				for (int i = 0; i < list2.size(); i++) {

					int indexfound2 = list2.get(i).indexOf("." + extensionRemoved);
					if (indexfound2 > -1)
						check = true;

				}
				if (check == false)
					list.add(list1.get(j));

			} else {
				if (!list2.contains(list1.get(j))) {
					list.add(list1.get(j));
				}
			}
		}
		System.out.println(list1);
		System.out.println(list2);
		System.out.println("list");
		System.out.println(list);
		return list;
	}

	public <T> List<T> intersection2(List<T> list1, List<T> list2) {
		List<T> list = new ArrayList<T>();

		for (T t : list1) {

			int indexfound = ((String) t).indexOf(".");
			System.out.println(indexfound);
			if (indexfound > -1) {
				String extensionRemoved = ((String) t).split("\\.")[1];
				for (int i = 0; i < list2.size(); i++) {
					@SuppressWarnings("unchecked")
					int indexfound2 = ((List<String>) list2.get(i)).indexOf("." + extensionRemoved);
					if (indexfound2 > -1)
						list.add(t);
				}

			} else {
				if (!list2.contains(t)) {
					list.add(t);
				}
			}
		}

		return list;
	}

	public <T> List<T> intersection5(List<T> list1, List<T> list2) {
		List<T> list = new ArrayList<T>();

		for (T t : list1) {
			if (!list2.contains(t)) {
				list.add(t);
			}
		}

		return list;
	}

	public <T> List<T> intersection(List<T> list1, List<T> list2) {
		List<T> list = new ArrayList<T>();

		for (T t : list1) {
			if (list2.contains(t)) {
				list.add(t);
			}
		}

		return list;
	}

	/////////////////////////////// For NSGA 2

	void mutation1() {

		int numoperation = Operations.random(min_operations_interval, max_operations_interval);

		int position = Operations.random(0, (this.operations.size() - 1));
		System.out.println("la position a changer est " + position + " qui est :" + operations.get(position));

		this.operations.set(position, numoperation);

		// }
	}

	void mutation2() {

		System.out.println("a modifier");

		int numoperation = Operations.random(min_operations_interval, max_operations_interval);

		int position = Operations.random(0, (this.operations.size() - 1));
		System.out.println("la position a changer est " + position + " qui est :" + operations.get(position));

		this.operations.set(position, numoperation);

		// }
	}

	void print_metrics() {
		System.out.println("\n Rank :  " + this.rank + " Distance : " + this.distance + "\n");
		for (int i = 0; i < this.objectives.size(); i++) {
			System.out.print(" " + objectives_names.get(i) + " : " + this.objectives.get(i));
		}
		System.out.println("\n");
	}

	String objectives_names_to_string() {
		String result = " ";
		for (int i = 0; i < this.objectives_names.size(); i++) {
			result += (String) objectives_names.get(i) + " ";
		}
		return result;
	}

	void print_solution() {
		System.out.println("\n --- Printing solution operations ---");
		System.out.println("\n --- Rank : " + this.rank);
		for (int i = 0; i < this.operations.size(); i++) {
			System.out.println("wael , je dois ajouter la liste des operations dans une liste operation");
			// System.out.println(operation[this.operations.get(i)]);
		}
	}

	String objectives_values_to_string() {
		String result = " ";
		for (int i = 0; i < this.objectives.size(); i++) {
			result += Double.toString(objectives.get(i));

			if (i != (this.objectives.size() - 1)) {
				result += " , ";
			}

		}
		return result;
	}
	///////////// FIN FOR NSGA

}
