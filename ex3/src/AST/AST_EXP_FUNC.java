package AST;

import java.util.Optional;

import TYPES.TYPE;

public class AST_EXP_FUNC extends AST_EXP {
    public String id;
    public AST_EXP_LIST arguments;

    public AST_EXP_FUNC(String id, AST_EXP_LIST arguments) {
        SerialNumber = AST_Node_Serial_Number.getFresh();

        this.id = id;
        this.arguments = arguments;
    }

    public void PrintMe() {
        /*************************************************/
        /* AST NODE TYPE = AST NODE FUNCTION DECLARATION */
        /*************************************************/
        System.out.format("CALL(%s)\nWITH:\n", id);

        /***************************************/
        /* RECURSIVELY PRINT params + body ... */
        /***************************************/
        if (arguments != null)
            arguments.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("CALL(%s)\nWITH", id));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, arguments.SerialNumber);
    }

    @Override
    public TYPE SemantMe(Optional<String> classId) {
        return null;
    }
}
