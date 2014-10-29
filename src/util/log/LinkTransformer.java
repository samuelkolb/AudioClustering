package util.log;

import util.StringUtil;

/**
 * Created by samuelkolb on 20/10/14.
 *
 * @author Samuel Kolb
 */
public class LinkTransformer implements StringTransformer {

	public final static int LINK_WIDTH = 50;

	//region Variables

	//endregion

	//region Construction

	//endregion

	//region Public methods

	@Override
	public String transform(StackTraceElement stackTraceElement, String string) {
		String link = createLink(stackTraceElement);
		int surplus = LINK_WIDTH - link.length();
		String spaces = StringUtil.getRepeated(' ', Math.max(surplus, 3));
		String[] lines = string.split("\n");
		for(int i = 0; i < lines.length; i++)
			lines[i] = link + spaces + lines[i];
		return StringUtil.join("\n", lines);
	}

	//endregion

	private String createLink(StackTraceElement element) {
		return "." + element.getMethodName() + "(" + element.getFileName() + ":" + element.getLineNumber() + ")";
	}

}
