/**
 */
package ruletypesmm;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see ruletypesmm.RuletypesmmFactory
 * @model kind="package"
 * @generated
 */
public interface RuletypesmmPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "ruletypesmm";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://ruletypesmm/1.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "ruletypesmm";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	RuletypesmmPackage eINSTANCE = ruletypesmm.impl.RuletypesmmPackageImpl.init();

	/**
	 * The meta object id for the '{@link ruletypesmm.impl.RuleImpl <em>Rule</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ruletypesmm.impl.RuleImpl
	 * @see ruletypesmm.impl.RuletypesmmPackageImpl#getRule()
	 * @generated
	 */
	int RULE = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Inherits</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE__INHERITS = 1;

	/**
	 * The feature id for the '<em><b>In Features</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE__IN_FEATURES = 2;

	/**
	 * The feature id for the '<em><b>In Types</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE__IN_TYPES = 3;

	/**
	 * The feature id for the '<em><b>Out Types</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE__OUT_TYPES = 4;

	/**
	 * The feature id for the '<em><b>Out Features Imperative</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE__OUT_FEATURES_IMPERATIVE = 5;

	/**
	 * The feature id for the '<em><b>All Navigation Paths</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE__ALL_NAVIGATION_PATHS = 6;

	/**
	 * The feature id for the '<em><b>Binding Features</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE__BINDING_FEATURES = 7;

	/**
	 * The feature id for the '<em><b>All Foot Prints</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE__ALL_FOOT_PRINTS = 8;

	/**
	 * The feature id for the '<em><b>Out Features</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE__OUT_FEATURES = 9;

	/**
	 * The number of structural features of the '<em>Rule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_FEATURE_COUNT = 10;

	/**
	 * The number of operations of the '<em>Rule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link ruletypesmm.impl.TrafoImpl <em>Trafo</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ruletypesmm.impl.TrafoImpl
	 * @see ruletypesmm.impl.RuletypesmmPackageImpl#getTrafo()
	 * @generated
	 */
	int TRAFO = 1;

	/**
	 * The feature id for the '<em><b>Rules</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRAFO__RULES = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRAFO__NAME = 1;

	/**
	 * The feature id for the '<em><b>Mms</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRAFO__MMS = 2;

	/**
	 * The number of structural features of the '<em>Trafo</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRAFO_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Trafo</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRAFO_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link ruletypesmm.impl.MMNamesImpl <em>MM Names</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ruletypesmm.impl.MMNamesImpl
	 * @see ruletypesmm.impl.RuletypesmmPackageImpl#getMMNames()
	 * @generated
	 */
	int MM_NAMES = 2;

	/**
	 * The feature id for the '<em><b>Input MM</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MM_NAMES__INPUT_MM = 0;

	/**
	 * The feature id for the '<em><b>Output MM</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MM_NAMES__OUTPUT_MM = 1;

	/**
	 * The feature id for the '<em><b>Trafo</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MM_NAMES__TRAFO = 2;

	/**
	 * The number of structural features of the '<em>MM Names</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MM_NAMES_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>MM Names</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MM_NAMES_OPERATION_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link ruletypesmm.Rule <em>Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Rule</em>'.
	 * @see ruletypesmm.Rule
	 * @generated
	 */
	EClass getRule();

	/**
	 * Returns the meta object for the attribute '{@link ruletypesmm.Rule#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see ruletypesmm.Rule#getName()
	 * @see #getRule()
	 * @generated
	 */
	EAttribute getRule_Name();

	/**
	 * Returns the meta object for the reference '{@link ruletypesmm.Rule#getInherits <em>Inherits</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Inherits</em>'.
	 * @see ruletypesmm.Rule#getInherits()
	 * @see #getRule()
	 * @generated
	 */
	EReference getRule_Inherits();

	/**
	 * Returns the meta object for the attribute list '{@link ruletypesmm.Rule#getInFeatures <em>In Features</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>In Features</em>'.
	 * @see ruletypesmm.Rule#getInFeatures()
	 * @see #getRule()
	 * @generated
	 */
	EAttribute getRule_InFeatures();

	/**
	 * Returns the meta object for the attribute list '{@link ruletypesmm.Rule#getInTypes <em>In Types</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>In Types</em>'.
	 * @see ruletypesmm.Rule#getInTypes()
	 * @see #getRule()
	 * @generated
	 */
	EAttribute getRule_InTypes();

	/**
	 * Returns the meta object for the attribute list '{@link ruletypesmm.Rule#getOutTypes <em>Out Types</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Out Types</em>'.
	 * @see ruletypesmm.Rule#getOutTypes()
	 * @see #getRule()
	 * @generated
	 */
	EAttribute getRule_OutTypes();

	/**
	 * Returns the meta object for the attribute list '{@link ruletypesmm.Rule#getOutFeaturesImperative <em>Out Features Imperative</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Out Features Imperative</em>'.
	 * @see ruletypesmm.Rule#getOutFeaturesImperative()
	 * @see #getRule()
	 * @generated
	 */
	EAttribute getRule_OutFeaturesImperative();

	/**
	 * Returns the meta object for the attribute list '{@link ruletypesmm.Rule#getAllNavigationPaths <em>All Navigation Paths</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>All Navigation Paths</em>'.
	 * @see ruletypesmm.Rule#getAllNavigationPaths()
	 * @see #getRule()
	 * @generated
	 */
	EAttribute getRule_AllNavigationPaths();

	/**
	 * Returns the meta object for the attribute list '{@link ruletypesmm.Rule#getBindingFeatures <em>Binding Features</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Binding Features</em>'.
	 * @see ruletypesmm.Rule#getBindingFeatures()
	 * @see #getRule()
	 * @generated
	 */
	EAttribute getRule_BindingFeatures();

	/**
	 * Returns the meta object for the attribute list '{@link ruletypesmm.Rule#getAllFootPrints <em>All Foot Prints</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>All Foot Prints</em>'.
	 * @see ruletypesmm.Rule#getAllFootPrints()
	 * @see #getRule()
	 * @generated
	 */
	EAttribute getRule_AllFootPrints();

	/**
	 * Returns the meta object for the attribute list '{@link ruletypesmm.Rule#getOutFeatures <em>Out Features</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Out Features</em>'.
	 * @see ruletypesmm.Rule#getOutFeatures()
	 * @see #getRule()
	 * @generated
	 */
	EAttribute getRule_OutFeatures();

	/**
	 * Returns the meta object for class '{@link ruletypesmm.Trafo <em>Trafo</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Trafo</em>'.
	 * @see ruletypesmm.Trafo
	 * @generated
	 */
	EClass getTrafo();

	/**
	 * Returns the meta object for the containment reference list '{@link ruletypesmm.Trafo#getRules <em>Rules</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Rules</em>'.
	 * @see ruletypesmm.Trafo#getRules()
	 * @see #getTrafo()
	 * @generated
	 */
	EReference getTrafo_Rules();

	/**
	 * Returns the meta object for the attribute '{@link ruletypesmm.Trafo#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see ruletypesmm.Trafo#getName()
	 * @see #getTrafo()
	 * @generated
	 */
	EAttribute getTrafo_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link ruletypesmm.Trafo#getMms <em>Mms</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Mms</em>'.
	 * @see ruletypesmm.Trafo#getMms()
	 * @see #getTrafo()
	 * @generated
	 */
	EReference getTrafo_Mms();

	/**
	 * Returns the meta object for class '{@link ruletypesmm.MMNames <em>MM Names</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>MM Names</em>'.
	 * @see ruletypesmm.MMNames
	 * @generated
	 */
	EClass getMMNames();

	/**
	 * Returns the meta object for the attribute '{@link ruletypesmm.MMNames#getInputMM <em>Input MM</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Input MM</em>'.
	 * @see ruletypesmm.MMNames#getInputMM()
	 * @see #getMMNames()
	 * @generated
	 */
	EAttribute getMMNames_InputMM();

	/**
	 * Returns the meta object for the attribute '{@link ruletypesmm.MMNames#getOutputMM <em>Output MM</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Output MM</em>'.
	 * @see ruletypesmm.MMNames#getOutputMM()
	 * @see #getMMNames()
	 * @generated
	 */
	EAttribute getMMNames_OutputMM();

	/**
	 * Returns the meta object for the container reference '{@link ruletypesmm.MMNames#getTrafo <em>Trafo</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Trafo</em>'.
	 * @see ruletypesmm.MMNames#getTrafo()
	 * @see #getMMNames()
	 * @generated
	 */
	EReference getMMNames_Trafo();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	RuletypesmmFactory getRuletypesmmFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link ruletypesmm.impl.RuleImpl <em>Rule</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ruletypesmm.impl.RuleImpl
		 * @see ruletypesmm.impl.RuletypesmmPackageImpl#getRule()
		 * @generated
		 */
		EClass RULE = eINSTANCE.getRule();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RULE__NAME = eINSTANCE.getRule_Name();

		/**
		 * The meta object literal for the '<em><b>Inherits</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RULE__INHERITS = eINSTANCE.getRule_Inherits();

		/**
		 * The meta object literal for the '<em><b>In Features</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RULE__IN_FEATURES = eINSTANCE.getRule_InFeatures();

		/**
		 * The meta object literal for the '<em><b>In Types</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RULE__IN_TYPES = eINSTANCE.getRule_InTypes();

		/**
		 * The meta object literal for the '<em><b>Out Types</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RULE__OUT_TYPES = eINSTANCE.getRule_OutTypes();

		/**
		 * The meta object literal for the '<em><b>Out Features Imperative</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RULE__OUT_FEATURES_IMPERATIVE = eINSTANCE.getRule_OutFeaturesImperative();

		/**
		 * The meta object literal for the '<em><b>All Navigation Paths</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RULE__ALL_NAVIGATION_PATHS = eINSTANCE.getRule_AllNavigationPaths();

		/**
		 * The meta object literal for the '<em><b>Binding Features</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RULE__BINDING_FEATURES = eINSTANCE.getRule_BindingFeatures();

		/**
		 * The meta object literal for the '<em><b>All Foot Prints</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RULE__ALL_FOOT_PRINTS = eINSTANCE.getRule_AllFootPrints();

		/**
		 * The meta object literal for the '<em><b>Out Features</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RULE__OUT_FEATURES = eINSTANCE.getRule_OutFeatures();

		/**
		 * The meta object literal for the '{@link ruletypesmm.impl.TrafoImpl <em>Trafo</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ruletypesmm.impl.TrafoImpl
		 * @see ruletypesmm.impl.RuletypesmmPackageImpl#getTrafo()
		 * @generated
		 */
		EClass TRAFO = eINSTANCE.getTrafo();

		/**
		 * The meta object literal for the '<em><b>Rules</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRAFO__RULES = eINSTANCE.getTrafo_Rules();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRAFO__NAME = eINSTANCE.getTrafo_Name();

		/**
		 * The meta object literal for the '<em><b>Mms</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRAFO__MMS = eINSTANCE.getTrafo_Mms();

		/**
		 * The meta object literal for the '{@link ruletypesmm.impl.MMNamesImpl <em>MM Names</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ruletypesmm.impl.MMNamesImpl
		 * @see ruletypesmm.impl.RuletypesmmPackageImpl#getMMNames()
		 * @generated
		 */
		EClass MM_NAMES = eINSTANCE.getMMNames();

		/**
		 * The meta object literal for the '<em><b>Input MM</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MM_NAMES__INPUT_MM = eINSTANCE.getMMNames_InputMM();

		/**
		 * The meta object literal for the '<em><b>Output MM</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MM_NAMES__OUTPUT_MM = eINSTANCE.getMMNames_OutputMM();

		/**
		 * The meta object literal for the '<em><b>Trafo</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MM_NAMES__TRAFO = eINSTANCE.getMMNames_Trafo();

	}

} //RuletypesmmPackage
