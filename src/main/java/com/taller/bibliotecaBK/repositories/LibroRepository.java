package com.taller.bibliotecaBK.repositories;

import com.taller.bibliotecaBK.models.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Integer> {
    // Spring Boot es tan inteligente que si nombras el método así,
    // él solo crea la consulta SQL: SELECT * FROM libros WHERE titulo LIKE %titulo%
    List<Libro> findByTituloContainingIgnoreCase(String titulo);
}
