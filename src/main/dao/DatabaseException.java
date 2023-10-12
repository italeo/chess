package dao;
/** This exception will be thrown when there is an error with the database */
public class DatabaseException extends Exception {

    /** Constructs the exception message to be thrown when database error occurs.
     * @param message - The error message describing the database error.
     * */
    public DatabaseException(String message) {
        super(message);
    }
}
