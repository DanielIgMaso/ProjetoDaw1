package web.controlevacinacao.service;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import web.controlevacinacao.report.RelatorioPedidosBuilder;

@Service
public class RelatoriosService {

    private static final Logger logger = LoggerFactory.getLogger(RelatoriosService.class);

    private DataSource dataSource;
    private RelatorioPedidosBuilder relatorioPedidosBuilder;

    public RelatoriosService(DataSource dataSource, RelatorioPedidosBuilder relatorioPedidosBuilder) {
        this.dataSource = dataSource;
        this.relatorioPedidosBuilder = relatorioPedidosBuilder;
    }

    public byte[] gerarRelatorioTodosPedidos() {
        try (Connection conexao = dataSource.getConnection()) {
            return relatorioPedidosBuilder.gerar(conexao);
        } catch (SQLException e) {
            logger.error("Problemas na obtenção de uma conexão com o BD na geração de relatório: " + e);
        } catch (net.sf.jasperreports.engine.JRException e) {
            logger.error("Problemas no Jasper na geracao do PDF do relatório: " + e);
        }
        return null;
    }
}