package kelvis.com.br.agendamentoconsulta.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import kelvis.com.br.agendamentoconsulta.entidades.Consulta;
import kelvis.com.br.agendamentoconsulta.util.Constantes;

/**
 * Created by Usuario on 03/07/2017.
 */

public class ConsultaRepository extends SQLiteOpenHelper {
    SQLiteDatabase db;

    public ConsultaRepository(Context context) {
        super(context, Constantes.BD_NOME, null, Constantes.BD_VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE IF NOT EXISTS TB_CONSULTA( ");
        // query.append(" ID INTEGER PRIMARY KEY AUTOINCREMENT,");
        query.append(" ID_CONSULTA INTEGER PRIMARY KEY, ");
        query.append(" ID_PACIENTE TEXT ,");
        query.append(" DATA_CONSULTA TEXT,");
        query.append(" HORA_CONSULTA TEXT,");
        query.append(" SITUACAO TEXT )");
        db.execSQL(query.toString());
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public void salvarConsulta(Consulta consulta) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = getContentValuesConsulta(consulta);
        db.insert("TB_CONSULTA", null, contentValues);
    }

    public void limparTabela() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("TB_CONSULTA", null, null);
        db.close();
    }

    @NonNull
    private ContentValues getContentValuesConsulta(Consulta consulta) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID_CONSULTA", consulta.getId());
        contentValues.put("ID_PACIENTE", consulta.getPaciente_id());
        contentValues.put("DATA_CONSULTA", consulta.getData_consulta());
        contentValues.put("HORA_CONSULTA", consulta.getHora_consulta());
        contentValues.put("SITUACAO", consulta.getSituacao());
        return contentValues;
    }

    public void atualizarConsulta(Consulta consulta) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = getContentValuesConsulta(consulta);
        db.update("TB_CONSULTA", contentValues, "ID_CONSULTA = ?", new String[]{String.valueOf(consulta.getId())});
    }

    public List<Consulta> listarConsultas() {
        List<Consulta> lista = new ArrayList<Consulta>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("TB_CONSULTA", null, null, null, null, null, "ID_CONSULTA");
        while (cursor.moveToNext()) {
            Consulta consulta = new Consulta();
            setConsultaFromCursor(cursor, consulta);
            lista.add(consulta);
        }
        return lista;
    }

    public Consulta consultarConsultaPorId(int idConsulta) {
        Consulta consulta = new Consulta();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("TB_CONSULTA", null, "ID_CONSULTA = ?", new String[]{String.valueOf(idConsulta)}, null, null, "ID_CONSULTA");
        if (cursor.moveToNext()) {
            setConsultaFromCursor(cursor, consulta);
        }
        return consulta;
    }

    public void removerConsultaPorId(int idConsulta) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("TB_CONSULTA", "ID_CONSULTA = ?", new String[]{String.valueOf(idConsulta)});
    }

    private void setConsultaFromCursor(Cursor cursor, Consulta consulta) {
        consulta.setId(cursor.getInt(cursor.getColumnIndex("ID_CONSULTA")));
        consulta.setPaciente_id(cursor.getString(cursor.getColumnIndex("ID_PACIENTE")));
        consulta.setData_consulta(cursor.getString(cursor.getColumnIndex("DATA_CONSULTA")));
        consulta.setHora_consulta(cursor.getString(cursor.getColumnIndex("HORA_CONSULTA")));
        consulta.setSituacao(cursor.getString(cursor.getColumnIndex("SITUACAO")));
    }


}
