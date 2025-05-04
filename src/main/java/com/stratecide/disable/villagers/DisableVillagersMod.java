package com.stratecide.disable.villagers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.api.ModInitializer;
import net.minecraft.loot.LootGsons;
import net.minecraft.loot.LootTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class DisableVillagersMod implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("disable.villagers");
	private static final String CONFIG_FOLDER = "config/";
	private static final String CONFIG_FILE = CONFIG_FOLDER + "disable-villagers.json";
	private static final String DEFAULT_CONFIG_PATH = "data/disable-villagers/default_config.json";
	private static final Gson GSON = LootGsons.getTableGsonBuilder().create();
	
	private static ModConfig config;

	@Override
	public void onInitialize() {
		loadConfig();
		LOGGER.info("DisableVillagers mod initialized with configuration: {}", config);
	}

	/**
	 * Loads the mod configuration from file or creates default if not present
	 */
	private static void loadConfig() {
		try {
			Path configPath = Path.of(CONFIG_FILE);
			String configData;

			if (!Files.exists(configPath)) {
				LOGGER.info("Creating default configuration file");
				Files.createDirectories(configPath.getParent());
				configData = loadDefaultConfig();
				Files.writeString(configPath, configData);
			} else {
				configData = Files.readString(configPath);
			}

			JsonObject jsonConfig = JsonParser.parseString(configData).getAsJsonObject();
			config = new ModConfig(jsonConfig);
			LOGGER.info("Configuration loaded successfully");
		} catch (IOException e) {
			LOGGER.error("Failed to load configuration", e);
			// Fallback to default config
			try {
				String defaultConfig = loadDefaultConfig();
				config = new ModConfig(JsonParser.parseString(defaultConfig).getAsJsonObject());
			} catch (IOException ex) {
				LOGGER.error("Failed to load default configuration", ex);
				throw new RuntimeException("Failed to load configuration", ex);
			}
		}
	}

	private static String loadDefaultConfig() throws IOException {
		try (InputStream is = DisableVillagersMod.class.getClassLoader().getResourceAsStream(DEFAULT_CONFIG_PATH)) {
			if (is == null) {
				throw new IOException("Default configuration file not found in resources");
			}
			return new String(is.readAllBytes());
		}
	}

	// Configuration getters
	public static boolean isKillVillagers() {
		return config.killVillagers;
	}

	public static boolean isDisableWanderingTrader() {
		return config.disableWanderingTrader;
	}

	public static boolean isBlockTrading() {
		return config.blockTrading;
	}

	public static boolean isSpareExperiencedVillagers() {
		return config.spareExperiencedVillagers;
	}

	public static boolean isBreeding() {
		return config.breeding;
	}

	public static boolean isDisableZombies() {
		return config.disableZombies;
	}

	public static boolean isCurableZombies() {
		return config.curableZombies;
	}

	public static boolean isDisableVillages() {
		return config.disableVillages;
	}

	public static LootTable getCuredZombieLoot() {
		return config.curedZombieLoot;
	}

	public static boolean isTradeCycling() {
		return config.tradeCycling;
	}
}
