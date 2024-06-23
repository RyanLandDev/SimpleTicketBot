package net.ryanland.simpleticketbot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.ryanland.colossus.command.BaseCommand;
import net.ryanland.colossus.command.CommandException;
import net.ryanland.colossus.command.arguments.ArgumentSet;
import net.ryanland.colossus.command.regular.CommandBuilder;
import net.ryanland.colossus.command.regular.SlashCommand;
import net.ryanland.colossus.events.command.SlashCommandEvent;
import net.ryanland.colossus.sys.message.PresetBuilder;

@CommandBuilder(
    name = "helpdesk",
    description = "Generates a rich helpdesk message where users can request to open a ticket."
)
public class HelpdeskCommand extends BaseCommand implements SlashCommand {
    @Override
    public DefaultMemberPermissions getDefaultPermissions() {
        return DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR);
    }

    @Override
    public ArgumentSet getArguments() {
        return null;
    }

    @Override
    public void run(SlashCommandEvent event) throws CommandException {
        PresetBuilder msg = new PresetBuilder("Helpdesk", "If you need assistance, please press the button below to open a ticket.")
            .addLogo();
        event.getChannel().sendMessageEmbeds(msg.embed())
            .setActionRow(Button.secondary("openticket", "Create Ticket").withEmoji(Emoji.fromUnicode("📨"))).queue();
        event.reply("Message posted.", true);
    }
}
