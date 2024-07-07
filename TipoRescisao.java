public enum TipoRescisao {
    SEM_JUSTA_CAUSA("Sem Justa Causa"),
    POR_JUSTA_CAUSA("Por Justa Causa"),
    PEDIDO_DE_DEMISSAO("Pedido de Demissão"),
    TERMINO_CONTRATO_EXPERIENCIA("Término de Contrato de Experiência"),
    ACORDO_ENTRE_AS_PARTES("Acordo entre as Partes"),
    RESCISAO_INDIRETA("Rescisão Indireta"),
    FALECIMENTO_DO_EMPREGADO("Falecimento do Empregado"),
    RESCISAO_POR_CULPA_RECIPROCA("Rescisão por Culpa Recíproca");

    private final String descricao;

    TipoRescisao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
