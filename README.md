[![GitHub tag](https://img.shields.io/github/tag/justADeni/ColorBlock?include_prereleases=&sort=semver&color=blue)](https://github.com/justADeni/ColorBlock/releases/)
[![License](https://img.shields.io/badge/License-BSD--3_-blue)](#license)

# ColorBlock
allows you to color block by clicking it with dye

*To download, go to https://github.com/prosteDeniGC/ColorBlock/releases*

### How does it work?

When you click a block that can have color, such as wool, shulker box, glass pane, etc. This block will gain color of the dye you're holding in your hand, and you will lose one dye. If you're in creative mode, you won't lose it. You also won't lose it if clicked block already has color corresponding with the dye in your hand. By shift + right clicking you can get the color back and the block resets to white/blank.

### Permissions

* colorblock.dye, or having OP

allows you to use this plugin

* colorblock.undye

allows you to get rid of color by shift + right clicking colored block

* colorblock.worldguard

allows you to use this plugin in worldguard regions

### Config

* WorldGuardHook: false

when set to true, will check if WorldGuard plugin is installed and use it

* Sound: true

when set to true, any action will make note pling sound

### WorldGuard hook

When you have WorldGuard plugin installed and **WorldGuardHook: true** in settings, only players with **colorblock.worldguard** permission will be able to use the plugin when in region
