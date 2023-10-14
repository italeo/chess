package dao;
/** This exception will be thrown when there is an error with the database */
public class DataAccessException extends Exception {

    /** Constructs the exception message to be thrown when database error occurs.
     * @param message - The error message describing the database error.
     * */
    public DataAccessException(String message) {
        super(message);
    }
}
