/**<p>*********************************************************************************************************************
 * <h1>BuildSqlDelete</h1>
 * @since 20230329
 * =====================================================================================================================
 * DATE      VSN/MOD               BY....
 * =====================================================================================================================
 * 20230329  @version 01           @author ORIGINAL AUTHOR
 * 20230329  TODO: verify the use of transaction
 * =====================================================================================================================
 * INFO, ERRORS AND WARNINGS:
 * EQ01, EQ02, EQ03, EQ04
 **********************************************************************************************************************</p>*/

package com.badlogic.gdx.sql.builder.android;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.sql.SQLiteGdxException;
import com.badlogic.gdx.sql.SqliteDataTypes;
import com.badlogic.gdx.sql.builder.Column;
import com.badlogic.gdx.sql.builder.SqlBuilderDelete;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.OptionalInt;

public class BuildSqlDelete extends SqlBuilderDelete {
    private static final String TAG = BuildSqlDelete.class.getCanonicalName();
    public static final String NAME = TAG;
    private SQLiteDatabase db;
    private String androidSql;

    private final String EQ01 = "Unknown Sqlite DataType, use SqliteDataTypes";
    private final String EQ02 = "Table is undefined";
    private final String EQ03 = "Operating System not Supported";
    private final String EQ04 = "Database is not an instance of SQLiteDatabase";

    /** Runs on Windows */
    @Override
    public OptionalInt delete(Connection connection) throws SQLiteGdxException, SQLException {
        throw new SQLiteGdxException(EQ03);
    }
    @Override
    public OptionalInt delete(Object androidDatabase) throws SQLiteGdxException {
        if (table == null) throw new SQLiteGdxException(EQ02);
        if(!(androidDatabase instanceof SQLiteDatabase)) throw new SQLiteGdxException(EQ04);
        db  = (SQLiteDatabase) androidDatabase;
        androidSql = createStatement();
        db.beginTransaction();
        int index = 1;
        SQLiteStatement aStatement = db.compileStatement(androidSql);
        for (Map.Entry<Column, Object> p : clauses.entrySet()) {
            if (p!= null) {
                aStatement.clearBindings();
                /* Based on SqliteDataTypes */
                switch (p.getKey().getType()) {
                    case SqliteDataTypes.BLOB:
                        aStatement.bindBlob(index++, (byte[]) p.getValue());
                        break;
                    case SqliteDataTypes.DOUBLE:
                        aStatement.bindDouble(index++, Double.parseDouble(String.valueOf(p.getValue())));
                        break;
                    case SqliteDataTypes.LONG:
                        aStatement.bindLong(index++,Long.parseLong(String.valueOf(p.getValue())));
                        break;
                    case SqliteDataTypes.STRING:
                        aStatement.bindString(index++, String.valueOf(p.getValue()));
                        break;
                    case SqliteDataTypes.NULL:
                        aStatement.bindNull(index++);
                        break;
                    default:
                        Gdx.app.error(TAG, EQ01);
                        break;
                }
            }
        }
        OptionalInt optionalInt = OptionalInt.of(aStatement.executeUpdateDelete());
        db.setTransactionSuccessful();
        db.endTransaction();
        return optionalInt;
    }
}
