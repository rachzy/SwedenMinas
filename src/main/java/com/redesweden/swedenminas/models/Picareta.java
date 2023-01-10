package com.redesweden.swedenminas.models;

import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedenminas.SwedenMinas;
import com.redesweden.swedenminas.data.Picaretas;
import com.redesweden.swedenminas.files.PicaretasFile;
import com.redesweden.swedenminas.types.Level;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Picareta {
    private final String uuid;
    private final String dono;
    private final List<LevelPicareta> leveis = new ArrayList<>();
    private int nivel;
    private BigDecimal energia;

    public Picareta(String uuid, String dono, int nivel, BigDecimal energia) {
        this.uuid = uuid;
        this.dono = dono;
        this.nivel = nivel;
        this.energia = energia;
    }

    public void salvarDados() {
        PicaretasFile.get().set(String.format("picaretas.%s.nivel", uuid), nivel);
        PicaretasFile.get().set(String.format("picaretas.%s.energia", uuid), energia.toString());

        List<String> leveisStrings = new ArrayList<>();

        leveis.forEach(level -> {
            leveisStrings.add(String.format("%s,%s", level.getLevel(), level.getLevelAtual()));
        });

        PicaretasFile.get().set(String.format("picaretas.%s.leveis", uuid), leveisStrings);
        PicaretasFile.save();
    }

    public String getDono() {
        return dono;
    }

    public String getPicaretaNome() {
        return String.format("§e§lPICARETA §7(%s)", nivel);
    }

    public List<String> getPicaretaLore() {
        List<String> picaretaLore = new ArrayList<>();

        picaretaLore.add("");
        picaretaLore.add(String.format(" §a■ §fNível: §a%s§7/§a%s", nivel, 100));
        picaretaLore.add(String.format(" §6■ §fEnergia: §e%s§7/§e%s", new ConverterQuantia(energia).emLetras(), new ConverterQuantia(BigDecimal.valueOf((nivel + 1) * 2000L)).emLetras()));
        picaretaLore.add("");

        leveis.forEach((level) -> {
            picaretaLore.add(String.format("§7%s %s", level.getMeta().getTitle(), level.getLevelAtual()));
        });

        return picaretaLore;
    }

    public ItemStack pegarPicareta(boolean aplicarEfeitos) {
        ItemStack picareta = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        ItemMeta picaretaMeta = picareta.getItemMeta();
        picaretaMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        picaretaMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        picaretaMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        picaretaMeta.setDisplayName(getPicaretaNome());
        picaretaMeta.setLore(getPicaretaLore());

        picaretaMeta.spigot().setUnbreakable(true);

        picareta.setItemMeta(picaretaMeta);

        if(aplicarEfeitos) {
            Player player = Bukkit.getServer().getPlayer(dono);

            if(player != null && player.isOnline()) {
                leveis
                        .stream()
                        .filter(level -> level.getLevel() == Level.VELOCIDADE)
                        .findFirst()
                        .ifPresent(level -> {
                            if (level.getLevelAtual() != 0) {
                                BukkitScheduler scheduler = Bukkit.getScheduler();
                                scheduler.runTaskLater(SwedenMinas.getPlugin(SwedenMinas.class), () -> {
                                    player.addPotionEffect(PotionEffectType.SPEED.createEffect(999 * 999, level.getLevelAtual() - 1), true);
                                }, 1L);
                            }
                        });

                leveis
                        .stream()
                        .filter(level -> level.getLevel() == Level.PRESSA)
                        .findFirst()
                        .ifPresent(level -> {
                            if (level.getLevelAtual() != 0) {
                                BukkitScheduler scheduler = Bukkit.getScheduler();
                                scheduler.runTaskLater(SwedenMinas.getPlugin(SwedenMinas.class), () -> {
                                    player.addPotionEffect(PotionEffectType.FAST_DIGGING.createEffect(999 * 999, level.getLevelAtual() - 1), true);
                                }, 1L);
                            }
                        });
            }
        }

        leveis.stream().filter(level -> level.getLevel() == Level.EFICIENCIA).findFirst().ifPresent(levelEficiencia ->
                picareta.addUnsafeEnchantment(Enchantment.DIG_SPEED, levelEficiencia.getLevelAtual())
        );

        return picareta;
    }

    public void setarLevel(Level level, int valor) {
        LevelPicareta levelPassado = leveis.stream().filter((levelIn) -> levelIn.getLevel() == level).findFirst().orElse(null);
        if (levelPassado == null) {
            leveis.add(new LevelPicareta(this, level, valor));
        } else {
            levelPassado.setLevelAtual(valor);
        }
    }

    public List<LevelPicareta> getLeveis() {
        return leveis;
    }

    public int getNivel() {
        return nivel;
    }

    public void addNivel() {
        setNivel(nivel + 1);
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
        Picaretas.addPicaretaModificada(this);
    }

    public BigDecimal getEnergia() {
        return energia;
    }

    public void addEnergia() {
        setEnergia(energia.add(BigDecimal.valueOf(1)));
    }

    public void setEnergia(BigDecimal energia) {
        this.energia = energia;
        Picaretas.addPicaretaModificada(this);
    }
}
