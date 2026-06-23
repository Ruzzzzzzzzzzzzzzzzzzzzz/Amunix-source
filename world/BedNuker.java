package missu.amunix.modules.world;
import missu.amunix.core.*;
import missu.amunix.settings.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Hand;

public class BedNuker extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private final NumberSetting range = new NumberSetting("Range",5,1,10,0.5);
    private final NumberSetting delay = new NumberSetting("Delay ms",100,0,500,10);
    private final BooleanSetting autoTool = new BooleanSetting("Auto Tool",true);
    private long lastBreak;

    public BedNuker(){super("BedNuker","Automatically breaks beds in BedWars.",ModuleCategory.WORLD);addSettings(toggle,range,delay,autoTool);}

    @Override public void onTick(){
        if(!toggle.getValue())return;
        MinecraftClient mc=MinecraftClient.getInstance();
        if(mc.player==null||mc.world==null||mc.interactionManager==null)return;
        if(System.currentTimeMillis()-lastBreak<delay.getValue())return;
        double r=range.getValue();
        for(int x=-(int)r;x<=r;x++)for(int y=-(int)r;y<=r;y++)for(int z=-(int)r;z<=r;z++){
            BlockPos pos=mc.player.getBlockPos().add(x,y,z);
            BlockState state=mc.world.getBlockState(pos);
            if(state.getBlock() instanceof BedBlock){
                mc.interactionManager.attackBlock(pos,net.minecraft.util.math.Direction.UP);
                mc.player.swingHand(Hand.MAIN_HAND);
                lastBreak=System.currentTimeMillis();return;
            }
        }
    }
}
