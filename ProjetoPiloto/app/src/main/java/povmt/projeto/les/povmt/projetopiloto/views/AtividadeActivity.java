package povmt.projeto.les.povmt.projetopiloto.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
<<<<<<< HEAD

import povmt.projeto.les.povmt.projetopiloto.R;
import povmt.projeto.les.povmt.projetopiloto.utils.HttpUtils;

public class AtividadeActivity extends ActionBarActivity {

    private HttpUtils mHttp;
    private View mLoading;

=======
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import povmt.projeto.les.povmt.projetopiloto.R;
import povmt.projeto.les.povmt.projetopiloto.adapters.DrawerListAdapter;
import povmt.projeto.les.povmt.projetopiloto.models.NavItem;

public class AtividadeActivity extends ActionBarActivity {

    private ListView mDrawerList;
    private RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ArrayList<NavItem> mNavItems;
>>>>>>> ecf657b46a5a1cde8a422925fa9325d5ce80914c

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atividade);

        mNavItems = new ArrayList<>();
        setmDrawer(mNavItems);
    }

    public void setmDrawer(ArrayList<NavItem> mNavItems){
        mNavItems.add(new NavItem("Início", R.mipmap.ic_launcher));
        mNavItems.add(new NavItem("Minha semana",  R.mipmap.ic_launcher));
        mNavItems.add(new NavItem("Histórico",  R.mipmap.ic_launcher));
        mNavItems.add(new NavItem("Sair",  R.mipmap.ic_launcher));

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
                    setView(AtividadeActivity.this, MainActivity.class);
                } else if (position == 1){
                    mDrawerLayout.closeDrawer(mDrawerPane);
                    setView(AtividadeActivity.this, AcompanhamentoActivity.class);
                }

            }
        });
    }
    public void setView(Context context, Class classe){
        Intent it = new Intent();
        it.setClass(context, classe);
        startActivity(it);
    }





}
