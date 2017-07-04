package kelvis.com.br.agendamentoconsulta;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import kelvis.com.br.agendamentoconsulta.adapter.ConsultasArrayAdapter;
import kelvis.com.br.agendamentoconsulta.async.AsyncUsuarioHttpClient;
import kelvis.com.br.agendamentoconsulta.entidades.Consulta;
import kelvis.com.br.agendamentoconsulta.entidades.Paciente;
import kelvis.com.br.agendamentoconsulta.repository.ConsultaRepository;
import kelvis.com.br.agendamentoconsulta.util.Constantes;
import kelvis.com.br.agendamentoconsulta.util.Util;

public class MainActivity extends AppCompatActivity {

    TextView tvMsg;

    public List<Consulta> consultas;
    public List<String> lsConsulta;
    public ListView lvResultado;
    public Context contexto;
    private ConsultaRepository consultaRepository;
    private int posicaoSelecionada;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contexto = getApplicationContext();
        lvResultado = (ListView) findViewById(R.id.lvResultado);
        consultaRepository = new ConsultaRepository(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                startActivity(new Intent(MainActivity.this, SolicitarConsultaActivity.class));
            }
        });


        buscarConsultas();
        lvResultado.setOnItemClickListener(clickListenerPessoas);
        lvResultado.setOnCreateContextMenuListener(contextMenuListener);
        lvResultado.setOnItemLongClickListener(longClickListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_atualizar) {
            buscarConsultas();
        }

        if (id == R.id.action_deslogar) {
            SharedPreferences prefs = getSharedPreferences(Constantes.PREFS, 0);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("modoLogado", false);
            editor.commit();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void buscarConsultas() {
        RequestParams params = new RequestParams();

        SharedPreferences prefs = getSharedPreferences(Constantes.PREFS, 0);
        String idPaciente = prefs.getString("id", "naoencontrada");
        Log.d("PACIENTE", idPaciente);


        //Integer pacienteId = 1;
        params.put("paciente_id", idPaciente);

        AsyncUsuarioHttpClient.get("consultas/listarConsultaPorId", params, new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("response", response.toString());
                // Toast.makeText(contexto, response.toString(), Toast.LENGTH_SHORT).show();
                consultas = new ArrayList<Consulta>();
                if (response != null) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            consultas.add(new Gson().fromJson(jsonObject.toString(), Consulta.class));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    consultaRepository.limparTabela();
                    for (Consulta con : consultas) {
                        consultaRepository.salvarConsulta(con);
                    }
                }
                if (response.length() == 0) {
                    Toast.makeText(contexto, "Você ainda não possuem consultas. Por favor agende uma! ", Toast.LENGTH_SHORT).show();
                }


               // Toast.makeText(contexto, "Consultas " + consultaRepository.listarConsultas(), Toast.LENGTH_LONG).show();
                setArrayAdapterPessoas();

                //ConsultasArrayAdapter arrayAdapter = new ConsultasArrayAdapter(contexto, R.layout.lista_consultas, consultas);

 /*               ConsultasArrayAdapter arrayAdapter = new ConsultasArrayAdapter(contexto, R.layout.lista_consultas, consultaRepository.listarConsultas());
                lvResultado.setAdapter(arrayAdapter);
                lvResultado.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView arg0, View arg1, int posicao, long arg3) {
                        final int posicaoSelecionada = posicao; //.getItemAtPosition(arg2).toString();
                        final CharSequence[] dialogitem = {"Detalhes", "Solicitar Cancelamento"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Ações");
                        builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                switch (item) {
                                    case 0:

                                        break;
                                    case 1:
                                        Toast.makeText(contexto, String.valueOf(posicaoSelecionada), Toast.LENGTH_SHORT).show();
                                    case 2:
                                }
                            }
                        });
                        builder.create().show();
                    }
                });*/
            }
        });
    }


    private void setArrayAdapterPessoas() {
        ConsultasArrayAdapter arrayAdapter = new ConsultasArrayAdapter(contexto, R.layout.lista_consultas, consultaRepository.listarConsultas());
        arrayAdapter.clear();
        lvResultado.setAdapter(arrayAdapter);
    }

    private AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            posicaoSelecionada = position;
            return false;
        }
    };

    private View.OnCreateContextMenuListener contextMenuListener = new View.OnCreateContextMenuListener() {
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            //menu.setHeaderTitle("Opções").setHeaderIcon(R.drawable.edit).add(1, 10, 1, "Editar");
            menu.setHeaderTitle("Opções").add(1, 10, 1, "Detalhes da Consulta");
            menu.add(1, 20, 2, "Solicitar Cancelamento");
        }
    };

    private AdapterView.OnItemClickListener clickListenerPessoas = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            view = view;
            Consulta consulta = consultaRepository.consultarConsultaPorId(consultas.get(position).getId());

            //DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            StringBuilder info = new StringBuilder();
            info.append("Consulta: " + consulta.getId());
            info.append("\nData da Consulta: " + consulta.getData_consulta());
            info.append("\nHorário: " + consulta.getHora_consulta());

            Util.showMsgAlertOK(MainActivity.this, "Detalhes", info.toString());
        }
    };

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 10:
                final Consulta consulta = consultaRepository.consultarConsultaPorId(consultas.get(posicaoSelecionada).getId());
                StringBuilder info = new StringBuilder();
                info.append("Consulta: " + consulta.getId());
                info.append("\nData da Consulta: " + consulta.getData_consulta());
                info.append("\nHorário: " + consulta.getHora_consulta());

                Util.showMsgAlertOK(MainActivity.this, "Consulta", info.toString());
/*                Intent i = new Intent(this, EditarPessoaActivity.class);
                i.putExtra("pessoa", pessoa);
                startActivity(i);
                finish();*/
                break;
            case 20:
                Util.showMsgConfirm(MainActivity.this, "Solicitação de Cancelamento", "Deseja realmente solicitar o cancelamento dessa consulta?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int id = consultas.get(posicaoSelecionada).getId();
                        solicitarCancelamento(id);



                       /* repository.removerPessoaPorId(id);
                        setArrayAdapterPessoas();
                        adapter.notifyDataSetChanged();*/


                    }
                });
                break;
        }
        return true;
    }

    public void solicitarCancelamento(int idConsulta) {

        RequestParams params = new RequestParams();
        params.put("id", idConsulta);

        AsyncUsuarioHttpClient.get("consultas/cancelarConsulta", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("RESPONSE", responseString.toString());
                // Toast.makeText(contexto, "Paciente" + responseString.toString(), Toast.LENGTH_SHORT).show();
                Consulta consulta = new Consulta();
                consulta = new Gson().fromJson(responseString, Consulta.class);

                if (consulta == null) {
                    Toast.makeText(contexto, "Não foi possível realizar o cancelamento", Toast.LENGTH_SHORT).show();
                    // Toast.makeText(contexto, "Usuário / Senha incorretos", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(contexto, "Solicitação de consulta " + consulta.getId() + " realizada com sucesso", Toast.LENGTH_SHORT).show();
                }
                consulta = null;
                buscarConsultas();
            }
        });
    }
}
