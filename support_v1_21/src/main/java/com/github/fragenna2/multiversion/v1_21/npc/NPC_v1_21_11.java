package com.github.fragenna2.multiversion.v1_21.npc;

import com.github.fragenna2.multiversion.api.NPC;
import com.mojang.authlib.GameProfile;
import io.netty.channel.Channel;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class NPC_v1_21_11 implements NPC {

    private final String name;
    private final Location location;
    private final UUID uuid = UUID.randomUUID();

    private final ServerPlayer serverPlayer;

    public NPC_v1_21_11 (String name, Location location) {
        this.name = name;
        this.location = location;

        if (location == null) {
            throw new IllegalStateException("You must provide a valid location to the NPC");
        }

        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        ServerLevel serverLevel = ((CraftWorld) location.getWorld()).getHandle();
        GameProfile profile = new GameProfile(uuid, name);

        this.serverPlayer = new ServerPlayer(server, serverLevel, profile, ClientInformation.createDefault());
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
        for (Player player :  Bukkit.getOnlinePlayers()) {
            if (player.getWorld() == location.getWorld()) {
                show(player);
            }
        }
    }

    @Override
    public void show(Player player) {
        sendPacket(player, new ClientboundPlayerInfoUpdatePacket(
                ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER,
                serverPlayer
        ));

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
            if (player.getWorld() == location.getWorld()) {
                hide(player);
            }
        }
    }

    private void sendPacket(Player player, Packet<?> packet) {
        ServerPlayer playerHandle = ((CraftPlayer) player).getHandle();
        Channel channel = playerHandle.connection.connection.channel;

        channel.writeAndFlush(packet);
    }
}
