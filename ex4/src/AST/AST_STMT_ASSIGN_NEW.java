package AST;

import java.util.ArrayList;
import java.util.Optional;

import TYPES.TYPE;
import TYPES.TYPE_ARRAY;
import TYPES.TYPE_CLASS;
import ast_annotation.AstAnnotation;
import ast_notation_type.AstNotationType;
import global_variables.GlobalVariables;
import pair.Pair;
import TEMP.*;
import IR.*;
import SYMBOL_TABLE.SYMBOL_TABLE;
import SYMBOL_TABLE.ScopeEntry;
import SYMBOL_TABLE.ScopeType;
import SYMBOL_TABLE.SymbolTableEntry;

public class AST_STMT_ASSIGN_NEW extends AST_STMT {
    public AST_VAR var;
    public AST_NEW_EXP exp;
    public AstAnnotation astAnnotation;
    public String varName;

    public AST_STMT_ASSIGN_NEW(AST_VAR var, AST_NEW_EXP exp) {
        this.var = var;
        this.exp = exp;
    }

    @Override
    public TYPE SemantMe(Optional<String> classId, int localVarIndex) {
        System.out.format("-- AST_STMT_ASSIGN_NEW SemantMe");
        TYPE t1 = null;
        TYPE t2 = null;

        // Retrieve children types
        if (var != null) {
            t1 = var.SemantMe(classId);
        }
        if (exp != null) {
            t2 = exp.SemantMe(classId);
        }

        // Compate the two types
        // TODO apply polymorphism
        if (!exp.subscript.isPresent() && TYPE.isSubtype(t2, t1)) {
            System.out.format(">> ERROR [%d:%d] type mismatch for var := exp\n", 6, 6);
        }

        // Validate arrays
        if (exp.subscript.isPresent() &&
                (!t1.isArray() || (t1.isArray() && !TYPE.isSubtype(t2, ((TYPE_ARRAY) t1).type)))) {
            System.out.format(">> ERROR [%d:%d] type mismatch: type %s is not an array of %s\n",
                    2, 2, t1.name, t2.name);
            throw new SemanticErrorException("" + lineNum);
        }

        AST_VAR_SIMPLE varSimple;
        if (var instanceof AST.AST_VAR_SIMPLE) {
            varSimple = ((AST.AST_VAR_SIMPLE) var);
        } else if (var instanceof AST.AST_VAR_SUBSCRIPT) {
            varSimple = ((AST.AST_VAR_SUBSCRIPT) var).var.getSimple();
        } else { // instanceof AST.AST_VAR_FIELD
            varSimple = ((AST.AST_VAR_FIELD) var).var.getSimple();
        }

        Optional<SymbolTableEntry> entry = SYMBOL_TABLE.getInstance().findEntry(varSimple.name);
        varName = varSimple.name;
        setNotation(Optional.of(entry.get().position));

        return null;
    }

    private void setNotation(Optional<Integer> offset) {
        System.out.println("-- AST_STMT_ASSIGN setNotation");
        ScopeType scopeType = SYMBOL_TABLE.getInstance().getScopeTypeByEntryName(varName);
        AstNotationType astNotationType = SYMBOL_TABLE.getInstance().findEntry(varName).get().astNotationType;

        System.out.println("\t\tvariable scope type = " + scopeType);

        switch (scopeType) {
            case Global:
                astAnnotation = new AstAnnotation(AstAnnotation.TYPE.GLOBAL_VAR, Optional.empty());
                System.out.format("\t\t%s is a global variable\n", varName);
                break;
            case Class:
                ScopeEntry entry = SYMBOL_TABLE.getInstance().findScopeType(scopeType).get();
                this.setFieldNotation((TYPE_CLASS) SYMBOL_TABLE.getInstance().find(entry.scopeName.get()));
                break;
            default:
                if (astNotationType == AstNotationType.parameter) {
                    astAnnotation = new AstAnnotation(AstAnnotation.TYPE.PARAMETER, offset);
                    System.out.format("\t\t%s is a parameter | its index = %s\n", varName, offset);
                } else {
                    // local
                    astAnnotation = new AstAnnotation(AstAnnotation.TYPE.LOCAL_VAR, offset);
                    System.out.format("\t\t%s is a local variable | its index = %s\n", varName, offset);
                }
                break;
        }
    }

    private void setFieldNotation(TYPE_CLASS classToSearch) {
        int ind = 0;
        ArrayList<Pair<String, Optional<Object>>> fields = classToSearch.initialValues;
        boolean filedFound = false;
        while (classToSearch != null) {
            for (Pair<String, Optional<Object>> initializations : fields) {
                if (!initializations.getKey().equals(this.varName)) {
                    ind++;
                } else {
                    filedFound = true;
                    break;
                }
            }
            if (filedFound) {
                break;
            }
            classToSearch = classToSearch.father.orElse(null);
            if (classToSearch != null)
                fields = classToSearch.initialValues;
        }
        astAnnotation = new AstAnnotation(AstAnnotation.TYPE.FIELD,
                Optional.of(ind));
    }

    @Override
    public void IRme() {
        System.out.println("-- AST_STMT_ASSIGN_NEW IRme");

        TEMP rValueTmp;

        int localVarInd = astAnnotation.ind.orElse(-1);
        if (var instanceof AST.AST_VAR_SUBSCRIPT) {
            TEMP arrayTmp = ((AST_VAR_SUBSCRIPT) var).var.IRme();
            AST_EXP subscript = ((AST_VAR_SUBSCRIPT) var).subscript;
            TEMP offsetTmp = subscript.IRme();
            rValueTmp = exp.IRme();
            IR.getInstance()
                    .Add_IRcommand(new IRcommand_Assign_To_Local_Array_Element(arrayTmp, offsetTmp, rValueTmp));
        } else if (var instanceof AST.AST_VAR_FIELD) {
            TEMP varOfTmp = ((AST_VAR_FIELD) var).var.IRme();
            int fieldInd = ((AST_VAR_FIELD) var).astAnnotation.ind.orElse(-1);
            rValueTmp = exp.IRme();
            IR.getInstance().Add_IRcommand(new IRcommand_Assign_To_Field(varOfTmp, fieldInd, rValueTmp));

        } else {
            // instanceof AST.AST_VAR_SIMPLE

            switch (astAnnotation.type) {
                case GLOBAL_VAR:
                    System.out.format("\t\t%s is a global variable\n", varName);
                    String globalVarLabel = GlobalVariables.getGlobalVarLabel(varName);
                    rValueTmp = exp.IRme();
                    IR.getInstance().Add_IRcommand(new IRcommand_Assign_To_Global_Var(globalVarLabel, rValueTmp));
                    break;
                case PARAMETER:
                    System.out.format("\t\t%s is a parameter\n", varName);
                    rValueTmp = exp.IRme();
                    IR.getInstance().Add_IRcommand(new IRcommand_Assign_To_Parameter(localVarInd, rValueTmp));
                    break;
                case FIELD:
                    System.out.format("\t\t%s is a class field\n", varName);

                    // The object is the first parameter of the method
                    TEMP objectTemp = TEMP_FACTORY.getInstance().getFreshTEMP();
                    IR.getInstance().Add_IRcommand(new IRcommand_Initialize_Tmp_With_Parameter(objectTemp, 1));
                    rValueTmp = exp.IRme();
                    IR.getInstance().Add_IRcommand(
                            new IRcommand_Assign_To_Field(objectTemp, astAnnotation.ind.get(), rValueTmp));
                    break;
                case LOCAL_VAR:
                    System.out.format("\t\t%s is a local variable\n", varName);
                    rValueTmp = exp.IRme();
                    IR.getInstance().Add_IRcommand(new IRcommand_Assign_To_Local_Var(localVarInd, rValueTmp));
                    break;
            }

        }

    }
}
