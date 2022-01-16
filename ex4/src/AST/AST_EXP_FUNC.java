package AST;

import java.util.Optional;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;
import TEMP.*;
import IR.*;

public class AST_EXP_FUNC extends AST_EXP {
    public String id;
    public AST_EXP_LIST argsList;

    public AST_EXP_FUNC(String id, AST_EXP_LIST argsList) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        System.out.println("-- AST_EXP_FUNC ctor\n\n\t line num = " + lineNum);
        this.id = id;
        if (argsList == null) {
            this.argsList = new AST_EXP_LIST(null, null);
        } else {
            this.argsList = argsList;
        }
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
        // Search the function in the symbol table or class scopes
        Optional<TYPE> type = SYMBOL_TABLE.getInstance().findPotentialMember(this.id);
        if (type.isPresent()) {
            if (type.get().isFunction()) {
                return (TYPE_FUNCTION) type.get();
            } else {
                System.out.format(">> ERROR [%d] '%s' is a not a funciton\n", lineNum, this.id);
            }
        } else {
            System.out.format(">> ERROR [%d] funciton '%s' does not exist at the current scope/outer scopes\n",
                    lineNum, this.id);
        }

        throw new SemanticErrorException("" + lineNum);
    }

    // throws an error if there isn't a match between the parameters list and
    // arguments list
    // (the match includes number of items and items' types)
    private void checkMatchingParamsArgs(TYPE_LIST argsTypes, TYPE_LIST paramsTypes) {

        // Compare the number of parameters and arguments
        if (argsTypes.length() != paramsTypes.length()) {
            System.out.format(">> ERROR [" + lineNum + "] %s arguments in call of '%s'\n",
                    argsTypes.length() < paramsTypes.length() ? "missing" : "too many",
                    this.id);
            throw new SemanticErrorException("" + lineNum);
        }

        // Match the types of each argument to that of a parameter
        while (argsTypes != null && argsTypes.head != null) {

            if (!TYPE.isSubtype(argsTypes.head, paramsTypes.head)) {
                System.out.format(">> ERROR [" + lineNum + "] type(arg) = %s != %s = type(param)\n",
                        argsTypes.head.name,
                        paramsTypes.head.name);
                throw new SemanticErrorException("" + lineNum);
            }

            paramsTypes = paramsTypes.tail;
            argsTypes = argsTypes.tail;
        }
    }

    // diff between args to params: foo(<args>) | puclic int foo(<params>)

    @Override
    public TYPE SemantMe(Optional<String> fatherClassId) {
        System.out.println("-- AST_EXP_FUNC SemantMe");

        System.out.println("-- AST_EXP_FUNC SemantMe\n\n\tline number = " + lineNum);
        TYPE_FUNCTION funcType = getFunctionType();
        System.out.println("-- AST_EXP_FUNC\n\t\tfuncType.name = " + funcType.name);

        TYPE_LIST paramsTypes = funcType.params;
        // checkForEmptyLists(paramsTypes);

        if (paramsTypes == null) {
            paramsTypes = new TYPE_LIST(null, null);
        }

        TYPE_LIST argsTypes = this.argsList.SemantMe(fatherClassId);
        checkMatchingParamsArgs(argsTypes, paramsTypes);

        return funcType.returnType;
    }

    public TEMP IRme() {
        System.out.println("-- AST_EXP_FUNC IRme");

        TEMP_LIST argsTempList = null;
        if (argsList != null) {
            argsTempList = argsList.IRme();
        }

        TEMP funcRetValTemp = TEMP_FACTORY.getInstance().getFreshTEMP();
        IR.getInstance().Add_IRcommand(new IRcommand_Call_Func_Exp(funcRetValTemp, id, argsTempList));

        return funcRetValTemp;
    }
}
