package net.factmc.FactAchievements.bungee;

import net.md_5.bungee.api.plugin.Plugin;

public class FactAchievementsBungee extends Plugin {
	
	public static Plugin plugin;
	
	public static Plugin getPlugin() {
		return plugin;
	}
	
	
	@Override
	public void onEnable() {
		
		registerListeners();
		registerCommands();
		
		plugin = this;
		
	}
	
	@Override
	public void onDisable() {
		
		plugin = null;
		
	}
	
	
	public void registerListeners() {
		
		
		
	}
	
	public void registerCommands() {
		
		this.getProxy().getPluginManager().registerCommand(this, new AchievementsCommand());
		
	}
	
}