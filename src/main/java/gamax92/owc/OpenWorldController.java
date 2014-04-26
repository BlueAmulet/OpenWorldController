package gamax92.owc;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = OpenWorldController.MODID, name = "OpenWorldController", version = "1.0.0", dependencies = "required-after:OpenComputers")
public class OpenWorldController
{
    public static final String MODID = "openworldcontroller";

	@Mod.Instance
    public static OpenWorldController instance;

    public static BlockWorldController worldControl;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	worldControl = new BlockWorldController(3660);
        GameRegistry.registerBlock(worldControl, "openworldcontroller:controller");
        GameRegistry.registerTileEntity(TileEntityWorldController.class, "openworldcontroller:controller");
        LanguageRegistry.addName(worldControl, "World Controller");
    }

}
