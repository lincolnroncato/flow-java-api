package br.com.fiap.flow.to;

import java.time.LocalDate;

public class ProcessoTO {

    private Long codigo;
    private String titulo;
    private String descricao;
    private LocalDate dataCriacao;

    public ProcessoTO() {
    }

    public ProcessoTO(Long codigo, String titulo, String descricao, LocalDate dataCriacao) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
