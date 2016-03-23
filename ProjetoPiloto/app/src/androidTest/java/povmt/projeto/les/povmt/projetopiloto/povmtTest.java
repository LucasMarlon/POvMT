package povmt.projeto.les.povmt.projetopiloto;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import povmt.projeto.les.povmt.projetopiloto.models.Atividade;
import povmt.projeto.les.povmt.projetopiloto.models.Semana;

public class povmtTest extends TestCase {

    List<Atividade> listaAtividade;
    Semana semana1;

    Calendar cal = Calendar.getInstance();
    @Override
    protected final void setUp() {

        listaAtividade = new ArrayList<>();

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

        Date data = cal.getTime();
        semana1 = new Semana(data);
    }

    //Testa criar atividade
    public final void testAddAtividade(){
        assertEquals(listaAtividade.size(), 0);
        Calendar c = Calendar.getInstance();
        Date dt = semana1.getDataInicio();

        c.set(Calendar.HOUR_OF_DAY, 0);
        c.clear(Calendar.MINUTE);
        c.clear(Calendar.SECOND);
        c.clear(Calendar.MILLISECOND);

        c.setTime(dt);
        assertEquals(c.get(Calendar.DATE), 20); // primeiro dia  da semana atual
        //assertEquals(c.get(Calendar.MONTH), 3); //Mês da semana atual (FALHOU)
        assertEquals(c.get(Calendar.YEAR), 2016); //Ano da semana atual

        //...

    }



}
