package user.theovercaste.redprotect;

public class StartupException extends RuntimeException {
	private static final long serialVersionUID = 8630115432743132912L;

	public StartupException(Throwable t) {
		super(t);
	}

	public StartupException(String message) {
		super(message);
	}

	public StartupException(String message, Throwable t) {
		super(message, t);
	}
}

/*
 * Location: E:\Downloads\redProtect_1.9.4.jar Qualified Name:
 * me.ikillforeyou.redprotect.StartupException JD-Core Version: 0.6.2
 */