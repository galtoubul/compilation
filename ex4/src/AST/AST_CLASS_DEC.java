package AST;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import IR.IR;
import IR.IRcommand_ClassDec;
import SYMBOL_TABLE.SYMBOL_TABLE;
import SYMBOL_TABLE.ScopeType;
import TYPES.TYPE;
import TYPES.TYPE_CLASS;
import TYPES.TYPE_FUNCTION;
import TYPES.TYPE_LIST;
import pair.Pair;
import TEMP.*;

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

    private boolean isOverridesFunc(String funcName) {
        AST_CFIELD_LIST ptr1 = this.fields;

        while (ptr1 != null && ptr1.head != null) {
            if (ptr1.head instanceof AST.AST_CFIELD_FUNC_DEC && funcName.equals(((AST_CFIELD_FUNC_DEC) ptr1.head).func.id)) {
                return true;
            }
            ptr1 = ptr1.tail;
        }
        return false;
    }

    private void createVtable() {
        this.vtableMethods = new HashSet<>();
        if (father.isPresent()) {
            TYPE_CLASS fatherClass = (TYPE_CLASS) SYMBOL_TABLE.getInstance().find(this.father.get());
            this.vtable = new ArrayList<>(fatherClass.vtable);
            for (int i = 0; i<this.vtable.size(); i++) {
                if (this.isOverridesFunc(this.vtable.get(i).getValue())) {
                    vtableMethods.add(this.vtable.get(i).getValue());
                    this.vtable.set(i, new Pair<>(id, this.vtable.get(i).getValue()));
                }
            }
        }
        AST_CFIELD_LIST ptr1 = this.fields;

        while (ptr1 != null && ptr1.head != null) {
            if (ptr1.head instanceof AST.AST_CFIELD_FUNC_DEC && !vtableMethods.contains(((AST_CFIELD_FUNC_DEC) ptr1.head).func.id)) {
                this.vtable.add(new Pair<>(id, ((AST_CFIELD_FUNC_DEC) ptr1.head).func.id));
            }
            ptr1 = ptr1.tail;
        }
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

        TYPE_CLASS scopeDummy = new TYPE_CLASS(base, id, new TYPE_LIST(null, null), fieldsNum);
        createVtable();

        SYMBOL_TABLE.getInstance().enter(id, scopeDummy, true);
        System.out.println("\t\tline number = " + lineNum);

        // Semant Data Members
        TYPE_CLASS type = new TYPE_CLASS(base, id, fields.SemantMe(base.map(classType -> classType.name)), fieldsNum,
                scopeDummy.initialValues, this.vtable);
        // End Scope
        SYMBOL_TABLE.getInstance().endScope();

        // Reenter the Class Type to the Symbol Table
        SYMBOL_TABLE.getInstance().enter(id, type, true);

        type.methodOffsets = IntStream
                .range(0, this.vtable.size())
                .boxed()
                .collect(Collectors.toMap(index -> this.vtable.get(index).getValue(), index -> index));

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
