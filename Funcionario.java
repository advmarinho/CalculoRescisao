import java.time.LocalDate;

public class Funcionario {
    private String nome;
    private LocalDate dataAdmissao;
    private LocalDate dataDemissao;
    private double salarioBase;
    private double outrosDescontos;
    private double feriasVencidas;
    private double indenizacaoAdicional;
    private double multaAtrasoPagamento;
    private TipoRescisao tipoRescisao;
    private int diasAvisoPrevio;

    public Funcionario(String nome, LocalDate dataAdmissao, LocalDate dataDemissao, double salarioBase, double outrosDescontos, double feriasVencidas, double indenizacaoAdicional, double multaAtrasoPagamento, TipoRescisao tipoRescisao, int diasAvisoPrevio) {
        this.nome = nome;
        this.dataAdmissao = dataAdmissao;
        this.dataDemissao = dataDemissao;
        this.salarioBase = salarioBase;
        this.outrosDescontos = outrosDescontos;
        this.feriasVencidas = feriasVencidas;
        this.indenizacaoAdicional = indenizacaoAdicional;
        this.multaAtrasoPagamento = multaAtrasoPagamento;
        this.tipoRescisao = tipoRescisao;
        this.diasAvisoPrevio = diasAvisoPrevio;
    }

    // Getters
    public String getNome() {
        return nome;
    }

    public LocalDate getDataAdmissao() {
        return dataAdmissao;
    }

    public LocalDate getDataDemissao() {
        return dataDemissao;
    }

    public double getSalarioBase() {
        return salarioBase;
    }

    public double getOutrosDescontos() {
        return outrosDescontos;
    }

    public double getFeriasVencidas() {
        return feriasVencidas;
    }

    public double getIndenizacaoAdicional() {
        return indenizacaoAdicional;
    }

    public double getMultaAtrasoPagamento() {
        return multaAtrasoPagamento;
    }

    public TipoRescisao getTipoRescisao() {
        return tipoRescisao;
    }

    public int getDiasAvisoPrevio() {
        return diasAvisoPrevio;
    }
}
