/**
 */
package ruletypesmm.tests;

import junit.framework.TestCase;

import junit.textui.TestRunner;

import ruletypesmm.MMNames;
import ruletypesmm.RuletypesmmFactory;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>MM Names</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class MMNamesTest extends TestCase {

	/**
	 * The fixture for this MM Names test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MMNames fixture = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(MMNamesTest.class);
	}

	/**
	 * Constructs a new MM Names test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MMNamesTest(String name) {
		super(name);
	}

	/**
	 * Sets the fixture for this MM Names test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void setFixture(MMNames fixture) {
		this.fixture = fixture;
	}

	/**
	 * Returns the fixture for this MM Names test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MMNames getFixture() {
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
		setFixture(RuletypesmmFactory.eINSTANCE.createMMNames());
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

} //MMNamesTest
