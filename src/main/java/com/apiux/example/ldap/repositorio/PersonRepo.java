package com.apiux.example.ldap.repositorio;

import java.util.List;

import com.apiux.example.ldap.model.Persona;

public interface PersonRepo {

	List<String> obtenerNombres();

	List<Persona> obtenerPersonas();
	
	List<String> obtenerCorreos();

	void helloWorld();
	
	public void actualizarContrasena(Persona p);

}
