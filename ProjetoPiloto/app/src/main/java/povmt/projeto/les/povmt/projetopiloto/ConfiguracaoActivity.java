package povmt.projeto.les.povmt.projetopiloto;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TimePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import povmt.projeto.les.povmt.projetopiloto.utils.HttpListener;
import povmt.projeto.les.povmt.projetopiloto.utils.HttpUtils;
import povmt.projeto.les.povmt.projetopiloto.views.LoginActivity;


public class ConfiguracaoActivity extends ActionBarActivity {

    private Switch switchLembrete;
    private TimePicker tp_hora_notificacao;
    private HttpUtils mHttp;
    private Calendar cal = Calendar.getInstance();
    private boolean foiCadastradoTI;
    public static final String ACTION = "com.example.android.receivers.NOTIFICATION_ALARM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);

        mHttp = new HttpUtils(this);
        foiCadastradoTI = false;
        verificaSeCadastrouTI();

        notificar(00, 37);
        Log.d("DANI", "NOTI");

        switchLembrete = (Switch) findViewById(R.id.switchLembrete);
        tp_hora_notificacao = (TimePicker) findViewById(R.id.tp_hora_notificacao);

        //definir o switch para ON
        switchLembrete.setChecked(true);
        //verificar se há mudanças no estado
        switchLembrete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tp_hora_notificacao.setEnabled(true);
                } else {
                    tp_hora_notificacao.setEnabled(false);
                }

            }
        });

        //verificar o estado atual antes de exibir a tela
        if(switchLembrete.isChecked()){
            tp_hora_notificacao.setEnabled(true);
        }
        else {
            tp_hora_notificacao.setEnabled(false);
        }


}

    public void notificar(int hora, int minuto)
    {
        if (!foiCadastradoTI) {

        }
        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();
        calSet.setTimeInMillis(System.currentTimeMillis());
        calSet.set(Calendar.HOUR_OF_DAY, hora);
        calSet.set(Calendar.MINUTE, minuto);
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);

        setAlarm(calSet);
    }

    private void setAlarm(Calendar targetCall)
    {
        Intent intent = new Intent(ACTION);
        PendingIntent pendingintent = PendingIntent.getBroadcast(getBaseContext(), 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCall.getTimeInMillis(), pendingintent);
    }

    private void verificaSeCadastrouTI() {
        String url = "http://povmt-armq.rhcloud.com/findAtividadesSemana";
        JSONObject json = new JSONObject();
        try {
            cal.setTime(new Date());
            cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
            Date dateSem = cal.getTime();
            DateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
            String dataInicioSemana = format1.format(dateSem);

            json.put("dataInicioSemana", dataInicioSemana);
            json.put("usuario", LoginActivity.emailLogado);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mHttp.post(url, json.toString(), new HttpListener() {
            @Override
            public void onSucess(JSONObject result) throws JSONException {
                if (result.getInt("ok") == 1) {
                    try {
                        JSONArray array = result.getJSONArray("result");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            Boolean foiCadastradoTIHoje = obj.getBoolean("foiCadastradoTIHoje");
                            Boolean foiCadastradoTIOntem = obj.getBoolean("foiCadastradoTIOntem");

                            if (foiCadastradoTIOntem || foiCadastradoTIHoje) {
                                foiCadastradoTI = true;
                                break;
                            }
                        }
                    } catch (Exception e) {


                    }
                }
            }

            @Override
            public void onTimeout() {
                new AlertDialog.Builder(ConfiguracaoActivity.this)
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_configuracao, menu);
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
