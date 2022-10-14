package com.srs.account.util;

import com.srs.market.MarketType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author duynt on 2/18/22
 */
public class UserUtil {

    public static String grpcToNativeMarketDivisions(List<MarketType> divisions) {
        return divisions.stream()
                .filter(d -> !d.equals(MarketType.UNRECOGNIZED))
                .map(MarketType::getNumber)
                .map(String::valueOf)
                .sorted(String::compareTo)
                .collect(Collectors.joining(","));
    }

    public static List<MarketType> nativeToGrpcMarketDivisions(String divisionStr) {
        if (StringUtils.isBlank(divisionStr)) {
            return Collections.emptyList();
        }

        return Arrays.stream(divisionStr.split(","))
                .filter(StringUtils::isNotBlank)
                .map(Integer::parseInt)
                .filter(s -> s > 0)
                .filter(division -> MarketType.forNumber(division) != null)
                .map(MarketType::forNumber)
                .collect(Collectors.toList());
    }

    public static String grpcToNativeMarketCodes(List<String> marketCodes) {
        if (CollectionUtils.isEmpty(marketCodes)) {
            return "";
        }

        var codes = new ArrayList<String>();

        for (String marketCode : marketCodes) {
            if (StringUtils.isNotBlank(marketCode) && !codes.contains(marketCode)) {
                codes.add(marketCode);
            }
        }

        codes.sort(String::compareTo);

        return String.join(",", codes);
    }

    public static List<String> nativeToGrpcMarketCodes(String codeStr) {
        if (StringUtils.isBlank(codeStr)) {
            return Collections.emptyList();
        }

        return Arrays.asList(codeStr.split(","));
    }

}
