package missu.amunix.modules.movement;
import missu.amunix.core.*;
import net.minecraft.client.MinecraftClient;
public class AutoSprint extends BaseModule {
    public AutoSprint(){super("AutoSprint","Automatically sprints.",ModuleCategory.MOVEMENT);}
    @Override public void onTick(){if(enabled){var mc=MinecraftClient.getInstance();if(mc.player!=null)mc.player.setSprinting(true);}}
}
