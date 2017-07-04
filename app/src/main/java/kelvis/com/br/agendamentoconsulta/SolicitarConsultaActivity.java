package kelvis.com.br.agendamentoconsulta;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.Calendar;

import cz.msebera.android.httpclient.Header;
import kelvis.com.br.agendamentoconsulta.async.AsyncUsuarioHttpClient;
import kelvis.com.br.agendamentoconsulta.entidades.Consulta;
import kelvis.com.br.agendamentoconsulta.entidades.Paciente;
import kelvis.com.br.agendamentoconsulta.fragment.DatePickerFragment;
import kelvis.com.br.agendamentoconsulta.util.Constantes;


public class SolicitarConsultaActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtNasc, edtHora;
    private Button btnSolicitar;
    private Context contexto;
    private boolean retorno = false;
    private int hora, minutos;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitar_consulta);
        edtNasc = (EditText) findViewById(R.id.edtNasc);
        edtHora = (EditText) findViewById(R.id.edtHora);
        btnSolicitar = (Button) findViewById(R.id.btnSolicitar);
        btnSolicitar.setOnClickListener(this);
        contexto = getApplicationContext();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    public void setHora(View view) {
        final Calendar c = Calendar.getInstance();
        hora = c.get(Calendar.HOUR_OF_DAY);
        minutos = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                edtHora.setText(hourOfDay + ":" + minute);
            }
        }, hora, minutos, false);
        timePickerDialog.show();
    }

    public void setData(View view) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();

        Calendar cal = Calendar.getInstance();

        Bundle bundle = new Bundle();
        bundle.putInt("dia", cal.get(Calendar.DAY_OF_MONTH));
        bundle.putInt("mes", cal.get(Calendar.MONTH));
        bundle.putInt("ano", cal.get(Calendar.YEAR));

        datePickerFragment.setArguments(bundle);
        datePickerFragment.setDateListener(dateListener);
        datePickerFragment.show(getFragmentManager(), "Data Consulta");
    }

    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            edtNasc.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSolicitar:
                this.view = view;
                cadastrarConsulta();

                break;
            default:
        }
    }

    public void cadastrarConsulta() {
        SharedPreferences prefs = getSharedPreferences(Constantes.PREFS, 0);
        String idPaciente = prefs.getString("id", "naoencontrada");
        Log.d("PACIENTE", idPaciente);

        RequestParams params = new RequestParams();
        params.put("data_consulta", edtNasc.getText().toString());
        params.put("hora_consulta", edtHora.getText().toString());
        params.put("paciente_id", idPaciente);

        AsyncUsuarioHttpClient.post("consultas/addConsultaPost", params, new TextHttpResponseHandler() {
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
                    Snackbar.make(view, "Agendamento não realizado", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    // Toast.makeText(contexto, "Usuário / Senha incorretos", Toast.LENGTH_SHORT).show();
                    retorno = false;
                } else {
                    Snackbar.make(view, "Agendamento realizado com sucesso!", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();

                    edtNasc.setText(null);
                    edtHora.setText(null);

                    //Toast.makeText(contexto, "Paciente é não é null", Toast.LENGTH_SHORT).show();

                    Log.d("PACIENTE ", consulta.toString());

                   /* if (Integer.parseInt(paciente.getSituacao()) == 0) {
                        startActivity(new Intent(SolicitarConsultaActivity.this, AlterarSenhaActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(SolicitarConsultaActivity.this, MainActivity.class));
                        finish();
                    }*/


                }
            }
        });
    }
}
