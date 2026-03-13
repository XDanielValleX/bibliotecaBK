package com.taller.bibliotecaBK.controllers;

import com.taller.bibliotecaBK.models.Usuario;
import com.taller.bibliotecaBK.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*") // importante para Angular
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Endpoint para que Angular valide el Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        String email = credenciales.get("email");
        String password = credenciales.get("password");

        // Buscamos si el correo existe en la base de datos
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            // Validamos que la contraseña coincida
            if (usuario.getPassword().equals(password)) {
                // Login exitoso: Devolvemos los datos del usuario a Angular
                return ResponseEntity.ok(usuario);
            } else {
                // Contraseña mal
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña incorrecta");
            }
        } else {
            // Correo no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }

    // Endpoint para Registrar nuevos usuarios (Opción 2 - Puntos Extra)
    @PostMapping("/registro")
    public ResponseEntity<?> registrarUsuario(@RequestBody Usuario nuevoUsuario) {
        // 1. Verificamos si el correo ya existe para no tener duplicados
        if (usuarioRepository.findByEmail(nuevoUsuario.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("El correo ya está registrado en el sistema");
        }

        try {
            // --- NUEVO BLINDAJE: Si el frontend no envía el rol, le ponemos ESTUDIANTE por defecto ---
            if (nuevoUsuario.getTipoUsuario() == null) {
                nuevoUsuario.setTipoUsuario(Usuario.TipoUsuario.ESTUDIANTE);
            }

            // 2. Le asignamos la fecha y hora actual de registro
            nuevoUsuario.setFechaRegistro(java.time.LocalDateTime.now());

            // 3. Guardamos el usuario en la base de datos
            Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);

            return ResponseEntity.ok(usuarioGuardado);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al registrar el usuario: " + e.getMessage());
        }
    }
}