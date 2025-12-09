import { AdCategory } from '../enums/ad-category.enum';
import { EnergyClass } from '../enums/energy-class.enum';
import {
  GeographicalPositionResponse,
  RealEstate,
  UtilityResponse,
} from '../services/models';

export interface FullRealEstate extends RealEstate {
  geographicalPosition: GeographicalPositionResponse;
  utility: UtilityResponse;
  energyClass?: EnergyClass;
}
