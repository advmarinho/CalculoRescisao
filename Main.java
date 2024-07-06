import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.JFormattedTextField;
import javax.swing.JComboBox;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.NumberFormatter;
import javax.swing.text.MaskFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Funcionario funcionario = new Funcionario();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Painel para conter os campos de entrada
        JPanel panel = new JPanel(new GridLayout(7, 2));

        // Campo de entrada para o nome
        JLabel nomeLabel = new JLabel("Nome do funcionário:");
        JTextField nomeField = new JTextField();
        panel.add(nomeLabel);
        panel.add(nomeField);

        // Campo de entrada formatado para data de admissão
        JLabel dataAdmissaoLabel = new JLabel("Data de Admissão (dd/MM/yyyy):");
        JFormattedTextField dataAdmissaoField = new JFormattedTextField(createDateFormatter("##/##/####"));
        dataAdmissaoField.setInputVerifier(new DateInputVerifier());
        panel.add(dataAdmissaoLabel);
        panel.add(dataAdmissaoField);

        // Campo de entrada formatado para data de demissão
        JLabel dataDemissaoLabel = new JLabel("Data de Demissão (dd/MM/yyyy):");
        JFormattedTextField dataDemissaoField = new JFormattedTextField(createDateFormatter("##/##/####"));
        dataDemissaoField.setInputVerifier(new DateInputVerifier());
        panel.add(dataDemissaoLabel);
        panel.add(dataDemissaoField);

        // Campo de entrada formatado para salário base
        JLabel salarioBaseLabel = new JLabel("Salário Base:");
        JFormattedTextField salarioBaseField = createCurrencyField();
        panel.add(salarioBaseLabel);
        panel.add(salarioBaseField);

        // Campo de entrada formatado para outros descontos
        JLabel outrosDescontosLabel = new JLabel("Outros Descontos:");
        JFormattedTextField outrosDescontosField = createCurrencyField();
        panel.add(outrosDescontosLabel);
        panel.add(outrosDescontosField);

        // Dropdown para seleção de tipo de rescisão
        JLabel tipoRescisaoLabel = new JLabel("Tipo de Rescisão:");
        JComboBox<TipoRescisao> tipoRescisaoDropdown = new JComboBox<>(TipoRescisao.values());
        panel.add(tipoRescisaoLabel);
        panel.add(tipoRescisaoDropdown);

        boolean isComplete = false;
        while (!isComplete) {
            int result = JOptionPane.showConfirmDialog(null, panel, "Preencha os dados do funcionário", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    String nome = nomeField.getText();
                    String dataAdmissao = dataAdmissaoField.getText();
                    String dataDemissao = dataDemissaoField.getText();
                    String salarioBase = salarioBaseField.getText().replace(".", "").replace(",", ".");
                    String outrosDescontos = outrosDescontosField.getText().replace(".", "").replace(",", ".");

                    funcionario.setNome(nome);
                    funcionario.setDataAdmissao(LocalDate.parse(dataAdmissao, formatter));
                    funcionario.setDataDemissao(LocalDate.parse(dataDemissao, formatter));
                    funcionario.setSalarioBase(Double.parseDouble(salarioBase));
                    if (outrosDescontos.isEmpty()) {
                        funcionario.setOutrosDescontos(0.0);
                    } else {
                        funcionario.setOutrosDescontos(Double.parseDouble(outrosDescontos));
                    }

                    // Criando exemplo de cálculo de rescisão
                    CalculoRescisao calculo = new CalculoRescisao();
                    calculo.setFuncionario(funcionario);
                    calculo.setTotalRescisao(calculo.calcularTotalRescisao());

                    // Lista de funcionários para múltiplas rescisões
                    List<Funcionario> listaFuncionarios = new ArrayList<>();
                    listaFuncionarios.add(funcionario);

                    // Configurando lista de funcionários no cálculo
                    calculo.setListaFuncionarios(listaFuncionarios);

                    // Configurando tipo de rescisão selecionado
                    TipoRescisao tipoRescisaoSelecionado = (TipoRescisao) tipoRescisaoDropdown.getSelectedItem();
                    calculo.getTiposRescisao().add(tipoRescisaoSelecionado);

                    // Gerando relatório
                    Relatorio relatorio = new Relatorio();
                    relatorio.setFuncionario(funcionario);
                    relatorio.setMotivoRescisao(tipoRescisaoSelecionado);

                    Map<String, Double> detalhes = new HashMap<>();
                    relatorio.setDetalhesRescisao(detalhes);
                    relatorio.gerarRelatorio(calculo);
                    relatorio.salvarRelatorio("relatorios/relatorio.txt");

                    // Gerando relatórios para múltiplas rescisões
                    relatorio.setListaFuncionarios(listaFuncionarios);
                    relatorio.gerarRelatorioParaTodos(calculo);

                    JOptionPane.showMessageDialog(null, "Relatório de rescisão gerado e salvo com sucesso!");

                    isComplete = true;
                } catch (DateTimeParseException e) {
                    JOptionPane.showMessageDialog(null, "Data inválida. Use o formato dd/MM/yyyy.");
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Valor numérico inválido.");
                }
            } else {
                break; // Usuário cancelou a entrada de dados
            }
        }
    }

    // Método auxiliar para criar formatadores de data
    private static MaskFormatter createDateFormatter(String s) {
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter(s);
            formatter.setPlaceholderCharacter('_');
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatter;
    }

    // Método auxiliar para criar campos de entrada de valores monetários
    private static JFormattedTextField createCurrencyField() {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        NumberFormatter numberFormatter = new NumberFormatter(decimalFormat);
        numberFormatter.setValueClass(Double.class);
        numberFormatter.setAllowsInvalid(false);
        numberFormatter.setOverwriteMode(false);
        JFormattedTextField textField = new JFormattedTextField(numberFormatter);
        textField.setColumns(10);
        return textField;
    }

    // Validador de datas
    static class DateInputVerifier extends InputVerifier {
        @Override
        public boolean verify(JComponent input) {
            if (input instanceof JFormattedTextField) {
                JFormattedTextField ftf = (JFormattedTextField) input;
                String text = ftf.getText();
                try {
                    LocalDate date = LocalDate.parse(text, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    if (!text.equals(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))) {
                        throw new DateTimeParseException("Data inválida", text, 0);
                    }
                    return true;
                } catch (DateTimeParseException e) {
                    JOptionPane.showMessageDialog(null, "Data inválida. Use o formato dd/MM/yyyy e verifique se o dia, mês e ano são válidos.");
                    return false;
                }
            }
            return true;
        }
    }
}
