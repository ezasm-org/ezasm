package com.ezasm.backend;

import java.util.ArrayList;
import java.util.HashMap;

public sealed interface GDBOutput {
    record Done(Results results) implements GDBOutput {}
    record Connected() implements GDBOutput {}
    record Error(Value.Text msg, Value.Text code) implements GDBOutput {}
    record Exit() implements GDBOutput {}

    record Results(HashMap<String, Value> variables) implements GDBOutput {}

    sealed interface Value extends GDBOutput {
        record Text(String value) implements Value {}
        record Tuple(Results results) implements Value {}
        sealed interface List extends Value {
            record ValueList(java.util.List<Value> values) implements List {}
            record ResultList(Results results) implements List {}
        }
    }
}
