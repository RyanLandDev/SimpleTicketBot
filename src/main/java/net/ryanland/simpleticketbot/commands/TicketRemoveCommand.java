package net.ryanland.simpleticketbot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.BaseCommand;
import net.ryanland.colossus.command.CommandException;
import net.ryanland.colossus.command.arguments.ArgumentSet;
import net.ryanland.colossus.command.arguments.types.snowflake.MemberArgument;
import net.ryanland.colossus.command.regular.CommandBuilder;
import net.ryanland.colossus.command.regular.SlashCommand;
import net.ryanland.colossus.command.regular.SubCommand;
import net.ryanland.colossus.events.command.SlashCommandEvent;
import net.ryanland.colossus.sys.entities.ColossusMember;
import net.ryanland.colossus.sys.message.DefaultPresetType;
import net.ryanland.colossus.sys.message.PresetBuilder;

@CommandBuilder(
    name = "remove",
    description = "Removes a member from this ticket."
)
public class TicketRemoveCommand extends BaseCommand implements SlashCommand, SubCommand {
    @Override
    public DefaultMemberPermissions getDefaultPermissions() {
        return DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR);
    }

    @Override
    public ArgumentSet getArguments() {
        return new ArgumentSet().addArguments(
            new MemberArgument()
                .name("member")
                .description("Member to remove")
        );
    }

    @Override
    public void run(SlashCommandEvent event) throws CommandException {
        // checks
        if (Colossus.getSQLDatabaseDriver().queryIsZero("SELECT COUNT(*) FROM tickets WHERE channel = ?", event.getChannel().getId())) {
            throw new CommandException("This is not a ticket channel.");
        }
        ColossusMember member = event.getArgument("member");
        if (member.getId().equals(Colossus.getSelfUser().getId())) throw new CommandException("You cannot remove me from a ticket.");
        if (Colossus.getSQLDatabaseDriver().queryValue("SELECT author FROM tickets WHERE channel = ?", event.getChannel().getId()).equals(member.getId())) {
            throw new CommandException("You cannot remove the author of the ticket.");
        }
        if (!member.hasPermission(event.getGuildChannel(), Permission.VIEW_CHANNEL)) throw new CommandException("This member already does not have access to this ticket.");

        // remove
        event.getChannel().asTextChannel().upsertPermissionOverride(member.member()).clear(Permission.VIEW_CHANNEL).queue();
        event.reply(new PresetBuilder(DefaultPresetType.SUCCESS, "Member Removed",
            "Successfully removed " + member.getAsMention() + " from this ticket."));
    }
}
