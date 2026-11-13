package br.com.starlog.model;

/** CAMADA MODEL - Carga (versão com constantes nomeadas e validação por métodos auxiliares). */
public class Carga {
    private static final double TAXA_BASE = 0.02;
    private static final double SOBRETAXA_RISCO = 150.0;
    private static final double LIMITE_PESO_KG = 100.0;
    private static final String CATEGORIA_PERIGOSA = "PERIGOSA";

    private final String codigoRastreio;
    private final String categoria;
    private final double peso;
    private final double valorDeclarado;

    public Carga(String codigoRastreio, String categoria, double peso, double valorDeclarado) {
        this.codigoRastreio = textoObrigatorio(codigoRastreio, "Código de rastreio não pode ser nulo ou vazio.");
        this.categoria = textoObrigatorio(categoria, "Categoria não pode ser nula ou vazia.");
        this.peso = positivo(peso, "Peso deve ser estritamente positivo (maior que zero).");
        this.valorDeclarado = positivo(valorDeclarado, "Valor declarado deve ser maior que zero.");
    }

    private static String textoObrigatorio(String v, String erro) {
        if (v == null || v.isBlank()) {
            throw new IllegalArgumentException(erro);
        }
        return v.strip().toUpperCase();
    }

    private static double positivo(double v, String erro) {
        if (Double.isNaN(v) || v <= 0) {
            throw new IllegalArgumentException(erro);
        }
        return v;
    }

    public String getCodigoRastreio() {
        return codigoRastreio;
    }

    public String getCategoria() {
        return categoria;
    }

    public double getPeso() {
        return peso;
    }

    public double getValorDeclarado() {
        return valorDeclarado;
    }

    public boolean exigeSobretaxa() {
        return CATEGORIA_PERIGOSA.equals(categoria) || peso > LIMITE_PESO_KG;
    }

    public double calcularSeguro() {
        return valorDeclarado * TAXA_BASE + (exigeSobretaxa() ? SOBRETAXA_RISCO : 0.0);
    }

    @Override
    public String toString() {
        return String.format("[%s] Categoria: %s | Peso: %.2f kg | Valor: R$ %.2f | Seguro: R$ %.2f",
                codigoRastreio, categoria, peso, valorDeclarado, calcularSeguro());
    }
}
