package club.p6e.cloud.console.application.service;

import club.p6e.cloud.console.infrastructure.context.DictionaryContext;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface DictionaryService {
    DictionaryContext.ListDto list(DictionaryContext.Request request);

    DictionaryContext.Dto create(DictionaryContext.Request request);

    DictionaryContext.Dto get(DictionaryContext.Request request);

    DictionaryContext.Dto delete(DictionaryContext.Request request);

    DictionaryContext.Dto update(DictionaryContext.Request request);

    DictionaryContext.Type.Dto type(DictionaryContext.Type.Request request);
}
