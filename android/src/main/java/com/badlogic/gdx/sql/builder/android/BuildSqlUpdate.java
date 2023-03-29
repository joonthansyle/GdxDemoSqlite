/**<p>*********************************************************************************************************************
 * <h1>BuildSqlUpdate</h1>
 * @since 20230329
 * =====================================================================================================================
 * DATE      VSN/MOD               BY....
 * =====================================================================================================================
 * 20230329  original author       evanwht1@gmail.com
 *
 * =====================================================================================================================
 * INFO, ERRORS AND WARNINGS:
 * EQ02, EQ03
 **********************************************************************************************************************</p>*/
package com.badlogic.gdx.sql.builder.android;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.badlogic.gdx.sql.SQLiteGdxException;
import com.badlogic.gdx.sql.SqliteDataTypes;
import com.badlogic.gdx.sql.builder.Column;
import com.badlogic.gdx.sql.builder.SqlBuilderUpdate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.OptionalInt;

public class BuildSqlUpdate extends SqlBuilderUpdate {
    private static final String TAG = BuildSqlUpdate.class.getCanonicalName();
    public static final String NAME = TAG;

    private SQLiteDatabase db;
    private String androidSql;


    private final String EQ01 = "Unknown Sqlite DataType, use SqliteDataTypes";
    private final String EQ02 = "Table is undefined";
    private final String EQ03 = "Operating System not Supported";
    private final String EQ04 = "Database is not an instance of SQLiteDatabase";

    /** Runs on Windows */
    @Override
    public OptionalInt update(Connection connection) throws SQLException, SQLiteGdxException {
        throw new SQLiteGdxException(EQ03);
    }

    /** Runs on Android */
    @Override
    public OptionalInt update(Object androidDatabase) throws SQLiteGdxException {
        if (table == null) throw new SQLiteGdxException(EQ02);
        if(!(androidDatabase instanceof SQLiteDatabase)) throw new SQLiteGdxException(EQ04);
        db  = (SQLiteDatabase) androidDatabase;
        androidSql = createStatement();
        int index = 1;
        SQLiteStatement aStatement = db.compileStatement(androidSql);
        aStatement.clearBindings();
        for (Map.Entry<Column, Object> p : values.entrySet()) {
            switch (p.getKey().getType()) {
                case SqliteDataTypes.BLOB:
                    aStatement.bindBlob(index++, (byte[]) p.getValue());
                    break;
                case SqliteDataTypes.DOUBLE:
                    aStatement.bindDouble(index++, Double.parseDouble(String.valueOf(p.getValue())));
                    break;
                case SqliteDataTypes.LONG:
                    aStatement.bindLong(index++, Long.parseLong(String.valueOf(p.getValue())));
                    break;
                case SqliteDataTypes.STRING:
                    aStatement.bindString(index++, String.valueOf(p.getValue()));
                    break;
                case SqliteDataTypes.NULL:
                    aStatement.bindNull(index++);
                    break;
            }
        }
        for (Map.Entry<Column, Object> p : clauses.entrySet()) {
            if (p != null) {
                switch (p.getKey().getType()) {
                    case SqliteDataTypes.BLOB:
                        aStatement.bindBlob(index++, (byte[]) p.getValue());
                        break;
                    case SqliteDataTypes.DOUBLE:
                        aStatement.bindDouble(index++, Double.parseDouble(String.valueOf(p.getValue())));
                        break;
                    case SqliteDataTypes.LONG:
                        aStatement.bindLong(index++, Long.parseLong(String.valueOf(p.getValue())));
                        break;
                    case SqliteDataTypes.STRING:
                        aStatement.bindString(index++, String.valueOf(p.getValue()));
                        break;
                    case SqliteDataTypes.NULL:
                        aStatement.bindNull(index++);
                        break;
                }
            }
        }
        final int rows = aStatement.executeUpdateDelete();
        if (rows > 0) {
            return OptionalInt.of(rows);
        }
        return OptionalInt.empty();
    }
}
