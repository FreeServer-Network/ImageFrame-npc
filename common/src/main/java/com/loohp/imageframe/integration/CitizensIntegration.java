package com.loohp.imageframe.integration;

import com.loohp.imageframe.ImageFrame;
import com.loohp.imageframe.objectholders.ImageMap;
import com.loohp.imageframe.objectholders.Scheduler;
import com.loohp.imageframe.objectholders.URLImageMap;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitInfo;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;

public class CitizensIntegration {

    private static final String TRAIT_NAME = "imagemap";
    private static boolean registered = false;

    public static void initialize() {
        if (!registered && CitizensAPI.getPlugin() != null) {
            try {
                CitizensAPI.getTraitFactory().registerTrait(
                        TraitInfo.create(ImageMapTrait.class).withName(TRAIT_NAME)
                );
                registered = true;
                ImageFrame.plugin.getLogger().info("Successfully registered ImageMap trait with Citizens!");
            } catch (Exception e) {
                ImageFrame.plugin.getLogger().severe("Failed to register ImageMap trait with Citizens: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static boolean addImageMapToNPC(NPC npc, ImageMap imageMap) {
        if (!registered || npc == null || imageMap == null) {
            return false;
        }

        try {
            ImageMapTrait trait = npc.getOrAddTrait(ImageMapTrait.class);
            trait.setImageMap(imageMap);
            return true;
        } catch (Exception e) {
            ImageFrame.plugin.getLogger().severe("Failed to add ImageMap to NPC: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateNPCMapUrl(NPC npc, String newUrl) {
        if (!registered || npc == null || newUrl == null) {
            return false;
        }

        try {
            ImageMapTrait trait = npc.getTraitNullable(ImageMapTrait.class);
            if (trait == null || trait.getImageMap() == null) {
                return false;
            }

            ImageMap imageMap = trait.getImageMap();
            if (!(imageMap instanceof URLImageMap)) {
                return false;
            }

            URLImageMap urlImageMap = (URLImageMap) imageMap;
            String oldUrl = urlImageMap.getUrl();
            urlImageMap.setUrl(newUrl);

            try {
                Scheduler.runTaskAsynchronously(ImageFrame.plugin, () -> {
                    try {
                        imageMap.update();
                        trait.updateNPCMap();
                    } catch (Exception e) {
                        urlImageMap.setUrl(oldUrl);
                        ImageFrame.plugin.getLogger().severe("Failed to update NPC map URL: " + e.getMessage());
                        e.printStackTrace();
                    }
                });
                return true;
            } catch (Exception e) {
                urlImageMap.setUrl(oldUrl);
                throw e;
            }
        } catch (Exception e) {
            ImageFrame.plugin.getLogger().severe("Failed to update NPC map URL: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static class ImageMapTrait extends Trait {
        private ImageMap imageMap;

        public ImageMapTrait() {
            super(TRAIT_NAME);
        }

        public void setImageMap(ImageMap map) {
            this.imageMap = map;
            if (getNPC().isSpawned()) {
                updateNPCMap();
            }
        }

        public ImageMap getImageMap() {
            return imageMap;
        }

        @Override
        public void onSpawn() {
            updateNPCMap();
        }

        public void updateNPCMap() {
            try {
                if (imageMap != null && getNPC().isSpawned() && getNPC().getEntity() instanceof org.bukkit.entity.Player) {
                    MapView mapView = imageMap.getMapViews().get(0);
                    ItemStack mapItem = new ItemStack(Material.FILLED_MAP);
                    mapItem.setDurability((short) mapView.getId());
                    ((org.bukkit.entity.Player) getNPC().getEntity()).getInventory().setItemInMainHand(mapItem);
                }
            } catch (Exception e) {
                ImageFrame.plugin.getLogger().warning("Failed to update NPC map: " + e.getMessage());
            }
        }
    }
}