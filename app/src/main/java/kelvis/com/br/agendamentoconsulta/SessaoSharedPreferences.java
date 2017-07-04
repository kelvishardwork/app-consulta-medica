package kelvis.com.br.agendamentoconsulta;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Usuario on 02/07/2017.
 */

public class SessaoSharedPreferences {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;

    public SessaoSharedPreferences(Context contexto) {
        this.ctx = contexto;
        prefs = ctx.getSharedPreferences("sharedDoMeuApp", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setLogar(boolean logado) {
        editor.putBoolean("modoLogado", logado);
        editor.commit();
    }

    public boolean isLogado() {
        return prefs.getBoolean("modoLogado", false);
    }

}
