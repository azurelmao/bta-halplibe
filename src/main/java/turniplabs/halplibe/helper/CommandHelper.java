package turniplabs.halplibe.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.Commands;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.ApiStatus;
import turniplabs.halplibe.HalpLibe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

abstract public class CommandHelper {
    @ApiStatus.Internal // used in CommandsCoreMixin
    private static final List<Command> coreCommands = new ArrayList<>();
    @ApiStatus.Internal // used in CommandsClientMixin
    private static final List<Function<AtomicReference<Minecraft>, Command>> clientCommands = new ArrayList<>();
    @ApiStatus.Internal // used in CommandsServerMixin
    private static final List<Function<AtomicReference<MinecraftServer>, Command>> serverCommands = new ArrayList<>();

    /**
     * @param command Command to be added to the commands list
     */
    public static void createCommand(Command command) {
        coreCommands.add(command);
        if (!Commands.commands.isEmpty()){ // If commands already initialized add directly
            Commands.commands.add(command);
        }
    }
    /**
     * @param clientCommand Function that returns a client command when supplied with an AtomicReference<Minecraft>
     */
    @SuppressWarnings("unused") // API function
    public static void createClientCommand(Function<AtomicReference<Minecraft>, Command> clientCommand) {
        if (!HalpLibe.isClient) return;
        clientCommands.add(clientCommand);
        if (!Commands.commands.isEmpty()){
            Commands.commands.add(clientCommand.apply(new AtomicReference<>(Minecraft.getMinecraft(Minecraft.class))));
        }
    }
    /**
     * @param serverCommand Function that returns a server command when supplied with an AtomicReference<Minecraft>
     */
    @SuppressWarnings("unused") // API function
    public static void createServerCommand(Function<AtomicReference<MinecraftServer>, Command> serverCommand){
        if (HalpLibe.isClient) return;
        serverCommands.add(serverCommand);
        if (!Commands.commands.isEmpty()){
            Commands.commands.add(serverCommand.apply(new AtomicReference<>(MinecraftServer.getInstance())));
        }
    }


}
