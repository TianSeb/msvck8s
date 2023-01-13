package com.example.cursos.client;

import com.example.cursos.model.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "msvc-usuarios",url = "http://localhost:8001")
public interface UsuarioClienRest {

    @GetMapping("/{id}")
    Usuario buscarId(@PathVariable Long id);

    @PostMapping
    Usuario crear(@RequestBody Usuario usuario);
}
