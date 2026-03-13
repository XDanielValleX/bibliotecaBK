package com.taller.bibliotecaBK.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taller.bibliotecaBK.models.Prestamo;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Integer> {

    // Sirve para consultar el historial de préstamos de un usuario
    List<Prestamo> findByUsuario_IdUsuario(Integer idUsuario);

    // Sirve para el panel: listar solo reservas pendientes
    List<Prestamo> findByEstado(Prestamo.EstadoPrestamo estado);

    // Sirve para la regla de negocio: contar cuántos libros activos o reservados tiene un usuario
    long countByUsuario_IdUsuarioAndEstadoIn(Integer idUsuario, List<Prestamo.EstadoPrestamo> estados);
}