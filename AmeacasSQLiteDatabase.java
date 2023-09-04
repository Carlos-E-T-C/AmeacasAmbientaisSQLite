// Classe para gerenciar o banco de dados SQLite das ameaças ambientais
package br.feevale.ameacasambientaissqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AmeacasSQLiteDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ameacas_db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "ameacas";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ENDERECO = "endereco";
    private static final String COLUMN_DATA = "data";
    private static final String COLUMN_DESCRICAO = "descricao";

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_ENDERECO + " TEXT," +
            COLUMN_DATA + " TEXT," +
            COLUMN_DESCRICAO + " TEXT);";

    // Construtor
    public AmeacasSQLiteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Métodos de criação e atualização do banco de dados
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            // Crie a tabela se ela não existir
            db.execSQL(CREATE_TABLE);
            Log.d("SQLite", "Tabela criada com sucesso: " + TABLE_NAME);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("SQLite", "Erro ao criar tabela: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Atualize a tabela em caso de mudança de versão do banco de dados
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Métodos para adicionar, obter, atualizar e excluir ameaças do banco de dados
    public long addAmeaca(AmeacaAmbiental ameaca) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ENDERECO, ameaca.getEndereco());
        values.put(COLUMN_DATA, ameaca.getData());
        values.put(COLUMN_DESCRICAO, ameaca.getDescricao());

        // Insira a nova ameaça e obtenha o ID da nova linha inserida
        long newRowId = db.insert(TABLE_NAME, null, values);
        db.close();

        return newRowId; // Retorna o ID da nova ameaça
    }

    public List<AmeacaAmbiental> getAllAmeacas() {
        List<AmeacaAmbiental> ameacasList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        try (SQLiteDatabase db = this.getReadableDatabase(); Cursor cursor = db.rawQuery(selectQuery, null)) {

            int columnIndexId = cursor.getColumnIndex(COLUMN_ID);
            int columnIndexEndereco = cursor.getColumnIndex(COLUMN_ENDERECO);
            int columnIndexData = cursor.getColumnIndex(COLUMN_DATA);
            int columnIndexDescricao = cursor.getColumnIndex(COLUMN_DESCRICAO);

            if (cursor.moveToFirst()) {
                do {
                    AmeacaAmbiental ameaca = new AmeacaAmbiental();

                    // Preencha o objeto "ameaca" com os dados do cursor
                    if (columnIndexId >= 0) {
                        ameaca.setId(cursor.getInt(columnIndexId));
                    }

                    if (columnIndexEndereco >= 0) {
                        ameaca.setEndereco(cursor.getString(columnIndexEndereco));
                    }

                    if (columnIndexData >= 0) {
                        ameaca.setData(cursor.getString(columnIndexData));
                    }

                    if (columnIndexDescricao >= 0) {
                        ameaca.setDescricao(cursor.getString(columnIndexDescricao));
                    }

                    ameacasList.add(ameaca);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SQLite", "Erro ao buscar ameaças: " + e.getMessage());
        }

        return ameacasList;
    }

    public void updateAmeaca(AmeacaAmbiental ameaca) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ENDERECO, ameaca.getEndereco());
        values.put(COLUMN_DATA, ameaca.getData());
        values.put(COLUMN_DESCRICAO, ameaca.getDescricao());

        // Atualize a ameaça no banco de dados com base no ID
        db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(ameaca.getId())});
        db.close();
    }

    public void deleteAmeaca(long ameacaId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Exclua a ameaça do banco de dados com base no ID
        db.delete(TABLE_NAME, COLUMN_ID + " = ?",
                new String[]{String.valueOf(ameacaId)});
        db.close();
    }
}
