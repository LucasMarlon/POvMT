package povmt.projeto.les.povmt.projetopiloto.models;

/**
 * Created by Lucas on 08/03/2016.
 */
public enum CATEGORIA {
    TRABALHO("Trabalho"), LAZER("Lazer");
    private String valor;

    private CATEGORIA(String valor) {
        this.valor = valor;
    }
}
