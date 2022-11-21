package com.example.cursos.service;

import com.example.cursos.model.entity.Curso;
import com.example.cursos.repository.CursoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class CursoServiceImpl implements CursoService {
    private final CursoRepository cursoRepository;

    public CursoServiceImpl(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Curso> listar() {
        return StreamSupport
                .stream(cursoRepository.findAll()
                        .spliterator(), false)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porId(Long idCurso) {
        return cursoRepository.findById(idCurso);
    }

    @Override
    @Transactional
    public Curso guardar(Curso curso) {
        return cursoRepository.save(curso);
    }

    @Override
    @Transactional
    public void eliminar(Long idCurso) {
        cursoRepository.deleteById(idCurso);
    }
}
