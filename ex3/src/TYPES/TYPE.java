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

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof TYPE) && this.name.equals(((TYPE) obj).name);
	}

	public boolean isSubtype(TYPE other) {
		return this.equals(other) || TYPE_NIL.nil_match(other, this) ||
				(this instanceof TYPE_CLASS && other instanceof TYPE_CLASS &&
						((TYPE_CLASS) other).isAncestor((TYPE_CLASS) this));
	}

}
