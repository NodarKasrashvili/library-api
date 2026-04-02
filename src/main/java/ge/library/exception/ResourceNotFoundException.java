package ge.library.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, Long id) {
        super(resourceName + " ID=" + id + " ვერ მოიძებნა");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
