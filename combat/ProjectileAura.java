package missu.amunix.modules.combat;
import missu.amunix.core.*;
import missu.amunix.settings.*;
import missu.amunix.utils.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.Hand;
import java.util.*;
public class ProjectileAura extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private final NumberSetting range = new NumberSetting("Range", 6, 1, 20, 0.5);
    private final ModeSetting projectile = new ModeSetting("Projectile", new String[]{"Snowball","Egg","Bow","Rod","ExpBottle"},"Snowball");
    private final NumberSetting delay = new NumberSetting("Delay ms", 200, 50, 2000, 50);
    private final BooleanSetting playersOnly = new BooleanSetting("Players Only", true);
    private final BooleanSetting invisible = new BooleanSetting("Invisibles", false);
    private final ModeSetting rotation = new ModeSetting("Rotation", new String[]{"None","Smooth"},"Smooth");
    private final NumberSetting rotSpeed = new NumberSetting("Rot Speed", 30, 5, 90, 1);
    private final BooleanSetting autoSwitch = new BooleanSetting("Auto Switch", true);
    private final BooleanSetting swingBack = new BooleanSetting("Swing Back", true);
    private LivingEntity target;
    private long lastThrow;
    private int prevSlot = -1;
    private final Random random = new Random();
    public ProjectileAura() { super("ProjectileAura","Throws projectiles at nearby targets.",ModuleCategory.COMBAT); addSettings(toggle,range,projectile,delay,playersOnly,invisible,rotation,rotSpeed,autoSwitch,swingBack); }
    @Override public void onDisable() { target = null; if (prevSlot >= 0) { MinecraftClient.getInstance().player.getInventory().selectedSlot = prevSlot; prevSlot = -1; } }
    @Override public void onTick() {
        if (!toggle.getValue()) return;
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null || mc.player.isDead()) { target = null; return; }
        target = findTarget(mc);
        if (target == null) return;
        long now = System.currentTimeMillis();
        if (now - lastThrow < delay.getValue()) return;
        String proj = projectile.getValue();
        int slot = findProjectileSlot(mc, proj);
        if (slot < 0) return;
        if (autoSwitch.getValue()) { prevSlot = mc.player.getInventory().selectedSlot; mc.player.getInventory().selectedSlot = slot; }
        if (!rotation.getValue().equals("None")) {
            float[] angles = RotationUtil.calculateAngles(mc.player, target);
            float yaw = RotationUtil.smoothAngle(mc.player.getYaw(), angles[0], rotSpeed.getValue());
            float pitch = RotationUtil.smoothAngle(mc.player.getPitch(), angles[1], rotSpeed.getValue() / 2);
            mc.player.setYaw(yaw); mc.player.setPitch(pitch);
        }
        if (proj.equals("Bow")) {
            mc.options.useKey.setPressed(true);
            if (mc.player.getItemUseTime() >= 3) {
                mc.interactionManager.stopUsingItem(mc.player);
                mc.options.useKey.setPressed(false);
            }
        } else if (proj.equals("Rod")) {
            mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
        } else {
            mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
        }
        lastThrow = now;
        if (swingBack.getValue() && autoSwitch.getValue() && prevSlot >= 0) {
            mc.player.getInventory().selectedSlot = prevSlot; prevSlot = -1;
        }
    }
    private LivingEntity findTarget(MinecraftClient mc) {
        LivingEntity best = null;
        double bestDist = range.getValue();
        for (Entity e : mc.world.getEntities()) {
            if (!(e instanceof LivingEntity le) || le == mc.player || !le.isAlive()) continue;
            if (playersOnly.getValue() && !(le instanceof PlayerEntity)) continue;
            if (!invisible.getValue() && le.isInvisible()) continue;
            double dist = mc.player.distanceTo(le);
            if (dist < bestDist) { bestDist = dist; best = le; }
        }
        return best;
    }
    private int findProjectileSlot(MinecraftClient mc, String type) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.isEmpty()) continue;
            Item item = stack.getItem();
            switch (type) {
                case "Snowball": if (item instanceof SnowballItem) return i; break;
                case "Egg": if (item instanceof EggItem) return i; break;
                case "Bow": if (item instanceof BowItem) return i; break;
                case "Rod": if (item instanceof FishingRodItem) return i; break;
                case "ExpBottle": if (item instanceof ExperienceBottleItem) return i; break;
            }
        }
        return -1;
    }
}
