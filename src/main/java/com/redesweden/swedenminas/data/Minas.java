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

    public static void removerMina(Mina mina) {
        minas.remove(mina);
    }

    public static Mina getMinaPorId(String id) {
        return minas.stream().filter(mina -> mina.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
    }

    public static Mina getMinaPorTitulo(String titulo) {
        return minas.stream().filter(mina -> mina.getTitulo().equals(titulo)).findFirst().orElse(null);
    }

    public static List<Mina> getMinas() {
        return minas;
    }
}
