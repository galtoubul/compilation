package register_alloc;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Optional;

import TEMP.TEMP;

public class RegisterAllocation {

    /**
     * Color an interference graph with (up to) `colors` colors.
     * 
     * As required by the definition of graph coloring, two neighboring temporaries
     * in the graph must not have the same color.
     * 
     * @param graph  An interference graph of the temporaries that appeared in the
     *               original code.
     * @param colors The maximum number of colors to color the graph with.
     * @return The coloring of the graph as a map from the temporaries to the
     *         colors, if such coloring exists. The colors are integers in the range
     *         [0, `colors`).
     * 
     *         If the graph cannot be colored with this number of colors, return an
     *         empty `Optional` instance.
     */
    private static Optional<HashMap<TEMP, Integer>> colorInterferenceGraph(InterferenceGraph graph, int colors) {
        if (colors <= 0) {
            return Optional.empty();
        }

        HashMap<TEMP, Integer> coloring = new HashMap<>();

        // Stack of temporaries that were removed from the interference graph
        ArrayDeque<TEMP> removedStack = new ArrayDeque<>();

        // Remove all the temporaries from the interference graph and push them to the
        // stack
        graph.stream().forEach(temp -> {
            graph.removeTemp(temp);
            removedStack.push(temp);
        });

        // Reinsert the temporaries to the interference graph and color them
        // appropriately
        for (TEMP temp : removedStack) {
            graph.reinsertTemp(temp);

            // Find the first color that does not collide with existing neighboring colors
            int freshColor = graph
                    .neighbors(temp)
                    .map(neighbor -> coloring.get(neighbor))
                    .reduce(0, (current, next) -> current < next ? current : next + 1);

            if (freshColor > colors) {
                return Optional.empty();
            }
            coloring.put(temp, freshColor);
        }

        return Optional.of(coloring);
    }

}
