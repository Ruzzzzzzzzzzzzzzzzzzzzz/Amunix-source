package missu.amunix.modules.movement;
import missu.amunix.core.*;
import missu.amunix.settings.*;
import net.minecraft.client.MinecraftClient;
public class NoSlow extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private final BooleanSetting eating = new BooleanSetting("Eating", true);
    private final BooleanSetting drinking = new BooleanSetting("Drinking", true);
    private final BooleanSetting blocking = new BooleanSetting("Blocking", true);
    private final BooleanSetting bow = new BooleanSetting("Bow", true);
    private final NumberSetting slowdown = new NumberSetting("Slowdown %", 0, 0, 100, 1);
    public NoSlow(){super("NoSlow","Prevents slowdown while using items.",ModuleCategory.MOVEMENT);addSettings(toggle,eating,drinking,blocking,bow,slowdown);}
    public float applySlowdown(float original) {
        if (!toggle.getValue()) return original;
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return original;
        if (mc.player.isUsingItem()) {
            var item = mc.player.getActiveItem().getItem();
            boolean isFood = item.getFoodComponent() != null;
            boolean isDrink = item.getFoodComponent() != null && item.getFoodComponent().isDrink();
            boolean isBlocking = mc.player.isUsingItem() && mc.player.getActiveItem().getItem() instanceof net.minecraft.item.ShieldItem;
            if ((isFood && eating.getValue()) || (isDrink && drinking.getValue()) || (isBlocking && blocking.getValue())) return original * slowdown.getValue() / 100f;
        }
        return original;
    }
}
