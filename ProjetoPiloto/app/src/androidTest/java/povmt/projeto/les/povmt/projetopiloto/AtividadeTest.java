package povmt.projeto.les.povmt.projetopiloto;

import android.graphics.Bitmap;
import android.util.Base64;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import povmt.projeto.les.povmt.projetopiloto.models.Atividade;
import povmt.projeto.les.povmt.projetopiloto.models.CATEGORIA;
import povmt.projeto.les.povmt.projetopiloto.models.PRIORIDADE;

/**
 * Created by MARCOS- on 23/03/2016.
 */
public class AtividadeTest extends TestCase {

    Atividade atv1, atv2, atv3;
    Calendar cal;
    Date data;

    @Before
    public void setUp() throws Exception {
        atv1 = new Atividade("Comprar iorgut", 2);
        atv2 = new Atividade("Estudar, estudar e estudar",0);
        atv3 = new Atividade("Ir ao cinema", 3, data, PRIORIDADE.BAIXA, CATEGORIA.LAZER, null);

        cal = Calendar.getInstance();
        data = cal.getTime();
    }

    @Test
    public void testCriarAtividade(){
        try{
            atv1 = new Atividade(null,0);
            atv3 = new Atividade(null,3);
        }catch (Exception e){
            Assert.assertEquals("Nome inválido!", e.getMessage());
        }
        try{
            atv1 = new Atividade("",0);
            atv3 = new Atividade("",3);
        }catch (Exception e){
            assertEquals("Nome inválido!", e.getMessage());
        }
        try{
            atv1 = new Atividade("Comprar iorgut",-1);
            atv3 = new Atividade("Estudar, estudar e estudar", -10);
        }catch (Exception e){
            assertEquals("Tempo investido inválido.", e.getMessage());
        }
        try{
            atv1.setFoto(null);
            atv2.setFoto(null);
        }catch (Exception e){
            assertEquals("Foto inválida.", e.getMessage());
        }
        try{
            atv1.setData(null);
            atv3.setData(data);
        }catch (Exception e){
            assertEquals("Data inválida.", e.getMessage());
        }
    }

    @Test
    public void testGetsAtividade() {
        assertEquals(atv1.getNome(), new String("Comprar iorgut"));
        assertEquals(atv1.getTempoInvestido(), 2);
        assertEquals(atv1.getCategoria(), null);
        assertEquals(atv1.getData(), null);
        assertEquals(atv1.getFoto(), null);
        assertEquals(atv1.getPrioridade(), null);
    }
    @Test
    public void testSetsAtividade() throws Exception {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        byte[] fotoByte = b.toByteArray();
        String fotoString = Base64.encodeToString(fotoByte, Base64.NO_WRAP);

        atv1.setCategoria(CATEGORIA.TRABALHO);
        atv1.setData(data);
        atv1.setFoto(fotoByte);
        atv1.setPrioridade(PRIORIDADE.ALTA);

        assertEquals(atv1.getNome(), new String("Comprar iorgut"));
        assertEquals(atv1.getTempoInvestido(), 2);
        assertEquals(atv1.getCategoria(), CATEGORIA.TRABALHO);
        assertEquals(atv1.getData(), data);
        assertEquals(atv1.getFoto(), fotoString);
        assertEquals(atv1.getPrioridade(), PRIORIDADE.ALTA);
        atv3.setPrioridade(PRIORIDADE.MEDIA);
        assertEquals(atv3.getPrioridade(), PRIORIDADE.MEDIA);
    }

    @Test
    public void testIncrementaTempoInvestidoAtividade() throws Exception {
        try{
            atv1.incrementaTempoInvestido(27);
        }catch (Exception e){
            assertEquals("Tempo investido deve ser maior que 0h e menor que 24h.", e.getMessage());
        }
        try{
            atv1.incrementaTempoInvestido(-4);
        }catch (Exception e){
            assertEquals("Tempo investido deve ser maior que 0h e menor que 24h.", e.getMessage());
        }

        atv1.incrementaTempoInvestido(0);
        assertEquals(atv1.getTempoInvestido(), 2);
        atv1.incrementaTempoInvestido(24);
        assertEquals(atv1.getTempoInvestido(), 26);
        atv1.incrementaTempoInvestido(5);
        assertEquals(atv1.getTempoInvestido(), 3);
        atv1.incrementaTempoInvestido(20);
        assertEquals(atv1.getTempoInvestido(), 51);
    }

    @Test
    public void testCompareToAtividade() throws Exception {
        assertEquals(atv1.compareTo(atv2), 0);
        assertEquals(atv2.compareTo(atv1), 0);

        atv2.incrementaTempoInvestido(2);
        assertEquals(atv1.compareTo(atv2), -1);
        assertEquals(atv2.compareTo(atv1), 1);

        atv1.incrementaTempoInvestido(10);
        assertEquals(atv1.compareTo(atv2), 1);
        assertEquals(atv2.compareTo(atv1), -1);
    }
}
