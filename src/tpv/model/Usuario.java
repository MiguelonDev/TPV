package tpv.model;

/**
 * Objeto de modelo que representa a un usuario
 * 
 * @author miguel.aguirre
 *
 */
public class Usuario {
	
	private String nombre;
	
	private String password;
	
	

	@Override
	public String toString() {
		return "Usuario [nombre=" + nombre + ", password=" + password + "]";
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
