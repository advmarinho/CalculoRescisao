import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Relatorio {
    private Funcionario funcionario;
    private Map<String, Double> detalhesRescisao;
    private List<Funcionario> listaFuncionarios;
    private TipoRescisao motivoRescisao;

    // Getters e Setters
    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Map<String, Double> getDetalhesRescisao() {
        return detalhesRescisao;
    }

    public void setDetalhesRescisao(Map<String, Double> detalhesRescisao) {
        this.detalhesRescisao = detalhesRescisao;
    }

    public List<Funcionario> getListaFuncionarios() {
        return listaFuncionarios;
    }

    public void setListaFuncionarios(List<Funcionario> listaFuncionarios) {
        this.listaFuncionarios = listaFuncionarios;
    }

    public TipoRescisao getMotivoRescisao() {
        return motivoRescisao;
    }

    public void setMotivoRescisao(TipoRescisao motivoRescisao) {
        this.motivoRescisao = motivoRescisao;
    }

    // Métodos
    public void gerarRelatorio(CalculoRescisao calculo) {
        detalhesRescisao.put("Saldo de Salário", calculo.calcularSaldoSalario());
        detalhesRescisao.put("Aviso Prévio", calculo.calcularAvisoPrevio());
        detalhesRescisao.put("Férias Vencidas", calculo.calcularFeriasVencidas());
        detalhesRescisao.put("13º Salário", calculo.calcularDecimoTerceiro());
        detalhesRescisao.put("Multa FGTS", calculo.calcularMultaFGTS());
        detalhesRescisao.put("INSS", calculo.calcularINSS());
        detalhesRescisao.put("IRRF", calculo.calcularIRRF());
        detalhesRescisao.put("Total Rescisão", calculo.calcularTotalRescisao());
    }

    public void salvarRelatorio(String caminho) {
        File file = new File(caminho);
        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }

        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("pt", "BR"));
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("Relatório de Rescisão\n");
            writer.write("=====================\n");
            writer.write("Nome: " + funcionario.getNome() + "\n");
            writer.write("Data de Admissão: " + funcionario.getDataAdmissao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n");
            writer.write("Data de Demissão: " + funcionario.getDataDemissao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n");
            writer.write("Último Salário: R$ " + numberFormat.format(funcionario.getSalarioBase()) + "\n");

            writer.write("Motivo da Rescisão: " + motivoRescisao.toString().replace('_', ' ').toLowerCase() + "\n");

            double saldoSalario = detalhesRescisao.get("Saldo de Salário");
            int diasTrabalhados = funcionario.getDataDemissao().getDayOfMonth();
            writer.write("Saldo de Salário: Dias: " + diasTrabalhados + ", Valor: R$ " + numberFormat.format(saldoSalario) + "\n");

            double avisoPrevio = detalhesRescisao.get("Aviso Prévio");
            long anosDeServico = ChronoUnit.YEARS.between(funcionario.getDataAdmissao(), funcionario.getDataDemissao());
            long diasAvisoPrevio = 30 + (anosDeServico * 3);
            if (diasAvisoPrevio > 90) {
                diasAvisoPrevio = 90;
            }
            writer.write("Aviso Prévio Indenizado: Dias: " + diasAvisoPrevio + ", Valor: R$ " + numberFormat.format(avisoPrevio) + "\n");

            writer.write("\nDescrição das Verbas:\n");
            writer.write("=====================\n");
            for (Map.Entry<String, Double> entry : detalhesRescisao.entrySet()) {
                writer.write(entry.getKey() + ": R$ " + numberFormat.format(entry.getValue()) + "\n");
            }

            double outrosDescontos = funcionario.getOutrosDescontos();
            writer.write("Valor do desconto outros digitado: R$ " + numberFormat.format(outrosDescontos) + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gerarRelatorioParaTodos(CalculoRescisao calculo) {
        for (Funcionario func : listaFuncionarios) {
            this.funcionario = func;
            gerarRelatorio(calculo);
            salvarRelatorio("relatorios/relatorio_" + func.getNome() + ".txt");
        }
    }
}
