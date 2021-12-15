package AST;

import java.util.Optional;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public class AST_EXP_FUNC extends AST_EXP {
    public String id;
    public AST_EXP_LIST argsList;

    public AST_EXP_FUNC(String id, AST_EXP_LIST argsList) {
        SerialNumber = AST_Node_Serial_Number.getFresh();

        this.id = id;
        this.argsList = argsList;
    }

    public void PrintMe() {
        System.out.format("CALL(%s)\nWITH:\n", id);

        /***************************************/
        /* RECURSIVELY PRINT params + body ... */
        /***************************************/
        if (argsList != null)
            argsList.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("CALL(%s)\nWITH", id));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, argsList.SerialNumber);
    }

    // throws an error if the function is used before definition
    private TYPE_FUNCTION getFunctionType() {
        TYPE type = SYMBOL_TABLE.getInstance().find(this.id);

        // Didn't find function in the lookup
        if (type == null) {
            System.out.format(">> ERROR [line] function use before define\n");
            throw new semanticErrorException("line");
        }

        if (!(type instanceof TYPE_FUNCTION)) {
            System.out.format(">> ERROR [line] '%s' is not a function\n", this.id);
            throw new semanticErrorException("line");
        }

        return (TYPE_FUNCTION) type;
    }

    // throws an error if args list is empty and params list isn't, or vice versa
    private void checkForEmptyLists(TYPE_LIST funcParamsList) {
        // empty arguments list, but non-empty parameters list
        if (this.argsList == null && funcParamsList != null) {
            System.out.format(">> ERROR [line] args# (=0) != params#\n");
            throw new semanticErrorException("line");
        }
        // empty parameters list, but non-empty arguments list
        if (this.argsList != null && funcParamsList == null) {
            System.out.format(">> ERROR [line] args# != params# (=0)\n");
            throw new semanticErrorException("line");
        }
    }

    // throws an error if there isn't a match between the parameters list and
    // arguments list
    // (the match includes number of items and items' types)
    private void checkMatchingParamsArgs(TYPE_LIST argsTypes, TYPE_LIST paramsTypes) {

        // Compare the number of parameters and arguments
        if (argsTypes.length() != paramsTypes.length()) {
            System.out.format(">> ERROR [line] %s arguments in call of '%s'\n",
                    argsTypes.length() < paramsTypes.length() ? "missing" : "too many",
                    this.id);
            throw new semanticErrorException("line");
        }

        // Match the types of each argument to that of a parameter
        while (argsTypes != null && argsTypes.head != null) {

            if (!TYPE.isSubtype(argsTypes.head, paramsTypes.head)) {
                System.out.format(">> ERROR [line] type(arg) = %s != %s = type(param)\n", argsTypes.head.name,
                        paramsTypes.head.name);
                throw new semanticErrorException("line");
            }

            paramsTypes = paramsTypes.tail;
            argsTypes = argsTypes.tail;
        }
    }

    // diff between args to params: foo(<args>) | puclic int foo(<params>)

    @Override
    public TYPE SemantMe(Optional<String> fatherClassId) {
        System.out.println("-- AST_EXP_FUNC SemantMe");

        TYPE_FUNCTION funcType = getFunctionType();
        System.out.println("-- AST_EXP_FUNC\n\t\tfuncType.name = " + funcType.name);

        TYPE_LIST paramsTypes = funcType.params;
        checkForEmptyLists(paramsTypes);

        TYPE_LIST argsTypes = this.argsList.SemantMe(fatherClassId);
        checkMatchingParamsArgs(argsTypes, paramsTypes);

        return funcType.returnType;
    }
}
