package net.minecraft.client.resources;

import net.minecraft.client.gui.GuiScreenResourcePacks;

public class ResourcePackListEntryFound extends ResourcePackListEntry
{
    private final ResourcePackRepository.Entry field_148319_c;

    public ResourcePackListEntryFound(GuiScreenResourcePacks resourcePacksGUIIn, ResourcePackRepository.Entry p_i45053_2_)
    {
        super(resourcePacksGUIIn);
        this.field_148319_c = p_i45053_2_;
    }

    protected void bindTexture()
    {
        this.field_148319_c.bindTexturePackIcon(this.mc.getTextureManager());
    }

    protected int getPackFormat()
    {
        return this.field_148319_c.getPackFormat();
    }

    protected String getPackDescription()
    {
        return this.field_148319_c.getTexturePackDescription();
    }

    protected String getPackName()
    {
        return this.field_148319_c.getResourcePackName();
    }

    public ResourcePackRepository.Entry func_148318_i()
    {
        return this.field_148319_c;
    }
}
