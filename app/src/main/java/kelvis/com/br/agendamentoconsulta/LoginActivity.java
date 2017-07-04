package kelvis.com.br.agendamentoconsulta;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import kelvis.com.br.agendamentoconsulta.async.AsyncUsuarioHttpClient;
import kelvis.com.br.agendamentoconsulta.entidades.Paciente;
import kelvis.com.br.agendamentoconsulta.util.Constantes;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etLogin, etSenha;
    private Button btnLogar;
    private Context contexto;
    public Paciente paciente;
    private boolean retorno = false;
    View view;
    private ProgressBar mLoadingIndicator;
    //final RelativeLayout lytLoading = (RelativeLayout) view.findViewById(R.id.lytLoading);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        SharedPreferences prefs = getSharedPreferences(Constantes.PREFS, 0);
        boolean estaLogado = prefs.getBoolean("modoLogado", false);

        if (estaLogado) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }


        etLogin = (EditText) findViewById(R.id.etLogin);
        etSenha = (EditText) findViewById(R.id.etSenha);
        btnLogar = (Button) findViewById(R.id.btnLogar);
        btnLogar.setOnClickListener(this);
        contexto = getApplicationContext();

       // mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogar:

               // lytLoading.setVisibility(View.VISIBLE);

                this.view = view;
                logar();

                break;
            default:
        }
    }

    public void logar() {
        verificarLogin();

       /* startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();*/
    }

    public boolean verificarLogin() {
        //mLoadingIndicator.setVisibility(View.VISIBLE);
        RequestParams params = new RequestParams();
        params.put("login", etLogin.getText().toString());
        params.put("senha", etSenha.getText().toString());

        AsyncUsuarioHttpClient.get("pacientes/loginGet", params, new TextHttpResponseHandler() {
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
                    Snackbar.make(view, "Usuário / Senha incorretos", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    // Toast.makeText(contexto, "Usuário / Senha incorretos", Toast.LENGTH_SHORT).show();
                    retorno = false;
                } else {
                    Snackbar.make(view, "Bem vindo" + paciente.getNome() + " !!!", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    //Toast.makeText(contexto, "Paciente é não é null", Toast.LENGTH_SHORT).show();
                    SharedPreferences prefs = getSharedPreferences(Constantes.PREFS, 0);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("id", paciente.getId());
                    editor.putString("nome", paciente.getNome());
                    editor.putString("login", paciente.getLogin());
                    editor.putString("senha", paciente.getSenha());
                    editor.putBoolean("modoLogado", true);
                    editor.commit();

                    Log.d("PACIENTE ", paciente.getSituacao());

                    if (Integer.parseInt(paciente.getSituacao()) == 0) {
                      //  mLoadingIndicator.setVisibility(View.INVISIBLE);
                    //    lytLoading.setVisibility(View.GONE);
                        startActivity(new Intent(LoginActivity.this, AlterarSenhaActivity.class));
                        finish();
                    } else {
                     //   mLoadingIndicator.setVisibility(View.INVISIBLE);
                      //  lytLoading.setVisibility(View.GONE);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }


                    retorno = true;
                }
            }
        });
        return retorno;
    }
}
