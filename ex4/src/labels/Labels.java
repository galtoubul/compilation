package labels;

import java.util.HashSet;
import java.util.Set;

public class Labels {

    public static Set<String> labels = new HashSet<>();

    public static String getAvialableLabel(String label) {
        int i = 0;
        String generatedLabel = label;
        while (labels.contains(generatedLabel)) {
            generatedLabel = String.format("%s%d", label, i++);
        }
        labels.add(generatedLabel);
        return generatedLabel;
    }

}
