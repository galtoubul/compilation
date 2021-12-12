package TYPES;

public class TYPE_LIST {
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
}
