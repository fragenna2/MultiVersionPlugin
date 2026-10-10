package com.github.fragenna2.multiversion.v1_8;

import com.github.fragenna2.multiversion.api.NPC;
import com.github.fragenna2.multiversion.api.NPCHandler;
import com.github.fragenna2.multiversion.v1_8.npc.NPC_v1_8;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class NPCHandler_v1_8 implements NPCHandler {

    @Override
    public NPC spawnNpc(String name, Location location) {
        return new NPC_v1_8(name, location);
    }

    @Override
    public void removeNpc(NPC npc) {
        if (npc == null) return;
        npc.delete();
    }

    @Override
    public void showToPlayer(Player player, NPC npc) {
        if (player == null || npc == null) return;
        npc.show(player);
    }

    @Override
    public void hideFromPlayer(Player player, NPC npc) {
        if (player == null || npc == null) return;
        npc.hide(player);
    }
}
