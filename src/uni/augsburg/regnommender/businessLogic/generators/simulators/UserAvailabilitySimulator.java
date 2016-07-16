package uni.augsburg.regnommender.businessLogic.generators.simulators;

/**
 * 
 * controll simulated user availability using static functions
 *
 */
public class UserAvailabilitySimulator {
		static boolean userAviable=true;
		static boolean atHome=true;
		
		public static void setUserAviable(boolean aviable)
		{
			if (aviable) userAviable=true;
			else  userAviable=false;
		}
		public static boolean getUserAviable()
		{
			return userAviable;
		}
		public static void setAtHome(boolean home)
		{
			if (home) atHome=true;
			else  atHome=false;
		}
		public static boolean getAtHome()
		{
			return atHome;
		}
}
