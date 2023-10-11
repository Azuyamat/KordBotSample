package commands

import InteractionClass
import command.Command
import command.CommandClass
import dev.kord.common.entity.ButtonStyle
import dev.kord.common.entity.Permission
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.behavior.interaction.response.createEphemeralFollowup
import dev.kord.core.builder.components.emoji
import dev.kord.core.entity.ReactionEmoji
import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.rest.builder.interaction.ChatInputCreateBuilder
import dev.kord.rest.builder.interaction.channel
import dev.kord.rest.builder.interaction.string
import dev.kord.rest.builder.message.create.actionRow
import dev.kord.rest.builder.message.create.embed

@CommandClass
@InteractionClass
class Panel : Command {
    override val name: String = "panel"
    override val description: String = "Show the panel for any module"
    override val builder: ChatInputCreateBuilder.() -> Unit = {
        string("module", "The module to show the panel for"){
            choice("ticket", "ticket")
            required = true
        }
        channel("channel", "The channel to show the panel in")
    }
    override val permission = Permission.Administrator

    override suspend fun execute(event: GuildChatInputCommandInteractionCreateEvent) {
        val interaction = event.interaction
        val command = interaction.command
        val channel = command.channels["channel"]?.asChannel() ?: interaction.getChannel().asChannel()
        val module = command.strings["module"]

        if (channel !is TextChannel) {
            interaction.respondEphemeral {
                content = "Channel is not a text channel"
            }
            return
        }

        val response = interaction.respondEphemeral { content = "Creating $module panel..." }
        when (module) {
            "ticket" -> {
                channel.createMessage {
                    embed {
                        title = "Ticket Panel"
                        description = "Need to contact me for a quote or have a question? Click the button below to open a ticket!"
                        footer {
                            text = "Made by Azuyamat"
                        }
                        thumbnail {
                            url = channel.getGuild().icon?.cdnUrl?.toUrl()?:""
                        }
                    }
                    actionRow {
                        interactionButton(ButtonStyle.Secondary, "ticket//open"){
                            label = "Create a ticket"
                            emoji(ReactionEmoji.Unicode("\uD83C\uDFAB"))
                        }
                    }
                }
            }
            else -> {
                response.createEphemeralFollowup {
                    content = "Module not found"
                }
            }
        }
    }
}