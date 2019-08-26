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
    lateinit var ethereumService: InfuraEthereumService

    @Test
    fun `테스트`() {
        val x = ethereumService.getBlockByNumber("latest", true)
    }

}