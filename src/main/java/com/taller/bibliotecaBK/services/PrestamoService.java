package com.taller.bibliotecaBK.services;

import com.taller.bibliotecaBK.models.Libro;
import com.taller.bibliotecaBK.models.Prestamo;
import com.taller.bibliotecaBK.models.Usuario;
import com.taller.bibliotecaBK.repositories.LibroRepository;
import com.taller.bibliotecaBK.repositories.PrestamoRepository;
import com.taller.bibliotecaBK.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class PrestamoService {

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LibroRepository libroRepository;

    // Método central para hacer una reserva
    @Transactional
    public Prestamo realizarReserva(Integer idUsuario, Integer idLibro) throws Exception {

        // 1. Buscamos si el usuario y el libro existen en la base de datos
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        Libro libro = libroRepository.findById(idLibro)
                .orElseThrow(() -> new Exception("Libro no encontrado"));

        // 2. REGLA DE NEGOCIO (Profesor): Verificar límite de 5 libros
        List<Prestamo.EstadoPrestamo> estadosActivos = Arrays.asList(
                Prestamo.EstadoPrestamo.RESERVADA,
                Prestamo.EstadoPrestamo.ACTIVO
        );
        long librosActuales = prestamoRepository.countByUsuario_IdUsuarioAndEstadoIn(idUsuario, estadosActivos);

        if (librosActuales >= 5) {
            throw new Exception("El usuario ya tiene el límite máximo de 5 libros reservados o prestados.");
        }

        // 3. REGLA DE NEGOCIO: Verificar que haya stock disponible
        if (libro.getStockDisponible() <= 0) {
            throw new Exception("No hay stock disponible para este libro en este momento.");
        }

        // 4. Si todo está bien, restamos 1 al stock del libro y lo guardamos
        libro.setStockDisponible(libro.getStockDisponible() - 1);
        libroRepository.save(libro);

        // 5. Creamos el nuevo registro de préstamo/reserva
        Prestamo nuevoPrestamo = new Prestamo();
        nuevoPrestamo.setUsuario(usuario);
        nuevoPrestamo.setLibro(libro);
        nuevoPrestamo.setFechaSolicitud(LocalDateTime.now());
        // Le damos 3 días para recoger/devolver el libro por defecto
        nuevoPrestamo.setFechaLimite(LocalDateTime.now().plusDays(3));
        nuevoPrestamo.setEstado(Prestamo.EstadoPrestamo.RESERVADA);

        return prestamoRepository.save(nuevoPrestamo);
    }

    // Método para consultar el historial (Requisito del profesor)
    public List<Prestamo> obtenerHistorialUsuario(Integer idUsuario) {
        return prestamoRepository.findByUsuario_IdUsuario(idUsuario);
    }
}