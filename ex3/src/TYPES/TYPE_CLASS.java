package TYPES;

import java.util.Optional;

public class TYPE_CLASS extends TYPE {
	/*********************************************************************/
	/* If this class does not extend a father class this should be null */
	/*********************************************************************/
	public TYPE_CLASS father;

	/**************************************************/
	/* Gather up all data members in one place */
	/* Note that data members coming from the AST are */
	/* packed together with the class methods */
	/**************************************************/
	public TYPE_LIST data_members;

	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_CLASS(TYPE_CLASS father, String name, TYPE_LIST data_members) {
		this.name = name;
		this.father = father;
		this.data_members = data_members;
	}

	public boolean isAncestor(TYPE_CLASS other) {
		do {
			if (this.equals(other)) {
				return true;
			}
			other = other.father;
		} while (other != null && other.father != null);
		if (this.equals(other)) {
			return true;
		}
		return false;
	}

	// look for a member (field or method) name in the given TYPE_CLASS
	// if found -> returns null. OW, returns null
	private Optional<TYPE> lookupMember(String memberName) {
		for (TYPE_LIST dataMembersList = this.data_members; dataMembersList != null; dataMembersList = dataMembersList.tail) {
			System.out.println("-- AST_VAR_FIELD\n\t\tdata member name = " + dataMembersList.head.name);
			System.out.println("-- AST_VAR_FIELD\n\t\tfieldName = " + memberName);

			if (dataMembersList.head.name.equals(memberName)) {
				return Optional.of(dataMembersList.head);
			}
		}
		return Optional.empty();
	}

	public Optional<TYPE> lookupMemberInAncestors(String memberName) {
		TYPE_CLASS varTypeClass = this;
		System.out.println("lookupMemberInAncestors varTypeClass.name = " + varTypeClass);

		while (varTypeClass != null) {
			Optional<TYPE> memberType = varTypeClass.lookupMember(memberName);
			if (memberType.isPresent()) {
				return memberType;
			}

			varTypeClass = varTypeClass.father;
		}

		return Optional.empty();
	}
}
