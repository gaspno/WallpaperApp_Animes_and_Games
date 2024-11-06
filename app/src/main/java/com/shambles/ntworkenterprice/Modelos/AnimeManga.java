package com.shambles.ntworkenterprice.Modelos;

public class AnimeManga {

    private String titulo,description,texto;
    private String imageUrl;

    public AnimeManga(String titulo, String description, String texto, String imageUrl) {
        this.titulo = titulo;
        this.description = description;
        this.texto = texto;
        this.imageUrl = imageUrl;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
