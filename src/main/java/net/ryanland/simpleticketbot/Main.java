package net.ryanland.simpleticketbot;

import net.dv8tion.jda.api.entities.Activity;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.ColossusBuilder;
import net.ryanland.colossus.command.Category;
import net.ryanland.colossus.command.executor.CommandHandler;
import net.ryanland.colossus.sys.file.database.sql.SQLProvider;
import net.ryanland.colossus.sys.file.database.sql.SQLiteDatabaseDriver;
import net.ryanland.simpleticketbot.commands.CreateTicketCommand;
import net.ryanland.simpleticketbot.commands.HelpdeskCommand;
import net.ryanland.simpleticketbot.commands.TicketCommand;

import java.util.concurrent.TimeUnit;

public class Main {

    public static Category HELP_CATEGORY = new Category("Help", "Help commands", "❓");
    public static Category TICKET_CATEGORY = new Category("Tickets", "Ticket commands", "✉",
        new CreateTicketCommand(), new TicketCommand(), new HelpdeskCommand());

    public static void main(String[] args) {
        Colossus bot = new ColossusBuilder(".")
            .registerCategories(HELP_CATEGORY, TICKET_CATEGORY)
            // sqlite db
            .setDatabaseDriver(((SQLiteDatabaseDriver) new SQLiteDatabaseDriver("db.sqlite")
                .updatePrimaryKeys("tickets", "author", "channel"))
                .registerValueProvider("tickets", "author", "varchar(25) constraint ticket_pk primary key",
                    authorId -> authorId, result -> result.getString("author"))
                .registerValueProvider("tickets", "channel", "varchar(25) constraint ticket_pk primary key",
                    channelId -> channelId, result -> result.getString("channel"))
            ).registerProviders(SQLProvider.of("tickets"))
            // misc
            .addEventListeners(new TicketHandler())
            .setActivity(Activity.watching("/help"))
            .setDefaultComponentListenerExpirationTime(30, TimeUnit.MINUTES)
            .registerConfigEntry("ticket_category_id", "")
            .build();

        bot.initialize();

        CommandHandler.getCommand("help").setCategory(HELP_CATEGORY);
        CommandHandler.getCommand("disable").setCategory(HELP_CATEGORY);
        CommandHandler.getCommand("enable").setCategory(HELP_CATEGORY);
    }
}
