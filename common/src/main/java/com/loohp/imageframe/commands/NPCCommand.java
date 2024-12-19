package com.loohp.imageframe.commands;

import com.loohp.imageframe.ImageFrame;
import com.loohp.imageframe.integration.CitizensIntegration;
import com.loohp.imageframe.objectholders.ImageMap;
import com.loohp.imageframe.objectholders.ImageMapAccessPermissionType;
import com.loohp.imageframe.utils.ImageMapUtils;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class NPCCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("imageframe.npc")) {
            sender.sendMessage(ImageFrame.messageNoPermission);
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage("§cUsage:");
            sender.sendMessage("§c/imagemap npc <npc-id> <map-name> - Assign a map to NPC");
            sender.sendMessage("§c/imagemap npc refresh <npc-id> <new-url> - Update NPC's map URL");
            return true;
        }

        try {
            if (args[1].equalsIgnoreCase("refresh")) {
                if (args.length < 4) {
                    sender.sendMessage("§cUsage: /imagemap npc refresh <npc-id> <new-url>");
                    return true;
                }

                int npcId = Integer.parseInt(args[2]);
                NPC npc = CitizensAPI.getNPCRegistry().getById(npcId);

                if (npc == null) {
                    sender.sendMessage("§cNPC not found with ID: " + npcId);
                    return true;
                }

                String newUrl = args[3];
                if (!ImageFrame.isURLAllowed(newUrl)) {
                    sender.sendMessage(ImageFrame.messageURLRestricted);
                    return true;
                }

                if (CitizensIntegration.updateNPCMapUrl(npc, newUrl)) {
                    sender.sendMessage("§aSuccessfully updated NPC's map URL!");
                } else {
                    sender.sendMessage("§cFailed to update NPC's map URL. Make sure the NPC has a URL-based map assigned.");
                }
                return true;
            }

            // Original assign map logic
            int npcId = Integer.parseInt(args[1]);
            NPC npc = CitizensAPI.getNPCRegistry().getById(npcId);

            if (npc == null) {
                sender.sendMessage("§cNPC not found with ID: " + npcId);
                return true;
            }

            ImageMap imageMap = ImageMapUtils.getFromPlayerPrefixedName(sender, args[2]);
            if (imageMap == null) {
                sender.sendMessage(ImageFrame.messageNotAnImageMap);
                return true;
            }

            if (!ImageFrame.hasImageMapPermission(imageMap, sender, ImageMapAccessPermissionType.GET)) {
                sender.sendMessage(ImageFrame.messageNoPermission);
                return true;
            }

            if (CitizensIntegration.addImageMapToNPC(npc, imageMap)) {
                sender.sendMessage("§aSuccessfully assigned image map to NPC!");
            } else {
                sender.sendMessage("§cFailed to assign image map to NPC. Check console for details.");
            }

        } catch (NumberFormatException e) {
            sender.sendMessage("§cInvalid NPC ID number format");
        } catch (Exception e) {
            sender.sendMessage("§cAn error occurred while processing the command");
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 2) {
            completions.add("refresh");
            if (args[1].isEmpty() || args[1].matches("\\d*")) {
                completions.add("<npc-id>");
            }
        } else if (args.length == 3) {
            if (args[1].equalsIgnoreCase("refresh")) {
                if (args[2].isEmpty() || args[2].matches("\\d*")) {
                    completions.add("<npc-id>");
                }
            } else {
                completions.add("<map-name>");
            }
        } else if (args.length == 4 && args[1].equalsIgnoreCase("refresh")) {
            completions.add("<new-url>");
        }

        return completions;
    }
}