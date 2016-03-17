package povmt.projeto.les.povmt.projetopiloto.models;

/**
 * Created by Lucas on 08/03/2016.
 */
public enum PRIORIDADE {

    ALTA("Alta"), MEDIA("Média"), BAIXA("Baixa");
    private String valor;

    private PRIORIDADE(String valor) {
        this.valor = valor;
    }


    public String getValor(){
        return valor;
    }
}
