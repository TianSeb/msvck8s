package com.example.cursos.controller;

import com.example.cursos.model.Curso;
import com.example.cursos.service.CursoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/cursos")
public record CursoController(CursoService cursoService) {

    @GetMapping
    public ResponseEntity<List<Curso>> buscar() {
        return ResponseEntity.ok(cursoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends Curso> buscarId(@PathVariable Long id) {
        Optional<Curso> curso = cursoService.porId(id);
        return curso.<ResponseEntity<? extends Curso>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity crear(@Valid @RequestBody Curso curso, BindingResult result) {

        return result.hasErrors() ? validar(result)
                : ResponseEntity.status(HttpStatus.CREATED).body(cursoService.guardar(curso));
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
            cursoDb.setName(curso.getName());
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
            cursoService.eliminar(response.get().getId());
            return ResponseEntity.noContent().build();
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
