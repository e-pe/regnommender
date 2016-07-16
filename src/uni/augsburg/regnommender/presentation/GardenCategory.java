package uni.augsburg.regnommender.presentation;

//Unkraut, strukturiert, Ziergarten,
public enum GardenCategory {
	// Kategorie, // current(1), wanted(2): Beschreibung
	raw,		// 1: Rohzustand, Brachland
	herbs,		// 2: Kräutergarten
	lawn,		// 1,2: Rasen
	escaped,	// 1,2: verwildert
	structured,	// 1,2: strukturiert, sauber
	trees,		// 1,2: viele Bäume
	orchard,	// 1,2: Obstgarten
	vegetables,	// 1,2: Gemüsegarten
	flowerbed,	// 1,2: Blumenbeete
}
