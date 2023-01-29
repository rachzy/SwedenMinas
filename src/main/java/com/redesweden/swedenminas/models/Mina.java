package com.redesweden.swedenminas.models;

import com.redesweden.swedenminas.SwedenMinas;
import com.redesweden.swedenminas.data.Minas;
import com.redesweden.swedenminas.data.Players;
import com.redesweden.swedenminas.files.MinasFile;
import com.redesweden.swedenminas.functions.PastedBlock;
import com.redesweden.swedenminas.types.MinaTipo;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Mina {
    private final String id;
    private final String titulo;
    private final MinaTipo tipo;
    private final int chanceLuckyBlock;
    private BigDecimal valorBloco;
    private ItemStack bloco;
    private Location spawn;
    private Location pos1;
    private Location pos2;


    public Mina(String id, String titulo, MinaTipo tipo, BigDecimal valorBloco, ItemStack bloco, Location spawn, Location pos1, Location pos2) {
        this.id = id;
        this.titulo = titulo;
        this.tipo = tipo;
        this.valorBloco = valorBloco;
        this.bloco = bloco;
        this.spawn = spawn;
        this.pos1 = pos1;
        this.pos2 = pos2;

        this.chanceLuckyBlock = Bukkit.getPluginManager().getPlugin("SwedenMinas").getConfig().getInt(String.format("chance_luckyBlock_mina_%s", tipo.toString().toLowerCase()));
    }

    public void save(String key, Object value) {
        MinasFile.get().set(String.format("minas.%s.%s", id, key), value);
        MinasFile.save();
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public MinaTipo getTipo() {
        return tipo;
    }

    public BigDecimal getValorBloco() {
        return valorBloco;
    }

    public void setValorBloco(BigDecimal valorBloco) {
        this.valorBloco = valorBloco;
        save("valorPorBloco", valorBloco);
    }

    public ItemStack getBloco() {
        return bloco;
    }

    public void setBloco(ItemStack bloco) {
        this.bloco = bloco;
        save("blocoID", bloco.getTypeId());
        save("blocoSubID", bloco.getData().getData());
    }

    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
        save("spawn.x", spawn.getX());
        save("spawn.y", spawn.getY());
        save("spawn.z", spawn.getZ());
        save("spawn.mundo", spawn.getWorld().getName());
    }

    public Location getPos1() {
        return pos1;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
        save("pos1.x", pos1.getX());
        save("pos1.y", pos1.getY());
        save("pos1.z", pos1.getZ());
        save("pos1.mundo", pos1.getWorld().getName());

        ItemStack bloco;
        Block blocoAbaixo = pos1.clone().subtract(0, 1, 0).getBlock();

        bloco = new ItemStack(blocoAbaixo.getType(), 1, blocoAbaixo.getData());
        setBloco(bloco);
    }

    public Location getPos2() {
        return pos2;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
        save("pos2.x", pos2.getX());
        save("pos2.y", pos2.getY());
        save("pos2.z", pos2.getZ());
        save("pos2.mundo", pos2.getWorld().getName());
    }

    public void resetar() {
        Players.getPlayers().forEach(player -> {
            if (player.getMina() != this) return;

            Player playerB = Bukkit.getServer().getPlayer(player.getNickname());
            if (playerB == null || !playerB.isOnline()) return;

            playerB.teleport(this.getSpawn());
            playerB.playSound(playerB.getLocation(), Sound.LEVEL_UP, 3.0F, 2F);
            playerB.sendTitle("§e§lMINA", "§aA mina foi resetada.");
        });

        double xMaior = Math.max(pos1.getX(), pos2.getX());
        double xMenor = Math.min(pos1.getX(), pos2.getX());

        double yMaior = Math.max(pos1.getY(), pos2.getY());
        double yMenor = Math.min(pos1.getY(), pos2.getY());

        double zMaior = Math.max(pos1.getZ(), pos2.getZ());
        double zMenor = Math.min(pos1.getZ(), pos2.getZ());
        for (double x = xMenor; x <= xMaior; x++) {
            for (double y = yMenor; y < yMaior; y++) {
                for (double z = zMenor; z <= zMaior; z++) {
                    Block blocoMina = pos1.getWorld().getBlockAt(new Location(pos1.getWorld(), x, y, z));

                    int chanceAleatoria = new Random().nextInt(61);

                    PastedBlock pastedBlock;

                    if (chanceAleatoria < chanceLuckyBlock) {
                        pastedBlock = new PastedBlock((int) x, (int) y, (int) z, Material.GOLD_BLOCK.getId(), (byte) 0);
                    } else {
                        pastedBlock = new PastedBlock((int) x, (int) y, (int) z, bloco.getTypeId(), bloco.getData().getData());
                    }

                    PastedBlock.BlockQueue.getQueue(blocoMina.getWorld()).add(pastedBlock);
                }
            }
        }

    }

    public void iniciarResetAutomatico() {
        BukkitScheduler scheduler = Bukkit.getScheduler();

        if (pos1 != null && pos2 != null && Players.getPlayers().stream().anyMatch(player -> player.getMina() == this)) {
            resetar();
        }

        scheduler.runTaskLater(SwedenMinas.getPlugin(SwedenMinas.class), this::iniciarResetAutomatico, 20L * 150L);
    }

    public void destroy() {
        Players.getPlayers().forEach((player) -> {
            if (player.getMina() != this) return;
            player.sairDaMina(true, false);
        });

        MinasFile.get().set(String.format("minas.%s", id), null);
        MinasFile.save();

        Bukkit.getScheduler().runTaskLater(SwedenMinas.getPlugin(SwedenMinas.class), () -> {
            Minas.removerMina(this);
        }, 20L);
    }
}
