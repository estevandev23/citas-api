package co.edu.fcv.citas.adapter.http;

import co.edu.fcv.citas.application.SchedulingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/v1/admin/catalogs") @PreAuthorize("hasRole('ADMIN')")
class AdminCatalogController {
  private final SchedulingService service; AdminCatalogController(SchedulingService service){this.service=service;}
  @GetMapping("/specialties") List<SchedulingService.Specialty> specialties(@RequestParam(defaultValue="true") boolean all){return service.specialties(all);}
  @PostMapping("/specialties") SchedulingService.Specialty specialty(@Valid @RequestBody SpecialtyRequest r){return service.saveSpecialty(r.code(),r.name(),r.durationMinutes(),r.general());}
  @PatchMapping("/specialties/{code}") void specialtyState(@PathVariable String code,@RequestParam boolean active){service.toggleSpecialty(code,active);}
  @GetMapping("/insurers") List<SchedulingService.Insurer> insurers(@RequestParam(defaultValue="true") boolean all){return service.insurers(all);}
  @PostMapping("/insurers") SchedulingService.Insurer insurer(@Valid @RequestBody InsurerRequest r){return service.saveInsurer(r.code(),r.name());}
  @PatchMapping("/insurers/{code}") void insurerState(@PathVariable String code,@RequestParam boolean active){service.toggleInsurer(code,active);}
  @GetMapping("/insurers/{code}/plans") List<SchedulingService.Plan> plans(@PathVariable String code,@RequestParam(defaultValue="true") boolean all){return service.plans(code,all);}
  @PostMapping("/insurers/{code}/plans") SchedulingService.Plan plan(@PathVariable String code,@Valid @RequestBody PlanRequest r){return service.savePlan(code,r.code(),r.name());}
  @PatchMapping("/insurers/{code}/plans/{planCode}") void planState(@PathVariable String code,@PathVariable String planCode,@RequestParam boolean active){service.togglePlan(code,planCode,active);}
  record SpecialtyRequest(@NotBlank String code,@NotBlank String name,@NotNull Integer durationMinutes,boolean general){}
  record InsurerRequest(@NotBlank String code,@NotBlank String name){}
  record PlanRequest(@NotBlank String code,@NotBlank String name){}
}
