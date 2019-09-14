package net.factmc.FactAchievements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.factmc.FactCore.FactSQLConnector;

public class Achievements {
	
	// WILL ADD MORE THINGS NOT UPDATE
	public static void updateAchievement(UUID uuid, String server, String achievement, String progress) {
		
		if (progress.split("/").length == 2) return;
		String name = FactSQLConnector.getName(uuid);
		
		boolean exists = FactSQLConnector.getCount(FactSQLConnector.getAchievementsTable(), new String[] {"UUID", "SERVER", "NAME"},
				new Object[] {uuid.toString(), server, achievement}) > 0;
		
		if (!exists) {
			FactSQLConnector.insertRow(FactSQLConnector.getAchievementsTable(), new String[] {"UUID", "SERVER", "NAME", "PROGRESS"},
					new Object[] {uuid.toString(), server, achievement, progress});
		}
	
		else {
			FactSQLConnector.updateRow(FactSQLConnector.getAchievementsTable(), new String[] {"UUID", "SERVER", "NAME"},
					new Object[] {uuid.toString(), server, achievement}, "PROGRESS", progress);
		}
		
		System.out.println("[FactAchievements] Set progress of " + achievement +
				" on " + server + " for " + name + " to " + progress);
		
	}
	
	public static void grantAchievement(UUID uuid, String server, String achievement) {
		updateAchievement(uuid, server, achievement, "1/1");
	}
	public static void revokeAchievement(UUID uuid, String server, String achievement) {
		updateAchievement(uuid, server, achievement, "0/1");
	}
	
	
	public static String getAchievement(UUID uuid, String server, String achievement) {
		
		return FactSQLConnector.getValues(FactSQLConnector.getAchievementsTable(), new String[] {"UUID", "SERVER", "NAME"},
				new Object[] {uuid.toString(), server, achievement}, "PROGRESS").toString();
		
	}
	
	
	
	public static List<String[]> getAchievementList(UUID uuid, String server) {
		
		List<String[]> list = new ArrayList<String[]>();
		
		List<Map<String, Object>> rows = FactSQLConnector.getRows(FactSQLConnector.getAchievementsTable(), new String[] {"UUID", "SERVER"},
				new Object[] {uuid.toString(), server});
		for (Map<String, Object> row : rows) {
			list.add(new String[] {row.get("NAME").toString(), row.get("PROGRESS").toString()});
		}
		
		return list;
	}
}
