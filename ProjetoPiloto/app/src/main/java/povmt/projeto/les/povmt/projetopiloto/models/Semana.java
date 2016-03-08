package povmt.projeto.les.povmt.projetopiloto.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by Lucas on 08/03/2016.
 */
public class Semana {

    private Date dataInicio;
    private Date dataFim;
    private List<Atividade> atividades;

    public Semana(){
        atividades = new ArrayList<Atividade>();

    }

    public int calculaTempoTotalInvestido(){
        int soma = 0;
        for(Atividade atividade: this.atividades){
            soma += atividade.getTempo_investido();
        }
        return soma;
    }

    public float calculaProporcao(Atividade atividade) {
        if(this.calculaTempoTotalInvestido() == 0){
            return 0;
        }
        return atividade.getTempo_investido()/this.calculaTempoTotalInvestido();
    }

    public List<Atividade> ordenarMaiorTempoInvestido(){
        Collections.sort(atividades);
        return atividades;
    }
}
