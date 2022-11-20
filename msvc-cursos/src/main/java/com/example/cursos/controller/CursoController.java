package com.example.cursos.controller;

import com.example.cursos.model.Curso;
import com.example.cursos.service.CursoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    @ResponseStatus(HttpStatus.CREATED)
    public Curso crear(@RequestBody Curso curso) {
        return cursoService.guardar(curso);
    }

    @PutMapping("/{id}")
    public ResponseEntity<? extends Curso> editar(@PathVariable Long id,
                                                  @RequestBody Curso curso) {
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
}
