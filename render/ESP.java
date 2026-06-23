package missu.amunix.modules.render;
import missu.amunix.core.*;
import missu.amunix.settings.*;
import missu.amunix.utils.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.client.gui.DrawContext;
public class ESP extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private final ModeSetting mode = new ModeSetting("Mode", new String[]{"Box","Outline","2D","Shader"},"Box");
    private final BooleanSetting players = new BooleanSetting("Players", true);
    private final BooleanSetting monsters = new BooleanSetting("Monsters", false);
    private final BooleanSetting animals = new BooleanSetting("Animals", false);
    private final BooleanSetting invisibles = new BooleanSetting("Invisibles", true);
    private final NumberSetting red = new NumberSetting("Red",255,0,255,1);
    private final NumberSetting green = new NumberSetting("Green",0,0,255,1);
    private final NumberSetting blue = new NumberSetting("Blue",0,0,255,1);
    private final NumberSetting alpha = new NumberSetting("Alpha",100,0,255,1);
    private final BooleanSetting health = new BooleanSetting("Health Bar", false);
    public ESP(){super("ESP","Renders outlines around entities.",ModuleCategory.RENDER);addSettings(toggle,mode,players,monsters,animals,invisibles,red,green,blue,alpha,health);}
    @Override public void onRender3D(DrawContext ctx, float delta) {
        if(!toggle.getValue())return;
        MinecraftClient mc=MinecraftClient.getInstance();
        if(mc.world==null||mc.player==null)return;
        int r=(int)red.getValue(),g=(int)green.getValue(),b=(int)blue.getValue(),a=(int)alpha.getValue();
        for(Entity e:mc.world.getEntities()){
            if(!(e instanceof LivingEntity le)||le==mc.player||!le.isAlive())continue;
            if(le instanceof PlayerEntity&&!players.getValue())continue;
            if(le instanceof Monster&&!monsters.getValue())continue;
            if(le.isInvisible()&&!invisibles.getValue())continue;
            if(mc.player.distanceTo(le)>128)continue;
            String m=mode.getValue();
            if(m.equals("Box"))RenderUtil.drawBox(ctx,le,r,g,b,a);
            else if(m.equals("Outline"))RenderUtil.drawBox(ctx,le,r,g,b,a);
            if(health.getValue()){
                float hp=le.getHealth()/le.getMaxHealth();
                int hx=(int)(ctx.getScaledWindowWidth()/2)-20,hy=(int)(ctx.getScaledWindowHeight()/2)+20;
                ctx.fill(hx,hy,hx+40,hy+4,0x80000000);
                ctx.fill(hx,hy,hx+(int)(40*hp),hy+3,0xFF00FF00);
            }
        }
    }
}
