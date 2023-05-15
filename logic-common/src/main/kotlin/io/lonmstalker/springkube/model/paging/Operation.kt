package io.lonmstalker.springkube.model.paging

enum class Operation(val value: String) {
    EQUALS("equals"),
    NOT_EQUALS("nequals"),
    IN("in"),
    NOT_IN("not in"),
    BETWEEN("between"),
    CONTAINS("contains"),
    GTE("gte"),
    GT("gt"),
    LTE("lte"),
    LT("lt"),
    OR("or"),
    AND("and")
}