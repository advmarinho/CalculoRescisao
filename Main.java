import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Painel para coleta de dados
        JPanel panel = new JPanel(new GridLayout(12, 2));

        JTextField nomeField = new JTextField("NOME");
        JFormattedTextField dataAdmissaoField = createFormattedField("##/##/####");
        JFormattedTextField dataDemissaoField = createFormattedField("##/##/####");
        JFormattedTextField salarioBaseField = createFormattedFieldWithMaxLength();
        JFormattedTextField valorFeriasVencidasField = createFormattedFieldWithMaxLength();
        JFormattedTextField indenizacaoAdicionalField = createFormattedFieldWithMaxLength();
        JFormattedTextField multaAtrasoPagamentoField = createFormattedFieldWithMaxLength();
        JFormattedTextField outrosDescontosField = createFormattedFieldWithMaxLength();
        JFormattedTextField diasAvisoPrevioField = createFormattedField("##");

        // Configuração de valores padrão
        diasAvisoPrevioField.setText("30");
        salarioBaseField.setText("0.00");
        valorFeriasVencidasField.setText("0.00");
        indenizacaoAdicionalField.setText("0.00");
        multaAtrasoPagamentoField.setText("0.00");
        outrosDescontosField.setText("0.00");

        // Adicionando verificadores de entrada
        dataAdmissaoField.setInputVerifier(new DateInputVerifier());
        dataDemissaoField.setInputVerifier(new DateInputVerifier());

        JComboBox<TipoRescisao> tipoRescisaoComboBox = new JComboBox<>(TipoRescisao.values());

        panel.add(new JLabel("Nome: *"));
        panel.add(nomeField);
        panel.add(new JLabel("Data de Admissão (dd/MM/yyyy): *"));
        panel.add(dataAdmissaoField);
        panel.add(new JLabel("Data de Demissão (dd/MM/yyyy): *"));
        panel.add(dataDemissaoField);
        panel.add(new JLabel("Dias de Aviso Prévio: *"));
        panel.add(diasAvisoPrevioField);
        panel.add(new JLabel("Salário Base: *"));
        panel.add(salarioBaseField);
        panel.add(new JLabel("Valor de Férias Vencidas:"));
        panel.add(valorFeriasVencidasField);
        panel.add(new JLabel("Indenização Adicional:"));
        panel.add(indenizacaoAdicionalField);
        panel.add(new JLabel("Multa por Atraso de Pagamento:"));
        panel.add(multaAtrasoPagamentoField);
        panel.add(new JLabel("Outros Descontos:"));
        panel.add(outrosDescontosField);
        panel.add(new JLabel("Tipo de Rescisão: *"));
        panel.add(tipoRescisaoComboBox);

        while (true) {
            int result = JOptionPane.showConfirmDialog(null, panel, "RPA Cromex - Preencha os Dados para Prévia TRCT", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    String nome = nomeField.getText();
                    LocalDate dataAdmissao = validateDate(dataAdmissaoField.getText());
                    LocalDate dataDemissao = validateDate(dataDemissaoField.getText());
                    double salarioBase = parseFormattedText(salarioBaseField.getText());
                    int diasAvisoPrevio = Integer.parseInt(diasAvisoPrevioField.getText());
                    double feriasVencidas = parseFormattedText(valorFeriasVencidasField.getText());
                    double indenizacaoAdicional = parseFormattedText(indenizacaoAdicionalField.getText());
                    double multaAtrasoPagamento = parseFormattedText(multaAtrasoPagamentoField.getText());
                    double outrosDescontos = parseFormattedText(outrosDescontosField.getText());
                    TipoRescisao tipoRescisao = (TipoRescisao) tipoRescisaoComboBox.getSelectedItem();

                    Funcionario funcionario = new Funcionario(
                            nome,
                            dataAdmissao,
                            dataDemissao,
                            salarioBase,
                            outrosDescontos,
                            feriasVencidas,
                            indenizacaoAdicional,
                            multaAtrasoPagamento,
                            tipoRescisao,
                            diasAvisoPrevio
                    );

                    Map<String, Double> detalhes = Rescisao.calcularDetalhesRescisao(funcionario);

                    StringBuilder mensagem = new StringBuilder();
                    mensagem.append("Dados para o cálculo: \n");
                    mensagem.append("-----------------------------------------\n");
                    mensagem.append("Nome do funcionário: \t").append(funcionario.getNome()).append("\n");
                    mensagem.append("Data de admissão: \t").append(funcionario.getDataAdmissao().format(formatter)).append("\n");
                    mensagem.append("Data de demissão: \t").append(funcionario.getDataDemissao().format(formatter)).append("\n");
                    mensagem.append("Último salário: \t").append(String.format("%,.2f", funcionario.getSalarioBase())).append("\n");
                    mensagem.append("Motivo da rescisão: \t").append(funcionario.getTipoRescisao().getDescricao()).append("\n\n");

                    mensagem.append("Descrição das verbas \t\t | Valor\t | FGTS\n");
                    mensagem.append("-----------------------------------------\n");
                    mensagem.append(String.format("Aviso Prévio Indenizado: \t | %,.2f\t | %,.2f\n", detalhes.get("Aviso Prévio Indenizado"), detalhes.get("FGTS sobre Aviso Prévio")));
                    mensagem.append(String.format("13º Salário sobre Aviso: \t | %,.2f\t | %,.2f\n", detalhes.get("13º Salário sobre Aviso"), detalhes.get("FGTS sobre 13º Aviso")));
                    mensagem.append(String.format("Férias Salário sobre Aviso: \t | %,.2f\t | \n", detalhes.get("Férias Salário sobre Aviso")));
                    mensagem.append(String.format("1/3 Férias Salário sobre Aviso: \t | %,.2f\t | \n", detalhes.get("1/3 Férias Salário sobre Aviso")));
                    mensagem.append("-----------------------------------------\n");
                    mensagem.append(String.format("Saldo de Salário: \t\t | %,.2f\t | %,.2f\n", detalhes.get("Saldo de Salário"), detalhes.get("FGTS sobre Saldo de Salário")));
                    mensagem.append(String.format("13º Salário Proporcional: \t | %,.2f\t | %,.2f\n", detalhes.get("13º Salário Proporcional"), detalhes.get("FGTS sobre 13º Proporcional")));
                    mensagem.append(String.format("Férias Proporcionais: \t\t | %,.2f\t | \n", detalhes.get("Férias Proporcionais")));
                    mensagem.append(String.format("1/3 Férias Proporcionais: \t | %,.2f\t | \n", detalhes.get("1/3 Férias Proporcionais")));
                    mensagem.append(String.format("Férias Vencidas: \t\t | %,.2f\t | \n", detalhes.get("Férias Vencidas")));
                    mensagem.append(String.format("1/3 Férias Vencidas: \t\t | %,.2f\t | \n", detalhes.get("1/3 Férias Vencidas")));
                    mensagem.append("-----------------------------------------\n");
                    mensagem.append(String.format("Indenização adicional (Lei nº 7.238/1984 art. 9º): \t | %,.2f\t | \n", detalhes.get("Indenização adicional (Lei nº 7.238/1984 art. 9º)")));
                    mensagem.append(String.format("Multa atraso pagto rescisão: \t | %,.2f\t | \n", detalhes.get("Multa atraso pagto rescisão")));
                    mensagem.append("-----------------------------------------\n");
                    mensagem.append(String.format("Total Rescisao: \t\t\t | %,.2f\t | \n\n", detalhes.get("Total Rescisao")));

                    // Incluir o valor de descontos
                    mensagem.append("Descontos: ").append(String.format("%,.2f", funcionario.getOutrosDescontos())).append("\n");
                    mensagem.append("-----------------------------------------\n");
                    mensagem.append("Estimativa do FGTS não depositado (sobre salários): \t").append(String.format("%,.2f", detalhes.get("FGTS não depositado admissão até demissão"))).append("\n");
                    mensagem.append("FGTS sobre verbas rescisórias: \t").append(String.format("%,.2f", detalhes.get("FGTS Total Rescisão"))).append("\n");
                    if (tipoRescisao == TipoRescisao.SEM_JUSTA_CAUSA || tipoRescisao == TipoRescisao.RESCISAO_INDIRETA || tipoRescisao == TipoRescisao.FALECIMENTO_DO_EMPREGADO) {
                        mensagem.append("Multa 40% sobre FGTS: \t").append(String.format("%,.2f", detalhes.get("Multa 40% sobre FGTS"))).append("\n");
                    } else if (tipoRescisao == TipoRescisao.ACORDO_ENTRE_AS_PARTES || tipoRescisao == TipoRescisao.RESCISAO_POR_CULPA_RECIPROCA) {
                        mensagem.append("Multa 20% sobre FGTS: \t").append(String.format("%,.2f", detalhes.get("Multa 20% sobre FGTS"))).append("\n");
                    }
                    mensagem.append("-----------------------------------------\n");

                    // Coloque minha assinatura By Anderson Marinho
                    mensagem.append("\n Anderson Marinho - ADS Coder Github: @advmarinho ");

                    JOptionPane.showMessageDialog(null, mensagem.toString(), "Cálculo TRCT Prévia Cromex", JOptionPane.INFORMATION_MESSAGE);

                    // Exportar para TXT
                    String nomeArquivo = "rescisao_" + funcionario.getNome().replaceAll(" ", "_") + ".txt";
                    ExportadorTXT.exportar(nomeArquivo, funcionario, detalhes);

                    break; // Se tudo estiver correto, sai do loop
                } catch (DateTimeParseException e) {
                    JOptionPane.showMessageDialog(null, "Data inválida. Use o formato dd/MM/yyyy e verifique se o dia, mês e ano são válidos.");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Erro nos dados fornecidos: " + e.getMessage());
                }
            } else {
                break; // Sai do loop se o usuário cancelar
            }
        }
    }

    private static JFormattedTextField createFormattedField(String pattern) {
        try {
            MaskFormatter maskFormatter = new MaskFormatter(pattern);
            maskFormatter.setPlaceholderCharacter('0');
            return new JFormattedTextField(maskFormatter);
        } catch (ParseException e) {
            throw new RuntimeException("Erro na criação do campo formatado: " + e.getMessage());
        }
    }

    private static JFormattedTextField createFormattedFieldWithMaxLength() {
        JFormattedTextField field = new JFormattedTextField();
        field.setColumns(10); // Define o comprimento máximo
        return field;
    }

    private static LocalDate validateDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = LocalDate.parse(date, formatter);
        if (localDate.getDayOfMonth() > localDate.lengthOfMonth()) {
            throw new DateTimeParseException("Data inválida", date, 0);
        }
        return localDate;
    }

    private static double parseFormattedText(String text) {
        return Double.parseDouble(text.replace(",", "."));
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
