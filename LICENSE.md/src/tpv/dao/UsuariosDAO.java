package tpv.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import tpv.exceptions.IdNotFoundException;
import tpv.model.Usuario;

/**
 * DAO de acceso a los objetos de tipo {@link Usuario} que se corresponden con la tabla usuarios
 * 
 * @author miguel.aguirre
 *
 */
public class UsuariosDAO extends AbstractDAO<Usuario, String>{
	
	private static final String FIND_USUARIO_POR_NOMBRE = "SELECT * FROM USUARIOS WHERE NOMBRE = ?";
	private static final String FIND_ALL_USUARIOS = "SELECT * FROM USUARIOS";
	
	private static final String INSERT_USUARIO = "INSERT INTO usuarios (nombre, password) VALUES (?, ?)";
		
	

	/*
	 * (non-Javadoc)
	 * @see tpv.dao.AbstractDAO#getConsultaFindById()
	 */
	@Override
	protected String getConsultaFindById() {
		return FIND_USUARIO_POR_NOMBRE;
	}

	/*
	 * (non-Javadoc)
	 * @see tpv.dao.AbstractDAO#getConsultaFindByAll()
	 */
	@Override
	protected String getConsultaFindAll() {
		return FIND_ALL_USUARIOS;
	}
	
	/* (non-Javadoc)
	 * @see tpv.dao.AbstractDAO#getConsultaInsert()
	 */
	@Override
	protected String getConsultaInsert() {
		return INSERT_USUARIO;
	}

	/*
	 * (non-Javadoc)
	 * @see tpv.dao.AbstractDAO#buildEntity(java.sql.ResultSet)
	 */
	@Override
	protected Usuario buildEntity(ResultSet rs) throws SQLException {
		Usuario usuario = null;
		
		usuario = new Usuario();
		usuario.setNombre(rs.getString("nombre"));
		usuario.setPassword(rs.getString("password"));
		
		
		return usuario;
	}

	/* (non-Javadoc)
	 * @see tpv.dao.AbstractDAO#findById(java.lang.Object)
	 */
	@Override
	public Usuario findById(String id) throws SQLException {
		Usuario usuario = super.findById(id);
		if(usuario == null) {
			throw new IdNotFoundException("No se ha encontrado el Usuario" );
		}
		
		return usuario;
		
	}

	

	/* (non-Javadoc)
	 * @see tpv.dao.AbstractDAO#setInsertParams(java.sql.PreparedStatement, java.lang.Object)
	 */
	@Override
	protected void setInsertParams(PreparedStatement stmt, Usuario entity) throws SQLException {
		stmt.setString(1, entity.getNombre());
		stmt.setString(2, entity.getPassword());
		
	}
	

	
	

}
