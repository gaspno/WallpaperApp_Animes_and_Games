package com.shambles.ntworkenterprice.Modelos;

public class GeneroAnimeManga {


    private int imageUrl;
    private String titulo;//,HtmlUrl;

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public GeneroAnimeManga(int imageUrl, String titulo) {
        this.imageUrl = imageUrl;
        this.titulo = titulo;
    }
}
