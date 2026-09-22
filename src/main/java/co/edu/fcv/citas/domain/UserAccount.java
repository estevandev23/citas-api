package co.edu.fcv.citas.domain;

import java.util.Set;

public record UserAccount(Long id, String firstName, String lastName, String documentType,
                          String documentNumber, String email, String phone, String passwordHash,
                          boolean active, Set<String> roles) {}
