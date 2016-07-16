package uni.augsburg.regnommender.core;

/**
 * This interface represents a generic method for indicating that an operation succeeded. 
 * 
 */
public interface ISuccess<T> {
    /**
     * Invokes the method for indicating that the request execution has succeeded.
     * 
     * @param value The result of the request operation. 
     */
    public void invoke(T data);
}
