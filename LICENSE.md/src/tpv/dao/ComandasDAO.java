package tpv.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import tpv.model.Comanda;
import tpv.model.Producto;

/**
 * DAO de acceso a los objetos de tipo {@link Comanda} que se corresponden con las tablas identifcomanda y productoscomanda
 *  
 * @author miguel.aguirre
 *
 */
public class ComandasDAO extends AbstractDAO<Comanda, Long>{
	
	private static final String FIND_ALL_COMANDAS = "";
	private static final String FIND_COMANDA_BY_ID = "SELECT * FROM productoscomanda WHERE id = ?";
	private static final String INSERT_COMANDA = "INSERT INTO identifcomanda (fecha, usuario, subtotal, impuestos, total, tasa) VALUES (now(), ?, ?, ?, ?, ?)";
	private static final String INSERT_PRODUCTOS_COMANDA = "INSERT INTO productoscomanda (comanda_id, producto_nombre, producto_precio) VALUES (?, ?, ?)";

	/* (non-Javadoc)
	 * @see tpv.dao.AbstractDAO#getConsultaFindById()
	 */
	@Override
	protected String getConsultaFindById() {
		return FIND_COMANDA_BY_ID;
	}

	/* (non-Javadoc)
	 * @see tpv.dao.AbstractDAO#getConsultaFindAll()
	 */
	@Override
	protected String getConsultaFindAll() {
		return FIND_ALL_COMANDAS;
	}
	
	/* (non-Javadoc)
	 * @see tpv.dao.AbstractDAO#getConsultaInsert()
	 */
	@Override
	protected String getConsultaInsert() {
		return INSERT_COMANDA;
	}
	
	/**
	 * @return sentencia sql de insercion preparada para recibir 3 par�metros
	 */
	protected String getConsultaInsertListaComanda() {
		return INSERT_PRODUCTOS_COMANDA;
	}

	/* (non-Javadoc)
	 * @see tpv.dao.AbstractDAO#buildEntity(java.sql.ResultSet)
	 */
	@Override
	protected Comanda buildEntity(ResultSet rs) throws SQLException {
		Comanda comanda = new Comanda();
		comanda.setId(rs.getLong("id"));
		do {
			Producto producto = new Producto();
			producto.setId(rs.getLong("producto_id"));
			producto.setNombre(rs.getString("producto_nombre"));
			producto.setPrecio(rs.getDouble("producto_precio"));
		} while (rs.next() && comanda.getId().equals(rs.getLong("id")));
		
		
		return comanda;
	}

	
	/**
	 * Se deja vac�o porque ya no se utiliza al haber sobrescrito el m�todo save completo de {@link AbstractDAO}
	 */
	@Override
	protected void setInsertParams(PreparedStatement stmt, Comanda entity) throws SQLException {
		
	}

	/**
	 * Se sobrescribe el m�todo original del interface para poder hacer N inserciones y no s�lo una.
	 */
	@Override
	public void save(Comanda entity, String subtotal, String impuestos, String total, Double tipoImpuesto) throws SQLException {
		Conexion con = new Conexion("root", "", "tpv"); // Se crea una conexi�n a BBDD
		PreparedStatement stmt = con.prepareStatmentReturnKeys(getConsultaInsert());
		
		
		stmt.setString(1, "admin"); // a falta de establecer el m�todo de login y sesi�n
		stmt.setDouble(2, Double.parseDouble(subtotal.replaceAll(",", ".")));
		stmt.setDouble(3, Double.parseDouble(impuestos.replaceAll(",", ".")));
		stmt.setDouble(4, Double.parseDouble(total.replaceAll(",", ".")));
		stmt.setDouble(5, tipoImpuesto);
		int numFilas = stmt.executeUpdate();
		if(numFilas != 1) {
			throw new RuntimeException("Ha ocurrido un error en la inserci�n de la entidad");
		}else {
			// obtenemos el id de la tabla identifComanda que acabamos de a�adir, que ser� el �ltimo por ser AUTOINCREMENT
			ResultSet rs = stmt.getGeneratedKeys();
			
//			stmt = con.prepareStatement("SELECT MAX(ID) FROM identifcomanda");
//			ResultSet rs = stmt.executeQuery();
			if(rs != null) {
				rs.next();
				long maxId = rs.getLong(1);
				
				// Podemos reutilizar la consulta que inserta productos
				PreparedStatement stmtProducto = con.prepareStatement(INSERT_PRODUCTOS_COMANDA);
				
				for(Producto producto: entity.getProductos()) {
					
					//stmt = con.prepareStatement(getConsultaInsertListaComanda()); // llamada al m�todo que devuelve la consulta preparada en una constante privada
					stmtProducto.clearParameters();
					stmtProducto.setLong(1, maxId); //maxId = numero que identifica de forma �nica la comanda
					stmtProducto.setString(2, producto.getNombre());
					stmtProducto.setDouble(3, producto.getPrecio());
					stmtProducto.executeUpdate();
					
				}
			
				if(stmtProducto != null) {
					try {
						stmtProducto.close();
					} catch(SQLException e) {
						System.err.println("Error cerrando statement");
					}
					
				}
				
			} else {
				throw new RuntimeException("Ha ocurrido un error en la recuperaci�n del ID de la comanda");
			}
			
			// El control de cierre se podr�a haber evitado con un try-with-resources
			
			
			if(rs != null) {
				try {
					rs.close();
				} catch(SQLException e) {
					System.err.println("Error cerrando resultset");
				}
			}
			
			if(stmt != null) {
				try {
					stmt.close();
				} catch(SQLException e) {
					System.err.println("Error cerrando statement");
				}
				
			}
			
		}
		
	}
	
}
