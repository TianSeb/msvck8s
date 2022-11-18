package com.tianseb.msvcusuarios.repository;

import com.tianseb.msvcusuarios.models.entity.Usuario;
import org.springframework.data.repository.CrudRepository;

public interface UsuarioRepository extends CrudRepository<Usuario,Long> {

}
