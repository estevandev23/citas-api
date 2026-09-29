package co.edu.fcv.citas.adapter.http;

import co.edu.fcv.citas.application.FixedCatalogService;
import co.edu.fcv.citas.application.port.FixedCatalogs;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/catalogs")
class FixedCatalogController {
    private final FixedCatalogService service;

    FixedCatalogController(FixedCatalogService service) { this.service = service; }

    @GetMapping("/fixed")
    FixedCatalogResponse fixed() {
        var snapshot = service.consult();
        return new FixedCatalogResponse(items(snapshot.roles()), items(snapshot.appointmentStatuses()),
                items(snapshot.reschedulingStatuses()), items(snapshot.regimes()), items(snapshot.facilities()));
    }

    @GetMapping("/affiliation-options")
    AffiliationOptionsResponse affiliationOptions() {
        var snapshot = service.affiliationOptions();
        return new AffiliationOptionsResponse(items(snapshot.insurers()), snapshot.plans().stream()
                .map(plan -> new AffiliationPlanResponse(plan.insurerCode(), plan.code(), plan.label())).toList());
    }

    private static List<CatalogItemResponse> items(List<FixedCatalogs.Item> items) {
        return items.stream().map(item -> new CatalogItemResponse(item.code(), item.label())).toList();
    }

    record FixedCatalogResponse(List<CatalogItemResponse> roles, List<CatalogItemResponse> appointmentStatuses,
                                List<CatalogItemResponse> reschedulingStatuses, List<CatalogItemResponse> regimes,
                                List<CatalogItemResponse> facilities) {}
    record CatalogItemResponse(String code, String label) {}
    record AffiliationOptionsResponse(List<CatalogItemResponse> insurers, List<AffiliationPlanResponse> plans) {}
    record AffiliationPlanResponse(String insurerCode, String code, String label) {}
}
