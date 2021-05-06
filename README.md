# About Discord Wire:
Discord Wire is a paper plugin that connects a paper server and discord together. Currently users can register by giving a discord command: `!register`.
It will send the user a dm containing a registration key to bring to minecraft where a command: `/discord register [unique key]` can be used to register.

After the registration process is completed when the user sends a message in minecraft it will be forwarded to discord and use their discord username.

## Data Storage:
Currently the only storage method is in JSON format, but MySql format will follow soon.
MongoDB might be implemented in the future.

Any requested database format will be considered.

## Features:
Planned and implemented features:
* - [x] Minecraft chat mirrored into Discord
* - [x] [PaperMC](https://papermc.io/) support
* - [ ] [Spigot](https://www.spigotmc.org/) support
* - [ ] Multiple Server Support
* - [ ] MySQL database
* - [ ] MongoDB datase
* - [ ] Discord Permissions

### Limitations:
Current limitations:
* Only [PaperMC](https://papermc.io/) is supported.
* Only Minecraft 1.16 is supported.
* Discord does not havy any permissions system anyone can change the default channel.

## Acknowledgements
### Contributers:
* [heinGertnbach](https://github.com/heinGertenbach)
* @UdoElleh

### 3rd-party softare
I would like to thank the following projects and people maintaining them:
* [PaperMC](https://papermc.io/)
* [Spigot](https://www.spigotmc.org/)
* [Discord4J](https://discord4j.com/)
