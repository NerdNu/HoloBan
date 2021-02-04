HoloBan
=======
Bukkit plugin that prevents items with additional NBT data from being spawned and placed in Creative mode. Fixes
the armor stand "hologram" exploit described [here](https://www.spigotmc.org/threads/player-created-holograms-new-exploit-1-10-2.190846/).

The code uses reflection to access CraftBukkit and NMS classes, so it should not
need to be updated for new Minecraft versions.


Configuration
-------------
To add a new NBT tag to block, add a line to `config.yml` in the format:
```
material: [tags...]
```
where `material` is the case-insensitive Bukkit Material type to check and `tags`
is a list of strings corresponding to the NBT tags to block.

To prevent the use of illegal (above maximum level) enchantments, set 
`block_illegal_enchants: true` (the default).


Commands
--------
Use `/holoban-reload` to reload the configuration.


Testing
-------
To test, ensure that you have `holoban.notify` permission and not `holoban.bypass`.
In particular, note that you must de-op yourself to avoid receiving the 
`holoban.bypass` permission.

Give yourself an invisible named armor stand using:

    /minecraft:give @p armor_stand{EntityTag:{Invisible:1,CustomNameVisible:1b,CustomName:'{"text":"Test"}'}} 1

Or an invisible item frame:

    /minecraft:give @p item_frame{EntityTag:{Invisible:1}} 1

You may refer to <https://mcstacker.net/> for other `/give` commands.


Permissions
-----------
 * `holoban.notify` - Permission to receive notifications about player actions
   blocked by the plugin.
 * `holoban.bypass` - Permission to bypass all restrictions on NBT tags and
   enchantments.
 * `holoban.admin` - Permission to run `/holoban-reload`.
