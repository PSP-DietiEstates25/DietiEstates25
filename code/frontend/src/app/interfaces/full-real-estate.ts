import { AdCategory } from '../enums/ad-category.enum';
import {
  GeographicalPositionResponse,
  RealEstate,
  UtilityResponse,
} from '../services/models';

export interface FullRealEstate extends RealEstate {
  geographicalPosition: GeographicalPositionResponse;
  utility: UtilityResponse;
}
