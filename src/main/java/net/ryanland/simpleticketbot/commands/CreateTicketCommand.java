package net.ryanland.simpleticketbot.commands;

import net.ryanland.colossus.command.BaseCommand;
import net.ryanland.colossus.command.CommandException;
import net.ryanland.colossus.command.arguments.ArgumentSet;
import net.ryanland.colossus.command.regular.CommandBuilder;
import net.ryanland.colossus.command.regular.SlashCommand;
import net.ryanland.colossus.events.command.SlashCommandEvent;
import net.ryanland.simpleticketbot.TicketHandler;

@CommandBuilder(
    name = "createticket",
    description = "Creates a ticket."
)
public class CreateTicketCommand extends BaseCommand implements SlashCommand {
    @Override
    public ArgumentSet getArguments() {
        return null;
    }

    @Override
    public void run(SlashCommandEvent event) throws CommandException {
        TicketHandler.createTicket(event);
    }
}
