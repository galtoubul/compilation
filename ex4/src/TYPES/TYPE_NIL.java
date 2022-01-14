package TYPES;

public class TYPE_NIL extends TYPE {
	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static TYPE_NIL instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected TYPE_NIL() {
	}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static TYPE_NIL getInstance() {
		if (instance == null) {
			instance = new TYPE_NIL();
			instance.name = "nil";

		}
		return instance;
	}

	public static boolean convertible(TYPE type) {
		return type.isClass() || type.isArray() || type instanceof TYPE_NIL;
	}

	public static boolean nil_match(TYPE convertible, TYPE matchedType) {
		return (TYPE_NIL.convertible(convertible) && matchedType == TYPE_NIL.getInstance());
	}
}
