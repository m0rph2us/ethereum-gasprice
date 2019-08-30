package ilhoyu.gasprice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.backoff.FixedBackOffPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate
import java.net.SocketTimeoutException

@Configuration
class AppConfig {

    @Bean
    fun retryTemplate(): RetryTemplate {
        return RetryTemplate().also { template ->
            template.setBackOffPolicy(
                    FixedBackOffPolicy().apply { backOffPeriod = 1000 }
            )

            template.setRetryPolicy(
                    SimpleRetryPolicy(
                            3,
                            mapOf(
                                    SocketTimeoutException::class.java to true
                            )
                    )
            )
        }
    }

}