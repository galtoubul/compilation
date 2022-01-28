package register_alloc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import TEMP.TEMP;

/**
 * Implementation of an interference graph of temporaries in IR.
 * 
 * Used to match registers to temporaries via coloring.
 */
class InterferenceGraph {
    /**
     * The temporaries in the original code that we want to match to machine
     * registers.
     */
    private final Set<TEMP> temps;

    /**
     * The interferences of temporaries - namely, two neighboring temporaries cannot
     * match to the same register.
     */
    private final HashMap<TEMP, Set<TEMP>> edges;

    /**
     * Flags that indicate if a temporary is currently in the graph or if it was
     * deleted from the graph.
     * 
     * This is required, because during the coloring
     * algorithm nodes are removed from the graph, and later reinserted to it in a
     * way that preserves the original topology.
     */
    private HashMap<TEMP, Boolean> inGraph;

    /**
     * Construct an interference graph from the result of a liveness analysis.
     * 
     * @param liveness The list of sets of temporaries returned for each command
     *                 from the liveness analysis. Each set represents the live
     *                 temporaries after the execution of said command.
     */
    public InterferenceGraph(Collection<Set<TEMP>> liveness) {
        this.inGraph = new HashMap<>();

        // Construct the edges and initialize the `inGraph` flags
        HashMap<TEMP, Stream<TEMP>> lazyEdges = createLazyEdges(liveness);
        HashMap<TEMP, Set<TEMP>> edges = new HashMap<>();
        for (TEMP temp : lazyEdges.keySet()) {
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
    private HashMap<TEMP, Stream<TEMP>> createLazyEdges(Collection<Set<TEMP>> liveness) {
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
     * Return if a given temporary was inserted to the graph during its creation.
     * 
     * This means that the temporary can be currently in the graph, or that it was
     * deleted from the graph.
     */
    private boolean existsInGraph(TEMP temp) {
        return this.temps.contains(temp);
    }

    /**
     * Remove a temporary from a graph, such that it can be inserted later in a way
     * that preserves the original topology.
     * 
     * If the temporary is not in the graph, this does nothing.
     */
    public void removeTemp(TEMP temp) {
        this.setInGraph(temp, false);
    }

    /**
     * Reinsert a temporary that was removed from the graph, in a way that preserves
     * the original topology.
     * 
     * If the temporary was not originally in the graph, this does nothing.
     */
    public void reinsertTemp(TEMP temp) {
        this.setInGraph(temp, true);
    }

    /**
     * Set the `inGraph` flag of `temp`, if `temp` exists in the graph.
     * 
     * If the temporary does not exist in the graph, this does nothing.
     */
    private void setInGraph(TEMP temp, boolean in) {
        if (this.existsInGraph(temp)) {
            this.inGraph.put(temp, in);
        }
    }

    public Stream<TEMP> stream() {
        return this.temps.stream().filter(temp -> this.inGraph.get(temp));
    }

    /**
     * Return the (non-removed) neighbors of a temporary in the interference graph.
     * If the temporary
     * 
     * Assumes the temporary is in the graph.
     * 
     * @param temp
     * @return
     */
    public Stream<TEMP> neighbors(TEMP temp) {
        return this.edges.get(temp).stream().filter(neighbor -> this.inGraph.get(neighbor));
    }
}
