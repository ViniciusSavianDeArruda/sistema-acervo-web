package com.example.museu.service;

import com.example.museu.model.Item;
import com.example.museu.model.Log;
import com.example.museu.model.Usuario;
import com.example.museu.repository.LogRepository;
import com.example.museu.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogService {

    private final LogRepository logRepository;
    private final UsuarioRepository usuarioRepository;

    public LogService(LogRepository logRepository, UsuarioRepository usuarioRepository) {
        this.logRepository = logRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public void registrar(Item item, String emailUsuario, int operacao) {
        Log log = new Log();
        log.setItem(item);
        log.setItemTitulo(item.getTitulo());
        log.setOperacao(operacao);

        if (emailUsuario != null) {
            usuarioRepository.findByEmail(emailUsuario).ifPresent(log::setUsuario);
        }

        logRepository.save(log);
    }

    public List<Log> listarTodos() {
        return logRepository.findAllByOrderByDataHoraDesc();
    }

    public void limparReferenciaItem(Integer itemId) {
        logRepository.limparReferenciaItem(itemId);
    }
}
