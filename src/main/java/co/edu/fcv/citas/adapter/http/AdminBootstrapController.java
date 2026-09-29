package co.edu.fcv.citas.adapter.http;
import co.edu.fcv.citas.application.SchedulingException;
import co.edu.fcv.citas.application.SchedulingService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/auth") class AdminBootstrapController {
 private final SchedulingService service; private final String configuredToken;
 AdminBootstrapController(SchedulingService service,@Value("${app.admin.bootstrap-token:}") String token){this.service=service;this.configuredToken=token;}
 @PostMapping("/bootstrap-admin") ResponseEntity<Void> bootstrap(@RequestHeader(value="X-Bootstrap-Token",required=false) String token,@Valid @RequestBody Request request){if(configuredToken.isBlank()||token==null||!configuredToken.equals(token))throw new SchedulingException("INVALID_BOOTSTRAP_TOKEN","Token de bootstrap inválido");service.bootstrapAdmin(request.email());return ResponseEntity.noContent().build();}
 record Request(@Email @NotBlank String email){}
}
