package co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "departments")
public class DepartmentEntity {

    @Id
    @Column(name = "code", nullable = false, length = 10)
    private String code;

    @Column(name = "name", nullable = false, unique = true, length = 80)
    private String name;

    @Column(name = "active", nullable = false)
    private Boolean active = Boolean.TRUE;

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

    public Boolean getActive() {
        return active;
    }

    public void setActive(final Boolean active) {
        this.active = active;
    }
}
