package br.com.fiap.flow.dao;

import br.com.fiap.flow.factory.ConnectionFactory;
import br.com.fiap.flow.to.EtapaTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EtapaDAO {

    public List<EtapaTO> findAll() {
        List<EtapaTO> etapas = new ArrayList<>();

        String sql = "SELECT codigo, cod_processo, titulo, descricao, ordem, tempo_estimado_min " +
                     "FROM DDD_ETAPAS ORDER BY codigo";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                EtapaTO etapa = new EtapaTO();
                etapa.setCodigo(rs.getLong("codigo"));
                etapa.setCodProcesso(rs.getLong("cod_processo"));
                etapa.setTitulo(rs.getString("titulo"));
                etapa.setDescricao(rs.getString("descricao"));
                etapa.setOrdem(rs.getInt("ordem"));
                etapa.setTempoEstimadoMin(rs.getInt("tempo_estimado_min"));
                etapas.add(etapa);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar etapas.", e);
        }

        return etapas;
    }

    public EtapaTO findByCodigo(Long codigo) {
        EtapaTO etapa = null;

        String sql = "SELECT codigo, cod_processo, titulo, descricao, ordem, tempo_estimado_min " +
                     "FROM DDD_ETAPAS WHERE codigo = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    etapa = new EtapaTO();
                    etapa.setCodigo(rs.getLong("codigo"));
                    etapa.setCodProcesso(rs.getLong("cod_processo"));
                    etapa.setTitulo(rs.getString("titulo"));
                    etapa.setDescricao(rs.getString("descricao"));
                    etapa.setOrdem(rs.getInt("ordem"));
                    etapa.setTempoEstimadoMin(rs.getInt("tempo_estimado_min"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar etapa por código.", e);
        }

        return etapa;
    }

    public List<EtapaTO> findByProcesso(Long codProcesso) {
        List<EtapaTO> etapas = new ArrayList<>();

        String sql = "SELECT codigo, cod_processo, titulo, descricao, ordem, tempo_estimado_min " +
                     "FROM DDD_ETAPAS WHERE cod_processo = ? ORDER BY ordem";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, codProcesso);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    EtapaTO etapa = new EtapaTO();
                    etapa.setCodigo(rs.getLong("codigo"));
                    etapa.setCodProcesso(rs.getLong("cod_processo"));
                    etapa.setTitulo(rs.getString("titulo"));
                    etapa.setDescricao(rs.getString("descricao"));
                    etapa.setOrdem(rs.getInt("ordem"));
                    etapa.setTempoEstimadoMin(rs.getInt("tempo_estimado_min"));
                    etapas.add(etapa);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar etapas por processo.", e);
        }

        return etapas;
    }

    public EtapaTO save(EtapaTO etapa) {
        String sql = "INSERT INTO DDD_ETAPAS (codigo, cod_processo, titulo, descricao, ordem, tempo_estimado_min) " +
                     "VALUES (DDD_ETAPAS_SEQ.NEXTVAL, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, etapa.getCodProcesso());
            ps.setString(2, etapa.getTitulo());
            ps.setString(3, etapa.getDescricao());
            ps.setInt(4, etapa.getOrdem());
            ps.setInt(5, etapa.getTempoEstimadoMin());
            ps.executeUpdate();

            try (PreparedStatement psSeq = conn.prepareStatement("SELECT DDD_ETAPAS_SEQ.CURRVAL FROM DUAL");
                 ResultSet rs = psSeq.executeQuery()) {

                if (rs.next()) {
                    etapa.setCodigo(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar etapa.", e);
        }

        return etapa;
    }

    public EtapaTO update(EtapaTO etapa) {
        String sql = "UPDATE DDD_ETAPAS SET cod_processo = ?, titulo = ?, descricao = ?, ordem = ?, tempo_estimado_min = ? " +
                     "WHERE codigo = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, etapa.getCodProcesso());
            ps.setString(2, etapa.getTitulo());
            ps.setString(3, etapa.getDescricao());
            ps.setInt(4, etapa.getOrdem());
            ps.setInt(5, etapa.getTempoEstimadoMin());
            ps.setLong(6, etapa.getCodigo());

            int linhas = ps.executeUpdate();
            if (linhas == 0) {
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar etapa.", e);
        }

        return etapa;
    }

    public boolean delete(Long codigo) {
        String sql = "DELETE FROM DDD_ETAPAS WHERE codigo = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, codigo);
            int linhas = ps.executeUpdate();
            return linhas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar etapa.", e);
        }
    }

    public boolean existsByProcessoAndOrdem(Long codProcesso, Integer ordem, Long codigoExcluir) {
        String sql = "SELECT COUNT(*) FROM DDD_ETAPAS " +
                     "WHERE cod_processo = ? AND ordem = ? AND codigo <> ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, codProcesso);
            ps.setInt(2, ordem);
            ps.setLong(3, codigoExcluir != null ? codigoExcluir : -1);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar ordem duplicada.", e);
        }

        return false;
    }

    public EtapaTO findPrimeiraEtapaDoProcesso(Long codProcesso) {
        EtapaTO etapa = null;

        String sql = "SELECT codigo, cod_processo, titulo, descricao, ordem, tempo_estimado_min " +
                     "FROM DDD_ETAPAS WHERE cod_processo = ? ORDER BY ordem FETCH FIRST 1 ROW ONLY";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, codProcesso);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    etapa = new EtapaTO();
                    etapa.setCodigo(rs.getLong("codigo"));
                    etapa.setCodProcesso(rs.getLong("cod_processo"));
                    etapa.setTitulo(rs.getString("titulo"));
                    etapa.setDescricao(rs.getString("descricao"));
                    etapa.setOrdem(rs.getInt("ordem"));
                    etapa.setTempoEstimadoMin(rs.getInt("tempo_estimado_min"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar primeira etapa do processo.", e);
        }

        return etapa;
    }

    public EtapaTO findProximaEtapa(Long codProcesso, Integer ordemAtual) {
        EtapaTO etapa = null;

        String sql = "SELECT codigo, cod_processo, titulo, descricao, ordem, tempo_estimado_min " +
                     "FROM DDD_ETAPAS WHERE cod_processo = ? AND ordem > ? ORDER BY ordem FETCH FIRST 1 ROW ONLY";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, codProcesso);
            ps.setInt(2, ordemAtual);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    etapa = new EtapaTO();
                    etapa.setCodigo(rs.getLong("codigo"));
                    etapa.setCodProcesso(rs.getLong("cod_processo"));
                    etapa.setTitulo(rs.getString("titulo"));
                    etapa.setDescricao(rs.getString("descricao"));
                    etapa.setOrdem(rs.getInt("ordem"));
                    etapa.setTempoEstimadoMin(rs.getInt("tempo_estimado_min"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar próxima etapa.", e);
        }

        return etapa;
    }
}
