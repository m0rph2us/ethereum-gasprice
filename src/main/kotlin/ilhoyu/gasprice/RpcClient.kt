package ilhoyu.gasprice

import com.googlecode.jsonrpc4j.ProxyUtil
import com.googlecode.jsonrpc4j.JsonRpcHttpClient
import org.springframework.context.annotation.Bean
import java.net.URL
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service

/*
BLOCK - A block object, or null when no block was found
number: the block number. Null when the returned block is the pending block.
hash: 32 Bytes - hash of the block. Null when the returned block is the pending block.
parentHash: 32 Bytes - hash of the parent block.
nonce: 8 Bytes - hash of the generated proof-of-work. Null when the returned block is the pending block.
sha3Uncles: 32 Bytes - SHA3 of the uncles data in the block.
logsBloom: 256 Bytes - the bloom filter for the logs of the block. Null when the returned block is the pending block.
transactionsRoot: 32 Bytes - the root of the transaction trie of the block.
stateRoot: 32 Bytes - the root of the final state trie of the block.
receiptsRoot: 32 Bytes - the root of the receipts trie of the block.
miner: 20 Bytes - the address of the beneficiary to whom the mining rewards were given.
difficulty: integer of the difficulty for this block.
totalDifficulty: integer of the total difficulty of the chain until this block.
extraData: the "extra data" field of this block.
size: integer the size of this block in bytes.
gasLimit: the maximum gas allowed in this block.
gasUsed: the total used gas by all transactions in this block.
timestamp: the unix timestamp for when the block was collated.
transactions: Array - Array of transaction objects, or 32 Bytes transaction hashes depending on the last given parameter.
uncles: an Array of uncle hashes.
 */

data class EthereumBlock(
        val number: String? = null,
        val hash: String = "",
        val parentHash: String = "",
        val nonce: String = "",
        val sha3Uncles: String = "",
        val logsBloom: String = "",
        val transactionsRoot: String = "",
        val stateRoot: String = "",
        val receiptsRoot: String = "",
        val miner: String = "",
        val mixHash: String = "",
        val difficulty: String = "",
        val totalDifficulty: String = "",
        val extraData: String = "",
        val size: String = "",
        val gasLimit: String = "",
        val gasUsed: String = "",
        val timestamp: String = "",
        val transactions: List<Any> = listOf(),
        val uncles: List<Any> = listOf()
)


interface InfuraEthereumRpcClient {
    fun eth_getBlockByNumber(blockParam: String, showTransDetails: Boolean): EthereumBlock
}

@Service
class InfuraEthereumService @Autowired constructor(
        private val rpcClient: InfuraEthereumRpcClient
) {
    fun getBlockByNumber(blockParam: String, showTransDetails: Boolean): EthereumBlock {
        return rpcClient.eth_getBlockByNumber(blockParam, showTransDetails)
    }
}

@Configuration
class RpcClientConfig {
    @Bean
    fun infuraEthereumRpcClient(): InfuraEthereumRpcClient {
        return ProxyUtil.createClientProxy(
                javaClass.classLoader,
                InfuraEthereumRpcClient::class.java,
                JsonRpcHttpClient(
                        URL("https://mainnet.infura.io/v3/2e5a50f41e4844518710c6c10e4a9bb8"),
                        hashMapOf()
                )
        )
    }
}