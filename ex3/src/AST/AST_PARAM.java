package AST;

public class AST_PARAM extends AST_Node {
	public AST_TYPE type;
	public String id;

	public AST_PARAM(AST_TYPE type, String id) {
		this.type = type;
		this.id = id;
	}
}
