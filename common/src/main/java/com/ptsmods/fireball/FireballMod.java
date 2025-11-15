package com.ptsmods.fireball;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.phys.Vec3;

public final class FireballMod {
    public static final String MOD_ID = "fireball";

    public static void init() {
        CommandRegistrationEvent.EVENT.register((dispatcher, ctx, cmdSelection) ->
                registerFireballCommand(dispatcher));
    }

    private static void registerFireballCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("fireball")
                .requires(cs -> cs.hasPermission(2))
                .executes(ctx -> execute(ctx, 0))
                .then(Commands.argument("power", IntegerArgumentType.integer(0))
                        .executes(ctx -> execute(ctx, IntegerArgumentType.getInteger(ctx, "power"))))
        );
    }

    private static int execute(CommandContext<CommandSourceStack> ctx, int power) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();
        ServerLevel level = source.getLevel();
        Vec3 pos = source.getPosition();
        ServerPlayer player = source.getPlayerOrException();
        Vec3 forward = player.getForward();

        pos = pos.add(0, player.getEyeHeight(), 0);

        Vec3 velocity = forward.scale(2);
        Vec3 acceleration = velocity.scale(1 / 19f);

        // (x + y) * 0.95 = x
        // 0.95x + 0.95y = x
        // -0.05x + 0.95y = 0
        // 0.05x = 0.95y
        // x = 19y
        // y = 1/19 x

        LargeFireball fireball = new LargeFireball(level, player, 0, 0, 0, power);
        fireball.setPos(pos);
        fireball.setDeltaMovement(velocity);
        fireball.xPower = acceleration.x;
        fireball.yPower = acceleration.y;
        fireball.zPower = acceleration.z;
        level.addFreshEntity(fireball);
        return 1;
    }
}
