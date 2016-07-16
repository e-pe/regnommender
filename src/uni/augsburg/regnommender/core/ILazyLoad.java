package uni.augsburg.regnommender.core;

/**
 * 
 * This interface provides the functionality to implement lazy loading pattern.
 *
 */
public interface ILazyLoad<T> {
	/**
	 * 
	 * @return the specific object of type T
	 */
	public T load();
}
