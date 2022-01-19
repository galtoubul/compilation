package register_alloc;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import TEMP.TEMP;

/**
 * Implementation of an interference graph of temporaries in IR.
 * 
 * Used to match registers to temporaries.
 */
class InterferenceGraph {
    private final Set<TEMP> temps;
    private final HashMap<TEMP, Set<TEMP>> edges;
    private HashMap<TEMP, Boolean> inGraph;

    /**
     * Construct an interference graph from the result of a liveness analysis.
     * 
     * @param liveness The list of sets of temporaries returned for each command
     *                 from the liveness analysis. Each set represents the live
     *                 temporaries after the execution of said command.
     */
    public InterferenceGraph(List<Set<TEMP>> liveness) {
        this.inGraph = new HashMap<>();

        // Construct the edges and initialize the `inGraph` flags
        HashMap<TEMP, Stream<TEMP>> lazyEdges = createLazyEdges(liveness);
        HashMap<TEMP, Set<TEMP>> edges = new HashMap<>();
        for (TEMP temp : edges.keySet()) {
            edges.put(temp, lazyEdges.get(temp)
                    .filter(neighbor -> temp.getSerialNumber() != neighbor.getSerialNumber())
                    .collect(Collectors.toSet()));
            this.inGraph.put(temp, true);
        }
        this.edges = edges;

        // Construct the nodes
        this.temps = edges.keySet();
    }

    /**
     * Lazily construct the edges of an interference graph from the result of a
     * liveness analysis.
     * 
     * @param liveness The list of sets of temporaries returned for each command
     *                 from the liveness analysis. Each set represents the live
     *                 temporaries after the execution of said command.
     * @return The edges as a map from every node (temporary) to the stream of its
     *         neighbors. The stream might contain repetitions, and it might also
     *         contain the node itself that is mapped to it. Therefore, the node
     *         itself must be removed, and the stream should be collected into a
     *         set.
     */
    private HashMap<TEMP, Stream<TEMP>> createLazyEdges(List<Set<TEMP>> liveness) {
        HashMap<TEMP, Stream<TEMP>> edges = new HashMap<>();
        for (Set<TEMP> temps : liveness) {
            for (TEMP temp : temps) {
                Optional<Stream<TEMP>> neighbors = Optional.ofNullable(edges.get(temp));
                Stream<TEMP> newNeighbors;
                if (neighbors.isPresent()) {
                    newNeighbors = Stream.concat(neighbors.get(), temps.stream());
                } else {
                    newNeighbors = temps.stream();
                }
                edges.put(temp, newNeighbors);
            }
        }

        return edges;
    }

    /**
     * Return if a given temporary is in the graph.
     * 
     * Note that the temporary can be in the graph, but (temporarily) be considered
     * "deleted". In that case, the function returns `false`.
     */
    private boolean inGraph(TEMP temp) {
        return this.temps.contains(temp) && this.inGraph.get(temp);
    }
}
