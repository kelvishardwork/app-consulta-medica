package kelvis.com.br.agendamentoconsulta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import kelvis.com.br.agendamentoconsulta.async.AsyncUsuarioHttpClient;
import kelvis.com.br.agendamentoconsulta.entidades.Paciente;
import kelvis.com.br.agendamentoconsulta.util.Constantes;


public class AlterarSenhaActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etNovaSenha;
    private Button btnAlterarSenha;
    private Context contexto;
    public Paciente paciente;
    private boolean retorno = false;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_senha);
        etNovaSenha = (EditText) findViewById(R.id.etNovaSenha);
        btnAlterarSenha = (Button) findViewById(R.id.btnAlterarSenha);
        btnAlterarSenha.setOnClickListener(this);
        contexto = getApplicationContext();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAlterarSenha:
                atualizarSenha();
                break;
            default:
        }
    }

    public void atualizarSenha() {

        RequestParams params = new RequestParams();

        SharedPreferences prefs = getSharedPreferences(Constantes.PREFS, 0);
        String idPaciente = prefs.getString("id", "naoencontrada");

        params.put("id", idPaciente);
        params.put("senha", etNovaSenha.getText().toString());

        AsyncUsuarioHttpClient.get("pacientes/alterarSenhaGet", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("RESPONSE", responseString.toString());
                // Toast.makeText(contexto, "Paciente" + responseString.toString(), Toast.LENGTH_SHORT).show();
                paciente = new Paciente();
                paciente = new Gson().fromJson(responseString, Paciente.class);

                if (paciente == null) {
                    Snackbar.make(view, "Senha incorretos", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    // Toast.makeText(contexto, "Usuário / Senha incorretos", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(contexto, "Bem vindo  " + paciente.getNome() + " !!!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AlterarSenhaActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
    }
}
