package blu3.ruhamaplus.errors;

public class NetworkError extends Error{

        private static final long serialVersionUID = 69696969420L;

        public NetworkError() {
            super("Error Connecting To Hwid Server");
            this.setStackTrace(new StackTraceElement[0]);
        }

        @Override
        public String toString() {
            return "Error Connecting To Hwid Server";
        }

        @Override
        public synchronized Throwable fillInStackTrace() {
            return this;
        }


}
