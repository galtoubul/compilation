package TEMP;

import java.util.NoSuchElementException;
import java.util.Iterator;

public class TEMP_LIST {
    public int length = 0;
    public TEMP head;
    public TEMP_LIST tail;

    public TEMP_LIST(TEMP head, TEMP_LIST tail) {
        this.head = head;
        this.tail = tail;
        if (head != null) {
            this.length = 1;
        }
        if (tail != null) {
            this.length += tail.length;
        }
    }

    public int length() {
        return this.length;
    }

}