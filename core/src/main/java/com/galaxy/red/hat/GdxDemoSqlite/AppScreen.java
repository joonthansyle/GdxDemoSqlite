package com.galaxy.red.hat.GdxDemoSqlite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class AppScreen implements Screen {

    private static final String TAG = AppScreen.class.getCanonicalName();
    public static final String NAME = TAG;
    private Stage stage;
    private Skin skin;
    private ScreenViewport viewport;

    @Override
    public void show() {
        viewport = new ScreenViewport();
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        Table root = new Table();
        root.setFillParent(true);
        root.add(new Label("Hello World", skin));
        stage.addActor(root);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(new Color(0));
        stage.act();
        stage.draw();
        if (Gdx.input.isKeyPressed(Input.Keys.F5)) {
            dispose();
            show();
        }

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
