package com.example.cursos.repository;

import com.example.cursos.model.Curso;
import org.springframework.data.repository.CrudRepository;

public interface CursoRepository extends CrudRepository<Curso,Long> {
}
