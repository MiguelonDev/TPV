package tpv.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 *
 * Clase abstracta que implementa la interfaz {@link DAOInterface} desarrollando sus m�todos parcialmente
 * Se generan tres m�todos abstractos que deber�n ser implementados por las clases que extiendan esta,
 * completando as� la implementacion de los m�todos heredados de la interfaz
 * 
 * @param <T> Tipo con la que opera el DAO (Producto, Usuario, Comanda...)
 * @param <K> Tipo de la Clave Primaria (String en caso de Usuario, Long en caso de Producto, etc)
 * @author miguel.aguirre
 */
public abstract class AbstractDAO<T, K> implements DAOInterface<T, K> {

	
	/**
	 *  M�todo que recibe un objeto id (v�lido para el tipo K) mediante el cual devolver�
	 *  un resultado de BBDD convertido en una entidad del tipo T que utiliza este m�todo
	 *  
	 * @param <K> Tipo de la clave primaria
	 * @return <T> Tipo de la entidad
	 */
	@Override
	public T findById(K id) throws SQLException {
		Conexion con = new Conexion("root", "", "tpv"); // Se crea una conexi�n a BBDD
		PreparedStatement stmt = con.prepareStatement(getConsultaFindById()); // llamada al m�todo que devuelve la consulta preparada en una constante privada
		//stmt.setLong(1, id);
		stmt.setObject(1, id); // llamada al m�todo que establece el valor de los par�metros establecidos en la consulta preparada
		
		ResultSet rs = stmt.executeQuery(); // Ejecuci�n de la query que devuelve el ResultSet
		T entidad = null;
		if(rs.next()) {
			entidad = buildEntity(rs); // condicional que construir� la entidad en funci�n de quien implemente este m�todo que no se ejecutar� si ResultSet no tiene resultados
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
		Conexion con = new Conexion("root", "", "tpv"); // Se crea una conexi�n a BBDD
		PreparedStatement stmt = con.prepareStatement(getConsultaFindAll()); // llamada al m�todo que devuelve la consulta preparada en una constante privada
				
		ResultSet rs = stmt.executeQuery(); // Ejecuci�n de la query que devuelve el ResultSet
		List<T> lista = new ArrayList<>();
		while(rs.next()) {
			T entidad = buildEntity(rs); // bucle que construir� la entidad en funci�n de quien implemente este m�todo a�adiendo el 
			lista.add(entidad);          // nuevo objeto a la lista y que se ejecutar� mientras ResultSet tiene resultados
		}
		con.desconectar(); // Se desconecta de la BBDD
		
		return lista;
	}
	
	/**
	 * M�todo que se encarga de persistir una entidad de Base de datos
	 * 
	 * @param <T> Entidad que se va a persistir en base de datos 
	 * @param String subtotal calculado
	 * @param String impuestos calculado
	 * @param String total calculado
	 */
	@SuppressWarnings("unused")
	@Override
	public void save(T entity, String subtotal, String impuestos, String total, Double tipoImpuesto) throws SQLException {
		Conexion con = new Conexion("root", "", "tpv"); // Se crea una conexi�n a BBDD
		PreparedStatement stmt = con.prepareStatmentReturnKeys(getConsultaInsert()); // llamada al m�todo que devuelve la consulta preparada en una constante privada
		setInsertParams(stmt, entity);
		
		int numFilas = stmt.executeUpdate(); // Ejecuta la query y devuelve el numero de filas afectadas
		if(numFilas != 1) {
			throw new RuntimeException("Ha ocurrido un error en la inserci�n de la entidad");
		}
		// Obtenemos la clave generada por el AUTONUM
		ResultSet rs = stmt.getGeneratedKeys();
		
	}

	@SuppressWarnings("unused")
	@Override
	public void save(T entity) throws SQLException {
		Conexion con = new Conexion("root", "", "tpv"); // Se crea una conexi�n a BBDD
		PreparedStatement stmt = con.prepareStatmentReturnKeys(getConsultaInsert()); // llamada al m�todo que devuelve la consulta preparada en una constante privada
		setInsertParams(stmt, entity);
		
		int numFilas = stmt.executeUpdate(); // Ejecuta la query y devuelve el numero de filas afectadas
		if(numFilas != 1) {
			throw new RuntimeException("Ha ocurrido un error en la inserci�n de la entidad");
		}
		// Obtenemos la clave generada por el AUTONUM
		ResultSet rs = stmt.getGeneratedKeys();
		
	}
	
	
	/**
	 * 
	 * @return Devuelve la consulta SQL de b�queda por ID
	 */
	protected abstract String getConsultaFindById();
	
	/**
	 * 
	 * @return Devuelve la consulta SQL de b�queda de todos los elementos
	 */
	protected abstract String getConsultaFindAll();
	
	/**
	 * 
	 * @return Devuelve la sentencia de inserci�n de un nuevo elemento
	 */
	protected abstract String getConsultaInsert();
	
	/**
	 * Construye una entidad de tipo T a partir de un {@link ResultSet} situado en la fila adecuada.
	 * El rs.next() ha de hacerse <b>previamente</b> a la llamada de este m�todo
	 * 
	 * @param rs ResultSet con la fila que se convertir� en una entidad
	 * 
	 * @return instancia de la entidad construida a partir del {@link ResultSet}
	 * 
	 * @throws SQLException en caso de que haya un problema de consulta en el {@link ResultSet}
	 */
	protected abstract T buildEntity(ResultSet rs) throws SQLException;
	
	/**
	 * Establece los par�metros de la consulta de INSERT de una entidad sobre el {@link PreparedStatement} 
	 * que se utiliza para persistirla
	 * 
	 * @param stmt {@link PreparedStatement} 
	 * @param entity entidad
	 */
	protected abstract void setInsertParams(PreparedStatement stmt, T entity) throws SQLException;
	
}
