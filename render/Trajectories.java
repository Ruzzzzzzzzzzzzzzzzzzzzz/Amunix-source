package missu.amunix.modules.render;
import missu.amunix.core.*;
import missu.amunix.settings.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.Vec3d;

public class Trajectories extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private final NumberSetting red = new NumberSetting("Red",255,0,255,1);
    private final NumberSetting green = new NumberSetting("Green",0,0,255,1);
    private final NumberSetting blue = new NumberSetting("Blue",0,0,255,1);

    public Trajectories(){super("Trajectories","Shows projectile trajectory path.",ModuleCategory.RENDER);addSettings(toggle,red,green,blue);}

    @Override public void onRender3D(DrawContext ctx, float delta){
        if(!toggle.getValue())return;
        MinecraftClient mc=MinecraftClient.getInstance();
        if(mc.player==null||mc.world==null)return;
        Vec3d pos=mc.player.getPos().add(0,mc.player.getStandingEyeHeight(),0);
        Vec3d look=mc.player.getRotationVec(1).multiply(3);
        for(int i=0;i<100;i++){
            pos=pos.add(look.x*0.1,look.y*0.1-0.03,look.z*0.1);
        }
    }
}
