package povmt.projeto.les.povmt.projetopiloto.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import povmt.projeto.les.povmt.projetopiloto.R;
import povmt.projeto.les.povmt.projetopiloto.utils.HttpListener;
import povmt.projeto.les.povmt.projetopiloto.utils.HttpUtils;

public class NovaAtividadeActivity extends ActionBarActivity {

    private HttpUtils mHttp;
    private Calendar cal = Calendar.getInstance();
    Date data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_atividade);

        mHttp = new HttpUtils(this);

        final EditText nomeAtividade = (EditText) findViewById(R.id.et_nome);
        final Button registraAtividade =  (Button) findViewById(R.id.bt_add_atividade);

        registraAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
                cal.clear(Calendar.MINUTE);
                cal.clear(Calendar.SECOND);
                cal.clear(Calendar.MILLISECOND);
                cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                data = new Date();

                Date dateSem = cal.getTime();
                String nome = nomeAtividade.getText().toString();
                SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
                String dataSemana = format1.format(dateSem);
                String dataAtual = format1.format(data.getTime());
                registraAtividade(nome, dataSemana, dataAtual);
            }
        });


    }

    private void registraAtividade(final String nome, final String dataSemana, final String dataAtual) {
        if (nome.equals("")) {
            new AlertDialog.Builder(NovaAtividadeActivity.this)
                    .setTitle("Erro")
                    .setMessage("O nome da atividade não pode ser vazio.")
                    .setNeutralButton("OK", null)
                    .create()
                    .show();
        } else {
            String url = "http://povmt-armq.rhcloud.com/cadastrarAtividade";
            JSONObject json = new JSONObject();
            try {
                json.put("nomeAtividade", nome);
                json.put("dataInicioSemana", dataSemana);
                json.put("dataFimSemana", "");
                json.put("prioridade", "");
                json.put("foto", "");
                json.put("categoria", "");
                json.put("dataAtividade", dataAtual);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mHttp.post(url, json.toString(), new HttpListener() {
                @Override
                public void onSucess(JSONObject result) throws JSONException{
                    if (result.getInt("ok") == 0) {
                        new AlertDialog.Builder(NovaAtividadeActivity.this)
                                .setTitle("Erro")
                                .setMessage(result.getString("msg"))
                                .setNeutralButton("OK", null)
                                .create()
                                .show();
                    } else {
                        new AlertDialog.Builder(NovaAtividadeActivity.this)
                                .setTitle("Sucesso")
                                .setMessage("Atividade registrada com sucesso")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        setView(NovaAtividadeActivity.this, MainActivity.class);
                                    }
                                })
                                .create()
                                .show();
                    }
                }

            @Override
            public void onTimeout() {
                new AlertDialog.Builder(NovaAtividadeActivity.this)
                    .setTitle("Erro")
                    .setMessage("Conexão não disponível")
                    .setNeutralButton("OK", null)
                    .create()
                    .show();
            }
            });
        }
    }

    public void setView(Context context, Class classe){
        Intent it = new Intent();
        it.setClass(context, classe);
        startActivity(it);
    }

}