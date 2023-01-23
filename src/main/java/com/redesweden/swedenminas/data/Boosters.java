package com.redesweden.swedenminas.data;

import com.redesweden.swedenminas.models.Booster;
import com.redesweden.swedenminas.types.BoosterTipo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Boosters {
    private static final List<Booster> boosters = new ArrayList<>();

    public static void setup() {
        boosters.add(new Booster("7 dias", BoosterTipo.MONEY, 2, new BigDecimal("175000"), "swedenminas.money2x", "lp user {player} permission settemp {permission} true 7d"));
        boosters.add(new Booster("15 dias", BoosterTipo.MONEY, 2, new BigDecimal("280000"), "swedenminas.money2x", "lp user {player} permission settemp {permission} true 15d"));
        boosters.add(new Booster("30 dias", BoosterTipo.MONEY, 2, new BigDecimal("490000"), "swedenminas.money2x", "lp user {player} permission settemp {permission} true 30d"));
        boosters.add(new Booster("7 dias", BoosterTipo.MONEY, 3, new BigDecimal("215000"), "swedenminas.money3x", "lp user {player} permission settemp {permission} true 7d"));
        boosters.add(new Booster("15 dias", BoosterTipo.MONEY, 3, new BigDecimal("398000"), "swedenminas.money3x", "lp user {player} permission settemp {permission} true 15d"));
        boosters.add(new Booster("30 dias", BoosterTipo.MONEY, 3, new BigDecimal("710000"), "swedenminas.money3x", "lp user {player} permission settemp {permission} true 30d"));

        boosters.add(new Booster("7 dias", BoosterTipo.FLOCOS, 2, new BigDecimal("200000"), "swedenminas.flocos2x", "lp user {player} permission settemp {permission} true 7d"));
        boosters.add(new Booster("15 dias", BoosterTipo.FLOCOS, 2, new BigDecimal("340000"), "swedenminas.flocos2x", "lp user {player} permission settemp {permission} true 15d"));
        boosters.add(new Booster("30 dias", BoosterTipo.FLOCOS, 2, new BigDecimal("550000"), "swedenminas.flocos2x", "lp user {player} permission settemp {permission} true 30d"));
        boosters.add(new Booster("7 dias", BoosterTipo.FLOCOS, 3, new BigDecimal("310000"), "swedenminas.flocos3x", "lp user {player} permission settemp {permission} true 7d"));
        boosters.add(new Booster("15 dias", BoosterTipo.FLOCOS, 3, new BigDecimal("580000"), "swedenminas.flocos3x", "lp user {player} permission settemp {permission} true 15d"));
        boosters.add(new Booster("30 dias", BoosterTipo.FLOCOS, 3, new BigDecimal("1000000"), "swedenminas.flocos3x", "lp user {player} permission settemp {permission} true 30d"));
    }

    public static List<Booster> getBoosters() {
        return boosters;
    }
}
