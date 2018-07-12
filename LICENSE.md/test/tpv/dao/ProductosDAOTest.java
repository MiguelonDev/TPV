package tpv.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import tpv.model.Producto;

public class ProductosDAOTest {
	
	private static final Long PRIMER_PRODUCTO = 1L;
	private ProductosDAO dao;
	private Producto productoInsert;
	
	
	@Before
	public void setUp() {
		System.out.println("Iniciando test...");
		dao = new ProductosDAO();
		productoInsert = new Producto();
		productoInsert.setNombre("TEST");
		productoInsert.setPrecio(5.0);
	}
	
	@After //M�todo que cierra el test borrando todos los registros de la tabla productos cuyo nombre sea TEST, por si alguna vez se ejecut� sin llegar a hacerlo 
	public void tearDown() {
		System.out.println("Finalizando test...");
		if(dao != null) {
			Conexion con = new Conexion("root", "", "tpv"); // Se crea una conexi�n a BBDD
			try {
				PreparedStatement stmt = con.prepareStatement("DELETE FROM productos WHERE nombre = ? ");
				stmt.setString(1, productoInsert.getNombre());
				stmt.executeUpdate();
			} catch(SQLException e) {
				throw new RuntimeException(e);
			}
			
			
		}
	}
	
	
	@Test
	public void testFindById() throws SQLException{
		ProductosDAO dao = new ProductosDAO();
		// Buscamos el primer producto de la tabla
		Producto producto = dao.findById(PRIMER_PRODUCTO);
		System.out.println("Buscado por id ");
		// Comprobamos que el usuario no es nulo
		assertNotNull("El usuario es nulo", producto);
		
		// Comprobamos que el producto es el que hab�amos buscado
		assertTrue(producto.getNombre().equals("Caf�"));
			
	}

	
	@SuppressWarnings("unused")
	@Test
	public void testFindAll() throws SQLException {
		ProductosDAO dao = new ProductosDAO();
		
		// Buscamos todos los usuarios
		List<Producto> productos =  dao.findAll();
		
		// La lista no es nula
		assertNotNull("La lista de usuarios es nula", productos);
		
		// Compruebo que la lista no est� vac�a
		assertTrue("La lista est� vac�a", !productos.isEmpty());
		
		// Compruebo que devuelve M�S DE 1 ELEMENTO
		assertTrue("La lista no contiene m�s de un elemento", productos.size() > 1);
		
		// Compruebo que todos los elementos son objetos no nulos
		for(Producto producto: productos) {
			assertNotNull("Uno de los usuarios viene a null", productos);
		}
		
		// Comprobamos que entre los usuarios devueltos est� admin
		boolean existe = false;
		
		for(Iterator<Producto> it = productos.iterator(); it.hasNext() && !existe; ) {
			Producto prod = it.next();
			existe = "Caf�".equalsIgnoreCase(prod.getNombre());
		}
		
		assertTrue("El usuario admin no se encuentra en la lista", existe);
	}
	
	@Test
	public void testInsert() throws SQLException {
	
		// Creamos un nuevo usuario
		dao.save(productoInsert);
		
		// comprobamos fila insertada
		Conexion con = new Conexion("root", "", "tpv"); // Se crea una conexi�n a BBDD
		PreparedStatement stmt = con.prepareStatement("SELECT MAX(ID) FROM productos"); // Se consulta cu�l es el �ltimo ID a�adido para conocer su nombre y poder confirmar la inserci�n
				
		ResultSet rs = stmt.executeQuery(); // Ejecuci�n de la query que devuelve el ResultSet
		rs.next();
		int id = rs.getInt(1);
		
		// Ahora consultamos
		Producto reciente = dao.findById((long) id);
		
		assertNotNull("No se ha encontrado el usuario insertado", reciente);
		assertTrue(productoInsert.getNombre().equals(reciente.getNombre()));
		assertTrue(productoInsert.getPrecio().equals(reciente.getPrecio()));
		
	}
	
	
}
