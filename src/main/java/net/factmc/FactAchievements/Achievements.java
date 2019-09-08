package net.factmc.FactAchievements;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.factmc.FactCore.FactSQLConnector;

public class Achievements {
	
	// WILL ADD MORE THINGS NOT UPDATE
	public static void updateAchievement(UUID uuid, String server, String achievement, String progress) {
		
		if (progress.split("/").length == 2) return;
		String name = FactSQLConnector.getName(uuid);
		
		boolean exists = false;
		try {
			
			PreparedStatement statement = FactSQLConnector.getMysql().getConnection()
					.prepareStatement("SELECT * FROM " + FactSQLConnector.getAchievementsTable() + " WHERE `UUID`=? AND `SERVER`=? AND `NAME`=?");
			statement.setString(1, uuid.toString());
			statement.setString(2, server);
			statement.setString(3, achievement);
			
			ResultSet rs = statement.executeQuery();
			if (rs.next()) exists = true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (!exists) {
			try {
				
				PreparedStatement insert = FactSQLConnector.getMysql().getConnection()
						.prepareStatement("INSERT INTO " + FactSQLConnector.getAchievementsTable()
						+ " (UUID,SERVER,NAME,PROGRESS) VALUE (?,?,?,?)");
				insert.setString(1, uuid.toString());
				insert.setString(2, server);
				insert.setString(3, achievement);
				insert.setString(4, progress);
				insert.executeUpdate();
				
				System.out.println("[FactAchievements] Set progress of " + achievement +
						" on " + server + " for " + name + " to " + progress);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	
		else {
			
			try {
				
				PreparedStatement insert = FactSQLConnector.getMysql().getConnection()
						.prepareStatement("UPDATE " + FactSQLConnector.getAchievementsTable()
						+ " SET `PROGRESS`=? WHERE `UUID`=? AND `SERVER`=? AND `NAME`=?");
				insert.setString(1, progress);
				insert.setString(2, uuid.toString());
				insert.setString(3, server);
				insert.setString(4, achievement);
				insert.executeUpdate();
				
				System.out.println("[FactAchievements] Set progress of " + achievement +
						" on " + server + " for " + name + " to " + progress);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	public static void grantAchievement(UUID uuid, String server, String achievement) {
		updateAchievement(uuid, server, achievement, "1/1");
	}
	public static void revokeAchievement(UUID uuid, String server, String achievement) {
		updateAchievement(uuid, server, achievement, "0/1");
	}
	
	
	public static String getAchievement(UUID uuid, String server, String achievement) {
		
		try {
			
			PreparedStatement statement = FactSQLConnector.getMysql().getConnection()
					.prepareStatement("SELECT * FROM " + FactSQLConnector.getAchievementsTable() + " WHERE `UUID`=? AND `SERVER`=? AND `NAME`=?");
			statement.setString(1, uuid.toString());
			statement.setString(2, server);
			statement.setString(3, achievement);
			
			ResultSet rs = statement.executeQuery();
			if (!rs.next()) return null;
			return rs.getString("PROGRESS");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	
	
	public static List<String[]> getAchievementList(UUID uuid, String server) {
		
		List<String[]> list = new ArrayList<String[]>();
		
		try {
			
			PreparedStatement statement = FactSQLConnector.getMysql().getConnection()
					.prepareStatement("SELECT * FROM " + FactSQLConnector.getAchievementsTable() + " WHERE `UUID`=? AND `SERVER`=?");
			statement.setString(1, uuid.toString());
			statement.setString(2, server);
			
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				String name = rs.getString("NAME");
				String progress = rs.getString("PROGRESS");
				list.add(new String[] {name, progress});	
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
}
