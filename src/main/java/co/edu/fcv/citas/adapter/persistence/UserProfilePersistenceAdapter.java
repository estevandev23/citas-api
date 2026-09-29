package co.edu.fcv.citas.adapter.persistence;

import co.edu.fcv.citas.application.port.UserProfiles;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
class UserProfilePersistenceAdapter implements UserProfiles {
    @PersistenceContext private EntityManager entityManager;

    @Override @SuppressWarnings("unchecked")
    public Optional<Profile> find(Long userId) {
        List<Object[]> rows = (List<Object[]>) (List<?>) entityManager.createNativeQuery("""
                SELECT u.id,u.first_name,u.last_name,u.document_type,u.document_number,u.email,u.phone,
                       a.insurer_code,i.display_name,a.plan_code,p.display_name,a.regime_code,r.display_name
                FROM app_user u LEFT JOIN user_affiliation a ON a.user_id=u.id
                LEFT JOIN health_insurer i ON i.code=a.insurer_code
                LEFT JOIN insurance_plan p ON p.insurer_code=a.insurer_code AND p.code=a.plan_code
                LEFT JOIN regime r ON r.code=a.regime_code WHERE u.id=?1""")
                .setParameter(1, userId).getResultList();
        return rows.stream().findFirst().map(row -> new Profile(((Number) row[0]).longValue(), (String) row[1],
                (String) row[2], (String) row[3], (String) row[4], (String) row[5], (String) row[6],
                (String) row[7], (String) row[8], (String) row[9], (String) row[10], (String) row[11], (String) row[12]));
    }
    public boolean validAffiliation(String insurer, String plan, String regime) {
        return ((Number) entityManager.createNativeQuery("""
                SELECT COUNT(*) FROM insurance_plan p JOIN health_insurer i ON i.code=p.insurer_code
                JOIN regime r ON r.code=?3 WHERE p.insurer_code=?1 AND p.code=?2 AND i.active AND p.active""")
                .setParameter(1, insurer).setParameter(2, plan).setParameter(3, regime).getSingleResult()).longValue() == 1;
    }
    public void update(Long id, String first, String last, String phone, String insurer, String plan, String regime) {
        entityManager.createNativeQuery("UPDATE app_user SET first_name=?2,last_name=?3,phone=?4 WHERE id=?1")
                .setParameter(1,id).setParameter(2,first).setParameter(3,last).setParameter(4,phone).executeUpdate();
        int updated = entityManager.createNativeQuery("UPDATE user_affiliation SET insurer_code=?2,plan_code=?3,regime_code=?4 WHERE user_id=?1")
                .setParameter(1,id).setParameter(2,insurer).setParameter(3,plan).setParameter(4,regime).executeUpdate();
        if (updated == 0) entityManager.createNativeQuery("INSERT INTO user_affiliation (user_id,insurer_code,plan_code,regime_code) VALUES (?1,?2,?3,?4)")
                .setParameter(1,id).setParameter(2,insurer).setParameter(3,plan).setParameter(4,regime).executeUpdate();
    }
}
