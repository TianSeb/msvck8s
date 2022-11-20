package com.tianseb.msvcusuarios.repository;

import com.tianseb.msvcusuarios.models.Usuario;
import org.springframework.data.repository.CrudRepository;

public interface UsuarioRepository extends CrudRepository<Usuario,Long> {

}
