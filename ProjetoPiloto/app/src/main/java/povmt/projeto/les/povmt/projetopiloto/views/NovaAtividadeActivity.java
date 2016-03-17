package povmt.projeto.les.povmt.projetopiloto.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import povmt.projeto.les.povmt.projetopiloto.R;
import povmt.projeto.les.povmt.projetopiloto.models.PRIORIDADE;
import povmt.projeto.les.povmt.projetopiloto.utils.HttpListener;
import povmt.projeto.les.povmt.projetopiloto.utils.HttpUtils;

public class NovaAtividadeActivity extends ActionBarActivity implements View.OnClickListener {

    private HttpUtils mHttp;
    private Calendar cal = Calendar.getInstance();
    Date data;
    private ImageView fotoAtividadeImageView;
    private ImageButton btnCamera;
    private ImageButton btnGaleria;
    private ImageButton btnLixeira;
    private static final int RESULT_CAMERA = 111;
    private static final int RESULT_GALERIA = 222;
    private Bitmap foto;
    Spinner spinnerPrioridade;
    List<String> prioridades;
    private String selectedPrioridade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_atividade);

        Bitmap avatar = BitmapFactory.decodeResource(getResources(), R.drawable.atividade);
        foto = avatar;
        fotoAtividadeImageView = (ImageView) findViewById(R.id.fotoAtividade);
        btnCamera = (ImageButton) findViewById(R.id.imgCamera);
        btnCamera.setOnClickListener(this);
        btnGaleria = (ImageButton) findViewById(R.id.imgGaleria);
        btnGaleria.setOnClickListener(this);
        btnLixeira = (ImageButton) findViewById(R.id.imgLixeira);
        btnLixeira.setOnClickListener(this);
        spinnerPrioridade = (Spinner) findViewById(R.id.sp_prioridade);
        mHttp = new HttpUtils(this);
        prioridades = new ArrayList<>();

        final EditText nomeAtividade = (EditText) findViewById(R.id.et_nome);
        final Button registraAtividade =  (Button) findViewById(R.id.bt_add_atividade);

        putPriorityElementsOnSpinnerArray(prioridades);

        ArrayAdapter<String> spinnerArrayAdapterPrioridades = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, prioridades);
        spinnerArrayAdapterPrioridades.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinnerPrioridade.setAdapter(spinnerArrayAdapterPrioridades);

        spinnerPrioridade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(final AdapterView<?> parent, final View view, final int pos, final long id) {
                Object item = parent.getItemAtPosition(pos);
                String itemPrioridade = item.toString().toUpperCase(Locale.getDefault());

                if (itemPrioridade.equals(PRIORIDADE.BAIXA.getValor().toUpperCase(Locale.getDefault()))) {
                    selectedPrioridade = PRIORIDADE.BAIXA.getValor();
                } else if (itemPrioridade.equals(PRIORIDADE.MEDIA.getValor().toUpperCase(Locale.getDefault()))) {
                    selectedPrioridade = PRIORIDADE.MEDIA.getValor();
                } else if (itemPrioridade.equals(PRIORIDADE.ALTA.getValor().toUpperCase(Locale.getDefault()))) {
                    selectedPrioridade = PRIORIDADE.ALTA.getValor();
                }
            }

            public void onNothingSelected(final AdapterView<?> parent) {
            }
        });

        registraAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
                cal.clear(Calendar.MINUTE);
                cal.clear(Calendar.SECOND);
                cal.clear(Calendar.MILLISECOND);
                cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                data = new Date();

                Date dateSem = cal.getTime();
                String nome = nomeAtividade.getText().toString();
                SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
                String dataSemana = format1.format(dateSem);
                String dataAtual = format1.format(data.getTime());
                String prioridade = selectedPrioridade;

                ByteArrayOutputStream b = new ByteArrayOutputStream();
                foto.compress(Bitmap.CompressFormat.JPEG, 100, b);
                byte[] fotoByte = b.toByteArray();
                //Convertendo a foto do formato byte[] para String
                String fotoString = Base64.encodeToString(fotoByte, Base64.NO_WRAP);
                //Convertendo a foto do formato String para byte[]
                //byte[] data = Base64.decode(imgString, Base64.DEFAULT);
                registraAtividade(nome, dataSemana, dataAtual, fotoString, prioridade);
            }
        });


    }

    private void registraAtividade(final String nome, final String dataSemana, final String dataAtual, final String foto, final String prioridade) {
        if (nome.equals("")) {
            new AlertDialog.Builder(NovaAtividadeActivity.this)
                    .setTitle("Erro")
                    .setMessage("O nome da atividade não pode ser vazio.")
                    .setNeutralButton("OK", null)
                    .create()
                    .show();
        } else {
            String url = "http://povmt-armq.rhcloud.com/cadastrarAtividade";
            JSONObject json = new JSONObject();
            try {
               // json.put("usuario", "danielachenrique@gmail.com");
                json.put("nomeAtividade", nome);
                json.put("dataInicioSemana", dataSemana);
                json.put("dataFimSemana", "");
                json.put("prioridade", prioridade);
                json.put("foto", foto);
                json.put("categoria", "");
                json.put("dataAtividade", dataAtual);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mHttp.post(url, json.toString(), new HttpListener() {
                @Override
                public void onSucess(JSONObject result) throws JSONException{
                    if (result.getInt("ok") == 0) {
                        new AlertDialog.Builder(NovaAtividadeActivity.this)
                                .setTitle("Erro")
                                .setMessage(result.getString("msg"))
                                .setNeutralButton("OK", null)
                                .create()
                                .show();
                    } else {
                        new AlertDialog.Builder(NovaAtividadeActivity.this)
                                .setTitle("Sucesso")
                                .setMessage("Atividade registrada com sucesso")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                        finish();
                                        setView(NovaAtividadeActivity.this, MainActivity.class);
                                    }
                                })
                                .create()
                                .show();

                    }
                }

            @Override
            public void onTimeout() {
                new AlertDialog.Builder(NovaAtividadeActivity.this)
                    .setTitle("Erro")
                    .setMessage("Conexão não disponível")
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

    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {
            case R.id.imgCamera:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, RESULT_CAMERA);
                break;
            case R.id.imgGaleria:
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_GALERIA);
                break;
            default:
                Bitmap avatar = BitmapFactory.decodeResource(getResources(), R.drawable.atividade);
                fotoAtividadeImageView.setImageBitmap(avatar);
                fotoAtividadeImageView.setImageBitmap(Bitmap.createScaledBitmap(avatar, 100, 100, false));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_CAMERA && resultCode == RESULT_OK) {
            foto = (Bitmap)data.getExtras().get("data");
            fotoAtividadeImageView.setImageBitmap(foto);
            fotoAtividadeImageView.setImageBitmap(Bitmap.createScaledBitmap(foto, 100, 100, false));
        } else if (requestCode == RESULT_GALERIA && resultCode == RESULT_OK) {
            //Uri (local da tabela do banco de dados) do dado (no caso, da imagem)
            Uri imageUri = data.getData();
            //Se tratantando de banco de dados, devemos selecionar exatamente qual coluna queremos
            String[] colunaArquivo = {MediaStore.Images.Media.DATA};
            //Fazemos um select simples na tabela trazendo apenas a coluna que selecionamos
            Cursor cursor = getContentResolver().query(imageUri, colunaArquivo, null, null, null);
            //Movemos nosso cursor para o primeiro resultado do select
            cursor.moveToFirst();
            //Recuperamos o indice de qual coluna da tabela estamos referenciando
            int columnIndex = cursor.getColumnIndex(colunaArquivo[0]);
            //O campo da tabela guarda o caminho da imagem. Recuperamos tal caminho
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            //Pegamos o arquivo do caminho que recuperamos e decodificamos para imagem
            foto = BitmapFactory.decodeFile(picturePath.toString());
            //Se o arquivo nao estiver nulo (e for uma imagem e nao um video por exemplo)
            if (foto != null) {
                fotoAtividadeImageView.setImageBitmap(foto);
                fotoAtividadeImageView.setImageBitmap(Bitmap.createScaledBitmap(foto, 100, 100, false));
            }
        }
    }

    private void putPriorityElementsOnSpinnerArray(List<String> prioridades) {
        prioridades.add(PRIORIDADE.BAIXA.getValor());
        prioridades.add(PRIORIDADE.MEDIA.getValor());
        prioridades.add(PRIORIDADE.ALTA.getValor());
    }

}