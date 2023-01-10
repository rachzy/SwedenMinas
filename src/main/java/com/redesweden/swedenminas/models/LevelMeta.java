package com.redesweden.swedenminas.models;

import com.redesweden.swedenminas.types.Level;

import java.math.BigDecimal;

public class LevelMeta {
    private final String title;
    private final String description;
    private final Level level;
    private final int levelMaximo;
    private final BigDecimal custoPorLevel;

    public LevelMeta(String title, String description, Level level, int levelMaximo, BigDecimal custoPorLevel) {
        this.title = title;
        this.description = description;
        this.level = level;
        this.levelMaximo = levelMaximo;
        this.custoPorLevel = custoPorLevel;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Level getLevel() {
        return level;
    }

    public int getLevelMaximo() {
        return levelMaximo;
    }

    public BigDecimal getCustoPorLevel() {
        return custoPorLevel;
    }
}
