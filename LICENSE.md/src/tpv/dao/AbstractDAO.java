package tpv.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 *
 * Clase abstracta que implementa la interfaz {@link DAOInterface} desarrollando sus métodos parcialmente
 * Se generan tres métodos abstractos que deberán ser implementados por las clases que extiendan esta,
 * completando así la implementacion de los métodos heredados de la interfaz
 * 
 * @param <T> Tipo con la que opera el DAO (Producto, Usuario, Comanda...)
 * @param <K> Tipo de la Clave Primaria (String en caso de Usuario, Long en caso de Producto, etc)
 * @author miguel.aguirre
 */
public abstract class AbstractDAO<T, K> implements DAOInterface<T, K> {

	
	/**
	 *  Método que recibe un objeto id (válido para el tipo K) mediante el cual devolverá
	 *  un resultado de BBDD convertido en una entidad del tipo T que utiliza este método
	 *  
	 * @param <K> Tipo de la clave primaria
	 * @return <T> Tipo de la entidad
	 */
	@Override
	public T findById(K id) throws SQLException {
		Conexion con = new Conexion("root", "", "tpv"); // Se crea una conexión a BBDD
		PreparedStatement stmt = con.prepareStatement(getConsultaFindById()); // llamada al método que devuelve la consulta preparada en una constante privada
		//stmt.setLong(1, id);
		stmt.setObject(1, id); // llamada al método que establece el valor de los parámetros establecidos en la consulta preparada
		
		ResultSet rs = stmt.executeQuery(); // Ejecución de la query que devuelve el ResultSet
		T entidad = null;
		if(rs.next()) {
			entidad = buildEntity(rs); // condicional que construirá la entidad en función de quien implemente este método que no se ejecutará si ResultSet no tiene resultados
		}
		con.desconectar(); // Se desconecta de la BBDD
		
		return entidad;
	}
	
	/**
	 *  Metodo que se encarga de recuperar todos los registros de BBDD correspondientes a la entidad T. 
	 *  
	 * 
	 * @return List<T> List de objetos del tipo de la entidad
	 */

	@Override
	public List<T> findAll() throws SQLException {
		Conexion con = new Conexion("root", "", "tpv"); // Se crea una conexión a BBDD
		PreparedStatement stmt = con.prepareStatement(getConsultaFindAll()); // llamada al método que devuelve la consulta preparada en una constante privada
				
		ResultSet rs = stmt.executeQuery(); // Ejecución de la query que devuelve el ResultSet
		List<T> lista = new ArrayList<>();
		while(rs.next()) {
			T entidad = buildEntity(rs); // bucle que construirá la entidad en función de quien implemente este método añadiendo el 
			lista.add(entidad);          // nuevo objeto a la lista y que se ejecutará mientras ResultSet tiene resultados
		}
		con.desconectar(); // Se desconecta de la BBDD
		
		return lista;
	}
	
	/**
	 * Método que se encarga de persistir una entidad de Base de datos
	 * 
	 * @param <T> Entidad que se va a persistir en base de datos 
	 * @param String subtotal calculado
	 * @param String impuestos calculado
	 * @param String total calculado
	 */
	@SuppressWarnings("unused")
	@Override
	public void save(T entity, String subtotal, String impuestos, String total, Double tipoImpuesto) throws SQLException {
		Conexion con = new Conexion("root", "", "tpv"); // Se crea una conexión a BBDD
		PreparedStatement stmt = con.prepareStatmentReturnKeys(getConsultaInsert()); // llamada al método que devuelve la consulta preparada en una constante privada
		setInsertParams(stmt, entity);
		
		int numFilas = stmt.executeUpdate(); // Ejecuta la query y devuelve el numero de filas afectadas
		if(numFilas != 1) {
			throw new RuntimeException("Ha ocurrido un error en la inserción de la entidad");
		}
		// Obtenemos la clave generada por el AUTONUM
		ResultSet rs = stmt.getGeneratedKeys();
		
	}

	@SuppressWarnings("unused")
	@Override
	public void save(T entity) throws SQLException {
		Conexion con = new Conexion("root", "", "tpv"); // Se crea una conexión a BBDD
		PreparedStatement stmt = con.prepareStatmentReturnKeys(getConsultaInsert()); // llamada al método que devuelve la consulta preparada en una constante privada
		setInsertParams(stmt, entity);
		
		int numFilas = stmt.executeUpdate(); // Ejecuta la query y devuelve el numero de filas afectadas
		if(numFilas != 1) {
			throw new RuntimeException("Ha ocurrido un error en la inserción de la entidad");
		}
		// Obtenemos la clave generada por el AUTONUM
		ResultSet rs = stmt.getGeneratedKeys();
		
	}
	
	
	/**
	 * 
	 * @return Devuelve la consulta SQL de búqueda por ID
	 */
	protected abstract String getConsultaFindById();
	
	/**
	 * 
	 * @return Devuelve la consulta SQL de búqueda de todos los elementos
	 */
	protected abstract String getConsultaFindAll();
	
	/**
	 * 
	 * @return Devuelve la sentencia de inserción de un nuevo elemento
	 */
	protected abstract String getConsultaInsert();
	
	/**
	 * Construye una entidad de tipo T a partir de un {@link ResultSet} situado en la fila adecuada.
	 * El rs.next() ha de hacerse <b>previamente</b> a la llamada de este método
	 * 
	 * @param rs ResultSet con la fila que se convertirá en una entidad
	 * 
	 * @return instancia de la entidad construida a partir del {@link ResultSet}
	 * 
	 * @throws SQLException en caso de que haya un problema de consulta en el {@link ResultSet}
	 */
	protected abstract T buildEntity(ResultSet rs) throws SQLException;
	
	/**
	 * Establece los parámetros de la consulta de INSERT de una entidad sobre el {@link PreparedStatement} 
	 * que se utiliza para persistirla
	 * 
	 * @param stmt {@link PreparedStatement} 
	 * @param entity entidad
	 */
	protected abstract void setInsertParams(PreparedStatement stmt, T entity) throws SQLException;
	
}
