package com.melaniebalam.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.melaniebalam.model.Usuario;

public interface UsuariosRepository extends JpaRepository<Usuario, Integer> {

}
