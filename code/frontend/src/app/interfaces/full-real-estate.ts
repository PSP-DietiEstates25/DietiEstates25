import { AdCategory } from '../enums/ad-category.enum';
import { EnergyClass } from '../enums/energy-class.enum';
import {
  CadastralDataResponse,
  GeographicalPositionResponse,
  RealEstate,
  RealEstateResponse,
  UtilityResponse,
} from '../services/models';

export interface FullRealEstate extends RealEstateResponse {
  //aggiunta che può funzionare
  cadastralData: CadastralDataResponse,
  //
  geographicalPosition: GeographicalPositionResponse;
  utility: UtilityResponse;
  energyClass?: EnergyClass;
}
