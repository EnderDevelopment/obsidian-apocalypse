# Minecraft Plugin: ObsidianApocalypse

ObsidianApocalypse is a Minecraft plugin for Paper servers that creates a staged, three-dimensional obsidian omen structure at the world spawn and tracks its progression across seven real-world days. It includes a persistent seven-day countdown, automatic structure updates, and an `/omen` status command.

## Install

1. Install Java 21.
2. Run the project build with Maven:

```bash
mvn package
```

3. Copy the generated `target/plugin.jar` file into your Paper server's `plugins` folder.
4. Start or restart the server.
5. Confirm that the server is running Paper 1.21.11 or a compatible Paper 1.21 server.

The plugin creates its configuration file automatically. The countdown start time is stored in `plugins/SevenDayOmen/config.yml` and persists across server restarts.

## Use

1. Join the server and visit the main world's spawn location.
2. Observe the obsidian structure, which grows through seven stages over seven real-world days.
3. Run the following command to view the current stage and remaining time:

```text
/omen
```

The command displays the current omen day from 1 to 7 and the remaining time in days and hours. Players are allowed to use the command by default through the `sevendayomen.status` permission.

The plugin currently creates only the staged obsidian display structure and status countdown. It does not implement a creator character, dialogue, books, item rewards, player attacks, or a destruction event.

---

More Minecraft plugin projects by EnderDevelopment: https://github.com/EnderDevelopment

Project page: https://dash.enderdevelopment.com/community/6ccda39d-56f7-4b93-af41-c06a2302e3a8?utm_source=github&utm_medium=readme&utm_campaign=auto_publish&utm_content=6ccda39d-56f7-4b93-af41-c06a2302e3a8
