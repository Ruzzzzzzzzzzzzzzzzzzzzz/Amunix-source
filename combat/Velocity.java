package missu.amunix.modules.combat;
import missu.amunix.core.*;
import missu.amunix.settings.*;
import missu.amunix.utils.KeyBindingUtil;
import net.minecraft.client.MinecraftClient;
public class Velocity extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private final NumberSetting horizontal = new NumberSetting("Horizontal %", 0, 0, 100, 1);
    private final NumberSetting vertical = new NumberSetting("Vertical %", 0, 0, 100, 1);
    private final ModeSetting mode = new ModeSetting("Mode", new String[]{"Simple","Advanced","JumpReset","Hypixel"},"Simple");
    private final NumberSetting chance = new NumberSetting("Chance %", 100, 0, 100, 1);
    private final BooleanSetting onlyInAir = new BooleanSetting("Only In Air", false);
    private final BooleanSetting whileTargeting = new BooleanSetting("While Targeting", true);
    private double lastVelX, lastVelZ;
    private boolean wasOnGround;
    private final java.util.Random random = new java.util.Random();
    public Velocity() { super("Velocity","Reduces incoming knockback.",ModuleCategory.COMBAT); addSettings(toggle,horizontal,vertical,mode,chance,onlyInAir,whileTargeting); }
    public double applyHorizontal(double velocity) {
        if (!toggle.getValue() || random.nextInt(100) >= chance.getValue()) return velocity;
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return velocity;
        if (onlyInAir.getValue() && mc.player.isOnGround()) return velocity;
        if (horizontal.getValue() == 0) return 0;
        return velocity * horizontal.getValue() / 100.0;
    }
    public double applyVertical(double velocity) {
        if (!toggle.getValue() || random.nextInt(100) >= chance.getValue()) return velocity;
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return velocity;
        if (onlyInAir.getValue() && mc.player.isOnGround()) return velocity;
        String m = mode.getValue();
        if (m.equals("JumpReset") && !mc.player.isOnGround() && velocity > 0) {
            return 0;
        }
        return velocity * vertical.getValue() / 100.0;
    }
    @Override public void onTick() {
        if (!toggle.getValue()) return;
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;
        if (mode.getValue().equals("JumpReset")) {
            if (!mc.player.isOnGround() && mc.player.hurtTime == 9) {
                KeyBindingUtil.pressJump();
                new Thread(() -> { try { Thread.sleep(30); KeyBindingUtil.stopJump(); } catch(Exception e){} }).start();
            }
        }
    }
}
