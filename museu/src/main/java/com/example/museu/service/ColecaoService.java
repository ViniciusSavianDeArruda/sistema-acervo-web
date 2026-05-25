package com.example.museu.service;

import com.example.museu.model.Colecao;
import com.example.museu.repository.ColecaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ColecaoService {

    private final ColecaoRepository colecaoRepository;

    public ColecaoService(ColecaoRepository colecaoRepository) {
        this.colecaoRepository = colecaoRepository;
    }

    public List<Colecao> listarTodas() {
        return colecaoRepository.findAllByAtivoTrue();
    }

    public Optional<Colecao> buscarPorId(Integer id) {
        return colecaoRepository.findById(id);
    }

    public void salvar(Colecao colecao) {
        if (colecao.getNome() == null || colecao.getNome().isBlank()) {
            throw new RuntimeException("O nome da coleção é obrigatório.");
        }
        colecaoRepository.save(colecao);
    }

    public void deletar(Integer id) {
        Colecao colecao = colecaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coleção não encontrada."));
        colecao.setAtivo(false);
        colecaoRepository.save(colecao);
    }
    public Colecao salvarRapido(String nome, String descricao) {
        Colecao colecao = new Colecao();
        colecao.setNome(nome);
        colecao.setDescricao(descricao);
        colecao.setAtivo(true);
        return colecaoRepository.save(colecao);
    }
}
