package co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.repository;

import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.entity.SystemParameterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemParameterJpaRepository extends JpaRepository<SystemParameterEntity, String> {
}
