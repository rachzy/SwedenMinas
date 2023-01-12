package com.redesweden.swedenminas.functions;

import com.redesweden.swedenminas.models.Nevasca;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class NevascaFactory {
    private final Nevasca nevascaDummy;

    public NevascaFactory(int level) {
        nevascaDummy = new Nevasca("dummy", "dummy", level, new ArrayList<>(), null);
    }

    public ItemStack gerarItem() {
        return nevascaDummy.getNevasca();
    }
}
