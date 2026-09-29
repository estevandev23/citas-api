package co.edu.fcv.citas.adapter.persistence;

import co.edu.fcv.citas.application.port.FixedCatalogs;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
class FixedCatalogPersistenceAdapter implements FixedCatalogs {
    @PersistenceContext private EntityManager entityManager;

    @Override
    public Snapshot findAll() {
        return new Snapshot(items("role"), items("appointment_status"), items("rescheduling_status"),
                items("regime"), items("facility"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public AffiliationSnapshot findAffiliationOptions() {
        List<Item> insurers = ((List<Object[]>) (List<?>) entityManager
                .createNativeQuery("SELECT code, display_name FROM health_insurer WHERE active ORDER BY display_name")
                .getResultList()).stream().map(row -> new Item((String) row[0], (String) row[1])).toList();
        List<Plan> plans = ((List<Object[]>) (List<?>) entityManager
                .createNativeQuery("SELECT insurer_code, code, display_name FROM insurance_plan WHERE active ORDER BY display_name")
                .getResultList()).stream().map(row -> new Plan((String) row[0], (String) row[1], (String) row[2])).toList();
        return new AffiliationSnapshot(insurers, plans);
    }

    @SuppressWarnings("unchecked")
    private List<Item> items(String table) {
        List<Object[]> rows = (List<Object[]>) (List<?>) entityManager
                .createNativeQuery("SELECT code, display_name FROM " + table + " ORDER BY display_order")
                .getResultList();
        return rows.stream()
                .map(row -> new Item((String) row[0], (String) row[1]))
                .toList();
    }
}
