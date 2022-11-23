package com.example.cursos.service;

import com.example.cursos.model.Usuario;
import com.example.cursos.model.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoService {
    List<Curso> listar();
    Optional<Curso> porId(Long idCurso);
    Curso guardar(Curso curso);
    void eliminar(Long idCurso);
    Optional<Usuario> asignarUsuario(Usuario usuario, Long idCurso);
    Optional<Usuario> crearUsuario(Usuario usuario, Long idCurso);
    Optional<Usuario> eliminarUsuario(Usuario usuario, Long idCurso);


}
