package blu3.ruhamaplus.errors;

public class BlacklistedHwidError extends Error{
    private static final long serialVersionUID = 6969696969L;
    private String hwid;

    public BlacklistedHwidError(final String hwid) {
        super(hwid);
        this.setStackTrace(new StackTraceElement[0]);
        this.hwid = hwid;
    }

    @Override
    public String toString() {
        return "Blacklisted HWID. Now you're fucked :)";
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
