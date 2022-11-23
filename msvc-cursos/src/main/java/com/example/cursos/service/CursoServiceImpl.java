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
    private CursoRepository cursoRepository;
    private UsuarioClientRest client;

    public CursoServiceImpl(CursoRepository cursoRepository,
                            UsuarioClientRest client) {
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
    @Transactional
    public Curso guardar(Curso curso) {
        return cursoRepository.save(curso);
    }

    @Override
    @Transactional
    public void eliminar(Long idCurso) {
        cursoRepository.deleteById(idCurso);
    }

    @Override
    @Transactional
    public Optional<Usuario> asignarUsuario(Usuario usuario, Long idCurso) {
        Optional<Curso> o = cursoRepository.findById(idCurso);

        if (o.isPresent()) {
            Usuario usuarioMsvc = client.buscarId(usuario.getId());

            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());

            curso.addCursoUsuario(cursoUsuario);
            cursoRepository.save(curso);

            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> crearUsuario(Usuario usuario, Long idCurso) {
        Optional<Curso> o = cursoRepository.findById(idCurso);

        if (o.isPresent()) {
            Usuario usuarioNuevoMsvc = client.crear(usuario);

            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioNuevoMsvc.getId());

            curso.addCursoUsuario(cursoUsuario);
            cursoRepository.save(curso);

            return Optional.of(usuarioNuevoMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> eliminarUsuario(Usuario usuario, Long idCurso) {
        Optional<Curso> o = cursoRepository.findById(idCurso);

        if (o.isPresent()) {
            Usuario usuarioMsvc = client.buscarId(usuario.getId());

            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());

            curso.removeCursoUsuario(cursoUsuario);
            cursoRepository.save(curso);

            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }
}
