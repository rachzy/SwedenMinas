package com.redesweden.swedenminas.data;

import com.redesweden.swedenflocos.models.PlayerFlocos;
import com.redesweden.swedenminas.SwedenMinas;
import com.redesweden.swedenminas.models.Picareta;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Picaretas {
    private static final List<Picareta> picaretasList = new ArrayList<>();
    private static final List<Picareta> picaretasTop = new ArrayList<>();
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

    public static List<Picareta> getTopPicaretas() {
        return picaretasTop;
    }

    public static void addPicaretaModificada(Picareta picareta) {
        picaretasModificadas.put(picareta, picareta);
    }

    public static void calcularTopPicaretas() {
        System.out.println("[LOGGER] Calculando TOP Players por Bloco quebrados");
        picaretasTop.clear();
        List<Picareta> sortedPlayers = picaretasList;
        sortedPlayers = sortedPlayers
                .stream()
                .sorted((p1, p2) -> p2.getBlocosQuebrados().compareTo(p1.getBlocosQuebrados()))
                .collect(Collectors.toList());

        sortedPlayers.forEach((picaretaIn) -> {
            Picareta picareta = new Picareta(
                    picaretaIn.getUuid(),
                    picaretaIn.getDono(),
                    picaretaIn.getNivel(),
                    picaretaIn.getMultiplicadorDeLevel(),
                    picaretaIn.getEnergia(),
                    picaretaIn.getBlocosQuebrados()
            );
            picaretasTop.add(picareta);
        });
    }

    public static void iniciarLoopDeTopPicaretas() {
        calcularTopPicaretas();
        Bukkit.getScheduler().runTaskLater(SwedenMinas.getPlugin(SwedenMinas.class), Picaretas::iniciarLoopDeTopPicaretas, 20L * 600L);
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
