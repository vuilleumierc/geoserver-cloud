/*
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */
package org.geoserver.cloud.config.jdbcconfig.bus;

import lombok.extern.slf4j.Slf4j;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CatalogInfo;
import org.geoserver.cloud.bus.event.RemoteInfoEvent;
import org.geoserver.cloud.bus.event.catalog.RemoteCatalogEvent;
import org.geoserver.cloud.bus.event.catalog.RemoteCatalogModifyEvent;
import org.geoserver.cloud.bus.event.catalog.RemoteCatalogRemoveEvent;
import org.geoserver.jdbcconfig.internal.ConfigDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.bus.ServiceMatcher;
import org.springframework.context.event.EventListener;

/**
 * Listens to {@link RemoteCatalogEvent}s and evicts the modified or deleted {@link CatalogInfo }
 * from the {@link ConfigDatabase} cache
 */
@Slf4j(topic = "org.geoserver.cloud.bus.incoming.jdbcconfig")
public class JdbcConfigRemoteEventProcessor {
    private @Autowired ServiceMatcher busServiceMatcher;

    private @Autowired ConfigDatabase jdbcConfigDatabase;

    @EventListener(RemoteCatalogRemoveEvent.class)
    public void onCatalogRemoteRemoveEvent(RemoteCatalogRemoveEvent event) {
        if (!busServiceMatcher.isFromSelf(event)) {
            evictConfigDatabaseEntry(event);
        }
    }

    @EventListener(RemoteCatalogModifyEvent.class)
    public void onCatalogRemoteModifyEvent(RemoteCatalogModifyEvent event) {
        if (!busServiceMatcher.isFromSelf(event)) {
            evictConfigDatabaseEntry(event);
        }
    }

    private void evictConfigDatabaseEntry(RemoteInfoEvent<Catalog, CatalogInfo> event) {
        if (!busServiceMatcher.isFromSelf(event)) {
            log.debug("Evict JDBCConfig cache for {}", event);
            String catalogInfoId = event.getObjectId();
            jdbcConfigDatabase.clearCacheIfPresent(catalogInfoId);
        }
    }
}
