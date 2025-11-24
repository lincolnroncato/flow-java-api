package br.com.fiap.flow.bo;

import br.com.fiap.flow.dao.UsuarioDAO;
import br.com.fiap.flow.exception.BusinessException;
import br.com.fiap.flow.to.UsuarioTO;

import java.util.List;

public class UsuarioBO {

    private UsuarioDAO usuarioDAO;

    public UsuarioBO() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public List<UsuarioTO> findAll() {
        return usuarioDAO.findAll();
    }

    public UsuarioTO findByCodigo(Long codigo) {
        if (codigo == null) {
            throw new BusinessException("Código do usuário não informado.");
        }
        return usuarioDAO.findByCodigo(codigo);
    }

    public UsuarioTO save(UsuarioTO usuario) {
        validar(usuario);

        // (Opcional se quiser impedir duplicidade)
        // UsuarioTO existente = usuarioDAO.findByEmail(usuario.getEmail());
        // if (existente != null) {
        //     throw new BusinessException("Já existe um usuário com este e-mail.");
        // }

        return usuarioDAO.save(usuario);
    }

    public UsuarioTO update(UsuarioTO usuario) {
        if (usuario == null || usuario.getCodigo() == null) {
            throw new BusinessException("Código do usuário é obrigatório para atualizar.");
        }

        validar(usuario);

        UsuarioTO existente = usuarioDAO.findByCodigo(usuario.getCodigo());
        if (existente == null) {
            throw new BusinessException("Usuário não encontrado para atualização.");
        }

        return usuarioDAO.update(usuario);
    }

    public boolean delete(Long codigo) {
        if (codigo == null) {
            throw new BusinessException("Código do usuário não informado.");
        }

        UsuarioTO existente = usuarioDAO.findByCodigo(codigo);
        if (existente == null) {
            throw new BusinessException("Usuário não encontrado para exclusão.");
        }

        return usuarioDAO.delete(codigo);
    }

    private void validar(UsuarioTO usuario) {
        if (usuario == null) {
            throw new BusinessException("Usuário não pode ser nulo.");
        }

        if (usuario.getNome() == null || usuario.getNome().trim().equals("")) {
            throw new BusinessException("Nome do usuário é obrigatório.");
        }

        if (usuario.getEmail() == null || usuario.getEmail().trim().equals("")) {
            throw new BusinessException("E-mail do usuário é obrigatório.");
        }

        if (usuario.getCargo() == null || usuario.getCargo().trim().equals("")) {
            throw new BusinessException("Cargo do usuário é obrigatório.");
        }
    }
}
