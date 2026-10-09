package com.github.fragenna2.multiversion.v1_21;


import com.github.fragenna2.multiversion.api.NMSHandler;
import org.bukkit.entity.Player;

public class Handler_v_1_21_11 implements NMSHandler {


    @Override
    public void spawnNpc(Player player) {

    }

    @Override
    public void showNpc(Player player) {

    }

    @Override
    public void hide(Player player) {

    }

    @Override
    public void destroyNpc(int id) {

    }

    @Override
    public void test(Player player) {
        player.sendRichMessage("<green>Welcome from version 1.21.11");
    }
}