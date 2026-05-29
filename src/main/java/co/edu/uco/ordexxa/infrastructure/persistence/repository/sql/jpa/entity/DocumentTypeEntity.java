package co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "document_types")
public class DocumentTypeEntity {

    @Id
    @Column(name = "code", nullable = false, length = 20)
    private String code;

    @Column(name = "name", nullable = false, length = 80)
    private String name;

    @Column(name = "min_length", nullable = false)
    private Integer minLength;

    @Column(name = "max_length", nullable = false)
    private Integer maxLength;

    @Column(name = "numeric_only", nullable = false)
    private Boolean numericOnly;

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(final Integer minLength) {
        this.minLength = minLength;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(final Integer maxLength) {
        this.maxLength = maxLength;
    }

    public Boolean getNumericOnly() {
        return numericOnly;
    }

    public void setNumericOnly(final Boolean numericOnly) {
        this.numericOnly = numericOnly;
    }
}
