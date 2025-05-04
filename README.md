# DisableVillagers

A lightweight **Fabric** mod to control or remove villagers and related game mechanics.

## Configuration

All options are set in the `disable-villagers.json` file, located in your `.minecraft/config/` directory.
You must manually edit this file to change behavior.

### Available Options

* **`killVillagers`**
  Instantly removes all loaded villagers.
  *Effect: No villagers will be visible or interactable.*

* **`disableWanderingTrader`**
  Prevents wandering traders from spawning.

* **`blockTrading`**
  Disables opening the trading interface.

* **`spareExperiencedVillagers`**
  Only works if `killVillagers` is `true`.
  Villagers that have been traded with (i.e., have XP) will be spared.
  So if you want to keep some villagers but prevent more from spawning, set this to true.

* **`breeding`**
  Enables or disables villager breeding.

* **`disableVillages`**
  Stops village structures from generating in the world.

* **`disableZombies`**
  Prevents zombie villagers from spawning randomly.

* **`curableZombies`**
  If `false`, zombie villagers cannot be cured with golden apples, even after applying Weakness.

* **`curedZombieLoot`**

  * `null`: Zombie villagers cure normally.
  * Custom loot table: Healed zombie villagers die and drop specified loot.
    Create loot tables at [misode.github.io/loot-table](https://misode.github.io/loot-table/)
