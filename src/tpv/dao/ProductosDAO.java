package tpv.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import tpv.model.Producto;

/**
 * DAO de acceso a objetos de tipo {@link Producto} que se corresponden con la tabla productos
 * 
 * @author miguel.aguirre
 *
 */
public class ProductosDAO extends AbstractDAO<Producto, Long>{
	
	private static final String FIND_PRODUCTO_BY_ID = "SELECT * FROM Productos WHERE id = ?"; 
	private static final String FIND_ALL_PRODUCTOS = "SELECT * FROM Productos";
	private static final String INSERT_PRODUCTO = "INSERT INTO Productos(nombre, precio) VALUES (?, ?)";
	
	/* (non-Javadoc)
	 * @see tpv.dao.AbstractDAO#getConsultaFindById()
	 */
	@Override
	protected String getConsultaFindById() {
		return FIND_PRODUCTO_BY_ID;
	}
	
	/* (non-Javadoc)
	 * @see tpv.dao.AbstractDAO#getConsultaFindAll()
	 */
	@Override
	protected String getConsultaFindAll() {
		return FIND_ALL_PRODUCTOS;
	}
	
	/* (non-Javadoc)
	 * @see tpv.dao.AbstractDAO#getConsultaInsert()
	 */
	@Override
	protected String getConsultaInsert() {
		return INSERT_PRODUCTO;
	}
	
	
	
	/* (non-Javadoc)
	 * @see tpv.dao.AbstractDAO#buildEntity(java.sql.ResultSet)
	 */
	@Override
	protected Producto buildEntity(ResultSet rs) throws SQLException {
		Producto producto =  new Producto();
		producto.setId(rs.getLong("id"));
		producto.setNombre(rs.getString("nombre"));
		producto.setPrecio(rs.getDouble("precio"));
	
		
		return producto;
	}


	/* (non-Javadoc)
	 * @see tpv.dao.AbstractDAO#setInsertParams(java.sql.PreparedStatement, java.lang.Object)
	 */
	@Override
	protected void setInsertParams(PreparedStatement stmt, Producto entity) throws SQLException {
		stmt.setString(1, entity.getNombre());
		stmt.setDouble(2, entity.getPrecio());
	}

	
	
	
}
