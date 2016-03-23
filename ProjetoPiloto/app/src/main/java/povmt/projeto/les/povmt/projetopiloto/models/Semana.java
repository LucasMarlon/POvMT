package povmt.projeto.les.povmt.projetopiloto.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class Semana {

    private Date dataInicio;
    private List<Atividade> atividades;
    private Date dataFim;

    public Semana(Date dataInicio){
        atividades = new ArrayList<Atividade>();
        this.dataInicio = dataInicio;

    }

    public Semana (Date dataInicio, Date dataFim) {
        atividades = new ArrayList<Atividade>();
        this.dataInicio = dataInicio;
        this.dataFim =  dataFim;
    }

    public int calculaTempoTotalInvestido(){
        int soma = 0;
        for(Atividade atividade: this.atividades){
            if (atividade != null)
                soma += atividade.getTempoInvestido();
        }
        return soma;
    }

    public float calculaProporcaoTempoInvestido(Atividade atividade) {
        if(this.calculaTempoTotalInvestido() == 0){
            return 0;
        }
        return (atividade.getTempoInvestido()/(float)calculaTempoTotalInvestido()) * 100;
    }

    public void adicionaAtividade(Atividade atividade) {
        atividades.add(atividade);
    }

    public List<Atividade> getAtividadesOrdenadas(){
        Collections.sort(atividades);
        return atividades;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public List<Atividade> getAtividades() {
        return atividades;
    }


}
