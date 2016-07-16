package uni.augsburg.regnommender.businessLogic.generators.simulators;

/**
 * 
 *  used to generate all tasks independent of actual priority using static functions
 *
 */
public class JustGenerateTaskSimulator {
	private static boolean justDoIt=false;
	public static boolean toggle()
	{
		justDoIt=!justDoIt;
		return justDoIt;
		
	}
	public static boolean On()
	{
		return justDoIt;
	}

}
