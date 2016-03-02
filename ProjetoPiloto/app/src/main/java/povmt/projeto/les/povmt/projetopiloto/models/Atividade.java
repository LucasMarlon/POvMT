package povmt.projeto.les.povmt.projetopiloto.models;

/**Classe para objetos do tipo Atividade, onde ser�o contidos, valores e m�todos para o mesmo.
 * @author Marcos Nascimento
 * @version 1.00
 * @since Release 01 da aplica��o
 */
public class Atividade {

    private long Id;
    private String nome;

    /**
     * O construtor Atividade sem argumento n�o inicializa nenhuma vari�vel.
     */
    public Atividade(){    }

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
     * @throws Exception no caso do nome ser inv�lido.
     */
    public void setNome(String nome) throws Exception {
        if(nome == null){
            throw new Exception("Atributo nome n�o pode ser null.");
        }
        else if(nome.equals("")){
            throw new Exception("Nome n�o pode ser a string vazia.");

        }
        else{
            this.nome = nome;
        }
    }

    /**
     * Obt�m o nome.
     * @return <code>String</code> nomeando a Atividade.*/
    public String getNome() {
        return nome;
    }
}
