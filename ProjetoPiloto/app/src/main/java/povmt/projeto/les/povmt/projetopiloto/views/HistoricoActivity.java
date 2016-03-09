package povmt.projeto.les.povmt.projetopiloto.views;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

import povmt.projeto.les.povmt.projetopiloto.R;
import povmt.projeto.les.povmt.projetopiloto.models.Atividade;
import povmt.projeto.les.povmt.projetopiloto.models.Semana;

public class HistoricoActivity extends ActionBarActivity {

    private HorizontalBarChart mChart1;
    private HorizontalBarChart mChart2;
    private HorizontalBarChart mChart3;
    private Semana semanaAtual;
    private TextView tv_total_ti1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        tv_total_ti1 = (TextView) findViewById(R.id.tv_total_ti1);

        mChart1 = (HorizontalBarChart) findViewById(R.id.chart1);
        mChart2 = (HorizontalBarChart) findViewById(R.id.chart2);
        mChart3 = (HorizontalBarChart) findViewById(R.id.chart3);

        preencheGrafico(mChart1);
        preencheGrafico(mChart2);
        preencheGrafico(mChart3);
    }

    private void preencheGrafico(HorizontalBarChart chart) {
        //Criando um array com as Proporções de TI
        ArrayList<BarEntry> entradas = new ArrayList<>();
        ArrayList<String> nomeDeAtividades = new ArrayList<String>();

        List<Atividade> atividadesSemanaAtual = semanaAtual.getAtividadesOrdenadas();

        for (int i = atividadesSemanaAtual.size()-1; i >= 0; i--) {
            entradas.add(new BarEntry(semanaAtual.calculaProporcaoTempoInvestido(atividadesSemanaAtual.get(i)), i));
            nomeDeAtividades.add(atividadesSemanaAtual.get(i).getNome());
        }

        BarDataSet dataset = new BarDataSet(entradas, "Proporção de TI (%)");

        BarData data = new BarData(nomeDeAtividades, dataset);
        chart.setData(data);
        tv_total_ti1.setText("Total de TI: " + semanaAtual.calculaTempoTotalInvestido() + "hs");
        chart.setDescription("");
        chart.invalidate();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_historico, menu);
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
