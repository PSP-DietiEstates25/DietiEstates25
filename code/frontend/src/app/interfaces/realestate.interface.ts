import { EnergyClass } from "../enums/energy-class.enum";

export interface RealEstate {
    propertyId: number,
    price: number,
    size: number,
    address: string,
    rooms: number,
    floor: number,
    energyClass: EnergyClass
}