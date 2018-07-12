package tpv.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import tpv.exceptions.IdNotFoundException;
import tpv.model.Usuario;



public class UsuariosDAOTest {
	
	private static final String USUARIO_ADMIN = "admin";
	
	private UsuariosDAO dao;
	
	private Usuario usuarioInsert = new Usuario();
	
	
	@Before
	public void setUp() {
		System.out.println("Iniciando test...");
		dao = new UsuariosDAO();
		usuarioInsert = new Usuario();
		usuarioInsert.setNombre("TEST");
		usuarioInsert.setPassword("TEST");
	}
	
	@After
	public void tearDown() {
		System.out.println("Finalizando test...");
		if(dao != null) {
			// Aquí desconectaríamos el DAO de la base de datos si existiese un método para hacerlo
			Conexion con = new Conexion("root", "", "tpv"); // Se crea una conexión a BBDD
			try {
				PreparedStatement stmt = con.prepareStatement("DELETE FROM usuarios WHERE nombre = ? ");
				stmt.setString(1, usuarioInsert.getNombre());
				stmt.executeUpdate();
			} catch(SQLException e) {
				throw new RuntimeException(e);
			}
			
			
		}
	}
	
	@Test
	public void testFindById() throws SQLException{
		UsuariosDAO dao = new UsuariosDAO();
		// Buscamos el usuario admin
		Usuario usuario = dao.findById(USUARIO_ADMIN);
		System.out.println("encontrado usuario");
		// Comprobamos que el usuario no es nulo
		assertNotNull("El usuario es nulo", usuario);
		
		// Comprobamos que el nombre es el que habíamos buscado
		assertEquals("El nombre del usuario admin no coincide", USUARIO_ADMIN, usuario.getNombre());
		
	}
	
	@SuppressWarnings("unused")
	@Test(expected = IdNotFoundException.class)
	public void testFindByIdNoExiste() throws SQLException{
		UsuariosDAO dao = new UsuariosDAO();
		// Buscamos el usuario admin
		Usuario usuario = dao.findById("meloinvento");
	}
	
	@Test
	public void testFindAll() throws SQLException {
		UsuariosDAO dao = new UsuariosDAO();
		
		// Buscamos todos los usuarios
		List<Usuario> usuarios =  dao.findAll();
		
		// La lista no es nula
		assertNotNull("La lista de usuarios es nula", usuarios);
		
		// Compruebo que la lista no está vacía
		assertTrue("La lista está vacía", !usuarios.isEmpty());
		
		// Compruebo que devuelve MÁS DE 1 ELEMENTO
		assertTrue("La lista no contiene más de un elemento", usuarios.size() > 1);
		
		// Compruebo que todos los elementos son objetos no nulos
		for(Usuario usuario: usuarios) {
			assertNotNull("Uno de los usuarios viene a null", usuario);
		}
		
		// Comprobamos que entre los usuarios devueltos está admin
		boolean existe = false;
		
		for(Iterator<Usuario> it = usuarios.iterator(); it.hasNext() && !existe; ) {
			Usuario usuario = it.next();
			existe = USUARIO_ADMIN.equalsIgnoreCase(usuario.getNombre());
		}
		
		assertTrue("El usuario admin no se encuentra en la lista", existe);
	}
	
	@Test
	public void testInsert() throws SQLException {
		UsuariosDAO dao = new UsuariosDAO();
		
		// Creamos un nuevo usuario
		dao.save(usuarioInsert);
		// Ahora consultamos
		Usuario reciente = dao.findById(usuarioInsert.getNombre());
		
		assertNotNull("No se ha encontrado el usuario insertado", reciente);
		assertTrue(usuarioInsert.getNombre().equals(reciente.getNombre()));
		assertTrue(usuarioInsert.getPassword().equals(reciente.getPassword()));

		
	}
}
