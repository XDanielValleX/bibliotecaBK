package com.taller.bibliotecaBK.repositories;

import com.taller.bibliotecaBK.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    // Busca un usuario por su correo electrónico
    Optional<Usuario> findByEmail(String email);
}