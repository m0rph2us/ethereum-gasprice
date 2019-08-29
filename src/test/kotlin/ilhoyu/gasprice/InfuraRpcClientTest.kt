package ilhoyu.gasprice

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class InfuraRpcClientTest {

    @Autowired
    lateinit var rpcClient: InfuraRpcClient

    @Test
    fun `test`() {
        val ethBlock = rpcClient.eth_getBlockByNumber("latest", true)
    }

}