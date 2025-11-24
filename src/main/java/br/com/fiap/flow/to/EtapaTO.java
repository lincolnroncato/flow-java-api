package br.com.fiap.flow.to;

public class EtapaTO {

    private Long codigo;
    private Long codProcesso;
    private String titulo;
    private String descricao;
    private Integer ordem;
    private Integer tempoEstimadoMin;

    public EtapaTO() {
    }

    public EtapaTO(Long codigo, Long codProcesso, String titulo, String descricao,
                   Integer ordem, Integer tempoEstimadoMin) {
        this.codigo = codigo;
        this.codProcesso = codProcesso;
        this.titulo = titulo;
        this.descricao = descricao;
        this.ordem = ordem;
        this.tempoEstimadoMin = tempoEstimadoMin;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public Long getCodProcesso() {
        return codProcesso;
    }

    public void setCodProcesso(Long codProcesso) {
        this.codProcesso = codProcesso;
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

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public Integer getTempoEstimadoMin() {
        return tempoEstimadoMin;
    }

    public void setTempoEstimadoMin(Integer tempoEstimadoMin) {
        this.tempoEstimadoMin = tempoEstimadoMin;
    }
}
