package fun.ksmc.extend.coi;

import net.coreprotect.database.Database;
import net.coreprotect.model.Config;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        Bukkit.getScheduler().runTaskLater(this, () -> {
            Connection conn = Database.getConnection(false);
            execPs(conn,"sign");
            execPs(conn,"block");
            execPs(conn,"skull");
            execPs(conn,"container");
            execPs(conn,"command");
            execPs(conn,"session");
            execPs(conn,"entity");
            execPs(conn,"chat");
            closeConnection(conn);
        }, 20L);
    }

    private void execPs(Connection conn, String name) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(String.format("delete from %s where time<?", Config.prefix+name));
            ps.setLong(1, System.currentTimeMillis()/1000-7*24*60*60);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement(ps);
        }

    }
    public void closePreparedStatement(PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
