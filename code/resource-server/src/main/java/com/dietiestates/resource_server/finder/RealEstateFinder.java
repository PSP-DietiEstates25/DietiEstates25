package com.dietiestates.resource_server.finder;

import java.util.List;

import com.dietiestates.resource_server.exception.notfound.RealEstateNotFoundException;
import com.dietiestates.resource_server.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RealEstateFinder {
	RealEstate getRealEstateById(Long id) throws RealEstateNotFoundException;
    Page<RealEstate> getEstateAgentRealEstates(Long estateAgentId, Pageable pageable);
    Page<RealEstate> getAdminRealEstates(Admin admin, Pageable pageable);
    List<RealEstate> getAllRealEstates();
}
