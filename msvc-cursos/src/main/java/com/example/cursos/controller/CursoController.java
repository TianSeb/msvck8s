package com.example.cursos.controller;

import com.example.cursos.model.Usuario;
import com.example.cursos.model.entity.Curso;
import com.example.cursos.service.CursoService;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;

@RestController
public record CursoController(CursoService cursoService) {

    @GetMapping
    public ResponseEntity<List<Curso>> buscar() {
        return ResponseEntity.ok(cursoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends Curso> buscarId(@PathVariable Long id) {
        Optional<Curso> curso = cursoService.porIdConUsuarios(id);//cursoService.porId(id);
        return curso.<ResponseEntity<? extends Curso>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Curso curso,
                                   BindingResult result) {
        try {
            return result.hasErrors() ? validar(result)
                    : ResponseEntity.status(HttpStatus.CREATED).body(cursoService.guardar(curso));
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Curso curso,
                                                  BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return validar(result);
        }

        Optional<Curso> response = cursoService.porId(id);

        if (response.isPresent()) {
            Curso cursoDb = response.get();
            cursoDb.setNombre(curso.getNombre());
            cursoDb.setUsuarios(curso.getUsuarios());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(cursoService.guardar(cursoDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> eliminar(@PathVariable Long id) {
        Optional<Curso> response = cursoService.porId(id);
        if (response.isPresent()) {
            cursoService.eliminarCurso(response.get().getId());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/asignar-usuario/{cursoId}")
    public ResponseEntity<?> asignarUsuario(@RequestBody Usuario usuario,
                                            @PathVariable Long cursoId) {
        Optional<Usuario> o;
        try {
            o = cursoService.asignarUsuario(usuario, cursoId);
        } catch(FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("msg", e.getMessage()));
        }
        if(o.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/crear-usuario/{cursoId}")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario,
                                            @PathVariable Long cursoId) {
        Optional<Usuario> o;
        try {
            o = cursoService.crearUsuario(usuario, cursoId);
        } catch(FeignException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("msg", e.getMessage()));
        }
        if(o.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-usuario/{cursoId}")
    public ResponseEntity<?> eliminarUsuario(@RequestBody Usuario usuario,
                                          @PathVariable Long cursoId) {
        Optional<Usuario> o;
        try {
            o = cursoService.eliminarUsuario(usuario, cursoId);
        } catch(FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("msg", e.getMessage()));
        }
        if(o.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-curso-usuario/{id}")
    public ResponseEntity<?> eliminarCursoUsuarioPorId(@PathVariable Long id) {
        cursoService.eliminarCursoUsuarioPorId(id);
        return ResponseEntity.noContent().build();
    }

    private static ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String,String> errores = new HashMap<>();
        result.getFieldErrors()
                .forEach(fieldError -> errores.put(fieldError.getField(), "El Campo " +
                        fieldError.getField() + " " + fieldError.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errores);
    }
}
