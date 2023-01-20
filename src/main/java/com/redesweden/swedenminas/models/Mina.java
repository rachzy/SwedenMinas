package com.redesweden.swedenminas.models;

import com.redesweden.swedenminas.SwedenMinas;
import com.redesweden.swedenminas.data.Minas;
import com.redesweden.swedenminas.data.Players;
import com.redesweden.swedenminas.files.MinasFile;
import com.redesweden.swedenminas.types.MinaTipo;
import com.redesweden.swedenranks.data.Ranks;
import com.redesweden.swedenranks.models.Rank;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

import java.math.BigDecimal;
import java.util.Random;

public class Mina {
    private final String id;
    private final String titulo;
    private final MinaTipo tipo;
    private BigDecimal valorBloco;
    private final int chanceLuckyBlock;
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

                    int chanceAleatoria = new Random().nextInt(21);
                    if (chanceAleatoria < chanceLuckyBlock) {
                        blocoMina.setType(Material.GOLD_BLOCK);
                    } else {
                        blocoMina.setType(bloco.getType());
                        blocoMina.setData(bloco.getData().getData());
                    }

                }
            }
        }

        Players.getPlayers().forEach(player -> {
            if(player.getMina() != this) return;

            Player playerB = Bukkit.getServer().getPlayer(player.getNickname());
            if(playerB == null || !playerB.isOnline()) return;

            playerB.teleport(this.getSpawn());
            playerB.sendTitle("§e§lMINA", "§aA mina foi resetada.");
        });
    }

    public void iniciarResetAutomatico() {
        BukkitScheduler scheduler = Bukkit.getScheduler();

        if(pos1 != null && pos2 != null) {
            resetar();
        }

        scheduler.runTaskLater(SwedenMinas.getPlugin(SwedenMinas.class), this::iniciarResetAutomatico, 20L * 150L);
    }

    public void destroy() {
        Players.getPlayers().forEach((player) -> {
            if(player.getMina() != this) return;
            player.sairDaMina(true, false);
        });

        MinasFile.get().set(String.format("minas.%s", id), null);
        MinasFile.save();

        Bukkit.getScheduler().runTaskLater(SwedenMinas.getPlugin(SwedenMinas.class), () -> {
            Minas.removerMina(this);
        }, 20L);
    }
}
