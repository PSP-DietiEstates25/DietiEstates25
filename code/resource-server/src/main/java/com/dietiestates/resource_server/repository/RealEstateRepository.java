package com.dietiestates.resource_server.repository;

import com.dietiestates.resource_server.enums.RealEstateStatus;
import com.dietiestates.resource_server.model.RealEstate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface RealEstateRepository extends CrudRepository<RealEstate, Long>, PagingAndSortingRepository<RealEstate, Long>{

    //HELPER - usati nei service
    default Optional<RealEstate> findActiveById(Long id){
        return findByIdAndStatus(id, RealEstateStatus.ACTIVE);
    }

    default boolean existsActiveById(Long id){
        return existsByIdAndStatus(id, RealEstateStatus.ACTIVE);
    }

    default boolean existsActiveByIdAndEstateAgentEmail(Long id, String estateAgentEmail){
        return existsByIdAndEstateAgentEmailAndStatus(id, estateAgentEmail, RealEstateStatus.ACTIVE);
    }

    default List<RealEstate> findAllActive(){
        return findAllByStatus(RealEstateStatus.ACTIVE);
    }

    default Page<RealEstate> findActiveByEstateAgentId(Long estateAgentId, Pageable pageable){
        return findByEstateAgentIdAndStatus(estateAgentId, RealEstateStatus.ACTIVE, pageable);
    }

    //QUERY METHODS
    Optional<RealEstate> findByIdAndStatus(Long id, RealEstateStatus status);
    boolean existsByIdAndStatus(Long id, RealEstateStatus status);
    boolean existsByIdAndEstateAgentEmailAndStatus(Long id, String estateAgentEmail, RealEstateStatus status);
    List<RealEstate> findAllByStatus(RealEstateStatus status);
    Page<RealEstate> findByEstateAgentIdAndStatus(Long estateAgentId, RealEstateStatus status, Pageable pageable);
}
