package com.redesweden.swedenminas.data;

import com.redesweden.swedenminas.SwedenMinas;
import com.redesweden.swedenminas.models.Picareta;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Picaretas {
    private static final List<Picareta> picaretasList = new ArrayList<>();
    private static final Map<Picareta, Picareta> picaretasModificadas = new HashMap<>();

    public static void addPicareta(Picareta picareta) {
        picaretasList.add(picareta);
    }

    public static Picareta getPicaretaPorDono(String nickname) {
        return picaretasList
                .stream()
                .filter(picareta -> picareta.getDono().equals(nickname))
                .findFirst()
                .orElse(null);
    }

    public static void addPicaretaModificada(Picareta picareta) {
        picaretasModificadas.put(picareta, picareta);
    }

    public static void salvarPicaretas() {
        System.out.println("[SwedenMinas] Salvando picaretas modificadas...");
        picaretasModificadas.values().forEach(Picareta::salvarDados);
    }

    public static void iniciarSalvamentoAutomatico() {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        salvarPicaretas();
        scheduler.runTaskLater(SwedenMinas.getPlugin(SwedenMinas.class), Picaretas::iniciarSalvamentoAutomatico, 20L * 1800L);
    }
}
