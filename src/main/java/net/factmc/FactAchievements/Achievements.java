package net.factmc.FactAchievements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import net.factmc.FactCore.FactSQL;

public class Achievements {
	
	// WILL ADD MORE THINGS NOT UPDATE
	public static CompletableFuture<Boolean> updateAchievement(UUID uuid, String server, String achievement, String progress) {
		
		if (progress.split("/").length != 2) return CompletableFuture.completedFuture(false);
		
		return FactSQL.getInstance().update(FactSQL.getAchievementsTable(), "PROGRESS", progress, "`UUID`=? AND `SERVER`=? AND `NAME`=?",
				new Object[] {uuid.toString(), server, achievement}).thenCompose((result) -> {
					
					if (result <= 0) {
						return FactSQL.getInstance().insert(FactSQL.getAchievementsTable(), new String[] {"UUID", "SERVER", "NAME", "PROGRESS"},
								new Object[] {uuid.toString(), server, achievement, progress});
					}
					
					else return CompletableFuture.completedFuture(result);
					
				}).thenApply((result) -> {
					
					if (result > 0) {
						System.out.println("[FactAchievements] Set progress of " + achievement +
								" on " + server + " for " + FactSQL.getInstance().getName(uuid) + " to " + progress);
						return true;
					}
					else {
						System.out.println("[FactAchievements] Failed to set progress of " + achievement +
							" on " + server + " for " + FactSQL.getInstance().getName(uuid) + " to " + progress);
						return false;
					}
					
				});
		
	}
	
	public static CompletableFuture<Boolean> grantAchievement(UUID uuid, String server, String achievement) {
		return updateAchievement(uuid, server, achievement, "1/1");
	}
	public static CompletableFuture<Boolean> revokeAchievement(UUID uuid, String server, String achievement) {
		return updateAchievement(uuid, server, achievement, "0/1");
	}
	
	
	public static String getAchievement(UUID uuid, String server, String achievement) {
		return FactSQL.getInstance().select(FactSQL.getAchievementsTable(), "PROGRESS", "`UUID`=? AND `SERVER`=? AND `NAME`=?",
				new Object[] {uuid.toString(), server, achievement}).toString();
	}
	
	
	
	public static CompletableFuture<List<String[]>> getAchievementList(UUID uuid, String server) {
		
		return FactSQL.getInstance().select(FactSQL.getAchievementsTable(), new String[] {"NAME", "PROGRESS"},
				"`UUID`=? AND `SERVER`=?", new Object[] {uuid.toString(), server}).thenApply((rows) -> {
					
					List<String[]> list = new ArrayList<String[]>();
					
					for (Map<String, Object> row : rows) {
						list.add(new String[] {row.get("NAME").toString(), row.get("PROGRESS").toString()});
					}
					
					return list;
					
				});
		
	}
}
