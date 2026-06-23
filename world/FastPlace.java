package missu.amunix.modules.world;
import missu.amunix.core.*;
import missu.amunix.settings.*;
import net.minecraft.client.MinecraftClient;
public class FastPlace extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private final NumberSetting delay = new NumberSetting("Delay ticks", 0, 0, 4, 1);
    private final BooleanSetting blocksOnly = new BooleanSetting("Blocks Only", true);
    private final BooleanSetting allItems = new BooleanSetting("All Items", false);
    public FastPlace(){super("FastPlace","Reduces block placement delay.",ModuleCategory.WORLD);addSettings(toggle,delay,blocksOnly,allItems);}
    public int getPlaceDelay(){return toggle.getValue()?(int)delay.getValue():4;}
}
