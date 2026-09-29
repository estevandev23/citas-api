package co.edu.fcv.citas.application.port;

import java.util.List;

public interface FixedCatalogs {
    Snapshot findAll();
    AffiliationSnapshot findAffiliationOptions();

    record Item(String code, String label) {}
    record Snapshot(List<Item> roles, List<Item> appointmentStatuses,
                    List<Item> reschedulingStatuses, List<Item> regimes,
                    List<Item> facilities) {}
    record Plan(String insurerCode, String code, String label) {}
    record AffiliationSnapshot(List<Item> insurers, List<Plan> plans) {}
}
