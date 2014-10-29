package util.log;

import util.StringUtil;

/**
* Created by samuelkolb on 20/10/14.
*
* @author Samuel Kolb
*/
class CharacterPrinter implements PrintOperation {

	private final int number;

	private final char character;

	public CharacterPrinter(int number, char character) {
		this.number = number;
		this.character = character;
	}

	@Override
	public String getString() {
		return StringUtil.getRepeated(this.character, this.number);
	}


}
