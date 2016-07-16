package uni.augsburg.regnommender.businessLogic.generators;

import java.util.*;
import uni.augsburg.regnommender.businessLogic.*;

/**
 * This interface represents the functionality which every type of task generator must contain (RuleBased, ContentBased, MeanBased). 
 */
public interface ITaskGenerator {
	/**
	 * Generates user tasks according to the specific task generator logic.
	 * @return A collection of generated user tasks.
	 */
	public ArrayList<UserTaskData> generateUserTasks();
}
