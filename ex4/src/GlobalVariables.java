package GlobalVariables;

import java.util.HashMap;
import javafx.util.Pair;
import AstNotationType.*;
import java.util.*;
import Labels.*;

public class GlobalVariables {

    public static HashMap<String, Pair<String, String>> globalVarNameToLabelTypePair = new HashMap<>();
    public static HashMap<String, Pair<String, String>> globalVarLabelToStringConstLabelTypePair = new HashMap<>();

    public static String getGlobalLabel(String globalVarName) {
        return Labels.getAvialableLabel(String.format("global_%s", globalVarName));
    }

    public static String getStringConstLabel() {
        return Labels.getAvialableLabel("str_const");
    }

    public static String insertGlobal(String globalVarName, String type) {
        String label = getGlobalLabel(globalVarName);
        System.out.format("-- NotationTable\n\t\t inserted %s to globalVarNameToLabelTypePair with label %s\n", globalVarName, label);
        Pair<String, String> labelTypePair = new Pair <>(label, type);
        globalVarNameToLabelTypePair.put(globalVarName, labelTypePair);
        return label;
    }

    public static boolean isGlobal(String varName) {
        return globalVarNameToLabelTypePair.containsKey(varName);
    }

    public static String getGlobalVarLabel(String globalVarName) {
        return globalVarNameToLabelTypePair.get(globalVarName).getKey();
    }

    public static String getGlobalVarType(String globalVarName) {
        return globalVarNameToLabelTypePair.get(globalVarName).getValue();
    }
}
