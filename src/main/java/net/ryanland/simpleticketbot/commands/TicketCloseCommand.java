package net.ryanland.simpleticketbot.commands;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.BaseCommand;
import net.ryanland.colossus.command.CommandException;
import net.ryanland.colossus.command.arguments.ArgumentSet;
import net.ryanland.colossus.command.regular.CommandBuilder;
import net.ryanland.colossus.command.regular.SlashCommand;
import net.ryanland.colossus.command.regular.SubCommand;
import net.ryanland.colossus.events.command.SlashCommandEvent;
import net.ryanland.simpleticketbot.TicketHandler;

@CommandBuilder(
    name = "close",
    description = "Closes the ticket in this channel."
)
public class TicketCloseCommand extends BaseCommand implements SlashCommand, SubCommand {
    @Override
    public ArgumentSet getArguments() {
        return null;
    }

    @Override
    public void run(SlashCommandEvent event) throws CommandException {
        if (Colossus.getSQLDatabaseDriver().queryIsZero("SELECT COUNT(*) FROM tickets WHERE channel = ?", event.getChannel().getId())) {
            throw new CommandException("This is not a ticket channel.");
        }

        event.reply("Ticket closed.", true);
        TicketHandler.deleteTicket(event.getChannel());
    }
}
