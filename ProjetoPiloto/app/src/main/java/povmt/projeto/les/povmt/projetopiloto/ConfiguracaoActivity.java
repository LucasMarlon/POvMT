package povmt.projeto.les.povmt.projetopiloto;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;


public class ConfiguracaoActivity extends ActionBarActivity {

    private Switch switchLembrete;
    private TimePicker tp_hora_notificacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);

        switchLembrete = (Switch) findViewById(R.id.switchLembrete);
        tp_hora_notificacao = (TimePicker) findViewById(R.id.tp_hora_notificacao);

        //definir o switch para ON
        switchLembrete.setChecked(true);
        //verificar se há mudanças no estado
        switchLembrete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tp_hora_notificacao.setEnabled(true);
                }else{
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
