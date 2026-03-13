package com.taller.bibliotecaBK.repositories;

import com.taller.bibliotecaBK.models.Prestamo;
import com.taller.bibliotecaBK.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Integer> {

    // Sirve para consultar el historial de préstamos de un usuario
    List<Prestamo> findByUsuario_IdUsuario(Integer idUsuario);

    // Sirve para la regla de negocio: contar cuántos libros activos o reservados tiene un usuario
    long countByUsuario_IdUsuarioAndEstadoIn(Integer idUsuario, List<Prestamo.EstadoPrestamo> estados);
}