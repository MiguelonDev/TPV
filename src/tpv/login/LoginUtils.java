package tpv.login;

import java.sql.SQLException;

import tpv.dao.UsuariosDAO;
import tpv.model.Usuario;

/**
 * Clase de utilidades de Login (a falta de implementación)
 * 
 * @author miguel.aguirre
 *
 */
public abstract class LoginUtils {

	/**
	 * Comprueba si una combinación user/password es correcta
	 * Se necesitará para implementar un sistema en el que varias personas usen el terminal
	 * 
	 * 
	 * @param login
	 * @param password
	 * 
	 * @return true si las credenciales son válidas, false si no lo son
	 */
	public static boolean loginCorrecto(String login, String password) {
		boolean retorno = false;
		UsuariosDAO dao = new UsuariosDAO();
		try {
			Usuario usuario = dao.findById(login);
			retorno = usuario != null && usuario.getPassword() != null && usuario.getPassword().equals(password); 
		} catch(SQLException e) {
			System.err.println("Error de SQL buscando al usuario " + login);
			System.err.println(e);
			// TODO Los DAO igual no deberían lanzar SQLException sino excepciones de negocio en todo caso
		}
		
		
		return retorno;
	}
	
}
