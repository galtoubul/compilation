package AST;

import java.util.Optional;

import SYMBOL_TABLE.*;
import TYPES.*;
import ast_annotation.AstAnnotation;
import ast_notation_type.AstNotationType;
import global_variables.GlobalVariables;
import IR.*;
import pair.Pair;

public class AST_VAR_DECLERATION extends AST_VAR_DEC {
    public String id;
    public String varType;
    public AstAnnotation astAnnotation;

    public AST_VAR_DECLERATION(AST_TYPE type, String id) {
        super(type, id);
        this.id = id;
        this.type = type;
    }

    public void PrintMe() {
        System.out.format("VAR-DEC(%s):%s                \n", id, type);

        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("VAR\nDEC(%s)\n:%s", id, type));
    }

    @Override
    public TYPE SemantMe(Optional<String> fatherClassId, Optional<Integer> localVarInd) {
        System.out.format("-- AST_VAR_DECLERATION SemantMe %s\n", fatherClassId.isPresent() ? "extends" : "");

        if (fatherClassId.isPresent()) {
            TYPE_CLASS fatherType = (TYPE_CLASS) SYMBOL_TABLE.getInstance().find(fatherClassId.get());
            System.out.println(
                    "\t\tthe variable is declared as part of the scope of class that extends class "
                            + fatherType.name);
            TYPE_LIST dataMembers = fatherType.data_members;

            // Check that the variable doesn't shadow a variable/method in a derrived class
            // TODO: continue this check to ancient ancestors
            while (dataMembers != null && dataMembers.head != null) {
                System.out.println("\t\twhile (dataMembers.head != null)");
                if (dataMembers.head instanceof TYPE_FUNCTION ||
                        dataMembers.head instanceof TYPE_VOID ||
                        dataMembers.head instanceof TYPE_CLASS_VAR_DEC) {

                    System.out.println("-- AST_FUNC_DEC SemantMe\n\t\tdataMembers.head is instance of method/field...");
                    TYPE dm = dataMembers.head;

                    if (dm.name.equals(id)) {
                        System.out.format("\t\tid = %s\n", id);
                        System.out.format("\t\tdm.name = %s\n", dm.name);
                        System.out.println(">> ERROR [line] overloading var and func names isn't allowed");
                        throw new SemanticErrorException("" + lineNum);
                    }
                }
                dataMembers = dataMembers.tail;
            }
        }

        // Check If Type exists
        TYPE t = this.type.getTYPE(lineNum);

        // Check that the type isn't void
        if (t == TYPE_VOID.getInstance()) {
            System.out.format(">> ERROR [%d:%d] cannot declare a variable as void\n", 2, 2, type.name());
            throw new SemanticErrorException("" + lineNum);
        }

        // Check that id does NOT exist at the same scope
        if (SYMBOL_TABLE.getInstance().isInScope(id)) {
            System.out.format(">> ERROR [%d:%d] variable %s already exists in scope\n",
                    2, 2, id);
            throw new SemanticErrorException("" + lineNum);
        }

        // Done with all checks -> insert the variable to the Symbol Table

        if (SYMBOL_TABLE.getInstance().currentScopeType() == ScopeType.Class) {
            TYPE_CLASS currentClass = (TYPE_CLASS) SYMBOL_TABLE.getInstance()
                    .find(SYMBOL_TABLE.getInstance().currentScope().scopeName.get());
            currentClass.initialValues.add(new Pair<String, Optional<Object>>(id, Optional.empty()));
        }

        String callerClassName = (Thread.currentThread().getStackTrace())[2].getClassName();
        System.out.println("\t\tcaller's class = " + callerClassName);

        AstNotationType astNotationType = AstNotationType.getAstNotationType(callerClassName);
        Optional<AstNotationType> astNotationTypeOpt = Optional.of(astNotationType);

        SYMBOL_TABLE.getInstance().enter(id, t, false, localVarInd, astNotationTypeOpt);

        TYPE_CLASS_VAR_DEC var = new TYPE_CLASS_VAR_DEC(t, id);
        System.out.format("\t\tinserted variable %s of type %s to the symbol table\n", var.name,
                var.type.name);
        this.varType = var.type.name;
        setNotation(localVarInd);
        return var;
    }

    private void setNotation(Optional<Integer> localVarInd) {
        System.out.println("-- AST_VAR_DECLERATION setNotation");

        ScopeType scopeType = SYMBOL_TABLE.getInstance().currentScopeType();
        if (scopeType == ScopeType.Global) {
            astAnnotation = new AstAnnotation(AstAnnotation.TYPE.GLOBAL_VAR, localVarInd);
            System.out.format("\t\t%s is a global variable\n", id);
        } else { // local
            astAnnotation = new AstAnnotation(AstAnnotation.TYPE.LOCAL_VAR, localVarInd);
            int ind = localVarInd.orElse(-1);
            System.out.format("\t\t%s is a local variable | its index = %s\n", id, ind);
        }
    }

    @Override
    public void IRme() {
        System.out.println("-- AST_VAR_DECLERATION IRme");

        if (astAnnotation.type == AstAnnotation.TYPE.GLOBAL_VAR) {
            System.out.println("\t\tglobal variable");
            String globalVarLabel = GlobalVariables.insertGlobal(this.id, this.varType);
            IR.getInstance().Add_IRcommand(new IRcommand_Create_Global_Var(globalVarLabel, varType));
        } else { // local variable
            System.out.println("\t\tlocal variable");
        }
    }

}
