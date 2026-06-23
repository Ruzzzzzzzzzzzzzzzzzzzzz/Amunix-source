package missu.amunix.modules.render;
import missu.amunix.core.*;
import missu.amunix.settings.*;
public class Watermark extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private final ModeSetting style = new ModeSetting("Style",new String[]{"Amunix","Simple","Minimal"},"Amunix");
    public Watermark(){super("Watermark","Displays client watermark on screen.",ModuleCategory.RENDER);addSettings(toggle,style);}
}
