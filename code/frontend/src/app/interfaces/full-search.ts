import {
  GeographicalPositionResponse,
  Search,
  UtilityResponse,
} from '../services/models';

export interface FullSearch extends Search {
  geographicalPosition: GeographicalPositionResponse;
  utility: UtilityResponse;
}
