package br.com.fiap.flow.to;

import java.time.LocalDate;

public class ExecucaoTO {

    private Long codigo;
    private Long codUsuario;
    private Long codProcesso;
    private Long codEtapa;
    private LocalDate dataExecucao;
    private String status;
    private String observacao;

    public ExecucaoTO() {
    }

    public ExecucaoTO(Long codigo, Long codUsuario, Long codProcesso, Long codEtapa,
                      LocalDate dataExecucao, String status, String observacao) {
        this.codigo = codigo;
        this.codUsuario = codUsuario;
        this.codProcesso = codProcesso;
        this.codEtapa = codEtapa;
        this.dataExecucao = dataExecucao;
        this.status = status;
        this.observacao = observacao;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public Long getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(Long codUsuario) {
        this.codUsuario = codUsuario;
    }

    public Long getCodProcesso() {
        return codProcesso;
    }

    public void setCodProcesso(Long codProcesso) {
        this.codProcesso = codProcesso;
    }

    public Long getCodEtapa() {
        return codEtapa;
    }

    public void setCodEtapa(Long codEtapa) {
        this.codEtapa = codEtapa;
    }

    public LocalDate getDataExecucao() {
        return dataExecucao;
    }

    public void setDataExecucao(LocalDate dataExecucao) {
        this.dataExecucao = dataExecucao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
