package com.example.cursos.service;

import com.example.cursos.client.UsuarioClientRest;
import com.example.cursos.model.Usuario;
import com.example.cursos.model.entity.Curso;
import com.example.cursos.model.entity.CursoUsuario;
import com.example.cursos.repository.CursoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class CursoServiceImpl implements CursoService {
    private final CursoRepository cursoRepository;

    private final UsuarioClientRest client;

    public CursoServiceImpl(CursoRepository cursoRepository, UsuarioClientRest client) {
        this.cursoRepository = cursoRepository;
        this.client = client;
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
    @Transactional(readOnly = true)
    public Optional<Curso> porIdConUsuarios(Long idCurso) {
        Optional<Curso> o = cursoRepository.findById(idCurso);
        if(o.isPresent()) {
            Curso curso = o.get();
            if (!curso.getCursoUsuarios().isEmpty()) {
                List<Long> ids = curso.getCursoUsuarios()
                        .stream()
                        .map(CursoUsuario::getUsuarioId)
                        .toList();
                List<Usuario> listaUsuarios = client.obtenerAlumnosPorCurso(ids);
                curso.setUsuarios(listaUsuarios);
            }
            return Optional.of(curso);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Curso guardar(Curso curso) {
        return cursoRepository.save(curso);
    }

    @Override
    @Transactional
    public void eliminarCurso(Long idCurso) {
        cursoRepository.deleteById(idCurso);
    }

    @Override
    @Transactional
    public void eliminarCursoUsuarioPorId(Long id) {
        cursoRepository.eliminarCursoUsuarioPorId(id);
    }

    @Override
    @Transactional
    public Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> curso = cursoRepository.findById(cursoId);

        if(curso.isPresent()) {
           Usuario userFound = client.buscarId(usuario.getId());
            cursoUsuarioHanlder(curso.get(), userFound, true);

            return Optional.of(userFound);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> curso = cursoRepository.findById(cursoId);

        if(curso.isPresent()) {
            Usuario userFound = client.crear(usuario);

            cursoUsuarioHanlder(curso.get(), userFound, true);

            return Optional.of(userFound);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> eliminarUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> curso = cursoRepository.findById(cursoId);

        if(curso.isPresent()) {
            Usuario userFound = client.buscarId(usuario.getId());
            cursoUsuarioHanlder(curso.get(), userFound, false);

            return Optional.of(userFound);
        }
        return Optional.empty();
    }

    private void cursoUsuarioHanlder(Curso curso, Usuario userFound, boolean add) {

        CursoUsuario cursoUsuario = new CursoUsuario();
        cursoUsuario.setUsuarioId(userFound.getId());
        if(add) {
            curso.addCursoUsuario(cursoUsuario);
        } else {
            curso.removeCursoUsuario(cursoUsuario);
        }

        cursoRepository.save(curso);
    }
}
