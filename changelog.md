------------------------------------------------------
Version 1.3.0
------------------------------------------------------
Added
- Noctis Rozuli worldgen, mineable on night, including veins exposed to air. Nitrogen and Cryonoctis chemicals.
- Lunar generators, from Basic to Creative, and a Lunar Neutron Activator.
- Evolved wind generators, from Advanced to Creative. Output still depends on height, then the tier multiplier is applied.
- Fluid Combiner (two molten fluids in, one out) and Fluid Alloyer (three in, one out).
- Molten alloys follow the existing item recipes. One ingot, dust, or gem is 90 mB. Combiner: bronze, electrum, invar, constantan, rose gold, brass, manyullyn, pewter, netherite. Alloyer: hepatizon, signalum, lumium, enderium, dark steel. Netherite keeps the vanilla 4 ancient debris + 4 gold = 1 ingot ratio.
- Anchor upgrade on the alloyer, both fluid machines, the chemixer, the melter, the solidifier, and the evolved solar, lunar, and wind generators. The Lunar Neutron Activator does not take it.
- Curios slots for the canteen, portable QIO dashboard, dosimeter, Geiger counter, HDPE elytra, portable hazmat suit, energy tablets, portable teleporter, scuba mask and tank, free runners, armored free runners, jetpack, and armored jetpack.
- The portable hazmat suit toggles with Mekanism's Item Mode Switch key.
- Alloyer recipes for ExtendedAE, MEGA Cells, Applied Generators, AE2 Lightning Tech, AE2 Omni Cells, Data Energistics, and NeoECO AE, when those mods are installed.
- EMI categories, including the APT and the chemixer, plus EMI workstations and JEI/EMI search aliases.
- Capturing Unit can be used on [Mekanism Weapons](https://www.curseforge.com/minecraft/mc-mods/mekanism-weapons) items.
- Enchanting Station. Sets enchantment levels with cryonoctis and energy, and plays the vanilla enchanting sound when it finishes. Curses are shown in red and cannot be lowered. Other enchantments can still be removed, and the book is not returned.
- Disenchantment Laser.
- Sortifichantor. Sends enchanted items to one side and plain items to the other.
- Advanced Vision Enhancement Unit for the MekaSuit. Night vision from the standard unit, with a color you can set (and fog deactivation).
- EnderIO Evolution molten fluids, with alloyer and fluid recipes when that mod is installed: crude steel, construction alloy, energetic silver, vivid alloy, crystalline alloy, crystalline pink slime, melodic alloy, and stellar alloy. Vivid, and crystalline pink slime made from Industrial Foregoing pink slime, are fluid combiners. Crystalline alloy (both recipes), melodic alloy, crude steel, and construction alloy stay on the item alloyer. Stellar alloy is both, and uses 4 clay. The energetic silver fluid recipe uses silver only.
- Ender pearls, clay balls, slime balls, nether stars, and Industrial Foregoing pink slime can be melted.

Changed
- Solar, lunar, and wind tier multipliers now scale by x4 per tier, starting at x8. They are configurable in `config/EvolvedMekanism/generators.toml` and still sit on top of Mekanism Generators' own rates. The single-block lunar generator keeps using solarGeneration.
- Melting outputs and solidifying inputs use `forge:molten_*` tags, so another mod's molten fluid is accepted when it is in the tag. Recipes with an empty tag are not loaded.
- Amethyst, Glowstone, Quartz, Certus Quartz, and Fluix blocks melt and cast at 360 mB. A block of those is not nine items.
- Better gold in the APT no longer requires spent nuclear waste.
- The Maximum Tier Installer stops at the tier set in `general.toml` (maxInstallerTier, default Multiversal). It no longer stacks a Creative machine onto a Creative one.
- Tier installers work on the alloyer and on Basic, Advanced, and Elite alloying factories.
- Alloy blocks are no longer valid Oredictionificator filters. `allowAlloyBlocksInOredictionificator` turns the old behavior back on.
- EnderIO alloys match EnderIO 6.2. Conductive alloy is a fluid alloyer (copper alloy + iron + redstone, 90 mB). Energetic alloy is redstone + gold + glowstone, 90 mB. Vibrant alloy and pulsating alloy are fluid combiners and output 90 mB. Redstone alloy has no fluid recipe, because 6.2 uses silicon. Dark steel fluid output is 90 mB.
- Energy use and storage for the alloyer, both fluid machines, the chemixer, the melter, the solidifier, the Enchanting Station, and the Disenchantment Laser are configurable in `general.toml`, along with the Lunar Neutron Activator rate and melter heat loss. Evolved storage and cable tiers are in `tiers.toml`. Ore regeneration and the Curios radiation decay timer are configurable too.

Fixed
- Creative energy cables crashing the server when two full networks merged, because the stored energy overflowed past `Long.MAX_VALUE`. (#49)
- Breaking a logistical transporter that held a huge stack spawning one item entity per 64 items and freezing the world. Oversized stacks now drop as one entity. (#48)
- QIO drive loading crashes (`NoClassDefFoundError` / NPE), including QIO v2, in heavy modpacks. (#4, #68, #74)
- JEI loading EMItems before the extra QIO drive tiers existed. (#52)
- Mixin crashes with Supplementaries, and with Crabber's Delight signs when Mekanism Tools was also installed. (#40, #53)
- Client crash on startup with Goetic Legacy. Missing generated data. (#85)
- Crash when Evolved Mekanism, Mekanism More Machine, and Mekanism Extras were installed together. Vanilla Mekanism registries were registered twice. (#80)
- Ender IO conductive alloy recipe pointing at an empty copper alloy tag. (#75)
- ProjectE kicking players on a server with a network protocol error. (#56)
- Solidification chamber refusing to run while the output still had room, and crashing when its tooltip was shown with Shift. (#58, #82)
- `IndexOutOfBoundsException` when opening an inventory.
- APT ports logging an error on world load because their energy was not read back. The multiblock itself kept working. (#76)
- `general.toml` being rewritten every second by the config watcher. (#55)
- EMI missing APT and chemixer recipes, and listing molten fluids from mods that were not installed. (#54, #57, #64, #67)
- Evolved Mekanism upgrades disappearing from machines after a world reload.
- Solar and lunar generators losing their item when broken, plus a related visual bug and dupe.
- Wrong default side configuration on machines.
- Creative fluid tanks for a fluid whose material is not in the pack.
- Client crash while drawing an extra gauge on a machine.

Removed
- Extra ore variants on other stones (depthrock, holystone, end stone, netherrack, and the same kind of copies).
