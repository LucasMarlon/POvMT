package povmt.projeto.les.povmt.projetopiloto.views;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

import povmt.projeto.les.povmt.projetopiloto.R;

public class AcompanhamentoActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acompanhamento);

        //Criando um array com as Proporções de TI
        ArrayList<BarEntry> entradas = new ArrayList<>();
        entradas.add(new BarEntry(10f, 4)); //O primeiro parâmetro do objeto BarEntry é um valor float e o sengundo refere-se a posição
        entradas.add(new BarEntry(8f, 3));
        entradas.add(new BarEntry(6f, 2));
        entradas.add(new BarEntry(3f, 1));
        entradas.add(new BarEntry(1f, 0));

        BarDataSet dataset = new BarDataSet(entradas, "Proporção de TI (%)");

        //Criando um array com o nome das atividades
        ArrayList<String> nomeDeAtividades = new ArrayList<String>();
        nomeDeAtividades.add("Atividade 1");
        nomeDeAtividades.add("Atividade 2");
        nomeDeAtividades.add("Atividade 3");
        nomeDeAtividades.add("Atividade 4");
        nomeDeAtividades.add("Atividade 5");

        HorizontalBarChart  chart = new HorizontalBarChart (this);
        setContentView(chart);

        BarData data = new BarData(nomeDeAtividades, dataset);
        chart.setData(data);

        chart.setDescription("Total de TI: ");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_acompanhamento, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}