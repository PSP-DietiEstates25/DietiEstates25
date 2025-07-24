import { AdCategory } from "../enums/ad-category.enum";

export interface Ad {
    category: AdCategory,
    photo: string,
    description: string
}