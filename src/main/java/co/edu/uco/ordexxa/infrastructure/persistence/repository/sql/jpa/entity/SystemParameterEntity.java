package co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "system_parameters")
public class SystemParameterEntity {

    @Id
    @Column(name = "code", nullable = false, length = 80)
    private String code;

    @Column(name = "value", nullable = false, length = 250)
    private String value;

    @Column(name = "description", nullable = false, length = 300)
    private String description;

    @Column(name = "active", nullable = false)
    private Boolean active;

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(final Boolean active) {
        this.active = active;
    }
}
