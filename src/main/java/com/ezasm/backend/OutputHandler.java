package com.ezasm.backend;

import com.ezasm.backend.parser.GDBMIBaseVisitor;
import com.ezasm.backend.parser.GDBMIParser;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class OutputHandler extends GDBMIBaseVisitor<GDBOutput> {
    private GDBOutput.Results visitResults(List<GDBMIParser.ResultContext> ctx) {
        HashMap<String, GDBOutput.Value> variables = new HashMap<>();

        ctx.forEach(r -> variables.put(r.variable().getText(), visitValue(r.value())));

        return new GDBOutput.Results(variables);
    }

    @Override
    public GDBOutput.Value visitValue(GDBMIParser.ValueContext ctx) {
        if (ctx.c_string() != null) {
            return new GDBOutput.Value.Text(ctx.c_string().STRING().getText());
        } else if (ctx.list() != null) {
            if (ctx.list().value().size() > 0) {
                return new GDBOutput.Value.List.ValueList(ctx.list()
                        .value()
                        .stream()
                        .map(this::visitValue)
                        .collect(Collectors.toList()));
            } else {
                return new GDBOutput.Value.List.ResultList(visitResults(ctx.list().result()));
            }

        } else if (ctx.tuple() != null) {
            return new GDBOutput.Value.Tuple(visitResults(ctx.tuple().result()));
        } else {
            return (GDBOutput.Value) super.visitValue(ctx);
        }
    }
}
