package com.dietiestates.resource_server.factory;

import com.dietiestates.resource_server.model.CadastralFilter;
import com.dietiestates.resource_server.spec.CadastralFilterSpec;

public interface CadastralFilterFactory {

    CadastralFilter createCadastralFilterFromSpec(
            CadastralFilterSpec spec
    );
}
