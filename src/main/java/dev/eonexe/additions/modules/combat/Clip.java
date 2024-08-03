package dev.eonexe.additions.modules.combat;

import dev.eonexe.additions.Additions;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.network.PacketUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.NetworkUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Clip extends Module {

    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();
    private final Setting<ClipMode> mode = sgGeneral.add(new EnumSetting.Builder<ClipMode>().name("Clip Mode").defaultValue(ClipMode.Corner).build());
    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder().name("Delay").min(1).max(10).defaultValue(5).build());
    private final Setting<Boolean> disable = sgGeneral.add(new BoolSetting.Builder().name("Disable").defaultValue(false).build());
    private final Setting<Integer> updates = sgGeneral.add(new IntSetting.Builder().name("Updates").defaultValue(10).min(1).max(30).build());

    public Clip() {
        super(Additions.CATEGORY, "Clip", "Clips the player into blocks.");
    }

    public BlockPos pos;
    int disableTime = 0;

    private enum ClipMode {
        Corner, FiveB, AutoCentre
    }

    @EventHandler
    public void onUpdate(TickEvent.Pre event) {
        if (mc.world == null || mc.player == null) {
            return;
        }
        if (!this.noMovementKeys()) {
            toggle();
            return;
        }
        switch (mode.get()) {
            case AutoCentre:
                try {
                    Vec3d setCentre = new Vec3d(pos.getX() + 0.5, mc.player.getY(), pos.getZ() + 0.5);

                    mc.player.setVelocity(Vec3d.ZERO);

                    mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(setCentre.x, setCentre.y, setCentre.z, true));
                    mc.player.setPosition(setCentre.x, setCentre.y, setCentre.z);
                } catch (Exception e2) {
                    System.out.println(2);
                }
                break;
            case Corner:
                if (mc.world.getBlockCollisions(mc.player, mc.player.getBoundingBox().expand(0.01, 0, 0.01)).hashCode() < 2) {
                    mc.player.setPosition(roundToClosest(mc.player.getX(), Math.floor(mc.player.getX()) + 0.301, Math.floor(mc.player.getX()) + 0.699), mc.player.getY(), roundToClosest(mc.player.getZ(), Math.floor(mc.player.getZ()) + 0.301, Math.floor(mc.player.getZ()) + 0.699));

                } else if (mc.getTickDelta() % delay.get() == 0) {
                    mc.player.setPosition(mc.player.getX() + MathHelper.clamp(roundToClosest(mc.player.getX(), Math.floor(mc.player.getX()) + 0.241, Math.floor(mc.player.getX()) + 0.759) - mc.player.getX(), -0.03, 0.03), mc.player.getY(), mc.player.getZ() + MathHelper.clamp(roundToClosest(mc.player.getZ(), Math.floor(mc.player.getZ()) + 0.241, Math.floor(mc.player.getZ()) + 0.759) - mc.player.getZ(), -0.03, 0.03));
                    mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), true));
                    mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(roundToClosest(mc.player.getX(), Math.floor(mc.player.getX()) + 0.23, Math.floor(mc.player.getX()) + 0.77), mc.player.getY(), roundToClosest(mc.player.getZ(), Math.floor(mc.player.getZ()) + 0.23, Math.floor(mc.player.getZ()) + 0.77), true));
                }
                break;

            case FiveB:
                mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() - 0.0042123, mc.player.getZ(), mc.player.groundCollision));
                mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() - 0.02141, mc.player.getZ(), mc.player.groundCollision));
                mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() - 0.097421, mc.player.getZ(), mc.player.groundCollision));
                // https://github.com/WMSGaming/Abstract-1.12.2/blob/master/src/main/java/com/wms/abstractclient/module/modules/exploit/CornerClip.java
                break;
        }

        disableTime++;
        if (disable.get()) {
            if (disableTime >= updates.get()) {
                toggle();
            }
        }
    }

    public static double roundToClosest(double num, double low, double high) {
        double d2 = high - num;
        double d1 = num - low;
        return (d2 > d1 ? low : high);
    }

    @Override
    public void onDeactivate() {
        disableTime = 0;
    }

    public boolean noMovementKeys()
    {
        return !mc.options.forwardKey.isPressed()
            && !mc.options.backKey.isPressed()
            && !mc.options.rightKey.isPressed()
            && !mc.options.leftKey.isPressed();
    }
}
