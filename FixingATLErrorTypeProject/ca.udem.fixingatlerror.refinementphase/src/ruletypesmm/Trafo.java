/**
 */
package ruletypesmm;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Trafo</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link ruletypesmm.Trafo#getRules <em>Rules</em>}</li>
 *   <li>{@link ruletypesmm.Trafo#getName <em>Name</em>}</li>
 *   <li>{@link ruletypesmm.Trafo#getMms <em>Mms</em>}</li>
 * </ul>
 *
 * @see ruletypesmm.RuletypesmmPackage#getTrafo()
 * @model
 * @generated
 */
public interface Trafo extends EObject {
	/**
	 * Returns the value of the '<em><b>Rules</b></em>' containment reference list.
	 * The list contents are of type {@link ruletypesmm.Rule}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rules</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rules</em>' containment reference list.
	 * @see ruletypesmm.RuletypesmmPackage#getTrafo_Rules()
	 * @model containment="true"
	 * @generated
	 */
	EList<Rule> getRules();

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see ruletypesmm.RuletypesmmPackage#getTrafo_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link ruletypesmm.Trafo#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Mms</b></em>' containment reference list.
	 * The list contents are of type {@link ruletypesmm.MMNames}.
	 * It is bidirectional and its opposite is '{@link ruletypesmm.MMNames#getTrafo <em>Trafo</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Mms</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mms</em>' containment reference list.
	 * @see ruletypesmm.RuletypesmmPackage#getTrafo_Mms()
	 * @see ruletypesmm.MMNames#getTrafo
	 * @model opposite="trafo" containment="true"
	 * @generated
	 */
	EList<MMNames> getMms();

} // Trafo
