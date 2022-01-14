package TYPES;

public abstract class TYPE {
	/******************************/
	/* Every type has a name ... */
	/******************************/
	public String name;

	/*************/
	/* isClass() */
	/*************/
	public boolean isClass() {
		return this instanceof TYPE_CLASS;
	}

	/*************/
	/* isArray() */
	/*************/
	public boolean isArray() {
		return this instanceof TYPE_ARRAY;
	}

	/*************/
	/* isFunction() */
	/*************/
	public boolean isFunction() {
		return this instanceof TYPE_FUNCTION;
	}

	public boolean isType() {
		return this == TYPE_INT.getInstance() || this == TYPE_STRING.getInstance() || this.isClass() || this.isArray();
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof TYPE)
				&& (this.name == ((TYPE) obj).name || (this.name != null && this.name.equals(((TYPE) obj).name)));
	}

	public static boolean isSubtype(TYPE thisType, TYPE otherType) {
		if (thisType == null || otherType == null) {
			return false;
		}
		return thisType.equals(otherType) || TYPE_NIL.nil_match(otherType, thisType) ||
				(thisType instanceof TYPE_CLASS && otherType instanceof TYPE_CLASS &&
						((TYPE_CLASS) otherType).isAncestor((TYPE_CLASS) thisType));
	}

}
