package com.hakan.core.packet.player;

import com.hakan.core.packet.event.PacketEvent;
import com.hakan.core.packet.utils.PacketUtils;
import com.hakan.core.utils.Validate;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * {@inheritDoc}
 */
public final class HPacketPlayer_v1_19_1_R1 extends HPacketPlayer {

    private final PlayerConnection connection;

    /**
     * {@inheritDoc}
     */
    public HPacketPlayer_v1_19_1_R1(@Nonnull Player player) {
        super(player);
        this.connection = ((CraftPlayer) player).getHandle().b;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(@Nonnull Object... packets) {
        Validate.notNull(packets, "packets cannot be null!");

        if (!super.player.isOnline())
            return;
        for (Object packet : packets)
            this.connection.a((Packet<?>) packet);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void register() {
        try {
            this.pipeline = this.connection.b.m.pipeline().addBefore("packet_handler", CHANNEL + super.player.getUniqueId(), new ChannelDuplexHandler() {
                @Override
                public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
                    PacketEvent packetEvent = new PacketEvent(player, msg, PacketEvent.Type.READ);
                    PacketUtils.callEvent(packetEvent);

                    if (packetEvent.isCancelled()) return;
                    super.channelRead(channelHandlerContext, msg);
                }

                @Override
                public void write(ChannelHandlerContext channelHandlerContext, Object o, ChannelPromise channelPromise) throws Exception {
                    PacketEvent packetEvent = new PacketEvent(player, o, PacketEvent.Type.WRITE);
                    PacketUtils.callEvent(packetEvent);

                    if (packetEvent.isCancelled()) return;
                    super.write(channelHandlerContext, o, channelPromise);
                }
            });
        } catch (Exception ignored) {
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregister() {
        if (this.pipeline != null && this.pipeline.get(CHANNEL) != null)
            this.pipeline.remove(CHANNEL);
    }
}