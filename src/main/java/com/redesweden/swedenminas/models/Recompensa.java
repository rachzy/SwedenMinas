package com.redesweden.swedenminas.models;

import com.redesweden.swedencaixas.data.Caixas;
import com.redesweden.swedenmaquinas.functions.MaquinasFactory;
import com.redesweden.swedenmaquinas.types.TipoMaquina;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.Random;

public class Recompensa {
    private final int sorte;

    public Recompensa(int sorte) {
        this.sorte = sorte;
    }

    public ItemStack gerar() {
        int chance = new Random().nextInt(201);

        if(chance >= 80 + sorte) {
            return new ItemStack(Material.AIR);
        }

        if(chance >= 50 + sorte) {
            return Caixas.getCaixaPorID("MINERADOR").getCaixa(1);
        }

        if(chance >= 20 + sorte) {
            return new MaquinasFactory(TipoMaquina.FLOCOS, new BigDecimal("300")).criar();
        }

        if(chance >= 10 + sorte) {
            return Caixas.getCaixaPorID("MISTERIOSA").getCaixa(1);
        }

        return new MaquinasFactory(TipoMaquina.CASH, new BigDecimal("50")).criar();
    }
}
