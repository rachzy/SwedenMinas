package com.redesweden.swedenminas;

import com.redesweden.swedenminas.commands.*;
import com.redesweden.swedenminas.data.*;
import com.redesweden.swedenminas.events.*;
import com.redesweden.swedenminas.files.ConfigFile;
import com.redesweden.swedenminas.files.MinasFile;
import com.redesweden.swedenminas.files.NevascasFile;
import com.redesweden.swedenminas.files.PicaretasFile;
import org.bukkit.plugin.java.JavaPlugin;

public final class SwedenMinas extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("Ativando SwedenMinas...");

        // Cria o arquivo de config padrão
        getConfig().options().copyDefaults();
        saveConfig();

        // Inicializar leveis
        LeveisPicareta.setup();

        // Configurar boosters
        Boosters.setup();

        // Inicializar arquivos
        MinasFile.setup();
        MinasFile.get().options().copyDefaults(true);
        MinasFile.save();

        NevascasFile.setup();
        NevascasFile.get().options().copyDefaults(true);
        NevascasFile.save();

        PicaretasFile.setup();
        PicaretasFile.get().options().copyDefaults(true);
        PicaretasFile.save();

        ConfigFile.setup();
        ConfigFile.get().options().copyDefaults(true);
        ConfigFile.save();

        // Registrar eventos
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        getServer().getPluginManager().registerEvents(new SwitchWorldListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDropListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractionListener(), this);
        getServer().getPluginManager().registerEvents(new nChatMessageListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new EntityClickListener(), this);

        // Registrar comandos
        getCommand("admina").setExecutor(new AdminaCommand());
        getCommand("mina").setExecutor(new MinaCommand());
        getCommand("minerar").setExecutor(new MinerarCommand());
        getCommand("minas").setExecutor(new MinasCommand());
        getCommand("darnevasca").setExecutor(new DarNevascaCommand());
        getCommand("nevasca").setExecutor(new NevascaCommand());
    }

    @Override
    public void onDisable() {
        System.out.println("Retirando jogadores da mina");
        Nevascas.salvarNevascasAlteradas();
        Picaretas.salvarPicaretas();
        Players.removerTodos();
        System.out.println("Desativando SwedenMinas...");
    }
}
