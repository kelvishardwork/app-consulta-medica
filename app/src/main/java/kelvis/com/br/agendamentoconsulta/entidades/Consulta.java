package kelvis.com.br.agendamentoconsulta.entidades;

/**
 * Created by Usuario on 30/06/2017.
 */

public class Consulta {

    private int id;
    private String paciente_id;
    private String situacao;
    private String created;
    private String modified;
    private String data_consulta;
    private String hora_consulta;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPaciente_id() {
        return paciente_id;
    }

    public void setPaciente_id(String paciente_id) {
        this.paciente_id = paciente_id;
    }

    public String getSituacao() {
        return situacao;
    }
    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getData_consulta() {
        return data_consulta;
    }

    public void setData_consulta(String data_consulta) {
        this.data_consulta = data_consulta;
    }

    public String getHora_consulta() {
        return hora_consulta;
    }

    public void setHora_consulta(String hora_consulta) {
        this.hora_consulta = hora_consulta;
    }

    @Override
    public String toString() {
        return "Consulta{" +
                "id='" + id + '\'' +
                ", paciente_id='" + paciente_id + '\'' +
                ", situacao='" + situacao + '\'' +
                ", created='" + created + '\'' +
                ", modified='" + modified + '\'' +
                ", data_consulta='" + data_consulta + '\'' +
                ", hora_consulta='" + hora_consulta + '\'' +
                '}';
    }
}
