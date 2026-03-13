package com.taller.bibliotecaBK.controllers;

import com.taller.bibliotecaBK.models.Prestamo;
import com.taller.bibliotecaBK.services.PrestamoService;
import com.taller.bibliotecaBK.repositories.PrestamoRepository; // Importante
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/prestamos")
@CrossOrigin(origins = "*")
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    // Inyectamos el repositorio para consultas rápidas del panel
    @Autowired
    private PrestamoRepository prestamoRepository;

    // Hacer reservas
    @PostMapping("/reservar")
    public ResponseEntity<?> reservarLibro(@RequestParam Integer idUsuario, @RequestParam Integer idLibro) {
        try {
            Prestamo nuevaReserva = prestamoService.realizarReserva(idUsuario, idLibro);
            return ResponseEntity.ok(nuevaReserva);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Consultar historial
    @GetMapping("/historial/{idUsuario}")
    public List<Prestamo> verHistorial(@PathVariable Integer idUsuario) {
        return prestamoService.obtenerHistorialUsuario(idUsuario);
    }

    // --- NUEVO: Ver todas las reservas (Panel Docente/Admin) ---
    @GetMapping("/reservas")
    public List<Prestamo> obtenerTodasLasReservas() {
        return prestamoRepository.findAll();
    }

    // --- NUEVO: Aprobar una reserva (Panel Docente/Admin) ---
    @PutMapping("/aprobar/{idPrestamo}")
    public ResponseEntity<?> aprobarReserva(@PathVariable Integer idPrestamo) {
        try {
            Optional<Prestamo> prestamoOpt = prestamoRepository.findById(idPrestamo);
            if (prestamoOpt.isPresent()) {
                Prestamo prestamo = prestamoOpt.get();

                // CORREGIDO: Llamamos al Enum a través de la clase Prestamo
                prestamo.setEstado(Prestamo.EstadoPrestamo.ACTIVO);

                prestamoRepository.save(prestamo);
                return ResponseEntity.ok(prestamo);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al aprobar la reserva");
        }
    }
}