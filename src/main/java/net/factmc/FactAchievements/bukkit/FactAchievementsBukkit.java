package net.factmc.FactAchievements.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

import net.factmc.FactAchievements.server.creative.CreativeListeners;
import net.factmc.FactAchievements.server.hub.HubListeners;
import net.factmc.FactAchievements.server.survival.SurvivalListeners;

public class FactAchievementsBukkit extends JavaPlugin {
	
	public static JavaPlugin plugin;
	
	public static JavaPlugin getPlugin() {
		return plugin;
	}
	
	
	@Override
	public void onEnable() {
		
		saveDefaultConfig();
		
		registerListeners();
		registerCommands();
		
		plugin = this;
		
	}
	
	@Override
	public void onDisable() {
		
		plugin = null;
		
	}
	
	
	public void registerListeners() {
		
		String server = getConfig().getString("server");
		if (server.equalsIgnoreCase("creative")) {
			getServer().getPluginManager().registerEvents(new CreativeListeners(), plugin);
		}
		else if (server.equalsIgnoreCase("hub")) {
			getServer().getPluginManager().registerEvents(new HubListeners(), plugin);
		}
		else if (server.equalsIgnoreCase("survival")) {
			getServer().getPluginManager().registerEvents(new SurvivalListeners(), plugin);
		}
		else {
			getLogger().info("Invalid server in config");
		}
		
	}
	
	public void registerCommands() {
		
		
		
	}
	
}