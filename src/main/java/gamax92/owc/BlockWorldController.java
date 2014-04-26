package gamax92.owc;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockWorldController extends Block {

    private Icon topTexture;
	private Icon bottomTexture;
	private Icon sideTexture;
	private Icon sideActiveTexture;

	public BlockWorldController(int blockId) {
        super(blockId, Material.iron);
        setCreativeTab(li.cil.oc.api.CreativeTab.Instance);
        setUnlocalizedName("controller");
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntityWorldController();
    }
    

	public void registerIcons(IconRegister register)
	{
		this.topTexture = register.registerIcon(OpenWorldController.MODID + ":controller_top");
		this.bottomTexture = register.registerIcon(OpenWorldController.MODID + ":controller_bottom");
		this.sideTexture = register.registerIcon(OpenWorldController.MODID + ":controller_side");
	}
	
	public Icon getIcon(int side, int meta)
	{
		if(side == 0)
		{
			return this.topTexture;
		}
		else if(side == 1)
		{
			return this.bottomTexture;
		}
		else
		{
			return this.sideTexture;
		}
	}
}