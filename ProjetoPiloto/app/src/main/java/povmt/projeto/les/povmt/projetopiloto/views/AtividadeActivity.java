package povmt.projeto.les.povmt.projetopiloto.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import povmt.projeto.les.povmt.projetopiloto.R;
import povmt.projeto.les.povmt.projetopiloto.models.Atividade;
import povmt.projeto.les.povmt.projetopiloto.models.CATEGORIA;
import povmt.projeto.les.povmt.projetopiloto.utils.HttpListener;
import povmt.projeto.les.povmt.projetopiloto.utils.HttpUtils;

public class AtividadeActivity extends ActionBarActivity {

    private HttpUtils mHttp;
    private Calendar cal = Calendar.getInstance();
    private Atividade atividade;
    private TextView tv_nome_atividade;
    private ImageView iv_foto_atividade;
    Spinner spinnerCategoria;
    List<String> categorias;
    private String selectedCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atividade);

        mHttp = new HttpUtils(this);
        Button btAddTime = (Button) findViewById(R.id.bt_add_time);
        final EditText etTime = (EditText) findViewById(R.id.et_time);

        Intent it = getIntent();
        atividade = (Atividade) it.getSerializableExtra("ATIVIDADE");

        byte [] fotoByte = atividade.getFoto();
        Bitmap fotoBitmap = BitmapFactory.decodeByteArray(fotoByte, 0, fotoByte.length);

        iv_foto_atividade = (ImageView) findViewById(R.id.iv_foto_atividade);
        iv_foto_atividade.setImageBitmap(fotoBitmap);
        iv_foto_atividade.setImageBitmap(Bitmap.createScaledBitmap(fotoBitmap, 50, 50, false));

        tv_nome_atividade = (TextView) findViewById(R.id.tv_activity_name);
        tv_nome_atividade.setText(atividade.getNome());

        btAddTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempoInvestido = etTime.getText().toString();
                registrarTI(Integer.parseInt(tempoInvestido));
            }
        });

        spinnerCategoria = (Spinner) findViewById(R.id.sp_categoria);
        categorias = new ArrayList<>();
        final Button atribuiCategoria =  (Button) findViewById(R.id.bt_add_categoria);

        putCategoryElementsOnSpinnerArray(categorias);

        ArrayAdapter<String> spinnerArrayAdapterCategorias = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categorias);
        spinnerArrayAdapterCategorias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinnerCategoria.setAdapter(spinnerArrayAdapterCategorias);

        spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(final AdapterView<?> parent, final View view, final int pos, final long id) {
                Object item = parent.getItemAtPosition(pos);
                String itemCategoria = item.toString().toUpperCase(Locale.getDefault());

                if (itemCategoria.equals(CATEGORIA.TRABALHO.getValor().toUpperCase(Locale.getDefault()))) {
                    selectedCategoria = CATEGORIA.TRABALHO.getValor();
                } else if (itemCategoria.equals(CATEGORIA.LAZER.getValor().toUpperCase(Locale.getDefault()))) {
                    selectedCategoria = CATEGORIA.LAZER.getValor();
                }
            }

            public void onNothingSelected(final AdapterView<?> parent) {
            }
        });

        atribuiCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
                cal.clear(Calendar.MINUTE);
                cal.clear(Calendar.SECOND);
                cal.clear(Calendar.MILLISECOND);
                cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

                Date dateSem = cal.getTime();
                String nome = atividade.getNome();
                SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
                String dataInicioSemana = format1.format(dateSem);
                String categoria = selectedCategoria;
                atribuirCategoria(dataInicioSemana, nome, categoria);
            }
        }
        );


    }

    private void  atribuirCategoria(String dataInicioSemana, String nome, String categoria){
        String url = "http://povmt-armq.rhcloud.com/adicionarCategoria";
        JSONObject json = new JSONObject();
        try {
            json.put("usuario", LoginActivity.emailLogado);
            json.put("dataInicioSemana", dataInicioSemana);
            json.put("nomeAtividade", nome);
            json.put("categoria", categoria);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mHttp.post(url, json.toString(), new HttpListener() {
            @Override
            public void onSucess(JSONObject result) throws JSONException {
                if (result.getInt("ok") == 0) {
                    new AlertDialog.Builder(AtividadeActivity.this)
                            .setTitle("Erro")
                            .setMessage(result.getString("msg"))
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            })
                            .create()
                            .show();
                } else {
                    new AlertDialog.Builder(AtividadeActivity.this)
                            .setTitle("Sucesso")
                            .setMessage("Atribuído uma categoria")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    setView(AtividadeActivity.this, MainActivity.class);
                                }
                            })
                            .create()
                            .show();
                }
            }

            @Override
            public void onTimeout() {
                new AlertDialog.Builder(AtividadeActivity.this)
                        .setTitle("Erro")
                        .setMessage("Conexão não disponível.")
                        .setNeutralButton("OK", null)
                        .create()
                        .show();
            }
        });

    }
    private void registrarTI(int tempo) {
        String url = "http://povmt-armq.rhcloud.com/incrementaTempoInvestido";
        JSONObject json = new JSONObject();
        try {
            cal.setTime(atividade.getData());
            cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
            Date dateSem = cal.getTime();
            DateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
            String dataInicioSemana = format1.format(dateSem);

            System.out.println("DATA: " + dataInicioSemana);

            json.put("nomeAtividade", atividade.getNome());
            json.put("dataInicioSemana", dataInicioSemana);
            json.put("usuario", LoginActivity.emailLogado);
            json.put("tempoInvestido", tempo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mHttp.post(url, json.toString(), new HttpListener() {
            @Override
            public void onSucess(JSONObject result) throws JSONException {
                if (result.getInt("ok") == 0) {
                    new AlertDialog.Builder(AtividadeActivity.this)
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
                    new AlertDialog.Builder(AtividadeActivity.this)
                            .setTitle("Sucesso")
                            .setMessage("TI registrado com sucesso")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    setView(AtividadeActivity.this, MainActivity.class);
                                }
                            })
                            .create()
                            .show();
                }
            }

            @Override
            public void onTimeout() {
                new AlertDialog.Builder(AtividadeActivity.this)
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

    private void putCategoryElementsOnSpinnerArray(List<String> categorias) {
        categorias.add(CATEGORIA.TRABALHO.getValor());
        categorias.add(CATEGORIA.LAZER.getValor());
    }
}
