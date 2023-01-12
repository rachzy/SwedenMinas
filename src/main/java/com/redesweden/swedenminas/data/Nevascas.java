package com.redesweden.swedenminas.data;

import com.redesweden.swedenminas.SwedenMinas;
import com.redesweden.swedenminas.models.Nevasca;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Nevascas {
    private final static List<Nevasca> nevascasList = new ArrayList<>();
    private final static Map<Nevasca, Nevasca> nevascasAlteradas = new HashMap<>();

    public static void addNevasca(Nevasca nevasca) {
        nevascasList.add(nevasca);
    }

    public static Nevasca getNevascaPorDono(String nickname) {
        return nevascasList.stream().filter(nevasca -> nevasca.getDono().equals(nickname)).findFirst().orElse(null);
    }

    public static Nevasca getNevascaPorLocal(Location local) {
        return nevascasList.stream().filter(nevasca -> nevasca.getLocal().getX() == local.getX() && nevasca.getLocal().getY() == local.getY() && nevasca.getLocal().getZ() == local.getZ()).findFirst().orElse(null);
    }

    public static List<Nevasca> getNevascas() {
        return nevascasList;
    }

    public static void removerNevasca(Nevasca nevasca) {
        nevascasList.remove(nevasca);
    }

    public static void addNevascaAlterada(Nevasca nevasca) {
        nevascasAlteradas.put(nevasca, nevasca);
    }

    public static Nevasca removerNevascaAlterada(Nevasca nevasca) {
        return nevascasAlteradas.remove(nevasca);
    }

    public static void salvarNevascasAlteradas() {
        nevascasAlteradas.values().forEach(Nevasca::salvarDados);
    }

    public static void iniciarSalvamentoAutomatico() {
        salvarNevascasAlteradas();
        Bukkit.getScheduler().runTaskLater(SwedenMinas.getPlugin(SwedenMinas.class), Nevascas::iniciarSalvamentoAutomatico, 20L * 1800L);
    }
}
