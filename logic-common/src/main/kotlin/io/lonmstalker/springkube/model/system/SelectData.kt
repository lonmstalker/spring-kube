package io.lonmstalker.springkube.model.system

import org.jooq.Condition
import org.jooq.Table

data class SelectData(
    val conditions: MutableList<Condition>,
    val neededJoin: Collection<Pair<Table<*>, Condition>>
)
