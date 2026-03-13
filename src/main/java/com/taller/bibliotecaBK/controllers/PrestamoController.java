package com.taller.bibliotecaBK.controllers;

import com.taller.bibliotecaBK.models.Prestamo;
import com.taller.bibliotecaBK.services.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
@CrossOrigin(origins = "*")
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    // 2. Requisito del profe: Hacer reservas
    @PostMapping("/reservar")
    public ResponseEntity<?> reservarLibro(@RequestParam Integer idUsuario, @RequestParam Integer idLibro) {
        try {
            // Llamamos a la regla de negocio que creaste
            Prestamo nuevaReserva = prestamoService.realizarReserva(idUsuario, idLibro);
            return ResponseEntity.ok(nuevaReserva);
        } catch (Exception e) {
            // Si tiene más de 5 libros o no hay stock, devolvemos el error al Frontend
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 3. Requisito del profe: Consultar historial de préstamos
    @GetMapping("/historial/{idUsuario}")
    public List<Prestamo> verHistorial(@PathVariable Integer idUsuario) {
        return prestamoService.obtenerHistorialUsuario(idUsuario);
    }
}