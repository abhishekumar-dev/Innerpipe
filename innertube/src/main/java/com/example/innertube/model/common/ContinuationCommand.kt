import kotlinx.serialization.Serializable

@Serializable
data class ContinuationCommand(
    val token: String
)