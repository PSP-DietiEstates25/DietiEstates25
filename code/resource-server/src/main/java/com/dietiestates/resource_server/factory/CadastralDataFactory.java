package com.dietiestates.resource_server.factory;

import com.dietiestates.resource_server.model.CadastralData;
import com.dietiestates.resource_server.spec.CadastralDataSpec;

public interface CadastralDataFactory {
    CadastralData createCadastralDataFromSpec(CadastralDataSpec spec);
}
