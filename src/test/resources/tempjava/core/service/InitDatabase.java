/*******************************************************************************
 * Copyright (c) 2018-11-05 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.jgit.core.service;

import org.iff.jgit.core.SystemEventService;
import org.iff.infra.util.Assert;
import org.iff.infra.util.CacheHelper;
import org.iff.infra.util.EventBusHelper;

import java.io.File;
import java.util.Map;
import java.util.Properties;

import static org.iff.jgit.util.SystemHelper.*;

/**
 * InitDatabase
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-05
 * auto generate by qdp.
 */
public class InitDatabase {

    public static Properties loadQuery() {
        File file = find("", new String[]{PATH_QUERY, PATH_QUERY_DEV});
        Assert.notNull(file, "InitDatabase can't load the query file from : " + PATH_QUERY + " or " + PATH_QUERY_DEV);
        Properties load = load(file);
        Assert.notNull(file, "InitDatabase can't parse the query file : " + file.getAbsolutePath());
        for (Map.Entry entry : load.entrySet()) {
            CacheHelper.getCache().set(entry.getKey().toString(), entry.getValue());
        }
        return load;
    }

    public static void createTable() {
        EventBusHelper.me().syncEvent(SystemEventService.EVENT_NAME, SystemEventService.Tuple.createTableDomainQueryModel());
        EventBusHelper.me().syncEvent(SystemEventService.EVENT_NAME, SystemEventService.Tuple.createTableDomainParamModel());
        EventBusHelper.me().syncEvent(SystemEventService.EVENT_NAME, SystemEventService.Tuple.createTableDataSourceModel());
    }
}
