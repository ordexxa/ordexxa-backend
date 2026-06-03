package co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.repository;

import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CityJpaRepository extends JpaRepository<CityEntity, Long> {

    @Query("""
           select city
           from CityEntity city
           join fetch city.department department
           where city.active = true
             and department.active = true
           order by department.name asc, city.name asc
           """)
    List<CityEntity> findActiveCitiesWithActiveDepartment();
}
