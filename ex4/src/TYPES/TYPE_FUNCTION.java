package TYPES;

public class TYPE_FUNCTION extends TYPE
{

	public TYPE returnType;
	public TYPE_LIST params;
	public int localVarsNum = 0;
	
	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_FUNCTION(TYPE returnType, String name, TYPE_LIST params, int localVarsNum) {
		this.name = name;
		this.returnType = returnType;
		this.params = params;
		this.localVarsNum = localVarsNum;
	}

	public TYPE_FUNCTION(TYPE returnType, String name, TYPE_LIST params) {
		this.name = name;
		this.returnType = returnType;
		this.params = params;
	}
}
