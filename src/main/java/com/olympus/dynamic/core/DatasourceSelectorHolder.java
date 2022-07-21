package com.olympus.dynamic.core;

import org.apache.commons.lang3.StringUtils;


/**
 * since 4/26/22
 *
 * @author eddie
 */
public class DatasourceSelectorHolder {

    /**
     * 默认宪法
     */
    public static final String DEFAULT_COUNTRY = "CHN";

    private static final InheritableThreadLocal<String> DATASOURCE_HOLDER = new InheritableThreadLocal<>();

    public static String getCurrentDatabase() {
        if (StringUtils.isNotBlank(DATASOURCE_HOLDER.get())){
            return DATASOURCE_HOLDER.get();
        }
        return DEFAULT_COUNTRY;
    }

    public static void setCurrentDatabase(String currentCountry) {
        DATASOURCE_HOLDER.set(currentCountry);
    }

    public static void clear() {
        DATASOURCE_HOLDER.remove();
    }
}
