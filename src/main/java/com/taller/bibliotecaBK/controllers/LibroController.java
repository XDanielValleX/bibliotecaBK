package com.taller.bibliotecaBK.controllers;

import com.taller.bibliotecaBK.models.Libro;
import com.taller.bibliotecaBK.repositories.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libros")
@CrossOrigin(origins = "*") // Permite que Angular se conecte sin problemas
public class LibroController {

    @Autowired
    private LibroRepository libroRepository;

    // 1. Buscar libros y consultar disponibilidad
    @GetMapping
    public List<Libro> obtenerLibros(@RequestParam(required = false) String titulo) {
        if (titulo != null && !titulo.isEmpty()) {
            return libroRepository.findByTituloContainingIgnoreCase(titulo);
        }
        return libroRepository.findAll();
    }

    // --- NUEVO: Agregar un libro (Panel Admin) ---
    @PostMapping
    public ResponseEntity<?> agregarLibro(@RequestBody Libro nuevoLibro) {
        try {
            // SOLUCIÓN: Si Angular no envía el stock disponible, lo igualamos al stock total
            if (nuevoLibro.getStockDisponible() == null) {
                nuevoLibro.setStockDisponible(nuevoLibro.getStockTotal());
            }

            Libro libroGuardado = libroRepository.save(nuevoLibro);
            return ResponseEntity.ok(libroGuardado);
        } catch (Exception e) {
            // Agregamos e.getMessage() para que si vuelve a fallar, la consola nos diga exactamente por qué
            return ResponseEntity.internalServerError().body("Error al guardar el libro: " + e.getMessage());
        }
    }

    // --- NUEVO: Eliminar un libro (Panel Admin) ---
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarLibro(@PathVariable Integer id) {
        try {
            if(libroRepository.existsById(id)) {
                libroRepository.deleteById(id);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al eliminar el libro");
        }
    }
}