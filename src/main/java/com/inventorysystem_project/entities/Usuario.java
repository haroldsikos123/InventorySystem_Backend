package com.inventorysystem_project.entities;

import jakarta.persistence.*;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 50, nullable = true)  // Permite que 'nombre' sea nulo
	private String nombre;

	@Column(length = 50, nullable = true)  // Permite que 'apellido' sea nulo
	private String apellido;

	@Column(length = 100, nullable = true)  // Permite que 'correo' sea nulo
	private String correo;

	@Column(length = 200, nullable = true)  // Permite que 'password' sea nulo
	private String password;

	@Column(length = 50, nullable = true)  // Permite que 'username' sea nulo
	private String username;

	@Column(length = 50, nullable = true)  // Permite que 'genero' sea nulo
	private String genero;

	@Column(nullable = true)  // Permite que 'dni' sea nulo
	private Long dni;

	@Column(nullable = true)  // Permite que 'fechaNacimiento' sea nulo
	private Date fechaNacimiento;

	@Column(nullable = true)  // Permite que 'telefono' sea nulo
	private Long telefono;

	@Column(nullable = true)  // Permite que 'enabled' sea nulo
	private Boolean enabled;

	@ManyToOne
	@JoinColumn(name = "empresa_id", referencedColumnName = "id", nullable = true)  // Permite que la relación con 'empresa' sea nula
	private Empresa empresa;

	@ManyToOne(fetch = FetchType.LAZY)  // Elimina el cascade = CascadeType.ALL
	@JoinColumn(name = "rol_id", nullable = true)
	private Rol rol;
	// Getters and Setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public Long getDni() {
		return dni;
	}

	public void setDni(Long dni) {
		this.dni = dni;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public Long getTelefono() {
		return telefono;
	}

	public void setTelefono(Long telefono) {
		this.telefono = telefono;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}
}
