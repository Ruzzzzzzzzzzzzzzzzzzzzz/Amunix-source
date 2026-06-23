package missu.amunix.modules.render;
import missu.amunix.core.*;
import missu.amunix.settings.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.Box;
import missu.amunix.utils.RenderUtil;

public class ChestESP extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private final ModeSetting mode = new ModeSetting("Mode",new String[]{"Box","Outline","Shader"},"Box");
    private final NumberSetting range = new NumberSetting("Range",64,8,256,8);
    private final NumberSetting red = new NumberSetting("Red",255,0,255,1);
    private final NumberSetting green = new NumberSetting("Green",165,0,255,1);
    private final NumberSetting blue = new NumberSetting("Blue",0,0,255,1);

    public ChestESP(){super("ChestESP","Renders outlines around chests.",ModuleCategory.RENDER);addSettings(toggle,mode,range,red,green,blue);}

    @Override public void onRender3D(DrawContext ctx, float delta){
        if(!toggle.getValue())return;
        MinecraftClient mc=MinecraftClient.getInstance();
        if(mc.world==null||mc.player==null)return;
        int r=(int)red.getValue(),g=(int)green.getValue(),b=(int)blue.getValue();
        double rv=range.getValue();
        for(var be:mc.world.blockEntities){
            if(!(be instanceof ChestBlockEntity))continue;
            if(mc.player.squaredDistanceTo(be.getPos().toCenterPos())>rv*rv)continue;
            Box box=new Box(be.getPos());
            RenderUtil.drawBox(ctx,null,r,g,b,80);
        }
    }
}
