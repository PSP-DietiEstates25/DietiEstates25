package com.dietiestates.resourceserver.serviceImpl.vecchi;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietiestates.resourceserver.enums.NotificationCategoryType;
import com.dietiestates.resourceserver.model.User;
import com.dietiestates.resourceserver.repository.NotificationPreferenceRepository;
import com.dietiestates.resourceserver.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationPreferenceService {

	
	/*
	private final NotificationPreferenceRepository prefRepo;
	private final UserRepository userRepo;

	
	 //restituisce tutte le preferenze; se una categoria manca, default =
	 //enabled(true).
	@Transactional(readOnly = true)
	public List<NotificationPreferenceResponse> listMine(String email) {
		return java.util.Arrays.stream(NotificationCategoryType.values())
				.map(cat -> prefRepo.findByUser_EmailAndCategory(email, cat)
						.map(p -> NotificationPreferenceResponse.builder()
								.category(cat.name())
								.enabled(p.getEnabled())
								.build())
						.orElse(NotificationPreferenceResponse.builder()
								.category(cat.name())
								.enabled(true)
								.build()))
				.toList();
	}

	// Imposta una singola categoria.
	@Transactional
	public NotificationPreferenceResponse set(String email, NotificationCategoryType cat, boolean enabled) {
		User user = userRepo.findByEmail(email)
				.orElseThrow(() -> new IllegalArgumentException("User not found: " + email));

		NotificationPreference pref = prefRepo.findByUser_EmailAndCategory(email, cat)
				.orElseGet(() -> NotificationPreference.builder()
						.category(cat)
						.user(user)
						.enabled(true)
						.build());

		pref.setEnabled(enabled);
		NotificationPreference saved = prefRepo.save(pref);

		return NotificationPreferenceResponse.builder()
				.category(saved.getCategory().name())
				.enabled(saved.getEnabled())
				.build();
	}

	// aggiornamento "in blocco" delle preferenze
	@Transactional
	public List<NotificationPreferenceResponse> bulkUpdate(String email, UpdateNotificationPreferencesRequest req) {
		for (var it : req.items()) {
			NotificationCategoryType cat = NotificationCategoryType.valueOf(it.category().toUpperCase());
			set(email, cat, it.enabled());
		}
		return listMine(email);
	}

	@Transactional(readOnly = true)
	public boolean isEnabled(String email, NotificationCategoryType cat) {
		return prefRepo.findByUser_EmailAndCategory(email, cat)
				.map(NotificationPreference::getEnabled)
				.orElse(true);
	}
	*/
}
