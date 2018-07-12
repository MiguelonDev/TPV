package tpv.login;

import java.sql.SQLException;

import tpv.dao.UsuariosDAO;
import tpv.model.Usuario;

/**
 * Clase de utilidades de Login (a falta de implementaci�n)
 * 
 * @author miguel.aguirre
 *
 */
public abstract class LoginUtils {

	/**
	 * Comprueba si una combinaci�n user/password es correcta
	 * Se necesitar� para implementar un sistema en el que varias personas usen el terminal
	 * 
	 * 
	 * @param login
	 * @param password
	 * 
	 * @return true si las credenciales son v�lidas, false si no lo son
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
			// TODO Los DAO igual no deber�an lanzar SQLException sino excepciones de negocio en todo caso
		}
		
		
		return retorno;
	}
	
}
