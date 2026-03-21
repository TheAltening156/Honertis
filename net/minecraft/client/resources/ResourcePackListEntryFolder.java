package net.minecraft.client.resources;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.ResourcePackRepository.SubFolder;

public class ResourcePackListEntryFolder extends ResourcePackListEntry
{
    private final SubFolder packEntry;

    public ResourcePackListEntryFolder(GuiScreen resourcePacksGUIIn, SubFolder packEntry)
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

    public SubFolder getPackEntry()
    {
        return this.packEntry;
    }
}