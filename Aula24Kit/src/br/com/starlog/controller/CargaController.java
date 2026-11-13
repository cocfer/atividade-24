package br.com.starlog.controller;

import br.com.starlog.model.Carga;
import br.com.starlog.view.TelaCadastroCarga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** CAMADA CONTROLLER - converte tipos, aciona o Model e notifica a View. */
public class CargaController {

    private final List<Carga> repositorio = new ArrayList<>();

    public void cadastrarCarga(String codigoRaw, String categoriaRaw, String pesoRaw, String valorRaw,
                               TelaCadastroCarga tela) {
        try {
            double peso = lerNumero(pesoRaw, "Peso");
            double valor = lerNumero(valorRaw, "Valor declarado");

            Carga carga = new Carga(codigoRaw, categoriaRaw, peso, valor);
            repositorio.add(carga);

            if (tela != null) {
                tela.exibirMensagemSucesso("Carga cadastrada com sucesso!\n" + carga);
                tela.atualizarRelatorio(gerarTextoRelatorio());
            }
        } catch (NumberFormatException e) {
            avisarErro(tela, "Erro de formato numérico: Digite apenas números válidos para Peso e Valor!");
        } catch (IllegalArgumentException e) {
            avisarErro(tela, "Regra de Negócio Violada: " + e.getMessage());
        }
    }

    private double lerNumero(String bruto, String campo) {
        if (bruto == null || bruto.isBlank()) {
            throw new IllegalArgumentException(campo + " é obrigatório.");
        }
        return Double.parseDouble(bruto.strip().replace(',', '.'));
    }

    private void avisarErro(TelaCadastroCarga tela, String msg) {
        if (tela != null) {
            tela.exibirMensagemErro(msg);
        }
    }

    public String gerarTextoRelatorio() {
        if (repositorio.isEmpty()) {
            return "Nenhuma carga registrada na esteira.";
        }

        StringBuilder sb = new StringBuilder("--- RELATÓRIO OFICIAL DE DESPACHO ---\n");
        int n = 1;
        for (Carga c : repositorio) {
            sb.append(String.format("%02d. %s%n", n++, c));
        }
        double pesoTotal = repositorio.stream().mapToDouble(Carga::getPeso).sum();
        double seguroTotal = repositorio.stream().mapToDouble(Carga::calcularSeguro).sum();

        sb.append("-------------------------------------\n");
        sb.append(String.format("Total de Cargas: %d | Peso Total: %.2f kg | Seguro Acumulado: R$ %.2f%n",
                repositorio.size(), pesoTotal, seguroTotal));
        return sb.toString();
    }

    public List<Carga> getRepositorio() {
        return Collections.unmodifiableList(repositorio);
    }
}
