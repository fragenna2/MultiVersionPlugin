package com.github.fragenna2.multiversion.v1_8;

import com.github.fragenna2.multiversion.api.NMSHandler;
import com.github.fragenna2.multiversion.api.NPC;
import com.github.fragenna2.multiversion.api.NPCHandler;
import com.github.fragenna2.multiversion.v1_8.npc.NPC_v1_8;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Handler_v1_8 implements NMSHandler {

    private final NPCHandler_v1_8 npcHandler = new NPCHandler_v1_8();

    @Override
    public NPCHandler getNpcHandler() {
        return npcHandler;
    }

    @Override
    public void test(Player player) {
        player.sendMessage("§a[1.8.8] Welcome!");
    }
}