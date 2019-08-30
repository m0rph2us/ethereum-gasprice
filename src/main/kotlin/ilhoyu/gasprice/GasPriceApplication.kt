package ilhoyu.gasprice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.retry.annotation.EnableRetry

@EnableRetry
@SpringBootApplication
class GasPriceApplication

fun main(args: Array<String>) {
	runApplication<GasPriceApplication>(*args)
}