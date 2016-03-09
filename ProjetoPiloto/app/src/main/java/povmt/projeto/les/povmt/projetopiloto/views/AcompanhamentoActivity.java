package povmt.projeto.les.povmt.projetopiloto.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import povmt.projeto.les.povmt.projetopiloto.R;
import povmt.projeto.les.povmt.projetopiloto.models.Atividade;
import povmt.projeto.les.povmt.projetopiloto.models.Semana;
import povmt.projeto.les.povmt.projetopiloto.utils.HttpListener;
import povmt.projeto.les.povmt.projetopiloto.utils.HttpUtils;

public class AcompanhamentoActivity extends ActionBarActivity {

    private HttpUtils mHttp;
    private Calendar cal = Calendar.getInstance();
    private Semana semanaAtual;
    private HorizontalBarChart mChart;
    private TextView tv_total_ti;

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

        semanaAtual = new Semana(cal.getTime());



        String url = "http://povmt-armq.rhcloud.com/findAtividadesSemana";
        JSONObject json = new JSONObject();
        try {
            json.put("dataInicioSemana", "08/03/2016");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mHttp.post(url, json.toString(), new HttpListener() {
            @Override
            public void onSucess(final JSONObject result) {
                try {
                    if (result.getInt("ok") == 0) {
                        new AlertDialog.Builder(AcompanhamentoActivity.this)
                                .setTitle("Erro")
                                .setMessage(result.getString("msg"))
                                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //mLoading.setVisibility(View.GONE);
                                    }
                                })
                                .create()
                                .show();
                    } else {
                        JSONArray array = result.getJSONArray("result");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            String nome = obj.getString("nomeAtividade");
                            int tempoInvestido = obj.getInt("tempoInvestido");
                            Atividade atv = new Atividade(nome, tempoInvestido);
                            semanaAtual.adicionaAtividade(atv);
                        }
                        criaGrafico(mChart);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTimeout() {
                new AlertDialog.Builder(AcompanhamentoActivity.this)
                        .setTitle("Erro")
                        .setMessage("Conexão não disponível.")
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // mLoading.setVisibility(View.GONE);
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    private void criaGrafico(HorizontalBarChart chart) {
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