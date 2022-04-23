package blu3.ruhamaplus.errors;

public class InvalidHwidError extends Error{
    private static final long serialVersionUID = 6969696969L;
    private String hwid;

    public InvalidHwidError(final String hwid) {
        super(hwid);
        this.setStackTrace(new StackTraceElement[0]);
        this.hwid = hwid;
    }

    @Override
    public String toString() {
        return "Invaild HWID or outdated release.";
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
