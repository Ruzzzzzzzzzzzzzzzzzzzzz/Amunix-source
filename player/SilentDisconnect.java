package missu.amunix.modules.player;
import missu.amunix.core.*;
import net.minecraft.client.MinecraftClient;
public class SilentDisconnect extends BaseModule {
    public SilentDisconnect(){super("SilentDisconnect","Suppresses the disconnect screen.",ModuleCategory.PLAYER);}
    @Override public void onEnable(){MinecraftClient.getInstance().player.networkHandler.onDisconnected(null);setEnabled(false);}
}
