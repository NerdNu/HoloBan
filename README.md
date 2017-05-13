HoloBan
=======
Bukkit plugin that prevents items with additional NBT data from being spawned and placed in Creative mode. Fixes
the armor stand "hologram" exploit described [here](https://www.spigotmc.org/threads/player-created-holograms-new-exploit-1-10-2.190846/).

Uses CraftBukkit/NMS, so this must be updated for each version of Minecraft.

To add a new NBT tag to block, add a line to `config.yml` in the format:
```
material: [tags...]
```
where `material` is the case-insensitive Bukkit Material type to check and `tags` is a list of strings corresponding to
the NBT tags to block. The configuration can be reloaded by anyone with the `holoban.admin` permissions with the
`/holoban-reload` command.
