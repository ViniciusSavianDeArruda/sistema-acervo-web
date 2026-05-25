package com.example.museu.security;

import com.example.museu.model.*;
import com.example.museu.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Order(2)
public class DataSeeder implements CommandLineRunner {

    private final ColecaoRepository colecaoRepo;
    private final AutorRepository autorRepo;
    private final AssuntoRepository assuntoRepo;
    private final ItemRepository itemRepo;
    private final UsuarioRepository usuarioRepo;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(ColecaoRepository colecaoRepo,
                      AutorRepository autorRepo,
                      AssuntoRepository assuntoRepo,
                      ItemRepository itemRepo,
                      UsuarioRepository usuarioRepo,
                      PasswordEncoder passwordEncoder) {
        this.colecaoRepo   = colecaoRepo;
        this.autorRepo     = autorRepo;
        this.assuntoRepo   = assuntoRepo;
        this.itemRepo      = itemRepo;
        this.usuarioRepo   = usuarioRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (itemRepo.count() > 0) return;

        // ── Usuários ──────────────────────────────────────────────────────────
        criarUsuario("Ana Paula Ferreira", "ana@museu.com", "editor123", Usuario.EDITOR, "111.222.333-44", "(11) 91234-5678");
        criarUsuario("Carlos Mendes",      "carlos@museu.com", "editor123", Usuario.EDITOR, "555.666.777-88", "(21) 98765-4321");
        criarUsuario("Beatriz Santos",     "beatriz@museu.com", "admin123",  Usuario.ADMIN,  "999.888.777-66", "(31) 97654-3210");

        // ── Coleções ──────────────────────────────────────────────────────────
        Colecao litBras   = colecao("Acervo Literário Brasileiro",
                "Obras de ficção, poesia e ensaios produzidos por autores brasileiros dos séculos XIX e XX.");
        Colecao cient     = colecao("Acervo Científico e Tecnológico",
                "Publicações científicas, técnicas e tecnológicas de relevância histórica e acadêmica.");
        Colecao perHist   = colecao("Periódicos Históricos",
                "Revistas, jornais e boletins publicados entre os séculos XIX e XX de circulação nacional.");
        Colecao docHist   = colecao("Documentos Históricos",
                "Manuscritos, decretos, cartas e documentos oficiais de valor histórico para o Brasil.");
        Colecao artCult   = colecao("Artes e Cultura",
                "Catálogos de exposições, publicações culturais e obras relacionadas às artes visuais e música.");

        // ── Autores ───────────────────────────────────────────────────────────
        Autor machado     = autor("Machado de Assis");
        Autor clarice     = autor("Clarice Lispector");
        Autor guimaraes   = autor("João Guimarães Rosa");
        Autor alencar     = autor("José de Alencar");
        Autor euclides    = autor("Euclides da Cunha");
        Autor amado       = autor("Jorge Amado");
        Autor lobato      = autor("Monteiro Lobato");
        Autor drummond    = autor("Carlos Drummond de Andrade");
        Autor graciliano  = autor("Graciliano Ramos");
        Autor lima        = autor("Lima Barreto");
        Autor santos      = autor("Alberto Santos Dumont");
        Autor oswaldo     = autor("Oswaldo Cruz");
        Autor ruibarbosa  = autor("Rui Barbosa");
        Autor saintHilaire = autor("Auguste de Saint-Hilaire");
        Autor darwin      = autor("Charles Darwin");
        Autor julio       = autor("Júlio Verne");
        Autor capistrano  = autor("João Capistrano de Abreu");
        Autor varnhagen   = autor("Francisco Adolfo de Varnhagen");
        Autor nabuco      = autor("Joaquim Nabuco");
        Autor cecilia     = autor("Cecília Meireles");

        // ── Assuntos ──────────────────────────────────────────────────────────
        Assunto litBrasAssunto = assunto("Literatura Brasileira");
        Assunto romance    = assunto("Romance");
        Assunto poesia     = assunto("Poesia");
        Assunto conto      = assunto("Conto");
        Assunto ciencias   = assunto("Ciências Naturais");
        Assunto botanica   = assunto("Botânica");
        Assunto historia   = assunto("História do Brasil");
        Assunto imperio    = assunto("Império Brasileiro");
        Assunto medicina   = assunto("Medicina e Saúde Pública");
        Assunto aviacao    = assunto("Aviação e Aeronáutica");
        Assunto abolicion  = assunto("Abolicionismo");
        Assunto sertao     = assunto("Cultura Sertaneja");
        Assunto infanto    = assunto("Literatura Infantil");
        Assunto jornalismo = assunto("Jornalismo e Imprensa");
        Assunto direito    = assunto("Direito e Legislação");

        // ── LIVROS ────────────────────────────────────────────────────────────
        item(Item.LIVRO, "Dom Casmurro", null, "978-85-359-0277-9", "2ª edição",
                "Est. A / Prat. 3 / Pos. 12", litBras,
                "Romance que narra a história de Bentinho e Capitu, explorando temas de ciúme e memória.",
                "336", null, null, "Rio de Janeiro", "Garnier", LocalDate.of(1899, 1, 1),
                "18 × 12 cm", "Capa dura, papel offset", null, "Doação família Assis",
                List.of(machado), List.of(litBrasAssunto, romance));

        item(Item.LIVRO, "A Paixão Segundo G.H.", null, "978-85-359-0412-4", "3ª edição",
                "Est. A / Prat. 3 / Pos. 13", litBras,
                "Monólogo interior de uma mulher diante de uma barata; obra existencialista de Clarice Lispector.",
                "180", null, null, "Rio de Janeiro", "Rocco", LocalDate.of(1964, 1, 1),
                "21 × 14 cm", "Brochura", null, "Doação Biblioteca Central UFRJ",
                List.of(clarice), List.of(litBrasAssunto, romance));

        item(Item.LIVRO, "Grande Sertão: Veredas", null, "978-85-220-0580-6", "1ª edição",
                "Est. A / Prat. 4 / Pos. 1", litBras,
                "Epopeia do sertão mineiro narrada pelo ex-jagunço Riobaldo; obra-prima da literatura brasileira.",
                "608", null, null, "Rio de Janeiro", "José Olympio", LocalDate.of(1956, 1, 1),
                "23 × 15 cm", "Capa dura", null, "Doação Instituto Guimarães Rosa",
                List.of(guimaraes), List.of(litBrasAssunto, romance, sertao));

        item(Item.LIVRO, "Iracema", "Iracema – The Honey-Lips", "978-85-359-0110-9", "Edição comemorativa",
                "Est. B / Prat. 1 / Pos. 5", litBras,
                "Lenda do Ceará que narra o romance entre a índia Iracema e o português Martim.",
                "166", null, null, "São Paulo", "Ática", LocalDate.of(1865, 1, 1),
                "20 × 13 cm", "Brochura", null, "Doação Instituto Cultural José de Alencar",
                List.of(alencar), List.of(litBrasAssunto, romance, historia));

        item(Item.LIVRO, "Vidas Secas", null, "978-85-359-0100-0", "12ª edição",
                "Est. A / Prat. 4 / Pos. 8", litBras,
                "Romance sobre a família de Fabiano, retirantes que fogem da seca no Nordeste brasileiro.",
                "176", null, null, "Rio de Janeiro", "Record", LocalDate.of(1938, 1, 1),
                "19 × 12 cm", "Brochura", null, "Doação Academia Brasileira de Letras",
                List.of(graciliano), List.of(litBrasAssunto, romance, sertao));

        item(Item.LIVRO, "Os Sertões", null, "978-85-503-0005-1", "1ª edição fac-símile",
                "Est. B / Prat. 2 / Pos. 3", litBras,
                "Relato da Guerra de Canudos; divide-se em 'A Terra', 'O Homem' e 'A Luta'.",
                "632", null, null, "São Paulo", "Laemmert", LocalDate.of(1902, 1, 1),
                "24 × 16 cm", "Capa dura, papel envelhecido", null, "Doação Museu da República",
                List.of(euclides), List.of(litBrasAssunto, historia, sertao));

        item(Item.LIVRO, "Gabriela, Cravo e Canela", null, "978-85-03-00660-2", "6ª edição",
                "Est. B / Prat. 1 / Pos. 9", litBras,
                "Romance ambientado em Ilhéus na era do cacau; retrato da sociedade baiana dos anos 1920.",
                "324", null, null, "São Paulo", "Martins Fontes", LocalDate.of(1958, 1, 1),
                "21 × 14 cm", "Brochura", null, "Doação Fundação Jorge Amado",
                List.of(amado), List.of(litBrasAssunto, romance));

        item(Item.LIVRO, "Reinações de Narizinho", null, "978-85-7914-025-6", "Edição ilustrada",
                "Est. C / Prat. 1 / Pos. 2", litBras,
                "Aventuras de Narizinho e Pedrinho no Sítio do Picapau Amarelo.",
                "248", null, null, "São Paulo", "Companhia Editora Nacional", LocalDate.of(1931, 1, 1),
                "22 × 15 cm", "Capa dura, ilustrado", null, "Doação Instituto Monteiro Lobato",
                List.of(lobato), List.of(litBrasAssunto, infanto));

        item(Item.LIVRO, "A Conquista do Ar", "My Air-Ships", "978-85-000-0001-0", "1ª edição brasileira",
                "Est. D / Prat. 1 / Pos. 1", cient,
                "Memórias e experimentos aeronáuticos do pioneiro da aviação Alberto Santos Dumont.",
                "312", null, null, "Paris / Rio de Janeiro", "Charpentier", LocalDate.of(1901, 1, 1),
                "22 × 14 cm", "Capa dura, encadernação original", null, "Doação Museu Aeronáutico Nacional",
                List.of(santos), List.of(aviacao, ciencias));

        item(Item.LIVRO, "O Abolicionismo", null, "978-85-000-0002-7", "2ª edição",
                "Est. B / Prat. 3 / Pos. 7", litBras,
                "Obra seminal de Joaquim Nabuco sobre o movimento abolicionista brasileiro.",
                "229", null, null, "Londres", "Abraham Kingdon", LocalDate.of(1883, 1, 1),
                "20 × 13 cm", "Brochura", null, "Doação Instituto Histórico e Geográfico Brasileiro",
                List.of(nabuco), List.of(historia, abolicion, direito));

        item(Item.LIVRO, "Viagem ao Interior do Brasil", "Voyage dans l'intérieur du Brésil",
                null, "Tradução comentada",
                "Est. D / Prat. 2 / Pos. 4", cient,
                "Relato das expedições botânicas de Saint-Hilaire pelas províncias brasileiras entre 1816 e 1822.",
                "450", null, null, "Paris / Belo Horizonte", "Editora Itatiaia", LocalDate.of(1830, 1, 1),
                "24 × 16 cm", "Capa dura", null, "Doação Herbário Nacional",
                List.of(saintHilaire), List.of(botanica, ciencias, historia));

        item(Item.LIVRO, "Triste Fim de Policarpo Quaresma", null, "978-85-359-0201-4", "3ª edição",
                "Est. A / Prat. 5 / Pos. 2", litBras,
                "Sátira ao nationalismo exacerbado através das desventuras do major Policarpo Quaresma.",
                "210", null, null, "Rio de Janeiro", "Garnier", LocalDate.of(1915, 1, 1),
                "19 × 12 cm", "Brochura", null, "Doação Academia Brasileira de Letras",
                List.of(lima), List.of(litBrasAssunto, romance));

        // ── REVISTAS ──────────────────────────────────────────────────────────
        item(Item.REVISTA, "Revista Brasileira de Medicina", null, null, null,
                "Per. / Gav. 1 / N. 1", perHist,
                "Publicação médica de referência do início do século XX com artigos sobre doenças tropicais.",
                "88", "Vol. IV", 3, "Rio de Janeiro", "Imprensa Nacional", LocalDate.of(1920, 7, 1),
                "28 × 20 cm", "Brochura, papel jornal", null, "Doação Fiocruz",
                List.of(oswaldo), List.of(medicina, ciencias));

        item(Item.REVISTA, "Fon-Fon!", null, null, null,
                "Per. / Gav. 1 / N. 2", perHist,
                "Revista carioca de variedades, humor e cultura do início do século XX.",
                "32", "Ano IX", 423, "Rio de Janeiro", "Fon-Fon! Editora", LocalDate.of(1915, 3, 15),
                "30 × 22 cm", "Brochura, ilustrado", null, "Doação Biblioteca Nacional",
                List.of(), List.of(jornalismo));

        item(Item.REVISTA, "O Cruzeiro", null, null, null,
                "Per. / Gav. 1 / N. 3", perHist,
                "Uma das revistas ilustradas mais importantes do Brasil, com fotorreportagens e artigos culturais.",
                "64", "Ano XII", 612, "Rio de Janeiro", "Diários Associados", LocalDate.of(1940, 11, 5),
                "30 × 22 cm", "Brochura, capa colorida", null, "Doação Museu da Imprensa",
                List.of(), List.of(jornalismo));

        item(Item.REVISTA, "Vida Doméstica", null, null, null,
                "Per. / Gav. 2 / N. 1", perHist,
                "Revista feminina voltada para o cotidiano doméstico, moda e culinária nos anos 1920.",
                "48", "Ano VI", 71, "Rio de Janeiro", "Editora Record", LocalDate.of(1925, 5, 1),
                "28 × 20 cm", "Brochura", null, "Doação família Pimentel",
                List.of(), List.of(jornalismo));

        item(Item.REVISTA, "Revista do Brasil", null, null, null,
                "Per. / Gav. 2 / N. 2", artCult,
                "Publicação literária e cultural fundada por Monteiro Lobato com contos, ensaios e crítica.",
                "96", "Vol. II", 18, "São Paulo", "Monteiro Lobato & Cia.", LocalDate.of(1917, 6, 1),
                "27 × 19 cm", "Brochura", null, "Doação Biblioteca Mário de Andrade",
                List.of(lobato), List.of(litBrasAssunto, jornalismo));

        item(Item.REVISTA, "Revista de Botânica do Museu Nacional", null, null, null,
                "Per. / Gav. 3 / N. 1", cient,
                "Publicação científica com descrições de novas espécies da flora brasileira.",
                "120", "Vol. III", 7, "Rio de Janeiro", "Museu Nacional", LocalDate.of(1910, 1, 1),
                "28 × 21 cm", "Brochura, com pranchas ilustradas", null, "Doação Museu Nacional UFRJ",
                List.of(saintHilaire), List.of(botanica, ciencias));

        // ── JORNAIS ───────────────────────────────────────────────────────────
        item(Item.JORNAL, "Gazeta de Notícias – Edição da Abolição", null, null, null,
                "Jor. / Caixa 1 / N. 1", perHist,
                "Edição histórica noticiando a assinatura da Lei Áurea em 13 de maio de 1888.",
                "4", null, null, "Rio de Janeiro", "Gazeta de Notícias", LocalDate.of(1888, 5, 14),
                "60 × 44 cm", "Papel jornal frágil, amarelado", null, "Doação Arquivo Nacional",
                List.of(), List.of(historia, abolicion, jornalismo));

        item(Item.JORNAL, "O Estado de S. Paulo – Proclamação da República", null, null, null,
                "Jor. / Caixa 1 / N. 2", perHist,
                "Edição de 16 de novembro de 1889 noticiando a proclamação da República.",
                "4", null, null, "São Paulo", "O Estado de S. Paulo", LocalDate.of(1889, 11, 16),
                "58 × 42 cm", "Papel jornal, dobrado", null, "Doação Arquivo Público do Estado de SP",
                List.of(), List.of(historia, jornalismo));

        item(Item.JORNAL, "Correio da Manhã – Morte de Machado de Assis", null, null, null,
                "Jor. / Caixa 2 / N. 1", perHist,
                "Edição de 30 de setembro de 1908 com necrológio e homenagens ao escritor Machado de Assis.",
                "6", null, null, "Rio de Janeiro", "Correio da Manhã", LocalDate.of(1908, 9, 30),
                "60 × 44 cm", "Papel jornal", null, "Doação Biblioteca Nacional",
                List.of(), List.of(historia, litBrasAssunto, jornalismo));

        item(Item.JORNAL, "Jornal do Brasil – Santos Dumont voa em Paris", null, null, null,
                "Jor. / Caixa 2 / N. 2", perHist,
                "Edição de outubro de 1906 relatando o voo do 14-Bis de Santos Dumont no Campo de Bagatelle.",
                "8", null, null, "Rio de Janeiro", "Jornal do Brasil", LocalDate.of(1906, 10, 24),
                "58 × 42 cm", "Papel jornal, manchete em destaque", null, "Doação Museu Aeronáutico",
                List.of(), List.of(historia, aviacao, jornalismo));

        item(Item.JORNAL, "Diário de Pernambuco – Abolição", null, null, null,
                "Jor. / Caixa 1 / N. 3", perHist,
                "Edição histórica do jornal mais antigo do Brasil em circulação, noticiando o fim da escravidão.",
                "4", null, null, "Recife", "Diário de Pernambuco", LocalDate.of(1888, 5, 15),
                "55 × 40 cm", "Papel jornal fragmentado, restaurado", null, "Doação Arquivo Público de Pernambuco",
                List.of(), List.of(historia, abolicion, jornalismo));

        // ── PERIÓDICOS ────────────────────────────────────────────────────────
        item(Item.PERIODICO, "Anais da Academia Brasileira de Ciências", null, null, null,
                "Per. / Est. E / V. 1", cient,
                "Publicação da Academia Brasileira de Ciências com artigos nas áreas de física, química e biologia.",
                "210", "Vol. II", 4, "Rio de Janeiro", "Academia Brasileira de Ciências", LocalDate.of(1930, 1, 1),
                "28 × 20 cm", "Brochura", null, "Doação Academia Brasileira de Ciências",
                List.of(), List.of(ciencias, medicina));

        item(Item.PERIODICO, "Boletim do Museu Paraense Emílio Goeldi", null, null, null,
                "Per. / Est. E / V. 2", cient,
                "Boletim com pesquisas sobre a fauna, flora e etnografia da Amazônia.",
                "180", "Vol. VIII", 2, "Belém", "Museu Paraense", LocalDate.of(1913, 1, 1),
                "28 × 21 cm", "Brochura, com pranchas", null, "Doação Museu Goeldi",
                List.of(darwin), List.of(botanica, ciencias));

        item(Item.PERIODICO, "Arquivos do Museu Nacional", null, null, null,
                "Per. / Est. E / V. 3", cient,
                "Periódico científico do Museu Nacional com pesquisas em paleontologia e zoologia.",
                "160", "Vol. XIV", 1, "Rio de Janeiro", "Museu Nacional", LocalDate.of(1907, 1, 1),
                "27 × 20 cm", "Brochura", null, "Doação Museu Nacional UFRJ",
                List.of(), List.of(ciencias));

        item(Item.PERIODICO, "Revista do Instituto Histórico e Geográfico Brasileiro", null, null, null,
                "Per. / Est. F / V. 1", perHist,
                "Publicação do IHGB com estudos sobre história, geografia e etnografia do Brasil.",
                "350", "Vol. 43", null, "Rio de Janeiro", "IHGB", LocalDate.of(1880, 1, 1),
                "25 × 17 cm", "Brochura, encadernação tela", null, "Doação IHGB",
                List.of(capistrano, varnhagen), List.of(historia, imperio));

        item(Item.PERIODICO, "Anais da Biblioteca Nacional", null, null, null,
                "Per. / Est. F / V. 2", litBras,
                "Publicação anual com catálogos, inventários e artigos sobre o acervo da Biblioteca Nacional.",
                "280", "Vol. XXXV", null, "Rio de Janeiro", "Biblioteca Nacional", LocalDate.of(1913, 1, 1),
                "26 × 18 cm", "Brochura", null, "Doação Biblioteca Nacional",
                List.of(), List.of(historia, jornalismo));

        // ── HISTÓRICO ─────────────────────────────────────────────────────────
        item(Item.HISTORICO, "Carta de Pero Vaz de Caminha – Cópia Fac-símile", null, null, null,
                "Doc. / Cofre 1 / N. 1", docHist,
                "Reprodução fac-similada da carta enviada por Pero Vaz de Caminha ao Rei D. Manuel I " +
                "relatando o descobrimento do Brasil em 1500.",
                "14 fólios", null, null, "Lisboa / Rio de Janeiro", "Arquivo Nacional de Portugal", null,
                "32 × 22 cm", "Pergaminho artificial, tinta ferrogálica, capa em couro",
                "1500", "Doação Arquivo Nacional",
                List.of(), List.of(historia));

        item(Item.HISTORICO, "Constituição do Império do Brasil – Exemplar Original", null, null, null,
                "Doc. / Cofre 1 / N. 2", docHist,
                "Exemplar impresso da Constituição outorgada por D. Pedro I em 25 de março de 1824.",
                "48 páginas", null, null, "Rio de Janeiro", "Imprensa Nacional", null,
                "35 × 25 cm", "Papel algodão, tinta tipográfica, capa pergaminho",
                "1824", "Doação Arquivo Nacional",
                List.of(), List.of(historia, imperio, direito));

        item(Item.HISTORICO, "Lei Áurea – Cópia Autenticada", null, null, null,
                "Doc. / Cofre 1 / N. 3", docHist,
                "Cópia notarialmente autenticada da Lei nº 3.353 de 13 de maio de 1888, " +
                "assinada pela Princesa Isabel, abolindo a escravidão no Brasil.",
                "1 fólio", null, null, "Rio de Janeiro", "Secretaria de Estado", null,
                "33 × 22 cm", "Papel algodão, tinta ferrogálica, selo imperial",
                "1888", "Doação Arquivo Nacional",
                List.of(), List.of(historia, abolicion, direito));

        item(Item.HISTORICO, "Manifesto à Nação – Proclamação da República", null, null, null,
                "Doc. / Cofre 2 / N. 1", docHist,
                "Impresso original do manifesto redigido pelo Marechal Deodoro da Fonseca anunciando " +
                "a proclamação da República em 15 de novembro de 1889.",
                "2 fólios", null, null, "Rio de Janeiro", "Imprensa do Exército", null,
                "40 × 28 cm", "Papel offset, tinta tipográfica",
                "1889", "Doação Museu da República",
                List.of(), List.of(historia, direito));

        item(Item.HISTORICO, "Diário de Bordo da Expedição Von Spix e Von Martius – Trecho Copiado", null, null, null,
                "Doc. / Cofre 2 / N. 2", docHist,
                "Transcrição manuscrita de trechos do diário da expedição científica austríaco-bávara " +
                "ao Brasil entre 1817 e 1820, com anotações botânicas e etnográficas.",
                "68 páginas", null, null, "Munique / Rio de Janeiro", null, null,
                "24 × 17 cm", "Papel linho, tinta nanquim, encadernação artesanal",
                "1817-1820", "Doação Jardim Botânico do Rio de Janeiro",
                List.of(saintHilaire), List.of(botanica, historia, ciencias));

        item(Item.HISTORICO, "Alvará Régio de Fundação da Biblioteca Nacional – Cópia", null, null, null,
                "Doc. / Cofre 2 / N. 3", docHist,
                "Cópia do alvará emitido por D. João VI em 1810 criando a Real Biblioteca Pública " +
                "da Corte, atual Biblioteca Nacional.",
                "3 fólios", null, null, "Rio de Janeiro", "Real Imprensa", null,
                "38 × 26 cm", "Pergaminho, tinta ferrogálica, selo real em cera",
                "1810", "Doação Biblioteca Nacional",
                List.of(), List.of(historia, imperio));

        System.out.println("[DataSeeder] Banco de dados populado com sucesso.");
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private void criarUsuario(String nome, String email, String senha, int tipo, String cpf, String telefone) {
        if (usuarioRepo.findByEmail(email).isEmpty()) {
            Usuario u = new Usuario();
            u.setNome(nome);
            u.setEmail(email);
            u.setSenha(passwordEncoder.encode(senha));
            u.setTipo(tipo);
            u.setCpf(cpf);
            u.setTelefone(telefone);
            u.setAtivo(true);
            usuarioRepo.save(u);
        }
    }

    private Colecao colecao(String nome, String descricao) {
        return colecaoRepo.save(new Colecao(nome, descricao));
    }

    private Autor autor(String nome) {
        return autorRepo.findByNomeIgnoreCase(nome).orElseGet(() -> autorRepo.save(new Autor(nome)));
    }

    private Assunto assunto(String nome) {
        return assuntoRepo.findByNomeIgnoreCase(nome).orElseGet(() -> assuntoRepo.save(new Assunto(nome)));
    }

    private void item(int tipo, String titulo, String tituloOriginal,
                      String isbn, String edicao, String localizacao,
                      Colecao colecao, String descricao,
                      String paginacao, String volume, Integer numero,
                      String localPub, String editora, LocalDate dataPub,
                      String dimensao, String material, String dataPeriodo,
                      String doador, List<Autor> autores, List<Assunto> assuntos) {

        Item i = new Item();
        i.setTipo(tipo);
        i.setTitulo(titulo);
        i.setTituloOriginal(tituloOriginal);
        i.setIsbn(isbn);
        i.setEdicao(edicao);
        i.setLocalizacao(localizacao);
        i.setColecao(colecao);
        i.setDescricao(descricao);
        i.setPaginacao(paginacao);
        i.setVolume(volume);
        i.setNumero(numero);
        i.setLocalPublicacao(localPub);
        i.setEditora(editora);
        i.setDataPublicacao(dataPub);
        i.setDimensao(dimensao);
        i.setMaterial(material);
        i.setDataPeriodo(dataPeriodo);
        i.setDoador(doador);
        i.setAtivo(true);
        i.setDataCadastro(LocalDate.now());
        i.getAutores().addAll(autores);
        i.getAssuntos().addAll(assuntos);
        itemRepo.save(i);
    }
}
