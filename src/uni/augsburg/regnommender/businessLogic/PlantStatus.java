package uni.augsburg.regnommender.businessLogic;

/**
 * 
 * PlantStatus is only "not_existing" if the plant was never inserted into UserPlants;
 * if a UserPlant is removed its attribute "isRemoved" is set to true - its PlantStatus will remain the same;
 * 
 */
public enum PlantStatus {not_existing, seed, cutting, exists}
