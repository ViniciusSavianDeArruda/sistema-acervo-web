package com.example.museu.security;

import com.example.museu.model.Colecao;
import com.example.museu.model.Item;
import com.example.museu.model.Usuario;
import com.example.museu.repository.ColecaoRepository;
import com.example.museu.repository.ItemRepository;
import com.example.museu.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final ItemRepository itemRepository;
    private final ColecaoRepository colecaoRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository,
                           ItemRepository itemRepository,
                           ColecaoRepository colecaoRepository,
                           PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.itemRepository = itemRepository;
        this.colecaoRepository = colecaoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        for (Usuario u : usuarioRepository.findAll()) {
            if (u.getAtivo() == null) { u.setAtivo(true); usuarioRepository.save(u); }
        }
        for (Item i : itemRepository.findAll()) {
            if (i.getAtivo() == null) { i.setAtivo(true); itemRepository.save(i); }
        }
        for (Colecao c : colecaoRepository.findAll()) {
            if (c.getAtivo() == null) { c.setAtivo(true); colecaoRepository.save(c); }
        }

        if (usuarioRepository.count() == 0) {
            Usuario admin = new Usuario();
            admin.setNome("Administrador");
            admin.setEmail("admin@museu.com");
            admin.setSenha(passwordEncoder.encode("admin123"));
            admin.setTipo(Usuario.ADMIN);
            usuarioRepository.save(admin);
            System.out.println("Usuário admin criado: admin@museu.com / admin123");
        }
    }
}
