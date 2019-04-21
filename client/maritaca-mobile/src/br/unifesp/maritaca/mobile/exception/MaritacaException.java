package br.unifesp.maritaca.mobile.exception;

public class MaritacaException extends RuntimeException {

	private static final long serialVersionUID = -3244296384447060916L;

	public MaritacaException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public MaritacaException(String message) {
		super(message);
	}
}
