package povmt.projeto.les.povmt.projetopiloto.models;

import java.io.Serializable;
import java.util.Date;

public class Atividade implements Comparable<Atividade>, Serializable{

    private String nome;
    private PRIORIDADE prioridade;
    private byte[] foto;
    private CATEGORIA categoria;
    private int tempoInvestido;
    private Date data;


    public Atividade(String nome, Date dataAtual) throws Exception{
        this(nome, 0, null);
        this.data = dataAtual;
    }
    public Atividade(String nome ) throws Exception{
        this(nome, 0, null);
    }

    public Atividade(String nome, int tempoInvestido) throws Exception {
        this(nome, tempoInvestido, null);
    }

    public Atividade(String nome, int tempoInvestido, byte[] foto) throws Exception{
        if(nome == null || nome.equals("")) {
            throw new Exception("Nome inválido!");
        }

        if(tempoInvestido < 0){
            throw new Exception("Tempo investido inválido.");
        }

        this.nome = nome;
        this.tempoInvestido = tempoInvestido;
        this.data = new Date();
    }

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

    public String getNome() {
        return nome;
    }

    public PRIORIDADE getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(PRIORIDADE prioridade) {
        this.prioridade = prioridade;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) throws Exception{
        if(foto == null){
            throw new Exception("Foto inválida.");
        }
        this.foto = foto;
    }

    public CATEGORIA getCategoria() {
        return categoria;
    }

    public void setCategoria(CATEGORIA categoria) {
        this.categoria = categoria;
    }

    public int getTempoInvestido() {
        return tempoInvestido;
    }

    public void incrementaTempoInvestido(int tempo) throws Exception{
        if(tempo > 24 || tempo < 0){
            throw new Exception("Tempo investido deve ser maior que 0h e menor que 24h.");
        }
        this.tempoInvestido += tempo;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) throws Exception{
        if(data == null){
            throw new Exception("Data inválida.");
        }
        this.data = data;
    }

    @Override
    public int compareTo(Atividade atividade) {
        if(this.tempoInvestido < atividade.getTempoInvestido()) {
            return -1;
        } else if (this.tempoInvestido > atividade.getTempoInvestido()) {
            return 1;
        }
        return 0;

    }
}
