package com.example.museu.model;

import jakarta.persistence.*;
import org.hibernate.annotations.BatchSize;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "item")
public class Item {

    public static final int LIVRO      = 1;
    public static final int REVISTA    = 2;
    public static final int JORNAL     = 3;
    public static final int PERIODICO  = 4;
    public static final int HISTORICO  = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer tipo;

    @Column(nullable = false, length = 300)
    private String titulo;

    @Column(name = "titulo_original", length = 300)
    private String tituloOriginal;

    @Column(length = 20)
    private String isbn;

    @Column(length = 100)
    private String edicao;

    @Column(length = 200)
    private String localizacao;

    @Column(columnDefinition = "TEXT")
    private String notas;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(length = 100)
    private String paginacao;

    @Column(length = 100)
    private String volume;

    private Integer numero;

    @Column(name = "local_publicacao", length = 100)
    private String localPublicacao;

    @Column(length = 150)
    private String editora;

    @Column(name = "data_publicacao")
    private LocalDate dataPublicacao;

    @Column(length = 200)
    private String dimensao;

    @Column(length = 200)
    private String material;

    @Column(name = "data_periodo", length = 100)
    private String dataPeriodo;

    @Column(length = 200)
    private String doador;

    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;

    @Column(nullable = false)
    private Boolean ativo = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colecao_id")
    private Colecao colecao;

    @BatchSize(size = 25)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "item_autor",
        joinColumns = @JoinColumn(name = "item_id"),
        inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private List<Autor> autores = new ArrayList<>();

    @BatchSize(size = 25)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "assunto_item",
        joinColumns = @JoinColumn(name = "item_id"),
        inverseJoinColumns = @JoinColumn(name = "assunto_id")
    )
    private List<Assunto> assuntos = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Imagem> imagens = new ArrayList<>();

    @Transient
    private String autoresTexto;

    @Transient
    private String assuntosTexto;

    @Transient
    private String imagensUrls;

    public Item() {
        this.dataCadastro = LocalDate.now();
        this.ativo = true;
    }

    public String getTipoLabel() {
        if (tipo == null) return "";
        return switch (tipo) {
            case LIVRO     -> "Livro";
            case REVISTA   -> "Revista";
            case JORNAL    -> "Jornal";
            case PERIODICO -> "Periódico";
            case HISTORICO -> "Histórico";
            default        -> "Outro";
        };
    }

    public boolean isBibliografico() {
        return tipo != null && tipo != HISTORICO;
    }

    // Getters e Setters

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getTipo() { return tipo; }
    public void setTipo(Integer tipo) { this.tipo = tipo; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getTituloOriginal() { return tituloOriginal; }
    public void setTituloOriginal(String tituloOriginal) { this.tituloOriginal = tituloOriginal; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getEdicao() { return edicao; }
    public void setEdicao(String edicao) { this.edicao = edicao; }

    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getPaginacao() { return paginacao; }
    public void setPaginacao(String paginacao) { this.paginacao = paginacao; }

    public String getVolume() { return volume; }
    public void setVolume(String volume) { this.volume = volume; }

    public Integer getNumero() { return numero; }
    public void setNumero(Integer numero) { this.numero = numero; }

    public String getLocalPublicacao() { return localPublicacao; }
    public void setLocalPublicacao(String localPublicacao) { this.localPublicacao = localPublicacao; }

    public String getEditora() { return editora; }
    public void setEditora(String editora) { this.editora = editora; }

    public LocalDate getDataPublicacao() { return dataPublicacao; }
    public void setDataPublicacao(LocalDate dataPublicacao) { this.dataPublicacao = dataPublicacao; }

    public String getDimensao() { return dimensao; }
    public void setDimensao(String dimensao) { this.dimensao = dimensao; }

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }

    public String getDataPeriodo() { return dataPeriodo; }
    public void setDataPeriodo(String dataPeriodo) { this.dataPeriodo = dataPeriodo; }

    public String getDoador() { return doador; }
    public void setDoador(String doador) { this.doador = doador; }

    public LocalDate getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDate dataCadastro) { this.dataCadastro = dataCadastro; }

    public Colecao getColecao() { return colecao; }
    public void setColecao(Colecao colecao) { this.colecao = colecao; }

    public List<Autor> getAutores() { return autores; }
    public void setAutores(List<Autor> autores) { this.autores = autores; }

    public List<Assunto> getAssuntos() { return assuntos; }
    public void setAssuntos(List<Assunto> assuntos) { this.assuntos = assuntos; }

    public List<Imagem> getImagens() { return imagens; }
    public void setImagens(List<Imagem> imagens) { this.imagens = imagens; }

    public String getAutoresTexto() { return autoresTexto; }
    public void setAutoresTexto(String autoresTexto) { this.autoresTexto = autoresTexto; }

    public String getAssuntosTexto() { return assuntosTexto; }
    public void setAssuntosTexto(String assuntosTexto) { this.assuntosTexto = assuntosTexto; }

    public String getImagensUrls() { return imagensUrls; }
    public void setImagensUrls(String imagensUrls) { this.imagensUrls = imagensUrls; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}
