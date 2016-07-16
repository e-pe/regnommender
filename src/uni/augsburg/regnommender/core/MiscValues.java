package uni.augsburg.regnommender.core;


public class MiscValues {
	
	
	
	// Zeitintervalle unbedingt in Sekunden angeben!
	public static int interval_long = 480;	// notification interval long (in seconds) for reminderSetting case 1
	public static int interval_middle = 240;	// notification interval middle (in seconds) for reminderSetting case 2
	public static int interval_short = 60;	// notification interval short (in seconds) for reminderSetting case 3
	
	public static float priority_veryhigh = 0.92f;  		// notification-priority 1 for reminderSetting case 1
	public static float priority_high = 0.78f;			// notification priority 2 for reminderSetting case 2
	public static float priority_moderate = 0.64f;		// notification priority 3 for reminderSetting case 3

	
	/**
	 * The matric
	 */
	// 											Nutz, Zier, Ordn., Kraft, Zeit, Finanz
	/** The lawn values. */
	public static int[] lawnValues = 			{1,   2,    5,     3,     5,    0};
	
	/** The escaped values. */
	public static int[] escapedValues = 		{1,   0,    0,     0,     0,    0};
	
	/** The structured values. */
	public static int[] structuredValues =  	{4,   7,    8,     5,     8,    4};
	
	/** The trees values. */
	public static int[] treesValues = 			{2,   4,    3,     6,     3,    8};
	
	/** The orchard values. */
	public static int[] orchardValues = 		{8,   3,    3,     8,     8,    8};
	
	/** The vegetables values. */
	public static int[] vegetablesValues = 		{8,   2,    3,     6,     7,    4};
	
	/** The flowerbed values. */
	public static int[] flowerbedValues = 		{2,   8,    6,     4,     4,    4};
	
	/** The herbs values. */	
	public static int[] herbsValues = 			{8,   2,    2,     2,     5,    0};
	
}
