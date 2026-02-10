package daw2026.exception;

/**
 * Excepción lanzada cuando se intenta acceder a un recurso que no existe
 * en la base de datos.
 */
public class ResourceNotFoundException extends RuntimeException {
   
    public ResourceNotFoundException(String message) {
        super(message);
    }
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
