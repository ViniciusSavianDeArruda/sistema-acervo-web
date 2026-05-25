package com.example.museu.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "log")
public class Log {

    public static final int CRIAR   = 1;
    public static final int EDITAR  = 2;
    public static final int DELETAR = 3;
    public static final int ATIVAR = 4;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = true)
    private Item item;

    @Column(name = "item_titulo", length = 300)
    private String itemTitulo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = true)
    private Usuario usuario;

    @Column(nullable = false)
    private Integer operacao;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    public Log() {
        this.dataHora = LocalDateTime.now();
    }

    public String getOperacaoLabel() {
        if (operacao == null) return "";
        return switch (operacao) {
            case CRIAR   -> "Cadastro";
            case EDITAR  -> "Edição";
            case DELETAR -> "Exclusão";
            case ATIVAR -> "Ativação";
            default      -> "Operação";
        };
    }

    public String getDataHoraFormatada() {
        if (dataHora == null) return "—";
        return dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public String getOperacaoCssClass() {
        if (operacao == null) return "";
        return switch (operacao) {
            case CRIAR   -> "log-create";
            case EDITAR  -> "log-edit";
            case DELETAR -> "log-delete";
            case ATIVAR -> "log-ativate";
            default      -> "";
        };
    }

    // Getters e Setters

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }

    public String getItemTitulo() { return itemTitulo; }
    public void setItemTitulo(String itemTitulo) { this.itemTitulo = itemTitulo; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Integer getOperacao() { return operacao; }
    public void setOperacao(Integer operacao) { this.operacao = operacao; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
}
