package povmt.projeto.les.povmt.projetopiloto.views;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import povmt.projeto.les.povmt.projetopiloto.R;
import povmt.projeto.les.povmt.projetopiloto.models.Atividade;
import povmt.projeto.les.povmt.projetopiloto.utils.HttpUtils;

public class AtividadeActivity extends ActionBarActivity {

    private HttpUtils mHttp;

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

        try {
            atividade.incrementaTempoInvestido(tempo);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}