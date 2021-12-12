package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

import java.util.Optional;

public class AST_STMT_FUNC extends AST_STMT {
    public String id;
    public AST_EXP_LIST argsList;

    public AST_STMT_FUNC(String id, AST_EXP_LIST argsList) {
        this.id = id;
        this.argsList = argsList;
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

    public TYPE_FUNCTION checkFuncUseBeforeDefine() {
        TYPE_FUNCTION funcType = getFuncType();

        // Didn't find function in the lookup
        if (funcType == null) {
            System.out.format(">> ERROR [line] function use before define\n");
            throw new semanticErrorException("line");
        }

        return funcType;
    }

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

    public void checkMatchingParamsArgs(TYPE_LIST argsTypes, TYPE_LIST paramsTypes) {
        while (argsTypes != null && argsTypes.head != null) {

            System.out.println("-- AST_STMT_FUNC\n\t\targsTypes.head.name = " + argsTypes.head.name);
            System.out.println("-- AST_STMT_FUNC\n\t\tparamsList.head.name = " + paramsTypes.head.name);

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
    public TYPE SemantMe(Optional<String> classId) {
        System.out.println("-- AST_STMT_FUNC SemantMe");

        TYPE_FUNCTION funcType = checkFuncUseBeforeDefine();
        System.out.println("-- AST_STMT_FUNC\n\t\tfuncType.name = " + funcType.name);

        TYPE_LIST paramsTypes = funcType.params;
        checkForEmptyAndNonEmptyLists(paramsTypes);

        TYPE_LIST argsTypes = this.argsList.SemantMe(classId);
        checkMatchingParamsArgs(argsTypes, paramsTypes);

        return null;
    }
}


//                if(paramsList.head == null) {
//                    System.out.println("advancing paramsList is null");
//                }else {
//                    System.out.println("advancing paramsList.head.name = " + paramsList.head.name);
//
//                }
//                if(argsTypes.head == null) {
//                    System.out.println("advancing argsTypes is null");
//                }else {
//                    System.out.println("advancing argsTypes.head.name = " + argsTypes.head.name);
//                }