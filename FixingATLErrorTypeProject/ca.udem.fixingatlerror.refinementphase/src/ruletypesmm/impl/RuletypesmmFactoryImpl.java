/**
 */
package ruletypesmm.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import ruletypesmm.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class RuletypesmmFactoryImpl extends EFactoryImpl implements RuletypesmmFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static RuletypesmmFactory init() {
		try {
			RuletypesmmFactory theRuletypesmmFactory = (RuletypesmmFactory)EPackage.Registry.INSTANCE.getEFactory(RuletypesmmPackage.eNS_URI);
			if (theRuletypesmmFactory != null) {
				return theRuletypesmmFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new RuletypesmmFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RuletypesmmFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case RuletypesmmPackage.RULE: return createRule();
			case RuletypesmmPackage.TRAFO: return createTrafo();
			case RuletypesmmPackage.MM_NAMES: return createMMNames();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Rule createRule() {
		RuleImpl rule = new RuleImpl();
		return rule;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Trafo createTrafo() {
		TrafoImpl trafo = new TrafoImpl();
		return trafo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MMNames createMMNames() {
		MMNamesImpl mmNames = new MMNamesImpl();
		return mmNames;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RuletypesmmPackage getRuletypesmmPackage() {
		return (RuletypesmmPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static RuletypesmmPackage getPackage() {
		return RuletypesmmPackage.eINSTANCE;
	}

} //RuletypesmmFactoryImpl
