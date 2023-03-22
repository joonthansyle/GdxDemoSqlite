package com.galaxy.red.hat.GdxDemoSqlite;

import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class AppMain extends Game {
    @Override
    public void create() {
        setScreen(new AppScreen());
    }
}
