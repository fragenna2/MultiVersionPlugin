package com.github.fragenna2.multiversion.v1_21;


import com.github.fragenna2.multiversion.api.NMSHandler;
import com.github.fragenna2.multiversion.api.NPCHandler;
import org.bukkit.entity.Player;

public class Handler_v_1_21_11 implements NMSHandler {

    private final NPCHandler npcHandler = new NPCHandler_v_1_21_11();

    @Override
    public NPCHandler getNpcHandler() {
        return npcHandler;
    }

    @Override
    public void test(Player player) {
        player.sendRichMessage("<green>Welcome from version 1.21.11");
    }
}