package missu.amunix.modules.misc;
import missu.amunix.core.*;
public class ClickGUI extends BaseModule {
    public ClickGUI(){super("ClickGUI","Click GUI for module settings.",ModuleCategory.RENDER);setKeyBind(344);}
    @Override public void onEnable(){net.minecraft.client.MinecraftClient.getInstance().setScreen(new ClickGUIScreen());}
    private static class ClickGUIScreen extends net.minecraft.client.gui.screen.Screen {
        protected ClickGUIScreen(){super(net.minecraft.text.Text.literal("Amunix ClickGUI"));}
    }
}
