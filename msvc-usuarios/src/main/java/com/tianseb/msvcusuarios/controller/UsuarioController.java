package com.tianseb.msvcusuarios.controller;

import com.tianseb.msvcusuarios.models.Usuario;
import com.tianseb.msvcusuarios.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/usuarios")
public record UsuarioController(UsuarioService usuarioService) {

    @GetMapping
    public List<Usuario> buscar() {
        return usuarioService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity buscarId(@PathVariable Long id) {
        Optional<Usuario> response = usuarioService.porId(id);

        return response.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity crear(@RequestBody Usuario usuario,
                                BindingResult result) {

        if (result.hasErrors()) {
            return validar(result);
        }

        if(!usuario.getEmail().isEmpty() &&
                usuarioService.porEmail(usuario.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("mensaje","el mail ya existe"));
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.guardar(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity editar(@Valid @RequestBody Usuario usuario,
                                     BindingResult result,
                                     @PathVariable Long id) {

        if (result.hasErrors()) {
            return validar(result);
        }

        Optional<Usuario> response = usuarioService.porId(id);

        if (response.isPresent()) {
            Usuario usuarioDb = response.get();
            if(!usuario.getEmail().isEmpty() &&
                    !usuario.getEmail().equalsIgnoreCase(usuarioDb.getEmail()) &&
                    usuarioService.porEmail(usuario.getEmail()).isPresent()) {
                return ResponseEntity.badRequest()
                        .body(Collections.singletonMap("mensaje","el mail ya existe"));
            }
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
    public ResponseEntity<HttpStatus> eliminar(@PathVariable Long id) {
        Optional<Usuario> response = usuarioService.porId(id);
        if (response.isPresent()) {
            usuarioService.eliminar(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    private static ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String,String> errores = new HashMap<>();
        result.getFieldErrors()
                .forEach(fieldError -> {
                    errores.put(fieldError.getField(), "El Campo " +
                            fieldError.getField() + " " + fieldError.getDefaultMessage());
                });
        return ResponseEntity.badRequest().body(errores);
    }
}
