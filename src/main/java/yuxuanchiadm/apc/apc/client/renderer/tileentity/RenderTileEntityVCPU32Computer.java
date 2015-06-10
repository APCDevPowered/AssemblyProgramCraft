package yuxuanchiadm.apc.apc.client.renderer.tileentity;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yuxuanchiadm.apc.apc.client.renderer.AssemblyProgramCraftRenderGlobal;
import yuxuanchiadm.apc.apc.common.AssemblyProgramCraft;
import yuxuanchiadm.apc.apc.common.block.BlockVCPU32Computer;
import yuxuanchiadm.apc.apc.common.tileEntity.TileEntityVCPU32Computer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderTileEntityVCPU32Computer extends TileEntitySpecialRenderer
{
    private static final ResourceLocation computerTextures = new ResourceLocation(AssemblyProgramCraft.MODID + ":" + "textures/tileentity/computer.png");
    
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTickTime, int partialBlockDamage)
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        
        TileEntityVCPU32Computer tileentityvcpu32computer = (TileEntityVCPU32Computer)tileentity;
        if (tileentityvcpu32computer == null)
        {
            return;
        }
        EnumFacing face = (EnumFacing) tileentityvcpu32computer.getWorld().getBlockState(tileentityvcpu32computer.getPos()).getValue(BlockVCPU32Computer.FACING);
        
        GlStateManager.disableLighting();
        
        GlStateManager.pushMatrix();
        
        this.bindTexture(computerTextures);
        
        GlStateManager.translate(x, y, z);
        
        if(face.equals(EnumFacing.NORTH))
        {
            GlStateManager.rotate(0, 1, 0, 0);
            GlStateManager.translate(0, 0, 0);
        }
        else if(face.equals(EnumFacing.EAST))
        {
            GlStateManager.rotate(-90, 0, 1, 0);
            GlStateManager.translate(0, 0, -1);
        }
        else if(face.equals(EnumFacing.SOUTH))
        {
            GlStateManager.rotate(-180, 0, 1, 0);
            GlStateManager.translate(-1, 0, -1);
        }
        else if(face.equals(EnumFacing.WEST))
        {
            GlStateManager.rotate(-270, 0, 1, 0);
            GlStateManager.translate(-1, 0, 0);
        }
        
        worldRenderer.startDrawingQuads();
        
        worldRenderer.addVertexWithUV(1, 0, (4.0/16.0), 0, 0.5);
        worldRenderer.addVertexWithUV(0, 0, (4.0/16.0), 0.5, 0.5);
        worldRenderer.addVertexWithUV(0, 1, (4.0/16.0), 0.5, 0);
        worldRenderer.addVertexWithUV(1, 1, (4.0/16.0), 0, 0);
        
        worldRenderer.addVertexWithUV(0, 0, 1, 0.5, 0.5);
        worldRenderer.addVertexWithUV(1, 0, 1, 1, 0.5);
        worldRenderer.addVertexWithUV(1, 1, 1, 1, 0);
        worldRenderer.addVertexWithUV(0, 1, 1, 0.5, 0);
        
        worldRenderer.addVertexWithUV((4.0/16.0), 0, 0, 0, 1);
        worldRenderer.addVertexWithUV((4.0/16.0), 0, 1, 0.5, 1);
        worldRenderer.addVertexWithUV((4.0/16.0), 1, 1, 0.5, 0.5);
        worldRenderer.addVertexWithUV((4.0/16.0), 1, 0, 0, 0.5);
        
        worldRenderer.addVertexWithUV((12.0/16.0), 0, 1, 0.5, 1);
        worldRenderer.addVertexWithUV((12.0/16.0), 0, 0, 0, 1);
        worldRenderer.addVertexWithUV((12.0/16.0), 1, 0, 0, 0.5);
        worldRenderer.addVertexWithUV((12.0/16.0), 1, 1, 0.5, 0.5);
        
        worldRenderer.addVertexWithUV(1, (12.0/16.0), 0, 0.5, 0.5);
        worldRenderer.addVertexWithUV(0, (12.0/16.0), 0, 0.5, 1);
        worldRenderer.addVertexWithUV(0, (12.0/16.0), 1, 1, 1);
        worldRenderer.addVertexWithUV(1, (12.0/16.0), 1, 1, 0.5);
        
        worldRenderer.addVertexWithUV(0, 0, 0, 0.5, 1);
        worldRenderer.addVertexWithUV(1, 0, 0, 0.5, 0.5);
        worldRenderer.addVertexWithUV(1, 0, 1, 1, 0.5);
        worldRenderer.addVertexWithUV(0, 0, 1, 1, 1);
        
        tessellator.draw();
        
        this.bindTexture(AssemblyProgramCraftRenderGlobal.colorsTextures);
        
        if(tileentityvcpu32computer.isError())
        {
            worldRenderer.startDrawingQuads();
            
            worldRenderer.addVertexWithUV((7.0/16.0), (5.0/16.0), (4.0/16.0), 0.25, 0.25);
            worldRenderer.addVertexWithUV((6.0/16.0), (5.0/16.0), (4.0/16.0), 0.5, 0.25);
            worldRenderer.addVertexWithUV((6.0/16.0), (6.0/16.0), (4.0/16.0), 0.5, 0);
            worldRenderer.addVertexWithUV((7.0/16.0), (6.0/16.0), (4.0/16.0), 0.25, 0);
            
            tessellator.draw();
        }
        else
        {
            if(tileentityvcpu32computer.isRunning())
            {
                worldRenderer.startDrawingQuads();
                
                worldRenderer.addVertexWithUV((7.0/16.0), (5.0/16.0), (4.0/16.0), 0, 0.5);
                worldRenderer.addVertexWithUV((6.0/16.0), (5.0/16.0), (4.0/16.0), 0.25, 0.5);
                worldRenderer.addVertexWithUV((6.0/16.0), (6.0/16.0), (4.0/16.0), 0.25, 0.25);
                worldRenderer.addVertexWithUV((7.0/16.0), (6.0/16.0), (4.0/16.0), 0, 0.25);
                
                tessellator.draw();
            }
            else
            {
                worldRenderer.startDrawingQuads();
                
                worldRenderer.addVertexWithUV((7.0/16.0), (5.0/16.0), (4.0/16.0), 0.25, 0.5);
                worldRenderer.addVertexWithUV((6.0/16.0), (5.0/16.0), (4.0/16.0), 0.5, 0.5);
                worldRenderer.addVertexWithUV((6.0/16.0), (6.0/16.0), (4.0/16.0), 0.5, 0.25);
                worldRenderer.addVertexWithUV((7.0/16.0), (6.0/16.0), (4.0/16.0), 0.25, 0.25);
                
                tessellator.draw();
            }
        }
                
        GlStateManager.popMatrix();
        
        GlStateManager.enableLighting();
    }
}