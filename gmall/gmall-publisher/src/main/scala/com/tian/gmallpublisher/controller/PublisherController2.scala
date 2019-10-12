package com.tian.gmallpublisher.controller

import java.text.DecimalFormat
import com.tian.gmallpublisher.bean.{Opt, SaleInfo, Stat}
import com.tian.gmallpublisher.service.PublisherService2
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{GetMapping, RequestParam, RestController}

/**
 * @author tian
 * @date 2019/10/12 15:44
 * @version 1.0.0
 */
@RestController
class PublisherController2 {
    @Autowired val publisherService: PublisherService2 = null

    /*
    接口: http://localhost:8070/sale_detail?date=2019-05-20&&startpage=1&&size=5&&keyword=手机小米

     */
    @GetMapping(Array("sale_detail"))
    def saleDetail(@RequestParam("date") date: String,
                   @RequestParam("startPage") startPage: Int,
                   @RequestParam("size") pageSize: Int,
                   @RequestParam("keyword") keyword: String): Unit = {
        val formatter: DecimalFormat = new DecimalFormat(".00")

        // 按照性别统计
        val saleDetailMapByGender: Map[String, Any] = publisherService
            .getSaleDetailAndAggResultByAggField(date, keyword, startPage, pageSize, "user_gender", 2)
        val total: Integer = saleDetailMapByGender("total").asInstanceOf[Integer]
        val genderMap: Map[String, Long] = saleDetailMapByGender("aggMap").asInstanceOf[Map[String, Long]]
        val maleCount: Long = genderMap("M")
        val femaleCount: Long = genderMap("F")
        // 个数变比例, F M 变 男 女 用户性别占比
        val genderOpts = List[Opt](
            Opt("total", total.toString),
            Opt("男", formatter.format(maleCount.toDouble / total)),
            Opt("女", formatter.format(femaleCount.toDouble / total))
        )
        // 性别图片的数据
        val genderStat = Stat("用户性别占比", genderOpts)

        // 按照年龄统计
        val saleDetailMapByAge: Map[String, Any] = publisherService
            .getSaleDetailAndAggResultByAggField(date, keyword, startPage, pageSize, "user_age", 100)
        // 分别计算每个年龄段的人数
        val ageMap: Map[String, Long] = saleDetailMapByAge("aggMap").asInstanceOf[Map[String, Long]]
        val ageOpts = ageMap.groupBy {
            case (age, _) =>
                val intAge = age.toInt
                if (intAge < 20) "20岁以下"
                else if (intAge >= 20 && intAge <= 30) "20岁到30岁"
                else "30岁以上"
        }.map {
            case (age2age, map) =>
                (age2age, (0L /: map) (_ + _._2))
        }.map {
            case (age2age, count) =>
                (age2age, formatter.format(count.toDouble / total))
        }.toList.map {
            case (age2age, rate) =>
                Opt(age2age, rate)
        }

        // 年龄段图表的数据
        val ageStat = Stat("用户年龄占比", ageOpts)

        // 详表
        val detailList: List[Map[String, Any]] = saleDetailMapByAge("detail").asInstanceOf[List[Map[String, Any]]]
        val saleInfo = SaleInfo(total, List(genderStat, ageStat), detailList)


        // 返回给前端数据
        /*
        import org.json4s.jackson.Serialization.write  // 序列化方法
        implicit val formats = DefaultFormats
        write(saleInfo)
        */
    }

}
