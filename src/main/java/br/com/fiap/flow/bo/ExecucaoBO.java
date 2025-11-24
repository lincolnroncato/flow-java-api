package br.com.fiap.flow.bo;

import br.com.fiap.flow.dao.EtapaDAO;
import br.com.fiap.flow.dao.ExecucaoDAO;
import br.com.fiap.flow.exception.BusinessException;
import br.com.fiap.flow.to.EtapaTO;
import br.com.fiap.flow.to.ExecucaoTO;
import java.time.LocalDate;
import java.util.List;

public class ExecucaoBO {

    private ExecucaoDAO execucaoDAO;
    private EtapaDAO etapaDAO;

    public ExecucaoBO() {
        execucaoDAO = new ExecucaoDAO();
        etapaDAO = new EtapaDAO();
    }

    public List<ExecucaoTO> findAll() {
        return execucaoDAO.findAll();
    }

    public ExecucaoTO findByCodigo(Long codigo) {
        if (codigo == null) {
            throw new BusinessException("Código da execução não informado.");
        }
        return execucaoDAO.findByCodigo(codigo);
    }

    public List<ExecucaoTO> findByUsuario(Long codUsuario) {
        if (codUsuario == null) {
            throw new BusinessException("Código do usuário não informado.");
        }
        return execucaoDAO.findByUsuario(codUsuario);
    }

    public ExecucaoTO save(ExecucaoTO execucao) {
        if (execucao == null) {
            throw new BusinessException("Execução não pode ser nula.");
        }

        if (execucao.getCodUsuario() == null) {
            throw new BusinessException("Usuário é obrigatório.");
        }

        if (execucao.getCodEtapa() == null) {
            throw new BusinessException("Etapa é obrigatória.");
        }

        if (execucao.getStatus() == null || execucao.getStatus().trim().equals("")) {
            throw new BusinessException("Status é obrigatório.");
        }

        String status = execucao.getStatus();

        if (!"PENDENTE".equals(status) && !"EM_ANDAMENTO".equals(status)) {
            throw new BusinessException("Status inicial deve ser PENDENTE ou EM_ANDAMENTO.");
        }

        return execucaoDAO.save(execucao);
    }

    public ExecucaoTO update(ExecucaoTO execucao) {
        if (execucao == null || execucao.getCodigo() == null) {
            throw new BusinessException("Código da execução é obrigatório para atualizar.");
        }

        ExecucaoTO execucaoExistente = execucaoDAO.findByCodigo(execucao.getCodigo());

        if (execucaoExistente == null) {
            throw new BusinessException("Execução não encontrada.");
        }

        String novoStatus = execucao.getStatus();
        String statusAtual = execucaoExistente.getStatus();

        if (novoStatus == null || novoStatus.trim().equals("")) {
            throw new BusinessException("Status é obrigatório.");
        }

        if ("CONCLUIDA".equals(novoStatus)) {
            if ("PENDENTE".equals(statusAtual) && execucaoExistente.getDataExecucao() == null) {
                throw new BusinessException("Não dá pra concluir uma execução pendente sem data de execução.");
            }
        }

        return execucaoDAO.update(execucao);
    }

    public boolean delete(Long codigo) {
        if (codigo == null) {
            throw new BusinessException("Código da execução não informado.");
        }
        return execucaoDAO.delete(codigo);
    }

    public ExecucaoTO iniciar(Long codProcesso, Long codUsuario) {
        if (codProcesso == null) {
            throw new BusinessException("Código do processo é obrigatório.");
        }

        if (codUsuario == null) {
            throw new BusinessException("Código do usuário é obrigatório.");
        }

        ExecucaoTO execucaoExistente = execucaoDAO.findByProcessoAndUsuario(codProcesso, codUsuario);

        if (execucaoExistente != null) {
            throw new BusinessException("Já existe uma execução em andamento para este usuário e processo.");
        }

        EtapaTO primeiraEtapa = etapaDAO.findPrimeiraEtapaDoProcesso(codProcesso);

        if (primeiraEtapa == null) {
            throw new BusinessException("Processo não possui etapas cadastradas.");
        }

        return execucaoDAO.iniciarExecucao(codProcesso, codUsuario, primeiraEtapa.getCodigo());
    }

    public ExecucaoTO concluirEtapa(Long codExecucao, Long codEtapa) {
        if (codExecucao == null) {
            throw new BusinessException("Código da execução é obrigatório.");
        }

        if (codEtapa == null) {
            throw new BusinessException("Código da etapa é obrigatório.");
        }

        ExecucaoTO execucao = execucaoDAO.findByCodigo(codExecucao);

        if (execucao == null) {
            throw new BusinessException("Execução não encontrada.");
        }

        if (!execucao.getCodEtapa().equals(codEtapa)) {
            throw new BusinessException("Etapa informada não corresponde à etapa atual da execução.");
        }

        if (!"EM_ANDAMENTO".equals(execucao.getStatus())) {
            throw new BusinessException("Apenas execuções em andamento podem ter etapas concluídas.");
        }

        EtapaTO etapaAtual = etapaDAO.findByCodigo(codEtapa);

        if (etapaAtual == null) {
            throw new BusinessException("Etapa não encontrada.");
        }

        EtapaTO proximaEtapa = etapaDAO.findProximaEtapa(etapaAtual.getCodProcesso(), etapaAtual.getOrdem());

        String novoStatus;
        Long novaEtapa;

        if (proximaEtapa == null) {
            novoStatus = "CONCLUIDA";
            novaEtapa = codEtapa;
        } else {
            novoStatus = "EM_ANDAMENTO";
            novaEtapa = proximaEtapa.getCodigo();
        }

        return execucaoDAO.atualizarEtapaAtual(codExecucao, novaEtapa, novoStatus, LocalDate.now());
    }

    public ExecucaoTO finalizar(Long codExecucao) {
        if (codExecucao == null) {
            throw new BusinessException("Código da execução é obrigatório.");
        }

        ExecucaoTO execucao = execucaoDAO.findByCodigo(codExecucao);

        if (execucao == null) {
            throw new BusinessException("Execução não encontrada.");
        }

        if ("CONCLUIDA".equals(execucao.getStatus())) {
            throw new BusinessException("Execução já está concluída.");
        }

        return execucaoDAO.atualizarEtapaAtual(codExecucao, execucao.getCodEtapa(), "CONCLUIDA", LocalDate.now());
    }

    public ExecucaoTO findByProcessoAndUsuario(Long codProcesso, Long codUsuario) {
        if (codProcesso == null) {
            throw new BusinessException("Código do processo é obrigatório.");
        }

        if (codUsuario == null) {
            throw new BusinessException("Código do usuário é obrigatório.");
        }

        return execucaoDAO.findByProcessoAndUsuario(codProcesso, codUsuario);
    }
}
