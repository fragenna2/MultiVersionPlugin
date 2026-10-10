package com.github.fragenna2.multiversion.v1_8.npc;

import com.github.fragenna2.multiversion.api.NPC;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class NPC_v1_8 implements NPC {

    private final String name;

    private final Location location;

    private final EntityPlayer entityPlayer;
    private final UUID uuid;

    public NPC_v1_8(String name, Location location) {
        this.name = name;
        this.location = location;

        final WorldServer nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        final MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();

        this.uuid = UUID.randomUUID();
        GameProfile profile = new GameProfile(uuid, name);

        this.entityPlayer = new EntityPlayer(nmsServer, nmsWorld, profile, new PlayerInteractManager(nmsWorld));
        this.entityPlayer.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        spawn();
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void spawn() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getWorld() == location.getWorld()) {
                show(player);
            }
        }
    }

    @Override
    public void show(Player player) {
        if (this.entityPlayer == null) return;
        if (player == null || !player.isOnline()) return;

        sendPacket(player, new PacketPlayOutPlayerInfo(
                PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER,
                this.entityPlayer
        ));

        sendPacket(player, new PacketPlayOutNamedEntitySpawn(this.entityPlayer));

        byte yawByte = (byte) (this.entityPlayer.yaw * 256.0F / 360.0F);
        sendPacket(player, new PacketPlayOutEntityHeadRotation(
                this.entityPlayer,
                yawByte
        ));

        Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("MultiVersion"), () -> {
            sendPacket(player, new PacketPlayOutPlayerInfo(
                    PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER,
                    this.entityPlayer
            ));
        }, 20L);
    }

    @Override
    public void hide(Player player) {
        if (this.entityPlayer == null) return;
        if (player == null || !player.isOnline()) return;

        sendPacket(player, new PacketPlayOutEntityDestroy(
                this.entityPlayer.getId()
        ));
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public void delete() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getWorld() == location.getWorld()) {
                hide(player);
            }
        }
    }

    private void sendPacket(Player player, Packet<?> packet) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(packet);
    }
}
