package com.hakan.core.npc.entity;

import com.hakan.core.HCore;
import com.hakan.core.npc.HNPC;
import com.hakan.core.npc.pathfinder.PathfinderEntity_v1_9_R2;
import com.hakan.core.skin.Skin;
import com.hakan.core.utils.Validate;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_9_R2.DataWatcher;
import net.minecraft.server.v1_9_R2.DataWatcherObject;
import net.minecraft.server.v1_9_R2.DataWatcherRegistry;
import net.minecraft.server.v1_9_R2.EntityPlayer;
import net.minecraft.server.v1_9_R2.EnumItemSlot;
import net.minecraft.server.v1_9_R2.MinecraftServer;
import net.minecraft.server.v1_9_R2.PacketPlayOutEntity;
import net.minecraft.server.v1_9_R2.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_9_R2.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_9_R2.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_9_R2.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_9_R2.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_9_R2.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_9_R2.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_9_R2.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_9_R2.PlayerInteractManager;
import net.minecraft.server.v1_9_R2.Scoreboard;
import net.minecraft.server.v1_9_R2.ScoreboardTeam;
import net.minecraft.server.v1_9_R2.ScoreboardTeamBase;
import net.minecraft.server.v1_9_R2.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R2.CraftServer;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

/**
 * {@inheritDoc}
 */
public final class HNpcEntity_v1_9_R2 implements HNpcEntity {

    /**
     * Creates nms player.
     *
     * @param npc HNPC instance.
     * @return nms player.
     */
    @Nonnull
    private static EntityPlayer createEntityPlayer(@Nonnull HNPC npc) {
        Validate.notNull(npc, "npc cannot be null");

        Skin skin = npc.getSkin();
        Location location = npc.getLocation();
        WorldServer world = ((CraftWorld) npc.getWorld()).getHandle();
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), npc.getID());
        PlayerInteractManager manager = new PlayerInteractManager(((CraftWorld) npc.getWorld()).getHandle());

        Property property = new Property("textures", skin.getTexture(), skin.getSignature());
        gameProfile.getProperties().clear();
        gameProfile.getProperties().put("textures", property);

        EntityPlayer entityPlayer = new EntityPlayer(server, world, gameProfile, manager);
        entityPlayer.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        entityPlayer.setInvisible(false);
        entityPlayer.setHealth(77.21f);
        return entityPlayer;
    }


    private final HNPC hnpc;
    private final EntityPlayer nmsPlayer;
    private final ScoreboardTeam scoreboard;

    /**
     * {@inheritDoc}
     */
    public HNpcEntity_v1_9_R2(@Nonnull HNPC hnpc) {
        this.hnpc = Validate.notNull(hnpc, "hnpc cannot be null!");
        this.nmsPlayer = createEntityPlayer(hnpc);
        this.scoreboard = new ScoreboardTeam(new Scoreboard(), hnpc.getID());
        this.scoreboard.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);
        this.scoreboard.getPlayerNameSet().add(this.nmsPlayer.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getID() {
        return this.nmsPlayer.getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void walk(double speed, @Nonnull Location to, @Nonnull Runnable runnable) {
        PathfinderEntity_v1_9_R2.create(this.hnpc.getLocation(), to, speed,
                (custom) -> this.hnpc.setLocation(custom.getBukkitEntity().getLocation()),
                (custom) -> {
                    custom.dead = true;
                    this.hnpc.setLocation(to);
                    runnable.run();
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateLocation(@Nonnull List<Player> players) {
        this.updateHeadRotation(Validate.notNull(players, "players cannot be null!"));
        HCore.sendPacket(players, new PacketPlayOutEntityTeleport(this.nmsPlayer));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateHeadRotation(@Nonnull List<Player> players) {
        Validate.notNull(players, "players cannot be null!");

        Location location = this.hnpc.getLocation();
        this.nmsPlayer.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        float yaw = Math.round(location.getYaw() % 360f * (256f / 360f));
        float pitch = Math.round(location.getPitch() % 360f * (256f / 360f));
        HCore.sendPacket(players, new PacketPlayOutEntityHeadRotation(this.nmsPlayer, (byte) (location.getYaw() * (256f / 360f))),
                new PacketPlayOutEntity.PacketPlayOutEntityLook(this.getID(), (byte) yaw, (byte) pitch, false));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateSkin(@Nonnull List<Player> players) {
        Validate.notNull(players, "players cannot be null!");

        this.hide(players);

        GameProfile gameProfile = this.nmsPlayer.getProfile();
        gameProfile.getProperties().get("textures").clear();
        gameProfile.getProperties().put("textures", new Property("textures", this.hnpc.getSkin().getTexture(), this.hnpc.getSkin().getSignature()));

        this.show(players);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateEquipments(@Nonnull List<Player> players) {
        Validate.notNull(players, "players cannot be null!");

        if (this.hnpc.getEquipments().size() == 0)
            return;

        this.hnpc.getEquipments().forEach((slot, item) -> HCore.sendPacket(players,
                new PacketPlayOutEntityEquipment(this.getID(), EnumItemSlot.valueOf(slot.name()), CraftItemStack.asNMSCopy(item))));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show(@Nonnull List<Player> players) {
        Validate.notNull(players, "players cannot be null!");

        GameProfile gameProfile = this.nmsPlayer.getProfile();
        DataWatcher dataWatcher = this.nmsPlayer.getDataWatcher();
        gameProfile.getProperties().get("textures").clear();
        gameProfile.getProperties().put("textures", new Property("textures", this.hnpc.getSkin().getTexture(), this.hnpc.getSkin().getSignature()));
        dataWatcher.set(new DataWatcherObject<>(12, DataWatcherRegistry.a), (byte) 127);

        players.forEach(player -> this.scoreboard.getPlayerNameSet().add(player.getName()));
        HCore.sendPacket(players,
                new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, this.nmsPlayer),
                new PacketPlayOutNamedEntitySpawn(this.nmsPlayer),
                new PacketPlayOutEntityMetadata(this.getID(), dataWatcher, true),
                new PacketPlayOutScoreboardTeam(this.scoreboard, 0));
        HCore.asyncScheduler().after(5)
                .run(() -> HCore.sendPacket(players, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, this.nmsPlayer)));

        HCore.syncScheduler().after(2)
                .run(() -> this.updateLocation(players));
        this.updateEquipments(players);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hide(@Nonnull List<Player> players) {
        Validate.notNull(players, "players cannot be null!");

        players.forEach(player -> this.scoreboard.getPlayerNameSet().remove(player.getName()));
        HCore.sendPacket(players, new PacketPlayOutEntityDestroy(this.getID()),
                new PacketPlayOutScoreboardTeam(this.scoreboard, 0));
    }
}