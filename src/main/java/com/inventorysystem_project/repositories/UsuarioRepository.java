package com.inventorysystem_project.repositories;

import com.inventorysystem_project.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	public Usuario findByUsername(String username);
	public Usuario findByCorreo(String correo);

	//BUSCAR POR NOMBRE
	@Query("select count(u.username) from Usuario u where u.username =:username")
	public int buscarUsername(@Param("username") String nombre);
	
	
	//INSERTAR ROLES
	@Transactional
	@Modifying
	@Query(value = "insert into roles (rol, user_id) VALUES (:rol, :user_id)", nativeQuery = true)
	public void insRol(@Param("rol") String authority, @Param("user_id") Long user_id);

	/**
	 * Nueva consulta para buscar usuarios cuyo rol NO esté en la lista de excluidos.
	 * Filtra usuarios que pueden ser asignados como responsables de tickets.
	 */
	@Query("SELECT u FROM Usuario u WHERE u.rol.rol NOT IN :rolesExcluidos")
	List<Usuario> findUsuariosByRolNotIn(@Param("rolesExcluidos") List<String> rolesExcluidos);
}