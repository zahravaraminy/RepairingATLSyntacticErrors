/**
 */
package ruletypesmm.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

import ruletypesmm.Rule;
import ruletypesmm.RuletypesmmPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Rule</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link ruletypesmm.impl.RuleImpl#getName <em>Name</em>}</li>
 *   <li>{@link ruletypesmm.impl.RuleImpl#getInherits <em>Inherits</em>}</li>
 *   <li>{@link ruletypesmm.impl.RuleImpl#getInFeatures <em>In Features</em>}</li>
 *   <li>{@link ruletypesmm.impl.RuleImpl#getInTypes <em>In Types</em>}</li>
 *   <li>{@link ruletypesmm.impl.RuleImpl#getOutTypes <em>Out Types</em>}</li>
 *   <li>{@link ruletypesmm.impl.RuleImpl#getOutFeaturesImperative <em>Out Features Imperative</em>}</li>
 *   <li>{@link ruletypesmm.impl.RuleImpl#getAllNavigationPaths <em>All Navigation Paths</em>}</li>
 *   <li>{@link ruletypesmm.impl.RuleImpl#getBindingFeatures <em>Binding Features</em>}</li>
 *   <li>{@link ruletypesmm.impl.RuleImpl#getAllFootPrints <em>All Foot Prints</em>}</li>
 *   <li>{@link ruletypesmm.impl.RuleImpl#getOutFeatures <em>Out Features</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RuleImpl extends MinimalEObjectImpl.Container implements Rule {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getInherits() <em>Inherits</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInherits()
	 * @generated
	 * @ordered
	 */
	protected Rule inherits;

	/**
	 * The cached value of the '{@link #getInFeatures() <em>In Features</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInFeatures()
	 * @generated
	 * @ordered
	 */
	protected EList<String> inFeatures;

	/**
	 * The cached value of the '{@link #getInTypes() <em>In Types</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInTypes()
	 * @generated
	 * @ordered
	 */
	protected EList<String> inTypes;

	/**
	 * The cached value of the '{@link #getOutTypes() <em>Out Types</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOutTypes()
	 * @generated
	 * @ordered
	 */
	protected EList<String> outTypes;

	/**
	 * The cached value of the '{@link #getOutFeaturesImperative() <em>Out Features Imperative</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOutFeaturesImperative()
	 * @generated
	 * @ordered
	 */
	protected EList<String> outFeaturesImperative;

	/**
	 * The cached value of the '{@link #getAllNavigationPaths() <em>All Navigation Paths</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAllNavigationPaths()
	 * @generated
	 * @ordered
	 */
	protected EList<String> allNavigationPaths;

	/**
	 * The cached value of the '{@link #getBindingFeatures() <em>Binding Features</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBindingFeatures()
	 * @generated
	 * @ordered
	 */
	protected EList<String> bindingFeatures;

	/**
	 * The cached value of the '{@link #getAllFootPrints() <em>All Foot Prints</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAllFootPrints()
	 * @generated
	 * @ordered
	 */
	protected EList<String> allFootPrints;

	/**
	 * The cached value of the '{@link #getOutFeatures() <em>Out Features</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOutFeatures()
	 * @generated
	 * @ordered
	 */
	protected EList<String> outFeatures;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RuleImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RuletypesmmPackage.Literals.RULE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuletypesmmPackage.RULE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Rule getInherits() {
		if (inherits != null && inherits.eIsProxy()) {
			InternalEObject oldInherits = (InternalEObject)inherits;
			inherits = (Rule)eResolveProxy(oldInherits);
			if (inherits != oldInherits) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, RuletypesmmPackage.RULE__INHERITS, oldInherits, inherits));
			}
		}
		return inherits;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Rule basicGetInherits() {
		return inherits;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInherits(Rule newInherits) {
		Rule oldInherits = inherits;
		inherits = newInherits;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuletypesmmPackage.RULE__INHERITS, oldInherits, inherits));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getInFeatures() {
		if (inFeatures == null) {
			inFeatures = new EDataTypeEList<String>(String.class, this, RuletypesmmPackage.RULE__IN_FEATURES);
		}
		return inFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getInTypes() {
		if (inTypes == null) {
			inTypes = new EDataTypeUniqueEList<String>(String.class, this, RuletypesmmPackage.RULE__IN_TYPES);
		}
		return inTypes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getOutTypes() {
		if (outTypes == null) {
			outTypes = new EDataTypeUniqueEList<String>(String.class, this, RuletypesmmPackage.RULE__OUT_TYPES);
		}
		return outTypes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getOutFeaturesImperative() {
		if (outFeaturesImperative == null) {
			outFeaturesImperative = new EDataTypeEList<String>(String.class, this, RuletypesmmPackage.RULE__OUT_FEATURES_IMPERATIVE);
		}
		return outFeaturesImperative;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getAllNavigationPaths() {
		if (allNavigationPaths == null) {
			allNavigationPaths = new EDataTypeUniqueEList<String>(String.class, this, RuletypesmmPackage.RULE__ALL_NAVIGATION_PATHS);
		}
		return allNavigationPaths;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getBindingFeatures() {
		if (bindingFeatures == null) {
			bindingFeatures = new EDataTypeEList<String>(String.class, this, RuletypesmmPackage.RULE__BINDING_FEATURES);
		}
		return bindingFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getAllFootPrints() {
		if (allFootPrints == null) {
			allFootPrints = new EDataTypeUniqueEList<String>(String.class, this, RuletypesmmPackage.RULE__ALL_FOOT_PRINTS);
		}
		return allFootPrints;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getOutFeatures() {
		if (outFeatures == null) {
			outFeatures = new EDataTypeEList<String>(String.class, this, RuletypesmmPackage.RULE__OUT_FEATURES);
		}
		return outFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case RuletypesmmPackage.RULE__NAME:
				return getName();
			case RuletypesmmPackage.RULE__INHERITS:
				if (resolve) return getInherits();
				return basicGetInherits();
			case RuletypesmmPackage.RULE__IN_FEATURES:
				return getInFeatures();
			case RuletypesmmPackage.RULE__IN_TYPES:
				return getInTypes();
			case RuletypesmmPackage.RULE__OUT_TYPES:
				return getOutTypes();
			case RuletypesmmPackage.RULE__OUT_FEATURES_IMPERATIVE:
				return getOutFeaturesImperative();
			case RuletypesmmPackage.RULE__ALL_NAVIGATION_PATHS:
				return getAllNavigationPaths();
			case RuletypesmmPackage.RULE__BINDING_FEATURES:
				return getBindingFeatures();
			case RuletypesmmPackage.RULE__ALL_FOOT_PRINTS:
				return getAllFootPrints();
			case RuletypesmmPackage.RULE__OUT_FEATURES:
				return getOutFeatures();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case RuletypesmmPackage.RULE__NAME:
				setName((String)newValue);
				return;
			case RuletypesmmPackage.RULE__INHERITS:
				setInherits((Rule)newValue);
				return;
			case RuletypesmmPackage.RULE__IN_FEATURES:
				getInFeatures().clear();
				getInFeatures().addAll((Collection<? extends String>)newValue);
				return;
			case RuletypesmmPackage.RULE__IN_TYPES:
				getInTypes().clear();
				getInTypes().addAll((Collection<? extends String>)newValue);
				return;
			case RuletypesmmPackage.RULE__OUT_TYPES:
				getOutTypes().clear();
				getOutTypes().addAll((Collection<? extends String>)newValue);
				return;
			case RuletypesmmPackage.RULE__OUT_FEATURES_IMPERATIVE:
				getOutFeaturesImperative().clear();
				getOutFeaturesImperative().addAll((Collection<? extends String>)newValue);
				return;
			case RuletypesmmPackage.RULE__ALL_NAVIGATION_PATHS:
				getAllNavigationPaths().clear();
				getAllNavigationPaths().addAll((Collection<? extends String>)newValue);
				return;
			case RuletypesmmPackage.RULE__BINDING_FEATURES:
				getBindingFeatures().clear();
				getBindingFeatures().addAll((Collection<? extends String>)newValue);
				return;
			case RuletypesmmPackage.RULE__ALL_FOOT_PRINTS:
				getAllFootPrints().clear();
				getAllFootPrints().addAll((Collection<? extends String>)newValue);
				return;
			case RuletypesmmPackage.RULE__OUT_FEATURES:
				getOutFeatures().clear();
				getOutFeatures().addAll((Collection<? extends String>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case RuletypesmmPackage.RULE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case RuletypesmmPackage.RULE__INHERITS:
				setInherits((Rule)null);
				return;
			case RuletypesmmPackage.RULE__IN_FEATURES:
				getInFeatures().clear();
				return;
			case RuletypesmmPackage.RULE__IN_TYPES:
				getInTypes().clear();
				return;
			case RuletypesmmPackage.RULE__OUT_TYPES:
				getOutTypes().clear();
				return;
			case RuletypesmmPackage.RULE__OUT_FEATURES_IMPERATIVE:
				getOutFeaturesImperative().clear();
				return;
			case RuletypesmmPackage.RULE__ALL_NAVIGATION_PATHS:
				getAllNavigationPaths().clear();
				return;
			case RuletypesmmPackage.RULE__BINDING_FEATURES:
				getBindingFeatures().clear();
				return;
			case RuletypesmmPackage.RULE__ALL_FOOT_PRINTS:
				getAllFootPrints().clear();
				return;
			case RuletypesmmPackage.RULE__OUT_FEATURES:
				getOutFeatures().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case RuletypesmmPackage.RULE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case RuletypesmmPackage.RULE__INHERITS:
				return inherits != null;
			case RuletypesmmPackage.RULE__IN_FEATURES:
				return inFeatures != null && !inFeatures.isEmpty();
			case RuletypesmmPackage.RULE__IN_TYPES:
				return inTypes != null && !inTypes.isEmpty();
			case RuletypesmmPackage.RULE__OUT_TYPES:
				return outTypes != null && !outTypes.isEmpty();
			case RuletypesmmPackage.RULE__OUT_FEATURES_IMPERATIVE:
				return outFeaturesImperative != null && !outFeaturesImperative.isEmpty();
			case RuletypesmmPackage.RULE__ALL_NAVIGATION_PATHS:
				return allNavigationPaths != null && !allNavigationPaths.isEmpty();
			case RuletypesmmPackage.RULE__BINDING_FEATURES:
				return bindingFeatures != null && !bindingFeatures.isEmpty();
			case RuletypesmmPackage.RULE__ALL_FOOT_PRINTS:
				return allFootPrints != null && !allFootPrints.isEmpty();
			case RuletypesmmPackage.RULE__OUT_FEATURES:
				return outFeatures != null && !outFeatures.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(", inFeatures: ");
		result.append(inFeatures);
		result.append(", inTypes: ");
		result.append(inTypes);
		result.append(", outTypes: ");
		result.append(outTypes);
		result.append(", outFeaturesImperative: ");
		result.append(outFeaturesImperative);
		result.append(", allNavigationPaths: ");
		result.append(allNavigationPaths);
		result.append(", bindingFeatures: ");
		result.append(bindingFeatures);
		result.append(", allFootPrints: ");
		result.append(allFootPrints);
		result.append(", outFeatures: ");
		result.append(outFeatures);
		result.append(')');
		return result.toString();
	}

} //RuleImpl
