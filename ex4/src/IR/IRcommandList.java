/***********/
/* PACKAGE */
/***********/
package IR;

import java.util.Iterator;
import java.util.NoSuchElementException;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

public class IRcommandList implements Iterable<IRcommand> {

	public IRcommand head;
	public IRcommandList tail;

	IRcommandList(IRcommand head, IRcommandList tail) {
		this.head = head;
		this.tail = tail;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
		System.out.println("-- IRcommandList MIPSme");
		if (head != null) {
			head.MIPSme();
		}
		if (tail != null) {
			tail.MIPSme();
		}
	}

	private class IRListIterator implements Iterator<IRcommand> {
		private IRcommandList list;

		private IRListIterator(IRcommandList list) {
			this.list = list;
		}

		@Override
		public boolean hasNext() {
			return this.list != null && this.list.head != null;
		}

		@Override
		public IRcommand next() {
			if (!this.hasNext()) {
				throw new NoSuchElementException();
			}
			IRcommand temp = this.list.head;
			this.list = this.list.tail;
			return temp;
		}
	}

	@Override
	public Iterator<IRcommand> iterator() {
		return new IRListIterator(this);
	}
}
