package TYPES;

import java.util.Optional;

public class TYPE_CLASS extends TYPE {
	/*********************************************************************/
	/* If this class does not extend a father class this should be null */
	/*********************************************************************/
	public Optional<TYPE_CLASS> father;

	/**************************************************/
	/* Gather up all data members in one place */
	/* Note that data members coming from the AST are */
	/* packed together with the class methods */
	/**************************************************/
	public TYPE_LIST data_members;

	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_CLASS(Optional<TYPE_CLASS> father, String name, TYPE_LIST data_members) {
		this.name = name;
		this.father = father;
		this.data_members = data_members;
	}

	/**
	 * Check if the class `other` inherits from this class (directly or indirectly).
	 */
	public boolean isAncestor(TYPE_CLASS other) {
		for (; other.father.isPresent(); other = other.father.get()) {
			if (this.equals(other.father.get())) {
				return true;
			}
		}
		return false;
	}

	// look for a member (field or method) name in the given TYPE_CLASS
	// if found -> returns null. OW, returns null
	private Optional<TYPE> lookupMember(String memberName) {
		for (TYPE member : this.data_members) {
			System.out.println("-- lookupMember\n\t\tdata member name = " + member.name);
			System.out.println("-- lookupMember\n\t\tfieldName = " + memberName);

			if (member.name.equals(memberName)) {
				return Optional.of(member);
			}
		}

		return Optional.empty();
	}

	public Optional<TYPE> lookupMemberInAncestors(String memberName) {
		Optional<TYPE_CLASS> varTypeClass = Optional.of(this);
		System.out.println("lookupMemberInAncestors varTypeClass.name = " + varTypeClass.get().name);

		while (varTypeClass.isPresent()) {
			Optional<TYPE> memberType = varTypeClass.get().lookupMember(memberName);
			if (memberType.isPresent()) {
				return memberType;
			}

			varTypeClass = varTypeClass.get().father;
		}

		return Optional.empty();
	}
}
