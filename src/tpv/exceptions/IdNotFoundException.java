package tpv.exceptions;

/**
 * Excepci�n de negocio que ocurre cuando se busca por ID y no se encuentra la entidad
 * @author miguel.aguirre
 *
 */
public class IdNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7633128464987973565L;

	public IdNotFoundException() {
		super();
	}

	public IdNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public IdNotFoundException(String message) {
		super(message);
	}

	public IdNotFoundException(Throwable cause) {
		super(cause);
	}
	
}
