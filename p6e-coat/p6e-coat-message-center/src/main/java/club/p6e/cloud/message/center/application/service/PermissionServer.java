package club.p6e.cloud.message.center.application.service;

import club.p6e.cloud.message.center.infrastructure.context.PermissionContext;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface PermissionServer {
    PermissionContext.Url.ListDto urlList(PermissionContext.Url.Request request);

    PermissionContext.Url.Dto urlCreate(PermissionContext.Url.Request request);

    PermissionContext.Url.Dto urlUpdate(PermissionContext.Url.Request request);

    PermissionContext.Url.Dto urlGet(PermissionContext.Url.Request request);

    PermissionContext.Url.Dto urlDelete(PermissionContext.Url.Request request);


    PermissionContext.UrlGroup.ListDto urlGroupList(PermissionContext.UrlGroup.Request request);

    PermissionContext.UrlGroup.Dto urlGroupCreate(PermissionContext.UrlGroup.Request request);

    PermissionContext.UrlGroup.Dto urlGroupUpdate(PermissionContext.UrlGroup.Request request);

    PermissionContext.UrlGroup.Dto urlGroupGet(PermissionContext.UrlGroup.Request request);

    PermissionContext.UrlGroup.Dto urlGroupDelete(PermissionContext.UrlGroup.Request request);

    PermissionContext.Url.Dto urlDetails(PermissionContext.Url.Request request);

    PermissionContext.UrlGroup.Details.Dto urlGroupDetails(PermissionContext.UrlGroup.Details.Request request);
}
