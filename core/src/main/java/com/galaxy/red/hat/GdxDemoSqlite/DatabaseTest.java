package com.galaxy.red.hat.GdxDemoSqlite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.sql.builder.SqlBuilderSelect;
import com.badlogic.gdx.sql.builder.SqlBuilderSelectFactory;

//import com.badlogic.gdx.sql.builder.win.BuildSqlSelect;



import com.badlogic.gdx.sql.DatabaseCursor;
import com.badlogic.gdx.sql.DatabaseFactory;
import com.badlogic.gdx.sql.SQLiteGdxException;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseTest {
    Database dbHandler;
    public static final String TABLE_COMMENTS = "comments";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_COMMENT = "comment";
    private static final String DATABASE_NAME = "comments.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table if not exists "
        + TABLE_COMMENTS + "(" + COLUMN_ID
        + " integer primary key autoincrement, " + COLUMN_COMMENT
        + " text not null);";

    public DatabaseTest() {
        Gdx.app.log("DatabaseTest", "creation started");
        dbHandler = DatabaseFactory.getNewDatabase(DATABASE_NAME,
            DATABASE_VERSION, DATABASE_CREATE, null);

        dbHandler.setupDatabase();
        try {
            dbHandler.openOrCreateDatabase();
            dbHandler.execSQL(DATABASE_CREATE);
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }

        Gdx.app.log("DatabaseTest", "created successfully");
        System.out.println("DatabaseTest" + "created successfully");

        try {
            dbHandler
                .execSQL("INSERT INTO comments ('comment') VALUES ('This is a test comment')");
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }

        DatabaseCursor cursor = null;

        try {
            cursor = dbHandler.rawQuery("SELECT * FROM comments");
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        while (cursor.next()) {
            Gdx.app.log("FromDb", String.valueOf(cursor.getString(1)));
        }

        System.out.println("Using GETMANY");

//        List<Result> results = null;
//        try {
//            SelectBuilder<Result> builder = new SelectBuilder<>(
//                rs -> new Result(rs.getInt(CommentColumns.ID.getName()), rs.getString(CommentColumns.COMMENT.getName()))
//            )
//                .table("comments")
//                .select(CommentColumns.ID)
//                .select(CommentColumns.COMMENT);
//            results = dbHandler.getMany(builder);
//            for (Result r : results){
//                System.out.print("ID->"+r.index);
//                System.out.println("COMMEND->"+r.comment);
//            }
//            System.out.println(results.size());
//        } catch (SQLiteGdxException e) {
//            throw new RuntimeException(e);
//        }

//        System.out.println("Using GETCURSOR");
//
//        try {
//            SelectBuilder<Result> builder = new SelectBuilder<>(
//                rs -> new Result(rs.getInt(CommentColumns.ID.getName()), rs.getString(CommentColumns.COMMENT.getName()))
//            )
//                .table("comments")
//                .select(CommentColumns.COMMENT)
//                .where(CommentColumns.ID, 24);
//
//            cursor = dbHandler.getCursor(builder);
//        } catch (SQLiteGdxException e) {
//            e.printStackTrace();
//        }
//        while (cursor.next()) {
//            Gdx.app.log("CURSOR->", String.valueOf(cursor.getString(0)));
//        }

        System.out.println("Using ABSTRACT");



        try {
            SqlBuilderSelect<Result> builder = new SqlBuilderSelectFactory<Result>().builderSelect(
                rs -> new Result(rs.getInt(CommentColumns.ID.getName()), rs.getString(CommentColumns.COMMENT.getName()))
            )
                .table("comments")
                .select(CommentColumns.COMMENT)
                .where(CommentColumns.ID, 1);

            cursor = dbHandler.getCursor(builder);
        } catch (SQLiteGdxException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                 InstantiationException | IllegalAccessException | SQLException e) {
            e.printStackTrace();
        }
        while (cursor.next()) {
            Gdx.app.log("CURSOR->", String.valueOf(cursor.getString(0)));
        }

        try {
            SqlBuilderSelectFactory<Result> factory = new SqlBuilderSelectFactory<>();
//            factory.builderSelect(
//                    rs -> new Result(rs.getInt(CommentColumns.ID.getName()), rs.getString(CommentColumns.COMMENT.getName()))
//                                 )
//                .table("comments")
//                .select(CommentColumns.COMMENT)
//                .where(CommentColumns.ID, 50);

            cursor = dbHandler.getCursor(
                factory.builderSelect(
                        rs -> new Result(rs.getInt(CommentColumns.ID.getName()), rs.getString(CommentColumns.COMMENT.getName()))
                                     )
                    .table("comments")
                    .select(CommentColumns.COMMENT)
                    .where(CommentColumns.ID, 1)
                                        );
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
        while (cursor.next()) {
            Gdx.app.log("CURSOR->", String.valueOf(cursor.getString(0)));
        }

        try {
            dbHandler.closeDatabase();
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }

        dbHandler = null;
        Gdx.app.log("DatabaseTest", "dispose");
    }

    static class Result {

        private final int index;
        private final String comment;
        public Result(final int in, final String comment) {
            this.index = in;
            this.comment = comment;
        }
    }
}
