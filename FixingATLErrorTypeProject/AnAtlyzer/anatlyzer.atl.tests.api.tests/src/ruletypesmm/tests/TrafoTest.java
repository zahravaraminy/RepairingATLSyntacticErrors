/**
 */
package ruletypesmm.tests;

import junit.framework.TestCase;

import junit.textui.TestRunner;

import ruletypesmm.RuletypesmmFactory;
import ruletypesmm.Trafo;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Trafo</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class TrafoTest extends TestCase {

	/**
	 * The fixture for this Trafo test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Trafo fixture = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(TrafoTest.class);
	}

	/**
	 * Constructs a new Trafo test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TrafoTest(String name) {
		super(name);
	}

	/**
	 * Sets the fixture for this Trafo test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void setFixture(Trafo fixture) {
		this.fixture = fixture;
	}

	/**
	 * Returns the fixture for this Trafo test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Trafo getFixture() {
		return fixture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(RuletypesmmFactory.eINSTANCE.createTrafo());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#tearDown()
	 * @generated
	 */
	@Override
	protected void tearDown() throws Exception {
		setFixture(null);
	}

} //TrafoTest
