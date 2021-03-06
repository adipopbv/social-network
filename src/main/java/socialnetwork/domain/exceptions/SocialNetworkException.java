package socialnetwork.domain.exceptions;

public class SocialNetworkException extends RuntimeException {
    public SocialNetworkException() {
    }

    public SocialNetworkException(String message) {
        super(message);
    }

    public SocialNetworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public SocialNetworkException(Throwable cause) {
        super(cause);
    }

    public SocialNetworkException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
