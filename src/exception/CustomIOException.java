package exception;

/* Send this to AutoException, exception is handled in FileIO by FixHelper*/

public class CustomIOException extends AutoException {

	private static final long serialVersionUID = -1766348172930812730L;

	public CustomIOException(CustomExceptionEnum exception) {
		super(exception);
	}
	
}
