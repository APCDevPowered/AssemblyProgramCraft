package yuxuanchiadm.apc.apc.common.tileEntity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import yuxuanchiadm.apc.apc.common.block.BlockExternalDeviceKeyboard;
import yuxuanchiadm.apc.vcpu32.extdev.ExternalDeviceKeyboard;
import yuxuanchiadm.apc.vcpu32.vm.AbstractExternalDevice;

public class TileEntityExternalDeviceKeyboard extends TileEntityExternalDevice
{
    public ExternalDeviceKeyboard externalDeviceKeyboard = new ExternalDeviceKeyboard();
    
    public AbstractExternalDevice getExternalDevice()
    {
        return externalDeviceKeyboard;
    }
    public EnumFacing[] getConnectorConnectableFaces(IBlockState state)
    {
        return new EnumFacing[]
        {
                ((EnumFacing) state.getValue(BlockExternalDeviceKeyboard.FACING)).getOpposite()
        };
    }
}
