package povmt.projeto.les.povmt.projetopiloto.views;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import povmt.projeto.les.povmt.projetopiloto.R;
import povmt.projeto.les.povmt.projetopiloto.utils.HttpUtils;

public class AtividadeActivity extends ActionBarActivity {

    private HttpUtils mHttp;
    private View mLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atividade);
    }


}
