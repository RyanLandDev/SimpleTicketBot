package net.ryanland.simpleticketbot;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.events.ButtonClickEvent;
import net.ryanland.colossus.events.repliable.InteractionRepliableEvent;
import net.ryanland.colossus.events.repliable.RepliableEvent;
import net.ryanland.colossus.sys.file.config.Config;
import net.ryanland.colossus.sys.file.database.Supply;
import net.ryanland.colossus.sys.message.DefaultPresetType;
import net.ryanland.colossus.sys.message.PresetBuilder;

import java.util.Map;

public class TicketHandler extends ListenerAdapter {

    public static void createTicket(RepliableEvent event) {
        // create channel (with same perms as parent category)
        event.getGuild().createTextChannel("ticket-"+event.getUser().getName(),
            event.getGuild().getCategoryById(Config.getString("ticket_category_id"))).queue(channel -> {
                // insert ticket into database
                Colossus.getSQLDatabaseDriver().insertSupply(new Supply("tickets",
                    Map.of("author", event.getUser().getId(), "channel", channel.getId())));
                // add user to channel
                channel.upsertPermissionOverride(event.getMember().member()).setAllowed(Permission.VIEW_CHANNEL,
                    Permission.MESSAGE_HISTORY, Permission.MESSAGE_SEND, Permission.MESSAGE_ATTACH_FILES).queue();
                // send start message
                PresetBuilder msg = new PresetBuilder("Support Ticket",
                    event.getMember().getAsMention() +
                        ", please explain the reason for this ticket below.\nOur staff members will get to you shortly.").addLogo();
                channel.sendMessageEmbeds(msg.embed())
                    .setContent("@everyone")
                    .setActionRow(Button.danger("closeticket", "Close Ticket"))
                    .queue();
                // reply confirmation
                event.reply(new PresetBuilder(DefaultPresetType.SUCCESS, "Ticket Created",
                    "A new ticket has been opened in " + channel.getAsMention() + ".").setEphemeral(true).addLogo());
        });
    }

    public static void deleteTicket(Channel channel) {
        channel.delete().queue();
        Colossus.getSQLDatabaseDriver().query("DELETE FROM tickets WHERE channel = ?", channel);
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        switch (event.getButton().getId()) {
            case "openticket" -> createTicket(new ButtonClickEvent(event));
            case "closeticket" -> {
                event.deferEdit().queue();
                deleteTicket(event.getChannel());
            }
        }
    }
}
