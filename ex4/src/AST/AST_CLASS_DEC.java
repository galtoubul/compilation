package AST;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import IR.IR;
import IR.IRcommand_ClassDec;
import SYMBOL_TABLE.SYMBOL_TABLE;
import SYMBOL_TABLE.ScopeType;
import TYPES.TYPE;
import TYPES.TYPE_CLASS;
import pair.Pair;

public class AST_CLASS_DEC extends AST_Node {
    public String id;
    Optional<String> father;
    public AST_CFIELD_LIST fields;
    public ArrayList<Pair<String, Optional<Object>>> initialValues; // Very disgusting

    private ArrayList<Pair<String, String>> vtable = new ArrayList<>();

    HashSet<String> vtableMethods;

    public AST_CLASS_DEC(String id, Optional<String> father, AST_CFIELD_LIST fields) {
        this.id = id;
        this.father = father;
        this.fields = fields;
    }

    public void PrintMe() {
        System.out.format("CLASS DEC = %s %s\n", id, father.isPresent() ? "EXTENDS " + father : "");
        if (fields != null) {
            fields.PrintMe();
        }

        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("CLASS\n%s", id));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, fields.SerialNumber);
    }

    private int getFieldsNum() {
        int fieldsNum = 0;
        AST_CFIELD_LIST ptr1 = this.fields;
        while (ptr1 != null && ptr1.head != null) {
            if (ptr1.head instanceof AST.AST_CFIELD_VAR_DEC) {
                fieldsNum++;
            }
            ptr1 = ptr1.tail;
        }
        return fieldsNum;
    }

    public TYPE SemantMe() {
        System.out.println("-- AST_CLASS_DEC SemantMe");
        Optional<TYPE_CLASS> base = Optional.empty();
        // Validating base class
        if (father.isPresent()) {
            // TYPE fatherType = SYMBOL_TABLE.getInstance().find(this.father.get());
            TYPE fatherType = (new AST_TYPE_WITH_VALUE(this.father.get())).getTYPE(lineNum);

            // Didn't find father in the lookup
            if (fatherType == null) {
                System.out.format(">> ERROR [%d] no class named '%s'", lineNum, father.get());
                throw new SemanticErrorException(String.valueOf(lineNum));
            }

            if (!fatherType.isClass()) {
                System.out.format(">> ERROR [$d] '%s' is not a class\n", lineNum, father.get());
                throw new SemanticErrorException(String.valueOf(lineNum));
            }

            base = Optional.of((TYPE_CLASS) fatherType);
        }

        // Begin Class Scope
        SYMBOL_TABLE.getInstance().beginScope(ScopeType.Class, id);

        // Enter the Class Type to the Symbol Table (for semantic checking inside the
        // class's scope)
        int fieldsNum = getFieldsNum();

        TYPE_CLASS type = new TYPE_CLASS(base, id, new ArrayList<>(), fieldsNum);

        SYMBOL_TABLE.getInstance().enter(id, type, true);
        System.out.println("\t\tline number = " + lineNum);

        // Semant Data Members
        fields.SemantMe(base.map(classType -> classType.name));
        // End Scope
        SYMBOL_TABLE.getInstance().endScope();

        // Reenter the Class Type to the Symbol Table
        SYMBOL_TABLE.getInstance().enter(id, type, true);

        // Set AST annotation - the vtable
        this.vtable = type.vtable;

        // Return value is irrelevant for class declarations
        return null;
    }

    // TODO
    public void IRme() {
        AST_CFIELD_LIST ptr1 = this.fields;
        while (ptr1 != null && ptr1.head != null) {
            ptr1.head.IRme();
            ptr1 = ptr1.tail;
        }
        IR.getInstance().Add_IRcommand(new IRcommand_ClassDec(new ArrayList<>(this.vtable), id));
    }
}
