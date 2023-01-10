package com.redesweden.swedenminas.data;

import com.redesweden.swedenminas.models.Mina;
import com.redesweden.swedenranks.models.Rank;

import java.util.ArrayList;
import java.util.List;

public class Minas {
    private static final List<Mina> minas = new ArrayList<>();

    public static void addMina(Mina mina) {
        minas.add(mina);
    }

    public static Mina getMinaPorRank(Rank rank) {
        return minas.stream().filter(mina -> mina.getRank() == rank).findFirst().orElse(null);
    }

    public static List<Mina> getMinas() {
        return minas;
    }
}
