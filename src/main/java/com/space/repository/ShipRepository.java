package com.space.repository;

import com.space.model.Ship;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ShipRepository extends JpaRepository<Ship, Long> {

    @Query(value = "SELECT * FROM ship WHERE " +
            "CASE WHEN :name IS NOT NULL THEN name like %:name% ELSE TRUE END AND " +
            "CASE WHEN :planet IS NOT NULL THEN planet like %:planet% ELSE TRUE END AND " +
            "CASE WHEN :shipType IS NOT NULL THEN shipType = :shipType ELSE TRUE END AND " +
            "CASE WHEN :after IS NOT NULL THEN YEAR(prodDate) >= :after ELSE TRUE END AND " +
            "CASE WHEN :before IS NOT NULL THEN YEAR(prodDate) <= :before ELSE TRUE END AND " +
            "CASE WHEN :isUsed IS NOT NULL THEN isUsed = :isUsed ELSE TRUE END AND " +
            "CASE WHEN :minSpeed IS NOT NULL THEN speed >= :minSpeed ELSE TRUE END AND " +
            "CASE WHEN :maxSpeed IS NOT NULL THEN speed <= :maxSpeed ELSE TRUE END AND " +
            "CASE WHEN :minCrewSize IS NOT NULL THEN crewSize >= :minCrewSize ELSE TRUE END AND " +
            "CASE WHEN :maxCrewSize IS NOT NULL THEN crewSize <= :maxCrewSize ELSE TRUE END AND " +
            "CASE WHEN :minRating IS NOT NULL THEN rating >= :minRating ELSE TRUE END AND " +
            "CASE WHEN :maxRating IS NOT NULL THEN rating <= :maxRating ELSE TRUE END", nativeQuery = true)

    List<Ship> getShips(@Param("name") String name,
                        @Param("planet") String planet,
                        @Param("shipType") String shipType,
                        @Param("after") Integer after,
                        @Param("before") Integer before,
                        @Param("isUsed") Boolean isUsed,
                        @Param("minSpeed") Double minSpeed,
                        @Param("maxSpeed") Double maxSpeed,
                        @Param("minCrewSize") Double minCrewSize,
                        @Param("maxCrewSize") Double maxCrewSize,
                        @Param("minRating") Double minRating,
                        @Param("maxRating") Double maxRating,
                        Pageable pageable);
}
