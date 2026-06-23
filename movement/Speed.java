package missu.amunix.modules.movement;
import missu.amunix.core.*;
import missu.amunix.settings.*;
import missu.amunix.utils.KeyBindingUtil;
import net.minecraft.client.MinecraftClient;
public class Speed extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private final ModeSetting mode = new ModeSetting("Mode", new String[]{"Vanilla","NCP","Hypixel","Ground","Legit"},"Hypixel");
    private final NumberSetting speed = new NumberSetting("Speed", 1.5, 0.1, 10, 0.1);
    private final BooleanSetting autoJump = new BooleanSetting("Auto Jump", false);
    private final BooleanSetting onlyOnGround = new BooleanSetting("Only On Ground", true);
    private final BooleanSetting stopOnSneak = new BooleanSetting("Stop On Sneak", true);
    private final NumberSetting timer = new NumberSetting("Timer", 1.0, 0.1, 5.0, 0.05);
    private int tickCounter;
    private boolean jumped;
    public Speed() { super("Speed","Increases movement speed.",ModuleCategory.MOVEMENT); addSettings(toggle,mode,speed,autoJump,onlyOnGround,stopOnSneak,timer); }
    @Override public void onDisable() { tickCounter = 0; jumped = false; }
    @Override public void onTick() {
        if (!toggle.getValue()) return;
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.player.isDead()) return;
        if (mc.player.isRiding() || mc.player.isSpectator() || mc.player.isCreative()) return;
        if (onlyOnGround.getValue() && !mc.player.isOnGround()) return;
        if (stopOnSneak.getValue() && mc.player.isSneaking()) return;
        if (!MovementUtil.isMoving()) return;
        double spd = speed.getValue() / 10.0;
        String m = mode.getValue();
        tickCounter++;
        switch (m) {
            case "Vanilla" -> {
                mc.player.setSprinting(true);
                double rad = Math.toRadians(mc.player.getYaw());
                mc.player.setVelocity(-Math.sin(rad) * spd, mc.player.getVelocity().y, Math.cos(rad) * spd);
            }
            case "NCP" -> {
                if (mc.player.isOnGround()) {
                    mc.player.jump();
                    mc.player.setVelocity(mc.player.getVelocity().x * 1.05, 0.42, mc.player.getVelocity().z * 1.05);
                } else {
                    mc.player.setVelocity(mc.player.getVelocity().x * 0.98, mc.player.getVelocity().y, mc.player.getVelocity().z * 0.98);
                }
            }
            case "Hypixel" -> {
                if (mc.player.isOnGround()) {
                    if (MovementUtil.isMoving()) { mc.player.jump(); mc.player.setSprinting(true); }
                    double rad = Math.toRadians(mc.player.getYaw());
                    mc.player.setVelocity(-Math.sin(rad) * spd * 1.15, 0.42f, Math.cos(rad) * spd * 1.15);
                } else {
                    mc.player.setVelocity(mc.player.getVelocity().x * 0.99, mc.player.getVelocity().y, mc.player.getVelocity().z * 0.99);
                }
            }
            case "Ground" -> {
                mc.player.setSprinting(true);
                if (mc.player.isOnGround()) {
                    double rad = Math.toRadians(mc.player.getYaw());
                    mc.player.setVelocity(-Math.sin(rad) * spd, mc.player.getVelocity().y, Math.cos(rad) * spd);
                }
            }
            case "Legit" -> {
                mc.player.setSprinting(true);
                if (autoJump.getValue() && mc.player.isOnGround() && mc.player.forwardSpeed > 0) mc.player.jump();
            }
        }
        if (autoJump.getValue() && mc.player.isOnGround() && MovementUtil.isMoving() && !mc.player.horizontalCollision) {
            mc.player.jump();
        }
    }
}
