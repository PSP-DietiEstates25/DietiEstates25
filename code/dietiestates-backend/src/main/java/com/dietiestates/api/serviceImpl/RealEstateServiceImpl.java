package com.dietiestates.api.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.AccessDeniedException;

import com.dietiestates.api.dto.request.RealEstateRequest;
import com.dietiestates.api.dto.response.RealEstateResponse;
import com.dietiestates.api.enums.AdCategory;
import com.dietiestates.api.factory.RealEstateFactory;
import com.dietiestates.api.finder.CadastralDataFinder;
import com.dietiestates.api.finder.DetailFinder;
import com.dietiestates.api.finder.EstateAgentFinder;
import com.dietiestates.api.finder.RealEstateFinder;
import com.dietiestates.api.mapper.RealEstateMapper;
import com.dietiestates.api.model.CadastralFilter;
import com.dietiestates.api.model.GeographicalPosition;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.model.Search;
import com.dietiestates.api.model.Utility;
import com.dietiestates.api.repository.RealEstateRepository;
import com.dietiestates.api.service.RealEstateService;
import com.dietiestates.api.service.GeoProximityService;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class RealEstateServiceImpl implements RealEstateService {

	private final RealEstateRepository realEstateRepository;
	private final RealEstateFactory realEstateFactory;
	private final RealEstateFinder realEstateFinder;
	private final RealEstateMapper realEstateMapper;

	private final EstateAgentFinder estateAgentFinder;
	private final CadastralDataFinder cadastralDataFinder;
	private final DetailFinder detailFinder;

	private final GeoProximityService geoProximityService;

	private static final Logger log = LoggerFactory.getLogger(RealEstateServiceImpl.class);

	@Override
	public RealEstateResponse createRealEstate(RealEstateRequest request) {

		var realEstateSpec = realEstateMapper.toSpec(request);

		var estateAgent = estateAgentFinder.getEstateAgentByEmail(realEstateSpec.getEstateAgentEmail());
		var cadastralData = cadastralDataFinder.getCadastralDataById(realEstateSpec.getCadastralDataId());
		var detail = detailFinder.getDetailById(realEstateSpec.getDetailId());

		var realEstate = realEstateFactory.createRealEstateFromSpec(realEstateSpec, estateAgent, cadastralData, detail);

		// proximity
		if (detail != null && detail.getGeographicalPosition() != null) {
			var gp = detail.getGeographicalPosition();
			var lat = gp.getLatitude();
			var lon = gp.getLongitude();
			var radius = gp.getRadius() != null ? gp.getRadius() : 500;
			var tags = geoProximityService.detectTags(lat, lon, radius);
			realEstate.setProximityTags(tags);
		}

		realEstateRepository.save(realEstate);

		return realEstateMapper.fromEntity(realEstate);
	}

	@Override
	@Transactional(readOnly = true)
	public List<RealEstateResponse> createRealEstatesResponse(List<RealEstate> realEstates) {

		var response = new ArrayList<RealEstateResponse>();

		realEstates.forEach(realEstate -> {
			var realEstateResponse = realEstateMapper.fromEntity(realEstate);
			response.add(realEstateResponse);
		});

		return response;
	}

	@Override
	@Transactional(readOnly = true)
	public RealEstateResponse getRealEstateById(Long id) {
		var realEstate = realEstateFinder.getRealEstateById(id);
		return realEstateMapper.fromEntity(realEstate);
	}

	@Override
	public List<RealEstate> getRealEstatesBySearchFilter(Search search) {

		var allRealEstates = realEstateFinder.getAllRealEstates();

		var realEstatesByGeographicalPosition = getRealEstatesByGeographicalPosition(
				search.getDetail().getGeographicalPosition(), allRealEstates);
		var realEstatesByUtility = getRealEstatesByUtility(search.getDetail().getUtility(),
				realEstatesByGeographicalPosition);
		var realEstatesByCadastralFilter = getRealEstatesByCadastralFilter(search.getCadastralFilter(),
				realEstatesByUtility);

		return realEstatesByCadastralFilter;
	}

	@Override
	public List<RealEstate> getRealEstatesByGeographicalPosition(
			GeographicalPosition geographicalPosition,
			List<RealEstate> realEstates) {

		if (geographicalPosition == null)
			return realEstates;

		var out = new ArrayList<RealEstate>();
		for (var re : realEstates) {
			var detail = re.getDetail();
			if (detail == null)
				continue;
			var gp = detail.getGeographicalPosition();
			if (gp == null)
				continue;

			var sameCity = safeEq(gp.getCity(), geographicalPosition.getCity());
			var sameMun = safeEq(gp.getMunicipality(), geographicalPosition.getMunicipality());
			if (sameCity && sameMun)
				out.add(re);

		}
		return out;
	}

	private boolean safeEq(String a, String b) {
		if (b == null || b.isBlank())
			return true;
		if (a == null)
			return false;
		return a.equalsIgnoreCase(b);
	}

	@Override
	public List<RealEstate> getRealEstatesByUtility(Utility utility, List<RealEstate> realEstates) {
		if (utility == null)
			return realEstates; // nessun filtro

		var out = new ArrayList<RealEstate>();
		for (var re : realEstates) {
			var detail = re.getDetail();
			if (detail == null)
				continue;
			var u = detail.getUtility();
			if (u == null)
				continue;

			boolean ok = eqBool(u.getHasAirConditioning(), utility.getHasAirConditioning()) &&
					eqBool(u.getHasDoorman(), utility.getHasDoorman()) &&
					eqBool(u.getHasElevator(), utility.getHasElevator());
			if (ok)
				out.add(re);
		}
		return out;
	}

	private boolean eqBool(Boolean a, Boolean b) {
		if (b == null)
			return true;
		return Boolean.TRUE.equals(a) == Boolean.TRUE.equals(b);
	}

	@Override
	public List<RealEstate> getRealEstatesByCadastralFilter(CadastralFilter cadastralFilter,
			List<RealEstate> realEstates) {
		var cadastralFilterRealEstates = new ArrayList<RealEstate>();
		realEstates.forEach(realEstate -> {
			var realEstateCadastralData = realEstate.getCadastralData();
			if (cadastralFilter.getPriceRange().contains(realEstateCadastralData.getPrice()) &&
					cadastralFilter.getSquareMetersRange().contains(realEstateCadastralData.getSquareMeters()) &&
					cadastralFilter.getEnergyClassRange().contains(realEstateCadastralData.getEnergyClass().getOrder())
					&&
					cadastralFilter.getRoomsRange().contains(realEstateCadastralData.getRooms()) &&
					cadastralFilter.getFloorRange().contains(realEstateCadastralData.getFloor()))
				cadastralFilterRealEstates.add(realEstate);
		});

		return cadastralFilterRealEstates;
	}

	@Override
	@Transactional
	public RealEstateResponse updateRealEstate(Long id, RealEstateRequest request) {
		var entity = realEstateFinder.getRealEstateById(id);

		// category
		if (request.getCategory() != null) {
			var cat = AdCategory.valueOf(request.getCategory().trim().toUpperCase(java.util.Locale.ROOT));
			entity.setCategory(cat);
		}

		// description
		if (request.getDescription() != null) {
			entity.setDescription(request.getDescription());
		}

		// estateAgent (se cambi l’email)
		if (request.getEstateAgentEmail() != null) {
			var agent = estateAgentFinder.getEstateAgentByEmail(request.getEstateAgentEmail());
			entity.setEstateAgent(agent);
		}

		// cadastralData
		if (request.getCadastralDataId() != null) {
			var cadastral = cadastralDataFinder.getCadastralDataById(request.getCadastralDataId());
			entity.setCadastralData(cadastral);
		}

		// detail
		if (request.getDetailId() != null) {
			var detail = detailFinder.getDetailById(request.getDetailId());
			entity.setDetail(detail);
		}
		
		// images
		if (request.getImages() != null) {
			entity.getImages().clear();
			entity.getImages().addAll(request.getImages());
		}

		var saved = realEstateRepository.save(entity);
		return realEstateMapper.fromEntity(saved);
	}

	@Override
	@Transactional
	public void deleteRealEstate(Long id) {
		var entity = realEstateFinder.getRealEstateById(id);
		realEstateRepository.delete(entity);
	}

}
