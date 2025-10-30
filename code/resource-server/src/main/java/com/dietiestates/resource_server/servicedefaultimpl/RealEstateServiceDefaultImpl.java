package com.dietiestates.resource_server.servicedefaultimpl;

import com.dietiestates.resource_server.dto.request.RealEstateRequest;
import com.dietiestates.resource_server.dto.response.RealEstateResponse;
import com.dietiestates.resource_server.enums.AdCategory;
import com.dietiestates.resource_server.factory.RealEstateFactory;
import com.dietiestates.resource_server.filter.RealEstateFilter;
import com.dietiestates.resource_server.finder.CadastralDataFinder;
import com.dietiestates.resource_server.finder.DetailFinder;
import com.dietiestates.resource_server.finder.EstateAgentFinder;
import com.dietiestates.resource_server.finder.RealEstateFinder;
import com.dietiestates.resource_server.mapper.RealEstateMapper;
import com.dietiestates.resource_server.model.*;
import com.dietiestates.resource_server.repository.RealEstateRepository;
import com.dietiestates.resource_server.service.NotificationService;
import com.dietiestates.resource_server.service.RealEstateService;
import com.dietiestates.resource_server.service.SearchRealEstateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RealEstateServiceDefaultImpl implements RealEstateService {
	
	private final RealEstateRepository realEstateRepository;
	private final RealEstateFactory realEstateFactory;
	private final RealEstateFinder realEstateFinder;
	private final RealEstateMapper realEstateMapper;
	
	private final EstateAgentFinder estateAgentFinder;
	private final CadastralDataFinder cadastralDataFinder;
	private final DetailFinder detailFinder;
    private final SearchRealEstateService searchRealEstateService;
    private final NotificationService notificationService;
	
	@Override
	public RealEstateResponse createRealEstate(RealEstateRequest request) {

		var realEstateSpec = realEstateMapper.toSpec(request);
		
		var estateAgent = estateAgentFinder.getEstateAgentByEmail(realEstateSpec.getEstateAgentEmail());
		var cadastralData = cadastralDataFinder.getCadastralDataById(realEstateSpec.getCadastralDataId());
		var detail = detailFinder.getDetailById(realEstateSpec.getDetailId());
		
		var realEstate = realEstateFactory.createRealEstateFromSpec(
                realEstateSpec,
                estateAgent,
                cadastralData,
                detail
        );
        var searchesToNotify = searchRealEstateService.createRealEstateSearchesLink(realEstate);
		realEstateRepository.save(realEstate);

        notificationService.createNotificationsAfterRealEstateCreation(searchesToNotify);

		return realEstateMapper.fromEntity(realEstate);
	}


	@Override
	public RealEstateResponse getRealEstateById(Long id) {

		var realEstate = realEstateFinder.getRealEstateById(id);
		return realEstateMapper.fromEntity(realEstate);
	}

    @Override
    public Page<RealEstateResponse> getPagedRealEstates(Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        var realEstates = realEstateRepository.findAll(pageable);
        return realEstateMapper.createPagedRealEstatesResponse(realEstates);
    }

    @Override
    @Transactional
    public RealEstateResponse updateRealEstate(Long id, RealEstateRequest request) {

        var realEstateSpec = realEstateMapper.toSpec(request);
        var realEstateToUpdate = realEstateFinder.getRealEstateById(id);

        var estateAgent = estateAgentFinder.getEstateAgentByEmail(realEstateSpec.getEstateAgentEmail());
        var cadastralData = cadastralDataFinder.getCadastralDataById(realEstateSpec.getCadastralDataId());
        var detail = detailFinder.getDetailById(realEstateSpec.getDetailId());

        realEstateToUpdate.setCategory(
                AdCategory.valueOf(realEstateSpec.getCategory())
        );
        realEstateToUpdate.setImages(Arrays.asList(realEstateSpec.getImages()));
        realEstateToUpdate.setDescription(realEstateSpec.getDescription());
        realEstateToUpdate.setEstateAgent(estateAgent);
        realEstateToUpdate.setCadastralData(cadastralData);
        realEstateToUpdate.setDetail(detail);

        realEstateRepository.save(realEstateToUpdate);
        return realEstateMapper.fromEntity(realEstateToUpdate);
    }

    @Override
    @Transactional
    public void deleteRealEstate(Long realEstateId) {
        var entity = realEstateFinder.getRealEstateById(realEstateId);
        realEstateRepository.delete(entity);
    }
}
