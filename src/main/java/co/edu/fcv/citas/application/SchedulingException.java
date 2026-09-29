package co.edu.fcv.citas.application;

public class SchedulingException extends RuntimeException {
    private final String code;
    public SchedulingException(String code, String message) { super(message); this.code = code; }
    public String code() { return code; }
}
