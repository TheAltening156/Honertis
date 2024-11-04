package fr.honertis.manager;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.resources.ResourcePackListEntry;

public class ResourcePackManager {
	public List<ResourcePackListEntry> availableResourcePacks = null;
    public List<ResourcePackListEntry> selectedResourcePacks = null;

    public ResourcePackManager() {}

    public List<ResourcePackListEntry> getAvailableResourcePacks() {
        return availableResourcePacks;
    }

    public List<ResourcePackListEntry> getSelectedResourcePacks() {
        return selectedResourcePacks;
    }
    
    public void setAvailableResourcePacks(List<ResourcePackListEntry> packs) {
        this.availableResourcePacks = packs;
    }

    public void setSelectedResourcePacks(List<ResourcePackListEntry> packs) {
        this.selectedResourcePacks = packs;
    } 
    
}
