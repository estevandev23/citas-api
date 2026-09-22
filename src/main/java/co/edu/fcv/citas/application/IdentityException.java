package co.edu.fcv.citas.application;

public class IdentityException extends RuntimeException {
    private final String code;
    public IdentityException(String code, String message) { super(message); this.code = code; }
    public String code() { return code; }
}
