package povmt.projeto.les.povmt.projetopiloto.models;


public enum PRIORIDADE {

    ALTA("Alta"), MEDIA("Média"), BAIXA("Baixa");
    private String valor;

    PRIORIDADE(String valor) {
        this.valor = valor;
    }


    public String getValor(){
        return valor;
    }

    public static PRIORIDADE getEnum(String value){
        if(ALTA.getValor().equals(value)){
            return ALTA;
        }else if(MEDIA.getValor().equals(value)){
            return MEDIA;
        }else if(BAIXA.getValor().equals(value)){
            return BAIXA;
        }
        return null;
    }
}
