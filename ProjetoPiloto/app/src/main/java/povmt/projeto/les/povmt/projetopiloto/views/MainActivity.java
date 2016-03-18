package povmt.projeto.les.povmt.projetopiloto.views;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import povmt.projeto.les.povmt.projetopiloto.ConfiguracaoActivity;
import povmt.projeto.les.povmt.projetopiloto.R;
import povmt.projeto.les.povmt.projetopiloto.adapters.ActivityAdapter;
import povmt.projeto.les.povmt.projetopiloto.adapters.DrawerListAdapter;
import povmt.projeto.les.povmt.projetopiloto.models.Atividade;
import povmt.projeto.les.povmt.projetopiloto.models.MySharedPreferences;
import povmt.projeto.les.povmt.projetopiloto.models.NavItem;
import povmt.projeto.les.povmt.projetopiloto.utils.HttpListener;
import povmt.projeto.les.povmt.projetopiloto.utils.HttpUtils;

public class MainActivity extends ActionBarActivity {

    private ListView mDrawerList;
    private TextView no_recorde;
    private RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ArrayList<NavItem> mNavItems;
    private ActivityAdapter adapter;
    private ListView listViewAtividades;
    private List<Atividade> listaAtividades;
    private HttpUtils mHttp;
    private Calendar cal = Calendar.getInstance();
    public static final String ACTION = "com.example.android.receivers.NOTIFICATION_ALARM";
    Date data;
    Date dataAtividade;
    MySharedPreferences mySharedPreferences;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavItems = new ArrayList<>();
        setmDrawer(mNavItems);

        notificar(00, 37);

        no_recorde = (TextView) findViewById(R.id.tv_no_record);

        mySharedPreferences = new MySharedPreferences(getApplicationContext());

        mHttp = new HttpUtils(this);
        listViewAtividades = (ListView) findViewById(R.id.lv_activities);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        data = new Date();

        Date dateSem = cal.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
        String dataInicioSemana = format1.format(dateSem);
        getListaAtividades(dataInicioSemana);

        listViewAtividades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Atividade atividade = (Atividade) adapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, AtividadeActivity.class);
                intent.putExtra("ATIVIDADE", atividade);
                startActivity(intent);
            }
        });

        context = this;

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void getListaAtividades(final String dataInicioSemana) {
        String url = "http://povmt-armq.rhcloud.com/findAtividadesSemana";
        final JSONObject json = new JSONObject();
        try {
            json.put("dataInicioSemana", dataInicioSemana);  //TODO PASSAR a String dataInicioSemana dada como parâmetro
            json.put("usuario", LoginActivity.emailLogado);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mHttp.post(url, json.toString(), new HttpListener() {
            @Override
            public void onSucess(JSONObject result) throws JSONException {
                no_recorde.setVisibility(View.GONE);
                listViewAtividades.setVisibility(View.VISIBLE);
                if (result.getInt("ok") == 1) {
                    JSONArray jsonArray = result.getJSONArray("result");
                    mySharedPreferences.salvaListaAtividades(jsonArray.toString());
                    listaAtividades = mySharedPreferences.getListAtividades();
                    if (listaAtividades.size() == 0) {
                        no_recorde.setVisibility(View.VISIBLE);
                        listViewAtividades.setVisibility(View.GONE);
                    }
                    adapter = new ActivityAdapter(MainActivity.this, listaAtividades);
                    listViewAtividades.setAdapter(adapter);
                }
            }

            @Override
            public void onTimeout() {
                if (mySharedPreferences.getListAtividades() != null) {
                    listaAtividades = mySharedPreferences.getListAtividades();
                }
                if (listaAtividades != null && listaAtividades.size() == 0 || listaAtividades == null) {
                    Log.d("MAIN", "Tamanho da lista é zero");
                    no_recorde.setVisibility(View.VISIBLE);
                    listViewAtividades.setVisibility(View.GONE);
                } else {
                    adapter = new ActivityAdapter(context, listaAtividades);
                    listViewAtividades.setAdapter(adapter);
//                    Log.d("MAIN", "Tamanho da lista é maior que zero");
//                    listViewAtividades.setVisibility(View.VISIBLE);
//                    no_recorde.setVisibility(View.GONE);
//                    String jsonArrayString = mySharedPreferences.getListaAtividades();
//                    try {
//                        JSONArray jsonArray = new JSONArray(jsonArrayString);
//                        carregaLista(jsonArray);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        });

    }




    public void setView(Context context, Class classe) {
        Intent it = new Intent();
        it.setClass(context, classe);
        startActivity(it);
    }

    public void setmDrawer(ArrayList<NavItem> mNavItems) {
        mNavItems.add(new NavItem("Minha semana", R.mipmap.ic_launcher));
        mNavItems.add(new NavItem("Histórico", R.mipmap.ic_launcher));
        mNavItems.add(new NavItem("Configuração", R.mipmap.ic_launcher));
        mNavItems.add(new NavItem("Sair", R.mipmap.ic_launcher));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter2 = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter2);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        mDrawerLayout.closeDrawer(mDrawerPane);
                        setView(MainActivity.this, AcompanhamentoActivity.class);
                        break;
                    case 1:
                        mDrawerLayout.closeDrawer(mDrawerPane);
                        setView(MainActivity.this, HistoricoActivity.class);
                        break;
                    case 2:
                        mDrawerLayout.closeDrawer(mDrawerPane);
                        setView(MainActivity.this, ConfiguracaoActivity.class);
                        break;
                    default:

                        break;
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void registrarAtividade(View view) {
        setView(MainActivity.this, NovaAtividadeActivity.class);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Main Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://povmt.projeto.les.povmt.projetopiloto.views/http/host/path")
//        );
//       AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
    }

    public void notificar(int hora, int minuto)
    {
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
}








