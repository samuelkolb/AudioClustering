package util.log;

import java.util.function.Predicate;

/**
 * Created by samuelkolb on 20/10/14.
 *
 * @author Samuel Kolb
 */
class LogState {

	//region Variables
	private OutputChannel channel;

	public OutputChannel getChannel() {
		return channel;
	}

	public void setChannel(OutputChannel channel) {
		this.channel = channel;
	}

	private Predicate<StackTraceElement> callerPredicate;

	public Predicate<StackTraceElement> getCallerPredicate() {
		return callerPredicate;
	}

	public void setCallerPredicate(Predicate<StackTraceElement> callerPredicate) {
		this.callerPredicate = callerPredicate;
	}

	private Predicate<LogMessage> messagePredicate;

	public Predicate<LogMessage> getMessagePredicate() {
		return messagePredicate;
	}

	public void setMessagePredicate(Predicate<LogMessage> messagePredicate) {
		this.messagePredicate = messagePredicate;
	}

	private StringTransformer[] transformers;

	public void setTransformers(StringTransformer[] transformers) {
		this.transformers = transformers;
	}

	public void addTransformers(StringTransformer... transformers) {
		StringTransformer[] newTransformers = new StringTransformer[this.transformers.length + transformers.length];
		System.arraycopy(this.transformers, 0, newTransformers, 0, this.transformers.length);
		System.arraycopy(transformers, 0, newTransformers, this.transformers.length, transformers.length);
		setTransformers(newTransformers);
	}

	//endregion

	//region Construction
	public LogState(OutputChannel channel) {
		this(channel, null, null, new StringTransformer[0]);
	}

	public LogState(OutputChannel channel, Predicate<StackTraceElement> callerPredicate) {
		this(channel, callerPredicate, null, new StringTransformer[0]);
	}

	public LogState(OutputChannel channel, StringTransformer[] transformers) {
		this(channel, null, null, transformers);
	}

	public LogState(OutputChannel channel, Predicate<StackTraceElement> callerPredicate,
					Predicate<LogMessage> messagePredicate, StringTransformer[] transformers) {
		this.channel = channel;
		this.callerPredicate = callerPredicate;
		this.messagePredicate = messagePredicate;
		this.transformers = transformers;
	}

	//endregion

	//region Public methods
	public boolean test(StackTraceElement stackTraceElement) {
		return callerPredicate == null || callerPredicate.test(stackTraceElement);
	}

	public boolean test(LogMessage message) {
		return messagePredicate == null || messagePredicate.test(message);
	}

	public String transform(LogMessage message) {
		String string = message.MESSAGE;
		for(StringTransformer transformer : transformers)
			string = transformer.transform(message.STACK_TRACE_ELEMENT, string);
		return string;
	}
	//endregion
}
