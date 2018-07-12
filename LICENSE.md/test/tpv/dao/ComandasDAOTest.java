package tpv.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import tpv.model.Comanda;
import tpv.model.Producto;

public class ComandasDAOTest {

	private ComandasDAO dao = new ComandasDAO();
	private Comanda comandaInsert  = new Comanda();
	private ProductosDAO productos = new ProductosDAO();

	
	@Before
	public void setUp() throws SQLException {
		System.out.println("Iniciando test...");
		comandaInsert.setProductos(productos.findAll());
	}
	
	@Test
	public void save() throws SQLException {
		
		Conexion con = new Conexion("root", "", "tpv"); // Se crea una conexión a BBDD
		PreparedStatement stmt = con.prepareStatmentReturnKeys(dao.getConsultaInsert());
		
		
		stmt.setString(1, "admin"); // a falta de establecer el método de login y sesión
		int numFilas = stmt.executeUpdate();
		if(numFilas != 1) {
			throw new RuntimeException("Ha ocurrido un error en la inserción de la entidad");
		}else {
			// obtenemos el id de la tabla identifComanda que acabamos de añadir, que será el último por ser AUTOINCREMENT
			ResultSet rs = stmt.getGeneratedKeys();
			
//			stmt = con.prepareStatement("SELECT MAX(ID) FROM identifcomanda");
//			ResultSet rs = stmt.executeQuery();
			if(rs != null) {
				rs.next();
				long maxId = rs.getLong(1);
				
				// Podemos reutilizar la consulta que inserta productos
				PreparedStatement stmtProducto = con.prepareStatement(dao.getConsultaInsertListaComanda());
				
				
				for(Producto producto: comandaInsert.getProductos()) {
					stmtProducto.clearParameters();
					stmtProducto.setLong(1, maxId); //maxId = numero que identifica de forma única la comanda
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
				throw new RuntimeException("Ha ocurrido un error en la recuperación del ID de la comanda");
			}
			
			// El control de cierre se podría haber evitado con un try-with-resources
			
			
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
