package daw2026.exception;

/**
 * Excepción lanzada cuando se intenta crear un local con un nombre
 * que ya existe en la base de datos.
 */
public class LocalAlreadyExistsException extends RuntimeException {
    
    public LocalAlreadyExistsException(String message) {
        super(message);
    }
    public LocalAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
