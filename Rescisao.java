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

        // Calcula os avos de 13º salário no ano da demissão
        long avosDecimoTerceiro = dataDemissao.getMonthValue();
        if (diasTrabalhadosNoMes < 15) { // Considere dias < 15 como mês não completo para 13º
            avosDecimoTerceiro -= 1;
        }

        // Limitar os avos do 13º salário a no máximo 12
        avosDecimoTerceiro = Math.min(avosDecimoTerceiro, 12);

        // Calcula os meses trabalhados desde a admissão até a demissão
        long mesesTrabalhados = ChronoUnit.MONTHS.between(dataAdmissao.withDayOfMonth(1), dataDemissao.withDayOfMonth(1));

        // Calcula os avos de férias proporcionais
        long avosFeriasProporcionais = mesesTrabalhados % 12;
        if (diasTrabalhadosNoMes > 14) { // Considere dias > 14 como mês completo para férias
            avosFeriasProporcionais += 1;
        }

        // Limitar os avos proporcionais a no máximo 12
        avosFeriasProporcionais = Math.min(avosFeriasProporcionais, 12);


        // Cálculo das verbas rescisórias baseado no tipo de rescisão
        double avisoPrevioIndenizado = (tipoRescisao == TipoRescisao.PEDIDO_DE_DEMISSAO || tipoRescisao == TipoRescisao.POR_JUSTA_CAUSA || tipoRescisao == TipoRescisao.FALECIMENTO_DO_EMPREGADO) ? 0 : (salarioBase / 30) * diasAvisoPrevio;
        double saldoDeSalario = (salarioBase / 30) * diasTrabalhadosNoMes;
        double decimoTerceiroProporcional = (salarioBase / 12) * avosDecimoTerceiro;
        double feriasProporcionais = (salarioBase / 12) * avosFeriasProporcionais;
        double tercoFeriasProporcionais = feriasProporcionais / 3;

        // Zerar valores não devidos de acordo com o tipo de rescisão
        switch (tipoRescisao) {
            case POR_JUSTA_CAUSA:
                avisoPrevioIndenizado = 0;
                feriasProporcionais = 0;
                tercoFeriasProporcionais = 0;
                decimoTerceiroProporcional = 0;
                indenizacaoAdicional = 0;
                multaAtrasoPagamento = 0;
                break;

            case TERMINO_CONTRATO_EXPERIENCIA:
                avisoPrevioIndenizado = 0;
                feriasVencidas = 0;
                indenizacaoAdicional = 0;
                multaAtrasoPagamento = 0;
                break;

            case FALECIMENTO_DO_EMPREGADO:
                avisoPrevioIndenizado = 0;
                indenizacaoAdicional = 0;
                break;

            case PEDIDO_DE_DEMISSAO:
                indenizacaoAdicional = 0;
                break;

            case ACORDO_ENTRE_AS_PARTES:
                // Acordo prevê aviso prévio indenizado 50%, multa FGTS 20%
                avisoPrevioIndenizado /= 2;
                break;

            case RESCISAO_POR_CULPA_RECIPROCA:
                // Culpa Recíproca prevê aviso prévio indenizado 50%, multa FGTS 20%
                avisoPrevioIndenizado /= 2;
                break;

            default:
                break;
        }

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
        double fgtsNaoDepositado = mesesTrabalhados * salarioBase * 0.08;

        // Somar todos os FGTS calculados
        double fgtsTotal = fgtsRescisaoSoma + fgtsNaoDepositado;

        // Cálculo da multa de 40% ou 20% sobre o FGTS
        double multa40Fgts = 0;
        double multa20Fgts = 0;
        if (tipoRescisao == TipoRescisao.SEM_JUSTA_CAUSA || tipoRescisao == TipoRescisao.RESCISAO_INDIRETA || tipoRescisao == TipoRescisao.FALECIMENTO_DO_EMPREGADO) {
            multa40Fgts = fgtsTotal * 0.40;
            detalhes.put("Multa 40% sobre FGTS", multa40Fgts);
        } else if (tipoRescisao == TipoRescisao.ACORDO_ENTRE_AS_PARTES || tipoRescisao == TipoRescisao.RESCISAO_POR_CULPA_RECIPROCA) {
            multa20Fgts = fgtsTotal * 0.20;
            detalhes.put("Multa 20% sobre FGTS", multa20Fgts);
        }

        // Adicionar ao mapa de detalhes
        detalhes.put("FGTS sobre Saldo de Salário", fgtsSobreSaldoSalario);
        detalhes.put("FGTS sobre Aviso Prévio", fgtsSobreAvisoPrevio);
        detalhes.put("FGTS sobre Férias Aviso", fgtsSobreFeriasAviso);
        detalhes.put("FGTS sobre 1/3 Férias Aviso", fgtsSobreTercoFeriasAviso);
        detalhes.put("FGTS sobre 13º Aviso", fgtsSobreDecimoTerceiroAviso);
        detalhes.put("FGTS sobre 13º Proporcional", fgtsSobreDecimoTerceiroProporcional);
        detalhes.put("FGTS Total Rescisão", fgtsRescisaoSoma);
        detalhes.put("FGTS não depositado admissão até demissão", fgtsNaoDepositado);
        // Inclua descontos
        detalhes.put("Outros Descontos", outrosDescontos);

        // Calcular o total da rescisão
        double totalRescisao = saldoDeSalario + avisoPrevioIndenizado + decimoTerceiroProporcional + feriasProporcionais + tercoFeriasProporcionais + feriasVencidas + (feriasVencidas / 3) + indenizacaoAdicional + multaAtrasoPagamento + avisoPrevioIndenizado / 12 + avisoPrevioIndenizado / 12 + avisoPrevioIndenizado / 12 / 3 - outrosDescontos;
        detalhes.put("Total Rescisao", totalRescisao);

        return detalhes;
    }
}
