package co.edu.fcv.citas.adapter.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
class LocalRecoveryTokenExposure {
    private final Environment environment;
    private final boolean enabled;

    LocalRecoveryTokenExposure(Environment environment,
                               @Value("${app.password-recovery.expose-token:false}") boolean enabled) {
        this.environment = environment;
        this.enabled = enabled;
    }

    boolean enabled() { return enabled && environment.matchesProfiles("local"); }
}
