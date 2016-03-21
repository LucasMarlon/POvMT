package povmt.projeto.les.povmt.projetopiloto.models;


public enum CATEGORIA {
    TRABALHO("Trabalho"), LAZER("Lazer");

    private String valor;

    CATEGORIA(String valor) { this.valor = valor; }

    public String getValor(){ return valor; }

    public static CATEGORIA getEnum(String value){
        if(TRABALHO.getValor().equals(value)){
            return TRABALHO;
        }else if(LAZER.getValor().equals(value)){
            return LAZER;
        }
        return null;
    }
}
