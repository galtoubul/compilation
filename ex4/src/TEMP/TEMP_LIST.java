package TEMP;

import java.util.NoSuchElementException;
import java.util.Iterator;

public class TEMP_LIST implements Iterable<TEMP> {
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

    private class TempListIterator implements Iterator<TEMP> {
        private TEMP_LIST list;

        private TempListIterator(TEMP_LIST list) {
            this.list = list;
        }

        @Override
        public boolean hasNext() {
            return this.list != null && this.list.head != null;
        }

        @Override
        public TEMP next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            TEMP temp = this.list.head;
            this.list = this.list.tail;
            return temp;
        }
    }

    @Override
    public Iterator<TEMP> iterator() {
        return new TempListIterator(this);
    }
}