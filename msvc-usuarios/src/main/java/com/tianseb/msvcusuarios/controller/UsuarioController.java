package com.tianseb.msvcusuarios.controller;

import com.tianseb.msvcusuarios.models.entity.Usuario;
import com.tianseb.msvcusuarios.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public record UsuarioController(UsuarioService usuarioService) {

    @GetMapping
    public List<Usuario> buscar() {
        return usuarioService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarId(@PathVariable Long id) {
        Optional<Usuario> response = usuarioService.porId(id);

        if (response.isPresent()) {
            return ResponseEntity.ok(response.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario crear(@RequestBody Usuario usuario) {
        return usuarioService.guardar(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@RequestBody Usuario usuario,
                                     @PathVariable Long id) {
        Optional<Usuario> response = usuarioService.porId(id);

            if (response.isPresent()) {
            Usuario usuarioDb = response.get();
            usuarioDb.setEmail(usuario.getEmail());
            usuarioDb.setNombre(usuario.getNombre());
            usuarioDb.setPassword(usuario.getPassword());

            Usuario savedUser = usuarioService.guardar(usuarioDb);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(savedUser);
        }

        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Usuario> response = usuarioService.porId(id);
        if (response.isPresent()) {
            usuarioService.eliminar(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
