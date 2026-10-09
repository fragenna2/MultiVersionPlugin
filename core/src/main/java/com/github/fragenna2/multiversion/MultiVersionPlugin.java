package com.github.fragenna2.multiversion;

import com.github.fragenna2.multiversion.api.NMSHandler;
import com.github.fragenna2.multiversion.commands.SpawnNpc;
import com.github.fragenna2.multiversion.listeners.JoinEvent;
import com.github.fragenna2.multiversion.utils.VersionUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;

public class MultiVersionPlugin extends JavaPlugin {

    private NMSHandler nmsHandler;

    @Override
    public void onEnable() {
        getLogger().info("Welcome!");
        if (!setupNms()) {
            getLogger().warning("Couldn't initialize the NMS");
            return;
        }

        getServer().getPluginManager().registerEvents(new JoinEvent(this.nmsHandler), this);
        getCommand("spawnNpc").setExecutor(new SpawnNpc(this.nmsHandler));
    }

    private boolean setupNms() {
        String serverVersion = Bukkit.getBukkitVersion();
        int javaMajorVersion = VersionUtils.getJavaMajorVersion(serverVersion);

        if (serverVersion.contains("26.2")) {
            if (javaMajorVersion < 25) {
                getLogger().severe("This plugin requires minecraft server's version to be 26.2");
                getLogger().severe("This server is using java: " + System.getProperty("java.version"));
                return false;
            }

            return loadHandler("com.github.fragenna2.multiversion.v26_2.Handler_v26_2");
        }

        if (serverVersion.contains("1.8.8")) {
            if (javaMajorVersion > 8) {
                getLogger().severe("This plugin requires minecraft server's version to be 8");
                getLogger().severe("This server is using java: " + System.getProperty("java.version"));
                return false;
            }


            return loadHandler("com.github.fragenna2.multiversion.v1_8.Handler_v1_8");
        }

        if (serverVersion.contains("1.21")) {
//            if (javaMajorVersion < 21) {
//                getLogger().severe("This plugin required java version to be not newer of java 21");
//                getLogger().severe("This server is using java: " + System.getProperty("java.version"));
//                return false;
//            }

            return loadHandler("com.github.fragenna2.multiversion.v1_21.Handler_v_1_21_11");
        }

        return false;
    }

    private boolean loadHandler(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            this.nmsHandler = (NMSHandler) clazz.getDeclaredConstructor().newInstance();
            return true;
        } catch (ClassNotFoundException e) {
            getLogger().severe("Error while initializing the nms handler: " + className);
            getLogger().severe(e.getMessage());
            return false;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    public NMSHandler getNmsHandler() {
        return nmsHandler;
    }
}