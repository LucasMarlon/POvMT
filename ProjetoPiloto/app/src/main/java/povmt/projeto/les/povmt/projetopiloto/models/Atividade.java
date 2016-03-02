package povmt.projeto.les.povmt.projetopiloto.models;

/**Classe para objetos do tipo Atividade, onde serão contidos, valores e métodos para o mesmo.
 * @author Marcos Nascimento
 * @version 1.00
 * @since Release 01 da aplicação
 */
public class Atividade {

    private long Id;
    private String nome;

    /**
     * O construtor Atividade sem argumento não inicializa nenhuma variável.
     */
    public Atividade(){    }

    /**
     * Obtém o Id.
     * @return um <code>long</code> especificando a Atividade.
     */
    public long getId() {
        return Id;
    }

    /**
     * Configura o nome.
     * @param nome o nome
     * @throws Exception no caso do nome ser inválido.
     */
    public void setNome(String nome) throws Exception {
        if(nome == null){
            throw new Exception("Atributo nome não pode ser null.");
        }
        else if(nome.equals("")){
            throw new Exception("Nome não pode ser a string vazia.");

        }
        else{
            this.nome = nome;
        }
    }

    /**
     * Obtém o nome.
     * @return <code>String</code> nomeando a Atividade.*/
    public String getNome() {
        return nome;
    }
}
