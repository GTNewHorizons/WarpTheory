package shukaro.warptheory.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import shukaro.warptheory.util.NameMetaPair;

public class TileEntityVanish extends TileEntity {

    private NameMetaPair pair;
    private NBTTagCompound tag;
    private long returnTime;

    public TileEntityVanish(World world, int x, int y, int z, long returnTime) {
        this.pair = new NameMetaPair(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z));
        if (world.getTileEntity(x, y, z) != null) {
            this.tag = new NBTTagCompound();
            world.getTileEntity(x, y, z).writeToNBT(this.tag);
        }
        this.returnTime = returnTime;
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    @Override
    public void updateEntity() {
        if (this.worldObj.getTotalWorldTime() >= this.returnTime) rebuildBlock();
    }

    private void rebuildBlock() {
        this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, this.pair.getBlock(), this.pair.getMetadata(), 0);
        if (this.tag != null) this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord).readFromNBT(this.tag);
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setString("oldName", this.pair.getName());
        tag.setInteger("oldMeta", this.pair.getMetadata());
        tag.setTag("oldTag", this.tag);
        tag.setLong("returnTime", this.returnTime);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.pair = new NameMetaPair(tag.getString("oldName"), tag.getInteger("oldMeta"));
        if (tag.hasKey("oldTag")) this.tag = tag.getCompoundTag("oldTag");
        this.returnTime = tag.getLong("returnTime");
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeToNBT(tag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, -999, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        this.readFromNBT(pkt.func_148857_g());
    }
}
