import { AdCategory } from "../../enums/ad-category.enum";
import { EnergyClass } from "../../enums/energy-class.enum";

export interface AdRequest {
    category: AdCategory,
    photo: string,
    description: string,
    propertyId: number,
    price: number,
    size: number,
    address: string,
    rooms: number,
    floor: number,
    energyClass: EnergyClass
}