package GlobalVariables;

import java.util.HashMap;
import javafx.util.Pair;
import AstNotationType.*;
import java.util.*;

public class GlobalVariables {

    public static HashMap<String, Pair<String, String>> globalVarToLabelTypePair = new HashMap<>();
    public static Set<String> lables = new HashSet<>();


    public static String getGlobalLabel(String globalVarName) {
        String label = String.format("global_%s", globalVarName);
        int i = 0;
        while (lables.contains(label)) {
            label = String.format("%s%d",label, i++);
        }
        return label;
    }

    public static String insertGlobal(String globalVarName, String type) {
        String label = getGlobalLabel(globalVarName);
        System.out.format("-- NotationTable\n\t\t inserted %s to globalVarToLabel with label %s\n", globalVarName, label);
        Pair<String, String> labelTypePair = new Pair <>(label, type);
        globalVarToLabelTypePair.put(globalVarName, labelTypePair);
        return label;
    }

    public static boolean isGlobal(String varName) {
        return globalVarToLabelTypePair.containsKey(varName);
    }

    public static String getGlobalVarLabel(String globalVarName) {
        return globalVarToLabelTypePair.get(globalVarName).getKey();
    }

    public static String getGlobalVarType(String globalVarName) {
        return globalVarToLabelTypePair.get(globalVarName).getValue();
    }
}
