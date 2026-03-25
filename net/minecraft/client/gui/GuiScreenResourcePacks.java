package net.minecraft.client.gui;

import com.google.common.collect.Lists;

import fr.honertis.Honertis;
import fr.honertis.utils.ChatUtils;
import fr.honertis.utils.LangManager;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.ResourcePackListEntry;
import net.minecraft.client.resources.ResourcePackListEntryDefault;
import net.minecraft.client.resources.ResourcePackListEntryFolder;
import net.minecraft.client.resources.ResourcePackListEntryFound;
import net.minecraft.client.resources.ResourcePackListEntryFoundSubFolder;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.resources.ResourcePackRepository.SubFolder;
import net.minecraft.client.resources.ResourcePackRepository.SubFolderPack;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.Sys;

public class GuiScreenResourcePacks extends GuiScreen
{
    private static final Logger logger = LogManager.getLogger();
    private final GuiScreen parentScreen;
    private List<ResourcePackListEntry> availableResourcePacks;
    private List<ResourcePackListEntry> selectedResourcePacks;

    /** List component that contains the available resource packs */
    private GuiResourcePackAvailable availableResourcePacksList;

    /** List component that contains the selected resource packs */
    private GuiResourcePackSelected selectedResourcePacksList;
    private boolean changed = false;

    public GuiScreenResourcePacks(GuiScreen parentScreenIn)
    {
        this.parentScreen = parentScreenIn;
    }
    private static List<ResourcePackRepository.Entry> cachedPackList = null;
    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui()
    {
        this.buttonList.add(new GuiOptionButton(2, this.width / 2 - 154, this.height - 48, I18n.format("resourcePack.openFolder", new Object[0])));
        this.buttonList.add(new GuiOptionButton(1, this.width / 2 + 4, this.height - 48, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(new GuiButton(99, 10, 10, 70, 20, LangManager.format("gui.refresh")));
        if (!this.changed) {
			this.availableResourcePacks = Lists.<ResourcePackListEntry>newArrayList();
			this.selectedResourcePacks = Lists.<ResourcePackListEntry>newArrayList();
			ResourcePackRepository resourcepackrepository = this.mc.getResourcePackRepository();
			if (Honertis.INSTANCE.packsThread == null) {
				(Honertis.INSTANCE.packsThread = 
				new Thread(() -> {
					if (cachedPackList == null) {
						resourcepackrepository.updateRepositoryEntriesAll();
				    	cachedPackList = new ArrayList<>(resourcepackrepository.getRepositoryEntriesAll());
					}
					List<ResourcePackRepository.Entry> list = cachedPackList;
					list.removeAll(resourcepackrepository.getRepositoryEntries());
	
					for (ResourcePackRepository.Entry resourcepackrepository$entry : list) {
						if (resourcepackrepository$entry instanceof SubFolder) {
							this.availableResourcePacks.add(new ResourcePackListEntryFolder(this, (SubFolder) resourcepackrepository$entry));
						} else
						if (resourcepackrepository$entry instanceof SubFolderPack) {
							this.availableResourcePacks.add(new ResourcePackListEntryFoundSubFolder(this, (SubFolderPack)resourcepackrepository$entry));
						} else {
							this.availableResourcePacks.add(new ResourcePackListEntryFound(this, resourcepackrepository$entry));
						}
					}
					if (!(mc.currentScreen instanceof GuiScreenResourcePacks))
						ChatUtils.printChat(LangManager.format("gui.packs.loaded"));
					Honertis.INSTANCE.packsThread = null;
				}, "Resource Packs Thread")).start();
			}
			for (ResourcePackRepository.Entry resourcepackrepository$entry1 : Lists
					.reverse(resourcepackrepository.getRepositoryEntries())) {
				if (resourcepackrepository$entry1 instanceof SubFolderPack) {
					this.selectedResourcePacks.add(new ResourcePackListEntryFoundSubFolder(this, (SubFolderPack)resourcepackrepository$entry1));
				} else {
					this.selectedResourcePacks.add(new ResourcePackListEntryFound(this, resourcepackrepository$entry1));
				}
				//this.selectedResourcePacks.add(new ResourcePackListEntryFound(this, resourcepackrepository$entry1));
			}

			this.selectedResourcePacks.add(new ResourcePackListEntryDefault(this));
		}

        this.availableResourcePacksList = new GuiResourcePackAvailable(this.mc, 200, this.height, this.availableResourcePacks);
        this.availableResourcePacksList.setSlotXBoundsFromLeft(this.width / 2 - 4 - 200);
        this.availableResourcePacksList.registerScrollButtons(7, 8);
        this.selectedResourcePacksList = new GuiResourcePackSelected(this.mc, 200, this.height, this.selectedResourcePacks);
        this.selectedResourcePacksList.setSlotXBoundsFromLeft(this.width / 2 + 4);
        this.selectedResourcePacksList.registerScrollButtons(7, 8);
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
        this.selectedResourcePacksList.handleMouseInput();
        this.availableResourcePacksList.handleMouseInput();
    }

    public boolean hasResourcePackEntry(ResourcePackListEntry p_146961_1_)
    {
        return this.selectedResourcePacks.contains(p_146961_1_);
    }

    public List<ResourcePackListEntry> getListContaining(ResourcePackListEntry p_146962_1_)
    {
        return this.hasResourcePackEntry(p_146962_1_) ? this.selectedResourcePacks : this.availableResourcePacks;
    }

    public List<ResourcePackListEntry> getAvailableResourcePacks()
    {
        return this.availableResourcePacks;
    }

    public List<ResourcePackListEntry> getSelectedResourcePacks()
    {
        return this.selectedResourcePacks;
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            if (button.id == 2)
            {
            	File file1 = this.mc.getResourcePackRepository().getDirResourcepacks();

				fr.honertis.utils.Utils.openPath(file1.getAbsolutePath(), file1);
            }
            else if (button.id == 1)
            {
                if (this.changed)
                {
                    List<ResourcePackRepository.Entry> list = Lists.<ResourcePackRepository.Entry>newArrayList();

                    for (ResourcePackListEntry resourcepacklistentry : this.selectedResourcePacks)
                    {
                        if (resourcepacklistentry instanceof ResourcePackListEntryFound)
                        {
                            list.add(((ResourcePackListEntryFound)resourcepacklistentry).func_148318_i());
                        }
                    }

                    Collections.reverse(list);
                    this.mc.getResourcePackRepository().setRepositories(list);
                    this.mc.gameSettings.resourcePacks.clear();
                    this.mc.gameSettings.incompatibleResourcePacks.clear();

                    for (ResourcePackRepository.Entry resourcepackrepository$entry : list)
                    {
                        this.mc.gameSettings.resourcePacks.add(resourcepackrepository$entry.getResourcePackName());

                        if (resourcepackrepository$entry.getPackFormat() != 1)
                        {
                            this.mc.gameSettings.incompatibleResourcePacks.add(resourcepackrepository$entry.getResourcePackName());
                        }
                    }

                    this.mc.gameSettings.saveOptions();
                    this.mc.refreshResources();
                }

                this.mc.displayGuiScreen(this.parentScreen);
            } else if (button.id == 99) {
    		    cachedPackList = null;
    		    changed = false;
    		    mc.displayGuiScreen(this);
    		}
        }
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.availableResourcePacksList.mouseClicked(mouseX, mouseY, mouseButton);
        this.selectedResourcePacksList.mouseClicked(mouseX, mouseY, mouseButton);
    }

    /**
     * Called when a mouse button is released.  Args : mouseX, mouseY, releaseButton
     */
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        super.mouseReleased(mouseX, mouseY, state);
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawBackground(0);
        this.availableResourcePacksList.drawScreen(mouseX, mouseY, partialTicks);
        this.selectedResourcePacksList.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, I18n.format("resourcePack.title", new Object[0]), this.width / 2, 16, 16777215);
        this.drawCenteredString(this.fontRendererObj, I18n.format("resourcePack.folderInfo", new Object[0]), this.width / 2 - 77, this.height - 26, 8421504);
        if (Honertis.INSTANCE.packsThread != null)
        	mc.fontRendererObj.drawCenteredStringWithShadow(LangManager.format("gui.loading"), width /2 - 77 - mc.fontRendererObj.getStringWidth(LangManager.format("gui.loading"))/2, height/2 - mc.fontRendererObj.FONT_HEIGHT, Color.LIGHT_GRAY.getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /**
     * Marks the selected resource packs list as changed to trigger a resource reload when the screen is closed
     */
    public void markChanged()
    {
        this.changed = true;
    }
}
