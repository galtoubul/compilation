package NotationTable;

import java.util.HashMap;
import javafx.util.Pair;
import AstNotationType.*;

public class NotationTable {

    // Singelton
    private static NotationTable instance = null;

    public HashMap<String, Pair<AstNotationType,Integer>> funcToTypeAndIndex = new HashMap<>();
    public HashMap<String, String> globalVarToLabel = new HashMap<>();
    public HashMap<String, HashMap<String,Integer>> classToFieldToInd = new HashMap<>();
    public HashMap<String, HashMap<String,Integer>> classToMethodToLabel = new HashMap<>();

    public static NotationTable getInstance() {
        if (instance == null) {
            instance = new SYMBOL_TABLE();
        }
        return instance;
    }
}
