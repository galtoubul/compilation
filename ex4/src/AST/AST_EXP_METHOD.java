package AST;

import java.util.Optional;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TEMP.*;
import TYPES.TYPE_CLASS;
import TYPES.TYPE_FUNCTION;
import TYPES.TYPE_LIST;

public class AST_EXP_METHOD extends AST_EXP {
    public AST_VAR var;
    public String id;
    public AST_EXP_LIST argsList;

    public AST_EXP_METHOD(AST_VAR var, String id, AST_EXP_LIST pl) {
        this.var = var;
        this.id = id;
        this.argsList = pl;
    }

    private TYPE_FUNCTION getFunctionType() {
        // Search the function in the symbol table or class scopes
        TYPE_CLASS methodClass = (TYPE_CLASS) SYMBOL_TABLE.getInstance().find(var.getSimple().name);
        TYPE_CLASS classToSearch = methodClass;
        while (classToSearch != null) {
            TYPE_LIST members =(classToSearch).data_members;
            while (members != null && members.head != null) {
                if (members.head instanceof TYPE_FUNCTION && members.head.name.equals(id)) {
                    return (TYPE_FUNCTION) members.head;
                }
            }
            classToSearch = classToSearch.father.orElse(null);
        }
        System.out.format(">> ERROR [%d] method '%s' does not exist at class %s\n",
                lineNum, this.id, methodClass.name);
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
        if (argsList != null) {
            TYPE_LIST argsTypes = this.argsList.SemantMe(fatherClassId);
            checkMatchingParamsArgs(argsTypes, paramsTypes);
        }
        return funcType.returnType;
    }

    // TODO
    public TEMP IRme() { return null; }
}
