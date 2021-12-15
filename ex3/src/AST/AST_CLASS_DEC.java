package AST;

import java.util.Optional;

import SYMBOL_TABLE.SYMBOL_TABLE;
import SYMBOL_TABLE.ScopeType;
import TYPES.TYPE;
import TYPES.TYPE_CLASS;
import TYPES.TYPE_LIST;

public class AST_CLASS_DEC extends AST_Node {
    public String id;
    Optional<String> father;
    public AST_CFIELD_LIST fields;

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

    public TYPE SemantMe() {
        System.out.println("-- AST_CLASS_DEC SemantMe");

        Optional<TYPE_CLASS> base = Optional.empty();

        // Validating base class
        if (father.isPresent()) {
            TYPE fatherType = SYMBOL_TABLE.getInstance().find(this.father.get());

            // Didn't find father in the lookup
            if (fatherType == null) {
                System.out.format(">> ERROR [line] no class named '%s'", father.get());
                throw new semanticErrorException("line");
            }

            if (!fatherType.isClass()) {
                System.out.format(">> ERROR [line] '%s' is not a class\n", father.get());
                throw new semanticErrorException("line");
            }

            base = Optional.of((TYPE_CLASS) fatherType);
        }

        // Begin Class Scope
        SYMBOL_TABLE.getInstance().beginScope(ScopeType.Class, id);

        // Enter the Class Type to the Symbol Table (for semantic checking inside the
        // class's scope)
        SYMBOL_TABLE.getInstance().enter(id, new TYPE_CLASS(base, id, new TYPE_LIST(null, null)));

        // Semant Data Members
        TYPE_CLASS type = new TYPE_CLASS(base, id, fields.SemantMe(base.map(classType -> classType.name)));

        // End Scope
        SYMBOL_TABLE.getInstance().endScope();

        // Reenter the Class Type to the Symbol Table
        SYMBOL_TABLE.getInstance().enter(id, type);

        // Return value is irrelevant for class declarations
        return null;
    }
}
