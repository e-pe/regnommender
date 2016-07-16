package uni.augsburg.regnommender.businessLogic.generators;

/**
 *  This class provides the functionality for creating different types of task generators.
 */
public class TaskGeneratorFactory {
	
	/**
	 * Creates a new task generator.
	 * @param generatorName is the type of the specific task generator.
	 */
	public static TaskGenerator createTaskGenerator(String generatorName) {
		
		if(generatorName.equals("RuleBased"))
			return new RuleBasedTaskGenerator();
		else if(generatorName.equals("ContentBased"))
			return new ContentBasedTaskGenerator();
		else if(generatorName.equals("MeanBased"))
			return new MeanBasedTaskGenerator();
		
		return null;
	}
}
