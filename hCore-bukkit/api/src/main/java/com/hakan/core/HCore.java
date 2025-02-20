package com.hakan.core;

import com.hakan.core.configuration.ConfigHandler;
import com.hakan.core.configuration.containers.ConfigContainer;
import com.hakan.core.item.ItemBuilder;
import com.hakan.core.item.nbt.NbtManager;
import com.hakan.core.item.skull.SkullBuilder;
import com.hakan.core.listener.ListenerAdapter;
import com.hakan.core.protocol.ProtocolVersion;
import com.hakan.core.scheduler.Scheduler;
import com.hakan.core.ui.Gui;
import com.hakan.core.ui.GuiHandler;
import com.hakan.core.ui.inventory.InventoryGui;
import com.hakan.core.ui.inventory.builder.InventoryBuilder;
import com.hakan.core.utils.Serializer;
import com.hakan.core.utils.Validate;
import com.hakan.core.utils.hooks.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Main class of this core.
 * You can reach all APIs from this
 * class as static.
 */
@SuppressWarnings({"unused"})
public final class HCore {

    private static JavaPlugin INSTANCE;
    private static ProtocolVersion VERSION;

    /**
     * Gets instance.
     *
     * @return Instance.
     */
    @Nonnull
    public static JavaPlugin getInstance() {
        return INSTANCE;
    }

    /**
     * Sets instance plugin of hCore.
     *
     * @param plugin Instance.
     */
    public static void setInstance(@Nonnull JavaPlugin plugin) {
        INSTANCE = Validate.notNull(plugin, "instance cannot be null!");
        VERSION = ProtocolVersion.getCurrentVersion();
    }

    /**
     * Initializes all APIs.
     *
     * @param plugin Instance of the main class.
     */
    public static void initialize(@Nonnull JavaPlugin plugin) {
        if (INSTANCE != null) return;

        HCore.setInstance(plugin);
        Metrics.initialize(plugin);

        GuiHandler.initialize();
        ItemBuilder.initialize();
    }


    /*
    OTHERS
     */

    /**
     * Gets the protocol version
     * of the current server.
     *
     * @return Protocol version.
     */
    @Nonnull
    public static ProtocolVersion getProtocolVersion() {
        return VERSION;
    }

    /**
     * Gets version string (example: v1_8_R3)
     *
     * @return Version string.
     */
    @Nonnull
    public static String getVersionString() {
        return VERSION.getKey();
    }


    /*
    SCHEDULER
     */

    /**
     * Creates scheduler.
     *
     * @param async Async status.
     * @return Scheduler.
     */
    @Nonnull
    public static Scheduler scheduler(boolean async) {
        return new Scheduler(INSTANCE, async);
    }

    /**
     * Creates async scheduler.
     *
     * @return Scheduler.
     */
    @Nonnull
    public static Scheduler asyncScheduler() {
        return HCore.scheduler(true);
    }

    /**
     * Creates sync scheduler.
     *
     * @return Scheduler.
     */
    @Nonnull
    public static Scheduler syncScheduler() {
        return HCore.scheduler(false);
    }


    /*
    UI API
     */

    /**
     * Gets content as safe.
     *
     * @return Content.
     */
    @Nonnull
    public static Map<UUID, Gui> getGUIContentSafe() {
        return GuiHandler.getContentSafe();
    }

    /**
     * Gets content.
     *
     * @return Content.
     */
    @Nonnull
    public static Map<UUID, Gui> getGUIContent() {
        return GuiHandler.getContent();
    }

    /**
     * Gets values as safe.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<Gui> getGUIValuesSafe() {
        return GuiHandler.getValuesSafe();
    }

    /**
     * Gets values.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<Gui> getGUIValues() {
        return GuiHandler.getValues();
    }

    /**
     * Finds InventoryGui by player.
     *
     * @param player Player.
     * @return InventoryGui as optional.
     */
    @Nonnull
    public static Optional<Gui> findGUIByPlayer(@Nonnull Player player) {
        return GuiHandler.findByPlayer(player);
    }

    /**
     * Gets InventoryGui by player.
     *
     * @param player Player.
     * @return InventoryGui.
     */
    @Nonnull
    public static Gui getGUIByPlayer(@Nonnull Player player) {
        return GuiHandler.getByPlayer(player);
    }

    /**
     * Finds InventoryGui by UID.
     *
     * @param uid Player UID.
     * @return InventoryGui as optional.
     */
    @Nonnull
    public static Optional<Gui> findGUIByUID(@Nonnull UUID uid) {
        return GuiHandler.findByUID(uid);
    }

    /**
     * Gets InventoryGui by UID.
     *
     * @param uid Player UId.
     * @return InventoryGui.
     */
    @Nonnull
    public static Gui getGUIByUID(@Nonnull UUID uid) {
        return GuiHandler.getByUID(uid);
    }

    /**
     * Gets content.
     *
     * @return Content.
     */
    @Nonnull
    public static Map<UUID, InventoryGui> getInventoryContentSafe() {
        return GuiHandler.getInventoryContentSafe();
    }

    /**
     * Gets values as safe.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<InventoryGui> getInventoryValuesSafe() {
        return GuiHandler.getInventoryValuesSafe();
    }

    /**
     * Finds InventoryGui by player.
     *
     * @param player Player.
     * @return InventoryGui as optional.
     */
    @Nonnull
    public static Optional<InventoryGui> findInventoryByPlayer(@Nonnull Player player) {
        return GuiHandler.findInventoryByPlayer(player);
    }

    /**
     * Gets InventoryGui by player.
     *
     * @param player Player.
     * @return InventoryGui.
     */
    @Nonnull
    public static InventoryGui getInventoryByPlayer(@Nonnull Player player) {
        return GuiHandler.getInventoryByPlayer(player);
    }

    /**
     * Finds InventoryGui by UID.
     *
     * @param uid Player UID.
     * @return InventoryGui as optional.
     */
    @Nonnull
    public static Optional<InventoryGui> findInventoryByUID(@Nonnull UUID uid) {
        return GuiHandler.findInventoryByUID(uid);
    }

    /**
     * Gets InventoryGui by UID.
     *
     * @param uid Player UId.
     * @return InventoryGui.
     */
    @Nonnull
    public static InventoryGui getInventoryByUID(@Nonnull UUID uid) {
        return GuiHandler.getInventoryByUID(uid);
    }

    /**
     * Creates builder with ID.
     *
     * @param id ID.
     * @return InventoryBuilder.
     */
    @Nonnull
    public static InventoryBuilder inventoryBuilder(@Nonnull String id) {
        return GuiHandler.inventoryBuilder(id);
    }

    /*
    LISTENERS
     */

    /**
     * Registers listeners to server.
     *
     * @param listeners List of listeners.
     */
    public static void registerListeners(@Nonnull Listener... listeners) {
        Arrays.asList(Validate.notNull(listeners, "listeners cannot be null!"))
                .forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, INSTANCE));
    }

    /**
     * Registers listeners to server.
     *
     * @param eventClass Class of event.
     * @param <T>        Event type.
     * @return Listener.
     */
    @Nonnull
    public static <T extends Event> ListenerAdapter<T> registerEvent(@Nonnull Class<T> eventClass) {
        return new ListenerAdapter<>(eventClass);
    }


    /*
    PACKET
     */

    /**
     * Sends packet to players
     *
     * @param player Player.
     * @param packet Packet.
     */
    public static void sendPacket(@Nonnull Player player, @Nonnull Object packet) {
        HCore.sendPacket(player, new Object[]{packet});
    }

    /**
     * Sends packet to player list.
     *
     * @param players Player list.
     * @param packet  Packet.
     */
    public static void sendPacket(@Nonnull Collection<Player> players, @Nonnull Object packet) {
        HCore.sendPacket(players, new Object[]{packet});
    }

    /**
     * Sends packets to player list.
     *
     * @param players Player list.
     * @param packets Packets.
     */
    public static void sendPacket(@Nonnull Collection<Player> players, @Nonnull Object... packets) {
        Validate.notNull(players, "players cannot be null!").forEach(player -> HCore.sendPacket(player, packets));
    }

    /*
    ITEM
     */

    /**
     * Gets NbtManager object.
     *
     * @return NbtManager object.
     */
    @Nonnull
    public static NbtManager getNbtManager() {
        return ItemBuilder.getNbtManager();
    }

    /**
     * Creates new item stack builder.
     *
     * @param type Material.
     * @return New instance of ItemBuilder.
     */
    @Nonnull
    public static ItemBuilder itemBuilder(@Nonnull Material type) {
        return new ItemBuilder(type);
    }

    /**
     * Creates new item stack builder.
     *
     * @param type   Material.
     * @param amount Amount.
     * @return New instance of ItemBuilder.
     */
    @Nonnull
    public static ItemBuilder itemBuilder(@Nonnull Material type, int amount) {
        return new ItemBuilder(type, amount);
    }

    /**
     * Creates new item stack builder.
     *
     * @param type       Material.
     * @param amount     Amount.
     * @param durability Durability.
     * @return New instance of ItemBuilder.
     */
    @Nonnull
    public static ItemBuilder itemBuilder(@Nonnull Material type, int amount, short durability) {
        return new ItemBuilder(type, amount, durability);
    }

    /**
     * Creates new item stack builder.
     *
     * @param itemStack Item stack.
     * @return New instance of ItemBuilder.
     */
    @Nonnull
    public static ItemBuilder itemBuilder(@Nonnull ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }

    /**
     * Creates new item stack builder.
     *
     * @param itemBuilder Item builder.
     * @return New instance of ItemBuilder.
     */
    @Nonnull
    public static ItemBuilder itemBuilder(@Nonnull ItemBuilder itemBuilder) {
        return new ItemBuilder(itemBuilder);
    }

    /**
     * Creates new item stack builder.
     *
     * @return New instance of SkullBuilder.
     */
    @Nonnull
    public static SkullBuilder skullBuilder() {
        return new SkullBuilder();
    }

    /**
     * Creates new item stack builder.
     *
     * @param amount Amount.
     * @return New instance of SkullBuilder.
     */
    @Nonnull
    public static SkullBuilder skullBuilder(int amount) {
        return new SkullBuilder(amount);
    }

    /**
     * Creates new item stack builder.
     *
     * @param skullBuilder Skull builder.
     * @return New instance of SkullBuilder.
     */
    @Nonnull
    public static SkullBuilder skullBuilder(@Nonnull SkullBuilder skullBuilder) {
        return new SkullBuilder(skullBuilder);
    }

    /**
     * Creates new item stack builder.
     *
     * @param texture Texture of skull.
     * @return New instance of SkullBuilder.
     */
    @Nonnull
    public static SkullBuilder skullBuilder(@Nullable String texture) {
        return new SkullBuilder().texture(texture);
    }

    /**
     * Creates new item stack builder.
     *
     * @param owner Owner of texture.
     * @return New instance of SkullBuilder.
     */
    @Nonnull
    public static SkullBuilder skullBuilderByPlayer(@Nullable String owner) {
        return new SkullBuilder().textureByPlayer(owner);
    }

    /**
     * Creates new item stack builder.
     *
     * @param owner Owner of texture.
     * @return New instance of SkullBuilder.
     */
    @Nonnull
    public static SkullBuilder skullBuilderByPlayer(@Nullable Player owner) {
        return new SkullBuilder().textureByPlayer(owner);
    }

    /*
    CONFIGURATION
     */

    /**
     * Loads configuration container.
     *
     * @param configClass Config class.
     * @return Config container class.
     */
    @Nonnull
    public static <T extends ConfigContainer> T loadConfig(@Nonnull Object configClass) {
        return ConfigHandler.load(configClass);
    }

    /**
     * Loads config container.
     *
     * @param file Configuration container.
     * @return Configuration container.
     */
    @Nonnull
    public static <T extends ConfigContainer> T loadConfig(@Nonnull ConfigContainer file) {
        return ConfigHandler.load(file);
    }

    /**
     * Finds configuration container.
     *
     * @param path Configuration container path.
     * @return Configuration container.
     */
    @Nonnull
    public static Optional<ConfigContainer> findConfigByPath(@Nonnull String path) {
        return ConfigHandler.findByPath(path);
    }

    /**
     * Gets configuration file.
     *
     * @param path Configuration file container.
     * @return Configuration container.
     */
    @Nonnull
    public static ConfigContainer getConfigByPath(@Nonnull String path) {
        return ConfigHandler.getByPath(path);
    }

    /*
    SERIALIZER
     */

    /**
     * Serializes object.
     *
     * @param object Object.
     * @return Serialized string as optional.
     */
    @Nonnull
    public synchronized static Optional<String> serializeSafe(@Nonnull Object object) {
        return Serializer.serializeSafe(object);
    }

    /**
     * Serializes object.
     *
     * @param object Object.
     * @return Serialized string.
     */
    @Nonnull
    public synchronized static String serialize(@Nonnull Object object) {
        return Serializer.serialize(object);
    }

    /**
     * Deserializes object.
     *
     * @param serializedText Text that want to deserialize.
     * @param clazz          Object type class.
     * @param <T>            Object type.
     * @return Deserialized object as optional.
     */
    @Nonnull
    public synchronized static <T> Optional<T> deserializeSafe(@Nonnull String serializedText, @Nonnull Class<T> clazz) {
        return Serializer.deserializeSafe(serializedText, clazz);
    }

    /**
     * Deserializes object.
     *
     * @param serializedText Text that want to deserialize.
     * @param clazz          Object type class.
     * @param <T>            Object type.
     * @return Deserialized object as optional.
     */
    @Nonnull
    public synchronized static <T> T deserialize(@Nonnull String serializedText, @Nonnull Class<T> clazz) {
        return Serializer.deserialize(serializedText, clazz);
    }
}
