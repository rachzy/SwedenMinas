package com.redesweden.swedenminas.models;

import com.redesweden.swedencaixas.data.Caixas;
import com.redesweden.swedencash.models.ChequeCash;
import com.redesweden.swedenflocos.models.ChequeFlocos;
import com.redesweden.swedenminas.types.RecompensaTipo;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

public class Recompensa {
    private final int sorte;
    private final RecompensaTipo tipo;

    public Recompensa(int sorte, RecompensaTipo tipo) {
        this.sorte = sorte;
        this.tipo = tipo;
    }

    public ItemStack gerar() {
        if(tipo == RecompensaTipo.MINA) {
            int chance = new Random().nextInt(401);
            if(chance >= 50 + sorte) {
                return new ItemStack(Material.AIR);
            }

            if(chance >= 25 + sorte) {
                return Caixas.getCaixaPorID("MINERADOR").getCaixa(1);
            }

            if(chance >= 10 + sorte) {
                return new ChequeFlocos("Mina", new BigDecimal("5000"), LocalDateTime.of(2023, 2, 1, 1, 10)).gerar();
            }

            if(chance >= 1 + sorte) {
                return Caixas.getCaixaPorID("MISTERIOSA").getCaixa(1);
            }

            return Caixas.getCaixaPorID("MAQUINAS").getCaixa(1);
        }

        int chance = new Random().nextInt(3701);

        int chanceAdicional = 1;

        if(sorte > 1) {
            chanceAdicional = 1 + (sorte / 2);
        }

        if(chance >= 350 * chanceAdicional) {
            return new ItemStack(Material.AIR);
        }

        if(chance >= 200 * chanceAdicional) {
            return Caixas.getCaixaPorID("MISTERIOSA").getCaixa(1);
        }

        if(chance >= 100 * chanceAdicional) {
            return Caixas.getCaixaPorID("MAQUINAS").getCaixa(1);
        }

        if(chance >= 50 * chanceAdicional) {
            return Caixas.getCaixaPorID("MITICA").getCaixa(1);
        }

        if(chance >= 20 * chanceAdicional) {
            return new ChequeCash("§b§lNEVASCA", new BigDecimal("3000"), LocalDateTime.of(2022, 1, 12, 12, 43)).gerar();
        }

        if(chance >= 3 * chanceAdicional) {
            return Caixas.getCaixaPorID("BOSS").getCaixa(1);
        }

        return Caixas.getCaixaPorID("VIP").getCaixa(1);
    }
}
