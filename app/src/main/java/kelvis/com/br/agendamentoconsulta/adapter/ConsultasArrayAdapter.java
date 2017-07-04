package kelvis.com.br.agendamentoconsulta.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import kelvis.com.br.agendamentoconsulta.R;
import kelvis.com.br.agendamentoconsulta.entidades.Consulta;

public class ConsultasArrayAdapter extends ArrayAdapter<Consulta> {

    private List<Consulta> consultas;
    private Context context;

    public ConsultasArrayAdapter(Context context, int resource, List<Consulta> consultas) {
        super(context, resource);
        this.context = context;
        this.consultas = consultas;
    }

    @Override
    public int getCount() {
        return consultas.size();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getViewAux(position, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getViewAux(position, parent);
    }

    @NonNull
    private View getViewAux(int position, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View linha = layoutInflater.inflate(R.layout.lista_consultas, parent, false);

        Consulta consulta = consultas.get(position);

        TextView txtIdConsulta = (TextView) linha.findViewById(R.id.txtIdConsulta);
        txtIdConsulta.append(String.valueOf(consulta.getId()));

        TextView txtSituacao = (TextView) linha.findViewById(R.id.txtSituacao);
        String situacao = null;
        switch (consulta.getSituacao()) {
            case "0":
                situacao = "Agendado";
                break;
            case "1":
                situacao = "Confirmado";
                break;
            case "2":
                situacao = "Cancelamento Pendente";
                break;
            case "3":
                situacao = "Cancelado";
                break;
        }
        txtSituacao.setText(situacao);
        return linha;
    }

    @Override
    public Consulta getItem(int position) {
        return consultas.get(position);
    }
}
