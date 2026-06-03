package co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.repository;

import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentJpaRepository extends JpaRepository<DepartmentEntity, String> {

    List<DepartmentEntity> findByActiveTrueOrderByNameAsc();
}
