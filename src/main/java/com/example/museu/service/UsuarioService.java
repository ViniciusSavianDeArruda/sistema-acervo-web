package com.example.museu.service;

import com.example.museu.model.Usuario;
import com.example.museu.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    public void salvar(Usuario usuario) {
        if (usuario.getNome() == null || usuario.getNome().isBlank()) {
            throw new RuntimeException("O nome é obrigatório.");
        }
        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            throw new RuntimeException("O e-mail é obrigatório.");
        }

        boolean isNovo = (usuario.getId() == null);

        if (isNovo) {
            if (usuarioRepository.existsByEmail(usuario.getEmail())) {
                throw new RuntimeException("Já existe um usuário com este e-mail.");
            }
            if (usuario.getSenha() == null || usuario.getSenha().isBlank()) {
                throw new RuntimeException("A senha é obrigatória para novos usuários.");
            }
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        } else {
            Usuario existente = usuarioRepository.findById(usuario.getId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
            if (usuario.getSenha() != null && !usuario.getSenha().isBlank()) {
                usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
            } else {
                usuario.setSenha(existente.getSenha());
            }
        }

        usuarioRepository.save(usuario);
    }

    public void deletar(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }
}
