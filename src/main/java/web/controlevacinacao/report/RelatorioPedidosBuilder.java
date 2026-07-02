package web.controlevacinacao.report;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JRDesignSubreport;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import org.springframework.stereotype.Component;
import net.sf.jasperreports.engine.design.JRDesignGroup;

@Component
public class RelatorioPedidosBuilder {

    public byte[] gerar(Connection conexao) throws JRException {
        JasperReport subreportItens = compilarSubreportItens();
        JasperReport reportPedidos = compilarReportPedidos();

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("SUBREPORT_ITENS", subreportItens);

        JasperPrint print = JasperFillManager.fillReport(reportPedidos, parametros, conexao);
        return JasperExportManager.exportReportToPdf(print);
    }

    // =========================================================================
    // SUBREPORT: lista os itens (produto, quantidade, preço, subtotal) de UM pedido
    // =========================================================================
    private JasperReport compilarSubreportItens() throws JRException {
        JasperDesign design = new JasperDesign();
        design.setName("relatoriositens");
        design.setPageWidth(495);
        design.setPageHeight(802);
        design.setColumnWidth(495);
        design.setLeftMargin(0);
        design.setRightMargin(0);
        design.setTopMargin(0);
        design.setBottomMargin(0);

        design.addStyle(criarEstilo("normal", 9f, false, true));
        design.addStyle(criarEstilo("negrito", 9f, true, false));
        design.addStyle(criarEstilo("nomeCliente", 12f, true, false));

        // Parâmetro recebido do relatório mestre: código do pedido atual
        JRDesignParameter parametroCodigoPedido = new JRDesignParameter();
        parametroCodigoPedido.setName("CODIGOPEDIDO");
        parametroCodigoPedido.setValueClass(Long.class);
        design.addParameter(parametroCodigoPedido);

        JRDesignQuery query = new JRDesignQuery();
        query.setText(
                "SELECT p.nome AS nome_produto, i.quantidade AS quantidade, "
                        + "p.preco AS preco_unitario, (i.quantidade * p.preco) AS subtotal "
                        + "FROM item_pedido i "
                        + "JOIN produto p ON p.codigo = i.codigo_produto "
                        + "WHERE i.codigo_pedido = $P{CODIGOPEDIDO} AND i.status = 'ATIVO' "
                        + "ORDER BY p.nome ASC");
        design.setQuery(query);

        design.addField(criarField("nome_produto", String.class));
        design.addField(criarField("quantidade", Integer.class));
        design.addField(criarField("preco_unitario", java.math.BigDecimal.class));
        design.addField(criarField("subtotal", java.math.BigDecimal.class));

        // Cabeçalho de colunas
        JRDesignBand columnHeader = new JRDesignBand();
        columnHeader.setHeight(16);
        columnHeader.addElement(criarTextoEstatico("Produto", 0, 0, 220, 16, "negrito", HorizontalTextAlignEnum.LEFT));
        columnHeader.addElement(criarTextoEstatico("Qtd", 220, 0, 50, 16, "negrito", HorizontalTextAlignEnum.RIGHT));
        columnHeader.addElement(criarTextoEstatico("Preço Unit.", 270, 0, 110, 16, "negrito", HorizontalTextAlignEnum.RIGHT));
        columnHeader.addElement(criarTextoEstatico("Subtotal", 380, 0, 115, 16, "negrito", HorizontalTextAlignEnum.RIGHT));
        design.setColumnHeader(columnHeader);

        // Detail: uma linha por item
        JRDesignBand detail = new JRDesignBand();
        detail.setHeight(14);
        detail.addElement(criarCampoTexto("$F{nome_produto}", 0, 0, 220, 14, "normal", HorizontalTextAlignEnum.LEFT, null));
        detail.addElement(criarCampoTexto("$F{quantidade}", 220, 0, 50, 14, "normal", HorizontalTextAlignEnum.RIGHT, null));
        detail.addElement(criarCampoTexto("$F{preco_unitario}", 270, 0, 110, 14, "normal", HorizontalTextAlignEnum.RIGHT, "R$ #,##0.00"));
        detail.addElement(criarCampoTexto("$F{subtotal}", 380, 0, 115, 14, "normal", HorizontalTextAlignEnum.RIGHT, "R$ #,##0.00"));

        ((net.sf.jasperreports.engine.design.JRDesignSection) design.getDetailSection()).addBand(detail);

        return JasperCompileManager.compileReport(design);
    }

    // =========================================================================
    // RELATÓRIO MESTRE: lista os pedidos, e para cada um chama o subreport de itens
    // =========================================================================
    private JasperReport compilarReportPedidos() throws JRException {
        JasperDesign design = new JasperDesign();
        design.setName("relatoriopedidos");
        design.setPageWidth(595);
        design.setPageHeight(842);
        design.setColumnWidth(535);
        design.setLeftMargin(30);
        design.setRightMargin(30);
        design.setTopMargin(20);
        design.setBottomMargin(20);

        design.addStyle(criarEstilo("normal", 10f, false, true));
        design.addStyle(criarEstilo("negrito", 10f, true, false));
        design.addStyle(criarEstilo("titulo", 18f, true, false));
        design.addStyle(criarEstilo("nomeCliente", 12f, true, false));

        // Parâmetro recebido do código Java: o subreport já compilado
        JRDesignParameter parametroSubreport = new JRDesignParameter();
        parametroSubreport.setName("SUBREPORT_ITENS");
        parametroSubreport.setValueClass(JasperReport.class);
        design.addParameter(parametroSubreport);

        JRDesignQuery query = new JRDesignQuery();
        query.setText(
                "SELECT ped.codigo AS codigo, ped.data AS data, cli.nome AS nome_cliente "
                        + "FROM pedido ped "
                        + "JOIN cliente cli ON cli.codigo = ped.codigo_cliente "
                        + "WHERE ped.status = 'ATIVO' "
                        + "ORDER BY cli.nome ASC, ped.data DESC, ped.codigo DESC");
        design.setQuery(query);

        design.addField(criarField("codigo", Long.class));
        design.addField(criarField("data", java.sql.Date.class));
        design.addField(criarField("nome_cliente", String.class));

        // Title
        JRDesignBand title = new JRDesignBand();
        title.setHeight(40);
        title.addElement(criarTextoEstatico("Relatório de Pedidos", 0, 0, 535, 30, "titulo", HorizontalTextAlignEnum.LEFT));
        design.setTitle(title);

        // Grupo: um cabeçalho por cliente, reunindo todos os pedidos dele
        JRDesignGroup grupoCliente = new JRDesignGroup();
        grupoCliente.setName("GrupoCliente");
        JRDesignExpression expressaoGrupo = new JRDesignExpression();
        expressaoGrupo.setText("$F{nome_cliente}");   // campo que define quando muda de grupo
        grupoCliente.setExpression(expressaoGrupo);

        JRDesignBand cabecalhoGrupo = new JRDesignBand();
        cabecalhoGrupo.setHeight(26);
        cabecalhoGrupo.addElement(criarCampoTextoConcatenado("\"Cliente: \" + $F{nome_cliente}",
                0, 6, 535, 18, "nomeCliente", HorizontalTextAlignEnum.LEFT));
        ((net.sf.jasperreports.engine.design.JRDesignSection) grupoCliente.getGroupHeaderSection())
                .addBand(cabecalhoGrupo);

        design.addGroup(grupoCliente);

        // Detail: cabeçalho do pedido (código, data, cliente) + subreport de itens
        JRDesignBand detail = new JRDesignBand();
        detail.setHeight(95);

        detail.addElement(criarCampoTextoConcatenado("\"Pedido #\" + $F{codigo}", 0, 4, 200, 16, "negrito", HorizontalTextAlignEnum.LEFT));
        detail.addElement(criarCampoTexto("$F{data}", 385, 4, 150, 16, "normal", HorizontalTextAlignEnum.RIGHT, "dd/MM/yyyy"));
        //detail.addElement(criarCampoTextoConcatenado("\"Cliente: \" + $F{nome_cliente}", 0, 20, 535, 16, "normal", HorizontalTextAlignEnum.LEFT));

        // Subreport: itens daquele pedido específico
        JRDesignSubreport subreport = new JRDesignSubreport(design);
        subreport.setX(10);
        subreport.setY(24);
        subreport.setWidth(495);
        subreport.setHeight(50);

        JRDesignExpression connectionExpression = new JRDesignExpression();
        connectionExpression.setText("$P{" + JRParameter.REPORT_CONNECTION + "}");
        subreport.setConnectionExpression(connectionExpression);

        JRDesignExpression subreportExpression = new JRDesignExpression();
        subreportExpression.setText("$P{SUBREPORT_ITENS}");
        subreport.setExpression(subreportExpression);

        // Passa o código do pedido atual (do mestre) como parâmetro pro subreport
        net.sf.jasperreports.engine.design.JRDesignSubreportParameter parametroCodigo =
                new net.sf.jasperreports.engine.design.JRDesignSubreportParameter();
        parametroCodigo.setName("CODIGOPEDIDO");
        JRDesignExpression expressaoCodigo = new JRDesignExpression();
        expressaoCodigo.setText("$F{codigo}");
        parametroCodigo.setExpression(expressaoCodigo);
        subreport.addParameter(parametroCodigo);

        detail.addElement(subreport);

        ((net.sf.jasperreports.engine.design.JRDesignSection) design.getDetailSection()).addBand(detail);

        return JasperCompileManager.compileReport(design);
    }

    // =========================================================================
    // Métodos auxiliares de construção
    // =========================================================================

    private JRDesignStyle criarEstilo(String nome, float tamanhoFonte, boolean negrito, boolean padrao) {
        JRDesignStyle estilo = new JRDesignStyle();
        estilo.setName(nome);
        estilo.setFontSize(tamanhoFonte);
        estilo.setBold(negrito);
        estilo.setDefault(padrao);
        return estilo;
    }

    private JRDesignField criarField(String nome, Class<?> tipo) {
        JRDesignField field = new JRDesignField();
        field.setName(nome);
        field.setValueClass(tipo);
        return field;
    }

    private JRDesignStaticText criarTextoEstatico(String texto, int x, int y, int largura, int altura,
                                                  String estilo, HorizontalTextAlignEnum alinhamento) {
        JRDesignStaticText elemento = new JRDesignStaticText();
        elemento.setText(texto);
        elemento.setX(x);
        elemento.setY(y);
        elemento.setWidth(largura);
        elemento.setHeight(altura);
        elemento.setStyleNameReference(estilo);
        elemento.setHorizontalTextAlign(alinhamento);
        return elemento;
    }

    private JRDesignTextField criarCampoTexto(String expressao, int x, int y, int largura, int altura,
                                              String estilo, HorizontalTextAlignEnum alinhamento, String padrao) {
        JRDesignTextField campo = new JRDesignTextField();
        campo.setX(x);
        campo.setY(y);
        campo.setWidth(largura);
        campo.setHeight(altura);
        campo.setStyleNameReference(estilo);
        campo.setHorizontalTextAlign(alinhamento);
        if (padrao != null) {
            campo.setPattern(padrao);
        }
        JRDesignExpression exp = new JRDesignExpression();
        exp.setText(expressao);
        campo.setExpression(exp);
        return campo;
    }

    private JRDesignTextField criarCampoTextoConcatenado(String expressao, int x, int y, int largura, int altura,
                                                         String estilo, HorizontalTextAlignEnum alinhamento) {
        return criarCampoTexto(expressao, x, y, largura, altura, estilo, alinhamento, null);
    }

}