package co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.repository;

import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CityJpaRepository extends JpaRepository<CityEntity, Long> {

    @Query(value = """
            select c.id, c.name, c.department_code
            from cities c
            join departments d on d.code = c.department_code
            where c.active = true
              and d.active = true
            order by d.name asc, c.name asc
            """, nativeQuery = true)
    List<Object[]> findActiveCityRowsWithActiveDepartment();
}
