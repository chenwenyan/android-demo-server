package com.nenu.android.service.impl;

import com.nenu.android.service.IndexService;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.iterator.FileLineIterable;
import org.apache.mahout.common.iterator.StringRecordIterator;
import org.apache.mahout.fpm.pfpgrowth.convertors.ContextStatusUpdater;
import org.apache.mahout.fpm.pfpgrowth.convertors.SequenceFileOutputCollector;
import org.apache.mahout.fpm.pfpgrowth.convertors.string.StringOutputConverter;
import org.apache.mahout.fpm.pfpgrowth.convertors.string.TopKStringPatterns;
import org.apache.mahout.fpm.pfpgrowth.fpgrowth.FPGrowth;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * IndexService
 *
 * @author: wychen
 * @time: 2017/5/9 16:16
 */
@Service("indexService")
public class IndexServiceImpl implements IndexService {

    public String test(String test) throws Exception {
        return test;
    }

    public String PFPGrowth(String test) throws Exception {

//        System.setProperty("hadoop.home.dir", "D:\\software\\hadoop-2.7.3");

        //获取开始时间
        long startTime = System.currentTimeMillis();

        Set<String> features = new HashSet<String>();//集合，只存储不重复的项
        String input = "D://T10I4D100K.txt";//测试数据集
//      网址：  http://fimi.ua.ac.be/data/T10I4D100K.dat

//        String input = "D://T40I10D100K.txt";//测试数据集
//      网址：  http://fimi.ua.ac.be/data/T40I10D100K.dat

        int minSupport = 100;//支持度阈值
        int maxHeapSize = 50;//top-k  大根堆的大小
        String pattern = " \"[ ,\\t]*[,|\\t][ ,\\t]*\" ";//do not know why
        Charset encoding = Charset.forName("utf-8");//设置编码格式
        FPGrowth<String> fp = new FPGrowth<String>();
        String output = "D://output.txt"; //输出结果文件
        Path path = new Path(output);
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf); //通过配置文件获取一个FileSystem实例

        SequenceFile.Writer writer = new SequenceFile.Writer(fs, conf, path, Text.class, TopKStringPatterns.class);
        fp.generateTopKFrequentPatterns(
                new StringRecordIterator(
                        new FileLineIterable(
                                new File(input), encoding, false),
                        pattern),
                fp.generateFList(new StringRecordIterator(new FileLineIterable(new File(input), encoding, false), pattern),
                        minSupport),
                minSupport,
                maxHeapSize,
                features,
                new StringOutputConverter(new SequenceFileOutputCollector<Text, TopKStringPatterns>(writer)),
                new ContextStatusUpdater(null));
        writer.close();

        List<Pair<String, TopKStringPatterns>> frequentPatterns = FPGrowth.readFrequentPattern(conf, path);
        for (Pair<String, TopKStringPatterns> entry : frequentPatterns) {
            System.out.println(entry.getFirst());
            System.out.println(entry.getSecond());
        }
        //获取结束时间
        long endTime = System.currentTimeMillis();
        long costTime = endTime - startTime;
        System.out.println("PFPGrowth程序运行时间:" + costTime + "ms");

        return String.valueOf(costTime);
    }

    public String computeGoldenPoint(int n) throws Exception {
        try {
//            double pi=0;
//            double dx=1e-3;
//            for(double x=-100;x<=+100;x+=dx){
//                pi+=Math.exp(-x*x)*dx;
//            }
            BigDecimal a = new BigDecimal(0.618);
            BigDecimal b = new BigDecimal("1");

            for (int i = 1; i < n + 1; i++) {
                //这个循环实现  1/(1/...+1)  本题主要是考察BigDecimal的用法..
                a = b.divide(a.add(b), n, BigDecimal.ROUND_HALF_UP);
            }

            System.out.println("计算黄金分割点：");
            System.out.println(a.toString());
            return a.toString();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

}
