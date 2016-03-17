package povmt.projeto.les.povmt.projetopiloto.models;


public enum PRIORIDADE {

    ALTA("Alta"), MEDIA("Média"), BAIXA("Baixa");
    private String valor;

    private PRIORIDADE(String valor) {
        this.valor = valor;
    }


    public String getValor(){
        return valor;
    }

    public static PRIORIDADE getEnum(String value){
        if(ALTA.name().equals(value)){
            return ALTA;
        }else if(MEDIA.name().equals(value)){
            return MEDIA;
        }else if(BAIXA.name().equals(value)){
            return BAIXA;
        }
        return null;
    }
}
