package com.github.fragenna2.multiversion.v26_2.npc;

import com.github.fragenna2.multiversion.api.NPC;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class NPC_v26_2 implements NPC {

    private final String name;
    private final Location location;
    private final UUID uuid;
    private final ServerPlayer serverPlayer;

    public NPC_v26_2(String name, Location location) {
        this.name = name;
        this.location = location;
        this.uuid = UUID.randomUUID();

        GameProfile profile = new GameProfile(this.uuid, name);

        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        ServerLevel level = ((CraftWorld) location.getWorld()).getHandle();

        this.serverPlayer = new ServerPlayer(server, level, profile, ClientInformation.createDefault());
        this.serverPlayer.setPos(location.getX(), location.getY(), location.getZ());
        this.serverPlayer.setXRot(location.getPitch());
        this.serverPlayer.setYRot(location.getYaw());

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
            if (player.getWorld().equals(location.getWorld())) {
                show(player);
            }
        }
    }

    @Override
    public void show(Player player) {

        ClientboundPlayerInfoUpdatePacket.Entry entry = new ClientboundPlayerInfoUpdatePacket.Entry(
            serverPlayer.getUUID(),
            serverPlayer.getGameProfile(),
            false,
            0,
            GameType.CREATIVE,
                null,
                true,
                0,
                null
        );

        sendPacket(player, new ClientboundPlayerInfoUpdatePacket(
                EnumSet.of(
                        ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER,
                        ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LISTED
                ),
                List.of(entry)
        ));

        //spawns the entity
        sendPacket(player, new ClientboundAddEntityPacket(
                serverPlayer.getId(),
                serverPlayer.getUUID(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getPitch(),
                location.getYaw(),
                serverPlayer.getType(),
                0,
                Vec3.ZERO,
                location.getYaw()
        ));

        //Synchronization between head and body
        sendPacket(player, new ClientboundRotateHeadPacket(serverPlayer, (byte) (serverPlayer.getYRot() * 256 / 360)));
        sendPacket(player, new ClientboundMoveEntityPacket.Rot(
                serverPlayer.getId(),
                (byte) (serverPlayer.getYRot() * 256 / 360),
                (byte) (serverPlayer.getXRot() * 256 / 360),
                true
        ));

        Bukkit.getScheduler().runTaskLater(
                Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("MultiVersion")),
                () -> sendPacket(player, new ClientboundPlayerInfoRemovePacket(List.of(serverPlayer.getUUID()))),
                20L
        );

    }

    @Override
    public void hide(Player player) {

        sendPacket(player, new ClientboundRemoveEntitiesPacket(serverPlayer.getId()));
        sendPacket(player, new ClientboundPlayerInfoRemovePacket(List.of(getUUID())));
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public void delete() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getWorld().equals(getLocation().getWorld())) {
                hide(player);
            }
        }
    }


    private void sendPacket(Player player, Packet<?> packet) {
        ((CraftPlayer) player).getHandle().connection.connection.send(packet);
    }
}
