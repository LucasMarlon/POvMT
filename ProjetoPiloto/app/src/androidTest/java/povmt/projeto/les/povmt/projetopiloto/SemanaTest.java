package povmt.projeto.les.povmt.projetopiloto;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import povmt.projeto.les.povmt.projetopiloto.models.Atividade;
import povmt.projeto.les.povmt.projetopiloto.models.Semana;

/**
 * Created by MARCOS- on 24/03/2016.
 */
public class SemanaTest extends TestCase {

    private Atividade[] atividades = new Atividade[4];
    private Semana sem1;
    private Calendar cal;

    @Before
    public void setUp() throws Exception {
        cal = Calendar.getInstance();

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

        Date data = cal.getTime();
        sem1 = new Semana(data);

        for(Atividade atv: atividades){
            atv = new Atividade("Comprar iorgut",2);
        }
    }

    @Test
    public void testCriarSemana() {
        try {
            sem1 = new Semana(null);
        }catch (Exception e){
            assertEquals("Data inválida!", e.getMessage());
        }
    }

    @Test
    public void testGetsSemana() {
        Calendar cal2 = Calendar.getInstance();

        cal2.set(Calendar.HOUR_OF_DAY, 0);
        cal2.clear(Calendar.MINUTE);
        cal2.clear(Calendar.SECOND);
        cal2.clear(Calendar.MILLISECOND);
        cal2.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

        Date data2 = cal2.getTime();

        assertEquals(sem1.getDataInicio(), data2);
        assertEquals(sem1.getAtividades(), new ArrayList<Atividade>());
        assertEquals(sem1.getAtividades().size(), 0);
    }

    @Test
    public void testSetsSemana() {
        Calendar cal2 = Calendar.getInstance();

        cal2.set(Calendar.HOUR_OF_DAY, 0);
        cal2.clear(Calendar.MINUTE);
        cal2.clear(Calendar.SECOND);
        cal2.clear(Calendar.MILLISECOND);
        cal2.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

        Date dataInicio = cal2.getTime();

        sem1.setDataInicio(dataInicio);
        assertEquals(sem1.getDataInicio(), dataInicio);
    }
    @Test
    public void testAdicionaAtividadeSemana(){
        int aux = 0;
        int index = 0;
        for(Atividade atv: atividades){
            sem1.adicionaAtividade(atv);
            aux++;
            assertEquals(sem1.getAtividades().size(), aux);
            assertEquals(sem1.getAtividades().get(index), atv);
            index++;
        }
    }

    @Test
    public void testCalculoSemana() {
        assertEquals(sem1.calculaTempoTotalInvestido(), 0);
        int soma = 0;
        for(Atividade atv: atividades){
            sem1.adicionaAtividade(atv);
            soma += atv.getTempoInvestido();
            assertEquals(sem1.calculaTempoTotalInvestido(), soma);
        }
    }


}
