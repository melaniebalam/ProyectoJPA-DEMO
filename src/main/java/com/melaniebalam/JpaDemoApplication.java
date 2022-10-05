package com.melaniebalam;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.melaniebalam.model.Categoria;
import com.melaniebalam.model.Perfil;
import com.melaniebalam.model.Usuario;
import com.melaniebalam.model.Vacante;
import com.melaniebalam.repository.CategoriasRepository;
import com.melaniebalam.repository.PerfilesRepository;
import com.melaniebalam.repository.UsuariosRepository;
import com.melaniebalam.repository.VacantesRepository;

// todos los ejemplos de bases de datos se hacen aqui, hay que inyectar una instancia
@SpringBootApplication
public class JpaDemoApplication implements CommandLineRunner {

	@Autowired // Asi se inyecta nuestro repositorio, asi la variable repo se puede usar en cualquier metodo
	private CategoriasRepository repoCategorias;
	@Autowired
	private VacantesRepository repoVacantes;
	@Autowired
	private UsuariosRepository repoUsuarios;
	@Autowired
	private PerfilesRepository repoPerfiles;
	
	public static void main(String[] args) {
		SpringApplication.run(JpaDemoApplication.class, args);
	}

	// Este es el metodo que se ejecutara una vez iniciada la aplicacion y se terminara de ejecutar
	// Cuando haya finalizado este metodo
	@Override
	public void run(String... args) throws Exception {
		buscarVacantesVariosEstatus();
	}
	
	/*  QUERY METHOD*/
	
	// Metodo para buscar vacantes por varios estatus (in)
	private void buscarVacantesVariosEstatus() {
		String[] estatus = new String[] {"Eliminada", "Aprobada"};
		List<Vacante> lista = repoVacantes.findByEstatusIn(estatus);
		System.out.println("Registros encontrados: " + lista.size());
		for(Vacante v : lista) {
			System.out.println(v.getId() + ": " + v.getNombre()+ ": " + v.getEstatus());
		}
	}
	
	// Metodo para buscar vacantes con un rango de salario (between) y en orden descendente
	private void buscarVacantesSalario() {
		List<Vacante> lista = repoVacantes.findBySalarioBetweenOrderBySalarioDesc(7000, 14000);
		System.out.println("Registros encontrados: " + lista.size());
		for(Vacante v : lista) {
			System.out.println(v.getId() + ": " + v.getNombre()+ ": $" + v.getSalario());
		}
	}
	// Buscar Vacantes por Destacado y Estatus Ordenado por Id Desc
	private void buscarVacantesPorDestacadiEstatus() {
		List<Vacante> lista = repoVacantes.findByDestacadoAndEstatusOrderByIdDesc(1, "Aprobada");
		System.out.println("Registros encontrados: " + lista.size());
		for(Vacante v : lista) {
			System.out.println(v.getId() + ": " + v.getNombre()+ ": " + v.getEstatus()+ ": " + v.getDestacado());
		}
	}
	// Buscar vacantes por el estatus
	private void buscarVacantesPorEstatus() {
		List<Vacante> lista = repoVacantes.findByEstatus("Aprobada");
		System.out.println("Registros encontrados: " + lista.size());
		for(Vacante v : lista) {
			System.out.println(v.getId() + ": " + v.getNombre()+ ": " + v.getEstatus());
		}
	}
	/* FIN DEL QUERY METHOD*/
	
	
	
	/* VACANTES con JpaRepository*/
	
	// Metodo para buscar un usuario y desplegar sus perfiles asociados.
	public void buscarUsuario() {
		Optional<Usuario> optional = repoUsuarios.findById(1);
		if(optional.isPresent()) {
			Usuario u = optional.get();
			System.out.println("Usuario: " + u.getNombre());
			System.out.println("Perfiles asignados");
			for(Perfil p : u.getPerfiles()) {
				System.out.println(p.getPerfil());
			}
		} else {
			System.out.println("Usuario no encontrado");
		}
	}
	
	
	// Metodo para crear un usuario con  2 perfiles administrador y usuario
	private void crearUsuarioConDosPerfiles() {
		Usuario user = new Usuario();
		user.setNombre("Melanie Balam");
		user.setEmail("melanibalam29@gmail.com");
		user.setFechaRegistro(new Date()); // pone la fecha actual
		user.setUsername("Melanie");
		user.setPassword("12345");
		user.setEstatus(1);
		
		// Aqui se le asigna los perfiles
		Perfil per1 = new Perfil();
		per1.setId(2);
		
		Perfil per2 = new Perfil();
		per2.setId(3);
		
		user.agregar(per1);
		user.agregar(per2);
		
		repoUsuarios.save(user); // solo ponemos user por la configuracion que usamos en la clase USUario en @ManyToMany
		
	}
	
	
	// Metodo para crear PERFILES/ROLES en la base de datos
	private void crearPerfilesAplicacion() {
		repoPerfiles.saveAll(getPerfilesAplicacion());
	}
	
	
	// Metodo para guardar registros en la tabla de vacantes
	private void guardarVacante() {
		Vacante vacante = new Vacante();
		vacante.setNombre("Profesor de Matematicas");
		vacante.setDescripcion("Escuela primaria solicita profesor para curso de Matematicas");
		vacante.setFecha(new Date());
		vacante.setSalario(8500.0);
		vacante.setEstatus("Aprobada");
		vacante.setDestacado(0);
		vacante.setImagen("escuela.png");
		vacante.setDetalles("<h1>Los requisitos para profesor de Matematicas</h1>");
		// Se tiene que poner la categoria, pq asi esta en la estrcutura, asi le ponemos una categoria
		Categoria cat = new Categoria();
		cat.setId(9); // el numero de categoria al que pertenece la vacante
		vacante.setCategoria(cat);
		repoVacantes.save(vacante); // Asi se guardan los datos
	}
	//Metodo que busca todos los registros de vacantes y sus categorias
	private void buscarVacantes() {
		List<Vacante> lista = repoVacantes.findAll();
		for(Vacante v : lista) {
			System.out.println(v.getId()+ " " + v.getNombre()+ "->" + v.getCategoria().getNombre());
		}
	}
	
	
	
	
	
	/*Fin de la interfaz JpaRepository*/
	
	
	
	
	/*   CATEGORIAS CON JpaRepository*/
	// Metodo findAll - Interfaz JpaRepository
	private void buscarTodosJpa() {
		List<Categoria> categorias = repoCategorias.findAll(); // regresa una coleccion de tipo LIST
		for (Categoria c : categorias) {
			System.out.println(c.getId() + " " + c.getNombre());
		}
	}
	
	// Metodo deleteAllInBatch 
	// Este metodo elimina todos los registros de la tabla categoria
	// PRECAUCION
	private void borrarTodoEnBloque() {
		repoCategorias.deleteAllInBatch();
	}
	
	/* FIN DE JPA*/
	
	/* CATEGORIAS CON PagingAndSortingRepository*/
	// Metodo findAll [Ordenados por un campo]
	private void buscarTodosOrdenados() {
		//List<Categoria> categorias = repo.findAll(Sort.by("nombre")); para llamarlo por orden alfabetico
		List<Categoria> categorias = repoCategorias.findAll(Sort.by("nombre").descending()); // para llamarlo por orden alfabetico descendente
		for(Categoria c : categorias) {
			System.out.println(c.getId()+ " " + c.getNombre());
		}
	}
	
	// Metodo findAll [Con paginacion]
	private void buscarTodosPaginacion() {
		Page<Categoria> page = repoCategorias.findAll(PageRequest.of(0, 2));// el primer valor es una pagina y el segundo cuantos datos de ello
		System.out.println("Total Registros: " + page.getTotalElements()); // para saber cuantos elementos hay en la pagina
		System.out.println("Total Paginas: " + page.getTotalPages()); // para saber cuantas paginas tenemos
		for (Categoria c : page.getContent()) {
			System.out.println(c.getId()+ " " + c.getNombre());
		}
	}
	
	// Metodo findAll[Con paginacion y ordenados por atributos]
	private void buscarTodosPaginacionOrdenados() {
		//Page<Categoria> page = repo.findAll(PageRequest.of(3,2 ,Sort.by("nombre")())); Para orden albafetico
		// para ordenarlo por orden alfabetico descendente
		Page<Categoria> page = repoCategorias.findAll(PageRequest.of(3,2 ,Sort.by("nombre").descending()));// el primer valor es una pagina y el segundo cuantos datos de ello
		System.out.println("Total Registros: " + page.getTotalElements()); // para saber cuantos elementos hay en la pagina
		System.out.println("Total Paginas: " + page.getTotalPages()); // para saber cuantas paginas tenemos
		for (Categoria c : page.getContent()) {
			System.out.println(c.getId()+ " " + c.getNombre());
		}
	}
	
	/* FIN DE LA INTERFAZ PagingAndSortingRepository*/
	
	
	/*      METODOS    */
	// METODO SAVEALL - INTERFAZ CRUDREPOSITORY
	// Lo podemos usar cuando guardemos varias categorias
	private void guardarTodas() {
		List<Categoria> categorias = getListaCategorias();
		repoCategorias.saveAll(categorias);
	}
	// METODO EXISTSBYID - INTERFAZ CRUDREPOSITORY
	// Si ponemos un id que existe saldra TRUE si ponemos uno que no existe saldra FALSE
	private void existeId() {
		boolean existe = repoCategorias.existsById(2);
		System.out.println("La categoria existe: " + existe);
	}
	// METODO FINDALL - INTERFAZ CRUDREPOSITORY
	// Busca todas las categorias
	private void buscarTodos() {
		Iterable<Categoria> categorias = repoCategorias.findAll();
		for (Categoria cat : categorias) {
			System.out.println(cat);
		}
	}
	// METODO FINDALLBTID - INTERFAZ CRUDREPOSITORY
	// Busca Ids en especifico
	private void encontrarPorIds() {
		List<Integer> ids = new LinkedList<Integer>();
		ids.add(1);
		ids.add(3);
		Iterable<Categoria> categorias = repoCategorias.findAllById(ids);
		for (Categoria cat : categorias) {
			System.out.println(cat);
		}
	}
	// METODO DELETEALL - INTERFAZ CRUDREPOSITORY
	// Se recomienda usar solo cuando hay pocos registros, ya que elimina uno por uno
	private void eliminarTodos() {
		repoCategorias.deleteAll();
	}
	// METODO COUNT - INTERFAZ CRUDREPOSITORY
	private void conteo() {
		long count = repoCategorias.count();
		System.out.println("Total Categorias: " + count);
	}
	/*     FIN DE LOS METODOS   */
	
	/*     OPERACIONES CRUD    */
	// METODO DELETEBYID - INTERFAZ CRUDREPOSITORY
	// solo tenemos que poner el ID que queremos eliminar
	private void eliminar() {
		int idCategoria = 1;
		repoCategorias.deleteById(idCategoria);
	}
	// METODO SAVE(UPDATE) - INTERFAZ CRUDREPOSITORY
	// Si el metodo save ya tiene un id, diferente a null, automaticamente se hace un UPDATE, si no tiene un id se hace un INSERT 
	private void modificar(){
		Optional<Categoria> optional = repoCategorias.findById(2); // La clase optional sirve para verificar si la categoria fue encontrada o no
		// El isPresent devuelve true o false (ES UN BOOLEANO)
		if (optional.isPresent()) { // Con esta funcion verificamos si fue encontrada la categoria o no 
			Categoria catTmp = optional.get();
			catTmp.setNombre("Ing. de software");
			catTmp.setDescripcion("Desarrollo de sistemas");
			repoCategorias.save(catTmp);
			System.out.println(optional.get());
		}
		else 
			System.out.println("Categoria no encontrada");
	}
	// METODO FINDBYID - INTERFAZ CRUDREPOSITORY
	private void buscarPorId() {
		Optional<Categoria> optional = repoCategorias.findById(5); // La clase optional sirve para verificar si la categoria fue encontrada o no
		// El isPresent devuelve true o false (ES UN BOOLEANO)
		if (optional.isPresent()) // Con esta funcion verificamos si fue encontrada la categoria o no 
			System.out.println(optional.get());
		else 
			System.out.println("Categoria no encontrada"); // En dado caso que la categoria no se encuentre sale el mensaje
	}
	// METODO SAVE del CRUD
	private void guardar() {
		// De esta manera podemos ingresar un dato en la tabla de categorias.
		// con el comando "select * from Categorias" podemos ver los datos ingresados
		Categoria cat = new Categoria();
		// no se pone el ID porque ya es autoincrementable
		cat.setNombre("Finanzas");
		cat.setDescripcion("Trabajos relacionados con finanzas y contabilidad");
		repoCategorias.save(cat);
		System.out.println(cat);
	}
	/*    FIN DE LAS OPERACIONES CRUD     */
	//private void eliminar() {
	//	System.out.println("Eliminando un registro");
	//}
	
	
	/* METODO QUE REGRESA 3 CATEGORIAS*/
	private List<Categoria> getListaCategorias(){
		List<Categoria> lista = new LinkedList<Categoria>();
		// Categoria 1
		Categoria cat1 = new Categoria();
		cat1.setNombre("Programador de Blockchain");
		cat1.setDescripcion("Trabajos relacionados con Bitcoin y Criptomonedas");
		
		// Categoria 2
		Categoria cat2 = new Categoria();
		cat2.setNombre("Soldador/Pintura");
		cat2.setDescripcion("Trabajos relacionados con pintura");
		
		// Categoria 3
		Categoria cat3 = new Categoria();
		cat3.setNombre("Ingeniero Industrial");
		cat3.setDescripcion("Trabajos relacionados con la ingenieria industrial");
		
		lista.add(cat1);
		lista.add(cat2);
		lista.add(cat3);
		return lista;
	}
	
	/*Metodo que regresa una lista de objetos de tipo Perfil que representa diferentes perfiles*/
	private List<Perfil> getPerfilesAplicacion(){
		List<Perfil> lista = new LinkedList<Perfil>();
		Perfil per1 = new Perfil();
		per1.setPerfil("SUPERVISOR");
		
		Perfil per2 = new Perfil();
		per2.setPerfil("ADMINISTRADOR");
		
		Perfil per3 = new Perfil();
		per3.setPerfil("USUARIO");
		
		lista.add(per1);
		lista.add(per2);
		lista.add(per3);
		return lista;
	}

}
