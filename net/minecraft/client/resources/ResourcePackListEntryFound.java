package net.minecraft.client.resources;

import net.minecraft.client.gui.GuiScreenResourcePacks;

public class ResourcePackListEntryFound extends ResourcePackListEntry
{
    private final ResourcePackRepository.Entry packEntry;

    public ResourcePackListEntryFound(GuiScreenResourcePacks resourcePacksGUIIn, ResourcePackRepository.Entry packEntry)
    {
        super(resourcePacksGUIIn);
        this.packEntry = packEntry;
    }

    protected void bindTexture()
    {
        this.packEntry.bindTexturePackIcon(this.mc.getTextureManager());
    }

    protected int getPackFormat()
    {
        return this.packEntry.getPackFormat();
    }

    protected String getPackDescription()
    {
        return this.packEntry.getTexturePackDescription();
    }

    protected String getPackName()
    {
        return this.packEntry.getResourcePackName();
    }

    public ResourcePackRepository.Entry getPackEntry()
    {
        return this.packEntry;
    }
}
