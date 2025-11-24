package br.com.fiap.flow.dao;

import br.com.fiap.flow.factory.ConnectionFactory;
import br.com.fiap.flow.to.ExecucaoTO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExecucaoDAO {

    public List<ExecucaoTO> findAll() {
        List<ExecucaoTO> execucoes = new ArrayList<>();

        String sql = "SELECT codigo, cod_usuario, cod_processo, cod_etapa, data_execucao, status, observacao " +
                     "FROM DDD_EXECUCOES ORDER BY codigo";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ExecucaoTO execucao = new ExecucaoTO();
                execucao.setCodigo(rs.getLong("codigo"));
                execucao.setCodUsuario(rs.getLong("cod_usuario"));
                
                if (rs.getObject("cod_processo") != null) {
                    execucao.setCodProcesso(rs.getLong("cod_processo"));
                }
                
                execucao.setCodEtapa(rs.getLong("cod_etapa"));

                Date dataExecucao = rs.getDate("data_execucao");
                if (dataExecucao != null) {
                    execucao.setDataExecucao(dataExecucao.toLocalDate());
                }

                execucao.setStatus(rs.getString("status"));
                execucao.setObservacao(rs.getString("observacao"));

                execucoes.add(execucao);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar execuções.", e);
        }

        return execucoes;
    }

    public ExecucaoTO findByCodigo(Long codigo) {
        ExecucaoTO execucao = null;

        String sql = "SELECT codigo, cod_usuario, cod_processo, cod_etapa, data_execucao, status, observacao " +
                     "FROM DDD_EXECUCOES WHERE codigo = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    execucao = new ExecucaoTO();
                    execucao.setCodigo(rs.getLong("codigo"));
                    execucao.setCodUsuario(rs.getLong("cod_usuario"));
                    
                    if (rs.getObject("cod_processo") != null) {
                        execucao.setCodProcesso(rs.getLong("cod_processo"));
                    }
                    
                    execucao.setCodEtapa(rs.getLong("cod_etapa"));

                    Date dataExecucao = rs.getDate("data_execucao");
                    if (dataExecucao != null) {
                        execucao.setDataExecucao(dataExecucao.toLocalDate());
                    }

                    execucao.setStatus(rs.getString("status"));
                    execucao.setObservacao(rs.getString("observacao"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar execução por código.", e);
        }

        return execucao;
    }

    public List<ExecucaoTO> findByUsuario(Long codUsuario) {
        List<ExecucaoTO> execucoes = new ArrayList<>();

        String sql = "SELECT codigo, cod_usuario, cod_processo, cod_etapa, data_execucao, status, observacao " +
                     "FROM DDD_EXECUCOES WHERE cod_usuario = ? ORDER BY codigo";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, codUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ExecucaoTO execucao = new ExecucaoTO();
                    execucao.setCodigo(rs.getLong("codigo"));
                    execucao.setCodUsuario(rs.getLong("cod_usuario"));
                    
                    if (rs.getObject("cod_processo") != null) {
                        execucao.setCodProcesso(rs.getLong("cod_processo"));
                    }
                    
                    execucao.setCodEtapa(rs.getLong("cod_etapa"));

                    Date dataExecucao = rs.getDate("data_execucao");
                    if (dataExecucao != null) {
                        execucao.setDataExecucao(dataExecucao.toLocalDate());
                    }

                    execucao.setStatus(rs.getString("status"));
                    execucao.setObservacao(rs.getString("observacao"));

                    execucoes.add(execucao);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar execuções por usuário.", e);
        }

        return execucoes;
    }

    public ExecucaoTO save(ExecucaoTO execucao) {
        String sql = "INSERT INTO DDD_EXECUCOES " +
                     "(codigo, cod_usuario, cod_processo, cod_etapa, data_execucao, status, observacao) " +
                     "VALUES (DDD_EXECUCOES_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, execucao.getCodUsuario());
            
            if (execucao.getCodProcesso() != null) {
                ps.setLong(2, execucao.getCodProcesso());
            } else {
                ps.setNull(2, java.sql.Types.NUMERIC);
            }
            
            ps.setLong(3, execucao.getCodEtapa());

            if (execucao.getDataExecucao() != null) {
                ps.setDate(4, Date.valueOf(execucao.getDataExecucao()));
            } else {
                ps.setDate(4, null);
            }

            ps.setString(5, execucao.getStatus());
            ps.setString(6, execucao.getObservacao());

            ps.executeUpdate();

            try (PreparedStatement psSeq = conn.prepareStatement("SELECT DDD_EXECUCOES_SEQ.CURRVAL FROM DUAL");
                 ResultSet rs = psSeq.executeQuery()) {

                if (rs.next()) {
                    execucao.setCodigo(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar execução.", e);
        }

        return execucao;
    }

    public ExecucaoTO update(ExecucaoTO execucao) {
        String sql = "UPDATE DDD_EXECUCOES SET cod_usuario = ?, cod_processo = ?, cod_etapa = ?, data_execucao = ?, status = ?, observacao = ? " +
                     "WHERE codigo = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, execucao.getCodUsuario());
            
            if (execucao.getCodProcesso() != null) {
                ps.setLong(2, execucao.getCodProcesso());
            } else {
                ps.setNull(2, java.sql.Types.NUMERIC);
            }
            
            ps.setLong(3, execucao.getCodEtapa());

            if (execucao.getDataExecucao() != null) {
                ps.setDate(4, Date.valueOf(execucao.getDataExecucao()));
            } else {
                ps.setDate(4, null);
            }

            ps.setString(5, execucao.getStatus());
            ps.setString(6, execucao.getObservacao());
            ps.setLong(7, execucao.getCodigo());

            int linhas = ps.executeUpdate();
            if (linhas == 0) {
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar execução.", e);
        }

        return execucao;
    }

    public boolean delete(Long codigo) {
        String sql = "DELETE FROM DDD_EXECUCOES WHERE codigo = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, codigo);
            int linhas = ps.executeUpdate();
            return linhas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar execução.", e);
        }
    }

    public ExecucaoTO findByProcessoAndUsuario(Long codProcesso, Long codUsuario) {
        ExecucaoTO execucao = null;

        String sql = "SELECT codigo, cod_usuario, cod_processo, cod_etapa, data_execucao, status, observacao " +
                     "FROM DDD_EXECUCOES WHERE cod_processo = ? AND cod_usuario = ? AND status = 'EM_ANDAMENTO'";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, codProcesso);
            ps.setLong(2, codUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    execucao = new ExecucaoTO();
                    execucao.setCodigo(rs.getLong("codigo"));
                    execucao.setCodUsuario(rs.getLong("cod_usuario"));
                    
                    if (rs.getObject("cod_processo") != null) {
                        execucao.setCodProcesso(rs.getLong("cod_processo"));
                    }
                    
                    execucao.setCodEtapa(rs.getLong("cod_etapa"));

                    Date dataExecucao = rs.getDate("data_execucao");
                    if (dataExecucao != null) {
                        execucao.setDataExecucao(dataExecucao.toLocalDate());
                    }

                    execucao.setStatus(rs.getString("status"));
                    execucao.setObservacao(rs.getString("observacao"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar execução por processo e usuário.", e);
        }

        return execucao;
    }

    public ExecucaoTO iniciarExecucao(Long codProcesso, Long codUsuario, Long codEtapaInicial) {
        ExecucaoTO execucao = new ExecucaoTO();
        execucao.setCodProcesso(codProcesso);
        execucao.setCodUsuario(codUsuario);
        execucao.setCodEtapa(codEtapaInicial);
        execucao.setStatus("EM_ANDAMENTO");
        execucao.setDataExecucao(null);

        return save(execucao);
    }

    public ExecucaoTO atualizarEtapaAtual(Long codExecucao, Long novaEtapa, String status, java.time.LocalDate dataExecucao) {
        ExecucaoTO execucao = findByCodigo(codExecucao);
        
        if (execucao == null) {
            return null;
        }

        execucao.setCodEtapa(novaEtapa);
        execucao.setStatus(status);
        execucao.setDataExecucao(dataExecucao);

        return update(execucao);
    }
}
