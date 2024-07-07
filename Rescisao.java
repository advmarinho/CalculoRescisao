import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class Rescisao {

    public static Map<String, Double> calcularDetalhesRescisao(Funcionario funcionario) {
        Map<String, Double> detalhes = new HashMap<>();
        double salarioBase = funcionario.getSalarioBase();
        double outrosDescontos = funcionario.getOutrosDescontos();
        double feriasVencidas = funcionario.getFeriasVencidas();
        double indenizacaoAdicional = funcionario.getIndenizacaoAdicional();
        double multaAtrasoPagamento = funcionario.getMultaAtrasoPagamento();
        int diasAvisoPrevio = funcionario.getDiasAvisoPrevio();
        LocalDate dataAdmissao = funcionario.getDataAdmissao();
        LocalDate dataDemissao = funcionario.getDataDemissao();
        TipoRescisao tipoRescisao = funcionario.getTipoRescisao();

        // Calcula os dias trabalhados no mês de demissão
        int diasTrabalhadosNoMes = dataDemissao.getDayOfMonth();
        // Calcula os meses trabalhados no ano corrente
        int mesesTrabalhadosNoAno = dataDemissao.getMonthValue() - 1; // janeiro = 0
        if (diasTrabalhadosNoMes > 15) {
            mesesTrabalhadosNoAno += 1;
        }

        // Cálculo das verbas rescisórias baseado no tipo de rescisão
        double avisoPrevioIndenizado = tipoRescisao == TipoRescisao.PEDIDO_DE_DEMISSAO ? 0 : (salarioBase / 30) * diasAvisoPrevio;
        double saldoDeSalario = (salarioBase / 30) * diasTrabalhadosNoMes;
        double decimoTerceiroProporcional = (salarioBase / 12) * mesesTrabalhadosNoAno;
        double feriasProporcionais = (salarioBase / 12) * mesesTrabalhadosNoAno;
        double tercoFeriasProporcionais = feriasProporcionais / 3;

        // Colocar os detalhes das verbas rescisórias
        detalhes.put("Aviso Prévio Indenizado", avisoPrevioIndenizado);
        detalhes.put("13º Salário sobre Aviso", avisoPrevioIndenizado / 12);
        detalhes.put("Férias Salário sobre Aviso", avisoPrevioIndenizado / 12);
        detalhes.put("1/3 Férias Salário sobre Aviso", avisoPrevioIndenizado / 12 / 3);
        detalhes.put("Saldo de Salário", saldoDeSalario);
        detalhes.put("13º Salário Proporcional", decimoTerceiroProporcional);
        detalhes.put("Férias Proporcionais", feriasProporcionais);
        detalhes.put("1/3 Férias Proporcionais", tercoFeriasProporcionais);
        detalhes.put("Férias Vencidas", feriasVencidas);
        detalhes.put("1/3 Férias Vencidas", feriasVencidas / 3);
        detalhes.put("Indenização adicional (Lei nº 7.238/1984 art. 9º)", indenizacaoAdicional);
        detalhes.put("Multa atraso pagto rescisão", multaAtrasoPagamento);

        // Cálculo do FGTS sobre as verbas rescisórias
        double fgtsSobreSaldoSalario = saldoDeSalario * 0.08;
        double fgtsSobreAvisoPrevio = avisoPrevioIndenizado * 0.08;
        double fgtsSobreFeriasAviso = (avisoPrevioIndenizado / 12) * 0.08;
        double fgtsSobreTercoFeriasAviso = (avisoPrevioIndenizado / 12 / 3) * 0.08;
        double fgtsSobreDecimoTerceiroAviso = (avisoPrevioIndenizado / 12) * 0.08;
        double fgtsSobreDecimoTerceiroProporcional = decimoTerceiroProporcional * 0.08;
        double fgtsRescisaoSoma = fgtsSobreSaldoSalario + fgtsSobreAvisoPrevio + fgtsSobreDecimoTerceiroAviso + fgtsSobreDecimoTerceiroProporcional;

        // Calcula os meses trabalhados da admissão até a demissão e o FGTS sobre o salário base
        long mesesTrabalhados = ChronoUnit.MONTHS.between(dataAdmissao, dataDemissao);
        double fgtsNaoDepositado = mesesTrabalhados * salarioBase * 0.08;

        // Somar todos os FGTS calculados
        double fgtsTotal = fgtsRescisaoSoma + fgtsNaoDepositado;

        // Cálculo da multa de 40% sobre o FGTS
        double multa40Fgts = fgtsTotal * 0.40;

        // Adicionar ao mapa de detalhes
        detalhes.put("FGTS sobre Saldo de Salário", fgtsSobreSaldoSalario);
        detalhes.put("FGTS sobre Aviso Prévio", fgtsSobreAvisoPrevio);
        detalhes.put("FGTS sobre Férias Aviso", fgtsSobreFeriasAviso);
        detalhes.put("FGTS sobre 1/3 Férias Aviso", fgtsSobreTercoFeriasAviso);
        detalhes.put("FGTS sobre 13º Aviso", fgtsSobreDecimoTerceiroAviso);
        detalhes.put("FGTS sobre 13º Proporcional", fgtsSobreDecimoTerceiroProporcional);
        detalhes.put("FGTS Total Rescisão", fgtsRescisaoSoma);
        detalhes.put("FGTS não depositado admissão até demissão", fgtsNaoDepositado);
        detalhes.put("Multa 40% sobre FGTS", multa40Fgts);
        //Inclua descontos
        detalhes.put("Outros Descontos", outrosDescontos);

        // Calcular o total da rescisão
        double totalRescisao = saldoDeSalario + avisoPrevioIndenizado + decimoTerceiroProporcional + feriasProporcionais + tercoFeriasProporcionais + feriasVencidas + (feriasVencidas / 3) + indenizacaoAdicional + multaAtrasoPagamento + avisoPrevioIndenizado / 12 + avisoPrevioIndenizado / 12 + avisoPrevioIndenizado / 12 / 3 - outrosDescontos;
        detalhes.put("Total Rescisao", totalRescisao);

        return detalhes;
    }
}
