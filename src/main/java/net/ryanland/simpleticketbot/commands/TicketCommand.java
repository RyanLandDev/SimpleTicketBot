package net.ryanland.simpleticketbot.commands;

import net.ryanland.colossus.command.BaseCommand;
import net.ryanland.colossus.command.CommandException;
import net.ryanland.colossus.command.arguments.ArgumentSet;
import net.ryanland.colossus.command.regular.CommandBuilder;
import net.ryanland.colossus.command.regular.SlashCommand;
import net.ryanland.colossus.command.regular.SubCommand;
import net.ryanland.colossus.command.regular.SubCommandHolder;
import net.ryanland.colossus.events.command.SlashCommandEvent;

import java.util.List;

@CommandBuilder(
    name = "ticket",
    description = "Ticket action commands."
)
public class TicketCommand extends BaseCommand implements SubCommandHolder {

    @Override
    public List<SubCommand> registerSubCommands() {
        return List.of(new TicketCloseCommand(), new TicketAddCommand(), new TicketRemoveCommand());
    }

    @Override
    public ArgumentSet getArguments() {
        return null;
    }
}
