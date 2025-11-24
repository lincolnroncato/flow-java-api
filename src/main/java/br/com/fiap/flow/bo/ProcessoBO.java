package br.com.fiap.flow.bo;

import br.com.fiap.flow.dao.ProcessoDAO;
import br.com.fiap.flow.exception.BusinessException;
import br.com.fiap.flow.to.ProcessoTO;

import java.util.List;

public class ProcessoBO {

    private ProcessoDAO processoDAO;

    public ProcessoBO() {
        processoDAO = new ProcessoDAO();
    }

    public List<ProcessoTO> findAll() {
        return processoDAO.findAll();
    }

    public ProcessoTO findByCodigo(Long codigo) {
        if (codigo == null) {
            throw new BusinessException("Código do processo não informado.");
        }
        return processoDAO.findByCodigo(codigo);
    }

    public ProcessoTO save(ProcessoTO processo) {
        if (processo == null) {
            throw new BusinessException("Processo não pode ser nulo.");
        }

        if (processo.getTitulo() == null || processo.getTitulo().trim().equals("")) {
            throw new BusinessException("Título do processo é obrigatório.");
        }

        if (processo.getDescricao() == null || processo.getDescricao().trim().equals("")) {
            throw new BusinessException("Descrição do processo é obrigatória.");
        }

        return processoDAO.save(processo);
    }

    public ProcessoTO update(ProcessoTO processo) {
        if (processo == null || processo.getCodigo() == null) {
            throw new BusinessException("Código do processo é obrigatório para atualizar.");
        }

        if (processo.getTitulo() == null || processo.getTitulo().trim().equals("")) {
            throw new BusinessException("Título do processo é obrigatório.");
        }

        if (processo.getDescricao() == null || processo.getDescricao().trim().equals("")) {
            throw new BusinessException("Descrição do processo é obrigatória.");
        }

        // garante que o processo existe antes de atualizar
        ProcessoTO existente = processoDAO.findByCodigo(processo.getCodigo());
        if (existente == null) {
            throw new BusinessException("Processo não encontrado para atualização.");
        }

        return processoDAO.update(processo);
    }

    public boolean delete(Long codigo) {
        if (codigo == null) {
            throw new BusinessException("Código do processo não informado.");
        }

        // garante que existe antes de excluir
        ProcessoTO existente = processoDAO.findByCodigo(codigo);
        if (existente == null) {
            throw new BusinessException("Processo não encontrado para exclusão.");
        }

        return processoDAO.delete(codigo);
    }
}
