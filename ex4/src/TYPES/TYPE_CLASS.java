package TYPES;

import pair.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	// public TYPE_LIST data_members;
	public List<TYPE> data_members;

	/**
	 * A list of the initial values that data members (variables) may have.
	 * The value can be either int (stored as an `Integer`) or string (stored as a
	 * `String`).
	 */
	public ArrayList<Pair<String, Optional<Object>>> initialValues; // Very disgusting
	public ArrayList<Pair<String, String>> vtable = new ArrayList<>();

	public HashMap<String, Integer> methodOffsets = new HashMap<>();

	public int fieldsNum = 0;

	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_CLASS(Optional<TYPE_CLASS> father, String name, List<TYPE> data_members, int fieldsNum) {
		this(father, name, data_members, fieldsNum,
				father.isPresent() ? new ArrayList<>(father.get().initialValues) : new ArrayList<>(),
				father.isPresent() ? new ArrayList<>(father.get().vtable) : new ArrayList<>(),
				father.isPresent() ? new HashMap<>(father.get().methodOffsets) : new HashMap<>());
	}

	private TYPE_CLASS(Optional<TYPE_CLASS> father, String name, List<TYPE> data_members, int fieldsNum,
			ArrayList<Pair<String, Optional<Object>>> initialValues,
			ArrayList<Pair<String, String>> vtable, HashMap<String, Integer> methodOffsets) {
		this.name = name;
		this.father = father;
		this.data_members = data_members;
		this.fieldsNum = fieldsNum;
		this.initialValues = initialValues;
		this.vtable = vtable;
		this.methodOffsets = methodOffsets;
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
				System.out.println(Optional.of(member));
				return Optional.of(member);
			}
		}

		return Optional.empty();
	}

	public Optional<TYPE> lookupMemberInAncestors(String memberName) {
		Optional<TYPE_CLASS> varTypeClass = Optional.of(this);
		System.out.println("lookupMemberInAncestors varTypeClass.name = " + varTypeClass.get().name);
		System.out.println("memberName = " + memberName);

		while (varTypeClass.isPresent()) {
			Optional<TYPE> memberType = varTypeClass.get().lookupMember(memberName);
			System.out.println(memberType);
			if (memberType.isPresent()) {
				System.out.println("here!");
				return memberType;
			}

			varTypeClass = varTypeClass.get().father;
		}

		return Optional.empty();
	}
}
