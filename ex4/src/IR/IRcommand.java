package IR;

import java.util.HashSet;
import java.util.Set;

import TEMP.TEMP;

public abstract class IRcommand {
	/*****************/
	/* Label Factory */
	/*****************/
	protected static int label_counter = 0;

	public static String getFreshLabel(String msg) {
		return String.format("Label_%d_%s", label_counter++, msg);
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public abstract void MIPSme();

	/**
	 * Transform a set of temporaries according to the given command.
	 * The transformation works backwards.
	 * 
	 * @param liveTemps The set of temporaries alive after the execution of the
	 *                  command.
	 * @return The set of temporaries alive before the execution of the command.
	 */
	public abstract HashSet<TEMP> transform(Set<TEMP> liveTemps);
}
