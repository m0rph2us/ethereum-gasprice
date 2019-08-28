package ilhoyu.gasprice

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class GasPriceServiceTest {
    @Autowired
    lateinit var ethService: EthService

    @Test
    fun `테스트`() {
        val gasPriceSummary = ethService.getLatestBlockGasPriceSummary()
    }
}