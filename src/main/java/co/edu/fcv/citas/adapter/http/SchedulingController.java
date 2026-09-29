package co.edu.fcv.citas.adapter.http;

import co.edu.fcv.citas.application.SchedulingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/v1")
class SchedulingController {
    private final SchedulingService service;
    SchedulingController(SchedulingService service) { this.service = service; }
    private static long id(JwtAuthenticationToken a) { return Long.parseLong(a.getToken().getSubject()); }
    private static LocalDate date(String value) { return value == null ? LocalDate.now().plusDays(1) : LocalDate.parse(value); }

    @GetMapping("/specialties") List<SchedulingService.Specialty> specialties(@RequestParam(defaultValue="false") boolean all) { return service.specialties(all); }
    @GetMapping("/professionals") List<SchedulingService.Professional> professionals(@RequestParam(defaultValue="false") boolean all) { return service.professionals(all); }

    @PostMapping("/admin/professionals") @PreAuthorize("hasRole('ADMIN')")
    SchedulingService.Professional createProfessional(@Valid @RequestBody ProfessionalRequest r) { return service.createProfessional(r.userId(),r.professionalCode(),r.licenseNumber(),r.specialtyCodes(),r.facilityCodes()); }
    @PatchMapping("/admin/professionals/{id}") @PreAuthorize("hasRole('ADMIN')")
    void toggleProfessional(@PathVariable long id,@RequestParam boolean active) { service.toggleProfessional(id,active); }

    @PostMapping("/professional/availability") @PreAuthorize("hasRole('PROFESSIONAL')")
    SchedulingService.Block createBlock(JwtAuthenticationToken a,@Valid @RequestBody BlockRequest r) { return service.createBlock(id(a),r.facilityCode(),r.date(),r.startTime(),r.endTime()); }
    @PatchMapping("/professional/availability/{blockId}") @PreAuthorize("hasRole('PROFESSIONAL')")
    SchedulingService.Block updateBlock(JwtAuthenticationToken a,@PathVariable long blockId,@Valid @RequestBody BlockRequest r) { return service.updateBlock(id(a),blockId,r.facilityCode(),r.date(),r.startTime(),r.endTime()); }
    @GetMapping("/professional/availability") @PreAuthorize("hasRole('PROFESSIONAL')")
    List<SchedulingService.Block> blocks(JwtAuthenticationToken a,@RequestParam(required=false) String from,@RequestParam(required=false) String to) { return service.blocks(id(a),date(from),to==null?date(from).plusDays(30):LocalDate.parse(to)); }
    @DeleteMapping("/professional/availability/{blockId}") @PreAuthorize("hasRole('PROFESSIONAL')")
    void deleteBlock(JwtAuthenticationToken a,@PathVariable long blockId) { service.deleteBlock(id(a),blockId); }

    @GetMapping("/availability") List<SchedulingService.Slot> availability(@RequestParam String facilityCode,@RequestParam String specialtyCode,@RequestParam(required=false) String professionalCode,@RequestParam String date) { return service.availability(facilityCode,specialtyCode,professionalCode,LocalDate.parse(date)); }
    @PostMapping("/appointments") @PreAuthorize("hasRole('USER')")
    SchedulingService.Appointment book(JwtAuthenticationToken a,@Valid @RequestBody BookRequest r) { return service.book(id(a),r.facilityCode(),r.specialtyCode(),r.professionalId(),r.startAt()); }
    @GetMapping("/appointments/me") @PreAuthorize("hasRole('USER')")
    List<SchedulingService.Appointment> mine(JwtAuthenticationToken a,@RequestParam(required=false) String status) { return service.mine(id(a),status); }
    @PostMapping("/appointments/{id}/cancel") @PreAuthorize("hasRole('USER')") void cancel(JwtAuthenticationToken a,@PathVariable long id) { service.cancel(id(a),id); }
    @GetMapping("/appointments/{id}/history") @PreAuthorize("hasAnyRole('USER','PROFESSIONAL','ADMIN')") List<SchedulingService.StatusHistory> history(@PathVariable long id) { return service.history(id); }
    @GetMapping("/professional/appointments") @PreAuthorize("hasRole('PROFESSIONAL')")
    List<SchedulingService.Appointment> professionalAppointments(JwtAuthenticationToken a,@RequestParam String date,@RequestParam(required=false) String facilityCode) { return service.professionalAppointments(id(a),LocalDate.parse(date),facilityCode); }
    @PostMapping("/professional/appointments/{id}/close") @PreAuthorize("hasRole('PROFESSIONAL')") void closeAppointment(JwtAuthenticationToken a,@PathVariable long id,@RequestParam String status) { service.close(id(a),id,status); }

    @GetMapping("/admin/appointments/requests") @PreAuthorize("hasRole('ADMIN')") List<SchedulingService.Appointment> requests() { return service.requested(); }
    @PostMapping("/admin/appointments/{id}/decision") @PreAuthorize("hasRole('ADMIN')") void decide(JwtAuthenticationToken a,@PathVariable long id,@RequestParam boolean approve,@RequestParam(required=false) String reason) { service.decide(id(a),id,approve,reason); }
    @PostMapping("/appointments/{id}/reschedules") @PreAuthorize("hasRole('USER')") SchedulingService.Reschedule reschedule(JwtAuthenticationToken a,@PathVariable long id,@Valid @RequestBody RescheduleRequest r) { return service.requestReschedule(id(a),id,r.facilityCode(),r.startAt()); }
    @GetMapping("/admin/reschedules/pending") @PreAuthorize("hasRole('ADMIN')") List<SchedulingService.Reschedule> pending() { return service.pendingReschedules(); }
    @PostMapping("/admin/reschedules/{id}/decision") @PreAuthorize("hasRole('ADMIN')") void decideReschedule(JwtAuthenticationToken a,@PathVariable long id,@RequestParam boolean approve,@RequestParam(required=false) String reason) { service.decideReschedule(id(a),id,approve,reason); }

    record ProfessionalRequest(@NotNull Long userId,@NotBlank String professionalCode,@NotBlank String licenseNumber,@NotNull List<String> specialtyCodes,@NotNull List<String> facilityCodes) {}
    record BlockRequest(@NotBlank String facilityCode,@NotNull LocalDate date,@NotNull LocalTime startTime,@NotNull LocalTime endTime) {}
    record BookRequest(@NotBlank String facilityCode,@NotBlank String specialtyCode,@NotNull Long professionalId,@NotNull LocalDateTime startAt) {}
    record RescheduleRequest(@NotBlank String facilityCode,@NotNull LocalDateTime startAt) {}
}
