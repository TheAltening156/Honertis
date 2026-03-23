package net.minecraft.client.resources;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.ResourcePackRepository.SubFolder;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;

public abstract class ResourcePackListEntry implements GuiListExtended.IGuiListEntry
{
    private static final ResourceLocation RESOURCE_PACKS_TEXTURE = new ResourceLocation("textures/gui/resource_packs.png");
    private static final IChatComponent field_183020_d = new ChatComponentTranslation("resourcePack.incompatible", new Object[0]);
    private static final IChatComponent field_183021_e = new ChatComponentTranslation("resourcePack.incompatible.old", new Object[0]);
    private static final IChatComponent field_183022_f = new ChatComponentTranslation("resourcePack.incompatible.new", new Object[0]);
    protected final Minecraft mc;
    protected final GuiScreenResourcePacks resourcePacksGUI;

    public ResourcePackListEntry(GuiScreen resourcePacksGUIIn)
    {
        this.resourcePacksGUI = (GuiScreenResourcePacks) resourcePacksGUIIn;
        this.mc = Minecraft.getMinecraft();
    }

    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected)
    {
    	if (this instanceof ResourcePackListEntryFoundSubFolder) {
    		x += 15;
    		listWidth -= 15;
    	}
        int i = this.getPackFormat();

        if (i != 1)
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            Gui.drawRect(x - 1, y - 1, x + listWidth - 9, y + slotHeight + 1, -8978432);
        }

        this.bindTexture();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
        String s = this.getPackName();
        String s1 = this.getPackDescription();

        if ((this.mc.gameSettings.touchscreen || isSelected) && this.showHoverOverlay() && (this instanceof ResourcePackListEntryFolder && ((ResourcePackListEntryFolder)this).packEntry.opened))
        {
            this.mc.getTextureManager().bindTexture(RESOURCE_PACKS_TEXTURE);
            Gui.drawRect(x, y, x + 32, y + 32, -1601138544);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            int j = mouseX - x;
            int k = mouseY - y;

            if (i < 1)
            {
                s = field_183020_d.getFormattedText();
                s1 = field_183021_e.getFormattedText();
            }
            else if (i > 1)
            {
                s = field_183020_d.getFormattedText();
                s1 = field_183022_f.getFormattedText();
            }

            if (this.dontHavePackEntry())
            {
                if (j < 32)
                {
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                }
                else
                {
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                }
            }
            else
            {
                if (this.hasPackEntry())
                {
                    if (j < 16)
                    {
                        Gui.drawModalRectWithCustomSizedTexture(x, y, 32.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                    }
                    else
                    {
                        Gui.drawModalRectWithCustomSizedTexture(x, y, 32.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                    }
                }

                if (this.canMoveUp())
                {
                    if (j < 32 && j > 16 && k < 16)
                    {
                        Gui.drawModalRectWithCustomSizedTexture(x, y, 96.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                    }
                    else
                    {
                        Gui.drawModalRectWithCustomSizedTexture(x, y, 96.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                    }
                }

                if (this.canMoveDown())
                {
                    if (j < 32 && j > 16 && k > 16)
                    {
                        Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                    }
                    else
                    {
                        Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                    }
                }
            }
        }

        int width = this.mc.fontRendererObj.getStringWidth(s);

        int limit = 157;
        if (this instanceof ResourcePackListEntryFoundSubFolder) {
        	limit -= 15;
        }
        
        if (width > limit)
        {
            s = this.mc.fontRendererObj.trimStringToWidth(s, limit - this.mc.fontRendererObj.getStringWidth("...")) + "...";
        }

        this.mc.fontRendererObj.drawStringWithShadow(s, x + 32 + 2, y + 1, 16777215);
        List<String> list = this.mc.fontRendererObj.listFormattedStringToWidth(s1, limit);

        for (int l = 0; l < 2 && l < list.size(); ++l)
        {
            this.mc.fontRendererObj.drawStringWithShadow(list.get(l), x + 32 + 2, y + 12 + 10 * l, 8421504);
        }
    }

    protected abstract int getPackFormat();

    protected abstract String getPackDescription();

    protected abstract String getPackName();

    protected abstract void bindTexture();

    protected boolean showHoverOverlay()
    {
        return true;
    }

    protected boolean dontHavePackEntry()
    {
        return !this.resourcePacksGUI.hasResourcePackEntry(this);
    }

    protected boolean hasPackEntry()
    {
        return this.resourcePacksGUI.hasResourcePackEntry(this);
    }

    protected boolean canMoveUp()
    {
        List<ResourcePackListEntry> list = this.resourcePacksGUI.getListContaining(this);
        int i = list.indexOf(this);
        return i > 0 && ((ResourcePackListEntry)list.get(i - 1)).showHoverOverlay();
    }

    protected boolean canMoveDown()
    {
        List<ResourcePackListEntry> list = this.resourcePacksGUI.getListContaining(this);
        int i = list.indexOf(this);
        return i >= 0 && i < list.size() - 1 && ((ResourcePackListEntry)list.get(i + 1)).showHoverOverlay();
    }

    /**
     * Returns true if the mouse has been pressed on this control.
     */
    public boolean mousePressed(int slotIndex, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_)
    {
        if (this.showHoverOverlay() && p_148278_5_ <= 32)
        {
            if (this.dontHavePackEntry())
            {
                this.resourcePacksGUI.markChanged();
                int j = this.getPackFormat();

                if (j != 1)
                {
                    String s1 = I18n.format("resourcePack.incompatible.confirm.title", new Object[0]);
                    String s = I18n.format("resourcePack.incompatible.confirm." + (j > 1 ? "new" : "old"), new Object[0]);
                    this.mc.displayGuiScreen(new GuiYesNo(new GuiYesNoCallback()
                    {
                        public void confirmClicked(boolean result, int id)
                        {
                            List<ResourcePackListEntry> list2 = ResourcePackListEntry.this.resourcePacksGUI.getListContaining(ResourcePackListEntry.this);
                            ResourcePackListEntry.this.mc.displayGuiScreen(ResourcePackListEntry.this.resourcePacksGUI);

                            if (result)
                            {
                                list2.remove(ResourcePackListEntry.this);
                                ResourcePackListEntry.this.resourcePacksGUI.getSelectedResourcePacks().add(0, ResourcePackListEntry.this);
                            }
                        }
                    }, s1, s, 0));
                }
                else
                {
                	addOrChangePack();
                }

                return true;
            }

            if (p_148278_5_ < 16 && this.hasPackEntry())
            {
            	this.resourcePacksGUI.getListContaining(this).remove(this);
                this.resourcePacksGUI.getAvailableResourcePacks().add(0, this);
                this.resourcePacksGUI.markChanged();
                return true;
            }

            if (p_148278_5_ > 16 && p_148278_6_ < 16 && this.canMoveUp())
            {
                List<ResourcePackListEntry> list1 = this.resourcePacksGUI.getListContaining(this);
                int k = list1.indexOf(this);
                list1.remove(this);
                list1.add(k - 1, this);
                this.resourcePacksGUI.markChanged();
                return true;
            }

            if (p_148278_5_ > 16 && p_148278_6_ > 16 && this.canMoveDown())
            {
                List<ResourcePackListEntry> list = this.resourcePacksGUI.getListContaining(this);
                int i = list.indexOf(this);
                list.remove(this);
                list.add(i + 1, this);
                this.resourcePacksGUI.markChanged();
                return true;
            }
        }

        return false;
    }
    
    private void openCloseSubFolder(SubFolder packEntry) {
		//TODO: A Faire
    	packEntry.opened = !packEntry.opened;
    	packEntry.bindTexturePackIcon(mc.getTextureManager());
	}

    public void addOrChangePack() {
    	if (this instanceof ResourcePackListEntryFolder) {
			openCloseSubFolder(((ResourcePackListEntryFolder)this).getPackEntry());	
    	} else {
    		this.resourcePacksGUI.getListContaining(this).remove(this);
			this.resourcePacksGUI.getSelectedResourcePacks().add(0, this);
    	}
    }
    
    public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_)
    {
    }

    /**
     * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
     */
    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY)
    {
    }
}
