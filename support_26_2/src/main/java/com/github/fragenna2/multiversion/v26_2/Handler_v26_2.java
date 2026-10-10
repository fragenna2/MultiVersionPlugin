package com.github.fragenna2.multiversion.v26_2;

import com.github.fragenna2.multiversion.api.NMSHandler;
import com.github.fragenna2.multiversion.api.NPC;
import com.github.fragenna2.multiversion.api.NPCHandler;
import com.github.fragenna2.multiversion.v26_2.npc.NPC_v26_2;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Handler_v26_2 implements NMSHandler {


    @Override
    public NPCHandler getNpcHandler() {
        return null;
    }

    @Override
    public void test(org.bukkit.entity.Player player) {
        player.sendRichMessage("<green>Version: 26.2");
    }
}