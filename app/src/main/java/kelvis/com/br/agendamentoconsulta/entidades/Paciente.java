package kelvis.com.br.agendamentoconsulta.entidades;

/**
 * Created by Usuario on 02/07/2017.
 */

public class Paciente {
    private String id;
    private String login;
    private String senha;
    private String situacao;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    private String nome;

    @Override
    public String toString() {
        return "Paciente{" +
                "id='" + id + '\'' +
                ", login='" + login + '\'' +
                ", senha='" + senha + '\'' +
                ", situacao='" + situacao + '\'' +
                ", nome='" + nome + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
}
