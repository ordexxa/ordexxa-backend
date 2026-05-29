package co.edu.uco.ordexxa.features.provider.registerprovider.application.usecase.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class ProviderDomain {

    private UUID id;
    private String businessName;
    private String documentType;
    private String documentNumber;
    private String email;
    private String phoneNumber;
    private String address;
    private Boolean active;
    private LocalDateTime createdAt;

    public ProviderDomain() {
        setId(UUID.randomUUID());
        setActive(Boolean.TRUE);
        setCreatedAt(LocalDateTime.now());
    }

    public ProviderDomain(
            final UUID id,
            final String businessName,
            final String documentType,
            final String documentNumber,
            final String email,
            final String phoneNumber,
            final String address,
            final Boolean active,
            final LocalDateTime createdAt
    ) {
        setId(id);
        setBusinessName(businessName);
        setDocumentType(documentType);
        setDocumentNumber(documentNumber);
        setEmail(email);
        setPhoneNumber(phoneNumber);
        setAddress(address);
        setActive(active);
        setCreatedAt(createdAt);
    }

    public static ProviderDomain create(
            final String businessName,
            final String documentType,
            final String documentNumber,
            final String email,
            final String phoneNumber,
            final String address
    ) {
        return new ProviderDomain(
                UUID.randomUUID(),
                businessName,
                documentType,
                documentNumber,
                email,
                phoneNumber,
                address,
                Boolean.TRUE,
                LocalDateTime.now()
        );
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(final String businessName) {
        this.businessName = businessName;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(final String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(final String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(final Boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
