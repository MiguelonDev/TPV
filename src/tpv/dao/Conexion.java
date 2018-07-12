package tpv.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Esta clase encapsula la conexi�n a la base de datos.
 * 
 * La cadena de conexi�n es similar para todos los usos (conexi�n local de mysql por el puerto por defecto) pero
 * el esquema podr�a cambiar
 * 
 * TODO: Si s�lo vamos a utilizar un esquema, integrarlo en la constrante STR_CONEXION
 * 
 * @author miguel.aguirre
 *
 */
public class Conexion {
	
	// Esta es la plantilla de conexi�n
	// El primer par�metro de tipo %s es el esquema a conectarse
	private static final String STR_CONEXION = "jdbc:mysql://localhost:3306/%s";
	
	/**
	 * Esquema al que de debemos conectarnos
	 */
	private String schema;
	
	/**
	 * Nombre de usuario para conectar a la base de datos
	 */
	private String userName;
	
	/**
	 * Contrase�a para conectar a la base de datos
	 */
	private String pass;
	
	/**
	 * Objeto connection que pervive mientras est� activo esta instancia
	 */
	private Connection datosDeConexion;
	
	/**
	 * Construye una nueva conexi�n pas�ndole credenciales y esquema al que conectar
	 * 
	 * @param userName Nombre de usuario
	 * @param pass Password
	 * @param schema Esquema
	 * @param conectar autoconectar
	 * 
	 */
	public Conexion(String userName, String pass, String schema, boolean conectar) {
		super();
		this.setUserName(userName);
		this.setPass(pass);
		this.setSchema(schema);
		
		try {
			
			conectar();
						
			
		}catch(Exception e) {
			System.out.println("no se ha podido conectar");
		}
		
	}
	
	/**
	 * Construye una nueva conexi�n pas�ndole credenciales y esquema al que conectar 
	 * autoconectando a la base de datos
	 * 
	 * @param userName Nombre de usuario
	 * @param pass Password
	 * @param schema Esquema
	 */
	public Conexion(String userName, String pass, String schema) {
		this(userName, pass, schema, true);
	}
	
	/**
	 * Devuelve un objeto {@link PreparedStatement} para su uso.
	 * 
	 * @param query Consulta a preparar
	 * 
	 * @return {@link PreparedStatement} preparado
	 * 
	 * @throws SQLException si ha habido alg�n problema a la hora de preparar la consulta
	 */
	public PreparedStatement prepareStatement(String query) throws SQLException {
		return datosDeConexion.prepareStatement(query);
	}
	
	/**
	 * Devuelve un objeto {@link PreparedStatement} para su uso en la inserci�n para que se capaz de devolver
	 * la clave autogenerada
	 * 
	 * @param query Consulta a preparar
	 * 
	 * @return {@link PreparedStatement} preparado
	 * 
	 * @throws SQLException si ha habido alg�n problema a la hora de preparar la consulta
	 */
	public PreparedStatement prepareStatmentReturnKeys(String query) throws SQLException {
		return datosDeConexion.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
	}
	
	/**
	 * Desconecta el objeto de la base de datos
	 */
	public void desconectar() {
		if(datosDeConexion != null) {
			try {
				datosDeConexion.close();
			} catch(SQLException e) {
				System.err.println("Error cerrando la conexi�n: " + e);
			}
		}
		
	}
	
	/**
	 * Conecta este objeto a la base de datos.  En caso de ya estar conectado no realiza nada m�s
	 * 
	 * @throws SQLException si ha habido un problema a la hora de verificar si la conexi�n estaba cerrada
	 * o a la hora de abrirla
	 */
	public void conectar() throws SQLException {
		if(datosDeConexion == null || datosDeConexion.isClosed()) {
			String cadena = String.format(STR_CONEXION, schema);
			datosDeConexion = DriverManager.getConnection(cadena, userName, pass);
		}
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}
	
	
}
