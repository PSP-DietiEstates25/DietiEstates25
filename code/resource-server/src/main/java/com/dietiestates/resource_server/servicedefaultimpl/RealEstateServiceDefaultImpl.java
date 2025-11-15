package com.dietiestates.resource_server.servicedefaultimpl;

import com.dietiestates.resource_server.dto.request.RealEstateRequest;
import com.dietiestates.resource_server.dto.response.RealEstateResponse;
import com.dietiestates.resource_server.enums.RealEstateCategory;
import com.dietiestates.resource_server.factory.RealEstateFactory;
import com.dietiestates.resource_server.finder.*;
import com.dietiestates.resource_server.mapper.RealEstateMapper;
import com.dietiestates.resource_server.model.Admin;
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

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class RealEstateServiceDefaultImpl implements RealEstateService {
	
	private final RealEstateRepository realEstateRepository;
	private final RealEstateFactory realEstateFactory;
	private final RealEstateFinder realEstateFinder;
	private final RealEstateMapper realEstateMapper;
	
    private final EstateAgentFinder estateAgentFinder;
    private final AdminFinder adminFinder;
	private final CadastralDataFinder cadastralDataFinder;
	private final DetailFinder detailFinder;
    private final SearchRealEstateService searchRealEstateService;
    private final NotificationService notificationService;
	
	@Override
	public RealEstateResponse createRealEstate(RealEstateRequest request, String estateAgentEmail) {

		var realEstateSpec = realEstateMapper.toSpec(request);
		
		var estateAgent = estateAgentFinder.getEstateAgentByEmail(estateAgentEmail);
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
    public Page<RealEstateResponse> getRealEstates(Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        var realEstates = realEstateRepository.findAll(pageable);
        return realEstateMapper.createPagedRealEstatesResponse(realEstates);
    }

    @Override
    public Page<RealEstateResponse> getEstateAgentRealEstates(String estateAgentEmail, Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        var estateAgent = estateAgentFinder.getEstateAgentByEmail(estateAgentEmail);
        var estateAgentRealEstates = realEstateFinder.getEstateAgentRealEstates(estateAgent.getId(), pageable);

        return realEstateMapper.createPagedRealEstatesResponse(estateAgentRealEstates);
    }

    @Override
    public Page<RealEstateResponse> getAdminRealEstates(String adminEmail, Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        var admin = adminFinder.getAdminByEmail(adminEmail);
        var adminRealEstates = realEstateFinder.getAdminRealEstates(admin, pageable);
        return realEstateMapper.createPagedRealEstatesResponse(adminRealEstates);
    }

    @Override
    public Page<RealEstateResponse> getSearchRealEstates(Long searchId, Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        var searchRealEstates = realEstateFinder.getSearchRealEstates(searchId, pageable);

        return realEstateMapper.createPagedRealEstatesResponse(searchRealEstates);
    }

    @Override
    @Transactional
    public RealEstateResponse updateRealEstate(Long id, RealEstateRequest request, String estateAgentEmail) {

        var realEstateSpec = realEstateMapper.toSpec(request);
        var realEstateToUpdate = realEstateFinder.getRealEstateById(id);

        var estateAgent = estateAgentFinder.getEstateAgentByEmail(estateAgentEmail);
        var cadastralData = cadastralDataFinder.getCadastralDataById(realEstateSpec.getCadastralDataId());
        var detail = detailFinder.getDetailById(realEstateSpec.getDetailId());

        realEstateToUpdate.setCategory(
                RealEstateCategory.valueOf(realEstateSpec.getCategory())
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
