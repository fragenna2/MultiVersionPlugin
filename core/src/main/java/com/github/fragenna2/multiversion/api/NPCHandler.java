package com.github.fragenna2.multiversion.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface NPCHandler {

    NPC spawnNpc(String name, Location location);
    void removeNpc(NPC npc);

    void showToPlayer(Player player, NPC npc);
    void hideFromPlayer(Player player, NPC npc);

}
