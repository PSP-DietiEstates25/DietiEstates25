import {
  CadastralFilterResponse,
  GeographicalPositionResponse,
  Search,
  SearchResponse,
  UtilityResponse,
} from '../services/models';

export interface FullSearch extends SearchResponse {
  cadastralFilter?: CadastralFilterResponse;
  geographicalPosition: GeographicalPositionResponse;
  utility: UtilityResponse;
}
