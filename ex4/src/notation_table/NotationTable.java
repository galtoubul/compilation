package notation_table;

import java.util.HashMap;

import ast_notation_type.AstNotationType;
import pair.Pair;

import java.util.*;

public class NotationTable {

    // Singelton
    private static NotationTable instance = null;

    public HashMap<String, Pair<AstNotationType, Integer>> funcToTypeAndIndex = new HashMap<>();
    public HashMap<String, Pair<String, String>> globalVarToLabelTypePair = new HashMap<>();
    public HashMap<String, HashMap<String, Integer>> classToFieldToInd = new HashMap<>();
    public HashMap<String, HashMap<String, Integer>> classToMethodToLabel = new HashMap<>();
    Set<String> lables = new HashSet<>();

    public static NotationTable getInstance() {
        if (instance == null) {
            instance = new NotationTable();
        }
        return instance;
    }

    public String getGlobalLabel(String globalVarName) {
        String label = String.format("global_%s", globalVarName);
        int i = 0;
        while (this.lables.contains(label)) {
            label = String.format("%s%d", label, i++);
        }
        return label;
    }

    public void insertGlobal(String globalVarName, String type) {
        String label = getGlobalLabel(globalVarName);
        System.out.format("-- NotationTable\n\t\t inserted %s to globalVarToLabel with label %s\n", globalVarName,
                label);
        Pair<String, String> labelTypePair = new Pair<>(label, type);
        globalVarToLabelTypePair.put(globalVarName, labelTypePair);
    }

    public boolean isGlobal(String varName) {
        return this.globalVarToLabelTypePair.containsKey(varName);
    }

    public String getGlobalVarLabel(String globalVarName) {
        return this.globalVarToLabelTypePair.get(globalVarName).getKey();
    }

    public String getGlobalVarType(String globalVarName) {
        return this.globalVarToLabelTypePair.get(globalVarName).getValue();
    }
}
