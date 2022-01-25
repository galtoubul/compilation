package register_alloc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import IR.IRcommand;
import IR.IRcommandList;
import IR.IRcommand_Jump_If_Eq_To_Zero;
import IR.IRcommand_Jump_Label;
import IR.IRcommand_Label;

/**
 * The Control Flow Graph (CFG) of an IR function. This can be used for static
 * analysis.
 * The CFG can be created backwards (i.e., the edges are reversed), which is
 * needed for liveness analysis.
 */
class ControlFlowGraph {

    Collection<Node> nodes;

    private ControlFlowGraph(Collection<Node> nodes) {
        this.nodes = nodes;
    }

    /**
     * Create a reversed CFG from a given code.
     * 
     * While it is not necessary in the creation of the CFG, the code has to be of a
     * single function if it is desired to run analyses on the resulting CFG.
     * 
     * @param code The IR code to create the CFG from.
     * @return The CFG of the code, but with all the edges reversed.
     */
    static ControlFlowGraph backwardCFG(IRcommandList code) {
        // In order to construct the CFG edges in reverse, if there is a jump in the
        // code we store the source point as a neighbor of the labled destination point,
        // in a map. Otherwise, the (only) neighbor of the next command will be the
        // current command.
        //
        // When we construct a node from a command, if it is a label, then it might have
        // a neighbor that jumps to it. Also, it is always possible that the previous
        // command is a neighbor.
        //
        // Since we can't know at a specific point if a jump to a label has already been
        // seen, we have to construct the CFG in two passes on the code: one for
        // detecting all the jumps, and the other for constructing the neighbors.

        HashMap<IRcommand, Node> nodes = new HashMap<>();
        HashMap<String, Node> labelsNeighbors = new HashMap<>();

        // Construct the CFG nodes and update all the label neighbors
        for (IRcommand command : code) {
            Node node = new Node(command, new HashSet<>());
            nodes.put(command, node);

            if (command instanceof IRcommand_Jump_If_Eq_To_Zero) {
                labelsNeighbors.put(((IRcommand_Jump_If_Eq_To_Zero) command).jumpLabel(), node);
            } else if (command instanceof IRcommand_Jump_Label) {
                labelsNeighbors.put(((IRcommand_Jump_Label) command).jumpLabel(), node);
            }
        }

        // Construct the graph
        Optional<Node> previous = Optional.empty();
        for (IRcommand command : code) {
            Node node = nodes.get(command);

            // Update neighbors of current node
            if (command instanceof IRcommand_Label) {
                Optional<Node> jumpDest = Optional.ofNullable(labelsNeighbors.get(((IRcommand_Label) command).label()));
                if (jumpDest.isPresent()) {
                    node.neighbors.add(jumpDest.get());
                }
            }
            if (previous.isPresent()) {
                node.neighbors.add(previous.get());
            }

            // Set neighbors for the next node
            if (command instanceof IRcommand_Jump_Label) {
                previous = Optional.empty();
            } else {
                previous = Optional.of(node);
            }
        }

        return new ControlFlowGraph(nodes.values());
    }

    /**
     * A node in the CFG includes a command, and points to some neighbors.
     * Specifically, it can have at most two neighbors.
     */
    private static class Node {
        IRcommand command;

        Set<Node> neighbors; // Will have up to two neighbors

        Node(IRcommand command, Set<Node> neighbors) {
            this.command = command;
            this.neighbors = neighbors;
        }
    }

}
