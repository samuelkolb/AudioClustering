package util.log;

/**
* Created by samuelkolb on 20/10/14.
*
* @author Samuel Kolb
 *
 * Copyright (c) Samuel Kolb. All rights reserved.
*/
abstract class OutputChannel {

	public abstract void printString(String string, boolean newLine);
	public abstract boolean willOutput();
}
