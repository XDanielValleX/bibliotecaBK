package com.taller.bibliotecaBK.controllers;

import com.taller.bibliotecaBK.models.Libro;
import com.taller.bibliotecaBK.repositories.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libros")
@CrossOrigin(origins = "*") // Permite que Angular (en otro puerto) se conecte sin problemas
public class LibroController {

    @Autowired
    private LibroRepository libroRepository;

    // 1. Requisito del profe: Buscar libros y consultar disponibilidad
    // Si mandas un título, lo busca. Si no, devuelve todos.
    @GetMapping
    public List<Libro> obtenerLibros(@RequestParam(required = false) String titulo) {
        if (titulo != null && !titulo.isEmpty()) {
            return libroRepository.findByTituloContainingIgnoreCase(titulo);
        }
        return libroRepository.findAll();
    }
}