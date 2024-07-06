import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.EnumSet;

public class CalculoRescisao {
    private Funcionario funcionario;
    private double totalRescisao;
    private List<Funcionario> listaFuncionarios; // Múltiplas rescisões
    private Set<TipoRescisao> tiposRescisao; // Tipos de rescisão selecionados

    // Construtor
    public CalculoRescisao() {
        this.tiposRescisao = EnumSet.noneOf(TipoRescisao.class); // Inicializa o conjunto vazio
    }

    // Getters e Setters
    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public double getTotalRescisao() {
        return totalRescisao;
    }

    public void setTotalRescisao(double totalRescisao) {
        this.totalRescisao = totalRescisao;
    }

    public List<Funcionario> getListaFuncionarios() {
        return listaFuncionarios;
    }

    public void setListaFuncionarios(List<Funcionario> listaFuncionarios) {
        this.listaFuncionarios = listaFuncionarios;
    }

    public Set<TipoRescisao> getTiposRescisao() {
        return tiposRescisao;
    }

    public void setTiposRescisao(Set<TipoRescisao> tiposRescisao) {
        this.tiposRescisao = tiposRescisao;
    }

    // Métodos de cálculo
    public double calcularSaldoSalario() {
        LocalDate dataDemissao = funcionario.getDataDemissao();
        int diasTrabalhados = dataDemissao.getDayOfMonth();
        double valorDiario = funcionario.getSalarioBase() / dataDemissao.lengthOfMonth();
        return diasTrabalhados * valorDiario;
    }

    public double calcularAvisoPrevio() {
        long anosDeServico = ChronoUnit.YEARS.between(funcionario.getDataAdmissao(), funcionario.getDataDemissao());
        long diasAvisoPrevio = 30 + (anosDeServico * 3);
        if (diasAvisoPrevio > 90) {
            diasAvisoPrevio = 90;
        }
        double valorAvisoPrevio = (funcionario.getSalarioBase() / 30) * diasAvisoPrevio;
        return valorAvisoPrevio;
    }

    public double calcularFeriasVencidas() {
        // Implementar lógica de cálculo das férias vencidas
        return 0.0;
    }

    public double calcularDecimoTerceiro() {
        // Implementar lógica de cálculo do décimo terceiro salário
        return 0.0;
    }

    public double calcularMultaFGTS() {
        // Implementar lógica de cálculo da multa do FGTS
        return 0.0;
    }

    public double calcularINSS() {
        // Implementar lógica de cálculo do INSS
        return 0.0;
    }

    public double calcularIRRF() {
        // Implementar lógica de cálculo do IRRF
        return 0.0;
    }

    public double calcularTotalRescisao() {
        // Implementar lógica de cálculo do total da rescisão
        double saldoSalario = calcularSaldoSalario();
        double avisoPrevio = calcularAvisoPrevio();
        // Adicionar outros cálculos aqui e somar ao total
        totalRescisao = saldoSalario + avisoPrevio; // + outros valores
        return totalRescisao;
    }

    public double calcularRescisaoParaTodos() {
        // Implementar lógica de cálculo para múltiplas rescisões
        double total = 0.0;
        for (Funcionario func : listaFuncionarios) {
            this.funcionario = func;
            total += calcularTotalRescisao();
        }
        return total;
    }
}
