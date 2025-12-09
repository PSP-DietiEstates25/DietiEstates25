package com.dietiestates.resource_server.servicedefaultimpl;

import com.dietiestates.resource_server.dto.request.RealEstateRequest;
import com.dietiestates.resource_server.dto.response.RealEstateResponse;
import com.dietiestates.resource_server.enums.RealEstateCategory;
import com.dietiestates.resource_server.factory.RealEstateFactory;
import com.dietiestates.resource_server.finder.*;
import com.dietiestates.resource_server.mapper.RealEstateMapper;
import com.dietiestates.resource_server.model.Admin;
import com.dietiestates.resource_server.model.Negotiation;
import com.dietiestates.resource_server.repository.NegotiationRepository;
import com.dietiestates.resource_server.repository.RealEstateRepository;
import com.dietiestates.resource_server.service.NotificationService;
import com.dietiestates.resource_server.service.RealEstateService;
import com.dietiestates.resource_server.service.SearchRealEstateService;
import com.dietiestates.resource_server.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final AdminFinder adminFinder;
	private final CadastralDataFinder cadastralDataFinder;
	private final DetailFinder detailFinder;
    private final SearchRealEstateService searchRealEstateService;
    private final NotificationService notificationService;
    private final NegotiationRepository negotiationRepository;

    private final StorageService storageService;

	@Override
	public RealEstateResponse createRealEstate(RealEstateRequest request, List<MultipartFile> images, String estateAgentEmail) throws IOException {

        // 1. Salvo le immagini su filesystem e mi faccio restituire le URL pubbliche
        List<String> imageUrls = new ArrayList<>();
        if (images != null) {
            for (MultipartFile img : images) {
                if (img != null && !img.isEmpty()) {
                    String url = storageService.uploadImageToFileSystem(img);
                    imageUrls.add(url);
                }
            }
        }

		var realEstateSpec = realEstateMapper.toSpec(request, imageUrls);
		
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
    public RealEstateResponse updateRealEstate(Long id, RealEstateRequest request, List<MultipartFile> images, String estateAgentEmail) throws IOException {

        var realEstateToUpdate = realEstateFinder.getRealEstateById(id);

        // 2. Gestione immagini
        List<String> imageUrls = new ArrayList<>();

        boolean hasNewImages = images != null && images.stream()
                .anyMatch(f -> f != null && !f.isEmpty());

        if (hasNewImages) {
            // Sostituisco COMPLETAMENTE le immagini con le nuove
            for (MultipartFile img : images) {
                if (img != null && !img.isEmpty()) {
                    String url = storageService.uploadImageToFileSystem(img);
                    imageUrls.add(url);
                }
            }
        } else {
            // Non è stato caricato nulla di nuovo: mantengo quelle esistenti
            imageUrls.addAll(realEstateToUpdate.getImages());
        }

        var realEstateSpec = realEstateMapper.toSpec(request, imageUrls);

        var estateAgent = estateAgentFinder.getEstateAgentByEmail(estateAgentEmail);
        var cadastralData = cadastralDataFinder.getCadastralDataById(realEstateSpec.getCadastralDataId());
        var detail = detailFinder.getDetailById(realEstateSpec.getDetailId());

        realEstateToUpdate.setCategory(
                RealEstateCategory.valueOf(realEstateSpec.getCategory())
        );
        realEstateToUpdate.setImages(new ArrayList<>(realEstateSpec.getImages()));
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
        var realEstate = realEstateFinder.getRealEstateById(realEstateId);
        for (Negotiation negotiation : realEstate.getNegotiations()) {
            negotiation.setRealEstate(null);
            negotiationRepository.save(negotiation);
        }
        realEstateRepository.delete(realEstate);
    }
}
