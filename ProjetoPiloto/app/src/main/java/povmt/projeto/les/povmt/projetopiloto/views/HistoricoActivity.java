package povmt.projeto.les.povmt.projetopiloto.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import povmt.projeto.les.povmt.projetopiloto.R;
import povmt.projeto.les.povmt.projetopiloto.models.Atividade;
import povmt.projeto.les.povmt.projetopiloto.models.MySharedPreferences;
import povmt.projeto.les.povmt.projetopiloto.models.Semana;
import povmt.projeto.les.povmt.projetopiloto.utils.HttpListener;
import povmt.projeto.les.povmt.projetopiloto.utils.HttpUtils;

public class HistoricoActivity extends ActionBarActivity {

    private HttpUtils mHttp;
    private Calendar calSemana1 = Calendar.getInstance();
    private Calendar calSemana2 = Calendar.getInstance();
    private Calendar calSemana3 = Calendar.getInstance();
    private HorizontalBarChart mChart1;
    private HorizontalBarChart mChart2;
    private HorizontalBarChart mChart3;
    private Semana semana;
    private Semana semanaAnterior;
    private Semana semanaRetrasada;
    private TextView tv_total_ti1;
    private TextView tv_total_ti2;
    private TextView tv_total_ti3;
    private MySharedPreferences mySharedPreferences;
    private List<Atividade> listaAtividadesSemanaAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        tv_total_ti1 = (TextView) findViewById(R.id.tv_total_ti1);
        tv_total_ti2 = (TextView) findViewById(R.id.tv_total_ti2);
        tv_total_ti3 = (TextView) findViewById(R.id.tv_total_ti3);

        mChart1 = (HorizontalBarChart) findViewById(R.id.chart1);
        mChart2 = (HorizontalBarChart) findViewById(R.id.chart2);
        mChart3 = (HorizontalBarChart) findViewById(R.id.chart3);

        mHttp = new HttpUtils(this);

        calSemana1.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        calSemana1.clear(Calendar.MINUTE);
        calSemana1.clear(Calendar.SECOND);
        calSemana1.clear(Calendar.MILLISECOND);
        calSemana1.set(Calendar.DAY_OF_WEEK, calSemana1.getFirstDayOfWeek());

        Date time = calSemana1.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
        String dateFormat = format1.format(time);

        mySharedPreferences = new MySharedPreferences(getApplicationContext());
        semana = new Semana(time);
        listaAtividadesSemanaAtual = new ArrayList<>();
        if (mySharedPreferences.getListAtividades() != null) {
            listaAtividadesSemanaAtual = mySharedPreferences.getListAtividades();
        }
        semana = new Semana(time);
        if (listaAtividadesSemanaAtual.size() > 0) {
            for (Atividade atividade : listaAtividadesSemanaAtual) {
                semana.adicionaAtividade(atividade);
            }
            preencheGrafico(mChart1, semana, tv_total_ti1);
            mChart1.setVisibility(View.VISIBLE);
        }

        String url = "http://povmt-armq.rhcloud.com/findAtividadesSemana";
        calSemana2.setTime(time);
        calSemana2.add(Calendar.DAY_OF_YEAR, -7);
        calSemana2.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        calSemana2.clear(Calendar.MINUTE);
        calSemana2.clear(Calendar.SECOND);
        calSemana2.clear(Calendar.MILLISECOND);
        calSemana2.set(Calendar.DAY_OF_WEEK, calSemana2.getFirstDayOfWeek());

        Date time2 = calSemana2.getTime();
        String dateSemana2 = format1.format(time2);

        semanaAnterior = new Semana(time2);

        JSONObject json2 = new JSONObject();
        try {
            json2.put("dataInicioSemana", dateSemana2);
            json2.put("usuario", LoginActivity.emailLogado);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mHttp.post(url, json2.toString(), new HttpListener() {
            @Override
            public void onSucess(final JSONObject result) {
                try {
                    if (result.getInt("ok") == 0) {
                        new AlertDialog.Builder(HistoricoActivity.this)
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
                            semanaAnterior.adicionaAtividade(atv);
                        }
                        if(array.length() != 0) {
                            preencheGrafico(mChart2, semanaAnterior, tv_total_ti2);
                            mChart2.setVisibility(View.VISIBLE);
                        } else {
                            new AlertDialog.Builder(HistoricoActivity.this)
                                    .setTitle("HISTÓRICO")
                                    .setMessage("Nenhuma atividade registrada na semana anterior.")
                                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    })
                                    .create()
                                    .show();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTimeout() {
                new AlertDialog.Builder(HistoricoActivity.this)
                        .setTitle("Erro")
                        .setMessage("Conexão não disponível.")
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create()
                        .show();
            }
        });

        calSemana3.setTime(time2);
        calSemana3.add(Calendar.DAY_OF_YEAR, -7);
        calSemana3.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        calSemana3.clear(Calendar.MINUTE);
        calSemana3.clear(Calendar.SECOND);
        calSemana3.clear(Calendar.MILLISECOND);
        calSemana3.set(Calendar.DAY_OF_WEEK, calSemana3.getFirstDayOfWeek());

        Date time3 = calSemana3.getTime();
        String dateSemana3 = format1.format(time3);

        semanaRetrasada = new Semana(time3);

        JSONObject json3 = new JSONObject();
        try {
            json3.put("dataInicioSemana", dateSemana3);
            json3.put("usuario", LoginActivity.emailLogado);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mHttp.post(url, json3.toString(), new HttpListener() {
            @Override
            public void onSucess(final JSONObject result) {
                try {
                    if (result.getInt("ok") == 0) {
                        new AlertDialog.Builder(HistoricoActivity.this)
                                .setTitle("Erro")
                                .setMessage(result.getString("msg"))
                                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

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
                            semanaRetrasada.adicionaAtividade(atv);
                        }
                        if(array.length() != 0) {
                            preencheGrafico(mChart3, semanaRetrasada, tv_total_ti3);
                            mChart3.setVisibility(View.VISIBLE);
                        } else {
                            new AlertDialog.Builder(HistoricoActivity.this)
                                    .setTitle("HISTÓRICO")
                                    .setMessage("Nenhuma atividade registrada na semana retrasada.")
                                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    })
                                    .create()
                                    .show();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTimeout() {
                new AlertDialog.Builder(HistoricoActivity.this)
                        .setTitle("Erro")
                        .setMessage("Conexão não disponível.")
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create()
                        .show();
            }
        });

    }

    private void preencheGrafico(HorizontalBarChart chart, Semana semana, TextView tv_total_ti) {
        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        ArrayList<BarEntry> priAlta = new ArrayList<>();
        ArrayList<BarEntry> priMedia = new ArrayList<>();
        ArrayList<BarEntry> priBaixa = new ArrayList<>();

        List<Atividade> atividadesSemana = semana.getAtividadesOrdenadas();

        for (int i = atividadesSemana.size()-1; i >= 0; i--) {
            if (atividadesSemana.get(i).getPrioridade().getValor().equals("Alta")){
                priAlta.add(new BarEntry(this.semana.calculaProporcaoTempoInvestido(atividadesSemana.get(i)), i));
            }
            else if (atividadesSemana.get(i).getPrioridade().getValor().equals("Média")){
                priMedia.add(new BarEntry(this.semana.calculaProporcaoTempoInvestido(atividadesSemana.get(i)), i));
            }
            else if (atividadesSemana.get(i).getPrioridade().getValor().equals("Baixa")){
                priBaixa.add(new BarEntry(this.semana.calculaProporcaoTempoInvestido(atividadesSemana.get(i)), i));
            }
        }

        ArrayList<String> nomeDeAtividades = new ArrayList<String>();

        for (int i = 0; i < atividadesSemana.size(); i++) {
            nomeDeAtividades.add(atividadesSemana.get(i).getNome());
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
        tv_total_ti.setText("Total de TI: " + semana.calculaTempoTotalInvestido() + "hs");
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