package util.log;

/**
 * Created by samuelkolb on 20/10/14.
 *
 * @author Samuel Kolb
 */
public class OutputContainer {

	//region Variables
	private StringBuilder stringBuilder = new StringBuilder();

	public String getContent() {
		return this.stringBuilder.toString();
	}

	void addContent(String content) {
		stringBuilder.append(content);
	}
	//endregion

	//region Construction

	//endregion

	//region Public methods

	//endregion
}
