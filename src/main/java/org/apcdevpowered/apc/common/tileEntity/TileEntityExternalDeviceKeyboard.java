package org.apcdevpowered.apc.common.tileEntity;

import org.apcdevpowered.apc.common.block.BlockExternalDeviceKeyboard;
import org.apcdevpowered.vcpu32.extdev.ExternalDeviceKeyboard;
import org.apcdevpowered.vcpu32.vm.AbstractExternalDevice;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

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
