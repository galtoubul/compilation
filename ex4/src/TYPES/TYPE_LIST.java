package TYPES;

import java.util.NoSuchElementException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.Iterator;

public class TYPE_LIST implements Iterable<TYPE> {
	private int length = 0;

	public TYPE head;
	public TYPE_LIST tail;

	public TYPE_LIST(TYPE head, TYPE_LIST tail) {
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

	class TypeListIterator implements Iterator<TYPE> {
		private TYPE_LIST list;

		private TypeListIterator(TYPE_LIST list) {
			this.list = list;
		}

		@Override
		public boolean hasNext() {
			return this.list != null && this.list.head != null;
		}

		@Override
		public TYPE next() {
			if (!this.hasNext()) {
				throw new NoSuchElementException();
			}
			TYPE type = this.list.head;
			this.list = this.list.tail;
			return type;
		}
	}

	@Override
	public Iterator<TYPE> iterator() {
		return new TypeListIterator(this);
	}

	public Stream<TYPE> stream() {
		return StreamSupport.stream(this.spliterator(), true);
	}
}
