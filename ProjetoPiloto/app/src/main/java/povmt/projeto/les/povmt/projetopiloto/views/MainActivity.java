package povmt.projeto.les.povmt.projetopiloto.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.ContactsContract;
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

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import povmt.projeto.les.povmt.projetopiloto.R;
import povmt.projeto.les.povmt.projetopiloto.adapters.ActivityAdapter;
import povmt.projeto.les.povmt.projetopiloto.adapters.DrawerListAdapter;
import povmt.projeto.les.povmt.projetopiloto.models.Atividade;
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
    Date data;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private static final String PREFER_NAME = "Pref";
    private static final String KEY_LISTA = "lista_atividades";
    int PRIVATE_MODE = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavItems = new ArrayList<>();
        setmDrawer(mNavItems);

        no_recorde = (TextView) findViewById(R.id.tv_no_record);
        pref = this.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();



        listaAtividades = new ArrayList<>();

        mHttp = new HttpUtils(this);
        listViewAtividades = (ListView) findViewById(R.id.lv_activities);

        String dataInicioSemana = ""; //TODO recuperar a data do início da semana
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

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();

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
//        AppIndex.AppIndexApi.start(client, viewAction);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public void getListaAtividades(final String dataInicioSemana) {
        String url = "http://povmt-armq.rhcloud.com/findAtividadesSemana";
        JSONObject json = new JSONObject();
        try {
            json.put("dataInicioSemana", "08/03/2016");  //TODO PASSAR a String dataInicioSemana dada como parâmetro
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
                    editor.putString(KEY_LISTA, jsonArray.toString());
                    editor.commit();
                    carregaLista(jsonArray);
                }
            }

            @Override
            public void onTimeout() {

                if (listaAtividades != null && listaAtividades.size() == 0) {
                    no_recorde.setVisibility(View.VISIBLE);
                    listViewAtividades.setVisibility(View.GONE);
                } else {
                    listViewAtividades.setVisibility(View.VISIBLE);
                    no_recorde.setVisibility(View.GONE);
                    String jsonArrayString = pref.getString(KEY_LISTA, "");
                    try {
                        JSONArray jsonArray = new JSONArray(jsonArrayString);
                        carregaLista(jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }


    public void carregaLista(JSONArray jsonArray) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonAtividade = jsonArray.getJSONObject(i);
            String nome = jsonAtividade.getString("nomeAtividade");
            try {
                Atividade atividade = new Atividade(nome);
                listaAtividades.add(atividade);
                adapter = new ActivityAdapter(this, listaAtividades);
                listViewAtividades.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void setView(Context context, Class classe) {
        Intent it = new Intent();
        it.setClass(context, classe);
        startActivity(it);
    }

    public void setmDrawer(ArrayList<NavItem> mNavItems) {
        mNavItems.add(new NavItem("Minha semana", R.mipmap.ic_launcher));
        mNavItems.add(new NavItem("Histórico", R.mipmap.ic_launcher));
        mNavItems.add(new NavItem("Sair", R.mipmap.ic_launcher));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter2 = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter2);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    mDrawerLayout.closeDrawer(mDrawerPane);
                    setView(MainActivity.this, AcompanhamentoActivity.class);
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
}
