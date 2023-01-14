package com.example.cursos.client;

import com.example.cursos.model.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "msvc-usuarios",url = "localhost:8001")
public interface UsuarioClientRest {

    @GetMapping("/{id}")
    Usuario buscarId(@PathVariable Long id);

    @PostMapping("/")
    Usuario crear(@RequestBody Usuario usuario);

    @GetMapping("/usuarios-por-curso")
    List<Usuario>  obtenerAlumnosPorCurso(@RequestParam Iterable<Long> ids);
}
