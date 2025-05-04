package com.stratecide.disable.villagers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.loot.LootGsons;
import net.minecraft.loot.LootTable;

/**
 * Configuration class to hold all mod settings
 */
public class ModConfig {
    private static final Gson GSON = LootGsons.getTableGsonBuilder().create();

    public final boolean killVillagers;
    public final boolean disableWanderingTrader;
    public final boolean blockTrading;
    public final boolean spareExperiencedVillagers;
    public final boolean breeding;
    public final boolean disableZombies;
    public final boolean curableZombies;
    public final boolean disableVillages;
    public final LootTable curedZombieLoot;

    public ModConfig(JsonObject json) {
        this.killVillagers = json.get("killVillagers").getAsBoolean();
        this.disableWanderingTrader = json.has("disableWanderingTrader") && json.get("disableWanderingTrader").getAsBoolean();
        this.blockTrading = json.get("blockTrading").getAsBoolean();
        this.spareExperiencedVillagers = json.get("spareExperiencedVillagers").getAsBoolean();
        this.breeding = json.get("breeding").getAsBoolean();
        this.disableZombies = json.has("disableZombies") && json.get("disableZombies").getAsBoolean();
        this.curableZombies = json.get("curableZombies").getAsBoolean();
        this.disableVillages = json.get("disableVillages").getAsBoolean();
        this.tradeCycling = json.get("tradeCycling").getAsBoolean();

        JsonElement lootJson = json.get("curedZombieLoot");
        this.curedZombieLoot = lootJson != null ? GSON.fromJson(lootJson, LootTable.class) : null;
    }
} 