package com.example.museu.controller;

import com.example.museu.model.Item;
import com.example.museu.repository.ColecaoRepository;
import com.example.museu.repository.ItemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ItemRepository itemRepository;
    private final ColecaoRepository colecaoRepository;

    public HomeController(ItemRepository itemRepository, ColecaoRepository colecaoRepository) {
        this.itemRepository = itemRepository;
        this.colecaoRepository = colecaoRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("totalBiblioteca", itemRepository.countBibliograficos());
        model.addAttribute("totalHistorico", itemRepository.countByTipo(Item.HISTORICO));
        model.addAttribute("totalColecoes", colecaoRepository.count());
        model.addAttribute("destaquesBiblioteca", itemRepository.findBibliograficos().stream().limit(6).toList());
        model.addAttribute("destaquesHistorico", itemRepository.findHistoricos().stream().limit(4).toList());
        model.addAttribute("itensDestaque", itemRepository.findAll().stream().filter(Item::getAtivo).limit(4).toList());
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/biblioteca")
    public String biblioteca(Model model) {
        var itens = itemRepository.findBibliograficos();
        model.addAttribute("itens", itens);
        model.addAttribute("total", itens.size());
        return "biblioteca/lista";
    }

    @GetMapping("/historico")
    public String historico(Model model) {
        var itens = itemRepository.findHistoricos();
        model.addAttribute("itens", itens);
        model.addAttribute("total", itens.size());
        return "historico/lista";
    }
}
