package br.com.fiap.flow.dao;

import br.com.fiap.flow.factory.ConnectionFactory;
import br.com.fiap.flow.to.ProcessoTO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProcessoDAO {

    public List<ProcessoTO> findAll() {
        List<ProcessoTO> processos = new ArrayList<>();

        String sql = "SELECT codigo, titulo, descricao, data_criacao " +
                     "FROM DDD_PROCESSOS ORDER BY codigo";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ProcessoTO processo = new ProcessoTO();
                processo.setCodigo(rs.getLong("codigo"));
                processo.setTitulo(rs.getString("titulo"));
                processo.setDescricao(rs.getString("descricao"));

                Date data = rs.getDate("data_criacao");
                if (data != null) {
                    processo.setDataCriacao(data.toLocalDate());
                }

                processos.add(processo);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar processos.", e);
        }

        return processos;
    }

    public ProcessoTO findByCodigo(Long codigo) {
        ProcessoTO processo = null;

        String sql = "SELECT codigo, titulo, descricao, data_criacao " +
                     "FROM DDD_PROCESSOS WHERE codigo = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    processo = new ProcessoTO();
                    processo.setCodigo(rs.getLong("codigo"));
                    processo.setTitulo(rs.getString("titulo"));
                    processo.setDescricao(rs.getString("descricao"));

                    Date dataCriacao = rs.getDate("data_criacao");
                    if (dataCriacao != null) {
                        processo.setDataCriacao(dataCriacao.toLocalDate());
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar processo por código.", e);
        }

        return processo;
    }

    public ProcessoTO save(ProcessoTO processo) {
        String sql = "INSERT INTO DDD_PROCESSOS " +
                     "(codigo, titulo, descricao, data_criacao) " +
                     "VALUES (DDD_PROCESSOS_SEQ.NEXTVAL, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, processo.getTitulo());
            ps.setString(2, processo.getDescricao());

            if (processo.getDataCriacao() != null) {
                ps.setDate(3, Date.valueOf(processo.getDataCriacao()));
            } else {
                ps.setDate(3, null);
            }

            ps.executeUpdate();

            try (PreparedStatement psSeq = conn.prepareStatement("SELECT DDD_PROCESSOS_SEQ.CURRVAL FROM DUAL");
                 ResultSet rs = psSeq.executeQuery()) {

                if (rs.next()) {
                    processo.setCodigo(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar processo.", e);
        }

        return processo;
    }

    public ProcessoTO update(ProcessoTO processo) {
        String sql = "UPDATE DDD_PROCESSOS " +
                     "SET titulo = ?, descricao = ?, data_criacao = ? " +
                     "WHERE codigo = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, processo.getTitulo());
            ps.setString(2, processo.getDescricao());

            if (processo.getDataCriacao() != null) {
                ps.setDate(3, Date.valueOf(processo.getDataCriacao()));
            } else {
                ps.setDate(3, null);
            }

            ps.setLong(4, processo.getCodigo());

            int linhas = ps.executeUpdate();
            if (linhas == 0) {
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar processo.", e);
        }

        return processo;
    }

    public boolean delete(Long codigo) {
        String sql = "DELETE FROM DDD_PROCESSOS WHERE codigo = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, codigo);
            int linhas = ps.executeUpdate();
            return linhas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar processo.", e);
        }
    }
}
