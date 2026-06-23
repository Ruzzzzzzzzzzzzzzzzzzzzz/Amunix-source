package missu.amunix.modules.render;
import missu.amunix.core.*;
import missu.amunix.settings.*;
public class Animations extends BaseModule {
    private final ModeSetting style = new ModeSetting("Style",new String[]{"1.7","Slide","Swing","Custom"},"1.7");
    public Animations(){super("Animations","Custom first person item animations.",ModuleCategory.RENDER);addSetting(style);}
}
