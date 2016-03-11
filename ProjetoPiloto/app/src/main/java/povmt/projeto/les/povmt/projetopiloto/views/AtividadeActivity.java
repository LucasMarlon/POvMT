package povmt.projeto.les.povmt.projetopiloto.views;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import povmt.projeto.les.povmt.projetopiloto.R;
import povmt.projeto.les.povmt.projetopiloto.models.Atividade;
import povmt.projeto.les.povmt.projetopiloto.utils.HttpListener;
import povmt.projeto.les.povmt.projetopiloto.utils.HttpUtils;

public class AtividadeActivity extends ActionBarActivity {

    private HttpUtils mHttp;
    private Calendar cal = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atividade);

        mHttp = new HttpUtils(this);
        Button btAddTime = (Button) findViewById(R.id.bt_add_time);
        final EditText etTime = (EditText) findViewById(R.id.et_time);

        btAddTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempoInvestido = etTime.getText().toString();
                registrarTI(Integer.parseInt(tempoInvestido));
            }
        });
    }

    private void registrarTI(int tempo) {
        Intent it = getIntent();
        Atividade atividade = (Atividade) it.getSerializableExtra("ATIVIDADE");

        String url = "http://povmt-armq.rhcloud.com/incrementaTempoInvestido";
        JSONObject json = new JSONObject();
        try {
            cal.setTime(atividade.getData());
            cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
            Date dateSem = cal.getTime();
            DateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
            String dataInicioSemana = format1.format(dateSem);

            System.out.println("DATA: " + dataInicioSemana);

            json.put("nomeAtividade", atividade.getNome());
            json.put("dataInicioSemana", dataInicioSemana);
            json.put("tempoInvestido", tempo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mHttp.post(url, json.toString(), new HttpListener() {
            @Override
            public void onSucess(JSONObject result) throws JSONException {
                if (result.getInt("ok") == 0) {
                    new AlertDialog.Builder(AtividadeActivity.this)
                            .setTitle("Erro")
                            .setMessage(result.getString("msg"))
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //  mLoading.setVisibility(View.GONE);
                                }
                            })
                            .create()
                            .show();
                } else {
                    new AlertDialog.Builder(AtividadeActivity.this)
                            .setTitle("Sucesso")
                            .setMessage("TI registrado com sucesso")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    setView(AtividadeActivity.this, MainActivity.class);
                                }
                            })
                            .create()
                            .show();
                }
            }

            @Override
            public void onTimeout() {
                new AlertDialog.Builder(AtividadeActivity.this)
                        .setTitle("Erro")
                        .setMessage("Conexão não disponível.")
                        .setNeutralButton("OK", null)
                        .create()
                        .show();
            }
        });
    }

    public void setView(Context context, Class classe){
        Intent it = new Intent();
        it.setClass(context, classe);
        startActivity(it);
    }

}