package kg.news.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.springframework.stereotype.Component;
import org.xm.Similarity;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class KeyWordUtil {
    public enum UPDATE_TYPE {
        INCREASE, DECREASE
    }

    /**
     * 计算并获取文章关键词列表
     * KeyWord的形式为： 关键词/权重
     * @param title 标题
     * @param content 内容
     * @param keyNums 关键词数量
     * @return 关键词列表
     */
    public static List<Keyword> getKeyWordList(String title, String content, int keyNums) {
        KeyWordComputer kwc = new KeyWordComputer(keyNums);
        return kwc.computeArticleTfidf(title, content);
    }

    /**
     * 更新关键词
     * @param targetKeyWordJson 待更新的目标关键词列表 (JSON格式：{"keyWord1":权重1, "keyWord2":权重2, ...})
     * @param srcKeyWordJson 源关键词列表 (JSON格式：{"keyWord1":权重1, "keyWord2":权重2, ...})
     * @param type 更新类型（增加或降低targetKeyWord的权重）
     * @param convertValue 权重转换值
     * @return 更新后的关键词（JSON格式：{"keyWord1":权重1, "keyWord2":权重2, ...})
     */
    public static String updateKeyWord(String targetKeyWordJson,
                                       String srcKeyWordJson,
                                       UPDATE_TYPE type,
                                       double threshold,
                                       double convertValue) {
        // 如果源关键词Json字符串为空，直接返回目标关键词Json字符串（增权）
        if (targetKeyWordJson == null || Objects.equals(targetKeyWordJson, "")) {
            if (type == UPDATE_TYPE.DECREASE)
                return null;
            return srcKeyWordJson;
        }
        // 将目标关键词Json字符串转换为Map
//        Map<String, Double> targetKeyWordMap = JSON.parseObject(targetKeyWordJson, Map.class);
        Map<String, Double> targetKeyWordMap = JSON.parseObject(targetKeyWordJson, new TypeReference<Map<String, BigDecimal>>() {})
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().doubleValue()));
        // 将源关键词Json字符串转换为Map
//        Map<String, Double> srcKeyWordMap = JSON.parseObject(srcKeyWordJson, Map.class);
        Map<String, Double> srcKeyWordMap = JSON.parseObject(srcKeyWordJson, new TypeReference<Map<String, BigDecimal>>() {})
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().doubleValue()));
        // 如果目标兴趣词表不为空，需要将源关键词表与目标关键词表进行合并
        // 合并过程如下：
        // 1. 遍历源关键词表，如果目标词表中没有该关键词
        //    则将该源关键词与目标词表进行逐个遍历
        //    把近似度最高的源关键词的权重增加（降低）到与他最近似的目标词的权重上，完成合并
        // 2. 若是要增加权重，如果进行了一轮匹配后相似度仍未达到阈值，说明它很可能是新关键词
        //    则将该源关键词添加到目标词表中
        // 3. 如果是要降低权重，如果关键词权重小于等于0，删除该关键词
        //    注意：如果是要降低权重，不需要添加新关键词，因为降低权重的目的是删除不感兴趣的关键词
        for (Map.Entry<String, Double> srcEntry : srcKeyWordMap.entrySet()) {
            boolean isMatched = false;
            for (Map.Entry<String, Double> targetEntry : targetKeyWordMap.entrySet()) {
                // 使用词林相似度计算两个关键词的相似度，对于一些新词汇，效果不好
                double similarity = Similarity.cilinSimilarity(srcEntry.getKey(), targetEntry.getKey());
                if (similarity >= threshold) {
                    isMatched = true;
                    if (type == UPDATE_TYPE.INCREASE) {
                        targetKeyWordMap.put(targetEntry.getKey(), targetEntry.getValue() + srcEntry.getValue() * convertValue);
                    } else {
                        targetKeyWordMap.put(targetEntry.getKey(), targetEntry.getValue() - srcEntry.getValue() * convertValue);
                        // 如果关键词权重小于等于0，删除该关键词
                        if (targetKeyWordMap.get(targetEntry.getKey()) <= 0) {
                            targetKeyWordMap.remove(targetEntry.getKey());
                        }
                    }
                }
            }
            // 如果进行了一轮匹配后相似度仍未达到阈值，说明它很可能是用户新兴趣
            // 则将该源关键词添加到目标关键词表中
            if (!isMatched && type == UPDATE_TYPE.INCREASE) {
                targetKeyWordMap.put(srcEntry.getKey(), srcEntry.getValue());
            }
        }
        return JSON.toJSONString(targetKeyWordMap);
    }

    /**
     * 使用 Map按value进行排序
     * @param map 待排序的Map
     * @return 排序后的Map
     */
    public Map<Long, Double> sortMapByValue(Map<Long, Double> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<Long, Double> sortedMap = new LinkedHashMap<>();
        List<Map.Entry<Long, Double>> entryList = new ArrayList<>(map.entrySet());
        entryList.sort(new MapValueComparator());

        Iterator<Map.Entry<Long, Double>> iter = entryList.iterator();
        Map.Entry<Long, Double> tmpEntry;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }
        return sortedMap;
    }

    /**
     * 比较器类
     */
    private static class MapValueComparator implements Comparator<Map.Entry<Long, Double>> {
        @Override
        public int compare(Map.Entry<Long, Double> o1, Map.Entry<Long, Double> o2) {
            // 降序排序
            return o2.getValue().compareTo(o1.getValue());
        }
    }
}
