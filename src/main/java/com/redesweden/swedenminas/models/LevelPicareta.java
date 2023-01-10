package com.redesweden.swedenminas.models;

import com.redesweden.swedenminas.data.LeveisPicareta;
import com.redesweden.swedenminas.data.Picaretas;
import com.redesweden.swedenminas.types.Level;

import java.math.BigDecimal;

public class LevelPicareta {
    private final Picareta picareta;
    private final Level level;
    private int levelAtual;
    private final int levelMaximo;
    private final BigDecimal custoPorLevel;

    public LevelPicareta(Picareta picareta, Level level, int levelAtual) {
        this.picareta = picareta;
        this.level = level;
        this.levelAtual = levelAtual;
        this.levelMaximo = LeveisPicareta.getLevel(level).getLevelMaximo();
        this.custoPorLevel = LeveisPicareta.getLevel(level).getCustoPorLevel();
    }

    public Level getLevel() {
        return level;
    }

    public LevelMeta getMeta() {
        return LeveisPicareta.getLevel(level);
    }

    public int getLevelAtual() {
        return levelAtual;
    }

    public int getLevelMaximo() {
        return levelMaximo;
    }

    public BigDecimal getCustoProximoLevel() {
        return custoPorLevel.multiply(BigDecimal.valueOf(levelAtual + 1));
    }

    public void setLevelAtual(int levelAtual) {
        this.levelAtual = levelAtual;
        Picaretas.addPicaretaModificada(picareta);
    }

    public void evoluirLevel() {
        setLevelAtual(levelAtual += 1);
    }
}
