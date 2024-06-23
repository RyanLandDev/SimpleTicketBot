# SimpleTicketBot
A simple ticket bot made using [Colossus](https://github.com/RyanLandDev/Colossus) and [JDA](https://github.com/discord-jda/JDA)
with minimalistic features

## Commands

`/createticket` - Creates a ticket  
`/ticket close` - Closes a ticket  
`/ticket add <member>` - Adds a member to a ticket  
`/ticket remove <member>` - Removes a member from a ticket  
`/helpdesk` - Generates a rich helpdesk message with a button that allows members to open a ticket

## Instructions

Ensure you have at least Java 17 installed.

**Step 1:** Open a commandline and `cd` to the directory

**Step 2:** Run `./gradlew shadowJar && java -jar build/libs/SimpleTicketBot-1.0-all.jar`

Running it for the first time will give you an error.
A `config.json` and `db.sqlite` file should be automatically generated.

**Step 3:** Set up the configuration file

The config file (`config.json`) will have a few empty fields for you to fill in:
- `ticket_category_id` - The ID of the Discord server category under which ticket channels should be created. Ensure this is a private category (and give your staff access), since ticket channels will copy the category's permission settings upon creation.
- `guild_id` - Your Discord server's ID.
- `token` - Your bot token.

**Step 4:** Run the bot again using `java -jar build/libs/SimpleTicketBot-1.0-all.jar`