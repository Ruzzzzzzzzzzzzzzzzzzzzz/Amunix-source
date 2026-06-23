package missu.amunix.modules.render;
import missu.amunix.core.*;
import missu.amunix.settings.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.block.entity.*;
import net.minecraft.util.math.Box;
import missu.amunix.utils.RenderUtil;
public class StorageESP extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private final BooleanSetting chests = new BooleanSetting("Chests", true);
    private final BooleanSetting enderChests = new BooleanSetting("Ender Chests", true);
    private final BooleanSetting furnaces = new BooleanSetting("Furnaces", false);
    private final BooleanSetting shulkers = new BooleanSetting("Shulkers", true);
    private final BooleanSetting hoppers = new BooleanSetting("Hoppers", false);
    private final NumberSetting range = new NumberSetting("Range", 64, 8, 256, 8);
    private final NumberSetting red = new NumberSetting("Red", 255,0,255,1);
    private final NumberSetting green = new NumberSetting("Green", 165,0,255,1);
    private final NumberSetting blue = new NumberSetting("Blue", 0,0,255,1);
    public StorageESP(){super("StorageESP","Renders outlines around containers.",ModuleCategory.RENDER);addSettings(toggle,chests,enderChests,furnaces,shulkers,hoppers,range,red,green,blue);}
    @Override public void onRender3D(DrawContext ctx, float delta){
        if(!toggle.getValue())return;
        MinecraftClient mc=MinecraftClient.getInstance();
        if(mc.world==null||mc.player==null)return;
        int r=(int)red.getValue(),g=(int)green.getValue(),b=(int)blue.getValue();
        double rangeVal=range.getValue();
        for(BlockEntity be:mc.world.blockEntities){
            if(mc.player.squaredDistanceTo(be.getPos().toCenterPos())>rangeVal*rangeVal)continue;
            boolean draw=false;
            if(be instanceof ChestBlockEntity&&chests.getValue())draw=true;
            else if(be instanceof EnderChestBlockEntity&&enderChests.getValue())draw=true;
            else if(be instanceof FurnaceBlockEntity&&furnaces.getValue())draw=true;
            else if(be instanceof ShulkerBoxBlockEntity&&shulkers.getValue())draw=true;
            else if(be instanceof HopperBlockEntity&&hoppers.getValue())draw=true;
            if(draw){Box box=new Box(be.getPos());RenderUtil.drawBox(ctx,null,r,g,b,80);}
        }
    }
}
