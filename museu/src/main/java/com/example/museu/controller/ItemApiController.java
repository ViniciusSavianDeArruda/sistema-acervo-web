package com.example.museu.controller;

import com.example.museu.model.Assunto;
import com.example.museu.model.Autor;
import com.example.museu.model.Imagem;
import com.example.museu.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/itens")
public class ItemApiController {

    private final ItemService itemService;

    public ItemApiController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getItem(@PathVariable Integer id) {
        return itemService.buscarComDetalhes(id).map(item -> {
            Map<String, Object> dto = new LinkedHashMap<>();
            dto.put("id",             item.getId());
            dto.put("tipo",           item.getTipo());
            dto.put("tipoLabel",      item.getTipoLabel());
            dto.put("titulo",         item.getTitulo());
            dto.put("tituloOriginal", item.getTituloOriginal());
            dto.put("isbn",           item.getIsbn());
            dto.put("edicao",         item.getEdicao());
            dto.put("localizacao",    item.getLocalizacao());
            dto.put("descricao",      item.getDescricao());
            dto.put("notas",          item.getNotas());
            dto.put("paginacao",      item.getPaginacao());
            dto.put("volume",         item.getVolume());
            dto.put("numero",         item.getNumero());
            dto.put("localPublicacao",item.getLocalPublicacao());
            dto.put("editora",        item.getEditora());
            dto.put("dataPublicacao", item.getDataPublicacao() != null
                    ? item.getDataPublicacao().toString() : null);
            dto.put("dimensao",       item.getDimensao());
            dto.put("material",       item.getMaterial());
            dto.put("dataPeriodo",    item.getDataPeriodo());
            dto.put("doador",         item.getDoador());
            dto.put("dataCadastro",   item.getDataCadastro() != null
                    ? item.getDataCadastro().toString() : null);
            dto.put("colecao",        item.getColecao() != null ? item.getColecao().getNome() : null);
            dto.put("autores",        item.getAutores().stream().map(Autor::getNome).toList());
            dto.put("assuntos",       item.getAssuntos().stream().map(Assunto::getNome).toList());
            dto.put("imagens",        item.getImagens().stream().map(Imagem::getUrl).toList());
            return ResponseEntity.ok(dto);
        }).orElse(ResponseEntity.notFound().build());
    }
}
