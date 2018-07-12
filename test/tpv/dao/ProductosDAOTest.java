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
	
	@After //Método que cierra el test borrando todos los registros de la tabla productos cuyo nombre sea TEST, por si alguna vez se ejecutó sin llegar a hacerlo 
	public void tearDown() {
		System.out.println("Finalizando test...");
		if(dao != null) {
			Conexion con = new Conexion("root", "", "tpv"); // Se crea una conexión a BBDD
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
		
		// Comprobamos que el producto es el que habíamos buscado
		assertTrue(producto.getNombre().equals("Café"));
			
	}

	
	@SuppressWarnings("unused")
	@Test
	public void testFindAll() throws SQLException {
		ProductosDAO dao = new ProductosDAO();
		
		// Buscamos todos los usuarios
		List<Producto> productos =  dao.findAll();
		
		// La lista no es nula
		assertNotNull("La lista de usuarios es nula", productos);
		
		// Compruebo que la lista no está vacía
		assertTrue("La lista está vacía", !productos.isEmpty());
		
		// Compruebo que devuelve MÁS DE 1 ELEMENTO
		assertTrue("La lista no contiene más de un elemento", productos.size() > 1);
		
		// Compruebo que todos los elementos son objetos no nulos
		for(Producto producto: productos) {
			assertNotNull("Uno de los usuarios viene a null", productos);
		}
		
		// Comprobamos que entre los usuarios devueltos está admin
		boolean existe = false;
		
		for(Iterator<Producto> it = productos.iterator(); it.hasNext() && !existe; ) {
			Producto prod = it.next();
			existe = "Café".equalsIgnoreCase(prod.getNombre());
		}
		
		assertTrue("El usuario admin no se encuentra en la lista", existe);
	}
	
	@Test
	public void testInsert() throws SQLException {
	
		// Creamos un nuevo usuario
		dao.save(productoInsert);
		
		// comprobamos fila insertada
		Conexion con = new Conexion("root", "", "tpv"); // Se crea una conexión a BBDD
		PreparedStatement stmt = con.prepareStatement("SELECT MAX(ID) FROM productos"); // Se consulta cuál es el último ID añadido para conocer su nombre y poder confirmar la inserción
				
		ResultSet rs = stmt.executeQuery(); // Ejecución de la query que devuelve el ResultSet
		rs.next();
		int id = rs.getInt(1);
		
		// Ahora consultamos
		Producto reciente = dao.findById((long) id);
		
		assertNotNull("No se ha encontrado el usuario insertado", reciente);
		assertTrue(productoInsert.getNombre().equals(reciente.getNombre()));
		assertTrue(productoInsert.getPrecio().equals(reciente.getPrecio()));
		
	}
	
	
}
