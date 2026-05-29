package co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.repository;

import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.entity.NotificationTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationTemplateJpaRepository extends JpaRepository<NotificationTemplateEntity, String> {
}
