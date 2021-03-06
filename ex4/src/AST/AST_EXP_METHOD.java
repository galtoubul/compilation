package AST;

import java.util.Optional;

import IR.IR;
import IR.IRcommand_Call_Method_Exp;
import TYPES.TYPE;
import TEMP.*;
import TYPES.TYPE_CLASS;
import TYPES.TYPE_FUNCTION;
import TYPES.TYPE_LIST;
import pair.Pair;

public class AST_EXP_METHOD extends AST_EXP {
    public String id;
    public AST_EXP_LIST argsList;
    public AST_VAR var;
    public int offset;

    public AST_EXP_METHOD(AST_VAR var, String id, AST_EXP_LIST argsList) {
        this.var = var;
        this.id = id;
        if (argsList == null) {
            this.argsList = new AST_EXP_LIST(null, null);
        } else {
            this.argsList = argsList;
        }
    }

    private Pair<TYPE_CLASS, TYPE_FUNCTION> getMethodTypes(TYPE_CLASS methodClass) {
        // Search the method in the class scopes
        TYPE_CLASS classToSearch = methodClass;
        while (classToSearch != null) {
            for (TYPE member : classToSearch.data_members) {
                if (member instanceof TYPE_FUNCTION && member.name.equals(id)) {
                    return new Pair<>(classToSearch, (TYPE_FUNCTION) member);
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
        System.out.println("-- AST_EXP_METHOD SemantMe");

        TYPE_CLASS classType = (TYPE_CLASS) var.SemantMe(fatherClassId);

        System.out.println("-- AST_EXP_METHOD SemantMe\n\n\tline number = " + lineNum);
        Pair<TYPE_CLASS, TYPE_FUNCTION> types = getMethodTypes(classType);
        TYPE_FUNCTION funcType = types.getValue();
        System.out.println("-- AST_EXP_METHOD\n\t\tfuncType.name = " + funcType.name);

        TYPE_LIST paramsTypes = funcType.params;
        // checkForEmptyLists(paramsTypes);

        if (paramsTypes == null) {
            paramsTypes = new TYPE_LIST(null, null);
        }
        if (argsList != null) {
            TYPE_LIST argsTypes = this.argsList.SemantMe(fatherClassId);
            checkMatchingParamsArgs(argsTypes, paramsTypes);
        }

        this.offset = types.getKey().methodOffsets.get(this.id);
        System.out.println(types.getKey().name);
        return funcType.returnType;
    }

    public TEMP IRme() {
        System.out.println("-- AST_EXP_METHOD IRme");

        TEMP_LIST argsTempList = null;
        if (argsList != null) {
            argsTempList = argsList.IRme();
        }

        TEMP funcRetValTemp = TEMP_FACTORY.getInstance().getFreshTEMP();
        IR.getInstance()
                .Add_IRcommand(new IRcommand_Call_Method_Exp(funcRetValTemp, var.IRme(), this.offset,
                        argsTempList));

        return funcRetValTemp;
    }
}
