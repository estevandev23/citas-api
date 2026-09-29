package co.edu.fcv.citas.adapter.http;
import co.edu.fcv.citas.application.ProfileService;
import co.edu.fcv.citas.application.port.UserProfiles;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
@Component class TransactionalProfileFacade {
 private final ProfileService service; TransactionalProfileFacade(ProfileService service){this.service=service;}
 @Transactional(readOnly=true) UserProfiles.Profile mine(Long id){return service.mine(id);}
 @Transactional UserProfiles.Profile update(Long id, ProfileService.Update update){return service.update(id,update);}
}
