package br.com.fiap.flow.dao;

import br.com.fiap.flow.factory.ConnectionFactory;
import br.com.fiap.flow.to.UsuarioTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public List<UsuarioTO> findAll() {
        List<UsuarioTO> usuarios = new ArrayList<>();

        String sql = "SELECT codigo, nome, email, cargo " +
                     "FROM DDD_USUARIOS ORDER BY codigo";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                UsuarioTO usuario = new UsuarioTO();
                usuario.setCodigo(rs.getLong("codigo"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setCargo(rs.getString("cargo"));
                usuarios.add(usuario);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuários.", e);
        }

        return usuarios;
    }

    public UsuarioTO findByCodigo(Long codigo) {
        UsuarioTO usuario = null;

        String sql = "SELECT codigo, nome, email, cargo " +
                     "FROM DDD_USUARIOS WHERE codigo = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new UsuarioTO();
                    usuario.setCodigo(rs.getLong("codigo"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setCargo(rs.getString("cargo"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por código.", e);
        }

        return usuario;
    }

    public UsuarioTO save(UsuarioTO usuario) {
        String sql = "INSERT INTO DDD_USUARIOS (codigo, nome, email, cargo) " +
                     "VALUES (DDD_USUARIOS_SEQ.NEXTVAL, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getCargo());
            ps.executeUpdate();

            try (PreparedStatement psSeq = conn.prepareStatement("SELECT DDD_USUARIOS_SEQ.CURRVAL FROM DUAL");
                 ResultSet rs = psSeq.executeQuery()) {

                if (rs.next()) {
                    usuario.setCodigo(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar usuário.", e);
        }

        return usuario;
    }

    public UsuarioTO update(UsuarioTO usuario) {
        String sql = "UPDATE DDD_USUARIOS SET nome = ?, email = ?, cargo = ? " +
                     "WHERE codigo = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getCargo());
            ps.setLong(4, usuario.getCodigo());

            int linhas = ps.executeUpdate();
            if (linhas == 0) {
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuário.", e);
        }

        return usuario;
    }

    public boolean delete(Long codigo) {
        String sql = "DELETE FROM DDD_USUARIOS WHERE codigo = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, codigo);
            int linhas = ps.executeUpdate();
            return linhas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar usuário.", e);
        }
    }
}
