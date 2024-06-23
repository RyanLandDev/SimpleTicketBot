package net.ryanland.simpleticketbot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.BaseCommand;
import net.ryanland.colossus.command.CommandException;
import net.ryanland.colossus.command.arguments.ArgumentSet;
import net.ryanland.colossus.command.arguments.types.snowflake.MemberArgument;
import net.ryanland.colossus.command.permission.PermissionHolder;
import net.ryanland.colossus.command.regular.CommandBuilder;
import net.ryanland.colossus.command.regular.SlashCommand;
import net.ryanland.colossus.command.regular.SubCommand;
import net.ryanland.colossus.events.command.SlashCommandEvent;
import net.ryanland.colossus.sys.entities.ColossusMember;
import net.ryanland.colossus.sys.message.DefaultPresetType;
import net.ryanland.colossus.sys.message.PresetBuilder;

@CommandBuilder(
    name = "add",
    description = "Adds a member to this ticket."
)
public class TicketAddCommand extends BaseCommand implements SlashCommand, SubCommand {
    @Override
    public DefaultMemberPermissions getDefaultPermissions() {
        return DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR);
    }

    @Override
    public ArgumentSet getArguments() {
        return new ArgumentSet().addArguments(
            new MemberArgument()
                .name("member")
                .description("Member to add")
        );
    }

    @Override
    public void run(SlashCommandEvent event) throws CommandException {
        // checks
        if (Colossus.getSQLDatabaseDriver().queryIsZero("SELECT COUNT(*) FROM tickets WHERE channel = ?", event.getChannel().getId())) {
            throw new CommandException("This is not a ticket channel.");
        }
        ColossusMember member = event.getArgument("member");
        if (member.hasPermission(event.getGuildChannel(), Permission.VIEW_CHANNEL)) throw new CommandException("This member already has access to this ticket.");

        // add
        event.getChannel().asTextChannel().upsertPermissionOverride(member.member()).setAllowed(Permission.VIEW_CHANNEL,
            Permission.MESSAGE_HISTORY, Permission.MESSAGE_SEND, Permission.MESSAGE_ATTACH_FILES).queue();
        event.reply(new PresetBuilder(DefaultPresetType.SUCCESS, "Member Added",
            "Successfully added " + member.getAsMention() + " to this ticket."));
    }
}
