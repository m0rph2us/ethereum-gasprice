package ilhoyu.gasprice.config

import com.googlecode.jsonrpc4j.DefaultExceptionResolver
import com.googlecode.jsonrpc4j.JsonRpcHttpClient
import com.googlecode.jsonrpc4j.ProxyUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.URL

/**
 * TRANSACTION - A transaction object, or null when no transaction was found
 */
data class EthTransaction(
        val blockHash: String = "",         // 32 Bytes - hash of the block where this transaction was in.
                                            // null when its pending.
        val blockNumber: String = "",       // block number where this transaction was in. null when its pending.
        val from: String = "",              // 20 Bytes - address of the sender.
        val gas: String = "",               // gas provided by the sender.
        val gasPrice: String = "",          // gas price provided by the sender in Wei.
        val hash: String = "",              // 32 Bytes - hash of the transaction.
        val input: String = "",             // the data send along with the transaction.
        val nonce: String = "",             // the number of transactions made by the sender prior to this one.
        val r: String = "",
        val s: String = "",
        val to: String = "",                // 20 Bytes - address of the receiver.
                                            // null when its a contract creation transaction.
        val transactionIndex: String = "",  // integer of the transactions index position in the block.
                                            // null when its pending.
        val v: String = "",
        val value: String = ""
)

/**
 * A block object, or null when no block was found
 */
data class EthBlock(
        val number: String? = null,         // the block number. Null when the returned block is the pending block.
        val hash: String = "",              // 32 Bytes - hash of the block. Null when the returned block is the
                                            // pending block.
        val parentHash: String = "",        // 32 Bytes - hash of the parent block.
        val nonce: String = "",             // 8 Bytes - hash of the generated proof-of-work.
                                            // Null when the returned block is the pending block.
        val sha3Uncles: String = "",        // 32 Bytes - SHA3 of the uncles data in the block.
        val logsBloom: String = "",         // 256 Bytes - the bloom filter for the logs of the block.
                                            // Null when the returned block is the pending block.
        val transactionsRoot: String = "",  // 32 Bytes - the root of the transaction trie of the block.
        val stateRoot: String = "",         // 32 Bytes - the root of the final state trie of the block.
        val receiptsRoot: String = "",      // 32 Bytes - the root of the receipts trie of the block.
        val miner: String = "",             // 20 Bytes - the address of the beneficiary to whom the
                                            // mining rewards were given.
        val mixHash: String = "",
        val difficulty: String = "",        // integer of the difficulty for this block.
        val totalDifficulty: String = "",   // integer of the total difficulty of the chain until this block.
        val extraData: String = "",         // the "extra data" field of this block.
        val size: String = "",              // integer the size of this block in bytes.
        val gasLimit: String = "",          // the maximum gas allowed in this block.
        val gasUsed: String = "",           // the total used gas by all transactions in this block.
        val timestamp: String = "",         // the unix timestamp for when the block was collated.
        val transactions: List<EthTransaction> = listOf(),  // Array - Array of transaction objects,
                                            // or 32 Bytes transaction hashes depending on the last given parameter.
        val uncles: List<String> = listOf() // an Array of uncle hashes.
)

interface InfuraRpcClient {
    fun eth_getBlockByNumber(blockParam: String, showTransDetails: Boolean): EthBlock
}

@Configuration
class InfuraRpcClientConfig constructor(
        val endpointBase: String = "https://mainnet.infura.io/v3/",
        val projectId: String = "2e5a50f41e4844518710c6c10e4a9bb8",
        val connectionTimeoutMillis: Int = 3000,
        val readTimeoutMillis: Int = 3000
) {

    @Bean
    fun jsonRpcHttpClient(): JsonRpcHttpClient {
        val url = URL(endpointBase + projectId)

        return JsonRpcHttpClient(url, hashMapOf()).also {
            it.connectionTimeoutMillis = connectionTimeoutMillis
            it.readTimeoutMillis = readTimeoutMillis
        }
    }

    @Bean
    fun infuraRpcClient(jsonRpcHttpClient: JsonRpcHttpClient): InfuraRpcClient {
        return ProxyUtil.createClientProxy(
                javaClass.classLoader,
                InfuraRpcClient::class.java,
                jsonRpcHttpClient
        )
    }

}