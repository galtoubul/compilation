package AST;

import java.util.Optional;

import SYMBOL_TABLE.SYMBOL_TABLE;
import SYMBOL_TABLE.ScopeType;
import TYPES.TYPE;
import TYPES.TYPE_CLASS;

public class AST_CLASS_EXTENDS_DECLER extends AST_CLASS_DEC {
    public String id_extends;

    public AST_CLASS_EXTENDS_DECLER(String id, String id_extends, AST_CFIELD_LIST fields) {
        super(id, fields);
        this.id_extends = id_extends;
    }

    public void PrintMe() {
        System.out.format("CLASS DECLER = %s EXTENDS %s\n", id, id_extends);
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

    @Override
    public TYPE SemantMe() {
        System.out.println("-- AST_CLASS_EXTENDS_DECLER SemantMe");

        TYPE_CLASS fatherType;
        try {
            fatherType = (TYPE_CLASS) SYMBOL_TABLE.getInstance().find(this.id_extends);
        } catch (ClassCastException exc) {
            System.out.format(">> ERROR [line] ClassCastException\n");
            throw new semanticErrorException("line");
        }

        // Didn't find father in the lookup
        if (fatherType == null) {
            System.out.format(">> ERROR [line] derived class doesn't exist");
            throw new semanticErrorException("line");
        }

        // Begin Class's Scope
        SYMBOL_TABLE.getInstance().beginScope(ScopeType.Class, id);
        System.out.println("-- AST_CLASS_EXTENDS_DECLER\n\t\tBeginning of a new scope for class " + this.id);

        // Semant class's Data Members
        TYPE_CLASS t = new TYPE_CLASS(fatherType, id, fields.SemantMe(Optional.of(fatherType.name)));

        // End Class's Scope
        SYMBOL_TABLE.getInstance().endScope();
        System.out.println("-- AST_CLASS_EXTENDS_DECLER\n\t\tEnding of a new scope for class " + this.id);

        // Enter the Class Type to the Symbol Table
        SYMBOL_TABLE.getInstance().enter(id, t);
        System.out.format("-- AST_CLASS_EXTENDS_DECLER\n\t\tinserted class %s of type %s to the symbol table\n", id,
                t.name);

        // Return value is irrelevant for class declarations
        return null;
    }
}
