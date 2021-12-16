package AST;

import java.util.Optional;

import SYMBOL_TABLE.SYMBOL_TABLE;
import SYMBOL_TABLE.SymbolTableEntry;
import TYPES.TYPE;

public class AST_TYPE extends AST_Node {
    public String name() {
        return null;
    }

    public TYPE getTYPE(int lineNum) {
        Optional<SymbolTableEntry> entry = SYMBOL_TABLE.getInstance().findEntry(this.name());
        if (!entry.isPresent()) {
            System.out.format(">> ERROR [%d] non existing type '%s'\n", lineNum, this.name());
            throw new SemanticErrorException(String.valueOf(lineNum));
        }
        if (!entry.get().isType) {
            System.out.format(">> ERROR [%d] '%s' is not a type\n", lineNum, this.name());
            throw new SemanticErrorException(String.valueOf(lineNum));
        }

        return entry.get().type;
    }
}
