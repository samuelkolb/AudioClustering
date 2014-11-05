package util.log;

import java.util.Stack;
import java.util.function.Predicate;

/**
 * Created by samuelkolb on 19/10/14.
 *
 * @author Samuel Kolb
 */
public class Log {

	public final static Log LOG = new Log();

	private class PrintChannel extends OutputChannel {

		@Override
		public void printString(String string, boolean newLine) {
			System.out.print(string);
			if(newLine)
				System.out.println();
		}

		@Override
		public boolean willOutput() {
			return true;
		}
	}

	private class NoChannel extends OutputChannel {

		@Override
		public void printString(String string, boolean newLine) {

		}

		@Override
		public boolean willOutput() {
			return false;
		}
	}

	private class ContainerChannel extends OutputChannel {

		public final OutputContainer container;

		public ContainerChannel() {
			this(new OutputContainer());
		}

		public ContainerChannel(OutputContainer container) {
			this.container = container;
		}

		@Override
		public void printString(String string, boolean newLine) {
			container.addContent(string);
			container.addContent("\n");
		}

		@Override
		public boolean willOutput() {
			return true;
		}
	}

	//region Variables
	private Stack<LogState> states = new Stack<LogState>();

	private LogState getState() {
		return this.states.peek();
	}

	/**
	 * Saves the old state
	 * @return The log itself for chaining
	 */
	public Log saveState() {
		pushState(new LogState(getState()));
		return this;
	}

	private void pushState(LogState state) {
		this.states.push(state);
	}

	/**
	 * Returns the log to the previous state
	 * @return The log itself for chaining
	 */
	public Log revert() {
		this.states.pop();
		return this;
	}

	public OutputChannel getChannel() {
		return getState().getChannel();
	}
	//endregion

	//region Construction
	private Log() {
		pushState(new LogState(new PrintChannel()));
	}
	//endregion

	//region Public methods

	/**
	 * Logs a new line
	 * @return The log itself for chaining
	 */
	public Log newLine() {
		return printLine("");
	}

	/**
	 * Logs the given string and adds a new line
	 * @param string	The string to print
	 * @return The log itself for chaining
	 */
	public Log printLine(final String string) {
		return process(new PrintOperation() {
			@Override
			public String getString() {
				return string;
			}
		}, true);
	}

	/**
	 * Logs the given object and adds a new line
	 * @param object	The object to print
	 * @return The log itself for chaining
	 */
	public Log printLine(final Object object) {
		return process(new PrintOperation() {
			@Override
			public String getString() {
				return String.format("%s", object);
			}
		}, true);
	}

	/**
	 * Logs the given string
	 * @param string	The string to print
	 * @return The log itself for chaining
	 */
	public Log print(final String string) {
		return process(new PrintOperation() {
			@Override
			public String getString() {
				return string;
			}
		}, false);
	}

	/**
	 * Logs a given number of whitespaces
	 * @param number	The number of white spaces to print
	 * @return The log itself for chaining
	 */
	public Log printWhiteSpace(final int number) {
		return process(new CharacterPrinter(number, ' '), false);
	}

	/**
	 * Logs a title with a dashed line around it and adds a new line
	 * @param title	The title to log
	 * @return The log itself for chaining
	 */
	public Log printTitle(String title) {
		return process(new DashLinePrinter(title), true);
	}

	/**
	 * Logs the given objects by appending their string representations and adds a new line
	 * @param objects	The objects to print
	 * @return The log itself for chaining
	 */
	public Log printObjects(final Object... objects) {
		return formatObjects(PrintFormat.NOT_SEPARATED, objects);
	}

	/**
	 * Logs the given objects using the print format.
	 * A new line will be started afterwards.
	 * @param format	The printing format
	 * @param objects	The objects to print
	 * @return The log itself for chaining
	 */
	public Log formatObjects(final PrintFormat format, final Object[] objects) {
		return process(new PrintOperation() {
			@Override
			public String getString() {
				return format.print(objects);
			}
		}, true);
	}

	public Log format(final String formatString, final Object... values) {
		return process(new PrintOperation() {
			@Override
			public String getString() {
				return String.format(formatString, values);
			}
		}, false);
	}

	public Log formatLine(final String formatString, final Object... values) {
		return process(new PrintOperation() {
			@Override
			public String getString() {
				return String.format(formatString, values);
			}
		}, true);
	}

	/**
	 * Turns the log on
	 * @return	The log itself for chaining
	 */
	public Log on() {
		getState().setChannel(new PrintChannel());
		return this;
	}

	/**
	 * Turns the log off
	 * @return	The log itself for chaining
	 */
	public Log off() {
		getState().setChannel(new NoChannel());
		return this;
	}

	/**
	 * Enables logging to a read-only output container
	 * @return	The container
	 */
	public OutputContainer buffer() {
		ContainerChannel containerChannel = new ContainerChannel();
		getState().setChannel(containerChannel);
		return containerChannel.container;
	}

	/**
	 * Enables logging to the given read-only output container
	 * @param container	The container to log to
	 * @return	The log itself for chaining
	 */
	public Log buffer(OutputContainer container) {
		ContainerChannel containerChannel = new ContainerChannel(container);
		getState().setChannel(containerChannel);
		return this;
	}

	/**
	 * Adds a new call filter
	 * @param callFilter	The new call filter
	 * @return	The log itself for chaining
	 */
	public Log addCallFilter(Predicate<StackTraceElement> callFilter) {
		if(getState().getCallerPredicate() == null)
			getState().setCallerPredicate(callFilter);
		else
			getState().setCallerPredicate(getState().getCallerPredicate().and(callFilter));
		return this;
	}

	/**
	 * Adds a new message filter
	 * @param messageFilter	The new message filter
	 * @return	The log itself for chaining
	 */
	public Log addMessageFilter(Predicate<LogMessage> messageFilter) {
		if(getState().getMessagePredicate() == null)
			getState().setMessagePredicate(messageFilter);
		else
			getState().setMessagePredicate(getState().getMessagePredicate().and(messageFilter));
		return this;
	}

	/**
	 * Adds a new transformer
	 * @param transformer	The new transformer
	 * @return	The log itself for chaining
	 */
	public Log addTransformer(StringTransformer transformer) {
		getState().addTransformers(transformer);
		return this;
	}
	//endregion

	private Log process(PrintOperation printOperation, boolean newLine) {
		if(getChannel().willOutput()) {
			StackTraceElement stackTraceElement = findCaller();
			if(getState().test(stackTraceElement)) {
				String string = printOperation.getString();
				LogMessage message = new LogMessage(string, stackTraceElement);
				if(getState().test(message))
					getChannel().printString(getState().transform(message), newLine);
			}
		}
		return this;
	}

	private StackTraceElement findCaller() {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		for(StackTraceElement element : elements)
			if(!"java.lang.Thread".equals(element.getClassName()) && !element.getClassName().startsWith("util.log.Log"))
				return element;
		throw new IllegalStateException();
	}
}
