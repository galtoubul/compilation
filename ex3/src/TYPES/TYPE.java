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

	public boolean isSubtype(TYPE other) {
		// TODO subtype
		return this.name.equals(other.name) || TYPE_NIL.nil_match(other, this);
	}

}
