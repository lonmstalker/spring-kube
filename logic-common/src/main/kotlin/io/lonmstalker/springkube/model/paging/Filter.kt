package io.lonmstalker.springkube.model.paging

/**
 *  Общий фильтр по объектам системы
 *
 *  field с использованием join: botId.title, где botId - поле, ссылающееся на бота, botId  в бд должен иметь ссылку на таблицу Bot
 *  field для получения вложенного значения в jsonb: data -> title
 *
 *  @author lonmstalker
 */
data class Filter(
    val field: String?,
    val value: List<Any>?,
    val operation: Operation,
)
