package web.controlevacinacao.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import web.controlevacinacao.service.RelatoriosService;

@Controller
public class RelatoriosController {

    private static final Logger logger = LoggerFactory.getLogger(RelatoriosController.class);

    private RelatoriosService relatorioService;

    public RelatoriosController(RelatoriosService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @GetMapping("/relatorios/todospedidos")
    public ResponseEntity<byte[]> gerarRelatorioTodosPedidos() {
        logger.debug("Gerando relatório de todos os pedidos");

        byte[] relatorio = relatorioService.gerarRelatorioTodosPedidos();

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Pedidos.pdf")
                .body(relatorio);
    }

}