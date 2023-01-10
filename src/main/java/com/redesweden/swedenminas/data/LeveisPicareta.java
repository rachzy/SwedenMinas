package com.redesweden.swedenminas.data;

import com.redesweden.swedenminas.models.LevelMeta;
import com.redesweden.swedenminas.types.Level;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class LeveisPicareta {
    private static final List<LevelMeta> leveis = new ArrayList<>();

    public static void setup() {
        LevelMeta eficiencia = new LevelMeta("Eficiência", "Quebra blocos mais rápido", Level.EFICIENCIA, 1000, new BigDecimal("1000"));
        LevelMeta fortuna = new LevelMeta("Fortuna", "Multiplica o money recebido", Level.FORTUNA, 200, new BigDecimal("5000"));
        LevelMeta multiplicador = new LevelMeta("Multiplicador", "Multiplica os flocos recebidos", Level.MULTPLICADOR, 200, new BigDecimal("4000"));
        LevelMeta invocador = new LevelMeta("Invocador", "Aumenta as chances de transformar o \nbloco quebrado em uma Lucky Block", Level.INVOCADOR, 1000, new BigDecimal("5000"));
        LevelMeta sortudo = new LevelMeta("Sortudo", "Aumenta suas chances de receber itens melhores \nao quebrar Lucky Blocks", Level.SORTUDO, 1000, new BigDecimal("2000"));
        LevelMeta velocidade = new LevelMeta("Velocidade", "Lhe dá efeito de Velocidade na Mina", Level.VELOCIDADE, 2, new BigDecimal("10000"));
        LevelMeta pressa = new LevelMeta("Pressa", "Lhe dá efeito de Pressa na Mina", Level.PRESSA, 2, new BigDecimal("10000"));
        LevelMeta explosao = new LevelMeta("Explosão", "Aumenta suas chances de criar uma \nexplosão de blocos enquanto minera", Level.EXPLOSAO, 1000, new BigDecimal("1000"));
        LevelMeta laser = new LevelMeta("Laser", "Aumenta suas chances de quebrar \numa camada inteira de blocos", Level.LASER, 3000, new BigDecimal("4000"));
        LevelMeta superLevel = new LevelMeta("Super", "Aumenta suas chances de quebrar\n TODA a Mina\n(Também conhecido como Nuclear)", Level.SUPER, 10000, new BigDecimal("2000"));

        leveis.add(eficiencia);
        leveis.add(fortuna);
        leveis.add(multiplicador);
        leveis.add(invocador);
        leveis.add(sortudo);
        leveis.add(velocidade);
        leveis.add(pressa);
        leveis.add(explosao);
        leveis.add(laser);
        leveis.add(superLevel);
    }

    public static LevelMeta getLevel(Level level) {
        return leveis.stream().filter(levelIn -> levelIn.getLevel() == level).findFirst().orElse(null);
    }
}
