package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

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

    // diff between args to params: foo(<args>) | puclic int foo(<params>)

    @Override
    public TYPE SemantMe() {
        System.out.println("-- AST_STMT_FUNC SemantMe");

        TYPE_FUNCTION funcType = getFuncType();

        // Didn't find function in the lookup
        if (funcType == null) {
            System.out.format(">> ERROR [line] function use before define\n");
            throw new semanticErrorException("line");
        } else {
            System.out.println("-- AST_STMT_FUNC \n\t\tfuncType = " + funcType.name);
        }

        TYPE_LIST funcParamsList = funcType.params;
        if (this.argsList != null) {

            TYPE_LIST argsTypes = this.argsList.SemantMe();
            while (argsTypes != null && argsTypes.head != null) {

                if (argsTypes.head instanceof TYPE_NONE) {
                    System.out.format(">> ERROR [line] parameter use before define\n");
                    throw new semanticErrorException("line");
                }
                else {
                    System.out.println("-- AST_STMT_FUNC \n\t\tparameter was defined before using it");
                    System.out.println("-- AST_STMT_FUNC \n\t\targsTypes.head.name = " + argsTypes.head.name);

                    // The following search is meant to find objects
                    TYPE argType = SYMBOL_TABLE.getInstance().find(argsTypes.head.name);

                    // argType isnt a variable, trying to check if it is a field of an object (<object_name>.<field_name>)
                    if (argType == null && argsList.head instanceof AST_EXP_VAR) {

                        // <object_name>.<field_name> = <varSimple><varSimpleField>
                        AST_VAR_SIMPLE varSimple = ((AST_EXP_VAR) argsList.head).var.getSimple();
                        String varSimpleField = ((AST_VAR_FIELD)((AST_EXP_VAR) argsList.head).var).fieldName;
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

                            // name(varSimpleClassType data member) != name(varSimpleField)
                            if(!varSimpleClassTypeDataMembers.head.name.equals(varSimpleField)){
                                varSimpleClassTypeDataMembers = varSimpleClassTypeDataMembers.tail;
                            }
                            // name(varSimpleClassType data member) == name(varSimpleField)
                            else {
                                // check for type equality between the parameter and the argument
                                TYPE fieldType = ((TYPE_CLASS_VAR_DEC) varSimpleClassTypeDataMembers.head).t;
                                TYPE paramType = SYMBOL_TABLE.getInstance().find(funcParamsList.head.name);

                                System.out.println("-- AST_STMT_FUNC \n\t\tfieldType = " + fieldType.name);
                                System.out.println("-- AST_STMT_FUNC \n\t\tparamType = " + paramType.name);

                                if(paramType.getClass().equals(fieldType.getClass())) {
                                    argIsEqual = true;
                                    break;
                                }
                            }
                        }

                        if(!argIsEqual){
                            System.out.format(">> ERROR [line] arg class is not match param class\n");
                            throw new semanticErrorException("line");
                        }
                        else {
                            funcParamsList = funcParamsList.tail;
                            argsTypes = argsTypes.tail;
//                            if(paramsList.head == null) {
//                                System.out.println("advancing paramsList is null");
//                            }else {
//                                System.out.println("advancing paramsList.head.name = " + paramsList.head.name);
//
//                            }
//                            if(argsTypes.head == null) {
//                                System.out.println("advancing argsTypes is null");
//                            }else {
//                                System.out.println("advancing argsTypes.head.name = " + argsTypes.head.name);
//                            }
                            continue;
                        }
                    }

                    System.out.println("argsTypes.head.name = " + argsTypes.head.name);
                    System.out.println("argType.name = " + argType.name);
                    if(funcParamsList == null || funcParamsList.head == null) {
                        System.out.format(">> ERROR [line] number of args isnt match number of params\n");
                        throw new semanticErrorException("line");
                    }
                    TYPE paramType = SYMBOL_TABLE.getInstance().find(funcParamsList.head.name);
                    System.out.println("paramsList.head.name = " + funcParamsList.head.name);

                    if((!(argType instanceof TYPE_CLASS) && !paramType.getClass().equals(argType.getClass())) ||
                            ((argType instanceof TYPE_CLASS && paramType instanceof TYPE_CLASS) && !((TYPE_CLASS)paramType).isAncestor((TYPE_CLASS) argType))) {
                        System.out.format(">> ERROR [line] type of arg %s doesnt match type of param %s\n",argsTypes.head.name ,funcParamsList.head.name);
                        throw new semanticErrorException("line");
                    }
                }
                System.out.println("advancing param and arg lists");
                funcParamsList = funcParamsList.tail;
                argsTypes = argsTypes.tail;
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
            }

        }
        // empty arguments list, but non empty parameters list
        else if (funcParamsList != null) {
            System.out.format(">> ERROR [line] number of args isnt match number of params\n");
            throw new semanticErrorException("line");
        }
        return null;
    }


}
