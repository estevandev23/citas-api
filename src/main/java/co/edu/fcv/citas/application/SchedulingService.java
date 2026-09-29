package co.edu.fcv.citas.application;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SchedulingService {
    private final JdbcTemplate db;
    public SchedulingService(JdbcTemplate db) { this.db = db; }
    @Transactional public void bootstrapAdmin(String email){Long id=db.query("SELECT id FROM app_user WHERE email=?",(r,n)->r.getLong(1),email).stream().findFirst().orElseThrow(()->new SchedulingException("USER_NOT_FOUND","Usuario no encontrado"));db.update("INSERT IGNORE INTO user_role(user_id,role_code) VALUES(?, 'ADMIN')",id);}

    public record Specialty(String code,String name,int durationMinutes,boolean general,boolean approval,boolean active) {}
    public record Professional(long id,long userId,String code,String license,boolean active,List<String> specialties,List<String> facilities) {}
    public record Block(long id,long professionalId,String facility,LocalDate date,LocalTime start,LocalTime end) {}
    public record Slot(String facility,String specialty,String professionalCode,long professionalId,LocalDateTime start,LocalDateTime end,int durationMinutes) {}
    public record Appointment(long id,long patientId,long professionalId,String professionalCode,String facility,String specialty,LocalDateTime start,LocalDateTime end,String status,String reason) {}
    public record Reschedule(long id,long appointmentId,String facility,LocalDateTime start,LocalDateTime end,String status,String reason) {}
    public record StatusHistory(String status,String source,String reason,LocalDateTime changedAt,long actorUserId) {}

    public List<Specialty> specialties(boolean all) {
        return db.query("SELECT code,display_name,duration_minutes,is_general,requires_admin_approval,active FROM specialty "+(all?"":"WHERE active ")+"ORDER BY display_name",
                (r,n)->new Specialty(r.getString(1),r.getString(2),r.getInt(3),r.getBoolean(4),r.getBoolean(5),r.getBoolean(6)));
    }
    @Transactional public Specialty saveSpecialty(String code,String name,int duration,boolean general) { if(duration!=30&&duration!=60) throw new SchedulingException("INVALID_DURATION","La duración debe ser 30 o 60 minutos"); db.update("INSERT INTO specialty(code,display_name,duration_minutes,is_general,requires_admin_approval) VALUES(?,?,?,?,?) ON DUPLICATE KEY UPDATE display_name=VALUES(display_name),duration_minutes=VALUES(duration_minutes),is_general=VALUES(is_general)",code,name,duration,general,!general); return specialties(true).stream().filter(s->s.code().equals(code)).findFirst().orElseThrow(); }
    @Transactional public void toggleSpecialty(String code,boolean active) { if(db.update("UPDATE specialty SET active=? WHERE code=?",active,code)!=1) throw new SchedulingException("SPECIALTY_NOT_FOUND","Especialidad no encontrada"); }
    public record Insurer(String code,String name,boolean active) {}
    public record Plan(String insurerCode,String code,String name,boolean active) {}
    public List<Insurer> insurers(boolean all){return db.query("SELECT code,display_name,active FROM health_insurer "+(all?"":"WHERE active ")+"ORDER BY display_name",(r,n)->new Insurer(r.getString(1),r.getString(2),r.getBoolean(3)));}
    public List<Plan> plans(String insurer,boolean all){return db.query("SELECT insurer_code,code,display_name,active FROM insurance_plan WHERE insurer_code=? "+(all?"":"AND active ")+"ORDER BY display_name",(r,n)->new Plan(r.getString(1),r.getString(2),r.getString(3),r.getBoolean(4)),insurer);}
    @Transactional public Insurer saveInsurer(String code,String name){db.update("INSERT INTO health_insurer(code,display_name) VALUES(?,?) ON DUPLICATE KEY UPDATE display_name=VALUES(display_name)",code,name);return insurers(true).stream().filter(i->i.code().equals(code)).findFirst().orElseThrow();}
    @Transactional public Plan savePlan(String insurer,String code,String name){if(db.queryForObject("SELECT COUNT(*) FROM health_insurer WHERE code=?",Integer.class,insurer)==0)throw new SchedulingException("INSURER_NOT_FOUND","EPS no encontrada");db.update("INSERT INTO insurance_plan(insurer_code,code,display_name) VALUES(?,?,?) ON DUPLICATE KEY UPDATE display_name=VALUES(display_name)",insurer,code,name);return plans(insurer,true).stream().filter(p->p.code().equals(code)).findFirst().orElseThrow();}
    @Transactional public void toggleInsurer(String code,boolean active){db.update("UPDATE health_insurer SET active=? WHERE code=?",active,code);}
    @Transactional public void togglePlan(String insurer,String code,boolean active){db.update("UPDATE insurance_plan SET active=? WHERE insurer_code=? AND code=?",active,insurer,code);}
    @Transactional
    public Professional createProfessional(long userId,String code,String license,List<String> specialties,List<String> facilities) {
        if (db.queryForObject("SELECT COUNT(*) FROM app_user WHERE id=?",Integer.class,userId)==0) throw new SchedulingException("USER_NOT_FOUND","Usuario no encontrado");
        if (specialties==null||specialties.isEmpty()||facilities==null||facilities.isEmpty()) throw new SchedulingException("ASSIGNMENTS_REQUIRED","Asigne especialidad y sede");
        db.update("INSERT INTO professional(user_id,professional_code,license_number) VALUES(?,?,?)",userId,code,license);
        long id=db.queryForObject("SELECT id FROM professional WHERE user_id=?",Long.class,userId);
        for(String s:specialties) db.update("INSERT INTO professional_specialty(professional_id,specialty_code,is_primary) VALUES(?,?,?)",id,s,s.equals(specialties.get(0)));
        for(String f:facilities) db.update("INSERT INTO professional_facility(professional_id,facility_code) VALUES(?,?)",id,f);
        db.update("INSERT IGNORE INTO user_role(user_id,role_code) VALUES(?, 'PROFESSIONAL')",userId);
        return professional(id);
    }
    public List<Professional> professionals(boolean all) {
        List<Long> ids=db.query("SELECT id FROM professional "+(all?"":"WHERE active ")+"ORDER BY id",(r,n)->r.getLong(1));
        return ids.stream().map(this::professional).toList();
    }
    private Professional professional(long id) {
        Map<String,Object> p=db.queryForMap("SELECT id,user_id,professional_code,license_number,active FROM professional WHERE id=?",id);
        List<String> specs=db.query("SELECT specialty_code FROM professional_specialty WHERE professional_id=? ORDER BY specialty_code",(r,n)->r.getString(1),id);
        List<String> facilities=db.query("SELECT facility_code FROM professional_facility WHERE professional_id=? ORDER BY facility_code",(r,n)->r.getString(1),id);
        return new Professional(((Number)p.get("id")).longValue(),((Number)p.get("user_id")).longValue(),(String)p.get("professional_code"),(String)p.get("license_number"),(Boolean)p.get("active"),specs,facilities);
    }
    @Transactional
    public void toggleProfessional(long id,boolean active) { if(db.update("UPDATE professional SET active=? WHERE id=?",active,id)!=1) throw new SchedulingException("PROFESSIONAL_NOT_FOUND","Profesional no encontrado"); }

    @Transactional
    public Block createBlock(long userId,String facility,LocalDate date,LocalTime start,LocalTime end) {
        long professionalId=professionalIdFor(userId); validateFuture(date,start); if(!end.isAfter(start)) throw new SchedulingException("INVALID_BLOCK","El bloque debe tener un fin posterior");
        if(db.queryForObject("SELECT COUNT(*) FROM professional_facility WHERE professional_id=? AND facility_code=?",Integer.class,professionalId,facility)==0) throw new SchedulingException("FACILITY_NOT_ASSIGNED","La sede no está asignada");
        if(db.queryForObject("SELECT COUNT(*) FROM availability_block WHERE professional_id=? AND available_date=? AND start_time < ? AND end_time > ? AND active",Integer.class,professionalId,date,end,start)>0) throw new SchedulingException("OVERLAPPING_BLOCK","El bloque se solapa con otro");
        db.update("INSERT INTO availability_block(professional_id,facility_code,available_date,start_time,end_time) VALUES(?,?,?,?,?)",professionalId,facility,date,start,end);
        long id=db.queryForObject("SELECT LAST_INSERT_ID()",Long.class); return new Block(id,professionalId,facility,date,start,end);
    }
    @Transactional
    public Block updateBlock(long userId,long blockId,String facility,LocalDate date,LocalTime start,LocalTime end) {
        long professionalId=professionalIdFor(userId); validateFuture(date,start); if(!end.isAfter(start)) throw new SchedulingException("INVALID_BLOCK","El bloque debe tener un fin posterior");
        if(db.queryForObject("SELECT COUNT(*) FROM availability_block WHERE id=? AND professional_id=? AND active",Integer.class,blockId,professionalId)==0) throw new SchedulingException("BLOCK_NOT_FOUND","Bloque no encontrado");
        if(db.queryForObject("SELECT COUNT(*) FROM professional_facility WHERE professional_id=? AND facility_code=?",Integer.class,professionalId,facility)==0) throw new SchedulingException("FACILITY_NOT_ASSIGNED","La sede no está asignada");
        if(db.queryForObject("SELECT COUNT(*) FROM availability_block WHERE professional_id=? AND available_date=? AND start_time < ? AND end_time > ? AND active AND id<>?",Integer.class,professionalId,date,end,start,blockId)>0) throw new SchedulingException("OVERLAPPING_BLOCK","El bloque se solapa con otro");
        if(db.queryForObject("SELECT COUNT(*) FROM appointment_slot WHERE professional_id=? AND slot_start>=? AND slot_start<?",Integer.class,professionalId,Timestamp.valueOf(date.atStartOfDay()),Timestamp.valueOf(date.plusDays(1).atStartOfDay()))>0) throw new SchedulingException("BLOCK_COMMITTED","El bloque tiene citas comprometidas");
        db.update("UPDATE availability_block SET facility_code=?,available_date=?,start_time=?,end_time=? WHERE id=?",facility,date,start,end,blockId);
        return new Block(blockId,professionalId,facility,date,start,end);
    }
    public List<Block> blocks(long userId,LocalDate from,LocalDate to) {
        long pid=professionalIdFor(userId); return db.query("SELECT id,professional_id,facility_code,available_date,start_time,end_time FROM availability_block WHERE professional_id=? AND available_date BETWEEN ? AND ? AND active ORDER BY available_date,start_time",(r,n)->new Block(r.getLong(1),r.getLong(2),r.getString(3),r.getDate(4).toLocalDate(),r.getTime(5).toLocalTime(),r.getTime(6).toLocalTime()),pid,from,to);
    }
    @Transactional public void deleteBlock(long userId,long id) { long pid=professionalIdFor(userId); List<Map<String,Object>> rows=db.queryForList("SELECT available_date,start_time,end_time FROM availability_block WHERE id=? AND professional_id=? AND active",id,pid); if(rows.isEmpty()) throw new SchedulingException("BLOCK_NOT_FOUND","Bloque no encontrado"); Map<String,Object> block=rows.get(0); LocalDate date=((java.sql.Date)block.get("available_date")).toLocalDate(); LocalTime start=((java.sql.Time)block.get("start_time")).toLocalTime(); LocalTime end=((java.sql.Time)block.get("end_time")).toLocalTime(); if(db.queryForObject("SELECT COUNT(*) FROM appointment_slot WHERE professional_id=? AND slot_start>=? AND slot_start<?",Integer.class,pid,Timestamp.valueOf(date.atTime(start)),Timestamp.valueOf(date.atTime(end)))>0) throw new SchedulingException("BLOCK_COMMITTED","El bloque tiene citas comprometidas"); db.update("UPDATE availability_block SET active=FALSE WHERE id=?",id); }

    public List<Slot> availability(String facility,String specialty,String professional,LocalDate date) {
        if(date==null||date.isBefore(LocalDate.now())) return List.of();
        int duration=db.queryForObject("SELECT duration_minutes FROM specialty WHERE code=? AND active",Integer.class,specialty);
        StringBuilder sql=new StringBuilder("SELECT b.professional_id,b.facility_code,b.start_time,b.end_time,p.professional_code FROM availability_block b JOIN professional p ON p.id=b.professional_id JOIN professional_specialty ps ON ps.professional_id=p.id WHERE b.active AND p.active AND b.available_date=? AND b.facility_code=? AND ps.specialty_code=?");
        List<Object> args=new ArrayList<>(List.of(date,facility,specialty)); if(professional!=null&&!professional.isBlank()){sql.append(" AND p.professional_code=?");args.add(professional);} sql.append(" ORDER BY b.start_time");
        return db.query(sql.toString(),(r,n)->potentialSlots(r,facility,specialty,duration,date),args.toArray()).stream().flatMap(List::stream).filter(s->db.queryForObject("SELECT COUNT(*) FROM appointment_slot WHERE professional_id=? AND slot_start=?",Integer.class,s.professionalId(),Timestamp.valueOf(s.start()))==0).filter(s->db.queryForObject("SELECT COUNT(*) FROM reschedule_slot WHERE professional_id=? AND slot_start=?",Integer.class,s.professionalId(),Timestamp.valueOf(s.start()))==0).toList();
    }
    private List<Slot> potentialSlots(java.sql.ResultSet r,String facility,String specialty,int duration,LocalDate date) throws java.sql.SQLException { List<Slot> out=new ArrayList<>(); LocalTime start=r.getTime(3).toLocalTime(), end=r.getTime(4).toLocalTime(); long pid=r.getLong(1); for(LocalTime t=start;!t.plusMinutes(duration).isAfter(end);t=t.plusMinutes(30)) out.add(new Slot(facility,specialty,r.getString(5),pid,LocalDateTime.of(date,t),LocalDateTime.of(date,t.plusMinutes(duration)),duration)); return out; }

    @Transactional
    public Appointment book(long userId,String facility,String specialty,long professionalId,LocalDateTime start) {
        Map<String,Object> s=db.queryForMap("SELECT duration_minutes,is_general,requires_admin_approval FROM specialty WHERE code=? AND active",specialty); int duration=((Number)s.get("duration_minutes")).intValue(); boolean general=(Boolean)s.get("is_general");
        String code=db.queryForObject("SELECT professional_code FROM professional WHERE id=? AND active",String.class,professionalId); LocalDateTime end=start.plusMinutes(duration); ensureAvailable(facility,specialty,professionalId,start,end);
        String status=general?"APPROVED":"REQUESTED"; db.update("INSERT INTO appointment(patient_user_id,professional_id,facility_code,specialty_code,start_at,end_at,status_code,created_by,approved_by) VALUES(?,?,?,?,?,?,?,?,?)",userId,professionalId,facility,specialty,Timestamp.valueOf(start),Timestamp.valueOf(end),status,userId,general?userId:null); long id=db.queryForObject("SELECT LAST_INSERT_ID()",Long.class);
        reserveAppointmentSlots(id,professionalId,start,duration); history(id,status,userId,general?"SYSTEM":"USER",null); return new Appointment(id,userId,professionalId,code,facility,specialty,start,end,status,null);
    }
    private void ensureAvailable(String facility,String specialty,long pid,LocalDateTime start,LocalDateTime end) { if(start.isBefore(LocalDateTime.now())) throw new SchedulingException("PAST_APPOINTMENT","No se permiten citas en el pasado"); if(db.queryForObject("SELECT COUNT(*) FROM professional_facility WHERE professional_id=? AND facility_code=?",Integer.class,pid,facility)==0||db.queryForObject("SELECT COUNT(*) FROM professional_specialty WHERE professional_id=? AND specialty_code=?",Integer.class,pid,specialty)==0) throw new SchedulingException("PROFESSIONAL_NOT_ASSIGNED","El profesional no está habilitado"); if(db.queryForObject("SELECT COUNT(*) FROM availability_block WHERE professional_id=? AND facility_code=? AND available_date=? AND start_time<=? AND end_time>=? AND active",Integer.class,pid,facility,start.toLocalDate(),start.toLocalTime(),end.toLocalTime())==0) throw new SchedulingException("SLOT_UNAVAILABLE","Horario no disponible"); }
    private void reserveAppointmentSlots(long id,long pid,LocalDateTime start,int duration) { try { for(int i=0;i<duration/30;i++) db.update("INSERT INTO appointment_slot(professional_id,slot_start,appointment_id) VALUES(?,?,?)",pid,Timestamp.valueOf(start.plusMinutes(i*30)),id); } catch(DuplicateKeyException ex){throw new SchedulingException("SLOT_UNAVAILABLE","El horario acaba de ser reservado");} }
    private void history(long id,String status,long actor,String source,String reason){db.update("INSERT INTO appointment_status_history(appointment_id,status_code,actor_user_id,source_code,reason) VALUES(?,?,?,?,?)",id,status,actor,source,reason);}
    public List<Appointment> mine(long userId,String status) { String q="SELECT a.id,a.patient_user_id,a.professional_id,p.professional_code,a.facility_code,a.specialty_code,a.start_at,a.end_at,a.status_code,a.rejection_reason FROM appointment a JOIN professional p ON p.id=a.professional_id WHERE a.patient_user_id=?"+(status==null?"":" AND a.status_code=?")+" ORDER BY a.start_at"; return db.query(q,(r,n)->appointment(r),status==null?new Object[]{userId}:new Object[]{userId,status}); }
    public List<Appointment> requested() { return db.query("SELECT a.id,a.patient_user_id,a.professional_id,p.professional_code,a.facility_code,a.specialty_code,a.start_at,a.end_at,a.status_code,a.rejection_reason FROM appointment a JOIN professional p ON p.id=a.professional_id WHERE a.status_code='REQUESTED' ORDER BY a.start_at",(r,n)->appointment(r)); }
    public List<StatusHistory> history(long appointmentId){return db.query("SELECT status_code,source_code,reason,changed_at,COALESCE(actor_user_id,0) FROM appointment_status_history WHERE appointment_id=? ORDER BY changed_at,id",(r,n)->new StatusHistory(r.getString(1),r.getString(2),r.getString(3),r.getTimestamp(4).toLocalDateTime(),r.getLong(5)),appointmentId);}
    public List<Appointment> professionalAppointments(long userId,LocalDate date,String facility) { long pid=professionalIdFor(userId); return db.query("SELECT a.id,a.patient_user_id,a.professional_id,p.professional_code,a.facility_code,a.specialty_code,a.start_at,a.end_at,a.status_code,a.rejection_reason FROM appointment a JOIN professional p ON p.id=a.professional_id WHERE a.professional_id=? AND a.status_code='APPROVED' AND DATE(a.start_at)=?"+(facility==null?"":" AND a.facility_code=?")+" ORDER BY a.start_at",(r,n)->appointment(r),facility==null?new Object[]{pid,date}:new Object[]{pid,date,facility}); }
    private Appointment appointment(java.sql.ResultSet r) throws java.sql.SQLException{return new Appointment(r.getLong(1),r.getLong(2),r.getLong(3),r.getString(4),r.getString(5),r.getString(6),r.getTimestamp(7).toLocalDateTime(),r.getTimestamp(8).toLocalDateTime(),r.getString(9),r.getString(10));}
    @Transactional public void close(long userId,long id,String status){if(!status.equals("COMPLETED")&&!status.equals("NO_SHOW"))throw new SchedulingException("INVALID_STATUS","Estado inválido");long pid=professionalIdFor(userId);if(db.update("UPDATE appointment SET status_code=? WHERE id=? AND professional_id=? AND status_code='APPROVED' AND start_at<?",status,id,pid,Timestamp.valueOf(LocalDateTime.now()))!=1)throw new SchedulingException("INVALID_TRANSITION","La cita no puede cerrarse");db.update("DELETE FROM appointment_slot WHERE appointment_id=?",id);history(id,status,userId,"USER",null);}
    @Transactional public void cancel(long userId,long id){Map<String,Object>a=db.queryForMap("SELECT patient_user_id,start_at,status_code FROM appointment WHERE id=?",id);if(((Number)a.get("patient_user_id")).longValue()!=userId)throw new SchedulingException("FORBIDDEN","La cita no pertenece al usuario");if(!"APPROVED".equals(a.get("status_code"))||toLocalDateTime(a.get("start_at")).isBefore(LocalDateTime.now()))throw new SchedulingException("INVALID_TRANSITION","La cita no se puede cancelar");db.update("UPDATE appointment SET status_code='CANCELLED' WHERE id=?",id);db.update("DELETE FROM appointment_slot WHERE appointment_id=?",id);history(id,"CANCELLED",userId,"USER",null);}
    @Transactional public void decide(long admin,long id,boolean approve,String reason){if(!approve&&(reason==null||reason.isBlank()))throw new SchedulingException("REASON_REQUIRED","El rechazo exige motivo");if(db.update("UPDATE appointment SET status_code=?,rejection_reason=?,approved_by=? WHERE id=? AND status_code='REQUESTED'",approve?"APPROVED":"REJECTED",approve?null:reason,approve?admin:null,id)!=1)throw new SchedulingException("INVALID_TRANSITION","Solicitud no disponible");if(!approve)db.update("DELETE FROM appointment_slot WHERE appointment_id=?",id);history(id,approve?"APPROVED":"REJECTED",admin,"ADMIN",reason);}
    @Transactional public Reschedule requestReschedule(long userId,long appointmentId,String facility,LocalDateTime start){Map<String,Object>a=db.queryForMap("SELECT patient_user_id,professional_id,specialty_code,status_code,start_at FROM appointment WHERE id=?",appointmentId);if(((Number)a.get("patient_user_id")).longValue()!=userId||!"APPROVED".equals(a.get("status_code")))throw new SchedulingException("INVALID_TRANSITION","Solo citas aprobadas propias pueden reprogramarse");int duration=db.queryForObject("SELECT duration_minutes FROM specialty WHERE code=?",Integer.class,a.get("specialty_code"));LocalDateTime end=start.plusMinutes(duration);ensureAvailable(facility,(String)a.get("specialty_code"),((Number)a.get("professional_id")).longValue(),start,end);db.update("INSERT INTO reschedule_request(appointment_id,requested_by,facility_code,requested_start_at,requested_end_at) VALUES(?,?,?,?,?)",appointmentId,userId,facility,Timestamp.valueOf(start),Timestamp.valueOf(end));long id=db.queryForObject("SELECT LAST_INSERT_ID()",Long.class);try{for(int i=0;i<duration/30;i++)db.update("INSERT INTO reschedule_slot(professional_id,slot_start,request_id) VALUES(?,?,?)",((Number)a.get("professional_id")).longValue(),Timestamp.valueOf(start.plusMinutes(i*30)),id);}catch(DuplicateKeyException ex){throw new SchedulingException("SLOT_UNAVAILABLE","El horario no está disponible");}return new Reschedule(id,appointmentId,facility,start,end,"PENDING",null);}
    public List<Reschedule> pendingReschedules(){return db.query("SELECT id,appointment_id,facility_code,requested_start_at,requested_end_at,status_code,decision_reason FROM reschedule_request WHERE status_code='PENDING' ORDER BY created_at",(r,n)->new Reschedule(r.getLong(1),r.getLong(2),r.getString(3),r.getTimestamp(4).toLocalDateTime(),r.getTimestamp(5).toLocalDateTime(),r.getString(6),r.getString(7)));}
    @Transactional public void decideReschedule(long admin,long id,boolean approve,String reason){if(!approve&&(reason==null||reason.isBlank()))throw new SchedulingException("REASON_REQUIRED","El rechazo exige motivo");Map<String,Object>r=db.queryForMap("SELECT appointment_id,requested_start_at,requested_end_at,status_code FROM reschedule_request WHERE id=?",id);if(!"PENDING".equals(r.get("status_code")))throw new SchedulingException("INVALID_TRANSITION","Solicitud no disponible");long ap=((Number)r.get("appointment_id")).longValue();if(approve){db.update("DELETE FROM appointment_slot WHERE appointment_id=?",ap);db.update("INSERT INTO appointment_slot(professional_id,slot_start,appointment_id) SELECT s.professional_id,s.slot_start,? FROM reschedule_slot s WHERE s.request_id=?",ap,id);db.update("UPDATE appointment SET start_at=?,end_at=? WHERE id=?",r.get("requested_start_at"),r.get("requested_end_at"),ap);}else db.update("DELETE FROM reschedule_slot WHERE request_id=?",id);db.update("UPDATE reschedule_request SET status_code=?,decision_reason=?,decided_by=? WHERE id=?",approve?"APPROVED":"REJECTED",reason,admin,id);}
    private long professionalIdFor(long userId){List<Long> ids=db.query("SELECT id FROM professional WHERE user_id=? AND active",(r,n)->r.getLong(1),userId);if(ids.isEmpty())throw new SchedulingException("PROFESSIONAL_NOT_FOUND","El usuario no es un profesional activo");return ids.get(0);}
    private LocalDateTime toLocalDateTime(Object value){if(value instanceof LocalDateTime localDateTime)return localDateTime;if(value instanceof Timestamp timestamp)return timestamp.toLocalDateTime();if(value instanceof java.util.Date date)return LocalDateTime.ofInstant(date.toInstant(),java.time.ZoneId.systemDefault());return LocalDateTime.parse(value.toString().replace(' ','T'));}
    private void validateFuture(LocalDate date,LocalTime start){if(date.isBefore(LocalDate.now())||date.equals(LocalDate.now())&&!start.isAfter(LocalTime.now()))throw new SchedulingException("PAST_BLOCK","No se permiten bloques en el pasado");}
}
