package buttons

import InteractionClass
import button.Button
import dev.kord.common.entity.TextInputStyle
import dev.kord.core.behavior.interaction.modal
import dev.kord.core.event.interaction.ButtonInteractionCreateEvent

@InteractionClass
class TicketButton : Button {
    override val id: String = "ticket"

    override suspend fun execute(event: ButtonInteractionCreateEvent) {
        val interaction = event.interaction
        val task = interaction.componentId.split("//")[1]

        when (task) {
            "open" -> {
                interaction.modal("Ticket Menu", "ticket//open"){
                    actionRow {
                        textInput(TextInputStyle.Paragraph, "description", "Enter a description for your ticket") {
                            placeholder = "Enter a description for your ticket"
                            allowedLength = 20..1000
                            required = true
                        }
                    }
                }
            }
        }
    }
}