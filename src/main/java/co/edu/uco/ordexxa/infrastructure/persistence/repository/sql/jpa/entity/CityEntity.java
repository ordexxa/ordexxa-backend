package co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "cities")
public class CityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_code", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private DepartmentEntity department;

    @Column(name = "active", nullable = false)
    private Boolean active = Boolean.TRUE;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public DepartmentEntity getDepartment() {
        return department;
    }

    public void setDepartment(final DepartmentEntity department) {
        this.department = department;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(final Boolean active) {
        this.active = active;
    }
}
