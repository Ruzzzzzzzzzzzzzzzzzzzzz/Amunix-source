package missu.amunix.modules.player;
import missu.amunix.core.*;
import missu.amunix.settings.*;
import missu.amunix.utils.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.Blocks;
import net.minecraft.util.Hand;
public class AntiVoid extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private final ModeSetting mode = new ModeSetting("Mode",new String[]{"Freeze","Boost","Fly","Pearl"},"Freeze");
    private final NumberSetting fallDist = new NumberSetting("Fall Distance",5,1,10,0.5);
    private final BooleanSetting autoDisable = new BooleanSetting("Auto Disable", true);
    public AntiVoid(){super("AntiVoid","Saves you from falling into the void.",ModuleCategory.PLAYER);addSettings(toggle,mode,fallDist,autoDisable);}
    @Override public void onTick(){
        if(!toggle.getValue())return;
        MinecraftClient mc=MinecraftClient.getInstance();
        if(mc.player==null||mc.player.isOnGround())return;
        if(!isOverVoid(mc))return;
        if(mc.player.fallDistance<fallDist.getValue())return;
        switch(mode.getValue()){
            case "Freeze"->{mc.player.setVelocity(0,0,0);mc.player.getAbilities().allowFlying=true;}
            case "Boost"->mc.player.setVelocity(0,0.8,0);
            case "Fly"->mc.player.getAbilities().allowFlying=true;
            case "Pearl"->{
                int slot=InventoryUtil.findItem(net.minecraft.item.EnderPearlItem.class);
                if(slot>=0){int prev=mc.player.getInventory().selectedSlot;mc.player.getInventory().selectedSlot=slot;mc.interactionManager.interactItem(mc.player,Hand.MAIN_HAND);mc.player.getInventory().selectedSlot=prev;}
            }
        }
        if(autoDisable.getValue()&&mc.player.isOnGround())setEnabled(false);
    }
    private boolean isOverVoid(MinecraftClient mc){
        for(int y=0;y<mc.player.getBlockY();y++){
            BlockPos pos=new BlockPos((int)mc.player.getX(),y,(int)mc.player.getZ());
            if(!mc.world.getBlockState(pos).isAir()&&mc.world.getBlockState(pos).getBlock()!=Blocks.VOID_AIR)return false;
        }
        return true;
    }
}
