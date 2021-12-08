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

    @Override
    public TYPE SemantMe() {
        System.out.println("-- AST_STMT_FUNC SemantMe");

        // function name lookup
        TYPE_FUNCTION funcType = null;
        try {
            funcType = (TYPE_FUNCTION) SYMBOL_TABLE.getInstance().find(this.id);
        } catch (ClassCastException exc) {
            System.out.format(">> ERROR [line] class extends\n");
            throw new semanticErrorException("line");
        }

        // Didn't find function in the lookup
        if (funcType == null) {
            System.out.format(">> ERROR [line] function use before define\n");
            throw new semanticErrorException("line");
        } else {
            System.out.println("-- AST_STMT_FUNC funcType isnt null");
        }

        TYPE_FUNCTION typeFunction = (TYPE_FUNCTION) SYMBOL_TABLE.getInstance().find(id);
        TYPE_LIST paramsList = typeFunction.params;
        if (argsList != null) {
            TYPE_LIST argsTypes = this.argsList.SemantMe();
            // this.el.SemantMe() is a TYPE_LIST -> need to check that head isnt null
            while (argsTypes != null && argsTypes.head != null) {
                if (argsTypes.head instanceof TYPE_NONE) {
                    System.out.format(">> ERROR [line] parameter use before define\n");
                    throw new semanticErrorException("line");
                } else {
                    System.out.println("-- AST_STMT_FUNC parameter isnt null");
                    TYPE argType = SYMBOL_TABLE.getInstance().find(argsTypes.head.name);

                    if (argType == null && argsList.head instanceof AST_EXP_VAR) {
                        AST_VAR_SIMPLE v = ((AST_EXP_VAR) argsList.head).var.getSimple();
                        TYPE_CLASS classType = (TYPE_CLASS) SYMBOL_TABLE.getInstance().find(v.name);
                        System.out.println("classType.name = " + classType.name);
                        TYPE_LIST dataMembers = classType.data_members;
                        System.out.println("classType.data_members.head.name" + classType.data_members.head);
                        boolean argIsEqual = false;
                        while (dataMembers != null && dataMembers.head != null) {
                            TYPE fieldType = ((TYPE_CLASS_VAR_DEC) dataMembers.head).t;
                            System.out.println("fieldType.name = " + fieldType.name);
                            System.out.println("argsTypes.head.name = " + argsTypes.head.name);
                            System.out.println("dataMembers.head.name = " + dataMembers.head.name);

                            if(!dataMembers.head.name.equals(argsTypes.head.name)){
                                dataMembers = dataMembers.tail;
                            }else{
                                argIsEqual = true;
                                break;
                            }
                        }
                        if(!argIsEqual){
                            System.out.format(">> ERROR [line] arg class is not match param class\n");
                            throw new semanticErrorException("line");
                        }else {
                            paramsList = paramsList.tail;
                            argsTypes = argsTypes.tail;
                            if(paramsList.head == null) {
                                System.out.println("advancing paramsList is null");
                            }else {
                                System.out.println("advancing paramsList.head.name = " + paramsList.head.name);

                            }
                            if(argsTypes.head == null) {
                                System.out.println("advancing argsTypes is null");
                            }else {
                                System.out.println("advancing argsTypes.head.name = " + argsTypes.head.name);
                            }
                            continue;
                        }

                    }

                    System.out.println("argsTypes.head.name = " + argsTypes.head.name);
                    System.out.println("argType.name = " + argType.name);
                    if(paramsList == null || paramsList.head == null) {
                        System.out.format(">> ERROR [line] number of args isnt match number of params\n");
                        throw new semanticErrorException("line");
                    }
                    TYPE paramType = SYMBOL_TABLE.getInstance().find(paramsList.head.name);
                    System.out.println("paramsList.head.name = " + paramsList.head.name);

                    if((!(argType instanceof TYPE_CLASS) && !paramType.getClass().equals(argType.getClass())) ||
                            ((argType instanceof TYPE_CLASS && paramType instanceof TYPE_CLASS) && !isAncestor((TYPE_CLASS)paramType, (TYPE_CLASS) argType))) {
                        System.out.format(">> ERROR [line] type of arg %s doesnt match type of param %s\n",argsTypes.head.name ,paramsList.head.name);
                        throw new semanticErrorException("line");
                    }
                }
                System.out.println("advancing param and arg lists");
                paramsList = paramsList.tail;
                argsTypes = argsTypes.tail;
                if(paramsList.head == null) {
                    System.out.println("advancing paramsList is null");
                }else {
                    System.out.println("advancing paramsList.head.name = " + paramsList.head.name);

                }
                if(argsTypes.head == null) {
                    System.out.println("advancing argsTypes is null");
                }else {
                    System.out.println("advancing argsTypes.head.name = " + argsTypes.head.name);
                }
            }

        } else if (paramsList != null) {
            System.out.format(">> ERROR [line] number of args isnt match number of params\n");
            throw new semanticErrorException("line");
        }
        return null;
    }

    public boolean isAncestor(TYPE_CLASS paramType, TYPE_CLASS argType) {
        do  {
            if(paramType.getClass().equals(argType.getClass())) {
                return true;
            }
            argType = argType.father;
        } while ((argType.father != null));
        if(paramType.getClass().equals(argType.getClass())) {
            return true;
        }
        return false;
    }
}
