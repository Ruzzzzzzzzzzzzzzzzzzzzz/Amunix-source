package missu.amunix.modules.movement;
import missu.amunix.core.*;
import missu.amunix.settings.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.ingame.*;
public class InvMove extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private final BooleanSetting inv = new BooleanSetting("Inventory",true);
    private final BooleanSetting chest = new BooleanSetting("Chests",true);
    private final BooleanSetting furnace = new BooleanSetting("Furnaces",true);
    private final BooleanSetting chatOnly = new BooleanSetting("Chat Only",false);
    private final BooleanSetting allowSprint = new BooleanSetting("Allow Sprint",true);
    private final BooleanSetting allowJump = new BooleanSetting("Allow Jump",true);
    public InvMove(){super("InvMove","Allows movement while inside menus.",ModuleCategory.MOVEMENT);addSettings(toggle,inv,chest,furnace,chatOnly,allowSprint,allowJump);}
    public boolean shouldAllowMove(Screen screen){
        if(!toggle.getValue()||screen==null)return false;
        if(chatOnly.getValue())return screen instanceof ChatScreen;
        if(screen instanceof InventoryScreen&&!inv.getValue())return false;
        if(screen instanceof GenericContainerScreen&&!chest.getValue())return false;
        if(screen instanceof FurnaceScreen&&!furnace.getValue())return false;
        return screen instanceof HandledScreen||screen instanceof ChatScreen;
    }
}
