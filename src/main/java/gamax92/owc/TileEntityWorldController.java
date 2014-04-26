package gamax92.owc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import li.cil.oc.api.Network;
import li.cil.oc.api.network.Arguments;
import li.cil.oc.api.network.Callback;
import li.cil.oc.api.network.Connector;
import li.cil.oc.api.network.Context;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.TileEntityEnvironment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldServer;

public class TileEntityWorldController extends TileEntityEnvironment {
    public static final double EnergyCostPerTick = 0.5;

    public static final double RadarRange = 32;

    protected boolean hasEnergy;

    public TileEntityWorldController() {
        // The 'node' of a tile entity is used to connect it to other components
        // including computers. They are connected to nodes of neighboring
        // blocks, forming a network that way. That network is also used for
        // distributing energy among components for the mod.
        node = Network.newNode(this, Visibility.Network).
                withConnector().
                withComponent("world_control").
                create();
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        // Nodes are only created on the server side, so we have to check.
        if (node != null) {
            // Consume some energy per tick to keep the radar running!
            hasEnergy = ((Connector) node).tryChangeBuffer(-EnergyCostPerTick);
        }
    }
    
    // getComponentPos()
    @Callback
    public Object[] getComponentPos(Context context, Arguments args) {
        return new Object[]{this.xCoord, this.yCoord, this.zCoord};
    }
    
    // getPlayerList()
    @Callback
    public Object[] getPlayerList(Context context, Arguments args) {
    	List playerEntites = worldObj.playerEntities;
    	List<String> playerList = new ArrayList<String>();;

    	Iterator player = playerEntites.iterator();
    	while (player.hasNext()) {
    		playerList.add(((EntityPlayer)player.next()).username);
    	}
    	
        return new Object[]{playerList.toArray()};
    }
    
    // getBlock(x, y, z)
    @Callback
    public Object[] getBlock(Context context, Arguments args) {
    	int xPos = args.checkInteger(0);
    	int yPos = args.checkInteger(1);
    	int zPos = args.checkInteger(2);
        return new Object[]{worldObj.getBlockId(xPos, yPos, zPos), worldObj.getBlockMetadata(xPos, yPos, zPos)};
    }

    // setBlock(x, y, z, id, metadata)
    @Callback
    public Object[] setBlock(Context context, Arguments args) {
    	int xPos = args.checkInteger(0);
    	int yPos = args.checkInteger(1);
    	int zPos = args.checkInteger(2);
    	int ID = args.checkInteger(3);
    	int metadata = args.checkInteger(4);
        return new Object[]{worldObj.setBlock(xPos, yPos, zPos, ID, metadata, 3)};
    }
    
    // setBlockNoUpdate(x, y, z, id, metadata)
    @Callback
    public Object[] setBlockNoUpdate(Context context, Arguments args) {
    	int xPos = args.checkInteger(0);
    	int yPos = args.checkInteger(1);
    	int zPos = args.checkInteger(2);
    	int ID = args.checkInteger(3);
    	int metadata = args.checkInteger(4);
        return new Object[]{worldObj.setBlock(xPos, yPos, zPos, ID, metadata, 2)};
    }
    
    // setTime(time)
    @Callback
    public Object[] setTime(Context context, Arguments args) {
    	int time = args.checkInteger(0);
    	worldObj.setWorldTime(time);
        return new Object[]{};
    }

    // playSound(x, y, z, sound, volume, pitch)
    @Callback
    public Object[] playSound(Context context, Arguments args) {
    	double xPos = args.checkDouble(0);
    	double yPos = args.checkDouble(1);
    	double zPos = args.checkDouble(2);
    	String sound = args.checkString(3);
    	double volume = args.checkDouble(4);
    	double pitch = args.checkDouble(4);
    	worldObj.playSoundEffect(xPos + 0.5D, yPos + 0.5D, zPos + 0.5D, sound, (float) volume, (float) pitch);
        return new Object[]{};
    }
    
    // explode(x, y, z, power, damage)
    @Callback
    public Object[] explode(Context context, Arguments args) {
    	double xPos = args.checkDouble(0);
    	double yPos = args.checkDouble(1);
    	double zPos = args.checkDouble(2);
    	double strength = args.checkDouble(3);
    	boolean damage = args.checkBoolean(4);
	    worldObj.createExplosion(null, xPos, yPos, zPos, (float) strength, damage);
	    return new Object[]{};
	}

    // getPlayerLocation(name)
    @Callback
    public Object[] getPlayerLocation(Context context, Arguments args) {
    	String player = args.checkString(0);
    	EntityPlayer playerEntity = worldObj.getPlayerEntityByName(player);
        return new Object[]{playerEntity.posX, playerEntity.posY, playerEntity.posZ};
    }
    
    // getPlayerLooking(name)
    @Callback
    public Object[] getPlayerLooking(Context context, Arguments args) {
    	String player = args.checkString(0);
    	EntityPlayer playerEntity = worldObj.getPlayerEntityByName(player);
    	Vec3 looking = playerEntity.getLookVec();
    	return new Object[]{looking.xCoord, looking.yCoord, looking.zCoord};
    }
    
    // getPlayerHealth(name)
    @Callback
    public Object[] getPlayerHealth(Context context, Arguments args) {
    	String player = args.checkString(0);
    	EntityPlayer playerEntity = worldObj.getPlayerEntityByName(player);
        return new Object[]{playerEntity.getHealth()};
    }
    
    // getPlayerHunger(name)
    @Callback
    public Object[] getPlayerHunger(Context context, Arguments args) {
    	String player = args.checkString(0);
    	EntityPlayer playerEntity = worldObj.getPlayerEntityByName(player);
        return new Object[]{playerEntity.getFoodStats().getFoodLevel()};
    }
    
    // setPlayerLocation(name, x, y, z)
    @Callback
    public Object[] setPlayerLocation(Context context, Arguments args) {
    	String player = args.checkString(0);
    	double xPos = args.checkDouble(1);
    	double yPos = args.checkDouble(2);
    	double zPos = args.checkDouble(3);
    	EntityPlayer playerEntity = worldObj.getPlayerEntityByName(player);
    	playerEntity.setPositionAndUpdate(xPos, yPos, zPos);
        return new Object[]{};
    }
    
    // setPlayerHealth(name, health)
    @Callback
    public Object[] setPlayerHealth(Context context, Arguments args) {
    	String player = args.checkString(0);
    	float health = args.checkInteger(1);
    	EntityPlayer playerEntity = worldObj.getPlayerEntityByName(player);
    	playerEntity.setHealth(health);
        return new Object[]{};
    }
    
    // sendPlayerChat(name, message)
    @Callback
    public Object[] sendPlayerChat(Context context, Arguments args) {
    	String player = args.checkString(0);
    	String message = args.checkString(1);
    	EntityPlayer playerEntity = worldObj.getPlayerEntityByName(player);
    	playerEntity.addChatMessage(message);
        return new Object[]{};
    }
}
