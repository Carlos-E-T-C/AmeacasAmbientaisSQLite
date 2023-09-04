// Classe que representa uma ameaça ambiental. As instâncias dessa classe
// armazenam informações sobre o ID, endereço, data e descrição da ameaça.
package br.feevale.ameacasambientaissqlite;

import java.io.Serializable;

public class AmeacaAmbiental implements Serializable {

    private long id;
    private String endereco;
    private String data;
    private String descricao;

    // Construtor padrão sem argumentos
    public AmeacaAmbiental() {
    }

    // Construtor com argumentos para inicializar os dados da ameaça.
    public AmeacaAmbiental(String endereco, String data, String descricao) {
        this.endereco = endereco;
        this.data = data;
        this.descricao = descricao;
    }

    // Métodos getters e setters para acessar e modificar os atributos da ameaça.
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
