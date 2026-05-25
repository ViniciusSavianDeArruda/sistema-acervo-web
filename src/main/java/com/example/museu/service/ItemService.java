package com.example.museu.service;

import com.example.museu.model.*;
import com.example.museu.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final AutorRepository autorRepository;
    private final AssuntoRepository assuntoRepository;
    private final ColecaoRepository colecaoRepository;
    private final LogService logService;

    public ItemService(ItemRepository itemRepository,
                       AutorRepository autorRepository,
                       AssuntoRepository assuntoRepository,
                       ColecaoRepository colecaoRepository,
                       LogService logService) {
        this.itemRepository = itemRepository;
        this.autorRepository = autorRepository;
        this.assuntoRepository = assuntoRepository;
        this.colecaoRepository = colecaoRepository;
        this.logService = logService;
    }

    public List<Item> listarBibliograficos() {
        return itemRepository.findBibliograficos();
    }

    public List<Item> listarHistoricos() {
        return itemRepository.findHistoricos();
    }

    public List<Item> listarExcluidos() {
        return itemRepository.findBibliograficosEHistoricosExcluidos();
    }

    public Optional<Item> buscarPorId(Integer id) {
        return itemRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Item> buscarComDetalhes(Integer id) {
        return itemRepository.findById(id).map(item -> {
            // força inicialização das coleções lazy dentro da transação
            item.getAutores().size();
            item.getAssuntos().size();
            item.getImagens().size();
            if (item.getColecao() != null) item.getColecao().getNome();
            return item;
        });
    }

    @Transactional
    public void salvar(Item formItem, String emailUsuario) {
        if (formItem.getTitulo() == null || formItem.getTitulo().isBlank()) {
            throw new RuntimeException("O título é obrigatório.");
        }

        boolean isNovo = (formItem.getId() == null);
        Item item;

        if (!isNovo) {
            item = itemRepository.findById(formItem.getId())
                    .orElseThrow(() -> new RuntimeException("Item não encontrado."));
            copiarCampos(formItem, item);
        } else {
            item = formItem;
            item.setDataCadastro(LocalDate.now());
        }

        resolverColecao(formItem, item);
        item.setAutores(parseAutores(formItem.getAutoresTexto()));
        item.setAssuntos(parseAssuntos(formItem.getAssuntosTexto()));

        Item salvo = itemRepository.save(item);

        salvo.getImagens().clear();
        if (formItem.getImagensUrls() != null && !formItem.getImagensUrls().isBlank()) {
            for (String url : formItem.getImagensUrls().split("\\n")) {
                String u = url.trim();
                if (!u.isEmpty()) {
                    salvo.getImagens().add(new Imagem(u, salvo));
                }
            }
        }
        itemRepository.save(salvo);

        logService.registrar(salvo, emailUsuario, isNovo ? Log.CRIAR : Log.EDITAR);
    }

    @Transactional
    public void deletar(Integer id, String emailUsuario) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item não encontrado."));
        logService.registrar(item, emailUsuario, Log.DELETAR);
        item.setAtivo(false);
        itemRepository.save(item);
    }

    @Transactional
    public void ativar(Integer id, String emailUsuario) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item não encontrado."));
        logService.registrar(item, emailUsuario, Log.ATIVAR);
        item.setAtivo(true);
        itemRepository.save(item);
    }

    public void popularCamposTransientes(Item item) {
        if (item.getAutores() != null && !item.getAutores().isEmpty()) {
            item.setAutoresTexto(
                item.getAutores().stream().map(Autor::getNome).collect(Collectors.joining(", "))
            );
        }
        if (item.getAssuntos() != null && !item.getAssuntos().isEmpty()) {
            item.setAssuntosTexto(
                item.getAssuntos().stream().map(Assunto::getNome).collect(Collectors.joining(", "))
            );
        }
        if (item.getImagens() != null && !item.getImagens().isEmpty()) {
            item.setImagensUrls(
                item.getImagens().stream().map(Imagem::getUrl).collect(Collectors.joining("\n"))
            );
        }
    }

    private void copiarCampos(Item origem, Item destino) {
        destino.setTipo(origem.getTipo());
        destino.setTitulo(origem.getTitulo());
        destino.setTituloOriginal(origem.getTituloOriginal());
        destino.setIsbn(origem.getIsbn());
        destino.setEdicao(origem.getEdicao());
        destino.setLocalizacao(origem.getLocalizacao());
        destino.setNotas(origem.getNotas());
        destino.setDescricao(origem.getDescricao());
        destino.setPaginacao(origem.getPaginacao());
        destino.setVolume(origem.getVolume());
        destino.setNumero(origem.getNumero());
        destino.setLocalPublicacao(origem.getLocalPublicacao());
        destino.setEditora(origem.getEditora());
        destino.setDataPublicacao(origem.getDataPublicacao());
        destino.setDimensao(origem.getDimensao());
        destino.setMaterial(origem.getMaterial());
        destino.setDataPeriodo(origem.getDataPeriodo());
        destino.setDoador(origem.getDoador());
        destino.setAutoresTexto(origem.getAutoresTexto());
        destino.setAssuntosTexto(origem.getAssuntosTexto());
        destino.setImagensUrls(origem.getImagensUrls());
    }

    private void resolverColecao(Item formItem, Item item) {
        if (formItem.getColecao() != null && formItem.getColecao().getId() != null) {
            colecaoRepository.findById(formItem.getColecao().getId()).ifPresent(item::setColecao);
        } else {
            item.setColecao(null);
        }
    }

    private List<Autor> parseAutores(String texto) {
        List<Autor> autores = new ArrayList<>();
        if (texto == null || texto.isBlank()) return autores;
        for (String nome : texto.split(",")) {
            String n = nome.trim();
            if (!n.isEmpty()) {
                Autor autor = autorRepository.findByNomeIgnoreCase(n)
                        .orElseGet(() -> autorRepository.save(new Autor(n)));
                autores.add(autor);
            }
        }
        return autores;
    }

    private List<Assunto> parseAssuntos(String texto) {
        List<Assunto> assuntos = new ArrayList<>();
        if (texto == null || texto.isBlank()) return assuntos;
        for (String nome : texto.split(",")) {
            String n = nome.trim();
            if (!n.isEmpty()) {
                Assunto assunto = assuntoRepository.findByNomeIgnoreCase(n)
                        .orElseGet(() -> assuntoRepository.save(new Assunto(n)));
                assuntos.add(assunto);
            }
        }
        return assuntos;
    }
}
