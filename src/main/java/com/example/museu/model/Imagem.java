package com.example.museu.model;

import jakarta.persistence.*;

@Entity
@Table(name = "imagem")
public class Imagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 500)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public Imagem() {}

    public Imagem(String url, Item item) {
        this.url = url;
        this.item = item;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }
}
