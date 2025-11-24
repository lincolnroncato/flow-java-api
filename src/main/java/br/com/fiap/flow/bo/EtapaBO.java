package br.com.fiap.flow.bo;

import br.com.fiap.flow.dao.EtapaDAO;
import br.com.fiap.flow.exception.BusinessException;
import br.com.fiap.flow.to.EtapaTO;

import java.util.List;

public class EtapaBO {

    private EtapaDAO etapaDAO;

    public EtapaBO() {
        etapaDAO = new EtapaDAO();
    }

    public List<EtapaTO> findAll() {
        return etapaDAO.findAll();
    }

    public EtapaTO findByCodigo(Long codigo) {
        if (codigo == null) {
            throw new BusinessException("Código da etapa não informado.");
        }
        return etapaDAO.findByCodigo(codigo);
    }

    public List<EtapaTO> findByProcesso(Long codProcesso) {
        if (codProcesso == null) {
            throw new BusinessException("Código do processo não informado.");
        }
        return etapaDAO.findByProcesso(codProcesso);
    }

    public EtapaTO save(EtapaTO etapa) {
        validar(etapa);

        boolean existe = etapaDAO.existsByProcessoAndOrdem(
                etapa.getCodProcesso(),
                etapa.getOrdem(),
                null
        );

        if (existe) {
            throw new BusinessException("Já existe uma etapa com essa ordem para este processo.");
        }

        return etapaDAO.save(etapa);
    }

    public EtapaTO update(EtapaTO etapa) {
        validar(etapa);

        if (etapa.getCodigo() == null) {
            throw new BusinessException("Código da etapa é obrigatório para atualizar.");
        }

        // Garante existência antes de atualizar
        EtapaTO existente = etapaDAO.findByCodigo(etapa.getCodigo());
        if (existente == null) {
            throw new BusinessException("Etapa não encontrada para atualização.");
        }

        boolean existe = etapaDAO.existsByProcessoAndOrdem(
                etapa.getCodProcesso(),
                etapa.getOrdem(),
                etapa.getCodigo()
        );

        if (existe) {
            throw new BusinessException("Já existe uma etapa com essa ordem para este processo.");
        }

        return etapaDAO.update(etapa);
    }

    public boolean delete(Long codigo) {
        if (codigo == null) {
            throw new BusinessException("Código da etapa não informado.");
        }

        // Garante existência antes de excluir
        EtapaTO existente = etapaDAO.findByCodigo(codigo);
        if (existente == null) {
            throw new BusinessException("Etapa não encontrada para exclusão.");
        }

        return etapaDAO.delete(codigo);
    }

    private void validar(EtapaTO etapa) {
        if (etapa == null) {
            throw new BusinessException("Etapa não pode ser nula.");
        }

        if (etapa.getCodProcesso() == null) {
            throw new BusinessException("Etapa precisa estar vinculada a um processo.");
        }

        if (etapa.getOrdem() == null || etapa.getOrdem() <= 0) {
            throw new BusinessException("Ordem da etapa inválida.");
        }

        if (etapa.getTitulo() == null || etapa.getTitulo().trim().equals("")) {
            throw new BusinessException("Título da etapa é obrigatório.");
        }
    }
}
