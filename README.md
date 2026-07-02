# NoBadBooms!

A server-side NeoForge **1.21.1** mod that adds a `tntExplodes` gamerule — similar to
Bedrock Edition — letting you stop TNT from destroying blocks.

## Usage

```
/gamerule tntExplodes false
```

When `false`, TNT blocks and TNT minecarts still ignite and detonate (fuse, sound,
particles, knockback, and entity damage all still happen), but **no blocks are
destroyed**. Default is `true` (vanilla behavior).

The rule is per-world and saved like any vanilla gamerule.

> Note: this differs slightly from Bedrock, where `tntExplodes false` prevents TNT
> from igniting at all. Here the explosion still occurs — only terrain damage is
> disabled. Other explosion sources (creepers, ghasts, beds, end crystals) are
> unaffected; creepers are already covered by vanilla's `mobGriefing`.

## Server-side only

Install the jar in the **server's** `mods` folder. Vanilla / unmodded clients can
join normally. It also works in single-player if installed on a client.


## How it works

- Registers the gamerule with `GameRules.register("tntExplodes", Category.MISC, BooleanValue.create(true))`
  during mod class initialization (NeoForge's access transformers make these public).
- Listens to `ExplosionEvent.Detonate`, which fires after the explosion has computed
  its affected blocks but before anything is destroyed. If the explosion's direct
  source entity is a `PrimedTnt` or `MinecartTNT` and the gamerule is `false`, the
  affected-block list is cleared.
