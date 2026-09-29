package co.edu.fcv.citas.application;

import co.edu.fcv.citas.application.port.FixedCatalogs;

public class FixedCatalogService {
    private final FixedCatalogs catalogs;

    public FixedCatalogService(FixedCatalogs catalogs) { this.catalogs = catalogs; }

    public FixedCatalogs.Snapshot consult() { return catalogs.findAll(); }
    public FixedCatalogs.AffiliationSnapshot affiliationOptions() { return catalogs.findAffiliationOptions(); }
}
