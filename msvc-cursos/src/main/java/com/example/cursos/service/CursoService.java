package com.example.cursos.service;

import com.example.cursos.model.Usuario;
import com.example.cursos.model.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoService {
    List<Curso> listar();
    Optional<Curso> porId(Long idCurso);
    Optional<Curso> porIdConUsuarios(Long idCurso);
    Curso guardar(Curso curso);
    void eliminarCurso(Long idCurso);
    void eliminarCursoUsuarioPorId(Long id);
    Optional<Usuario> asignarUsuario(Usuario usuario,Long cursoId);
    Optional<Usuario> crearUsuario(Usuario usuario,Long cursoId);
    Optional<Usuario> eliminarUsuario(Usuario usuario,Long cursoId);
}
