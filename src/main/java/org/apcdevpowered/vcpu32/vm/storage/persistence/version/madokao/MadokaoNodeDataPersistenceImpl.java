package org.apcdevpowered.vcpu32.vm.storage.persistence.version.madokao;

import org.apcdevpowered.vcpu32.vm.storage.container.NodeContainerArray;
import org.apcdevpowered.vcpu32.vm.storage.container.NodeContainerMap;
import org.apcdevpowered.vcpu32.vm.storage.persistence.BaseNodeDataPersistenceImpl;
import org.apcdevpowered.vcpu32.vm.storage.scalar.NodeScalarByte;
import org.apcdevpowered.vcpu32.vm.storage.scalar.NodeScalarByteArray;
import org.apcdevpowered.vcpu32.vm.storage.scalar.NodeScalarDouble;
import org.apcdevpowered.vcpu32.vm.storage.scalar.NodeScalarFloat;
import org.apcdevpowered.vcpu32.vm.storage.scalar.NodeScalarInteger;
import org.apcdevpowered.vcpu32.vm.storage.scalar.NodeScalarIntegerArray;
import org.apcdevpowered.vcpu32.vm.storage.scalar.NodeScalarLong;
import org.apcdevpowered.vcpu32.vm.storage.scalar.NodeScalarShort;

public class MadokaoNodeDataPersistenceImpl extends BaseNodeDataPersistenceImpl
{
    public MadokaoNodeDataPersistenceImpl()
    {
        super(1);
        mapping.addMapping(1, NodeScalarByte.class);
        mapping.addMapping(2, NodeScalarShort.class);
        mapping.addMapping(3, NodeScalarInteger.class);
        mapping.addMapping(4, NodeScalarLong.class);
        mapping.addMapping(5, NodeScalarFloat.class);
        mapping.addMapping(6, NodeScalarDouble.class);
        mapping.addMapping(7, NodeScalarByteArray.class);
        mapping.addMapping(8, NodeScalarIntegerArray.class);
        mapping.addMapping(9, NodeContainerArray.class);
        mapping.addMapping(10, NodeContainerMap.class);
    }
}