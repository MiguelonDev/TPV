package tpv.dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz genérica con los métodos comunes necesarios
 * 
 * @author miguel.aguirre
 *
 * @param <T>
 * @param <K>
 */
public interface DAOInterface<T, K> {
	
	public T findById(K id) throws SQLException;
	public List<T> findAll() throws SQLException;
	public void save(T entity, String subtotal, String impuestos, String total, Double tipoImpuesto) throws SQLException;
	public void save(T entity) throws SQLException;

}
