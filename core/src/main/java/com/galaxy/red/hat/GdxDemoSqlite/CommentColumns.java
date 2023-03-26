package com.galaxy.red.hat.GdxDemoSqlite;

import com.badlogic.gdx.sql.builder.Column;

import java.sql.Types;

public enum CommentColumns implements Column {
    ID("_id", Types.INTEGER),
    COMMENT("comment", Types.VARCHAR);

    private final String name;
    private final int type;

    CommentColumns(final String name, final int type) {
        this.name = name;
        this.type = type;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getType() {
        return type;
    }
}
