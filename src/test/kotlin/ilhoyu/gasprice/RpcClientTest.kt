package ilhoyu.gasprice

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class RpcClientTest {

    @Autowired
    lateinit var ethService: InfuraEthService

    @Test
    fun `테스트`() {
        val ethBlock = ethService.getLatestBlockWithTransDetails()


        val gasPriceSummary = GasPriceSummary.from(ethBlock)
    }

    @Test
    fun `변환 테스트`() {
        assert(0 == "0x".replace("0x", "").toInt(16))
    }

}