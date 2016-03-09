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

import povmt.projeto.les.povmt.projetopiloto.R;
import povmt.projeto.les.povmt.projetopiloto.utils.HttpListener;
import povmt.projeto.les.povmt.projetopiloto.utils.HttpUtils;

public class NovaAtividadeActivity extends ActionBarActivity {

    private TextView dataInicial;
    private TextView dataFinal;
    private HttpUtils mHttp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_atividade);

        mHttp = new HttpUtils(this);

        final EditText nomeAtividade = (EditText) findViewById(R.id.et_nome);
        final Button registraAtividade =  (Button) findViewById(R.id.bt_add_atividade);
        dataInicial = (TextView) findViewById(R.id.tv_data_inicio);
        dataFinal = (TextView) findViewById(R.id.tv_data_final);

        registraAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = nomeAtividade.getText().toString();
                registraAtividade(nome);
            }
        });
    }

    private void registraAtividade(final String nome) {
        if (nome.equals("")) {
            new AlertDialog.Builder(NovaAtividadeActivity.this)
                    .setTitle("Erro")
                    .setMessage("O nome da atividade n�o pode ser vazio.")
                    .setNeutralButton("OK", null)
                    .create()
                    .show();
        } else {
            String url = "http://povmt-armq.rhcloud.com/cadastrarAtividade";
            JSONObject json = new JSONObject();
            try {
                json.put("nomeAtividade", nome);
                json.put("dataInicioSemana", "08/03/2016");
                /*json.put("dataFimSemana", "");
                json.put("prioridade", "");
                json.put("foto", "");
                json.put("categoria", "");
                json.put("dataAtividade", "");*/
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mHttp.post(url, json.toString(), new HttpListener() {
                @Override
                public void onSucess(JSONObject result) {
                    Log.d("RESULT",result.toString());
                    try {
                        Log.d("RESULT",result.getInt("ok") + "");
                        if (result.getInt("ok") == 0) {
                            Log.d("NOVA_ACTIVITY", "ok = 0");
                            new AlertDialog.Builder(NovaAtividadeActivity.this)
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
                            Log.d("NOVA_ACTIVITY", "ok = 1");
                            new AlertDialog.Builder(NovaAtividadeActivity.this)
                                    .setTitle("Sucesso")
                                    .setMessage("Cadastro realizado com sucesso")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            setView(NovaAtividadeActivity.this, MainActivity.class);
                                        }
                                    })
                                    .create()
                                    .show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            @Override
            public void onTimeout() {
                new AlertDialog.Builder(NovaAtividadeActivity.this)
                    .setTitle("Erro")
                    .setMessage("Conex�o n�o dispon�vel.")
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