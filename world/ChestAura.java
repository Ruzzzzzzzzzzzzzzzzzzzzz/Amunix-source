package missu.amunix.modules.world;
import missu.amunix.core.*;
import missu.amunix.settings.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.Hand;

public class ChestAura extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private final NumberSetting range = new NumberSetting("Range",6,1,20,0.5);
    private final NumberSetting delay = new NumberSetting("Delay ms",200,0,1000,50);
    private final BooleanSetting autoClose = new BooleanSetting("Auto Close",true);
    private long lastOpen;

    public ChestAura(){super("ChestAura","Automatically opens nearby chests.",ModuleCategory.WORLD);addSettings(toggle,range,delay,autoClose);}

    @Override public void onTick(){
        if(!toggle.getValue())return;
        MinecraftClient mc=MinecraftClient.getInstance();
        if(mc.player==null||mc.world==null||mc.interactionManager==null)return;
        if(System.currentTimeMillis()-lastOpen<delay.getValue())return;
        if(mc.player.currentScreenHandler!=mc.player.playerScreenHandler&&autoClose.getValue()){mc.player.closeHandledScreen();return;}
        for(var be:mc.world.blockEntities){
            if(!(be instanceof ChestBlockEntity))continue;
            if(mc.player.squaredDistanceTo(be.getPos().toCenterPos())>range.getValue()*range.getValue())continue;
            mc.interactionManager.interactBlock(mc.player,Hand.MAIN_HAND,new net.minecraft.util.hit.BlockHitResult(be.getPos().toCenterPos(),net.minecraft.util.math.Direction.UP,be.getPos(),false));
            lastOpen=System.currentTimeMillis();break;
        }
    }
}
