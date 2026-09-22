package co.edu.fcv.citas.application.port;

public interface PasswordService {
    String hash(String raw);
    boolean matches(String raw, String hash);
}
