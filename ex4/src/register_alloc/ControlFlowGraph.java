package register_alloc;

import java.util.HashSet;

import IR.IRcommand;
import TEMP.TEMP;

class ControlFlowGraph {

    private class Node {
        IRcommand command;

        /**
         * The output of the node in the liveness analysis
         */
        HashSet<TEMP> liveness = new HashSet<>();
    }

}
