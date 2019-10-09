package com.tian.dw.gmallcanal.util

import java.util

import com.alibaba.otter.canal.protocol.CanalEntry
import com.alibaba.otter.canal.protocol.CanalEntry.{EventType, RowData}

/**
 * @author tian
 * @date 2019-10-9 16:34:31
 */
object CanalHandler {
    /**
     * 处理从 canal 取来的数据
     *
     * @param tableName   表名
     * @param eventType   事件类型
     * @param rowDataList 数据类别
     */
    def handle(tableName: String, eventType: EventType, rowDataList: util.List[RowData]): Unit = {
        import scala.collection.JavaConversions._
        if ("order_info" == tableName && eventType == EventType.INSERT && rowDataList.size() > 0) {
            // 1. rowData 表示一行数据, 通过他得到每一列. 首先遍历每一行数据
            for (rowData <- rowDataList) {
                // 2. 得到每行中, 所有列组成的列表
                val columnList: util.List[CanalEntry.Column] = rowData.getAfterColumnsList
                for (column <- columnList) {
                    // 3. 得到列名和列值
                    println(column.getName + ":" + column.getValue)
                }
            }
        }
    }
}

