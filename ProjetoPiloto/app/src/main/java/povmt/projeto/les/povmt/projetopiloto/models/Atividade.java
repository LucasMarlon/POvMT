package povmt.projeto.les.povmt.projetopiloto.models;

/**Classe para objetos do tipo Atividade, onde serao contidos, valores e metodos para o mesmo.
 * @author Marcos Nascimento
 * @version 1.00
 * @since Release 01 da aplicacao
 */
public class Atividade {

    private long Id;
    private String nome;
    private int tempoInvestido;

    /**
     * O construtor Atividade com argumentos nome e tempoIvestido.
     */
    public Atividade(String nome, int tempoInvestido){
        this.nome = nome;
        this.tempoInvestido = tempoInvestido;
    }

    /**
     * Obt�m o Id.
     * @return um <code>long</code> especificando a Atividade.
     */
    public long getId() {
        return Id;
    }

    /**
     * Configura o nome.
     * @param nome o nome
     * @throws Exception no caso do nome ser invalido.
     */
    public void setNome(String nome) throws Exception {
        if(nome == null){
            throw new Exception("Atributo nome nao pode ser null.");
        }
        else if(nome.equals("")){
            throw new Exception("Nome nao pode ser a string vazia.");

        }
        else{
            this.nome = nome;
        }
    }

    /**
     * Obtem o nome.
     * @return <code>String</code> nomeando a Atividade.*/
    public String getNome() {
        return nome;
    }

    /**
     * Obtem o tempo investido na atividade
     * @return int atribuindo tempo a atividade
     */
    public int getTempo_investido() {
        return tempoInvestido;
    }

    /**
     * Modifica o tempo investido na atividade
     * @param tempoInvestido
     */
    public void setTempoInvestido(int tempoInvestido) throws Exception {
        if (tempoInvestido < 0) {
            throw new Exception("Tempo não pode ser negativo.");
        } else {
            this.tempoInvestido = tempoInvestido;
        }
    }
}
