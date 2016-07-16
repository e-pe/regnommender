package uni.augsburg.regnommender.core;

/**
 * This interface represents generic method for indicating that an operation failed. 
 * 
 */
public interface IFailure<T extends Exception> {
    /**
     * Invokes the method for indicating that the request execution has failed.
     * 
     * @param e Exception which was thrown and can be handled within the method.
     */
    public void invoke(T exception);
}

