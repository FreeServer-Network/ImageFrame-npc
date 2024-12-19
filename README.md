[![Build Status](https://ci.loohpjames.com/job/ImageFrame/badge/icon)](https://ci.loohpjames.com/job/ImageFrame/)
# ImageFrame (Modified)
I modified the plugin to make my server able to refresh the image automatically with NPC loading it.
## usage:
1. install citizens plugin
2. create map with `/imageframe create mapName ....`
3. create a NPC `/npc create ...` and get the ID
4. assign the map to the NPC `/imagemap npc <ID> mapName`
Done! now you can refresh the map with `/imagemap npc refresh <ID> <URL>`

# Original Readme:
https://www.spigotmc.org/resources/106031/

Put images on maps and walls!

More information (screenshots, commands, permissions) about the plugin can be found on the Spigot page linked above.

## Built against Spigot
Built against [Spigot's API](https://www.spigotmc.org/wiki/buildtools/) (required mc versions are listed on the spigot page above).
Plugins built against Spigot usually also work with [Paper](https://papermc.io/).

## Development Builds

- [Jenkins](https://ci.loohpjames.com/job/ImageFrame/)

## Maven
```html
<repository>
  <id>loohp-repo</id>
  <url>https://repo.loohpjames.com/repository</url>
</repository>
```
```html
<dependency>
  <groupId>com.loohp</groupId>
  <artifactId>ImageFrame</artifactId>
  <version>VERSION</version>
  <scope>provided</scope>
</dependency>
```
Replace `VERSION` with the version number.

## Partnerships

### Server Hosting
**Use the link or click the banner** below to **get a 25% discount off** your first month when buying any of their gaming servers!<br>
It also **supports my development**, take it as an alternative way to donate while getting your very own Minecraft server as well!

*P.S. Using the link or clicking the banner rather than the code supports me more! (Costs you no extra!)*

**https://www.bisecthosting.com/loohp**

![https://www.bisecthosting.com/loohp](https://www.bisecthosting.com/partners/custom-banners/fc7f7b10-8d1a-4478-a23a-8a357538a180.png)
