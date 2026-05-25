package com.example.museu.controller;

import com.example.museu.model.Item;
import com.example.museu.model.Colecao;
import com.example.museu.model.Usuario;
import com.example.museu.repository.ColecaoRepository;
import com.example.museu.repository.ItemRepository;
import com.example.museu.repository.UsuarioRepository;
import com.example.museu.service.ColecaoService;
import com.example.museu.service.ItemService;
import com.example.museu.service.LogService;
import com.example.museu.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.ResponseEntity;
import java.util.Map;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ItemRepository itemRepository;
    private final ColecaoRepository colecaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ItemService itemService;
    private final ColecaoService colecaoService;
    private final UsuarioService usuarioService;
    private final LogService logService;

    public AdminController(ItemRepository itemRepository,
                           ColecaoRepository colecaoRepository,
                           UsuarioRepository usuarioRepository,
                           ItemService itemService,
                           ColecaoService colecaoService,
                           UsuarioService usuarioService,
                           LogService logService) {
        this.itemRepository = itemRepository;
        this.colecaoRepository = colecaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.itemService = itemService;
        this.colecaoService = colecaoService;
        this.usuarioService = usuarioService;
        this.logService = logService;
    }

    /* ===== DASHBOARD ===== */

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("totalBiblioteca", itemRepository.countBibliograficos());
        model.addAttribute("totalHistorico", itemRepository.countByTipo(Item.HISTORICO));
        model.addAttribute("totalColecoes", colecaoRepository.countByAtivoTrue());
        model.addAttribute("totalUsuarios", usuarioRepository.countByAtivoTrue());
        model.addAttribute("ultimosBiblioteca", itemRepository.findBibliograficos().stream().limit(5).toList());
        model.addAttribute("ultimosHistorico", itemRepository.findHistoricos().stream().limit(5).toList());
        return "admin/dashboard";
    }

    /* ===== BIBLIOTECA (tipos 1-4) ===== */

    @GetMapping("/biblioteca")
    public String listarBiblioteca(Model model) {
        model.addAttribute("itens", itemService.listarBibliograficos());
        model.addAttribute("paginaAtiva", "biblioteca");
        return "admin/biblioteca";
    }

    @GetMapping("/biblioteca/novo")
    public String novoBiblioteca(Model model) {
        Item item = new Item();
        item.setTipo(Item.LIVRO);
        item.setColecao(new Colecao());
        model.addAttribute("item", item);
        model.addAttribute("colecoes", colecaoService.listarTodas());
        model.addAttribute("paginaAtiva", "biblioteca");
        model.addAttribute("modulo", "biblioteca");
        return "admin/formulario-item";
    }

    /* ===== HISTÓRICO (tipo 5) ===== */

    @GetMapping("/historico")
    public String listarHistorico(Model model) {
        model.addAttribute("itens", itemService.listarHistoricos());
        model.addAttribute("paginaAtiva", "historico");
        return "admin/historico";
    }

    @GetMapping("/historico/novo")
    public String novoHistorico(Model model) {
        Item item = new Item();
        item.setTipo(Item.HISTORICO);
        item.setColecao(new Colecao());
        model.addAttribute("item", item);
        model.addAttribute("colecoes", colecaoService.listarTodas());
        model.addAttribute("paginaAtiva", "historico");
        model.addAttribute("modulo", "historico");
        return "admin/formulario-item";
    }

    /* ===== FORMULÁRIO COMPARTILHADO ===== */

    @GetMapping("/itens/editar/{id}")
    public String editarItem(@PathVariable Integer id, Model model) {
        Optional<Item> opt = itemRepository.findById(id);
        if (opt.isEmpty()) return "redirect:/admin/biblioteca";
        Item item = opt.get();
        itemService.popularCamposTransientes(item);
        if (item.getColecao() == null) {
            item.setColecao(new Colecao());
        }
        String modulo = (item.getTipo() != null && item.getTipo() == Item.HISTORICO) ? "historico" : "biblioteca";
        model.addAttribute("item", item);
        model.addAttribute("colecoes", colecaoService.listarTodas());
        model.addAttribute("paginaAtiva", modulo);
        model.addAttribute("modulo", modulo);
        return "admin/formulario-item";
    }

    @PostMapping("/itens/salvar")
    public String salvarItem(Item item, Authentication auth, RedirectAttributes ra) {
        try {
            itemService.salvar(item, auth.getName());
        } catch (RuntimeException e) {
            ra.addFlashAttribute("erro", e.getMessage());
            String modulo = (item.getTipo() != null && item.getTipo() == Item.HISTORICO) ? "historico" : "biblioteca";
            return item.getId() != null
                    ? "redirect:/admin/itens/editar/" + item.getId()
                    : "redirect:/admin/" + modulo + "/novo";
        }
        String destino = (item.getTipo() != null && item.getTipo() == Item.HISTORICO) ? "historico" : "biblioteca";
        ra.addFlashAttribute("sucesso", "Item salvo com sucesso.");
        return "redirect:/admin/" + destino;
    }
    @PostMapping("/colecoes/quick-save")
    @ResponseBody
    public ResponseEntity<?> quickSaveColecao(@RequestParam String nome,
                                              @RequestParam(required = false) String descricao) {
        Colecao colecao = colecaoService.salvarRapido(nome, descricao);
        return ResponseEntity.ok(Map.of("id", colecao.getId(), "nome", colecao.getNome()));
    }

    @GetMapping("/itens/deletar/{id}")
    public String deletarItem(@PathVariable Integer id, Authentication auth, RedirectAttributes ra) {
        Optional<Item> opt = itemRepository.findById(id);
        if (opt.isEmpty()) return "redirect:/admin/biblioteca";
        String destino = (opt.get().getTipo() == Item.HISTORICO) ? "historico" : "biblioteca";
        itemService.deletar(id, auth.getName());
        ra.addFlashAttribute("sucesso", "Item excluído com sucesso.");
        return "redirect:/admin/" + destino;
    }

    /* ===== COLEÇÕES ===== */

    @GetMapping("/colecoes")
    public String listarColecoes(Model model) {
        model.addAttribute("colecoes", colecaoService.listarTodas());
        model.addAttribute("paginaAtiva", "colecoes");
        return "admin/colecoes";
    }

    @GetMapping("/colecoes/novo")
    public String novaColecao(Model model) {
        model.addAttribute("colecao", new Colecao());
        model.addAttribute("paginaAtiva", "colecoes");
        return "admin/formulario-colecao";
    }

    @GetMapping("/colecoes/editar/{id}")
    public String editarColecao(@PathVariable Integer id, Model model) {
        Optional<Colecao> opt = colecaoRepository.findById(id);
        if (opt.isEmpty()) return "redirect:/admin/colecoes";
        model.addAttribute("colecao", opt.get());
        model.addAttribute("paginaAtiva", "colecoes");
        return "admin/formulario-colecao";
    }

    @PostMapping("/colecoes/salvar")
    public String salvarColecao(Colecao colecao, RedirectAttributes ra) {
        try {
            colecaoService.salvar(colecao);
        } catch (RuntimeException e) {
            ra.addFlashAttribute("erro", e.getMessage());
            return colecao.getId() != null
                    ? "redirect:/admin/colecoes/editar/" + colecao.getId()
                    : "redirect:/admin/colecoes/novo";
        }
        ra.addFlashAttribute("sucesso", "Coleção salva com sucesso.");
        return "redirect:/admin/colecoes";
    }

    @GetMapping("/colecoes/deletar/{id}")
    public String deletarColecao(@PathVariable Integer id, RedirectAttributes ra) {
        colecaoService.deletar(id);
        ra.addFlashAttribute("sucesso", "Coleção excluída com sucesso.");
        return "redirect:/admin/colecoes";
    }

    /* ===== USUÁRIOS ===== */

    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAllByAtivoTrue());
        model.addAttribute("paginaAtiva", "usuarios");
        return "admin/usuarios";
    }

    @GetMapping("/usuarios/novo")
    public String novoUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("paginaAtiva", "usuarios");
        return "admin/formulario-usuario";
    }

    @GetMapping("/usuarios/editar/{id}")
    public String editarUsuario(@PathVariable Integer id, Model model) {
        Optional<Usuario> opt = usuarioRepository.findById(id);
        if (opt.isEmpty()) return "redirect:/admin/usuarios";
        model.addAttribute("usuario", opt.get());
        model.addAttribute("paginaAtiva", "usuarios");
        return "admin/formulario-usuario";
    }

    @PostMapping("/usuarios/salvar")
    public String salvarUsuario(Usuario usuario, RedirectAttributes ra) {
        try {
            usuarioService.salvar(usuario);
        } catch (RuntimeException e) {
            ra.addFlashAttribute("erro", e.getMessage());
            return usuario.getId() != null
                    ? "redirect:/admin/usuarios/editar/" + usuario.getId()
                    : "redirect:/admin/usuarios/novo";
        }
        ra.addFlashAttribute("sucesso", "Usuário salvo com sucesso.");
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/usuarios/deletar/{id}")
    public String deletarUsuario(@PathVariable Integer id, RedirectAttributes ra) {
        usuarioService.deletar(id);
        ra.addFlashAttribute("sucesso", "Usuário excluído com sucesso.");
        return "redirect:/admin/usuarios";
    }

    /* ===== LOGS ===== */

    @GetMapping("/logs")
    public String logs(Model model) {
        model.addAttribute("logs", logService.listarTodos());
        model.addAttribute("paginaAtiva", "logs");
        return "admin/logs";
    }

    /* ===== Excluidos ===== */

    @GetMapping("/excluidos")
    public String listarExcluidos(Model model) {
        model.addAttribute("itens", itemService.listarExcluidos());
        model.addAttribute("paginaAtiva", "excluidos");
        return "admin/excluidos";
    }

    @GetMapping("/itens/ativar/{id}")
    public String ativarItem(@PathVariable Integer id, Authentication auth, RedirectAttributes ra) {
        itemService.ativar(id, auth.getName());
        ra.addFlashAttribute("sucesso", "Item ativado com sucesso.");
        return "redirect:/admin/excluidos";
    }
}
