package com.melaniebalam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.melaniebalam.model.Vacante;
// Asi ya esta listo el repositorio
public interface VacantesRepository extends JpaRepository<Vacante, Integer> {
	List<Vacante> findByEstatus(String estatus); // el findby inicia con el nomnbre del metodo

	List<Vacante> findByDestacadoAndEstatusOrderByIdDesc(int destacado, String estatus);

	List <Vacante> findBySalarioBetweenOrderBySalarioDesc(double s1, double s2); // metodo para buscar un salario definido en cierto rango y que vaya en orden descendente 

	List<Vacante> findByEstatusIn(String[] estatus);
}
