package com.tian.dw.gmallcanal

import java.net.InetSocketAddress

import com.alibaba.otter.canal.client.{CanalConnector, CanalConnectors}
import com.alibaba.otter.canal.protocol.CanalEntry.{EntryType, RowChange}
import com.alibaba.otter.canal.protocol.{CanalEntry, Message}
import com.google.protobuf.ByteString
import com.tian.dw.gmallcanal.util.CanalHandler

/**
 * @author tian
 * @date 2019-10-9 16:30:43
 */
object CanalClient {
    def main(args: Array[String]): Unit = {
        // 1. 创建能连接到 Canal 的连接器对象
        val connector: CanalConnector =
            CanalConnectors.newSingleConnector(
                new InetSocketAddress("hadoop102", 11111),
                "example",
                "",
                "")
        // 2. 连接到 Canal
        connector.connect()
        // 3. 监控指定的表的数据的变化
        connector.subscribe("gmall.order_info")
        while (true) {
            // 4. 获取消息  (一个消息对应 多条sql 语句的执行)
            val msg: Message = connector.get(100) // 一次最多获取 100 条 sql
            // 5. 个消息对应多行数据发生了变化, 一个 entry 表示一条 sql 语句的执行
            val entries: java.util.List[CanalEntry.Entry] = msg.getEntries
            import scala.collection.JavaConversions._
            if (entries.size() > 0) {
                // 6. 遍历每行数据
                for (entry <- entries) {
                    // 7. EntryType.ROWDATA 只对这样的 EntryType 做处理
                    if (entry.getEntryType == EntryType.ROWDATA) {
                        // 8. 获取到这行数据, 但是这种数据不是字符串, 所以要解析
                        val value: ByteString = entry.getStoreValue
                        val rowChange: RowChange = RowChange.parseFrom(value)
                        // 9.定义专门处理的工具类: 参数 1 表名, 参数 2 事件类型(插入, 删除等), 参数 3: 具体的数据
                        CanalHandler.handle(entry.getHeader.getTableName, rowChange.getEventType, rowChange.getRowDatasList)
                    }
                }

            } else {
                println("没有抓取到数据...., 2s 之后重新抓取")
                Thread.sleep(2000)
            }
        }

    }
}
