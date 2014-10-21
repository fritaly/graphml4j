package fr.ritaly.graphml4j;

public class GraphMLException extends Exception {

	private static final long serialVersionUID = 6783323986366115671L;

	public GraphMLException() {
	}

	public GraphMLException(String message) {
		super(message);
	}

	public GraphMLException(Throwable cause) {
		super(cause);
	}

	public GraphMLException(String message, Throwable cause) {
		super(message, cause);
	}

	public GraphMLException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}