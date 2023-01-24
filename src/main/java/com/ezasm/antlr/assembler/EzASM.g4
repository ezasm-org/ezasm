grammar EzASM;

prog: ((expr | Comment)? NewLine)*;

expr: Identifier (argument)* Comment?
    | LabelDef Comment?;

argument: immediate | Register | dereference | Identifier;

dereference: (immediate)? '(' Register ')';

immediate: Hex | Decimal | Char | String;

Comment : '#' ~( '\r' | '\n' )*;

Identifier : [A-Za-z_\\.]+;

LabelDef : Identifier ':';

NewLine : [\r\n]+;

Register : '$' [0-9a-zA-Z]+;

Decimal : ([+-]? [0-9]+) | '0';

Hex : [+-]?[0-9A-Fa-f]'h';

Char : '\''.'\'';

String : '"' .*? '"';

WS : [ \t]+ -> skip;