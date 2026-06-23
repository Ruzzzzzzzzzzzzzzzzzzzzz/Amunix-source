package missu.amunix.modules.render;
import missu.amunix.core.*;
import missu.amunix.settings.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class Nametags extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private final NumberSetting scale = new NumberSetting("Scale",1.0,0.5,3.0,0.1);
    private final BooleanSetting health = new BooleanSetting("Health",true);
    private final BooleanSetting armor = new BooleanSetting("Armor",true);
    private final BooleanSetting distance = new BooleanSetting("Distance",true);
    private final BooleanSetting background = new BooleanSetting("Background",true);

    public Nametags(){super("Nametags","Enhanced player nametags with info.",ModuleCategory.RENDER);addSettings(toggle,scale,health,armor,distance,background);}

    @Override public void onRender3D(DrawContext ctx, float delta){
        if(!toggle.getValue())return;
        MinecraftClient mc=MinecraftClient.getInstance();
        if(mc.world==null||mc.player==null)return;
        for(var e:mc.world.getEntities()){
            if(!(e instanceof PlayerEntity p)||p==mc.player)continue;
            float dist=mc.player.distanceTo(p);
            if(dist>64)continue;
            String text=p.getGameProfile().getName();
            if(health.getValue())text+=" "+String.format("%.0f",p.getHealth());
            if(distance.getValue())text+=" "+String.format("%.1fm",dist);
            int x=(int)(ctx.getScaledWindowWidth()/2);
            int y=(int)(ctx.getScaledWindowHeight()/2-dist*4);
            ctx.drawText(mc.textRenderer,text,x-mc.textRenderer.getWidth(text)/2,y,0xFFFFFF,true);
        }
    }
}
