package net.factmc.FactAchievements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.factmc.FactCore.FactSQL;

public class Achievements {
	
	// WILL ADD MORE THINGS NOT UPDATE
	public static boolean updateAchievement(UUID uuid, String server, String achievement, String progress) {
		
		if (progress.split("/").length != 2) return false;
		boolean exists = FactSQL.getInstance().count(FactSQL.getAchievementsTable(), "`UUID`=? AND `SERVER`=? AND `NAME`=?",
				new Object[] {uuid.toString(), server, achievement}) > 0;
		
		boolean success = false;
		if (!exists) {
			success = FactSQL.getInstance().insert(FactSQL.getAchievementsTable(), new String[] {"UUID", "SERVER", "NAME", "PROGRESS"},
					new Object[] {uuid.toString(), server, achievement, progress});
		}
	
		else {
			success = FactSQL.getInstance().update(FactSQL.getAchievementsTable(), "PROGRESS", progress, "`UUID`=? AND `SERVER`=? AND `NAME`=?",
					new Object[] {uuid.toString(), server, achievement});
		}
		
		if (success) System.out.println("[FactAchievements] Set progress of " + achievement +
					" on " + server + " for " + FactSQL.getInstance().getName(uuid) + " to " + progress);
		else System.out.println("[FactAchievements] Failed to set progress of " + achievement +
				" on " + server + " for " + FactSQL.getInstance().getName(uuid) + " to " + progress);
		return success;
		
	}
	
	public static boolean grantAchievement(UUID uuid, String server, String achievement) {
		return updateAchievement(uuid, server, achievement, "1/1");
	}
	public static boolean revokeAchievement(UUID uuid, String server, String achievement) {
		return updateAchievement(uuid, server, achievement, "0/1");
	}
	
	
	public static String getAchievement(UUID uuid, String server, String achievement) {
		
		return FactSQL.getInstance().select(FactSQL.getAchievementsTable(), "PROGRESS", "`UUID`=? AND `SERVER`=? AND `NAME`=?",
				new Object[] {uuid.toString(), server, achievement}).toString();
		
	}
	
	
	
	public static List<String[]> getAchievementList(UUID uuid, String server) {
		
		List<String[]> list = new ArrayList<String[]>();
		
		List<Map<String, Object>> rows = FactSQL.getInstance().select(FactSQL.getAchievementsTable(), new String[] {"NAME", "PROGRESS"},
				"`UUID`=? AND `SERVER`=?", new Object[] {uuid.toString(), server});
		for (Map<String, Object> row : rows) {
			list.add(new String[] {row.get("NAME").toString(), row.get("PROGRESS").toString()});
		}
		
		return list;
	}
}
