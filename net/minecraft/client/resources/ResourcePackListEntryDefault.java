package net.minecraft.client.resources;

import com.google.gson.JsonParseException;
import java.io.IOException;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResourcePackListEntryDefault extends ResourcePackListEntry
{
    private static final Logger logger = LogManager.getLogger();
    private final IResourcePack defaultPack;
    private final ResourceLocation resourcePackIcon;

    public ResourcePackListEntryDefault(GuiScreen resourcePacksGUIIn)
    {
        super(resourcePacksGUIIn);
        this.defaultPack = this.mc.getResourcePackRepository().rprDefaultResourcePack;
        DynamicTexture dynamictexture;

        try
        {
            dynamictexture = new DynamicTexture(this.defaultPack.getPackImage());
        }
        catch (IOException var4)
        {
            dynamictexture = TextureUtil.missingTexture;
        }

        this.resourcePackIcon = this.mc.getTextureManager().getDynamicTextureLocation("texturepackicon", dynamictexture);
    }

    protected int getPackFormat()
    {
        return 1;
    }

    protected String getPackDescription()
    {
        try
        {
            PackMetadataSection packmetadatasection = (PackMetadataSection)this.defaultPack.getPackMetadata(this.mc.getResourcePackRepository().rprMetadataSerializer, "pack");

            if (packmetadatasection != null)
            {
                return packmetadatasection.getPackDescription().getFormattedText();
            }
        }
        catch (IOException | JsonParseException exception)
        {
            logger.error((String)"Couldn\'t load metadata info", (Throwable)exception);
        }

        return EnumChatFormatting.RED + "Missing " + "pack.mcmeta" + " :(";
    }

    protected boolean func_148309_e()
    {
        return false;
    }

    protected boolean func_148308_f()
    {
        return false;
    }

    protected boolean func_148314_g()
    {
        return false;
    }

    protected boolean func_148307_h()
    {
        return false;
    }

    protected String getPackName()
    {
        return "Default";
    }

    protected void bindTexture()
    {
        this.mc.getTextureManager().bindTexture(this.resourcePackIcon);
    }

    protected boolean func_148310_d()
    {
        return false;
    }
}
