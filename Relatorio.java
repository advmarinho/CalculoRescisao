import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class RelatorioRescisao {

    public static void gerarRelatorio(Funcionario funcionario, TipoRescisao tipoRescisao, Map<String, Double> detalhes, Map<String, Double> fgtsDetalhes) {
        String fileName = "relatorio_rescisaoT.txt";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Dados para o cálculo:\n");
            writer.write("-----------------------------------------\n");
            writer.write("Nome do funcionário:\t" + funcionario.getNome() + "\n");
            writer.write("Data de admissão:\t" + funcionario.getDataAdmissao().format(formatter) + "\n");
            writer.write("Data de demissão:\t" + funcionario.getDataDemissao().format(formatter) + "\n");
            writer.write("Último salário:\t" + String.format("%,.2f", funcionario.getSalarioBase()) + "\n");
            writer.write("Dias de Aviso Prévio:\t" + String.format("%,.1f", funcionario.getDiasAvisoPrevio()) + "\n");
            writer.write("Motivo da rescisão:\t" + tipoRescisao + "\n\n");

            writer.write("Descrição das verbas\t\t | Valor\t | FGTS\n");
            writer.write("-----------------------------------------\n");
            writer.write(String.format("Aviso Prévio Indenizado:\t | %,.2f\t | %,.2f\n", detalhes.get("Aviso Prévio Indenizado"), fgtsDetalhes.get("Aviso Prévio Indenizado")));
            writer.write(String.format("13º Salário sobre Aviso:\t | %,.2f\t | %,.2f\n", detalhes.get("13º Salário sobre Aviso"), fgtsDetalhes.get("13º Salário sobre Aviso")));
            writer.write(String.format("Férias Salário sobre Aviso:\t | %,.2f\t | \n", detalhes.get("Férias Salário sobre Aviso")));
            writer.write(String.format("1/3 Férias Salário sobre Aviso:\t | %,.2f\t | \n", detalhes.get("1/3 Férias Salário sobre Aviso")));
            writer.write("-----------------------------------------\n");
            writer.write(String.format("Saldo de Salário:\t\t | %,.2f\t | %,.2f\n", detalhes.get("Saldo de Salário"), fgtsDetalhes.get("Saldo de Salário")));
            writer.write(String.format("13º Salário Proporcional:\t | %,.2f\t | %,.2f\n", detalhes.get("13º Salário Proporcional"), fgtsDetalhes.get("13º Salário Proporcional")));
            writer.write(String.format("Férias Proporcionais:\t\t | %,.2f\t | \n", detalhes.get("Férias Proporcionais")));
            writer.write(String.format("1/3 Férias Proporcionais:\t | %,.2f\t | \n", detalhes.get("1/3 Férias Proporcionais")));
            writer.write(String.format("Férias Vencidas:\t\t | %,.2f\t | \n", detalhes.get("Férias Vencidas")));
            writer.write(String.format("1/3 Férias Vencidas:\t\t | %,.2f\t | \n", detalhes.get("1/3 Férias Vencidas")));
            writer.write("-----------------------------------------\n");
            writer.write(String.format("Indenização adicional (Lei nº 7.238/1984 art. 9º):\t | %,.2f\t | \n", detalhes.get("Indenização adicional (Lei nº 7.238/1984 art. 9º)")));
            writer.write(String.format("Multa atraso pagto rescisão:\t | %,.2f\t | \n", detalhes.get("Multa atraso pagto rescisão")));
            writer.write(String.format("Total Rescisao:\t\t\t | %,.2f\t | \n\n", detalhes.get("Total Rescisao")));

            writer.write(String.format("Estimativa do FGTS não depositado (sobre salários)\t | \t | %,.2f\n", fgtsDetalhes.get("Estimativa FGTS não depositado")));
            writer.write(String.format("Multa 40% sobre FGTS\t\t | %,.2f\t | \n", fgtsDetalhes.get("Multa 40% sobre FGTS")));
            writer.write(String.format("Total FGTS\t\t\t | %,.2f\t | \n", fgtsDetalhes.get("Total FGTS")));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void gerarRelatorio(String nome, LocalDate localDate, LocalDate localDate2, double double1,
            String string, Map<String, Double> detalhes, Map<String, Double> fgtsDetalhes) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'gerarRelatorio'");
    }
}
