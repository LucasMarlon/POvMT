package povmt.projeto.les.povmt.projetopiloto.models;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MySharedPreferences {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context mycontext;
    int PRIVATE_MODE = 0;

    List<Atividade> listaAtividades;
    private static final String PREFER_NAME = "Pref";
    public static final String KEY_LISTA_ATIVIDADES = "lista_atividades";
    public static final String KEY_CHECKBOX_VALUE = "checkbox_value";


    public MySharedPreferences(Context context) {
        this.mycontext = context;
        pref = mycontext.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();

    }

    public void salvaListaAtividades(String listaAtividades) {
        editor.putString(KEY_LISTA_ATIVIDADES, listaAtividades);
        editor.commit();
    }

    public void guardaCheckboxValue(boolean value){
        editor.putBoolean(KEY_CHECKBOX_VALUE, value);
        editor.commit();
    }

    public boolean recuperaCheckboxValue(){
        boolean value = pref.getBoolean(KEY_CHECKBOX_VALUE, true);

        return value;
    }

    public List<Atividade> getListAtividades() {
        String jsonArrayString = pref.getString(KEY_LISTA_ATIVIDADES, "");
        try {
            JSONArray jsonArray = new JSONArray(jsonArrayString);
            carregaLista(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listaAtividades;
    }


    public void carregaLista(JSONArray jsonArray) throws JSONException {
        listaAtividades = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonAtividade = jsonArray.getJSONObject(i);
            String nome = jsonAtividade.getString("nomeAtividade");
            int tempoInvestido = jsonAtividade.getInt("tempoInvestido");
            String data = jsonAtividade.getString("dataAtividade");
            String prioridade_value = jsonAtividade.getString("prioridade");
            String categoria_value = jsonAtividade.getString("categoria");
            String fotoString = jsonAtividade.getString("foto");

            PRIORIDADE prioridade = PRIORIDADE.getEnum(prioridade_value);
            CATEGORIA categoria = CATEGORIA.getEnum(categoria_value);

            byte[] fotoByte = Base64.decode(fotoString, Base64.DEFAULT);

            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date dataAtividade = new Date();
            try {
                dataAtividade = format.parse(data);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                Atividade atividade = new Atividade(nome, tempoInvestido, dataAtividade, prioridade, categoria, fotoByte);
                listaAtividades.add(atividade);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}