package povmt.projeto.les.povmt.projetopiloto.views;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import povmt.projeto.les.povmt.projetopiloto.R;
import povmt.projeto.les.povmt.projetopiloto.models.Atividade;
import povmt.projeto.les.povmt.projetopiloto.models.MySharedPreferences;
import povmt.projeto.les.povmt.projetopiloto.models.Semana;
import povmt.projeto.les.povmt.projetopiloto.utils.HttpUtils;

public class AcompanhamentoActivity extends ActionBarActivity {

    private HttpUtils mHttp;
    private Calendar cal = Calendar.getInstance();
    private Semana semanaAtual;
    private HorizontalBarChart mChart;
    private TextView tv_total_ti;
    private MySharedPreferences mySharedPreferences;
    private List<Atividade> listaAtividades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acompanhamento);

        tv_total_ti = (TextView) findViewById(R.id.tv_total_ti);
        mChart = (HorizontalBarChart) findViewById(R.id.chart);
        mHttp = new HttpUtils(this);

        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

        Date time = cal.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
        String dateFormat = format1.format(time);

        mySharedPreferences = new MySharedPreferences(getApplicationContext());
        semanaAtual = new Semana(time);
        listaAtividades = new ArrayList<>();
        if (mySharedPreferences.getListAtividades() != null) {
            listaAtividades = mySharedPreferences.getListAtividades();
        }
        if (listaAtividades.size() > 0) {
            for (Atividade atividade : listaAtividades) {
                semanaAtual.adicionaAtividade(atividade);
            }
            preencheGrafico(mChart);
            mChart.setVisibility(View.VISIBLE);
        }
    }

    private void preencheGrafico(HorizontalBarChart chart) {

        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        ArrayList<BarEntry> priAlta = new ArrayList<>();
        ArrayList<BarEntry> priMedia = new ArrayList<>();
        ArrayList<BarEntry> priBaixa = new ArrayList<>();

        List<Atividade> atividadesSemanaAtual = semanaAtual.getAtividadesOrdenadas();

        for (int i = atividadesSemanaAtual.size()-1; i >= 0; i--) {
            if (atividadesSemanaAtual.get(i).getPrioridade().getValor().equals("Alta")){
                priAlta.add(new BarEntry(semanaAtual.calculaProporcaoTempoInvestido(atividadesSemanaAtual.get(i)), i));
            }
            else if (atividadesSemanaAtual.get(i).getPrioridade().getValor().equals("Média")){
                priMedia.add(new BarEntry(semanaAtual.calculaProporcaoTempoInvestido(atividadesSemanaAtual.get(i)), i));
            }
            else if (atividadesSemanaAtual.get(i).getPrioridade().getValor().equals("Baixa")){
                priBaixa.add(new BarEntry(semanaAtual.calculaProporcaoTempoInvestido(atividadesSemanaAtual.get(i)), i));
            }
        }

        ArrayList<String> nomeDeAtividades = new ArrayList<String>();

        for (int i = 0; i < atividadesSemanaAtual.size(); i++) {
            nomeDeAtividades.add(atividadesSemanaAtual.get(i).getNome());
        }

        BarDataSet barDataSetAlta = new BarDataSet(priAlta, "Alta - Proporção de TI (%)");
        barDataSetAlta.setColor(Color.rgb(0, 155, 0));
        BarDataSet barDataSetMedia = new BarDataSet(priMedia, "Média");
        barDataSetMedia.setColor(Color.rgb(255, 0, 0));
        BarDataSet barDataSetBaixa = new BarDataSet(priBaixa, "Prioridade Baixa");
        barDataSetBaixa.setColor(Color.rgb(0, 0, 255));

        dataSets.add(barDataSetBaixa);
        dataSets.add(barDataSetMedia);
        dataSets.add(barDataSetAlta);

        BarData data = new BarData(nomeDeAtividades, dataSets);
        chart.setData(data);
        tv_total_ti.setText("Total de TI: " + semanaAtual.calculaTempoTotalInvestido() + "hs");
        chart.setDescription("");
        chart.invalidate();
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