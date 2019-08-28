package ilhoyu.gasprice

import com.googlecode.jsonrpc4j.ProxyUtil
import com.googlecode.jsonrpc4j.JsonRpcHttpClient
import org.springframework.context.annotation.Bean
import java.net.URL
import org.springframework.context.annotation.Configuration


/*
{
			"blockHash": "0xf315f5a74de77d1d0db2dcae6143e7e88e9095a38c86f0c0864cd00aec06d827",
			"blockNumber": "0x806e51",
			"from": "0x9ab6f0a7483218b99204336a50ec4170a120eca7",
			"gas": "0x5208",
			"gasPrice": "0xc5a9f5900",
			"hash": "0x449f2c02922b4b6b38d6d1f26595237bb200a4c975c494bf391e473e1f8fc24b",
			"input": "0x",
			"nonce": "0x4f1b",
			"r": "0xf1b5719c0d32f18d9fadf700a1077c101ab8ffe7557cb636476747300c43011d",
			"s": "0x7f32cde9e8b4d8352b7bdf0d421ff257afba3b0adc3d25656c025c004098afc1",
			"to": "0xc4c9bceecf6914f94ffb34e002d3c8d12bc867da",
			"transactionIndex": "0x0",
			"v": "0x25",
			"value": "0x4180cefe0bd80000"
		}
 */

data class EthTransaction(
    val blockHash: String = "",
    val blockNumber: String = "",
    val from: String = "",
    val gas: String = "",
    val gasPrice: String = "",
    val hash: String = "",
    val input: String = "",
    val nonce: String = "",
    val r: String = "",
    val s: String = "",
    val to: String = "",
    val transactionIndex: String = "",
    val v: String = "",
    val value: String = ""
)

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


data class EthBlock(
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
        val transactions: List<EthTransaction> = listOf(),
        val uncles: List<String> = listOf()
)


interface InfuraEthRpcClient {
    fun eth_getBlockByNumber(blockParam: String, showTransDetails: Boolean): EthBlock
}

@Configuration
class RpcClientConfig {
    @Bean
    fun infuraEthRpcClient(): InfuraEthRpcClient {
        return ProxyUtil.createClientProxy(
                javaClass.classLoader,
                InfuraEthRpcClient::class.java,
                JsonRpcHttpClient(
                        URL("https://mainnet.infura.io/v3/2e5a50f41e4844518710c6c10e4a9bb8"),
                        hashMapOf()
                )
        )
    }
}