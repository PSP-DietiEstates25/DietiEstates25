package com.dietiestates.resource_server.verifierdefaultimpl;

import com.dietiestates.resource_server.exception.notfound.CadastralDataNotFoundException;
import com.dietiestates.resource_server.exception.notowned.CadastralDataNotOwnedByRealEstateException;
import com.dietiestates.resource_server.repository.CadastralDataRepository;
import com.dietiestates.resource_server.verifier.CadastralDataVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CadastralDataVerifierDefaultImpl implements CadastralDataVerifier {
	
}
