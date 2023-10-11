package modals

import InteractionClass
import TICKETCATEGORY
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.behavior.channel.createTextChannel
import dev.kord.core.behavior.interaction.response.createEphemeralFollowup
import dev.kord.core.entity.channel.Category
import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.event.interaction.ModalSubmitInteractionCreateEvent
import dev.kord.rest.builder.message.create.embed
import modal.Modal

@InteractionClass
class TicketModal : Modal {
    override val id: String = "ticket"

    override suspend fun execute(event: ModalSubmitInteractionCreateEvent) {
        val interaction = event.interaction
        val task = interaction.modalId.split("//")[1]
        val channel = interaction.getChannel() as TextChannel
        val guild = channel.guild.asGuild()
        val ticketCategory = guild.getChannel(Snowflake(TICKETCATEGORY)) as Category
        val ticketDescription = interaction.textInputs.firstNotNullOf { it.value }.value?:"No description provided"
        val member = interaction.user.asMember(guild.id)

        when (task) {
            "open" -> {
                val response = interaction.deferEphemeralMessageUpdate()
                val ticketChannel = ticketCategory.createTextChannel("ticket-${interaction.user.username}"){

                }.apply {
                    createMessage {
                        content = "${member.mention} <@745025777632018472>"
                        embed {
                            title = "Ticket"
                            description = "Ticket created by ${interaction.user.mention}"
                            field {
                                name = "Description"
                                value = ticketDescription
                            }
                            thumbnail {
                                url = member.avatar?.cdnUrl?.toUrl()?:""
                            }
                        }
                    }
                }
                response.createEphemeralFollowup {
                    embed {
                        title = "Ticket created"
                        description = "Thank you for your interest! A ticket has been created and will be responded to shortly."
                        field {
                            name = "Channel"
                            value = ticketChannel.mention
                        }
                    }
                }
            }
        }
    }
}