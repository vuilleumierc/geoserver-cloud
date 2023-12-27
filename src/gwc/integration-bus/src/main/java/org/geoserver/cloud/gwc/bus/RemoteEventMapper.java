/*
 * (c) 2022 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */
package org.geoserver.cloud.gwc.bus;

import lombok.Generated;
import lombok.NonNull;

import org.geoserver.cloud.gwc.event.BlobStoreEvent;
import org.geoserver.cloud.gwc.event.GeoWebCacheEvent;
import org.geoserver.cloud.gwc.event.GridsetEvent;
import org.geoserver.cloud.gwc.event.TileLayerEvent;
import org.mapstruct.AnnotateWith;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;
import org.mapstruct.ReportingPolicy;

/**
 * @since 1.0
 */
@Mapper(componentModel = "default", unmappedTargetPolicy = ReportingPolicy.ERROR)
@AnnotateWith(value = Generated.class)
interface RemoteEventMapper {

    default RemoteGeoWebCacheEvent toRemote(
            @NonNull GeoWebCacheEvent local,
            @NonNull Object source,
            @NonNull String originService) {
        if (local instanceof TileLayerEvent tle) return toRemote(tle, source, originService);
        if (local instanceof GridsetEvent gse) return toRemote(gse, source, originService);
        if (local instanceof BlobStoreEvent bse) return toRemote(bse, source, originService);
        throw new IllegalArgumentException("unknown GeoWebCacheEvent type: " + local);
    }

    default GeoWebCacheEvent toLocal(
            @NonNull RemoteGeoWebCacheEvent remote, @NonNull Object source) {
        if (remote instanceof RemoteTileLayerEvent remoteTle) return toLocal(remoteTle, source);
        if (remote instanceof RemoteGridsetEvent remoteGse) return toLocal(remoteGse, source);
        if (remote instanceof RemoteBlobStoreEvent remoteBse) return toLocal(remoteBse, source);
        throw new IllegalArgumentException("unknown RemoteGeoWebCacheEvent type: " + remote);
    }

    TileLayerEvent toLocal(RemoteTileLayerEvent remote, @Context Object source);

    RemoteTileLayerEvent toRemote(
            TileLayerEvent local, @Context Object source, @Context String originService);

    GridsetEvent toLocal(RemoteGridsetEvent remote, @Context Object source);

    RemoteGridsetEvent toRemote(
            GridsetEvent local, @Context Object source, @Context String originService);

    BlobStoreEvent toLocal(RemoteBlobStoreEvent remote, @Context Object source);

    RemoteBlobStoreEvent toRemote(
            BlobStoreEvent local, @Context Object source, @Context String originService);

    @ObjectFactory
    default RemoteTileLayerEvent newRemoteTileEvent(
            @Context Object source, @Context String originService) {
        return new RemoteTileLayerEvent(source, originService);
    }

    @ObjectFactory
    default RemoteGridsetEvent newRemoteGridsetEvent(
            @Context Object source, @Context String originService) {
        return new RemoteGridsetEvent(source, originService);
    }

    @ObjectFactory
    default RemoteBlobStoreEvent newRemoteBlobStoreEvent(
            @Context Object source, @Context String originService) {
        return new RemoteBlobStoreEvent(source, originService);
    }

    @ObjectFactory
    default TileLayerEvent newTileEvent(@Context Object source) {
        return new TileLayerEvent(source);
    }

    @ObjectFactory
    default GridsetEvent newGridsetEvent(@Context Object source) {
        return new GridsetEvent(source);
    }

    @ObjectFactory
    default BlobStoreEvent newBlobStoreEvent(@Context Object source) {
        return new BlobStoreEvent(source);
    }
}
