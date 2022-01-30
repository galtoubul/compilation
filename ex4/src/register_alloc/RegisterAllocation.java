package register_alloc;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import IR.IRcommand;
import TEMP.TEMP;
import pair.Pair;
import register_alloc.ControlFlowGraph.Node;

public class RegisterAllocation {
    /**
     * Try to allocate up to `registers` registers and map them to the existing
     * temporaries appearing in `function`, without any interferences (namely, a
     * single register cannot be allocated to two interfering temporaries).
     * 
     * @param function  An IR function code.
     * @param registers The maximum amount of registers to map to the temporaries in
     *                  the function.
     * @return A map from the temporaries to registers, if such mapping exists. The
     *         registers are represented as integers in the range [0, `registers`).
     * 
     *         If the the given amound of registers cannot be allocated for the
     *         temporaries, return an empty `Optional` instance.
     */
    public static Optional<HashMap<TEMP, Integer>> allocateRegisters(List<IRcommand> code, int registers) {
        return colorInterferenceGraph(new InterferenceGraph(livenessAnalysis(ControlFlowGraph.backwardCFG(code))),
                registers);
    }

    /**
     * Run liveness analysis on a (reversed) CFG, and return the resulting
     * interference sets.
     * 
     * While the liveness analysis itself returns an interference set for every
     * command, note that the exact mapping of commands to sets is not important in
     * the registers allocation process.
     * 
     * @param graph A CFG of a function with reversed edges.
     * @return A list of sets of interfering temporaries.
     */
    private static Collection<Set<TEMP>> livenessAnalysis(ControlFlowGraph graph) {
        // Performs the analysis using chaotic iteration: each node is mapped to a value
        // (the interference set), and until we get to a fixpoint (namely, all the
        // values stays the same), we update a node's value by joining it with the
        // transformed value of the previous node.
        //
        // To be more efficient, the unfixed values are stored in a worklist. Thus, we
        // always work only on the values that have been changed, and we can easily
        // detect when we reached a fixpoint.

        Map<Node, Set<TEMP>> liveness = graph.nodes.stream()
                .collect(Collectors.toMap(node -> node, node -> new HashSet<TEMP>()));

        ArrayDeque<Pair<Node, Set<TEMP>>> worklist = new ArrayDeque<>(graph.nodes.stream()
                .map(node -> new Pair<>(node, (Set<TEMP>) new HashSet<TEMP>()))
                .collect(Collectors.toList()));

        // Ensure that every node transforms at least once (we must have that since the
        // initial value of the analysis is bottom (the empty set) (in other analyses it
        // is usually top))
        HashSet<Node> firstIteration = new HashSet<>(graph.nodes);

        // Join every node's value with its resulting transformation until a fixed point
        // is reached
        while (!worklist.isEmpty()) {
            Pair<Node, Set<TEMP>> incomming = worklist.pop();
            Node node = incomming.getKey();

            Set<TEMP> value = liveness.get(node);

            // Excluding the first value of the initial node, `incoming` is the performed on
            // `value`
            Set<TEMP> newValue = incomming.getValue();
            newValue.addAll(value);

            // Check if a fixed point has been reached for `node`, if not update the
            // liveness value and the worklist
            boolean first = firstIteration.contains(node);
            if (first || !value.equals(newValue)) {
                liveness.put(node, newValue);

                for (Node neighbor : node.neighbors) {
                    worklist.push(new Pair<>(neighbor, node.command.transform(newValue)));
                }

                if (first) {
                    firstIteration.remove(node);
                }
            }
        }

        System.out.println();
        graph.nodes.stream().forEach(node -> System.out.format("%10s %-25s %-10s\n",
                node.command.transform(liveness.get(node)).stream().map(temp -> temp.getSerialNumber())
                        .collect(Collectors.toList()),
                node.command,
                liveness.get(node).stream().map(temp -> temp.getSerialNumber()).collect(Collectors.toList())));
        System.out.println();

        return liveness.values();
    }

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

        System.out.println();
        graph.print();

        HashMap<TEMP, Integer> coloring = new HashMap<>();

        // Stack of temporaries that were removed from the interference graph
        ArrayDeque<TEMP> removedStack = new ArrayDeque<>();

        // Remove all the temporaries from the interference graph and push them to the
        // stack
        graph.stream().forEach(temp -> {
            graph.removeTemp(temp);
            removedStack.push(temp);
            // System.out.format("\nRemoved: t%d\n", temp.getSerialNumber());
            // graph.print();
            // System.out.println(removedStack.stream().map(removed -> String.format("t%d",
            // removed.getSerialNumber()))
            // .collect(Collectors.toList()));
        });

        // Reinsert the temporaries to the interference graph and color them
        // appropriately
        for (TEMP temp : removedStack) {
            graph.reinsertTemp(temp);

            // System.out.println();
            // graph.print();

            // Find the first color that does not collide with existing neighboring colors
            int freshColor = getFreshColor(graph, temp, coloring);

            if (freshColor > colors) {
                return Optional.empty();
            }
            coloring.put(temp, freshColor);
        }

        System.out.println();
        graph.stream()
                .forEach(temp -> System.out.format("%s -> %s\n", temp, coloring.get(temp)));

        return Optional.of(coloring);
    }

    private static int getFreshColor(InterferenceGraph graph, TEMP temp, HashMap<TEMP, Integer> coloring) {
        return graph
                .neighbors(temp)
                .map(neighbor -> coloring.get(neighbor))
                .sorted()
                .reduce(0, (current, next) -> current < next ? current : next + 1);
    }

}
