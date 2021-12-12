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

    public TYPE_FUNCTION getFuncType() {
        // function name lookup
        TYPE_FUNCTION funcType = null;
        try {
            funcType = (TYPE_FUNCTION) SYMBOL_TABLE.getInstance().find(this.id);
        } catch (ClassCastException exc) {
            System.out.format(">> ERROR [line] class extends\n");
            throw new semanticErrorException("line");
        }
        return funcType;
    }

    // throws an error if the function is used before definition
    public TYPE_FUNCTION checkFuncUseBeforeDefine() {
        TYPE_FUNCTION funcType = getFuncType();

        // Didn't find function in the lookup
        if (funcType == null) {
            System.out.format(">> ERROR [line] function use before define\n");
            throw new semanticErrorException("line");
        }

        return funcType;
    }

    // throws an error if args list is empty and params list isn't, or vice versa
    public void checkForEmptyAndNonEmptyLists(TYPE_LIST funcParamsList) {
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

    // throws an error if there isn't a match between the parameters list and arguments list
    // (the match includes number of items and items' types)
    public void checkMatchingParamsArgs(TYPE_LIST argsTypes, TYPE_LIST paramsTypes) {
        while (argsTypes != null && argsTypes.head != null) {

            System.out.println("-- AST_EXP_FUNC\n\t\targsTypes.head.name = " + argsTypes.head.name);
            System.out.println("-- AST_EXP_FUNC\n\t\tparamsList.head.name = " + paramsTypes.head.name);

            // The following search is meant to find objects
            TYPE argType = SYMBOL_TABLE.getInstance().find(argsTypes.head.name);

            // argType isn't a variable, trying to check if it is a field of an object (<object_name>.<field_name>)
            if (argType == null && argsList.head instanceof AST_EXP_VAR) {

                // <object_name>.<field_name> = <varSimple>.<varSimpleField>
                AST_VAR_SIMPLE varSimple = ((AST_EXP_VAR) argsList.head).var.getSimple();
                String varSimpleField = ((AST_VAR_FIELD) ((AST_EXP_VAR) argsList.head).var).fieldName;
                System.out.println("-- AST_STMT_FUNC \n\t\tvarSimple.name = " + varSimple.name);
                System.out.println("-- AST_STMT_FUNC \n\t\tvarSimpleField.name" + varSimpleField);

                // varSimpleClassType = the class from which varSimple was created
                TYPE_CLASS varSimpleClassType = (TYPE_CLASS) SYMBOL_TABLE.getInstance().find(varSimple.name);
                System.out.println("-- AST_STMT_FUNC \n\t\tclassType.name = " + varSimpleClassType.name);

                // varSimpleClassTypeDataMembers = the data members of class varSimpleClassType
                TYPE_LIST varSimpleClassTypeDataMembers = varSimpleClassType.data_members;
                System.out.println("-- AST_STMT_FUNC \n\t\tvarSimpleClassType.data_members.head.name" + varSimpleClassType.data_members.head.name);

                // check if varSimpleField is a data member of varSimpleClassType
                boolean argIsEqual = false;
                while (varSimpleClassTypeDataMembers != null && varSimpleClassTypeDataMembers.head != null) {

                    if (!varSimpleClassTypeDataMembers.head.name.equals(varSimpleField)){
                        varSimpleClassTypeDataMembers = varSimpleClassTypeDataMembers.tail;
                    }
                    else {
                        // check for type equality between the parameter and the argument
                        TYPE dataMemberType = ((TYPE_CLASS_VAR_DEC) varSimpleClassTypeDataMembers.head).t;
                        TYPE paramType = SYMBOL_TABLE.getInstance().find(paramsTypes.head.name);

                        System.out.println("-- AST_STMT_FUNC \n\t\tdataMemberType.name = " + dataMemberType.name);
                        System.out.println("-- AST_STMT_FUNC \n\t\tparamType.name = " + paramType.name);

                        if (paramType.getClass().equals(dataMemberType.getClass())) {
                            argIsEqual = true;
                            break;
                        }
                    }
                }
                if (!argIsEqual){
                    System.out.format(">> ERROR [line] arg class doesn't match param class\n");
                    throw new semanticErrorException("line");
                }
                else {
                    paramsTypes = paramsTypes.tail;
                    argsTypes = argsTypes.tail;
                    continue;
                }
            }

            TYPE paramType = SYMBOL_TABLE.getInstance().find(paramsTypes.head.name);

            // the argument isn't an instance of a class
            boolean argIsntInstance = !(argType instanceof TYPE_CLASS);
            // the parameter isn't of the same type of the argument
            boolean argAndParamArentOfTheSameType = !paramType.getClass().equals(argType.getClass());
            boolean condA = argIsntInstance && argAndParamArentOfTheSameType;

            // the argument and the parameter are instances of different classes
            boolean condB = (argType instanceof TYPE_CLASS && paramType instanceof TYPE_CLASS) && !((TYPE_CLASS)paramType).isAncestor((TYPE_CLASS) argType);

            if (condA || condB) {
                System.out.format(">> ERROR [line] type(arg) = %s != %s = type(param)\n",argsTypes.head.name ,paramsTypes.head.name);
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

        TYPE_FUNCTION funcType = checkFuncUseBeforeDefine();
        System.out.println("-- AST_EXP_FUNC\n\t\tfuncType.name = " + funcType.name);

        TYPE_LIST paramsTypes = funcType.params;
        checkForEmptyAndNonEmptyLists(paramsTypes);

        TYPE_LIST argsTypes = this.argsList.SemantMe(fatherClassId);
        checkMatchingParamsArgs(argsTypes, paramsTypes);

        return funcType.returnType;
    }
}
