package se.tuxflux.gibson.api.model;

public class GibsonException extends Exception {

  public GibsonException() {
    super();
  }

  public GibsonException(String message) {
    super(message);
  }

  public GibsonException(String message, Throwable cause) {
    super(message, cause);
  }

  public GibsonException(Throwable cause) {
    super(cause);
  }

  protected GibsonException(String message, Throwable cause,
                            boolean enableSuppression,
                            boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
