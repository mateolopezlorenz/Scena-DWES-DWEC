package daw2026.exception;

/**
 * Excepción lanzada cuando se intenta crear un usuario con un username o email
 * que ya existe en la base de datos.
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
