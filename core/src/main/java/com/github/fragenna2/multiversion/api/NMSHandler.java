package com.github.fragenna2.multiversion.api;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public interface NMSHandler {

    NPCHandler getNpcHandler();

    void test(Player player);
}
