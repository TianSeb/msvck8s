package com.tianseb.msvcusuarios.service;

import com.tianseb.msvcusuarios.client.CursoClienteRest;
import com.tianseb.msvcusuarios.models.Usuario;
import com.tianseb.msvcusuarios.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private UsuarioRepository repository;

    private CursoClienteRest client;

    public UsuarioServiceImpl(UsuarioRepository repository, CursoClienteRest client) {
        this.repository = repository;
        this.client = client;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return (List<Usuario>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> porId(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Usuario guardar(Usuario usuario) {
        return repository.save(usuario);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
        client.eliminarCursoUsuarioPorId(id);
    }

    @Override
    public Optional<Usuario> porEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarPorIds(Iterable<Long> ids) {
        List<Usuario> lista = new ArrayList<>();
        repository.findAllById(ids)
                .forEach(lista::add);
        return lista;
    }
}
